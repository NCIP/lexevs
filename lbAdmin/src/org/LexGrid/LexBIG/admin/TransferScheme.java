/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.dataAccess.ExportUtility;
import org.LexGrid.annotations.LgAdminFunction;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.dao.database.connection.SQLInterface;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.ResourceManager;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.service.SystemResourceService;

/**
 * Assists in transferring a terminology from one server to another.
 * 
 * This tool was written specifically for the needs of NCI's production
 * environment, it may not work well for general purpose transfers.
 * 
 * Given two servers, STAGE and PRODUCTION - this tool is meant to help move a
 * terminology that was loaded on STAGE over to the PRODUCTION server. It makes
 * the assumption that you will NEVER load data on PRODUCTION that has not been
 * loaded on STAGE.
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.TransferScheme
 *   -u,--urn &lt;name&gt; URN or local name of the coding scheme to transfer.
 *   -v,--version &lt;id&gt; The assigned tag/label or absolute version
 *        identifier of the coding scheme.
 * 
 * Example: java -cp lgRuntime.jar
 *   org.LexGrid.LexBIG.admin.TransferScheme -u &quot;NCI Thesaurus&quot; -v &quot;6.06&quot;
 * </pre>
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Daniel Armbrust</A>
 */
@LgAdminFunction
public class TransferScheme {

    public static void main(String[] args) {
        try {
            new TransferScheme().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayTaggedMessage(e.getMessage());
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public TransferScheme() {
        super();
    }

    /**
     * Primary entry point for the program.
     * 
     * @throws Exception
     */
    public void run(String[] args) throws Exception {
        
        Registry sysReg = LexEvsServiceLocator.getInstance().getRegistry();
        SystemResourceService sysResServ = LexEvsServiceLocator.getInstance().getSystemResourceService();
        
        // Parse the command line ...
        CommandLine cl = null;
        Options options = getCommandOptions();
        try {
            cl = new BasicParser().parse(options, args);
        } catch (ParseException e) {
            Util.displayMessage("The TransferScheme Tool assists in transfering a terminology from one server");
            Util.displayMessage("to another.  This tool was written specifically for the needs of NCI's ");
            Util.displayMessage("production environment, it may not work well for general purpose tranfers.");
            Util.displayMessage("");
            Util.displayMessage("Given two servers, STAGE and PRODUCTION - this tool is meant to help");
            Util.displayMessage("move a terminology that was loaded on STAGE over to the PRODUCTION server.");
            Util.displayMessage("It makes the assumption that you will NEVER load data on PRODUCTION that");
            Util.displayMessage("has not been loaded on STAGE.");

            Util.displayCommandOptions("TransferScheme", options, "\n TransferScheme -u \"NCI_Thesaurus\" -v \"6.06\""
                    + Util.getURIHelp(), e);
            Util.displayMessage(Util.getPromptForSchemeHelp());
            return;
        }

        // Interpret provided values ...
        String cs = cl.getOptionValue("u");
        String vt = cl.getOptionValue("v");

        // Prompt if URN and version are not provided ...
        if (cs == null && vt == null) {
            CodingSchemeSummary css = Util.promptForCodeSystem();
            if (css != null) {
                cs = css.getCodingSchemeURI();
                vt = css.getRepresentsVersion();
            }
        }

        // figure out if they gave me a tag
        String internalVersion;
        try {
            internalVersion = sysResServ.getInternalVersionStringForTag(cs, vt);
//            internalVersion = ResourceManager.instance().getInternalVersionStringForTag(cs, vt);
        } catch (Exception e) {
            // if this throws an exception, its not a tag.
            internalVersion = vt;
        }
        String internalCSName = sysResServ.getInternalCodingSchemeNameForUserCodingSchemeName(cs,internalVersion);
//        String internalCSName = ResourceManager.instance().getInternalCodingSchemeNameForUserCodingSchemeName(cs,
//                internalVersion);

        System.out.println("The following instructions are for transfering the code system '" + internalCSName
                + "' version '" + internalVersion + "'.");
        boolean moveDB = isDatabaseOnDifferentServer();
        String newDBName = "";
        int step = 1;
        
        SystemVariables sv = sysResServ.getSystemVariables();
//        SystemVariables sv = ResourceManager.instance().getSystemVariables();
        File registry = new File(sv.getAutoLoadRegistryPath());

        if (moveDB) {
            newDBName = getNewDBName();
            System.out.println();
            System.out.println("Step " + step++ + ")");

            if (sv.getAutoLoadSingleDBMode()) {
                System.out.println("You are using Single Database mode.");
                System.out.println("You will need to copy all of the tables from the database '"
                        + sv.getAutoLoadDBURL() + "'");

                SQLInterface si = ResourceManager.instance().getSQLInterface(internalCSName, internalVersion);
                System.out.println("that have the prefix '" + si.getTablePrefix() + "' to the new database.  ");
                System.out.println("You will have to copy these with native tools provided by your SQL database.");
                System.out
                        .println("Following is a list of the tables in an order which will alleviate foreign key problems.");
                System.out.print(si.getTableName(SQLTableConstants.CODING_SCHEME));
                System.out.print(si.getTableName(SQLTableConstants.CODING_SCHEME_PROP));
                System.out.print(si.getTableName(SQLTableConstants.CODING_SCHEME_PROP_MULTI_ATTRIB));
                System.out.print(" " + si.getTableName(SQLTableConstants.CODING_SCHEME_MULTI_ATTRIBUTES));
                System.out.print(" " + si.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES));
                System.out.print(" " + si.getTableName(SQLTableConstants.ENTITY));
                System.out.print(" " + si.getTableName(SQLTableConstants.ENTITY_PROPERTY));
                System.out.print(" " + si.getTableName(SQLTableConstants.ENTITY_PROPERTY_MULTI_ATTRIBUTES));
                System.out.print(" " + si.getTableName(SQLTableConstants.ENTITY_PROPERTY_LINKS));
                System.out.print(" " + si.getTableName(SQLTableConstants.RELATION));
                // System.out.print(" " +
                // si.getTableName(SQLTableConstants.RELATION_MULTI_ATTRIBUTES));
                System.out.print(" " + si.getTableName(SQLTableConstants.ASSOCIATION));
                System.out.print(" " + si.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY));
                System.out.print(" " + si.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_E_QUALS));
                System.out.print(" " + si.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_DATA));
                System.out.print(" " + si.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_D_QUALS));
                System.out.print(" " + si.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE));
                System.out.println(" " + si.getTableName(SQLTableConstants.LEXGRID_TABLE_META_DATA));
            } else {
                RegistryEntry entry = sysReg.getCodingSchemeEntry(
                        DaoUtility.createAbsoluteCodingSchemeVersionReference(
                                internalCSName, 
                                internalVersion));
                String connUrl = entry.getDbUri();
//                String connUrl = ResourceManager.instance().getSQLInterface(internalCSName, internalVersion)
//                        .getConnectionDescriptor().getDbUrl();
                System.out.println("You are using Multiple Database mode.");
                System.out.println("You will need to copy the database '" + connUrl + "' to the new database server.");
                System.out.println("You should do this with native tools provided by your SQL database.");
                // more specific help for mysql
                if (connUrl.toLowerCase().indexOf("mysql") != -1) {
                    String dbName = connUrl.substring(connUrl.lastIndexOf('/') + 1);
                    System.out.println("Here is an example of how to do this for MySQL:");
                    System.out.println("mysqldump -u username -p --databases " + dbName + " > mysqlDump.sql");
                    System.out.println();
                    System.out.println("After the data is dumped, you can load it onto the new database like this:");
                    System.out.println("mysql -u username -p < mysqlDump.sql");
                }
            }

        }

        System.out.println();
        System.out.println("Step " + step++ + ")");
        System.out.println("You will need to copy the following folder to the appropriate location on the new server:");
        System.out.println(ResourceManager.instance().getIndexInterface(internalCSName, internalVersion)
                .getIndexLocation(internalCSName, internalVersion));

        System.out.println();
        System.out.println("Step " + step++ + ")");
        System.out.println("You will need to copy the following file to the appropriate location on the new server:");
//        System.out.println(ResourceManager.instance().getIndexInterface(internalCSName, internalVersion)
//                .getMetaLocation());
        System.out
                .println("It is recommended that you rename the current file on the new server, in case you need to undo this change.");

        System.out.println();
        System.out.println("Step " + step++ + ")");

        if (moveDB) {
            File outputFile = new File(registry.getParent(), "registryToTransfer.xml");
            ExportUtility.copyAndEditRegistry(outputFile, ResourceManager.instance().getSystemVariables()
                    .getAutoLoadDBURL(), newDBName);
            System.out
                    .println("You will need to move the following file to the appropriate location on the new server:");
            System.out.println(outputFile.getCanonicalPath());
            System.out.println("and rename it to '" + registry.getName() + "' on the new server.");
            System.out.println("This file has been modified for the new database name that you provided.");
            System.out.println("After you move it to the new server, it can be deleted from the current server.");
            System.out.println();
            System.out
                    .println("It is recommended that you rename the current file on the new server, in case you need to undo this change.");
        } else {
            System.out
                    .println("You will need to copy the following file to the appropriate location on the new server:");
            System.out.println(registry.getCanonicalPath());
            System.out
                    .println("It is recommended that you rename the current file on the new server, in case you need to undo this change.");
        }

        System.out.println();
        System.out.println("If the new server was running while you copied these files,");
        System.out.println("it should automatically notice the changes within 5 minutes.");
        System.out.println();
        System.out.println("It would be a good idea to check the log files in 5 minutes");
        System.out.println("to make sure no errors occured reading the new configuration.");

    }

    /**
     * Return supported command options.
     * 
     * @return org.apache.commons.cli.Options
     */
    private Options getCommandOptions() {
        Options options = new Options();
        Option o;

        o = new Option("u", "urn", true, "URN or local name of the coding scheme to transfer.");
        o.setArgName("name");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("v", "version", true,
                "The assigned tag/label or absolute version identifier of the coding scheme.");
        o.setArgName("id");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }

    private String getNewDBName() throws IOException {
        BufferedReader br = null;

        br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("You are currently using the following DB Server:");
            System.out.println(ResourceManager.instance().getSystemVariables().getAutoLoadDBURL());
            System.out.println("Please enter the connection string for the new server that you will be ");
            System.out.println("moving the data to, so that the registry file can be modified as appropriate.");
            System.out.println("What is the connection string for the new server?");
            String newServer = br.readLine();

            while (true) {
                System.out.println("You provided '" + newServer + "' as the new server.  Is this correct? (y/n): ");
                String ans = br.readLine();

                if (ans.trim().toLowerCase().equals("y")) {
                    return newServer;
                } else if (ans.trim().toLowerCase().equals("n")) {
                    break;
                } else {
                    System.out.println("Please answer 'y' or 'n'.");
                }
            }
        }

    }

    private boolean isDatabaseOnDifferentServer() throws IOException {
        System.out.print("Will you also be transfering the database to another server? (y/n): ");
        BufferedReader br = null;

        br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String ans = br.readLine();
            if (ans.trim().toLowerCase().equals("y")) {
                return true;
            } else if (ans.trim().toLowerCase().equals("n")) {
                return false;
            } else {
                System.out.println("Please answer 'y' or 'n'.");
            }
        }

    }
}
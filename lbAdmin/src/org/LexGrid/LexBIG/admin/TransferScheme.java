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
            Util.displayAndLogError("Resource is unavailable: " + e.getMessage(), e);
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

        Util.displayAndLogMessage("The following instructions are for transfering the code system '" + internalCSName
                + "' version '" + internalVersion + "'.");
        boolean moveDB = isDatabaseOnDifferentServer();
        String newDBName = "";
        int step = 1;
        
        SystemVariables sv = sysResServ.getSystemVariables();
//        SystemVariables sv = ResourceManager.instance().getSystemVariables();
        File registry = new File(sv.getAutoLoadRegistryPath());

        if (moveDB) {
            newDBName = getNewDBName();
            Util.displayAndLogMessage("");
            Util.displayAndLogMessage("Step " + step++ + ")");

            if (sv.getAutoLoadSingleDBMode()) {
                Util.displayAndLogMessage("You are using Single Database mode.");
                Util.displayAndLogMessage("You will need to copy all of the tables from the database '"
                        + sv.getAutoLoadDBURL() + "'");

                SQLInterface si = ResourceManager.instance().getSQLInterface(internalCSName, internalVersion);
                Util.displayAndLogMessage("that have the prefix '" + si.getTablePrefix() + "' to the new database.  ");
                Util.displayAndLogMessage("You will have to copy these with native tools provided by your SQL database.");
                Util.displayAndLogMessage
                        ("Following is a list of the tables in an order which will alleviate foreign key problems.");
                Util.displayAndLogMessage(si.getTableName(SQLTableConstants.CODING_SCHEME));
                Util.displayAndLogMessage(si.getTableName(SQLTableConstants.CODING_SCHEME_PROP));
                Util.displayAndLogMessage(si.getTableName(SQLTableConstants.CODING_SCHEME_PROP_MULTI_ATTRIB));
                Util.displayAndLogMessage(" " + si.getTableName(SQLTableConstants.CODING_SCHEME_MULTI_ATTRIBUTES));
                Util.displayAndLogMessage(" " + si.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES));
                Util.displayAndLogMessage(" " + si.getTableName(SQLTableConstants.ENTITY));
                Util.displayAndLogMessage(" " + si.getTableName(SQLTableConstants.ENTITY_PROPERTY));
                Util.displayAndLogMessage(" " + si.getTableName(SQLTableConstants.ENTITY_PROPERTY_MULTI_ATTRIBUTES));
                Util.displayAndLogMessage(" " + si.getTableName(SQLTableConstants.ENTITY_PROPERTY_LINKS));
                Util.displayAndLogMessage(" " + si.getTableName(SQLTableConstants.RELATION));
                // Util.displayAndLogMessage(" " +
                // si.getTableName(SQLTableConstants.RELATION_MULTI_ATTRIBUTES));
                Util.displayAndLogMessage(" " + si.getTableName(SQLTableConstants.ASSOCIATION));
                Util.displayAndLogMessage(" " + si.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY));
                Util.displayAndLogMessage(" " + si.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_E_QUALS));
                Util.displayAndLogMessage(" " + si.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_DATA));
                Util.displayAndLogMessage(" " + si.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_D_QUALS));
                Util.displayAndLogMessage(" " + si.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE));
                Util.displayAndLogMessage(" " + si.getTableName(SQLTableConstants.LEXGRID_TABLE_META_DATA));
            } else {
                RegistryEntry entry = sysReg.getCodingSchemeEntry(
                        DaoUtility.createAbsoluteCodingSchemeVersionReference(
                                internalCSName, 
                                internalVersion));
                String connUrl = entry.getDbUri();
//                String connUrl = ResourceManager.instance().getSQLInterface(internalCSName, internalVersion)
//                        .getConnectionDescriptor().getDbUrl();
                Util.displayAndLogMessage("You are using Multiple Database mode.");
                Util.displayAndLogMessage("You will need to copy the database '" + connUrl + "' to the new database server.");
                Util.displayAndLogMessage("You should do this with native tools provided by your SQL database.");
                // more specific help for mysql
                if (connUrl.toLowerCase().indexOf("mysql") != -1) {
                    String dbName = connUrl.substring(connUrl.lastIndexOf('/') + 1);
                    Util.displayAndLogMessage("Here is an example of how to do this for MySQL:");
                    Util.displayAndLogMessage("mysqldump -u username -p --databases " + dbName + " > mysqlDump.sql");
                    Util.displayAndLogMessage("");
                    Util.displayAndLogMessage("After the data is dumped, you can load it onto the new database like this:");
                    Util.displayAndLogMessage("mysql -u username -p < mysqlDump.sql");
                }
            }

        }

        Util.displayAndLogMessage("");
        Util.displayAndLogMessage("Step " + step++ + ")");
        Util.displayAndLogMessage("You will need to copy the following folder to the appropriate location on the new server:");
        Util.displayAndLogMessage(ResourceManager.instance().getIndexInterface(internalCSName, internalVersion)
                .getIndexLocation(internalCSName, internalVersion));

        Util.displayAndLogMessage("");
        Util.displayAndLogMessage("Step " + step++ + ")");
        Util.displayAndLogMessage("You will need to copy the following file to the appropriate location on the new server:");
//        Util.displayAndLogMessage(ResourceManager.instance().getIndexInterface(internalCSName, internalVersion)
//                .getMetaLocation());
        System.out
                .println("It is recommended that you rename the current file on the new server, in case you need to undo this change.");

        Util.displayAndLogMessage("");
        Util.displayAndLogMessage("Step " + step++ + ")");

        if (moveDB) {
            File outputFile = new File(registry.getParent(), "registryToTransfer.xml");
            ExportUtility.copyAndEditRegistry(outputFile, ResourceManager.instance().getSystemVariables()
                    .getAutoLoadDBURL(), newDBName);
            System.out
                    .println("You will need to move the following file to the appropriate location on the new server:");
            Util.displayAndLogMessage(outputFile.getCanonicalPath());
            Util.displayAndLogMessage("and rename it to '" + registry.getName() + "' on the new server.");
            Util.displayAndLogMessage("This file has been modified for the new database name that you provided.");
            Util.displayAndLogMessage("After you move it to the new server, it can be deleted from the current server.");
            Util.displayAndLogMessage("");
            System.out
                    .println("It is recommended that you rename the current file on the new server, in case you need to undo this change.");
        } else {
            System.out
                    .println("You will need to copy the following file to the appropriate location on the new server:");
            Util.displayAndLogMessage(registry.getCanonicalPath());
            System.out
                    .println("It is recommended that you rename the current file on the new server, in case you need to undo this change.");
        }

        Util.displayAndLogMessage("");
        Util.displayAndLogMessage("If the new server was running while you copied these files,");
        Util.displayAndLogMessage("it should automatically notice the changes within 5 minutes.");
        Util.displayAndLogMessage("");
        Util.displayAndLogMessage("It would be a good idea to check the log files in 5 minutes");
        Util.displayAndLogMessage("to make sure no errors occured reading the new configuration.");

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
            Util.displayAndLogMessage("You are currently using the following DB Server:");
            Util.displayAndLogMessage(ResourceManager.instance().getSystemVariables().getAutoLoadDBURL());
            Util.displayAndLogMessage("Please enter the connection string for the new server that you will be ");
            Util.displayAndLogMessage("moving the data to, so that the registry file can be modified as appropriate.");
            Util.displayAndLogMessage("What is the connection string for the new server?");
            String newServer = br.readLine();

            while (true) {
                Util.displayAndLogMessage("You provided '" + newServer + "' as the new server.  Is this correct? (y/n): ");
                String ans = br.readLine();

                if (ans.trim().toLowerCase().equals("y")) {
                    return newServer;
                } else if (ans.trim().toLowerCase().equals("n")) {
                    break;
                } else {
                    Util.displayAndLogMessage("Please answer 'y' or 'n'.");
                }
            }
        }

    }

    private boolean isDatabaseOnDifferentServer() throws IOException {
        Util.displayAndLogMessage("Will you also be transfering the database to another server? (y/n): ");
        BufferedReader br = null;

        br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String ans = br.readLine();
            if (ans.trim().toLowerCase().equals("y")) {
                return true;
            } else if (ans.trim().toLowerCase().equals("n")) {
                return false;
            } else {
                Util.displayAndLogMessage("Please answer 'y' or 'n'.");
            }
        }

    }
}
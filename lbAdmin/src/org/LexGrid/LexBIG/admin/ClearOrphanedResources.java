/*
 * Copyright: (c) 2004-2007 Mayo Foundation for Medical Education and 
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

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.dataAccess.CleanUpUtility;
import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Clears orphaned indexes and databases (resources that were created by LexBIG,
 * but now for some reason are no longer properly referenced)
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.ClearOrphanedResources
 *  -li,--listIndexes List all unused indexes.
 *  -ldb,--listDatabases List all unused databases (with matching prefix).
 *  -ri,--removeIndex &lt;name&gt; Remove the (unused) index with the given name.
 *  -rdb,--removeDatabase &lt;name&gt; Remove the (unused) database with the given name.
 *  -a,--all Remove all unreferenced indexes and databases (with matching prefix).
 * </pre>
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Daniel Armbrust</A>
 */
@LgAdminFunction
public class ClearOrphanedResources {

    public static void main(String[] args) {
        try {
            new ClearOrphanedResources().run(args);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public ClearOrphanedResources() {
        super();
    }

    /**
     * Primary entry point for the program.
     * 
     * @throws Exception
     */
    public void run(String[] args) throws Exception {

        // Parse the command line ...
        CommandLine cl = null;
        Options options = getCommandOptions();
        try {
            cl = new BasicParser().parse(options, args);
        } catch (ParseException e) {
            Util.displayCommandOptions("ClearOrphanedResources", options, "ClearOrphanedResources -li", e);
            return;
        }

        // Interpret provided values ...
        String indexToClear = cl.hasOption("ri") ? cl.getOptionValue("ri").trim() : null;
        String dbToClear = cl.hasOption("rdb") ? cl.getOptionValue("rdb").trim() : null;
        boolean listIndex = cl.hasOption("li");
        boolean listDB = cl.hasOption("ldb");
        boolean cleanAll = cl.hasOption("a");

        // Make them specify something ...
        if (indexToClear == null && dbToClear == null && !(listIndex || listDB || cleanAll)) {
            Util.displayCommandOptions("ClearOrphanedResources", options, "ClearOrphanedResources -li", null);
            return;
        }

        // Process specified options (more than 1 allowed) ...
        if (listIndex) {
            String[] temp = CleanUpUtility.listUnusedIndexes();
            if (temp.length == 0)
                Util.displayMessage("No unused indexes found.");
            else {
                Util.displayMessage("Unused Indexes:");
                for (int i = 0; i < temp.length; i++) {
                    Util.displayMessage(temp[i]);
                }
            }
        }

        if (listDB) {
            String[] temp = CleanUpUtility.listUnusedDatabases();
            if (temp.length == 0)
                Util.displayMessage("No unused databases found.");
            else {
                Util.displayMessage("Unused Databases:");
                for (int i = 0; i < temp.length; i++) {
                    Util.displayMessage(temp[i]);
                }
            }
        }

        if (indexToClear != null && indexToClear.length() > 0)
            try {
                CleanUpUtility.removeUnusedIndex(indexToClear);
                Util.displayTaggedMessage("Index cleared: " + indexToClear);
            } catch (LBParameterException e) {
                Util.displayTaggedMessage("Failure clearing index '" + indexToClear + "':\n" + e.getMessage());
            }

        if (dbToClear != null && dbToClear.length() > 0)
            try {
                CleanUpUtility.removeUnusedDatabase(dbToClear);
                Util.displayTaggedMessage("Database cleared: " + indexToClear);
            } catch (LBParameterException e) {
                Util.displayTaggedMessage("Failure clearing database '" + dbToClear + "':\n" + e.getMessage());
            }

        if (cleanAll)
            try {
                String[] removed = CleanUpUtility.removeAllUnusedIndexes();
                if (removed.length == 0)
                    Util.displayMessage("No unused indexes found.");
                else {
                    for (int i = 0; i < removed.length; i++)
                        Util.displayTaggedMessage("Index cleared: " + removed[i]);
                }

                removed = CleanUpUtility.removeAllUnusedDatabases();
                if (removed.length == 0)
                    Util.displayMessage("No unused databases found.");
                else {
                    for (int i = 0; i < removed.length; i++)
                        Util.displayTaggedMessage("Database cleared: " + removed[i]);
                }
            } catch (LBParameterException e) {
                Util.displayTaggedMessage("Failure clearing unused resources:\n" + e.getMessage());
            }
    }

    /**
     * Return supported command options.
     * 
     * @return org.apache.commons.cli.Options
     */
    private Options getCommandOptions() {
        Options options = new Options();
        Option o;

        o = new Option("ri", "removeIndex", true, "Remove the (unused) index with the given name.");
        o.setArgName("name");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("rdb", "removeDatabase", true, "Remove the (unused) database with the given name.");
        o.setArgName("name");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("ldb", "listDatabases", false, "List all unused databases (with matching prefix).");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("li", "listIndexes", false, "List all unused indexes.");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("a", "all", false, "Remove all unreferenced indexes and databases (with matching prefix).");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }

}
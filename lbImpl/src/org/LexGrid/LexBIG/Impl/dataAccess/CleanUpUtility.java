/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.Impl.dataAccess;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.lexevs.logging.LgLoggerIF;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.registry.service.Registry.DBEntry;
import org.lexevs.registry.service.Registry.HistoryEntry;
import org.lexevs.system.ResourceManager;
import org.lexevs.system.constants.SystemVariables;

import edu.mayo.informatics.indexer.api.IndexerService;

/**
 * This class implements methods useful for cleaning up orphaned databases and
 * indexes.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CleanUpUtility {
    protected static LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    public static String[] listUnusedDatabases() throws LBInvocationException {
        SystemVariables vars = ResourceManager.instance().getSystemVariables();
        if (vars.getAutoLoadSingleDBMode()) {
            return listUnusedTables();
        } else {
            String errorMessage = "This utility has been disabled for Multi-database mode.";
            String logId = getLogger().error(errorMessage);
            throw new LBInvocationException(errorMessage, logId);   
        }
    }

    private static String[] listUnusedTables() throws LBInvocationException {
        try {
            SystemVariables vars = ResourceManager.instance().getSystemVariables();
            DBEntry[] regEntries = ResourceManager.instance().getRegistry().getDBEntries();
            HashSet<String> registeredDBPrefixes = new HashSet<String>();
            HashSet<String> registeredHistoryPrefixes = new HashSet<String>();
            for (int i = 0; i < regEntries.length; i++) {
                registeredDBPrefixes.add(regEntries[i].prefix);
            }

            HistoryEntry[] hisEntriesPrefixes = ResourceManager.instance().getRegistry().getHistoryEntries();
            for (int i = 0; i < hisEntriesPrefixes.length; i++) {
                registeredHistoryPrefixes.add(hisEntriesPrefixes[i].prefix);
            }

            // figure out which sets of lexgrid tables aren't in use.
            ArrayList<String> unusedPrefixes = new ArrayList<String>();
            String userPrefix = vars.getAutoLoadDBPrefix();
            String max = ResourceManager.instance().getRegistry().getNextDBIdentifier();

            // a0 is the prefix we start with in LexBig
            String prefix = "a0";

            int count = 0;
            boolean incrementCounter = false;
            // go through all the registered prefixes, plus 50.
            while (count < 50) {
                if (!registeredDBPrefixes.contains(userPrefix + prefix + "_")) {
                    if (doesTableExist(vars.getAutoLoadDBURL() + vars.getAutoLoadDBParameters(), vars
                            .getAutoLoadDBDriver(), vars.getAutoLoadDBUsername(), vars.getAutoLoadDBPassword(),
                            userPrefix + prefix + "_", false)) {
                        unusedPrefixes.add(userPrefix + prefix + "_");
                    }
                }
                if (prefix.equals(max)) {
                    incrementCounter = true;
                }
                prefix = DBUtility.computeNextIdentifier(prefix);
                if (incrementCounter) {
                    count++;
                }
            }

            // figure out which sets of history tables aren't in use
            max = ResourceManager.instance().getRegistry().getNextHistoryIdentifier();

            prefix = "a0";

            count = 0;
            incrementCounter = false;
            // go through all the registered prefixes, plus 50.
            while (count < 50) {
                if (!registeredHistoryPrefixes.contains(userPrefix + "h" + prefix + "_")) {
                    if (doesTableExist(vars.getAutoLoadDBURL() + vars.getAutoLoadDBParameters(), vars
                            .getAutoLoadDBDriver(), vars.getAutoLoadDBUsername(), vars.getAutoLoadDBPassword(),
                            userPrefix + "h" + prefix + "_", true)) {
                        unusedPrefixes.add("(history)" + userPrefix + "h" + prefix + "_");
                    }
                }
                if (prefix.equals(max)) {
                    incrementCounter = true;
                }
                prefix = DBUtility.computeNextIdentifier(prefix);
                if (incrementCounter) {
                    count++;
                }
            }
            return unusedPrefixes.toArray(new String[unusedPrefixes.size()]);

        } catch (LBInvocationException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Problem reading unused dbs", e);
            throw new LBInvocationException("There was a problem trying to read the unused dbs", logId);
        }

    }

    public static String[] listUnusedIndexes() throws LBParameterException, LBInvocationException {
        try {
            DBEntry[] regEntries = ResourceManager.instance().getRegistry().getDBEntries();
            boolean singleIndexMode = ResourceManager.instance().getSystemVariables().getAutoLoadSingleDBMode();
            HashSet<String> registeredDBs = new HashSet<String>();
            for (int i = 0; i < regEntries.length; i++) {

                registeredDBs.add(singleIndexMode ? regEntries[i].prefix : regEntries[i].dbName);
            }

            String indexLocation = ResourceManager.instance().getSystemVariables().getAutoLoadIndexLocation();
            File indexParentFolder = new File(indexLocation);

            File[] indexes = indexParentFolder.listFiles();
            if (indexes == null) {
                throw new LBParameterException("The file '" + indexParentFolder.getAbsolutePath()
                        + "' does not exist, or is not a folder");
            }

            ArrayList<String> unusedIndexes = new ArrayList<String>();
            for (int i = 0; i < indexes.length; i++) {
                if (indexes[i].isDirectory() && !registeredDBs.contains(indexes[i].getName())
                        && !indexes[i].getName().equals("MetaDataIndex")) {
                    unusedIndexes.add(indexes[i].getName());
                }
            }
            return unusedIndexes.toArray(new String[unusedIndexes.size()]);
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Problem removing an index", e);
            throw new LBInvocationException("There was a problem trying to read the indexes", logId);
        }
    }

    public static void removeAllUnusedResources() throws LBParameterException, LBInvocationException {
        String[] temp = listUnusedDatabases();
        for (int i = 0; i < temp.length; i++) {
            removeUnusedDatabase(temp[i]);
        }

        temp = listUnusedIndexes();
        for (int i = 0; i < temp.length; i++) {
            removeUnusedIndex(temp[i]);
        }
    }

    public static String[] removeAllUnusedIndexes() throws LBParameterException, LBInvocationException {
        String[] temp = listUnusedIndexes();
        for (int i = 0; i < temp.length; i++) {
            removeUnusedIndex(temp[i]);
        }
        return temp;
    }

    public static String[] removeAllUnusedDatabases() throws LBParameterException, LBInvocationException {
        String[] temp = listUnusedDatabases();
        for (int i = 0; i < temp.length; i++) {
            removeUnusedDatabase(temp[i]);
        }
        return temp;
    }

    public static void removeUnusedIndex(String index) throws LBParameterException, LBInvocationException {
        try {
            getLogger().debug("Removing index '" + index + "'.");
            DBEntry[] regEntries = ResourceManager.instance().getRegistry().getDBEntries();
            if (ResourceManager.instance().getSystemVariables().getAutoLoadSingleDBMode()) {
                for (int i = 0; i < regEntries.length; i++) {
                    if (regEntries[i].prefix.equals(index)) {
                        throw new LBParameterException(
                                "That index is registered with the service.  Please use the appropriate service delete method.");
                    }
                }
            } else {
                for (int i = 0; i < regEntries.length; i++) {
                    if (regEntries[i].dbName.equals(index)) {
                        throw new LBParameterException(
                                "That index is registered with the service.  Please use the appropriate service delete method.");
                    }
                }
            }

            String indexLocation = ResourceManager.instance().getSystemVariables().getAutoLoadIndexLocation();
            File indexParentFolder = new File(indexLocation);
            File theIndexFolder = new File(indexParentFolder, index);
            if (!theIndexFolder.exists() || !theIndexFolder.isDirectory()) {
                throw new LBParameterException("The index '" + theIndexFolder.getAbsolutePath()
                        + "' does not exist, or is not a folder");
            }

            IndexerService is = new IndexerService(indexParentFolder.getAbsolutePath(), false);
            is.deleteIndex(index);
            is.getMetaData().removeAllIndexMetaDataValue(index);
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Problem removing an index", e);
            throw new LBInvocationException("There was a problem trying to remove the index", logId);
        }
    }

    public static void removeUnusedDatabase(String dbName) throws LBParameterException, LBInvocationException {

        try {
            getLogger().debug("Removing database '" + dbName + "'.");
            SystemVariables vars = ResourceManager.instance().getSystemVariables();
            if (vars.getAutoLoadSingleDBMode()) {
                removeUnusedTables(dbName);
                return;
            }

            String server = vars.getAutoLoadDBURL() + vars.getAutoLoadDBParameters();
            String driver = vars.getAutoLoadDBDriver();

            if (!DBUtility.doesDBExist(server, driver, dbName, vars.getAutoLoadDBParameters(), vars
                    .getAutoLoadDBUsername(), vars.getAutoLoadDBPassword())) {
                throw new LBParameterException("The database '" + dbName + "' does not seem to exist");
            }

            // Ok, the db exists - make sure its not registered.
            DBEntry[] regEntries = ResourceManager.instance().getRegistry().getDBEntries();
            for (int i = 0; i < regEntries.length; i++) {
                if (regEntries[i].dbName.equals(dbName)) {
                    throw new LBParameterException(
                            "That database is registered with the service.  Please use the appropriate service delete method.");
                }
            }

            // finally, delete the DB.
            DBUtility.dropDatabase(server, vars.getAutoLoadDBDriver(), dbName, vars.getAutoLoadDBUsername(), vars
                    .getAutoLoadDBPassword());
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Problem removing a database", e);
            throw new LBInvocationException("There was a problem trying to remove the database", logId);
        }

    }

    private static void removeUnusedTables(String prefix) throws Exception {
        SystemVariables vars = ResourceManager.instance().getSystemVariables();
        String server = vars.getAutoLoadDBURL() + vars.getAutoLoadDBParameters();
        String driver = vars.getAutoLoadDBDriver();
        String username = vars.getAutoLoadDBUsername();
        String password = vars.getAutoLoadDBPassword();

        boolean history = false;
        if (prefix.startsWith("(history)")) {
            prefix = prefix.substring("(history)".length());
            history = true;
        }

        if (!doesTableExist(server, driver, username, password, prefix, history)) {
            throw new LBParameterException("The tables do not seem to exist");
        }

        // Ok, the table exists - make sure its not registered.
        if (history) {
            HistoryEntry[] hisEntries = ResourceManager.instance().getRegistry().getHistoryEntries();
            for (int i = 0; i < hisEntries.length; i++) {
                if (hisEntries[i].prefix.equals(prefix)) {
                    throw new LBParameterException(
                            "That table prefix is registered with the service.  Please use the appropriate service delete method.");
                }
            }
        } else {
            DBEntry[] regEntries = ResourceManager.instance().getRegistry().getDBEntries();
            for (int i = 0; i < regEntries.length; i++) {
                if (regEntries[i].prefix.equals(prefix)) {
                    throw new LBParameterException(
                            "That table prefix is registered with the service.  Please use the appropriate service delete method.");
                }
            }
        }

        // finally, delete the tables.
        Connection connection = DBUtility.connectToDatabase(server, driver, username, password);
        SQLTableUtilities stu = new SQLTableUtilities(connection, prefix);
        stu.dropTables();
        connection.close();

    }

    private static boolean doesTableExist(String server, String driver, String username, String password,
            String prefix, boolean history) {
        if (history) {
            return SQLTableUtilities.doHistoryTablesExist(server, driver, username, password, prefix);
        } else {
            return SQLTableUtilities.doTablesExist(server, driver, username, password, prefix);
        }
    }
}
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
package org.LexGrid.LexBIG.Impl.dataAccess;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.prefix.CyclingCharDbPrefixGenerator;
import org.lexevs.dao.indexer.api.IndexerService;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.lexevs.dao.indexer.utility.Utility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.registry.service.XmlRegistry.DBEntry;
import org.lexevs.registry.service.XmlRegistry.HistoryEntry;
import org.lexevs.system.ResourceManager;
import org.lexevs.system.constants.SystemVariables;


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
 //       SystemVariables vars = LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables();
//        if (vars.getIsSingleIndex()) {
            return listUnusedTables();
//        } else {
//            String errorMessage = "This utility has been disabled for Multi-database mode.";
//            String logId = getLogger().error(errorMessage);
//            throw new LBInvocationException(errorMessage, logId);   
//        }
    }

    private static String[] listUnusedTables() throws LBInvocationException {
        try {
            SystemVariables vars = LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables();
            List<RegistryEntry> regEntries = LexEvsServiceLocator.getInstance().getRegistry().getAllRegistryEntries();
            HashSet<String> registeredDBPrefixes = new HashSet<String>();
            HashSet<String> registeredHistoryPrefixes = new HashSet<String>();
            for (RegistryEntry reg : regEntries) {
                registeredDBPrefixes.add(reg.getPrefix());
            }

            List<RegistryEntry> hisEntriesPrefixes = LexEvsServiceLocator.getInstance().getRegistry().getAllRegistryEntriesOfType(ResourceType.NCI_HISTORY);
            for (RegistryEntry reg : hisEntriesPrefixes) {
                registeredHistoryPrefixes.add(reg.getPrefix());
            }

            // figure out which sets of lexgrid tables aren't in use.
            ArrayList<String> unusedPrefixes = new ArrayList<String>();
            String userPrefix = vars.getAutoLoadDBPrefix();
            String max = LexEvsServiceLocator.getInstance().getRegistry().getNextDBIdentifier();

            // a0 is the prefix we start with in LexBig
            String prefix = "aaaa";

            int count = 0;
            boolean incrementCounter = false;
            CyclingCharDbPrefixGenerator gen = new CyclingCharDbPrefixGenerator();
            // go through all the registered prefixes, plus 50.
            while (count < 50) {
                if (!registeredDBPrefixes.contains(prefix)) {
                    if (doesTableExist(vars.getAutoLoadDBURL() + vars.getAutoLoadDBParameters(), vars
                            .getAutoLoadDBDriver(), vars.getAutoLoadDBUsername(), vars.getAutoLoadDBPassword(),
                            userPrefix + prefix + SQLTableConstants.TBL_CODING_SCHEME, false)) {
                        unusedPrefixes.add(userPrefix + prefix);
                    }
                }
                if (prefix.equals(max)) {
                    incrementCounter = true;
                }
                String movePrefixtoUC = prefix.toUpperCase();
                prefix = String.valueOf(gen.incrementByOne(movePrefixtoUC.toCharArray())).toLowerCase();
                if (incrementCounter) {
                    count++;
                }
            }

            // figure out which sets of history tables aren't in use
//            max = LexEvsServiceLocator.getInstance().getRegistry().getNextHistoryIdentifier();
//
//            prefix = "aaaa";
//
//            count = 0;
//            incrementCounter = false;
//            // go through all the registered prefixes, plus 50.
//            while (count < 50) {
//                if (!registeredHistoryPrefixes.contains(userPrefix + "h" + prefix + "_")) {
//                    if (doesTableExist(vars.getAutoLoadDBURL() + vars.getAutoLoadDBParameters(), vars
//                            .getAutoLoadDBDriver(), vars.getAutoLoadDBUsername(), vars.getAutoLoadDBPassword(),
//                            userPrefix + "h" + prefix + "_", true)) {
//                        unusedPrefixes.add("(history)" + userPrefix + "h" + prefix + "_");
//                    }
//                }
//                if (prefix.equals(max)) {
//                    incrementCounter = true;
//                }
//                prefix = String.valueOf(gen.incrementByOne(prefix.toCharArray()));
//                if (incrementCounter) {
//                   count ++;
//                }
//            }
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
            List<RegistryEntry> regEntries = LexEvsServiceLocator.getInstance().getRegistry().getAllRegistryEntries();

            HashSet<String> registeredDBs = new HashSet<String>();
            for (RegistryEntry reg : regEntries) {
                AbsoluteCodingSchemeVersionReference reference = new AbsoluteCodingSchemeVersionReference();
                reference.setCodingSchemeURN(reg.getResourceUri());
                reference.setCodingSchemeVersion(reg.getResourceVersion());
                registeredDBs.add(Utility.getIndexName(reference));
            }

            String indexLocation = LexEvsServiceLocator.getInstance().getSystemResourceService().
                    getSystemVariables().getAutoLoadIndexLocation();
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
            List<RegistryEntry> regEntries =  LexEvsServiceLocator.getInstance().getRegistry().getAllRegistryEntries();
//            if ( LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables().getIsSingleIndex()) {
//                for (RegistryEntry reg: regEntries) {
//                    if (reg.getPrefix().equals(index)) {
//                        throw new LBParameterException(
//                                "That index is registered with the service.  Please use the appropriate service delete method.");
//                    }
//                }
//            } else {
                for (RegistryEntry reg: regEntries) {
                    AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
                    ref.setCodingSchemeURN(reg.getResourceUri());
                    ref.setCodingSchemeVersion(reg.getResourceVersion());
                    String indexName = Utility.getIndexName(ref);
                    if (indexName.equals(index)) {
                        throw new LBParameterException(
                                "That index is registered with the service.  Please use the appropriate service delete method.");
                    }
                }
//            }

            String indexLocation = LexEvsServiceLocator.getInstance().getSystemResourceService().
                    getSystemVariables().getAutoLoadIndexLocation();
            File indexParentFolder = new File(indexLocation);
            File theIndexFolder = new File(indexParentFolder, index);
            if (!theIndexFolder.exists() || !theIndexFolder.isDirectory()) {
                throw new LBParameterException("The index '" + theIndexFolder.getAbsolutePath()
                        + "' does not exist, or is not a folder");
            }

            IndexerService is = new IndexerService(indexParentFolder.getAbsolutePath(), false);
            is.deleteIndex(index);
            //TODO Check correctness of the index value
            is.getMetaData().removeIndexMetaDataValue(index);
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
            SystemVariables vars = LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables();
            //TODO create conditional for use.
                removeUnusedTables(dbName);

//            String server = vars.getAutoLoadDBURL() + vars.getAutoLoadDBParameters();
//            String driver = vars.getAutoLoadDBDriver();
//
//            if (!DBUtility.doesDBExist(server, driver, dbName, vars.getAutoLoadDBParameters(), vars
//                    .getAutoLoadDBUsername(), vars.getAutoLoadDBPassword())) {
//                throw new LBParameterException("The database '" + dbName + "' does not seem to exist");
//            }
//
//            // Ok, the db exists - make sure its not registered.
//            List<RegistryEntry> regEntries = LexEvsServiceLocator.getInstance().getRegistry().getAllRegistryEntries();
//            for (RegistryEntry reg: regEntries) {
//                if (reg.getDbName().equals(dbName)) {
//                    throw new LBParameterException(
//                            "That database is registered with the service.  Please use the appropriate service delete method.");
//                }
//            }
//
//            // finally, delete the DB.
//            DBUtility.dropDatabase(server, vars.getAutoLoadDBDriver(), dbName, vars.getAutoLoadDBUsername(), vars
//                    .getAutoLoadDBPassword());
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Problem removing a database", e);
            throw new LBInvocationException("There was a problem trying to remove the database", logId);
        }

    }

    private static void removeUnusedTables(String prefix) throws Exception {
        SystemVariables vars = LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables();
        String server = vars.getAutoLoadDBURL() + vars.getAutoLoadDBParameters();
        String driver = vars.getAutoLoadDBDriver();
        String username = vars.getAutoLoadDBUsername();
        String password = vars.getAutoLoadDBPassword();

        boolean history = false;
        if (prefix.startsWith("(history)")) {
            prefix = prefix.substring("(history)".length());
            history = true;
        }

        if (!doesTableExist(server, driver, username, password, prefix + SQLTableConstants.TBL_CODING_SCHEME, history)) {
            throw new LBParameterException("The tables do not seem to exist");
        }

        // Ok, the table exists - make sure its not registered.
        if (history) {
            List<RegistryEntry> hisEntries = LexEvsServiceLocator.getInstance().getRegistry().
                    getAllRegistryEntriesOfType(ResourceType.NCI_HISTORY);
            for (RegistryEntry reg: hisEntries) {
                if (reg.getPrefix().equals(prefix)) {
                    throw new LBParameterException(
                            "That table prefix is registered with the service.  Please use the appropriate service delete method.");
                }
            }
        } else {
            List<RegistryEntry> regEntries = LexEvsServiceLocator.getInstance().getRegistry().getAllRegistryEntries();
            for (RegistryEntry reg: regEntries) {
                if (reg.getPrefix().equals(prefix)) {
                    throw new LBParameterException(
                            "That table prefix is registered with the service.  Please use the appropriate service delete method.");
                }
            }
        }

        // finally, delete the tables.

        LexEvsServiceLocator.getInstance().getLexEvsDatabaseOperations().dropCodingSchemeTablesByPrefix(prefix);

    }

    private static boolean doesTableExist(String server, String driver, String username, String password,
            String prefix, boolean history) {
        if (history) {
           return SQLTableUtilities.doHistoryTablesExist(server, driver, username, password, prefix);
        } else {
            return LexEvsServiceLocator.getInstance().getLexEvsDatabaseOperations().getDatabaseUtility().doesTableExist(prefix);
          //  return SQLTableUtilities.doTablesExist(server, driver, username, password, prefix);
        }
    }
}
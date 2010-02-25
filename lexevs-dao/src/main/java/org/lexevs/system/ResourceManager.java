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
package org.lexevs.system;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.BooleanQuery;
import org.lexevs.dao.database.connection.SQLConnectionInfo;
import org.lexevs.dao.database.connection.SQLHistoryInterface;
import org.lexevs.dao.database.connection.SQLInterface;
import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.dao.index.connection.IndexInterface;
import org.lexevs.exceptions.MissingResourceException;
import org.lexevs.exceptions.UnexpectedInternalError;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.Logger;
import org.lexevs.registry.WriteLockManager;
import org.lexevs.registry.service.XmlRegistry;
import org.lexevs.registry.service.Registry.KnownTags;
import org.lexevs.registry.service.XmlRegistry.DBEntry;
import org.lexevs.registry.service.XmlRegistry.HistoryEntry;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.model.LocalCodingScheme;
import org.lexevs.system.service.SystemResourceService;
import org.lexevs.system.utility.MyClassLoader;

/**
 * This class keeps track of all of the SQL servers and index locations
 * available to the system, and provides access to them.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 * @version subversion $Revision: $ checked in on $Date: $
 */
@Deprecated
public class ResourceManager implements SystemResourceService {
    private static ResourceManager resourceManager_;
    private SystemVariables systemVars_;
    private XmlRegistry registry_;
    private DataSource dataSource;
    private DatabaseType databaseType;

    private Logger logger_;

    public static final String codingSchemeVersionSeparator_ = "[:]";

    // This maps internal coding scheme names / version to the serverId that
    // contains them.
    private Hashtable<String, String> codingSchemeToServerMap_;
    // this maps internal coding scheme names / version to the indexId that
    // contains them.
    private Hashtable<String, String> codingSchemeToIndexMap_;

    // this maps history URNs to the SQLInterface object that acceesses them.
    private Hashtable<String, SQLHistoryInterface> historySqlServerInterfaces_;

    // this maps serverId's to the SQLInterface object that acceesses them.
    private Hashtable<String, SQLInterface> sqlServerInterfaces_;

    // This maps low level jdbc connection information to the connection pool
    // that accesses
    // this particular databases (multiple SQLInterfaces can use the same base
    // interface)
    //private Hashtable<String, SQLInterfaceBase> sqlServerBaseInterfaces_;
    //private SQLInterfaceBase sib;

    // this maps indexId's to the IndexInterface that accesses them.
    private Hashtable<String, IndexInterface> indexInterfaces_;

    // This maps all available coding scheme "localNames" to the internal coding
    // scheme name
    // local names key into another hashtable, from there, the key is the
    // version. the value in the
    // second hashtable is the internal coding scheme name.
    private Hashtable<String, Hashtable<String, String>> codingSchemeLocalNamesToInternalNameMap_;

    // This maps internal coding scheme names to their UID.
    private Hashtable<String, String> internalCodingSchemeNameUIDMap_;

    // this maps SupportedCodingScheme URN's (for relations) to the
    // codingschemeNames that are
    // used in the relations (on a per codingScheme basis)
    private Hashtable<String, String> supportedCodingSchemeToInternalMap_;


    // A cache to use for values that are frequently used. Automatically throws
    // away oldest unused items.
    private Map cache_;

    // This thread handles future deactivations
    private Thread deactivatorThread_;
    private FutureDeactivatorThread fdt_;

    // Properties object that I was launched with (need to keep incase reinit is
    // called)
    private static Properties props_;

    public static ResourceManager instance() {
        return LexEvsServiceLocator.getInstance().getResourceManager();
    }

    /*
     * This method is called if a startup completely fails, so that you can get
     * more debugging info.
     */
    public static void dumpLogQueue() {
        System.err.println("SystemResourceService - calling dump log queue.");
        System.out.println("SystemResourceService - calling dump log queue.");
    }


    /**
     * This method is used by the lock manager thread, if another thread changed
     * something. Means that I need to reinitialize myself. I only remove
     * references to the current resource manager, so currently running tasks
     * can complete. If properties object is provided, it uses it as the new
     * configuration.
     * 
     * This method is also used by JUnit tests, and some GUI tools to
     * reconfigure the LexBIG instance with a new configuration.
     */
    public static void reInit(Properties props) {

        try {
            if (props == null) {
                props = props_;
            }


            // stop the deactivator thread in the old resource manager
            ResourceManager oldRM = resourceManager_;
            oldRM.fdt_.continueRunning = false;
            oldRM.deactivatorThread_.interrupt();



            oldRM.fdt_ = null;
            oldRM.deactivatorThread_ = null;
            //force the finalize (to make sure the db connections get closed)
            oldRM.finalize();
            oldRM = null;
        } catch (Throwable e) {
            // I can't throw the InitializationException here, because it
            // depends on this class
            // being built properly.

        }

    }


    private void init() throws Exception {
        cache_ = Collections.synchronizedMap(new LRUMap(systemVars_.getCacheSize()));  
        
        // This increases the ability of Lucene to do queries against
        // large indexes like the MetaThesaurus without getting errors.
        BooleanQuery.setMaxClauseCount(
                systemVars_.getLuceneMaxClauseCount());

        codingSchemeToServerMap_ = new Hashtable<String, String>();
        sqlServerInterfaces_ = new Hashtable<String, SQLInterface>();
        //sqlServerBaseInterfaces_ = new Hashtable<String, SQLInterfaceBase>();
        historySqlServerInterfaces_ = new Hashtable<String, SQLHistoryInterface>();
        codingSchemeLocalNamesToInternalNameMap_ = new Hashtable<String, Hashtable<String, String>>();
        internalCodingSchemeNameUIDMap_ = new Hashtable<String, String>();
        supportedCodingSchemeToInternalMap_ = new Hashtable<String, String>();

        // populate the registry
        //registry_ = new XmlRegistry(systemVars_.getAutoLoadRegistryPath());

        // connect to the histories
        readHistories();

        // go through all of the sql servers and read all of the available code
        // systems.
        // initialize the SQL connections to each server.

        org.lexevs.registry.service.XmlRegistry.DBEntry[] entries = registry_.getDBEntries();
        for (int i = 0; i < entries.length; i++) {
            SQLConnectionInfo temp = new SQLConnectionInfo();
            temp.driver = systemVars_.getAutoLoadDBDriver();
            temp.password = systemVars_.getAutoLoadDBPassword();
            temp.server = entries[i].dbURL;
            temp.prefix = entries[i].prefix;
            temp.username = systemVars_.getAutoLoadDBUsername();
            readTerminologiesFromServer(temp);
        }

        logger_.debug("Reading available terminologies from SQL servers.");

        // same thing as above, this time for pre-configured servers
        Hashtable<String, SQLConnectionInfo> servers = systemVars_.getSqlServers();

        Enumeration<SQLConnectionInfo> e = servers.elements();
        while (e.hasMoreElements()) {
            SQLConnectionInfo server = e.nextElement();
            readTerminologiesFromServer(server);
        }

        logger_.debug("Reading available terminologies from the lucene index locations");

        // go through all of the index locations, finding the right index for
        // each code system.
        // initialize the index readers.
        HashSet<String> indexLocations = systemVars_.getIndexLocations();
        Iterator<String> iterator = indexLocations.iterator();

        indexInterfaces_ = new Hashtable<String, IndexInterface>();
        codingSchemeToIndexMap_ = new Hashtable<String, String>();

        while (iterator.hasNext()) {
            String location = iterator.next();

            File temp = new File(location);
            if (!temp.exists() || !temp.isDirectory()) {
                logger_.error("Bad index location " + location);
            } else {

                IndexInterface is = new IndexInterface(location);
				indexInterfaces_.put(location, is);

				ArrayList<String> keys = is.getCodeSystemKeys();
				for (int i = 0; i < keys.size(); i++) {
				    codingSchemeToIndexMap_.put(keys.get(i), location);
				}
            }
        }
      
        // Start up a thread to handle scheduled deactivations
        fdt_ = new FutureDeactivatorThread();
        deactivatorThread_ = new Thread(fdt_);
        // This allows the JVM to exit while this thread is still active.
        deactivatorThread_.setDaemon(true);
        deactivatorThread_.start();
    }


    public void readHistories() {
        Hashtable<String, SQLHistoryInterface> temp = new Hashtable<String, SQLHistoryInterface>();
        logger_.debug("Initializing available history services");
        HistoryEntry[] histories = registry_.getHistoryEntries();
        for (int i = 0; i < histories.length; i++) {
            try {
            	//TODO:
                //temp.put(histories[i].urn, new SQLHistoryInterface(dataSource), histories[i].prefix));
            } catch (Throwable e) {
                logger_.error("Skipping an invalid History configuration due to previous errors.", e);
            }
        }
        historySqlServerInterfaces_ = temp;
    }

    public void rereadAutoLoadIndexes() throws LBInvocationException, UnexpectedInternalError {
        // This wont handle removes - but does handle additions.
        IndexInterface is = indexInterfaces_.get(getSystemVariables().getAutoLoadIndexLocation());
        is.initCodingSchemes();
        ArrayList<String> keys = is.getCodeSystemKeys();
        for (int i = 0; i < keys.size(); i++) {
            codingSchemeToIndexMap_.put(keys.get(i), getSystemVariables().getAutoLoadIndexLocation());
        }
    }

    /**
     * The alias are the different names that a codingscheme can be referenced.
     * We add the alias to the codingSchemeLocalNamesToInternalNameMap_
     * hashtable.
     * 
     * @param alias
     * @param lcs
     */
    private void addToInternalNameMap(String alias, LocalCodingScheme lcs) {
        Hashtable<String, String> temp = codingSchemeLocalNamesToInternalNameMap_.get(alias);
        if (temp == null) {
            temp = new Hashtable<String, String>();
        }
        temp.put(lcs.version, lcs.codingSchemeName);
        codingSchemeLocalNamesToInternalNameMap_.put(alias, temp);
    }

    public AbsoluteCodingSchemeVersionReference[] readTerminologiesFromServer(SQLConnectionInfo server) {
        PreparedStatement getCodingSchemes = null;
        PreparedStatement getLocalNames = null;
        PreparedStatement getSupportedCodingSchemes = null;
        LocalCodingScheme lcs = null;

        ArrayList<AbsoluteCodingSchemeVersionReference> foundSchemes = new ArrayList<AbsoluteCodingSchemeVersionReference>();

        try {

            SQLInterface si = new SQLInterface(dataSource, databaseType, server.prefix);

            try {
                getCodingSchemes = si.checkOutPreparedStatement(" Select "
                        + SQLTableConstants.TBLCOL_CODINGSCHEMENAME
                        + ", "
                        + SQLTableConstants.TBLCOL_REPRESENTSVERSION
                        + ", "
                        + SQLTableConstants.TBLCOL_FORMALNAME
                        + ", "
                        + (si.supports2009Model() ? SQLTableConstants.TBLCOL_CODINGSCHEMEURI
                                : SQLTableConstants.TBLCOL_REGISTEREDNAME) + " from "
                        + si.getTableName(SQLTableConstants.CODING_SCHEME));

                ResultSet results = getCodingSchemes.executeQuery();

                while (results.next()) {
                    String registeredName = null;

                    registeredName = (si.supports2009Model() ? results
                            .getString(SQLTableConstants.TBLCOL_CODINGSCHEMEURI) : results
                            .getString(SQLTableConstants.TBLCOL_REGISTEREDNAME));

                    lcs = new LocalCodingScheme();
                    lcs.codingSchemeName = results.getString(SQLTableConstants.TBLCOL_CODINGSCHEMENAME);
                    lcs.version = results.getString(SQLTableConstants.TBLCOL_REPRESENTSVERSION);

                    codingSchemeToServerMap_.put(lcs.getKey(), si.getKey());

                    AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
                    acsvr.setCodingSchemeURN(registeredName);
                    acsvr.setCodingSchemeVersion(lcs.version);
                    foundSchemes.add(acsvr);

                    // populate the internal name / uid map
                    internalCodingSchemeNameUIDMap_.put(lcs.codingSchemeName, registeredName);

                    addToInternalNameMap(lcs.codingSchemeName, lcs);
                    addToInternalNameMap(lcs.getCodingSchemeNameWithoutVersion(), lcs);
                    addToInternalNameMap(registeredName, lcs);
                    addToInternalNameMap(results.getString(SQLTableConstants.TBLCOL_FORMALNAME), lcs);
                }
                results.close();
                si.checkInPreparedStatement(getCodingSchemes);

                // load all of the localName mappings
                getLocalNames = si.checkOutPreparedStatement(" select " + SQLTableConstants.TBLCOL_CODINGSCHEMENAME
                        + "," + SQLTableConstants.TBLCOL_ATTRIBUTEVALUE + " from "
                        + si.getTableName(SQLTableConstants.CODING_SCHEME_MULTI_ATTRIBUTES) + " where "
                        + SQLTableConstants.TBLCOL_TYPENAME + " = ?");
                getLocalNames.setString(1, SQLTableConstants.TBLCOLVAL_LOCALNAME);

                results = getLocalNames.executeQuery();
                while (results.next()) {
                    // append the version info onto all of the local names as
                    // well.

                    String value = results.getString(SQLTableConstants.TBLCOL_ATTRIBUTEVALUE);
                    addToInternalNameMap(value, lcs);
                }
                results.close();
                si.checkInPreparedStatement(getLocalNames);

                // load up the supportedCodingScheme mappings.

                getSupportedCodingSchemes = si.checkOutPreparedStatement(" Select "
                        + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + ", " + SQLTableConstants.TBLCOL_ID + ", "
                        + (si.supports2009Model() ? SQLTableConstants.TBLCOL_URI : SQLTableConstants.TBLCOL_URN)
                        + " from " + si.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES) + " where "
                        + SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG + " = ?");

                getSupportedCodingSchemes.setString(1, SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME);

                results = getSupportedCodingSchemes.executeQuery();
                while (results.next()) {
                    String codingSchemeName = results.getString(SQLTableConstants.TBLCOL_CODINGSCHEMENAME);
                    // pickup the values - tack the codingScheme on the end
                    // (these will be keys, need to
                    // search on a per codingScheme basis)
                    String urnAndCS = (si.supports2009Model() ? results.getString(SQLTableConstants.TBLCOL_URI)
                            : results.getString(SQLTableConstants.TBLCOL_URN))
                            + codingSchemeVersionSeparator_ + codingSchemeName;
                    String value = results.getString(SQLTableConstants.TBLCOL_ID);
                    String valueAndCS = value + codingSchemeVersionSeparator_ + codingSchemeName;

                    // one entry for URN to supportedCodingScheme
                    if (urnAndCS != null && urnAndCS.length() > 0) {
                        supportedCodingSchemeToInternalMap_.put(urnAndCS, value);
                    }
                    // one for value to itself
                    supportedCodingSchemeToInternalMap_.put(valueAndCS, value);
                }

                // All done loading the mapping details for this sql interface,
                // finally add the mapping to
                // the sql interface.
                sqlServerInterfaces_.put(si.getKey(), si);
            } finally {
                si.checkInPreparedStatement(getCodingSchemes);
                si.checkInPreparedStatement(getLocalNames);
                si.checkInPreparedStatement(getSupportedCodingSchemes);
            }
        } catch (SQLException e1) {
            logger_.error("Skipping SQL Server " + server.server
                    + " because of a problem reading the available coding schemes", e1);
        }

        return foundSchemes.toArray(new AbsoluteCodingSchemeVersionReference[foundSchemes.size()]);
    }

    public SystemVariables getSystemVariables() {
        return systemVars_;
    }

    public Logger getLogger() {
        return logger_;
    }

    public Map getCache() {
        return cache_;
    }

    /**
     * Get the connection information to use for loading a new db. This creates
     * a new database.
     */
    public SQLConnectionInfo getSQLConnectionInfoForLoad() throws LBInvocationException {
        try {
            String id = ResourceManager.instance().getRegistry().getNextDBIdentifier();

            String server = systemVars_.getAutoLoadDBURL();

            boolean singleDBMode = ResourceManager.instance().getSystemVariables().getAutoLoadSingleDBMode();

            if(!singleDBMode && !ResourceManager.instance().getSystemVariables().getOverrideSingleDbMode()){
                String errorMessage = "Multi-database Mode has been Deprecated." +
                        " Existing content loaded in Multi-database Mode may still be accessed, " +
                "but any new content must be loaded in Single-database Mode.";
                
                String logId = getLogger().error(errorMessage);              
                throw new LBInvocationException(errorMessage, logId);     
            }
            
            String dbName = "";
            String prefix = "";
            if (!singleDBMode) {
                dbName = systemVars_.getAutoLoadDBPrefix() + id;
                if (server.indexOf("Microsoft Access Driver") != -1) {
                    dbName += ".mdb";
                }

                DBUtility.createDatabase(server, systemVars_.getAutoLoadDBDriver(), dbName, systemVars_
                        .getAutoLoadDBUsername(), systemVars_.getAutoLoadDBPassword(), true);
            } else {
                dbName = "";
                // loop until we find a blank prefix -
                int i = 0;
                while (true) {
                    i++;
                    prefix = systemVars_.getAutoLoadDBPrefix() + id + "_";

                    // Need to check and see if this table naming scheme is
                    // already in use.
                    boolean dbLocationIsBlank = !SQLTableUtilities.doTablesExist(server, systemVars_
                            .getAutoLoadDBDriver(), systemVars_.getAutoLoadDBUsername(), systemVars_
                            .getAutoLoadDBPassword(), prefix);

                    if (dbLocationIsBlank) {
                        break;
                    } else if (i > 100) {
                        // 100 picked out of the blue as a stop point so we
                        // can't loop infinitely.
                        String errId = getLogger().error("Unable to find a blank prefix to use to create tables");
                        throw new LBInvocationException("Unable to find a blank prefix to use to create tables", errId);
                    } else {
                        // not blank.. need to get a new location.
                        id = ResourceManager.instance().getRegistry().getNextDBIdentifier();
                    }
                }
            }

            SQLConnectionInfo sci = new SQLConnectionInfo();
            sci.dbName = dbName;
            sci.prefix = prefix;
            sci.driver = systemVars_.getAutoLoadDBDriver();
            sci.password = systemVars_.getAutoLoadDBPassword();
            sci.server = server + dbName + systemVars_.getAutoLoadDBParameters();
            sci.username = systemVars_.getAutoLoadDBUsername();

            return sci;
        } catch (LBInvocationException e) {
            throw e;
        } catch (Exception e) {
            String id = logger_.error("There was a problem while trying to create a new database", e);
            throw new LBInvocationException("Could not create a new database to store the terminology", id);
        }
    }

    /**
     * Get the connection information to use for loading a new history file.
     * This creates a new database if we are not in single db mode, otherwise,
     * it just sets up a new prefix for table names.
     */
    public SQLConnectionInfo getSQLConnectionInfoForHistoryLoad() throws LBInvocationException {
        try {
            String id = ResourceManager.instance().getRegistry().getNextHistoryIdentifier();

            String server = systemVars_.getAutoLoadDBURL();
            boolean singleDBMode = ResourceManager.instance().getSystemVariables().getAutoLoadSingleDBMode();

            String dbName = "";
            String prefix = "";
            if (!singleDBMode) {
                dbName = systemVars_.getAutoLoadDBPrefix() + "h" + id;
                if (server.indexOf("Microsoft Access Driver") != -1) {
                    dbName += ".mdb";
                }
                DBUtility.createDatabase(server, systemVars_.getAutoLoadDBDriver(), dbName, systemVars_
                        .getAutoLoadDBUsername(), systemVars_.getAutoLoadDBPassword(), true);
            } else {
                dbName = "";
                // loop until we find a blank prefix -
                int i = 0;
                while (true) {
                    i++;
                    prefix = systemVars_.getAutoLoadDBPrefix() + "h" + id + "_";

                    // Need to check and see if this table naming scheme is
                    // already in use.
                    boolean dbLocationIsBlank = !SQLTableUtilities.doHistoryTablesExist(server, systemVars_
                            .getAutoLoadDBDriver(), systemVars_.getAutoLoadDBUsername(), systemVars_
                            .getAutoLoadDBPassword(), prefix);

                    if (dbLocationIsBlank) {
                        break;
                    } else if (i > 100) {
                        // 100 picked out of the blue as a stop point so we
                        // can't loop infinitely.
                        String errId = getLogger().error("Unable to find a blank prefix to use to create tables");
                        throw new LBInvocationException("Unable to find a blank prefix to use to create tables", errId);
                    } else {
                        // not blank.. need to get a new location.
                        id = ResourceManager.instance().getRegistry().getNextHistoryIdentifier();
                    }
                }

            }

            SQLConnectionInfo sci = new SQLConnectionInfo();
            sci.dbName = dbName;
            sci.prefix = prefix;
            sci.driver = systemVars_.getAutoLoadDBDriver();
            sci.password = systemVars_.getAutoLoadDBPassword();
            sci.server = server + dbName + systemVars_.getAutoLoadDBParameters();
            sci.username = systemVars_.getAutoLoadDBUsername();

            return sci;
        } catch (LBInvocationException e) {
            throw e;
        } catch (Exception e) {
            String id = logger_.error("There was a problem while trying to create a new database", e);
            throw new LBInvocationException("Could not create a new database to store the terminology", id);
        }
    }

    public SQLInterface getSQLInterface(String internalCodingSchemeName, String internalVersionString)
            throws MissingResourceException {
        logger_.debug("Returning SQLInterface for " + internalCodingSchemeName + " " + internalVersionString);
        LocalCodingScheme lcs = new LocalCodingScheme();
        lcs.codingSchemeName = internalCodingSchemeName;
        lcs.version = internalVersionString;

        String csKey = lcs.getKey();
        String serverKey = codingSchemeToServerMap_.get(csKey);
        if (serverKey == null) {
            throw new MissingResourceException("No server available for " + lcs.getKey());
        }

        return sqlServerInterfaces_.get(serverKey);
    }

    public String getInternalCodingSchemeNameForUserCodingSchemeName(String codingSchemeName, String version)
            throws LBParameterException {
        if (codingSchemeName == null || codingSchemeName.length() == 0) {
            throw new LBParameterException("The parameter is required", SQLTableConstants.TBLCOL_CODINGSCHEMENAME);
        }
        if (version == null || version.length() == 0) {
            throw new LBParameterException("The parameter is required", SQLTableConstants.TBLCOL_VERSION);
        }

        Hashtable<String, String> temp = codingSchemeLocalNamesToInternalNameMap_.get(codingSchemeName);
        if (temp == null) {
            throw new LBParameterException("No coding scheme could be located for the values you provided",
                    SQLTableConstants.TBLCOL_CODINGSCHEMENAME + ", " + SQLTableConstants.TBLCOL_VERSION,
                    codingSchemeName + ", " + version);
        }

        String result = temp.get(version);

        if (result == null) {
            throw new LBParameterException("No coding scheme could be located for the values you provided",
                    SQLTableConstants.TBLCOL_CODINGSCHEMENAME + ", " + SQLTableConstants.TBLCOL_VERSION,
                    codingSchemeName + ", " + version);
        }
        return result;
    }

    public String getExternalCodingSchemeNameForUserCodingSchemeNameOrId(String codingScheme, String version)

    {
        try {
            if (version == null || version.length() == 0) {
                version = getInternalVersionStringForTag(codingScheme, null);
            }

            return getInternalCodingSchemeNameForUserCodingSchemeName(codingScheme, version);
        } catch (LBParameterException e) {
            return codingScheme;
        }
    }

    /**
     * This works on the SupportedCodingScheme to URN mappings provided in the
     * database. Should take in a name or a URN, and map it to the
     * codingSchemeName that is used in the database as a table key in the
     * associations.
     * 
     * @param urn
     * @return
     * @throws MissingResourceException
     */
    public String getRelationshipCodingSchemeNameForURNorName(String supportedCodingSchemeNameOrUrn,
            String internalCodingSchemeName, boolean throwError) throws MissingResourceException {

        if (supportedCodingSchemeNameOrUrn.equals("urn:oid:1.3.6.1.4.1.2114.108.1.8.1")) {
            // this is the oid used to define hasSubtype - it should be
            // supported for all. The code
            // system that we want to return is whatever code system we are in.
            return internalCodingSchemeName;
        }

        String result = supportedCodingSchemeToInternalMap_.get(supportedCodingSchemeNameOrUrn
                + codingSchemeVersionSeparator_ + internalCodingSchemeName);

        if (result == null || result.length() == 0) {
            if (throwError) {
                throw new MissingResourceException("Cannot map '" + supportedCodingSchemeNameOrUrn + "' in '"
                        + internalCodingSchemeName + "' to an internal relationship name");
            } else {
                // This returns unknown if no mapping exists, because if you
                // union together two code systems,
                // it may exist in one, but not the other. Don't want queries to
                // fail in the one where it
                // doesn't exist - just want them to not get any results.
                result = "--UNKNOWN-RELATIONSHIP-NAME--" + supportedCodingSchemeNameOrUrn;
            }

        }
        return result;
    }

    public String getInternalVersionStringForTag(String externalCodeSystemName, String tag) throws LBParameterException {
        if (externalCodeSystemName == null || externalCodeSystemName.length() == 0) {
            throw new LBParameterException("The parameter is required", SQLTableConstants.TBLCOL_CODINGSCHEMENAME);
        }

        // I'm going to cache this, because it is kind of expensive, and may be
        // a frequent operation
        String key = "internalVersionStringForTag:" + externalCodeSystemName + ":"
                + (tag == null || tag.length() == 0 ? KnownTags.PRODUCTION.toString() : tag);

        String version = (String) cache_.get(key);

        if (version != null) {
            return version;
        }

        // not in the cache, find it.

        Hashtable<String, String> temp = codingSchemeLocalNamesToInternalNameMap_.get(externalCodeSystemName);
        if (temp == null) {
            throw new LBParameterException("No coding scheme could be located for the values you provided",
                    SQLTableConstants.TBLCOL_CODINGSCHEMENAME + ", " + SQLTableConstants.TBLCOL_VERSION,
                    externalCodeSystemName + ", " + tag);
        }

        // The hashtable that is returned is a mapping of versions to the
        // internal code system names.
        // it is likely but not guaranteed that all of the code system names
        // will be identical.
        // They should all map to the SAME urn however, so get the URN for the
        // first one.
        // ask the registry for the version number associated with the given tag
        // on the (found) urn.

        Enumeration<String> e = temp.elements();
        String urn = "";
        if (e.hasMoreElements()) {
            urn = getURNForInternalCodingSchemeName(e.nextElement());
        } else {
            throw new LBParameterException("No coding scheme could be located for the values you provided",
                    SQLTableConstants.TBLCOL_CODINGSCHEMENAME + ", " + SQLTableConstants.TBLCOL_VERSION,
                    externalCodeSystemName + ", " + tag);
        }

        // if the tag is missing or null, then we should use "PRODUCTION"
        version = registry_.getVersionForTag(urn, (tag == null || tag.length() == 0 ? KnownTags.PRODUCTION
                .toString() : tag));

        if (version == null) {
            if (tag != null && !tag.equals(KnownTags.PRODUCTION.toString())) {
                // if they specified a tag, and it wasn't the production tag,
                // and we didn't find it
                // then the tag is invalid.
                throw new LBParameterException("No version of the code system " + externalCodeSystemName
                        + " is tagged as " + tag);
            }
            // they didn't specify a tag, or the specified production, but
            // nothing it tagged
            // as production. If we only have one that matches, return it. Else,
            // ask for clairification.
            else if (temp.size() > 1) {

                throw new LBParameterException(
                        "Multiple code systems matched the values you provided - please be more specific (or designate one of the code systems as 'PRODUCTION'",
                        SQLTableConstants.TBLCOL_CODINGSCHEMENAME + ", " + SQLTableConstants.TBLCOL_VERSION,
                        externalCodeSystemName + ", " + tag);
            } else {
                version = temp.keySet().toArray(new String[temp.size()])[0];
            }
        }

        cache_.put(key, version);
        return version;
    }

    public IndexInterface getIndexInterface(String internalCodingSchemeName, String internalVersionString)
            throws MissingResourceException {
        logger_.debug("Returning index interface for " + internalCodingSchemeName + " " + internalVersionString);
        LocalCodingScheme lcs = new LocalCodingScheme();
        lcs.codingSchemeName = internalCodingSchemeName;
        lcs.version = internalVersionString;

        String indexKey = codingSchemeToIndexMap_.get(lcs.getKey());

        if (indexKey == null) {
            throw new MissingResourceException("No index available for " + lcs.getKey());
        }

        return indexInterfaces_.get(indexKey);
    }

    public IndexInterface getMetaDataIndexInterface() {
        logger_.debug("Returning MetaData index interface");
        // metadata index is always in the autoload index location
        return indexInterfaces_.get(systemVars_.getAutoLoadIndexLocation());
    }

    public SQLInterface[] getAllSQLInterfaces() {
        ArrayList<SQLInterface> temp = new ArrayList<SQLInterface>();

        Enumeration<SQLInterface> e = sqlServerInterfaces_.elements();
        while (e.hasMoreElements()) {
            temp.add(e.nextElement());
        }

        return temp.toArray(new SQLInterface[temp.size()]);
    }

    public String getURNForInternalCodingSchemeName(String internalCodingSchemeName) throws LBParameterException {
        String result = internalCodingSchemeNameUIDMap_.get(internalCodingSchemeName);
        if (result == null) {
            throw new LBParameterException("No URN was found for: ", "internalCodingSchemeName",
                    internalCodingSchemeName);
        }
        return result;
    }

    public String getURNForExternalCodingSchemeName(String externalCodingSchemeName) throws LBParameterException {
        String version = getInternalVersionStringForTag(externalCodingSchemeName, null);
        String internalName = getInternalCodingSchemeNameForUserCodingSchemeName(externalCodingSchemeName, version);

        return getURNForInternalCodingSchemeName(internalName);
    }

    public SQLHistoryInterface getSQLInterfaceForHistory(String codingSchemeURN) throws LBParameterException {
        SQLHistoryInterface result = historySqlServerInterfaces_.get(codingSchemeURN);
        if (result == null) {
            throw new LBParameterException("No History service could be found for: ", "codingSchemeURN",
                    codingSchemeURN);
        }
        return result;
    }

    /**
     * @return the registry
     */
    public XmlRegistry getRegistry() {
        return this.registry_;
    }

    private void removeInternalMap(LocalCodingScheme lcs) {

        Enumeration<String> e = codingSchemeLocalNamesToInternalNameMap_.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            Hashtable<String, String> temp = codingSchemeLocalNamesToInternalNameMap_.get(key);
            String temp_cs = temp.get(lcs.version);
            if (temp_cs != null && temp_cs.equals(lcs.codingSchemeName)) {
                temp.remove(lcs.version);
                if (temp.size() == 0) {
                    codingSchemeLocalNamesToInternalNameMap_.remove(key);
                }
            }

        }

    }

    public void removeCodeSystem(AbsoluteCodingSchemeVersionReference codingSchemeReference)
            throws LBInvocationException, LBParameterException {
        try {
            WriteLockManager.instance().acquireLock(codingSchemeReference.getCodingSchemeURN(),
                    codingSchemeReference.getCodingSchemeVersion());
            try {
                // garbage collect to ensure any unreferenced ResourceManagers
                // get removed (and the
                // finalize method gets called to drop unused db connections
                System.gc();

                // start by collecting necessary info about this code system
                LocalCodingScheme lcs = new LocalCodingScheme();
                lcs.codingSchemeName = getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeReference
                        .getCodingSchemeURN(), codingSchemeReference.getCodingSchemeVersion());
                lcs.version = codingSchemeReference.getCodingSchemeVersion();

                String lcsKey = lcs.getKey();

                String serverId = codingSchemeToServerMap_.get(lcsKey);
                String indexId = codingSchemeToIndexMap_.get(lcsKey);

                // clean out the first two maps..
                codingSchemeToServerMap_.remove(lcsKey);
                codingSchemeToIndexMap_.remove(lcsKey);

                boolean singleDBMode = ResourceManager.instance().getSystemVariables().getAutoLoadSingleDBMode();

                // close down the sql statements, remove it from them server
                // map.
                SQLInterface si = sqlServerInterfaces_.get(serverId);
                
                /* TODO:
                String connectionKey = si.getConnectionKey();
                sqlServerInterfaces_.remove(serverId);

                // drop the tables if we are in single db mode.
                if (singleDBMode) {
                    si.dropTables();
                }

                // close the connection
                boolean closedConnection = si.close();

                // if we closed the underlying connection, we need to remove
                // this from the sqlServerBaseInterfaces map.
                if (closedConnection) {
                    sqlServerBaseInterfaces_.remove(connectionKey);
                }
                 */
                
                String dbName = registry_.getDBCodingSchemeEntry(codingSchemeReference).dbName;
                
                //This is for backwards compatiblity. Since multi-db mode is now deprecated,
                //this enables us to still drop a database that has been previously loaded.
                //We detect a multi-db load by detecting if the 'dbName' is not blank in the
                //registry. We then reconstruct the jdbc url from the registry.
                if (!singleDBMode || StringUtils.isNotBlank(dbName)){   
                    String url = registry_.getDBCodingSchemeEntry(codingSchemeReference).dbURL;
                    url = this.constructJdbcUrlForDeprecatedMultiDbMode(url, dbName);
                    DBUtility.dropDatabase(url, systemVars_.getAutoLoadDBDriver(), dbName,
                            systemVars_.getAutoLoadDBUsername(), systemVars_.getAutoLoadDBPassword());
                }

                // all automatic code systems are in a single index interface -
                // so need to clean that
                // up from within.
                // mturk 1/8/2009 -- added check for null indexId value
                if (indexId != null) {
                    indexInterfaces_.get(indexId).deleteIndex(lcs.codingSchemeName, lcs.version);
                }

                // clean up the localName - internal name / version map

                removeInternalMap(lcs);

                Hashtable<String, String> temp = codingSchemeLocalNamesToInternalNameMap_.get(codingSchemeReference
                        .getCodingSchemeURN());

                // if the hashtable is now empty, we should remove any key that
                // maps to this hashtable in the
                // local name map
                if (temp == null || temp.size() == 0) {
                    // also, if this hashtable was empty, it means that no other
                    // code systems exist with the
                    // same UID - so I can also clear the
                    // internalCodingSchemeNameUIDMap
                    Enumeration<String> e = internalCodingSchemeNameUIDMap_.keys();
                    while (e.hasMoreElements()) {
                        String key = (String) e.nextElement();
                        if (internalCodingSchemeNameUIDMap_.get(key).equals(codingSchemeReference.getCodingSchemeURN())) {
                            internalCodingSchemeNameUIDMap_.remove(key);
                        }
                    }
                }

                // The supportecCodingSchemeToInternalMap_ should be cleaned
                // here, but it isn't structured in
                // a way that makes that easy. So skip it - shouldn't cause any
                // harm

                // clear the lru cache.

                cache_.clear();
                // remove it from the metadata search
                
                //TODO: Move this to the service layer
                //BaseMetaDataLoader.removeMeta(codingSchemeReference.getCodingSchemeURN(), codingSchemeReference
                //       .getCodingSchemeVersion());

                // remove it from the registry
                registry_.remove(codingSchemeReference);
                WriteLockManager.instance().releaseLock(codingSchemeReference.getCodingSchemeURN(),
                        codingSchemeReference.getCodingSchemeVersion());
            } catch (Exception e) {
                String id = logger_.error("Unexpected error while removing a coding scheme", e);
                throw new LBInvocationException("There was an unexpected error while removing the coding scheme", id);
            }
        } finally {
            WriteLockManager.instance().releaseLock(codingSchemeReference.getCodingSchemeURN(),
                    codingSchemeReference.getCodingSchemeVersion());
        }

    }

    public void removeHistoryService(String urn) throws LBInvocationException, LBParameterException {
        try {
            // garbage collect to ensure any unreferenced ResourceManagers get
            // removed (and the
            // finalize method gets called to drop unused db connections
            System.gc();

            // get the current interface.
            SQLHistoryInterface shi = getSQLInterfaceForHistory(urn);

            if (shi == null) {
                throw new LBParameterException("Unknown history service", urn);
            }

            //TODO:
            //String connectionKey = shi.getConnectionKey();

            // remove it from the map.
            historySqlServerInterfaces_.remove(urn);

            boolean singleDBMode = ResourceManager.instance().getSystemVariables().getAutoLoadSingleDBMode();

            // drop the tables if we are in single db mode.
            if (singleDBMode) {
                shi.dropTables();
            }

            //TODO:
            // close the connection
           // boolean closedConnection = shi.close();

        

            String dbName = registry_.getHistoryEntry(urn).dbName;
            
            // if we are not in single db mode, drop the database.
            if (!singleDBMode || StringUtils.isNotBlank(dbName)){
                String url = registry_.getHistoryEntry(urn).dbURL;
                url = this.constructJdbcUrlForDeprecatedMultiDbMode(url, dbName);
                DBUtility.dropDatabase(url, systemVars_.getAutoLoadDBDriver(), dbName,
                        systemVars_.getAutoLoadDBUsername(), systemVars_.getAutoLoadDBPassword());
            }

            // remove it from the registry
            registry_.removeHistoryEntry(urn);

        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String id = logger_.error("Unexpected error while removing a history coding scheme", e);
            throw new LBInvocationException("There was an unexpected error while removing the history coding scheme",
                    id);
        }
    }

    public void deactivate(AbsoluteCodingSchemeVersionReference codingScheme, Date date) throws LBInvocationException,
            LBParameterException {
        XmlRegistry r = getRegistry();
        DBEntry entry = r.getDBCodingSchemeEntry(codingScheme);
        if (entry == null) {
            throw new LBParameterException("The specified coding scheme is not registered");
        }

        if (date == null || date.getTime() <= System.currentTimeMillis()) {
            r.deactivate(entry);
        } else {
            r.setDeactivateDate(codingScheme, date);
            // let the deactivator thread know that it should update its info.
            deactivatorThread_.interrupt();
        }
    }

    public void setPendingStatus(AbsoluteCodingSchemeVersionReference codingScheme) throws LBInvocationException,
            LBParameterException {
        XmlRegistry r = getRegistry();
        DBEntry entry = r.getDBCodingSchemeEntry(codingScheme);
        if (entry == null) {
            throw new LBParameterException("The specified coding scheme is not registered");
        }

        r.setStatusPending(entry);
    }

    public void updateTag(AbsoluteCodingSchemeVersionReference codingScheme, String newTag)
            throws LBInvocationException, LBParameterException {
        getRegistry().updateTag(codingScheme, newTag);
        cache_.clear(); // TODO [Performance] it would be nice to not have to
                        // clear the entire cache because of this.
    }

    public void updateVersion(AbsoluteCodingSchemeVersionReference codingScheme, String newVersion)
            throws LBInvocationException, LBParameterException {
        getRegistry().updateVersion(codingScheme, newVersion);
        cache_.clear(); // TODO [Performance] it would be nice to not have to
                        // clear the entire cache because of this.
    }

    public class FutureDeactivatorThread implements Runnable {
        boolean continueRunning = true;

        public void run() {
            // first number doesn't really matter - want the loop to run once so
            // we find out
            // when the first real deactivation needs to happen.
            long sleepFor = 0;
            while (continueRunning) {

                try {
                    if (sleepFor > 0) {
                        Thread.sleep(sleepFor);
                    }
                } catch (InterruptedException e) {
                    // I've been woken up - see if anything needs deactivation,
                    // and
                    // recalculate how long to sleep.
                    if (!continueRunning) {
                        return;
                    }
                }

                try {
                    long currentTime = System.currentTimeMillis();
                    long timeOfNextClosestDeactivation = 0;
                    // see if anything needs to be deactivated.

                    DBEntry[] entries = getRegistry().getDBEntries();
                    for (int i = 0; i < entries.length; i++) {
                        if (entries[i].deactiveDate > 0 && entries[i].deactiveDate <= currentTime
                                && entries[i].status.equals(CodingSchemeVersionStatus.ACTIVE.toString())) {
                            getLogger().info(
                                    "Deactivating coding scheme (according to schedule) " + entries[i].urn + " : "
                                            + entries[i].version);
                            getRegistry().deactivate(entries[i]);
                        }
                        // if it is marked for deactivation, and its active, and
                        // its deactivation time is
                        // sooner than the next deactivation time, set the time.
                        else if (entries[i].deactiveDate > 0
                                && entries[i].status.equals(CodingSchemeVersionStatus.ACTIVE.toString())
                                && (entries[i].deactiveDate < timeOfNextClosestDeactivation || timeOfNextClosestDeactivation == 0))

                        {
                            timeOfNextClosestDeactivation = entries[i].deactiveDate;
                        }
                    }

                    if (timeOfNextClosestDeactivation > 0) {
                        sleepFor = timeOfNextClosestDeactivation - currentTime;
                    } else {
                        // set it to 30 minutes (arbitrary selection)
                        sleepFor = 30 * 60 * 1000;
                    }
                } catch (Exception e) {
                    getLogger().error("Something failed while running the future deactivate thread", e);
                    // sleep for 1 minute, then try again.
                    sleepFor = 1 * 60 * 1000;
                }
            }
        }
    }
/*
    @Override
    protected void finalize() throws Throwable {
        // close all of the SQL connections
        Enumeration<SQLInterfaceBase> e = sqlServerBaseInterfaces_.elements();
        while (e.hasMoreElements()) {
            try {
                e.nextElement().close();
            } catch (RuntimeException e1) {
            }
        }
        // close all of the index interfaces
        Enumeration<IndexInterface> ii = indexInterfaces_.elements();
        while (ii.hasMoreElements()) {
            try {
                ii.nextElement().close();
            } catch (RuntimeException e1) {
            }
        }
    }
    */

    private String constructJdbcUrlForDeprecatedMultiDbMode(String url, String dbName){
        return StringUtils.remove(url, dbName);
    }


	public void setSystemVariables(SystemVariables systemVars) {
		systemVars_ = systemVars;
	}

	public void setLogger(Logger logger) {
		logger_ = logger;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DatabaseType getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(DatabaseType databaseType) {
		this.databaseType = databaseType;
	}

	public XmlRegistry getXmlRegistry() {
		return registry_;
	}

	public void setXmlRegistry(XmlRegistry registry) {
		registry_ = registry;
	}

	public boolean containsCodingSchemeResource(String uri, String version)
			throws LBParameterException {
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(uri);
		ref.setCodingSchemeVersion(version);
		return this.getRegistry().containsCodingSchemeEntry(ref);
	}

	public boolean containsNonCodingSchemeResource(String uri)
			throws LBParameterException {
		return this.getRegistry().containsNonCodingSchemeEntry(uri);
	}

	public String createNewTablesForLoad() {
		throw new UnsupportedOperationException("Cannot load into Deprected LexEVS Database Schema.");
	}

	public MyClassLoader getClassLoader() {
		throw new UnsupportedOperationException("Please get System Classloader from a non-deprecated SystemResourceService.");
	}

	public String getUriForUserCodingSchemeName(String codingSchemeName) throws LBParameterException {
		return this.getURNForExternalCodingSchemeName(codingSchemeName);
	}

	public void removeCodingSchemeResourceFromSystem(String uri, String version)
			throws LBParameterException {
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(uri);
		ref.setCodingSchemeVersion(version);
		try {
			this.removeCodeSystem(ref);
		} catch (LBInvocationException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void removeNonCodingSchemeResourceFromSystem(String uri)
			throws LBParameterException {
		try {
			this.removeHistoryService(uri);
		} catch (LBInvocationException e) {
			throw new RuntimeException(e);
		}
	}

	public void updateCodingSchemeEntryTag(
			AbsoluteCodingSchemeVersionReference codingScheme, String newTag)
			throws LBParameterException {
		try {
			this.updateTag(codingScheme, newTag);
		} catch (LBInvocationException e) {
			throw new RuntimeException(e);
		}
	}

	public void updateCodingSchemeResourceStatus(
			AbsoluteCodingSchemeVersionReference codingScheme,
			CodingSchemeVersionStatus status) throws LBParameterException {
		if(status.equals(CodingSchemeVersionStatus.ACTIVE)){
			this.updateCodingSchemeEntryTag(codingScheme, status.toString());
		} else {
			try {
				this.deactivate(codingScheme, new Date());
			} catch (LBInvocationException e) {
				throw new RuntimeException(e);
			}
		}	
	}

	public void updateCodingSchemeResourceTag(
			AbsoluteCodingSchemeVersionReference codingScheme, String newTag)
			throws LBParameterException {
		this.updateCodingSchemeEntryTag(codingScheme, newTag);
	}

	public void updateNonCodingSchemeResourceStatus(String uri,
			CodingSchemeVersionStatus status) throws LBParameterException {
		throw new UnsupportedOperationException("Cannot update the status of a non Coding Scheme Resource.");
	}

	public void updateNonCodingSchemeResourceTag(String uri, String newTag)
			throws LBParameterException {
		throw new UnsupportedOperationException("Cannot update the tag of a non Coding Scheme Resource.");
	}  
}
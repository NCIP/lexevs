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
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.codingSchemes.CodingScheme;
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
import org.lexevs.registry.WriteLockManager;
import org.lexevs.registry.service.XmlRegistry;
import org.lexevs.registry.service.Registry.KnownTags;
import org.lexevs.registry.service.XmlRegistry.DBEntry;
import org.lexevs.registry.service.XmlRegistry.HistoryEntry;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.event.SystemEventListener;
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
/**
 * @deprecated
 * Use LexEvsResourceManagingService
 */
@Deprecated
public class ResourceManager implements SystemResourceService {
    
    /** The resource manager_. */
    private static ResourceManager resourceManager_;
    
    /** The system vars_. */
    private SystemVariables systemVars_;
    
    /** The registry_. */
    private XmlRegistry registry_;
    
    /** The data source. */
    private DataSource dataSource;
    
    /** The database type. */
    private DatabaseType databaseType;

    /** The logger_. */
    private LgLoggerIF logger_;

    /** The Constant codingSchemeVersionSeparator_. */
    public static final String codingSchemeVersionSeparator_ = "[:]";

    // This maps internal coding scheme names / version to the serverId that
    // contains them.
    /** The coding scheme to server map_. */
    private Hashtable<String, String> codingSchemeToServerMap_;
    // this maps internal coding scheme names / version to the indexId that
    // contains them.
    /** The coding scheme to index map_. */
    private Hashtable<String, String> codingSchemeToIndexMap_;

    // this maps history URNs to the SQLInterface object that acceesses them.
    /** The history sql server interfaces_. */
    private Hashtable<String, SQLHistoryInterface> historySqlServerInterfaces_;

    // this maps serverId's to the SQLInterface object that acceesses them.
    /** The sql server interfaces_. */
    private Hashtable<String, SQLInterface> sqlServerInterfaces_;

    // this maps indexId's to the IndexInterface that accesses them.
    /** The index interfaces_. */
    private Hashtable<String, IndexInterface> indexInterfaces_;

    // This maps all available coding scheme "localNames" to the internal coding
    // scheme name
    // local names key into another hashtable, from there, the key is the
    // version. the value in the
    // second hashtable is the internal coding scheme name.
    /** The coding scheme local names to internal name map_. */
    private Hashtable<String, Hashtable<String, String>> codingSchemeLocalNamesToInternalNameMap_;

    // This maps internal coding scheme names to their UID.
    /** The internal coding scheme name uid map_. */
    private Hashtable<String, List<LocalCodingScheme>> internalCodingSchemeNameUIDMap_;

    // this maps SupportedCodingScheme URN's (for relations) to the
    // codingschemeNames that are
    // used in the relations (on a per codingScheme basis)
    /** The supported coding scheme to internal map_. */
    private Hashtable<String, String> supportedCodingSchemeToInternalMap_;


    // A cache to use for values that are frequently used. Automatically throws
    // away oldest unused items.
    /** The cache_. */
    private Map cache_;

    // Properties object that I was launched with (need to keep incase reinit is
    // called)
    /** The props_. */
    private static Properties props_;

    /**
     * Instance.
     * 
     * @return the resource manager
     */
    public static ResourceManager instance() {
        return LexEvsServiceLocator.getInstance().getResourceManager();
    }

    /*
     * This method is called if a startup completely fails, so that you can get
     * more debugging info.
     */
    /**
     * Dump log queue.
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
     * 
     * @param props the props
     */
    public static void reInit(Properties props) {

        try {
            if (props == null) {
                props = props_;
            }


            // stop the deactivator thread in the old resource manager
            ResourceManager oldRM = resourceManager_;

            //force the finalize (to make sure the db connections get closed)
            oldRM.finalize();
            oldRM = null;
        } catch (Throwable e) {
            // I can't throw the InitializationException here, because it
            // depends on this class
            // being built properly.

        }

    }


    /**
     * Inits the.
     * 
     * @throws Exception the exception
     */
    public void init() throws Exception {
        cache_ = Collections.synchronizedMap(new LRUMap(systemVars_.getCacheSize()));  
        
        // This increases the ability of Lucene to do queries against
        // large indexes like the MetaThesaurus without getting errors.
        BooleanQuery.setMaxClauseCount(
                systemVars_.getLuceneMaxClauseCount());

        codingSchemeToServerMap_ = new Hashtable<String, String>();
        sqlServerInterfaces_ = new Hashtable<String, SQLInterface>();
        historySqlServerInterfaces_ = new Hashtable<String, SQLHistoryInterface>();
        codingSchemeLocalNamesToInternalNameMap_ = new Hashtable<String, Hashtable<String, String>>();
        internalCodingSchemeNameUIDMap_ = new Hashtable<String, List<LocalCodingScheme>>();
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
//        fdt_ = new FutureDeactivatorThread();
//        setDeactivatorThread_(new Thread(fdt_));
//        // This allows the JVM to exit while this thread is still active.
//        getDeactivatorThread_().setDaemon(true);
//        getDeactivatorThread_().start();
    }


    /**
     * Read histories.
     */
    public void readHistories() {
        Hashtable<String, SQLHistoryInterface> temp = new Hashtable<String, SQLHistoryInterface>();
        logger_.debug("Initializing available history services");
        HistoryEntry[] histories = registry_.getHistoryEntries();
        for (int i = 0; i < histories.length; i++) {
            try {
                temp.put(histories[i].urn, new SQLHistoryInterface(dataSource, this.getDatabaseType(), histories[i].prefix));
            } catch (Throwable e) {
                logger_.error("Skipping an invalid History configuration due to previous errors.", e);
            }
        }
        historySqlServerInterfaces_ = temp;
    }

    /**
     * The alias are the different names that a codingscheme can be referenced.
     * We add the alias to the codingSchemeLocalNamesToInternalNameMap_
     * hashtable.
     * 
     * @param alias the alias
     * @param lcs the lcs
     */
    private void addToInternalNameMap(String alias, LocalCodingScheme lcs) {
        Hashtable<String, String> temp = codingSchemeLocalNamesToInternalNameMap_.get(alias);
        if (temp == null) {
            temp = new Hashtable<String, String>();
        }
        temp.put(lcs.version, lcs.codingSchemeName);
        codingSchemeLocalNamesToInternalNameMap_.put(alias, temp);
    }

    /**
     * Read terminologies from server.
     * 
     * @param server the server
     * 
     * @return the absolute coding scheme version reference[]
     */
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
                    if(!internalCodingSchemeNameUIDMap_.containsKey(registeredName)) {
                    	internalCodingSchemeNameUIDMap_.put(registeredName, new ArrayList<LocalCodingScheme>());
                    }
                    internalCodingSchemeNameUIDMap_.get(registeredName).add(lcs);

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

    /**
     * Gets the system variables.
     * 
     * @return the system variables
     */
    public SystemVariables getSystemVariables() {
        return systemVars_;
    }

    /**
     * Gets the logger.
     * 
     * @return the logger
     */
    public LgLoggerIF getLogger() {
        return logger_;
    }

    /**
     * Gets the cache.
     * 
     * @return the cache
     */
    public Map getCache() {
        return cache_;
    }

    /**
     * Get the connection information to use for loading a new db. This creates
     * a new database.
     * 
     * @return the SQL connection info for load
     * 
     * @throws LBInvocationException the LB invocation exception
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
     * 
     * @return the SQL connection info for history load
     * 
     * @throws LBInvocationException the LB invocation exception
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

    /**
     * Gets the sQL interface.
     * 
     * @param internalCodingSchemeName the internal coding scheme name
     * @param internalVersionString the internal version string
     * 
     * @return the sQL interface
     * 
     * @throws MissingResourceException the missing resource exception
     */
    public SQLInterface getSQLInterface(String internalCodingSchemeName, String internalVersionString)
            throws MissingResourceException {
        logger_.debug("Returning SQLInterface for " + internalCodingSchemeName + " " + internalVersionString);
        LocalCodingScheme lcs = new LocalCodingScheme();
        lcs.codingSchemeName = internalCodingSchemeName;
        lcs.version = internalVersionString;

        String csKey = lcs.getKey();
        String serverKey = codingSchemeToServerMap_.get(csKey);
        if (serverKey == null) {
        	String uri;
			try {
				uri = LexEvsServiceLocator.getInstance().getSystemResourceService().
        			getUriForUserCodingSchemeName(internalCodingSchemeName, internalVersionString);
				
				SQLInterface sqlInterface = new SQLInterface(uri, internalVersionString);
				
				sqlServerInterfaces_.put(csKey, sqlInterface);
				
				return sqlInterface;
				
			} catch (LBParameterException e) {
				logger_.warn("Unexpected Error", e);
			}
        	
            throw new MissingResourceException("No server available for " + lcs.getKey());
        }

        return sqlServerInterfaces_.get(serverKey);
    }

    /* (non-Javadoc)
     * @see org.lexevs.system.service.SystemResourceService#getInternalCodingSchemeNameForUserCodingSchemeName(java.lang.String, java.lang.String)
     */
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

    /**
     * Gets the external coding scheme name for user coding scheme name or id.
     * 
     * @param codingScheme the coding scheme
     * @param version the version
     * 
     * @return the external coding scheme name for user coding scheme name or id
     */
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
     * @param supportedCodingSchemeNameOrUrn the supported coding scheme name or urn
     * @param internalCodingSchemeName the internal coding scheme name
     * @param throwError the throw error
     * 
     * @return the relationship coding scheme name for ur nor name
     * 
     * @throws MissingResourceException the missing resource exception
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

    /* (non-Javadoc)
     * @see org.lexevs.system.service.SystemResourceService#getInternalVersionStringForTag(java.lang.String, java.lang.String)
     */
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
            Set<String> uris = getAllURNsForInternalCodingSchemeName(e.nextElement());
            if(uris.size() == 1) {
            	urn = uris.iterator().next();
            } else {
            	if(uris.contains(externalCodeSystemName)) {
            		for(String uri : uris) {
            			if(uri.equals(externalCodeSystemName)) {
            				urn = uri;
            			}
            		}
            	}
            }
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

    /**
     * Gets the index interface.
     * 
     * @param internalCodingSchemeName the internal coding scheme name
     * @param internalVersionString the internal version string
     * 
     * @return the index interface
     * 
     * @throws MissingResourceException the missing resource exception
     */
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


    /**
     * Gets the all sql interfaces.
     * 
     * @return the all sql interfaces
     */
    public SQLInterface[] getAllSQLInterfaces() {
        ArrayList<SQLInterface> temp = new ArrayList<SQLInterface>();

        Enumeration<SQLInterface> e = sqlServerInterfaces_.elements();
        while (e.hasMoreElements()) {
            temp.add(e.nextElement());
        }

        return temp.toArray(new SQLInterface[temp.size()]);
    }

    /**
     * Gets the uRN for internal coding scheme name.
     * 
     * @param internalCodingSchemeName the internal coding scheme name
     * 
     * @return the uRN for internal coding scheme name
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public String getURNForInternalCodingSchemeName(String internalCodingSchemeName, 
    		String internalVersionString) throws LBParameterException {
    	for(Entry<String, List<LocalCodingScheme>> entry : this.internalCodingSchemeNameUIDMap_.entrySet()) {
    		for(LocalCodingScheme lcs : entry.getValue()) {
    			if(lcs.codingSchemeName.equals(internalCodingSchemeName)
    					&&
    					lcs.version.equals(internalVersionString)){
    				return entry.getKey();
    			}
    		}
    	}
    	throw new LBParameterException("No URN was found for: ", "internalCodingSchemeName",
    			internalCodingSchemeName);
    }
    
    private Set<String> getAllURNsForInternalCodingSchemeName(String internalCodingSchemeName) throws LBParameterException {
    	Set<String> returnSet = new HashSet<String>();
    	for(Entry<String, List<LocalCodingScheme>> entry : this.internalCodingSchemeNameUIDMap_.entrySet()) {
    		for(LocalCodingScheme lcs : entry.getValue()) {
    			if(lcs.codingSchemeName.equals(internalCodingSchemeName)){
    				returnSet.add(entry.getKey());
    			}
    		}
    	}
    	return returnSet;
    }

    /**
     * Gets the uRN for external coding scheme name.
     * 
     * @param externalCodingSchemeName the external coding scheme name
     * 
     * @return the uRN for external coding scheme name
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public String getURNForExternalCodingSchemeName(String externalCodingSchemeName, String version) throws LBParameterException {
        String internalName = getInternalCodingSchemeNameForUserCodingSchemeName(externalCodingSchemeName, version);

        return getURNForInternalCodingSchemeName(internalName, version);
    }

    public String getURNForExternalCodingSchemeName(String externalCodingSchemeName) throws LBParameterException {
        String version = this.getInternalVersionStringForTag(externalCodingSchemeName, null);
        String internalName = this.getInternalCodingSchemeNameForUserCodingSchemeName(externalCodingSchemeName, version);

        return getURNForInternalCodingSchemeName(internalName, version);
    }
    
    public String getURNForInternalCodingSchemeName(String internalCodingSchemeName) throws LBParameterException {
        
        Set<String> uris = getAllURNsForInternalCodingSchemeName(internalCodingSchemeName);
        
        if(uris.size() == 0) {
        	throw new LBParameterException("No URN was found for: ", "internalCodingSchemeName",
        			internalCodingSchemeName);
        }
        
        if(uris.size() > 1) {
        	throw new LBParameterException("Multiple URNs were found for: ", "internalCodingSchemeName",
        			internalCodingSchemeName);
        }
        
        return uris.iterator().next();
    }
    
    /**
     * Gets the sQL interface for history.
     * 
     * @param codingSchemeURN the coding scheme urn
     * 
     * @return the sQL interface for history
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public SQLHistoryInterface getSQLInterfaceForHistory(String codingSchemeURN) throws LBParameterException {
        SQLHistoryInterface result = historySqlServerInterfaces_.get(codingSchemeURN);
        if (result == null) {
            throw new LBParameterException("No History service could be found for: ", "codingSchemeURN",
                    codingSchemeURN);
        }
        return result;
    }

    /**
     * Gets the registry.
     * 
     * @return the registry
     */
    public XmlRegistry getRegistry() {
        return this.registry_;
    }

    /**
     * Removes the internal map.
     * 
     * @param lcs the lcs
     */
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

    /**
     * Removes the code system.
     * 
     * @param codingSchemeReference the coding scheme reference
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
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
                
                sqlServerInterfaces_.remove(serverId);

                // drop the tables if we are in single db mode.
                if (singleDBMode) {
                    si.dropTables();
                }
 
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

    @Override
	public void removeNciHistoryResourceToSystemFromSystem(String uri) {
		try {
			this.removeHistoryService(uri);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	/**
     * Removes the history service.
     * 
     * @param urn the urn
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
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

    /**
     * Deactivate.
     * 
     * @param codingScheme the coding scheme
     * @param date the date
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
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
        }
    }

    /**
     * Sets the pending status.
     * 
     * @param codingScheme the new pending status
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void setPendingStatus(AbsoluteCodingSchemeVersionReference codingScheme) throws LBInvocationException,
            LBParameterException {
        XmlRegistry r = getRegistry();
        DBEntry entry = r.getDBCodingSchemeEntry(codingScheme);
        if (entry == null) {
            throw new LBParameterException("The specified coding scheme is not registered");
        }

        r.setStatusPending(entry);
    }

    /**
     * Update tag.
     * 
     * @param codingScheme the coding scheme
     * @param newTag the new tag
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void updateTag(AbsoluteCodingSchemeVersionReference codingScheme, String newTag)
            throws LBInvocationException, LBParameterException {
        getRegistry().updateTag(codingScheme, newTag);
        cache_.clear(); // TODO [Performance] it would be nice to not have to
                        // clear the entire cache because of this.
    }

    /**
     * Update version.
     * 
     * @param codingScheme the coding scheme
     * @param newVersion the new version
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void updateVersion(AbsoluteCodingSchemeVersionReference codingScheme, String newVersion)
            throws LBInvocationException, LBParameterException {
        getRegistry().updateVersion(codingScheme, newVersion);
        cache_.clear(); // TODO [Performance] it would be nice to not have to
                        // clear the entire cache because of this.
    }

/**
 * Construct jdbc url for deprecated multi db mode.
 * 
 * @param url the url
 * @param dbName the db name
 * 
 * @return the string
 */
private String constructJdbcUrlForDeprecatedMultiDbMode(String url, String dbName){
        return StringUtils.remove(url, dbName);
    }


	/**
	 * Sets the system variables.
	 * 
	 * @param systemVars the new system variables
	 */
	public void setSystemVariables(SystemVariables systemVars) {
		systemVars_ = systemVars;
	}

	/**
	 * Sets the logger.
	 * 
	 * @param logger the new logger
	 */
	public void setLogger(LgLoggerIF logger) {
		logger_ = logger;
	}

	/**
	 * Gets the data source.
	 * 
	 * @return the data source
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * Sets the data source.
	 * 
	 * @param dataSource the new data source
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Gets the database type.
	 * 
	 * @return the database type
	 */
	public DatabaseType getDatabaseType() {
		return databaseType;
	}

	/**
	 * Sets the database type.
	 * 
	 * @param databaseType the new database type
	 */
	public void setDatabaseType(DatabaseType databaseType) {
		this.databaseType = databaseType;
	}

	/**
	 * Gets the xml registry.
	 * 
	 * @return the xml registry
	 */
	public XmlRegistry getXmlRegistry() {
		return registry_;
	}

	/**
	 * Sets the xml registry.
	 * 
	 * @param registry the new xml registry
	 */
	public void setXmlRegistry(XmlRegistry registry) {
		registry_ = registry;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#containsCodingSchemeResource(java.lang.String, java.lang.String)
	 */
	public boolean containsCodingSchemeResource(String uri, String version)
			throws LBParameterException {
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(uri);
		ref.setCodingSchemeVersion(version);
		return this.getRegistry().containsCodingSchemeEntry(ref);
	}
	
	public boolean containsValueSetDefinitionResource(String uri, String version)
		throws LBParameterException {		
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}
	
	public boolean containsPickListDefinitionResource(String uri, String version)
		throws LBParameterException {		
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#containsNonCodingSchemeResource(java.lang.String)
	 */
	public boolean containsNonCodingSchemeResource(String uri)
			throws LBParameterException {
		return this.getRegistry().containsNonCodingSchemeEntry(uri);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#createNewTablesForLoad()
	 */
	public String createNewTablesForLoad() {
		throw new UnsupportedOperationException("Cannot load into Deprected LexEVS Database Schema.");
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#getClassLoader()
	 */
	public MyClassLoader getClassLoader() {
		throw new UnsupportedOperationException("Please get System Classloader from a non-deprecated SystemResourceService.");
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#getUriForUserCodingSchemeName(java.lang.String)
	 */
	public String getUriForUserCodingSchemeName(String codingSchemeName, String version) throws LBParameterException {
		return this.getURNForExternalCodingSchemeName(codingSchemeName, version);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#removeCodingSchemeResourceFromSystem(java.lang.String, java.lang.String)
	 */
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
	
	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#removeNonCodingSchemeResourceFromSystem(java.lang.String)
	 */
	public void removeNonCodingSchemeResourceFromSystem(String uri)
			throws LBParameterException {
		try {
			this.removeHistoryService(uri);
		} catch (LBInvocationException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void removeValueSetDefinitionResourceFromSystem(String uri, String version)
			throws LBParameterException {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void removePickListDefinitionResourceFromSystem(String uri, String version)
			throws LBParameterException {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	/**
	 * Update coding scheme entry tag.
	 * 
	 * @param codingScheme the coding scheme
	 * @param newTag the new tag
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void updateCodingSchemeEntryTag(
			AbsoluteCodingSchemeVersionReference codingScheme, String newTag)
			throws LBParameterException {
		try {
			this.updateTag(codingScheme, newTag);
		} catch (LBInvocationException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#updateCodingSchemeResourceStatus(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus)
	 */
	public void updateCodingSchemeResourceStatus(
			AbsoluteCodingSchemeVersionReference codingScheme,
			CodingSchemeVersionStatus status) throws LBParameterException {
		try {
			if(status.equals(CodingSchemeVersionStatus.ACTIVE)){
				this.getRegistry().activate(codingScheme);
			} else {
				this.deactivate(codingScheme, new Date());
			}	
		} catch (LBInvocationException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#updateCodingSchemeResourceTag(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, java.lang.String)
	 */
	public void updateCodingSchemeResourceTag(
			AbsoluteCodingSchemeVersionReference codingScheme, String newTag)
			throws LBParameterException {
		this.updateCodingSchemeEntryTag(codingScheme, newTag);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#updateNonCodingSchemeResourceStatus(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus)
	 */
	public void updateNonCodingSchemeResourceStatus(String uri,
			CodingSchemeVersionStatus status) throws LBParameterException {
		throw new UnsupportedOperationException("Cannot update the status of a non Coding Scheme Resource.");
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#updateNonCodingSchemeResourceTag(java.lang.String, java.lang.String)
	 */
	public void updateNonCodingSchemeResourceTag(String uri, String newTag)
			throws LBParameterException {
		throw new UnsupportedOperationException("Cannot update the tag of a non Coding Scheme Resource.");
	}

	/**
	 * Adds the coding scheme resource from system.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void addCodingSchemeResourceFromSystem(String uri, String version)
			throws LBParameterException {
		throw new UnsupportedOperationException("Cannot add to Deprecated ResourceManager.");
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#addCodingSchemeResourceToSystem(java.lang.String, java.lang.String)
	 */
	@Override
	public void addCodingSchemeResourceToSystem(String uri, String version)
			throws LBParameterException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void addValueSetDefinitionResourceToSystem(String uri, String version)
			throws LBParameterException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void addPickListDefinitionResourceToSystem(String uri, String version)
			throws LBParameterException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#addCodingSchemeResourceToSystem(org.LexGrid.codingSchemes.CodingScheme)
	 */
	public void addCodingSchemeResourceToSystem(CodingScheme codingScheme)
			throws LBParameterException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addNciHistoryResourceToSystem(String uri)
			throws LBParameterException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void initialize() {
		// should already be initialized here.
	}  
	
	public void refresh() {
		try {
			this.init();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addSystemEventListeners(SystemEventListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void registerCodingSchemeSupplement(
			AbsoluteCodingSchemeVersionReference parentScheme,
			AbsoluteCodingSchemeVersionReference supplement)
			throws LBParameterException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void unRegisterCodingSchemeSupplement(
			AbsoluteCodingSchemeVersionReference parentScheme,
			AbsoluteCodingSchemeVersionReference supplement)
			throws LBParameterException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<AbsoluteCodingSchemeVersionReference> getMatchingCodingSchemeResources(
			CodingSchemeMatcher codingSchemeMatcher) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void shutdown() {
		if(resourceManager_ != null){
			resourceManager_.shutdown();
		}
		resourceManager_ = null;
	}

}
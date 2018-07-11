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
package org.lexevs.system.constants;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.connection.SQLConnectionInfo;
import org.lexevs.exceptions.InitializationException;
import org.lexevs.logging.Logger;
import org.lexevs.system.utility.CryptoUtility;
import org.lexevs.system.utility.PropertiesUtility;

/**
 * This class reads and provides access to values specified in the configuration
 * file.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class SystemVariables {
	public static final String ALL_IN_MEMORY_SYSTEM_VARIABLE = "inmemory";
	public static String LG_CONFIG_FILE_SYSTEM_VARIABLE = "LG_CONFIG_FILE";
    public static String CONFIG_FILE_NAME = "lbconfig.props";
    private static String OVERRIDE_SINGLE_DB_PROP = "OVERRIDE_SINGLE_DB";
    private Hashtable<String, SQLConnectionInfo> sqlServers_;
    private HashSet<String> indexLocations_;
    private int max_value_set_cache;

	private String logLocation_;

    private boolean isNormEnabled_;
    private String normConfigFile_;

    // this one is static, because it may need to be read by the logger before
    // this class is loaded.
    private static boolean isDebugEnabled_ = true;

    private boolean isAPILoggingEnabled = false;
    private boolean isSQLLoggingEnabled = false;
    
    private boolean isMigrateOnStartupEnabled = false;

    private int maxConnectionsPerDB_;

    private int cacheSize_;
    private int iteratorIdleTime_;
    private int maxResultSize_;

    private String[] jarFileLocations_;
    private String autoLoadRegistryPath_;
    private String autoLoadIndexLocation_;
    private String autoLoadDBURL_;
    private boolean autoLoadSingleDBMode;
    private String autoLoadDBPrefix_;
    private String autoLoadDBParameters_;
    private String autoLoadDBDriver_;
    private String autoLoadDBUsername_;
    private String autoLoadDBPassword_;
    private String relativePathStart_;
    
    private String assertedValueSetVersion;
	private String assertedValueSetHierarchyVSRelation;
    private String assertedValueSetCodingSchemeName;
    private String assertedValueSetCodingSchemeURI;
    private String assertedValueSetCodingSchemeTag;
    
	private String graphdbUser;
    private String graphdbpwd;
    private String graphdbUrl;

    private boolean singleTableMode = true;
    private static String SINGLE_TABLE_MODE_PROP = "SINGLE_TABLE_MODE";
    private static boolean SINGLE_TABLE_MODE_DEFAULT = false;
    
    private static String LUCENE_SINGLE_INDEX_PROP = "LUCENE_SINGLE_INDEX";
    private static String LUCENE_SINGLE_INDEX_DEFAULT = "false";
    
    private boolean isSingleIndex = true;

    private String configFileLocation_;
    
    private boolean overrideSingleDbMode;
    
    private int luceneMaxClauseCount = 40000;
    private static String LUCENE_MAX_CLAUSE_COUNT_PROP = "LUCENE_MAX_CLAUSE_COUNT";
    
    private String primaryKeyStrategy;
    private static String PRIMARY_KEY_STRATEGY_PROP = "DB_PRIMARY_KEY_STRATEGY";
    private static String DEFAULT_PRIMARY_KEY_STRATEGY = "GUID";
    
    private String currentPersistenceScheme;
    private static String CURRENT_PERSISTENCE_SCHEME_PROP = "CURRENT_PERSISTENCE_SCHEME";
    private static String DEFAULT_PERSISTENCE_SCHEME = "2.0";

    private static String SOURCE_ASSERTED_VALUE_SET_VERSION = "SOURCE_ASSERTED_VALUE_SET_VERSION";
    private static String SOURCE_ASSERTED_VALUE_SET_VERSION_DEFAULT = "18.01e";
    
    private static String SOURCE_ASSERTED_VALUE_SET_HIERARCHY_VS_RELATION = "SOURCE_ASSERTED_VALUE_SET_HIERARCHY_VS_REATION";
    private static String SOURCE_ASSERTED_VALUE_SET_HIERARCHY_VS_RELATION_DEFAULT = "Concept_In_Subset";
    
    private static String SOURCE_ASSERTED_VALUE_SET_CODING_SCHEME_NAME = "SOURCE_ASSERTED_VALUE_SET_CODING_SCHEME_NAME";
    private static String SOURCE_ASSERTED_VALUE_SET_CODING_SCHEME_NAME_DEFAULT = "NCI Thesaurus";

    private static String SOURCE_ASSERTED_VALUE_SET_CODING_SCHEME_URI = "SOURCE_ASSERTED_VALUE_SET_CODING_SCHEME_URI";
    private static String SOURCE_ASSERTED_VALUE_SET_CODING_SCHEME_URI_DEFAULT = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#";

    private static String SOURCE_ASSERTED_VALUE_SET_CODING_SCHEME_TAG = "SOURCE_ASSERTED_VALUE_SET_CODING_SCHEME_TAG";
    private static String SOURCE_ASSERTED_VALUE_SET_CODING_SCHEME_TAG_DEFAULT = "PRODUCTION";

    private boolean emailErrors_ = false;
    private String SMTPServer_;
    private String emailTo_;

    private String logChange_;
    private int eraseLogsAfter_;

    private static boolean realDebugEnableValue_ = false;
    private static boolean isDebugOverridden_ = false;
    
    private String historyDBSchema_ = null;
    private String mysql_collation= null;


	private static String DEFAULT_MYSQL_COLLATION= "utf8_bin";

    /**
     * @return the eraseLogsAfter
     */
    public int getEraseLogsAfter() {
        return this.eraseLogsAfter_;
    }

    /**
     * @return the logChange
     */
    public String getLogChange() {
        return this.logChange_;
    }

    /**
     * @return the emailTo
     */
    public String getEmailTo() {
        return this.emailTo_;
    }

    /**
     * @return the emailErrors
     */
    public boolean emailErrors() {
        return this.emailErrors_;
    }

    /**
     * @return the sMTPServer Address.
     */
    public String getSMTPServer() {
        return this.SMTPServer_;
    }

    public static boolean isDebugEnabled() {
        return isDebugEnabled_;
    }

    public static void debugEnableOverride() {
        isDebugOverridden_ = true;
        isDebugEnabled_ = true;
    }

    public static void debugEnableOverrideRemove() {
        isDebugOverridden_ = false;
        isDebugEnabled_ = realDebugEnableValue_;
    }

    private Properties loadPropsFile(Logger logger) throws Exception {
        try {
            PropertiesUtility.systemVariable = LG_CONFIG_FILE_SYSTEM_VARIABLE;
            String location = PropertiesUtility.locatePropFile("config" + System.getProperty("file.separator")
                    + CONFIG_FILE_NAME, this.getClass().getName(), logger);
            
            //If in in-memory (testing) mode, we can allow this, as config will be handled elsewhere.
            //If not in in-memory testing mode, throw an error if the config fiel isn't found.
            boolean inMemory = 
                BooleanUtils.toBoolean(System.getProperty(ALL_IN_MEMORY_SYSTEM_VARIABLE));
           
            if(StringUtils.isBlank(location) && !inMemory){
            	throw new InitializationException(
            			"\n============================================" +
            			"\nError finding the LexBIG Configuration File." +
            			"\n============================================" +
            			"\nThe LexGrid system attempts to automatically locate this file in one of two ways:" +
            			"\n" +
            			"\nOPTION 1 - AutoSearch" +
            			"\nIt determines the folder that the LexGrid classes are located (either in" +
            			"\na jar file, or a folder containing class files).  For this example, lets assume" +
            			"\nthat the jar file containing LexGrid was found at 'C:\\LexGrid\\LexBIG\\lib\\lbRuntime.jar'" +
            			"\nThen the path it starts with will be 'C:\\LexGrid\\LexBIG\\lib\\'.  Lets call this" +
            			"\nlocation 'A'.  Starting from location A, it checks for the following sub-path:" + 
            			"\n'resources\\config\\config.props'.  Lets call this path 'B'.  If a file exists" + 
            			"\n'A\\B', the search is over.  If this file is not found - it goes up one directory" +
            			"\nfrom A, and checks for B again.  So, now it is checking \"A\\..\\B\". - which is" +
            			"\n'C:\\LexGrid\\LexBIG\\resources\\config\\config.props'.  This process continues until" + 
            			"\nit finds the file, reaches the root of the file system, or it has gone up 10 levels." +
            			"\nAt that point, it quits and and the startup fails." +
            			"\n" +
            			"\nOPTION 2 - System Variable" +
            			"\nYou may skip the auto search by setting the System Config variable 'LG_CONFIG_FILE'" +
            			"\nto the full absolute path of the config.props file." +  
            			"\nExample - if you were starting from the command line, you would add this parameter to" + 
            			"\nthe java command to set the 'System Property'" + 
            			"\n-DLG_CONFIG_FILE=\"C:\\LexGrid\\LexBIG\\resources\\config\\config.props\"");           			
            }
            
            Properties props = new Properties();

            logger.debug("Reading properties from " + location);
            props.load(new FileInputStream(new File(location)));
            props.put("CONFIG_FILE_LOCATION", location);
            return props;
        } catch (Exception e) {
            logger.fatal("There was a problem finding or reading the properties file", e);
            throw e;
        }
    }

    public SystemVariables(Logger logger) throws Exception {
        Properties props = loadPropsFile(logger);
        init(logger, props);
    }

    public SystemVariables(Logger logger, Properties props) throws Exception {
        init(logger, props);
    }

    /**
     * Inits the.
     * 
     * @param logger the logger
     * @param props the props
     * 
     * @throws Exception the exception
     */
    private void init(Logger logger, Properties props) throws Exception {
        // Read in the config file
        try {
            configFileLocation_ = props.getProperty("CONFIG_FILE_LOCATION");
            sqlServers_ = new Hashtable<String, SQLConnectionInfo>();
            indexLocations_ = new HashSet<String>();

            logger.debug("Reading registry, index location and db configuration information from the properties file");
            String relPathStart = System.getProperty("LG_BASE_PATH");
            if (relPathStart != null && relPathStart.length() > 0) {
                logger.debug("Relative Path root read from system variable '" + relPathStart + "'");
                relativePathStart_ = relPathStart;
                if (!isPathValid(relativePathStart_)) {
                    logger.error("You provided an invalid relative path root as a system variable.  Ignoring.");
                    relativePathStart_ = null;
                }
            }
            if (relativePathStart_ == null) {
                relPathStart = props.getProperty("LG_BASE_PATH");
                if (relPathStart != null && relPathStart.length() > 0) {
                    logger.debug("Relative Path root read from config file '" + relPathStart + "'");
                    relativePathStart_ = relPathStart;
                    if (!isPathValid(relativePathStart_)) {
                        logger.error("You provided an invalid relative path root in the config file.  Ignoring.");
                        relativePathStart_ = null;
                    }
                }
                if (relativePathStart_ == null) {
                    // default to the location of the config file.
                    relativePathStart_ = props.getProperty("CONFIG_FILE_LOCATION");
                    logger.debug("Relative Path root defaulted to " + CONFIG_FILE_NAME + " location '" + relativePathStart_ + "'");
                }
            }
            String tempJarFileLocation = getProperty(props, "JAR_FILE_LOCATION");
            StringTokenizer tokenizer = new StringTokenizer(tempJarFileLocation, ";");
            jarFileLocations_ = new String[tokenizer.countTokens()];
            int i = 0;
            while (tokenizer.hasMoreElements()) {
                jarFileLocations_[i++] = processRelativePath(tokenizer.nextToken());
            }
            autoLoadRegistryPath_ = processRelativePath(getProperty(props, "REGISTRY_FILE"));
            autoLoadIndexLocation_ = processRelativePath(getProperty(props, "INDEX_LOCATION"));
            autoLoadDBURL_ = getProperty(props, "DB_URL");
          
            if(getNullableProperty(props, "MAX_IN_VS_CACHE") == null){
            	max_value_set_cache = 500;
            }
            else{
            	max_value_set_cache = getStringPropertyAsInt(props, "MAX_IN_VS_CACHE");
            }
            String tempSingleDb = getNullableProperty(props, "SINGLE_DB_MODE");
            
            autoLoadSingleDBMode = getNullableBoolean(tempSingleDb, true);
          
            String tempOverrideSingleDb = getNullableProperty(props, OVERRIDE_SINGLE_DB_PROP);
             
            overrideSingleDbMode = getNullableBoolean(tempOverrideSingleDb, false);
            
            singleTableMode = getNullableBoolean(
                    getNullableProperty(props, SINGLE_TABLE_MODE_PROP), SINGLE_TABLE_MODE_DEFAULT);
            
            autoLoadDBPrefix_ = getProperty(props, "DB_PREFIX");

            // this one can be left out
            autoLoadDBParameters_ = props.getProperty("DB_PARAM");
            if (autoLoadDBParameters_ == null) {
                autoLoadDBParameters_ = "";
            } else {
                autoLoadDBURL_ = getAutoLoadDBURL() + getAutoLoadDBParameters();
            }
            autoLoadDBDriver_ = getProperty(props, "DB_DRIVER");
            autoLoadDBUsername_ = getProperty(props, "DB_USER");
            autoLoadDBPassword_ = getProperty(props, "DB_PASSWORD");
            
//        	graphdbUser = getProperty(props, "GRAPH_DB_USER");
//        	graphdbpwd = getProperty(props, "GRAPH_DB_PWD");
//            graphdbUrl = getProperty(props, "GRAPH_DB_PATH");

            
            mysql_collation= getNullableProperty(props, "MYSQL_COLLATION", DEFAULT_MYSQL_COLLATION);

            String pwdEncrypted = getNullableProperty(props, "DB_PASSWORD_ENCRYPTED");
            if( pwdEncrypted != null && pwdEncrypted.equalsIgnoreCase("true"))
                autoLoadDBPassword_ = CryptoUtility.decrypt(autoLoadDBPassword_);
            
            File temp = new File(autoLoadIndexLocation_);
            if (!temp.exists()) {
                temp.mkdir();
            }
            indexLocations_.add(autoLoadIndexLocation_);

            logger.debug("Reading the Preconfigured SQL Server configurations from the properties file");
            loadSqlServerLocations(props);

            logger.debug("Reading the Prebuilt Lucene index configurations from the properties file");
            loadIndexLocations(props);

            logger.debug("Reading additional variables from the properties file");

            logLocation_ = processRelativePath(getProperty(props, "LOG_FILE_LOCATION"));

            isNormEnabled_ = false;
            // This has been disabled due to deployment complexity. Can be
            // re-enabled if necessary.
            // isNormEnabled_ = new
            // Boolean(props.getProperty("NORMALIZED_QUERIES_ENABLED")).booleanValue();
            // if (isNormEnabled_)
            // {
            // normConfigFile_ =
            // props.getProperty("LVG_NORM_CONFIG_FILE_ABSOLUTE");
            // }

            realDebugEnableValue_ = new Boolean(getProperty(props, "DEBUG_ENABLED")).booleanValue();
            if (!isDebugOverridden_) {
                isDebugEnabled_ = realDebugEnableValue_;
            }
            logger.info("Logging debug messages" + (isDebugEnabled_ == true ? " left on." : " turned off."));
            logger.setDebugEnabled(isDebugEnabled_);
            
            isAPILoggingEnabled = new Boolean(getProperty(props, "API_LOG_ENABLED")).booleanValue();
            logger.setAPILoggingEnabled(isAPILoggingEnabled);
            
            isMigrateOnStartupEnabled = getNullableBoolean("MOVE_REGISTRY_TO_DATABASE", false);
            logger.setAPILoggingEnabled(isMigrateOnStartupEnabled);
            
            String val= props.getProperty("SQL_LOG_ENABLED"); 
            if (val != null) {
               isSQLLoggingEnabled = new Boolean(val).booleanValue();
            }
            logger.info("Logging sql messages" + (isSQLLoggingEnabled == true ? " left on." : " turned off."));
           

            try {
                maxConnectionsPerDB_ = Integer.parseInt(getProperty(props, "MAX_CONNECTIONS_PER_DB"));
            } catch (NumberFormatException e) {
                logger.error("INVALID VALUE in config file for maxConnectionsPerDB - defaulting to 8");
                maxConnectionsPerDB_ = 8;
            }

            try {
                iteratorIdleTime_ = Integer.parseInt(getProperty(props, "ITERATOR_IDLE_TIME"));
            } catch (NumberFormatException e) {
                logger.error("INVALID VALUE in config file for ITERATOR_IDLE_TIME - defaulting to 5");
                iteratorIdleTime_ = 5;
            }

            try {
                maxResultSize_ = Integer.parseInt(getProperty(props, "MAX_RESULT_SIZE"));
            } catch (NumberFormatException e) {
                logger.error("INVALID VALUE in config file for MAX_RESULT_SIZE - defaulting to 1000");
                maxResultSize_ = 1000;
            }

            try {
                cacheSize_ = Integer.parseInt(getProperty(props, "CACHE_SIZE"));
            } catch (NumberFormatException e) {
                logger.error("INVALID VALUE in config file for CACHE_SIZE - defaulting to 200");
                cacheSize_ = 200;
            }
            
            try {
                maxResultSize_ = Integer.parseInt(getProperty(props, "MAX_RESULT_SIZE"));
            } catch (NumberFormatException e) {
                logger.error("INVALID VALUE in config file for MAX_RESULT_SIZE - defaulting to 1000");
                maxResultSize_ = 1000;
            }

            String luceneMaxClauses = getNullableProperty(props, LUCENE_MAX_CLAUSE_COUNT_PROP);
            if(luceneMaxClauses != null){
                try {
                    this.luceneMaxClauseCount = Integer.parseInt(luceneMaxClauses);
                } catch (NumberFormatException e) {
                    logger.error("INVALID VALUE in config file for " + LUCENE_MAX_CLAUSE_COUNT_PROP);
                }
            }
            
            
            this.isSingleIndex = BooleanUtils.toBoolean(getNullableProperty(props, LUCENE_SINGLE_INDEX_PROP, LUCENE_SINGLE_INDEX_DEFAULT));
            
            this.primaryKeyStrategy = getNullableProperty(props, PRIMARY_KEY_STRATEGY_PROP, DEFAULT_PRIMARY_KEY_STRATEGY);
            
            this.currentPersistenceScheme = getNullableProperty(props, CURRENT_PERSISTENCE_SCHEME_PROP, DEFAULT_PERSISTENCE_SCHEME);
           
            emailErrors_ = new Boolean(getNullableProperty(props, "EMAIL_ERRORS", "false"));
            if (emailErrors_) {
                SMTPServer_ = getProperty(props, "SMTP_SERVER");
                emailTo_ = getProperty(props, "EMAIL_TO");
            }

            try {
                logChange_ = getProperty(props, "LOG_CHANGE");

                if (!logChange_.equals("daily") && !logChange_.equals("weekly") && !logChange_.equals("monthly")) {
                    Integer.parseInt(logChange_);
                }
            } catch (NumberFormatException e) {
                logger.error("INVALID VALUE in config file for LOG_CHANGE - defaulting to 5");
                logChange_ = "5";
            }

            try {
                eraseLogsAfter_ = Integer.parseInt(getProperty(props, "ERASE_LOGS_AFTER"));
            } catch (NumberFormatException e) {
                logger.error("INVALID VALUE in config file for ERASE_LOGS_AFTER - defaulting to 5");
                eraseLogsAfter_ = 5;
            }
            
            if( autoLoadSingleDBMode )
                historyDBSchema_ = props.getProperty("HISTORY_DB_SCHEMA");
            
            assertedValueSetVersion = getNullableProperty(props, SOURCE_ASSERTED_VALUE_SET_VERSION, SOURCE_ASSERTED_VALUE_SET_VERSION_DEFAULT);
            assertedValueSetHierarchyVSRelation = getNullableProperty(props, SOURCE_ASSERTED_VALUE_SET_HIERARCHY_VS_RELATION, SOURCE_ASSERTED_VALUE_SET_HIERARCHY_VS_RELATION_DEFAULT);
            assertedValueSetCodingSchemeName = getNullableProperty(props, SOURCE_ASSERTED_VALUE_SET_CODING_SCHEME_NAME, SOURCE_ASSERTED_VALUE_SET_CODING_SCHEME_NAME_DEFAULT);
            assertedValueSetCodingSchemeURI = getNullableProperty(props, SOURCE_ASSERTED_VALUE_SET_CODING_SCHEME_URI, SOURCE_ASSERTED_VALUE_SET_CODING_SCHEME_URI_DEFAULT);
            
            // if tag isn't set, then return null.
            assertedValueSetCodingSchemeTag = props.getProperty("SOURCE_ASSERTED_VALUE_SET_CODING_SCHEME_TAG");
            	           
            logger.finishLogConfig(this);

        } catch (Exception e) {
            logger.fatal("There was a problem reading the properties", e);
            throw e;
        }
    }

    public boolean isNormEnabled() {
        return isNormEnabled_;
    }

    public String getNormConfigFile() {
        return normConfigFile_;
    }

    public static String getMetaDataIndexName() {
        return "MetaDataIndex";
    }
    
	public static String getAssertedValueSetIndexName() {
		return "AssertedValueSetIndex";
	}

    public Hashtable<String, SQLConnectionInfo> getSqlServers() {
        return sqlServers_;
    }

    public HashSet<String> getIndexLocations() {
        return indexLocations_;
    }

    private void loadSqlServerLocations(Properties props) throws LBParameterException {
        for (int i = 0; i <= 20; i++) {
            String name = props.getProperty("SQL_" + i + "_NAME");
            {
                if (name != null && name.length() > 0) {
                    String url = getProperty(props, "SQL_" + i + "_URL");
                    String driver = getProperty(props, "SQL_" + i + "_DRIVER");
                    String username = getProperty(props, "SQL_" + i + "_USERNAME");
                    String password = getProperty(props, "SQL_" + i + "_PASSWORD");

                    SQLConnectionInfo server = new SQLConnectionInfo();
                    server.dbName = name;
                    server.server = url;
                    server.driver = driver;
                    server.username = username;
                    server.password = password;

                    sqlServers_.put(server.getKey(), server);
                }
            }
        }
    }

    private void loadIndexLocations(Properties props) throws LBParameterException {
        for (int i = 0; i <= 20; i++) {
            String location = props.getProperty("INDEX_" + i + "_LOCATION");
            {
                if (location != null && location.length() > 0) {
                    indexLocations_.add(location);
                }
            }
        }
    }

    public int getMaxConnectionsPerDB() {
        return this.maxConnectionsPerDB_;
    }

    public int getCacheSize() {
        return this.cacheSize_;
    }

    /**
     * @return the autoLoadIndexLocation
     */
    public String getAutoLoadIndexLocation() {
        return this.autoLoadIndexLocation_;
    }

    /**
     * @return the autoLoadDBDriver
     */
    public String getAutoLoadDBDriver() {
        return this.autoLoadDBDriver_;
    }

    /**
     * @return the autoLoadDBParameters
     */
    public String getAutoLoadDBParameters() {
        return this.autoLoadDBParameters_;
    }

    /**
     * @return the autoLoadDBPassword
     */
    public String getAutoLoadDBPassword() {
        return this.autoLoadDBPassword_;
    }

    public boolean getOverrideSingleDbMode(){
        return this.overrideSingleDbMode;
    }
    
    /**
     * @return the autoLoadDBPrefix
     */
    public String getAutoLoadDBPrefix() {
        return this.autoLoadDBPrefix_;
    }

    /**
     * @return the autoLoadDBURL
     */
    public String getAutoLoadDBURL() {
        return this.autoLoadDBURL_;
    }

    /**
     * @return the autoLoadDBUsername
     */
    public String getAutoLoadDBUsername() {
        return this.autoLoadDBUsername_;
    }

    /**
     * @return the autoLoadRegistryPath
     */
    public String getAutoLoadRegistryPath() {
        return this.autoLoadRegistryPath_;
    }

    /**
     * @return the logLocation
     */
    public String getLogLocation() {
        return this.logLocation_;
    }

    /**
     * If the configuration was loaded from a properties file - this will have
     * the path of the properties file.
     * 
     * @return
     */
    public String getConfigFileLocation() {
        return this.configFileLocation_;
    }

    /**
     * @return the iteratorIdleTime
     */
    public int getIteratorIdleTime() {
        return this.iteratorIdleTime_;
    }

    /**
     * @return the maxResultSize
     */
    public int getMaxResultSize() {
        return this.maxResultSize_;
    }
    
    public boolean isSingleTableMode(){
        return this.singleTableMode;
    }

    @Deprecated
    public boolean getAutoLoadSingleDBMode() {
        return autoLoadSingleDBMode;
    }
    
    public int getMax_value_set_cache() {
        return max_value_set_cache;
    }

    public String[] getJarFileLocations() {
        if (jarFileLocations_ == null || jarFileLocations_.length == 0) {
            // not in the config file, put in the default
            return new String[] { processRelativePath("../runtime") };
        } else {
            return jarFileLocations_;
        }
    }
    
    public String getHistoryDBSchema() {
        return historyDBSchema_;
    }

    private String processRelativePath(String path) {
        if (path != null && isPathRelative(path) && relativePathStart_ != null && relativePathStart_.length() > 0) {
            // If I know what the relativePathStart_ is - I want to reprocess
            // any relative paths
            // so that they are relative to the relativePathStart_, rather than
            // relative to the
            // JVM location.
            File temp = new File(relativePathStart_);
            if (temp.isFile()) {
                // if they hand me the file, get the parent folder.
                temp = temp.getParentFile();
            }
            // temp is now the folder that contains the config file.
            // take the path they gave, and put it together with the config file
            // base path

            File newFile = new File(temp.getAbsolutePath() + System.getProperty("file.separator") + path);
            return newFile.getAbsolutePath();
        }
        return path;
    }

    private boolean isPathRelative(String path) {
        if (path != null && path.length() > 0) {
            if (path.charAt(0) == '/' || path.charAt(0) == '\\') {
                // if it starts with / or \ - it is already an absolute path (on
                // unix)
                return false;
            }
            if (path.length() > 1 && path.charAt(1) == ':') {
                // if the second char is ':' - then it is an absolute path on
                // windows.
                return false;
            }
        }
        return true;
    }

    private boolean isPathValid(String path) {
        File temp = new File(path);
        return temp.exists();
    }

    private String getProperty(Properties props, String key) throws LBParameterException {
        String value = getNullableProperty(props, key);
        if (value == null) {
            throw new LBParameterException("The required variable '" + key
                    + "' is missing from your supplied properties file");
        }
        return StringUtils.trim(value);
    }
    
    private String getNullableProperty(Properties props, String key) throws LBParameterException {
        return props.getProperty(key); 
    }
    
	private int getStringPropertyAsInt(final Properties props, final String key) throws NumberFormatException, LBParameterException{
    	return Integer.valueOf(getProperty(props, key));
    }
    
    private String getNullableProperty(Properties props, String key, String defaultValue) throws LBParameterException {
        String value = props.getProperty(key);
        if(StringUtils.isBlank(value)) {
        	return defaultValue;
        } else {
        	return StringUtils.trim(value);
        }
    }
    
    private boolean getNullableBoolean(String value, boolean valueIfNull){
        if (value != null) {
            return Boolean.parseBoolean(value);
        } else {
           return valueIfNull;
        }
    }

    /**
     * @return the isAPILoggingEnabled
     */
    public boolean isAPILoggingEnabled() {
        return this.isAPILoggingEnabled;
    }
    
    public boolean isSQLLoggingEnabled() {
        return isSQLLoggingEnabled;
    }

    public boolean isMigrateOnStartupEnabled() {
        return this.isMigrateOnStartupEnabled;
    }
    
    public void setSQLLoggingEnabled(boolean isSQLLoggingEnabled) {
        this.isSQLLoggingEnabled = isSQLLoggingEnabled;
    }

    public int getLuceneMaxClauseCount() {
        return luceneMaxClauseCount;
    }

	public void setPrimaryKeyStrategy(String primaryKeyStrategy) {
		this.primaryKeyStrategy = primaryKeyStrategy;
	}

	public String getPrimaryKeyStrategy() {
		return primaryKeyStrategy;
	}
	
	public String getCurrentPersistenceScheme() {
		return this.currentPersistenceScheme;
	}

	public boolean getIsSingleIndex() {
		return this.isSingleIndex;
	}
	
    public String getMysql_collation() {
		return mysql_collation;
	}

	public void setMysql_collation(String mysqlCollation) {
		mysql_collation = mysqlCollation;
	}
	
	
    public String getGraphdbUser() {
		return graphdbUser;
	}

	public void setGraphdbUser(String graphdbUser) {
		this.graphdbUser = graphdbUser;
	}

	public String getGraphdbpwd() {
		return graphdbpwd;
	}

	public void setGraphdbpwd(String graphdbpwd) {
		this.graphdbpwd = graphdbpwd;
	}

	public String getGraphdbUrl() {
		return graphdbUrl;
	}

	public void setGraphdbUrl(String graphdbUrl) {
		this.graphdbUrl = graphdbUrl;
	}
	
	public void setAssertedValueSetVersion(String assertedValueSetVersion) {
		this.assertedValueSetVersion = assertedValueSetVersion;
	}
	
	public String getAssertedValueSetVersion() {
		return assertedValueSetVersion;
	}
	
	public String getAssertedValueSetHierarchyVSRelation() {
		return assertedValueSetHierarchyVSRelation;
	}

	public void setAssertedValueSetHierarchyVSRelation(String assertedValueSetHierarchyVSRelation) {
		this.assertedValueSetHierarchyVSRelation = assertedValueSetHierarchyVSRelation;
	}

	public String getAssertedValueSetCodingSchemeName() {
		return assertedValueSetCodingSchemeName;
	}

	public void setAssertedValueSetCodingSchemeName(String assertedValueSetCodingSchemeName) {
		this.assertedValueSetCodingSchemeName = assertedValueSetCodingSchemeName;
	}

	public String getAssertedValueSetCodingSchemeURI() {
		return assertedValueSetCodingSchemeURI;
	}

	public void setAssertedValueSetCodingSchemeURI(String assertedValueSetCodingSchemeURI) {
		this.assertedValueSetCodingSchemeURI = assertedValueSetCodingSchemeURI;
	}
	
	public String getAssertedValueSetCodingSchemeTag() {
		return assertedValueSetCodingSchemeTag;
	}

	public void setAssertedValueSetCodingSchemeTag(String assertedValueSetCodingSchemeTag) {
		this.assertedValueSetCodingSchemeTag = assertedValueSetCodingSchemeTag;
	}

	
    public static String getAbsoluteIndexLocation(){
    	String location =  PropertiesUtility.locatePropFile("config" + System.getProperty("file.separator")
        + CONFIG_FILE_NAME, SystemVariables.class.getName());
    	File newFile = new File(location);
    	File tempFile = new File(newFile.getParent());
    	tempFile = new File(tempFile.getParent());
    	tempFile = new File(tempFile, "lbIndex");
    	return tempFile.getAbsolutePath();
    }
    

}
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
package org.lexevs.system.constants;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.util.config.PropertiesUtility;
import org.lexevs.dao.database.connection.SQLConnectionInfo;
import org.lexevs.logging.Logger;
import org.lexevs.system.utility.CryptoUtility;

/**
 * This class reads and provides access to values specified in the configuration
 * file.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class SystemVariables {
    
    /** The CONFI g_ fil e_ name. */
    public static String CONFIG_FILE_NAME = "lbconfig.props";
    
    /** The OVERRID e_ singl e_ d b_ prop. */
    private static String OVERRIDE_SINGLE_DB_PROP = "OVERRIDE_SINGLE_DB";
    
    /** The sql servers_. */
    private Hashtable<String, SQLConnectionInfo> sqlServers_;
    
    /** The index locations_. */
    private HashSet<String> indexLocations_;

    /** The log location_. */
    private String logLocation_;

    /** The is norm enabled_. */
    private boolean isNormEnabled_;
    
    /** The norm config file_. */
    private String normConfigFile_;

    // this one is static, because it may need to be read by the logger before
    // this class is loaded.
    /** The is debug enabled_. */
    private static boolean isDebugEnabled_ = true;

    /** The is api logging enabled. */
    private boolean isAPILoggingEnabled = false;
    
    /** The is sql logging enabled. */
    private boolean isSQLLoggingEnabled = false;

    /** The max connections per d b_. */
    private int maxConnectionsPerDB_;

    /** The cache size_. */
    private int cacheSize_;
    
    /** The iterator idle time_. */
    private int iteratorIdleTime_;
    
    /** The max result size_. */
    private int maxResultSize_;

    /** The jar file locations_. */
    private String[] jarFileLocations_;
    
    /** The auto load registry path_. */
    private String autoLoadRegistryPath_;
    
    /** The auto load index location_. */
    private String autoLoadIndexLocation_;
    
    /** The auto load dbur l_. */
    private String autoLoadDBURL_;
    
    /** The auto load single db mode. */
    private boolean autoLoadSingleDBMode;
    
    /** The auto load db prefix_. */
    private String autoLoadDBPrefix_;
    
    /** The auto load db parameters_. */
    private String autoLoadDBParameters_;
    
    /** The auto load db driver_. */
    private String autoLoadDBDriver_;
    
    /** The auto load db username_. */
    private String autoLoadDBUsername_;
    
    /** The auto load db password_. */
    private String autoLoadDBPassword_;
    
    /** The relative path start_. */
    private String relativePathStart_;
    
    /** The single table mode. */
    private boolean singleTableMode = true;
    
    /** The SINGL e_ tabl e_ mod e_ prop. */
    private static String SINGLE_TABLE_MODE_PROP = "SINGLE_TABLE_MODE";

    /** The config file location_. */
    private String configFileLocation_;
    
    /** The override single db mode. */
    private boolean overrideSingleDbMode;
    
    /** The lucene max clause count. */
    private int luceneMaxClauseCount = 40000;
    
    /** The LUCEN e_ ma x_ claus e_ coun t_ prop. */
    private static String LUCENE_MAX_CLAUSE_COUNT_PROP = "LUCENE_MAX_CLAUSE_COUNT";

    /** The email errors_. */
    private boolean emailErrors_ = false;
    
    /** The SMTP server_. */
    private String SMTPServer_;
    
    /** The email to_. */
    private String emailTo_;

    /** The log change_. */
    private String logChange_;
    
    /** The erase logs after_. */
    private int eraseLogsAfter_;

    /** The real debug enable value_. */
    private static boolean realDebugEnableValue_ = false;
    
    /** The is debug overridden_. */
    private static boolean isDebugOverridden_ = false;
    
    /** The history db schema_. */
    private String historyDBSchema_ = null;

    /**
     * Gets the erase logs after.
     * 
     * @return the eraseLogsAfter
     */
    public int getEraseLogsAfter() {
        return this.eraseLogsAfter_;
    }

    /**
     * Gets the log change.
     * 
     * @return the logChange
     */
    public String getLogChange() {
        return this.logChange_;
    }

    /**
     * Gets the email to.
     * 
     * @return the emailTo
     */
    public String getEmailTo() {
        return this.emailTo_;
    }

    /**
     * Email errors.
     * 
     * @return the emailErrors
     */
    public boolean emailErrors() {
        return this.emailErrors_;
    }

    /**
     * Gets the smtp server.
     * 
     * @return the sMTPServer Address.
     */
    public String getSMTPServer() {
        return this.SMTPServer_;
    }

    /**
     * Checks if is debug enabled.
     * 
     * @return true, if is debug enabled
     */
    public static boolean isDebugEnabled() {
        return isDebugEnabled_;
    }

    /**
     * Debug enable override.
     */
    public static void debugEnableOverride() {
        isDebugOverridden_ = true;
        isDebugEnabled_ = true;
    }

    /**
     * Debug enable override remove.
     */
    public static void debugEnableOverrideRemove() {
        isDebugOverridden_ = false;
        isDebugEnabled_ = realDebugEnableValue_;
    }

    /**
     * Load props file.
     * 
     * @param logger the logger
     * 
     * @return the properties
     * 
     * @throws Exception the exception
     */
    private Properties loadPropsFile(Logger logger) throws Exception {
        try {
            PropertiesUtility.systemVariable = "LG_CONFIG_FILE";
            String location = PropertiesUtility.locatePropFile("config" + System.getProperty("file.separator")
                    + CONFIG_FILE_NAME, this.getClass().getName());
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

    /**
     * Instantiates a new system variables.
     * 
     * @param logger the logger
     * 
     * @throws Exception the exception
     */
    public SystemVariables(Logger logger) throws Exception {
        Properties props = loadPropsFile(logger);
        init(logger, props);
    }

    /**
     * Instantiates a new system variables.
     * 
     * @param logger the logger
     * @param props the props
     * 
     * @throws Exception the exception
     */
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
            
            String tempSingleDb = getNullableProperty(props, "SINGLE_DB_MODE");
            
            autoLoadSingleDBMode = getNullableBoolean(tempSingleDb, true);
          
            String tempOverrideSingleDb = getNullableProperty(props, OVERRIDE_SINGLE_DB_PROP);
             
            overrideSingleDbMode = getNullableBoolean(tempOverrideSingleDb, false);
            
            singleTableMode = getNullableBoolean(
                    getNullableProperty(props, SINGLE_TABLE_MODE_PROP), true);
            
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
            

            String luceneMaxClauses = getNullableProperty(props, LUCENE_MAX_CLAUSE_COUNT_PROP);
            if(luceneMaxClauses != null){
                try {
                    this.luceneMaxClauseCount = Integer.parseInt(luceneMaxClauses);
                } catch (NumberFormatException e) {
                    logger.error("INVALID VALUE in config file for " + LUCENE_MAX_CLAUSE_COUNT_PROP);
                }
            }


            emailErrors_ = new Boolean(getProperty(props, "EMAIL_ERRORS")).booleanValue();
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
            
            logger.finishLogConfig(this);

        } catch (Exception e) {
            logger.fatal("There was a problem reading the properties", e);
            throw e;
        }
    }

    /**
     * Checks if is norm enabled.
     * 
     * @return true, if is norm enabled
     */
    public boolean isNormEnabled() {
        return isNormEnabled_;
    }

    /**
     * Gets the norm config file.
     * 
     * @return the norm config file
     */
    public String getNormConfigFile() {
        return normConfigFile_;
    }

    /**
     * Gets the meta data index name.
     * 
     * @return the meta data index name
     */
    public static String getMetaDataIndexName() {
        return "MetaDataIndex";
    }

    /**
     * Gets the sql servers.
     * 
     * @return the sql servers
     */
    public Hashtable<String, SQLConnectionInfo> getSqlServers() {
        return sqlServers_;
    }

    /**
     * Gets the index locations.
     * 
     * @return the index locations
     */
    public HashSet<String> getIndexLocations() {
        return indexLocations_;
    }

    /**
     * Load sql server locations.
     * 
     * @param props the props
     * 
     * @throws LBParameterException the LB parameter exception
     */
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

    /**
     * Load index locations.
     * 
     * @param props the props
     * 
     * @throws LBParameterException the LB parameter exception
     */
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

    /**
     * Gets the max connections per db.
     * 
     * @return the max connections per db
     */
    public int getMaxConnectionsPerDB() {
        return this.maxConnectionsPerDB_;
    }

    /**
     * Gets the cache size.
     * 
     * @return the cache size
     */
    public int getCacheSize() {
        return this.cacheSize_;
    }

    /**
     * Gets the auto load index location.
     * 
     * @return the autoLoadIndexLocation
     */
    public String getAutoLoadIndexLocation() {
        return this.autoLoadIndexLocation_;
    }

    /**
     * Gets the auto load db driver.
     * 
     * @return the autoLoadDBDriver
     */
    public String getAutoLoadDBDriver() {
        return this.autoLoadDBDriver_;
    }

    /**
     * Gets the auto load db parameters.
     * 
     * @return the autoLoadDBParameters
     */
    public String getAutoLoadDBParameters() {
        return this.autoLoadDBParameters_;
    }

    /**
     * Gets the auto load db password.
     * 
     * @return the autoLoadDBPassword
     */
    public String getAutoLoadDBPassword() {
        return this.autoLoadDBPassword_;
    }

    /**
     * Gets the override single db mode.
     * 
     * @return the override single db mode
     */
    public boolean getOverrideSingleDbMode(){
        return this.overrideSingleDbMode;
    }
    
    /**
     * Gets the auto load db prefix.
     * 
     * @return the autoLoadDBPrefix
     */
    public String getAutoLoadDBPrefix() {
        return this.autoLoadDBPrefix_;
    }

    /**
     * Gets the auto load dburl.
     * 
     * @return the autoLoadDBURL
     */
    public String getAutoLoadDBURL() {
        return this.autoLoadDBURL_;
    }

    /**
     * Gets the auto load db username.
     * 
     * @return the autoLoadDBUsername
     */
    public String getAutoLoadDBUsername() {
        return this.autoLoadDBUsername_;
    }

    /**
     * Gets the auto load registry path.
     * 
     * @return the autoLoadRegistryPath
     */
    public String getAutoLoadRegistryPath() {
        return this.autoLoadRegistryPath_;
    }

    /**
     * Gets the log location.
     * 
     * @return the logLocation
     */
    public String getLogLocation() {
        return this.logLocation_;
    }

    /**
     * If the configuration was loaded from a properties file - this will have
     * the path of the properties file.
     * 
     * @return the config file location
     */
    public String getConfigFileLocation() {
        return this.configFileLocation_;
    }

    /**
     * Gets the iterator idle time.
     * 
     * @return the iteratorIdleTime
     */
    public int getIteratorIdleTime() {
        return this.iteratorIdleTime_;
    }

    /**
     * Gets the max result size.
     * 
     * @return the maxResultSize
     */
    public int getMaxResultSize() {
        return this.maxResultSize_;
    }
    
    /**
     * Checks if is single table mode.
     * 
     * @return true, if is single table mode
     */
    public boolean isSingleTableMode(){
        return this.singleTableMode;
    }

    /**
     * Gets the auto load single db mode.
     * 
     * @return the auto load single db mode
     */
    @Deprecated
    public boolean getAutoLoadSingleDBMode() {
        return autoLoadSingleDBMode;
    }

    /**
     * Gets the jar file locations.
     * 
     * @return the jar file locations
     */
    public String[] getJarFileLocations() {
        if (jarFileLocations_ == null || jarFileLocations_.length == 0) {
            // not in the config file, put in the default
            return new String[] { processRelativePath("../runtime") };
        } else {
            return jarFileLocations_;
        }
    }
    
    /**
     * Gets the history db schema.
     * 
     * @return the history db schema
     */
    public String getHistoryDBSchema() {
        return historyDBSchema_;
    }

    /**
     * Process relative path.
     * 
     * @param path the path
     * 
     * @return the string
     */
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

    /**
     * Checks if is path relative.
     * 
     * @param path the path
     * 
     * @return true, if is path relative
     */
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

    /**
     * Checks if is path valid.
     * 
     * @param path the path
     * 
     * @return true, if is path valid
     */
    private boolean isPathValid(String path) {
        File temp = new File(path);
        return temp.exists();
    }

    /**
     * Gets the property.
     * 
     * @param props the props
     * @param key the key
     * 
     * @return the property
     * 
     * @throws LBParameterException the LB parameter exception
     */
    private String getProperty(Properties props, String key) throws LBParameterException {
        String value = getNullableProperty(props, key);
        if (value == null) {
            throw new LBParameterException("The required variable '" + key
                    + "' is missing from your supplied properties file");
        }
        return value;
    }
    
    /**
     * Gets the nullable property.
     * 
     * @param props the props
     * @param key the key
     * 
     * @return the nullable property
     * 
     * @throws LBParameterException the LB parameter exception
     */
    private String getNullableProperty(Properties props, String key) throws LBParameterException {
        return props.getProperty(key); 
    }
    
    /**
     * Gets the nullable boolean.
     * 
     * @param value the value
     * @param valueIfNull the value if null
     * 
     * @return the nullable boolean
     */
    private boolean getNullableBoolean(String value, boolean valueIfNull){
        if (value != null) {
            return Boolean.parseBoolean(value);
        } else {
           return valueIfNull;
        }
    }

    /**
     * Checks if is api logging enabled.
     * 
     * @return the isAPILoggingEnabled
     */
    public boolean isAPILoggingEnabled() {
        return this.isAPILoggingEnabled;
    }
    
    /**
     * Checks if is sQL logging enabled.
     * 
     * @return true, if is sQL logging enabled
     */
    public boolean isSQLLoggingEnabled() {
        return isSQLLoggingEnabled;
    }

    /**
     * Sets the sQL logging enabled.
     * 
     * @param isSQLLoggingEnabled the new sQL logging enabled
     */
    public void setSQLLoggingEnabled(boolean isSQLLoggingEnabled) {
        this.isSQLLoggingEnabled = isSQLLoggingEnabled;
    }

    /**
     * Gets the lucene max clause count.
     * 
     * @return the lucene max clause count
     */
    public int getLuceneMaxClauseCount() {
        return luceneMaxClauseCount;
    }
}

package org.LexGrid.LexBIG.Impl.testUtility;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.test.BaseInMemoryLexEvsTest;
import org.lexevs.system.ResourceManager;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.utility.PropertiesUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

/**
 * Singleton class for getting a LexBIGService for the JUnit test cases.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class ServiceHolder {
	
    private static Properties[] configs_;
    private static int currentConfig_ = 0;
    private static ArrayList<TestServerConfig> serverConfigs;
    private static ServiceHolder sh_;
    public static File testConfigFolder_;
    private boolean singleConfigMode_;

    private LexBIGService lbsi_;

    /**
     * Use this to get an instance of the ServiceHolder. If
     * 'configureForSingleConfig' has not been called, this will run the tests
     * on all configured databases in the testConfig.props file.
     * 
     * @return
     */
    public static ServiceHolder instance() {
        if (sh_ == null) {
            sh_ = new ServiceHolder(false);
        }
        return sh_;

    }

    /**
     * Use this to set up the tests for end user enviroment validation. Only
     * runs the tests once, using their normal config file.
     */
    public static void configureForSingleConfig() {

        sh_ = new ServiceHolder(false);

    }

    public void configureNext() throws LBException {
        ResourceManager.reInit(configs_[currentConfig_++]);
        lbsi_ = LexBIGServiceImpl.defaultInstance();
    }

    public ArrayList<TestServerConfig> getServerConfigs() {
        return serverConfigs;
    }

    public void removeCurrentConfigTestFolder() {
        String path = (String) configs_[0].get("REGISTRY_FILE");
        File temp = new File(path).getParentFile();
        deleteFolder(temp);
        temp.mkdir();
    }

    public void removeTestFolder() {
        deleteFolder(testConfigFolder_);
    }

    private ServiceHolder(boolean multiConfig) {

        try {
            if (multiConfig) {
                singleConfigMode_ = false;
                testConfigFolder_ = new File(System.getProperty("java.io.tmpdir")
                        + System.getProperty("file.separator") + "LexBIGTest-" + UUID.randomUUID().toString());

                // get rid of whatever is there.
                deleteFolder(testConfigFolder_);

                testConfigFolder_.mkdir();

                serverConfigs = TestServerConfigReader.getServerConfigs();

                configs_ = new Properties[serverConfigs.size()];

                for (int i = 0; i < serverConfigs.size(); i++) {
                    configs_[i] = createPropertiesObject(serverConfigs.get(i), i, testConfigFolder_);
                }

                if (configs_.length > 0) {
                    // clear the log files from the last run.
                    File temp = new File(configs_[0].getProperty("LOG_FILE_LOCATION"), "LexBIG_full_log.txt");
                    temp.delete();
                    temp = new File(configs_[0].getProperty("LOG_FILE_LOCATION"), "LexBIG_load_log.txt");
                    temp.delete();
                }
            } else {
                singleConfigMode_ = true;
              
                boolean inMemory = 
                	BooleanUtils.toBoolean(System.getProperty(SystemVariables.ALL_IN_MEMORY_SYSTEM_VARIABLE));
                
                if(inMemory){
                	BaseInMemoryLexEvsTest.initInMemory();
                }

                if(StringUtils.isNotBlank(System.getProperty(LexBIGServiceTestFactory.LBS_TEST_FACTORY_ENV))) {
                    lbsi_ = ((LexBIGServiceTestFactory) Class.forName(System.getProperty(LexBIGServiceTestFactory.LBS_TEST_FACTORY_ENV)).newInstance()).getLbs();
                } else {
                    lbsi_ = LexBIGServiceImpl.defaultInstance();
                }
            }

        } catch (Exception e) {
            System.err.println("Problem reading Test config file");
            e.printStackTrace();
            System.exit(-1);
        }

    }

    public LexBIGService getLexBIGService() {
        return lbsi_;
    }

    public boolean getSingleConfigMode() {
        return singleConfigMode_;
    }

    public static Properties createPropertiesObject(TestServerConfig serverInfo, int i, File configFolder) {
        Properties props = new Properties();

        // put these in the default log folder (but in a subfolder)
        File logFolder = new File("logs");
        if (!logFolder.exists()) {
            logFolder.mkdir();
        }

        logFolder = new File(logFolder, "JUnit");
        if (!logFolder.exists()) {
            logFolder.mkdir();
        }

        File indexFolder = new File(configFolder, "index" + i);
        indexFolder.mkdir();

        File confFolder = new File(configFolder, "conf");
        confFolder.mkdir();

        props.setProperty("NORMALIZED_QUERIES_ENABLED", "false");
        props.setProperty("LVG_NORM_CONFIG_FILE_ABSOLUTE", "");
        props.setProperty("DEBUG_ENABLED", "false");
        props.setProperty("MAX_CONNECTIONS_PER_DB", "8");
        props.setProperty("CACHE_SIZE", "500");
        props.setProperty("ITERATOR_IDLE_TIME", "5");
        props.setProperty("MAX_RESULT_SIZE", "1000");
        props.setProperty("JAR_FILE_LOCATION", "../../../lgSharedLibraries;../../extLib"); // assumes
                                                                                           // we
                                                                                           // are
                                                                                           // runnign
                                                                                           // in
                                                                                           // the
                                                                                           // eclipse
                                                                                           // workspace.
        PropertiesUtility.systemVariable = "LG_CONFIG_FILE";
        props.setProperty("CONFIG_FILE_LOCATION", PropertiesUtility.locatePropFile("config"
                + System.getProperty("file.separator") + SystemVariables.CONFIG_FILE_NAME , ServiceHolder.class.getName())); // assumes
                                                                                                          // we
                                                                                                          // are
                                                                                                          // running
                                                                                                          // in
                                                                                                          // the
                                                                                                          // eclipse
                                                                                                          // workspace
        props.setProperty("LOG_FILE_LOCATION", logFolder.getAbsolutePath());
        props.setProperty("REGISTRY_FILE", new File(confFolder, "registry.xml").getAbsolutePath());
        props.setProperty("INDEX_LOCATION", indexFolder.getAbsolutePath());
        props.setProperty("LOG_CHANGE", "5");
        props.setProperty("ERASE_LOGS_AFTER", "5");
        props.setProperty("EMAIL_ERRORS", "false");
        props.setProperty("API_LOG_ENABLED", "false");

        if (serverInfo.singleDBMode) {
            props.setProperty("SINGLE_DB_MODE", "true");

        } else {
            props.setProperty("SINGLE_DB_MODE", "false");
        }

        props.setProperty("DB_URL", serverInfo.url);
        props.setProperty("DB_DRIVER", serverInfo.driver);
        props.setProperty("DB_PREFIX", serverInfo.prefix == null ? "" : serverInfo.prefix);
        props.setProperty("DB_PARAM", serverInfo.param == null ? "" : serverInfo.param);
        props.setProperty("DB_USER", serverInfo.username == null ? "" : serverInfo.username);
        props.setProperty("DB_PASSWORD", serverInfo.password == null ? "" : serverInfo.password);
        
        if(serverInfo.overrideSingleDBMode){
            props.setProperty("OVERRIDE_SINGLE_DB", "true");
        }

        return props;
    }

    private static void deleteFolder(File file) {
        if (file.isDirectory()) {
            File[] temp = file.listFiles();
            for (int i = 0; i < temp.length; i++) {
                deleteFolder(temp[i]);
            }
        }
        file.delete();
    }
}
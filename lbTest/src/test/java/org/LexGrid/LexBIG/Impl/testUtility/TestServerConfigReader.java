
package org.LexGrid.LexBIG.Impl.testUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.lexevs.system.utility.PropertiesUtility;

/**
 * This class reads and provides access to values specified in the configuration
 * file.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class TestServerConfigReader {
    private static Properties loadPropsFile() throws FileNotFoundException, IOException {
        PropertiesUtility.systemVariable = "LG_CONFIG_FILE";
        String location = PropertiesUtility.locatePropFile("resources/config" + System.getProperty("file.separator")
                + "lbConfig.props", TestServerConfigReader.class.getName());
//        String location = "C:/workspaceLexEVS/lexevs-dao/resources/config/lbconfig.props";
        Properties props = new Properties();

        props.load(new FileInputStream(new File(location)));
        return props;

    }

    public static ArrayList<TestServerConfig> getServerConfigs() throws FileNotFoundException, IOException {
        Properties props = loadPropsFile();
        return load(props);
    }

    private static ArrayList<TestServerConfig> load(Properties props) {
        // Read in the config file
        ArrayList<TestServerConfig> serverConfigs = new ArrayList<TestServerConfig>();
        for (int i = 0; i < 30; i++) {
            if (props.get(i + "_DB_URL") != null) {
                TestServerConfig temp = new TestServerConfig();
                temp.url = props.getProperty(i + "_DB_URL");
                temp.driver = props.getProperty(i + "_DB_DRIVER");
                temp.username = props.getProperty(i + "_DB_USER");
                temp.password = props.getProperty(i + "_DB_PASSWORD");
                temp.singleDBMode = new Boolean(props.getProperty(i + "_SINGLE_DB_MODE"));
                temp.prefix = props.getProperty(i + "_DB_PREFIX");
                temp.param = props.getProperty(i + "_DB_PARAM");
                temp.overrideSingleDBMode = new Boolean(props.getProperty(i + "_OVERRIDE_SINGLE_DB"));
                serverConfigs.add(temp);
            }
        }
        return serverConfigs;

    }
}
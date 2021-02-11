
package org.lexevs.system.utility;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Class to aid in configuring log4j
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
public class Log4JUtility {
    /**
     * Configure log4j with the a config file specified by a value in a
     * properties object.
     * 
     * If the log4j file path specified in the properties object is a relative
     * (or short) path, this method will prepend the value of
     * 'propertiesFileParentFolder' to the path first.
     * 
     * The method PropertiesUtility.loadPropertiesFromFileOrURL will assign the
     * necessary information automatically.
     * 
     * It is recommended that the Properties object that you pass into this
     * class be created with the PropertiesUtility.loadPropertiesFromFileOrURL
     * method.
     * 
     * @param properties
     *            The properties object to look in for the file name of the
     *            log4j config file.
     * @param log4JConfigFilePropertyName
     *            The property name in the properties object that has a
     *            corresponding value with the path of the log4j config file.
     * @param setLogOutputBaseDirToPropertiesPath
     *            Should the log4j config file be modifed so that all appenders
     *            that end with "File" have the base directory of the location
     *            of the properties file prepended to them? If true - this also
     *            requires having the 'propertiesFileParentFolder' value set.
     * 
     * @throws Exception
     */
    public static void configureLog4JFromPathSpecifiedInProperties(Properties properties,
            String log4JConfigFilePropertyName, boolean setLogOutputBaseDirToPropertiesPath) throws Exception {
        String location = properties.getProperty(log4JConfigFilePropertyName);
        if (location == null || location.length() == 0) {
            throw new Exception("No value specified for '" + log4JConfigFilePropertyName
                    + "' in the properties object.");
        }

        String propertiesFolder = properties.getProperty(PropertiesUtility.propertiesParentFolderKey);
        if (propertiesFolder != null && propertiesFolder.length() > 0) {
            char lastChar = propertiesFolder.charAt(propertiesFolder.length() - 1);
            if (lastChar != '/' && lastChar != '\\') {
                propertiesFolder += System.getProperty("file.separator");
            }
        } else {
            propertiesFolder = "";
        }

        try {
            // if it is an absolute path, this will work.
            if (setLogOutputBaseDirToPropertiesPath) {
                configureLog4j(location, propertiesFolder);
            } else {
                configureLog4j(location);
            }
        } catch (IOException e) {
            // It will fail if it is a relative path - prefix on the parent path
            if (setLogOutputBaseDirToPropertiesPath) {
                configureLog4j(propertiesFolder + location, propertiesFolder);
            } else {
                configureLog4j(propertiesFolder + location);
            }

        }
    }

    /**
     * Method to read in a log4j config file, modify every property that ends
     * with "File" such that the baseFilePath value is prefixed onto it, and
     * then configure log4j with the results.
     * 
     * Useful for making log4j configuration work with relative file paths.
     * 
     * @param log4JConfigFile
     *            The file (or URL) to read the log4j configuration from
     * @param baseFilePath
     *            The path to prefix onto all values that end with "File" in the
     *            log4j config file.
     * @throws IOException
     */
    public static void configureLog4j(String log4JConfigFile, String baseLogOutputDirectory) throws IOException {
        Properties log4JProps;
        try {
            log4JProps = PropertiesUtility.loadPropertiesFromFileOrURL(log4JConfigFile);
        } catch (IOException e) {
            // try looking on the classpath
            log4JProps = PropertiesUtility.loadPropertiesFromClasspath(log4JConfigFile);
        }
        makeFileValsAbsolute(log4JProps, baseLogOutputDirectory);
        configureLog4j(log4JProps);
    }

    /**
     * Configure log4j from a config file.
     * 
     * @param log4JConfigFile
     *            File (or URL) to use to configure log4j.
     * @throws IOException
     */
    public static void configureLog4j(String log4JConfigFile) throws IOException {
        Properties log4JProps;
        try {
            log4JProps = PropertiesUtility.loadPropertiesFromFileOrURL(log4JConfigFile);
        } catch (IOException e) {
            // try looking on the classpath
            log4JProps = PropertiesUtility.loadPropertiesFromClasspath(log4JConfigFile);
        }
        configureLog4j(log4JProps);
    }

    /**
     * Configure log4j from a properties object.
     * 
     * @param props
     *            The log4j properties
     */
    public static void configureLog4j(Properties props) {
        org.apache.log4j.PropertyConfigurator.configure(props);
    }

    /**
     * Configure log4j from a properties object.
     * 
     * Modifies every property that ends with "File" such that the baseFilePath
     * value is prefixed onto it, and then configure log4j with the results.
     * 
     * Useful for making log4j configuration work with relative file paths.
     * 
     * @param props
     *            properties to load.
     * @param baseLogOutputDirectory
     *            directory path to prefix onto items ending with "File"
     */
    public static void configureLog4j(Properties props, String baseLogOutputDirectory) {
        makeFileValsAbsolute(props, baseLogOutputDirectory);
        org.apache.log4j.PropertyConfigurator.configure(props);
    }

    private static void makeFileValsAbsolute(Properties props, String baseDirectory) {
        if (baseDirectory != null && baseDirectory.length() > 0) {
            char lastChar = baseDirectory.charAt(baseDirectory.length() - 1);
            if (lastChar != '/' && lastChar != '\\') {
                baseDirectory += System.getProperty("file.separator");
            }

            // do this to make sure the base directory exists - isn't a URL.
            File temp = new File(baseDirectory);
            if (temp.exists()) {
                Enumeration enumeration = props.keys();
                while (enumeration.hasMoreElements()) {
                    String key = (String) enumeration.nextElement();
                    if (key.endsWith("File")) {
                        String currentValue = (String) props.get(key);
                        props.setProperty(key, baseDirectory + currentValue);
                    }
                }
            }
        }
    }
}
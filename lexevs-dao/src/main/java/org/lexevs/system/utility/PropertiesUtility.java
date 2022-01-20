
package org.lexevs.system.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;

/**
 * Class to aid in finding and loading properties files.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
public class PropertiesUtility {
    public static String propertiesLocationKey = "_propertiesFileLocation_";
    public static String propertiesParentFolderKey = "_propertiesFileParentFolder_";
    public static LgMessageDirectorIF logger = null; // If provided, warnings
                                                     // will be printed here.
    
    public static String systemVariable = "PropFileLocation"; // this is the system variable
                                                              // it will

    // use as an override

    /**
     * Create a java properties object from a location. Adds a
     * "propertiesFileParentFolder" value to the properties object. The value is
     * set to the file path of the folder containing the properties file. This
     * is useful for the method
     * Log4JUtility.configureLog4JFromPathSpecifiedInProperties
     * 
     * @param propertiesFile
     *            can be a file path or a url.
     * @return The loaded properties file.
     * @throws IOException
     */
    public static Properties loadPropertiesFromFileOrURL(String propertiesFile) throws IOException {
        if (propertiesFile == null || propertiesFile.length() == 0) {
            throw new IOException("No file provided");
        }
        InputStream inputStream = null;

        String location;
        String parentFolder;

        try {
            URL temp = new URL(propertiesFile);
            inputStream = temp.openStream();
            location = temp.toString();
            parentFolder = temp.toString();
            int i = parentFolder.lastIndexOf("/");
            if (i == -1) {
                i = parentFolder.lastIndexOf("\\");
            }
            parentFolder = parentFolder.substring(0, i);
        } catch (MalformedURLException e) // Its not a url, must be a file name
        {
            File inputFile = new File(propertiesFile);
            inputStream = new FileInputStream(inputFile);
            parentFolder = inputFile.getParentFile().getAbsolutePath();
            location = inputFile.getAbsolutePath();
        }
        Properties props = new Properties();
        props.load(inputStream);
        inputStream.close();
        props.put(propertiesLocationKey, location);
        props.put(propertiesParentFolderKey, parentFolder);

        return props;
    }

    /**
     * Load a properties file from the classpath.
     * 
     * @param absolutePath
     *            absolute path to the properties file in the classpath. Should
     *            start with '/'
     * @return the properties object.
     * @throws IOException
     */
    public static Properties loadPropertiesFromClasspath(String absolutePath) throws IOException {
        Properties props = new Properties();

        if (absolutePath.charAt(0) != '/') {
            absolutePath = '/' + absolutePath;
        }

        InputStream temp = PropertiesUtility.class.getResourceAsStream(absolutePath);
        if (temp == null) {
            throw new IOException("No file '" + absolutePath + "' found on the classpath");
        }
        props.load(temp);
        props.put(propertiesLocationKey, "CLASSPATH: " + absolutePath);
        return props;
    }

    /**
     * Located the named file on the file system. Does the best it can to find
     * it. Returns null if it could not be found. If you pass in the VM
     * parameter 'PropFileLocation' - this will override everything - example:
     * -DPropFileLocation=c:/temp/HL7TestProperties.prps However, if the named
     * file doesn't exist, then it will attempt to locate the file. This is not
     * a drive search, it will always return very quickly.
     * 
     * @param fileName
     *            Name of the file to locate
     * @return file location (or null if file could not be found) - the may be a
     *         URL.
     */
    public static String locatePropFile(String fileName) {
        return locatePropFile(fileName, PropertiesUtility.class.getName());
    }

    /**
     * Located the named file on the file system. Does the best it can to find
     * it. Returns null if it could not be found. If you pass in the VM
     * parameter 'PropFileLocation' - this will override everything - example:
     * -DPropFileLocation=c:/temp/HL7TestProperties.prps However, if the named
     * file doesn't exist, then it will attempt to locate the file. This is not
     * a drive search, it will always return very quickly.
     * 
     * @param fileName
     *            Name of the file to locate
     * @param classToSearchFor
     *            A class that exists in your classpath to use as a starting
     *            point for the search. The recommended value for most use cases
     *            is "this.getClass().getName()".
     * @return file location (or null if file could not be found) - this may be
     *         a URL.
     */
    public static String locatePropFile(String fileName, String classToSearchFor) {
        return locatePropFile(fileName, classToSearchFor, null);
    }
    
    public static String locatePropFile(String fileName, String classToSearchFor, LgLoggerIF logger) {
        // If they passed in a location, and it exists, then use it.
        String location = System.getProperty(systemVariable);
        if (location != null) {
            try {
                java.io.FileReader fileReader = new java.io.FileReader(location);
                fileReader.close();
                return location;
            } catch (Exception e) {
            } // file didn't exist at specified location... try others.
            try
            // is it a url
            {
                new URL(location);
                return location;
            } catch (Exception e) {
            }

            if (logger != null) {
                logger.warn("You provided the '-D" + systemVariable + "=" + location
                        + "', however, no file was found there.  Falling back to a search for the file '" + fileName
                        + "'");
            }
        }

        try {
            Class clazz = Class.forName(classToSearchFor);
            java.net.URL url = clazz.getProtectionDomain().getCodeSource().getLocation();
            location = url.toString();

            File file = null;

            if ((location.endsWith("jar")) || (location.startsWith("jar"))) {
                try {
                    url = ((java.net.JarURLConnection) url.openConnection()).getJarFileURL();

                } // javas behavior seems to have changed from 1.3 to 1.4 (or
                  // from linux to windows, not sure
                // whiche...
                // the above is necessary in 1.3, and not in 1.4
                catch (Exception e) {
                }

                // if its a jar file - need to chop off the file name.
                file = new java.io.File(convertEncodedSpaceToSpace(url.getFile()));
            } else {
                file = new java.io.File(convertEncodedSpaceToSpace(url.getFile()));
            }

            int depthCnt = 0;

            while (true) {
                // chop off the containing folder
                file = file.getParentFile();

                if (file == null) {
                    if (logger != null) {
                        logger.error("ERROR LOCATING PROPS FILE '" + fileName + "'!  Returning Null.");
                    }
                    return null;
                }

                // go down to the "resources" folder
                File tempFile = new File(file, "resources");

                // go down to the file name
                tempFile = new File(tempFile, fileName);

                // See if it exists...

                // location = convertEncodedSpaceToSpace(location);
                if (tempFile.exists()) {
                    return tempFile.getAbsolutePath();
                }

                // break out if we look too deep...
                depthCnt++;
                if (depthCnt > 10) {
                    if (logger != null) {
                        logger.error("ERROR LOCATING PROPS FILE '" + fileName + "'!  Returning Null.");
                    }
                    return null;
                }
            }

        } catch (Exception e) {
            if (logger != null) {
                logger.error("ERROR LOCATING PROPS FILE '" + fileName + "'!  Returning Null.", e);
            }
            return null;
        }
    }

    private static String convertEncodedSpaceToSpace(String in) {
        int loc = in.indexOf("%20");
        while (loc != -1) {
            in = in.substring(0, loc) + " " + in.substring(loc + 3);
            loc = in.indexOf("%20");
        }
        return in;
    }

    /**
     * Convenience method that combines locatePropFile(fileName) and
     * loadPropertiesFromFileOrURL(String)
     * 
     * @param fileName
     * @return
     * @throws IOException
     */
    public static Properties locateAndLoadPropFile(String fileName) throws IOException {
        String temp = locatePropFile(fileName, PropertiesUtility.class.getName());
        if (temp == null) {
            throw new IOException("Couldn't find file '" + fileName + "'");
        }
        return loadPropertiesFromFileOrURL(temp);
    }

    /**
     * Convenience method that combines locatePropFile(fileName,
     * classToSearchFor) and loadPropertiesFromFileOrURL(String)
     * 
     * @param fileName
     * @return
     * @throws IOException
     */
    public static Properties locateAndLoadPropFile(String fileName, String classToSearchFor) throws IOException {
        String temp = locatePropFile(fileName, classToSearchFor);
        if (temp == null) {
            throw new IOException("Couldn't find file '" + fileName + "'");
        }
        return loadPropertiesFromFileOrURL(temp);
    }

}

package org.lexevs.system.utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.commonTypes.Source;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.Logger;
import org.lexevs.system.constants.SystemVariables;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * My own class loader that defers to a URLClassLoader - picks up any jar files
 * that are present in the runtime folder of a lexbig install - useful for
 * picking up extensions and sql drivers (that aren't on the normal classpath).
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class MyClassLoader extends URLClassLoader {
	
    /** The my class loader_. */
    private static MyClassLoader myClassLoader_;
    
    /** The Constant EXTENSION_SCHEMA. */
    private static final String EXTENSION_SCHEMA = "http://LexGrid.org/schema/LexBIG/2009/01/extensions/extension.xsd";
    
    /** The Constant JAXP_SCHEMA_SOURCE. */
    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    
    /** The Constant JAXP_SCHEMA_LANGUAGE. */
    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    
    /** The Constant W3C_XML_SCHEMA. */
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema"; 
    
    /** The docfactory_. */
    private DocumentBuilderFactory docfactory_ = null;
    
    /** The logger. */
    private LgLoggerIF logger;
    
    /** The extension descriptions. */
    private List<ExtensionDescription> extensionDescriptions = 
    	new ArrayList<ExtensionDescription>();
    
    /**
     * Gets the logger.
     * 
     * @return the logger
     */
    private LgLoggerIF getLogger() {
        return logger;
    }

    /**
     * Instantiates a new my class loader.
     * 
     * @param systemVariables the system variables
     * @param logger the logger
     */
    private MyClassLoader(SystemVariables systemVariables, LgLoggerIF logger) {
        super(new URL[] {}, MyClassLoader.class.getClassLoader());
        myClassLoader_ = this;
        this.logger = logger;

        // Time to put together a set of URL's for this classpath...
        // first, assume we are running production. Want to get the lbPatch.jar
        // on the path first
        // then, lbRuntime.jar, then anything else that is in the runtime
        // folder.

        try {
            ArrayList<File> temp = getJarsFromFolders(systemVariables
                    .getJarFileLocations());
            File[] temp2 = temp.toArray(new File[temp.size()]);

            // make sure that lbPatch is first, lbRuntime is second.
            Arrays.sort(temp2, new FileSorter());

            getLogger().debug("MyClassLoader URL Add Order:");
            for (int i = 0; i < temp2.length; i++) {
                File file = temp2[i];
                super.addURL(file.toURL());
                if (!file.getName().equals("lbPatch.jar") && !file.getName().equals("lbRuntime.jar"))
                {
                    registerIfExtension(file);
                }
                getLogger().debug(file.getAbsolutePath());
            }
        } catch (MalformedURLException e) {
            getLogger().error("Problem generating classpath for custom class loader" + e);
        }
    }

    /**
     * Gets the jars from folders.
     * 
     * @param fileNames the file names
     * 
     * @return the jars from folders
     */
    private static ArrayList<File> getJarsFromFolders(String[] fileNames) {
        ArrayList<File> files = new ArrayList<File>();
        for (int j = 0; j < fileNames.length; j++) {
            File fileName = new File(fileNames[j]);
            files.addAll(getJarsFromFolder(fileName));
        }
        return files;
    }

    /**
     * Gets the jars from folder.
     * 
     * @param fileName the file name
     * 
     * @return the jars from folder
     */
    private static ArrayList<File> getJarsFromFolder(File fileName) {
        ArrayList<File> files = new ArrayList<File>();

        if (fileName.exists()) {
            if (fileName.isDirectory()) {
                File[] children = fileName.listFiles();
                for (int i = 0; i < children.length; i++) {
                    files.addAll(getJarsFromFolder(children[i]));
                }
            } else if (fileName.getName().toLowerCase().endsWith(".jar")) {
                files.add(fileName);
            }
        }
        return files;
    }

    /**
     * The Class FileSorter.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    private class FileSorter implements Comparator<File> {
        
        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(File o1, File o2) {
            if (o1.getName().equals("lbPatch.jar")) {
                return -1;
            }
            if (o2.getName().equals("lbPatch.jar")) {
                return 1;
            }
            if (o1.getName().equals("lbRuntime.jar")) {
                return o2.equals("lbPatch.jar") ? 1 : -1;
            }
            if (o2.getName().equals("lbRuntime.jar")) {
                return o1.equals("lbPatch.jar") ? -1 : 1;
            }
            return 0;
        }
    }
    
    /**
     * Check if it is an extension and registers if it is valid.
     * 
     * @param file extension jar
     */
    private void registerIfExtension(final File file){
        try{
            JarFile jarFile = new JarFile(file);
            ZipEntry ze = jarFile.getEntry("extension.xml");
            
            if (ze == null)
                return;
            
            InputStream is = jarFile.getInputStream(ze);
        
            DocumentBuilder parser = getDocFactory().newDocumentBuilder();
            
            parser.setErrorHandler(new DefaultHandler() {
                @Override
                public void error(SAXParseException e) throws SAXException {
                    getLogger().error("problem parsing extension : '" + file.getName() + "'  : " + e.getMessage());
                    return;
                }
            });
            
            Document document = null;
            try {
                document = parser.parse(is);
            } catch (SAXException e1) {
                // validation must have failed, so return without doing anything
                getLogger().error("Validation failed for extension : '" + file.getName() + "'" + " : " + e1.getMessage());
                return;
            }
            
            NodeList extensions = document.getElementsByTagName("extension");
            int numExtensions = extensions.getLength();
            
            for (int i = 0; i < numExtensions; i++) {
                ExtensionDescription ed = new ExtensionDescription();
                
                Node extensionNode = extensions.item(i);
                ed.setName(extensionNode.getAttributes().getNamedItem("name").getNodeValue().trim());
                
                if(extensionNode.getNodeType() == Node.ELEMENT_NODE){
                    
                    Element extensionElement = (Element)extensionNode;    
                    
                    //------- get extension description
                    NodeList descriptionList = extensionElement.getElementsByTagName("description");
                    
                    if (descriptionList != null)
                    {
                        Element descriptionElement = (Element)descriptionList.item(0);
                        
                        if (descriptionElement != null)
                        {
                            NodeList descList = descriptionElement.getChildNodes();
                            if (descList.item(0) != null)
                                ed.setDescription(descList.item(0).getNodeValue().trim());
                        }
                    }
                    
                    //------- get extension provider
                    NodeList providerList = extensionElement.getElementsByTagName("provider");
                    if (providerList != null)
                    {
                        Element providerElement = (Element)providerList.item(0);
                        
                        if (providerElement != null)
                        {
                            NodeList textLNList = providerElement.getChildNodes();
                            if (textLNList.item(0) != null)
                                ed.setExtensionProvider(new Source(textLNList.item(0).getNodeValue().trim()));
                        }
                    }
                    
                    //---- get base class
                    Element baseClassElement = (Element)extensionElement.getElementsByTagName("baseClass").item(0);                    
                    if (baseClassElement != null)
                    {
                        NodeList baseClassList = baseClassElement.getChildNodes();
                        if (baseClassList.item(0) != null)
                        ed.setExtensionBaseClass(baseClassList.item(0).getNodeValue().trim());
                    }
                    
                    //---- get implemented class
                    Element classElement = (Element)extensionElement.getElementsByTagName("class").item(0);                    
                    if (classElement != null)
                    {
                        NodeList classList = classElement.getChildNodes();
                        if (classList.item(0) != null)
                        ed.setExtensionClass(classList.item(0).getNodeValue().trim());
                    }
                    
                    //------ get the name of the interface this extension implements
                    String extImplements = null;
                    Element implElement = (Element)extensionElement.getElementsByTagName("implements").item(0);                    
                    if (implElement != null)
                    {
                        NodeList textAgeList = implElement.getChildNodes();
                        if (textAgeList.item(0) != null)
                            extImplements = textAgeList.item(0).getNodeValue().trim();
                    }
                    
                    //------ register the extension
                    if (extImplements != null)
                    {
                        registerExtension(ed);
                    }                    
                }
            }
        } catch (IOException e) {
            // do nothing
        } catch (ParserConfigurationException e) {
            // do nothing
        }
    }
    
    /**
     * Gets the doc factory.
     * 
     * @return the doc factory
     */
    private DocumentBuilderFactory getDocFactory(){
        if (docfactory_ == null)
        {
            docfactory_ = DocumentBuilderFactory.newInstance();
            docfactory_.setIgnoringComments(true);
            docfactory_.setCoalescing(true);
            docfactory_.setNamespaceAware(true);            
            docfactory_.setValidating(true);
            docfactory_.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);            
            docfactory_.setAttribute(JAXP_SCHEMA_SOURCE, EXTENSION_SCHEMA);            
        }
        return docfactory_;
    }
    
    /**
     * Registers new extension.
     * 
     * @param ed the ed
     */
    private void registerExtension(ExtensionDescription ed){
        getLogger().debug("Registering extension : '" + ed.getName() + "'");

       this.extensionDescriptions.add(ed);
    }
    
    /**
     * Gets the extension descriptions.
     * 
     * @return the extension descriptions
     */
    public List<ExtensionDescription> getExtensionDescriptions(){
    	return this.extensionDescriptions;
    }
    
    /**
     * Instance.
     * 
     * @return the my class loader
     */
    @Deprecated
    public static MyClassLoader instance(){
    	return (MyClassLoader) LexEvsServiceLocator.getInstance().getSystemResourceService().getClassLoader();
    }
    
    /**
     * Instance.
     * 
     * @param systemVariables the system variables
     * @param logger the logger
     * 
     * @return the my class loader
     */
    @LgClientSideSafe
    @Deprecated
    public static MyClassLoader instance(SystemVariables systemVariables, LgLoggerIF logger) {
        if (myClassLoader_ == null) {
            myClassLoader_ = new MyClassLoader(systemVariables, logger);
        }
        return myClassLoader_;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.ClassLoader#loadClass(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    @LgClientSideSafe
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        // if they ask for this class, I need to load it myself - otherwise,
        // punt to parent.
        if (name.equals("org.LexGrid.util.sql.sqlReconnect.ConnectionHelper")) {
            getLogger().debug("MyClassLoader loading '" + name + "'");
            // First, check if the class has already been loaded
            Class c = findLoadedClass(name);
            if (c == null) {
                try {
                    c = findClass(name);
                } catch (ClassNotFoundException e) {
                    // punt to the parent.
                    getLogger().debug("MyClassLoader couldn't find '" + name + "' - passing to parent.");
                    return super.loadClass(name);
                }
            }
            return c;
        } else {
            return super.loadClass(name);
        }
    }
    
    public void shutdown(){
    	myClassLoader_ = null;
    }
}
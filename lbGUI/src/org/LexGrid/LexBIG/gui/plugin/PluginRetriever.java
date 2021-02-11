
package org.LexGrid.LexBIG.gui.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * PluginRetriever loads all the plugins for the LexBig UI from a specified
 * top-level directory. By default, the directory "plugins" is used.
 * <p>
 * Each plugin must reside in its own directory underneath the top-level
 * directory, and must consist of a manifest file (named plugin.xml) and a jar
 * containing the class files which define the plugin.
 * <p>
 * 
 * 
 * @author <A HREF="mailto:leisch.jason@mayo.edu">Jason Leisch</A>
 */
public class PluginRetriever {
	private static Logger log = Logger.getLogger("LexBIG.GUI");

	private final String baseFolder;

	public static final String MANIFEST_FILE_NAME = "plugin.xml";
	public static final String DEFAULT_BASE_FOLDER = "plugins";

	/**
	 * Construct a PluginRetriever with a base directory other than the default.
	 * <p>
	 * TODO: Currently this class only supports "TabbedContent" style plugins.
	 * Eventually it should be modified to be more generic and allow for
	 * extension points in other areas.
	 * 
	 * @param directory
	 *            the name of the folder plugins are located, relative to the
	 *            home directory
	 */
	public PluginRetriever(String directory) {
		this.baseFolder = directory;
	}

	/**
	 * Construct a PluginRetriever with a base directory of "plugins".
	 * <p>
	 * TODO: Currently this class only supports "TabbedContent" style plugins.
	 * Eventually it should be modified to be more generic and allow for
	 * extension points in other areas.
	 */
	public PluginRetriever() {
		this(DEFAULT_BASE_FOLDER);
	}

	/**
	 * Returns the TabbedContent plugins.
	 * 
	 * @return an array of TabbedContent objects.
	 */
	public TabbedContent[] getTabbedContentPlugins() {
		List<TabbedContent> pluginList = new ArrayList<TabbedContent>();

		File pluginDirectory = new File(baseFolder);
		File[] plugins = pluginDirectory.listFiles();

		if (plugins == null) {
			return new TabbedContent[0];
		}

		for (File plugin : plugins) {
			if (!plugin.isDirectory()) {
				continue; // Each plugin must be in its own directory.
			}
			File xml = getXmlFile(plugin.getName());
			if (xml != null) {
				try {
					TabbedContent tc = parse(xml);
					if (tc != null) {
						pluginList.add(tc);
					}
				} catch (Exception e) {
					log.error("Unexpected Error", e);
				}
			}
		}
		TabbedContent[] tc = new TabbedContent[pluginList.size()];
		pluginList.toArray(tc);
		return tc;
	}

	/**
	 * Parses the given xml manifest file for a plugin and loads the described
	 * plugin class into the JVM.
	 * 
	 * @param xmlFile
	 *            the File object whose name is "plugin.xml". This file
	 *            describes the library and class that makes up the plugin.
	 * 
	 * @return the TabbedContent that the manifest file describes.
	 * @throws ParserConfigurationException
	 *             if a parser cannot be created which satisfies the requested
	 *             configuration.
	 * @throws SAXException
	 *             If any SAX errors occur during processing.
	 * @throws FileNotFoundException
	 *             If the file does not exist, is a directory rather than a
	 *             regular file, or for some other reason cannot be opened for
	 *             reading.
	 * @throws IOException
	 *             If any IO error occurs
	 */
	private TabbedContent parse(File xmlFile)
			throws ParserConfigurationException, SAXException,
			FileNotFoundException, IOException {
		File f = xmlFile.getAbsoluteFile();
		String parent = f.getParent();
		PluginHandler handler = new PluginHandler(parent);
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		saxParser.parse(new FileInputStream(xmlFile), handler);
		return handler.getTabbedContent();
	}

	/**
	 * Given a directory, return the plugin.xml manifest file within that
	 * directory.
	 * 
	 * @param directory
	 *            the name of a single directory.
	 * @return the plugin.xml File in the directory, or null if plugin.xml does
	 *         not exist
	 */
	private File getXmlFile(String directory) {
		directory = baseFolder + File.separator + directory;
		File dir = new File(directory);
		File[] plugins = dir.listFiles();

		if (plugins == null) {
			return null; // "directory" was not a directory after all
		}

		for (File plugin : plugins) {
			if (plugin.getName().equals(MANIFEST_FILE_NAME)) {
				return plugin;
			}
		}
		// log.warn("No plugin.xml manifest file exists");
		return null;
	}

	/**
	 * A class that parses XML files that conform to the LEXBIG plugin standard,
	 * and load the classes defined by the specified XML.
	 * 
	 * @author <A HREF="mailto:leisch.jason@mayo.edu">Jason Leisch</A>
	 */
	static class PluginHandler extends DefaultHandler {
		private static Logger log = Logger.getLogger("LexBIG.GUI");

		private TabbedContent tabbedContent;
		private final String absolutePath;

		private static final String LIBRARY_TAG = "library";
		private static final String LIBRARY_ATTR_NAME = "name";
		private static final String LIBRARY_ATTR_CLASS = "class";

		/**
		 * Create a handler to parse the XML file specified.
		 * 
		 * @param absolutePath
		 *            the location of the xml file.
		 */
		public PluginHandler(String absolutePath) {
			this.absolutePath = absolutePath;
		}

		/**
		 * Called when a new element in the plugin.xml file is encountered.
		 */
		public void startElement(String namespaceURI, String sName,
				String qName, Attributes attrs) throws SAXException {
			if (qName.equals(LIBRARY_TAG)) {
				if (attrs != null) {
					String name = attrs.getValue(LIBRARY_ATTR_NAME);
					String clazz = attrs.getValue(LIBRARY_ATTR_CLASS);

					try {
						String urlString = "file:////" + absolutePath
								+ File.separator + name;
						URL url = new URL(urlString);
						ClassLoader classLoader = new URLClassLoader(
								new URL[] { url });
						Class<?> pluginClass;
						pluginClass = classLoader.loadClass(clazz);
						tabbedContent = (TabbedContent) pluginClass
								.newInstance();
					} catch (Exception e) {
						log.error("Unexpected Error", e);
					}
				}
			}
		}

		/**
		 * Returns the TabbedContent object that was defined in the xml being
		 * parsed.
		 */
		public TabbedContent getTabbedContent() {
			return tabbedContent;
		}
	}
}
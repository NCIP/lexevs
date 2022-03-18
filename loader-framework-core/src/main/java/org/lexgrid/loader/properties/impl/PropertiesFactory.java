
package org.lexgrid.loader.properties.impl;

import java.util.Properties;

/**
 * A factory for creating Properties objects given a SQLConnection from LexEVS
 */
public class PropertiesFactory {

	/** The PREFIX. */
	public static String PREFIX = "prefix";

	/**
	 * Gets the properties.
	 * 
	 * @param connection the connection
	 * 
	 * @return the properties
	 */
	public Properties getProperties(String prefix){
		Properties props = new Properties();
		props.setProperty(PREFIX, prefix);
		return props;		
	}
}
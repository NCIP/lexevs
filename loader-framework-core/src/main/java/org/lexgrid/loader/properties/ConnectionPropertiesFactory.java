
package org.lexgrid.loader.properties;

import java.util.Properties;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;

/**
 * A factory for creating ConnectionProperties objects.
 */
public interface ConnectionPropertiesFactory {

	/**
	 * Gets the properties for new load.
	 * 
	 * @return the properties for new load
	 */
	public Properties getPropertiesForNewLoad();
	
	/**
	 * Gets the properties for existing load.
	 * 
	 * @param codingScheme the coding scheme
	 * @param version the version
	 * 
	 * @return the properties for existing load
	 * @throws LBParameterException 
	 */
	public Properties getPropertiesForExistingLoad(String codingScheme, String version) throws LBParameterException;
}
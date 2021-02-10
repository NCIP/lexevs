
package org.lexgrid.loader.properties.impl;

import java.util.Properties;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.service.Registry;
import org.lexgrid.loader.properties.ConnectionPropertiesFactory;

/**
 * A factory for creating DefaultLexEVSProperties objects.
 */
public class DefaultLexEVSPropertiesFactory extends PropertiesFactory implements ConnectionPropertiesFactory {
	
	private Registry registry = LexEvsServiceLocator.getInstance().getRegistry();
	
	private LexEvsDatabaseOperations lexEvsDatabaseOperations = LexEvsServiceLocator.getInstance().getLexEvsDatabaseOperations();


	/* (non-Javadoc)
	 * @see org.lexgrid.loader.properties.ConnectionPropertiesFactory#getPropertiesForNewLoad()
	 */
	public Properties getPropertiesForNewLoad() {	
		PrefixResolver prefixResolver = lexEvsDatabaseOperations.getPrefixResolver();
		String prefix = prefixResolver.resolveDefaultPrefix() +
			prefixResolver.getNextCodingSchemePrefix();
		return getProperties(prefix);		
	}
		
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.properties.ConnectionPropertiesFactory#getPropertiesForExistingLoad(java.lang.String, java.lang.String)
	 */
	public Properties getPropertiesForExistingLoad(String codingSchemeUri, String version) throws LBParameterException {		

			String 
				prefix = registry.getCodingSchemeEntry(DaoUtility.createAbsoluteCodingSchemeVersionReference(codingSchemeUri, version)).getStagingPrefix();
			
			if(StringUtils.isBlank(prefix)) {
				throw new LBParameterException("URI: " + codingSchemeUri + " Version: " + version + " does not have any Batch loaded artifacts to remove.");
			}
			return getProperties(prefix);
	
	}
}
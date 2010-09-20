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

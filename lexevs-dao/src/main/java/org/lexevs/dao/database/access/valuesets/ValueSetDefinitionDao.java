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
package org.lexevs.dao.database.access.valuesets;

import java.util.List;

import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

/**
 * The Interface ValueSetDefinitionDao.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public interface ValueSetDefinitionDao extends LexGridSchemaVersionAwareDao {
	
	/**
	 * Gets the value set definition by URI.
	 * 
	 * @param valueSetDefinitionURI the value set definition URI
	 * 
	 * @return the value set definition by value set definition URI
	 */
	public ValueSetDefinition getValueSetDefinitionByURI(String valueSetDefinitionURI);
	
	/**
	 * Gets the GUID from value set definition URI
	 * 
	 * @param valueSetDefinitionURI the value set definition URI
	 * 
	 * @return the GUID from value set definition URI
	 */
	public String getGuidFromvalueSetDefinitionURI(String valueSetDefinitionURI);
	
	/**
	 * Insert value set definition.
	 * 
	 * @param systemReleaseUri the system release URI
	 * @param definition the definition
	 * 
	 * @return the string
	 */
	public String insertValueSetDefinition(String systemReleaseUri, ValueSetDefinition definition);

	/**
	 * Gets the value set definition URIs.
	 * 
	 * @return List of value set definition URIs
	 */
	public List<String> getValueSetDefinitionURIs();
	
	/**
	 * Insert value set definition entry.
	 * 
	 * @param valueSetDefinitionGuid the value set definition GUID
	 * @param definition the definition
	 * 
	 * @return the string
	 */
	public String insertDefinitionEntry(String valueSetDefinitionGuid, ValueSetDefinition definition);
	
	/**
	 * Delete value set definition by value set definition URI.
	 * 
	 * @param valuesetdefinitionURI the value set definition URI
	 */
	public void removeValueSetDefinitionByValueSetDefinitionURI(String valueSetDefinitionURI);
}

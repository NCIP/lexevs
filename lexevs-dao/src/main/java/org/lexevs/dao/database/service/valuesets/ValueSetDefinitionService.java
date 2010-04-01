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
package org.lexevs.dao.database.service.valuesets;

import java.net.URI;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitions;

/**
 * The Interface ValueSetDefinitionService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface ValueSetDefinitionService {

	/**
	 * Insert value set definition into the system. 
	 * @param definition the definition
	 * @throws LBException
	 */
	public void insertValueSetDefinition(ValueSetDefinition definition) throws LBException;
	
	/**
	 * Insert value set definitions.
	 * 
	 * @param valueSetDefinitions the value set definitions
	 * @param systemReleaseUri the system release uri
	 */
	public void insertValueSetDefinitions(ValueSetDefinitions valueSetDefinitions, String systemReleaseUri);
	
	/**
	 * Return all value set definition URIs that match the supplied key
	 * @param key - null: return all value set definition URI's
	 *            - " ":  return all value set definition URI's with no name
	 *            - otherwise return all URIs that match the key
	 * @return list of matching URIs
	 * @throws LBException
	 */
	public List<URI> getValueSetDefinitionURISForName(String valueSetDefinitionName) throws LBException;
	
	/**
	 * Gets the value set definition by uri.
	 * 
	 * @param uri the uri
	 * 
	 * @return the value set definition
	 */
	public ValueSetDefinition getValueSetDefinitionByUri(URI uri);
}

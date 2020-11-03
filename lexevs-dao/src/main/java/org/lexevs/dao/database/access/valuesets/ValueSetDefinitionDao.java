/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.naming.Mappings;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitions;
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
	 * Returns all the value set definition URIs that contain supplied supported tag and value.
	 * 
	 * @param supportedTag like SupportedCodingScheme, SupportedAssociation etc.
	 * @param value value to look for
	 * @parma uri
	 * @return list of value set definition URIs that contains supportedTag with value.
	 */
	public List<String> getValueSetDefinitionURIForSupportedTagAndValue(String supportedTag, String value, String uri);
	
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
	 * Inserts value set definition and its mappings.
	 * 
	 * @param systemReleaseURI system release URI
	 * @param vsdef value set definition
	 * @param mappings the mappings
	 */
	public String insertValueSetDefinition(String systemReleaseURI, ValueSetDefinition vsdef, Mappings mappings);
	
	/**
	 * Inserts value set definitions and its mappings.
	 * 
	 * @param systemReleaseURI system release URI
	 * @param vsdefs value set definitions 
	 * @param mappings the mappings
	 */
	public void insertValueSetDefinitions(String systemReleaseURI, ValueSetDefinitions vsdefs, Mappings mappings);
	
	/**
	 * Insert value set definition entry.
	 * 
	 * @param vsdef value set definition this definition entry belongs to.
	 * @param definitionEntry definition entry itself.
	 */
	public void insertDefinitionEntry(ValueSetDefinition vsdef, DefinitionEntry definitionEntry);

	/**
	 * Gets the value set definition URIs.
	 * 
	 * @return List of value set definition URIs
	 */
	public List<String> getValueSetDefinitionURIs();
	
	/**
	 * Return the URI's for the value set definition(s) for the supplied
	 * value set definition name. If the name is null, returns everything. If the name is not
	 * null, returns the value set definition(s) that have the assigned name. 
	 * 
	 * Note: plural because there is no guarantee of valueSetDefinition uniqueness. If the name is the
	 * empty string "", returns all unnamed valueSetDefinitions.
	 * 
	 * @param valueSetDefinitionName
	 * @return value domain URI's
	 * @throws LBException
	 */
	public List<String> getValueSetDefinitionURIsForName(String valueSetDefinitionName) throws LBException;
	
	/**
	 * Return the URI's of all unnamed value set definition(s).
	 * 
	 * @return value set definition URI's
	 * @throws LBException
	 */
	public List<String> getAllValueSetDefinitionsWithNoName()  throws LBException;
		
	/**
	 * Delete value set definition by value set definition URI.
	 * 
	 * @param valuesetdefinitionURI the value set definition URI
	 */
	public void removeValueSetDefinitionByValueSetDefinitionURI(String valueSetDefinitionURI);
	

	public String insertHistoryValueSetDefinition(String valueSetDefUId);

	public String updateValueSetDefinition(String valueSetDefUId,
			ValueSetDefinition valueSetDefinition);

	public String updateValueSetDefinitionVersionableChanges(
			String valueSetDefUId, ValueSetDefinition valueSetDefinition);

	public String getValueSetDefEntryStateUId(String valueSetDefUId);

	public void updateValueSetDefEntryStateUId(String valueSetDefUId,
			String entryStateUId);

	public String getLatestRevision(String valueSetDefUId);
	
	public void insertMappings(String referenceGuid, Mappings mappings);

	public boolean entryStateExists(String entryStateUId);
	
	public ValueSetDefinition getValueSetDefinitionByRevision(String valueSetDefURI,
			String revisionId) throws LBRevisionException;
	
	/**
	 * Removes value set supported attribute
	 * @param referenceGuid Value Set UID
	 * @param supportedAttributeTag supported attribute tag
	 */
	public void deleteURIMap(String referenceGuid, String supportedAttributeTag);

	public List<AbsoluteCodingSchemeVersionReference> getValueSetDefinitionSchemeRefForTopNodeSourceCode(String code);

	public List<AbsoluteCodingSchemeVersionReference> getValueSetDefinitionDefRefForTopNodeSourceCode(
			String entityCode);

	public List<String> getValueSetURIsForContext(String uri);

	public Map<String, ValueSetDefinition> getValueSetURIMapToDefinitions();
}
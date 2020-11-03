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
package org.lexevs.dao.database.service.valuesets;

import java.net.URI;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.naming.Mappings;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitions;
import org.lexevs.dao.database.access.association.model.Node;

/**
 * The Interface ValueSetDefinitionService.
 */
public interface ValueSetDefinitionService {

	/**
	 * Insert value set definition into the system.
	 * 
	 * @param definition the definition
	 * @param systemReleaseUri the system release uri
	 * @param mappings Supported Attribute mappings to be applied to the value set definition
	 * 
	 * @throws LBException the LB exception
	 */
	public void insertValueSetDefinition(ValueSetDefinition definition, String systemReleaseUri, Mappings mappings) throws LBException;
	
	/**
	 * Insert value set definitions.
	 * 
	 * @param valueSetDefinitions the value set definitions
	 * @param systemReleaseUri the system release uri
	 * 
	 * @throws LBException the LB exception
	 */
	public void insertValueSetDefinitions(ValueSetDefinitions valueSetDefinitions, String systemReleaseUri) throws LBException;
	
	/**
	 * Insert value set definition Entry.
	 * 
	 * @param valueSetDefinition the value set definition the definition belongs to.
	 * @param definitionEntry the definition entry itself
	 * 
	 * @throws LBException the LB exception
	 */
	public void insertDefinitionEntry(ValueSetDefinition valueSetDefinition, DefinitionEntry definitionEntry) throws LBException;
	
	/**
	 * Return all value set definition URIs that match the supplied key.
	 * 
	 * @param valueSetDefinitionName the value set definition name
	 * 
	 * @return list of matching URIs
	 * 
	 * @throws LBException the LB exception
	 */
	public List<String> getValueSetDefinitionURISForName(String valueSetDefinitionName) throws LBException;
	
	/**
	 * Gets the value set definition by uri.
	 * 
	 * @param uri the uri
	 * 
	 * @return the value set definition
	 */
	public ValueSetDefinition getValueSetDefinitionByUri(URI uri);
	
	/**
	 * Returns list of Value Set Definition URIs that contain supplied SupportedAttribute Tag and Value.
	 * 
	 * @param supportedTag SupportedAttribute tag like SupportedCodingScheme, SupportedConceptDomain etc.
	 * @param value value of the supportedAttribute
	 * @param uri the uri
	 * 
	 * @return list of URIs
	 */
	public List<String> getValueSetDefinitionURIForSupportedTagAndValue(String supportedTag, String value, String uri);
	
	/**
	 * Lists all the value set definition URIs that are loaded in the system.
	 * 
	 * @return list of value set definition URIs
	 */
	public List<String> listValueSetDefinitionURIs();
	
	/**
	 * Return the URI's of all unnamed value set definition(s).
	 * 
	 * @return value set definition URI's
	 * 
	 * @throws LBException the LB exception
	 */
	public List<String> getAllValueSetDefinitionsWithNoName()  throws LBException;
	
	/**
	 * Delete value set definition by value set definition URI.
	 * 
	 * @param valueSetDefinitionURI the value set definition uri
	 */
	public void removeValueSetDefinition(String valueSetDefinitionURI);
	
	/**
	 * Update value set definition.
	 * 
	 * @param valueSetDefinition the value set definition
	 * 
	 * @throws LBException the LB exception
	 */
	public void updateValueSetDefinition(ValueSetDefinition valueSetDefinition) throws LBException;
	
	/**
	 * Insert dependent changes.
	 * 
	 * @param valueSetDefinition the value set definition
	 * 
	 * @throws LBException the LB exception
	 */
	public void insertDependentChanges(ValueSetDefinition valueSetDefinition) throws LBException;
	
	/**
	 * Update versionable attributes.
	 * 
	 * @param valueSetDefinition the value set definition
	 * 
	 * @throws LBException the LB exception
	 */
	public void updateVersionableAttributes(ValueSetDefinition valueSetDefinition) throws LBException;
	
	/**
	 * Revise.
	 * 
	 * @param valueSetDefinition the value set definition
	 * @param mapping the mapping
	 * @param releaseURI the release uri
	 * 
	 * @throws LBException the LB exception
	 */
	public void revise(ValueSetDefinition valueSetDefinition, Mappings mapping, String releaseURI) throws LBException;
	
	/**
	 * Gets the value set definition by revision.
	 * 
	 * @param valueSetDefURI the value set def uri
	 * @param revisionId the revision id
	 * 
	 * @return the value set definition by revision
	 * 
	 * @throws LBRevisionException the LB revision exception
	 */
	public ValueSetDefinition getValueSetDefinitionByRevision(String valueSetDefURI,
			String revisionId) throws LBRevisionException;
	
	/**
	 * Gets the value set definition by date.
	 * 
	 * @param valueSetDefURI the value set def uri
	 * @param date the date
	 * 
	 * @return the value set definition by date
	 * 
	 * @throws LBRevisionException the LB revision exception
	 */
	public ValueSetDefinition getValueSetDefinitionByDate(String valueSetDefURI,
			Date date) throws LBRevisionException;


	public List<AbsoluteCodingSchemeVersionReference> getValueSetDefinitionSchemeRefForTopNodeSourceCode(Node node)
			throws LBException;

	public List<AbsoluteCodingSchemeVersionReference> getValueSetDefinitionDefRefForTopNodeSourceCode(Node node) throws LBException;

	public List<String> getVSURIsForContextURI(String createUri);

	public Map<String, ValueSetDefinition> getValueSetDefinitionsByResgistryEntry();
}
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
package org.lexevs.dao.database.access.codingscheme;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.URIMap;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

/**
 * The Interface CodingSchemeDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CodingSchemeDao extends LexGridSchemaVersionAwareDao {
	
	/**
	 * Gets the coding scheme by id.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * 
	 * @return the coding scheme by id
	 */
	public CodingScheme getCodingSchemeById(String codingSchemeId);

	/**
	 * Insert coding scheme.
	 * 
	 * @param cs the cs
	 * 
	 * @return the string
	 */
	public String insertCodingScheme(CodingScheme cs, boolean cascade);
	
	/**
	 * Insert history coding scheme.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param codingScheme the coding scheme
	 */
	public void insertHistoryCodingScheme(String codingSchemeId, CodingScheme codingScheme);
	
	/**
	 * Gets the coding scheme by name and version.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param version the version
	 * 
	 * @return the coding scheme by name and version
	 */
	public CodingScheme getCodingSchemeByNameAndVersion(String codingSchemeName, String version);
	
	/**
	 * Gets the coding scheme by uri and version.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @return the coding scheme by uri and version
	 */
	public CodingScheme getCodingSchemeByUriAndVersion(String codingSchemeUri, String version);
	
	/**
	 * Gets the coding scheme summary by uri and version.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @return the coding scheme summary by uri and version
	 */
	public CodingSchemeSummary getCodingSchemeSummaryByUriAndVersion(String codingSchemeUri, String version);
	
	/**
	 * Gets the coding scheme by revision.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param version the version
	 * @param revisionId the revision id
	 * 
	 * @return the coding scheme by revision
	 */
	public CodingScheme getHistoryCodingSchemeByRevision(String codingSchemeId, String revisionId);
	
	/**
	 * Update coding scheme.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param codingScheme the coding scheme
	 */
	public void updateCodingScheme(String codingSchemeId, CodingScheme codingScheme);
	
	/**
	 * Gets the coding scheme id by name and version.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param version the version
	 * 
	 * @return the coding scheme id by name and version
	 */
	public String getCodingSchemeIdByNameAndVersion(String codingSchemeName, String version);
	
	/**
	 * Gets the coding scheme id by uri and version.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @return the coding scheme id by uri and version
	 */
	public String getCodingSchemeIdByUriAndVersion(String codingSchemeUri, String version);
	
	/**
	 * Gets the entry state id.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param version the version
	 * 
	 * @return the entry state id
	 */
	public String getEntryStateId(String codingSchemeName, String version);
	
	/**
	 * Delete coding scheme by id.
	 * 
	 * @param codingSchemeId the coding scheme id
	 */
	public void deleteCodingSchemeById(String codingSchemeId);
	
	/**
	 * Insert coding scheme source.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param source the source
	 */
	public void insertCodingSchemeSource(String codingSchemeId, Source source);
	
	public void insertOrUpdateCodingSchemeSource(String codingSchemeId, Source source);
	
	public void deleteCodingSchemeSources(String codingSchemeId);
	
	public void deleteCodingSchemeLocalNames(String codingSchemeId);
	
	public void deleteCodingSchemeMappings(String codingSchemeId);

	/**
	 * Gets the uri map.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param localId the local id
	 * @param uriMap the uri map
	 * 
	 * @return the uri map
	 */
	public <T extends URIMap> T getUriMap(String codingSchemeId, String localId, Class<T> uriMap);
	
	/**
	 * Validate supported attribute.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param localId the local id
	 * @param uriMap the uri map
	 * 
	 * @return true, if successful
	 */
	public <T extends URIMap> boolean validateSupportedAttribute(String codingSchemeId, String localId, Class<T> uriMap);
	
	/**
	 * Insert coding scheme local name.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param localName the local name
	 */
	public void insertCodingSchemeLocalName(String codingSchemeId, String localName);
	
	/**
	 * Insert mappings.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param mappings the mappings
	 */
	public void insertMappings(String codingSchemeId, Mappings mappings);
	
	/**
	 * Gets the mappings.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * 
	 * @return the mappings
	 */
	public Mappings getMappings(String codingSchemeId);
	
	/**
	 * Insert uri map.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param supportedProperty the supported property
	 */
	public void insertURIMap(String codingSchemeId, List<URIMap> supportedProperty);

	/**
	 * Insert uri map.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param supportedProperty the supported property
	 */
	public void insertURIMap(String codingSchemeId, URIMap supportedProperty);
	
	public void insertOrUpdateURIMap(String codingSchemeId, URIMap supportedProperty);
	
	/**
	 * Gets the distinct property names of coding scheme.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * 
	 * @return the distinct property names of coding scheme
	 */
	public List<String> getDistinctPropertyNamesOfCodingScheme(
			String codingSchemeId);
	
	/**
	 * Gets the distinct formats of coding scheme.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * 
	 * @return the distinct formats of coding scheme
	 */
	public List<String> getDistinctFormatsOfCodingScheme(
			String codingSchemeId);
	
	/**
	 * Gets the distinct property qualifier names of coding scheme.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * 
	 * @return the distinct property qualifier names of coding scheme
	 */
	public List<String> getDistinctPropertyQualifierNamesOfCodingScheme(
			String codingSchemeId);
	
	/**
	 * Gets the distinct property qualifier types of coding scheme.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * 
	 * @return the distinct property qualifier types of coding scheme
	 */
	public List<String> getDistinctPropertyQualifierTypesOfCodingScheme(
			String codingSchemeId);

	/**
	 * Gets the distinct namespaces of coding scheme.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * 
	 * @return the distinct namespaces of coding scheme
	 */
	public List<String> getDistinctNamespacesOfCodingScheme(String codingSchemeId);
	
	/**
	 * Gets the distinct entity types of coding scheme.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * 
	 * @return the distinct entity types of coding scheme
	 */
	public List<String> getDistinctEntityTypesOfCodingScheme(String codingSchemeId);
	
	/**
	 * Gets the distinct languages of coding scheme.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * 
	 * @return the distinct languages of coding scheme
	 */
	public List<String> getDistinctLanguagesOfCodingScheme(String codingSchemeId);
}

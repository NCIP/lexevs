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
package org.lexevs.dao.database.service.codingscheme;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.URIMap;
import org.LexGrid.versions.EntryState;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

/**
 * The Interface CodingSchemeService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CodingSchemeService {
	
	public static String INSERT_CODINGSCHEME_ERROR = "INSERT-CODING-SCHEME-ERROR";

	/**
	 * Gets the coding scheme by uri and version.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * 
	 * @return the coding scheme by uri and version
	 */
	public CodingScheme getCodingSchemeByUriAndVersion(
			String codingSchemeUri, String codingSchemeVersion);
	
	/**
	 * Gets the coding scheme summary by uri and version.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * 
	 * @return the coding scheme summary by uri and version
	 */
	public CodingSchemeSummary getCodingSchemeSummaryByUriAndVersion(
			String codingSchemeUri, String codingSchemeVersion);
	
	/**
	 * Destroy coding scheme.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 */
	public void destroyCodingScheme(
			String codingSchemeUri, String codingSchemeVersion);
	
	/**
	 * Insert coding scheme.
	 * 
	 * @param scheme the scheme
	 * 
	 * @throws CodingSchemeAlreadyLoadedException the coding scheme already loaded exception
	 */
	public void insertCodingScheme(
			CodingScheme scheme) throws CodingSchemeAlreadyLoadedException;
	
	/**
	 * Update coding scheme.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param codingScheme the coding scheme
	 */
	public void updateCodingScheme(
			String codingSchemeUri, 
			String codingSchemeVersion,
			CodingScheme codingScheme);
	
	/**
	 * Insert uri map.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param uriMap the uri map
	 */
	public void insertURIMap(
			String codingSchemeUri, 
			String codingSchemeVersion,
			URIMap uriMap);
	
	
	/**
	 * Update coding scheme entry state.
	 * 
	 * @param codingScheme the coding scheme
	 * @param entryState the entry state
	 */
	public void updateCodingSchemeEntryState( 
			CodingScheme codingScheme,
			EntryState entryState);
	
	/**
	 * Validated supported attribute.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param localId the local id
	 * @param attributeClass the attribute class
	 * 
	 * @return true, if successful
	 */
	public <T extends URIMap> boolean
		 validatedSupportedAttribute(String codingSchemeUri, String codingSchemeVersion, String localId, Class<T> attributeClass);

}

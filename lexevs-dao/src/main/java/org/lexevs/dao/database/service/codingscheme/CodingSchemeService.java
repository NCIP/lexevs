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

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.URIMap;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

/**
 * The Interface CodingSchemeService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CodingSchemeService {
	
	public static final String INSERT_CODINGSCHEME_ERROR = "INSERT-CODING-SCHEME-ERROR";
	public static final String REMOVE_CODINGSCHEME_ERROR = "REMOVE-CODING-SCHEME-ERROR";
	public static final String INSERT_CODINGSCHEME_URI_ERROR = "INSERT-CODING-SCHEME-URI-ERROR";
	public static final String UPDATE_CODINGSCHEME_URI_ERROR = "UPDATE-CODING-SCHEME-URI-ERROR";
	public static final String UPDATE_CODINGSCHEME_ERROR = "UPDATE-CODING-SCHEME-ERROR";
	public static final String UPDATE_CODINGSCHEME_ENTRYSTATE_ERROR = "UPDATE-CODING-SCHEME-ENTRYSTATE-ERROR";
	public static final String INSERT_CODINGSCHEME_VERSIONABLE_CHANGES_ERROR = "INSERT-CODING-SCHEME-VERSIONABLE-CHANGES-ERROR";
	public static final String INSERT_CODINGSCHEME_DEPENDENT_CHANGES_ERROR = "INSERT-CODING-SCHEME-DEPENDENT-CHANGES-ERROR";
	
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
	
	public CodingScheme getCompleteCodingScheme(
			String codingSchemeUri, String codingSchemeVersion);
	
	/**
	 * Destroy coding scheme.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 */
	public void removeCodingScheme(
			String codingSchemeUri, String codingSchemeVersion);
	
	/**
	 * Insert coding scheme.
	 * 
	 * @param scheme the scheme
	 * 
	 * @throws CodingSchemeAlreadyLoadedException the coding scheme already loaded exception
	 */
	public void insertCodingScheme(
			CodingScheme scheme, String releaseURI) throws CodingSchemeAlreadyLoadedException;
	
	/**
	 * Update coding scheme.
	 * 
	 * @param codingScheme the coding scheme
	 * @throws LBException 
	 */
	public void updateCodingScheme(CodingScheme codingScheme) throws LBException;
	
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
	
	public void updateURIMap(
			String codingSchemeUri, 
			String codingSchemeVersion,
			URIMap uriMap);
	
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
	
	/**
	 * Gets the property URI map that matches the propertyType.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param propertyType type of property
	 * 
	 * @return the uri map
	 */
	public List<SupportedProperty> getSupportedPropertyForPropertyType(String codingSchemeUri, String codingSchemeVersion, PropertyTypes propertyType);

	/**
	 * revise the codingScheme.
	 * 
	 * @param revisedCodingScheme
	 * @throws LBException
	 */
	public void revise(CodingScheme revisedCodingScheme, String releaseURI) throws LBException;

	public void removeCodingScheme(CodingScheme revisedCodingScheme);
	
	public CodingScheme resolveCodingSchemeByRevision(String codingSchemeURI,
			String version, String revisionId) throws LBRevisionException;
}

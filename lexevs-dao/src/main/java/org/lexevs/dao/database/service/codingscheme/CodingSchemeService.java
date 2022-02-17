
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
	
	/** The Constant INSERT_CODINGSCHEME_ERROR. */
	public static final String INSERT_CODINGSCHEME_ERROR = "INSERT-CODING-SCHEME-ERROR";
	
	/** The Constant REMOVE_CODINGSCHEME_ERROR. */
	public static final String REMOVE_CODINGSCHEME_ERROR = "REMOVE-CODING-SCHEME-ERROR";
	
	/** The Constant INSERT_CODINGSCHEME_URI_ERROR. */
	public static final String INSERT_CODINGSCHEME_URI_ERROR = "INSERT-CODING-SCHEME-URI-ERROR";
	
	/** The Constant UPDATE_CODINGSCHEME_URI_ERROR. */
	public static final String UPDATE_CODINGSCHEME_URI_ERROR = "UPDATE-CODING-SCHEME-URI-ERROR";
	
	/** The Constant UPDATE_CODINGSCHEME_ERROR. */
	public static final String UPDATE_CODINGSCHEME_ERROR = "UPDATE-CODING-SCHEME-ERROR";
	
	/** The Constant UPDATE_CODINGSCHEME_ENTRYSTATE_ERROR. */
	public static final String UPDATE_CODINGSCHEME_ENTRYSTATE_ERROR = "UPDATE-CODING-SCHEME-ENTRYSTATE-ERROR";
	
	/** The Constant INSERT_CODINGSCHEME_VERSIONABLE_CHANGES_ERROR. */
	public static final String INSERT_CODINGSCHEME_VERSIONABLE_CHANGES_ERROR = "INSERT-CODING-SCHEME-VERSIONABLE-CHANGES-ERROR";
	
	/** The Constant INSERT_CODINGSCHEME_DEPENDENT_CHANGES_ERROR. */
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
	
	/**
	 * Returns entire codingScheme.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * 
	 * @return the complete coding scheme
	 */
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
	 * @param releaseURI the release uri
	 * 
	 * @throws CodingSchemeAlreadyLoadedException the coding scheme already loaded exception
	 */
	public void insertCodingScheme(
			CodingScheme scheme, String releaseURI) throws CodingSchemeAlreadyLoadedException;
	
	/**
	 * Update coding scheme.
	 * 
	 * @param codingScheme the coding scheme
	 * 
	 * @throws LBException the LB exception
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
	
	/**
	 * Update uri map.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param uriMap the uri map
	 */
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
	 * @param revisedCodingScheme the revised coding scheme
	 * @param releaseURI the release uri
	 * @param indexNewCodingScheme the index new coding scheme
	 * 
	 * @throws LBException the LB exception
	 */
	public void revise(CodingScheme revisedCodingScheme, String releaseURI, Boolean indexNewCodingScheme) throws LBException;

	/**
	 * Removes the coding scheme.
	 * 
	 * @param revisedCodingScheme the revised coding scheme
	 */
	public void removeCodingScheme(CodingScheme revisedCodingScheme);
	
	/**
	 * Resolve coding scheme by revision.
	 * 
	 * @param codingSchemeURI the coding scheme uri
	 * @param version the version
	 * @param revisionId the revision id
	 * 
	 * @return the coding scheme
	 * 
	 * @throws LBRevisionException the LB revision exception
	 */
	public CodingScheme resolveCodingSchemeByRevision(String codingSchemeURI,
			String version, String revisionId) throws LBRevisionException;
}
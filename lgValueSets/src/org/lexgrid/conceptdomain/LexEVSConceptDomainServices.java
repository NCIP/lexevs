
package org.lexgrid.conceptdomain;

import java.io.Serializable;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;

public interface LexEVSConceptDomainServices extends Serializable {

/**
	 * Gets the concept domain coding scheme.
	 * 
	 * @param codingSchemeNameOrURI concept domain coding scheme name or uri
	 * @param versionOrTag concept domain coding scheme version or tag
	 * @return the concept domain coding scheme
	 * @throws LBException
	 */
public CodingScheme getConceptDomainCodingScheme(String codingSchemeNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Gets the concept domain coding scheme summary.
	 * 
	 * @param codingSchemeNameOrURI concept domain coding scheme name or uri
	 * @param versionOrTag concept domain coding scheme version or tag
	 * @return the concept domain coding scheme summary
	 * @throws LBException
	 */
	public CodingSchemeSummary getConceptDomainCodingSchemeSummary(String codingSchemeNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Returns concept domain entity object of the concept domain id.
	 * 
	 * @param conceptDomainId  id of concept domain
	 * @param namespace namespace of the concept domain
	 * @param codingSchemeNameOrURI concept domain coding scheme name or uri
	 * @param versionOrTag concept domain coding scheme version or tag
	 * @return concept domain entity object
	 * @throws LBException
	 */
	public Entity getConceptDomainEntity(String conceptDomainId, String namespace, String codingSchemeNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Returns list of concept domain entities matching the name provided.
	 * @param conceptDomainName 
	 * 			  match name of concept domain
	 * @param codingSchemeNameOrURI 
	 * 			  concept domain coding scheme name or uri
	 * @param versionOrTag 
	 * 			  concept domain coding scheme version or tag
	 * @param option 
	 *            Indicates the designations to search (one of the enumerated
	 *            type SearchDesignationOption).
	 * @param matchAlgorithm
	 *            Local name of the match algorithm - possible algorithms are
	 *            returned in LexBigService.getMatchAlgorithms().
	 * @param language
	 *            Language of search string. If missing, use the default
	 *            language specified in the context.
	 * @return list of entities containing matching concept domain name
	 * @throws LBException
	 */
	public List<Entity> getConceptDomainEntitisWithName(String conceptDomainName, String codingSchemeNameOrURI, CodingSchemeVersionOrTag versionOrTag, SearchDesignationOption option, String matchAlgorithm, String language) throws LBException;
	
	/**
	 * Returns coded node set for concept domain entities.
	 * 
	 * @param codingSchemeNameOrURI 
	 * 			  concept domain coding scheme name or uri
	 * @param versionOrTag 
	 * 			  concept domain coding scheme version or tag
	 * @return codedNodeSet of concept domain entities
	 * @throws LBException
	 */
	public CodedNodeSet getConceptDomainCodedNodeSet(String codingSchemeNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Gets all the concept domain found in the system as entities.
	 * 
	 * @param codingSchemeNameOrURI 
	 * 			  concept domain coding scheme name or uri
	 * @param versionOrTag 
	 * 			  concept domain coding scheme version or tag
	 * @return List of concept domain entities
	 * @throws LBException
	 */
	public List<Entity> listAllConceptDomainEntities(String codingSchemeNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Returns all the concept domain identifiers found in the system.
	 * 
	 * @param codingSchemeNameOrURI 
	 * 			  concept domain coding scheme name or uri
	 * @param versionOrTag 
	 * 			  concept domain coding scheme version or tag
	 * @return List of concept domain identifiers
	 * @throws LBException
	 */
	public List<String> listAllConceptDomainIds(String codingSchemeNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
//	/**
//	 * Inserts concept domain into concept domain coding scheme.
//	 * 
//	 * @param conceptDomainId - 
//	 * 				Id of concept domain
//	 * @param conceptDomainName -
//	 * 				concept domain name
//	 * @param revisionId - 
//	 * 				revision id of concept domain
//	 * @param description - 
//	 * 				concept domain description
//	 * @param status - 
//	 * 				concept domain status
//	 * @param isActive -
//	 * 				is concept domain active
//	 * @param properties -
//	 * 				concept domain properties
//	 * @param versionOrTag -
//	 * 			  concept domain coding scheme version or tag
//	 * 
//	 * @throws LBException
//	 */
//	public void insertConceptDomain(String conceptDomainId, String conceptDomainName, 
//			String revisionId, String description, String status, boolean isActive, Properties properties, CodingSchemeVersionOrTag versionOrTag) throws LBException;
//	
//	/**
//	 * Inserts concept domain entity into concept domain coding scheme.
//	 * 
//	 * @param conceptDomain - 
//	 * 				concept domain entity object to be inserted
//	 * @param versionOrTag -
//	 * 			  concept domain coding scheme version or tag
//	 * @throws LBException
//	 */
//	public void insertConceptDomain(Entity conceptDomain, CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Returns list of value set definition URIs that are bound to given concept domain.
	 * 
	 * @param conceptDomainId -
	 * 			  Identifier of the concept domain
	 * @param codingSchemeURI 
	 * 			  concept domain coding scheme URI
	 * @return list of value set definition URIs
	 * @throws LBException
	 */
	public List<String> getConceptDomainBindings(String conceptDomainId, String codingSchemeURI) throws LBException;
	
	/**
	 * Determines whether the supplied coded concept exists in a code system in use for the specified concept domain, 
	 * optionally within specific usage contexts.
	 * 
	 * Returns true if a coded concept is an element of a value set expansion bound to the provided concept domain, 
	 * or bound to both concept domain and usage context.
	 * 
	 * @param conceptDomainId - id of concept domain	  
	 * @param namespace - concept domain namespace
	 * @parma codingSchemeURI - URI of coding scheme the concept domain belongs to
	 * @param entityCode - entity code to check if it participates in concept domain
	 * @param codingSchemeVersionList - list of coding scheme URI and version that contains the entity code 
	 * 			and to be used to resolve.
	 * @param usageContext - (Optional) list of usage context
	 * @return list of value set definition URIs that are bound to concept domain (and usageContext) 
	 * 			and that contains given entity code.
	 * @throws LBException
	 */
	public List<String> isEntityInConceptDomain(String conceptDomainId, String namespace, String codingSchemURI, String entityCode, AbsoluteCodingSchemeVersionReferenceList codingSchemeVersionList, List<String> usageContext) 
		throws LBException;
	
}
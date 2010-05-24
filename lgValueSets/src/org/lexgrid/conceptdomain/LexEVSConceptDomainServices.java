package org.lexgrid.conceptdomain;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.concepts.Entity;

public interface LexEVSConceptDomainServices {
	
	/**
	 * Gets the concept domain coding scheme.
	 * 
	 * @return the concept domain coding scheme
	 * @throws LBException
	 */
	public CodingScheme getConceptDomainCodingScheme() throws LBException;
	
	/**
	 * Returns concept domain entity object of the concept domain id.
	 * 
	 * @param conceptDomainId  id of concept domain
	 * @return concept domain entity object
	 * @throws LBException
	 */
	public Entity getConceptDomainEntity(String conceptDomainId) throws LBException;
	
	/**
	 * Returns list of concept domain entities matching the name provided.
	 * @param conceptDomainName - match name of concept domain
	 * @return list of entities containing matching concept domain name
	 * @throws LBException
	 */
	public List<Entity> getConceptDomainEntitisWithName(String conceptDomainName) throws LBException;
	
	/**
	 * Returns coded node set for concept domain entities.
	 * 
	 * @return concept domain of concept domain entities
	 * @throws LBException
	 */
	public CodedNodeSet getConceptDomainCodedNodeSet() throws LBException;
	
	/**
	 * Gets all the concept domain found in the system as entities.
	 * 
	 * @return List of concept domain entities
	 * @throws LBException
	 */
	public List<Entity> listAllConceptDomainEntities() throws LBException;
	
	/**
	 * Returns all the concept domain identifiers found in the system.
	 * 
	 * @return List of concept domain identifiers
	 * @throws LBException
	 */
	public List<String> listAllConceptDomainIds() throws LBException;
	
	/**
	 * Inserts concept domain into concept domain coding scheme.
	 * 
	 * @param conceptDomainId - 
	 * 				Id of concept domain
	 * @param conceptDomainName -
	 * 				concept domain name
	 * @param description - 
	 * 				concept domain description
	 * @param status - 
	 * 				concept domain status
	 * @param properties -
	 * 				concept domain properties
	 * 
	 * @throws LBException
	 */
	public void insertConceptDomain(String conceptDomainId, String conceptDomainName, 
			String description, String status, Properties properties) throws LBException;
	
	/**
	 * Inserts concept domain entity into concept domain coding scheme.
	 * 
	 * @param conceptDomain - 
	 * 				concept domain entity object to be inserted
	 * 
	 * @throws LBException
	 */
	public void insertConceptDomain(Entity conceptDomain) throws LBException;
	
	/**
	 * Returns list of value set definition URIs that are bound to given concept domain.
	 * 
	 * @param conceptDomainId
	 * @return list of value set definition URIs
	 * @throws LBException
	 */
	public List<String> getValueSetDefinitionURIsForConceptDomain(String conceptDomainId) throws LBException;
	
	/**
	 * Determines whether the supplied coded concept exists in a code system in use for the specified concept domain, 
	 * optionally within specific usage contexts.
	 * 
	 * Returns true if a coded concept is an element of a value set expansion bound to the provided concept domain, 
	 * or bound to both concept domain and usage context.
	 * 
	 * @param conceptDomainId - id of concept domain	  
	 * @param entityCode - entity code to check if it participates in concept domain
	 * @param codingSchemeVersionList - list of coding scheme URI and version that contains the entity code 
	 * 			and to be used to resolve.
	 * @param usageContext - (Optional) list of usage context
	 * @return list of value set definition URIs that are bound to concept domain (and usageContext) 
	 * 			and that contains given entity code.
	 * @throws LBException
	 */
	public List<String> isEntityInConceptDomain(String conceptDomainId, String entityCode, AbsoluteCodingSchemeVersionReferenceList codingSchemeVersionList, List<String> usageContext) 
		throws LBException;
	
	/**
	 * Removed concept domain from the concept domain coding scheme.
	 * 
	 * @param conceptDomainId - 
	 * 				Identifier of the concept domain that will be removed
	 * @throws LBException
	 */
	public void removeConceptDomain(String conceptDomainId) throws LBException;
	
}

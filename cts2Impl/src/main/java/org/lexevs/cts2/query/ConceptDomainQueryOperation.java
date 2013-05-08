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
package org.lexevs.cts2.query;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;

/**
 * LexEVS CTS 2 Concept Domain Query Operations.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface ConceptDomainQueryOperation {
	
	/**
	 * Gets the concept domain coding scheme.
	 * @param codeSystemNameOrURI concept domain coding scheme name or URI
	 * @param codeSystemVersion concept domain coding scheme version
	 * @return the concept domain coding scheme
	 * @throws LBException
	 */
	public CodingScheme getConceptDomainCodingScheme(String codeSystemNameOrURI, String codeSystemVersion) throws LBException;
	
	/**
	 * Returns concept domain entity object of the concept domain id.
	 * 
	 * @param conceptDomainId  id of concept domain
	 * @param namespace concept domain name space
	 * @param codeSystemNameOrURI concept domain coding scheme name or URI
	 * @param codeSystemVersion concept domain coding scheme version
	 * @return concept domain entity object
	 * @throws LBException
	 */
	public Entity getConceptDomainEntity(String conceptDomainId, String namespace, String codeSystemNameOrURI, String codeSystemVersion) throws LBException;
	
	/**
	 * Returns list of concept domain entities matching the name provided.
	 * @param conceptDomainName 
	 * 			  match name of concept domain
	 * @param codeSystemNameOrURI 
	 * 			  concept domain coding scheme name or URI
	 * @param codeSystemVersion 
	 * 			  concept domain coding scheme version
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
	public List<Entity> getConceptDomainEntitisWithName(String conceptDomainName, String codeSystemNameOrURI, String codeSystemVersion, SearchDesignationOption option, String matchAlgorithm, String language) throws LBException;
	
	/**
	 * Returns coded node set for concept domain entities.
	 * 
	 * @param codeSystemNameOrURI concept domain coding scheme name or URI
	 * @param codeSystemVersion concept domain coding scheme version
	 * @return codedNodeSet of concept domain entities
	 * @throws LBException
	 */
	public CodedNodeSet getConceptDomainCodedNodeSet(String codeSystemNameOrURI, String codeSystemVersion) throws LBException;
	
	/**
	 * Gets all the concept domain found in the system as entities.
	 * 
	 * @param codeSystemNameOrURI concept domain coding scheme name or URI
	 * @param codeSystemVersion concept domain coding scheme version
	 * @return List of concept domain entities
	 * @throws LBException
	 */
	public List<Entity> listAllConceptDomainEntities(String codeSystemNameOrURI, String codeSystemVersion) throws LBException;
	
	/**
	 * Returns all the concept domain identifiers found in the system.
	 * 
	 * @param codeSystemNameOrURI concept domain coding scheme name or URI
	 * @param codeSystemVersion concept domain coding scheme version
	 * @return List of concept domain identifiers
	 * @throws LBException
	 */
	public List<String> listAllConceptDomainIds(String codeSystemNameOrURI, String codeSystemVersion) throws LBException;
	
	/**
	 * Returns list of value set definition URIs that are bound to given concept domain.
	 * 
	 * @param conceptDomainId -
	 * 			  Identifier of the concept domain
	 * @param codeSystemNameOrURI -
	 * 			  coding scheme name or URI to which the concept domain belongs to
	 * @return list of value set definition URIs
	 * @throws LBException
	 */
	public List<String> getConceptDomainBindings(String conceptDomainId, String codeSystemNameOrURI) throws LBException;
	
	/**
	 * Determines whether the supplied coded concept exists in a code system in use for the specified concept domain, 
	 * optionally within specific usage contexts.
	 * 
	 * Returns true if a coded concept is an element of a value set expansion bound to the provided concept domain, 
	 * or bound to both concept domain and usage context.
	 * 
	 * @param conceptDomainId - id of concept domain	
	 * @param namespace - code system namespace
	 * @param codeSystemNameOrURI - concept domain code system name or URI  
	 * @param entityCode - entity code to check if it participates in concept domain
	 * @param codingSchemeVersionList - list of coding scheme URI and version that contains the entity code 
	 * 			and to be used to resolve.
	 * @param usageContext - (Optional) list of usage context
	 * @return list of value set definition URIs that are bound to concept domain (and usageContext) 
	 * 			and that contains given entity code.
	 * @throws LBException
	 */
	public List<String> isEntityInConceptDomain(String conceptDomainId, String namespace, String codeSystemNameOrURI, String entityCode, AbsoluteCodingSchemeVersionReferenceList codingSchemeVersionList, List<String> usageContext) 
		throws LBException;
}
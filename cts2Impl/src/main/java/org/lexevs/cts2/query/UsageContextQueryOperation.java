/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and Research
 * (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the triple-shield Mayo
 * logo are trademarks and service marks of MFMER.
 * 
 * Except as contained in the copyright notice above, or as used to identify
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.lexevs.cts2.query;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;

/**
 * LexEVS Implementation of CTS2 Usage Context Query Operation.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface UsageContextQueryOperation {
	/**
	 * Gets the Usage Context coding scheme.
	 * @param codeSystemNameOrURI usageContext code system name or URI
	 * @param versionOrTag usageContext coding scheme version or tag
	 * @return the usage context coding scheme
	 * @throws LBException
	 */
	public CodingScheme getUsageContextCodingScheme(String codeSystemNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Returns UsageContext entity object with usageContextId.
	 * 
	 * @param usageContextId  id of usage context
	 * @param namespace of usage context
	 * @param codeSystemNameOrURI usageContext code system name or URI
	 * @param versionOrTag usageContext coding scheme version or tag
	 * @return usage context entity object
	 * @throws LBException
	 */
	public Entity getUsageContextEntity(String usageContextId, String namespace, String codeSystemNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Returns list of usage context entities matching the name provided.
	 * @param usageContextName 
	 * 			  match name of usage context
	 * @param codeSystemNameOrURI 
	 * 			  usageContext code system name or URI
	 * @param versionOrTag 
	 * 			  usage context coding scheme version or tag
	 * @param option 
	 *            Indicates the designations to search (one of the enumerated
	 *            type SearchDesignationOption).
	 * @param matchAlgorithm
	 *            Local name of the match algorithm - possible algorithms are
	 *            returned in LexBigService.getMatchAlgorithms().
	 * @param language
	 *            Language of search string. If missing, use the default
	 *            language specified in the context.
	 * @return list of entities containing matching usage context name
	 * @throws LBException
	 */
	public List<Entity> getUsageContextEntitisWithName(String usageContextName, String codeSystemNameOrURI, CodingSchemeVersionOrTag versionOrTag, SearchDesignationOption option, String matchAlgorithm, String language) throws LBException;
	
	/**
	 * Returns coded node set for usage context entities.
	 * 
	 * @param codeSystemNameOrURI 
	 * 			  usageContext code system name or URI
	 * @param versionOrTag 
	 * 			  usage context coding scheme version or tag
	 * @return codedNodeSet of usage context entities
	 * @throws LBException
	 */
	public CodedNodeSet getUsageContextCodedNodeSet(String codeSystemNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Gets all the usage context found in the system as entities.
	 * 
	 * @param codeSystemNameOrURI 
	 * 			  usageContext code system name or URI
	 * @param versionOrTag 
	 * 			  usage context coding scheme version or tag
	 * @return List of usage context entities
	 * @throws LBException
	 */
	public List<Entity> listAllUsageContextEntities(String codeSystemNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Returns all the usage context identifiers found in the system.
	 * 
	 * @param codeSystemNameOrURI 
	 * 	 		  usageContext code system name or URI
	 * @param versionOrTag 
	 * 			  usage context coding scheme version or tag
	 * @return List of usage context identifiers
	 * @throws LBException
	 */
	public List<String> listAllUsageContextIds(String codeSystemNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException;
}

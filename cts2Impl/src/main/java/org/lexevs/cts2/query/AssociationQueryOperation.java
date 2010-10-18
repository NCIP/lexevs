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

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.lexevs.dao.database.service.association.AssociationService.AssociationTriple;

public interface AssociationQueryOperation {

/**
	 * Returns the resolved concept reference (which contains the associations) according to given node.
	 * @param codingSystemName
	 * @param versionOrTag
	 * @param namespace
	 * @param code
	 * @param associationName
	 * @param isBackward
	 * @param depth
	 * @param maxToReturn
	 * @return ResolvedConceptReferenceList
	 */
public ResolvedConceptReferenceList listAssociations(
			String codingSystemName, CodingSchemeVersionOrTag versionOrTag,
			String namespace, String code, String associationName,
			boolean isBackward, int depth, int maxToReturn);

	/**
	 * Returns the path according to given two nodes.
	 * @param codingSystemUri
	 * @param versionOrTag
	 * @param relationContainerName
	 * @param associationName
	 * @param sourceCode
	 * @param sourceNS
	 * @param targetCode
	 * @param targetNS
	 * @return ResolvedConceptReference
	 */
	public ResolvedConceptReference determineTransitiveConceptRelationship(
			String codingSystemUri, CodingSchemeVersionOrTag versionOrTag,
			String relationContainerName, String associationName,
			String sourceCode, String sourceNS, String targetCode,
			String targetNS);

	/**
	 * Return whether the two nodes has a transitive closure path
	 * @param codingSystemName
	 * @param versionOrTag
	 * @param associationtype
	 * @param parentCode
	 * @param childCode
	 * @return boolean
	 */
	public boolean computeSubsumptionRelationship(String codingSystemName,
			CodingSchemeVersionOrTag versionOrTag, String associationtype,
			ConceptReference sourceCode, ConceptReference targetCode);

	/**
	 * Return association triple according to association instance id
	 * @param codingSchemeUri
	 * @param versionOrTag
	 * @param associationInstanceId
	 * @return AssociationTriple
	 */
	public AssociationTriple getAssociationDetails(String codingSchemeUri,
			CodingSchemeVersionOrTag versionOrTag,
			String associationInstanceId);
}
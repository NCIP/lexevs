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
package org.lexevs.dao.database.access.codednodegraph;

import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.CodeNamespacePair;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.QualifierNameValuePair;

/**
 * The Interface CodedNodeGraphDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CodedNodeGraphDao extends LexGridSchemaVersionAwareDao {
	
	public enum TripleNode {SUBJECT, OBJECT}
	
	public List<String> getTripleUidsContainingSubject(
			String codingSchemeUid,
			String associationPredicateUid,
			String subjectEntityCode,
			String subjectEntityCodeNamespace, 
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveObjectCodes,
			int start, 
			int pageSize);
	
	public Map<String,Integer> getTripleUidsContainingSubjectCount(
			String codingSchemeUid,
			String relationsContainerName,
			String subjectEntityCode,
			String subjectEntityCodeNamespace,
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveObjectCodes);
	
	public List<String> getTripleUidsContainingObject(
			String codingSchemeUid,
			String associationPredicateUid,
			String objectEntityCode,
			String objectEntityCodeNamespace, 
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveSubjectCodes,
			int start, 
			int pageSize);
	
	public Map<String,Integer> getTripleUidsContainingObjectCount(
			String codingSchemeUid,
			String relationsContainerName,
			String objectEntityCode,
			String objectEntityCodeNamespace,
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveSubjectCodes);
	
	public List<AssociatedConcept> getAssociatedConceptsFromUid(
			String codingSchemeUid, List<String> tripleUids, TripleNode tripleNode);
	
	public List<ConceptReference> getConceptReferencesFromUid(
			String codingSchemeUid, List<String> tripleUids, TripleNode tripleNode);
	
	public List<String> getAssociationPredicateNamesForCodingSchemeUid(
			String codingSchemeUid,
			String relationsContainerName);
	
	public List<Node> getDistinctSourceNodesForAssociationPredicate(
			String codingSchemeUid, 
			String associationPredicateUid);
	
	public List<Node> getTargetNodesForSource(
			String codingSchemeUid, 
			String associationPredicateUid, 
			String sourceEntityCode, 
			String sourceEntityCodeNamespace);
	
	public List<ConceptReference> getTailNodes(
			String codingSchemeUid,
			List<String> associationPredicateUids);
	
	public List<ConceptReference> getRootNodes(
			String codingSchemeUid,
			List<String> associationPredicateUids);
}

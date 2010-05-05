package org.lexevs.dao.database.service.codednodegraph;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;

public interface CodedNodeGraphService {
	
	public List<String> getTripleUidsContainingSubject(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			String associationPredicateName,
			String subjectEntityCode,
			String subjectEntityCodeNamespace, 
			GraphQuery query,
			int start, 
			int pageSize);
	
	public int getTripleUidsContainingSubjectCount(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			String associationPredicateName,
			String subjectEntityCode,
			String subjectEntityCodeNamespace,
			GraphQuery query);
	
	public List<String> getTripleUidsContainingObject(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			String associationPredicateName,
			String objectEntityCode,
			String objectEntityCodeNamespace, 
			GraphQuery query,
			int start, 
			int pageSize);
	
	public int getTripleUidsContainingObjectCount(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			String associationPredicateName,
			String objectEntityCode,
			String objectEntityCodeNamespace,
			GraphQuery query);
	
	public AssociatedConcept getAssociatedConceptFromUidSource(
			String codingSchemeUri,
			String codingSchemeVersion,
			String tripleUid);
	
	public List<AssociatedConcept> getAssociatedConceptsFromUidSource(
			String codingSchemeUri,
			String codingSchemeVersion,
			List<String> tripleUid);
	
	public List<ConceptReference> getConceptReferencesFromUidSource(
			String codingSchemeUri,
			String codingSchemeVersion,
			List<String> tripleUid);
	
	public AssociatedConcept getAssociatedConceptFromUidTarget(
			String codingSchemeUri,
			String codingSchemeVersion,
			String tripleUid);
	
	public List<AssociatedConcept> getAssociatedConceptsFromUidTarget(
			String codingSchemeUri,
			String codingSchemeVersion,
			List<String> tripleUid);
	
	public List<ConceptReference> getConceptReferencesFromUidTarget(
			String codingSchemeUri,
			String codingSchemeVersion,
			List<String> tripleUid);
	
	public List<String> getAssociationPredicateNamesForCodingScheme(
			String codingSchemeUri,
			String codingSchemeVersion);
	
}

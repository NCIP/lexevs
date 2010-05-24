package org.lexevs.dao.database.service.codednodegraph;

import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.TraverseAssociations;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;

public interface CodedNodeGraphService {
	
	public List<String> listCodeRelationships(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			String sourceEntityCode,
			String sourceEntityCodeNamespace, 
			String targetEntityCode,
			String targetEntityCodeNamespace, 
			GraphQuery query,
			boolean useTransitive);

	public List<ConceptReference> getRootConceptReferences(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			List<String> associationPredicateNames, 
			TraverseAssociations traverse);
	
	public List<ConceptReference> getTailConceptReferences(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			List<String> associationPredicateNames,
			TraverseAssociations traverse);
	
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
	
	public Map<String, Integer> getTripleUidsContainingSubjectCount(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
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
	
	public Map<String, Integer> getTripleUidsContainingObjectCount(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			String objectEntityCode,
			String objectEntityCodeNamespace,
			GraphQuery query);
	
	public AssociatedConcept getAssociatedConceptFromUidSource(
			String codingSchemeUri,
			String codingSchemeVersion,
			boolean resolve,
			LocalNameList propertyNames, 
	        PropertyType[] propertyTypes, 
			String tripleUid);
	
	public List<? extends AssociatedConcept> getAssociatedConceptsFromUidSource(
			String codingSchemeUri,
			String codingSchemeVersion,
			boolean resolve,
			LocalNameList propertyNames, 
	        PropertyType[] propertyTypes, 
			List<String> tripleUid);
	
	public List<ConceptReference> getConceptReferencesFromUidSource(
			String codingSchemeUri,
			String codingSchemeVersion,
			List<String> tripleUid);
	
	public AssociatedConcept getAssociatedConceptFromUidTarget(
			String codingSchemeUri,
			String codingSchemeVersion,
			boolean resolve,
			LocalNameList propertyNames, 
	        PropertyType[] propertyTypes, 
			String tripleUid);
	
	public List<? extends AssociatedConcept> getAssociatedConceptsFromUidTarget(
			String codingSchemeUri,
			String codingSchemeVersion,
			boolean resolve,
			LocalNameList propertyNames, 
	        PropertyType[] propertyTypes, 
			List<String> tripleUid);
	
	public List<ConceptReference> getConceptReferencesFromUidTarget(
			String codingSchemeUri,
			String codingSchemeVersion,
			List<String> tripleUid);
	
	public List<String> getAssociationPredicateNamesForCodingScheme(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName);
	
	public List<String> getAssociationPredicateUidsForNames(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			List<String> associationNames);
}




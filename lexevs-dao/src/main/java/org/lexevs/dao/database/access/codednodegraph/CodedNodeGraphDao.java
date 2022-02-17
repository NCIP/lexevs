
package org.lexevs.dao.database.access.codednodegraph;

import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.custom.relations.TerminologyMapBean;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.access.association.model.Sextuple;
import org.lexevs.dao.database.ibatis.codednodegraph.model.EntityReferencingAssociatedConcept;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.TraverseAssociations;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService.Sort;
import org.lexevs.dao.database.service.codednodegraph.model.CountConceptReference;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.CodeNamespacePair;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.QualifierNameValuePair;

/**
 * The Interface CodedNodeGraphDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CodedNodeGraphDao extends LexGridSchemaVersionAwareDao {
	
	public enum TripleNode {SUBJECT, OBJECT}
	
	public List<String> listCodeRelationships(
			String codingSchemeUid,
			String relationsContainerName,
			String sourceEntityCode,
			String sourceEntityCodeNamespace, 
			String targetEntityCode,
			String targetEntityCodeNamespace, 
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveSourceCodes,
			List<CodeNamespacePair> mustHaveTargetCodes,
			List<String> mustHaveSourceNamespace,
			List<String> mustHaveTargetNamespace,
			List<String> mustHaveEntityType,
			Boolean restrictToAnonymous,
			boolean useTransitive);
	
	public List<String> getTripleUidsContainingSubject(
			String codingSchemeUid,
			String associationPredicateUid,
			String subjectEntityCode,
			String subjectEntityCodeNamespace, 
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveObjectCodes,
			List<String> mustHaveObjectNamespace,
			List<String> mustHaveObjectEntityType,
			Boolean restrictToAnonymous,
			List<Sort> sorts,
			int start, 
			int pageSize);
	
	public Map<String,Integer> getTripleUidsContainingSubjectCount(
			String codingSchemeUid,
			String relationsContainerName,
			String subjectEntityCode,
			String subjectEntityCodeNamespace,
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveObjectCodes,
			List<String> mustHaveObjectNamespace,
			List<String> mustHaveObjectEntityType,
			Boolean restrictToAnonymous);
	
	public List<CountConceptReference> getCountConceptReferencesContainingSubject(
			String codingSchemeUid,
			String relationsContainerName,
			List<ConceptReference> subjects,
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveObjectCodes,
			List<String> mustHaveObjectNamespace,
			List<String> mustHaveObjectEntityType,
			Boolean restrictToAnonymous);
			
	public List<ConceptReference> getConceptReferencesContainingSubject(
			String codingSchemeUid,
			String relationsContainerName,
			List<ConceptReference> subjects,
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveObjectCodes,
			List<String> mustHaveObjectNamespace,
			List<String> mustHaveObjectEntityType,
			Boolean restrictToAnonymous,
			List<Sort> sorts,
			int start,
			int pageSize);
	
	public List<String> getTripleUidsContainingObject(
			String codingSchemeUid,
			String associationPredicateUid,
			String objectEntityCode,
			String objectEntityCodeNamespace, 
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveSubjectCodes,
			List<String> mustHaveSubjectNamespace,
			List<String> mustHaveSubjectEntityType,
			Boolean restrictToAnonymous,
			List<Sort> sorts,
			int start, 
			int pageSize);
	
	public Map<String,Integer> getTripleUidsContainingObjectCount(
			String codingSchemeUid,
			String relationsContainerName,
			String objectEntityCode,
			String objectEntityCodeNamespace,
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveSubjectCodes,
			List<String> mustHaveSubjectNamespace,
			List<String> mustHaveSubjectEntityType,
			Boolean restrictToAnonymous);
	
	public List<ConceptReference> getConceptReferencesContainingObject(
			String codingSchemeUid,
			String relationsContainerName,
			List<ConceptReference> objects,
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveSubjectCodes,
			List<String> mustHaveSubjectNamespace,
			List<String> mustHaveSubjectEntityType,
			Boolean restrictToAnonymous,
			List<Sort> sorts,
			int start,
			int pageSize);
			
	public List<CountConceptReference> getCountConceptReferencesContainingObject(
			String codingSchemeUid,
			String relationsContainerName,
			List<ConceptReference> objects,
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveSubjectCodes,
			List<String> mustHaveSubjectNamespace,
			List<String> mustHaveObjectEntityType,
			Boolean restrictToAnonymous);
	
	public List<EntityReferencingAssociatedConcept> getAssociatedConceptsFromUid(
			String codingSchemeUid, 
			List<String> tripleUids, 
			List<Sort> sorts, 
			TripleNode tripleNode);
	
	public List<ConceptReference> getConceptReferencesFromUid(
			String codingSchemeUid, 
			List<String> tripleUids, 
			TripleNode tripleNode, 
			List<Sort> sorts);
	
	public List<String> getAssociationPredicateNamesForCodingSchemeUid(
			String codingSchemeUid,
			String relationsContainerName);
	
	public List<Node> getDistinctSourceNodesForAssociationPredicate(
			String codingSchemeUid, 
			String associationPredicateUid);
	
	public List<Node> getDistinctTargetNodesForAssociationPredicate(
			String codingSchemeUid, 
			String associationPredicateUid);
	
	public List<Node> getTargetNodesForSource(
			String codingSchemeUid, 
			String associationPredicateUid, 
			String sourceEntityCode, 
			String sourceEntityCodeNamespace);
	
	public List<Node> getSourceNodesForTarget(
			String codingSchemeUid, 
			String associationPredicateUid, 
			String targetEntityCode, 
			String targetEntityCodeNamespace);

	public List<ConceptReference> getTailNodes(
			String codingSchemeUid,
			List<String> associationPredicateUids, 
			List<QualifierNameValuePair> qualifiers, 
			List<String> mustHaveSubjectNamespace,
			List<String> mustHaveObjectNamespace,
			TraverseAssociations traverse,
			List<Sort> sorts, 
			int start,
			int pageSize);
	
	public List<ConceptReference> getRootNodes(
			String codingSchemeUid,
			List<String> associationPredicateUids, 
			List<QualifierNameValuePair> qualifiers, 
			List<String> mustHaveSubjectNamespace,
			List<String> mustHaveObjectNamespace,
			TraverseAssociations traverse,
			List<Sort> sorts, 
			int start,
			int pageSize);

	public List<String> getTripleUidsForMappingRelationsContainer(
			String mappingCodingSchemeUid, 
			String sourceCodingSchemeUid,
			String targetCodingSchemeUid, 
			String relationsContainerName,
			List<Sort> sortOptionList,
			int start, 
			int pageSize);
	
	public List<String> getTripleUidsForMappingRelationsContainerAndCodes(
			String mappingCodingSchemeUid, 
			String sourceCodingSchemeUid,
			String targetCodingSchemeUid, 
			String relationsContainerName,
			List<ConceptReference> sourceConceptReferences,
			List<ConceptReference> targetConceptReferences,
			List<ConceptReference> sourceOrTargetConceptReferences,
			List<Sort> sortList, 
			int start, 
			int pageSize);
	
	public List<String> getTripleUidsForMappingRelationsContainerAndCodes(
			String mappingCodingSchemeUid, 
			String relationsContainerName,
			List<ConceptReference> sourceConceptReferences,
			List<ConceptReference> targetConceptReferences,
			List<ConceptReference> sourceOrTargetConceptReferences);
	
	public List<? extends ResolvedConceptReference> getTriplesForMappingRelationsContainer(
			String mappingCodingSchemeUid, 
			String sourceCodingSchemeUid,
			String targetCodingSchemeUid, 
			String relationsContainerName,
			List<String> tripleUids);
	
	public int getTriplesForMappingRelationsContainerAndCodesCount(
			String mappingCodingSchemeUid, 
			String relationsContainerName,
			List<ConceptReference> sourceConceptReferences,
			List<ConceptReference> targetConceptReferences,
			List<ConceptReference> sourceOrTargetConceptReferences);
	
	public int getTriplesForMappingRelationsContainerCount(
			String mappingCodingSchemeUid, 
			String relationsContainerName);

	public boolean doesEntityParticipateInRelationships(
			String mappingCodingSchemeUid,			
			String relationsContainerName,
			String code, 
			String namespace);

	public int getTransitiveTableCount(String codingSchemeUid);
	
	public int deleteFromTransitiveTableByCodingSchemeUid(String codingSchemeUid);

	public List<TerminologyMapBean> getMapAndTermsForMappingAndReferences(String mappingCodingSchemUid,
			String sourceCodingSchemeUid, String targetCodingSchemeUid, Relations rel, String qualifierName);

	public List<Triple> getTriplesForMappingRelationsContainer(String mappingCodingSchemeUid,
			String relationsContainerName);

	public List<Triple> getValidTriplesOfAssociation(String codingSchemeUid, String assocUid);

	public Integer validateNodeInAssociation(String codingSchemeUid, String assocUid, String entityCode);

	public List<String> getValidPredicatesForTargetandSourceOf(String codingSchemeUid, String entityCode);

	public List<Sextuple> getValidSexTuplesOfAssociation(String codingSchemeUid, String assocUid);
}
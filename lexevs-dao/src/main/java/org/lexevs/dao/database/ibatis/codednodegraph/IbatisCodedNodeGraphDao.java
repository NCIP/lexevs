package org.lexevs.dao.database.ibatis.codednodegraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.association.IbatisAssociationDao;
import org.lexevs.dao.database.ibatis.association.parameter.GetCodeRelationshipsBean;
import org.lexevs.dao.database.ibatis.association.parameter.GetEntityAssnUidsBean;
import org.lexevs.dao.database.ibatis.association.parameter.GetEntityAssnUidsCountBean;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterCollection;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.TraverseAssociations;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.CodeNamespacePair;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.QualifierNameValuePair;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.util.CollectionUtils;

public class IbatisCodedNodeGraphDao extends AbstractIbatisDao implements CodedNodeGraphDao {
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	private static String GET_ENTITY_ASSNSTOENTITY_UID_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getEntityAssnsToEntityUids";
	private static String GET_ENTITY_ASSNSTOENTITY_UID_COUNT_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getEntityAssnsToEntityUidsCount";
	private static String GET_ASSOCIATEDCONCEPT_FROM_ASSNSTOENTITY_UID_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getAssociatedConcpetFromEntityAssnsToEntityUid";
	private static String GET_CONCEPTREFERENCE_FROM_ASSNSTOENTITY_UID_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getConceptReferenceFromEntityAssnsToEntityUid";
	private static String GET_ASSOCIATION_PREDICATE_NAMES_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getAssociationPredicatNamesFromCodingSchemeUid";
	private static String GET_DISTINCT_SOURCE_NODES_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getDistinctSources";
	private static String GET_TARGET_NODES_OF_SOURCE_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getTargetsOfSource";
	private static String GET_TAIL_ENTITY_ASSNSTOENTITY_UID_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getTailEntityAssnsToEntityUids";
	private static String GET_ROOT_ENTITY_ASSNSTOENTITY_UID_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getRootEntityAssnsToEntityUids";
	private static String GET_CODE_RELATIONSHIPS_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getCodeRelationships";

	@SuppressWarnings("unchecked")
	@Override
	public List<String> listCodeRelationships(
			String codingSchemeUid,
			String relationsContainerName,
			String sourceEntityCode, String sourceEntityCodeNamespace,
			String targetEntityCode, String targetEntityCodeNamespace,
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveSourceCodes,
			List<CodeNamespacePair> mustHaveTargetCodes,
			List<String> mustHaveSourceNamespace,
			List<String> mustHaveTargetNamespace,
			boolean useTransitive) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		GetCodeRelationshipsBean bean = new GetCodeRelationshipsBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeUid(codingSchemeUid);
		bean.setRelationsContainerName(relationsContainerName);
		bean.setSourceCode(sourceEntityCode);
		bean.setSourceNamespace(sourceEntityCodeNamespace);
		bean.setTargetCode(targetEntityCode);
		bean.setTargetNamespace(targetEntityCodeNamespace);
		bean.setAssociations(associationNames);
		bean.setAssociationQualifiers(associationQualifiers);
		bean.setMustHaveSourceCodes(mustHaveSourceCodes);
		bean.setMustHaveTargetCodes(mustHaveTargetCodes);
		bean.setMustHaveSourceNamespaces(mustHaveSourceNamespace);
		bean.setMustHaveTargetNamespaces(mustHaveTargetNamespace);
		bean.setUseTransitive(useTransitive);
		
		return this.getSqlMapClientTemplate().queryForList(GET_CODE_RELATIONSHIPS_SQL, bean);
	}

	@Override
	public Map<String,Integer> getTripleUidsContainingObjectCount(
			String codingSchemeUid,
			String relationsContainerName,
			String objectEntityCode,
			String objectEntityCodeNamespace,
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveSubjectCodes,
			List<String> mustHaveSubjectNamespace) {
		return this.doGetTripleUidsCount(
				codingSchemeUid, 
				objectEntityCode, 
				objectEntityCodeNamespace, 
				associationNames,
				associationQualifiers,
				mustHaveSubjectCodes,
				mustHaveSubjectNamespace,
				TripleNode.OBJECT);
	}

	@Override
	public Map<String,Integer> getTripleUidsContainingSubjectCount(
			String codingSchemeUid, 
			String relationsContainerName,
			String subjectEntityCode, 
			String subjectEntityCodeNamespace,
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveObjectCodes,
			List<String> mustHaveObjectNamespace){
		return this.doGetTripleUidsCount(
				codingSchemeUid,  
				subjectEntityCode, 
				subjectEntityCodeNamespace, 
				associationNames,
				associationQualifiers,
				mustHaveObjectCodes,
				mustHaveObjectNamespace,
				TripleNode.SUBJECT);
	}

	@SuppressWarnings("unchecked")
	protected Map<String,Integer>  doGetTripleUidsCount(
			String codingSchemeUid,
			String entityCode,
			String entityCodeNamespace, 
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveCodes,
			List<String> mustHaveNamespace,
			TripleNode tripleNode) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		GetEntityAssnUidsCountBean bean = new GetEntityAssnUidsCountBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeUid(codingSchemeUid);
		bean.setEntityCode(entityCode);
		bean.setEntityCodeNamespace(entityCodeNamespace);
		bean.setAssociations(associationNames);
		bean.setAssociationQualifiers(associationQualifiers);
		bean.setMustHaveCodes(mustHaveCodes);
		bean.setMustHaveNamespaces(mustHaveNamespace);
		bean.setTripleNode(tripleNode);
		
		return (Map<String,Integer> ) this.getSqlMapClientTemplate().
			queryForMap(GET_ENTITY_ASSNSTOENTITY_UID_COUNT_SQL, bean, "key", "value");
	}
	
	@Override
	public List<String> getTripleUidsContainingSubject(
			String codingSchemeUid,
			String associationPredicateUid,
			String entityCode,
			String entityCodeNamespace, 
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveObjectCodes,
			List<String> mustHaveObjectNamespace,
			int start, 
			int pageSize){
		return this.doGetTripleUids(
				codingSchemeUid, 
				associationPredicateUid, 
				entityCode, 
				entityCodeNamespace, 
				associationQualifiers,
				mustHaveObjectCodes,
				mustHaveObjectNamespace,
				TripleNode.SUBJECT,
				start, 
				pageSize);		
	}

	@Override
	public List<String> getTripleUidsContainingObject(
			String codingSchemeUid,
			String associationPredicateUid,
			String entityCode,
			String entityCodeNamespace,
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveSubjectCodes,
			List<String> mustHaveSubjectNamespace,
			int start, 
			int pageSize){
		return this.doGetTripleUids(
				codingSchemeUid, 
				associationPredicateUid, 
				entityCode, 
				entityCodeNamespace,
				associationQualifiers,
				mustHaveSubjectCodes,
				mustHaveSubjectNamespace,
				TripleNode.OBJECT, 
				start, 
				pageSize);
		
	}
	
	@SuppressWarnings("unchecked")
	protected List<String> doGetTripleUids(
			String codingSchemeUid,
			String associationPredicateUid,
			String entityCode,
			String entityCodeNamespace,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveCodes,
			List<String> mustHaveNamespaces,
			TripleNode tripleNode,
			int start, 
			int pageSize){
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		GetEntityAssnUidsBean bean = new GetEntityAssnUidsBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeUid(codingSchemeUid);
		bean.setAssociationPredicateUid(associationPredicateUid);
		bean.setEntityCode(entityCode);
		bean.setEntityCodeNamespace(entityCodeNamespace);
		bean.setAssociationQualifiers(associationQualifiers);
		bean.setMustHaveCodes(mustHaveCodes);
		bean.setMustHaveNamespaces(mustHaveNamespaces);
		bean.setTripleNode(tripleNode);
		
		if(pageSize < 0) {
			pageSize = Integer.MAX_VALUE;
		}
		return this.getSqlMapClientTemplate().
			queryForList(GET_ENTITY_ASSNSTOENTITY_UID_SQL, bean, start, pageSize);
	}

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createNonTypedList(this.supportedDatebaseVersion);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptReference> getConceptReferencesFromUid(
			String codingSchemeUid, List<String> tripleUids,
			TripleNode tripleNode) {
		if(CollectionUtils.isEmpty(tripleUids)) {
			return new ArrayList<ConceptReference>();
		}
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		PrefixedParameterCollection bean = new PrefixedParameterCollection();
		bean.setPrefix(prefix);
		bean.setParam1(tripleNode.toString());
		bean.setParam2(tripleUids);
		
		return this.getSqlMapClientTemplate().queryForList(
				GET_CONCEPTREFERENCE_FROM_ASSNSTOENTITY_UID_SQL, bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AssociatedConcept> getAssociatedConceptsFromUid(
			String codingSchemeUid, List<String> tripleUids, TripleNode tripleNode) {
		if(CollectionUtils.isEmpty(tripleUids)) {
			return new ArrayList<AssociatedConcept>();
		}
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		PrefixedParameterCollection bean = new PrefixedParameterCollection();
		bean.setPrefix(prefix);
		bean.setParam1(tripleNode.toString());
		bean.setParam2(tripleUids);
		
		return this.getSqlMapClientTemplate().queryForList(
				GET_ASSOCIATEDCONCEPT_FROM_ASSNSTOENTITY_UID_SQL, bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAssociationPredicateNamesForCodingSchemeUid(
			String codingSchemeUid,
			String relationContainerName) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		PrefixedParameterTuple bean = new PrefixedParameterTuple();
		bean.setPrefix(prefix);
		bean.setParam1(codingSchemeUid);
		bean.setParam2(relationContainerName);
		
		return this.getSqlMapClientTemplate().
			queryForList(GET_ASSOCIATION_PREDICATE_NAMES_SQL, 
					bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Node> getDistinctSourceNodesForAssociationPredicate(
			String codingSchemeUid, String associationPredicateUid) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		PrefixedParameter bean = new PrefixedParameter();
		bean.setPrefix(prefix);
		bean.setParam1(associationPredicateUid);
		
		return this.getSqlMapClientTemplate().
			queryForList(GET_DISTINCT_SOURCE_NODES_SQL, 
				bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Node> getTargetNodesForSource(String codingSchemeUid,
			String associationPredicateUid, String sourceEntityCode,
			String sourceEntityCodeNamespace) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		PrefixedParameterTriple bean = new PrefixedParameterTriple();
		bean.setPrefix(prefix);
		bean.setParam1(associationPredicateUid);
		bean.setParam2(sourceEntityCode);
		bean.setParam3(sourceEntityCodeNamespace);
		
		return this.getSqlMapClientTemplate().
			queryForList(GET_TARGET_NODES_OF_SOURCE_SQL, 
				bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConceptReference> getRootNodes(String codingSchemeUid,
			List<String> associationPredicateUids, TraverseAssociations traverse) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		PrefixedParameterCollection bean = new PrefixedParameterCollection();
		bean.setPrefix(prefix);
		bean.setParam1(traverse.toString());
		bean.setParam2(associationPredicateUids);
		
		return this.getSqlMapClientTemplate().
			queryForList(GET_ROOT_ENTITY_ASSNSTOENTITY_UID_SQL, bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConceptReference> getTailNodes(String codingSchemeUid,
			List<String> associationPredicateUids, TraverseAssociations traverse) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		PrefixedParameterCollection bean = new PrefixedParameterCollection();
		bean.setPrefix(prefix);
		bean.setParam1(traverse.toString());
		bean.setParam2(associationPredicateUids);
		
		return this.getSqlMapClientTemplate().
			queryForList(GET_TAIL_ENTITY_ASSNSTOENTITY_UID_SQL, bean);
	}
}

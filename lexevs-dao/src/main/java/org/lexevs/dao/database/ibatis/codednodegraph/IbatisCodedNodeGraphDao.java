package org.lexevs.dao.database.ibatis.codednodegraph;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.association.IbatisAssociationDao;
import org.lexevs.dao.database.ibatis.association.parameter.GetEntityAssnUidsBean;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterCollection;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
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
	
	@Override
	public int getTripleUidsContainingObjectCount(
			String codingSchemeUid,
			String associationPredicateUid,
			String objectEntityCode,
			String objectEntityCodeNamespace,
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveSubjectCodes) {
		return this.doGetTripleUidsCount(
				codingSchemeUid, 
				associationPredicateUid, 
				objectEntityCode, 
				objectEntityCodeNamespace, 
				associationNames,
				associationQualifiers,
				mustHaveSubjectCodes,
				TripleNode.OBJECT);
	}

	@Override
	public int getTripleUidsContainingSubjectCount(
			String codingSchemeUid, 
			String associationPredicateUid,
			String subjectEntityCode, 
			String subjectEntityCodeNamespace,
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveObjectCodes){
		return this.doGetTripleUidsCount(
				codingSchemeUid, 
				associationPredicateUid, 
				subjectEntityCode, 
				subjectEntityCodeNamespace, 
				associationNames,
				associationQualifiers,
				mustHaveObjectCodes,
				TripleNode.SUBJECT);
	}

	protected int doGetTripleUidsCount(
			String codingSchemeUid,
			String associationPredicateUid, 
			String entityCode,
			String entityCodeNamespace, 
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveCodes,
			TripleNode tripleNode) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		GetEntityAssnUidsBean bean = new GetEntityAssnUidsBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeUid(codingSchemeUid);
		bean.setAssociationPredicateUid(associationPredicateUid);
		bean.setEntityCode(entityCode);
		bean.setEntityCodeNamespace(entityCodeNamespace);
		bean.setAssociationQualifiers(associationQualifiers);
		bean.setMustHaveCodes(mustHaveCodes);
		bean.setTripleNode(tripleNode);
		
		return (Integer) this.getSqlMapClientTemplate().
			queryForObject(GET_ENTITY_ASSNSTOENTITY_UID_COUNT_SQL, bean);
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
			int start, 
			int pageSize){
		return this.doGetTripleUids(
				codingSchemeUid, 
				associationPredicateUid, 
				entityCode, 
				entityCodeNamespace, 
				associationQualifiers,
				mustHaveObjectCodes,
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
			int start, 
			int pageSize){
		return this.doGetTripleUids(
				codingSchemeUid, 
				associationPredicateUid, 
				entityCode, 
				entityCodeNamespace,
				associationQualifiers,
				mustHaveSubjectCodes,
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
			String codingSchemeUid) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		PrefixedParameter bean = new PrefixedParameter();
		bean.setPrefix(prefix);
		bean.setParam1(codingSchemeUid);
		
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
	public List<String> getRootNodes(String codingSchemeUid,
			List<String> associationPredicateUids) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		PrefixedParameterCollection bean = new PrefixedParameterCollection();
		bean.setPrefix(prefix);
		bean.setParam2(associationPredicateUids);
		
		return this.getSqlMapClientTemplate().
			queryForList(GET_ROOT_ENTITY_ASSNSTOENTITY_UID_SQL, bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getTailNodes(String codingSchemeUid,
			List<String> associationPredicateUids) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		PrefixedParameterCollection bean = new PrefixedParameterCollection();
		bean.setPrefix(prefix);
		bean.setParam2(associationPredicateUids);
		
		return this.getSqlMapClientTemplate().
			queryForList(GET_TAIL_ENTITY_ASSNSTOENTITY_UID_SQL, bean);
	}
}

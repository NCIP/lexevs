package org.lexevs.dao.database.ibatis.codednodegraph;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.association.IbatisAssociationDao;
import org.lexevs.dao.database.ibatis.association.parameter.GetEntityAssnUidsBean;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.QualifierNameValuePair;
import org.lexevs.dao.database.utility.DaoUtility;

public class IbatisCodedNodeGraphDao extends AbstractIbatisDao implements CodedNodeGraphDao {
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	private static String GET_ENTITY_ASSNSTOENTITY_UID_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getEntityAssnsToEntityUids";
	private static String GET_ENTITY_ASSNSTOENTITY_UID_COUNT_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getEntityAssnsToEntityUidsCount";
	private static String GET_ASSOCIATEDCONCEPT_FROM_ASSNSTOENTITY_UID_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getAssociatedConcpetFromEntityAssnsToEntityUid";
	private static String GET_ASSOCIATION_PREDICATE_NAMES_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getAssociationPredicatNamesFromCodingSchemeUid";
	@Override
	public int getTripleUidsContainingObjectCount(
			String codingSchemeUid,
			String associationPredicateUid,
			String objectEntityCode,
			String objectEntityCodeNamespace,
			List<QualifierNameValuePair> associationQualifiers) {
		return this.doGetTripleUidsCount(
				codingSchemeUid, 
				associationPredicateUid, 
				objectEntityCode, 
				objectEntityCodeNamespace, 
				associationQualifiers,
				TripleNode.OBJECT);
	}

	@Override
	public int getTripleUidsContainingSubjectCount(
			String codingSchemeUid, 
			String associationPredicateUid,
			String subjectEntityCode, 
			String subjectEntityCodeNamespace,
			List<QualifierNameValuePair> associationQualifiers){
		return this.doGetTripleUidsCount(
				codingSchemeUid, 
				associationPredicateUid, 
				subjectEntityCode, 
				subjectEntityCodeNamespace, 
				associationQualifiers,
				TripleNode.SUBJECT);
	}

	protected int doGetTripleUidsCount(
			String codingSchemeUid,
			String associationPredicateUid, 
			String entityCode,
			String entityCodeNamespace, 
			List<QualifierNameValuePair> associationQualifiers,
			TripleNode tripleNode) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		GetEntityAssnUidsBean bean = new GetEntityAssnUidsBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeUid(codingSchemeUid);
		bean.setAssociationPredicateUid(associationPredicateUid);
		bean.setEntityCode(entityCode);
		bean.setEntityCodeNamespace(entityCodeNamespace);
		bean.setAssociationQualifiers(associationQualifiers);
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
			List<QualifierNameValuePair> associationQualifiers,
			int start, 
			int pageSize){
		return this.doGetTripleUids(
				codingSchemeUid, 
				associationPredicateUid, 
				entityCode, 
				entityCodeNamespace, 
				associationQualifiers,
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
			List<QualifierNameValuePair> associationQualifiers,
			int start, 
			int pageSize){
		return this.doGetTripleUids(
				codingSchemeUid, 
				associationPredicateUid, 
				entityCode, 
				entityCodeNamespace,
				associationQualifiers,
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
		bean.setTripleNode(tripleNode);
		
		if(pageSize < 0) {
			pageSize = Integer.MAX_VALUE;
		}
		return this.getSqlMapClientTemplate().
			queryForList(GET_ENTITY_ASSNSTOENTITY_UID_SQL, bean);
	}

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, this.supportedDatebaseVersion);
	}

	@Override
	public AssociatedConcept getAssociatedConceptFromUid(
			String codingSchemeUid, String tripleUid, TripleNode tripleNode) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		PrefixedParameterTuple bean = new PrefixedParameterTuple();
		bean.setPrefix(prefix);
		bean.setParam1(tripleUid);
		bean.setParam2(tripleNode.toString());
		
		return (AssociatedConcept) this.getSqlMapClientTemplate().queryForObject(
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

}

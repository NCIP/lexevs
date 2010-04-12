package org.lexevs.dao.database.ibatis.codednodegraph;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.association.IbatisAssociationDao;
import org.lexevs.dao.database.ibatis.association.parameter.GetEntityAssnUidsBean;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

public class IbatisCodedNodeGraphDao extends AbstractIbatisDao implements CodedNodeGraphDao {
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	private static String GET_ENTITY_ASSNSTOENTITY_UID_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getEntityAssnsToEntityUids";
	private static String GET_ENTITY_ASSNSTOENTITY_UID_COUNT_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getEntityAssnsToEntityUidsCount";
	private static String GET_ASSOCIATEDCONCEPT_FROM_ASSNSTOENTITY_UID_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getAssociatedConcpetFromEntityAssnsToEntityUid";
	
	@Override
	public int getTripleUidsContainingObjectCount(String codingSchemeUid,
			String associationPredicateUid, String objectEntityCode,
			String objectEntityCodeNamespace) {
		return this.doGetTripleUidsCount(
				codingSchemeUid, 
				associationPredicateUid, 
				objectEntityCode, 
				objectEntityCodeNamespace, 
				TripleNode.OBJECT);
	}

	@Override
	public int getTripleUidsContainingSubjectCount(
			String codingSchemeUid, String associationPredicateUid,
			String subjectEntityCode, String subjectEntityCodeNamespace) {
		return this.doGetTripleUidsCount(
				codingSchemeUid, 
				associationPredicateUid, 
				subjectEntityCode, 
				subjectEntityCodeNamespace, 
				TripleNode.SUBJECT);
	}

	protected int doGetTripleUidsCount(String codingSchemeUid,
			String associationPredicateUid, String entityCode,
			String entityCodeNamespace, TripleNode tripleNode) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		GetEntityAssnUidsBean bean = new GetEntityAssnUidsBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeUid(codingSchemeUid);
		bean.setAssociationPredicateUid(associationPredicateUid);
		bean.setEntityCode(entityCode);
		bean.setEntityCodeNamespace(entityCodeNamespace);
		bean.setTripleNode(tripleNode);
		
		return (Integer) this.getSqlMapClientTemplate().
			queryForObject(GET_ENTITY_ASSNSTOENTITY_UID_COUNT_SQL, bean);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getTripleUidsContainingSubject(
			String codingSchemeUid,
			String associationPredicateUid,
			String entityCode,
			String entityCodeNamespace, 
			int start, 
			int pageSize){
		return this.doGetTripleUids(
				codingSchemeUid, 
				associationPredicateUid, 
				entityCode, 
				entityCodeNamespace, 
				TripleNode.SUBJECT, 
				start, 
				pageSize);		
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getTripleUidsContainingObject(
			String codingSchemeUid,
			String associationPredicateUid,
			String entityCode,
			String entityCodeNamespace, 
			int start, 
			int pageSize){
		return this.doGetTripleUids(
				codingSchemeUid, 
				associationPredicateUid, 
				entityCode, 
				entityCodeNamespace, 
				TripleNode.OBJECT, 
				start, 
				pageSize);
		
	}
	
	protected List<String> doGetTripleUids(
			String codingSchemeUid,
			String associationPredicateUid,
			String entityCode,
			String entityCodeNamespace,
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
}

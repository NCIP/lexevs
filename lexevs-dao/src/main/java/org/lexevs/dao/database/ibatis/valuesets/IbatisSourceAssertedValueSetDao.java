package org.lexevs.dao.database.ibatis.valuesets;

import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.access.valuesets.SourceAssertedValueSetDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterQuad;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterQuint;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;

public class IbatisSourceAssertedValueSetDao extends AbstractIbatisDao implements SourceAssertedValueSetDao {
	

	public static final String ENTITY_NAMESPACE = "Entity.";
	public static final String ASSOCIATION_NAMESPACE = "Association.";
	private static final String GET_VS_ENTITIES_FROM_CODE = ASSOCIATION_NAMESPACE + "getValueSetEntitiesFromCode";
	private static final String GET_VS_ENTITY_FROM_CODE = ASSOCIATION_NAMESPACE + "getVSTopNodeEntityByCode";
	private static final String GET_VS_FROM_MEMBER_CODE = ASSOCIATION_NAMESPACE + "getValueSetTopNodesFromMemberCode";
	private static final String GET_VS_ENTITY_UIDS = ASSOCIATION_NAMESPACE + "getVSEntityUids";
	private static final String GET_VS_ENTITY_UIDS_FOR_TOPNODE_CODE = ASSOCIATION_NAMESPACE + "getVSEntityUidsForTopNodeCode";
	private static final String GET_VS_ENTITY_COUNT_FROM_CODE = ASSOCIATION_NAMESPACE + "getVSEntityCount";
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Entity> getSourceAssertedValueSetEntitiesForEntityCode(String matchCode, String assertedRelation, String predicateUID, String csUID) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUID);
		return this.getSqlMapClientTemplate().queryForList(
				GET_VS_ENTITIES_FROM_CODE, 
				new PrefixedParameterTuple(prefix, predicateUID, matchCode));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Entity> getSourceAssertedValueSetTopNodeForEntityCode(String matchCode, String codingSchemeUID) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUID);
		return this.getSqlMapClientTemplate().queryForList(
				GET_VS_ENTITY_FROM_CODE, 
				new PrefixedParameterTuple(prefix, codingSchemeUID,  matchCode));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getValueSetEntityUids(String codingSchemeUid, String predUid, int start, int pageSize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		if(pageSize < 0) {
			pageSize = Integer.MAX_VALUE;
		}
		return
			this.getSqlMapClientTemplate().queryForList(
					GET_VS_ENTITY_UIDS, 
					new PrefixedParameter(prefix, predUid),start, pageSize);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getValueSetEntityUidForTopNodeEntityCode(
			String codingSchemeUid, String predUid, String code, int start, int pageSize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		if(pageSize < 0) {
			pageSize = Integer.MAX_VALUE;
		}
		return
			this.getSqlMapClientTemplate().queryForList(
					GET_VS_ENTITY_UIDS_FOR_TOPNODE_CODE, 
					new PrefixedParameterTuple(prefix, predUid, code),start, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Entity> getPagedValueSetEntities(String matchCode, String csUID, String predicateUID, int start, int pageSize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUID);
		return this.getSqlMapClientTemplate().queryForList(
				GET_VS_ENTITIES_FROM_CODE, 
				new PrefixedParameterTuple(prefix, predicateUID, matchCode), start, pageSize);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int getValueSetEntityCount(String matchCode, String csUID, String predicateUID) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUID);
		List<String> results = this.getSqlMapClientTemplate().queryForList(
				GET_VS_ENTITY_COUNT_FROM_CODE, 
				new PrefixedParameterTuple(prefix, predicateUID, matchCode));
				if(!results.isEmpty()){
					return Integer.parseInt(results.get(0));}
					else {return 0;}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Entity> getSourceAssertedValueSetsForVSMemberEntityCode(String matchCode,
			String assertedValueSetRelation, String predUid, String csUID) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUID);
		return this.getSqlMapClientTemplate().queryForList(
		GET_VS_FROM_MEMBER_CODE,
		new PrefixedParameterTuple(prefix, predUid, matchCode));
	}

}

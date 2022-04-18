
package org.lexevs.dao.database.ibatis.valuesets;

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.apache.ibatis.session.RowBounds;
import org.lexevs.dao.database.access.association.model.DefinedNode;
import org.lexevs.dao.database.access.valuesets.SourceAssertedValueSetDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

public class IbatisSourceAssertedValueSetDao extends AbstractIbatisDao implements SourceAssertedValueSetDao {
	

	public static final String ENTITY_NAMESPACE = "Entity.";
	public static final String ASSOCIATION_NAMESPACE = "Association.";
	public static final String VS_PROPERTY = "VSProperty.";
	private static final String GET_VS_ENTITIES_FROM_CODE = ASSOCIATION_NAMESPACE + "getValueSetEntitiesFromCode";
	private static final String GET_VS_ENTITY_FROM_CODE = ASSOCIATION_NAMESPACE + "getVSTopNodeEntityByCode";
	private static final String GET_VS_ENTITY_FROM_DESCRIPITON = ASSOCIATION_NAMESPACE + "getVSTopNodeEntityByDescription";
	private static final String GET_VS_FROM_MEMBER_CODE = ASSOCIATION_NAMESPACE + "getValueSetTopNodesFromMemberCode";
	private static final String GET_VS_ENTITY_UIDS = ASSOCIATION_NAMESPACE + "getVSEntityUids";
	private static final String GET_VS_ENTITY_UIDS_FOR_TOPNODE_CODE = ASSOCIATION_NAMESPACE + "getVSEntityUidsForTopNodeCode";
	private static final String GET_VS_ENTITY_COUNT_FROM_CODE = ASSOCIATION_NAMESPACE + "getVSEntityCount";
	private static final String GET_VS_TRIPLES_OF_VS_SQL = ASSOCIATION_NAMESPACE + "getAllValidValueSetTopNodes";
	private static final String GET_VS_PROPERTIES = VS_PROPERTY + "getAssertedValueSetPropertyByCode";
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
	}

	
	@Override
	public List<Entity> getSourceAssertedValueSetEntitiesForEntityCode(String matchCode, String assertedRelation, String predicateUID, String csUID) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUID);
		return this.getSqlSessionTemplate().selectList(
				GET_VS_ENTITIES_FROM_CODE, 
				new PrefixedParameterTuple(prefix, predicateUID, matchCode));
	}

	
	@Override
	public List<Entity> getSourceAssertedValueSetTopNodeForEntityCode(String matchCode, String codingSchemeUID) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUID);
		return this.getSqlSessionTemplate().selectList(
				GET_VS_ENTITY_FROM_CODE, 
				new PrefixedParameterTuple(prefix, codingSchemeUID,  matchCode));
	}
	
	
	@Override
	public List<Entity> getSourceAssertedValueSetTopNodeDescription(String description, String codingSchemeUID) {
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUID);
		return this.getSqlSessionTemplate().selectList(
				GET_VS_ENTITY_FROM_DESCRIPITON, 
				new PrefixedParameterTuple(prefix, codingSchemeUID,  description));
	}
	
	
	@Override
	public List<String> getValueSetEntityUids(String codingSchemeUid, String predUid, int start, int pageSize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		if(pageSize < 0) {
			pageSize = Integer.MAX_VALUE;
		}
		return
			this.getSqlSessionTemplate().<String>selectList(
					GET_VS_ENTITY_UIDS, 
					new PrefixedParameter(prefix, predUid),new RowBounds(start, pageSize));
	}
	
	
	@Override
	public List<String> getValueSetEntityUidForTopNodeEntityCode(
			String codingSchemeUid, String predUid, String code, int start, int pageSize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		if(pageSize < 0) {
			pageSize = Integer.MAX_VALUE;
		}
		return
			this.getSqlSessionTemplate().<String>selectList(
					GET_VS_ENTITY_UIDS_FOR_TOPNODE_CODE, 
					new PrefixedParameterTuple(prefix, predUid, code),new RowBounds(start, pageSize));
	}

	
	@Override
	public List<Entity> getPagedValueSetEntities(String matchCode, String csUID, String predicateUID, int start, int pageSize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUID);
		return this.getSqlSessionTemplate().<Entity>selectList(
				GET_VS_ENTITIES_FROM_CODE, 
				new PrefixedParameterTuple(prefix, predicateUID, matchCode), new RowBounds(start, pageSize));
	}
	
	
	@Override
	public int getValueSetEntityCount(String matchCode, String csUID, String predicateUID) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUID);
		List<String> results = this.getSqlSessionTemplate().selectList(
				GET_VS_ENTITY_COUNT_FROM_CODE, 
				new PrefixedParameterTuple(prefix, predicateUID, matchCode));
				if(!results.isEmpty()){
					return Integer.parseInt(results.get(0));}
					else {return 0;}
	}

	
	@Override
	public List<Entity> getSourceAssertedValueSetsForVSMemberEntityCode(String matchCode,
			String assertedValueSetRelation, String predUid, String csUID) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUID);
		return this.getSqlSessionTemplate().selectList(
		GET_VS_FROM_MEMBER_CODE,
		new PrefixedParameterTuple(prefix, predUid, matchCode));
	}
	
	
	@Override
	public List<DefinedNode> getAllValidValueSetTopNodeCodes(
			String propertyName, String propertyValue, String predUid, String csUID){
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUID);
		return this.getSqlSessionTemplate().selectList(
				GET_VS_TRIPLES_OF_VS_SQL, 
				new PrefixedParameterTriple(prefix, predUid, propertyName, propertyValue));
	}

	
	@Override
	public List<Property> getValueSetEntityProperties(String entityCode, String csUid) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUid);
		return this.getSqlSessionTemplate().selectList(
		GET_VS_PROPERTIES,
		new PrefixedParameter(prefix, entityCode));
	}

}
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
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");

	@SuppressWarnings("unchecked")
	@Override
	public List<Entity> getSourceAssertedValueSetEntitiesForEntityCode(String matchCode, String assertedRelation, String predicateUID, String csUID) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUID);
		return this.getSqlMapClientTemplate().queryForList(
				GET_VS_ENTITIES_FROM_CODE, 
				new PrefixedParameterTuple(prefix, predicateUID, matchCode));
	}

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Entity> getSourceAssertedValueSetTopNodeForEntityCode(String matchCode, String codingSchemeUID) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUID);
		return this.getSqlMapClientTemplate().queryForList(
				GET_VS_ENTITY_FROM_CODE, 
				new PrefixedParameterTuple(prefix, codingSchemeUID,  matchCode));
	}

}

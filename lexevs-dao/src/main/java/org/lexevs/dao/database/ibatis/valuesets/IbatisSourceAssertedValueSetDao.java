package org.lexevs.dao.database.ibatis.valuesets;

import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.access.valuesets.SourceAssertedValueSetDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterQuad;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterQuint;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;

public class IbatisSourceAssertedValueSetDao extends AbstractIbatisDao implements SourceAssertedValueSetDao {
	

	public static final String ENTITY_NAMESPACE = "Entity.";
	public static final String ASSOCIATION_NAMESPACE = "Association.";
	private static final String GET_VS_ENTITIES_FROM_CODE = ASSOCIATION_NAMESPACE + "getValueSetEntitiesFromCode";
	private static final String GET_VS_ENTITY_FROM_CODE = ENTITY_NAMESPACE + "getValueSetTopNodeEntityFromCode";

	public IbatisSourceAssertedValueSetDao() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Entity> getSourceAssertedValueSetEntitiesForEntityCode(String matchCode, String assertedRelation) {
		String prefix = null;
		return this.getSqlMapClientTemplate().queryForList(
				GET_VS_ENTITIES_FROM_CODE, 
				new PrefixedParameterTuple(prefix, matchCode, assertedRelation));
	}

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Entity> getSourceAssertedValueSetTopNodeForEntityCode(String matchCode, String valueSetDesignation,
			String designationValue) {
		String prefix = null;
		return this.getSqlMapClientTemplate().queryForList(
				GET_VS_ENTITY_FROM_CODE, 
				new PrefixedParameterTriple(prefix, matchCode, valueSetDesignation,
						designationValue));
	}

}

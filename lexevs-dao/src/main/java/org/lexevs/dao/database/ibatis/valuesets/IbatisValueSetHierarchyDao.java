package org.lexevs.dao.database.ibatis.valuesets;

import java.util.List;

import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;
import org.lexevs.dao.database.access.valuesets.ValueSetHierarchyDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterQuad;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;

public class IbatisValueSetHierarchyDao extends AbstractIbatisDao implements ValueSetHierarchyDao {

	private static final String GET_VS_TRIPLES_OF_VSNODE_SQL = "getValueSetHierarchySourcesOfTargets";
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");

	
	@SuppressWarnings("unchecked")
	@Override
	public List<VSHierarchyNode> getAllVSTriplesTrOfVSNode(
			String codingSchemeId, String code, String associationGuid, String propertyOne, String propertyTwo,  int start, int pagesize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);

		return this.getSqlMapClientTemplate().queryForList(
				GET_VS_TRIPLES_OF_VSNODE_SQL, 
				new PrefixedParameterQuad(prefix, code, associationGuid, propertyOne, propertyTwo), start, pagesize);
	}

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
	}
	

}

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
	public static String ASSOCIATION_NAMESPACE = "Association.";
	private static final String GET_VS_TRIPLES_OF_VSNODE_SQL = ASSOCIATION_NAMESPACE + "getValueSetHierarchySourcesOfTargets";
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");

	
	@SuppressWarnings("unchecked")
	@Override
	public List<VSHierarchyNode> getAllVSTriplesTrOfVSNode(
			String codingSchemeId, String code, String associationGuid, String sourceDesignation, String publishName,  int start, int pagesize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		if(pagesize < 0) {
			pagesize = Integer.MAX_VALUE;
		}
		return this.getSqlMapClientTemplate().queryForList(
				GET_VS_TRIPLES_OF_VSNODE_SQL, 
				new PrefixedParameterQuad(prefix, associationGuid, code, sourceDesignation, publishName), start, pagesize);
	}

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
	}
	

}

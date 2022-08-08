
package org.lexevs.dao.database.ibatis.valuesets;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.lexevs.dao.database.access.association.model.DefinedNode;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;
import org.lexevs.dao.database.access.valuesets.ValueSetHierarchyDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterQuint;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

public class IbatisValueSetHierarchyDao extends AbstractIbatisDao implements ValueSetHierarchyDao {
	public static String ASSOCIATION_NAMESPACE = "Association.";
	private static final String GET_VS_TRIPLES_OF_VSNODE_SQL = ASSOCIATION_NAMESPACE + "getValueSetHierarchySourcesOfTargets";
	private static final String GET_VS_TRIPLES_OF_VS_SQL = ASSOCIATION_NAMESPACE + "getAllValidValueSetTopNodes";

/** The supported data base version. */
private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");

	
	@SuppressWarnings("unchecked")
	@Override
	/**
	 * This gets all value sets that are marked to be published.
	 * It pulls back contributing sources as one of its values
	 * 	sourceDescription = name of property holding the source (Ex "Contributing_Source")
	 * 	publishName = name of property that holds publication permission (Ex "Publish_Value_Set")
	 * 	canPublish = value of publishName property (Ex "Yes","No")
	 */
	public List<VSHierarchyNode> getAllVSTriplesTrOfVSNode(
			String codingSchemeId, String code, String associationGuid, String sourceDesignation, String publishName, String canPublish,  int start, int pagesize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		if(pagesize < 0) {
			pagesize = Integer.MAX_VALUE;
		}
		return this.getSqlSessionTemplate().<VSHierarchyNode>selectList(
				GET_VS_TRIPLES_OF_VSNODE_SQL, 
				new PrefixedParameterQuint(prefix, associationGuid, code, sourceDesignation, publishName, canPublish), new RowBounds(start, pagesize));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DefinedNode> getAllVSTriples(
			String codingSchemeId, String associationGuid, String publishName, String canPublish,  int start, int pagesize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		if(pagesize < 0) {
			pagesize = Integer.MAX_VALUE;
		}
		return this.getSqlSessionTemplate().<DefinedNode>selectList(
				GET_VS_TRIPLES_OF_VS_SQL, 
				new PrefixedParameterTriple(prefix, associationGuid, publishName, canPublish), new RowBounds(start, pagesize));
	}

	
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
	}
	

}
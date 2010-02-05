package org.lexevs.dao.database.ibatis.association;

import java.util.List;

import org.LexGrid.relations.Association;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.junit.Assert;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.association.parameter.InsertAssociationSourceBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertRelationsBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

public class IbatisAssociationDao extends AbstractIbatisDao implements AssociationDao {

	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	private static String INSERT_RELATIONS_SQL = "insertRelations";
	private static String INSERT_ENTITY_ASSNS_TO_ENTITY = "insertEntityAssnsToEntity";
	
	private VersionsDao versionsDao;	

	public String getAssociationPredicateId(String codingSchemeId,
			String relationContainerName, String associationPredicateName) {
		return null;
	}
	
	public String insertRelations(String codingSchemeId,
			Relations relations) {
		String relationsId = this.createUniqueId();
		String entryStateId = this.createUniqueId();
		
		InsertRelationsBean bean = new InsertRelationsBean();
		bean.setEntryStateId(entryStateId);
		bean.setId(relationsId);
		bean.setCodingSchemeId(codingSchemeId);
		bean.setRelations(relations);
		bean.setPrefix(this.getPrefixResolver().resolvePrefix());
		
		this.getSqlMapClientTemplate().insert(INSERT_RELATIONS_SQL, bean);
		
		return relationsId;
	}

	public String insertAssociation(String codingScheme, String version,
			Association association) {
		// TODO Auto-generated method stub
		return null;
	}

	public void insertAssociationSource(String codingSchemeId,
			String associationPredicateId, AssociationSource source) {
		Assert.assertTrue("Must Insert at least ONE AssociationTarget per AssociationSource.", source.getTarget().length > 0);
		
		for(AssociationTarget target : source.getTarget()){
			String id = this.createUniqueId();
			String entryStateId = this.createUniqueId();
			
			InsertAssociationSourceBean bean = new InsertAssociationSourceBean();
			bean.setPrefix(this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId));
			bean.setAssociationPredicateId(associationPredicateId);
			bean.setAssociationSource(source);
			bean.setAssociationTarget(target);
			bean.setEntryStateId(entryStateId);
			bean.setId(id);
			this.getSqlMapClientTemplate().insert(INSERT_ENTITY_ASSNS_TO_ENTITY, bean);
			
			versionsDao.insertEntryState(codingSchemeId, entryStateId, id, "associationSource", null, target.getEntryState());
		}
	}
	
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(supportedDatebaseVersion, LexGridSchemaVersion.class);
	}

	public void setVersionsDao(VersionsDao versionsDao) {
		this.versionsDao = versionsDao;
	}

	public VersionsDao getVersionsDao() {
		return versionsDao;
	}


}

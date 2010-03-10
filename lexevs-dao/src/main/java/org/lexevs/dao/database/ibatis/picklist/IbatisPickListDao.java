package org.lexevs.dao.database.ibatis.picklist;

import java.util.List;

import org.LexGrid.valueDomains.PickListDefinition;
import org.lexevs.dao.database.access.picklist.PickListDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.picklist.parameter.InsertPickListDefinitionBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

public class IbatisPickListDao extends AbstractIbatisDao implements PickListDao {
	
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	public static String PICKLIST_NAMESPACE = "PickList.";
	public static String INSERT_PICKLIST_DEFINITION_SQL = PICKLIST_NAMESPACE + "insertPickListDefinition";
	public static String GET_PICKLIST_IDS_SQL = PICKLIST_NAMESPACE + "getPickListIds";
	
	private VersionsDao versionsDao;
	

	@Override
	public PickListDefinition getPickListDefinitionById(String pickListId) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}
	
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
	}

	@Override
	public String insertPickListDefinition(String systemReleaseUri,
			PickListDefinition definition) {
		String pickListId = this.createUniqueId();
		
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		String systemReleaseId = this.versionsDao.getSystemReleaseIdByUri(systemReleaseUri);
		
		InsertPickListDefinitionBean bean = new InsertPickListDefinitionBean();
		bean.setId(pickListId);
		bean.setPickListDefinition(definition);
		bean.setPrefix(prefix);
		bean.setSystemReleaseId(systemReleaseId);
		this.getSqlMapClientTemplate().insert(INSERT_PICKLIST_DEFINITION_SQL, bean);
		
		return pickListId;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPickListIds() {
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		return this.getSqlMapClientTemplate().queryForList(
				GET_PICKLIST_IDS_SQL, prefix);
	}	

	public VersionsDao getVersionsDao() {
		return versionsDao;
	}

	public void setVersionsDao(VersionsDao versionsDao) {
		this.versionsDao = versionsDao;
	}

	
}

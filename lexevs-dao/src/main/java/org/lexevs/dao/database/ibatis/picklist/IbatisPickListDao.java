package org.lexevs.dao.database.ibatis.picklist;

import java.util.List;

import org.LexGrid.valueDomains.PickListDefinition;
import org.lexevs.dao.database.access.picklist.PickListDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.picklist.parameter.InsertPickListDefinitionBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

public class IbatisPickListDao extends AbstractIbatisDao implements PickListDao {
	
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	public static String PICKLIST_NAMESPACE = "PickList.";
	public static String INSERT_PICKLIST_DEFINITION_SQL = PICKLIST_NAMESPACE + "insertPickListDefinition";
	public static String GET_PICKLIST_IDS_SQL = PICKLIST_NAMESPACE + "getPickListIds";
	public static String GET_PICKLIST_GUID_BY_PICKLISTID_SQL = PICKLIST_NAMESPACE + "getPickListGuidByPickListId";
	public static String GET_PICKLIST_DEFINITION_BY_PICKLISTID_SQL = PICKLIST_NAMESPACE + "getPickListDefinitionByPickListId";
	
	private VersionsDao versionsDao;
	

	@Override
	public PickListDefinition getPickListDefinitionById(String pickListId) {
		String prefix = getPrefix();
		
		return (PickListDefinition) 
			this.getSqlMapClientTemplate().queryForObject(GET_PICKLIST_DEFINITION_BY_PICKLISTID_SQL, 
				new PrefixedParameter(prefix, pickListId));
	}
	
	@Override
	public String getGuidFromPickListId(String pickListId) {
		String prefix = getPrefix();
		
		return (String) 
		this.getSqlMapClientTemplate().queryForObject(GET_PICKLIST_GUID_BY_PICKLISTID_SQL, 
			new PrefixedParameter(prefix, pickListId));
	}

	@Override
	public String insertPickListEntry(String pickListGuid,
			PickListDefinition definition) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public String insertPickListDefinition(String systemReleaseUri,
			PickListDefinition definition) {
		String pickListId = this.createUniqueId();
		
		String systemReleaseId = this.versionsDao.getSystemReleaseIdByUri(systemReleaseUri);
		
		InsertPickListDefinitionBean bean = new InsertPickListDefinitionBean();
		bean.setId(pickListId);
		bean.setPickListDefinition(definition);
		bean.setPrefix(getPrefix());
		bean.setSystemReleaseId(systemReleaseId);
		this.getSqlMapClientTemplate().insert(INSERT_PICKLIST_DEFINITION_SQL, bean);
		
		return pickListId;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPickListIds() {
		return this.getSqlMapClientTemplate().queryForList(
				GET_PICKLIST_IDS_SQL, getPrefix());
	}	
	
	protected String getPrefix() {
		return this.getPrefixResolver().resolveDefaultPrefix();
	}
	
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
	}

	public VersionsDao getVersionsDao() {
		return versionsDao;
	}

	public void setVersionsDao(VersionsDao versionsDao) {
		this.versionsDao = versionsDao;
	}
}

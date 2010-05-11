package org.lexevs.dao.database.ibatis.valuesets;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.valueSets.PickListEntry;
import org.LexGrid.valueSets.PickListEntryExclusion;
import org.LexGrid.valueSets.PickListEntryNode;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.valuesets.PickListEntryNodeDao;
import org.lexevs.dao.database.access.valuesets.VSEntryStateDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao.ReferenceType;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.valuesets.parameter.InsertOrUpdatePickListEntryBean;
import org.lexevs.dao.database.ibatis.valuesets.parameter.InsertOrUpdateValueSetsMultiAttribBean;
import org.lexevs.dao.database.ibatis.valuesets.parameter.PickListEntryNodeBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

public class IbatisPickListEntryNodeDao extends AbstractIbatisDao implements
		PickListEntryNodeDao {

	private VSEntryStateDao vsEntryStateDao = null;
	
	private VSPropertyDao vsPropertyDao = null;	
	
	private VersionsDao versionsDao = null;
	
	private static final String PICKLIST_ENTRY_NODE_NAMESPACE = "PickListEntryNode.";
	
	private static String VS_MULTIATTRIB_NAMESPACE = "VSMultiAttrib.";	
	
	private static String INSERT_PICKLIST_ENTRY_SQL = PICKLIST_ENTRY_NODE_NAMESPACE + "insertPickListEntry";

	private static String INSERT_MULTI_ATTRIB_SQL = VS_MULTIATTRIB_NAMESPACE + "insertMultiAttrib";
	
	private static final String GET_PICKLIST_ENTRYNODE_UID_BY_PICKLISTID_AND_ENTRYNODEID_SQL = PICKLIST_ENTRY_NODE_NAMESPACE
			+ "getPickListEntryNodeUIdByPickListIdAndEntryNodeId";

	private static String DELETE_CONTEXT_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "deleteContextByParentGuidAndType";
	
	private static String DELETE_SOURCE_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "deleteSourceByParentGuidAndType";

	private static String GET_PICKLIST_ENTRYNODE_BEAN_BY_PICKLIST_GUID_SQL = PICKLIST_ENTRY_NODE_NAMESPACE + "getPickListEntryNodeBeanByPickListGuid";
	
	private static String UPDATE_PICKLIST_ENTRYNODE_BY_UID_SQL = PICKLIST_ENTRY_NODE_NAMESPACE + "updatePickListEntryNodeByUId";
	
	private static String UPDATE_PICKLIST_ENTRYNODE_VER_ATTRIB_BY_UID_SQL = PICKLIST_ENTRY_NODE_NAMESPACE + "updatePickListEntryNodeVerAttribByUId";
	
	private static String GET_ENTRYSTATE_UID_BY_PICKLISTENTRYNODE_UID_SQL = PICKLIST_ENTRY_NODE_NAMESPACE + "getEntryStateUIdByPickListEntryNodeUId";
	
	private static String UPDATE_PICKLIST_ENTRYNODE_ENTRYSTATE_UID_SQL = PICKLIST_ENTRY_NODE_NAMESPACE + "updateEntryStateUIdByPickListEntryUId";
	
	private static String GET_PICKLIST_ENTRYNODE_LATEST_REVISION_ID_BY_UID = PICKLIST_ENTRY_NODE_NAMESPACE + "getPickListEntryNodeLatestRevisionIdByUId";
	
	private static String DELETE_PL_ENTRY_NODE_BY_UID_SQL = PICKLIST_ENTRY_NODE_NAMESPACE + "deletePickListEntryNodeByUId";
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion
			.parseStringToVersion("2.0");

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class,
				supportedDatebaseVersion);
	}

	@Override
	public String getPickListEntryNodeUId(String pickListId,
			String pickListEntryNodeId) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();

		return (String) this.getSqlMapClientTemplate().queryForObject(
				GET_PICKLIST_ENTRYNODE_UID_BY_PICKLISTID_AND_ENTRYNODEID_SQL,
				new PrefixedParameterTuple(prefix, pickListId,
						pickListEntryNodeId));
	}
	
	@Override
	public String insertPickListEntry(String pickListGuid, PickListEntryNode entryNode) {
		if (entryNode == null)
			return null;
		
		String plEntryGuid = this.createUniqueId();
		String vsEntryStateGuid = this.createUniqueId();
		
		InsertOrUpdateValueSetsMultiAttribBean insertOrUpdateValueSetsMultiAttribBean = null;
		if (entryNode != null && entryNode.getPickListEntryNodeChoice() != null)
		{
			EntryState entryState = entryNode.getEntryState();
			
			if (entryState != null)
			{
				this.vsEntryStateDao.insertEntryState(vsEntryStateGuid, plEntryGuid, 
						ReferenceType.PICKLISTENTRY.name(), null, entryState);
			}
			
			InsertOrUpdatePickListEntryBean plEntryBean = new InsertOrUpdatePickListEntryBean();	
			
			plEntryBean.setUId(plEntryGuid);
			plEntryBean.setPickListEntryNode(entryNode);
			plEntryBean.setPickListUId(pickListGuid);
			plEntryBean.setEntryStateUId(vsEntryStateGuid);
			
			PickListEntry plEntry = entryNode.getPickListEntryNodeChoice().getInclusionEntry();
			PickListEntryExclusion plExclusion = entryNode.getPickListEntryNodeChoice().getExclusionEntry();
			List<InsertOrUpdateValueSetsMultiAttribBean> contextList = null;
			
			if (plEntry != null)
			{
				plEntryBean.setInclude(true);
				plEntryBean.setEntityCode(plEntry.getEntityCodeNamespace());
				plEntryBean.setEntityCode(plEntry.getEntityCode());
				plEntryBean.setDefault(plEntry.isIsDefault() == null? false : plEntry.isIsDefault());
				plEntryBean.setEntryOrder(plEntry.getEntryOrder() == null? 0 : plEntry.getEntryOrder());
				plEntryBean.setMatchIfNoContext(plEntry.getMatchIfNoContext() == null ? true : plEntry.getMatchIfNoContext());
				plEntryBean.setPropertyId(plEntry.getPropertyId());
				plEntryBean.setPickText(plEntry.getPickText());
				plEntryBean.setLangauage(plEntry.getLanguage());
				
				contextList = null;
				for (String pickContext : plEntry.getPickContextAsReference())
				{
					insertOrUpdateValueSetsMultiAttribBean = new InsertOrUpdateValueSetsMultiAttribBean();
					insertOrUpdateValueSetsMultiAttribBean.setUId(this.createUniqueId());
					insertOrUpdateValueSetsMultiAttribBean.setReferenceUId(plEntryGuid);
					insertOrUpdateValueSetsMultiAttribBean.setReferenceType(ReferenceType.PICKLISTENTRY.name());
					insertOrUpdateValueSetsMultiAttribBean.setAttributeType(SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT);
					insertOrUpdateValueSetsMultiAttribBean.setAttributeValue(pickContext);
					insertOrUpdateValueSetsMultiAttribBean.setRole(null);
					insertOrUpdateValueSetsMultiAttribBean.setSubRef(null);
					insertOrUpdateValueSetsMultiAttribBean.setEntryStateUId(this.createUniqueId());
					
					if (contextList == null)
						contextList = new ArrayList<InsertOrUpdateValueSetsMultiAttribBean>();
					
					contextList.add(insertOrUpdateValueSetsMultiAttribBean);
				}
			}
			else if (plExclusion != null)
			{
				plEntryBean.setInclude(false);
				plEntryBean.setEntityCode(plExclusion.getEntityCodeNamespace());
				plEntryBean.setEntityCode(plExclusion.getEntityCode());
			}
			
			// insert into plEntry table
			this.getSqlMapClientTemplate().insert(INSERT_PICKLIST_ENTRY_SQL, plEntryBean);
			
			// insert pickListEntryNode properties
			if (entryNode.getProperties() != null)
			{
				for (Property property : entryNode.getProperties().getPropertyAsReference())
				{
					this.vsPropertyDao.insertProperty(plEntryGuid, ReferenceType.PICKLISTENTRY, property);
				}
			}
			
			// insert pick list entry context list
			if (contextList != null)
			{
				for (InsertOrUpdateValueSetsMultiAttribBean pickContextMultiAttrib : contextList)
				{
					this.getSqlMapClientTemplate().insert(INSERT_MULTI_ATTRIB_SQL, pickContextMultiAttrib);
				}
			}
		
		}
		return plEntryGuid;
	}

	@Override
	public void removeAllPickListEntryNodeMultiAttributes(
			String pickListEntryNodeUId) {
		
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		this.getSqlMapClientTemplate().delete(
				DELETE_CONTEXT_BY_PARENT_GUID_AND_TYPE_SQL,
				new PrefixedParameterTuple(prefix, pickListEntryNodeUId,
						ReferenceType.PICKLISTENTRY.name()));
		
		this.getSqlMapClientTemplate().delete(
				DELETE_SOURCE_BY_PARENT_GUID_AND_TYPE_SQL,
				new PrefixedParameterTuple(prefix, pickListEntryNodeUId,
						ReferenceType.PICKLISTENTRY.name()));
	}

	/**
	 * @return the vsEntryStateDao
	 */
	public VSEntryStateDao getVsEntryStateDao() {
		return vsEntryStateDao;
	}

	/**
	 * @param vsEntryStateDao the vsEntryStateDao to set
	 */
	public void setVsEntryStateDao(VSEntryStateDao vsEntryStateDao) {
		this.vsEntryStateDao = vsEntryStateDao;
	}

	/**
	 * @return the vsPropertyDao
	 */
	public VSPropertyDao getVsPropertyDao() {
		return vsPropertyDao;
	}

	/**
	 * @param vsPropertyDao the vsPropertyDao to set
	 */
	public void setVsPropertyDao(VSPropertyDao vsPropertyDao) {
		this.vsPropertyDao = vsPropertyDao;
	}

	@Override
	public String insertHistoryPickListEntryNode(String pickListEntryNodeUId) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		String histPrefix = this.getPrefixResolver().resolveHistoryPrefix();
		
		PickListEntryNodeBean plEntryNodeBean = (PickListEntryNodeBean) this
				.getSqlMapClientTemplate().queryForObject(
						GET_PICKLIST_ENTRYNODE_BEAN_BY_PICKLIST_GUID_SQL,
						new PrefixedParameter(prefix, pickListEntryNodeUId));
	
		InsertOrUpdatePickListEntryBean bean = new InsertOrUpdatePickListEntryBean();
		
		PickListEntryNode entryNode = new PickListEntryNode();
		
		entryNode.setIsActive(plEntryNodeBean.getIsActive());
		entryNode.setOwner(plEntryNodeBean.getOwner());
		entryNode.setStatus(plEntryNodeBean.getStatus());
		entryNode.setEffectiveDate(plEntryNodeBean.getEffectiveDate());
		entryNode.setExpirationDate(plEntryNodeBean.getExpirationDate());
		
		bean.setPickListEntryNode(entryNode);
		bean.setPrefix(histPrefix);
		bean.setUId(plEntryNodeBean.getVsPLEntryGuid());
		bean.setEntryStateUId(plEntryNodeBean.getEntryStateUId());
		
		PickListEntry plEntry = plEntryNodeBean.getPickListEntry();
		
		if( plEntryNodeBean.getInclude() ) {
			
			bean.setEntityCode(plEntry.getEntityCode());
			bean.setEntityCodeNamespace(plEntry.getEntityCodeNamespace());
			bean.setEntryOrder(plEntry.getEntryOrder());
			bean.setDefault(plEntry.getIsDefault());
			bean.setMatchIfNoContext(plEntry.getMatchIfNoContext());
			bean.setPropertyId(plEntry.getPropertyId());
			bean.setLangauage(plEntry.getLanguage());
			bean.setPickText(plEntry.getPickText());
			bean.setInclude(true);
		} else {
			
			bean.setEntityCode(plEntry.getEntityCode());
			bean.setEntityCodeNamespace(plEntry.getEntityCodeNamespace());
			bean.setInclude(false);
		}
		
		this.getSqlMapClientTemplate().insert(
				INSERT_PICKLIST_ENTRY_SQL, bean);
		
		if (!vsEntryStateExists(prefix, plEntryNodeBean.getEntryStateUId())) {

			EntryState entryState = new EntryState();

			entryState.setChangeType(ChangeType.NEW);
			entryState.setRelativeOrder(0L);

			vsEntryStateDao
					.insertEntryState(plEntryNodeBean.getEntryStateUId(),
							plEntryNodeBean.getVsPLEntryGuid(), ReferenceType.PICKLISTENTRY.name(), null,
							entryState);
		}
		
		return plEntryNodeBean.getEntryStateUId();
	}

	@Override
	public String updatePickListEntryNode(String pickListEntryNodeUId,
			PickListEntryNode pickListEntryNode) {

		InsertOrUpdatePickListEntryBean bean = buildInsertOrUpdatePickListEntryBean(
				pickListEntryNodeUId, pickListEntryNode);

		this.getSqlMapClientTemplate().update(
				UPDATE_PICKLIST_ENTRYNODE_BY_UID_SQL, bean);

		return bean.getEntryStateUId();
	}

	@Override
	public String updateVersionableAttributes(String pickListEntryNodeUId,
			PickListEntryNode pickListEntryNode) {

		InsertOrUpdatePickListEntryBean bean = buildInsertOrUpdatePickListEntryBean(
				pickListEntryNodeUId, pickListEntryNode);

		this.getSqlMapClientTemplate().update(
				UPDATE_PICKLIST_ENTRYNODE_VER_ATTRIB_BY_UID_SQL, bean);

		return bean.getEntryStateUId();
	}

	private InsertOrUpdatePickListEntryBean buildInsertOrUpdatePickListEntryBean(
			String pickListEntryNodeUId, PickListEntryNode pickListEntryNode) {

		String entryStateUId = this.createUniqueId();
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();

		InsertOrUpdatePickListEntryBean bean = new InsertOrUpdatePickListEntryBean();

		PickListEntryExclusion excludeEntry = pickListEntryNode
				.getPickListEntryNodeChoice().getExclusionEntry();

		PickListEntry includeEntry = pickListEntryNode
				.getPickListEntryNodeChoice().getInclusionEntry();

		if (includeEntry != null) {

			bean.setEntityCode(includeEntry.getEntityCode());
			bean.setEntityCodeNamespace(includeEntry.getEntityCodeNamespace());
			bean.setEntryOrder(includeEntry.getEntryOrder());
			bean.setInclude(true);
			bean.setLangauage(includeEntry.getLanguage());
			bean.setMatchIfNoContext(includeEntry.getMatchIfNoContext());
			bean.setPickText(includeEntry.getPickText());
			bean.setPropertyId(includeEntry.getPropertyId());
			bean.setDefault(includeEntry.getIsDefault());

		} else if (excludeEntry != null) {

			bean.setInclude(false);
			bean.setEntityCode(excludeEntry.getEntityCode());
			bean.setEntityCodeNamespace(excludeEntry.getEntityCodeNamespace());
		}

		bean.setPrefix(prefix);
		bean.setPickListEntryNode(pickListEntryNode);
		bean.setUId(pickListEntryNodeUId);
		bean.setEntryStateUId(entryStateUId);

		return bean;
	}

	@Override
	public String getPickListEntryStateUId(String pickListEntryNodeUId) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();

		return (String) this.getSqlMapClientTemplate().queryForObject(
				GET_ENTRYSTATE_UID_BY_PICKLISTENTRYNODE_UID_SQL,
				new PrefixedParameter(prefix, pickListEntryNodeUId));
	}

	@Override
	public void updateEntryStateUId(String pickListEntryNodeUId,
			String entryStateUId) {
		
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		this.getSqlMapClientTemplate().update(
				UPDATE_PICKLIST_ENTRYNODE_ENTRYSTATE_UID_SQL, 
				new PrefixedParameterTuple(prefix, pickListEntryNodeUId, entryStateUId));
	}

	@Override
	public void createEntryStateIfAbsent(String entryStateUId, String vsPLEntryUId) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		if (!vsEntryStateExists(prefix, entryStateUId)) {

			EntryState entryState = new EntryState();

			entryState.setChangeType(ChangeType.NEW);
			entryState.setRelativeOrder(0L);

			vsEntryStateDao.insertEntryState(entryStateUId, vsPLEntryUId,
					ReferenceType.PICKLISTENTRY.name(), null, entryState);
		}
	}

	@Override
	public String getLatestRevision(String pickListEntryNodeUId) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		return (String) this.getSqlMapClientTemplate().queryForObject(
				GET_PICKLIST_ENTRYNODE_LATEST_REVISION_ID_BY_UID, 
				new PrefixedParameter(prefix, pickListEntryNodeUId));	
	}

	/**
	 * @return the versionsDao
	 */
	public VersionsDao getVersionsDao() {
		return versionsDao;
	}

	/**
	 * @param versionsDao the versionsDao to set
	 */
	public void setVersionsDao(VersionsDao versionsDao) {
		this.versionsDao = versionsDao;
	}

	@Override
	public void deletePLEntryNodeByUId(String pickListEntryNodeUId) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		this.getSqlMapClientTemplate().delete(
				DELETE_PL_ENTRY_NODE_BY_UID_SQL,
				new PrefixedParameterTuple(prefix, pickListEntryNodeUId,
						ReferenceType.PICKLISTENTRY.name()));
	}
}

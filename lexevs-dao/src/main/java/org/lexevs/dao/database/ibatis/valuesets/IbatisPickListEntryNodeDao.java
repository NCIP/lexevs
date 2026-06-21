
package org.lexevs.dao.database.ibatis.valuesets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.valueSets.PickListEntry;
import org.LexGrid.valueSets.PickListEntryExclusion;
import org.LexGrid.valueSets.PickListEntryNode;
import org.LexGrid.valueSets.PickListEntryNodeChoice;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.valuesets.PickListEntryNodeDao;
import org.lexevs.dao.database.access.valuesets.VSEntryStateDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao.ReferenceType;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.valuesets.parameter.InsertOrUpdatePickListEntryBean;
import org.lexevs.dao.database.ibatis.valuesets.parameter.InsertOrUpdateValueSetsMultiAttribBean;
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

	private static String UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "updateMultiAttribEntryStateUId";
	
	private static String GET_PICKLIST_ENTRYNODE_METADATA_BY_PLENTRY_GUID_SQL = PICKLIST_ENTRY_NODE_NAMESPACE + "getPickListEntryNodeMeatDataByPLEntryGuid";
	
	private static String UPDATE_PICKLIST_ENTRYNODE_BY_UID_SQL = PICKLIST_ENTRY_NODE_NAMESPACE + "updatePickListEntryNodeByUId";
	
	private static String UPDATE_PICKLIST_ENTRYNODE_VER_ATTRIB_BY_UID_SQL = PICKLIST_ENTRY_NODE_NAMESPACE + "updatePickListEntryNodeVerAttribByUId";
	
	private static String GET_ENTRYSTATE_UID_BY_PICKLISTENTRYNODE_UID_SQL = PICKLIST_ENTRY_NODE_NAMESPACE + "getEntryStateUIdByPickListEntryNodeUId";
	
	private static String UPDATE_PICKLIST_ENTRYNODE_ENTRYSTATE_UID_SQL = PICKLIST_ENTRY_NODE_NAMESPACE + "updateEntryStateUIdByPickListEntryUId";
	
	private static String GET_PICKLIST_ENTRYNODE_LATEST_REVISION_ID_BY_UID = PICKLIST_ENTRY_NODE_NAMESPACE + "getPickListEntryNodeLatestRevisionIdByUId";
	
	private static String DELETE_PL_ENTRY_NODE_BY_UID_SQL = PICKLIST_ENTRY_NODE_NAMESPACE + "deletePickListEntryNodeByUId";
	
	private static String GET_PREV_REV_ID_FROM_GIVEN_REV_ID_FOR_PLENTRY_SQL = PICKLIST_ENTRY_NODE_NAMESPACE + "getPrevRevIdFromGivenRevIdForPLEntry";
	
	private static String GET_PICKLIST_ENTRY_METADATA_FROM_HISTORY_BY_REVISION_SQL = PICKLIST_ENTRY_NODE_NAMESPACE + "getPickListEntryMetaDataHistoryByRevision";
	
	private static String GET_CONTEXT_LIST_FROM_HISTORY_BY_PARENT_ENTRYSTATEGUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "getContextListFromHistoryByParentEntryStateGuidandType";
	
	private static String GET_ENTRYNODE_PROPERTY_IDS_LIST_BY_ENTRYNODE_UID_SQL = PICKLIST_ENTRY_NODE_NAMESPACE + "getEntryNodePropertyIdsListByEntryNodeUId";

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

		return (String) this.getSqlSessionTemplate().selectOne(
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
			plEntryBean.setPrefix(this.getPrefixResolver().resolveDefaultPrefix());
			
			if (plEntry != null)
			{
				plEntryBean.setInclude(true);
				plEntryBean.setEntityCode(plEntry.getEntityCode());
				plEntryBean.setEntityCodeNamespace(plEntry.getEntityCodeNamespace());
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
					insertOrUpdateValueSetsMultiAttribBean.setEntryStateUId(vsEntryStateGuid);
					insertOrUpdateValueSetsMultiAttribBean.setPrefix(this.getPrefixResolver().resolveDefaultPrefix());
					
					if (contextList == null)
						contextList = new ArrayList<InsertOrUpdateValueSetsMultiAttribBean>();
					
					contextList.add(insertOrUpdateValueSetsMultiAttribBean);
				}
			}
			else if (plExclusion != null)
			{
				plEntryBean.setInclude(false);
				plEntryBean.setEntityCodeNamespace(plExclusion.getEntityCodeNamespace());
				plEntryBean.setEntityCode(plExclusion.getEntityCode());
			}
			
			// insert into plEntry table
			this.getSqlSessionTemplate().insert(INSERT_PICKLIST_ENTRY_SQL, plEntryBean);
			
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
					this.getSqlSessionTemplate().insert(INSERT_MULTI_ATTRIB_SQL, pickContextMultiAttrib);
				}
			}
		
		}
		return plEntryGuid;
	}

	@Override
	public void removeAllPickListEntryNodeMultiAttributes(
			String pickListEntryNodeUId) {
		
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		this.getSqlSessionTemplate().delete(
				DELETE_CONTEXT_BY_PARENT_GUID_AND_TYPE_SQL,
				new PrefixedParameterTuple(prefix, pickListEntryNodeUId,
						ReferenceType.PICKLISTENTRY.name()));
		
		this.getSqlSessionTemplate().delete(
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
		
		InsertOrUpdatePickListEntryBean plEntryNodeBean = (InsertOrUpdatePickListEntryBean) this
				.getSqlSessionTemplate().selectOne(
						GET_PICKLIST_ENTRYNODE_METADATA_BY_PLENTRY_GUID_SQL,
						new PrefixedParameter(prefix, pickListEntryNodeUId));
	
		plEntryNodeBean.setPrefix(histPrefix);
		
		this.getSqlSessionTemplate().insert(
				INSERT_PICKLIST_ENTRY_SQL, plEntryNodeBean);
		
		for (InsertOrUpdateValueSetsMultiAttribBean vsMultiAttrib : plEntryNodeBean.getVsMultiAttribList())
		{
			vsMultiAttrib.setPrefix(histPrefix);
			
			this.getSqlSessionTemplate().insert(INSERT_MULTI_ATTRIB_SQL, vsMultiAttrib);
		}
		
		if (!vsEntryStateExists(prefix, plEntryNodeBean.getEntryStateUId())) {

			EntryState entryState = new EntryState();

			entryState.setChangeType(ChangeType.NEW);
			entryState.setRelativeOrder(0L);

			vsEntryStateDao
					.insertEntryState(plEntryNodeBean.getEntryStateUId(),
							plEntryNodeBean.getUId(), ReferenceType.PICKLISTENTRY.name(), null,
							entryState);
		}
		
		return plEntryNodeBean.getEntryStateUId();
	}

	@Override
	public String updatePickListEntryNode(String pickListEntryNodeUId,
			PickListEntryNode pickListEntryNode) {

		InsertOrUpdatePickListEntryBean bean = buildInsertOrUpdatePickListEntryBean(
				pickListEntryNodeUId, pickListEntryNode);

		this.getSqlSessionTemplate().update(
				UPDATE_PICKLIST_ENTRYNODE_BY_UID_SQL, bean);
		
		PickListEntry pickListEntry = pickListEntryNode
				.getPickListEntryNodeChoice().getInclusionEntry();
		
		String prefix = bean.getPrefix();
		
		if( pickListEntry != null ) {
			
			if( pickListEntry.getPickContextCount() != 0 ) {
				
				this.getSqlSessionTemplate().delete(
						DELETE_CONTEXT_BY_PARENT_GUID_AND_TYPE_SQL,
						new PrefixedParameterTuple(prefix, pickListEntryNodeUId,
								ReferenceType.PICKLISTDEFINITION.name()));
				
				String[] contextList = pickListEntry.getPickContext();
				
				for (int i = 0; i < contextList.length; i++) {
					InsertOrUpdateValueSetsMultiAttribBean insertOrUpdateValueSetsMultiAttribBean = new InsertOrUpdateValueSetsMultiAttribBean();
					insertOrUpdateValueSetsMultiAttribBean.setUId(this.createUniqueId());
					insertOrUpdateValueSetsMultiAttribBean.setReferenceUId(pickListEntryNodeUId);
					insertOrUpdateValueSetsMultiAttribBean.setReferenceType(ReferenceType.PICKLISTENTRY.name());
					insertOrUpdateValueSetsMultiAttribBean.setAttributeType(SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT);
					insertOrUpdateValueSetsMultiAttribBean.setAttributeValue(contextList[i]);
					insertOrUpdateValueSetsMultiAttribBean.setRole(null);
					insertOrUpdateValueSetsMultiAttribBean.setSubRef(null);
					insertOrUpdateValueSetsMultiAttribBean.setEntryStateUId(bean.getEntryStateUId());
					insertOrUpdateValueSetsMultiAttribBean.setPrefix(prefix);
					
					this.getSqlSessionTemplate().insert(INSERT_MULTI_ATTRIB_SQL, insertOrUpdateValueSetsMultiAttribBean);
				}
			}
		} else {
			
			this.getSqlSessionTemplate().update(
					UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
					new PrefixedParameterTriple(prefix, pickListEntryNodeUId,
							SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT,
							bean.getEntryStateUId()));
		}
		
		return bean.getEntryStateUId();
	}

	@Override
	public String updateVersionableAttributes(String pickListEntryNodeUId,
			PickListEntryNode pickListEntryNode) {
		
		String entryStateUId = this.createUniqueId();
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();

		InsertOrUpdatePickListEntryBean bean = new InsertOrUpdatePickListEntryBean();
		
		bean.setUId(pickListEntryNodeUId);
		bean.setEntryStateUId(entryStateUId);
		bean.setPrefix(prefix);
		bean.setPickListEntryNode(pickListEntryNode);

		this.getSqlSessionTemplate().update(
				UPDATE_PICKLIST_ENTRYNODE_VER_ATTRIB_BY_UID_SQL, bean);

		this.getSqlSessionTemplate().update(
				UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
				new PrefixedParameterTriple(prefix, pickListEntryNodeUId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT,
						entryStateUId));
		
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

		return (String) this.getSqlSessionTemplate().selectOne(
				GET_ENTRYSTATE_UID_BY_PICKLISTENTRYNODE_UID_SQL,
				new PrefixedParameter(prefix, pickListEntryNodeUId));
	}

	@Override
	public void updateEntryStateUId(String pickListEntryNodeUId,
			String entryStateUId) {
		
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		this.getSqlSessionTemplate().update(
				UPDATE_PICKLIST_ENTRYNODE_ENTRYSTATE_UID_SQL, 
				new PrefixedParameterTuple(prefix, pickListEntryNodeUId, entryStateUId));
		
		this.getSqlSessionTemplate().update(
				UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
				new PrefixedParameterTriple(prefix, pickListEntryNodeUId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT,
						entryStateUId));
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
		
		return (String) this.getSqlSessionTemplate().selectOne(
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
		
		// delete entry state record
		this.vsEntryStateDao.deleteAllEntryStateByEntryUIdAndType(pickListEntryNodeUId, ReferenceType.PICKLISTENTRY.name());
		
		// delete pick list entry node
		this.getSqlSessionTemplate().delete(
				DELETE_PL_ENTRY_NODE_BY_UID_SQL,
				new PrefixedParameterTuple(prefix, pickListEntryNodeUId,
						ReferenceType.PICKLISTENTRY.name()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public PickListEntryNode resolvePLEntryNodeByRevision(
			String pickListId, String plEntryId, String revisionId) throws LBRevisionException {
		
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		String vsPLEntryUId = this.getPickListEntryNodeUId(pickListId, plEntryId);
		
		String tempRevId = revisionId;
		
		if (vsPLEntryUId == null) {
			throw new LBRevisionException(
					"PLEntry "
							+ plEntryId
							+ " doesn't exist in lexEVS. "
							+ "Please check the plEntryId. Its possible that the given pickListEntry "
							+ "has been REMOVEd from the lexEVS system in the past.");
		}
		
		String plEntryRevisionId = this.getLatestRevision(vsPLEntryUId);
		
		// 1. If 'revisionId' is null or 'revisionId' is the latest revision of the picklistEntry
		// then use getPLEntryById to get the PLEntry object and return.
		
		if (StringUtils.isEmpty(revisionId)
				|| StringUtils.isEmpty(plEntryRevisionId)
				|| revisionId.equals(plEntryRevisionId)) {
			return getPLEntryByUId(vsPLEntryUId);
		}
		
		// 2. Get the earliest revisionId on which change was applied on given 
		// PLEntry with reference given revisionId.
		
		HashMap<String,String> revisionIdMap = (HashMap) this.getSqlSessionTemplate()
				.<String,String>selectMap(
						GET_PREV_REV_ID_FROM_GIVEN_REV_ID_FOR_PLENTRY_SQL,
						new PrefixedParameterTuple(prefix, vsPLEntryUId,
								revisionId), "revId");
		
		if( revisionIdMap.isEmpty() ) {
			revisionId = null;
		} else {
		
			revisionId = (String) revisionIdMap.keySet().toArray()[0];
		
			if( revisionId.equals(plEntryRevisionId)) {
				return getPLEntryByUId(vsPLEntryUId);
			}
		}
		
		// 3. Get the pick list entry data from history.
		PickListEntryNode pickListEntryNode = null;
		InsertOrUpdatePickListEntryBean plEntryBean = null;
			
		plEntryBean = (InsertOrUpdatePickListEntryBean) this
				.getSqlSessionTemplate().selectOne(
						GET_PICKLIST_ENTRY_METADATA_FROM_HISTORY_BY_REVISION_SQL,
						new PrefixedParameterTuple(prefix, vsPLEntryUId,
								revisionId));
		
		if (plEntryBean != null) {

			pickListEntryNode = getPLEntryNode(plEntryBean);

			// Get pick list definition context
			if (pickListEntryNode != null) {
				PickListEntryNodeChoice entryNodeChoice = pickListEntryNode
						.getPickListEntryNodeChoice();

				if (entryNodeChoice.getInclusionEntry() != null) {
					List<String> contextList = this
							.getSqlSessionTemplate()
							.<String>selectList(
									GET_CONTEXT_LIST_FROM_HISTORY_BY_PARENT_ENTRYSTATEGUID_AND_TYPE_SQL,
									new PrefixedParameterTuple(prefix,
											plEntryBean.getEntryStateUId(),
											ReferenceType.PICKLISTENTRY.name()));

					entryNodeChoice.getInclusionEntry().setPickContext(
							contextList);
				}
			}
		}
		
		// 4. If pick list entry is not in history, get it from base table.
		if (pickListEntryNode == null && revisionId != null) {

			InsertOrUpdatePickListEntryBean plEntryNodeBean = (InsertOrUpdatePickListEntryBean) this
					.getSqlSessionTemplate().selectOne(
							GET_PICKLIST_ENTRYNODE_METADATA_BY_PLENTRY_GUID_SQL,
							new PrefixedParameter(prefix, vsPLEntryUId));

			if (plEntryNodeBean != null) {

				pickListEntryNode = getPLEntryNode(plEntryNodeBean);
			}
		}
		
		// 5. Get all pick list entry node property.
		if (pickListEntryNode != null) {
			List<String> propertyIdList = this
					.getSqlSessionTemplate()
					.<String>selectList(
							GET_ENTRYNODE_PROPERTY_IDS_LIST_BY_ENTRYNODE_UID_SQL,
							new PrefixedParameterTuple(prefix, vsPLEntryUId,
									ReferenceType.PICKLISTENTRY.name()));

			Properties properties = new Properties();

			for (String propId : propertyIdList) {
				Property pickListEntryProperty = null;

				try {
					pickListEntryProperty = vsPropertyDao
							.resolveVSPropertyByRevision(vsPLEntryUId, propId,
									tempRevId);
				} catch (LBRevisionException e) {
					continue;
				}

				properties.addProperty(pickListEntryProperty);
			}

			pickListEntryNode.setProperties(properties);
		}
		
		return pickListEntryNode;
	}

	public PickListEntryNode getPLEntryByUId(String vsPLEntryUId) {

		PickListEntryNode plEntryNode = null;

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();

		InsertOrUpdatePickListEntryBean plEntryNodeBean = (InsertOrUpdatePickListEntryBean) this
				.getSqlSessionTemplate().selectOne(
						GET_PICKLIST_ENTRYNODE_METADATA_BY_PLENTRY_GUID_SQL,
						new PrefixedParameter(prefix, vsPLEntryUId));

		if (plEntryNodeBean != null) {

			plEntryNode = getPLEntryNode(plEntryNodeBean);
			
			List<Property> props = this.vsPropertyDao.getAllPropertiesOfParent(
					plEntryNodeBean.getUId(), ReferenceType.PICKLISTENTRY);
			if (props != null) {
				Properties properties = new Properties();
				properties.getPropertyAsReference().addAll(props);
				plEntryNode.setProperties(properties);
			}
		}

		return plEntryNode;
	}
	
	private PickListEntryNode getPLEntryNode(
			InsertOrUpdatePickListEntryBean plEntryNodeBean) {
		PickListEntryNode plEntryNode = plEntryNodeBean.getPickListEntryNode();

		PickListEntryNodeChoice plEntryNodeChoice = new PickListEntryNodeChoice();

		if (plEntryNodeBean.isInclude()) {
			PickListEntry plEntry = new PickListEntry();

			plEntry.setEntityCode(plEntryNodeBean.getEntityCode());
			plEntry.setEntityCodeNamespace(plEntryNodeBean
					.getEntityCodeNamespace());
			plEntry.setEntryOrder(plEntryNodeBean.getEntryOrder());
			plEntry.setIsDefault(plEntryNodeBean.isDefault());
			plEntry.setLanguage(plEntryNodeBean.getLangauage());
			plEntry.setMatchIfNoContext(plEntryNodeBean.isMatchIfNoContext());
			plEntry.setPropertyId(plEntryNodeBean.getPropertyId());
			plEntry.setPickText(plEntryNodeBean.getPickText());

			List<InsertOrUpdateValueSetsMultiAttribBean> multiAttribList = plEntryNodeBean
					.getVsMultiAttribList();

			if( multiAttribList != null && multiAttribList.size() != 0 ) {
				List<String> contextList = new ArrayList<String>();
	
				for (InsertOrUpdateValueSetsMultiAttribBean multiAttibBean : multiAttribList) {
	
					if (SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT
							.equals(multiAttibBean.getAttributeType())) {
						contextList.add(multiAttibBean.getAttributeValue());
					}
				}
	
				if (contextList != null)
					plEntry.setPickContext(contextList);
			}

			plEntryNodeChoice.setInclusionEntry(plEntry);
		} else {
			PickListEntryExclusion plEntryExclude = new PickListEntryExclusion();

			plEntryExclude.setEntityCode(plEntryNodeBean.getEntityCode());
			plEntryExclude.setEntityCodeNamespace(plEntryNodeBean
					.getEntityCodeNamespace());

			plEntryNodeChoice.setExclusionEntry(plEntryExclude);
		}

		plEntryNode.setPickListEntryNodeChoice(plEntryNodeChoice);

		return plEntryNode;
	}
}

package org.lexevs.dao.database.ibatis.valuesets;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedHierarchy;
import org.LexGrid.naming.URIMap;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListEntry;
import org.LexGrid.valueSets.PickListEntryNode;
import org.LexGrid.valueSets.PickListEntryNodeChoice;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.dao.database.access.valuesets.PickListDao;
import org.lexevs.dao.database.access.valuesets.PickListEntryNodeDao;
import org.lexevs.dao.database.access.valuesets.VSEntryStateDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao.ReferenceType;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.constants.classifier.mapping.ClassToStringMappingClassifier;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.codingscheme.parameter.InsertOrUpdateURIMapBean;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.valuesets.parameter.InsertOrUpdateValueSetsMultiAttribBean;
import org.lexevs.dao.database.ibatis.valuesets.parameter.InsertPickListDefinitionBean;
import org.lexevs.dao.database.ibatis.valuesets.parameter.PickListEntryNodeBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * The Class IbatisPickListDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Cacheable(cacheName = "IbatisPickListDao")
public class IbatisPickListDao extends AbstractIbatisDao implements PickListDao {
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	/** The PICKLIS t_ namespace. */
	public static String PICKLIST_NAMESPACE = "PickList.";
	
	public static String PICKLIST_ENTRYNODE_NAMESPACE = "PickListEntryNode.";
	
	public static String VS_MULTIATTRIB_NAMESPACE = "VSMultiAttrib.";
	
	public static String VS_MAPPING_NAMESPACE = "VSMapping.";
	
	private static String SUPPORTED_ATTRIB_GETTER_PREFIX = "_supported";
	
	/** The INSER t_ picklis t_ definitio n_ sql. */
	public static String INSERT_PICKLIST_DEFINITION_SQL = PICKLIST_NAMESPACE + "insertPickListDefinition";
	
	/** The GE t_ picklis t_ id s_ sql. */
	public static String GET_PICKLIST_IDS_SQL = PICKLIST_NAMESPACE + "getPickListIds";
	
	/** The GE t_ picklis t_ gui d_ b y_ picklisti d_ sql. */
	public static String GET_PICKLIST_GUID_BY_PICKLISTID_SQL = PICKLIST_NAMESPACE + "getPickListGuidByPickListId";
	
	public static String GET_PICKLIST_ENTRYNODEGUID_BY_PICKLISTID_AND_PLENTRYID_SQL = PICKLIST_NAMESPACE + "getPickListEntryNodeGuidByPickListIdAndPLEntryId";
	
	/** The GE t_ picklis t_ definitio n_ b y_ picklisti d_ sql. */
	public static String GET_PICKLIST_DEFINITION_BY_PICKLISTID_SQL = PICKLIST_NAMESPACE + "getPickListDefinitionByPickListId";

	public static String GET_PICKLIST_DEFINITION_METADATA_BY_PICKLISTID_SQL = PICKLIST_NAMESPACE + "getPickListDefinitionMetadataByUId";
	
	public static String GET_PICKLIST_DEFINITION_ID_FOR_VALUESET_DEFINITION_URI_SQL = PICKLIST_NAMESPACE + "getPickListDefinitionIdForValueSetDefinitionUri";
	
	public static String GET_PICKLIST_ENTRYNODE_BEAN_BY_PICKLIST_GUID_SQL = PICKLIST_ENTRYNODE_NAMESPACE + "getPickListEntryNodeBeanByPickListGuid";
	
	public static String GET_PICKLIST_DEFINITION_ID_FOR_ENTITYCODE_ENTITYCODENAMESPACE_SQL = PICKLIST_NAMESPACE + "getPickListDefinitionIdForEntityCodeAndEntityNamespace";
	
	public static String GET_PICKLIST_DEFINITION_ID_FOR_ENTITYCODE_ENTITYCODENAMESPACE_PROPERTYID_SQL = PICKLIST_NAMESPACE + "getPickListDefinitionIdForEntityCodeEntityNamespaceAndPropertyId";
	
	public static String REMOVE_PICKLIST_DEFINITION_BY_PICKLISTID_SQL = PICKLIST_NAMESPACE + "removePickListDefinitionByPickListId";
	
	public static String REMOVE_PICKLIST_ENTRY_BY_PICKLISTGUID_SQL = PICKLIST_NAMESPACE + "removePickListEntryByPickListGuid";
	
	public static String GET_SOURCE_LIST_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "getSourceListByParentGuidandType";
	
	public static String GET_SOURCE_LIST_FROM_HISTORY_BY_PARENT_ENTRYSTATEGUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "getSourceListFromHistoryByParentEntryStateGuidandType";
	
	public static String GET_CONTEXT_LIST_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "getContextListByParentGuidandType";
	
	public static String GET_CONTEXT_LIST_FROM_HISTORY_BY_PARENT_ENTRYSTATEGUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "getContextListFromHistoryByParentEntryStateGuidandType";
	
	public static String INSERT_MULTI_ATTRIB_SQL = VS_MULTIATTRIB_NAMESPACE + "insertMultiAttrib";
	
	public static String DELETE_SOURCE_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "deleteSourceByParentGuidAndType";
	
	public static String DELETE_CONTEXT_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "deleteContextByParentGuidAndType";
	
	public static String DELETE_PICKLIST_ENTRY_CONTEXT_BY_PICKLIST_GUID_SQL = VS_MULTIATTRIB_NAMESPACE + "deletePickListEntryContextByPickListGuid";
	
	private static String UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "updateMultiAttribEntryStateUId";
	
	public static String GET_URIMAPS_BY_REFERENCE_GUID_SQL = VS_MAPPING_NAMESPACE + "getURIMaps";
	
	public static String GET_URIMAPS_BY_REFERENCE_GUID_LOCALNAME_AND_TYPE_SQL = VS_MAPPING_NAMESPACE + "getURIMapByLocalNameAndType";
	
	public static String GET_PICKLISTID_FOR_SUPPORTED_TAG_AND_VALUE_SQL = VS_MAPPING_NAMESPACE + "getPickListIdForSupportedTagAndValue";
	
	public static String INSERT_URIMAPS_SQL = VS_MAPPING_NAMESPACE + "insertURIMap";
	
	public static String UPDATE_URIMAPS_BY_LOCALID_SQL = VS_MAPPING_NAMESPACE + "updateUriMapByLocalId";
	
	public static String DELETE_URIMAPS_BY_REFERENCE_GUID_SQL = VS_MAPPING_NAMESPACE + "deleteMappingsByReferenceGuid";
	
	private static String UPDATE_PICKLIST_DEFINITION_BY_UID_SQL = PICKLIST_NAMESPACE + "updatePickListDefinitionByUId";
	
	private static String UPDATE_PL_VERSIONABLE_ATTRIBUTE_BY_UID_SQL = PICKLIST_NAMESPACE + "updatePLVersionableAttributesByUId";
	
	private static String GET_ENTRYSTATE_UID_BY_PICKLIST_UID_SQL = PICKLIST_NAMESPACE + "getEntryStateUIdByPickListUId";
	
	private static String UPDATE_PICKLIST_ENTRYSTATE_UID_SQL = PICKLIST_NAMESPACE + "updateEntryStateUIdByPickListUId";
	
	private static String GET_PICKLIST_DEFINITION_LATEST_REVISION_ID_BY_UID = PICKLIST_NAMESPACE + "getPickListDefinitionLatestRevisionIdByUId";
	
	private static String GET_PREV_REV_ID_FROM_GIVEN_REV_ID_FOR_PLDEF_SQL = PICKLIST_NAMESPACE + "getPrevRevIdFromGivenRevIdForPLDef";
	
	private static String GET_PICKLIST_DEFINITION_METADATA_FROM_HISTORY_BY_REVISION_SQL = PICKLIST_NAMESPACE + "getPickListDefinitionMetaDataHistoryByRevision";
	
	private static String GET_PL_ENTRY_NODES_LIST_BY_PICKLIST_ID_SQL = PICKLIST_NAMESPACE + "getPickListEntryNodeListByPickListId";
	
	private static String GET_PICKLIST_DEF_PROPERTY_LIST_BY_PICKLIST_ID_SQL = PICKLIST_NAMESPACE + "getPickListDefinitionPropertyListByPickListId";
	
	
	/** The class to string mapping classifier. */
	private ClassToStringMappingClassifier classToStringMappingClassifier = new ClassToStringMappingClassifier();
	
	/** The versions dao. */
	private VersionsDao versionsDao;
	
	private VSPropertyDao vsPropertyDao;
	
	private VSEntryStateDao vsEntryStateDao;
	
	private PickListEntryNodeDao pickListEntryNodeDao = null;

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.picklist.PickListDao#getPickListDefinitionById(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public PickListDefinition getPickListDefinitionById(String pickListId) {
		final String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		PickListDefinition plDef = (PickListDefinition) 
			this.getSqlMapClientTemplate().queryForObject(GET_PICKLIST_DEFINITION_BY_PICKLISTID_SQL, 
				new PrefixedParameter(prefix, pickListId));
		
		if (plDef != null)
		{
			String plDefGuid = getPickListGuidFromPickListId(pickListId);
			
			List<PickListEntryNodeBean> plEntryNodeBeans = (List<PickListEntryNodeBean>) this.getSqlMapClientTemplate().queryForList(GET_PICKLIST_ENTRYNODE_BEAN_BY_PICKLIST_GUID_SQL, 
				new PrefixedParameter(prefix, plDefGuid));
			
			if (plEntryNodeBeans != null)
			{
				for (PickListEntryNodeBean plEntryNodeBean : plEntryNodeBeans)		
				{
					PickListEntryNode plEntryNode = new PickListEntryNode();
					plEntryNode.setPickListEntryId(plEntryNodeBean.getPickListEntryId());
					plEntryNode.setEffectiveDate(plEntryNodeBean.getEffectiveDate());
					plEntryNode.setExpirationDate(plEntryNodeBean.getExpirationDate());
					plEntryNode.setEntryState(plEntryNodeBean.getEntryState());
					plEntryNode.setIsActive(plEntryNodeBean.getIsActive());
					plEntryNode.setOwner(plEntryNodeBean.getOwner());
					
					PickListEntryNodeChoice plEntryNodeChoice = new PickListEntryNodeChoice();
					
					if (plEntryNodeBean.getInclude().booleanValue())
					{
						PickListEntry plEntry = plEntryNodeBean.getPickListEntry();
						
						// get pick context list
						List<String> contextList = this.getSqlMapClientTemplate().queryForList(GET_CONTEXT_LIST_BY_PARENT_GUID_AND_TYPE_SQL, 
								new PrefixedParameterTuple(prefix, plEntryNodeBean.getVsPLEntryGuid(), ReferenceType.PICKLISTENTRY.name())); 
						
						if (contextList != null)
							plEntry.setPickContext(contextList);
						
						plEntryNodeChoice.setInclusionEntry(plEntry);						
					}
					else
					{
						plEntryNodeChoice.setExclusionEntry(plEntryNodeBean.getPickListEntryExclusion());
					}
					plEntryNode.setPickListEntryNodeChoice(plEntryNodeChoice);
					
					List<Property> props = this.vsPropertyDao.getAllPropertiesOfParent(plEntryNodeBean.getVsPLEntryGuid(), ReferenceType.PICKLISTENTRY);
					if (props != null)
					{
						Properties properties = new Properties();
						properties.getPropertyAsReference().addAll(props);
						plEntryNode.setProperties(properties);
					}
					
					plDef.addPickListEntryNode(plEntryNode);
				}
			}
			
			// get pick list definition properties
			List<Property> props = this.vsPropertyDao.getAllPropertiesOfParent(plDefGuid, ReferenceType.PICKLISTDEFINITION);
			
			if (props != null)
			{
				Properties properties = new Properties();
				properties.getPropertyAsReference().addAll(props);
				plDef.setProperties(properties);
			}
			
			// Get pick list definition source
			List<Source> sourceList = this.getSqlMapClientTemplate().queryForList(GET_SOURCE_LIST_BY_PARENT_GUID_AND_TYPE_SQL, 
					new PrefixedParameterTuple(prefix, plDefGuid, ReferenceType.PICKLISTDEFINITION.name())); 
			
			if (sourceList != null)
				plDef.setSource(sourceList);
			
			// Get pick list definition context
			List<String> contextList = this.getSqlMapClientTemplate().queryForList(GET_CONTEXT_LIST_BY_PARENT_GUID_AND_TYPE_SQL, 
					new PrefixedParameterTuple(prefix, plDefGuid, ReferenceType.PICKLISTDEFINITION.name())); 
			
			if (contextList != null)
				plDef.setDefaultPickContext(contextList);
			
			
			// get mappings
			plDef.setMappings(getMappings(plDefGuid));
		}
		return plDef;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.picklist.PickListDao#getGuidFromPickListId(java.lang.String)
	 */
	@Override
//	@CacheMethod
	public String getPickListGuidFromPickListId(String pickListId) {
		return (String) 
		this.getSqlMapClientTemplate().queryForObject(GET_PICKLIST_GUID_BY_PICKLISTID_SQL, 
			new PrefixedParameter(this.getPrefixResolver().resolveDefaultPrefix(), pickListId));
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@CacheMethod
	public List<String> getPickListDefinitionIdForValueSetDefinitionURI(
			String valueSetDefURI) {
		return (List<String>) this.getSqlMapClientTemplate().queryForList(GET_PICKLIST_DEFINITION_ID_FOR_VALUESET_DEFINITION_URI_SQL, 
				new PrefixedParameter(this.getPrefixResolver().resolveDefaultPrefix(), valueSetDefURI));
	}
	
	@Override
	public String insertPickListDefinition(PickListDefinition definition, String systemReleaseUri, Mappings mappings) {
		if (definition == null)
			return null;
		
		String pickListGuid = this.createUniqueId();
		String vsEntryStateGuid = this.createUniqueId();
		
		String systemReleaseId = this.versionsDao.getSystemReleaseIdByUri(systemReleaseUri);
		EntryState entryState = definition.getEntryState();
		
		if (entryState != null)
		{
			this.vsEntryStateDao.insertEntryState(vsEntryStateGuid, pickListGuid, 
					ReferenceType.PICKLISTDEFINITION.name(), null, entryState);
		}
		
		InsertPickListDefinitionBean plDefBean = new InsertPickListDefinitionBean();
		plDefBean.setUId(pickListGuid);
		plDefBean.setPickListDefinition(definition);
		plDefBean.setPrefix(getPrefix());
		plDefBean.setSystemReleaseUId(systemReleaseId);
		plDefBean.setEntryStateUId(vsEntryStateGuid);
		
		this.getSqlMapClientTemplate().insert(INSERT_PICKLIST_DEFINITION_SQL, plDefBean);
		
		// insert pickListDefinition properties
		if (definition.getProperties() != null)
		{
			for (Property property : definition.getProperties().getPropertyAsReference())
			{
				this.vsPropertyDao.insertProperty(pickListGuid, ReferenceType.PICKLISTDEFINITION, property);
			}
		}
		
		// insert pick list entry node
		for (PickListEntryNode plEntryNode : definition.getPickListEntryNode()) 
		{
			this.getPickListEntryNodeDao().insertPickListEntry(pickListGuid, plEntryNode);
		}
		
		// insert default pick context list
		for (String pickContext : definition.getDefaultPickContextAsReference())
		{
			InsertOrUpdateValueSetsMultiAttribBean insertOrUpdateValueSetsMultiAttribBean = new InsertOrUpdateValueSetsMultiAttribBean();
			insertOrUpdateValueSetsMultiAttribBean.setUId(this.createUniqueId());
			insertOrUpdateValueSetsMultiAttribBean.setReferenceUId(pickListGuid);
			insertOrUpdateValueSetsMultiAttribBean.setReferenceType(ReferenceType.PICKLISTDEFINITION.name());
			insertOrUpdateValueSetsMultiAttribBean.setAttributeType(SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT);
			insertOrUpdateValueSetsMultiAttribBean.setAttributeValue(pickContext);
			insertOrUpdateValueSetsMultiAttribBean.setRole(null);
			insertOrUpdateValueSetsMultiAttribBean.setSubRef(null);
			insertOrUpdateValueSetsMultiAttribBean.setEntryStateUId(vsEntryStateGuid);
			
			insertOrUpdateValueSetsMultiAttribBean.setPrefix(getPrefix());
			
			this.getSqlMapClientTemplate().insert(INSERT_MULTI_ATTRIB_SQL, insertOrUpdateValueSetsMultiAttribBean);
		}
		
		// insert pick list definition source list
		for (Source source : definition.getSourceAsReference())
		{
			InsertOrUpdateValueSetsMultiAttribBean insertOrUpdateValueSetsMultiAttribBean = new InsertOrUpdateValueSetsMultiAttribBean();
			insertOrUpdateValueSetsMultiAttribBean.setUId(this.createUniqueId());
			insertOrUpdateValueSetsMultiAttribBean.setReferenceUId(pickListGuid);
			insertOrUpdateValueSetsMultiAttribBean.setReferenceType(ReferenceType.PICKLISTDEFINITION.name());
			insertOrUpdateValueSetsMultiAttribBean.setAttributeType(SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE);
			insertOrUpdateValueSetsMultiAttribBean.setAttributeValue(source.getContent());
			insertOrUpdateValueSetsMultiAttribBean.setRole(source.getRole());
			insertOrUpdateValueSetsMultiAttribBean.setSubRef(source.getSubRef());
			insertOrUpdateValueSetsMultiAttribBean.setEntryStateUId(vsEntryStateGuid);
			
			insertOrUpdateValueSetsMultiAttribBean.setPrefix(getPrefix());
			
			this.getSqlMapClientTemplate().insert(INSERT_MULTI_ATTRIB_SQL, insertOrUpdateValueSetsMultiAttribBean);
		}
		
		// insert pick list definition mappings
		if (mappings != null)
			insertMappings(pickListGuid, mappings);
		
		if (definition.getMappings() != null)
			insertMappings(pickListGuid, definition.getMappings());
		
		return pickListGuid;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.picklist.PickListDao#getPickListIds()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPickListIds() {
		return this.getSqlMapClientTemplate().queryForList(GET_PICKLIST_IDS_SQL,  new PrefixedParameter(this.getPrefixResolver().resolveDefaultPrefix(), null));
	}	
	
	/**
	 * Gets the prefix.
	 * 
	 * @return the prefix
	 */
	protected String getPrefix() {
		return this.getPrefixResolver().resolveDefaultPrefix();
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.AbstractBaseDao#doGetSupportedLgSchemaVersions()
	 */
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createNonTypedList(supportedDatebaseVersion);
	}

	/**
	 * Gets the versions dao.
	 * 
	 * @return the versions dao
	 */
	public VersionsDao getVersionsDao() {
		return versionsDao;
	}

	/**
	 * Sets the versions dao.
	 * 
	 * @param versionsDao the new versions dao
	 */
	public void setVersionsDao(VersionsDao versionsDao) {
		this.versionsDao = versionsDao;
	}

	@ClearCache
	public void removePickListDefinitionByPickListId(String pickListDefinitionId) {
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		String pickListGuid = (String) this.getSqlMapClientTemplate().queryForObject(
				GET_PICKLIST_GUID_BY_PICKLISTID_SQL, 
				new PrefixedParameter(prefix, pickListDefinitionId));
		
		// remove entry state details
		this.vsEntryStateDao.deleteAllEntryStateByEntryUIdAndType(pickListGuid, ReferenceType.PICKLISTDEFINITION.name());
		//this.vsEntryStateDao.deleteAllEntryStatesOfPickListDefinitionByUId(pickListGuid);
		
		// remove all pick list entry context
		this.getSqlMapClientTemplate().delete(
				DELETE_PICKLIST_ENTRY_CONTEXT_BY_PICKLIST_GUID_SQL, 
				new PrefixedParameterTuple(prefix, ReferenceType.PICKLISTENTRY.name(), pickListGuid));
		
		// remove all pick list entry node properties
		this.vsPropertyDao.deleteAllPickListEntityPropertiesOfPickListDefinition(pickListGuid);
		
		// remove pick list entries
		this.getSqlMapClientTemplate().delete(
				REMOVE_PICKLIST_ENTRY_BY_PICKLISTGUID_SQL, 
				new PrefixedParameter(prefix, pickListGuid));
		
		// remove pick list definition properties
		this.vsPropertyDao.deleteAllPickListDefinitionProperties(pickListGuid);
		
		// remove pick list definition source list
		this.getSqlMapClientTemplate().delete(
				DELETE_SOURCE_BY_PARENT_GUID_AND_TYPE_SQL, 
				new PrefixedParameterTuple(prefix, pickListGuid, ReferenceType.PICKLISTDEFINITION.name()));
		
		// remove pick list definition default context
		this.getSqlMapClientTemplate().delete(
				DELETE_CONTEXT_BY_PARENT_GUID_AND_TYPE_SQL, 
				new PrefixedParameterTuple(prefix, pickListGuid, ReferenceType.PICKLISTDEFINITION.name()));
		
		// remove pick list definition mappings
		deletePickListDefinitionMappings(pickListGuid);
		
		// remove pick list definition
		this.getSqlMapClientTemplate().delete(
				REMOVE_PICKLIST_DEFINITION_BY_PICKLISTID_SQL, 
				new PrefixedParameter(prefix, pickListDefinitionId));	
	}

	@Override
	@CacheMethod
	public String getPickListEntryNodeGuidByPickListIdAndPLEntryId(
			String pickListDefinitionId, String plEntryId) {
		String pickListGuid = (String) this.getSqlMapClientTemplate().queryForObject(GET_PICKLIST_GUID_BY_PICKLISTID_SQL, new PrefixedParameter(this.getPrefixResolver().resolveDefaultPrefix(), pickListDefinitionId));
		
		return (String) 
		this.getSqlMapClientTemplate().queryForObject(GET_PICKLIST_ENTRYNODEGUID_BY_PICKLISTID_AND_PLENTRYID_SQL, 
			new PrefixedParameterTuple(this.getPrefixResolver().resolveDefaultPrefix(), pickListGuid, plEntryId));
	}

	@SuppressWarnings("unchecked")
	private Mappings getMappings(String referenceGuid) {
		Mappings mappings = new Mappings();
		
		List<URIMap> uriMaps = this.getSqlMapClientTemplate().queryForList(	
				GET_URIMAPS_BY_REFERENCE_GUID_SQL, 
				new PrefixedParameterTuple(this.getPrefixResolver().resolveDefaultPrefix(), referenceGuid, ReferenceType.PICKLISTDEFINITION.name()));
		
		for(URIMap uriMap : uriMaps) {
			DaoUtility.insertIntoMappings(mappings, uriMap);
		}
		
		return mappings;
	}
	
	@SuppressWarnings("unchecked")
	public void insertMappings(String referenceGuid, Mappings mappings){
		if(mappings == null){
			return;
		}
		for(Field field : mappings.getClass().getDeclaredFields()){
			if(field.getName().startsWith(SUPPORTED_ATTRIB_GETTER_PREFIX)){
				field.setAccessible(true);
				try {
					List<URIMap> urimapList = (List<URIMap>) field.get(mappings);
					this.insertURIMap(referenceGuid, urimapList);
				} catch (Exception e) {
					if (e.getMessage().indexOf("Duplicate") == -1 && e.getMessage().indexOf("unique constraint") == -1 && e.getMessage().indexOf("SQLSTATE: 23505") == -1)
						throw new RuntimeException(e);
				} 
			}
		}
	}
	
	public void insertURIMap(final String referenceGuid,
			final List<URIMap> urimapList) {
		final String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){
	
			public Object doInSqlMapClient(SqlMapExecutor executor)
			throws SQLException {
				executor.startBatch();
				for(URIMap uriMap : urimapList){
					String uriMapId = createUniqueId();
					executor.insert(INSERT_URIMAPS_SQL, 
							buildInsertOrUpdateURIMapBean(
									prefix,
									uriMapId, 
									referenceGuid,
									classToStringMappingClassifier.classify(uriMap.getClass()),
									uriMap));
				}
				return executor.executeBatch();
			}	
		});		
	}
	
	public void insertURIMap(String referenceGuid, URIMap uriMap) {
		String uriMapId = this.createUniqueId();
		this.getSqlMapClientTemplate().insert(
				INSERT_URIMAPS_SQL, buildInsertOrUpdateURIMapBean(
									this.getPrefixResolver().resolveDefaultPrefix(),
									uriMapId, 
									referenceGuid,
									classToStringMappingClassifier.classify(uriMap.getClass()),
									uriMap));
	}
	
	/**
	 * Builds the insert uri map bean.
	 * 
	 * @param prefix the prefix
	 * @param uriMapId the uri map id
	 * @param codingSchemeId the coding scheme id
	 * @param supportedAttributeTag the supported attribute tag
	 * @param uriMap the uri map
	 * 
	 * @return the insert uri map bean
	 */
	protected InsertOrUpdateURIMapBean buildInsertOrUpdateURIMapBean(String prefix, String uriMapId, String referenceGuid, String supportedAttributeTag, URIMap uriMap){
		InsertOrUpdateURIMapBean bean = new InsertOrUpdateURIMapBean();
		bean.setPrefix(prefix);
		bean.setSupportedAttributeTag(supportedAttributeTag);
		bean.setCodingSchemeUId(referenceGuid);
		bean.setReferenceType(ReferenceType.PICKLISTDEFINITION.name());
		bean.setUriMap(uriMap);
		bean.setUId(uriMapId);
		
		if (uriMap instanceof SupportedHierarchy)
		{
			String associations = null;
			List<String> associationList = ((SupportedHierarchy) uriMap).getAssociationNamesAsReference();
			if (associationList != null) {
				for (int i = 0; i < associationList.size(); i++) {
					String assoc = (String) associationList.get(i);
					associations = i == 0 ? assoc : (associations += ("," + assoc));
				}
				bean.setAssociationNames(associations);
			}
		}
		
		
		return bean;
	}
	
	@ClearCache
	public void deletePickListDefinitionMappings(String referenceGuid) {
		this.getSqlMapClientTemplate().delete(
				DELETE_URIMAPS_BY_REFERENCE_GUID_SQL, 
				new PrefixedParameterTuple(this.getPrefixResolver().resolveDefaultPrefix(), referenceGuid, ReferenceType.PICKLISTDEFINITION.name()));
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

	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public List<String> getPickListDefinitionIdForEntityReference(
			String entityCode, String entityCodeNameSpace, String propertyId) {
		List<String> pickListIds = null;
		if (propertyId != null)			
			pickListIds =  (List<String>) this.getSqlMapClientTemplate().queryForList(GET_PICKLIST_DEFINITION_ID_FOR_ENTITYCODE_ENTITYCODENAMESPACE_PROPERTYID_SQL,
					new PrefixedParameterTriple(this.getPrefixResolver().resolveDefaultPrefix(), entityCode, entityCodeNameSpace, propertyId));
		else
			pickListIds =  this.getSqlMapClientTemplate().queryForList(GET_PICKLIST_DEFINITION_ID_FOR_ENTITYCODE_ENTITYCODENAMESPACE_SQL,
					new PrefixedParameterTuple(this.getPrefixResolver().resolveDefaultPrefix(), entityCode, entityCodeNameSpace));
		
		return pickListIds;
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

	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public List<String> getPickListDefinitionIdForSupportedTagAndValue(
			String supportedTag, String value) {
		return (List<String>) this.getSqlMapClientTemplate().queryForList(GET_PICKLISTID_FOR_SUPPORTED_TAG_AND_VALUE_SQL,
				new PrefixedParameterTuple(this.getPrefixResolver().resolveDefaultPrefix(), supportedTag, value));
	}

	@Override
	public String insertHistoryPickListDefinition(String pickListDefUId, String pickListId) {
		
		String prefix = getPrefix();
		
		InsertPickListDefinitionBean plDefBean = (InsertPickListDefinitionBean) this
				.getSqlMapClientTemplate().queryForObject(
						GET_PICKLIST_DEFINITION_METADATA_BY_PICKLISTID_SQL,
						new PrefixedParameter(prefix, pickListDefUId));
	
		String histPrefix = this.getPrefixResolver().resolveHistoryPrefix();
		
		plDefBean.setPrefix(histPrefix);
		
		this.getSqlMapClientTemplate().insert(
				INSERT_PICKLIST_DEFINITION_SQL, plDefBean);
		
		for (InsertOrUpdateValueSetsMultiAttribBean vsMultiAttrib : plDefBean
				.getVsMultiAttribList()) {
			vsMultiAttrib.setPrefix(histPrefix);

			this.getSqlMapClientTemplate().insert(INSERT_MULTI_ATTRIB_SQL,
					vsMultiAttrib);
		}
		
		if (!vsEntryStateExists(prefix, plDefBean.getEntryStateUId())) {

			EntryState entryState = new EntryState();

			entryState.setChangeType(ChangeType.NEW);
			entryState.setRelativeOrder(0L);

			vsEntryStateDao
					.insertEntryState(plDefBean.getEntryStateUId(),
							plDefBean.getUId(), ReferenceType.PICKLISTDEFINITION.name(), null,
							entryState);
		}
		
		return plDefBean.getEntryStateUId();
	}

	@Override
	public String updatePickListDefinition(String pickListDefUId,
			PickListDefinition definition) {

		String entryStateUId = this.createUniqueId();
		String prefix = getPrefix();
		
		InsertPickListDefinitionBean bean = new InsertPickListDefinitionBean();
		bean.setPrefix(prefix);
		bean.setPickListDefinition(definition);
		bean.setUId(pickListDefUId);
		bean.setEntryStateUId(entryStateUId);
		
		this.getSqlMapClientTemplate().update(UPDATE_PICKLIST_DEFINITION_BY_UID_SQL, bean);
		
		if( definition.getSourceCount() != 0 ) {
			
			this.getSqlMapClientTemplate().delete(
					DELETE_SOURCE_BY_PARENT_GUID_AND_TYPE_SQL,
					new PrefixedParameterTuple(prefix, pickListDefUId, ReferenceType.PICKLISTDEFINITION.name()));
			
			Source[] sourceList = definition.getSource();
			
			for (int i = 0; i < sourceList.length; i++) {
				InsertOrUpdateValueSetsMultiAttribBean insertOrUpdateValueSetsMultiAttribBean = new InsertOrUpdateValueSetsMultiAttribBean();
				insertOrUpdateValueSetsMultiAttribBean.setUId(this.createUniqueId());
				insertOrUpdateValueSetsMultiAttribBean.setReferenceUId(pickListDefUId);
				insertOrUpdateValueSetsMultiAttribBean.setReferenceType(ReferenceType.PICKLISTDEFINITION.name());
				insertOrUpdateValueSetsMultiAttribBean.setAttributeType(SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE);
				insertOrUpdateValueSetsMultiAttribBean.setAttributeValue(sourceList[i].getContent());
				insertOrUpdateValueSetsMultiAttribBean.setRole(sourceList[i].getRole());
				insertOrUpdateValueSetsMultiAttribBean.setSubRef(sourceList[i].getSubRef());
				insertOrUpdateValueSetsMultiAttribBean.setEntryStateUId(entryStateUId);
				insertOrUpdateValueSetsMultiAttribBean.setPrefix(prefix);
				
				this.getSqlMapClientTemplate().insert(INSERT_MULTI_ATTRIB_SQL, insertOrUpdateValueSetsMultiAttribBean);
			}
		} else {
			
			this.getSqlMapClientTemplate().update(
					UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
					new PrefixedParameterTriple(prefix, pickListDefUId,
							SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE,
							entryStateUId));
		}
		
		if( definition.getDefaultPickContextCount() != 0 ) {
			
			this.getSqlMapClientTemplate().delete(
					DELETE_CONTEXT_BY_PARENT_GUID_AND_TYPE_SQL,
					new PrefixedParameterTuple(prefix, pickListDefUId,
							ReferenceType.PICKLISTDEFINITION.name()));
			
			String[] contextList = definition.getDefaultPickContext();
			
			for (int i = 0; i < contextList.length; i++) {
				InsertOrUpdateValueSetsMultiAttribBean insertOrUpdateValueSetsMultiAttribBean = new InsertOrUpdateValueSetsMultiAttribBean();
				insertOrUpdateValueSetsMultiAttribBean.setUId(this.createUniqueId());
				insertOrUpdateValueSetsMultiAttribBean.setReferenceUId(pickListDefUId);
				insertOrUpdateValueSetsMultiAttribBean.setReferenceType(ReferenceType.PICKLISTDEFINITION.name());
				insertOrUpdateValueSetsMultiAttribBean.setAttributeType(SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT);
				insertOrUpdateValueSetsMultiAttribBean.setAttributeValue(contextList[i]);
				insertOrUpdateValueSetsMultiAttribBean.setRole(null);
				insertOrUpdateValueSetsMultiAttribBean.setSubRef(null);
				insertOrUpdateValueSetsMultiAttribBean.setEntryStateUId(entryStateUId);
				insertOrUpdateValueSetsMultiAttribBean.setPrefix(prefix);
				
				this.getSqlMapClientTemplate().insert(INSERT_MULTI_ATTRIB_SQL, insertOrUpdateValueSetsMultiAttribBean);
			}
		} else {
			
			this.getSqlMapClientTemplate().update(
					UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
					new PrefixedParameterTriple(prefix, pickListDefUId,
							SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT,
							entryStateUId));
		}
		
		return entryStateUId;
	}

	@Override
	public String updateVersionableAttributes(String pickListDefUId, PickListDefinition definition) {

		String entryStateUId = this.createUniqueId();
		String prefix = getPrefix();
		
		InsertPickListDefinitionBean bean = new InsertPickListDefinitionBean();
		bean.setPrefix(prefix);
		bean.setPickListDefinition(definition);
		bean.setUId(pickListDefUId);
		bean.setEntryStateUId(entryStateUId);
		
		this.getSqlMapClientTemplate().update(UPDATE_PL_VERSIONABLE_ATTRIBUTE_BY_UID_SQL, bean);
		
		this.getSqlMapClientTemplate().update(
				UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
				new PrefixedParameterTriple(prefix, pickListDefUId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE,
						entryStateUId));
		
		this.getSqlMapClientTemplate().update(
				UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
				new PrefixedParameterTriple(prefix, pickListDefUId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT,
						entryStateUId));
		
		return entryStateUId;
	}

	@Override
	public String getPickListEntryStateUId(String pickListDefUId) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		return (String) this.getSqlMapClientTemplate().queryForObject(GET_ENTRYSTATE_UID_BY_PICKLIST_UID_SQL,
				new PrefixedParameter(prefix, pickListDefUId));
	}

	@Override
	public void updateEntryStateUId(String pickListDefUId, String entryStateUId) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		this.getSqlMapClientTemplate().update(
				UPDATE_PICKLIST_ENTRYSTATE_UID_SQL, 
				new PrefixedParameterTuple(prefix, pickListDefUId, entryStateUId));
		
		this.getSqlMapClientTemplate().update(
				UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
				new PrefixedParameterTriple(prefix, pickListDefUId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE,
						entryStateUId));
		
		this.getSqlMapClientTemplate().update(
				UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
				new PrefixedParameterTriple(prefix, pickListDefUId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT,
						entryStateUId));
	}

	/**
	 * @return the pickListEntryNodeDao
	 */
	public PickListEntryNodeDao getPickListEntryNodeDao() {
		return pickListEntryNodeDao;
	}

	/**
	 * @param pickListEntryNodeDao the pickListEntryNodeDao to set
	 */
	public void setPickListEntryNodeDao(PickListEntryNodeDao pickListEntryNodeDao) {
		this.pickListEntryNodeDao = pickListEntryNodeDao;
	}

	@Override
	public String getLatestRevision(String pickListDefUId) {
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		return (String) this.getSqlMapClientTemplate().queryForObject(
				GET_PICKLIST_DEFINITION_LATEST_REVISION_ID_BY_UID, 
				new PrefixedParameter(prefix, pickListDefUId));	
	}

	@Override
	public boolean entryStateExists(String entryStateUId) {

		String prefix = this.getPrefix();
		
		return	super.vsEntryStateExists(prefix, entryStateUId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PickListDefinition resolvePickListByRevision(String pickListId,
			String revisionId, Integer sortType) throws LBRevisionException {
		
		String prefix = this.getPrefix();
		String tempRevId = revisionId;
		
		String pickListDefinitionUId = this
				.getPickListGuidFromPickListId(pickListId);

		if (pickListDefinitionUId == null) {
			throw new LBRevisionException(
					"PickListDefinition "
							+ pickListId
							+ " doesn't exist in lexEVS. "
							+ "Please check the pickListId. Its possible that the given pickListDefinition "
							+ "has been REMOVEd from the lexEVS system in the past.");
		}

		String plDefRevisionId = this
				.getLatestRevision(pickListDefinitionUId);

		// 1. If 'revisionId' is null or 'revisionId' is the latest revision of the picklistDefinition
		// then use getPickListDefinitionById to get the pickListDefinition object and return.
		
		if (StringUtils.isEmpty(revisionId)
				|| StringUtils.isEmpty(plDefRevisionId)
				|| revisionId.equals(plDefRevisionId)) {
			return this.getPickListDefinitionById(pickListId);
		}
		
		// 2. Get the earliest revisionId on which change was applied on given 
		// pickList with reference to given revisionId.
		
		HashMap revisionIdMap = (HashMap) this.getSqlMapClientTemplate()
				.queryForMap(
						GET_PREV_REV_ID_FROM_GIVEN_REV_ID_FOR_PLDEF_SQL,
						new PrefixedParameterTuple(prefix, pickListId,
								revisionId), "revId", "revAppliedDate");
		
		if( revisionIdMap.isEmpty() ) {
			revisionId = null;
		} else {
			
			revisionId = (String) revisionIdMap.keySet().toArray()[0];
			
			if( plDefRevisionId.equals(revisionId)) {
				return this.getPickListDefinitionById(pickListId);
			}
		}
			
		// 3. Get the pick list definition data from history.
		PickListDefinition pickListDefinition = null;
		InsertPickListDefinitionBean plDefBean = null;
			
		plDefBean = (InsertPickListDefinitionBean) this
				.getSqlMapClientTemplate().queryForObject(
						GET_PICKLIST_DEFINITION_METADATA_FROM_HISTORY_BY_REVISION_SQL,
						new PrefixedParameterTuple(getPrefix(), pickListId,
								revisionId));
		
		if (plDefBean != null) {
			
			pickListDefinition = plDefBean.getPickListDefinition();
			
			// Get pick list definition source
			List<Source> sourceList = this
					.getSqlMapClientTemplate()
					.queryForList(
							GET_SOURCE_LIST_FROM_HISTORY_BY_PARENT_ENTRYSTATEGUID_AND_TYPE_SQL,
							new PrefixedParameterTuple(prefix, plDefBean
									.getEntryStateUId(),
									ReferenceType.PICKLISTDEFINITION.name()));

			if (sourceList != null)
				pickListDefinition.setSource(sourceList);

			// Get pick list definition context
			List<String> contextList = this
					.getSqlMapClientTemplate()
					.queryForList(
							GET_CONTEXT_LIST_FROM_HISTORY_BY_PARENT_ENTRYSTATEGUID_AND_TYPE_SQL,
							new PrefixedParameterTuple(prefix, plDefBean
									.getEntryStateUId(),
									ReferenceType.PICKLISTDEFINITION.name()));

			if (contextList != null)
				pickListDefinition.setDefaultPickContext(contextList);
		}
		
		// 4. If pick list is not in history, get it from base table.
		if (pickListDefinition == null) {
			pickListDefinition = (PickListDefinition) this
					.getSqlMapClientTemplate().queryForObject(
							GET_PICKLIST_DEFINITION_BY_PICKLISTID_SQL,
							new PrefixedParameterTuple(prefix, pickListId,
									revisionId));

			// Get pick list definition source
			List<Source> sourceList = this.getSqlMapClientTemplate()
					.queryForList(
							GET_SOURCE_LIST_BY_PARENT_GUID_AND_TYPE_SQL,
							new PrefixedParameterTuple(prefix,
									pickListDefinitionUId,
									ReferenceType.PICKLISTDEFINITION.name()));

			if (sourceList != null)
				pickListDefinition.setSource(sourceList);

			// Get pick list definition context
			List<String> contextList = this.getSqlMapClientTemplate()
					.queryForList(
							GET_CONTEXT_LIST_BY_PARENT_GUID_AND_TYPE_SQL,
							new PrefixedParameterTuple(prefix,
									pickListDefinitionUId,
									ReferenceType.PICKLISTDEFINITION.name()));

			if (contextList != null)
				pickListDefinition.setDefaultPickContext(contextList);
		}
		
		// 5. Get all pick list entry nodes.
		List<String> entryNodeList = this.getSqlMapClientTemplate().queryForList(
				GET_PL_ENTRY_NODES_LIST_BY_PICKLIST_ID_SQL,
				new PrefixedParameter(prefix, pickListId));
			
		for (String plEntryId : entryNodeList) {
			PickListEntryNode pickListEntryNode = null;

			try {
				pickListEntryNode = pickListEntryNodeDao
						.resolvePLEntryNodeByRevision(pickListId, plEntryId,
								tempRevId);
			} catch (LBRevisionException e) {
				continue;
			}
			
			if( pickListEntryNode != null)
				pickListDefinition.addPickListEntryNode(pickListEntryNode);
		}
		
		// 6. Get all pick list definition properties.
		
		List<String> propertyList = this.getSqlMapClientTemplate().queryForList(
				GET_PICKLIST_DEF_PROPERTY_LIST_BY_PICKLIST_ID_SQL,
				new PrefixedParameter(prefix, pickListId));
		
		Properties properties = new Properties();
		
		for (String propId : propertyList) {
			Property prop = null;
			
			try {
				prop = vsPropertyDao.resolveVSPropertyByRevision(
						pickListDefinitionUId, propId, tempRevId);
			} catch (LBRevisionException e) {
				continue;
			}
			properties.addProperty(prop);
		}
		
		pickListDefinition.setProperties(properties);
		
		return pickListDefinition;
	}
}
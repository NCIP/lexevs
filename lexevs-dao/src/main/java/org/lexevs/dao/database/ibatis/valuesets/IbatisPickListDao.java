/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexevs.dao.database.ibatis.valuesets;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedHierarchy;
import org.LexGrid.naming.URIMap;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListEntry;
import org.LexGrid.valueSets.PickListEntryExclusion;
import org.LexGrid.valueSets.PickListEntryNode;
import org.LexGrid.valueSets.PickListEntryNodeChoice;
import org.LexGrid.versions.EntryState;
import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.dao.database.access.valuesets.PickListDao;
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
import org.lexevs.dao.database.ibatis.valuesets.parameter.InsertOrUpdatePickListEntryBean;
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
	
	public static String VS_MULTIATTRIB_NAMESPACE = "VSMultiAttrib.";
	
	public static String VS_MAPPING_NAMESPACE = "VSMapping.";
	
	private static String SUPPORTED_ATTRIB_GETTER_PREFIX = "_supported";
	
	/** The INSER t_ picklis t_ definitio n_ sql. */
	public static String INSERT_PICKLIST_DEFINITION_SQL = PICKLIST_NAMESPACE + "insertPickListDefinition";
	
	public static String INSERT_PICKLIST_ENTRY_SQL = PICKLIST_NAMESPACE + "insertPickListEntry";
	
	/** The GE t_ picklis t_ id s_ sql. */
	public static String GET_PICKLIST_IDS_SQL = PICKLIST_NAMESPACE + "getPickListIds";
	
	/** The GE t_ picklis t_ gui d_ b y_ picklisti d_ sql. */
	public static String GET_PICKLIST_GUID_BY_PICKLISTID_SQL = PICKLIST_NAMESPACE + "getPickListGuidByPickListId";
	
	public static String GET_PICKLIST_ENTRYNODEGUID_BY_PICKLISTID_AND_PLENTRYID_SQL = PICKLIST_NAMESPACE + "getPickListEntryNodeIdByPickListGuidAndPLEntryId";
	
	/** The GE t_ picklis t_ definitio n_ b y_ picklisti d_ sql. */
	public static String GET_PICKLIST_DEFINITION_BY_PICKLISTID_SQL = PICKLIST_NAMESPACE + "getPickListDefinitionByPickListId";
	
	public static String GET_PICKLIST_DEFINITION_ID_FOR_VALUESET_DEFINITION_URI_SQL = PICKLIST_NAMESPACE + "getPickListDefinitionIdForValueSetDefinitionUri";
	
	public static String GET_PICKLIST_ENTRYNODE_BEAN_BY_PICKLIST_GUID_SQL = PICKLIST_NAMESPACE + "getPickListEntryNodeBeanByPickListGuid";
	
	public static String GET_PICKLIST_DEFINITION_ID_FOR_ENTITYCODE_ENTITYCODENAMESPACE_SQL = PICKLIST_NAMESPACE + "getPickListDefinitionIdForEntityCodeAndEntityNamespace";
	
	public static String GET_PICKLIST_DEFINITION_ID_FOR_ENTITYCODE_ENTITYCODENAMESPACE_PROPERTYID_SQL = PICKLIST_NAMESPACE + "getPickListDefinitionIdForEntityCodeEntityNamespaceAndPropertyId";
	
	public static String REMOVE_PICKLIST_DEFINITION_BY_PICKLISTID_SQL = PICKLIST_NAMESPACE + "removePickListDefinitionByPickListId";
	
	public static String REMOVE_PICKLIST_ENTRY_BY_PICKLISTGUID_SQL = PICKLIST_NAMESPACE + "removePickListEntryByPickListGuid";
	
	public static String GET_SOURCE_LIST_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "getSourceListByParentGuidandType";
	
	public static String GET_CONTEXT_LIST_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "getContextListByParentGuidandType";
	
	public static String INSERT_MULTI_ATTRIB_SQL = VS_MULTIATTRIB_NAMESPACE + "insertMultiAttrib";
	
	public static String DELETE_SOURCE_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "deleteSourceByParentGuidAndType";
	
	public static String DELETE_CONTEXT_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "deleteContextByParentGuidAndType";
	
	public static String DELETE_PICKLIST_ENTRY_CONTEXT_BY_PICKLIST_GUID_SQL = VS_MULTIATTRIB_NAMESPACE + "deletePickListEntryContextByPickListGuid";
	
	public static String GET_URIMAPS_BY_REFERENCE_GUID_SQL = VS_MAPPING_NAMESPACE + "getURIMaps";
	
	public static String GET_URIMAPS_BY_REFERENCE_GUID_LOCALNAME_AND_TYPE_SQL = VS_MAPPING_NAMESPACE + "getURIMapByLocalNameAndType";
	
	public static String GET_PICKLISTID_FOR_SUPPORTED_TAG_AND_VALUE_SQL = VS_MAPPING_NAMESPACE + "getPickListIdForSupportedTagAndValue";
	
	public static String INSERT_URIMAPS_SQL = VS_MAPPING_NAMESPACE + "insertURIMap";
	
	public static String UPDATE_URIMAPS_BY_LOCALID_SQL = VS_MAPPING_NAMESPACE + "updateUriMapByLocalId";
	
	public static String DELETE_URIMAPS_BY_REFERENCE_GUID_SQL = VS_MAPPING_NAMESPACE + "deleteMappingsByReferenceGuid";
	
	/** The class to string mapping classifier. */
	private ClassToStringMappingClassifier classToStringMappingClassifier = new ClassToStringMappingClassifier();
	
	/** The versions dao. */
	private VersionsDao versionsDao;
	
	private VSPropertyDao vsPropertyDao;
	
	private VSEntryStateDao vsEntryStateDao;

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.picklist.PickListDao#getPickListDefinitionById(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public PickListDefinition getPickListDefinitionById(String pickListId) {
		PickListDefinition plDef = (PickListDefinition) 
			this.getSqlMapClientTemplate().queryForObject(GET_PICKLIST_DEFINITION_BY_PICKLISTID_SQL, 
				new PrefixedParameter(null, pickListId));
		
		if (plDef != null)
		{
			String plDefGuid = getPickListGuidFromPickListId(pickListId);
			
			List<PickListEntryNodeBean> plEntryNodeBeans = (List<PickListEntryNodeBean>) this.getSqlMapClientTemplate().queryForList(GET_PICKLIST_ENTRYNODE_BEAN_BY_PICKLIST_GUID_SQL, 
				new PrefixedParameter(null, plDefGuid));
			
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
								new PrefixedParameterTuple(null, plEntryNodeBean.getVsPLEntryGuid(), ReferenceType.PICKLISTENTRY.name())); 
						
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
					new PrefixedParameterTuple(null, plDefGuid, ReferenceType.PICKLISTDEFINITION.name())); 
			
			if (sourceList != null)
				plDef.setSource(sourceList);
			
			// Get pick list definition context
			List<String> contextList = this.getSqlMapClientTemplate().queryForList(GET_CONTEXT_LIST_BY_PARENT_GUID_AND_TYPE_SQL, 
					new PrefixedParameterTuple(null, plDefGuid, ReferenceType.PICKLISTDEFINITION.name())); 
			
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
	@CacheMethod
	public String getPickListGuidFromPickListId(String pickListId) {
		return (String) 
		this.getSqlMapClientTemplate().queryForObject(GET_PICKLIST_GUID_BY_PICKLISTID_SQL, 
			new PrefixedParameter(null, pickListId));
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@CacheMethod
	public List<String> getPickListDefinitionIdForValueSetDefinitionURI(
			String valueSetDefURI) {
		return (List<String>) this.getSqlMapClientTemplate().queryForList(GET_PICKLIST_DEFINITION_ID_FOR_VALUESET_DEFINITION_URI_SQL, 
				new PrefixedParameter(null, valueSetDefURI));
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
				plEntryBean.setEntityCodeNamespace(plEntry.getEntityCodeNamespace());
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
				plEntryBean.setEntityCodeNamespace(plExclusion.getEntityCodeNamespace());
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
			insertPickListEntry(pickListGuid, plEntryNode);
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
			insertOrUpdateValueSetsMultiAttribBean.setEntryStateUId(this.createUniqueId());
			
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
			insertOrUpdateValueSetsMultiAttribBean.setEntryStateUId(this.createUniqueId());
			
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
		return this.getSqlMapClientTemplate().queryForList(GET_PICKLIST_IDS_SQL);
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
		
		String pickListGuid = (String) this.getSqlMapClientTemplate().queryForObject(GET_PICKLIST_GUID_BY_PICKLISTID_SQL, new PrefixedParameter(null, pickListDefinitionId));
		
		// remove all pick list entry context
		this.getSqlMapClientTemplate().delete(DELETE_PICKLIST_ENTRY_CONTEXT_BY_PICKLIST_GUID_SQL, new PrefixedParameterTuple(null, ReferenceType.PICKLISTENTRY.name(), pickListGuid));
		
		// remove all pick list entry node properties
		this.vsPropertyDao.deleteAllPickListEntityPropertiesOfPickListDefinition(pickListGuid);
		
		// remove pick list entries
		this.getSqlMapClientTemplate().delete(REMOVE_PICKLIST_ENTRY_BY_PICKLISTGUID_SQL, new PrefixedParameter(null, pickListGuid));
		
		// remove pick list definition properties
		this.vsPropertyDao.deleteAllPickListDefinitionProperties(pickListGuid);
		
		// remove pick list definition source list
		this.getSqlMapClientTemplate().delete(DELETE_SOURCE_BY_PARENT_GUID_AND_TYPE_SQL, new PrefixedParameterTuple(null, pickListGuid, ReferenceType.PICKLISTDEFINITION.name()));
		
		// remove pick list definition default context
		this.getSqlMapClientTemplate().delete(DELETE_CONTEXT_BY_PARENT_GUID_AND_TYPE_SQL, new PrefixedParameterTuple(null, pickListGuid, ReferenceType.PICKLISTDEFINITION.name()));
		
		// remove pick list definition mappings
		deletePickListDefinitionMappings(pickListGuid);
		
		// remove pick list definition
		this.getSqlMapClientTemplate().
			delete(REMOVE_PICKLIST_DEFINITION_BY_PICKLISTID_SQL, new PrefixedParameter(null, pickListDefinitionId));	
	}

	@Override
	@CacheMethod
	public String getPickListEntryNodeGuidByPickListIdAndPLEntryId(
			String pickListDefinitionId, String plEntryId) {
		String pickListGuid = (String) this.getSqlMapClientTemplate().queryForObject(GET_PICKLIST_GUID_BY_PICKLISTID_SQL, new PrefixedParameter(null, pickListDefinitionId));
		
		return (String) 
		this.getSqlMapClientTemplate().queryForObject(GET_PICKLIST_ENTRYNODEGUID_BY_PICKLISTID_AND_PLENTRYID_SQL, 
			new PrefixedParameterTuple(null, pickListGuid, plEntryId));
	}

	@SuppressWarnings("unchecked")
	private Mappings getMappings(String referenceGuid) {
		Mappings mappings = new Mappings();
		
		List<URIMap> uriMaps = this.getSqlMapClientTemplate().queryForList(	
				GET_URIMAPS_BY_REFERENCE_GUID_SQL, 
				new PrefixedParameterTuple(null, referenceGuid, ReferenceType.PICKLISTDEFINITION.name()));
		
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
					if (e.getMessage().indexOf("Duplicate") == -1)
						throw new RuntimeException(e);
				} 
			}
		}
	}
	
	public void insertURIMap(final String referenceGuid,
			final List<URIMap> urimapList) {
		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){
	
			public Object doInSqlMapClient(SqlMapExecutor executor)
			throws SQLException {
				executor.startBatch();
				for(URIMap uriMap : urimapList){
					String uriMapId = UUID.randomUUID().toString();
					
					executor.insert(INSERT_URIMAPS_SQL, 
							buildInsertOrUpdateURIMapBean(
									null,
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
									null,
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
				new PrefixedParameterTuple(null, referenceGuid, ReferenceType.PICKLISTDEFINITION.name()));
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
					new PrefixedParameterTriple(null, entityCode, entityCodeNameSpace, propertyId));
		else
			pickListIds =  this.getSqlMapClientTemplate().queryForList(GET_PICKLIST_DEFINITION_ID_FOR_ENTITYCODE_ENTITYCODENAMESPACE_SQL,
					new PrefixedParameterTuple(null, entityCode, entityCodeNameSpace));
		
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
				new PrefixedParameterTuple(null, supportedTag, value));
	}

	
}

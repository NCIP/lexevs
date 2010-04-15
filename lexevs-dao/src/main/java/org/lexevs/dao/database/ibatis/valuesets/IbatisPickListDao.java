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

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListEntry;
import org.LexGrid.valueSets.PickListEntryExclusion;
import org.LexGrid.valueSets.PickListEntryNode;
import org.LexGrid.valueSets.PickListEntryNodeChoice;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.dao.database.access.valuesets.PickListDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao.ReferenceType;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.valuesets.parameter.InsertOrUpdatePickListEntryBean;
import org.lexevs.dao.database.ibatis.valuesets.parameter.InsertOrUpdateValueSetsMultiAttribBean;
import org.lexevs.dao.database.ibatis.valuesets.parameter.InsertPickListDefinitionBean;
import org.lexevs.dao.database.ibatis.valuesets.parameter.PickListEntryNodeBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

/**
 * The Class IbatisPickListDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IbatisPickListDao extends AbstractIbatisDao implements PickListDao {
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	/** The PICKLIS t_ namespace. */
	public static String PICKLIST_NAMESPACE = "PickList.";
	
	public static String VS_MULTIATTRIB_NAMESPACE = "VSMultiAttrib.";
	
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
	
	public static String REMOVE_PICKLIST_DEFINITION_BY_PICKLISTID_SQL = PICKLIST_NAMESPACE + "removePickListDefinitionByPickListId";
	
	public static String REMOVE_PICKLIST_ENTRY_BY_PICKLISTGUID_SQL = PICKLIST_NAMESPACE + "removePickListEntryByPickListGuid";
	
	public static String GET_SOURCE_LIST_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "getSourceListByParentGuidandType";
	
	public static String GET_CONTEXT_LIST_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "getContextListByParentGuidandType";
	
	public static String INSERT_MULTI_ATTRIB_SQL = VS_MULTIATTRIB_NAMESPACE + "insertMultiAttrib";
	
	public static String DELETE_SOURCE_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "deleteSourceByParentGuidAndType";
	
	public static String DELETE_CONTEXT_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "deleteContextByParentGuidAndType";
	
	public static String DELETE_PICKLIST_ENTRY_CONTEXT_BY_PICKLIST_GUID_SQL = VS_MULTIATTRIB_NAMESPACE + "deletePickListEntryContextByPickListGuid";
	
	/** The versions dao. */
	private VersionsDao versionsDao;
	
	private VSPropertyDao vsPropertyDao;
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.picklist.PickListDao#getPickListDefinitionById(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
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
			
			
		}
		return plDef;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.picklist.PickListDao#getGuidFromPickListId(java.lang.String)
	 */
	@Override
	public String getPickListGuidFromPickListId(String pickListId) {
		return (String) 
		this.getSqlMapClientTemplate().queryForObject(GET_PICKLIST_GUID_BY_PICKLISTID_SQL, 
			new PrefixedParameter(null, pickListId));
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<String> getPickListDefinitionIdForValueSetDefinitionURI(
			String valueSetDefURI) {
		return (List<String>) this.getSqlMapClientTemplate().queryForList(GET_PICKLIST_DEFINITION_ID_FOR_VALUESET_DEFINITION_URI_SQL, 
				new PrefixedParameter(null, valueSetDefURI));
	}
	
	@Override
	public String insertPickListEntry(String pickListGuid, PickListEntryNode entryNode) {
		String plEntryGuid = null;
		InsertOrUpdateValueSetsMultiAttribBean insertOrUpdateValueSetsMultiAttribBean = null;
		if (entryNode != null && entryNode.getPickListEntryNodeChoice() != null)
		{
			InsertOrUpdatePickListEntryBean plEntryBean = new InsertOrUpdatePickListEntryBean();		
			plEntryGuid = this.createUniqueId();
			plEntryBean.setUId(plEntryGuid);
			plEntryBean.setPickListEntryNode(entryNode);
			plEntryBean.setPickListUId(pickListGuid);
			
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

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.picklist.PickListDao#insertPickListDefinition(java.lang.String, org.LexGrid.valueDomains.PickListDefinition)
	 */
	@Override
	public String insertPickListDefinition(String systemReleaseUri,
			PickListDefinition definition) {
		String pickListGuid = this.createUniqueId();
		
		String systemReleaseId = this.versionsDao.getSystemReleaseIdByUri(systemReleaseUri);
		
		InsertPickListDefinitionBean plDefBean = new InsertPickListDefinitionBean();
		plDefBean.setUId(pickListGuid);
		plDefBean.setPickListDefinition(definition);
		plDefBean.setPrefix(getPrefix());
		plDefBean.setSystemReleaseUId(systemReleaseId);
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
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
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
		
		// remove pick list definition
		this.getSqlMapClientTemplate().
			delete(REMOVE_PICKLIST_DEFINITION_BY_PICKLISTID_SQL, new PrefixedParameter(null, pickListDefinitionId));	
	}

	@Override
	public String getPickListEntryNodeGuidByPickListIdAndPLEntryId(
			String pickListDefinitionId, String plEntryId) {
		String pickListGuid = (String) this.getSqlMapClientTemplate().queryForObject(GET_PICKLIST_GUID_BY_PICKLISTID_SQL, new PrefixedParameter(null, pickListDefinitionId));
		
		return (String) 
		this.getSqlMapClientTemplate().queryForObject(GET_PICKLIST_ENTRYNODEGUID_BY_PICKLISTID_AND_PLENTRYID_SQL, 
			new PrefixedParameterTuple(null, pickListGuid, plEntryId));
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

	
}

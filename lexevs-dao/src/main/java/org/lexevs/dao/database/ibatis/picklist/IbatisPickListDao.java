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
package org.lexevs.dao.database.ibatis.picklist;

import java.util.List;

import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListEntry;
import org.LexGrid.valueSets.PickListEntryExclusion;
import org.LexGrid.valueSets.PickListEntryNode;
import org.LexGrid.valueSets.PickListEntryNodeChoice;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.dao.database.access.picklist.PickListDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.picklist.parameter.InsertOrUpdatePickListEntryBean;
import org.lexevs.dao.database.ibatis.picklist.parameter.InsertPickListDefinitionBean;
import org.lexevs.dao.database.ibatis.picklist.parameter.PickListEntryNodeBean;
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
	
	/** The INSER t_ picklis t_ definitio n_ sql. */
	public static String INSERT_PICKLIST_DEFINITION_SQL = PICKLIST_NAMESPACE + "insertPickListDefinition";
	
	public static String INSERT_PICKLIST_ENTRY_SQL = PICKLIST_NAMESPACE + "insertPickListEntry";
	
	/** The GE t_ picklis t_ id s_ sql. */
	public static String GET_PICKLIST_IDS_SQL = PICKLIST_NAMESPACE + "getPickListIds";
	
	/** The GE t_ picklis t_ gui d_ b y_ picklisti d_ sql. */
	public static String GET_PICKLIST_GUID_BY_PICKLISTID_SQL = PICKLIST_NAMESPACE + "getPickListGuidByPickListId";
	
	/** The GE t_ picklis t_ definitio n_ b y_ picklisti d_ sql. */
	public static String GET_PICKLIST_DEFINITION_BY_PICKLISTID_SQL = PICKLIST_NAMESPACE + "getPickListDefinitionByPickListId";
	
	public static String GET_PICKLIST_DEFINITION_ID_FOR_VALUESET_DEFINITION_URI_SQL = PICKLIST_NAMESPACE + "getPickListDefinitionIdForValueSetDefinitionUri";
	
	public static String GET_PICKLIST_ENTRYNODE_BEAN_BY_PICKLIST_GUID_SQL = PICKLIST_NAMESPACE + "getPickListEntryNodeBeanByPickListGuid";
	
	public static String REMOVE_PICKLIST_DEFINITION_BY_PICKLISTID_SQL = PICKLIST_NAMESPACE + "removePickListDefinitionByPickListId";
	
	public static String REMOVE_PICKLIST_ENTRY_BY_PICKLISTGUID_SQL = PICKLIST_NAMESPACE + "removePickListEntryByPickListGuid";
	
	/** The versions dao. */
	private VersionsDao versionsDao;
	

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
			String plDefGuid = getGuidFromPickListId(pickListId);
			
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
						plEntryNodeChoice.setInclusionEntry(plEntryNodeBean.getPickListEntry());
					}
					else
					{
						plEntryNodeChoice.setExclusionEntry(plEntryNodeBean.getPickListEntryExclusion());
					}
					plEntryNode.setPickListEntryNodeChoice(plEntryNodeChoice);
					plDef.addPickListEntryNode(plEntryNode);
				}
			}
		}
		return plDef;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.picklist.PickListDao#getGuidFromPickListId(java.lang.String)
	 */
	@Override
	public String getGuidFromPickListId(String pickListId) {
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
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.picklist.PickListDao#insertPickListEntry(java.lang.String, org.LexGrid.valueDomains.PickListDefinition)
	 */
	@Override
	public String insertPickListEntry(String pickListGuid,
			PickListDefinition definition) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
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
		plDefBean.setId(pickListGuid);
		plDefBean.setPickListDefinition(definition);
		plDefBean.setPrefix(getPrefix());
		plDefBean.setSystemReleaseId(systemReleaseId);
		this.getSqlMapClientTemplate().insert(INSERT_PICKLIST_DEFINITION_SQL, plDefBean);
		
		InsertOrUpdatePickListEntryBean plEntryBean = null;
		for (PickListEntryNode plEntryNode : definition.getPickListEntryNode()) 
		{
			String plEntryGuid = this.createUniqueId();
			plEntryBean = new InsertOrUpdatePickListEntryBean();
			plEntryBean.setId(plEntryGuid);
			plEntryBean.setPickListEntryNode(plEntryNode);
			plEntryBean.setPickListGuid(pickListGuid);
			
			if (plEntryNode.getPickListEntryNodeChoice() != null)
			{
				PickListEntry plEntry = plEntryNode.getPickListEntryNodeChoice().getInclusionEntry();
				PickListEntryExclusion plExclusion = plEntryNode.getPickListEntryNodeChoice().getExclusionEntry();
				
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
				}
				else if (plExclusion != null)
				{
					plEntryBean.setInclude(false);
					plEntryBean.setEntityCode(plExclusion.getEntityCodeNamespace());
					plEntryBean.setEntityCode(plExclusion.getEntityCode());
				}
			}
			
			// insert into plEntry table
			this.getSqlMapClientTemplate().insert(INSERT_PICKLIST_ENTRY_SQL, plEntryBean);
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
		
		// remove pick list entries
		this.getSqlMapClientTemplate().delete(REMOVE_PICKLIST_ENTRY_BY_PICKLISTGUID_SQL, new PrefixedParameter(null, pickListGuid));
		
		// remove pick list definition
		this.getSqlMapClientTemplate().
			delete(REMOVE_PICKLIST_DEFINITION_BY_PICKLISTID_SQL, new PrefixedParameter(null, pickListDefinitionId));	
	}

	
}

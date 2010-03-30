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
import org.lexevs.dao.database.access.picklist.PickListDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.picklist.parameter.InsertPickListDefinitionBean;
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
	
	/** The GE t_ picklis t_ id s_ sql. */
	public static String GET_PICKLIST_IDS_SQL = PICKLIST_NAMESPACE + "getPickListIds";
	
	/** The GE t_ picklis t_ gui d_ b y_ picklisti d_ sql. */
	public static String GET_PICKLIST_GUID_BY_PICKLISTID_SQL = PICKLIST_NAMESPACE + "getPickListGuidByPickListId";
	
	/** The GE t_ picklis t_ definitio n_ b y_ picklisti d_ sql. */
	public static String GET_PICKLIST_DEFINITION_BY_PICKLISTID_SQL = PICKLIST_NAMESPACE + "getPickListDefinitionByPickListId";
	
	/** The versions dao. */
	private VersionsDao versionsDao;
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.picklist.PickListDao#getPickListDefinitionById(java.lang.String)
	 */
	@Override
	public PickListDefinition getPickListDefinitionById(String pickListId) {
		String prefix = getPrefix();
		
		return (PickListDefinition) 
			this.getSqlMapClientTemplate().queryForObject(GET_PICKLIST_DEFINITION_BY_PICKLISTID_SQL, 
				new PrefixedParameter(prefix, pickListId));
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.picklist.PickListDao#getGuidFromPickListId(java.lang.String)
	 */
	@Override
	public String getGuidFromPickListId(String pickListId) {
		String prefix = getPrefix();
		
		return (String) 
		this.getSqlMapClientTemplate().queryForObject(GET_PICKLIST_GUID_BY_PICKLISTID_SQL, 
			new PrefixedParameter(prefix, pickListId));
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
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.picklist.PickListDao#getPickListIds()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPickListIds() {
		return this.getSqlMapClientTemplate().queryForList(
				GET_PICKLIST_IDS_SQL, getPrefix());
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
}

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
package org.lexevs.dao.database.ibatis.versions;

import java.util.List;

import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.batch.IbatisInserter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.versions.parameter.InsertEntryStateBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

/**
 * The Class IbatisVersionsDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IbatisVersionsDao extends AbstractIbatisDao implements VersionsDao {
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	/** The VERSION s_ namespace. */
	public static String VERSIONS_NAMESPACE = "Versions.";
	
	/** The INSER t_ entr y_ stat e_ sql. */
	public static String INSERT_ENTRY_STATE_SQL = VERSIONS_NAMESPACE + "insertEntryState";
	
	/** The GE t_ entr y_ stat e_ b y_ i d_ sql. */
	public static String GET_ENTRY_STATE_BY_ID_SQL = VERSIONS_NAMESPACE + "insertEntryState";
	
	/** The GE t_ syste m_ releas e_ i d_ b y_ uri. */
	public static String GET_SYSTEM_RELEASE_ID_BY_URI = VERSIONS_NAMESPACE + "getSystemReleaseIdByUri";
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.versions.VersionsDao#getSystemReleaseIdByUri(java.lang.String)
	 */
	@Override
	public String getSystemReleaseIdByUri(String systemReleaseUri) {
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		return (String) this.getSqlMapClientTemplate().queryForObject(GET_SYSTEM_RELEASE_ID_BY_URI, 
			new PrefixedParameter(prefix, systemReleaseUri));
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.versions.VersionsDao#getEntryStateById(java.lang.String, java.lang.String, java.lang.String)
	 */
	public EntryState getEntryStateById(String codingSchemeName, String codingSchemeVersion, String entryStateId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeName, codingSchemeVersion);
		return (EntryState) this.getSqlMapClientTemplate().queryForObject(GET_ENTRY_STATE_BY_ID_SQL, 
			new PrefixedParameter(prefix, entryStateId));
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.versions.VersionsDao#updateEntryState(java.lang.String, org.LexGrid.versions.EntryState)
	 */
	public void updateEntryState(String id, EntryState entryState) {
		
		
	}
	
	/**
	 * Insert entry state.
	 * 
	 * @param prefix the prefix
	 * @param entryStateId the entry state id
	 * @param entryId the entry id
	 * @param entryType the entry type
	 * @param previousEntryStateId the previous entry state id
	 * @param entryState the entry state
	 * @param ibatisInserter the ibatis inserter
	 */
	public void insertEntryState(String prefix, String entryStateId,
			String entryId, String entryType, String previousEntryStateId,
			EntryState entryState, IbatisInserter ibatisInserter){
		buildInsertEntryStateBean(
				prefix,
				entryStateId, 
				entryId,
				entryType,
				previousEntryStateId,
				entryState);
		
		if(entryState == null){
			return;
		}
		
		ibatisInserter.insert(INSERT_ENTRY_STATE_SQL, 
				buildInsertEntryStateBean(
						prefix,
						entryStateId, 
						entryId,
						entryType,
						previousEntryStateId,
						entryState));	
		
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.versions.VersionsDao#insertRevision(org.LexGrid.versions.Revision)
	 */
	@Override
	public void insertRevision(Revision revision) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.versions.VersionsDao#insertSystemRelease(org.LexGrid.versions.SystemRelease)
	 */
	@Override
	public void insertSystemRelease(SystemRelease systemRelease) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.versions.VersionsDao#insertEntryState(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.versions.EntryState)
	 */
	public void insertEntryState(String codingSchemeId, String entryStateId,
			String entryId, String entryType, String previousEntryStateId,
			EntryState entryState) {
		this.insertEntryState(
				this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId), 
				entryStateId, 
				entryId, 
				entryType, 
				previousEntryStateId, 
				entryState, 
				this.getNonBatchTemplateInserter());
	}
	

	/**
	 * Builds the insert entry state bean.
	 * 
	 * @param prefix the prefix
	 * @param entryStateId the entry state id
	 * @param entryId the entry id
	 * @param entryType the entry type
	 * @param previousEntryStateId the previous entry state id
	 * @param entryState the entry state
	 * 
	 * @return the insert entry state bean
	 */
	protected InsertEntryStateBean buildInsertEntryStateBean(
			String prefix, 
			String entryStateId, 
			String entryId, 
			String entryType,
			String previousEntryStateId,
			EntryState entryState){
		InsertEntryStateBean bean = new InsertEntryStateBean();
		bean.setPrefix(prefix);
		bean.setEntryId(entryId);
		bean.setEntryState(entryState);
		bean.setEntryType(entryType);
		bean.setEntryStateId(entryStateId);
		bean.setPreviousEntryStateId(previousEntryStateId);
		
		return bean;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.AbstractBaseDao#doGetSupportedLgSchemaVersions()
	 */
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
	}
}

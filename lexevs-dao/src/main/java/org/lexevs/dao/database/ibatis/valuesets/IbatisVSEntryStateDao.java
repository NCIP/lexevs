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

import java.util.List;

import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.lexevs.dao.database.access.valuesets.VSEntryStateDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.batch.IbatisInserter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.revision.IbatisRevisionDao;
import org.lexevs.dao.database.ibatis.versions.parameter.InsertEntryStateBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

/**
 * The Class IbatisVSEntryStateDao manages entrystate data to/fro database.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public class IbatisVSEntryStateDao extends AbstractIbatisDao implements VSEntryStateDao {
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	/** The VERSION s_ namespace. */
	public static String VERSIONS_NAMESPACE = "VSEntryState.";
	
	/** The INSER t_ entr y_ stat e_ sql. */
	public static String INSERT_ENTRY_STATE_SQL = VERSIONS_NAMESPACE + "insertEntryState";
	
	/** The GE t_ entr y_ stat e_ b y_ i d_ sql. */
	public static String GET_ENTRY_STATE_BY_ID_SQL = VERSIONS_NAMESPACE + "insertEntryState";
	
	/** ibatis revision dao*/
	private IbatisRevisionDao ibatisRevisionDao = null;
	

	public EntryState getEntryStateByUId(String entryStateUId) {
		return (EntryState) this.getSqlMapClientTemplate().queryForObject(GET_ENTRY_STATE_BY_ID_SQL, 
			new PrefixedParameter(null, entryStateUId));
	}
	
	public void updateEntryState(String id, EntryState entryState) {
		throw new UnsupportedOperationException();
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
	
	public void insertEntryState( String entryStateUId,
			String entryUId, String entryType, String previousEntryStateUId,
			EntryState entryState) {
		this.insertEntryState(
				this.getPrefixResolver().resolveDefaultPrefix(), 
				entryStateUId, 
				entryUId, 
				entryType, 
				previousEntryStateUId, 
				entryState, 
				this.getNonBatchTemplateInserter());
	}
	
	/**
	 * Builds the insert entry state bean.
	 * 
	 * @param prefix the prefix
	 * @param entryStateUId the entry state id
	 * @param entryUId the entry id
	 * @param entryType the entry type
	 * @param previousEntryStateUId the previous entry state id
	 * @param entryState the entry state
	 * 
	 * @return the insert entry state bean
	 */
	protected InsertEntryStateBean buildInsertEntryStateBean(
			String prefix, 
			String entryStateUId, 
			String entryUId, 
			String entryType,
			String previousEntryStateUId,
			EntryState entryState){
		
		String revisionUId = null;		
		String prevRevisionUId = null;
		
		if (entryState != null) {
			revisionUId = ibatisRevisionDao
					.getRevisionUIdById(entryState.getContainingRevision());
			if (revisionUId == null)
			{
				Revision revision = new Revision();
				revision.setRevisionId(entryState.getContainingRevision());
				revisionUId = ibatisRevisionDao.insertRevisionEntry(revision, null);
				
			}
			if (entryState.getPrevRevision() != null)
				prevRevisionUId = ibatisRevisionDao
					.getRevisionUIdById(entryState.getPrevRevision());
		}
		
		InsertEntryStateBean bean = new InsertEntryStateBean();
		bean.setPrefix(prefix);
		bean.setEntryUId(entryUId);
		bean.setEntryState(entryState);
		bean.setEntryType(entryType);
		bean.setEntryStateUId(entryStateUId);
		bean.setPreviousEntryStateUId(previousEntryStateUId);
		bean.setRevisionUId(revisionUId);
		bean.setPrevRevisionUId(prevRevisionUId);
		
		return bean;
	}

	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createNonTypedList(supportedDatebaseVersion);
	}

	public void setIbatisRevisionDao(IbatisRevisionDao ibatisRevisionDao) {
		this.ibatisRevisionDao = ibatisRevisionDao;
	}
}

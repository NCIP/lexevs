/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.database.ibatis.ncihistory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.versions.CodingSchemeVersion;
import org.LexGrid.versions.SystemRelease;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.lexevs.dao.database.access.ncihistory.NciHistoryDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.SequentialMappedParameterBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.service.ncihistory.NciHistoryService;
import org.lexevs.dao.database.utility.DaoUtility;

public class IbatisNciHistoryDao extends AbstractIbatisDao implements NciHistoryDao {
	
	private static String NCI_HISTORY_NAMESPACE = "NciHistory.";
	
	private static final String GET_REFERENCE_LIST_FOR_VERSION = NCI_HISTORY_NAMESPACE +  "getReferencesForVersion";

	private static final String GET_DATE_FOR_VERSION = NCI_HISTORY_NAMESPACE +  "getDateForVersion";
	
	private static final String GET_VERSIONS_FOR_DATE_RANGE = NCI_HISTORY_NAMESPACE +  "getVersionsForDateRange";
	
	private static String GET_BASELINES_SQL = NCI_HISTORY_NAMESPACE + "getBaseLines";
	
	private static String GET_EARLIEST_BASELINE_SQL = NCI_HISTORY_NAMESPACE + "getEarliestBaseLine";
	
	private static String GET_LATEST_BASELINE_SQL = NCI_HISTORY_NAMESPACE + "getLatestBaseLine";
	
	private static String GET_SYSTEMRELEASE_FOR_URI_SQL = NCI_HISTORY_NAMESPACE + "getSystemReleaseForUri";
	
	private static String INSERT_SYSTEM_RELEASE_SQL = NCI_HISTORY_NAMESPACE + "insertSystemRelease";
	
	private static String GET_DECENDANTS_SQL = NCI_HISTORY_NAMESPACE + "getDecendants";
	
	private static String GET_ANCESTORS_SQL = NCI_HISTORY_NAMESPACE + "getAncestors";
	
	private static String INSERT_NCI_CHANGEEVENT_SQL = NCI_HISTORY_NAMESPACE + "insertNciChangeEvent";
	
	private static String GET_SYSTEMRELEASE_UID_FOR_DATE_SQL = NCI_HISTORY_NAMESPACE + "getSystemReleaseUidForDate";
	
	private static String GET_CHANGE_EVENT_SQL = NCI_HISTORY_NAMESPACE + "getChangeEvent";
	
	private static String GET_CHANGE_EVENT_FOR_DATE_SQL = NCI_HISTORY_NAMESPACE + "getChangeEventForDate";
	
	private static String GET_CHANGE_EVENT_FOR_SYSTEM_RELEASE_SQL = NCI_HISTORY_NAMESPACE + "getChangeEventForSystemReleaseUri";
	
	private static String GET_CONCEPT_CREATION_VERSION_SQL = NCI_HISTORY_NAMESPACE + "getConceptCreationVersion";
	
	private static String GET_CONCEPT_CHANGE_VERSIONS_SQL = NCI_HISTORY_NAMESPACE + "getConceptChangeVersions";
	
	private static String GET_SYSTEMRELEASE_FOR_UID_SQL = NCI_HISTORY_NAMESPACE + "getSystemReleaseForUid";
	
	private static String DELETE_SYSTEMRELEASE_SQL = NCI_HISTORY_NAMESPACE + "deleteSystemRelease";

/** The supported datebase version. */
private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	@Override
	public void removeNciHistory(String codingSchemeUri) {
		this.getSqlMapClientTemplate().delete(DELETE_SYSTEMRELEASE_SQL, new SequentialMappedParameterBean(codingSchemeUri));
	}

	@Override
	public void insertSystemRelease(String codingSchemeUri,
			SystemRelease systemRelease) {
		
		String systemReleaseGuid = this.createUniqueId();
		
		this.getSqlMapClientTemplate().insert(
				INSERT_SYSTEM_RELEASE_SQL, 
				new SequentialMappedParameterBean(systemReleaseGuid,codingSchemeUri, systemRelease));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NCIChangeEvent> getAncestors(String codingSchemeUri, String conceptCode) {
		return this.getSqlMapClientTemplate().queryForList(GET_ANCESTORS_SQL, 
				new SequentialMappedParameterBean(
						codingSchemeUri,
						conceptCode));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemRelease> getBaseLines(String codingSchemeUri, Date releasedAfter,
			Date releasedBefore) {
		
		return this.getSqlMapClientTemplate().queryForList(GET_BASELINES_SQL, 
				new SequentialMappedParameterBean(
						codingSchemeUri,
						releasedAfter,
						releasedBefore));
	}

	@Override
	public CodingSchemeVersion getConceptCreateVersion(String codingSchemeUri, String conceptCode){
		NCIChangeEvent event = (NCIChangeEvent) this.getSqlMapClientTemplate().queryForObject(GET_CONCEPT_CREATION_VERSION_SQL, 
				new SequentialMappedParameterBean(
						codingSchemeUri,
						conceptCode));

		return this.buildCodingSchemeVersion(codingSchemeUri, event);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CodingSchemeVersion> getConceptChangeVersions(String codingSchemeUri, String conceptCode,
			Date beginDate, Date endDate) {
		List<NCIChangeEvent> events = (List<NCIChangeEvent>) this.getSqlMapClientTemplate().queryForList(GET_CONCEPT_CHANGE_VERSIONS_SQL, 
				new SequentialMappedParameterBean(
						codingSchemeUri,
						conceptCode,
						beginDate,
						endDate));
		
		if(CollectionUtils.isEmpty(events)){return null;}
		
		List<CodingSchemeVersion> returnList = new ArrayList<CodingSchemeVersion>();
		
		for(NCIChangeEvent event : events) {
			
			returnList.add(this.buildCodingSchemeVersion(codingSchemeUri, event));
		}

        return returnList;
	}
	
	private CodingSchemeVersion buildCodingSchemeVersion(String codingSchemeUri, NCIChangeEvent event) {
		CodingSchemeVersion result = new CodingSchemeVersion();
		
		String systemReleaseUid = this.getSystemReleaseUidForDate(codingSchemeUri, event.getEditDate());

		SystemRelease systemRelease = this.getSystemReleaseForReleaseUid(codingSchemeUri, systemReleaseUid);

		result.setIsComplete(new Boolean(false));
		result.setReleaseURN(systemRelease.getReleaseURI());
		result.setEntityDescription(systemRelease.getEntityDescription());
		result.setVersionDate(systemRelease.getReleaseDate());
		result.setVersion(NciHistoryService.dateFormat.format(event.getEditDate()).toUpperCase());
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NCIChangeEvent> getDescendants(String codingSchemeUri, String conceptCode) {
		return this.getSqlMapClientTemplate().queryForList(GET_DECENDANTS_SQL, 
				new SequentialMappedParameterBean(
						codingSchemeUri,
						conceptCode));
	}

	@Override
	public SystemRelease getEarliestBaseLine(String codingSchemeUri) {
		return (SystemRelease) this.getSqlMapClientTemplate().queryForObject(GET_EARLIEST_BASELINE_SQL, 
				new SequentialMappedParameterBean(codingSchemeUri));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NCIChangeEvent> getEditActionList(String codingSchemeUri,
			String conceptCode, Date date) {
		return (List<NCIChangeEvent>) this.getSqlMapClientTemplate().queryForList(
				GET_CHANGE_EVENT_FOR_DATE_SQL, 
				new SequentialMappedParameterBean(
						codingSchemeUri, 
						conceptCode,
						date));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NCIChangeEvent> getEditActionList(String codingSchemeUri,
			String conceptCode, Date beginDate, Date endDate) {
		return (List<NCIChangeEvent>) this.getSqlMapClientTemplate().queryForList(
				GET_CHANGE_EVENT_SQL, 
				new SequentialMappedParameterBean(
						codingSchemeUri, 
						conceptCode,
						beginDate,
						endDate));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NCIChangeEvent> getEditActionList(String codingSchemeUri,
			String conceptCode, String releaseURN) {
		
		return (List<NCIChangeEvent>) this.getSqlMapClientTemplate().queryForList(GET_CHANGE_EVENT_FOR_SYSTEM_RELEASE_SQL, 
				new SequentialMappedParameterBean(
						codingSchemeUri,
						conceptCode,
						releaseURN));
	}

	@Override
	public SystemRelease getLatestBaseLine(String codingSchemeUri) {
		return (SystemRelease) this.getSqlMapClientTemplate().queryForObject(GET_LATEST_BASELINE_SQL, 
				new SequentialMappedParameterBean(codingSchemeUri));
	}

	@Override
	public SystemRelease getSystemReleaseForReleaseUri(String codingSchemeUri,
			String releaseURN) {
		return (SystemRelease) this.getSqlMapClientTemplate().queryForObject(GET_SYSTEMRELEASE_FOR_URI_SQL, 
				new SequentialMappedParameterBean(
						codingSchemeUri,
						releaseURN));
	}
	
	@Override
	public SystemRelease getSystemReleaseForReleaseUid(String codingSchemeUri,
			String releaseUid) {
		return (SystemRelease) this.getSqlMapClientTemplate().queryForObject(GET_SYSTEMRELEASE_FOR_UID_SQL, 
				new SequentialMappedParameterBean(
						codingSchemeUri,
						releaseUid));
	}
	
	public void insertNciChangeEvent(String releaseUid, NCIChangeEvent changeEvent) {
		this.getSqlMapClientTemplate().insert(INSERT_NCI_CHANGEEVENT_SQL, 
				new SequentialMappedParameterBean(
						this.createUniqueId(),
						releaseUid,
						changeEvent));	
	}

	@Override
	public String getSystemReleaseUidForDate(String codingSchemeUri,
			Date editDate) {
		return (String) this.getSqlMapClientTemplate().queryForObject(GET_SYSTEMRELEASE_UID_FOR_DATE_SQL, 
				new SequentialMappedParameterBean(
						codingSchemeUri,
						editDate));	
	}

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createNonTypedList(supportedDatebaseVersion);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getCodeListForVersion(String currentVersion) {
		return (List<String>) this.getSqlMapClientTemplate().queryForList(GET_REFERENCE_LIST_FOR_VERSION, 
				new SequentialMappedParameterBean(
						currentVersion));	
	}
	
	@Override
	public Date getDateForVersion(String currentVersion) {
		return (Date) this.getSqlMapClientTemplate().queryForObject(GET_DATE_FOR_VERSION, 
				new SequentialMappedParameterBean(
						currentVersion));	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getVersionsForDateRange(String previousDate, String currentDate) {
		return (List<String>) this.getSqlMapClientTemplate().queryForList(GET_VERSIONS_FOR_DATE_RANGE, 
				new SequentialMappedParameterBean(
						previousDate, currentDate));	
	}
	
	

}
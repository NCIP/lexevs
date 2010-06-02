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
import org.lexevs.dao.database.constants.classifier.property.EntryStateTypeClassifier;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.revision.IbatisRevisionDao;
import org.lexevs.dao.database.ibatis.versions.parameter.InsertEntryStateBean;
import org.lexevs.dao.database.inserter.Inserter;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.batch.classify.Classifier;

/**
 * The Class IbatisVersionsDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IbatisVersionsDao extends AbstractIbatisDao implements VersionsDao {

	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion
			.parseStringToVersion("2.0");

	private Classifier<EntryStateType, String> entryStateTypeClassifier = new EntryStateTypeClassifier();

	/** The VERSION s_ namespace. */
	public static String VERSIONS_NAMESPACE = "Versions.";

	/** The INSER t_ entr y_ stat e_ sql. */
	public static String INSERT_ENTRY_STATE_SQL = VERSIONS_NAMESPACE
			+ "insertEntryState";

	/** The GE t_ entr y_ stat e_ b y_ i d_ sql. */
	public static String GET_ENTRY_STATE_BY_ID_SQL = VERSIONS_NAMESPACE
			+ "insertEntryState";

	/** The GE t_ syste m_ releas e_ i d_ b y_ uri. */
	public static String GET_SYSTEM_RELEASE_ID_BY_URI = VERSIONS_NAMESPACE
			+ "getSystemReleaseGuidByUri";

	private static String DELETE_ALL_ENTRYSTATE_ENTRIES_BY_ENTRY_UID = VERSIONS_NAMESPACE
			+ "deleteAllEntrySateEntriesByEntryUId";

	private static String DELETE_ALL_ENTITY_PROPERTY_ENTRYSTATE_OF_CODINGSCHEME_SQL = VERSIONS_NAMESPACE
			+ "deletaAllEntityPropertyEntryStateOfCodingScheme";

	private static String DELETE_ALL_ENTITY_ENTRYSTATE_OF_CODINGSCHEME_SQL = VERSIONS_NAMESPACE
			+ "deletaAllEntityEntryStateOfCodingScheme";

	private static String DELETE_ALL_CODINGSCHEME_ENTRYSTATES_SQL = VERSIONS_NAMESPACE
			+ "deleteAllCodingSchemeEntrySatesByCodingSchemeUId";
	
	private static String DELETE_ALL_RELATION_PROPERTY_ENTRYSTATE_OF_CODINGSCHEME_SQL = VERSIONS_NAMESPACE
			+ "deletaAllRelationPropertyEntryStateOfCodingScheme";

	private static String DELETE_ALL_RELATION_ENTRYSTATE_OF_CODINGSCHEME_SQL = VERSIONS_NAMESPACE
			+ "deletaAllRelationEntryStateOfCodingScheme";

	private static String DELETE_ALL_ASSN_TARGET_ENTRYSTATE_OF_CODINGSCHEME_SQL = VERSIONS_NAMESPACE
			+ "deletaAllAssnTargetEntryStateOfCodingScheme";

	private static String DELETE_ALL_ASSN_DATA_ENTRYSTATE_OF_CODINGSCHEME_SQL = VERSIONS_NAMESPACE
			+ "deletaAllAssnDataEntryStateOfCodingScheme";

	private static String DELETE_ALL_CS_PROP_ENTRYSTATE_OF_CODINGSCHEME_SQL = VERSIONS_NAMESPACE
			+ "deletaAllCSPropEntryStateOfCodingScheme";
	
	private static String DELETE_ALL_ENTITY_PROPERTY_ENTRYSTATE_OF_ENTITY_SQL = VERSIONS_NAMESPACE
			+ "deletaAllEntityPropEntryStateOfEntity";
	
	private static String DELETE_ALL_ASSN_TARGET_ENTRYSTATE_OF_RELATION_SQL = VERSIONS_NAMESPACE
			+ "deletaAllAssnTargetEntryStateOfRelation";
	
	private static String DELETE_ALL_ASSN_DATA_ENTRYSTATE_OF_RELATION_SQL = VERSIONS_NAMESPACE
			+ "deletaAllAssnDataEntryStateOfRelation";
	
	private static String DELETE_ALL_RELATION_PROPERTY_ENTRYSTATE_OF_RELATION_SQL = VERSIONS_NAMESPACE
			+ "deletaAllRelationPropertyEntryStateOfRelation";
	
	/** ibatis revision dao */
	private IbatisRevisionDao ibatisRevisionDao = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexevs.dao.database.access.versions.VersionsDao#getSystemReleaseIdByUri
	 * (java.lang.String)
	 */
	@Override
	public String getSystemReleaseIdByUri(String systemReleaseUri) {
		
		return (String) this.getSqlMapClientTemplate().queryForObject(
				GET_SYSTEM_RELEASE_ID_BY_URI, systemReleaseUri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexevs.dao.database.access.versions.VersionsDao#getEntryStateById
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	public EntryState getEntryStateById(String entryStateId) {
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		return (EntryState) this.getSqlMapClientTemplate().queryForObject(
				GET_ENTRY_STATE_BY_ID_SQL,
				new PrefixedParameter(prefix, entryStateId));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexevs.dao.database.access.versions.VersionsDao#updateEntryState(
	 * java.lang.String, org.LexGrid.versions.EntryState)
	 */
	public void updateEntryState(String id, EntryState entryState) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Insert entry state.
	 * 
	 * @param prefix
	 *            the prefix
	 * @param entryStateUId
	 *            the entry state id
	 * @param entryUId
	 *            the entry id
	 * @param entryType
	 *            the entry type
	 * @param previousEntryStateUId
	 *            the previous entry state id
	 * @param entryState
	 *            the entry state
	 * @param inserter
	 *            the ibatis inserter
	 */
	public void insertEntryState(String prefix, String entryStateUId,
			String entryUId, String entryType, String previousEntryStateUId,
			EntryState entryState, Inserter inserter) {
		InsertEntryStateBean insertEntryStateBean = buildInsertEntryStateBean(
				prefix, entryStateUId, entryUId, entryType,
				previousEntryStateUId, entryState);

		if (entryState == null) {
			return;
		}

		inserter.insert(INSERT_ENTRY_STATE_SQL, insertEntryStateBean);

	}

	@Override
	public void insertEntryState(String entryStateUId, String entryUId,
			String entryType, String previousEntryStateUId,
			EntryState entryState, Inserter inserter) {
		this.insertEntryState(
				entryStateUId, 
				entryUId, 
				entryType, 
				previousEntryStateUId, 
				entryState, 
				inserter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexevs.dao.database.access.versions.VersionsDao#insertRevision(org
	 * .LexGrid.versions.Revision)
	 */
	@Override
	public void insertRevision(Revision revision) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexevs.dao.database.access.versions.VersionsDao#insertSystemRelease
	 * (org.LexGrid.versions.SystemRelease)
	 */
	@Override
	public void insertSystemRelease(SystemRelease systemRelease) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexevs.dao.database.access.versions.VersionsDao#insertEntryState(
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, org.LexGrid.versions.EntryState)
	 */
	public String insertEntryState(String entryUId, String entryType,
			String previousEntryStateUId, EntryState entryState) {

		String entryStateUId = this.createUniqueId();

		this.insertEntryState(this.getPrefixResolver().resolveDefaultPrefix(),
				entryStateUId, entryUId, entryType, previousEntryStateUId,
				entryState, this.getNonBatchTemplateInserter());

		return entryStateUId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexevs.dao.database.access.versions.VersionsDao#insertEntryState(
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, org.LexGrid.versions.EntryState)
	 */
	public void insertEntryState(String entryStateUId, String entryUId,
			String entryType, String previousEntryStateUId,
			EntryState entryState) {

		this.insertEntryState(this.getPrefixResolver().resolveDefaultPrefix(),
				entryStateUId, entryUId, entryType, previousEntryStateUId,
				entryState, this.getNonBatchTemplateInserter());
	}

	/**
	 * Builds the insert entry state bean.
	 * 
	 * @param prefix
	 *            the prefix
	 * @param entryStateUId
	 *            the entry state id
	 * @param entryUId
	 *            the entry id
	 * @param entryType
	 *            the entry type
	 * @param previousEntryStateUId
	 *            the previous entry state id
	 * @param entryState
	 *            the entry state
	 * 
	 * @return the insert entry state bean
	 */
	protected InsertEntryStateBean buildInsertEntryStateBean(String prefix,
			String entryStateUId, String entryUId, String entryType,
			String previousEntryStateUId, EntryState entryState) {

		String revisionUId = null;
		String prevRevisionUId = null;

		if (entryState != null) {
			revisionUId = ibatisRevisionDao.getRevisionUIdById(entryState
					.getContainingRevision());
			prevRevisionUId = ibatisRevisionDao.getRevisionUIdById(entryState
					.getPrevRevision());
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

	public void deleteAllEntryStateEntriesByEntryUId(String codingSchemeUId,
			String entryUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);
		
		this.getSqlMapClientTemplate().delete(
				DELETE_ALL_ENTRYSTATE_ENTRIES_BY_ENTRY_UID,
				new PrefixedParameter(prefix, entryUId));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexevs.dao.database.access.AbstractBaseDao#doGetSupportedLgSchemaVersions
	 * ()
	 */
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createNonTypedList(supportedDatebaseVersion);
	}

	/**
	 * @return the ibatisRevisionDao
	 */
	public IbatisRevisionDao getIbatisRevisionDao() {
		return ibatisRevisionDao;
	}

	/**
	 * @param ibatisRevisionDao
	 *            the ibatisRevisionDao to set
	 */
	public void setIbatisRevisionDao(IbatisRevisionDao ibatisRevisionDao) {
		this.ibatisRevisionDao = ibatisRevisionDao;
	}

	@Override
	public void deleteAllEntryStateOfCodingScheme(String codingSchemeUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		/* 1. Delete all coding scheme property entry states. */
		this.getSqlMapClientTemplate().delete(
				DELETE_ALL_CS_PROP_ENTRYSTATE_OF_CODINGSCHEME_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.PROPERTY),
						codingSchemeUId));

		/* 2. Delete all entity property entry states. */
		this.getSqlMapClientTemplate().delete(
				DELETE_ALL_ENTITY_PROPERTY_ENTRYSTATE_OF_CODINGSCHEME_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.PROPERTY),
						codingSchemeUId));

		/* 3. Delete all relation property entry states. */
		this.getSqlMapClientTemplate().delete(
				DELETE_ALL_RELATION_PROPERTY_ENTRYSTATE_OF_CODINGSCHEME_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.PROPERTY),
						codingSchemeUId));

		/* 4. Delete all entityAssnsToEntity entry states. */
		this.getSqlMapClientTemplate().delete(
				DELETE_ALL_ASSN_TARGET_ENTRYSTATE_OF_CODINGSCHEME_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.ENTITYASSNSTOENTITY),
						codingSchemeUId));

		/* 5. Delete all entityAssnsToData entry states. */
		this.getSqlMapClientTemplate().delete(
				DELETE_ALL_ASSN_DATA_ENTRYSTATE_OF_CODINGSCHEME_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.ENTITYASSNSTODATA),
						codingSchemeUId));

		/* 7. Delete all relation entry states. */
		this.getSqlMapClientTemplate().delete(
				DELETE_ALL_RELATION_ENTRYSTATE_OF_CODINGSCHEME_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.RELATION),
						codingSchemeUId));

		/* 8. Delete all entity entry states. */
		this.getSqlMapClientTemplate().delete(
				DELETE_ALL_ENTITY_ENTRYSTATE_OF_CODINGSCHEME_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.ENTITY),
						codingSchemeUId));

		/* 9. Delete all coding scheme entry states. */	
		this.getSqlMapClientTemplate().delete(
				DELETE_ALL_CODINGSCHEME_ENTRYSTATES_SQL,
				new PrefixedParameter(prefix,
						codingSchemeUId));
	}

	@Override
	public void deleteAllEntryStateOfEntity(String codingSchemeUId,
			String entityUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		/* 1. Delete all entity property entry states. */
		this.getSqlMapClientTemplate().delete(
				DELETE_ALL_ENTITY_PROPERTY_ENTRYSTATE_OF_ENTITY_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.PROPERTY), entityUId));

		/* 2. Delete all entity entry states. */
		this.deleteAllEntryStateEntriesByEntryUId(codingSchemeUId, entityUId);
	}

	@Override
	public void deleteAllEntryStateOfRelation(String codingSchemeUId,
			String relationUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		/* 1. Delete all entityAssnsToEntity entry states. */
		this.getSqlMapClientTemplate().delete(
				DELETE_ALL_ASSN_TARGET_ENTRYSTATE_OF_RELATION_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.ENTITYASSNSTOENTITY),
						relationUId));

		/* 2. Delete all entityAssnsToData entry states. */
		this.getSqlMapClientTemplate().delete(
				DELETE_ALL_ASSN_DATA_ENTRYSTATE_OF_RELATION_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.ENTITYASSNSTODATA),
						relationUId));

		/* 3. Delete all relation property entry states. */
		this.getSqlMapClientTemplate().delete(
				DELETE_ALL_RELATION_PROPERTY_ENTRYSTATE_OF_RELATION_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.PROPERTY),
						codingSchemeUId));

		/* 4. Delete all relation entry states. */
		this.deleteAllEntryStateEntriesByEntryUId(codingSchemeUId, relationUId);
	}
}

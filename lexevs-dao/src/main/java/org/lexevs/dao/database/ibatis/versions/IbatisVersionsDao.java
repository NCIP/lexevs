
package org.lexevs.dao.database.ibatis.versions;

import java.util.List;

import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.constants.classifier.property.EntryStateTypeClassifier;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.parameter.SequentialMappedParameterBean;
import org.lexevs.dao.database.ibatis.revision.IbatisRevisionDao;
import org.lexevs.dao.database.ibatis.versions.parameter.InsertEntryStateBean;
import org.lexevs.dao.database.inserter.Inserter;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.classify.Classifier;
import org.springframework.util.Assert;

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

	public static String VERSIONS_NAMESPACE = "Versions.";

	public static String INSERT_ENTRY_STATE_SQL = VERSIONS_NAMESPACE
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
	
	private static String UPDATE_PREVIOUS_ENTRY_STATE_UIDS_SQL = VERSIONS_NAMESPACE
			+ "updatePreviousEntryStateUIds";
	
	private static String SET_PREVIOUS_ENTRY_STATE_UIDS_TO_NULL_SQL = VERSIONS_NAMESPACE
			+ "setPreviousEntryStatesNullByEntryUid";
	
	private static String GET_PREV_REV_ID_FROM_GIVEN_REV_ID_FOR_ENTRY_SQL = VERSIONS_NAMESPACE + "getPrevRevIdFromGivenRevIdForEntry";

	private static String GET_ENTRY_STATE_BY_ENTRY_UID_AND_REVISION_ID_SQL = VERSIONS_NAMESPACE + "getEntryStateByEntryUidAndRevisionId";
	
	/** ibatis revision dao */
	private IbatisRevisionDao ibatisRevisionDao = null;
	
	@Override
	public String getPreviousRevisionIdFromGivenRevisionIdForEntry(
			String codingSchemeUid, 
			String entityUid,
			String revisionId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUid);
		
		SequentialMappedParameterBean bean = new SequentialMappedParameterBean(entityUid, revisionId);
		
		bean.setPrefix(prefix);

		return (String) this.getSqlSessionTemplate().
			selectOne(GET_PREV_REV_ID_FROM_GIVEN_REV_ID_FOR_ENTRY_SQL, bean);
	}
	
	@Override
	public EntryState getEntryStateByEntryUidAndRevisionId(
			String codingSchemeUId,
			String entryUId, 
			String revisionId) {
		PrefixedParameterTriple bean = new PrefixedParameterTriple();
		bean.setPrefix(this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId));
		bean.setParam1(entryUId);
		bean.setParam2(revisionId);
		
		return (EntryState)this.getSqlSessionTemplate().
			selectOne(GET_ENTRY_STATE_BY_ENTRY_UID_AND_REVISION_ID_SQL, bean);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexevs.dao.database.access.versions.VersionsDao#getSystemReleaseIdByUri
	 * (java.lang.String)
	 */
	@Override
	public String getSystemReleaseIdByUri(String systemReleaseUri) {
		
		return (String) this.getSqlSessionTemplate().selectOne(
				GET_SYSTEM_RELEASE_ID_BY_URI, systemReleaseUri);
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

	@Override
	public void insertEntryState(
			String codingSchemeUId,
			String entryStateUId, 
			String entryUId,
			EntryStateType entryType, 
			String previousEntryStateUId,
			EntryState entryState, 
			SqlSessionTemplate session) {
		if (entryState == null) {
			return;
		}

		Assert.state(entryType != null);
		Assert.state(
				!entryType.equals(EntryStateType.VALUESETDEFINITION)
				&&
				!entryType.equals(EntryStateType.VALUESETDEFINITIONENTRY)
				&&
				!entryType.equals(EntryStateType.PICKLISTDEFINITION)
				&&
				!entryType.equals(EntryStateType.PICKLISTENTRYNODE),
				"For inserting a ValueSet/Picklist EntryState, use the " +
				" ValueSet DAOs.");

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		InsertEntryStateBean insertEntryStateBean = buildInsertEntryStateBean(
				prefix, 
				entryStateUId, 
				entryUId, 
				entryStateTypeClassifier.classify(entryType),
				previousEntryStateUId, 
				entryState);

		session.insert(INSERT_ENTRY_STATE_SQL, insertEntryStateBean);
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

	@Override
	public void updatePreviousEntryStateUIds(String codingSchemeUId,
			String entryUId, String prevEntryStateUId, String newEntryStateUId) {
		PrefixedParameterTriple bean = new PrefixedParameterTriple();
		bean.setPrefix(this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId));
		bean.setParam1(entryUId);
		bean.setParam2(prevEntryStateUId);
		bean.setParam3(newEntryStateUId);
		
		this.getSqlSessionTemplate().update(
				UPDATE_PREVIOUS_ENTRY_STATE_UIDS_SQL, bean);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexevs.dao.database.access.versions.VersionsDao#insertEntryState(
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, org.LexGrid.versions.EntryState)
	 */
	public String insertEntryState(
			String codingSchemeUId,
			String entryUId, 
			EntryStateType entryType,
			String previousEntryStateUId, 
			EntryState entryState) {

		String entryStateUId = this.createUniqueId();

		this.insertEntryState(
				codingSchemeUId,
				entryStateUId, 
				entryUId, 
				entryType, 
				previousEntryStateUId,
				entryState,
				this.getSqlSessionTemplate());

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
	public void insertEntryState(
			String codingSchemeUId,
			String entryStateUId, 
			String entryUId,
			EntryStateType entryType, 
			String previousEntryStateUId,
			EntryState entryState) {
		
		if (entryState != null && entryState.getRelativeOrder() == null)
			entryState.setRelativeOrder(0L);
		
		this.insertEntryState(
				codingSchemeUId, 
				entryStateUId, 
				entryUId, 
				entryType, 
				previousEntryStateUId, 
				entryState, 
				this.getSqlSessionTemplate());
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
	protected InsertEntryStateBean buildInsertEntryStateBean(
			String prefix,
			String entryStateUId, 
			String entryUId, 
			String entryType,
			String previousEntryStateUId, 
			EntryState entryState) {

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
		Assert.notNull(entryUId);

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);
		
		this.getSqlSessionTemplate().delete(
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
		this.getSqlSessionTemplate().delete(
				DELETE_ALL_CS_PROP_ENTRYSTATE_OF_CODINGSCHEME_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.PROPERTY),
						codingSchemeUId));

		/* 2. Delete all entity property entry states. */
		this.getSqlSessionTemplate().delete(
				DELETE_ALL_ENTITY_PROPERTY_ENTRYSTATE_OF_CODINGSCHEME_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.PROPERTY),
						codingSchemeUId));

		/* 3. Delete all relation property entry states. */
		this.getSqlSessionTemplate().delete(
				DELETE_ALL_RELATION_PROPERTY_ENTRYSTATE_OF_CODINGSCHEME_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.PROPERTY),
						codingSchemeUId));

		/* 4. Delete all entityAssnsToEntity entry states. */
		this.getSqlSessionTemplate().delete(
				DELETE_ALL_ASSN_TARGET_ENTRYSTATE_OF_CODINGSCHEME_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.ENTITYASSNSTOENTITY),
						codingSchemeUId));

		/* 5. Delete all entityAssnsToData entry states. */
		this.getSqlSessionTemplate().delete(
				DELETE_ALL_ASSN_DATA_ENTRYSTATE_OF_CODINGSCHEME_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.ENTITYASSNSTODATA),
						codingSchemeUId));

		/* 7. Delete all relation entry states. */
		this.getSqlSessionTemplate().delete(
				DELETE_ALL_RELATION_ENTRYSTATE_OF_CODINGSCHEME_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.RELATION),
						codingSchemeUId));

		/* 8. Delete all entity entry states. */
		this.getSqlSessionTemplate().delete(
				DELETE_ALL_ENTITY_ENTRYSTATE_OF_CODINGSCHEME_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.ENTITY),
						codingSchemeUId));

		/* 9. Delete all coding scheme entry states. */	
		
		//For some reason, MySQL can't delete these all the time because
		//the prev entry state references the same column. It fails about
		//half of the time. To be safe, set these to null first, then delete.
		this.getSqlSessionTemplate().update(
				SET_PREVIOUS_ENTRY_STATE_UIDS_TO_NULL_SQL,
				new PrefixedParameter(prefix,
						codingSchemeUId));
		
		this.getSqlSessionTemplate().delete(
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
		this.getSqlSessionTemplate().delete(
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
		this.getSqlSessionTemplate().delete(
				DELETE_ALL_ASSN_TARGET_ENTRYSTATE_OF_RELATION_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.ENTITYASSNSTOENTITY),
						relationUId));

		/* 2. Delete all entityAssnsToData entry states. */
		this.getSqlSessionTemplate().delete(
				DELETE_ALL_ASSN_DATA_ENTRYSTATE_OF_RELATION_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.ENTITYASSNSTODATA),
						relationUId));

		/* 3. Delete all relation property entry states. */
		this.getSqlSessionTemplate().delete(
				DELETE_ALL_RELATION_PROPERTY_ENTRYSTATE_OF_RELATION_SQL,
				new PrefixedParameterTuple(prefix,
						this.entryStateTypeClassifier
								.classify(EntryStateType.PROPERTY),
						codingSchemeUId));

		/* 4. Delete all relation entry states. */
		this.deleteAllEntryStateEntriesByEntryUId(codingSchemeUId, relationUId);
	}
}
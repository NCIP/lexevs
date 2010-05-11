package org.lexevs.dao.database.ibatis.association;

import java.util.List;

import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.association.AssociationTargetDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.constants.classifier.property.EntryStateTypeClassifier;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.association.parameter.InsertAssociationQualificationOrUsageContextBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertOrUpdateAssociationTargetBean;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.inserter.Inserter;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

public class IbatisAssociationTargetDao extends AbstractIbatisDao implements
		AssociationTargetDao {

	/** The versions dao. */
	private VersionsDao versionsDao;
	/** */
	private static String ASSOCIATION_NAMESPACE = "Association.";
	/** */
	private static String INSERT_ENTITY_ASSN_ENTITY_SQL = ASSOCIATION_NAMESPACE
			+ "insertEntityAssnsToEntity";
	/** */
	private static String GET_ENTITY_ASSN_TO_ENTITY_UID_BY_INSTANCE_ID_SQL = ASSOCIATION_NAMESPACE
			+ "getAccociationInstanceKey";
	/** */
	private static String GET_ASSN_TARGET_ATTRIBUTES_BY_UID_SQL = ASSOCIATION_NAMESPACE
			+ "getAssnTargetAttributesByUId";

	/** */
	private static String GET_ASSN_QUALS_BY_REF_UID_SQL = ASSOCIATION_NAMESPACE
			+ "getAssnQualsByReferenceUId";

	/** The INSER t_ associatio n_ qua l_ o r_ contex t_ sql. */
	private static String INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL = ASSOCIATION_NAMESPACE
			+ "insertAssociationQualificationOrUsageContext";

	private static String UPDATE_ENTITY_ASSN_TO_ENTITY_BY_UID_SQL = ASSOCIATION_NAMESPACE
			+ "updateEntityAssnToEntityByUId";

	private static String UPDATE_ENTITY_ASSN_TO_ENTITY_VER_ATTRIB_BY_UID_SQL = ASSOCIATION_NAMESPACE
			+ "updateEntityAssnToEntityVerAttribByUId";

	private static String DELETE_ASSOC_QUALS_BY_ASSOC_UID_SQL = ASSOCIATION_NAMESPACE
			+ "deleteAssocQualsByAssocUId";

	private static String DELETE_ASSOC_TARGET_BY_UID_SQL = ASSOCIATION_NAMESPACE
			+ "deleteAssocTargetByAssnUId";

	private static String GET_ASSOC_TARGET_LATEST_REVISION_ID_BY_UID = ASSOCIATION_NAMESPACE
			+ "getAssociationTargetLatestRevisionIdByUId";

	private EntryStateTypeClassifier entryStateClassifier = new EntryStateTypeClassifier();
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion
			.parseStringToVersion("2.0");

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexevs.dao.database.access.AbstractBaseDao#doGetSupportedLgSchemaVersions
	 * ()
	 */
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class,
				supportedDatebaseVersion);
	}

	@Override
	public String insertAssociationTarget(String codingSchemeUId, String associationPredicateUId,
			AssociationSource source, AssociationTarget target) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		String associationTargetUId = this.createUniqueId();

		String entryStateUId = this.doInsertAssociationTarget(
				prefix,
				associationPredicateUId, 
				associationTargetUId, 
				source, 
				target,
				this.getNonBatchTemplateInserter());

		this.versionsDao.insertEntryState(entryStateUId, associationTargetUId,
				entryStateClassifier
						.classify(EntryStateType.ENTITYASSNSTOENTITY), null,
				target.getEntryState());

		return associationTargetUId;
	}

	@Override
	public String updateAssociationTarget(String codingSchemeUId, 
			String associationTargetUId, AssociationSource source,
			AssociationTarget target) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		String entryStateUId = this.createUniqueId();

		InsertOrUpdateAssociationTargetBean bean = new InsertOrUpdateAssociationTargetBean();
		bean.setPrefix(prefix);
		bean.setAssociationSource(source);
		bean.setAssociationTarget(target);
		bean.setUId(associationTargetUId);
		bean.setEntryStateUId(entryStateUId);

		this.getSqlMapClientTemplate().update(
				UPDATE_ENTITY_ASSN_TO_ENTITY_BY_UID_SQL, bean);

		return entryStateUId;
	}

	protected String doInsertAssociationTarget(String prefix, String associationPredicateUId,
			String associationTargetUId, 
			AssociationSource source, AssociationTarget target,
			Inserter inserter) {

		String entryStateUId = this.createUniqueId();

		InsertOrUpdateAssociationTargetBean bean = new InsertOrUpdateAssociationTargetBean();

		bean.setPrefix(prefix);
		bean.setUId(associationTargetUId);
		bean.setAssociationPredicateUId(associationPredicateUId);
		bean.setEntryStateUId(entryStateUId);
		bean.setAssociationSource(source);
		bean.setAssociationTarget(target);

		inserter.insert(INSERT_ENTITY_ASSN_ENTITY_SQL, bean);

		for (AssociationQualification qual : target
				.getAssociationQualification()) {
			String qualUId = this.createUniqueId();

			InsertAssociationQualificationOrUsageContextBean qualBean = new InsertAssociationQualificationOrUsageContextBean();
			qualBean.setAssociationTargetUId(associationTargetUId);
			qualBean.setUId(qualUId);
			qualBean.setPrefix(prefix);
			qualBean.setQualifierName(qual.getAssociationQualifier());
			qualBean.setEntryStateUId(entryStateUId);

			if (qual.getQualifierText() != null) {
				qualBean
						.setQualifierValue(qual.getQualifierText().getContent());
			}

			inserter.insert(INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, qualBean);
		}

		for (String context : target.getUsageContext()) {
			String contextUId = this.createUniqueId();

			InsertAssociationQualificationOrUsageContextBean contextBean = new InsertAssociationQualificationOrUsageContextBean();
			contextBean.setAssociationTargetUId(associationTargetUId);
			contextBean.setUId(contextUId);
			contextBean.setPrefix(prefix);
			contextBean
					.setQualifierName(SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
			contextBean.setQualifierValue(context);
			contextBean.setEntryStateUId(entryStateUId);

			inserter
					.insert(INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, contextBean);
		}
		
		return entryStateUId;
	}

	/**
	 * @return the versionsDao
	 */
	public VersionsDao getVersionsDao() {
		return versionsDao;
	}

	/**
	 * @param versionsDao
	 *            the versionsDao to set
	 */
	public void setVersionsDao(VersionsDao versionsDao) {
		this.versionsDao = versionsDao;
	}

	@Override
	public String getAssociationTargetUId(String codingSchemeUId,
			String associationInstanceId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		return (String) this.getSqlMapClientTemplate().queryForObject(
				GET_ENTITY_ASSN_TO_ENTITY_UID_BY_INSTANCE_ID_SQL,
				new PrefixedParameterTuple(prefix, codingSchemeUId,
						associationInstanceId));
	}

	@Override
	public String insertHistoryAssociationTarget(String codingSchemeUId,
			String associationTargetUId, Boolean assnQualExists,
			Boolean contextExists) {

		String historyPrefix = this.getPrefixResolver()
				.resolvePrefixForHistoryCodingScheme(codingSchemeUId);
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		InsertOrUpdateAssociationTargetBean assnTargetBean = (InsertOrUpdateAssociationTargetBean) this
				.getSqlMapClientTemplate().queryForObject(
						GET_ASSN_TARGET_ATTRIBUTES_BY_UID_SQL,
						new PrefixedParameter(prefix, associationTargetUId));

		assnTargetBean.setPrefix(historyPrefix);

		this.getNonBatchTemplateInserter().insert(
				INSERT_ENTITY_ASSN_ENTITY_SQL, assnTargetBean);

		/* --- TO DO --- */
		if (assnQualExists) {
			InsertAssociationQualificationOrUsageContextBean assnQual = (InsertAssociationQualificationOrUsageContextBean) this
					.getSqlMapClientTemplate()
					.queryForObject(GET_ASSN_QUALS_BY_REF_UID_SQL,
							new PrefixedParameter(prefix, associationTargetUId));
		}

		if (contextExists) {

		}
		/* --- TO DO END --- */

		if (!entryStateExists(prefix, assnTargetBean.getEntryStateUId())) {

			EntryState entryState = new EntryState();

			entryState.setChangeType(ChangeType.NEW);
			entryState.setRelativeOrder(0L);

			this.versionsDao.insertEntryState(
					assnTargetBean.getEntryStateUId(), assnTargetBean.getUId(),
					entryStateClassifier
							.classify(EntryStateType.ENTITYASSNSTOENTITY),
					null, entryState);
		}

		return assnTargetBean.getEntryStateUId();
	}

	@Override
	public void deleteAssnTargetByUId(String codingSchemeUId,
			String associationTargetUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		this.getSqlMapClientTemplate().delete(DELETE_ASSOC_TARGET_BY_UID_SQL,
				new PrefixedParameter(prefix, associationTargetUId));
	}

	@Override
	public String updateVersionableChanges(String codingSchemeUId,
			String associationTargetUId, AssociationSource source,
			AssociationTarget target) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		String entryStateUId = this.createUniqueId();

		InsertOrUpdateAssociationTargetBean bean = new InsertOrUpdateAssociationTargetBean();
		bean.setPrefix(prefix);
		bean.setAssociationSource(source);
		bean.setAssociationTarget(target);
		bean.setUId(associationTargetUId);
		bean.setEntryStateUId(entryStateUId);

		this.getSqlMapClientTemplate().update(
				UPDATE_ENTITY_ASSN_TO_ENTITY_VER_ATTRIB_BY_UID_SQL, bean);

		return entryStateUId;
	}

	@Override
	public String getLatestRevision(String csUId, String targetUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUId);
		String defaultPrefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		return (String) this.getSqlMapClientTemplate().queryForObject(
				GET_ASSOC_TARGET_LATEST_REVISION_ID_BY_UID, 
				new PrefixedParameterTuple(prefix, defaultPrefix, targetUId));	
	}

	@Override
	public void deleteAssociationQualificationsByAssociationTargetUId(
			String codingSchemeUId, String associationTargetUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		this.getSqlMapClientTemplate().delete(
				DELETE_ASSOC_QUALS_BY_ASSOC_UID_SQL,
				new PrefixedParameter(prefix, associationTargetUId));
	}
}

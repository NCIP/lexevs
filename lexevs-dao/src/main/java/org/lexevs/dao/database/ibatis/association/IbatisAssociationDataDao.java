package org.lexevs.dao.database.ibatis.association;

import java.util.List;

import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.association.AssociationDataDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.constants.classifier.property.EntryStateTypeClassifier;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.association.parameter.InsertAssociationQualificationOrUsageContextBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertOrUpdateAssociationDataBean;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.inserter.Inserter;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

public class IbatisAssociationDataDao extends AbstractIbatisDao implements
		AssociationDataDao {

	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion
			.parseStringToVersion("2.0");

	/** The versions dao. */
	private VersionsDao versionsDao;

	/** */
	private static String ASSOCIATION_NAMESPACE = "Association.";

	/** */
	private static String INSERT_ENTITY_ASSN_DATA_SQL = ASSOCIATION_NAMESPACE
			+ "insertEntityAssnsToData";

	/** The INSER t_ associatio n_ qua l_ o r_ contex t_ sql. */
	private static String INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL = ASSOCIATION_NAMESPACE
			+ "insertAssociationQualificationOrUsageContext";

	/** */
	private static String GET_ENTITY_ASSN_TO_DATA_UID_BY_INSTANCE_ID_SQL = ASSOCIATION_NAMESPACE
			+ "getEntityAssnDataUIDByAssocInstanceId";

	/** */
	private static String GET_ASSN_DATA_ATTRIBUTES_BY_UID_SQL = ASSOCIATION_NAMESPACE
			+ "getAssnDataAttributesByUId";

	private static String UPDATE_ENTITY_ASSN_TO_DATA_BY_UID_SQL = ASSOCIATION_NAMESPACE
			+ "updateEntityAssnToDataByUId";

	private static String DELETE_ASSOC_QUALS_BY_ASSOC_UID_SQL = ASSOCIATION_NAMESPACE
			+ "deleteAssocQualsByAssocUId";

	private static String DELETE_ASSOC_DATA_BY_UID_SQL = ASSOCIATION_NAMESPACE
			+ "deleteAssocDataByAssnUId";
	
	private static String UPDATE_ENTITY_ASSN_TO_DATA_VER_ATTRIB_BY_UID_SQL = ASSOCIATION_NAMESPACE
			+ "updateEntityAssnToDataVerAttribByUId";

	private static String GET_ASSOC_DATA_LATEST_REVISION_ID_BY_UID = ASSOCIATION_NAMESPACE
			+ "getAssociationDataLatestRevisionIdByUId";
	
	private EntryStateTypeClassifier entryStateClassifier = new EntryStateTypeClassifier();
	
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

	public String insertAssociationData(String codingSchemeUId, String associationPredicateUId,
			AssociationSource source, AssociationData data) {

		return this.insertAssociationData(
				codingSchemeUId, 
				associationPredicateUId, 
				source, 
				data, 
				this.getNonBatchTemplateInserter());
	}
	
	@Override
	public String insertAssociationData(
			String codingSchemeUId,
			String associationPredicateUId, 
			AssociationSource source,
			AssociationData data, 
			Inserter inserter) {
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		String associationDataUId = this.createUniqueId();

		String entryStateUId = this.doInsertAssociationData(
				prefix,
				associationPredicateUId, 
				associationDataUId, 
				source, 
				data,
				inserter);

		this.versionsDao
				.insertEntryState(entryStateUId, associationDataUId,
						entryStateClassifier
								.classify(EntryStateType.ENTITYASSNSTODATA),
						null, data.getEntryState());

		return associationDataUId;
	}

	protected String doInsertAssociationData(String prefix, String associationPredicateUId,
			String associationDataUId, AssociationSource source, AssociationData data, Inserter inserter) {

		String entryStateUId = this.createUniqueId();

		InsertOrUpdateAssociationDataBean bean = new InsertOrUpdateAssociationDataBean();

		bean.setPrefix(prefix);
		bean.setUId(associationDataUId);
		bean.setAssociationPredicateUId(associationPredicateUId);
		bean.setEntryStateUId(entryStateUId);
		bean.setAssociationSource(source);
		bean.setAssociationData(data);

		inserter.insert(INSERT_ENTITY_ASSN_DATA_SQL, bean);

		for (AssociationQualification qual : data.getAssociationQualification()) {
			String qualUId = this.createUniqueId();

			InsertAssociationQualificationOrUsageContextBean qualBean = new InsertAssociationQualificationOrUsageContextBean();
			qualBean.setAssociationTargetUId(associationDataUId);
			qualBean.setUId(qualUId);
			qualBean.setPrefix(prefix);
			qualBean.setEntryStateUId(entryStateUId);
			qualBean.setQualifierName(qual.getAssociationQualifier());

			if (qual.getQualifierText() != null) {
				qualBean
						.setQualifierValue(qual.getQualifierText().getContent());
			}

			inserter.insert(INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, qualBean);
		}

		for (String context : data.getUsageContext()) {
			String contextUId = this.createUniqueId();

			InsertAssociationQualificationOrUsageContextBean contextBean = new InsertAssociationQualificationOrUsageContextBean();
			contextBean.setAssociationTargetUId(associationDataUId);
			contextBean.setUId(contextUId);
			contextBean.setPrefix(prefix);
			contextBean.setEntryStateUId(entryStateUId);
			contextBean
					.setQualifierName(SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
			contextBean.setQualifierValue(context);

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
	public String getAssociationDataUId(String codingSchemeUId,
			String associationInstanceId) {
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		return (String) this.getSqlMapClientTemplate().queryForObject(
				GET_ENTITY_ASSN_TO_DATA_UID_BY_INSTANCE_ID_SQL,
				new PrefixedParameterTuple(prefix, codingSchemeUId,
						associationInstanceId));
	}

	@Override
	public String insertHistoryAssociationData(String codingSchemeUId,
			String associationDataUId, Boolean assnQualExist,
			Boolean contextExist) {

		String historyPrefix = this.getPrefixResolver()
				.resolvePrefixForHistoryCodingScheme(codingSchemeUId);
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		InsertOrUpdateAssociationDataBean assnDataBean = (InsertOrUpdateAssociationDataBean) this
				.getSqlMapClientTemplate().queryForObject(
						GET_ASSN_DATA_ATTRIBUTES_BY_UID_SQL,
						new PrefixedParameter(prefix, associationDataUId));

		assnDataBean.setPrefix(historyPrefix);

		this.getNonBatchTemplateInserter().insert(
				INSERT_ENTITY_ASSN_DATA_SQL, assnDataBean);

		/* --- TO DO --- */
		if (assnQualExist) {
			/*InsertAssociationQualificationOrUsageContextBean assnQual = (InsertAssociationQualificationOrUsageContextBean) this
					.getSqlMapClientTemplate()
					.queryForObject(GET_ASSN_QUALS_BY_REF_UID_SQL,
							new PrefixedParameter(prefix, associationTargetUId));*/
		}

		if (contextExist) {

		}
		/* --- TO DO END --- */

		if (!entryStateExists(prefix, assnDataBean.getEntryStateUId())) {

			EntryState entryState = new EntryState();

			entryState.setChangeType(ChangeType.NEW);
			entryState.setRelativeOrder(0L);

			this.versionsDao.insertEntryState(
					assnDataBean.getEntryStateUId(), assnDataBean.getUId(),
					entryStateClassifier.classify(EntryStateType.ENTITYASSNSTODATA), null, entryState);
		}

		return assnDataBean.getEntryStateUId();
	}

	@Override
	public String updateAssociationData(String codingSchemeUId,
			String associationDataUId, AssociationSource source,
			AssociationData data) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		String entryStateUId = this.createUniqueId();

		InsertOrUpdateAssociationDataBean bean = new InsertOrUpdateAssociationDataBean();
		bean.setPrefix(prefix);
		bean.setAssociationSource(source);
		bean.setAssociationData(data);
		bean.setUId(associationDataUId);
		bean.setEntryStateUId(entryStateUId);

		this.getSqlMapClientTemplate().update(
				UPDATE_ENTITY_ASSN_TO_DATA_BY_UID_SQL, bean);

		return entryStateUId;
	}

	@Override
	public void deleteAllAssocQualsByAssocDataUId(String codingSchemeUId,
			String associationDataUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		this.getSqlMapClientTemplate().delete(
				DELETE_ASSOC_QUALS_BY_ASSOC_UID_SQL,
				new PrefixedParameter(prefix, associationDataUId));
	}
	
	@Override
	public void deleteAssociationData(String codingSchemeUId,
			String associationDataUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		this.getSqlMapClientTemplate().delete(DELETE_ASSOC_DATA_BY_UID_SQL,
				new PrefixedParameter(prefix, associationDataUId));
	}
	
	@Override
	public String updateVersionableChanges(String codingSchemeUId,
			String associationDataUId, AssociationSource source,
			AssociationData data) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		String entryStateUId = this.createUniqueId();

		InsertOrUpdateAssociationDataBean bean = new InsertOrUpdateAssociationDataBean();
		bean.setPrefix(prefix);
		bean.setAssociationSource(source);
		bean.setAssociationData(data);
		bean.setUId(associationDataUId);
		bean.setEntryStateUId(entryStateUId);

		this.getSqlMapClientTemplate().update(
				UPDATE_ENTITY_ASSN_TO_DATA_VER_ATTRIB_BY_UID_SQL, bean);

		return entryStateUId;
	}

	@Override
	public String getLatestRevision(String csUId, String assocDataUId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUId);
		String defaultPrefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		return (String) this.getSqlMapClientTemplate().queryForObject(
				GET_ASSOC_DATA_LATEST_REVISION_ID_BY_UID, 
				new PrefixedParameterTuple(prefix, defaultPrefix, assocDataUId));		}
}

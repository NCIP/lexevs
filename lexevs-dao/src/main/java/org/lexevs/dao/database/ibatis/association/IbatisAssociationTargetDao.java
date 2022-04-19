
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
import org.lexevs.dao.database.constants.DatabaseConstants;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.association.parameter.InsertAssociationQualificationOrUsageContextBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertOrUpdateAssociationTargetBean;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.inserter.Inserter;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.mybatis.spring.SqlSessionTemplate;

public class IbatisAssociationTargetDao extends AbstractIbatisDao implements
		AssociationTargetDao {

/** The versions dao. */
private VersionsDao versionsDao;
	/** */
	private static String ASSOCIATION_NAMESPACE = "Association.";
	/** */
	public static String INSERT_ENTITY_ASSN_ENTITY_SQL = ASSOCIATION_NAMESPACE
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
	protected static String INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL = ASSOCIATION_NAMESPACE
			+ "insertAssociationQualificationOrUsageContext";

	private static String UPDATE_ENTITY_ASSN_TO_ENTITY_BY_UID_SQL = ASSOCIATION_NAMESPACE
			+ "updateEntityAssnToEntityByUId";

	private static String UPDATE_ENTITY_ASSN_TO_ENTITY_VER_ATTRIB_BY_UID_SQL = ASSOCIATION_NAMESPACE
			+ "updateEntityAssnToEntityVerAttribByUId";

	private static String DELETE_ALL_ASSOC_MULTI_ATTRIBS_BY_ASSOC_UID_SQL = ASSOCIATION_NAMESPACE
			+ "deleteAllAssocMultiAttribByAssocUId";
	
	private static String DELETE_ASSOC_QUALS_BY_ASSOC_UID_SQL = ASSOCIATION_NAMESPACE
			+ "deleteAssocQualsByAssocUId";
	
	private static String DELETE_ASSOC_USAGE_CONTEXT_BY_ASSOC_UID_SQL = ASSOCIATION_NAMESPACE
			+ "deleteAssocUsageContextByAssocUId";

	private static String DELETE_ASSOC_TARGET_BY_UID_SQL = ASSOCIATION_NAMESPACE
			+ "deleteAssocTargetByAssnUId";

	private static String UPDATE_ASSN_QUALS_ENTRYSTATE_UID_BY_ID_SQL = ASSOCIATION_NAMESPACE
			+ "updateAssnQualsEntryStateUId";
	
	private static String UPDATE_ASSN_USAGECONTEXT_ENTRYSTATE_UID_BY_ID_SQL = ASSOCIATION_NAMESPACE
			+ "updateAssnUsageContextEntryStateUId";
	
	private static String GET_ASSOC_TARGET_LATEST_REVISION_ID_BY_UID = ASSOCIATION_NAMESPACE
			+ "getAssociationTargetLatestRevisionIdByUId";
	
	private static String GET_TRIPLE_BY_UID = ASSOCIATION_NAMESPACE 
			+ "getTripleByUid";
	
	private static String GET_HISTORY_TRIPLE_BY_UID_AND_REVISION_ID = ASSOCIATION_NAMESPACE 
			+ "getHistoryTripleByUidAndRevisionId";
	
	private static String GET_ENTRYSTATE_UID_BY_ASSOCIATION_TARGET_UID_SQL = ASSOCIATION_NAMESPACE
			+ "getEntryStateUidByAssociationTarget";

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
	public AssociationSource getTripleByUid(String codingSchemeUId, String tripleUid) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		return 
			(AssociationSource) this.getSqlSessionTemplate().selectOne(
					GET_TRIPLE_BY_UID, 
					new PrefixedParameter(prefix, tripleUid));
	}

	@Override
	public AssociationSource getHistoryTripleByRevision(String codingSchemeUId,
			String tripleUid, String revisionId) {
		String prefix = this.getPrefixResolver().resolvePrefixForHistoryCodingScheme(codingSchemeUId);
		String actualTableSetPrefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		PrefixedParameterTuple bean = new PrefixedParameterTuple();
		bean.setPrefix(prefix);
		bean.setActualTableSetPrefix(actualTableSetPrefix);
		bean.setParam1(tripleUid);
		bean.setParam2(revisionId);
		
		return 
			(AssociationSource) this.getSqlSessionTemplate().selectOne(
					GET_HISTORY_TRIPLE_BY_UID_AND_REVISION_ID, 
					bean);
	}

	@Override
	public String getEntryStateUId(String codingSchemeUId, String associationTargetUid) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		return (String) this.getSqlSessionTemplate().selectOne(
				GET_ENTRYSTATE_UID_BY_ASSOCIATION_TARGET_UID_SQL,
				new PrefixedParameter(prefix, associationTargetUid));
	}

	@Override
	public String insertAssociationTarget(String codingSchemeUId, String associationPredicateUId,
			AssociationSource source, AssociationTarget target) {

		return this.insertAssociationTarget(
				codingSchemeUId, 
				associationPredicateUId, 
				source, 
				target, 
				this.getSqlSessionTemplate());
	} 

	@Override
	public String insertAssociationTarget(
			String codingSchemeUId,
			String associationPredicateUId, 
			String sourceEntityCode,
			String sourceEntityCodeNamespace, 
			AssociationTarget target) {
		AssociationSource source = new AssociationSource();
		source.setSourceEntityCode(sourceEntityCode);
		source.setSourceEntityCodeNamespace(sourceEntityCodeNamespace);
	
		return 
			this.insertAssociationTarget(codingSchemeUId, associationPredicateUId, source, target);
	}

	@Override
	public String updateAssociationTarget(String codingSchemeUId, 
			String associationTargetUId,
			AssociationTarget target) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		String entryStateUId = this.createUniqueId();

		InsertOrUpdateAssociationTargetBean bean = new InsertOrUpdateAssociationTargetBean();
		bean.setPrefix(prefix);
		bean.setAssociationTarget(target);
		bean.setUId(associationTargetUId);
		bean.setEntryStateUId(entryStateUId);

		this.getSqlSessionTemplate().update(
				UPDATE_ENTITY_ASSN_TO_ENTITY_BY_UID_SQL, bean);
		
		AssociationQualification[] assocQual = target.getAssociationQualification();
		
		if (assocQual.length != 0) {
			
			this.getSqlSessionTemplate().delete(
					DELETE_ASSOC_QUALS_BY_ASSOC_UID_SQL,
					new PrefixedParameter(prefix, associationTargetUId));
			
			for (int i = 0; i < assocQual.length; i++) {

				InsertAssociationQualificationOrUsageContextBean qualBean = new InsertAssociationQualificationOrUsageContextBean();
				
				qualBean.setPrefix(prefix);
				qualBean.setUId(this.createUniqueId());
				qualBean.setReferenceUId(associationTargetUId);
				qualBean.setQualifierName(assocQual[i].getAssociationQualifier());
				if (assocQual[i].getQualifierText() != null) {
					qualBean.setQualifierValue(assocQual[i].getQualifierText().getContent());
				}
				qualBean.setEntryStateUId(entryStateUId);
				
				this.getSqlSessionTemplate().insert(INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, qualBean);
			}
		} else {
			this.getSqlSessionTemplate().update(
					UPDATE_ASSN_QUALS_ENTRYSTATE_UID_BY_ID_SQL,
					new PrefixedParameterTuple(prefix, associationTargetUId,
							entryStateUId));
		}
		
		String[] usageContext = target.getUsageContext();
		
		if (usageContext.length != 0) {
			
			this.getSqlSessionTemplate().delete(
					DELETE_ASSOC_USAGE_CONTEXT_BY_ASSOC_UID_SQL,
					new PrefixedParameter(prefix, associationTargetUId));
			
			for (int i = 0; i < usageContext.length; i++) {

				InsertAssociationQualificationOrUsageContextBean qualBean = new InsertAssociationQualificationOrUsageContextBean();
				
				qualBean.setPrefix(prefix);
				qualBean.setUId(this.createUniqueId());
				qualBean.setReferenceUId(associationTargetUId);
				qualBean.setQualifierName(SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
				if (assocQual[i].getQualifierText() != null) {
					qualBean.setQualifierValue(assocQual[i].getQualifierText().getContent());
				}
				qualBean.setEntryStateUId(entryStateUId);
				
				this.getSqlSessionTemplate().insert(INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, qualBean);
			}
		} else {
			this.getSqlSessionTemplate().update(
					UPDATE_ASSN_USAGECONTEXT_ENTRYSTATE_UID_BY_ID_SQL,
					new PrefixedParameterTuple(prefix, associationTargetUId,
							entryStateUId));
		}

		return entryStateUId;
	}

	@Override
	public String insertAssociationTarget(String codingSchemeUId,
			String associationPredicateUId, AssociationSource source,
			AssociationTarget target, SqlSessionTemplate session) {
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		String associationTargetUId = this.createUniqueId();

		String entryStateUId = this.doInsertAssociationTarget(
				prefix,
				associationPredicateUId, 
				associationTargetUId, 
				source, 
				target,
				session);

		this.versionsDao.insertEntryState(
				codingSchemeUId,
				entryStateUId, 
				associationTargetUId,
				EntryStateType.ENTITYASSNSTOENTITY, 
				null,
				target.getEntryState(), 
				session);

		return associationTargetUId;
	}

	protected String doInsertAssociationTarget(String prefix, String associationPredicateUId,
			String associationTargetUId, 
			AssociationSource source, AssociationTarget target,
			SqlSessionTemplate session) {

		String entryStateUId = this.createUniqueId();

		InsertOrUpdateAssociationTargetBean bean = new InsertOrUpdateAssociationTargetBean();

		if (target.getAssociationInstanceId() == null
				|| target.getAssociationInstanceId().trim().equals("")) {
			target.setAssociationInstanceId(DatabaseConstants.GENERATED_ID_PREFIX + this.createRandomIdentifier());
		}
		
		bean.setPrefix(prefix);
		bean.setUId(associationTargetUId);
		bean.setAssociationPredicateUId(associationPredicateUId);
		bean.setEntryStateUId(entryStateUId);
		bean.setAssociationSource(source);
		bean.setAssociationTarget(target);

		session.insert(INSERT_ENTITY_ASSN_ENTITY_SQL, bean);

		for (AssociationQualification qual : target
				.getAssociationQualification()) {
			String qualUId = this.createUniqueId();

			InsertAssociationQualificationOrUsageContextBean qualBean = new InsertAssociationQualificationOrUsageContextBean();
			qualBean.setReferenceUId(associationTargetUId);
			qualBean.setUId(qualUId);
			qualBean.setPrefix(prefix);
			qualBean.setQualifierName(qual.getAssociationQualifier());
			qualBean.setEntryStateUId(entryStateUId);

			if (qual.getQualifierText() != null) {
				qualBean
						.setQualifierValue(qual.getQualifierText().getContent());
			}

			session.insert(INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, qualBean);
		}

		for (String context : target.getUsageContext()) {
			String contextUId = this.createUniqueId();

			InsertAssociationQualificationOrUsageContextBean contextBean = new InsertAssociationQualificationOrUsageContextBean();
			contextBean.setReferenceUId(associationTargetUId);
			contextBean.setUId(contextUId);
			contextBean.setPrefix(prefix);
			contextBean
					.setQualifierName(SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
			contextBean.setQualifierValue(context);
			contextBean.setEntryStateUId(entryStateUId);

			session
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

		return (String) this.getSqlSessionTemplate().selectOne(
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
				.getSqlSessionTemplate().selectOne(
						GET_ASSN_TARGET_ATTRIBUTES_BY_UID_SQL,
						new PrefixedParameter(prefix, associationTargetUId));

		assnTargetBean.setPrefix(historyPrefix);

		this.getSqlSessionTemplate().insert(
				INSERT_ENTITY_ASSN_ENTITY_SQL, assnTargetBean);

		if (assnTargetBean.getAssnQualsAndUsageContext() != null) {
			for (int i = 0; i < assnTargetBean.getAssnQualsAndUsageContext()
					.size(); i++) {
				InsertAssociationQualificationOrUsageContextBean assocMultiAttrib = assnTargetBean
						.getAssnQualsAndUsageContext().get(i);

				assocMultiAttrib.setPrefix(historyPrefix);

				this.getSqlSessionTemplate().insert(
						INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL,
						assocMultiAttrib);
			}
		}
		
		if (!entryStateExists(codingSchemeUId, assnTargetBean.getEntryStateUId())) {

			EntryState entryState = new EntryState();

			entryState.setChangeType(ChangeType.NEW);
			entryState.setRelativeOrder(0L);

			this.versionsDao.insertEntryState(
					codingSchemeUId,
					assnTargetBean.getEntryStateUId(), 
					assnTargetBean.getUId(),
					EntryStateType.ENTITYASSNSTOENTITY,
					null,
					entryState);
		}

		return assnTargetBean.getEntryStateUId();
	}

	@Override
	public void deleteAssnTargetByUId(String codingSchemeUId,
			String associationTargetUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		this.getSqlSessionTemplate().delete(DELETE_ASSOC_TARGET_BY_UID_SQL,
				new PrefixedParameter(prefix, associationTargetUId));
	}

	@Override
	public String updateVersionableChanges(String codingSchemeUId,
			String associationTargetUId,
			AssociationTarget target) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		String entryStateUId = this.createUniqueId();

		InsertOrUpdateAssociationTargetBean bean = new InsertOrUpdateAssociationTargetBean();
		bean.setPrefix(prefix);
		bean.setAssociationTarget(target);
		bean.setUId(associationTargetUId);
		bean.setEntryStateUId(entryStateUId);

		this.getSqlSessionTemplate().update(
				UPDATE_ENTITY_ASSN_TO_ENTITY_VER_ATTRIB_BY_UID_SQL, bean);

		this.getSqlSessionTemplate().update(
				UPDATE_ASSN_QUALS_ENTRYSTATE_UID_BY_ID_SQL,
				new PrefixedParameterTuple(prefix, associationTargetUId,
						entryStateUId));
		
		this.getSqlSessionTemplate().update(
				UPDATE_ASSN_USAGECONTEXT_ENTRYSTATE_UID_BY_ID_SQL,
				new PrefixedParameterTuple(prefix, associationTargetUId,
						entryStateUId));
		
		return entryStateUId;
	}

	@Override
	public String getLatestRevision(String csUId, String targetUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUId);
		
		return (String) this.getSqlSessionTemplate().selectOne(
				GET_ASSOC_TARGET_LATEST_REVISION_ID_BY_UID, 
				new PrefixedParameter(prefix, targetUId));	
	}

	@Override
	public void deleteAssociationMultiAttribsByAssociationTargetUId(
			String codingSchemeUId, String associationTargetUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);
		
		String histPrefix = this.getPrefixResolver().resolvePrefixForHistoryCodingScheme(codingSchemeUId);

		this.getSqlSessionTemplate().delete(
				DELETE_ALL_ASSOC_MULTI_ATTRIBS_BY_ASSOC_UID_SQL,
				new PrefixedParameter(prefix, associationTargetUId));
		
		this.getSqlSessionTemplate().delete(
				DELETE_ALL_ASSOC_MULTI_ATTRIBS_BY_ASSOC_UID_SQL,
				new PrefixedParameter(histPrefix, associationTargetUId));
	}
	
	@Override
	public boolean entryStateExists(String codingSchemeUId, String entryStateUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		return super.entryStateExists(prefix, entryStateUId);
	}
}
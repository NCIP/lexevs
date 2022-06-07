
package org.lexevs.dao.database.ibatis.association;

import java.util.List;

import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexevs.dao.database.access.association.AssociationDataDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.constants.DatabaseConstants;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.association.parameter.InsertAssociationQualificationOrUsageContextBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertOrUpdateAssociationDataBean;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.inserter.Inserter;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.mybatis.spring.SqlSessionTemplate;

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
	public static String INSERT_ENTITY_ASSN_DATA_SQL = ASSOCIATION_NAMESPACE
			+ "insertEntityAssnsToData";

	/** The INSER t_ associatio n_ qua l_ o r_ contex t_ sql. */
	private static String INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL = ASSOCIATION_NAMESPACE
			+ "insertAssociationQualificationOrUsageContext";

	/** */
	private static String GET_ENTITY_ASSN_TO_DATA_UID_BY_INSTANCE_ID_SQL = ASSOCIATION_NAMESPACE
			+ "getEntityAssnDataUIDByAssocInstanceId";
	
	private static String GET_ENTITY_ASSN_TO_DATA_BY_UID_SQL = ASSOCIATION_NAMESPACE
			+ "getEntityAssnDataByUid";
	
	private static String GET_ENTITY_ASSN_TO_DATA_BY_UID_AND_REVISION_ID_SQL = ASSOCIATION_NAMESPACE
			+ "getEntityAssnDataByUidAndRevisionId";

	private static String GET_ASSN_DATA_ATTRIBUTES_BY_UID_SQL = ASSOCIATION_NAMESPACE
			+ "getAssnDataAttributesByUId";

	private static String UPDATE_ENTITY_ASSN_TO_DATA_BY_UID_SQL = ASSOCIATION_NAMESPACE
			+ "updateEntityAssnToDataByUId";
	
	private static String DELETE_ALL_ASSOC_MULTI_ATTRIBS_BY_ASSOC_UID_SQL = ASSOCIATION_NAMESPACE
			+ "deleteAllAssocMultiAttribByAssocUId";

	private static String DELETE_ASSOC_QUALS_BY_ASSOC_UID_SQL = ASSOCIATION_NAMESPACE
			+ "deleteAssocQualsByAssocUId";

	private static String DELETE_ASSOC_USAGE_CONTEXT_BY_ASSOC_UID_SQL = ASSOCIATION_NAMESPACE
			+ "deleteAssocUsageContextByAssocUId";


	private static String DELETE_ASSOC_DATA_BY_UID_SQL = ASSOCIATION_NAMESPACE
			+ "deleteAssocDataByAssnUId";
	
	private static String UPDATE_ASSN_QUALS_ENTRYSTATE_UID_BY_ID_SQL = ASSOCIATION_NAMESPACE
			+ "updateAssnQualsEntryStateUId";
	
	private static String UPDATE_ENTITY_ASSN_TO_DATA_VER_ATTRIB_BY_UID_SQL = ASSOCIATION_NAMESPACE
			+ "updateEntityAssnToDataVerAttribByUId";

	private static String GET_ASSOC_DATA_LATEST_REVISION_ID_BY_UID = ASSOCIATION_NAMESPACE
			+ "getAssociationDataLatestRevisionIdByUId";
	
	private static String GET_ENTRYSTATE_UID_BY_ASSOCIATION_DATA_UID_SQL = ASSOCIATION_NAMESPACE
			+ "getEntryStateUidByAssociationData";
	
	private static String GET_TRIPLE_BY_UID = ASSOCIATION_NAMESPACE
			+ "getAssociationDataTripleByUid";

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
				this.getSqlSessionTemplate());
	}
	
	@Override
	public void insertAssociationData(
			String codingSchemeUId,
			String associationPredicateUId, 
			String sourceEntityCode,
			String sourceEntityCodeNamespace, 
			AssociationData data) {
		AssociationSource source = new AssociationSource();
		source.setSourceEntityCode(sourceEntityCode);
		source.setSourceEntityCodeNamespace(sourceEntityCodeNamespace);
		
		this.insertAssociationData(codingSchemeUId, associationPredicateUId, source, data);
	}

	@Override
	public String insertAssociationData(
			String codingSchemeUId,
			String associationPredicateUId, 
			AssociationSource source,
			AssociationData data, 
			SqlSessionTemplate session) {
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		String associationDataUId = this.createUniqueId();

		String entryStateUId = this.doInsertAssociationData(
				prefix,
				associationPredicateUId, 
				associationDataUId, 
				source, 
				data,
				session);

		this.versionsDao
				.insertEntryState(
						codingSchemeUId, 
						entryStateUId, 
						associationDataUId,
						EntryStateType.ENTITYASSNSTODATA,
						null, 
						data.getEntryState());

		return associationDataUId;
	}
	
	@Override
	public AssociationSource getTripleByUid(String codingSchemeUId, String tripleUid) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		return 
			(AssociationSource) this.getSqlSessionTemplate().selectOne(
					GET_TRIPLE_BY_UID, 
					new PrefixedParameter(prefix, tripleUid));
	}

	protected String doInsertAssociationData(String prefix, String associationPredicateUId,
			String associationDataUId, AssociationSource source, AssociationData data, SqlSessionTemplate session) {

		String entryStateUId = this.createUniqueId();

		InsertOrUpdateAssociationDataBean bean = new InsertOrUpdateAssociationDataBean();

		if (data.getAssociationInstanceId() == null
				|| data.getAssociationInstanceId().trim().equals("")) {
			data.setAssociationInstanceId(DatabaseConstants.GENERATED_ID_PREFIX  + this.createRandomIdentifier());
		}
		
		bean.setPrefix(prefix);
		bean.setUId(associationDataUId);
		bean.setAssociationPredicateUId(associationPredicateUId);
		bean.setEntryStateUId(entryStateUId);
		bean.setAssociationSource(source);
		bean.setAssociationData(data);
		
		bean.setSourceEntityCode(source.getSourceEntityCode());
		bean.setSourceEntityCodeNamespace(source.getSourceEntityCodeNamespace());
		bean.setAssociationInstanceId(data.getAssociationInstanceId());
		bean.setIsDefining(data.getIsDefining());
		bean.setIsInferred(data.getIsInferred());
		bean.setDataValue(data.getAssociationDataText().getContent());
		bean.setIsActive(data.getIsActive());
		bean.setOwner(data.getOwner());
		bean.setStatus(data.getStatus());
		bean.setEffectiveDate(data.getEffectiveDate());
		bean.setExpirationDate(data.getExpirationDate());

		session.insert(INSERT_ENTITY_ASSN_DATA_SQL, bean);

		for (AssociationQualification qual : data.getAssociationQualification()) {
			String qualUId = this.createUniqueId();

			InsertAssociationQualificationOrUsageContextBean qualBean = new InsertAssociationQualificationOrUsageContextBean();
			qualBean.setReferenceUId(associationDataUId);
			qualBean.setUId(qualUId);
			qualBean.setPrefix(prefix);
			qualBean.setEntryStateUId(entryStateUId);
			qualBean.setQualifierName(qual.getAssociationQualifier());

			if (qual.getQualifierText() != null) {
				qualBean
						.setQualifierValue(qual.getQualifierText().getContent());
			}

			session.insert(INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, qualBean);
		}

		for (String context : data.getUsageContext()) {
			String contextUId = this.createUniqueId();

			InsertAssociationQualificationOrUsageContextBean contextBean = new InsertAssociationQualificationOrUsageContextBean();
			contextBean.setReferenceUId(associationDataUId);
			contextBean.setUId(contextUId);
			contextBean.setPrefix(prefix);
			contextBean.setEntryStateUId(entryStateUId);
			contextBean
					.setQualifierName(SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
			contextBean.setQualifierValue(context);

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
	public AssociationData getAssociationDataByUid(
			String codingSchemeUId,
			String associationDataUid) {
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		return (AssociationData) this.getSqlSessionTemplate().selectOne(
				GET_ENTITY_ASSN_TO_DATA_BY_UID_SQL,
				new PrefixedParameter(prefix,
						associationDataUid));
	}

	@Override
	public AssociationData getHistoryAssociationDataByRevision(
			String codingSchemeUId, 
			String associationDataUid, 
			String revisionId) {
		
		String prefix = this.getPrefixResolver().resolvePrefixForHistoryCodingScheme(
				codingSchemeUId);
		String actualTableSetPrefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);
		
		PrefixedParameterTuple bean = new PrefixedParameterTuple();
		bean.setPrefix(prefix);
		bean.setActualTableSetPrefix(actualTableSetPrefix);
		bean.setParam1(associationDataUid);
		bean.setParam2(revisionId);

		return (AssociationData) this.getSqlSessionTemplate().selectOne(
				GET_ENTITY_ASSN_TO_DATA_BY_UID_AND_REVISION_ID_SQL,
				bean
				);
	}

	@Override
	public String getAssociationDataUId(String codingSchemeUId,
			String associationInstanceId) {
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		return (String) this.getSqlSessionTemplate().selectOne(
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
				.getSqlSessionTemplate().selectOne(
						GET_ASSN_DATA_ATTRIBUTES_BY_UID_SQL,
						new PrefixedParameter(prefix, associationDataUId));

		assnDataBean.setPrefix(historyPrefix);

		this.getSqlSessionTemplate().insert(
				INSERT_ENTITY_ASSN_DATA_SQL, assnDataBean);

		if (assnDataBean.getAssnQualsAndUsageContext() != null) {
			for (int i = 0; i < assnDataBean.getAssnQualsAndUsageContext()
					.size(); i++) {
				InsertAssociationQualificationOrUsageContextBean assocMultiAttrib = assnDataBean
						.getAssnQualsAndUsageContext().get(i);

				assocMultiAttrib.setPrefix(historyPrefix);

				this.getSqlSessionTemplate().insert(
						INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL,
						assocMultiAttrib);
			}
		}

		return assnDataBean.getEntryStateUId();
	}

	@Override
	public String updateAssociationData(String codingSchemeUId,
			String associationDataUId,
			AssociationData data) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		String entryStateUId = this.createUniqueId();

		InsertOrUpdateAssociationDataBean bean = new InsertOrUpdateAssociationDataBean();
		bean.setPrefix(prefix);
		bean.setAssociationData(data);
		bean.setUId(associationDataUId);
		bean.setEntryStateUId(entryStateUId);

		this.getSqlSessionTemplate().update(
				UPDATE_ENTITY_ASSN_TO_DATA_BY_UID_SQL, bean);

		AssociationQualification[] assocQual = data.getAssociationQualification();
		
		if (assocQual.length != 0) {
			
			this.getSqlSessionTemplate().delete(
					DELETE_ASSOC_QUALS_BY_ASSOC_UID_SQL,
					new PrefixedParameter(prefix, associationDataUId));
			
			for (int i = 0; i < assocQual.length; i++) {

				InsertAssociationQualificationOrUsageContextBean qualBean = new InsertAssociationQualificationOrUsageContextBean();
				
				qualBean.setPrefix(prefix);
				qualBean.setUId(this.createUniqueId());
				qualBean.setReferenceUId(associationDataUId);
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
					new PrefixedParameterTuple(prefix, associationDataUId,
							entryStateUId));
		}
		
		String[] usageContext = data.getUsageContext();
		
		if (usageContext.length != 0) {
			
			this.getSqlSessionTemplate().delete(
					DELETE_ASSOC_USAGE_CONTEXT_BY_ASSOC_UID_SQL,
					new PrefixedParameter(prefix, associationDataUId));
			
			for (int i = 0; i < usageContext.length; i++) {

				InsertAssociationQualificationOrUsageContextBean qualBean = new InsertAssociationQualificationOrUsageContextBean();
				
				qualBean.setPrefix(prefix);
				qualBean.setUId(this.createUniqueId());
				qualBean.setReferenceUId(associationDataUId);
				qualBean.setQualifierName(SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
				if (assocQual[i].getQualifierText() != null) {
					qualBean.setQualifierValue(assocQual[i].getQualifierText().getContent());
				}
				qualBean.setEntryStateUId(entryStateUId);
				
				this.getSqlSessionTemplate().insert(INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, qualBean);
			}
		}
		
		return entryStateUId;
	}

	@Override
	public void deleteAllAssocQualsByAssocDataUId(String codingSchemeUId,
			String associationDataUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		this.getSqlSessionTemplate().delete(
				DELETE_ALL_ASSOC_MULTI_ATTRIBS_BY_ASSOC_UID_SQL,
				new PrefixedParameter(prefix, associationDataUId));
	}
	
	@Override
	public void deleteAssociationData(String codingSchemeUId,
			String associationDataUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		this.getSqlSessionTemplate().delete(DELETE_ASSOC_DATA_BY_UID_SQL,
				new PrefixedParameter(prefix, associationDataUId));
	}
	
	@Override
	public String updateVersionableChanges(
			String codingSchemeUId,
			String associationDataUId, 
			AssociationData data) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		String entryStateUId = this.createUniqueId();

		InsertOrUpdateAssociationDataBean bean = new InsertOrUpdateAssociationDataBean();
		bean.setPrefix(prefix);
		bean.setAssociationData(data);
		bean.setUId(associationDataUId);
		bean.setEntryStateUId(entryStateUId);

		this.getSqlSessionTemplate().update(
				UPDATE_ENTITY_ASSN_TO_DATA_VER_ATTRIB_BY_UID_SQL, bean);

		return entryStateUId;
	}

	@Override
	public String getLatestRevision(String csUId, String assocDataUId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUId);
		
		return (String) this.getSqlSessionTemplate().selectOne(
				GET_ASSOC_DATA_LATEST_REVISION_ID_BY_UID, 
				new PrefixedParameter(prefix, assocDataUId));		
	}

	@Override
	public boolean entryStateExists(String codingSchemeUid, String entryStateUId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		return super.entryStateExists(prefix, entryStateUId);
	}
	
	@Override
	public String getEntryStateUId(
			String codingSchemeUId, 
			String associationDataUId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		return (String) this.getSqlSessionTemplate().selectOne(
				GET_ENTRYSTATE_UID_BY_ASSOCIATION_DATA_UID_SQL,
				new PrefixedParameter(prefix, associationDataUId));
	}
}
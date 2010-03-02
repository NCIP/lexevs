package org.lexevs.dao.database.ibatis.association;

import java.sql.SQLException;
import java.util.List;

import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.junit.Assert;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.association.batch.AssociationSourceBatchInsertItem;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.association.parameter.InsertAssociationPredicateBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertAssociationQualificationOrUsageContextBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertAssociationSourceBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertRelationsBean;
import org.lexevs.dao.database.ibatis.batch.IbatisBatchInserter;
import org.lexevs.dao.database.ibatis.batch.IbatisInserter;
import org.lexevs.dao.database.ibatis.batch.SqlMapExecutorBatchInserter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.versions.IbatisVersionsDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;

public class IbatisAssociationDao extends AbstractIbatisDao implements AssociationDao {

	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	private static String INSERT_RELATIONS_SQL = "insertRelations";
	private static String INSERT_ENTITY_ASSNS_TO_ENTITY_SQL = "insertEntityAssnsToEntity";
	private static String INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL = "insertAssociationQualificationOrUsageContext";
	private static String INSERT_ASSOCIATION_PREDICATE_SQL = "insertAssociationPredicate";
	private static String GET_ASSOCIATION_INSTANCE_KEY_SQL = "getAccociationInstanceKey";
	private static String GET_RELATIONS_KEY_SQL = "getRelationsKey";
	private static String GET_ASSOCIATION_PREDICATE_KEY_SQL = "getAssociationPredicateKey";
	
	private IbatisVersionsDao ibatisVersionsDao;
	private CodingSchemeDao codingSchemeDao;

	public String getAssociationPredicateId(String codingSchemeId,
			String relationContainerId, String associationPredicateName) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return
			(String) 
				this.getSqlMapClientTemplate().queryForObject(GET_ASSOCIATION_PREDICATE_KEY_SQL, new PrefixedParameterTuple(
						prefix, relationContainerId, associationPredicateName));
	}
	
	public String getRelationsId(String codingSchemeId, String relationsName) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return
			(String) 
				this.getSqlMapClientTemplate().queryForObject(GET_RELATIONS_KEY_SQL, new PrefixedParameterTuple(prefix, codingSchemeId, relationsName));
	}

	public void insertRelations(String codingSchemeUri, String version,
			Relations relations) {
		String codingSchemeId = codingSchemeDao.getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
		this.insertRelations(
				codingSchemeId, 
				relations);
	}

	public String insertRelations(String codingSchemeId,
			Relations relations) {
		String relationsId = this.createUniqueId();
		String entryStateId = this.createUniqueId();
		
		InsertRelationsBean bean = new InsertRelationsBean();
		bean.setEntryStateId(entryStateId);
		bean.setId(relationsId);
		bean.setCodingSchemeId(codingSchemeId);
		bean.setRelations(relations);
		bean.setPrefix(this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId));
		
		this.getSqlMapClientTemplate().insert(INSERT_RELATIONS_SQL, bean);
		
		return relationsId;
	}

	public String insertAssociationPredicate(String codingSchemeId, String relationId,
			AssociationPredicate associationPredicate) {
		
		String id = this.createUniqueId();
		InsertAssociationPredicateBean bean = new InsertAssociationPredicateBean();
		bean.setPrefix(this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId));
		bean.setAssociationPredicate(associationPredicate);
		bean.setId(id);
		bean.setRelationId(relationId);
		
		this.getSqlMapClientTemplate().insert(INSERT_ASSOCIATION_PREDICATE_SQL, bean);
		
		return id;
	}
	
	public void insertBatchAssociationSources(final String codingSchemeId,
			final List<AssociationSourceBatchInsertItem> list) {
		
		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){
		
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {
				IbatisBatchInserter batchInserter = new SqlMapExecutorBatchInserter(executor);
				
				batchInserter.startBatch();
				
				for(AssociationSourceBatchInsertItem item : list){
					
					insertAssociationSource(
							codingSchemeId, 
							item.getParentId(), 
							item.getAssociationSource(), 
							batchInserter);
				}
				
				batchInserter.executeBatch();
				
				return null;
			}	
		});
	}
	
	public void insertAssociationSource(String codingSchemeId,
			String associationPredicateId, AssociationSource source){
		this.insertAssociationSource(codingSchemeId, associationPredicateId, source, 
				this.getNonBatchTemplateInserter());
	}

	public void insertAssociationSource(String codingSchemeId,
			String associationPredicateId, AssociationSource source, IbatisInserter inserter) {
		Assert.assertTrue("Must Insert at least ONE AssociationTarget per AssociationSource.", source.getTarget().length > 0);
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		for(AssociationTarget target : source.getTarget()){
			String associationTargetId = this.createUniqueId();
			String entryStateId = this.createUniqueId();
			
			InsertAssociationSourceBean bean = new InsertAssociationSourceBean();
			bean.setPrefix(prefix);
			bean.setAssociationPredicateId(associationPredicateId);
			bean.setAssociationSource(source);
			bean.setAssociationTarget(target);
			bean.setEntryStateId(entryStateId);
			bean.setId(associationTargetId);
			inserter.insert(INSERT_ENTITY_ASSNS_TO_ENTITY_SQL, bean);
			
			ibatisVersionsDao.insertEntryState(codingSchemeId, entryStateId, associationTargetId, "associationSource", null, target.getEntryState(), inserter);
			
			for(AssociationQualification qual : target.getAssociationQualification()){
				String qualId = this.createUniqueId();
				
				InsertAssociationQualificationOrUsageContextBean qualBean = new InsertAssociationQualificationOrUsageContextBean();
				qualBean.setAssociationTargetId(associationTargetId);
				qualBean.setId(qualId);
				qualBean.setPrefix(prefix);
				qualBean.setQualifierName(qual.getAssociationQualifier());
				qualBean.setQualifierValue(qual.getQualifierText().getContent());
				
				inserter.insert(
						INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, 
						qualBean);
			}
			
			for(String context : target.getUsageContext()){
				String contextId = this.createUniqueId();
				
				InsertAssociationQualificationOrUsageContextBean contextBean = new InsertAssociationQualificationOrUsageContextBean();
				contextBean.setAssociationTargetId(associationTargetId);
				contextBean.setId(contextId);
				contextBean.setPrefix(prefix);
				contextBean.setQualifierName(SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
				contextBean.setQualifierValue(context);
				
				inserter.insert(
						INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, 
						contextBean);
			}
		}
	}
	

	public void insertAssociationQualifier(String codingSchemeId,
			String associationInstanceId, AssociationQualification qualifier) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		String associationTargetId = this.getKeyForAssociationInstanceId(codingSchemeId, associationInstanceId);
		
		String qualId = this.createUniqueId();
		
		InsertAssociationQualificationOrUsageContextBean contextBean = new InsertAssociationQualificationOrUsageContextBean();
		contextBean.setAssociationTargetId(associationTargetId);
		contextBean.setId(qualId);
		contextBean.setPrefix(prefix);
		contextBean.setQualifierName(qualifier.getAssociationQualifier());
		contextBean.setQualifierValue(qualifier.getQualifierText().getContent());
		
		this.getSqlMapClientTemplate().insert(
				INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, 
				contextBean);
	}
	
	protected String getKeyForAssociationInstanceId(String codingSchemeId, String associationInstanceId){
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return (String) this.getSqlMapClientTemplate().queryForObject(
				
				GET_ASSOCIATION_INSTANCE_KEY_SQL, 
				new PrefixedParameterTuple(prefix, codingSchemeId, associationInstanceId));
	}
	
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
	}

	

	public IbatisVersionsDao getIbatisVersionsDao() {
		return ibatisVersionsDao;
	}

	public void setIbatisVersionsDao(IbatisVersionsDao ibatisVersionsDao) {
		this.ibatisVersionsDao = ibatisVersionsDao;
	}

	public void setCodingSchemeDao(CodingSchemeDao codingSchemeDao) {
		this.codingSchemeDao = codingSchemeDao;
	}

	public CodingSchemeDao getCodingSchemeDao() {
		return codingSchemeDao;
	}



}

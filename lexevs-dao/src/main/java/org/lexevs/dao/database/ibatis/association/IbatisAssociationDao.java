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
package org.lexevs.dao.database.ibatis.association;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.association.batch.AssociationSourceBatchInsertItem;
import org.lexevs.dao.database.access.association.batch.TransitiveClosureBatchInsertItem;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.association.parameter.InsertAssociationPredicateBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertAssociationQualificationOrUsageContextBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertAssociationSourceBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertOrUpdateAssociationEntityBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertRelationsBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertTransitiveClosureBean;
import org.lexevs.dao.database.ibatis.batch.IbatisBatchInserter;
import org.lexevs.dao.database.ibatis.batch.IbatisInserter;
import org.lexevs.dao.database.ibatis.batch.SqlMapExecutorBatchInserter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.versions.IbatisVersionsDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.util.Assert;

import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * The Class IbatisAssociationDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Cacheable(cacheName = "IbatisAssociationDao", cacheSize=100)
public class IbatisAssociationDao extends AbstractIbatisDao implements AssociationDao {

	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	public static String ASSOCIATION_NAMESPACE = "Association.";
	
	/** The INSER t_ relation s_ sql. */
	private static String INSERT_RELATIONS_SQL = ASSOCIATION_NAMESPACE + "insertRelations";
	
	/** The INSER t_ entit y_ assn s_ t o_ entit y_ sql. */
	private static String INSERT_ENTITY_ASSNS_TO_ENTITY_SQL = ASSOCIATION_NAMESPACE + "insertEntityAssnsToEntity";
	
	/** The INSER t_ associatio n_ qua l_ o r_ contex t_ sql. */
	private static String INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL = ASSOCIATION_NAMESPACE + "insertAssociationQualificationOrUsageContext";
	
	/** The INSER t_ associatio n_ predicat e_ sql. */
	private static String INSERT_ASSOCIATION_PREDICATE_SQL = ASSOCIATION_NAMESPACE + "insertAssociationPredicate";
	
	private static String INSERT_ASSOCIATIONENTITY_SQL = ASSOCIATION_NAMESPACE + "insertAssociationEntity";
	
	private static String UPDATE_ASSOCIATIONENTITY_FOR_ENTITY_ID_SQL = ASSOCIATION_NAMESPACE + "updateAssociationEntityForEntityId";
	
	/** The INSER t_ transitiv e_ closur e_ sql. */
	private static String INSERT_TRANSITIVE_CLOSURE_SQL = ASSOCIATION_NAMESPACE + "insertTransitiveClosure";
	
	/** The GE t_ associatio n_ instanc e_ ke y_ sql. */
	private static String GET_ASSOCIATION_INSTANCE_KEY_SQL = ASSOCIATION_NAMESPACE + "getAccociationInstanceKey";
	
	/** The GE t_ relation s_ ke y_ sql. */
	private static String GET_RELATIONS_KEY_SQL = ASSOCIATION_NAMESPACE + "getRelationsKey";
	
	/** The GE t_ associatio n_ predicat e_ ke y_ sql. */
	private static String GET_ASSOCIATION_PREDICATE_UID_SQL = ASSOCIATION_NAMESPACE + "getAssociationPredicateUid";
	
	private static String GET_ASSOCIATION_PREDICATE_NAME_FOR_ID_SQL = ASSOCIATION_NAMESPACE + "getAssociationPredicateNameForId";
	
	private static String GET_RELATIONS_IDS_FOR_CODINGSCHEME_ID_SQL = ASSOCIATION_NAMESPACE + "getRelationsKeysForCodingSchemeId";
	
	private static String GET_ASSOCIATION_PREDICATE_IDS_FOR_RELATIONS_ID_SQL = ASSOCIATION_NAMESPACE + "getAssociationPredicateKeysForRelationsId";
	
	private static String GET_ALL_TRIPLES_OF_CODINGSCHEME_SQL = ASSOCIATION_NAMESPACE + "getAllTriplesOfCodingScheme";
	
	private static String DELETE_ASSOCIATION_QUALS_FOR_CODINGSCHEME_ID_SQL = ASSOCIATION_NAMESPACE + "deleteAssocQualsByCodingSchemeId";
	
	private static String GET_ASSOCIATION_PREDICATE_FOR_ID_SQL = ASSOCIATION_NAMESPACE + "getAssociationPredicateForId";
	
	private static String GET_ASSOCIATION_PREDICATE_UID_FOR_DIRECTIONAL_NAME_SQL = ASSOCIATION_NAMESPACE + "getAssociationPredicateUidForDirectionalName";
	
	private static String GET_RELATIONS_FOR__ID_SQL = ASSOCIATION_NAMESPACE + "getRelationsForId";

	/** The ibatis versions dao. */
	private IbatisVersionsDao ibatisVersionsDao;
	
	/** The coding scheme dao. */
	private CodingSchemeDao codingSchemeDao;
	
	private EntityDao entityDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<Triple> getAllTriplesOfCodingScheme(
			String codingSchemeId,
			String associationPredicateId,
			int start, int pageSize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return this.getSqlMapClientTemplate().queryForList(
				GET_ALL_TRIPLES_OF_CODINGSCHEME_SQL, 
				new PrefixedParameterTuple(prefix, codingSchemeId, associationPredicateId), 
				start, 
				pageSize);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.association.AssociationDao#getAssociationPredicateId(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getAssociationPredicateUid(String codingSchemeUid, String relationsContainerName, String associationPredicateName) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		return
			(String) 
				this.getSqlMapClientTemplate().queryForObject(GET_ASSOCIATION_PREDICATE_UID_SQL, new PrefixedParameterTriple(
						prefix, codingSchemeUid, relationsContainerName, associationPredicateName));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAssociationPredicateUidsForDirectionalName(
			String codingSchemeId, String directionalName) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		PrefixedParameterTuple tuple = new PrefixedParameterTuple();
		tuple.setPrefix(prefix);
		tuple.setParam1(codingSchemeId);
		tuple.setParam2(directionalName);
		
		return this.getSqlMapClientTemplate()
			.queryForList(GET_ASSOCIATION_PREDICATE_UID_FOR_DIRECTIONAL_NAME_SQL, tuple);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.association.AssociationDao#getRelationsId(java.lang.String, java.lang.String)
	 */
	public String getRelationsId(String codingSchemeId, String relationsName) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return
			(String) 
				this.getSqlMapClientTemplate().queryForObject(GET_RELATIONS_KEY_SQL, new PrefixedParameterTuple(prefix, codingSchemeId, relationsName));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAssociationPredicateIdsForRelationsId(
			String codingSchemeId, String relationsId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return this.getSqlMapClientTemplate().queryForList(
				GET_ASSOCIATION_PREDICATE_IDS_FOR_RELATIONS_ID_SQL, 
				new PrefixedParameter(prefix, relationsId));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getRelationsUIdsForCodingSchemeUId(String codingSchemeId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return this.getSqlMapClientTemplate().queryForList(
				GET_RELATIONS_IDS_FOR_CODINGSCHEME_ID_SQL,
				new PrefixedParameter(prefix, codingSchemeId));
	}

	@Override
	public void deleteAssociationQualificationsByCodingSchemeId(String codingSchemeId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		this.getSqlMapClientTemplate().delete(
				DELETE_ASSOCIATION_QUALS_FOR_CODINGSCHEME_ID_SQL,
				new PrefixedParameter(prefix, codingSchemeId));
	}
	
	public String getAssociationPredicateNameForId(String codingSchemeId, String associationPredicateId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return
			(String) 
				this.getSqlMapClientTemplate().queryForObject(GET_ASSOCIATION_PREDICATE_NAME_FOR_ID_SQL, new PrefixedParameter(prefix, associationPredicateId));
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.association.AssociationDao#insertRelations(java.lang.String, org.LexGrid.relations.Relations)
	 */
	public String insertRelations(
			String codingSchemeId,
			Relations relations,
			boolean cascade) {
		String relationsId = this.createUniqueId();
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		this.doInsertRelations(
				prefix, 
				codingSchemeId, 
				relationsId, 
				relations, 
				cascade);
		return relationsId;
	}
	
	protected String doInsertRelations(
			String prefix,
			String codingSchemeId,
			String relationsId,
			Relations relations,
			boolean cascade) {
		String entryStateId = this.createUniqueId();
		
		InsertRelationsBean bean = new InsertRelationsBean();
		bean.setEntryStateUId(entryStateId);
		bean.setUId(relationsId);
		bean.setCodingSchemeUId(codingSchemeId);
		bean.setRelations(relations);
		bean.setPrefix(prefix);
		
		this.getSqlMapClientTemplate().insert(INSERT_RELATIONS_SQL, bean);
		
		if(cascade){
			for(AssociationPredicate predicate : relations.getAssociationPredicate()) {
				this.insertAssociationPredicate(
						codingSchemeId, 
						relationsId, 
						predicate,
						cascade);
			}
		}
		return relationsId;
	}
	
	public String insertAssociationEntity(
			String codingSchemeId,
			String entityId,
			AssociationEntity associationEntity,
			IbatisInserter inserter){
		
		String associationEntityId = this.createUniqueId();
		
		InsertOrUpdateAssociationEntityBean bean = new InsertOrUpdateAssociationEntityBean();
		bean.setPrefix(this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId));
		bean.setEntityUId(entityId);
		bean.setUId(associationEntityId);
		bean.setAssociationEntity(associationEntity);
		
		inserter.insert(INSERT_ASSOCIATIONENTITY_SQL, bean);	
		
		return associationEntityId;
	}
	
	public String insertAssociationEntity(
			String codingSchemeId,
			String entityId,
			AssociationEntity associationEntity) {
		return this.insertAssociationEntity(
				codingSchemeId, entityId, associationEntity, this.getNonBatchTemplateInserter());	
	}
	
	public void updateAssociationEntity(String codingSchemeId, String entityId,
			AssociationEntity entity) {
		InsertOrUpdateAssociationEntityBean bean = new InsertOrUpdateAssociationEntityBean();
		bean.setPrefix(this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId));
		bean.setEntityUId(entityId);
		bean.setAssociationEntity(entity);
		
		this.getSqlMapClientTemplate().update(
				UPDATE_ASSOCIATIONENTITY_FOR_ENTITY_ID_SQL, 
				bean, 1);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.association.AssociationDao#insertAssociationPredicate(java.lang.String, java.lang.String, org.LexGrid.relations.AssociationPredicate)
	 */
	public String insertAssociationPredicate(
			String codingSchemeId, 
			String relationId,
			AssociationPredicate associationPredicate,
			boolean cascade) {
		
		String id = this.createUniqueId();
		InsertAssociationPredicateBean bean = new InsertAssociationPredicateBean();
		bean.setPrefix(this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId));
		bean.setAssociationPredicate(associationPredicate);
		bean.setUId(id);
		bean.setRelationUId(relationId);
		
		this.getSqlMapClientTemplate().insert(INSERT_ASSOCIATION_PREDICATE_SQL, bean);
		
		if(cascade) {
			this.insertBatchAssociationSources(
					codingSchemeId, 
					id, 
					Arrays.asList(associationPredicate.getSource()));
		}
		return id;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.association.AssociationDao#insertBatchAssociationSources(java.lang.String, java.util.List)
	 */
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
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.association.AssociationDao#insertAssociationSource(java.lang.String, java.lang.String, org.LexGrid.relations.AssociationSource)
	 */
	public void insertAssociationSource(String codingSchemeId,
			String associationPredicateId, AssociationSource source){
		this.insertAssociationSource(codingSchemeId, associationPredicateId, source, 
				this.getNonBatchTemplateInserter());
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.association.AssociationDao#insertBatchAssociationSources(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public void insertBatchAssociationSources(final String codingSchemeId,
			final String associationPredicateId, final List<AssociationSource> batch) {
		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){
			
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {
				IbatisBatchInserter batchInserter = new SqlMapExecutorBatchInserter(executor);
				
				batchInserter.startBatch();
				
				for(AssociationSource source : batch) {
					insertAssociationSource(codingSchemeId, associationPredicateId, source, batchInserter);
				}
				
				batchInserter.executeBatch();
				
				return null;
			}	
		});
	}
	
	/**
	 * Insert into transitive closure.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param associationPredicateId the association predicate id
	 * @param sourceEntityCode the source entity code
	 * @param sourceEntityCodeNamespace the source entity code namespace
	 * @param targetEntityCode the target entity code
	 * @param targetEntityCodeNamespace the target entity code namespace
	 * @param executor the executor
	 * 
	 * @return the string
	 */
	public String insertIntoTransitiveClosure(
			String codingSchemeId,
			String associationPredicateId, 
			String sourceEntityCode,
			String sourceEntityCodeNamespace, 
			String targetEntityCode,
			String targetEntityCodeNamespace, 
			IbatisInserter executor) {
		
		String id = this.createUniqueId();
		
		InsertTransitiveClosureBean bean = new InsertTransitiveClosureBean();
		bean.setPrefix(this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId));
		bean.setUId(id);
		bean.setAssociationPredicateUId(associationPredicateId);
		bean.setSourceEntityCode(sourceEntityCode);
		bean.setSourceEntityCodeNamespace(sourceEntityCodeNamespace);
		bean.setTargetEntityCode(targetEntityCode);
		bean.setTargetEntityCodeNamespace(targetEntityCodeNamespace);
		
		this.getSqlMapClientTemplate().insert(INSERT_TRANSITIVE_CLOSURE_SQL, bean);
		
		return id;
		
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.association.AssociationDao#insertIntoTransitiveClosure(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String insertIntoTransitiveClosure(String codingSchemeId,
			String associationPredicateId, String sourceEntityCode,
			String sourceEntityCodeNamespace, String targetEntityCode,
			String targetEntityCodeNamespace) {
		
		return this.insertIntoTransitiveClosure(codingSchemeId,
				associationPredicateId, 
				sourceEntityCode,
				sourceEntityCodeNamespace, 
				targetEntityCode,
				targetEntityCodeNamespace,
				this.getNonBatchTemplateInserter());
	}
	
	/**
	 * Insert batch transitive closure.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param batch the batch
	 */
	public void insertBatchTransitiveClosure(final String codingSchemeId,
			final List<TransitiveClosureBatchInsertItem> batch) {
		
		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){

			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {
				IbatisBatchInserter batchInserter = new SqlMapExecutorBatchInserter(executor);
				
				batchInserter.startBatch();
				
				for(TransitiveClosureBatchInsertItem item : batch){
					insertIntoTransitiveClosure(codingSchemeId,
							item.getAssociationPredicateId(), 
							item.getSourceEntityCode(),
							item.getSourceEntityCodeNamespace(), 
							item.getTargetEntityCode(),
							item.getTargetEntityCodeNamespace());
				}

				batchInserter.executeBatch();

				return null;
			}
		});
	}


	/**
	 * Insert association source.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param associationPredicateId the association predicate id
	 * @param source the source
	 * @param inserter the inserter
	 */
	public void insertAssociationSource(String codingSchemeId,
			String associationPredicateId, AssociationSource source, IbatisInserter inserter) {
		Assert.isTrue(source.getTarget().length > 0, "Must Insert at least ONE AssociationTarget per AssociationSource.");
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		for(AssociationTarget target : source.getTarget()){
			String associationTargetId = this.createUniqueId();
			String entryStateId = this.createUniqueId();
			
			InsertAssociationSourceBean bean = new InsertAssociationSourceBean();
			bean.setPrefix(prefix);
			bean.setAssociationPredicateUId(associationPredicateId);
			bean.setAssociationSource(source);
			bean.setAssociationTarget(target);
			bean.setEntryStateUId(entryStateId);
			bean.setUId(associationTargetId);
			inserter.insert(INSERT_ENTITY_ASSNS_TO_ENTITY_SQL, bean);
			
			ibatisVersionsDao.insertEntryState(codingSchemeId, entryStateId, associationTargetId, "associationSource", null, target.getEntryState(), inserter);
			
			for(AssociationQualification qual : target.getAssociationQualification()){
				String qualId = this.createUniqueId();
				
				InsertAssociationQualificationOrUsageContextBean qualBean = new InsertAssociationQualificationOrUsageContextBean();
				qualBean.setAssociationTargetUId(associationTargetId);
				qualBean.setUId(qualId);
				qualBean.setPrefix(prefix);
				qualBean.setQualifierName(qual.getAssociationQualifier());
				
				if(qual.getQualifierText() != null) {
					qualBean.setQualifierValue(qual.getQualifierText().getContent());
				}
				
				inserter.insert(
						INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, 
						qualBean);
			}
			
			for(String context : target.getUsageContext()){
				String contextId = this.createUniqueId();
				
				InsertAssociationQualificationOrUsageContextBean contextBean = new InsertAssociationQualificationOrUsageContextBean();
				contextBean.setAssociationTargetUId(associationTargetId);
				contextBean.setUId(contextId);
				contextBean.setPrefix(prefix);
				contextBean.setQualifierName(SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
				contextBean.setQualifierValue(context);
				
				inserter.insert(
						INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, 
						contextBean);
			}
		}
	}
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.association.AssociationDao#insertAssociationQualifier(java.lang.String, java.lang.String, org.LexGrid.relations.AssociationQualification)
	 */
	public void insertAssociationQualifier(String codingSchemeId,
			String associationInstanceId, AssociationQualification qualifier) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		String associationTargetId = this.getKeyForAssociationInstanceId(codingSchemeId, associationInstanceId);
		
		String qualId = this.createUniqueId();
		
		InsertAssociationQualificationOrUsageContextBean contextBean = new InsertAssociationQualificationOrUsageContextBean();
		contextBean.setAssociationTargetUId(associationTargetId);
		contextBean.setUId(qualId);
		contextBean.setPrefix(prefix);
		contextBean.setQualifierName(qualifier.getAssociationQualifier());
		contextBean.setQualifierValue(qualifier.getQualifierText().getContent());
		
		this.getSqlMapClientTemplate().insert(
				INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, 
				contextBean);
	}
	
	
	
	@Override
	public AssociationPredicate getAssociationPredicateByUid(String codingSchemeId,
			String associationPredicateUid) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		PrefixedParameter bean = new PrefixedParameter();
		bean.setPrefix(prefix);
		bean.setParam1(associationPredicateUid);
		
		AssociationPredicate predicate = (AssociationPredicate) this.getSqlMapClientTemplate().queryForObject(
				GET_ASSOCIATION_PREDICATE_FOR_ID_SQL, 
				bean);
		
		return predicate;
	}

	@Override
	public Relations getRelationsByUid(String codingSchemeId,
			String relationsUid) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		PrefixedParameterTuple bean = new PrefixedParameterTuple();
		bean.setPrefix(prefix);
		bean.setParam1(codingSchemeId);
		bean.setParam2(relationsUid);
		
		Relations relations = (Relations) this.getSqlMapClientTemplate().queryForObject(GET_RELATIONS_FOR__ID_SQL, bean);
	
		for(String predicateId : this.getAssociationPredicateIdsForRelationsId(codingSchemeId, relationsUid)) {
			relations.addAssociationPredicate(getAssociationPredicateByUid(codingSchemeId, predicateId));
		}
		
		return relations;
	}

	/**
	 * Gets the key for association instance id.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param associationInstanceId the association instance id
	 * 
	 * @return the key for association instance id
	 */
	protected String getKeyForAssociationInstanceId(String codingSchemeId, String associationInstanceId){
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return (String) this.getSqlMapClientTemplate().queryForObject(
				
				GET_ASSOCIATION_INSTANCE_KEY_SQL, 
				new PrefixedParameterTuple(prefix, codingSchemeId, associationInstanceId));
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.AbstractBaseDao#doGetSupportedLgSchemaVersions()
	 */
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
	}

	

	/**
	 * Gets the ibatis versions dao.
	 * 
	 * @return the ibatis versions dao
	 */
	public IbatisVersionsDao getIbatisVersionsDao() {
		return ibatisVersionsDao;
	}

	/**
	 * Sets the ibatis versions dao.
	 * 
	 * @param ibatisVersionsDao the new ibatis versions dao
	 */
	public void setIbatisVersionsDao(IbatisVersionsDao ibatisVersionsDao) {
		this.ibatisVersionsDao = ibatisVersionsDao;
	}

	/**
	 * Sets the coding scheme dao.
	 * 
	 * @param codingSchemeDao the new coding scheme dao
	 */
	public void setCodingSchemeDao(CodingSchemeDao codingSchemeDao) {
		this.codingSchemeDao = codingSchemeDao;
	}

	/**
	 * Gets the coding scheme dao.
	 * 
	 * @return the coding scheme dao
	 */
	public CodingSchemeDao getCodingSchemeDao() {
		return codingSchemeDao;
	}

	public void setEntityDao(EntityDao entityDao) {
		this.entityDao = entityDao;
	}

	public EntityDao getEntityDao() {
		return entityDao;
	}
}

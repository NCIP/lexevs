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
package org.lexevs.dao.database.ibatis.entity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.association.IbatisAssociationDao;
import org.lexevs.dao.database.ibatis.batch.IbatisBatchInserter;
import org.lexevs.dao.database.ibatis.batch.IbatisInserter;
import org.lexevs.dao.database.ibatis.batch.SqlMapExecutorBatchInserter;
import org.lexevs.dao.database.ibatis.entity.parameter.InsertOrUpdateEntityBean;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.property.IbatisPropertyDao;
import org.lexevs.dao.database.ibatis.versions.IbatisVersionsDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.util.Assert;

import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * The Class IbatisEntityDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IbatisEntityDao extends AbstractIbatisDao implements EntityDao, InitializingBean {
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	/** The ENTIT y_ namespace. */
	public static String ENTITY_NAMESPACE = "Entity.";
	
	/** The INSER t_ entit y_ sql. */
	public static String INSERT_ENTITY_SQL = ENTITY_NAMESPACE + "insertEntity";
	
	/** The INSER t_ entit y_ typ e_ sql. */
	public static String INSERT_ENTITY_TYPE_SQL = ENTITY_NAMESPACE + "insertEntityType";
	
	/** The GE t_ entit y_ b y_ cod e_ an d_ namespac e_ sql. */
	public static String GET_ENTITY_BY_CODE_AND_NAMESPACE_SQL = ENTITY_NAMESPACE + "getEntityByCodeAndNamespace";
	
	public static String GET_ENTITY_BY_ID_AND_REVISION_ID_SQL = ENTITY_NAMESPACE + "getEntityByIdAndRevisionId";
	
	/** The GE t_ entit y_ coun t_ sql. */
	public static String GET_ENTITY_COUNT_SQL = ENTITY_NAMESPACE + "getEntityCount";
	
	/** The GE t_ entitie s_ o f_ codin g_ schem e_ sql. */
	public static String GET_ENTITIES_OF_CODING_SCHEME_SQL = ENTITY_NAMESPACE + "getAllEntitiesOfCodingScheme";
	
	public static String GET_ENTITY_ID_BY_CODE_AND_NAMESPACE = ENTITY_NAMESPACE + "getEntityIdByCodeAndNamespace";
	
	/** The ENTIT y_ cod e_ param. */
	public static String ENTITY_CODE_PARAM = SQLTableConstants.TBLCOL_ENTITYCODE;
	
	/** The ENTIT y_ cod e_ namespac e_ param. */
	public static String ENTITY_CODE_NAMESPACE_PARAM = SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE;
	
	public static String GET_ENTITY_BY_ID_SQL = ENTITY_NAMESPACE + "getEntityById";
	
	public static String UPDATE_ENTITY_BY_ID_SQL = ENTITY_NAMESPACE + "updateEntityById";
	
	/** The ENTITY. */
	public static String ENTITY = "entity";
	
	/** The ENTIT y_ i d_ param. */
	public static String ENTITY_ID_PARAM = "entityId";
	
	/** The ibatis versions dao. */
	private IbatisVersionsDao ibatisVersionsDao;

	/** The ibatis property dao. */
	private IbatisPropertyDao ibatisPropertyDao;
	
	private IbatisAssociationDao ibatisAssociationDao;

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.entity.EntityDao#getEntityByCodeAndNamespace(java.lang.String, java.lang.String, java.lang.String)
	 */
	public Entity getEntityByCodeAndNamespace(String codingSchemeId, String entityCode, String entityCodeNamespace){
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		String entityId = this.getEntityId(codingSchemeId, entityCode, entityCodeNamespace);

		return doGetEntity(prefix, codingSchemeId, entityId);
	}
	
	public Entity getEntityById(String codingSchemeId, String entityId){
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return doGetEntity(prefix, codingSchemeId, entityId);
	}
	
	@Override
	public Entity getHistoryEntityByRevision(String codingSchemeId, String entityId, String revisionId) {
		String prefix = this.getPrefixResolver().resolveHistoryPrefix();
		String entityTypeTablePrefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		PrefixedParameterTuple tuple = 
			new PrefixedParameterTuple(prefix, entityId, revisionId);
		
		tuple.setEntityTypeTablePrefix(entityTypeTablePrefix);
		
		return (Entity) this.getSqlMapClientTemplate().queryForObject(GET_ENTITY_BY_ID_AND_REVISION_ID_SQL, 
				tuple);
	}
	
	protected Entity doGetEntity(String prefix, String codingSchemeId, String entityId) {
		Entity entity = (Entity) this.getSqlMapClientTemplate().queryForObject(GET_ENTITY_BY_ID_SQL, 
				new PrefixedParameterTuple(prefix, entityId, codingSchemeId));
			
		entity.addAnyProperties(
				ibatisPropertyDao.getAllPropertiesOfParent(codingSchemeId, entityId, PropertyType.ENTITY));
		
		return entity;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.entity.EntityDao#getEntityCount(java.lang.String)
	 */
	@Override
	public int getEntityCount(String codingSchemeId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return (Integer) 
			this.getSqlMapClientTemplate().queryForObject(GET_ENTITY_COUNT_SQL, new PrefixedParameter(prefix, codingSchemeId));
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.entity.EntityDao#updateEntity(java.lang.String, org.LexGrid.concepts.Entity)
	 */
	public void updateEntity(String codingSchemeId,
			Entity entity) {
		Assert.hasText(entity.getEntityCode(), "An Entity Code is required to be populated to Updated an Entity.");
		Assert.hasText(entity.getEntityCodeNamespace(), "An Entity Code Namespace is required to be populated to Updated an Entity.");
		
		String entityId = this.getEntityId(codingSchemeId, entity.getEntityCode(), entity.getEntityCodeNamespace());
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		InsertOrUpdateEntityBean bean = new InsertOrUpdateEntityBean();
		bean.setPrefix(prefix);
		bean.setEntity(entity);
		bean.setCodingSchemeId(codingSchemeId);
		bean.setId(entityId);
		
		this.getSqlMapClientTemplate().update(UPDATE_ENTITY_BY_ID_SQL, bean);
	}
	
	@Override
	public String insertEntity(String codingSchemeId, Entity entity,
			boolean cascade) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return this.doInsertEntity(
				prefix, 
				codingSchemeId, 
				entity, 
				this.getNonBatchTemplateInserter(), 
				cascade);
	}
	
	@Override
	public String insertEntity(String codingSchemeId, AssociationEntity entity,
			boolean cascade) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		String entityId = this.doInsertEntity(
				prefix, 
				codingSchemeId, 
				entity, 
				this.getNonBatchTemplateInserter(), 
				cascade);
		
		ibatisAssociationDao.insertAssociationEntity(codingSchemeId, entityId, entity);
		
		return entityId;
	}
	
	/**
	 * Insert entity.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param entity the entity
	 * @param inserter the inserter
	 * 
	 * @return the string
	 */
	protected String doInsertEntity(
			String prefix, 
			String codingSchemeId, 
			Entity entity, 
			IbatisInserter inserter,
			boolean cascade) {
		Map<String,String> propertyIdToGuidMap = new HashMap<String,String>();
		
		String entityId = this.createUniqueId();
		String entryStateId = this.createUniqueId();
		
		this.ibatisVersionsDao.insertEntryState(
				entryStateId, entityId, "Entity", null, entity.getEntryState());
		
		inserter.insert(INSERT_ENTITY_SQL, 
				buildInsertEntityParamaterBean(
						prefix,
						prefix,
						codingSchemeId, entityId, entryStateId, entity));

		for(String entityType : entity.getEntityType()){
			inserter.insert(INSERT_ENTITY_TYPE_SQL, 
					new PrefixedParameterTuple(prefix, entityId, entityType));
		}
			
		if(cascade) {
			for(Property prop : entity.getAllProperties()) {
				String propertyId = this.createUniqueId();
				ibatisPropertyDao.doInsertProperty(
						prefix, 
						entityId, 
						propertyId, 
						PropertyType.ENTITY, 
						prop, 
						inserter);
				propertyIdToGuidMap.put(prop.getPropertyId(), propertyId);
			}
		}
		
		for(PropertyLink link : entity.getPropertyLink()) {
			String propertyLinkId = this.createUniqueId();
			String sourcePropertyId = propertyIdToGuidMap.get(link.getSourceProperty());
			String targetPropertyId = propertyIdToGuidMap.get(link.getTargetProperty());
			
			this.ibatisPropertyDao.doInsertPropertyLink(prefix, entityId, 
					propertyLinkId, link.getPropertyLink(), 
					sourcePropertyId, targetPropertyId, inserter);
		}

		return entityId;
	}
	
	protected String doInsertHistoryEntity( 
			String codingSchemeId, 
			String entityId,
			Entity entity, 
			IbatisInserter inserter,
			boolean cascade) {

		String prefix = this.getPrefixResolver().resolveHistoryPrefix();
		String entityTypeTablePrefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);

		String entryStateId = this.createUniqueId();
		
		this.ibatisVersionsDao.insertEntryState(
				entryStateId, entityId, "Entity", null, entity.getEntryState());
		
		inserter.insert(INSERT_ENTITY_SQL, 
				buildInsertEntityParamaterBean(
						prefix,
						entityTypeTablePrefix,
						codingSchemeId, entityId, entryStateId, entity));
		
		for(Property prop : entity.getAllProperties()) {
			String propertyId = this.createUniqueId();
			this.ibatisPropertyDao.doInsertProperty(
					prefix, 
					entityId, 
					propertyId, 
					PropertyType.ENTITY, 
					prop, 
					inserter);
		}

		return entryStateId;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.entity.EntityDao#insertHistoryEntity(java.lang.String, org.LexGrid.concepts.Entity)
	 */
	public String insertHistoryEntity(String codingSchemeId, String entityId, Entity entity) {
		return this.doInsertHistoryEntity(
				codingSchemeId, 
				entityId, 
				entity, 
				this.getNonBatchTemplateInserter(),
				true);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.entity.EntityDao#getAllEntitiesOfCodingScheme(java.lang.String, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<? extends Entity> getAllEntitiesOfCodingScheme(String codingSchemeId, int start, int pageSize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		if(pageSize < 0) {
			pageSize = Integer.MAX_VALUE;
		}
		
		List<Entity> entities = 
			this.getSqlMapClientTemplate().queryForList(GET_ENTITIES_OF_CODING_SCHEME_SQL, 
					new PrefixedParameter(prefix, codingSchemeId),
					start, pageSize);
		
		for(Entity entity : entities) {
			entity.addAnyProperties(
					this.ibatisPropertyDao.getAllPropertiesOfParent(
							codingSchemeId, 
							this.getEntityId(codingSchemeId, entity.getEntityCode(), entity.getEntityCodeNamespace()), 
							PropertyType.ENTITY));
		}

		return entities;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.entity.EntityDao#insertBatchEntities(java.lang.String, java.util.List)
	 */
	public void insertBatchEntities(
			final String codingSchemeId, 
			final List<? extends Entity> entities,
			final boolean cascade) {
		final String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);

		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){

			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {
				IbatisBatchInserter batchInserter = new SqlMapExecutorBatchInserter(executor);
				
				batchInserter.startBatch();
				
				for(Entity entity : entities){
					doInsertEntity(
							prefix, 
							codingSchemeId, 
							entity, 
							batchInserter,
							cascade);
				}
				
				batchInserter.executeBatch();

				return null;
			}
		});
	}
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.entity.EntityDao#getEntityId(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getEntityId(String codingSchemeId, String entityCode,
			String entityCodeNamespace) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return (String) this.getSqlMapClientTemplate().queryForObject(
				GET_ENTITY_ID_BY_CODE_AND_NAMESPACE, 
					new PrefixedParameterTriple(prefix, codingSchemeId, entityCode, entityCodeNamespace));
	}
	
	/**
	 * Builds the insert entity paramater bean.
	 * 
	 * @param prefix the prefix
	 * @param codingSchemeId the coding scheme id
	 * @param entityId the entity id
	 * @param entryStateId the entry state id
	 * @param entity the entity
	 * 
	 * @return the insert entity bean
	 */
	protected InsertOrUpdateEntityBean buildInsertEntityParamaterBean(
			String prefix, 
			String entityTypeTablePrefix,
			String codingSchemeId, String entityId, String entryStateId, Entity entity){
		InsertOrUpdateEntityBean bean = new InsertOrUpdateEntityBean();
		bean.setPrefix(prefix);
		bean.setEntityTypeTablePrefix(entityTypeTablePrefix);
		bean.setCodingSchemeId(codingSchemeId);
		bean.setId(entityId);
		bean.setEntryStateId(entryStateId);
		bean.setEntity(entity);
		
		return bean;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.AbstractBaseDao#doGetSupportedLgSchemaVersions()
	 */
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
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
	 * Gets the ibatis versions dao.
	 * 
	 * @return the ibatis versions dao
	 */
	public IbatisVersionsDao getIbatisVersionsDao() {
		return ibatisVersionsDao;
	}
	
	/**
	 * Gets the ibatis property dao.
	 * 
	 * @return the ibatis property dao
	 */
	public IbatisPropertyDao getIbatisPropertyDao() {
		return ibatisPropertyDao;
	}

	/**
	 * Sets the ibatis property dao.
	 * 
	 * @param ibatisPropertyDao the new ibatis property dao
	 */
	public void setIbatisPropertyDao(IbatisPropertyDao ibatisPropertyDao) {
		this.ibatisPropertyDao = ibatisPropertyDao;
	}

	public void setIbatisAssociationDao(IbatisAssociationDao ibatisAssociationDao) {
		this.ibatisAssociationDao = ibatisAssociationDao;
	}

	public IbatisAssociationDao getIbatisAssociationDao() {
		return ibatisAssociationDao;
	}

}

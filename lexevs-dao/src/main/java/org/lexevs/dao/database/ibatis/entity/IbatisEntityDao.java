package org.lexevs.dao.database.ibatis.entity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.access.property.batch.PropertyBatchInsertItem;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.batch.IbatisBatchInserter;
import org.lexevs.dao.database.ibatis.batch.IbatisInserter;
import org.lexevs.dao.database.ibatis.batch.SqlMapExecutorBatchInserter;
import org.lexevs.dao.database.ibatis.entity.parameter.InsertEntityBean;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.property.IbatisPropertyDao;
import org.lexevs.dao.database.ibatis.versions.IbatisVersionsDao;
import org.lexevs.dao.database.lazyload.LazyLoadableEntity;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;

public class IbatisEntityDao extends AbstractIbatisDao implements EntityDao, InitializingBean {
	
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	public static String ENTITY_NAMESPACE = "Entity.";
	public static String INSERT_ENTITY_SQL = ENTITY_NAMESPACE + "insertEntity";
	public static String INSERT_ENTITY_TYPE_SQL = ENTITY_NAMESPACE + "insertEntityType";
	public static String GET_ENTITY_BY_CODE_AND_NAMESPACE_SQL = ENTITY_NAMESPACE + "getEntityByCodeAndNamespace";
	public static String GET_ENTITY_COUNT_SQL = ENTITY_NAMESPACE + "getEntityCount";
	public static String GET_ENTITIES_OF_CODING_SCHEME_SQL = ENTITY_NAMESPACE + "getAllEntitiesOfCodingScheme";
	
	public static String ENTITY_CODE_PARAM = SQLTableConstants.TBLCOL_ENTITYCODE;
	public static String ENTITY_CODE_NAMESPACE_PARAM = SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE;
	public static String ENTITY = "entity";
	public static String ENTITY_ID_PARAM = "entityId";
	
	private IbatisVersionsDao ibatisVersionsDao;

	private IbatisPropertyDao ibatisPropertyDao;

	public Entity getEntityByCodeAndNamespace(String codingSchemeId, String entityCode, String entityCodeNamespace){
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		LazyLoadableEntity entity = (LazyLoadableEntity) this.getSqlMapClientTemplate().queryForObject(GET_ENTITY_BY_CODE_AND_NAMESPACE_SQL, 
			new PrefixedParameterTuple(prefix, entityCode, entityCodeNamespace));
		entity.setPropertyDao(ibatisPropertyDao);
		
		return entity;
	}
	
	@Override
	public int getEntityCount(String codingSchemeId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return (Integer) 
			this.getSqlMapClientTemplate().queryForObject(GET_ENTITY_COUNT_SQL, new PrefixedParameter(prefix, codingSchemeId));
	}

	public void updateEntity(String codingSchemeId,
			Entity entity) {
		// TODO Auto-generated method stub
		
	}
	
	public String insertEntity(String codingSchemeId, Entity entity) {
		return this.insertEntity(codingSchemeId, entity, this.getNonBatchTemplateInserter());
	}
	
	public String insertEntity(String codingSchemeId, Entity entity, IbatisInserter inserter) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		String entityId = this.createUniqueId();
		String entryStateId = this.createUniqueId();
		this.ibatisVersionsDao.insertEntryState(
				codingSchemeId,
				entryStateId, entityId, "Entity", null, entity.getEntryState());
		
		inserter.insert(INSERT_ENTITY_SQL, 
				buildInsertEntityParamaterBean(
						prefix,
						codingSchemeId, entityId, entryStateId, entity));

		for(String entityType : entity.getEntityType()){
			inserter.insert(INSERT_ENTITY_TYPE_SQL, 
					new PrefixedParameterTuple(prefix, entityId, entityType));
		}
		
		for(Property prop : entity.getAllProperties()) {
			ibatisPropertyDao.insertProperty(codingSchemeId, entityId, PropertyType.ENTITY, prop, inserter);
		}

		return entityId;
	}

	public String insertHistoryEntity(String codingSchemeId, Entity entity) {
		String entityId = this.createUniqueId();
		String entryStateId = this.createUniqueId();
		
		this.getSqlMapClientTemplate().insert(INSERT_ENTITY_SQL, 
				buildInsertEntityParamaterBean(
						this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId),
						codingSchemeId, entryStateId, entryStateId, entity));

		return entityId;
	}
	
	@SuppressWarnings("unchecked")
	public List<? extends Entity> getAllEntitiesOfCodingScheme(String codingSchemeId, int start, int pageSize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		if(pageSize < 0) {
			pageSize = Integer.MAX_VALUE;
		}
		
		List<LazyLoadableEntity> entities = 
			this.getSqlMapClientTemplate().queryForList(GET_ENTITIES_OF_CODING_SCHEME_SQL, 
					new PrefixedParameter(prefix, codingSchemeId),
					start, pageSize);
		
		for(LazyLoadableEntity entity : entities) {
			entity.setPropertyDao(this.ibatisPropertyDao);
		}
		
		return entities;
	}
	
	@SuppressWarnings("unchecked")
	public void insertBatchEntities(final String codingSchemeId, final List<? extends Entity> entities) {

		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){

			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {
				IbatisBatchInserter batchInserter = new SqlMapExecutorBatchInserter(executor);
				
				batchInserter.startBatch();
				
				for(Entity entity : entities){
					String id = insertEntity(codingSchemeId, entity, batchInserter);
					
					List<PropertyBatchInsertItem> props = new ArrayList<PropertyBatchInsertItem>();
					
					for(Property prop : entity.getAllProperties()){
						PropertyBatchInsertItem item = new PropertyBatchInsertItem();
						item.setParentId(id);
						item.setProperty(prop);
						props.add(item);
					}
					
					ibatisPropertyDao.insertBatchProperties(codingSchemeId, PropertyType.ENTITY, props);
				}
				
				batchInserter.executeBatch();

				return null;
			}
		});
	}
	

	public String getEntityId(String codingSchemeId, String entityCode,
			String entityCodeNamespace) {
		return null;
		
	}
	
	protected InsertEntityBean buildInsertEntityParamaterBean(String prefix, String codingSchemeId, String entityId, String entryStateId, Entity entity){
		InsertEntityBean bean = new InsertEntityBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeId(codingSchemeId);
		bean.setId(entityId);
		bean.setEntryStateId(entryStateId);
		bean.setEntity(entity);
		
		return bean;
	}
	
	protected InsertEntityBean buildEntityCodeNamespaceParamaterMap(String entityCode, String entityCodeNamespace){
		return null;
	}
	
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
	}

	public void setIbatisVersionsDao(IbatisVersionsDao ibatisVersionsDao) {
		this.ibatisVersionsDao = ibatisVersionsDao;
	}

	public IbatisVersionsDao getIbatisVersionsDao() {
		return ibatisVersionsDao;
	}
	
	public IbatisPropertyDao getIbatisPropertyDao() {
		return ibatisPropertyDao;
	}

	public void setIbatisPropertyDao(IbatisPropertyDao ibatisPropertyDao) {
		this.ibatisPropertyDao = ibatisPropertyDao;
	}
}

package org.lexevs.dao.database.ibatis.entity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.batch.IbatisBatchInserter;
import org.lexevs.dao.database.ibatis.batch.IbatisInserter;
import org.lexevs.dao.database.ibatis.batch.SqlMapExecutorBatchInserter;
import org.lexevs.dao.database.ibatis.entity.parameter.InsertEntityBean;
import org.lexevs.dao.database.ibatis.entity.parameter.InsertEntityTypeBean;
import org.lexevs.dao.database.ibatis.property.IbatisPropertyDao;
import org.lexevs.dao.database.ibatis.versions.IbatisVersionsDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;

public class IbatisEntityDao extends AbstractIbatisDao implements EntityDao, InitializingBean {
	
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	public static String INSERT_ENTITY_SQL = "insertEntity";
	public static String INSERT_ENTITY_TYPE_SQL = "insertEntityType";
	public static String GET_ENTITY_BY_CODE_AND_NAMESPACE_SQL = "insertEntity";
	public static String ENTITY_CODE_PARAM = SQLTableConstants.TBLCOL_ENTITYCODE;
	public static String ENTITY_CODE_NAMESPACE_PARAM = SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE;
	public static String ENTITY = "entity";
	public static String ENTITY_ID_PARAM = "entityId";
	
	private IbatisVersionsDao ibatisVersionsDao;
	public IbatisPropertyDao getIbatisPropertyDao() {
		return ibatisPropertyDao;
	}

	public void setIbatisPropertyDao(IbatisPropertyDao ibatisPropertyDao) {
		this.ibatisPropertyDao = ibatisPropertyDao;
	}

	private IbatisPropertyDao ibatisPropertyDao;

	public Entity getEntity(String entityCode, String entityCodeNamespace){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put(ENTITY_CODE_PARAM, entityCode);
		paramMap.put(ENTITY_CODE_NAMESPACE_PARAM, entityCodeNamespace);
		
		return (Entity) this.getSqlMapClientTemplate().queryForObject(GET_ENTITY_BY_CODE_AND_NAMESPACE_SQL, paramMap);
	}


	public void updateEntity(String codingSchemeName, String version,
			Entity entity) {
		// TODO Auto-generated method stub
		
	}
	
	public String insertEntity(String codingSchemeId, Entity entity) {
		return this.insertEntity(codingSchemeId, entity, this.getNonBatchTemplateInserter());
	}
	
	public String insertEntity(String codingSchemeId, Entity entity, IbatisInserter inserter) {
		String entityId = this.createUniqueId();
		String entryStateId = this.createUniqueId();
		this.ibatisVersionsDao.insertEntryState(
				codingSchemeId,
				entryStateId, entityId, "Entity", null, entity.getEntryState());
		
		inserter.insert(INSERT_ENTITY_SQL, 
				buildInsertEntityParamaterBean(codingSchemeId, entityId, entryStateId, entity));

		for(String entityType : entity.getEntityType()){
			inserter.insert(INSERT_ENTITY_TYPE_SQL, 
					new InsertEntityTypeBean(entityId, entityType));
		}

		return entityId;
	}

	public String insertHistoryEntity(String codingSchemeId, Entity entity) {
		String entityId = this.createUniqueId();
		String entryStateId = this.createUniqueId();
		
		this.getSqlMapClientTemplate().insert(INSERT_ENTITY_SQL, 
				buildInsertEntityParamaterBean(codingSchemeId, entryStateId, entryStateId, entity));

		return entityId;
	}
	
	@SuppressWarnings("unchecked")
	public void insertBatchEntities(final String codingSchemeId, final List<? extends Entity> entities) {

		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){

			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {
				IbatisBatchInserter batchInserter = new SqlMapExecutorBatchInserter(executor);
				
				batchInserter.startBatch();
				
				for(Entity entity : entities){
					insertEntity(codingSchemeId, entity, batchInserter);
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
	
	protected InsertEntityBean buildInsertEntityParamaterBean(String codingSchemeId, String entityId, String entryStateId, Entity entity){
		InsertEntityBean bean = new InsertEntityBean();
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
		return DaoUtility.createList(supportedDatebaseVersion, LexGridSchemaVersion.class);
	}

	public void setIbatisVersionsDao(IbatisVersionsDao ibatisVersionsDao) {
		this.ibatisVersionsDao = ibatisVersionsDao;
	}

	public IbatisVersionsDao getIbatisVersionsDao() {
		return ibatisVersionsDao;
	}

}

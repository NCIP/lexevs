package org.lexevs.dao.database.ibatis.entity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.LexGrid.concepts.Entity;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.entity.parameter.InsertEntityBean;
import org.lexevs.dao.database.ibatis.entity.parameter.InsertEntityTypeBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapExecutor;

public class IbatisEntityDao extends AbstractIbatisDao implements EntityDao {
	
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	public static String INSERT_ENTITY_SQL = "insertEntity";
	public static String INSERT_ENTITY_TYPE_SQL = "insertEntityType";
	public static String GET_ENTITY_BY_CODE_AND_NAMESPACE_SQL = "insertEntity";
	public static String ENTITY_CODE_PARAM = SQLTableConstants.TBLCOL_ENTITYCODE;
	public static String ENTITY_CODE_NAMESPACE_PARAM = SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE;
	public static String ENTITY = "entity";
	public static String ENTITY_ID_PARAM = "entityId";
	
	private VersionsDao versionsDao;


	public Entity getEntity(String entityCode, String entityCodeNamespace){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put(ENTITY_CODE_PARAM, entityCode);
		paramMap.put(ENTITY_CODE_NAMESPACE_PARAM, entityCodeNamespace);
		
		return (Entity) this.getSqlMapClientTemplate().queryForObject(GET_ENTITY_BY_CODE_AND_NAMESPACE_SQL, paramMap);
	}
	
	public String insertEntity(String codingSchemeName, String version,
			Entity entity){
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("codingSchemeName", codingSchemeName);
		paramMap.put("codingSchemeVersion", version);
		String codingSchemeId = (String) this.getSqlMapClientTemplate().queryForObject("getCodingSchemeId", paramMap);
		
		return this.insertEntity(codingSchemeId, entity);
	}


	public void updateEntity(String codingSchemeName, String version,
			Entity entity) {
		// TODO Auto-generated method stub
		
	}
	
	public String insertEntity(String codingSchemeId, Entity entity) {
		String entityId = this.createUniqueId();
		String entryStateId = this.createUniqueId();
		this.versionsDao.insertEntryState(
				codingSchemeId,
				entryStateId, entityId, "Entity", null, entity.getEntryState());
		
		this.getSqlMapClientTemplate().insert(INSERT_ENTITY_SQL, 
				buildInsertEntityParamaterBean(codingSchemeId, entityId, entryStateId, entity));
		
		for(String entityType : entity.getEntityType()){
		this.getSqlMapClientTemplate().insert(INSERT_ENTITY_TYPE_SQL, 
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
	public void insertEntity(final String codingSchemeId, final List<Entity> entities) {

		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){

			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {
				
				executor.startBatch();
				for(Entity entity : entities){
					String entityId = createUniqueId();
					String entryStateId = createUniqueId();
					versionsDao.insertEntryState(
							codingSchemeId,
							entryStateId, entityId, "Entity", null, entity.getEntryState());
					executor.insert(INSERT_ENTITY_SQL, buildInsertEntityParamaterBean(codingSchemeId, entryStateId, entryStateId, entity));
					
				}
				 executor.executeBatch();
				 return null;
			}
		});
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

	public void setVersionsDao(VersionsDao versionsDao) {
		this.versionsDao = versionsDao;
	}

	public VersionsDao getVersionsDao() {
		return versionsDao;
	}
}


package org.lexevs.dao.database.ibatis.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.ibatis.session.RowBounds;
import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.association.IbatisAssociationDao;
import org.lexevs.dao.database.ibatis.codingscheme.IbatisCodingSchemeDao;
import org.lexevs.dao.database.ibatis.entity.parameter.InsertOrUpdateEntityBean;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterCollection;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.parameter.SequentialMappedParameterBean;
import org.lexevs.dao.database.ibatis.property.IbatisPropertyDao;
import org.lexevs.dao.database.ibatis.versions.IbatisVersionsDao;
import org.lexevs.dao.database.inserter.Inserter;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;


/**
 * The Class IbatisEntityDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Cacheable(cacheName = "IbatisEntityDaoCache")
public class IbatisEntityDao extends AbstractIbatisDao implements EntityDao {
	
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
	
	public static String GET_ASSOCIATION_ENTITY_BY_CODE_AND_NAMESPACE_SQL = ENTITY_NAMESPACE + "getAssociationEntityByCodeAndNamespace";
	
	public static String GET_RESOLVED_CODED_NODE_REFERENCE_BY_CODE_AND_NAMESPACE_SQL = ENTITY_NAMESPACE + "getResolvedCodedNodeReferenceByCodeAndNamespace";
	
	public static String GET_ENTITY_BY_ID_AND_REVISION_ID_SQL = ENTITY_NAMESPACE + "getEntityByIdAndRevisionId";
	
	/** The GE t_ entit y_ coun t_ sql. */
	public static String GET_ENTITY_COUNT_SQL = ENTITY_NAMESPACE + "getEntityCount";
	
	/** The GE t_ entitie s_ o f_ codin g_ schem e_ sql. */
	public static String GET_ENTITY_UIDS_OF_CODING_SCHEME_SQL = ENTITY_NAMESPACE + "getAllEntityUidsOfCodingScheme";
	
	public static String GET_ENTITY_UID_BY_CODE_AND_NAMESPACE = ENTITY_NAMESPACE + "getEntityUidByCodeAndNamespace";
	
	/** The ENTIT y_ cod e_ param. */
	public static String ENTITY_CODE_PARAM = SQLTableConstants.TBLCOL_ENTITYCODE;
	
	/** The ENTIT y_ cod e_ namespac e_ param. */
	public static String ENTITY_CODE_NAMESPACE_PARAM = SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE;
	
	public static String GET_ENTITY_BY_ID_SQL = ENTITY_NAMESPACE + "getEntityById";
	
	public static String GET_ENTITIES_BY_UIDS_SQL = ENTITY_NAMESPACE + "getEntitiesByUids";
	
	public static String UPDATE_ENTITY_BY_UID_SQL = ENTITY_NAMESPACE + "updateEntityByUId";
	
	public static String GET_PROPERTY_LINKS_BY_ENTITY_UIDS_SQL = ENTITY_NAMESPACE + "getPropertyLinksByEntityUids";
	
	public static String GET_ENTITY_ATTRIBUTES_BY_UID_SQL = ENTITY_NAMESPACE + "getEntityAttributesByEntityUId";
	
	/** update codingScheme versionableAttrib. */
	private static String UPDATE_ENTITY_VER_ATTRIB_BY_ID_SQL = ENTITY_NAMESPACE + "updateEntityVerAttribByUId";
	
	private static String DELETE_ENTIYT_BY_UID_SQL = ENTITY_NAMESPACE + "deleteEntityByUId";
	
	private static String GET_ENTITY_LATEST_REVISION_ID_BY_UID = ENTITY_NAMESPACE + "getEntityLatestRevisionIdByUId";
	
	private static String CHECK_ENTITY_USAGE = ENTITY_NAMESPACE + "checkEntityUsage";
	
	private static String GET_ENTRYSTATE_UID_BY_ENTITY_UID_SQL = ENTITY_NAMESPACE + "getEntryStateUIdByEntityUId";
	
	private static String UPDATE_ENTITY_ENTRYSTATE_UID = ENTITY_NAMESPACE + "updateEntityEntryStateUId";
	
	public static String GET_ENTRY_STATE = VERSIONS_NAMESPACE + "getEntryState";
	
	private static String GET_ENTITY_DESCRIPTION_SQL = ENTITY_NAMESPACE + "getEntityDescription";
	
	private static String GET_ENTITY_DESCRIPTION_STRING = ENTITY_NAMESPACE + "getEntityDescriptionAsString";
	
	private static String GET_DISTINCT_ENTITY_NAMESPACES_SQL = ENTITY_NAMESPACE + "getDistinctNamespaces";
	
	/** The ENTITY. */
	public static String ENTITY = "entity";
	
	/** The ENTIT y_ i d_ param. */
	public static String ENTITY_ID_PARAM = "entityId";
	
	/** The ibatis versions dao. */
	private IbatisVersionsDao ibatisVersionsDao;

	/** The ibatis property dao. */
	private IbatisPropertyDao ibatisPropertyDao;
	
	private IbatisAssociationDao ibatisAssociationDao;
	
	private IbatisCodingSchemeDao ibatisCodingSchemeDao = null; 
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.entity.EntityDao#getEntityByCodeAndNamespace(java.lang.String, java.lang.String, java.lang.String)
	 */
	@CacheMethod
	public Entity getEntityByCodeAndNamespace(String codingSchemeUid, String entityCode, String entityCodeNamespace){
		return this.getEntityByCodeAndNamespace(
				codingSchemeUid, 
				entityCode, 
				entityCodeNamespace, 
				null, 
				null);		
	}

	@Override
	@CacheMethod
	public Entity getEntityByCodeAndNamespace(
			String codingSchemeUid,
			String entityCode, 
			String entityCodeNamespace,
			List<String> propertyNames, 
			List<String> propertyTypes) {
		
		String entityId = this.getEntityUId(codingSchemeUid, entityCode, entityCodeNamespace);
		
		return this.getEntityByUId(codingSchemeUid, entityId, propertyNames, propertyTypes);
	}

	@Override
	@CacheMethod
	public List<Entity> getEntities(
			String codingSchemeId,
			List<String> propertyNames, 
			List<String> propertyTypes,
			List<String> entityUids) {
		if(CollectionUtils.isEmpty(entityUids)) {
			return new ArrayList<Entity>();
		}
		
		return new ArrayList<Entity>(
				this.getEntitiesWithUidMap(codingSchemeId, propertyNames, propertyTypes, entityUids).values());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public Map<String,Entity> getEntitiesWithUidMap(String codingSchemeId,
			List<String> propertyNames, List<String> propertyTypes,
			List<String> entityUids) {
		if(CollectionUtils.isEmpty(entityUids)) {
			return new HashMap<String,Entity>();
		}
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		Map<String,Entity> entities = (Map<String,Entity>) this.getSqlSessionTemplate().<String,Entity>selectMap(GET_ENTITIES_BY_UIDS_SQL, 
				new PrefixedParameterCollection(prefix, codingSchemeId, entityUids), "id");
		
		for(Property prop : this.ibatisPropertyDao.getPropertiesOfParents(
				codingSchemeId, 
				propertyNames, 
				propertyTypes, 
				entityUids)){
			Entity entity = entities.get(prop.getParent());
			if(entity != null) {
				entity.addAnyProperty(prop);
			}
		}

		return entities;
	}

	@Override
	@CacheMethod
	public List<Entity> getEntities(String codingSchemeId,
			List<String> entityUids) {
		return this.getEntities(codingSchemeId, null, null, entityUids);
	}

	@Override
	@CacheMethod
	public AssociationEntity getAssociationEntityByCodeAndNamespace(String codingSchemeId, String entityCode, String entityCodeNamespace){
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		String entityId = this.getEntityUId(codingSchemeId, entityCode, entityCodeNamespace);
		
		AssociationEntity entity = (AssociationEntity) this.getSqlSessionTemplate().selectOne(GET_ASSOCIATION_ENTITY_BY_CODE_AND_NAMESPACE_SQL, 
				new PrefixedParameterTriple(prefix, codingSchemeId, entityCode, entityCodeNamespace));

		return addEntityAttributes(
				prefix, 
				codingSchemeId, 
				entityId,
				entity);
	}
	@Override
	public ResolvedConceptReference getResolvedCodedNodeReferenceByCodeAndNamespace(
			String codingSchemeId, String entityCode, String entityCodeNamespace) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		PrefixedParameterTriple triple = 
			new PrefixedParameterTriple(prefix, codingSchemeId, entityCode, entityCodeNamespace);
		
		List<ResolvedConceptReference> ref = 
		this.getSqlSessionTemplate().selectList(GET_RESOLVED_CODED_NODE_REFERENCE_BY_CODE_AND_NAMESPACE_SQL, triple);
		if(!ref.isEmpty()){
		return
			ref.get(0);}
		else{
			return null;
		}

	}
	
	protected Entity getEntityByEntryStateUid(
			String codingSchemeUid, 
			String entityUid,
			String entryStateUid) {
		SequentialMappedParameterBean bean = 
			new SequentialMappedParameterBean(entityUid, entryStateUid);
		bean.setPrefix(this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid));
		
		return (Entity) this.getSqlSessionTemplate().selectOne(GET_ENTITY_BY_ID_AND_REVISION_ID_SQL, 
				bean);
	}
	
	@CacheMethod
	public Entity getEntityByUId(
			String codingSchemeId, 
			String entityId) {
		return this.getEntityByUId(codingSchemeId, entityId, null, null);
	}

	@CacheMethod
	public Entity getEntityByUId(
			String codingSchemeId, 
			String entityId, 
			List<String> propertyNames, 
			List<String> propertyTypes){
		
		List<Entity> entities = this.getEntities(
				codingSchemeId, 
				propertyNames, 
				propertyTypes, 
				DaoUtility.createNonTypedList(entityId));
		
		if(CollectionUtils.isEmpty(entities)) {
			return null;
		} if(entities.size() > 1){
			throw new RuntimeException("Too many entities returned.");
		}
		return entities.get(0);
	}
	
	@Override
	@CacheMethod
	public Entity getHistoryEntityByRevision(String codingSchemeUid, String entityUid, String revisionId) {
		String REVISION_ID_PARAMETER = "revisionId";

		String prefix = this.getPrefixResolver().resolvePrefixForHistoryCodingScheme(codingSchemeUid);
		
		SequentialMappedParameterBean bean = 
			new SequentialMappedParameterBean(entityUid);
		bean.put(REVISION_ID_PARAMETER, revisionId);

		bean.setPrefix(prefix);
		
		return (Entity) this.getSqlSessionTemplate().selectOne(GET_ENTITY_BY_ID_AND_REVISION_ID_SQL, 
				bean);
	}
	
	protected <T extends Entity> T addEntityAttributes(
			String prefix, 
			String codingSchemeId, 
			String entityId,
			T entity) {
		if(entity == null) {
			return null;
		}
	
		entity.addAnyProperties(
				ibatisPropertyDao.getAllPropertiesOfParent(codingSchemeId, entityId, PropertyType.ENTITY));
		
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	@Deprecated
	protected List<PropertyLink> doGetPropertyLinks(String prefix, String codingSchemeId, List<String> entityUids){
		return this.getSqlSessionTemplate().
			selectList(GET_PROPERTY_LINKS_BY_ENTITY_UIDS_SQL, 
				new PrefixedParameterCollection(prefix, codingSchemeId, entityUids));
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.entity.EntityDao#getEntityCount(java.lang.String)
	 */
	@Override
	public int getEntityCount(String codingSchemeId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return (Integer) 
			this.getSqlSessionTemplate().selectOne(GET_ENTITY_COUNT_SQL, new PrefixedParameter(prefix, codingSchemeId));
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.entity.EntityDao#updateEntity(java.lang.String, org.LexGrid.concepts.Entity)
	 */
	@ClearCache
	public String updateEntity(String codingSchemeUId, String entityUId,
			Entity entity) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		return this.doUpdateEntity(prefix, codingSchemeUId, entityUId, entity);	
	}
	
	@Override
	@ClearCache
	public String updateEntityVersionableAttrib(String codingSchemeUId, String entityUId, Entity entity) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		String entryStateUId = this.createUniqueId();
		
		InsertOrUpdateEntityBean bean = new InsertOrUpdateEntityBean();
		bean.setPrefix(prefix);
		bean.setEntity(entity);
		bean.setCodingSchemeUId(codingSchemeUId);
		bean.setUId(entityUId);
		bean.setEntryStateUId(entryStateUId);
		
		this.getSqlSessionTemplate().update(UPDATE_ENTITY_VER_ATTRIB_BY_ID_SQL, bean);
		
		return entryStateUId;
	}
	
	protected String doUpdateEntity(String prefix, String codingSchemeUId, String entityUId, Entity entity) {
		Assert.hasText(entity.getEntityCode(), "An Entity Code is required to be populated to Update an Entity.");
		Assert.hasText(entity.getEntityCodeNamespace(), "An Entity Code Namespace is required to be populated to Update an Entity.");
	
		String entryStateUId = this.createUniqueId();
		
		InsertOrUpdateEntityBean bean = new InsertOrUpdateEntityBean();
		bean.setPrefix(prefix);
		bean.setEntity(entity);
		bean.setCodingSchemeUId(codingSchemeUId);
		bean.setUId(entityUId);
		bean.setEntryStateUId(entryStateUId);
		
		if (entity instanceof AssociationEntity) {

			AssociationEntity assocEntity = (AssociationEntity) entity;
			
			bean.setForwardName(assocEntity.getForwardName());
			bean.setReverseName(assocEntity.getReverseName());
			bean.setIsNavigable(assocEntity.getIsNavigable());
			bean.setIsTransitive(assocEntity.getIsTransitive());
		}
		
		this.getSqlSessionTemplate().update(UPDATE_ENTITY_BY_UID_SQL, bean);
		
		return entryStateUId;
	}
	
	@ClearCache
	public void updateEntity(String codingSchemeId,
			AssociationEntity entity) {
		String entityId = this.getEntityUId(codingSchemeId, entity.getEntityCode(), entity.getEntityCodeNamespace());
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		this.doUpdateEntity(prefix, codingSchemeId, entityId, entity);	
		
		this.ibatisAssociationDao.updateAssociationEntity(codingSchemeId, entityId, entity);
	}
	
	@Override
	@ClearCache
	public String insertEntity(String codingSchemeId, Entity entity,
			boolean cascade) {
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return this.doInsertEntity(
				prefix, 
				codingSchemeId, 
				entity, 
				this.getSqlSessionTemplate(), 
				cascade);
	}
	
	/**
	 * Insert entity.
	 * 
	 * @param codingSchemeUId the coding scheme id
	 * @param entity the entity
	 * @param session the inserter
	 * 
	 * @return the string
	 */
	protected String doInsertEntity(
			String prefix, 
			String codingSchemeUId, 
			Entity entity, 
			SqlSessionTemplate session,
			boolean cascade) {
		Map<String,String> propertyIdToGuidMap = new HashMap<String,String>();
		
		String entityUId = this.createUniqueId();
		String entryStateUId = this.createUniqueId();
		
		this.ibatisVersionsDao.insertEntryState(
				codingSchemeUId,
				entryStateUId, 
				entityUId, 
				EntryStateType.ENTITY, 
				null, 
				entity.getEntryState());
		
		session.insert(INSERT_ENTITY_SQL, 
				buildInsertEntityParamaterBean(
						prefix,
						prefix,
						codingSchemeUId, entityUId, entryStateUId, entity));

//		If source doesn't specify the entity, 
//		should it be defaulted to 'concept'?
		
		/*if (entity.getEntityType() == null
				|| entity.getEntityType().length == 0) {
			entity.setEntityType(new String[] { "concept" });
		}*/
		
		if( entity instanceof AssociationEntity) {
			if(entity.getEntityType().length == 0) {
				entity.setEntityType(new String[]{"association"});
			}
		}
		
		for(String entityType : entity.getEntityType()){
			session.insert(INSERT_ENTITY_TYPE_SQL, 
					new PrefixedParameterTuple(prefix, entityUId, entityType));
		}
			
		if(cascade) {
			for(Property prop : entity.getAllProperties()) {
				String propertyId = this.createUniqueId();
				ibatisPropertyDao.insertProperty(
						codingSchemeUId, 
						entityUId, 
						propertyId, 
						PropertyType.ENTITY, 
						prop, 
						session);
				propertyIdToGuidMap.put(prop.getPropertyId(), propertyId);
			}
		}
		
		for(PropertyLink link : entity.getPropertyLink()) {
			String propertyLinkId = this.createUniqueId();
			String sourcePropertyId = propertyIdToGuidMap.get(link.getSourceProperty());
			String targetPropertyId = propertyIdToGuidMap.get(link.getTargetProperty());
			
			this.ibatisPropertyDao.doInsertPropertyLink(prefix, entityUId, 
					propertyLinkId, link.getPropertyLink(), 
					sourcePropertyId, targetPropertyId, session);
		}

		return entityUId;
	}
	
	protected String doInsertHistoryEntity( 
			String codingSchemeUId, 
			String entityUId,
			Entity entity, 
			SqlSessionTemplate sqlSessionTemplate,
			boolean cascade) {
		Assert.notNull(entityUId);

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);

		InsertOrUpdateEntityBean entityData = (InsertOrUpdateEntityBean) this.getSqlSessionTemplate()
				.selectOne(GET_ENTITY_ATTRIBUTES_BY_UID_SQL,
						new PrefixedParameter(prefix, entityUId));
		
		String historyPrefix = this.getPrefixResolver().resolvePrefixForHistoryCodingScheme(codingSchemeUId);
		
		Assert.notNull(entityData);
		
		Assert.notNull(entityData.getEntryStateUId());
		
		entityData.setPrefix(historyPrefix);
		
		sqlSessionTemplate.insert(INSERT_ENTITY_SQL, entityData);
		
		return entityData.getEntryStateUId();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.entity.EntityDao#insertHistoryEntity(java.lang.String, org.LexGrid.concepts.Entity)
	 */
	public String insertHistoryEntity(String codingSchemeId, String entityId, Entity entity) {
		return this.doInsertHistoryEntity(
				codingSchemeId, 
				entityId, 
				entity, 
				this.getSqlSessionTemplate(),
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
		
		List<String> entityUids = 
			this.getSqlSessionTemplate().selectList(GET_ENTITY_UIDS_OF_CODING_SCHEME_SQL, 
					new PrefixedParameter(prefix, codingSchemeId),
					new RowBounds(start, pageSize));
		
		return this.getEntities(codingSchemeId, entityUids);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.entity.EntityDao#insertBatchEntities(java.lang.String, java.util.List)
	 */
	public void insertBatchEntities(
			final String codingSchemeId, 
			final List<? extends Entity> entities,
			final boolean cascade) {
		final String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);

		SqlSessionTemplate session = this.getSqlSessionTemplate();


				
				for(Entity entity : entities){
					doInsertEntity(
							prefix, 
							codingSchemeId, 
							entity, 
							session,
							cascade);
				}
				
				session.close();
				

	}
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.entity.EntityDao#getEntityId(java.lang.String, java.lang.String, java.lang.String)
	 */
	@ClearCache
	public String getEntityUId(String codingSchemeId, String entityCode,
			String entityCodeNamespace) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		List<String> result = this.getSqlSessionTemplate().selectList(
				GET_ENTITY_UID_BY_CODE_AND_NAMESPACE, 
				new PrefixedParameterTriple(prefix, codingSchemeId, entityCode, entityCodeNamespace));
		if(!result.isEmpty()){
		return result.get(0);}
		else {return null;}
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
		bean.setCodingSchemeUId(codingSchemeId);
		bean.setUId(entityId);
		bean.setEntryStateUId(entryStateId);
		bean.setEntity(entity);
		bean.setEntityCode(entity.getEntityCode());
		bean.setEntityCodeNamespace(entity.getEntityCodeNamespace());
		bean.setIsDefined(entity.getIsDefined()==null? false: entity.getIsDefined());
		bean.setIsAnonymous(entity.getIsAnonymous());
		bean.setDescription(entity.getEntityDescription()==null? 
				null: entity.getEntityDescription().getContent());
		bean.setIsActive(entity.getIsActive() == null? true: entity.getIsActive());
		bean.setOwner(entity.getOwner());
		bean.setStatus(entity.getStatus());
		bean.setEffectiveDate(entity.getEffectiveDate());
		bean.setExpirationDate(entity.getExpirationDate());

		
		if (entity instanceof AssociationEntity) {
			AssociationEntity assocEntity = (AssociationEntity) entity;

			bean.setForwardName(assocEntity.getForwardName());
			bean.setReverseName(assocEntity.getReverseName());
			bean.setIsNavigable(assocEntity.getIsNavigable());
			bean.setIsTransitive(assocEntity.getIsTransitive());
		}
		
		return bean;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.AbstractBaseDao#doGetSupportedLgSchemaVersions()
	 */
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createNonTypedList(supportedDatebaseVersion);
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

	@Override
	public void removeEntityByUId(String codingSchemeUId, String entityUId) {

		String prefix = 
			this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		this.getSqlSessionTemplate().
			delete(DELETE_ENTIYT_BY_UID_SQL, new PrefixedParameter(prefix, entityUId));	
	}

	@Override
	public String getLatestRevision(String csUId, String entityUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUId);
		
		String revId = (String) this.getSqlSessionTemplate().selectOne(
				GET_ENTITY_LATEST_REVISION_ID_BY_UID, 
				new PrefixedParameter(prefix, entityUId));
		
		return revId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean entityInUse(String codingSchemeUId, String entityCode,
			String entityCodeNamespace) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		List<String> assocUIdList = this.getSqlSessionTemplate()
				.selectList(
						CHECK_ENTITY_USAGE,
						new PrefixedParameterTuple(prefix, entityCode,
								entityCodeNamespace));

		if (assocUIdList != null && assocUIdList.size() != 0) {
			return true;
		}
		
		return false;
	}

	/**
	 * @return the ibatisCodingSchemeDao
	 */
	public IbatisCodingSchemeDao getIbatisCodingSchemeDao() {
		return ibatisCodingSchemeDao;
	}

	/**
	 * @param ibatisCodingSchemeDao the ibatisCodingSchemeDao to set
	 */
	public void setIbatisCodingSchemeDao(IbatisCodingSchemeDao ibatisCodingSchemeDao) {
		this.ibatisCodingSchemeDao = ibatisCodingSchemeDao;
	}

	@Override
	public String getEntryStateUId(String codingSchemeUId, String entityUId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		return (String) this.getSqlSessionTemplate().selectOne(
				GET_ENTRYSTATE_UID_BY_ENTITY_UID_SQL,
				new PrefixedParameter(prefix, entityUId));
	}

	@Override
	public void updateEntryStateUId(String codingSchemeUId, String entityUId,
			String entryStateUId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		this.getSqlSessionTemplate().update(
				UPDATE_ENTITY_ENTRYSTATE_UID, 
				new PrefixedParameterTuple(prefix, entityUId, entryStateUId));
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public List<String> getDistinctEntityNamespacesFromCode(
			String codingSchemeUId, 
			String entityCode) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		return this.getSqlSessionTemplate().selectList(
				GET_DISTINCT_ENTITY_NAMESPACES_SQL, 
				new PrefixedParameterTuple(prefix, codingSchemeUId, entityCode));
	}
	
	public String getEntryState(
			String codingSchemeUid, 
			String entityGuid, 
			String revisionGuid) {
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		return (String) this.getSqlSessionTemplate().selectOne(
				GET_ENTRY_STATE,
				new PrefixedParameterTuple(prefix, entityGuid, revisionGuid));
	}

	@Override
	public boolean entryStateExists(String codingSchemeUId, String entryStateUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		if (super.entryStateExists(prefix, entryStateUId))
			return true;
		else
			return false;
	}

	@Override
	public EntityDescription getEntityDescription(String codingSchemeUid,
			String entityCode, String entityCodeNamespace) {
		SequentialMappedParameterBean bean = 
			new SequentialMappedParameterBean(
					codingSchemeUid, 
					entityCode, 
					entityCodeNamespace);
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUid);
		
		bean.setPrefix(prefix);
		
		return (EntityDescription) this.getSqlSessionTemplate().selectOne(
				GET_ENTITY_DESCRIPTION_SQL,
				bean);
	}
	
	@Override
	public String getEntityDescriptionAsString(String codingSchemeUid,
			String entityCode, String entityCodeNamespace) {
		PrefixedParameterTuple bean = 
			new PrefixedParameterTuple(
					codingSchemeUid, 
					entityCode, 
					entityCodeNamespace);
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUid);
		
		bean.setPrefix(prefix);
		
		return  (String) this.getSqlSessionTemplate().selectOne(
				GET_ENTITY_DESCRIPTION_STRING,
				bean);
	}
}

package org.lexevs.dao.database.ibatis.association;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.apache.ibatis.session.RowBounds;
import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.association.AssociationDataDao;
import org.lexevs.dao.database.access.association.AssociationTargetDao;
import org.lexevs.dao.database.access.association.batch.AssociationQualifierBatchInsertItem;
import org.lexevs.dao.database.access.association.batch.AssociationSourceBatchInsertItem;
import org.lexevs.dao.database.access.association.batch.TransitiveClosureBatchInsertItem;
import org.lexevs.dao.database.access.association.model.InstanceToGuid;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.access.association.model.graphdb.GraphDbTriple;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.association.parameter.GetNodesPathBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertAssociationPredicateBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertAssociationQualificationOrUsageContextBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertOrUpdateAssociationEntityBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertOrUpdateRelationsBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertTransitiveClosureBean;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterCollection;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedTableParameterBean;
import org.lexevs.dao.database.ibatis.versions.IbatisVersionsDao;
import org.lexevs.dao.database.inserter.BatchInserter;
import org.lexevs.dao.database.inserter.Inserter;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.util.Assert;


/**
 * The Class IbatisAssociationDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Cacheable(cacheName = "IbatisAssociationDaoCache")
public class IbatisAssociationDao extends AbstractIbatisDao implements AssociationDao {
	



	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	public static String ASSOCIATION_NAMESPACE = "Association.";
	
	/** The INSER t_ relation s_ sql. */
	public static String INSERT_RELATIONS_SQL = ASSOCIATION_NAMESPACE + "insertRelations";
	
	/** The INSER t_ associatio n_ qua l_ o r_ contex t_ sql. */
	public static String INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL = ASSOCIATION_NAMESPACE + "insertAssociationQualificationOrUsageContext";
	
	/** The INSER t_ associatio n_ predicat e_ sql. */
	public static String INSERT_ASSOCIATION_PREDICATE_SQL = ASSOCIATION_NAMESPACE + "insertAssociationPredicate";
	
	public static String INSERT_ASSOCIATIONENTITY_SQL = ASSOCIATION_NAMESPACE + "insertAssociationEntity";
	
	private static String UPDATE_ASSOCIATIONENTITY_FOR_ENTITY_ID_SQL = ASSOCIATION_NAMESPACE + "updateAssociationEntityForEntityId";
	
	/** The INSER t_ transitiv e_ closur e_ sql. */
	public static String INSERT_TRANSITIVE_CLOSURE_SQL = ASSOCIATION_NAMESPACE + "insertTransitiveClosure";
	
	/** The GE t_ associatio n_ instanc e_ ke y_ sql. */
	private static String GET_ASSOCIATION_INSTANCE_KEY_SQL = ASSOCIATION_NAMESPACE + "getAccociationInstanceKey";
	
	/** Getting a UID. */
	private static String GET_ASSOCIATION_UID_SQL_FROM_INSTANCE_ID = ASSOCIATION_NAMESPACE + "getAssociationInstanceUIDFromInstanceIdOnly";
	
			/** The GE t_ relation s_ ke y_ sql. */
	private static String GET_RELATIONS_KEY_SQL = ASSOCIATION_NAMESPACE + "getRelationsKey";
	
	/** The GE t_ associatio n_ predicat e_ ke y_ sql. */

	private static String GET_ASSOCIATION_PREDICATE_UID_BY_CONTAINER_UID_SQL = ASSOCIATION_NAMESPACE + "getAssociationPredicateUIdByContainerUId";

	private static String GET_ASSOCIATION_PREDICATE_UID_BY_CONTAINER_NAME_SQL = ASSOCIATION_NAMESPACE + "getAssociationPredicateUIdByContainerName";

	private static String GET_ASSOCIATION_PREDICATE_NAME_FOR_ID_SQL = ASSOCIATION_NAMESPACE + "getAssociationPredicateNameForId";
	
	private static String GET_RELATIONS_IDS_FOR_CODINGSCHEME_ID_SQL = ASSOCIATION_NAMESPACE + "getRelationsKeysForCodingSchemeId";
	
	private static String GET_RELATIONS_NAMES_FOR_CODINGSCHEME_ID_SQL = ASSOCIATION_NAMESPACE + "getRelationsNamesForCodingSchemeId";
	
	private static String GET_ASSOCIATION_PREDICATE_IDS_FOR_RELATIONS_ID_SQL = ASSOCIATION_NAMESPACE + "getAssociationPredicateKeysForRelationsId";
	
	private static String GET_ALL_TRIPLES_OF_CODINGSCHEME_SQL = ASSOCIATION_NAMESPACE + "getAllTriplesOfCodingScheme";
	
	private static final String GET_ALL_GRAPHDB_TRIPLES_OF_CODINGSCHEME_SQL = ASSOCIATION_NAMESPACE + "getAllTriplesOfCodingSchemeForGraphDbLoad";

	private static final String GET_ALL_ENTITY_ASSOC_ENTITY_GUID_OF_CODINGSCHEME_SQL = ASSOCIATION_NAMESPACE + "getAllEntityAssocEntityGuids";

	private static final String GET_GRAPHDB_TRIPLES_OF_CODINGSCHEME_SQL = ASSOCIATION_NAMESPACE + "getGraphDbTriples";

	private static final String GET_ANCESTOR_TRIPLES_OF_CODINGSCHEME_SQL = ASSOCIATION_NAMESPACE + "getGraphDbTriplesAncestorsTr";

	private static final String GET_DESCENDANT_TRIPLES_OF_CODINGSCHEME_SQL = ASSOCIATION_NAMESPACE + "getGraphDbTriplesDecendentsTr";

	private static final String GET_COMPLETE_INSTANCE_TO_GUID_MAP =  ASSOCIATION_NAMESPACE + "getCompleteInstanceToGuidMap";
	
	private static String DELETE_ENTITY_ASSOCIATION_QUALS_FOR_CODINGSCHEME_UID_SQL = ASSOCIATION_NAMESPACE + "deleteEntityAssocQualsByCodingSchemeUId";
	
	private static String DELETE_DATA_ASSOCIATION_QUALS_FOR_CODINGSCHEME_UID_SQL = ASSOCIATION_NAMESPACE + "deleteDataAssocQualsByCodingSchemeUId";
	
	private static String DELETE_DATA_ASSOCIATION_QUALS_FOR_RELATION_UID_SQL = ASSOCIATION_NAMESPACE + "deleteDataAssocQualsByRelationUId";
	
	private static String DELETE_ENTITY_ASSOCIATION_QUALS_FOR_RELATION_UID_SQL = ASSOCIATION_NAMESPACE + "deleteEntityAssocQualsByRelationUId";

	private static String GET_ASSOCIATION_PREDICATE_FOR_ID_SQL = ASSOCIATION_NAMESPACE + "getAssociationPredicateForId";
	
	private static String GET_ASSOCIATION_PREDICATE_UID_FOR_DIRECTIONAL_NAME_SQL = ASSOCIATION_NAMESPACE + "getAssociationPredicateUidForDirectionalName";
	
	private static String GET_RELATIONS_FOR_UID_AND_REVISION_ID_SQL = ASSOCIATION_NAMESPACE + "getRelationsForUidAndRevisionId";
	
	private static String GET_RELATIONS_FOR_UID_SQL = ASSOCIATION_NAMESPACE + "getRelationsForUid";
	
	private static String GET_RELATION_ATTRIBUTES_BY_UID_SQL = ASSOCIATION_NAMESPACE + "getRelationAttributeForRelationUId";
	
	private static String GET_NODES_PATH = ASSOCIATION_NAMESPACE + "getNodesPath"; 
	
	private static String UPDATE_RELATION_BY_UID_SQL = ASSOCIATION_NAMESPACE + "updateRelationByUId";
	
	private static String UPDATE_RELATION_VERSIONABLE_CHANGES_BY_UID_SQL = ASSOCIATION_NAMESPACE + "updateRelationVersionableChangesByUId";
	
	private static String DELETE_RELATION_BY_UID_SQL = ASSOCIATION_NAMESPACE + "deleteRelationByUId";
	
	private static String GET_ENTRYSTATE_UID_BY_RELATION_UID_SQL = ASSOCIATION_NAMESPACE + "getEntryStateUIdByRelationUId";
	
	private static String UPDATE_RELATION_ENTRYSTATE_UID_SQL = ASSOCIATION_NAMESPACE + "updateRelationEntryStateUId";
	
	private static String GET_RELATIONS_CONTAINER_NAME_FOR_ASSOC_INSTANCE_ID = ASSOCIATION_NAMESPACE + "getRelationContainerNameForAssociationInstanceId";
	
	private static String GET_ASSOCIATION_PREDICATE_NAME_FOR_ASSOC_INSTANCE_ID = ASSOCIATION_NAMESPACE + "getAssociationPredicateNameForAssociationInstanceId";
	
	private static String GET_RELATION_LATEST_REVISION_ID_BY_UID = ASSOCIATION_NAMESPACE + "getRelationLatestRevisionIdByUId";
	
	private static String GET_ASSOCIATION_PREDICATE_UIDS_FOR_NAME_SQL = ASSOCIATION_NAMESPACE + "getAssociationPredicateUidsForName";
	
	private static String GET_ANON_DESIGNATION_FOR_PREDICATE_UID = ASSOCIATION_NAMESPACE + "getAnonDesignationForPredicateId";
	
	/** The ibatis versions dao. */
	private IbatisVersionsDao ibatisVersionsDao;
	
	private AssociationTargetDao associationTargetDao = null;
	
	private AssociationDataDao associationDataDao = null;
	
	private PropertyDao propertyDao = null;
	

	@Override
	public String getAssociationPredicateNameForAssociationInstanceId(
			String codingSchemeUId, String associationInstanceId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		PrefixedParameter bean = new PrefixedParameter(prefix, associationInstanceId);
		
		return (String) this.getSqlSessionTemplate().selectOne(
				GET_ASSOCIATION_PREDICATE_NAME_FOR_ASSOC_INSTANCE_ID, bean);
	}

	@Override
	public String getRelationsContainerNameForAssociationInstanceId(
			String codingSchemeUId, String associationInstanceId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		PrefixedParameter bean = new PrefixedParameter(prefix, associationInstanceId);
		
		return (String) this.getSqlSessionTemplate().selectOne(
				GET_RELATIONS_CONTAINER_NAME_FOR_ASSOC_INSTANCE_ID, bean);
	}

	@Override
	public Relations getHistoryRelationByRevisionId(String codingSchemeUid,
			String relationUid, String revisionId) {
		String prefix = this.getPrefixResolver().resolvePrefixForHistoryCodingScheme(codingSchemeUid);
		
		PrefixedParameterTriple bean = new PrefixedParameterTriple();
		bean.setPrefix(prefix);
		bean.setActualTableSetPrefix(this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid));
		bean.setParam1(codingSchemeUid);
		bean.setParam2(relationUid);
		bean.setParam3(revisionId);
		
		return (Relations) this.getSqlSessionTemplate().selectOne(
				GET_RELATIONS_FOR_UID_AND_REVISION_ID_SQL, bean);	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Triple> getAllTriplesOfCodingScheme(
			String codingSchemeId,
			String associationPredicateId,
			int start, int pageSize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return this.getSqlSessionTemplate().selectList(
				GET_ALL_TRIPLES_OF_CODINGSCHEME_SQL, 
				new PrefixedParameterTuple(prefix, codingSchemeId, associationPredicateId), 
				new RowBounds(start, 
				pageSize));
	}
	

	@SuppressWarnings("unchecked")
	public List<GraphDbTriple> getAllGraphDbTriplesOfCodingScheme(
			String codingSchemeId,
			String associationPredicateId,
			int start, int pageSize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return this.getSqlSessionTemplate().selectList(
				GET_ALL_GRAPHDB_TRIPLES_OF_CODINGSCHEME_SQL, 
				new PrefixedParameterTuple(prefix, codingSchemeId, associationPredicateId), 
				new RowBounds(start, 
				pageSize));
	}
	
	@Override
	public String getAnonDesignationForPredicate(String codingSchemeId, String associationPredicateId){
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return (String) this.getSqlSessionTemplate().selectOne(
				GET_ANON_DESIGNATION_FOR_PREDICATE_UID, 
				new PrefixedParameterTuple(prefix, associationPredicateId, "Association"));
		
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.association.AssociationDao#getAssociationPredicateId(java.lang.String, java.lang.String, java.lang.String)
	 */
	@CacheMethod
	public String getAssociationPredicateUIdByContainerUId(String codingSchemeId,
			String relationContainerId, String associationPredicateName) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeId);
		return (String) this.getSqlSessionTemplate().selectOne(
				GET_ASSOCIATION_PREDICATE_UID_BY_CONTAINER_UID_SQL,
				new PrefixedParameterTuple(prefix, relationContainerId,
						associationPredicateName));
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.association.AssociationDao#getAssociationPredicateId(java.lang.String, java.lang.String, java.lang.String)
	 */
	@CacheMethod
	public String getAssociationPredicateUIdByContainerName(String codingSchemeUid,
			String relationContainerName, String associationPredicateName) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUid);
		return (String) this.getSqlSessionTemplate().selectOne(
				GET_ASSOCIATION_PREDICATE_UID_BY_CONTAINER_NAME_SQL,
				new PrefixedParameterTriple(
						prefix, 
						codingSchemeUid,
						relationContainerName,
						associationPredicateName));

	}
	
	@SuppressWarnings("unchecked")
	@CacheMethod
	public List<String> getAssociationPredicateUidsForAssociationName(
			String codingSchemeUid,
			String relationContainerName, 
			String associationPredicateName) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUid);
		return (List<String>) this.getSqlSessionTemplate().<String>selectList(
				GET_ASSOCIATION_PREDICATE_UIDS_FOR_NAME_SQL,
				new PrefixedParameterTriple(
						prefix, 
						codingSchemeUid,
						relationContainerName,
						associationPredicateName));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	@Deprecated
	public List<String> getAssociationPredicateUidsForDirectionalName(
			String codingSchemeId, String directionalName) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		PrefixedParameterTuple tuple = new PrefixedParameterTuple();
		tuple.setPrefix(prefix);
		tuple.setParam1(codingSchemeId);
		tuple.setParam2(directionalName);
		
		return this.getSqlSessionTemplate()
			.selectList(GET_ASSOCIATION_PREDICATE_UID_FOR_DIRECTIONAL_NAME_SQL, tuple);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.association.AssociationDao#getRelationsId(java.lang.String, java.lang.String)
	 */
	@CacheMethod
	public String getRelationUId(String codingSchemeId, String relationsName) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeId);
		return (String) this.getSqlSessionTemplate().selectOne(
				GET_RELATIONS_KEY_SQL,
				new PrefixedParameterTuple(prefix, codingSchemeId,
						relationsName));
	}
	
	@CacheMethod
	public String getRelationEntryStateUId(String codingSchemeUId, String relationUId) {
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
				
		return (String) this.getSqlSessionTemplate().selectOne(GET_ENTRYSTATE_UID_BY_RELATION_UID_SQL,
				new PrefixedParameter(prefix, relationUId));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public List<String> getAssociationPredicateUIdsForRelationsUId(
			String codingSchemeId, String relationsId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return this.getSqlSessionTemplate().selectList(
				GET_ASSOCIATION_PREDICATE_IDS_FOR_RELATIONS_ID_SQL, 
				new PrefixedParameter(prefix, relationsId));
	}

	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public List<String> getRelationsUIdsForCodingSchemeUId(String codingSchemeId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		List<String> returnList = this.getSqlSessionTemplate().selectList(
				GET_RELATIONS_IDS_FOR_CODINGSCHEME_ID_SQL,
				new PrefixedParameter(prefix, codingSchemeId));
		
		return returnList;
	}
	
	@Override
	public String getNodesPath(
			String codingSchemeUid,
			String sourceCode, 
			String sourceNS,
			String targetCode, 
			String targetNS, 
			String associationUid) {
		
		String prefix = this.getPrefixResolver().
			resolvePrefixForCodingScheme(codingSchemeUid);
		
		GetNodesPathBean bean = new GetNodesPathBean();
		bean.setPrefix(prefix);
		bean.setSourceEntityCode(sourceCode);
		bean.setSourceEntityCodeNamespace(sourceNS);
		bean.setTargetEntityCode(targetCode);
		bean.setTargetEntityCodeNamespace(targetNS);
		bean.setAssociationPredicateUId(associationUid);
		return (String) this.getSqlSessionTemplate().selectOne(GET_NODES_PATH, bean);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public List<String> getRelationsNamesForCodingSchemeUId(String codingSchemeId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return this.getSqlSessionTemplate().selectList(
				GET_RELATIONS_NAMES_FOR_CODINGSCHEME_ID_SQL,
				new PrefixedParameter(prefix, codingSchemeId));
	}

	@Override
	@ClearCache
	public void deleteAssociationQualificationsByCodingSchemeUId(String codingSchemeUId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		this.getSqlSessionTemplate().delete(
				DELETE_ENTITY_ASSOCIATION_QUALS_FOR_CODINGSCHEME_UID_SQL,
				new PrefixedParameter(prefix, codingSchemeUId));
		
		this.getSqlSessionTemplate().delete(
				DELETE_DATA_ASSOCIATION_QUALS_FOR_CODINGSCHEME_UID_SQL,
				new PrefixedParameter(prefix, codingSchemeUId));
	}
	
	@CacheMethod
	public String getAssociationPredicateNameForUId(String codingSchemeId, String associationPredicateId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return
			(String) 
				this.getSqlSessionTemplate().selectOne(GET_ASSOCIATION_PREDICATE_NAME_FOR_ID_SQL, new PrefixedParameter(prefix, associationPredicateId));
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.association.AssociationDao#insertRelations(java.lang.String, org.LexGrid.relations.Relations)
	 */
	@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache"})
	public String insertRelations(
			String codingSchemeId,
			Relations relations,
			boolean cascade) {
		String relationsUId = this.createUniqueId();
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		this.doInsertRelations(
				prefix, 
				codingSchemeId, 
				relationsUId, 
				relations, 
				cascade);
		return relationsUId;
	}

	protected String doInsertRelations(
			String prefix,
			String codingSchemeUId,
			String relationsUId,
			Relations relations,
			boolean cascade) {
		String entryStateUId = this.createUniqueId();
		
		InsertOrUpdateRelationsBean bean = buildInsertOrUpdateRelationsBean(prefix,
				codingSchemeUId,
				relationsUId,
				relations,
				cascade);

		this.ibatisVersionsDao.insertEntryState(
				codingSchemeUId, 
				entryStateUId, 
				relationsUId,
				EntryStateType.RELATION, 
				null,
				relations.getEntryState());
		
		this.getSqlSessionTemplate().insert(INSERT_RELATIONS_SQL, bean);
		
		if(cascade){
			
			if (relations.getProperties() != null) {
				for (Property property : relations.getProperties()
						.getProperty()) {
					this.getPropertyDao().insertProperty(codingSchemeUId,
							relationsUId, PropertyType.RELATION, property);
				}
			}
			
			for(AssociationPredicate predicate : relations.getAssociationPredicate()) {
				this.insertAssociationPredicate(
						codingSchemeUId, 
						relationsUId, 
						predicate,
						cascade);
			}
		}
		return relationsUId;
	}
	
	private InsertOrUpdateRelationsBean buildInsertOrUpdateRelationsBean(String prefix, String codingSchemeUId,
			String relationsUId, Relations relations, boolean cascade) {
		InsertOrUpdateRelationsBean bean = new InsertOrUpdateRelationsBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeGuid(codingSchemeUId);
		bean.setRelationGuid(relationsUId);
		bean.setRelations(relations);
		bean.setContainerName(relations.getContainerName());
		bean.setIsMapping(relations.getIsMapping());
		bean.setSourceCodingScheme(relations.getSourceCodingScheme());
		bean.setSourceCodingSchemeVersion(relations.getSourceCodingSchemeVersion());
		bean.setTargetCodingScheme(relations.getTargetCodingScheme());
		bean.setSourceCodingSchemeVersion(relations.getTargetCodingSchemeVersion());
		bean.setDescription(relations.getEntityDescription()==null?
				null:relations.getEntityDescription().getContent());
		bean.setIsActive(relations.getIsActive());
		bean.setOwner(relations.getOwner());
		bean.setStatus(relations.getStatus());
		bean.setEffectiveDate(relations.getEffectiveDate());
		bean.setExpirationDate(relations.getExpirationDate());

		return bean;
	}

	public String insertAssociationEntity(
			String codingSchemeId,
			String entityId,
			AssociationEntity associationEntity,
			SqlSessionTemplate sqlSessionTemplate){
		
		String associationEntityId = this.createUniqueId();
		
		InsertOrUpdateAssociationEntityBean bean = new InsertOrUpdateAssociationEntityBean();
		bean.setPrefix(this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId));
		bean.setEntityUId(entityId);
		bean.setUId(associationEntityId);
		bean.setAssociationEntity(associationEntity);
		
		sqlSessionTemplate.insert(INSERT_ASSOCIATIONENTITY_SQL, bean);	
		
		return associationEntityId;
	}
	
	public String insertAssociationEntity(
			String codingSchemeId,
			String entityId,
			AssociationEntity associationEntity) {
		return this.insertAssociationEntity(
				codingSchemeId, entityId, associationEntity, this.getSqlSessionTemplate());	
	}
	
	public void updateAssociationEntity(String codingSchemeId, String entityId,
			AssociationEntity entity) {
		InsertOrUpdateAssociationEntityBean bean = new InsertOrUpdateAssociationEntityBean();
		bean.setPrefix(this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId));
		bean.setEntityUId(entityId);
		bean.setAssociationEntity(entity);
		
		this.getSqlSessionTemplate().update(
				UPDATE_ASSOCIATIONENTITY_FOR_ENTITY_ID_SQL, 
				bean);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.association.AssociationDao#insertAssociationPredicate(java.lang.String, java.lang.String, org.LexGrid.relations.AssociationPredicate)
	 */
	@ClearCache(clearCaches = {"IbatisCodingSchemeDaoCache"})
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
		
		this.getSqlSessionTemplate().insert(INSERT_ASSOCIATION_PREDICATE_SQL, bean);
		
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
	@ClearCache
	public void insertBatchAssociationSources(final String codingSchemeId,
			final List<AssociationSourceBatchInsertItem> list) {
		
		SqlSessionTemplate session = getSqlSessionTemplate();
		

				
				for(AssociationSourceBatchInsertItem item : list){
					
					insertAssociationSource(
							codingSchemeId, 
							item.getParentId(), 
							item.getAssociationSource(), 
							session);
				}
				
			}	

	
	
	@ClearCache
	public void insertBatchAssociationQualifiers(final String codingSchemeId,
			final List<AssociationQualifierBatchInsertItem> list, HashMap<String,String> instanceMap) {
		
				SqlSessionTemplate session = this.getSqlSessionTemplate();
				
				for(AssociationQualifierBatchInsertItem item : list){
					String associationId = instanceMap.get(item.getParentId());
					if(associationId == null) {
						System.out.println(item.getParentId() + "Is an associationId"
								+ " with no loaded association"); 
					}else {
						
						String qualUId = createUniqueId();
						InsertAssociationQualificationOrUsageContextBean qualBean = new InsertAssociationQualificationOrUsageContextBean();
						qualBean.setReferenceUId(associationId);
						qualBean.setUId(qualUId);
						qualBean.setPrefix(getPrefixResolver().resolvePrefixForCodingScheme(
								codingSchemeId));
						qualBean.setQualifierName(item.getAssociationQualifier().getAssociationQualifier());
						qualBean.setEntryStateUId(createUniqueId());

						if (item.getAssociationQualifier().getQualifierText() != null) {
							qualBean
									.setQualifierValue(item.getAssociationQualifier().getQualifierText().getContent());
						}

						session.insert(INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, qualBean);
					}
					}
			}

	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.association.AssociationDao#insertAssociationSource(java.lang.String, java.lang.String, org.LexGrid.relations.AssociationSource)
	 */
	@ClearCache
	public void insertAssociationSource(String codingSchemeId,
			String associationPredicateId, AssociationSource source){
		this.insertAssociationSource(codingSchemeId, associationPredicateId, source, 
				this.getSqlSessionTemplate());
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.association.AssociationDao#insertBatchAssociationSources(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	@ClearCache
	public void insertBatchAssociationSources(final String codingSchemeUId,
			final String associationPredicateUId, final List<AssociationSource> batch) {
		SqlSessionTemplate session = getSqlSessionTemplate();
			
				
				for(AssociationSource source : batch) {
					insertAssociationSource(codingSchemeUId, associationPredicateUId, source, session);
				}

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
	 * @param session the executor
	 * 
	 * @return the string
	 */
	protected String doInsertIntoTransitiveClosure(
			String prefix,
			String associationPredicateId, 
			String sourceEntityCode,
			String sourceEntityCodeNamespace, 
			String targetEntityCode,
			String targetEntityCodeNamespace, 
			String path,
			SqlSessionTemplate session) {
		
		String id = this.createUniqueId();
		
		InsertTransitiveClosureBean bean = new InsertTransitiveClosureBean();
		bean.setPrefix(prefix);
		bean.setUId(id);
		bean.setAssociationPredicateUId(associationPredicateId);
		bean.setSourceEntityCode(sourceEntityCode);
		bean.setSourceEntityCodeNamespace(sourceEntityCodeNamespace);
		bean.setTargetEntityCode(targetEntityCode);
		bean.setTargetEntityCodeNamespace(targetEntityCodeNamespace);
		bean.setPath(path);
		
		session.insert(INSERT_TRANSITIVE_CLOSURE_SQL, bean);
		
		return id;
		
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.association.AssociationDao#insertIntoTransitiveClosure(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String insertIntoTransitiveClosure(String codingSchemeId,
			String associationPredicateId, String sourceEntityCode,
			String sourceEntityCodeNamespace, String targetEntityCode,
			String targetEntityCodeNamespace, String path) {
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return this.doInsertIntoTransitiveClosure(prefix,
				associationPredicateId, 
				sourceEntityCode,
				sourceEntityCodeNamespace, 
				targetEntityCode,
				targetEntityCodeNamespace,
				path,
				this.getSqlSessionTemplate());
	}
	

	
	/**
	 * Insert batch transitive closure.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param batch the batch
	 */
	public void insertBatchTransitiveClosure(final String codingSchemeId,
			final List<TransitiveClosureBatchInsertItem> batch) {
		
		final String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		SqlSessionTemplate session = this.getSqlSessionTemplate();


				
				for(TransitiveClosureBatchInsertItem item : batch){
					doInsertIntoTransitiveClosure(
							prefix,
							item.getAssociationPredicateId(), 
							item.getSourceEntityCode(),
							item.getSourceEntityCodeNamespace(), 
							item.getTargetEntityCode(),
							item.getTargetEntityCodeNamespace(),
							item.getPath(),
							session);
				}


	}


	/**
	 * Insert association source.
	 * 
	 * @param codingSchemeUId the coding scheme id
	 * @param associationPredicateUId the association predicate id
	 * @param source the source
	 * @param session the inserter
	 */
	@ClearCache
	public void insertAssociationSource(String codingSchemeUId,
			String associationPredicateUId, AssociationSource source,
			SqlSessionTemplate session) {
		Assert
				.isTrue(
						source.getTarget().length > 0
								|| source.getTargetData().length > 0,
						"Must Insert at least ONE AssociationTarget or AssociationData per AssociationSource.");

		for (AssociationTarget target : source.getTarget()) {

			associationTargetDao.insertAssociationTarget(codingSchemeUId,
					associationPredicateUId, source, target, session);
		}

		for (AssociationData data : source.getTargetData()) {

			associationDataDao.insertAssociationData(codingSchemeUId,
					associationPredicateUId, source, data, session);
		}
	}
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.association.AssociationDao#insertAssociationQualifier(java.lang.String, java.lang.String, org.LexGrid.relations.AssociationQualification)
	 */
	@ClearCache
	public void insertAssociationQualifier(String codingSchemeId,
			String associationInstanceId, AssociationQualification qualifier) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		String associationTargetId = this.getKeyForAssociationInstanceId(codingSchemeId, associationInstanceId);
		
		String qualId = this.createUniqueId();
		
		InsertAssociationQualificationOrUsageContextBean contextBean = new InsertAssociationQualificationOrUsageContextBean();
		contextBean.setReferenceUId(associationTargetId);
		contextBean.setUId(qualId);
		contextBean.setPrefix(prefix);
		contextBean.setQualifierName(qualifier.getAssociationQualifier());
		contextBean.setQualifierValue(qualifier.getQualifierText().getContent());
		
		this.getSqlSessionTemplate().insert(
				INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, 
				contextBean);
	}
	
	
	
	@Override
	@CacheMethod
	public AssociationPredicate getAssociationPredicateByUId(String codingSchemeId,
			String associationPredicateUid) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		PrefixedParameter bean = new PrefixedParameter();
		bean.setPrefix(prefix);
		bean.setParam1(associationPredicateUid);
		
		AssociationPredicate predicate = (AssociationPredicate) this.getSqlSessionTemplate().selectOne(
				GET_ASSOCIATION_PREDICATE_FOR_ID_SQL, 
				bean);
		
		return predicate;
	}

	@Override
	@CacheMethod
	public Relations getRelationsByUId(String codingSchemeId,
			String relationsUid, Boolean getAssocPredicates) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		PrefixedParameterTuple bean = new PrefixedParameterTuple();
		bean.setPrefix(prefix);
		bean.setParam1(codingSchemeId);
		bean.setParam2(relationsUid);
		
		Relations relations = (Relations) this.getSqlSessionTemplate().selectOne(GET_RELATIONS_FOR_UID_SQL, bean);
	
		if (getAssocPredicates) {
			List<String> assocPredicateUIdList = this
					.getAssociationPredicateUIdsForRelationsUId(codingSchemeId,
							relationsUid);

			for (String predicateId : assocPredicateUIdList) {
				relations.addAssociationPredicate(getAssociationPredicateByUId(
						codingSchemeId, predicateId));
			}
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
	public String getKeyForAssociationInstanceId(String codingSchemeId, String associationInstanceId){
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return (String) this.getSqlSessionTemplate().selectOne(
				
				GET_ASSOCIATION_UID_SQL_FROM_INSTANCE_ID, 
				new PrefixedParameter(prefix, associationInstanceId));
	}
	
	@Override
	@CacheMethod
	public Map<String, String> getInstanceToGuidCache(String schemeId)
	{
		List<InstanceToGuid> list = (List<InstanceToGuid>) getGuidToInstanceMap(schemeId);
		return (Map<String, String>)list.stream().collect(Collectors.toMap(x -> x.getInstance(), y -> y.getValue()));
	}
	
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.AbstractBaseDao#doGetSupportedLgSchemaVersions()
	 */
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createNonTypedList(supportedDatebaseVersion);
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

	@Override
	public String insertHistoryRelation(String codingSchemeUId,
			String relationUId, Relations relation) {

		return this.doInsertHistoryRelation(codingSchemeUId, relationUId, relation,
				this.getSqlSessionTemplate());
	}
	
	protected String doInsertHistoryRelation(String codingSchemeUId, 
			String relationUId,
			Relations relation, 
			SqlSessionTemplate sqlSessionTemplate) {
		
		String historyPrefix = this.getPrefixResolver().resolvePrefixForHistoryCodingScheme(codingSchemeUId);
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);

		InsertOrUpdateRelationsBean relationData = (InsertOrUpdateRelationsBean) this.getSqlSessionTemplate()
				.selectOne(GET_RELATION_ATTRIBUTES_BY_UID_SQL,
						new PrefixedParameter(prefix, relationUId));
		
		relationData.setPrefix(historyPrefix);
		
		sqlSessionTemplate.insert(INSERT_RELATIONS_SQL, relationData);
	
		String historyRelationEntryStateUid = relationData.getEntryStateUId();

		return historyRelationEntryStateUid;
	}

	@Override
	@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache"})
	public String updateRelation(String codingSchemeUId, String relationUId,
			Relations relation) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		return this.doUpdateRelation(prefix, codingSchemeUId, relationUId, relation);	
	}

	protected String doUpdateRelation(String prefix, String codingSchemeUId,
			String relationUId, Relations relation) {
	
		String entryStateUId = this.createUniqueId();
		
		InsertOrUpdateRelationsBean bean = new InsertOrUpdateRelationsBean();
		bean.setPrefix(prefix);
		bean.setRelations(relation);
		bean.setCodingSchemeUId(codingSchemeUId);
		bean.setUId(relationUId);
		bean.setEntryStateUId(entryStateUId);
		
		this.getSqlSessionTemplate().update(UPDATE_RELATION_BY_UID_SQL, bean);
		
		List<String> associationPredicateUids = this.getAssociationPredicateUIdsForRelationsUId(codingSchemeUId, relationUId);
		
		List<String> alreadyLoadedAssociationPredicateNames = new ArrayList<String>();
		
		for(String associationPredicateUid : associationPredicateUids) {
			String associationPredicateName = this.getAssociationPredicateNameForUId(codingSchemeUId, associationPredicateUid);
			alreadyLoadedAssociationPredicateNames.add(associationPredicateName);
		}
		
		if(relation.getAssociationPredicateCount() > 0) {

			for(AssociationPredicate associationPredicateToAdd : relation.getAssociationPredicate()) {
				if(! alreadyLoadedAssociationPredicateNames.contains(associationPredicateToAdd.getAssociationName())) {
					this.insertAssociationPredicate(codingSchemeUId, relationUId, associationPredicateToAdd, false);
				}
			}
		}
		
		return entryStateUId;
	}

	@Override
	@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache"})
	public void removeRelationByUId(String codingSchemeUId, String relationUId) {

		String prefix = 
			this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		this.getSqlSessionTemplate().
			delete(DELETE_RELATION_BY_UID_SQL, new PrefixedParameter(prefix, relationUId));	
	}

	@Override
	@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache"})
	public String updateRelationVersionableChanges(String codingSchemeUId,
			String relationUId, Relations relation) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		String entryStateUId = this.createUniqueId();
		
		InsertOrUpdateRelationsBean bean = new InsertOrUpdateRelationsBean();
		bean.setPrefix(prefix);
		bean.setRelations(relation);
		bean.setCodingSchemeUId(codingSchemeUId);
		bean.setEntryStateUId(entryStateUId);
		bean.setUId(relationUId);
		
		this.getSqlSessionTemplate().update(UPDATE_RELATION_VERSIONABLE_CHANGES_BY_UID_SQL, bean);
		
		return entryStateUId;
	}

	@Override
	@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache"})
	public void updateRelationEntryStateUId(String codingSchemeUId,
			String relationUId, String entryStateUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		this.getSqlSessionTemplate().update(
				UPDATE_RELATION_ENTRYSTATE_UID_SQL, 
				new PrefixedParameterTuple(prefix, relationUId, entryStateUId));
	}
	
	@Override
	public void deleteAssociationQualificationsByRelationUId(
			String codingSchemeUId, String relationUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		this.getSqlSessionTemplate().delete(
				DELETE_DATA_ASSOCIATION_QUALS_FOR_RELATION_UID_SQL,
				new PrefixedParameterTuple(prefix, codingSchemeUId, relationUId));
		
		this.getSqlSessionTemplate().delete(
				DELETE_ENTITY_ASSOCIATION_QUALS_FOR_RELATION_UID_SQL,
				new PrefixedParameterTuple(prefix, codingSchemeUId, relationUId));
	}

	@Override
	public String getRelationLatestRevision(String csUId, String relationUId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUId);
		
		return (String) this.getSqlSessionTemplate().selectOne(
				GET_RELATION_LATEST_REVISION_ID_BY_UID, 
				new PrefixedParameter(prefix, relationUId));	
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllEntityAssocToEntityGuidsOfCodingScheme(
			String codingSchemeId, String associationPredicateId, int start,
			int pageSize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return this.getSqlSessionTemplate().selectList(
				GET_ALL_ENTITY_ASSOC_ENTITY_GUID_OF_CODINGSCHEME_SQL, 
				new PrefixedParameter(prefix, codingSchemeId), 
				new RowBounds(start, 
				pageSize));
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<InstanceToGuid> getGuidToInstanceMap(String codingSchemeId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		PrefixedTableParameterBean bean = new PrefixedTableParameterBean(prefix);
		return this.getSqlSessionTemplate().selectList(
				GET_COMPLETE_INSTANCE_TO_GUID_MAP, 
				bean);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GraphDbTriple> getAllGraphDbTriplesOfCodingScheme(
			String codingSchemeId,List<String> guids) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return this.getSqlSessionTemplate().selectList(
				GET_GRAPHDB_TRIPLES_OF_CODINGSCHEME_SQL, 
				new PrefixedParameterCollection(prefix, codingSchemeId, guids));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GraphDbTriple> getAllAncestorTriplesTrOfCodingScheme(
			String codingSchemeId, String associationName, String code, int start, int pagesize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return this.getSqlSessionTemplate().selectList(
				GET_ANCESTOR_TRIPLES_OF_CODINGSCHEME_SQL, 
				new PrefixedParameterTriple(prefix, codingSchemeId, code, associationName), new RowBounds(start, pagesize));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GraphDbTriple> getAllDescendantTriplesTrOfCodingScheme(
			String codingSchemeId, String associationName, String code, int start, int pagesize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return this.getSqlSessionTemplate().selectList(
				GET_DESCENDANT_TRIPLES_OF_CODINGSCHEME_SQL, 
				new PrefixedParameterTriple(prefix, codingSchemeId, code, associationName), new RowBounds(start, pagesize));
	}
	/**
	 * @return the propertyDao
	 */
	public PropertyDao getPropertyDao() {
		return propertyDao;
	}

	/**
	 * @param propertyDao the propertyDao to set
	 */
	public void setPropertyDao(PropertyDao propertyDao) {
		this.propertyDao = propertyDao;
	}

	/**
	 * @return the associationTargetDao
	 */
	public AssociationTargetDao getAssociationTargetDao() {
		return associationTargetDao;
	}

	/**
	 * @param associationTargetDao the associationTargetDao to set
	 */
	public void setAssociationTargetDao(AssociationTargetDao associationTargetDao) {
		this.associationTargetDao = associationTargetDao;
	}

	/**
	 * @return the associationDataDao
	 */
	public AssociationDataDao getAssociationDataDao() {
		return associationDataDao;
	}

	/**
	 * @param associationDataDao the associationDataDao to set
	 */
	public void setAssociationDataDao(AssociationDataDao associationDataDao) {
		this.associationDataDao = associationDataDao;
	}

	@Override
	public boolean entryStateExists(String codingSchemeUId, String entryStateUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		if(super.entryStateExists(prefix, entryStateUId))
			return true;
		else
			return false;
	}
	







}
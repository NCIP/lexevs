
package org.lexevs.dao.database.ibatis.property;

import java.sql.SQLException;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.property.batch.PropertyBatchInsertItem;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.constants.DatabaseConstants;
import org.lexevs.dao.database.constants.classifier.property.PropertyMultiAttributeClassifier;
import org.lexevs.dao.database.constants.classifier.property.PropertyTypeClassifier;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterCollectionTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.property.parameter.InsertOrUpdatePropertyBean;
import org.lexevs.dao.database.ibatis.property.parameter.InsertPropertyLinkBean;
import org.lexevs.dao.database.ibatis.property.parameter.InsertPropertyMultiAttribBean;
import org.lexevs.dao.database.ibatis.versions.IbatisVersionsDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.classify.Classifier;
import org.springframework.util.Assert;


/**
 * The Class IbatisPropertyDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Cacheable(cacheName = "IbatisPropertyDaoCache")
public class IbatisPropertyDao extends AbstractIbatisDao implements PropertyDao {
	
	/** The property type classifier. */
	private Classifier<PropertyType,String> propertyTypeClassifier = new PropertyTypeClassifier();
		
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	public static String PROPERTY_NAMESPACE = "Property.";
	
	/** The INSER t_ propert y_ sql. */
	public static String INSERT_PROPERTY_SQL = PROPERTY_NAMESPACE + "insertProperty";
	
	public static String DELETE_ALL_CODINGSCHEME_PROPERTIES_OF_CODINGSCHEME_SQL = PROPERTY_NAMESPACE + "deleteCodingSchemePropertiesByCodingSchemeUId";
	
	/** The DELET e_ al l_ entit y_ propertie s_ o f_ codingschem e_ sql. */
	public static String DELETE_ALL_ENTITY_PROPERTIES_OF_CODINGSCHEME_SQL = PROPERTY_NAMESPACE + "deleteEntityPropertiesByCodingSchemeUId";

	public static String DELETE_ALL_RELATION_PROPERTIES_OF_CODINGSCHEME_SQL = PROPERTY_NAMESPACE + "deleteRelationPropertiesByCodingSchemeUId";
	
	public static String DELETE_ALL_PROPERTIES_OF_PARENT_SQL = PROPERTY_NAMESPACE + "deletePropertiesByParentUIdAndParentType";
	
	/** The INSER t_ propert y_ qualifie r_ sql. */
	public static String INSERT_PROPERTY_QUALIFIER_SQL = PROPERTY_NAMESPACE + "insertPropertyMultiAttrib";
	
	/** The INSER t_ propert y_ sourc e_ sql. */
	public static String INSERT_PROPERTY_SOURCE_SQL = PROPERTY_NAMESPACE + "insertPropertyMultiAttrib";
	
	/** The INSER t_ propert y_ usagecontex t_ sql. */
	public static String INSERT_PROPERTY_USAGECONTEXT_SQL = PROPERTY_NAMESPACE + "insertPropertyMultiAttrib";
	
	public static String INSERT_PROPERTY_MULTIATTRIB_SQL = PROPERTY_NAMESPACE + "insertPropertyMultiAttrib";
	
	/** The INSER t_ propertylin k_ sql. */
	public static String INSERT_PROPERTYLINK_SQL = PROPERTY_NAMESPACE + "insertPropertyLink";
	
	/** The GE t_ al l_ propertie s_ o f_ paren t_ sql. */
	public static String GET_ALL_PROPERTIES_OF_PARENT_SQL = PROPERTY_NAMESPACE +  "getPropertiesByParent";
	
	public static String GET_PROPERTIES_OF_PARENT_UIDS_SQL = PROPERTY_NAMESPACE +  "getPropertiesByParentUids";

	public static String GET_ALL_HISTORY_PROPERTY_UIDS_OF_PARENT_SQL = PROPERTY_NAMESPACE + "getAllHistoryPropertyIdsByParentUId";
	
	/** The GE t_ propert y_ i d_ sql. */
	public static String GET_PROPERTY_ID_SQL = PROPERTY_NAMESPACE + "getPropertyId";
	
	public static String GET_PROPERTY_ATTRIBUTES_BY_UID_SQL = PROPERTY_NAMESPACE + "getPropertyAttributesByUId";
	
	public static String GET_PROPERTY_MULTIATTRIB_BY_PROPERTY_ID_SQL = PROPERTY_NAMESPACE + "getPropertyMultiAttribById";
	
	public static String DELETE_PROPERTY_MULTIATTRIB_BY_PROPERTY_ID_SQL = PROPERTY_NAMESPACE + "deletePropertyMultiAttribByPropertyId";
	
	public static String UPDATE_PROPERTY_BY_UID_SQL = PROPERTY_NAMESPACE + "updatePropertyByUId";
		
	public static String UPDATE_PROPERTY_VERSIONABLE_ATTRIB_BY_UID_SQL = PROPERTY_NAMESPACE + "updatePropertyVersionableAttribByUId";	
	
	public static String DELETE_PROPERTY_BY_UID_SQL = PROPERTY_NAMESPACE + "deletePropertyByUId";
	
	public static String GET_PROPERTY_UID_BY_ID_AND_NAME = PROPERTY_NAMESPACE + "getPropertyUIdByPropIdAndName";
			
	private static String GET_PROPERTY_LATEST_REVISION_ID_BY_UID = PROPERTY_NAMESPACE + "getLatestPropertyRevisionIdByUId";

	private static String  GET_PROPERTY_BY_UID_SQL = PROPERTY_NAMESPACE + "getPropertyByUid";
	
	private static String GET_ENTRYSTATE_UID_BY_PROPERTY_UID_SQL = PROPERTY_NAMESPACE + "getEntryStateUIdByPropertyUId";
	
	private static String GET_PROPERY_BY_PROPERTY_UID_AND_REVISION_ID_SQL = PROPERTY_NAMESPACE + "getPropertyByUidAndRevisionId";

	private PropertyMultiAttributeClassifier propertyMultiAttributeClassifier = new PropertyMultiAttributeClassifier();
	
	/** The ibatis versions dao. */
	private IbatisVersionsDao ibatisVersionsDao;
	
	@Override
	public String getEntryStateUId(String codingSchemeUId, String propertyUId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		return (String) this.getSqlSessionTemplate().selectOne(
				GET_ENTRYSTATE_UID_BY_PROPERTY_UID_SQL,
				new PrefixedParameter(prefix, propertyUId));
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#insertBatchProperties(java.lang.String, org.lexevs.dao.database.access.property.PropertyDao.PropertyType, java.util.List)
	 */
	@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache","IbatisEntityDaoCache"})
	public void insertBatchProperties(
			final String codingSchemeId, 
			final PropertyType type,
			final List<PropertyBatchInsertItem> batch) {

		SqlSessionTemplate session = this.getSqlSessionBatchTemplate();

				
				for(PropertyBatchInsertItem item : batch){
					String propertyId = createUniqueId();
					
					insertProperty(
							codingSchemeId,
							item.getParentId(),
							propertyId,
							type,
							item.getProperty(),
							session);
				}
				
				session.commit();
				session.clearCache();
	}
	
	/**
	 * Insert batch properties.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param type the type
	 * @param batch the batch
	 * @param inserter the inserter
	 */
	@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache","IbatisEntityDaoCache"})
	public void insertBatchProperties(
			final String codingSchemeId, 
			final PropertyType type,
			final List<PropertyBatchInsertItem> batch, 
			SqlSessionTemplate session) {
		String propertyId = this.createUniqueId();
		
		for(PropertyBatchInsertItem item : batch){
			this.insertProperty(
					codingSchemeId, 
					item.getParentId(), 
					propertyId, 
					type, 
					item.getProperty(), 
					session);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#insertProperty(java.lang.String, java.lang.String, org.lexevs.dao.database.access.property.PropertyDao.PropertyType, org.LexGrid.commonTypes.Property)
	 */
	@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache","IbatisEntityDaoCache"})
	public String insertProperty(
			String codingSchemeUId,
			String parentUId, 
			PropertyType type, 
			Property property) {
		String propertyUId = this.createUniqueId();
		
		return this.insertProperty(
				codingSchemeUId, 
				parentUId, 
				propertyUId, 
				type, 
				property, 
				this.getSqlSessionTemplate());	
	}

	public String insertHistoryProperty(String codingSchemeUId,
			String propertyUId, Property property) {
		
		return this.doInsertHistoryProperty(
				codingSchemeUId, 
				propertyUId, 
				property, 
				this.getSqlSessionTemplate());	
	}
	
	@SuppressWarnings("unchecked")
	//shouldn't need to clear any caches on a 'get'...leaving this here just incase.
	//@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache","IbatisEntityDaoCache"})
	public List<Property> getPropertiesOfParents(
			String codingSchemeId, 
			List<String> propertyNames, 
			List<String> propertyTypes,
			List<String> parentUids) {
		return this.getSqlSessionTemplate().selectList(GET_PROPERTIES_OF_PARENT_UIDS_SQL, 
				new PrefixedParameterCollectionTriple(
						this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId),
						this.propertyTypeClassifier.classify(PropertyType.ENTITY),
						propertyNames,
						propertyTypes,
						parentUids));
	}
	
	public Property getPropertyByUid(
			String codingSchemeId, 
			String propertyUid) {
		return (Property)this.getSqlSessionTemplate().selectOne(GET_PROPERTY_BY_UID_SQL, 
				new PrefixedParameter(
						this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId),
						propertyUid));
	}

	@Override
	public List<Property> getPropertiesOfParents(String codingSchemeId,
			List<String> parentUids) {
		return this.getPropertiesOfParents(codingSchemeId, null, null, parentUids);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#getAllPropertiesOfParent(java.lang.String, java.lang.String, org.lexevs.dao.database.access.property.PropertyDao.PropertyType)
	 */
	@SuppressWarnings("unchecked")
	public List<Property> getAllPropertiesOfParent(String codingSchemeId,
			String parentId, PropertyType type) {
		return this.getSqlSessionTemplate().selectList(GET_ALL_PROPERTIES_OF_PARENT_SQL, 
				new PrefixedParameterTuple(
						this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId),
						this.propertyTypeClassifier.classify(type),
						parentId));
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAllHistoryPropertyUidsOfParentByRevisionId(
			String codingSchemeUid,
			String parentId, 
			String revisionId) {
		
		PrefixedParameterTuple param = new PrefixedParameterTuple(
				this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid),
				parentId,
				revisionId);
		
		return this.getSqlSessionTemplate().selectList(GET_ALL_HISTORY_PROPERTY_UIDS_OF_PARENT_SQL, 
				param);
	}
	
	@Override
	public Property getHistoryPropertyByRevisionId(
			String codingSchemeUid,
			String propertyUid, 
			String revisionId) {
		
		PrefixedParameterTuple param = new PrefixedParameterTuple(
				this.getPrefixResolver().resolvePrefixForHistoryCodingScheme(codingSchemeUid),
				propertyUid,
				revisionId);
		
		param.setActualTableSetPrefix(this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid));
		
		return (Property)this.getSqlSessionTemplate().
			selectOne(
					GET_PROPERY_BY_PROPERTY_UID_AND_REVISION_ID_SQL, 
					param);
	}

	@SuppressWarnings("unchecked")
	protected <T> List<T> doGetPropertyMultiAttrib(String prefix, String propertyId, Class<T> multiAttrib){
		return this.getSqlSessionTemplate().selectList(GET_PROPERTY_MULTIATTRIB_BY_PROPERTY_ID_SQL, 
				new PrefixedParameterTuple(prefix, propertyId, this.propertyMultiAttributeClassifier.classify(multiAttrib)));
	}
	
	protected String getPropertyTypeString(Property property) {
		org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType propertyType = 
			DaoUtility.propertyClassToTypeMap.get(property.getClass());
		
		return DaoUtility.propertyTypeToStringMap.get(propertyType);
	}
	
	/**
	 * Insert property.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param referenceUId the entity code id
	 * @param referenceType the type
	 * @param property the property
	 * @param session the inserter
	 * 
	 * @return the string
	 */
	@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache","IbatisEntityDaoCache"})
	public String insertProperty(
			String codingSchemeUId,
			String referenceUId, 
			String propertyUId,
			PropertyType referenceType, 
			Property property, 
			SqlSessionTemplate session) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		String entryStateUId = this.createUniqueId();
		
		if(StringUtils.isBlank(property.getPropertyType())){
			property.setPropertyType(
					getPropertyTypeString(property));
		}
		
		if (property.getPropertyId() == null
				|| property.getPropertyId().trim().equals("")) {
			property.setPropertyId(DatabaseConstants.GENERATED_ID_PREFIX  + this.createRandomIdentifier());
		}
		
		if (property.getEntryState() != null) {
			this.ibatisVersionsDao.insertEntryState(
					codingSchemeUId, 
					entryStateUId,
					propertyUId, 
					EntryStateType.PROPERTY, 
					null, 
					property
							.getEntryState(), 
					session);
		}
		
		session.insert(INSERT_PROPERTY_SQL,
				buildInsertPropertyBean(
						prefix,
						referenceUId,
						propertyUId,
						entryStateUId,
						referenceType,
						property
				));

		for(Source source : property.getSource()) {
			String propertySourceUId = this.createUniqueId();
			this.doInsertPropertySource(prefix, propertyUId, propertySourceUId, entryStateUId, source, session);
		}
		
		for(String context : property.getUsageContext()) {
			String propertyUsageContextUId = this.createUniqueId();
			this.doInsertPropertyUsageContext(prefix, propertyUId, propertyUsageContextUId, entryStateUId, context, session);
		}
		
		for(PropertyQualifier qual : property.getPropertyQualifier()) {
			String propertyQualifierUId = this.createUniqueId();
			this.doInsertPropertyQualifier(prefix, propertyUId, propertyQualifierUId, entryStateUId, qual, session);
		}
		
		return propertyUId;
		
	}
	
	protected String doInsertHistoryProperty(
			String codingSchemeUId,
			String propertyUId,
			Property property, 
			SqlSessionTemplate sqlSessionTemplate) {
		
		Assert.notNull(propertyUId);

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		String historyPrefix = this.getPrefixResolver().resolvePrefixForHistoryCodingScheme(codingSchemeUId);
		
		InsertOrUpdatePropertyBean propertyData = (InsertOrUpdatePropertyBean) this.getSqlSessionTemplate()
				.selectOne(GET_PROPERTY_ATTRIBUTES_BY_UID_SQL,
						new PrefixedParameter(prefix, propertyUId));

		propertyData.setPrefix(historyPrefix);
		
		sqlSessionTemplate.insert(INSERT_PROPERTY_SQL, propertyData);
		
		for (InsertPropertyMultiAttribBean propMultiAttrib : propertyData.getPropertyMultiAttribList())
		{
			if (propMultiAttrib.getUId() != null) {
				propMultiAttrib.setPrefix(historyPrefix);
				propMultiAttrib.setEntryStateUId(propertyData.getEntryStateUId());

				sqlSessionTemplate.insert(
						INSERT_PROPERTY_MULTIATTRIB_SQL, propMultiAttrib);
			}
		}

		return propertyData.getEntryStateUId();
	}


	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#updateProperty(java.lang.String, java.lang.String, java.lang.String, org.lexevs.dao.database.access.property.PropertyDao.PropertyType, org.LexGrid.commonTypes.Property)
	 */
	@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache","IbatisEntityDaoCache"})
	public String updateProperty(String codingSchemeUId, String parentUId,
			String propertyUId, PropertyType type, Property property) {
		Assert.hasText(
				property.getPropertyId(),
				"Property must have a populated PropertyId " +
				"in order to be updated.");
		
		String entryStateUId = this.createUniqueId();
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);

		this.getSqlSessionTemplate().update(
				UPDATE_PROPERTY_BY_UID_SQL, 
				this.buildInsertPropertyBean(
						prefix, 
						parentUId, 
						propertyUId, 
						entryStateUId, 
						type, 
						property));	

		if(property.getSourceCount() > 0) {
			String multiAttributeType = this.propertyMultiAttributeClassifier.classify(Source.class);
			this.deleteMultiAttribOfProperty(codingSchemeUId, propertyUId, multiAttributeType);
			for(Source source : property.getSource()) {
				this.insertPropertySource(codingSchemeUId, propertyUId, source);
			}
		}
		
		if(property.getUsageContextCount() > 0) {
			String multiAttributeType = this.propertyMultiAttributeClassifier.classify(String.class);
			this.deleteMultiAttribOfProperty(codingSchemeUId, propertyUId, multiAttributeType);
			for(String usageContext : property.getUsageContext()) {
				this.insertPropertyUsageContext(codingSchemeUId, propertyUId, usageContext);
			}
		}
		
		if(property.getPropertyQualifierCount() > 0) {
			String multiAttributeType = this.propertyMultiAttributeClassifier.classify(PropertyQualifier.class);
			this.deleteMultiAttribOfProperty(codingSchemeUId, propertyUId, multiAttributeType);
			for(PropertyQualifier qualifier : property.getPropertyQualifier()) {
				this.insertPropertyQualifier(codingSchemeUId, propertyUId, qualifier);
			}
		}
		
		return entryStateUId;
	}
	
	private void deleteMultiAttribOfProperty(String codingSchemeUId,
			String propertyUId, String multiAttributeType) {
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		this.getSqlSessionTemplate().delete(
				DELETE_PROPERTY_MULTIATTRIB_BY_PROPERTY_ID_SQL, 
				new PrefixedParameterTuple(prefix,propertyUId,multiAttributeType));
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#updateProperty(java.lang.String, java.lang.String, java.lang.String, org.lexevs.dao.database.access.property.PropertyDao.PropertyType, org.LexGrid.commonTypes.Property)
	 */
	@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache","IbatisEntityDaoCache"})
	public String updatePropertyVersionableAttrib(String codingSchemeUId,
			String propertyUId, Property property) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		String entryStateUId = this.createUniqueId();
		
		this.getSqlSessionTemplate().update(
				UPDATE_PROPERTY_VERSIONABLE_ATTRIB_BY_UID_SQL, 
				this.buildInsertPropertyBean(
						prefix, 
						null, 
						propertyUId, 
						entryStateUId, 
						null, 
						property));	
		
		return entryStateUId;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#insertPropertyQualifier(java.lang.String, java.lang.String, org.LexGrid.commonTypes.PropertyQualifier)
	 */
	@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache","IbatisEntityDaoCache"})
	public void insertPropertyQualifier(String codingSchemeId, String propertyId, PropertyQualifier propertyQualifier) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		String propertyQualifierId = this.createUniqueId();
		this.doInsertPropertyQualifier(
				prefix, propertyId, 
				propertyQualifierId, 
				null,
				propertyQualifier, 
				this.getSqlSessionTemplate());	
	}
	
	/**
	 * Insert property qualifier.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param propertyUId the property id
	 * @param propertyQualifier the property qualifier
	 * @param session the inserter
	 */
	protected void doInsertPropertyQualifier(
			final String prefix, 
			final String propertyUId, 
			final String propertyQualifierUId, 
			final String entryStateUId,
			final PropertyQualifier propertyQualifier, 
			final SqlSessionTemplate session) {

				session.insert(INSERT_PROPERTY_QUALIFIER_SQL, 
						buildInsertPropertyQualifierBean(
								prefix,
								propertyUId, 
								propertyQualifierUId, 
								entryStateUId,
								propertyQualifier));
	}

	@Override
	public void insertPropertySource(String codingSchemeId, String propertyId,
			Source source) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		String propertySourceId = this.createUniqueId();
		
		this.doInsertPropertySource(
				prefix, 
				propertyId, 
				propertySourceId, 
				null,
				source,
				this.getSqlSessionTemplate());
	}

	/**
	 * Insert property source.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param propertyUId the property id
	 * @param source the source
	 * @param session the inserter
	 */
	protected void doInsertPropertySource(
			final String prefix, 
			final String propertyUId, 
			final String propertySourceUId, 
			final String entryStateUId,
			final Source source, 
			final SqlSessionTemplate session) {

				session.insert(INSERT_PROPERTY_SOURCE_SQL, 
						buildInsertPropertySourceBean(
								prefix,
								propertyUId, 
								propertySourceUId, 
								entryStateUId, 
								source));
	}
	
	/**
	 * Insert property usage context.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param propertyUId the property id
	 * @param usageContext the usage context
	 * @param session the inserter
	 */
	protected void doInsertPropertyUsageContext(
			final String prefix, 
			final String propertyUId,
			final String propertyUsageContextUId, 
			final String entryStateUId,
			final String usageContext, 
			final SqlSessionTemplate session) {

				session.insert(INSERT_PROPERTY_USAGECONTEXT_SQL, 
						buildInsertPropertyUsageContextBean(
								prefix,
								propertyUId, 
								propertyUsageContextUId, 
								entryStateUId, 
								usageContext));
	}
	
	@Override
	@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache","IbatisEntityDaoCache"})
	public void insertPropertyUsageContext(
			String codingSchemeId,
			String propertyId, 
			String usageContext) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		String usageContextId = this.createUniqueId();
		this.doInsertPropertyUsageContext(
				prefix, 
				propertyId, 
				usageContextId,
				null, 
				usageContext,
				this.getSqlSessionTemplate());
	}
	
	/**
	 * Do insert property link.
	 * 
	 * @param prefix the prefix
	 * @param entityUId the entity id
	 * @param propertyLinkUId the property link id
	 * @param link the link
	 * @param sourcePropertyUId the source property id
	 * @param targetPropertyUId the target property id
	 * @param session the inserter
	 */
	public void doInsertPropertyLink(final String prefix, 
			final String entityUId,
			final String propertyLinkUId,
			final String link, 
			final String sourcePropertyUId,
			final String targetPropertyUId,
			final SqlSessionTemplate session) {
		final InsertPropertyLinkBean bean = new InsertPropertyLinkBean();
		bean.setPrefix(prefix);
		bean.setLink(link);
		bean.setUId(propertyLinkUId);
		bean.setEntityUId(entityUId);
		bean.setSourcePropertyUId(sourcePropertyUId);
		bean.setTargetPropertyUId(targetPropertyUId);
		bean.setEntryStateUId(this.createUniqueId());


				session.insert(INSERT_PROPERTYLINK_SQL, 
						bean);

	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#insertPropertyLink(java.lang.String, java.lang.String, org.LexGrid.concepts.PropertyLink)
	 */
	@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache","IbatisEntityDaoCache"})
	public void insertPropertyLink(String codingSchemeUId, String parentUId,
			PropertyLink propertyLink) {
		String propertyLinkUId = this.createUniqueId();
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		String sourcePropertyUId = this.getPropertyUIdFromParentUIdAndPropId(codingSchemeUId, parentUId, propertyLink.getSourceProperty());
		String targetPropertyUId = this.getPropertyUIdFromParentUIdAndPropId(codingSchemeUId, parentUId, propertyLink.getTargetProperty());
		
		this.doInsertPropertyLink(prefix, parentUId, propertyLinkUId, 
				propertyLink.getPropertyLink(), sourcePropertyUId, targetPropertyUId, this.getSqlSessionTemplate());
	}
	
	public void deleteAllCodingSchemePropertiesOfCodingScheme(
			String codingSchemeUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		this.getSqlSessionTemplate().delete(
				DELETE_ALL_CODINGSCHEME_PROPERTIES_OF_CODINGSCHEME_SQL,
				new PrefixedParameterTuple(prefix, this.propertyTypeClassifier
						.classify(PropertyType.CODINGSCHEME), codingSchemeUId));
		
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#deleteAllEntityPropertiesOfCodingScheme(java.lang.String)
	 */
	@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache","IbatisEntityDaoCache"})
	public void deleteAllEntityPropertiesOfCodingScheme(String codingSchemeUId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		this.getSqlSessionTemplate().delete(DELETE_ALL_ENTITY_PROPERTIES_OF_CODINGSCHEME_SQL, 
				new PrefixedParameterTuple(prefix, 
						this.propertyTypeClassifier.classify(PropertyType.ENTITY), codingSchemeUId));
	}
	
	@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache","IbatisEntityDaoCache"})
	public void deleteAllRelationPropertiesOfCodingScheme(String codingSchemeUId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);

		this.getSqlSessionTemplate().delete(DELETE_ALL_RELATION_PROPERTIES_OF_CODINGSCHEME_SQL, 
				new PrefixedParameterTuple(prefix, 
						this.propertyTypeClassifier.classify(PropertyType.RELATION), codingSchemeUId));
	}
	
	@Override
	@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache","IbatisEntityDaoCache"})
	public void deleteAllPropertiesOfParent(String codingSchemeUId,  
			String parentUId, PropertyType parentType) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		this.getSqlSessionTemplate().delete(DELETE_ALL_PROPERTIES_OF_PARENT_SQL, 
				new PrefixedParameterTuple(prefix, 
						this.propertyTypeClassifier.classify(parentType), parentUId));
	}
	
	/**
	 * Gets the property id from parent id and prop id.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param parentId the parent id
	 * @param propId the prop id
	 * 
	 * @return the property id from parent id and prop id
	 */
	protected String getPropertyUIdFromParentUIdAndPropId(String codingSchemeId, String parentId, String propId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return (String) this.getSqlSessionTemplate().selectOne(GET_PROPERTY_ID_SQL, 
				new PrefixedParameterTuple(prefix, parentId, propId));
	}

	/**
	 * Builds the insert property bean.
	 * 
	 * @param prefix the prefix
	 * @param parentUId the reference unique id.
	 * @param propertyUId the property id
	 * @param entryStateUId the entry state id
	 * @param referenceType the type
	 * @param property the property
	 * 
	 * @return the insert property bean
	 */
	protected InsertOrUpdatePropertyBean buildInsertPropertyBean(String prefix, String parentUId, String propertyUId, 
			String entryStateUId, PropertyType referenceType, Property property){
		InsertOrUpdatePropertyBean bean = new InsertOrUpdatePropertyBean();
		bean.setPrefix(prefix);
		bean.setParentType(this.propertyTypeClassifier.classify(referenceType));
		bean.setParentUId(parentUId);
		bean.setUId(propertyUId);
		bean.setEntryStateUId(entryStateUId);
		bean.setProperty(property);
		bean.setPropertyId(property.getPropertyId());
		bean.setPropertyType(property.getPropertyType());
		bean.setPropertyName(property.getPropertyName());
		bean.setLanguage(property.getLanguage());
		bean.setFormat(property.getValue().getDataType());
		bean.setPropertyValue(property.getValue().getContent());
		bean.setIsActive(property.getIsActive());
		bean.setOwner(property.getOwner());
		bean.setStatus(property.getStatus());
		bean.setEffectiveDate(property.getEffectiveDate());
		bean.setExpirationDate(property.getExpirationDate());

		
		return bean;
	}
	
	/**
	 * Builds the insert property qualifier bean.
	 * 
	 * @param prefix the prefix
	 * @param propertyId the property id
	 * @param qualifierId the qualifier id
	 * @param propertyQualifier the property qualifier
	 * 
	 * @return the insert property multi attrib bean
	 */
	protected InsertPropertyMultiAttribBean buildInsertPropertyQualifierBean(
			String prefix,
			String propertyId, 
			String qualifierId, 
			String entryStateId,
			PropertyQualifier propertyQualifier){
		InsertPropertyMultiAttribBean bean = new InsertPropertyMultiAttribBean();
		bean.setPrefix(prefix);
		bean.setUId(qualifierId);
		bean.setPropertyUId(propertyId);
		bean.setAttributeId(propertyQualifier.getPropertyQualifierName());
		bean.setAttributeValue(propertyQualifier.getValue() != null ? propertyQualifier.getValue().getContent(): null);
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_QUALIFIER);
		bean.setQualifierType(propertyQualifier.getPropertyQualifierType());
		bean.setEntryStateUId(entryStateId);

		return bean;
	}
	
	/**
	 * Builds the insert property usage context bean.
	 * 
	 * @param prefix the prefix
	 * @param propertyId the property id
	 * @param qualifierId the qualifier id
	 * @param usageContext the usage context
	 * 
	 * @return the insert property multi attrib bean
	 */
	protected InsertPropertyMultiAttribBean buildInsertPropertyUsageContextBean(String prefix, String propertyId, 
			String qualifierId, String entryStateId, String usageContext){
		InsertPropertyMultiAttribBean bean = new InsertPropertyMultiAttribBean();
		bean.setPrefix(prefix);
		bean.setUId(qualifierId);
		bean.setPropertyUId(propertyId);
		bean.setAttributeValue(usageContext);
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
		bean.setEntryStateUId(entryStateId);

		return bean;
	}
	
	/**
	 * Builds the insert property source bean.
	 * 
	 * @param prefix the prefix
	 * @param propertyId the property id
	 * @param sourceId the source id
	 * @param source the source
	 * 
	 * @return the insert property multi attrib bean
	 */
	protected InsertPropertyMultiAttribBean buildInsertPropertySourceBean(
			String prefix, String propertyId, String sourceId, String entryStateId, Source source){
		InsertPropertyMultiAttribBean bean = new InsertPropertyMultiAttribBean();
		bean.setPrefix(prefix);
		bean.setUId(sourceId);
		bean.setPropertyUId(propertyId);
		bean.setAttributeValue(source.getContent());
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_SOURCE);
		bean.setRole(source.getRole());
		bean.setSubRef(source.getSubRef());
		bean.setEntryStateUId(entryStateId);

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
	 * Sets the property type classifier.
	 * 
	 * @param propertyTypeClassifier the property type classifier
	 */
	public void setPropertyTypeClassifier(Classifier<PropertyType,String> propertyTypeClassifier) {
		this.propertyTypeClassifier = propertyTypeClassifier;
	}

	/**
	 * Gets the property type classifier.
	 * 
	 * @return the property type classifier
	 */
	public Classifier<PropertyType,String> getPropertyTypeClassifier() {
		return propertyTypeClassifier;
	}

	@Override
	public String getPropertyUIdByPropertyIdAndName(String codingSchemeUId,
			String referenceUId, String propertyId, String propertyName) {
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		return (String) this.getSqlSessionTemplate().selectOne(
				GET_PROPERTY_UID_BY_ID_AND_NAME,
				new PrefixedParameterTriple(prefix, referenceUId, propertyId,
						propertyName));
	}

	@Override
	@ClearCache(clearCaches={"IbatisCodingSchemeDaoCache","IbatisEntityDaoCache"})
	public void removePropertyByUId(String codingSchemeUId, String propertyUId) {
		String prefix = 
			this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		this.getSqlSessionTemplate().
			delete(DELETE_PROPERTY_BY_UID_SQL, new PrefixedParameter(prefix, propertyUId));
	}

	@Override
	public String getLatestRevision(String csUId, String propertyUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUId);
		
		String revId = (String) this.getSqlSessionTemplate().selectOne(
				GET_PROPERTY_LATEST_REVISION_ID_BY_UID, 
				new PrefixedParameter(prefix, propertyUId));
		
		return revId;
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
	
	public Property getPropertyByUId(String vsPropertyUId) {
		
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		InsertOrUpdatePropertyBean propertyBean = (InsertOrUpdatePropertyBean) this
				.getSqlSessionTemplate().selectOne(
						GET_PROPERTY_ATTRIBUTES_BY_UID_SQL,
						new PrefixedParameter(prefix, vsPropertyUId));
		
		return getProperty(propertyBean);
	}
	
	private Property getProperty(InsertOrUpdatePropertyBean propertyBean) {
		
		Property property = propertyBean.getProperty();
		
		Property prop = null;
		
		if (SQLTableConstants.TBLCOLVAL_PRESENTATION.equals(property
				.getPropertyType())) {
			
			prop = new Presentation();
			
			((Presentation)prop).setIsPreferred(propertyBean.getIsPreferred());
			((Presentation)prop).setMatchIfNoContext(propertyBean.getMatchIfNoContext());
			((Presentation)prop).setDegreeOfFidelity(propertyBean.getDegreeOfFidelity());
			((Presentation)prop).setRepresentationalForm(propertyBean.getRepresentationalForm());
		} else if (SQLTableConstants.TBLCOLVAL_DEFINITION.equals(property
				.getPropertyType())) {
			
			prop = new Definition();
			
			((Definition)prop).setIsPreferred(propertyBean.getIsPreferred());
		} else if (SQLTableConstants.TBLCOLVAL_COMMENT.equals(property
				.getPropertyType())) {
			
			prop = new Comment();
			
		} else {
			
			prop = new Property();
		}
		
		prop.setPropertyId(property.getPropertyId());
		prop.setPropertyName(property.getPropertyName());
		prop.setPropertyType(property.getPropertyType());
		prop.setEffectiveDate(property.getEffectiveDate());
		prop.setExpirationDate(property.getExpirationDate());
		prop.setIsActive(property.getIsActive());
		prop.setOwner(property.getOwner());
		prop.setStatus(property.getStatus());
		prop.setLanguage(property.getLanguage());
		prop.setValue(property.getValue());
		
		List<InsertPropertyMultiAttribBean> multiAttribList = propertyBean.getPropertyMultiAttribList();
		
		for (InsertPropertyMultiAttribBean multiAttribBean : multiAttribList) {

			if (SQLTableConstants.TBLCOLVAL_SOURCE.equals(multiAttribBean
					.getAttributeType())) {
				Source source = new Source();

				source.setRole(multiAttribBean.getRole());
				source.setSubRef(multiAttribBean.getSubRef());
				source.setContent(multiAttribBean.getAttributeValue());

				prop.addSource(source);
			} else if (SQLTableConstants.TBLCOLVAL_QUALIFIER
					.equals(multiAttribBean.getAttributeType())) {

				PropertyQualifier qualifier = new PropertyQualifier();

				qualifier.setPropertyQualifierName(multiAttribBean
						.getAttributeId());
				qualifier
						.setPropertyQualifierType(SQLTableConstants.TBLCOLVAL_QUALIFIER);

				if (multiAttribBean.getAttributeValue() != null) {
					Text value = new Text();

					value.setContent(multiAttribBean.getAttributeValue());

					qualifier.setValue(value);
				}
				prop.addPropertyQualifier(qualifier);
			} else if (SQLTableConstants.TBLCOLVAL_USAGECONTEXT
					.equals(multiAttribBean.getAttributeType())) {
				
				prop.addUsageContext(multiAttribBean.getAttributeValue());
			}
		}
		
		return prop;
	}	
	
	@Override
	public boolean entryStateExists(String codingSchemeUId, String entryStateUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		return super.entryStateExists(prefix, entryStateUId);
	}
}
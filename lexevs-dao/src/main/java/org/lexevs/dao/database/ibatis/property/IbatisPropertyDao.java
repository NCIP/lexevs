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
package org.lexevs.dao.database.ibatis.property;

import java.sql.SQLException;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.property.batch.PropertyBatchInsertItem;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.constants.classifier.property.EntryStateTypeClassifier;
import org.lexevs.dao.database.constants.classifier.property.PropertyMultiAttributeClassifier;
import org.lexevs.dao.database.constants.classifier.property.PropertyTypeClassifier;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterCollection;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.property.parameter.InsertOrUpdatePropertyBean;
import org.lexevs.dao.database.ibatis.property.parameter.InsertPropertyLinkBean;
import org.lexevs.dao.database.ibatis.property.parameter.InsertPropertyMultiAttribBean;
import org.lexevs.dao.database.ibatis.versions.IbatisVersionsDao;
import org.lexevs.dao.database.inserter.BatchInserter;
import org.lexevs.dao.database.inserter.Inserter;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.batch.classify.Classifier;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.util.Assert;

import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * The Class IbatisPropertyDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IbatisPropertyDao extends AbstractIbatisDao implements PropertyDao {
	
	/** The property type classifier. */
	private Classifier<PropertyType,String> propertyTypeClassifier = new PropertyTypeClassifier();
	
	private Classifier<EntryStateType,String> entryStateTypeClassifier = new EntryStateTypeClassifier();
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	public static String PROPERTY_NAMESPACE = "Property.";
	
	/** The INSER t_ propert y_ sql. */
	public static String INSERT_PROPERTY_SQL = PROPERTY_NAMESPACE + "insertProperty";
	
	public static String DELETE_ALL_CODINGSCHEME_PROPERTIES_OF_CODINGSCHEME_SQL = PROPERTY_NAMESPACE + "deleteCodingSchemePropertiesByCodingSchemeUId";

	public static String DELETE_ALL_CODINGSCHEME_PROPERTY_MULTIATTRIB_OF_CODINGSCHEME_SQL = PROPERTY_NAMESPACE + "deleteCodingSchemePropertyMultiAttribByCodingSchemeUId";
	
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
	
	/** The INSER t_ propertylin k_ sql. */
	public static String INSERT_PROPERTYLINK_SQL = PROPERTY_NAMESPACE + "insertPropertyLink";
	
	/** The GE t_ al l_ propertie s_ o f_ paren t_ sql. */
	public static String GET_ALL_PROPERTIES_OF_PARENT_SQL = PROPERTY_NAMESPACE +  "getPropertiesByParent";
	
	public static String GET_PROPERTIES_OF_PARENT_UIDS_SQL = PROPERTY_NAMESPACE +  "getPropertiesByParentUids";

	public static String GET_ALL_PROPERTIES_OF_PARENT_BY_REVISION_SQL = PROPERTY_NAMESPACE + "getPropertiesByParentAndRevisionId";
	
	/** The GE t_ propert y_ i d_ sql. */
	public static String GET_PROPERTY_ID_SQL = PROPERTY_NAMESPACE + "getPropertyId";
	
	public static String GET_PROPERTY_ATTRIBUTES_BY_UID_SQL = PROPERTY_NAMESPACE + "getPropertyAttributesByUId";
	
	public static String GET_PROPERTY_MULTIATTRIB_BY_PROPERTY_ID_SQL = PROPERTY_NAMESPACE + "getPropertyMultiAttribById";
	
	public static String UPDATE_PROPERTY_BY_UID_SQL = PROPERTY_NAMESPACE + "updatePropertyByUId";
		
	public static String UPDATE_PROPERTY_VERSIONABLE_ATTRIB_BY_UID_SQL = PROPERTY_NAMESPACE + "updatePropertyVersionableAttribByUId";	
	
	public static String DELETE_PROPERTY_BY_UID_SQL = PROPERTY_NAMESPACE + "deletePropertyByUId";
	
	public static String GET_PROPERTY_UID_BY_ID_AND_NAME = PROPERTY_NAMESPACE + "getPropertyUIdByPropIdAndName";
			
	private static String GET_PROPERTY_LATEST_REVISION_ID_BY_UID = PROPERTY_NAMESPACE + "getLatestPropertyRevisionIdByUId";
	
	PropertyMultiAttributeClassifier propertyMultiAttributeClassifier = new PropertyMultiAttributeClassifier();
	
	/** The ibatis versions dao. */
	private IbatisVersionsDao ibatisVersionsDao;

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#insertBatchProperties(java.lang.String, org.lexevs.dao.database.access.property.PropertyDao.PropertyType, java.util.List)
	 */
	public void insertBatchProperties(
			final String codingSchemeId, 
			final PropertyType type,
			final List<PropertyBatchInsertItem> batch) {
		final String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){

			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {
				BatchInserter inserter = getBatchTemplateInserter(executor);
				
				inserter.startBatch();
				
				for(PropertyBatchInsertItem item : batch){
					String propertyId = createUniqueId();
					
					doInsertProperty(
							prefix,
							item.getParentId(),
							propertyId,
							type,
							item.getProperty(),
							inserter);
				}
				
				inserter.executeBatch();
				
				return null; 
			}	
		});
	}
	
	/**
	 * Insert batch properties.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param type the type
	 * @param batch the batch
	 * @param inserter the inserter
	 */
	public void insertBatchProperties(
			final String codingSchemeId, 
			final PropertyType type,
			final List<PropertyBatchInsertItem> batch, 
			BatchInserter inserter) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		String propertyId = this.createUniqueId();
		
		for(PropertyBatchInsertItem item : batch){
			this.doInsertProperty(
					prefix, 
					item.getParentId(), 
					propertyId, 
					type, 
					item.getProperty(), 
					inserter);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#insertProperty(java.lang.String, java.lang.String, org.lexevs.dao.database.access.property.PropertyDao.PropertyType, org.LexGrid.commonTypes.Property)
	 */
	public String insertProperty(String codingSchemeUId,
			String parentUId, 
			PropertyType type, 
			Property property) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		String propertyUId = this.createUniqueId();
		
		return this.doInsertProperty(
				prefix, 
				parentUId, 
				propertyUId, 
				type, 
				property, 
				this.getNonBatchTemplateInserter());	
	}
	
	public String insertHistoryProperty(String codingSchemeUId,
			String propertyUId, Property property) {
		
		return this.doInsertHistoryProperty(
				codingSchemeUId, 
				propertyUId, 
				property, 
				this.getNonBatchTemplateInserter());	
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Property> getPropertiesOfParents(String codingSchemeId,
			List<String> parentUids) {
		return this.getSqlMapClientTemplate().queryForList(GET_PROPERTIES_OF_PARENT_UIDS_SQL, 
				new PrefixedParameterCollection(
						this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId),
						this.propertyTypeClassifier.classify(PropertyType.ENTITY),
						parentUids));
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#getAllPropertiesOfParent(java.lang.String, java.lang.String, org.lexevs.dao.database.access.property.PropertyDao.PropertyType)
	 */
	@SuppressWarnings("unchecked")
	public List<Property> getAllPropertiesOfParent(String codingSchemeId,
			String parentId, PropertyType type) {
		return this.getSqlMapClientTemplate().queryForList(GET_ALL_PROPERTIES_OF_PARENT_SQL, 
				new PrefixedParameterTuple(
						this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId),
						this.propertyTypeClassifier.classify(type),
						parentId));
	}
	
	@SuppressWarnings("unchecked")
	public List<Property> getAllHistoryPropertiesOfParentByRevisionId(String codingSchemeId,
			String parentId, String revisionId, PropertyType type) {
		
		PrefixedParameterTriple param = new PrefixedParameterTriple(
				this.getPrefixResolver().resolveHistoryPrefix(),
				this.propertyTypeClassifier.classify(type),
				parentId,
				revisionId);
		
		String actualTablePrefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		param.setActualTableSetPrefix(actualTablePrefix);
		
		return this.getSqlMapClientTemplate().queryForList(GET_ALL_PROPERTIES_OF_PARENT_BY_REVISION_SQL, 
				param);
	}
	
	@SuppressWarnings("unchecked")
	protected <T> List<T> doGetPropertyMultiAttrib(String prefix, String propertyId, Class<T> multiAttrib){
		return this.getSqlMapClientTemplate().queryForList(GET_PROPERTY_MULTIATTRIB_BY_PROPERTY_ID_SQL, 
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
	 * @param inserter the inserter
	 * 
	 * @return the string
	 */
	public String doInsertProperty(
			String prefix,
			String referenceUId, 
			String propertyUId,
			PropertyType referenceType, 
			Property property, 
			Inserter inserter) {
		
		String entryStateUId = this.createUniqueId();
		
		if(StringUtils.isBlank(property.getPropertyType())){
			property.setPropertyType(
					getPropertyTypeString(property));
		}
		
		if (property.getEntryState() != null) {
			this.ibatisVersionsDao.insertEntryState(prefix, entryStateUId,
					propertyUId, entryStateTypeClassifier
							.classify(EntryStateType.PROPERTY), null, property
							.getEntryState(), inserter);
		}
		
		inserter.insert(INSERT_PROPERTY_SQL,
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
			this.doInsertPropertySource(prefix, propertyUId, propertySourceUId, entryStateUId, source, inserter);
		}
		
		for(String context : property.getUsageContext()) {
			String propertyUsageContextUId = this.createUniqueId();
			this.doInsertPropertyUsageContext(prefix, propertyUId, propertyUsageContextUId, entryStateUId, context, inserter);
		}
		
		for(PropertyQualifier qual : property.getPropertyQualifier()) {
			String propertyQualifierUId = this.createUniqueId();
			this.doInsertPropertyQualifier(prefix, propertyUId, propertyQualifierUId, entryStateUId, qual, inserter);
		}
		
		return propertyUId;
		
	}
	
	protected String doInsertHistoryProperty(String codingSchemeUId,
			String propertyUId,
			Property property, 
			Inserter inserter) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		String historyPrefix = this.getPrefixResolver().resolvePrefixForHistoryCodingScheme(codingSchemeUId);
		
		InsertOrUpdatePropertyBean propertyData = (InsertOrUpdatePropertyBean) this.getSqlMapClientTemplate()
				.queryForObject(GET_PROPERTY_ATTRIBUTES_BY_UID_SQL,
						new PrefixedParameter(prefix, propertyUId));
		
		inserter.insert(INSERT_PROPERTY_SQL, 
				buildInsertPropertyBean(
						historyPrefix,
						propertyData.getParentUId(),
						propertyData.getUId(),
						propertyData.getEntryStateUId(),
						PropertyTypeClassifier.getPropertyType(propertyData.getParentType()),
						propertyData.getProperty()
						));

		if (!entryStateExists(prefix, propertyData.getEntryStateUId())) {

			EntryState entryState = new EntryState();

			entryState.setChangeType(ChangeType.NEW);
			entryState.setRelativeOrder(0L);

			ibatisVersionsDao.insertEntryState(propertyData.getEntryStateUId(),
					propertyData.getUId(), entryStateTypeClassifier
							.classify(EntryStateType.PROPERTY), null,
					entryState);
		}
		
		return propertyData.getEntryStateUId();
	}


	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#updateProperty(java.lang.String, java.lang.String, java.lang.String, org.lexevs.dao.database.access.property.PropertyDao.PropertyType, org.LexGrid.commonTypes.Property)
	 */
	public void updateProperty(String codingSchemeUId, String parentUId,
			String propertyUId, PropertyType type, Property property) {
		Assert.hasText(
				property.getPropertyId(),
				"Property must have a populated PropertyId " +
				"in order to be updated.");
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		/*String propertyGuid = this.getPropertyUIdFromParentUIdAndPropId(
				codingSchemeUId, 
				parentUId, 
				property.getPropertyId());*/
		String entryStateUId = this.createUniqueId();
		
		this.getSqlMapClientTemplate().update(
				UPDATE_PROPERTY_BY_UID_SQL, 
				this.buildInsertPropertyBean(
						prefix, 
						parentUId, 
						propertyUId, 
						entryStateUId, 
						type, 
						property),
						1);	
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#updateProperty(java.lang.String, java.lang.String, java.lang.String, org.lexevs.dao.database.access.property.PropertyDao.PropertyType, org.LexGrid.commonTypes.Property)
	 */
	public void updatePropertyVersionableAttrib(String codingSchemeUId, String parentUId,
			String propertyUId, PropertyType type, Property property) {
		Assert.hasText(
				property.getPropertyId(),
				"Property must have a populated PropertyId " +
				"in order to be updated.");
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		/*String propertyGuid = this.getPropertyUIdFromParentUIdAndPropId(
				codingSchemeUId, 
				parentUId, 
				property.getPropertyId());*/
		String entryStateUId = this.createUniqueId();
		
		this.getSqlMapClientTemplate().update(
				UPDATE_PROPERTY_VERSIONABLE_ATTRIB_BY_UID_SQL, 
				this.buildInsertPropertyBean(
						prefix, 
						parentUId, 
						propertyUId, 
						entryStateUId, 
						type, 
						property),
						1);	
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#insertPropertyQualifier(java.lang.String, java.lang.String, org.LexGrid.commonTypes.PropertyQualifier)
	 */
	public void insertPropertyQualifier(String codingSchemeId, String propertyId, PropertyQualifier propertyQualifier) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		String propertyQualifierId = this.createUniqueId();
		this.doInsertPropertyQualifier(
				prefix, propertyId, 
				propertyQualifierId, 
				null,
				propertyQualifier, 
				this.getNonBatchTemplateInserter());	
	}
	
	/**
	 * Insert property qualifier.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param propertyUId the property id
	 * @param propertyQualifier the property qualifier
	 * @param inserter the inserter
	 */
	protected void doInsertPropertyQualifier(
			final String prefix, 
			final String propertyUId, 
			final String propertyQualifierUId, 
			final String entryStateUId,
			final PropertyQualifier propertyQualifier, 
			final Inserter inserter) {

				inserter.insert(INSERT_PROPERTY_QUALIFIER_SQL, 
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
				this.getNonBatchTemplateInserter());
	}

	/**
	 * Insert property source.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param propertyUId the property id
	 * @param source the source
	 * @param inserter the inserter
	 */
	protected void doInsertPropertySource(
			final String prefix, 
			final String propertyUId, 
			final String propertySourceUId, 
			final String entryStateUId,
			final Source source, 
			final Inserter inserter) {

				inserter.insert(INSERT_PROPERTY_SOURCE_SQL, 
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
	 * @param inserter the inserter
	 */
	protected void doInsertPropertyUsageContext(
			final String prefix, 
			final String propertyUId,
			final String propertyUsageContextUId, 
			final String entryStateUId,
			final String usageContext, 
			final Inserter inserter) {

				inserter.insert(INSERT_PROPERTY_USAGECONTEXT_SQL, 
						buildInsertPropertyUsageContextBean(
								prefix,
								propertyUId, 
								propertyUsageContextUId, 
								entryStateUId, 
								usageContext));
	}
	
	@Override
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
				this.getNonBatchTemplateInserter());
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
	 * @param inserter the inserter
	 */
	public void doInsertPropertyLink(final String prefix, 
			final String entityUId,
			final String propertyLinkUId,
			final String link, 
			final String sourcePropertyUId,
			final String targetPropertyUId,
			final Inserter inserter) {
		final InsertPropertyLinkBean bean = new InsertPropertyLinkBean();
		bean.setPrefix(prefix);
		bean.setLink(link);
		bean.setUId(propertyLinkUId);
		bean.setEntityUId(entityUId);
		bean.setSourcePropertyUId(sourcePropertyUId);
		bean.setTargetPropertyUId(targetPropertyUId);


				inserter.insert(INSERT_PROPERTYLINK_SQL, 
						bean);

	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#insertPropertyLink(java.lang.String, java.lang.String, org.LexGrid.concepts.PropertyLink)
	 */
	public void insertPropertyLink(String codingSchemeUId, String parentUId,
			PropertyLink propertyLink) {
		String propertyLinkUId = this.createUniqueId();
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		String sourcePropertyUId = this.getPropertyUIdFromParentUIdAndPropId(codingSchemeUId, parentUId, propertyLink.getSourceProperty());
		String targetPropertyUId = this.getPropertyUIdFromParentUIdAndPropId(codingSchemeUId, parentUId, propertyLink.getTargetProperty());
		
		this.doInsertPropertyLink(prefix, parentUId, propertyLinkUId, 
				propertyLink.getPropertyLink(), sourcePropertyUId, targetPropertyUId, this.getNonBatchTemplateInserter());
	}
	
	public void deleteAllCodingSchemePropertiesOfCodingScheme(
			String codingSchemeUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		this.getSqlMapClientTemplate().delete(
				DELETE_ALL_CODINGSCHEME_PROPERTIES_OF_CODINGSCHEME_SQL,
				new PrefixedParameterTuple(prefix, this.propertyTypeClassifier
						.classify(PropertyType.CODINGSCHEME), codingSchemeUId));
		
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#deleteAllEntityPropertiesOfCodingScheme(java.lang.String)
	 */
	public void deleteAllEntityPropertiesOfCodingScheme(String codingSchemeUId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		this.getSqlMapClientTemplate().delete(DELETE_ALL_ENTITY_PROPERTIES_OF_CODINGSCHEME_SQL, 
				new PrefixedParameterTuple(prefix, 
						this.propertyTypeClassifier.classify(PropertyType.ENTITY), codingSchemeUId));
	}
	
	public void deleteAllRelationPropertiesOfCodingScheme(String codingSchemeUId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);

		this.getSqlMapClientTemplate().delete(DELETE_ALL_RELATION_PROPERTIES_OF_CODINGSCHEME_SQL, 
				new PrefixedParameterTuple(prefix, 
						this.propertyTypeClassifier.classify(PropertyType.RELATION), codingSchemeUId));
	}
	
	@Override
	public void deleteAllPropertiesOfParent(String codingSchemeUId,  
			String parentUId, PropertyType parentType) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		this.getSqlMapClientTemplate().delete(DELETE_ALL_PROPERTIES_OF_PARENT_SQL, 
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
		
		return (String) this.getSqlMapClientTemplate().queryForObject(GET_PROPERTY_ID_SQL, 
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
		bean.setAttributeId(usageContext);
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
		bean.setAttributeId(source.getContent());
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

		return (String) this.getSqlMapClientTemplate().queryForObject(
				GET_PROPERTY_UID_BY_ID_AND_NAME,
				new PrefixedParameterTriple(prefix, referenceUId, propertyId,
						propertyName));
	}

	@Override

	public void removePropertyByUId(String codingSchemeUId, String propertyUId) {
		String prefix = 
			this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		this.getSqlMapClientTemplate().
			delete(DELETE_PROPERTY_BY_UID_SQL, new PrefixedParameter(prefix, propertyUId));
	}

	@Override
	public String getLatestRevision(String csUId, String propertyUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(csUId);
		
		String revId = (String) this.getSqlMapClientTemplate().queryForObject(
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
}

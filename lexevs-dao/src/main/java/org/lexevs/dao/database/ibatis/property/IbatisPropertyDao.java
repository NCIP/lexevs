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
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.property.batch.PropertyBatchInsertItem;
import org.lexevs.dao.database.constants.classifier.property.PropertyMultiAttributeClassifier;
import org.lexevs.dao.database.constants.classifier.property.PropertyTypeClassifier;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.batch.IbatisBatchInserter;
import org.lexevs.dao.database.ibatis.batch.IbatisInserter;
import org.lexevs.dao.database.ibatis.batch.SqlMapExecutorBatchInserter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.property.parameter.InsertPropertyBean;
import org.lexevs.dao.database.ibatis.property.parameter.InsertPropertyLinkBean;
import org.lexevs.dao.database.ibatis.property.parameter.InsertPropertyMultiAttribBean;
import org.lexevs.dao.database.ibatis.versions.IbatisVersionsDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.batch.classify.Classifier;
import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * The Class IbatisPropertyDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IbatisPropertyDao extends AbstractIbatisDao implements PropertyDao {
	
	/** The property type classifier. */
	private Classifier<PropertyType,String> propertyTypeClassifier = new PropertyTypeClassifier();
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	public static String PROPERTY_NAMESPACE = "Property.";
	
	/** The INSER t_ propert y_ sql. */
	public static String INSERT_PROPERTY_SQL = PROPERTY_NAMESPACE + "insertProperty";
	
	/** The DELET e_ al l_ entit y_ propertie s_ o f_ codingschem e_ sql. */
	public static String DELETE_ALL_ENTITY_PROPERTIES_OF_CODINGSCHEME_SQL = PROPERTY_NAMESPACE + "deleteEntityPropertiesByCodingSchemeId";
	
	/** The INSER t_ propert y_ qualifie r_ sql. */
	public static String INSERT_PROPERTY_QUALIFIER_SQL = PROPERTY_NAMESPACE + "insertPropertyMultiAttrib";
	
	/** The INSER t_ propert y_ sourc e_ sql. */
	public static String INSERT_PROPERTY_SOURCE_SQL = PROPERTY_NAMESPACE + "insertPropertyMultiAttrib";
	
	/** The INSER t_ propert y_ usagecontex t_ sql. */
	public static String INSERT_PROPERTY_USAGECONTEXT_SQL = PROPERTY_NAMESPACE + "insertPropertyMultiAttrib";
	
	/** The INSER t_ propertylin k_ sql. */
	public static String INSERT_PROPERTYLINK_SQL = PROPERTY_NAMESPACE + "insertPropertyLink";
	
	/** The GE t_ al l_ propertie s_ o f_ paren t_ sql. */
	public static String GET_ALL_PROPERTIES_OF_PARENT_SQL =PROPERTY_NAMESPACE +  "getPropertiesByParent";

	public static String GET_ALL_PROPERTIES_OF_PARENT_BY_REVISION_SQL = PROPERTY_NAMESPACE + "getPropertiesByParentAndRevisionId";
	
	/** The GE t_ propert y_ i d_ sql. */
	public static String GET_PROPERTY_ID_SQL = PROPERTY_NAMESPACE + "getPropertyId";
	
	public static String GET_PROPERTY_MULTIATTRIB_BY_PROPERTY_ID_SQL = PROPERTY_NAMESPACE + "getPropertyMultiAttribById";
	
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
				IbatisBatchInserter inserter = new SqlMapExecutorBatchInserter(executor);
				
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
			IbatisBatchInserter inserter) {
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
	public String insertProperty(String codingSchemeId,
			String entityCodeId, 
			PropertyType type, 
			Property property) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		String propertyId = this.createUniqueId();
		
		return this.doInsertProperty(
				prefix, 
				entityCodeId, 
				propertyId, 
				type, 
				property, 
				this.getNonBatchTemplateInserter());	
	}
	
	public String insertHistoryProperty(String codingSchemeId,
			String entityCodeId, String propertyId, PropertyType type, Property property) {
		String prefix = this.getPrefixResolver().resolveHistoryPrefix();
		
		return this.doInsertProperty(
				prefix, 
				entityCodeId, 
				propertyId, 
				type, 
				property, 
				this.getNonBatchTemplateInserter());	
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
						this.propertyTypeClassifier.classify(PropertyType.ENTITY),
						parentId));
	}
	
	@SuppressWarnings("unchecked")
	public List<Property> getAllHistoryPropertiesOfParentByRevisionId(String codingSchemeId,
			String parentId, String revisionId, PropertyType type) {
		return this.getSqlMapClientTemplate().queryForList(GET_ALL_PROPERTIES_OF_PARENT_BY_REVISION_SQL, 
				new PrefixedParameterTriple(
						this.getPrefixResolver().resolveHistoryPrefix(),
						this.propertyTypeClassifier.classify(PropertyType.ENTITY),
						parentId,
						revisionId));
	}
	
	@SuppressWarnings("unchecked")
	protected <T> List<T> doGetPropertyMultiAttrib(String prefix, String propertyId, Class<T> multiAttrib){
		return this.getSqlMapClientTemplate().queryForList(GET_PROPERTY_MULTIATTRIB_BY_PROPERTY_ID_SQL, 
				new PrefixedParameterTuple(prefix, propertyId, this.propertyMultiAttributeClassifier.classify(multiAttrib)));
	}
	
	/**
	 * Insert property.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param entityCodeId the entity code id
	 * @param type the type
	 * @param property the property
	 * @param inserter the inserter
	 * 
	 * @return the string
	 */
	public String doInsertProperty(
			String prefix,
			String entityCodeId, 
			String propertyId,
			PropertyType type, 
			Property property, 
			IbatisInserter inserter) {
		String entryStateId = this.createUniqueId();
		
		this.ibatisVersionsDao.insertEntryState(
				this.getPrefixResolver().resolveDefaultPrefix(),
				entryStateId, propertyId, "Property", null, property.getEntryState(), inserter);
		
		inserter.insert(INSERT_PROPERTY_SQL,
				buildInsertPropertyBean(
						prefix,
						entityCodeId,
						propertyId,
						entryStateId,
						type,
						property
						));
		
		for(Source source : property.getSource()) {
			String propertySourceId = this.createUniqueId();
			this.doInsertPropertySource(prefix, propertyId, propertySourceId, entryStateId, source, inserter);
		}
		
		for(String context : property.getUsageContext()) {
			String propertyUsageContextId = this.createUniqueId();
			this.doInsertPropertyUsageContext(prefix, propertyId, propertyUsageContextId, entryStateId, context, inserter);
		}
		
		for(PropertyQualifier qual : property.getPropertyQualifier()) {
			String propertyQualifierId = this.createUniqueId();
			this.doInsertPropertyQualifier(prefix, propertyId, propertyQualifierId, entryStateId, qual, inserter);
		}
		
		return propertyId;
		
	}


	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#updateProperty(java.lang.String, java.lang.String, java.lang.String, org.lexevs.dao.database.access.property.PropertyDao.PropertyType, org.LexGrid.commonTypes.Property)
	 */
	public void updateProperty(String codingSchemeName, String parentId,
			String propertyId, PropertyType type, Property property) {
		// TODO Auto-generated method stub
		
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
	 * @param propertyId the property id
	 * @param propertyQualifier the property qualifier
	 * @param inserter the inserter
	 */
	protected void doInsertPropertyQualifier(
			final String prefix, 
			final String propertyId, 
			final String propertyQualifierId, 
			final String entryStateId,
			final PropertyQualifier propertyQualifier, 
			final IbatisInserter inserter) {

		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){

			public Object doInSqlMapClient(SqlMapExecutor executor)
			throws SQLException {

				inserter.insert(INSERT_PROPERTY_QUALIFIER_SQL, 
						buildInsertPropertyQualifierBean(
								prefix,
								propertyId, 
								propertyQualifierId, 
								entryStateId,
								propertyQualifier));
				return null;
			}
		});
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
	 * @param propertyId the property id
	 * @param source the source
	 * @param inserter the inserter
	 */
	protected void doInsertPropertySource(
			final String prefix, 
			final String propertyId, 
			final String propertySourceId, 
			final String entryStateId,
			final Source source, 
			final IbatisInserter inserter) {
		final String sourceId = this.createUniqueId();	

		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){

			public Object doInSqlMapClient(SqlMapExecutor executor)
			throws SQLException {

				inserter.insert(INSERT_PROPERTY_SOURCE_SQL, 
						buildInsertPropertySourceBean(
								prefix,
								propertyId, 
								sourceId, 
								entryStateId, 
								source));
				return null;
			}
		});
	}
	
	/**
	 * Insert property usage context.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param propertyId the property id
	 * @param usageContext the usage context
	 * @param inserter the inserter
	 */
	protected void doInsertPropertyUsageContext(
			final String prefix, 
			final String propertyId,
			final String propertyUsageContextId, 
			final String entryStateId,
			final String usageContext, 
			final IbatisInserter inserter) {
		
		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){

			public Object doInSqlMapClient(SqlMapExecutor executor)
			throws SQLException {

				inserter.insert(INSERT_PROPERTY_USAGECONTEXT_SQL, 
						buildInsertPropertyUsageContextBean(
								prefix,
								propertyId, 
								propertyUsageContextId, 
								entryStateId, 
								usageContext));

				return null;
			}
		});
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
	 * @param entityId the entity id
	 * @param propertyLinkId the property link id
	 * @param link the link
	 * @param sourcePropertyId the source property id
	 * @param targetPropertyId the target property id
	 * @param inserter the inserter
	 */
	public void doInsertPropertyLink(final String prefix, 
			final String entityId,
			final String propertyLinkId,
			final String link, 
			final String sourcePropertyId,
			final String targetPropertyId,
			final IbatisInserter inserter) {
		final InsertPropertyLinkBean bean = new InsertPropertyLinkBean();
		bean.setPrefix(prefix);
		bean.setLink(link);
		bean.setId(propertyLinkId);
		bean.setSourcePropertyId(sourcePropertyId);
		bean.setTargetPropertyId(targetPropertyId);
		
		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){
			

			public Object doInSqlMapClient(SqlMapExecutor executor)
			throws SQLException {
				inserter.insert(INSERT_PROPERTYLINK_SQL, 
						bean);
				return null;
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#insertPropertyLink(java.lang.String, java.lang.String, org.LexGrid.concepts.PropertyLink)
	 */
	public void insertPropertyLink(String codingSchemeId, String entityId,
			PropertyLink propertyLink) {
		String propertyLinkId = this.createUniqueId();
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		String sourcePropertyId = this.getPropertyIdFromParentIdAndPropId(codingSchemeId, entityId, propertyLink.getSourceProperty());
		String targetPropertyId = this.getPropertyIdFromParentIdAndPropId(codingSchemeId, entityId, propertyLink.getTargetProperty());
		
		this.doInsertPropertyLink(prefix, entityId, propertyLinkId, 
				propertyLink.getPropertyLink(), sourcePropertyId, targetPropertyId, this.getNonBatchTemplateInserter());
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.property.PropertyDao#deleteAllEntityPropertiesOfCodingScheme(java.lang.String)
	 */
	public void deleteAllEntityPropertiesOfCodingScheme(String codingSchemeId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		this.getSqlMapClientTemplate().delete(DELETE_ALL_ENTITY_PROPERTIES_OF_CODINGSCHEME_SQL, 
				new PrefixedParameterTuple(prefix, 
						this.propertyTypeClassifier.classify(PropertyType.ENTITY), codingSchemeId));
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
	protected String getPropertyIdFromParentIdAndPropId(String codingSchemeId, String parentId, String propId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return (String) this.getSqlMapClientTemplate().queryForObject(GET_PROPERTY_ID_SQL, 
				new PrefixedParameterTuple(prefix, parentId, propId));
	}

	/**
	 * Builds the insert property bean.
	 * 
	 * @param prefix the prefix
	 * @param entityId the entity id
	 * @param propertyId the property id
	 * @param entryStateId the entry state id
	 * @param type the type
	 * @param property the property
	 * 
	 * @return the insert property bean
	 */
	protected InsertPropertyBean buildInsertPropertyBean(String prefix, String entityId, String propertyId, 
			String entryStateId, PropertyType type, Property property){
		InsertPropertyBean bean = new InsertPropertyBean();
		bean.setPrefix(prefix);
		bean.setReferenceType(this.propertyTypeClassifier.classify(type));
		bean.setEntityId(entityId);
		bean.setId(propertyId);
		bean.setEntryStateId(entryStateId);
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
		bean.setId(qualifierId);
		bean.setPropertyId(propertyId);
		bean.setAttributeId(propertyQualifier.getPropertyQualifierName());
		bean.setAttributeValue(propertyQualifier.getValue().getContent());
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_QUALIFIER);
		bean.setEntryStateId(entryStateId);

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
		bean.setId(qualifierId);
		bean.setPropertyId(propertyId);
		bean.setAttributeId(SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
		bean.setAttributeValue(usageContext);
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
		bean.setEntryStateId(entryStateId);

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
		bean.setId(sourceId);
		bean.setPropertyId(propertyId);
		bean.setAttributeId(SQLTableConstants.TBLCOLVAL_SOURCE);
		bean.setAttributeValue(source.getContent());
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_SOURCE);
		bean.setRole(source.getRole());
		bean.setSubRef(source.getSubRef());
		bean.setEntryStateId(entryStateId);

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

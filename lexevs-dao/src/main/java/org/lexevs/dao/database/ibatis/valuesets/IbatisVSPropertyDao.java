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
package org.lexevs.dao.database.ibatis.valuesets;

import java.sql.SQLException;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao;
import org.lexevs.dao.database.constants.classifier.property.PropertyMultiAttributeClassifier;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.batch.IbatisInserter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.property.parameter.InsertOrUpdatePropertyBean;
import org.lexevs.dao.database.ibatis.property.parameter.InsertPropertyMultiAttribBean;
import org.lexevs.dao.database.ibatis.versions.IbatisVersionsDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.util.Assert;

import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * The Class IbatisVSPropertyDao.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public class IbatisVSPropertyDao extends AbstractIbatisDao implements VSPropertyDao {
	
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	public static String PROPERTY_NAMESPACE = "VSProperty.";
	
	public static String INSERT_PROPERTY_SQL = PROPERTY_NAMESPACE + "insertProperty";
	
	public static String DELETE_ALL_PICKLIST_ENTRY_PROPERTIES_OF_PCIKLIST_SQL = PROPERTY_NAMESPACE + "deletePickListEntryPropertiesByPickListGuid";
	public static String DELETE_ALL_PICKLIST_DEFINITION_PROPERTIES_OF_PCIKLIST_SQL = PROPERTY_NAMESPACE + "deletePickListDefinitionPropertiesByPickListGuid";
	public static String DELETE_ALL_DEFINITIONENTRY_PROPERTIES_OF_VALUESET_SQL = PROPERTY_NAMESPACE + "deleteDefinitionEntryPropertiesByValueSetGuid";
	public static String DELETE_ALL_VALUESET_DEFINITION_PROPERTIES_OF_VALUESET_SQL = PROPERTY_NAMESPACE + "deleteValueSetDefinitionPropertiesByValueSetGuid";
	
	public static String INSERT_PROPERTY_QUALIFIER_SQL = PROPERTY_NAMESPACE + "insertPropertyMultiAttrib";
	
	public static String INSERT_PROPERTY_SOURCE_SQL = PROPERTY_NAMESPACE + "insertPropertyMultiAttrib";
	
	public static String INSERT_PROPERTY_USAGECONTEXT_SQL = PROPERTY_NAMESPACE + "insertPropertyMultiAttrib";
	
	public static String GET_ALL_PROPERTIES_OF_PARENT_SQL =PROPERTY_NAMESPACE +  "getPropertiesByParent";

	public static String GET_ALL_PROPERTIES_OF_PARENT_BY_REVISION_SQL = PROPERTY_NAMESPACE + "getPropertiesByParentAndRevisionId";
	
	public static String GET_PROPERTY_GUID_SQL = PROPERTY_NAMESPACE + "getPropertyGuid";
	
	public static String GET_PROPERTY_MULTIATTRIB_BY_PROPERTY_ID_SQL = PROPERTY_NAMESPACE + "getPropertyMultiAttribById";
	
	public static String UPDATE_PROPERTY_BY_ID_SQL = PROPERTY_NAMESPACE + "updatePropertyById";
	
	PropertyMultiAttributeClassifier propertyMultiAttributeClassifier = new PropertyMultiAttributeClassifier();
	
	/** The ibatis versions dao. */
	private IbatisVersionsDao ibatisVersionsDao;

	public String insertProperty(String parentGuid, 
			ReferenceType type, 
			Property property) {
		String propertyGuid = this.createUniqueId();
		
		return this.doInsertProperty(
				parentGuid, 
				propertyGuid, 
				type, 
				property, 
				this.getNonBatchTemplateInserter());	
	}
	
	public String insertHistoryProperty(String parentGuid, String propertyGuid, ReferenceType type, Property property) {
		return this.doInsertProperty(
				parentGuid, 
				propertyGuid, 
				type, 
				property, 
				this.getNonBatchTemplateInserter());	
	}
	
	@SuppressWarnings("unchecked")
	public List<Property> getAllPropertiesOfParent(String parentGuid, ReferenceType type) {
		return this.getSqlMapClientTemplate().queryForList(GET_ALL_PROPERTIES_OF_PARENT_SQL, 
				new PrefixedParameterTuple(
						null,
						type.name(),
						parentGuid));
	}
	
	@SuppressWarnings("unchecked")
	public List<Property> getAllHistoryPropertiesOfParentByRevisionGuid(String parentGuid, String revisionGuid, ReferenceType type) {
		
		PrefixedParameterTriple param = new PrefixedParameterTriple(
				this.getPrefixResolver().resolveHistoryPrefix(),
				type.name(),
				parentGuid,
				revisionGuid);
		
		return this.getSqlMapClientTemplate().queryForList(GET_ALL_PROPERTIES_OF_PARENT_BY_REVISION_SQL, 
				param);
	}
	
	@SuppressWarnings("unchecked")
	protected <T> List<T> doGetPropertyMultiAttrib(String propertyGuid, Class<T> multiAttrib){
		return this.getSqlMapClientTemplate().queryForList(GET_PROPERTY_MULTIATTRIB_BY_PROPERTY_ID_SQL, 
				new PrefixedParameterTuple(null, propertyGuid, this.propertyMultiAttributeClassifier.classify(multiAttrib)));
	}
	
	protected String getPropertyTypeString(Property property) {
		org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType propertyType = 
			DaoUtility.propertyClassToTypeMap.get(property.getClass());
		
		return DaoUtility.propertyTypeToStringMap.get(propertyType);
	}
	
	/**
	 * Insert property.
	 * 
	 * @param parentGuid the parentGUID
	 * @param type the type
	 * @param property the property
	 * @param inserter the inserter
	 * 
	 * @return the string
	 */
	public String doInsertProperty(
			String parentGuid, 
			String propertyGuid,
			ReferenceType type, 
			Property property, 
			IbatisInserter inserter) {
		String entryStateId = this.createUniqueId();
		
		if(StringUtils.isBlank(property.getPropertyType())){
			property.setPropertyType(
					getPropertyTypeString(property));
		}
		//TODO - implement entrystate below:
//		this.ibatisVersionsDao.insertEntryState(
//				this.getPrefixResolver().resolveDefaultPrefix(),
//				entryStateId, propertyGuid, "Property", null, property.getEntryState(), inserter);
		
		inserter.insert(INSERT_PROPERTY_SQL,
				buildInsertPropertyBean(
						parentGuid,
						propertyGuid,
						entryStateId,
						type,
						property
						));
		
		for(Source source : property.getSource()) {
			String propertySourceGuid = this.createUniqueId();
			this.doInsertPropertySource(propertyGuid, propertySourceGuid, entryStateId, source, inserter);
		}
		
		for(String context : property.getUsageContext()) {
			String propertyUsageContextId = this.createUniqueId();
			this.doInsertPropertyUsageContext(propertyGuid, propertyUsageContextId, entryStateId, context, inserter);
		}
		
		for(PropertyQualifier qual : property.getPropertyQualifier()) {
			String propertyQualifierId = this.createUniqueId();
			this.doInsertPropertyQualifier(propertyGuid, propertyQualifierId, entryStateId, qual, inserter);
		}
		
		return propertyGuid;
		
	}


	public void updateProperty(String parentGuid,
			String propertyId, ReferenceType type, Property property) {
		Assert.hasText(
				property.getPropertyId(),
				"Property must have a populated PropertyId " +
				"in order to be updated.");
		
		String propertyGuid = this.getPropertyGuidFromParentGuidAndPropertyId(
				parentGuid, 
				property.getPropertyId());
		
		this.getSqlMapClientTemplate().update(
				UPDATE_PROPERTY_BY_ID_SQL, 
				this.buildInsertPropertyBean(
						null, 
						propertyGuid, 
						null, 
						type, 
						property),
						1);	
	}
	
	public void insertPropertyQualifier(String propertyGuid, PropertyQualifier propertyQualifier) {
		String propertyQualifierGuid = this.createUniqueId();
		this.doInsertPropertyQualifier(
				propertyGuid, 
				propertyQualifierGuid, 
				null,
				propertyQualifier, 
				this.getNonBatchTemplateInserter());	
	}
	
	/**
	 * Insert property qualifier.
	 * 
	 * @param propertyGuid the property GUID
	 * @param propertyQualifier the property qualifier
	 * @param inserter the inserter
	 */
	protected void doInsertPropertyQualifier(
			final String propertyGuid, 
			final String propertyQualifierGuid, 
			final String entryStateGuid,
			final PropertyQualifier propertyQualifier, 
			final IbatisInserter inserter) {

		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){

			public Object doInSqlMapClient(SqlMapExecutor executor)
			throws SQLException {

				inserter.insert(INSERT_PROPERTY_QUALIFIER_SQL, 
						buildInsertPropertyQualifierBean(
								propertyGuid, 
								propertyQualifierGuid, 
								entryStateGuid,
								propertyQualifier));
				return null;
			}
		});
	}
	
	

	@Override
	public void insertPropertySource(String propertyGuid,
			Source source) {
		String propertySourceGuid = this.createUniqueId();
		
		this.doInsertPropertySource(
				propertyGuid, 
				propertySourceGuid, 
				null,
				source,
				this.getNonBatchTemplateInserter());
	}

	/**
	 * Insert property source.
	 * 
	 * @param propertyGuid the property Guid
	 * @param source the source
	 * @param inserter the inserter
	 */
	protected void doInsertPropertySource(
			final String propertyGuid, 
			final String propertySourceGuid, 
			final String entryStateId,
			final Source source, 
			final IbatisInserter inserter) {
		final String sourceId = this.createUniqueId();	

		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){

			public Object doInSqlMapClient(SqlMapExecutor executor)
			throws SQLException {

				inserter.insert(INSERT_PROPERTY_SOURCE_SQL, 
						buildInsertPropertySourceBean(
								propertyGuid, 
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
	 * @param propertyGuid the property GUID
	 * @param usageContext the usage context
	 * @param inserter the inserter
	 */
	protected void doInsertPropertyUsageContext(
			final String propertyGuid,
			final String propertyUsageContextGuid, 
			final String entryStateGuid,
			final String usageContext, 
			final IbatisInserter inserter) {
		
		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){

			public Object doInSqlMapClient(SqlMapExecutor executor)
			throws SQLException {

				inserter.insert(INSERT_PROPERTY_USAGECONTEXT_SQL, 
						buildInsertPropertyUsageContextBean(
								propertyGuid, 
								propertyUsageContextGuid, 
								entryStateGuid, 
								usageContext));

				return null;
			}
		});
	}
	
	@Override
	public void insertPropertyUsageContext(
			String propertyGuid, 
			String usageContext) {
		String usageContextId = this.createUniqueId();
		this.doInsertPropertyUsageContext(
				propertyGuid, 
				usageContextId,
				null, 
				usageContext,
				this.getNonBatchTemplateInserter());
	}
	
	public void deleteAllDefinitionEntityPropertiesOfValueSetDefinition(
			String valueSetDefinitionURI){
		this.getSqlMapClientTemplate().delete(DELETE_ALL_DEFINITIONENTRY_PROPERTIES_OF_VALUESET_SQL, 
				new PrefixedParameterTuple(null, 
						ReferenceType.DEFINITIONENTRY.name(), valueSetDefinitionURI));
	}
	
	public void deleteAllValueSetDefinitionProperties(
			String valueSetDefinitionURI){
		this.getSqlMapClientTemplate().delete(DELETE_ALL_VALUESET_DEFINITION_PROPERTIES_OF_VALUESET_SQL, 
				new PrefixedParameterTuple(null, 
						ReferenceType.VALUESETDEFINITION.name(), valueSetDefinitionURI));
	}
	
	public void deleteAllPickListEntityPropertiesOfPickListDefinition(
			String pickListId){
		this.getSqlMapClientTemplate().delete(DELETE_ALL_PICKLIST_ENTRY_PROPERTIES_OF_PCIKLIST_SQL, 
				new PrefixedParameterTuple(null, 
						ReferenceType.PICKLISTENTRY.name(), pickListId));
	}
	
	public void deleteAllPickListDefinitionProperties(
			String pickListId){
		this.getSqlMapClientTemplate().delete(DELETE_ALL_PICKLIST_DEFINITION_PROPERTIES_OF_PCIKLIST_SQL, 
				new PrefixedParameterTuple(null, 
						ReferenceType.PICKLISTDEFINITION.name(), pickListId));
	}
	
	/**
	 * Gets the propertyGuid from parentGuid and prop id.
	 * 
	 * @param parentGuid the parent GUID
	 * @param propertyId the propertyID
	 * 
	 * @return the propertyGUID from parentGuid and propertyID
	 */
	protected String getPropertyGuidFromParentGuidAndPropertyId(String parentGuid, String propertyId) {
		
		return (String) this.getSqlMapClientTemplate().queryForObject(GET_PROPERTY_GUID_SQL, 
				new PrefixedParameterTuple(null, parentGuid, propertyId));
	}

	/**
	 * Builds the insert property bean.
	 * 
	 * @param parentGuid the parent GUID
	 * @param propertyGuid the property GUID
	 * @param entryStateGuid the entry state GUID
	 * @param type the type
	 * @param property the property
	 * 
	 * @return the insert property bean
	 */
	protected InsertOrUpdatePropertyBean buildInsertPropertyBean(String parentGuid, String propertyGuid, 
			String entryStateGuid, ReferenceType type, Property property){
		InsertOrUpdatePropertyBean bean = new InsertOrUpdatePropertyBean();
		bean.setReferenceType(type.name());
		bean.setEntityId(parentGuid);
		bean.setId(propertyGuid);
		bean.setEntryStateId(entryStateGuid);
		bean.setProperty(property);
		
		return bean;
	}
	
	/**
	 * Builds the insert property qualifier bean.
	 * 
	 * @param prefix the prefix
	 * @param propertyGuid the property id
	 * @param qualifierGuid the qualifier id
	 * @param propertyQualifier the property qualifier
	 * 
	 * @return the insert property multi attrib bean
	 */
	protected InsertPropertyMultiAttribBean buildInsertPropertyQualifierBean(
			String propertyGuid, 
			String qualifierGuid, 
			String entryStateGuid,
			PropertyQualifier propertyQualifier){
		InsertPropertyMultiAttribBean bean = new InsertPropertyMultiAttribBean();
		bean.setId(qualifierGuid);
		bean.setPropertyId(propertyGuid);
		bean.setAttributeId(propertyQualifier.getPropertyQualifierName());
		if (propertyQualifier.getValue() != null)
		{
			bean.setAttributeValue(propertyQualifier.getValue().getContent());
		}
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_QUALIFIER);
		
		bean.setEntryStateId(entryStateGuid);

		return bean;
	}
	
	/**
	 * Builds the insert property usage context bean.
	 * 
	 * @param prefix the prefix
	 * @param propertyGuid the property GUID
	 * @param qualifierGuid the qualifier GUID
	 * @param usageContext the usage context
	 * 
	 * @return the insert property multi attrib bean
	 */
	protected InsertPropertyMultiAttribBean buildInsertPropertyUsageContextBean(String propertyGuid, 
			String qualifierGuid, String entryStateGuid, String usageContext){
		InsertPropertyMultiAttribBean bean = new InsertPropertyMultiAttribBean();
		bean.setId(qualifierGuid);
		bean.setPropertyId(propertyGuid);
		bean.setAttributeId(SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
		bean.setAttributeValue(usageContext);
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
		bean.setEntryStateId(entryStateGuid);

		return bean;
	}
	
	/**
	 * Builds the insert property source bean.
	 * 
	 * @param prefix the prefix
	 * @param propertyGuid the property id
	 * @param sourceId the source id
	 * @param source the source
	 * 
	 * @return the insert property multi attrib bean
	 */
	protected InsertPropertyMultiAttribBean buildInsertPropertySourceBean(
			String propertyGuid, String sourceId, String entryStateId, Source source){
		InsertPropertyMultiAttribBean bean = new InsertPropertyMultiAttribBean();
		bean.setId(sourceId);
		bean.setPropertyId(propertyGuid);
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

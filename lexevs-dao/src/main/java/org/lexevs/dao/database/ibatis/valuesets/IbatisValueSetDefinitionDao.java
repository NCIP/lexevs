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

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.URIMap;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao;
import org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao.ReferenceType;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.constants.classifier.mapping.ClassToStringMappingClassifier;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.codingscheme.parameter.InsertOrUpdateURIMapBean;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.valuesets.parameter.InsertOrUpdateDefinitionEntryBean;
import org.lexevs.dao.database.ibatis.valuesets.parameter.InsertOrUpdateValueSetsMultiAttribBean;
import org.lexevs.dao.database.ibatis.valuesets.parameter.InsertValueSetDefinitionBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * The Class IbatisValueSetDefinitionDao.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public class IbatisValueSetDefinitionDao extends AbstractIbatisDao implements ValueSetDefinitionDao {
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	public static String VALUESETDEFINITION_NAMESPACE = "ValueSetDefinition.";
	
	public static String VS_MULTIATTRIB_NAMESPACE = "VSMultiAttrib.";
	
	public static String VS_MAPPING_NAMESPACE = "VSMapping.";
	
	private static String SUPPORTED_ATTRIB_GETTER_PREFIX = "_supported";
	
	public static String INSERT_VALUESET_DEFINITION_SQL = VALUESETDEFINITION_NAMESPACE + "insertValueSetDefinition";
	
	public static String INSERT_DEFINITION_ENTRY_SQL = VALUESETDEFINITION_NAMESPACE + "insertDefinitionEntry";
	
	public static String GET_VALUESET_DEFINITION_URIS_SQL = VALUESETDEFINITION_NAMESPACE + "getValueSetDefinitionURIs";
	
	public static String GET_VALUESET_DEFINITION_URI_FOR_VALUESET_NAME_SQL = VALUESETDEFINITION_NAMESPACE + "getValueSetDefinitionURIForValueSetName";
	
	public static String GET_VALUESET_DEFINITION_GUID_BY_VALUESET_DEFINITION_URI_SQL = VALUESETDEFINITION_NAMESPACE + "getValueSetDefinitionGuidByValueSetDefinitionURI";
	
	public static String GET_VALUESET_DEFINITION_BY_VALUESET_DEFINITION_URI_SQL = VALUESETDEFINITION_NAMESPACE + "getValueSetDefinitionByValueSetURI";
	
	public static String GET_DEFINITION_ENTRY_BY_VALUESET_DEFINITION_GUID_SQL = VALUESETDEFINITION_NAMESPACE + "getDefinitionEntryByValueSetGuid";
	
	public static String REMOVE_VALUESET_DEFINITION_BY_VALUESET_DEFINITION_URI_SQL = VALUESETDEFINITION_NAMESPACE + "removevalueSetDefinitionByValueSetDefinitionURI";
	
	public static String REMOVE_DEFINITION_ENTRY_BY_VALUESET_DEFINITION_GUID_SQL = VALUESETDEFINITION_NAMESPACE + "removeDefinitionEntryByValueSetDefinitionGuid";
	
	public static String GET_SOURCE_LIST_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "getSourceListByParentGuidandType";
	
	public static String GET_CONTEXT_LIST_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "getContextListByParentGuidandType";
	
	public static String INSERT_MULTI_ATTRIB_SQL = VS_MULTIATTRIB_NAMESPACE + "insertMultiAttrib";
	
	public static String DELETE_SOURCE_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "deleteSourceByParentGuidAndType";
	
	public static String DELETE_CONTEXT_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "deleteContextByParentGuidAndType";
	
	public static String DELETE_PICKLIST_ENTRY_CONTEXT_BY_PICKLIST_GUID_SQL = VS_MULTIATTRIB_NAMESPACE + "deletePickListEntryContextByPickListGuid";
	
	public static String GET_URIMAPS_BY_REFERENCE_GUID_SQL = VS_MAPPING_NAMESPACE + "getURIMaps";
	
	public static String GET_URIMAPS_BY_REFERENCE_GUID_LOCALNAME_AND_TYPE_SQL = VS_MAPPING_NAMESPACE + "getURIMapByLocalNameAndType";
	
	public static String INSERT_URIMAPS_SQL = VS_MAPPING_NAMESPACE + "insertURIMap";
	
	public static String UPDATE_URIMAPS_BY_LOCALID_SQL = VS_MAPPING_NAMESPACE + "updateUriMapByLocalId";
	
	public static String DELETE_URIMAPS_BY_REFERENCE_GUID_SQL = VS_MAPPING_NAMESPACE + "deleteMappingsByReferenceGuid";
	
	/** The versions dao. */
	private VersionsDao versionsDao;
	
	private VSPropertyDao vsPropertyDao;
	
	/** The class to string mapping classifier. */
	private ClassToStringMappingClassifier classToStringMappingClassifier = new ClassToStringMappingClassifier();

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao#getValueSetDefinitionByURI(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ValueSetDefinition getValueSetDefinitionByURI(String valueSetDefinitionURI) {
		ValueSetDefinition vsd = (ValueSetDefinition) 
			this.getSqlMapClientTemplate().queryForObject(GET_VALUESET_DEFINITION_BY_VALUESET_DEFINITION_URI_SQL, 
				new PrefixedParameter(null, valueSetDefinitionURI));
		
		if (vsd != null)
		{
			String vsdGuid = getGuidFromvalueSetDefinitionURI(valueSetDefinitionURI);
			
			List<DefinitionEntry> des = this.getSqlMapClientTemplate().queryForList(GET_DEFINITION_ENTRY_BY_VALUESET_DEFINITION_GUID_SQL,
					new PrefixedParameter(null, vsdGuid));
			
			if (des != null)
				vsd.setDefinitionEntry(des);			
			
			List<Property> props = vsPropertyDao.getAllPropertiesOfParent(vsdGuid, ReferenceType.VALUESETDEFINITION);
			
			if (props != null)				
			{
				Properties properties = new Properties();
				properties.getPropertyAsReference().addAll(props);
				vsd.setProperties(properties);
			}
			
			// Get value set definition source list
			List<Source> sourceList = this.getSqlMapClientTemplate().queryForList(GET_SOURCE_LIST_BY_PARENT_GUID_AND_TYPE_SQL, 
					new PrefixedParameterTuple(null, vsdGuid, ReferenceType.VALUESETDEFINITION.name())); 
			
			if (sourceList != null)
				vsd.setSource(sourceList);
			
			// Get realm or context list
			List<String> contextList = this.getSqlMapClientTemplate().queryForList(GET_CONTEXT_LIST_BY_PARENT_GUID_AND_TYPE_SQL, 
					new PrefixedParameterTuple(null, vsdGuid, ReferenceType.VALUESETDEFINITION.name())); 
			
			if (contextList != null)
				vsd.setRepresentsRealmOrContext(contextList);
			
		}
		return vsd;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao#getGuidFromvalueSetDefinitionURI(java.lang.String)
	 */
	@Override
	public String getGuidFromvalueSetDefinitionURI(String valueSetDefinitionURI) {
		return (String) 
		this.getSqlMapClientTemplate().queryForObject(GET_VALUESET_DEFINITION_GUID_BY_VALUESET_DEFINITION_URI_SQL, 
			new PrefixedParameter(null, valueSetDefinitionURI));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllValueSetDefinitionsWithNoName() throws LBException {
		return this.getSqlMapClientTemplate().queryForList(GET_VALUESET_DEFINITION_URI_FOR_VALUESET_NAME_SQL,
				new PrefixedParameter(null, " "));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getValueSetDefinitionURIsForName(String valueSetDefinitionName)
			throws LBException {
		if (valueSetDefinitionName == null)
			return getValueSetDefinitionURIs();		
		else if (StringUtils.isBlank(valueSetDefinitionName))
			return getAllValueSetDefinitionsWithNoName();
		else
			return this.getSqlMapClientTemplate().queryForList(GET_VALUESET_DEFINITION_URI_FOR_VALUESET_NAME_SQL,
					new PrefixedParameter(null, valueSetDefinitionName));
	}

	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao#insertValueSetDefinition(java.lang.String, org.LexGrid.valueDomains.ValueSetDefinition)
	 */
	@Override
	public String insertValueSetDefinition(String systemReleaseUri,
			ValueSetDefinition definition) {
		String valueSetDefinitionGuid = this.createUniqueId();
		
		String systemReleaseId = this.versionsDao.getSystemReleaseIdByUri(systemReleaseUri);
		
		InsertValueSetDefinitionBean vsDefBean = new InsertValueSetDefinitionBean();
		vsDefBean.setId(valueSetDefinitionGuid);
		vsDefBean.setValueSetDefinition(definition);
		vsDefBean.setPrefix(getPrefix());
		vsDefBean.setSystemReleaseId(systemReleaseId);
		
		// insert into value set definition table
		this.getSqlMapClientTemplate().insert(INSERT_VALUESET_DEFINITION_SQL, vsDefBean);
		
		// insert definition entry
		for (DefinitionEntry vsdEntry : definition.getDefinitionEntryAsReference()) 
		{
			insertDefinitionEntry(valueSetDefinitionGuid, vsdEntry);
		}
		
		// insert value set definition properties
		if (definition.getProperties() != null)
		{
			for (Property property : definition.getProperties().getPropertyAsReference())
			{
				this.vsPropertyDao.insertProperty(valueSetDefinitionGuid, ReferenceType.VALUESETDEFINITION, property);
			}
		}
		
		// insert realm or context list
		for (String context : definition.getRepresentsRealmOrContextAsReference())
		{
			InsertOrUpdateValueSetsMultiAttribBean insertOrUpdateValueSetsMultiAttribBean = new InsertOrUpdateValueSetsMultiAttribBean();
			insertOrUpdateValueSetsMultiAttribBean.setId(this.createUniqueId());
			insertOrUpdateValueSetsMultiAttribBean.setReferenceGuid(valueSetDefinitionGuid);
			insertOrUpdateValueSetsMultiAttribBean.setReferenceType(ReferenceType.VALUESETDEFINITION.name());
			insertOrUpdateValueSetsMultiAttribBean.setAttributeType(SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT);
			insertOrUpdateValueSetsMultiAttribBean.setAttributeValue(context);
			insertOrUpdateValueSetsMultiAttribBean.setRole(null);
			insertOrUpdateValueSetsMultiAttribBean.setSubRef(null);
			insertOrUpdateValueSetsMultiAttribBean.setEntryStateId(this.createUniqueId());
			
			this.getSqlMapClientTemplate().insert(INSERT_MULTI_ATTRIB_SQL, insertOrUpdateValueSetsMultiAttribBean);
		}
		
		// insert value set definition source list
		for (Source source : definition.getSourceAsReference())
		{
			InsertOrUpdateValueSetsMultiAttribBean insertOrUpdateValueSetsMultiAttribBean = new InsertOrUpdateValueSetsMultiAttribBean();
			insertOrUpdateValueSetsMultiAttribBean.setId(this.createUniqueId());
			insertOrUpdateValueSetsMultiAttribBean.setReferenceGuid(valueSetDefinitionGuid);
			insertOrUpdateValueSetsMultiAttribBean.setReferenceType(ReferenceType.VALUESETDEFINITION.name());
			insertOrUpdateValueSetsMultiAttribBean.setAttributeType(SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE);
			insertOrUpdateValueSetsMultiAttribBean.setAttributeValue(source.getContent());
			insertOrUpdateValueSetsMultiAttribBean.setRole(source.getRole());
			insertOrUpdateValueSetsMultiAttribBean.setSubRef(source.getSubRef());
			insertOrUpdateValueSetsMultiAttribBean.setEntryStateId(this.createUniqueId());
			
			this.getSqlMapClientTemplate().insert(INSERT_MULTI_ATTRIB_SQL, insertOrUpdateValueSetsMultiAttribBean);
		}
		
		// insert value set definition mappings
		insertMappings(valueSetDefinitionGuid, definition.getMappings());
		
		return valueSetDefinitionGuid;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao#getValueSetDefinitionURIs()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getValueSetDefinitionURIs() {
		return this.getSqlMapClientTemplate().queryForList(GET_VALUESET_DEFINITION_URIS_SQL);
	}	
	
	/**
	 * Gets the prefix.
	 * 
	 * @return the prefix
	 */
	protected String getPrefix() {
		return this.getPrefixResolver().resolveDefaultPrefix();
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.AbstractBaseDao#doGetSupportedLgSchemaVersions()
	 */
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
	}

	/**
	 * Gets the versions dao.
	 * 
	 * @return the versions dao
	 */
	public VersionsDao getVersionsDao() {
		return versionsDao;
	}

	/**
	 * Sets the versions dao.
	 * 
	 * @param versionsDao the new versions dao
	 */
	public void setVersionsDao(VersionsDao versionsDao) {
		this.versionsDao = versionsDao;
	}

	@ClearCache
	public void removeValueSetDefinitionByValueSetDefinitionURI(String valueSetDefinitionURI) {
		
		String valueSetDefGuid = (String) this.getSqlMapClientTemplate().queryForObject(GET_VALUESET_DEFINITION_GUID_BY_VALUESET_DEFINITION_URI_SQL, new PrefixedParameter(null, valueSetDefinitionURI));
		
		// remove definition entries
		this.getSqlMapClientTemplate().delete(REMOVE_DEFINITION_ENTRY_BY_VALUESET_DEFINITION_GUID_SQL, new PrefixedParameter(null, valueSetDefGuid));
		
		// remove value set properties
		this.vsPropertyDao.deleteAllValueSetDefinitionProperties(valueSetDefGuid);
		
		// remove value set definition mappings
		deleteValueSetDefinitionMappings(valueSetDefGuid);
		
		// remove value set definition source list
		this.getSqlMapClientTemplate().delete(DELETE_SOURCE_BY_PARENT_GUID_AND_TYPE_SQL, new PrefixedParameterTuple(null, valueSetDefGuid, ReferenceType.VALUESETDEFINITION.name()));
		
		// remove realm or context list
		this.getSqlMapClientTemplate().delete(DELETE_CONTEXT_BY_PARENT_GUID_AND_TYPE_SQL, new PrefixedParameterTuple(null, valueSetDefGuid, ReferenceType.VALUESETDEFINITION.name()));
		
		// remove value set definition
		this.getSqlMapClientTemplate().
			delete(REMOVE_VALUESET_DEFINITION_BY_VALUESET_DEFINITION_URI_SQL, new PrefixedParameter(null, valueSetDefinitionURI));	
	}

	@Override
	public String insertDefinitionEntry(String valueSetDefinitionGuid,
			DefinitionEntry vsdEntry) {
		String vsdEntryGuid = this.createUniqueId();
		
		InsertOrUpdateDefinitionEntryBean vsdEntryBean = new InsertOrUpdateDefinitionEntryBean();
		vsdEntryBean.setId(vsdEntryGuid);
		vsdEntryBean.setDefinitionEntry(vsdEntry);
		vsdEntryBean.setValueSetDefGuid(valueSetDefinitionGuid);
		
		if (vsdEntry.getCodingSchemeReference() != null)
		{
			vsdEntryBean.setCodingSchemeReference(vsdEntry.getCodingSchemeReference().getCodingScheme());
		}
		
		else if (vsdEntry.getValueSetDefinitionReference() != null)
		{
			vsdEntryBean.setValueSetDefReference(vsdEntry.getValueSetDefinitionReference().getValueSetDefinitionURI());
		}
		else if (vsdEntry.getEntityReference() != null)
		{
			vsdEntryBean.setEntityCode(vsdEntry.getEntityReference().getEntityCode());
			vsdEntryBean.setEntityCodeNamespace(vsdEntry.getEntityReference().getEntityCodeNamespace());
			vsdEntryBean.setLeafOnly(vsdEntry.getEntityReference().getLeafOnly());
			vsdEntryBean.setReferenceAssociation(vsdEntry.getEntityReference().getReferenceAssociation());
			vsdEntryBean.setTargetToSource(vsdEntry.getEntityReference().getTargetToSource());
			vsdEntryBean.setTransitiveClosure(vsdEntry.getEntityReference().getTransitiveClosure());
		}
		else if (vsdEntry.getPropertyReference() != null)
		{
			vsdEntryBean.setPropertyName(vsdEntry.getPropertyReference().getPropertyName());
			vsdEntryBean.setPropertyRefCodingScheme(vsdEntry.getPropertyReference().getCodingScheme());
			if (vsdEntry.getPropertyReference().getPropertyMatchValue() != null)
			{
				vsdEntryBean.setPropertyMatchValue(vsdEntry.getPropertyReference().getPropertyMatchValue().getContent());
				vsdEntryBean.setFormat(vsdEntry.getPropertyReference().getPropertyMatchValue().getDataType());
				vsdEntryBean.setMatchAlgorithm(vsdEntry.getPropertyReference().getPropertyMatchValue().getMatchAlgorithm());
			}
		}			
		
		// insert into vsdEntry table
		this.getSqlMapClientTemplate().insert(INSERT_DEFINITION_ENTRY_SQL, vsdEntryBean);
		
		return vsdEntryGuid;
	}
	
	@SuppressWarnings("unchecked")
	public void insertMappings(String referenceGuid, Mappings mappings){
		if(mappings == null){
			return;
		}
		for(Field field : mappings.getClass().getDeclaredFields()){
			if(field.getName().startsWith(SUPPORTED_ATTRIB_GETTER_PREFIX)){
				field.setAccessible(true);
				try {
					List<URIMap> urimapList = (List<URIMap>) field.get(mappings);
					this.insertURIMap(referenceGuid, urimapList);
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 
			}
		}
	}
	
	public void insertURIMap(final String referenceGuid,
			final List<URIMap> urimapList) {
		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){
	
			public Object doInSqlMapClient(SqlMapExecutor executor)
			throws SQLException {
				executor.startBatch();
				for(URIMap uriMap : urimapList){
					String uriMapId = UUID.randomUUID().toString();
					
					executor.insert(INSERT_URIMAPS_SQL, 
							buildInsertOrUpdateURIMapBean(
									null,
									uriMapId, 
									referenceGuid,
									classToStringMappingClassifier.classify(uriMap.getClass()),
									uriMap));
				}
				return executor.executeBatch();
			}	
		});		
	}
	
	public void insertURIMap(String referenceGuid, URIMap uriMap) {
		String uriMapId = this.createUniqueId();
		this.getSqlMapClientTemplate().insert(
				INSERT_URIMAPS_SQL, buildInsertOrUpdateURIMapBean(
									null,
									uriMapId, 
									referenceGuid,
									classToStringMappingClassifier.classify(uriMap.getClass()),
									uriMap));
	}
	
	/**
	 * Builds the insert uri map bean.
	 * 
	 * @param prefix the prefix
	 * @param uriMapId the uri map id
	 * @param codingSchemeId the coding scheme id
	 * @param supportedAttributeTag the supported attribute tag
	 * @param uriMap the uri map
	 * 
	 * @return the insert uri map bean
	 */
	protected InsertOrUpdateURIMapBean buildInsertOrUpdateURIMapBean(String prefix, String uriMapId, String referenceGuid, String supportedAttributeTag, URIMap uriMap){
		InsertOrUpdateURIMapBean bean = new InsertOrUpdateURIMapBean();
		bean.setPrefix(prefix);
		bean.setSupportedAttributeTag(supportedAttributeTag);
		bean.setCodingSchemeId(referenceGuid);
		bean.setUriMap(uriMap);
		bean.setId(uriMapId);
		
		return bean;
	}
	
	public void deleteValueSetDefinitionMappings(String referenceGuid) {
		this.getSqlMapClientTemplate().delete(
				DELETE_URIMAPS_BY_REFERENCE_GUID_SQL, 
				new PrefixedParameter(null, referenceGuid));
	}
	
	/**
	 * @return the vsPropertyDao
	 */
	public VSPropertyDao getVsPropertyDao() {
		return vsPropertyDao;
	}

	/**
	 * @param vsPropertyDao the vsPropertyDao to set
	 */
	public void setVsPropertyDao(VSPropertyDao vsPropertyDao) {
		this.vsPropertyDao = vsPropertyDao;
	}
}

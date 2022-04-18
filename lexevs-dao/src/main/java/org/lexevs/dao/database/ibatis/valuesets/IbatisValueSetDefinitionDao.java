
package org.lexevs.dao.database.ibatis.valuesets;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedConceptDomain;
import org.LexGrid.naming.SupportedContext;
import org.LexGrid.naming.SupportedHierarchy;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.naming.SupportedStatus;
import org.LexGrid.naming.URIMap;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitions;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.dao.database.access.valuesets.VSDefinitionEntryDao;
import org.lexevs.dao.database.access.valuesets.VSEntryStateDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao;
import org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao.ReferenceType;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.constants.classifier.mapping.ClassToStringMappingClassifier;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.codingscheme.parameter.InsertOrUpdateURIMapBean;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedTableParameterBean;
import org.lexevs.dao.database.ibatis.valuesets.parameter.InsertOrUpdateValueSetsMultiAttribBean;
import org.lexevs.dao.database.ibatis.valuesets.parameter.InsertValueSetDefinitionBean;
import org.lexevs.dao.database.ibatis.valuesets.helper.ValueSetDefinitionMapHelper;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.registry.service.Registry.ResourceType;


import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * The Class IbatisValueSetDefinitionDao.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
@Cacheable(cacheName = "IbatisValueSetDefinitionDao")
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
	
	public static String GET_VALUESETSCHEMEREF_FOR_TOP_NODE_SOURCE_CODE = VALUESETDEFINITION_NAMESPACE + "getValueSetSchemeRefForTopNodeSourceCode";
	
	public static String GET_VALUESET_DEFINITION_GUID_BY_VALUESET_DEFINITION_URI_SQL = VALUESETDEFINITION_NAMESPACE + "getValueSetDefinitionGuidByValueSetDefinitionURI";
	
	public static String GET_VALUESET_DEFINITION_BY_VALUESET_DEFINITION_URI_SQL = VALUESETDEFINITION_NAMESPACE + "getValueSetDefinitionByValueSetURI";
	
	public static String GET_VALUESET_DEFINITION_METADATA_BY_VALUESET_DEFINITION_URI_SQL = VALUESETDEFINITION_NAMESPACE + "getValueSetDefinitionMetaDataByValueSetURI";
	
	public static String GET_DEFINITION_ENTRY_BY_VALUESET_DEFINITION_GUID_SQL = VALUESETDEFINITION_NAMESPACE + "getDefinitionEntryByValueSetGuid";
	
	public static String REMOVE_VALUESET_DEFINITION_BY_VALUESET_DEFINITION_URI_SQL = VALUESETDEFINITION_NAMESPACE + "removevalueSetDefinitionByValueSetDefinitionURI";
	
	public static String REMOVE_DEFINITION_ENTRY_BY_VALUESET_DEFINITION_GUID_SQL = VALUESETDEFINITION_NAMESPACE + "removeDefinitionEntryByValueSetDefinitionGuid";
	
	public static String GET_SOURCE_LIST_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "getSourceListByParentGuidandType";
	
	public static String GET_CONTEXT_LIST_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "getContextListByParentGuidandType";
	
	public static String INSERT_MULTI_ATTRIB_SQL = VS_MULTIATTRIB_NAMESPACE + "insertMultiAttrib";
	
	public static String DELETE_SOURCE_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "deleteSourceByParentGuidAndType";
	
	public static String DELETE_MAPPINGS_By_REFERENCE_GUID_TYPE_AND_SUPP_ATTRIB_SQL = VS_MAPPING_NAMESPACE + "deleteMappingsByReferenceGuidTypeAndSuppAttrib";
	
	public static String DELETE_CONTEXT_BY_PARENT_GUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "deleteContextByParentGuidAndType";
	
	public static String DELETE_PICKLIST_ENTRY_CONTEXT_BY_PICKLIST_GUID_SQL = VS_MULTIATTRIB_NAMESPACE + "deletePickListEntryContextByPickListGuid";
	
	public static String GET_URIMAPS_BY_REFERENCE_GUID_SQL = VS_MAPPING_NAMESPACE + "getURIMaps";
	
	public static String GET_URIMAPS_BY_REFERENCE_GUID_LOCALNAME_AND_TYPE_SQL = VS_MAPPING_NAMESPACE + "getURIMapByLocalNameAndType";
	
	public static String GET_VALUESETDEFINITIONURI_FOR_SUPPORTED_TAG_AND_VALUE_SQL = VS_MAPPING_NAMESPACE + "getValueSetDefinitionURIForSupportedTagAndValue";
	
	public static String GET_VALUESETDEFINITIONURI_FOR_SUPPORTED_TAG_AND_VALUE_AND_URI_SQL = VS_MAPPING_NAMESPACE + "getValueSetDefinitionURIForSupportedTagAndValueAndURI";
	
	public static String INSERT_URIMAPS_SQL = VS_MAPPING_NAMESPACE + "insertURIMap";
	
	public static String UPDATE_URIMAPS_BY_LOCALID_SQL = VS_MAPPING_NAMESPACE + "updateUriMapByLocalId";
	
	public static String DELETE_URIMAPS_BY_REFERENCE_GUID_SQL = VS_MAPPING_NAMESPACE + "deleteMappingsByReferenceGuid";
	
	private static String GET_VALUESET_DEFINITION_METADATA_BY_UID_SQL = VALUESETDEFINITION_NAMESPACE + "getValueSetDefinitionMetadataByUId";
	
	private static String UPDATE_VALUE_SET_DEFINITION_BY_ID_SQL = VALUESETDEFINITION_NAMESPACE + "updateValueSetDefinitionByUId";
	
	private static String UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "updateMultiAttribEntryStateUId";
	
	private static String UPDATE_VALUE_SET_DEFINITION_VERSIONABLE_CHANGES_BY_ID_SQL = VALUESETDEFINITION_NAMESPACE + "updateValueSetDefVersionableChangesByUId";
	
	private static String GET_ENTRYSTATE_UID_BY_VALUESET_DEFINITION_UID_SQL = VALUESETDEFINITION_NAMESPACE + "getEntryStateUIdByValuesetDefUId";
	
	private static String UPDATE_VALUESETDEFINITION_ENTRYSTATE_UID_SQL = VALUESETDEFINITION_NAMESPACE + "updateValueSetDefinitinEntryStateUId";
	
	private static String GET_VALUESET_DEFINITION_LATEST_REVISION_ID_BY_UID = VALUESETDEFINITION_NAMESPACE + "getValueSetDefinitionLatestRevisionIdByUId";
	
//	private static String GET_PREV_REV_ID_FROM_GIVEN_REV_ID_FOR_VALUESETDEF_SQL = VALUESETDEFINITION_NAMESPACE + "getPrevRevisionIdFromGivenRevIdForValueSetDefinition";
	
	private static String GET_VALUESET_DEFINITION_METADATA_FROM_HISTORY_BY_REVISION_SQL = VALUESETDEFINITION_NAMESPACE + "getValueSetDefinitionMetaDataHistoryByRevision";
	
	private static String GET_VALUESET_DEFINITION_METADATA_FROM_BASE_BY_REVISION_SQL = VALUESETDEFINITION_NAMESPACE + "getValueSetDefinitionMetaDataByRevision";
	
	public static String GET_SOURCE_LIST_FROM_HISTORY_BY_PARENT_ENTRYSTATEGUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "getSourceListFromHistoryByParentEntryStateGuidandType";
	
	public static String GET_CONTEXT_LIST_FROM_HISTORY_BY_PARENT_ENTRYSTATEGUID_AND_TYPE_SQL = VS_MULTIATTRIB_NAMESPACE + "getContextListFromHistoryByParentEntryStateGuidandType";
	
	public static String GET_DEFINITION_ENTRY_LIST_BY_VALUESET_DEFINITION_URI_SQL = VALUESETDEFINITION_NAMESPACE + "getDefinitionEntryListByValSetDefURI";
	
	public static String GET_VALUESET_DEF_PROPERTY_LIST_BY_VALUESET_DEFINITION_URI_SQL = VALUESETDEFINITION_NAMESPACE + "getValueSetDefPropertyListByValSetDefURI";
	
	private static final String GET_VS_URI_BY_CONTEXT = VALUESETDEFINITION_NAMESPACE +  "getValueSetURIsByContext";
	
	private static final String GET_MAP_OF_ALL_VSD_WITH_URI_KEY = VALUESETDEFINITION_NAMESPACE +  "getValueSetDefinitionMetadataHashMapByRegistryDesignation";
	
	/** The versions dao. */
	private VersionsDao versionsDao;
	
	private VSDefinitionEntryDao vsDefinitionEntryDao;
	
	private VSPropertyDao vsPropertyDao;
	
	private VSEntryStateDao vsEntryStateDao;
	
	/** The class to string mapping classifier. */
	private ClassToStringMappingClassifier classToStringMappingClassifier = new ClassToStringMappingClassifier();

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao#getValueSetDefinitionByURI(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
//	@CacheMethod
	public ValueSetDefinition getValueSetDefinitionByURI(String valueSetDefinitionURI) {
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		InsertValueSetDefinitionBean vsdBean = (InsertValueSetDefinitionBean) 
			this.getSqlSessionTemplate().selectOne(GET_VALUESET_DEFINITION_METADATA_BY_VALUESET_DEFINITION_URI_SQL, 
				new PrefixedParameter(prefix, valueSetDefinitionURI));
		
		ValueSetDefinition vsd = null;
		if (vsdBean != null)
		{
			vsd = vsdBean.getValueSetDefinition();
			
			String vsdGuid = vsdBean.getUId();
			
			vsd.setEntryState(vsEntryStateDao.getEntryStateByUId(vsdBean.getEntryStateUId()));
			
			List<DefinitionEntry> des = this.getSqlSessionTemplate().selectList(GET_DEFINITION_ENTRY_BY_VALUESET_DEFINITION_GUID_SQL,
					new PrefixedParameter(prefix, vsdGuid));
			
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
			List<Source> sourceList = this.getSqlSessionTemplate().selectList(GET_SOURCE_LIST_BY_PARENT_GUID_AND_TYPE_SQL, 
					new PrefixedParameterTuple(prefix, vsdGuid, ReferenceType.VALUESETDEFINITION.name())); 
			
			if (sourceList != null)
				vsd.setSource(sourceList);
			
			// Get realm or context list
			List<String> contextList = this.getSqlSessionTemplate().selectList(GET_CONTEXT_LIST_BY_PARENT_GUID_AND_TYPE_SQL, 
					new PrefixedParameterTuple(prefix, vsdGuid, ReferenceType.VALUESETDEFINITION.name())); 
			
			if (contextList != null)
				vsd.setRepresentsRealmOrContext(contextList);
			
			vsd.setMappings(getMappings(vsdGuid));
		}
		return vsd;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao#getGuidFromvalueSetDefinitionURI(java.lang.String)
	 */
	@Override
//	@CacheMethod
	public String getGuidFromvalueSetDefinitionURI(String valueSetDefinitionURI) {
		
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		String valueSetDefGuid = (String) 
		this.getSqlSessionTemplate().selectOne(GET_VALUESET_DEFINITION_GUID_BY_VALUESET_DEFINITION_URI_SQL, 
			new PrefixedParameter(prefix, valueSetDefinitionURI));
		
		return valueSetDefGuid;
	}

	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public List<String> getAllValueSetDefinitionsWithNoName() throws LBException {
		return this.getSqlSessionTemplate().selectList(GET_VALUESET_DEFINITION_URI_FOR_VALUESET_NAME_SQL,
				new PrefixedParameter(this.getPrefixResolver().resolveDefaultPrefix(), " "));
	}

	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public List<String> getValueSetDefinitionURIsForName(String valueSetDefinitionName)
			throws LBException {
		if (valueSetDefinitionName == null)
			return getValueSetDefinitionURIs();		
		else if (StringUtils.isBlank(valueSetDefinitionName))
			return getAllValueSetDefinitionsWithNoName();
		else
			return this.getSqlSessionTemplate().selectList(GET_VALUESET_DEFINITION_URI_FOR_VALUESET_NAME_SQL,
					new PrefixedParameter(this.getPrefixResolver().resolveDefaultPrefix(), valueSetDefinitionName));
	}
	
    @SuppressWarnings("unchecked")
	@Override
	public List<AbsoluteCodingSchemeVersionReference> getValueSetDefinitionSchemeRefForTopNodeSourceCode(String code){
		return (List<AbsoluteCodingSchemeVersionReference>) this.getSqlSessionTemplate().<AbsoluteCodingSchemeVersionReference>selectList( GET_VALUESETSCHEMEREF_FOR_TOP_NODE_SOURCE_CODE,
				new PrefixedParameterTuple(this.getPrefixResolver().resolveDefaultPrefix(), code, ResourceType.CODING_SCHEME.name()));
	}
    
    @SuppressWarnings("unchecked")
	@Override
	public List<AbsoluteCodingSchemeVersionReference> getValueSetDefinitionDefRefForTopNodeSourceCode(String code){
		return (List<AbsoluteCodingSchemeVersionReference>) this.getSqlSessionTemplate().<AbsoluteCodingSchemeVersionReference>selectList( GET_VALUESETSCHEMEREF_FOR_TOP_NODE_SOURCE_CODE,
				new PrefixedParameterTuple(this.getPrefixResolver().resolveDefaultPrefix(), code, ResourceType.VALUESET_DEFINITION.name()));
	}

	@Override
	public String insertValueSetDefinition(String systemReleaseURI,
			ValueSetDefinition vsdef, Mappings mappings) {
		
		String valueSetDefinitionGuid = this.createUniqueId();
		String vsEntryStateGuid = this.createUniqueId();
		
		String systemReleaseId = null;
		if(StringUtils.isNotBlank(systemReleaseURI)){
			systemReleaseId = this.versionsDao.getSystemReleaseIdByUri(systemReleaseURI);
		}
		
		EntryState entryState = vsdef.getEntryState();
		
		if (entryState != null)
		{
			this.vsEntryStateDao.insertEntryState(vsEntryStateGuid, valueSetDefinitionGuid, 
					ReferenceType.VALUESETDEFINITION.name(), null, entryState);
		}
		
		InsertValueSetDefinitionBean vsDefBean = new InsertValueSetDefinitionBean();
		vsDefBean.setUId(valueSetDefinitionGuid);
		vsDefBean.setValueSetDefinition(vsdef);
		vsDefBean.setPrefix(this.getPrefixResolver().resolveDefaultPrefix());
		vsDefBean.setSystemReleaseUId(systemReleaseId);
		vsDefBean.setEntryStateUId(vsEntryStateGuid);
		
		// insert into value set definition table
		this.getSqlSessionTemplate().insert(INSERT_VALUESET_DEFINITION_SQL, vsDefBean);
		
		// insert definition entry
		for (DefinitionEntry vsdEntry : vsdef.getDefinitionEntryAsReference()) 
		{
			this.vsDefinitionEntryDao.insertDefinitionEntry(valueSetDefinitionGuid, vsdEntry);
		}
		
		// insert value set definition properties
		if (vsdef.getProperties() != null)
		{
			for (Property property : vsdef.getProperties().getPropertyAsReference())
			{
				this.vsPropertyDao.insertProperty(valueSetDefinitionGuid, ReferenceType.VALUESETDEFINITION, property);
			}
		}
		
		// insert realm or context list
		for (String context : vsdef.getRepresentsRealmOrContextAsReference())
		{
			InsertOrUpdateValueSetsMultiAttribBean insertOrUpdateValueSetsMultiAttribBean = new InsertOrUpdateValueSetsMultiAttribBean();
			insertOrUpdateValueSetsMultiAttribBean.setUId(this.createUniqueId());
			insertOrUpdateValueSetsMultiAttribBean.setReferenceUId(valueSetDefinitionGuid);
			insertOrUpdateValueSetsMultiAttribBean.setReferenceType(ReferenceType.VALUESETDEFINITION.name());
			insertOrUpdateValueSetsMultiAttribBean.setAttributeType(SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT);
			insertOrUpdateValueSetsMultiAttribBean.setAttributeValue(context);
			insertOrUpdateValueSetsMultiAttribBean.setRole(null);
			insertOrUpdateValueSetsMultiAttribBean.setSubRef(null);
			insertOrUpdateValueSetsMultiAttribBean.setEntryStateUId(vsEntryStateGuid);
			insertOrUpdateValueSetsMultiAttribBean.setPrefix(this.getPrefixResolver().resolveDefaultPrefix());
			
			this.getSqlSessionTemplate().insert(INSERT_MULTI_ATTRIB_SQL, insertOrUpdateValueSetsMultiAttribBean);
		}
		
		// insert value set definition source list
		for (Source source : vsdef.getSourceAsReference())
		{
			InsertOrUpdateValueSetsMultiAttribBean insertOrUpdateValueSetsMultiAttribBean = new InsertOrUpdateValueSetsMultiAttribBean();
			insertOrUpdateValueSetsMultiAttribBean.setUId(this.createUniqueId());
			insertOrUpdateValueSetsMultiAttribBean.setReferenceUId(valueSetDefinitionGuid);
			insertOrUpdateValueSetsMultiAttribBean.setReferenceType(ReferenceType.VALUESETDEFINITION.name());
			insertOrUpdateValueSetsMultiAttribBean.setAttributeType(SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE);
			insertOrUpdateValueSetsMultiAttribBean.setAttributeValue(source.getContent());
			insertOrUpdateValueSetsMultiAttribBean.setRole(source.getRole());
			insertOrUpdateValueSetsMultiAttribBean.setSubRef(source.getSubRef());
			insertOrUpdateValueSetsMultiAttribBean.setEntryStateUId(vsEntryStateGuid);
			insertOrUpdateValueSetsMultiAttribBean.setPrefix(this.getPrefixResolver().resolveDefaultPrefix());
			
			this.getSqlSessionTemplate().insert(INSERT_MULTI_ATTRIB_SQL, insertOrUpdateValueSetsMultiAttribBean);
		}
		
		// insert value set definition mappings
		if (mappings != null)
			insertMappings(valueSetDefinitionGuid, mappings);
		
		if (vsdef.getMappings() != null)
			insertMappings(valueSetDefinitionGuid, vsdef.getMappings());
		
//		insertNonSuppliedMappings(valueSetDefinitionGuid, vsdef);
		
		// insert missing concept domain and usage context mappings
		insertConceptDomainAndUsageContextMappings(valueSetDefinitionGuid, vsdef);
		
		return valueSetDefinitionGuid;		
	}

	@Override
	public String insertHistoryValueSetDefinition(String valueSetDefUId) {

		String prefix = getPrefix();
		
		InsertValueSetDefinitionBean vsDefBean = (InsertValueSetDefinitionBean) this
				.getSqlSessionTemplate().selectOne(
						GET_VALUESET_DEFINITION_METADATA_BY_UID_SQL,
						new PrefixedParameter(prefix, valueSetDefUId));
	
		String histPrefix = this.getPrefixResolver().resolveHistoryPrefix();
		
		vsDefBean.setPrefix(histPrefix);
		
		this.getSqlSessionTemplate().insert(
				INSERT_VALUESET_DEFINITION_SQL, vsDefBean);
		
		for (InsertOrUpdateValueSetsMultiAttribBean vsMultiAttrib : vsDefBean.getVsMultiAttribList())
		{
			vsMultiAttrib.setPrefix(histPrefix);
			
			this.getSqlSessionTemplate().insert(INSERT_MULTI_ATTRIB_SQL, vsMultiAttrib);
		}
		
		if (!vsEntryStateExists(prefix, vsDefBean.getEntryStateUId())) {

			EntryState entryState = new EntryState();

			entryState.setChangeType(ChangeType.NEW);
			entryState.setRelativeOrder(0L);

			vsEntryStateDao
					.insertEntryState(vsDefBean.getEntryStateUId(),
							vsDefBean.getUId(), ReferenceType.VALUESETDEFINITION.name(), null,
							entryState);
		}
		
		return vsDefBean.getEntryStateUId();
	}
	
	@Override
	public void insertValueSetDefinitions(String systemReleaseURI,
			ValueSetDefinitions vsdefs, Mappings mappings) {

		for (ValueSetDefinition vsdef : vsdefs.getValueSetDefinitionAsReference())
		{
			insertValueSetDefinition(systemReleaseURI, vsdef, mappings);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao#insertValueSetDefinition(java.lang.String, org.LexGrid.valueSets.ValueSetDefinition)
	 */
	@Override
	public String insertValueSetDefinition(String systemReleaseUri,
			ValueSetDefinition definition) {
		
		return insertValueSetDefinition(systemReleaseUri, definition, null);
	}

	@Override
	public String updateValueSetDefinition(String valueSetDefUId,
			ValueSetDefinition valueSetDefinition) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		String entryStateUId = this.createUniqueId();
		
		InsertValueSetDefinitionBean bean = new InsertValueSetDefinitionBean();
		bean.setPrefix(prefix);
		bean.setValueSetDefinition(valueSetDefinition);
		bean.setUId(valueSetDefUId);
		bean.setEntryStateUId(entryStateUId);
		
		this.getSqlSessionTemplate().update(UPDATE_VALUE_SET_DEFINITION_BY_ID_SQL, bean);
		
		if (StringUtils.isEmpty(valueSetDefinition.getConceptDomain()) || StringUtils.isBlank(valueSetDefinition.getConceptDomain()))
			deleteURIMap(prefix, valueSetDefUId, ReferenceType.VALUESETDEFINITION.name(), SQLTableConstants.TBLCOLVAL_SUPPTAG_CONCEPTDOMAIN);
		
		if( valueSetDefinition.getSourceCount() != 0 ) {
			
			this.getSqlSessionTemplate().delete(
					DELETE_SOURCE_BY_PARENT_GUID_AND_TYPE_SQL,
					new PrefixedParameterTuple(prefix, valueSetDefUId, ReferenceType.VALUESETDEFINITION.name()));
			
			Source[] sourceList = valueSetDefinition.getSource();
			
			for (int i = 0; i < sourceList.length; i++) {
				InsertOrUpdateValueSetsMultiAttribBean insertOrUpdateValueSetsMultiAttribBean = new InsertOrUpdateValueSetsMultiAttribBean();
				insertOrUpdateValueSetsMultiAttribBean.setUId(this.createUniqueId());
				insertOrUpdateValueSetsMultiAttribBean.setReferenceUId(valueSetDefUId);
				insertOrUpdateValueSetsMultiAttribBean.setReferenceType(ReferenceType.VALUESETDEFINITION.name());
				insertOrUpdateValueSetsMultiAttribBean.setAttributeType(SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE);
				insertOrUpdateValueSetsMultiAttribBean.setAttributeValue(sourceList[i].getContent());
				insertOrUpdateValueSetsMultiAttribBean.setRole(sourceList[i].getRole());
				insertOrUpdateValueSetsMultiAttribBean.setSubRef(sourceList[i].getSubRef());
				insertOrUpdateValueSetsMultiAttribBean.setEntryStateUId(entryStateUId);
				insertOrUpdateValueSetsMultiAttribBean.setPrefix(prefix);
				
				this.getSqlSessionTemplate().insert(INSERT_MULTI_ATTRIB_SQL, insertOrUpdateValueSetsMultiAttribBean);
			}
		} else {
			
			this.getSqlSessionTemplate().update(
					UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
					new PrefixedParameterTriple(prefix, valueSetDefUId,
							SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE,
							entryStateUId));
		}
		
		if( valueSetDefinition.getRepresentsRealmOrContextCount() != 0 ) {
			
			this.getSqlSessionTemplate().delete(
					DELETE_CONTEXT_BY_PARENT_GUID_AND_TYPE_SQL,
					new PrefixedParameterTuple(prefix, valueSetDefUId,
							ReferenceType.VALUESETDEFINITION.name()));
			
			String[] contextList = valueSetDefinition.getRepresentsRealmOrContext();
			
			for (int i = 0; i < contextList.length; i++) {
				InsertOrUpdateValueSetsMultiAttribBean insertOrUpdateValueSetsMultiAttribBean = new InsertOrUpdateValueSetsMultiAttribBean();
				insertOrUpdateValueSetsMultiAttribBean.setUId(this.createUniqueId());
				insertOrUpdateValueSetsMultiAttribBean.setReferenceUId(valueSetDefUId);
				insertOrUpdateValueSetsMultiAttribBean.setReferenceType(ReferenceType.VALUESETDEFINITION.name());
				insertOrUpdateValueSetsMultiAttribBean.setAttributeType(SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT);
				insertOrUpdateValueSetsMultiAttribBean.setAttributeValue(contextList[i]);
				insertOrUpdateValueSetsMultiAttribBean.setRole(null);
				insertOrUpdateValueSetsMultiAttribBean.setSubRef(null);
				insertOrUpdateValueSetsMultiAttribBean.setEntryStateUId(entryStateUId);
				insertOrUpdateValueSetsMultiAttribBean.setPrefix(prefix);
				
				this.getSqlSessionTemplate().insert(INSERT_MULTI_ATTRIB_SQL, insertOrUpdateValueSetsMultiAttribBean);
			}
		} else {
			
			this.getSqlSessionTemplate().update(
					UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
					new PrefixedParameterTriple(prefix, valueSetDefUId,
							SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT,
							entryStateUId));
		}
		
		if (valueSetDefinition.getMappings() != null)
			insertMappings(valueSetDefUId, valueSetDefinition.getMappings());
		
		return entryStateUId;
	}
	
	@Override
	public String updateValueSetDefinitionVersionableChanges(
			String valueSetDefUId, ValueSetDefinition valueSetDefinition) {
		

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		String entryStateUId = this.createUniqueId();
		
		InsertValueSetDefinitionBean bean = new InsertValueSetDefinitionBean();
		bean.setPrefix(prefix);
		bean.setValueSetDefinition(valueSetDefinition);
		bean.setUId(valueSetDefUId);
		bean.setEntryStateUId(entryStateUId);
		
		this.getSqlSessionTemplate().update(UPDATE_VALUE_SET_DEFINITION_VERSIONABLE_CHANGES_BY_ID_SQL, bean);
		
		this.getSqlSessionTemplate().update(
				UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
				new PrefixedParameterTriple(prefix, valueSetDefUId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE,
						entryStateUId));
		
		this.getSqlSessionTemplate().update(
				UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
				new PrefixedParameterTriple(prefix, valueSetDefUId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT,
						entryStateUId));
		
		return entryStateUId;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao#getValueSetDefinitionURIs()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getValueSetDefinitionURIs() {
		return this.getSqlSessionTemplate().selectList(GET_VALUESET_DEFINITION_URIS_SQL, new PrefixedParameter(this.getPrefixResolver().resolveDefaultPrefix(), null));
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
		return DaoUtility.createNonTypedList(supportedDatebaseVersion);
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
		
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		String valueSetDefGuid = (String) this.getSqlSessionTemplate().selectOne(GET_VALUESET_DEFINITION_GUID_BY_VALUESET_DEFINITION_URI_SQL, new PrefixedParameter(prefix, valueSetDefinitionURI));
		
		//remove entrystates
		this.vsEntryStateDao.deleteAllEntryStatesOfValueSetDefinitionByUId(valueSetDefGuid);
		
		// remove definition entries
		this.getSqlSessionTemplate().delete(REMOVE_DEFINITION_ENTRY_BY_VALUESET_DEFINITION_GUID_SQL, new PrefixedParameter(prefix, valueSetDefGuid));
		
		// remove value set properties
		this.vsPropertyDao.deleteAllValueSetDefinitionProperties(valueSetDefGuid);
		
		// remove value set definition mappings
		deleteValueSetDefinitionMappings(valueSetDefGuid);
		
		// remove value set definition source list
		this.getSqlSessionTemplate().delete(DELETE_SOURCE_BY_PARENT_GUID_AND_TYPE_SQL, new PrefixedParameterTuple(prefix, valueSetDefGuid, ReferenceType.VALUESETDEFINITION.name()));
		
		// remove realm or context list
		this.getSqlSessionTemplate().delete(DELETE_CONTEXT_BY_PARENT_GUID_AND_TYPE_SQL, new PrefixedParameterTuple(prefix, valueSetDefGuid, ReferenceType.VALUESETDEFINITION.name()));
		
		// remove value set definition
		this.getSqlSessionTemplate().
			delete(REMOVE_VALUESET_DEFINITION_BY_VALUESET_DEFINITION_URI_SQL, new PrefixedParameter(prefix, valueSetDefinitionURI));	
	}
	
	@SuppressWarnings("unchecked")
	private Mappings getMappings(String referenceGuid) {
		Mappings mappings = new Mappings();
		
		List<URIMap> uriMaps = this.getSqlSessionTemplate().selectList(	
				GET_URIMAPS_BY_REFERENCE_GUID_SQL, 
				new PrefixedParameterTuple(this.getPrefixResolver().resolveDefaultPrefix(), referenceGuid, ReferenceType.VALUESETDEFINITION.name()));
		
		for(URIMap uriMap : uriMaps) {
			DaoUtility.insertIntoMappings(mappings, uriMap);
		}
		
		return mappings;
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
					if (e.getMessage().indexOf("Duplicate") == -1 
							&& e.getMessage().indexOf("unique constraint") == -1
							&& e.getMessage().indexOf("SQLSTATE: 23505") == -1)
						throw new RuntimeException(e);
				} 
			}
		}
	}
	
	public void insertURIMap(final String referenceGuid,
			final List<URIMap> urimapList) {
		final String prefix  = this.getPrefixResolver().resolveDefaultPrefix();
		this.getSqlSessionBatchTemplate().execute(new SqlMapClientCallback(){
	
			public Object doInSqlMapClient(SqlMapExecutor executor)
			throws SQLException {
				executor.startBatch();
				for(URIMap uriMap : urimapList){
					if (uriMap instanceof SupportedConceptDomain)
					{
						deleteURIMap(prefix, referenceGuid, ReferenceType.VALUESETDEFINITION.name(), SQLTableConstants.TBLCOLVAL_SUPPTAG_CONCEPTDOMAIN);
					}
					
					String uriMapId = createUniqueId();
					
					executor.insert(INSERT_URIMAPS_SQL, 
							buildInsertOrUpdateURIMapBean(
									prefix,
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
		this.getSqlSessionTemplate().insert(
				INSERT_URIMAPS_SQL, buildInsertOrUpdateURIMapBean(
						this.getPrefixResolver().resolveDefaultPrefix(),
									uriMapId, 
									referenceGuid,
									classToStringMappingClassifier.classify(uriMap.getClass()),
									uriMap));
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao#deleteURIMap(java.lang.String, java.lang.String)
	 */
	@ClearCache
	public void deleteURIMap(String referenceGuid, String supportedAttributeTag){
		final String prefix  = this.getPrefixResolver().resolveDefaultPrefix();
		this.deleteURIMap(prefix, referenceGuid, ReferenceType.VALUESETDEFINITION.name(), supportedAttributeTag);
	}
	
	private void deleteURIMap(String prefix, String referenceGuid, String referenceType, String supportedAttributeTag) {
		this.getSqlSessionTemplate().delete(DELETE_MAPPINGS_By_REFERENCE_GUID_TYPE_AND_SUPP_ATTRIB_SQL, 
				new PrefixedParameterTriple(prefix, referenceGuid, referenceType, supportedAttributeTag));
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
		bean.setCodingSchemeUId(referenceGuid);
		bean.setReferenceType(ReferenceType.VALUESETDEFINITION.name());
		bean.setUriMap(uriMap);
		bean.setUId(uriMapId);
		
		if (uriMap instanceof SupportedHierarchy)
		{
			String associations = null;
			List<String> associationList = ((SupportedHierarchy) uriMap).getAssociationNamesAsReference();
			if (associationList != null) {
				for (int i = 0; i < associationList.size(); i++) {
					String assoc = (String) associationList.get(i);
					associations = i == 0 ? assoc : (associations += ("," + assoc));
				}
				bean.setAssociationNames(associations);
			}
		}
		
		
		return bean;
	}
	
	@ClearCache
	public void deleteValueSetDefinitionMappings(String referenceGuid) {
		this.getSqlSessionTemplate().delete(
				DELETE_URIMAPS_BY_REFERENCE_GUID_SQL, 
				new PrefixedParameterTuple(this.getPrefixResolver().resolveDefaultPrefix(), referenceGuid, ReferenceType.VALUESETDEFINITION.name()));
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

	/**
	 * @return the vsEntryStateDao
	 */
	public VSEntryStateDao getVsEntryStateDao() {
		return vsEntryStateDao;
	}

	/**
	 * @param vsEntryStateDao the vsEntryStateDao to set
	 */
	public void setVsEntryStateDao(VSEntryStateDao vsEntryStateDao) {
		this.vsEntryStateDao = vsEntryStateDao;
	}

	@Override
	public String getValueSetDefEntryStateUId(String valueSetDefUId) {
		
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		return (String) this.getSqlSessionTemplate().selectOne(GET_ENTRYSTATE_UID_BY_VALUESET_DEFINITION_UID_SQL,
				new PrefixedParameter(prefix, valueSetDefUId));
	}

	@Override
	public void updateValueSetDefEntryStateUId(String valueSetDefUId,
			String entryStateUId) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		this.getSqlSessionTemplate().update(
				UPDATE_VALUESETDEFINITION_ENTRYSTATE_UID_SQL, 
				new PrefixedParameterTuple(prefix, valueSetDefUId, entryStateUId));
		
		this.getSqlSessionTemplate().update(
				UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
				new PrefixedParameterTriple(prefix, valueSetDefUId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE,
						entryStateUId));
		
		this.getSqlSessionTemplate().update(
				UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
				new PrefixedParameterTriple(prefix, valueSetDefUId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT,
						entryStateUId));
	}

	/**
	 * @return the vsDefinitionEntryDao
	 */
	public VSDefinitionEntryDao getVsDefinitionEntryDao() {
		return vsDefinitionEntryDao;
	}

	/**
	 * @param vsDefinitionEntryDao the vsDefinitionEntryDao to set
	 */
	public void setVsDefinitionEntryDao(VSDefinitionEntryDao vsDefinitionEntryDao) {
		this.vsDefinitionEntryDao = vsDefinitionEntryDao;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getValueSetDefinitionURIForSupportedTagAndValue(
			String supportedTag, String value, String uri) {
		if (StringUtils.isNotEmpty(uri))
			return (List<String>) this.getSqlSessionTemplate().<String>selectList(GET_VALUESETDEFINITIONURI_FOR_SUPPORTED_TAG_AND_VALUE_AND_URI_SQL,
					new PrefixedParameterTriple(this.getPrefixResolver().resolveDefaultPrefix(), supportedTag, value, uri));
		else
			return (List<String>) this.getSqlSessionTemplate().<String>selectList(GET_VALUESETDEFINITIONURI_FOR_SUPPORTED_TAG_AND_VALUE_SQL,
				new PrefixedParameterTuple(this.getPrefixResolver().resolveDefaultPrefix(), supportedTag, value));
	}

	@Override
	public void insertDefinitionEntry(ValueSetDefinition vsdef,
			DefinitionEntry definitionEntry) {
		String vsdGUID = getGuidFromvalueSetDefinitionURI(vsdef.getValueSetDefinitionURI());
		if (vsdGUID != null)
			this.vsDefinitionEntryDao.insertDefinitionEntry(vsdGUID, definitionEntry);
		
	}
	
	@Override
	public String getLatestRevision(String valueSetDefUId) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		return (String) this.getSqlSessionTemplate().selectOne(
				GET_VALUESET_DEFINITION_LATEST_REVISION_ID_BY_UID, 
				new PrefixedParameter(prefix, valueSetDefUId));
	}

	
	@SuppressWarnings("unused")
	private void insertNonSuppliedMappings(String vsdGuid, ValueSetDefinition vsDef){
		if (vsDef == null)
			return;
		
		Mappings mappings = new Mappings();
		
		for (Source src : vsDef.getSourceAsReference())
		{
			SupportedSource suppSrc = new SupportedSource();
			suppSrc.setLocalId(src.getContent());
			suppSrc.setContent(src.getContent());
			suppSrc.setUri(null);
			mappings.addSupportedSource(suppSrc);
		}
		for (String ctx : vsDef.getRepresentsRealmOrContextAsReference())
		{
			SupportedContext suppCtx = new SupportedContext();
			suppCtx.setLocalId(ctx);
			suppCtx.setContent(ctx);
			suppCtx.setUri(null);
			mappings.addSupportedContext(suppCtx);
		}
		if (vsDef.getDefaultCodingScheme() != null)
		{
			SupportedCodingScheme suppCS = new SupportedCodingScheme();
			suppCS.setLocalId(vsDef.getDefaultCodingScheme());
			suppCS.setContent(vsDef.getDefaultCodingScheme());
			suppCS.setUri(null);
			mappings.addSupportedCodingScheme(suppCS);
		}
		if (vsDef.getConceptDomain() != null)
		{
			SupportedConceptDomain suppCD = new SupportedConceptDomain();
			suppCD.setLocalId(vsDef.getConceptDomain());
			suppCD.setContent(vsDef.getConceptDomain());
			suppCD.setUri(null);
			mappings.addSupportedConceptDomain(suppCD);
		}
		if (vsDef.getStatus() != null)
		{
			String status = vsDef.getStatus();
			SupportedStatus suppStatus = new SupportedStatus();
			suppStatus.setLocalId(status);
			suppStatus.setContent(status);
			suppStatus.setUri(null);
			mappings.addSupportedStatus(suppStatus);
		}
		if (vsDef.getDefinitionEntry() != null)
		{
			for (DefinitionEntry de : vsDef.getDefinitionEntryAsReference())
			{
				if (de.getCodingSchemeReference() != null)
				{
					String cs = de.getCodingSchemeReference().getCodingScheme();
					SupportedCodingScheme suppCS = new SupportedCodingScheme();
					suppCS.setLocalId(cs);
					suppCS.setContent(cs);
					suppCS.setUri(null);
					mappings.addSupportedCodingScheme(suppCS);
				}
				else if (de.getEntityReference() != null)
				{
					EntityReference er = de.getEntityReference();
					String ns = er.getEntityCodeNamespace();
					if (StringUtils.isNotEmpty(ns))
					{
						SupportedNamespace suppNS = new SupportedNamespace();
						suppNS.setLocalId(ns);
						suppNS.setContent(ns);
						suppNS.setUri(null);
						mappings.addSupportedNamespace(suppNS);
					}
					String assn = er.getReferenceAssociation();
					if (StringUtils.isNotEmpty(assn))
					{
						SupportedAssociation suppAssn = new SupportedAssociation();
						suppAssn.setLocalId(assn);
						suppAssn.setContent(assn);
						suppAssn.setUri(null);
						mappings.addSupportedAssociation(suppAssn);
					}
				}
				else if (de.getPropertyReference() != null)
				{
					String prop = de.getPropertyReference().getPropertyName();
					if (prop != null)
					{
						SupportedProperty suppProp = new SupportedProperty();
						suppProp.setLocalId(prop);
						suppProp.setContent(prop);
						suppProp.setUri(null);
						mappings.addSupportedProperty(suppProp);
					}
				}
			}
		}
		
		insertMappings(vsdGuid, mappings);
	}
	
	private void insertConceptDomainAndUsageContextMappings(String vsdGuid, ValueSetDefinition vsDef){
		if (vsDef == null)
			return;
		
		Mappings mappings = new Mappings();
		
		for (String ctx : vsDef.getRepresentsRealmOrContextAsReference())
		{
			SupportedContext suppCtx = new SupportedContext();
			suppCtx.setLocalId(ctx);
			suppCtx.setContent(ctx);
			suppCtx.setUri(null);
			mappings.addSupportedContext(suppCtx);
		}
		if (vsDef.getConceptDomain() != null)
		{
			SupportedConceptDomain suppCD = new SupportedConceptDomain();
			suppCD.setLocalId(vsDef.getConceptDomain());
			suppCD.setContent(vsDef.getConceptDomain());
			suppCD.setUri(null);
			mappings.addSupportedConceptDomain(suppCD);
		}
		
		insertMappings(vsdGuid, mappings);
	}
	
	@Override
	public boolean entryStateExists(String entryStateUId) {
		String prefix = this.getPrefix();
		
		return	super.vsEntryStateExists(prefix, entryStateUId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ValueSetDefinition getValueSetDefinitionByRevision(String valueSetDefURI,
			String revisionId) throws LBRevisionException {
		
		String prefix = this.getPrefix();
		
		String valueSetDefUId = this
				.getGuidFromvalueSetDefinitionURI(valueSetDefURI);

		if (valueSetDefUId == null) {
			throw new LBRevisionException(
					"ValueSetDefinition "
							+ valueSetDefUId
							+ " doesn't exist in lexEVS. "
							+ "Please check the URI. Its possible that the given ValueSetDefinition "
							+ "has been REMOVEd from the lexEVS system in the past.");
		}

		String valueSetDefRevisionId = this
				.getLatestRevision(valueSetDefUId);

		// 1. If 'revisionId' is null or 'revisionId' is the latest revision of the valueSetDefinition
		// then use getValueSetDefinitionByURI to get the ValueSetDefinition object and return.
		
		if (StringUtils.isEmpty(revisionId) || StringUtils.isEmpty(valueSetDefRevisionId)) {
			return this.getValueSetDefinitionByURI(valueSetDefURI);
		}
		
		ValueSetDefinition valueSetDefinition = null;
		InsertValueSetDefinitionBean vsDefBean = null;
			
		// 2. Check if the value set definition metatdata in base table is latest compared to the input revisionId
		// if we get it in the base, we can just return it. Else will have to get it from history
		vsDefBean = (InsertValueSetDefinitionBean) this
			.getSqlSessionTemplate().selectOne(
				GET_VALUESET_DEFINITION_METADATA_FROM_BASE_BY_REVISION_SQL,
				new PrefixedParameterTuple(getPrefix(), valueSetDefURI,
						revisionId));
		if (vsDefBean != null) {
			
			valueSetDefinition = vsDefBean.getValueSetDefinition();
			
			// Get value set definition source
			List<Source> sourceList = this
					.getSqlSessionTemplate()
					.selectList(
							GET_SOURCE_LIST_BY_PARENT_GUID_AND_TYPE_SQL,
							new PrefixedParameterTuple(prefix, vsDefBean
									.getUId(),
									ReferenceType.VALUESETDEFINITION.name()));

			if (sourceList != null)
				valueSetDefinition.setSource(sourceList);

			// Get value set definition context
			List<String> contextList = this
					.getSqlSessionTemplate()
					.selectList(
							GET_CONTEXT_LIST_BY_PARENT_GUID_AND_TYPE_SQL,
							new PrefixedParameterTuple(prefix, vsDefBean
									.getUId(),
									ReferenceType.VALUESETDEFINITION.name()));

			if (contextList != null)
				valueSetDefinition.setRepresentsRealmOrContext(contextList);
		}

		// 3. If thevalue set definition meta data in base is applied after the revision in question, lets get it from history
		if (vsDefBean == null)
		{
			vsDefBean = (InsertValueSetDefinitionBean) this
					.getSqlSessionTemplate().selectOne(
							GET_VALUESET_DEFINITION_METADATA_FROM_HISTORY_BY_REVISION_SQL,
							new PrefixedParameterTuple(getPrefix(), valueSetDefURI,
									revisionId));
		
			if (vsDefBean != null) {
				
				valueSetDefinition = vsDefBean.getValueSetDefinition();
				
				// Get value set definition source
				List<Source> sourceList = this
						.getSqlSessionTemplate()
						.selectList(
								GET_SOURCE_LIST_FROM_HISTORY_BY_PARENT_ENTRYSTATEGUID_AND_TYPE_SQL,
								new PrefixedParameterTuple(prefix, vsDefBean
										.getEntryStateUId(),
										ReferenceType.VALUESETDEFINITION.name()));
	
				if (sourceList != null)
					valueSetDefinition.setSource(sourceList);
	
				// Get value set definition context
				List<String> contextList = this
						.getSqlSessionTemplate()
						.selectList(
								GET_CONTEXT_LIST_FROM_HISTORY_BY_PARENT_ENTRYSTATEGUID_AND_TYPE_SQL,
								new PrefixedParameterTuple(prefix, vsDefBean
										.getEntryStateUId(),
										ReferenceType.VALUESETDEFINITION.name()));
	
				if (contextList != null)
					valueSetDefinition.setRepresentsRealmOrContext(contextList);
			}
		}
		
		// 4. Get all definition entry nodes.
		
		List<String> definitionEntryRuleOrderList = this.getSqlSessionTemplate().selectList(
				GET_DEFINITION_ENTRY_LIST_BY_VALUESET_DEFINITION_URI_SQL,
				new PrefixedParameter(prefix, valueSetDefURI));
			
		for (String ruleOrder : definitionEntryRuleOrderList) {
			DefinitionEntry definitionEntry = null;

			try {
				definitionEntry = vsDefinitionEntryDao
						.resolveDefinitionEntryByRevision(valueSetDefURI, ruleOrder,
								revisionId);
			} catch (LBRevisionException e) {
				continue;
			}
			
			if (definitionEntry != null)
				valueSetDefinition.addDefinitionEntry(definitionEntry);
		}
		
		// 5. Get all value set definition properties.
		
		List<String> propertyList = this.getSqlSessionTemplate().selectList(
				GET_VALUESET_DEF_PROPERTY_LIST_BY_VALUESET_DEFINITION_URI_SQL,
				new PrefixedParameter(prefix, valueSetDefURI));
		
		Properties properties = new Properties();
		
		for (String propId : propertyList) {
			Property prop = null;
			
			try {
				prop = vsPropertyDao.resolveVSPropertyByRevision(
						valueSetDefUId, propId, revisionId);
			} catch (LBRevisionException e) {
				continue;
			}
			if (prop != null)
				properties.addProperty(prop);
		}
		
		valueSetDefinition.setProperties(properties);
		
		return valueSetDefinition;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getValueSetURIsForContext(String contextURI) {
		return  this
		.getSqlSessionTemplate().selectList(
				GET_VS_URI_BY_CONTEXT,
			new PrefixedParameter(getPrefix(), contextURI));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, ValueSetDefinition> getValueSetURIMapToDefinitions() {
		long start = System.currentTimeMillis();
		
		List<ValueSetDefinitionMapHelper> guidMapHelper = 
				(List<ValueSetDefinitionMapHelper>)this 
				.getSqlSessionTemplate().<ValueSetDefinitionMapHelper>selectList(
						GET_MAP_OF_ALL_VSD_WITH_URI_KEY,
						new PrefixedParameter(getPrefix(), "VALUESET_DEFINITION"));
		
		guidMapHelper.parallelStream().forEach(
				x -> x.valueSetDefinition.setMappings(getMappings(x.valueSetDefinitionGuid)));
		
		Map<String, ValueSetDefinition> uriMap = 
				(Map<String, ValueSetDefinition>)guidMapHelper.
				stream().
				collect(
						Collectors.toMap(k -> k.getValueSetDefinition().getValueSetDefinitionURI(), 
								k -> k.getValueSetDefinition()));
		System.out.println("Execution time: " + (System.currentTimeMillis() - start));
		return  uriMap;
	}
	
}
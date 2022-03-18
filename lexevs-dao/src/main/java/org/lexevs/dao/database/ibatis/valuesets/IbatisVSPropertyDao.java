
package org.lexevs.dao.database.ibatis.valuesets;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.valuesets.VSEntryStateDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao;
import org.lexevs.dao.database.constants.DatabaseConstants;
import org.lexevs.dao.database.constants.classifier.property.PropertyMultiAttributeClassifier;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.property.parameter.InsertOrUpdatePropertyBean;
import org.lexevs.dao.database.ibatis.property.parameter.InsertPropertyMultiAttribBean;
import org.lexevs.dao.database.ibatis.valuesets.parameter.VSPropertyBean;
import org.lexevs.dao.database.ibatis.versions.IbatisVersionsDao;
import org.lexevs.dao.database.inserter.Inserter;
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
	
	public static String DELETE_PROP_MULTI_ATTRIB_BY_PROP_UID_AND_TYPE_SQL = PROPERTY_NAMESPACE + "deletePropertyMultiAttrib";
	
	public static String UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_PROP_ID_TYPE_SQL = PROPERTY_NAMESPACE + "updatePropertyMultiAttribEntryStateUId";
	
	public static String INSERT_PROPERTY_QUALIFIER_SQL = PROPERTY_NAMESPACE + "insertPropertyMultiAttrib";
	
	public static String INSERT_PROPERTY_SOURCE_SQL = PROPERTY_NAMESPACE + "insertPropertyMultiAttrib";
	
	public static String INSERT_PROPERTY_USAGECONTEXT_SQL = PROPERTY_NAMESPACE + "insertPropertyMultiAttrib";
	
	public static String INSERT_PROPERTY_MULTIATTRIB_SQL = PROPERTY_NAMESPACE + "insertPropertyMultiAttrib";
	
	public static String GET_ALL_PROPERTIES_OF_PARENT_SQL =PROPERTY_NAMESPACE +  "getPropertiesByParent";

	public static String GET_ALL_PROPERTIES_OF_PARENT_BY_REVISION_SQL = PROPERTY_NAMESPACE + "getPropertiesByParentAndRevisionId";
	
	public static String GET_PROPERTY_GUID_SQL = PROPERTY_NAMESPACE + "getPropertyGuid";
	
	public static String GET_PROPERTY_MULTIATTRIB_BY_PROPERTY_ID_SQL = PROPERTY_NAMESPACE + "getPropertyMultiAttribById";
	
	public static String UPDATE_PROPERTY_BY_UID_SQL = PROPERTY_NAMESPACE + "updatePropertyByUId";
	
	public static String UPDATE_PROPERTY_VER_ATTRIB_BY_UID_SQL = PROPERTY_NAMESPACE + "updatePropertyVerAttribByUId";
	
	public static String DELETE_PROPERTY_BY_UID_SQL = PROPERTY_NAMESPACE + "deletePropertyByUId";
	
	public static String GET_PROPERTY_ATTRIBUTES_BY_UID_SQL = PROPERTY_NAMESPACE + "getPropertyAttributesByUId";
	
	private static String DELETE_ALL_PICKLIST_ENTRYNODE_PROPERTIES_BY_PICKLISTENTRYUID = PROPERTY_NAMESPACE + "deletePickListEntryPropertiesByPlEntryGuid";	
	
	private static String GET_VSPROPERTY_LATEST_REVISION_ID_BY_UID = PROPERTY_NAMESPACE + "getVSPropertyLatestRevisionIdByUId";
	
//	private static String GET_PREV_REV_ID_FROM_GIVEN_REV_ID_FOR_VSPROPERTY_SQL = PROPERTY_NAMESPACE + "getPrevRevIdFromGivenRevIdForVSProperty";
	
	private static String GET_VSPROPERTY_FROM_HISTORY_BY_REVISION_SQL = PROPERTY_NAMESPACE + "getVSPropertyFromHistoryByRevision";
	
	private static String GET_VSPROPERTY_FROM_BASE_BY_REVISION_SQL = PROPERTY_NAMESPACE + "getVSPropertyFromBaseByRevision";
	
	private static String GET_VSPROPERTY_MULTIATTRIB_FROM_HISTORY_BY_ENTRYSTATEUID_SQL = PROPERTY_NAMESPACE + "getVSPropertyMultiAttribFromHistoryByEntryStateUId";
	
	PropertyMultiAttributeClassifier propertyMultiAttributeClassifier = new PropertyMultiAttributeClassifier();
	
	/** The ibatis versions dao. */
	private IbatisVersionsDao ibatisVersionsDao;
	
	private VSEntryStateDao vsEntryStateDao;

	public String insertProperty(String parentGuid, 
			ReferenceType type, 
			Property property) {
		String propertyGuid = this.createUniqueId();
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		return this.doInsertProperty(
				prefix,
				parentGuid, 
				propertyGuid, 
				type, 
				property, 
				this.getNonBatchTemplateInserter());	
	}
	
	public String insertHistoryProperty(String parentGuid, String propertyGuid, ReferenceType type, Property property) {
		
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		String histPrefix = this.getPrefixResolver().resolveHistoryPrefix();
		
		InsertOrUpdatePropertyBean propertyData = (InsertOrUpdatePropertyBean) this.getSqlMapClientTemplate().queryForObject(
				GET_PROPERTY_ATTRIBUTES_BY_UID_SQL,
				new PrefixedParameter(prefix, propertyGuid));
		
		propertyData.setPrefix(histPrefix);
		
		this.getNonBatchTemplateInserter().insert(INSERT_PROPERTY_SQL, propertyData);
		
		for (InsertPropertyMultiAttribBean propMultiAttrib : propertyData.getPropertyMultiAttribList())
		{
			if (StringUtils.isNotEmpty(propMultiAttrib.getUId()))
			{
				propMultiAttrib.setPrefix(histPrefix);
				
				this.getSqlMapClientTemplate().insert(INSERT_PROPERTY_MULTIATTRIB_SQL, propMultiAttrib);
			}
		}
		
		if (!vsEntryStateExists(prefix, propertyData.getEntryStateUId())) {

			EntryState entryState = new EntryState();

			entryState.setChangeType(ChangeType.NEW);
			entryState.setRelativeOrder(0L);

			vsEntryStateDao
					.insertEntryState(propertyData.getEntryStateUId(),
							propertyData.getUId(), ReferenceType.VSPROPERTY.name(), null,
							entryState);
		}
		
		return propertyData.getEntryStateUId();
	}
	
	@SuppressWarnings("unchecked")
	public List<Property> getAllPropertiesOfParent(String parentGuid, ReferenceType type) {
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		List<Property> propertyList = new ArrayList<Property>();
		List<VSPropertyBean> propertyBeanList = this.getSqlMapClientTemplate().queryForList(GET_ALL_PROPERTIES_OF_PARENT_SQL, 
				new PrefixedParameterTuple(
						prefix,
						type.name(),
						parentGuid));
		
		for (VSPropertyBean propertyBean : propertyBeanList)
		{
			Property prop = propertyBean.getProperty();
			
			List<Object> multiAttribs =  this.getSqlMapClientTemplate().queryForList(GET_PROPERTY_MULTIATTRIB_BY_PROPERTY_ID_SQL, 
					new PrefixedParameterTuple(
							prefix,
							propertyBean.getVsPropertyGuid(),
							null));
			
			for (Object multiAttrib : multiAttribs)
			{
				if (multiAttrib instanceof Source)
					prop.addSource((Source)multiAttrib);
				else if (multiAttrib instanceof PropertyQualifier)
					prop.addPropertyQualifier((PropertyQualifier)multiAttrib);
				else if (multiAttrib instanceof String)
					prop.addUsageContext((String)multiAttrib);
			}
			propertyList.add(prop);
		}
		
		return propertyList;
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
				new PrefixedParameterTuple(this.getPrefixResolver().resolveDefaultPrefix(), propertyGuid, this.propertyMultiAttributeClassifier.classify(multiAttrib)));
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
			String prefix,
			String parentGuid, 
			String propertyGuid,
			ReferenceType type, 
			Property property, 
			Inserter inserter) {
		
		String entryStateId = this.createUniqueId();
		
		if(StringUtils.isBlank(property.getPropertyType())){
			property.setPropertyType(
					getPropertyTypeString(property));
		}
		
		if (property.getPropertyId() == null
				|| property.getPropertyId().trim().equals("")) {
			property.setPropertyId(DatabaseConstants.GENERATED_ID_PREFIX  + this.createRandomIdentifier());
		}
		
		EntryState entryState = property.getEntryState();
		
		if (entryState != null)
		{
			this.vsEntryStateDao.insertEntryState(entryStateId, propertyGuid, 
					ReferenceType.VSPROPERTY.name(), null, entryState);
		}
		
		inserter.insert(INSERT_PROPERTY_SQL,
				buildInsertPropertyBean(
						prefix,
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


	public String updateProperty(String parentGuid,
			String propertyId, ReferenceType type, Property property) {
		Assert.hasText(
				property.getPropertyId(),
				"Property must have a populated PropertyId " +
				"in order to be updated.");
		
		String propertyGuid = this.getPropertyGuidFromParentGuidAndPropertyId(
				parentGuid, 
				property.getPropertyId());
		
		String entryStateUId = this.createUniqueId();
		
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		this.getSqlMapClientTemplate().update(
				UPDATE_PROPERTY_BY_UID_SQL, 
				this.buildInsertPropertyBean(
						prefix,
						parentGuid, 
						propertyGuid, 
						entryStateUId, 
						type, 
						property),
						1);	
		
		if (property.getSourceCount() != 0) {

			this.getSqlMapClientTemplate().delete(
					DELETE_PROP_MULTI_ATTRIB_BY_PROP_UID_AND_TYPE_SQL,
					new PrefixedParameterTuple(prefix, propertyGuid,
							SQLTableConstants.TBLCOLVAL_SOURCE));

			for (Source source : property.getSource()) {
				String propertySourceGuid = this.createUniqueId();
				this.doInsertPropertySource(propertyGuid, propertySourceGuid,
						entryStateUId, source, this
								.getNonBatchTemplateInserter());
			}

		} else {
			this.getSqlMapClientTemplate().update(
					UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_PROP_ID_TYPE_SQL,
					new PrefixedParameterTriple(prefix, propertyGuid,
							SQLTableConstants.TBLCOLVAL_SOURCE, entryStateUId));
			
		}

		if (property.getUsageContextCount() != 0) {

			this.getSqlMapClientTemplate().delete(
					DELETE_PROP_MULTI_ATTRIB_BY_PROP_UID_AND_TYPE_SQL,
					new PrefixedParameterTuple(prefix, propertyGuid,
							SQLTableConstants.TBLCOLVAL_USAGECONTEXT));

			for (String context : property.getUsageContext()) {
				String propertyUsageContextId = this.createUniqueId();
				this
						.doInsertPropertyUsageContext(propertyGuid,
								propertyUsageContextId, entryStateUId, context,
								this.getNonBatchTemplateInserter());
			}
		} else {
			this.getSqlMapClientTemplate().update(
					UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_PROP_ID_TYPE_SQL,
					new PrefixedParameterTriple(prefix, propertyGuid,
							SQLTableConstants.TBLCOLVAL_USAGECONTEXT, entryStateUId));
			
		}
		
		if (property.getPropertyQualifierCount() != 0) {

			this.getSqlMapClientTemplate().delete(
					DELETE_PROP_MULTI_ATTRIB_BY_PROP_UID_AND_TYPE_SQL,
					new PrefixedParameterTuple(prefix, propertyGuid,
							SQLTableConstants.TBLCOLVAL_QUALIFIER));

			for (PropertyQualifier qual : property.getPropertyQualifier()) {
				String propertyQualifierId = this.createUniqueId();
				this.doInsertPropertyQualifier(propertyGuid,
						propertyQualifierId, entryStateUId, qual, this
								.getNonBatchTemplateInserter());
			}
		} else {
			this.getSqlMapClientTemplate().update(
					UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_PROP_ID_TYPE_SQL,
					new PrefixedParameterTriple(prefix, propertyGuid,
							SQLTableConstants.TBLCOLVAL_QUALIFIER, entryStateUId));
			
		}
		
		return entryStateUId;
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
			final Inserter inserter) {

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
			final Inserter inserter) {
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
			final Inserter inserter) {
		
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
				new PrefixedParameterTuple(this.getPrefixResolver().resolveDefaultPrefix(), 
						ReferenceType.DEFINITIONENTRY.name(), valueSetDefinitionURI));
	}
	
	public void deleteAllValueSetDefinitionProperties(
			String valueSetDefinitionUID){
		// delete property entry states
		this.vsEntryStateDao.deleteAllEntryStatesOfVsPropertiesByParentUId(valueSetDefinitionUID, ReferenceType.VALUESETDEFINITION.name());
		
		this.getSqlMapClientTemplate().delete(DELETE_ALL_VALUESET_DEFINITION_PROPERTIES_OF_VALUESET_SQL, 
				new PrefixedParameterTuple(this.getPrefixResolver().resolveDefaultPrefix(), 
						ReferenceType.VALUESETDEFINITION.name(), valueSetDefinitionUID));
	}
	
	public void deleteAllPickListEntityPropertiesOfPickListDefinition(
			String pickListUID){
		// delete property entry states
		this.vsEntryStateDao.deleteAllEntryStatesOfVsPropertiesByParentUId(pickListUID, ReferenceType.PICKLISTENTRY.name());
		
		this.getSqlMapClientTemplate().delete(DELETE_ALL_PICKLIST_ENTRY_PROPERTIES_OF_PCIKLIST_SQL, 
				new PrefixedParameterTuple(this.getPrefixResolver().resolveDefaultPrefix(), 
						ReferenceType.PICKLISTENTRY.name(), pickListUID));
	}
	
	public void deleteAllPickListDefinitionProperties(
			String pickListId){
		this.getSqlMapClientTemplate().delete(DELETE_ALL_PICKLIST_DEFINITION_PROPERTIES_OF_PCIKLIST_SQL, 
				new PrefixedParameterTuple(this.getPrefixResolver().resolveDefaultPrefix(), 
						ReferenceType.PICKLISTDEFINITION.name(), pickListId));
	}
	
	@Override
	public void deleteAllPickListEntryNodeProperties(String pickListEntryNodeUId) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();

		/* 1. Remove entrystate details of all properties of pickListEntryNode. */
		this.vsEntryStateDao.deleteAllEntryStatesOfVsPropertiesByParentUId(
				pickListEntryNodeUId, ReferenceType.PICKLISTENTRY.name());

		/* 2. Remove all pick list entry node properties */
		this.getSqlMapClientTemplate().delete(
				DELETE_ALL_PICKLIST_ENTRYNODE_PROPERTIES_BY_PICKLISTENTRYUID,
				new PrefixedParameterTuple(prefix,
						ReferenceType.PICKLISTENTRY.name(),
						pickListEntryNodeUId));
	}

	/**
	 * Gets the propertyGuid from parentGuid and prop id.
	 * 
	 * @param parentGuid the parent GUID
	 * @param propertyId the propertyID
	 * 
	 * @return the propertyGUID from parentGuid and propertyID
	 */
	@Override
	public String getPropertyGuidFromParentGuidAndPropertyId(String parentGuid, String propertyId) {
		
		return (String) this.getSqlMapClientTemplate().queryForObject(GET_PROPERTY_GUID_SQL, 
				new PrefixedParameterTuple(this.getPrefixResolver().resolveDefaultPrefix(), parentGuid, propertyId));
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
	protected InsertOrUpdatePropertyBean buildInsertPropertyBean(String prefix,
			String parentGuid, String propertyGuid, String entryStateGuid,
			ReferenceType type, Property property) {
		
		InsertOrUpdatePropertyBean bean = new InsertOrUpdatePropertyBean();
		bean.setPrefix(prefix);
		bean.setParentType(type.name());
		bean.setParentUId(parentGuid);
		bean.setUId(propertyGuid);
		bean.setEntryStateUId(entryStateGuid);
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
		bean.setUId(qualifierGuid);
		bean.setPropertyUId(propertyGuid);
		bean.setAttributeId(propertyQualifier.getPropertyQualifierName());
		if (propertyQualifier.getValue() != null)
		{
			bean.setAttributeValue(propertyQualifier.getValue().getContent());
		}
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_QUALIFIER);
		bean.setQualifierType(propertyQualifier.getPropertyQualifierType());
		bean.setEntryStateUId(entryStateGuid);
		bean.setPrefix(this.getPrefixResolver().resolveDefaultPrefix());

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
		bean.setUId(qualifierGuid);
		bean.setPropertyUId(propertyGuid);
		bean.setAttributeId(SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
		bean.setAttributeValue(usageContext);
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
		bean.setEntryStateUId(entryStateGuid);
		bean.setPrefix(this.getPrefixResolver().resolveDefaultPrefix());
		
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
		bean.setUId(sourceId);
		bean.setPropertyUId(propertyGuid);
		bean.setAttributeId(SQLTableConstants.TBLCOLVAL_SOURCE);
		bean.setAttributeValue(source.getContent());
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_SOURCE);
		bean.setRole(source.getRole());
		bean.setSubRef(source.getSubRef());
		bean.setEntryStateUId(entryStateId);
		bean.setPrefix(this.getPrefixResolver().resolveDefaultPrefix());
		
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
	public void deletePropertyByUId(String propertyUId) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		this.vsEntryStateDao.deleteAllEntryStateByEntryUIdAndType(propertyUId, ReferenceType.VSPROPERTY.name());
		
		this.getSqlMapClientTemplate().delete(DELETE_PROPERTY_BY_UID_SQL, 
				new PrefixedParameter(prefix, propertyUId));
	}

	@Override
	public String updateVersionableAttributes(String parentUId,
			String propertyUId, ReferenceType type,
			Property property) {

		Assert.hasText(
				property.getPropertyId(),
				"Property must have a populated PropertyId " +
				"in order to be updated.");
		
		String entryStateUId = this.createUniqueId();
		
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		this.getSqlMapClientTemplate().update(
				UPDATE_PROPERTY_VER_ATTRIB_BY_UID_SQL, 
				this.buildInsertPropertyBean(
						prefix,
						parentUId, 
						propertyUId, 
						entryStateUId, 
						type, 
						property),
						1);	
		
		this.getSqlMapClientTemplate().update(
				UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_PROP_ID_TYPE_SQL,
				new PrefixedParameterTriple(prefix, propertyUId,
						SQLTableConstants.TBLCOLVAL_SOURCE, entryStateUId));
		
		this.getSqlMapClientTemplate().update(
				UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_PROP_ID_TYPE_SQL,
				new PrefixedParameterTriple(prefix, propertyUId,
						SQLTableConstants.TBLCOLVAL_USAGECONTEXT, entryStateUId));
		
		this.getSqlMapClientTemplate().update(
				UPDATE_MULTI_ATTRIB_ENTRYSTATE_UID_BY_PROP_ID_TYPE_SQL,
				new PrefixedParameterTriple(prefix, propertyUId,
						SQLTableConstants.TBLCOLVAL_QUALIFIER, entryStateUId));
		
		return entryStateUId;
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
	public String getLatestRevision(String propertyUId) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		String revId = (String) this.getSqlMapClientTemplate().queryForObject(
				GET_VSPROPERTY_LATEST_REVISION_ID_BY_UID, 
				new PrefixedParameter(prefix, propertyUId));
		
		return revId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Property resolveVSPropertyByRevision(String parentGuid,
			String propertyId, String revisionId) throws LBRevisionException {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();

		String vsPropertyUId = this.getPropertyGuidFromParentGuidAndPropertyId(
				parentGuid, propertyId);

		if (vsPropertyUId == null) {
			throw new LBRevisionException(
					"Property "
							+ vsPropertyUId
							+ " doesn't exist in lexEVS. "
							+ "Please check the propertyId and its parent id. Its possible that the given property "
							+ "has been REMOVEd from the lexEVS system in the past.");
		}

		String vsPropertyRevisionId = this.getLatestRevision(vsPropertyUId);

		// 1. If 'revisionId' is null or 'revisionId' is the latest revision of
		// the vsProperty
		// then use getPropertyByUId to get the Property object and return.

		if (StringUtils.isEmpty(revisionId) || StringUtils.isEmpty(vsPropertyRevisionId)) {
			return getPropertyByUId(vsPropertyUId);
		}

		Property property = null;
		InsertOrUpdatePropertyBean propertyBean = null;

		// 2. Check if the vsd property in base table is latest compared to the input revisionId
		// if we get it in the base, we can just return it. Else will have to get it from history
		
		propertyBean = (InsertOrUpdatePropertyBean) this
							.getSqlMapClientTemplate().queryForObject(
									GET_VSPROPERTY_FROM_BASE_BY_REVISION_SQL,
									new PrefixedParameterTuple(prefix, vsPropertyUId,
											revisionId));
		// if found in base, populate multi attributes
		if (propertyBean != null) {

			List multiAttribList =  this.getSqlMapClientTemplate().queryForList(GET_PROPERTY_MULTIATTRIB_BY_PROPERTY_ID_SQL, 
					new PrefixedParameterTuple(
							prefix,
							propertyBean.getUId(),
							null));
			
			propertyBean.setPropertyMultiAttribList(multiAttribList);
			
			property = getProperty(propertyBean);
		}
		
		// 3. If the vsd property in base is applied after the revision in question, lets get it from history
		if (property == null)
		{
			propertyBean = (InsertOrUpdatePropertyBean) this
					.getSqlMapClientTemplate().queryForObject(
							GET_VSPROPERTY_FROM_HISTORY_BY_REVISION_SQL,
							new PrefixedParameterTuple(prefix, vsPropertyUId,
									revisionId));
	
			if (propertyBean != null) {
	
				List multiAttribList = this.getSqlMapClientTemplate().queryForList(
						GET_VSPROPERTY_MULTIATTRIB_FROM_HISTORY_BY_ENTRYSTATEUID_SQL,
						new PrefixedParameter(prefix, propertyBean.getEntryStateUId()));
	
				propertyBean.setPropertyMultiAttribList(multiAttribList);
				
				property = getProperty(propertyBean);
			}
		}

		return property;
	}

	public Property getPropertyByUId(String vsPropertyUId) {
		
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		InsertOrUpdatePropertyBean propertyBean = (InsertOrUpdatePropertyBean) this
				.getSqlMapClientTemplate().queryForObject(
						GET_PROPERTY_ATTRIBUTES_BY_UID_SQL,
						new PrefixedParameter(prefix, vsPropertyUId));
		
		return getProperty(propertyBean);
	}
	
	private Property getProperty(InsertOrUpdatePropertyBean propertyBean) {
		
		Property property = propertyBean.getProperty();
		
		if (propertyBean.getPropertyMultiAttribList() != null)
		{
			List<InsertPropertyMultiAttribBean> multiAttribList = propertyBean.getPropertyMultiAttribList();
			
			for (InsertPropertyMultiAttribBean multiAttribBean : multiAttribList) {
	
				if (SQLTableConstants.TBLCOLVAL_SOURCE.equals(multiAttribBean
						.getAttributeType())) {
					Source source = new Source();
	
					source.setRole(multiAttribBean.getRole());
					source.setSubRef(multiAttribBean.getSubRef());
					source.setContent(multiAttribBean.getAttributeValue());
	
					property.addSource(source);
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
					property.addPropertyQualifier(qualifier);
				} else if (SQLTableConstants.TBLCOLVAL_USAGECONTEXT
						.equals(multiAttribBean.getAttributeType())) {
					
					property.addUsageContext(multiAttribBean.getAttributeValue());
				}
			}
		}
		return property;
	}
}
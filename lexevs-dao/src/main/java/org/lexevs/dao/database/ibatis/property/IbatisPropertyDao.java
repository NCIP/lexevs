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
import org.lexevs.dao.database.constants.classifier.property.PropertyTypeClassifier;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.batch.IbatisBatchInserter;
import org.lexevs.dao.database.ibatis.batch.IbatisInserter;
import org.lexevs.dao.database.ibatis.batch.SqlMapExecutorBatchInserter;
import org.lexevs.dao.database.ibatis.property.parameter.InsertPropertyBean;
import org.lexevs.dao.database.ibatis.property.parameter.InsertPropertyLinkBean;
import org.lexevs.dao.database.ibatis.property.parameter.InsertPropertyMultiAttribBean;
import org.lexevs.dao.database.ibatis.versions.IbatisVersionsDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.batch.classify.Classifier;
import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;

public class IbatisPropertyDao extends AbstractIbatisDao implements PropertyDao {
	
	private Classifier<PropertyType,String> propertyTypeClassifier = new PropertyTypeClassifier();
	
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	public static String INSERT_PROPERTY_SQL = "insertProperty";
	public static String INSERT_PROPERTY_QUALIFIER_SQL = "insertPropertyMultiAttrib";
	public static String INSERT_PROPERTY_SOURCE_SQL = "insertPropertyMultiAttrib";
	public static String INSERT_PROPERTY_USAGECONTEXT_SQL = "insertPropertyMultiAttrib";
	public static String INSERT_PROPERTYLINK_SQL = "insertPropertyLink";
	
	private IbatisVersionsDao ibatisVersionsDao;

	public void insertBatchProperties(final String codingSchemeId, final PropertyType type,
			final List<PropertyBatchInsertItem> batch) {
		
		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){

			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {
				IbatisBatchInserter inserter = new SqlMapExecutorBatchInserter(executor);
				
				inserter.startBatch();
				
				for(PropertyBatchInsertItem item : batch){
					insertProperty(codingSchemeId,
							item.getParentId(),
							type,
							item.getProperty(),  inserter);
				}
				
				inserter.executeBatch();
				
				return null; 
			}	
		});
	}
	
	public void insertBatchProperties(final String codingSchemeId, final PropertyType type,
			final List<PropertyBatchInsertItem> batch, IbatisBatchInserter inserter) {
		
		for(PropertyBatchInsertItem item : batch){
			this.insertProperty(codingSchemeId, item.getParentId(), type, item.getProperty(), inserter);
		}
	}

	public String insertProperty(String codingSchemeId,
			String entityCodeId, PropertyType type, Property property) {
		return this.insertProperty(
				codingSchemeId, entityCodeId, type, property, this.getNonBatchTemplateInserter());	
	}
	
	public String insertProperty(String codingSchemeId,
			String entityCodeId, PropertyType type, Property property, IbatisInserter inserter) {
		String propertyId = this.createUniqueId();
		String entryStateId = this.createUniqueId();
		
		this.ibatisVersionsDao.insertEntryState(
				this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId),
				entryStateId, propertyId, "Property", null, property.getEntryState(), inserter);
		
		inserter.insert(INSERT_PROPERTY_SQL,
				buildInsertPropertyBean(
						this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId),
						entityCodeId,
						propertyId,
						entryStateId,
						type,
						property
						));
		
		return propertyId;
		
	}

	public String insertProperty(String codingSchemeUri, String version,
			String parentId, PropertyType type, Property property) {
		String propertyId = this.createUniqueId();
		String entryStateId = this.createUniqueId();
		
		this.getSqlMapClientTemplate().insert(INSERT_PROPERTY_SQL,
				buildInsertPropertyBean(
						this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUri, version),
						parentId,
						propertyId,
						entryStateId,
						type,
						property
						));
		
		return propertyId;
	}
	


	public void updateProperty(String codingSchemeName, String parentId,
			String propertyId, PropertyType type, Property property) {
		// TODO Auto-generated method stub
		
	}
	
	public void insertPropertyQualifier(String codingSchemeUri,
			String version, String propertyId, PropertyQualifier propertyQualifier) {
		String qualifierId = this.createUniqueId();
		
		this.getSqlMapClientTemplate().insert(INSERT_PROPERTY_QUALIFIER_SQL, 
				this.buildInsertPropertyQualifierBean(
						this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUri, version),
						propertyId, qualifierId, propertyQualifier));
	}
	

	public void insertPropertySource(String codingSchemeUri,
			String version, String propertyId, Source source) {
		String sourceId = this.createUniqueId();
		
		this.getSqlMapClientTemplate().insert(INSERT_PROPERTY_SOURCE_SQL, 
				this.buildInsertPropertySourceBean(
						this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUri, version),
						propertyId, sourceId, source));
	}
	
	public void insertPropertyUsageContext(String codingSchemeUri,
			String version, String propertyId, String usageContext) {
		String sourceId = this.createUniqueId();
		
		this.getSqlMapClientTemplate().insert(INSERT_PROPERTY_USAGECONTEXT_SQL, 
				this.buildInsertPropertyUsageContextBean(
						this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUri, version),
						propertyId, sourceId, usageContext));
	}
	
	public void insertPropertyLink(String codingSchemeId, String entityId,
			PropertyLink propertyLink) {
		String propertyLinkId = this.createUniqueId();
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		this.getSqlMapClientTemplate().insert(INSERT_PROPERTYLINK_SQL, 
				this.buildInsertPropertyLinkBean(prefix, 
						entityId, 
						propertyLinkId, 
						propertyLink
						));
	}

	
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
	
	protected InsertPropertyMultiAttribBean buildInsertPropertyQualifierBean(
			String prefix,
			String propertyId, 
			String qualifierId, 
			PropertyQualifier propertyQualifier){
		InsertPropertyMultiAttribBean bean = new InsertPropertyMultiAttribBean();
		bean.setPrefix(prefix);
		bean.setId(qualifierId);
		bean.setPropertyId(propertyId);
		bean.setAttributeId(propertyQualifier.getPropertyQualifierName());
		bean.setAttributeValue(propertyQualifier.getValue().getContent());
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_QUALIFIER);

		return bean;
	}
	
	protected InsertPropertyLinkBean buildInsertPropertyLinkBean(String prefix, String entityId, String propertyLinkId,
			PropertyLink propertyLink){
		InsertPropertyLinkBean bean = new InsertPropertyLinkBean();
		bean.setId(propertyLinkId);
		bean.setEntityId(entityId);
		bean.setPrefix(prefix);
		bean.setPropertyLink(propertyLink);

		return bean;
	}
	
	protected InsertPropertyMultiAttribBean buildInsertPropertyUsageContextBean(String prefix, String propertyId, String qualifierId, String usageContext){
		InsertPropertyMultiAttribBean bean = new InsertPropertyMultiAttribBean();
		bean.setPrefix(prefix);
		bean.setId(qualifierId);
		bean.setPropertyId(propertyId);
		bean.setAttributeId(SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
		bean.setAttributeValue(usageContext);
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_USAGECONTEXT);

		return bean;
	}
	
	protected InsertPropertyMultiAttribBean buildInsertPropertySourceBean(String prefix, String propertyId, String sourceId, Source source){
		InsertPropertyMultiAttribBean bean = new InsertPropertyMultiAttribBean();
		bean.setPrefix(prefix);
		bean.setId(sourceId);
		bean.setPropertyId(propertyId);
		bean.setAttributeId(SQLTableConstants.TBLCOLVAL_SOURCE);
		bean.setAttributeValue(source.getContent());
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_SOURCE);
		bean.setRole(source.getRole());
		bean.setSubRef(source.getSubRef());

		return bean;
	}

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(supportedDatebaseVersion, LexGridSchemaVersion.class);
	}

	public void setPropertyTypeClassifier(Classifier<PropertyType,String> propertyTypeClassifier) {
		this.propertyTypeClassifier = propertyTypeClassifier;
	}

	public Classifier<PropertyType,String> getPropertyTypeClassifier() {
		return propertyTypeClassifier;
	}


	public IbatisVersionsDao getIbatisVersionsDao() {
		return ibatisVersionsDao;
	}


	public void setIbatisVersionsDao(IbatisVersionsDao ibatisVersionsDao) {
		this.ibatisVersionsDao = ibatisVersionsDao;
	}
}

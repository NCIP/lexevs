package org.lexevs.dao.database.ibatis.codingscheme;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.URIMap;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.constants.classifier.mapping.ClassToStringMappingClassifier;
import org.lexevs.dao.database.constants.classifier.mapping.StringToClassMappingClassifier;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.codingscheme.parameter.InsertCodingSchemeMultiAttribBean;
import org.lexevs.dao.database.ibatis.codingscheme.parameter.InsertOrUpdateCodingSchemeBean;
import org.lexevs.dao.database.ibatis.codingscheme.parameter.InsertURIMapBean;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;

@Cacheable(cacheName = "IbatisCodingSchemeDao")
public class IbatisCodingSchemeDao extends AbstractIbatisDao implements CodingSchemeDao {
	
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");

	private static String SUPPORTED_ATTRIB_GETTER_PREFIX = "_supported";
	
	public static String CODING_SCHEME_NAMESPACE = "CodingScheme.";
	private static String REMOVE_CODING_SCHEME_BY_ID_SQL = CODING_SCHEME_NAMESPACE + "deleteCodingSchemeById";
	private static String INSERT_CODING_SCHEME_SQL = CODING_SCHEME_NAMESPACE + "insertCodingScheme";
	private static String GET_CODING_SCHEME_BY_ID_SQL = CODING_SCHEME_NAMESPACE + "getCodingSchemeById";
	private static String GET_CODING_SCHEME_SUMMARY_BY_URI_AND_VERSION_SQL = CODING_SCHEME_NAMESPACE + "getCodingSchemeSummaryByUriAndVersion";
	private static String GET_CODING_SCHEME_ID_BY_NAME_AND_VERSION_SQL = CODING_SCHEME_NAMESPACE + "getCodingSchemeIdByNameAndVersion";
	private static String GET_CODING_SCHEME_ID_BY_URI_AND_VERSION_SQL = CODING_SCHEME_NAMESPACE + "getCodingSchemeIdByUriAndVersion";
	private static String GET_CODING_SCHEME_SOURCE_LIST_SQL = CODING_SCHEME_NAMESPACE + "getSourceListByCodingSchemeId";
	private static String GET_CODING_SCHEME_LOCALNAME_LIST_SQL = CODING_SCHEME_NAMESPACE + "getLocalNameListByCodingSchemeId";
	private static String UPDATE_CODING_SCHEME_BY_ID_SQL = CODING_SCHEME_NAMESPACE + "updateCodingSchemeById";
	private static String INSERT_CODING_SCHEME_MULTIATTRIB_SQL = CODING_SCHEME_NAMESPACE + "insertCodingSchemeMultiAttrib";
	private static String INSERT_URIMAP_SQL = CODING_SCHEME_NAMESPACE + "insertURIMap";
	private static String GET_DISTINCT_PROPERTY_NAMES_OF_CS_SQL = CODING_SCHEME_NAMESPACE + "getDistinctPropertyNames";
	private static String GET_DISTINCT_ENTITY_TYPES_OF_CS_SQL = CODING_SCHEME_NAMESPACE + "getDistinctEntityTypes";
	private static String GET_DISTINCT_PROPERTY_QUALIFIER_NAMES_OF_CS_SQL = CODING_SCHEME_NAMESPACE + "getDistinctPropertyQualifierNames";
	private static String GET_DISTINCT_PROPERTY_QUALIFIER_TYPES_OF_CS_SQL = CODING_SCHEME_NAMESPACE + "getDistinctPropertyQualifierTypes";
	private static String GET_DISTINCT_FORMATS_OF_CS_SQL = CODING_SCHEME_NAMESPACE + "getDistinctFormats";
	private static String GET_DISTINCT_NAMESPACES_OF_CS_SQL = CODING_SCHEME_NAMESPACE + "getDistinctNamespaces";
	private static String GET_DISTINCT_LANGUAGES_OF_CS_SQL = CODING_SCHEME_NAMESPACE + "getDistinctLanguages";
	private static String GET_URIMAPS_SQL = CODING_SCHEME_NAMESPACE + "getURIMaps";
	private static String GET_URIMAP_BY_LOCALNAME_AND_TYPE_SQL = CODING_SCHEME_NAMESPACE + "getURIMapByLocalNameAndType";
	private static String GET_URIMAP_COUNT_BY_LOCALNAME_AND_TYPE_SQL = CODING_SCHEME_NAMESPACE + "getURIMapCountByLocalNameAndType";
	
	private ClassToStringMappingClassifier classToStringMappingClassifier = new ClassToStringMappingClassifier();
	private StringToClassMappingClassifier stringToClassMappingClassifier = new StringToClassMappingClassifier();
	
	private VersionsDao versionsDao;
	private EntityDao entityDao;

	@CacheMethod
	public CodingScheme getCodingSchemeById(String codingSchemeId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		CodingScheme scheme = 
			(CodingScheme) this.getSqlMapClientTemplate().queryForObject(
				GET_CODING_SCHEME_BY_ID_SQL, new PrefixedParameter(prefix, codingSchemeId));

		scheme.setMappings(
				this.getMappings(codingSchemeId));
		
		return scheme;
	}
	
	public CodingScheme getCodingSchemeByUriAndVersion(String codingSchemeUri,
			String version) {
		String codingSchemeId = this.getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
		return this.getCodingSchemeById(codingSchemeId);
	}


	@SuppressWarnings("unchecked")
	public CodingScheme getCodingSchemeByNameAndVersion(String codingSchemeName, String representsVersion){
		String codingSchemeId = this.getCodingSchemeIdByNameAndVersion(codingSchemeName, representsVersion);
		return this.getCodingSchemeById(codingSchemeId);
	}

	public void deleteLocalName(String codingSchemeName, String version,
			String localName) {
		throw new UnsupportedOperationException();	
	}

	public void deleteSource(String codingSchemeName, String version,
			Source source) {
		throw new UnsupportedOperationException();	
	}

	public CodingScheme getCodingSchemeByRevision(String codingSchemeName,
			String version, String revisionId) {
		throw new UnsupportedOperationException();
	}

	public CodingSchemeSummary getCodingSchemeSummaryByUriAndVersion(
			String codingSchemeUri, String version) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUri, version);
		return (CodingSchemeSummary)
			this.getSqlMapClientTemplate().queryForObject(GET_CODING_SCHEME_SUMMARY_BY_URI_AND_VERSION_SQL, 
				new PrefixedParameterTuple(prefix, codingSchemeUri, version));
	}


	public void insertHistoryCodingScheme(String codingSchemeId, CodingScheme codingScheme) {
		String prefix = this.getPrefixResolver().resolveHistoryPrefix();
		this.doInsertCodingScheme(codingSchemeId, prefix, codingScheme);
	}

	public void deleteCodingSchemeById(String codingSchemeId) {
		String prefix = 
			this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		this.getSqlMapClientTemplate().
			delete(REMOVE_CODING_SCHEME_BY_ID_SQL, new PrefixedParameter(prefix, codingSchemeId));	
	}
	
	public String insertCodingScheme(CodingScheme codingScheme) {
		String codingSchemeId = this.createUniqueId();
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return this.doInsertCodingScheme(codingSchemeId, prefix, codingScheme);
	}
	
	public String doInsertCodingScheme(String codingSchemeId, String prefix, CodingScheme codingScheme) {
		String entryStateId = this.createUniqueId();
		
		this.getSqlMapClientTemplate().insert(INSERT_CODING_SCHEME_SQL, 
				this.buildInsertCodingSchemeBean(
						prefix,
						codingSchemeId, entryStateId, codingScheme));	
		
		String previousRevisionId = null;
		if(codingScheme.getEntryState() != null && 
				codingScheme.getEntryState().getPrevRevision() != null &&
				!codingScheme.getEntryState().getPrevRevision().isEmpty()) {
			previousRevisionId = codingScheme.getEntryState().getPrevRevision();
		}
		
		versionsDao.insertEntryState(
				codingSchemeId,
				entryStateId, codingSchemeId, "CodingScheme", previousRevisionId, codingScheme.getEntryState());
		
		for(Source source : codingScheme.getSource()){
			this.insertCodingSchemeSource(codingSchemeId, source);
		}
		
		for(String localName : codingScheme.getLocalName()){
			this.insertCodingSchemeLocalName(codingSchemeId, localName);
		}
		
		this.insertMappings(codingSchemeId, codingScheme.getMappings());
		
		return codingSchemeId;
	}
	
	public void updateCodingScheme(String codingSchemeId, CodingScheme codingScheme) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		InsertOrUpdateCodingSchemeBean bean = new InsertOrUpdateCodingSchemeBean();
		bean.setPrefix(prefix);
		bean.setCodingScheme(codingScheme);
		bean.setId(codingSchemeId);
		
		this.getSqlMapClientTemplate().update(UPDATE_CODING_SCHEME_BY_ID_SQL, bean);
	}

	public void updateCodingScheme(String codingSchemeName, String version, CodingScheme codingScheme) {
		throw new UnsupportedOperationException();
	}

	public String getCodingSchemeIdByNameAndVersion(String codingSchemeName, String version) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeName, version);
		return (String) this.getSqlMapClientTemplate().queryForObject(GET_CODING_SCHEME_ID_BY_NAME_AND_VERSION_SQL,
				new PrefixedParameterTuple(prefix, codingSchemeName, version));
	}
	
	public String getCodingSchemeIdByUriAndVersion(String codingSchemeUri, String version) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUri, version);
		return (String) this.getSqlMapClientTemplate().queryForObject(GET_CODING_SCHEME_ID_BY_URI_AND_VERSION_SQL,
				new PrefixedParameterTuple(prefix, codingSchemeUri, version));
	}

	public String getEntryStateId(String codingSchemeName, String version) {
		throw new UnsupportedOperationException();
	}
	
	public void insertCodingSchemeSource(String codingSchemeId, Source source) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		String sourceId = this.createUniqueId();
		this.getSqlMapClientTemplate().insert(INSERT_CODING_SCHEME_MULTIATTRIB_SQL,
				this.buildInsertSourceBean(prefix, sourceId, codingSchemeId, source));
	}
	

	public void insertCodingSchemeLocalName(String codingSchemeId,
			String localName) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		String sourceId = this.createUniqueId();
		this.getSqlMapClientTemplate().insert(INSERT_CODING_SCHEME_MULTIATTRIB_SQL,
				this.buildInsertLocalNameBean(prefix, sourceId, codingSchemeId, localName));
	}
	
	public void insertURIMap(String codingSchemeName,
			String codingSchemeVersion, URIMap uriMap) {
		String codingSchemeId = this.getCodingSchemeIdByNameAndVersion(codingSchemeName, codingSchemeVersion);
		this.insertURIMap(codingSchemeId, uriMap);
	}

	public void insertURIMap(String codingSchemeId, URIMap uriMap) {
		String uriMapId = this.createUniqueId();
		this.getSqlMapClientTemplate().insert(
				INSERT_URIMAP_SQL, buildInsertURIMapBean(
									this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId),
									uriMapId, 
									codingSchemeId,
									classToStringMappingClassifier.classify(uriMap.getClass()),
									uriMap));
	}
	
	public void insertURIMap(final String codingSchemeId,
			final List<URIMap> supportedProperties) {
		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){
	
			public Object doInSqlMapClient(SqlMapExecutor executor)
			throws SQLException {
				executor.startBatch();
				for(URIMap uriMap : supportedProperties){
					String uriMapId = UUID.randomUUID().toString();
					
					executor.insert(INSERT_URIMAP_SQL, 
							buildInsertURIMapBean(
									getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId),
									uriMapId, 
									codingSchemeId,
									classToStringMappingClassifier.classify(uriMap.getClass()),
									uriMap));
				}
				return executor.executeBatch();
			}	
		});		
	}
	
	public void insertMappings(String codingSchemeName,
			String codingSchemeVersion, Mappings mappings) {
		String codingSchemeId = this.getCodingSchemeIdByNameAndVersion(codingSchemeName, codingSchemeVersion);
		this.insertMappings(codingSchemeId, mappings);
	}
	
	@SuppressWarnings("unchecked")
	public void insertMappings(String codingSchemeId, Mappings mappings){
		if(mappings == null){
			return;
		}
		for(Field field : mappings.getClass().getDeclaredFields()){
			if(field.getName().startsWith(SUPPORTED_ATTRIB_GETTER_PREFIX)){
				field.setAccessible(true);
				try {
					List<URIMap> urimapList = (List<URIMap>) field.get(mappings);
					this.insertURIMap(codingSchemeId, urimapList);
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 
			}
		}
	}
	
	@Override
	public List<String> getDistinctFormatsOfCodingScheme(String codingSchemeId) {
		return doDistinctQuery(
				GET_DISTINCT_FORMATS_OF_CS_SQL, codingSchemeId);
	}

	@Override
	public List<String> getDistinctLanguagesOfCodingScheme(String codingSchemeId) {
		return doDistinctQuery(
				GET_DISTINCT_LANGUAGES_OF_CS_SQL, codingSchemeId);
	}

	@Override
	public List<String> getDistinctNamespacesOfCodingScheme(
			String codingSchemeId) {
		return doDistinctQuery(
				GET_DISTINCT_NAMESPACES_OF_CS_SQL, codingSchemeId);
	}

	public List<String> getDistinctPropertyNamesOfCodingScheme(
			String codingSchemeId) {
		return doDistinctQuery(
				GET_DISTINCT_PROPERTY_NAMES_OF_CS_SQL, codingSchemeId);
	}
	
	@SuppressWarnings("unchecked")
	protected List<String> doDistinctQuery(String queryName, String codingSchemeId){
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return this.getSqlMapClientTemplate().queryForList(
				queryName, 
				new PrefixedParameter(prefix, codingSchemeId));
	}

	@Override
	public List<String> getDistinctPropertyQualifierNamesOfCodingScheme(
			String codingSchemeId) {
		return doDistinctQuery(
				GET_DISTINCT_PROPERTY_QUALIFIER_NAMES_OF_CS_SQL, codingSchemeId);
	}

	@Override
	public List<String> getDistinctPropertyQualifierTypesOfCodingScheme(
			String codingSchemeId) {
		return doDistinctQuery(
				GET_DISTINCT_PROPERTY_QUALIFIER_TYPES_OF_CS_SQL, codingSchemeId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getDistinctEntityTypesOfCodingScheme(
			String codingSchemeId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return this.getSqlMapClientTemplate().queryForList(
				GET_DISTINCT_ENTITY_TYPES_OF_CS_SQL, 
				new PrefixedParameter(prefix, codingSchemeId));
	}

	

	@SuppressWarnings("unchecked")
	@Override
	public Mappings getMappings(String codingSchemeId) {
		Mappings mappings = new Mappings();
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		List<URIMap> uriMaps = this.getSqlMapClientTemplate().queryForList(	
				GET_URIMAPS_SQL, 
				new PrefixedParameter(prefix, codingSchemeId));
		
		for(URIMap uriMap : uriMaps) {
			DaoUtility.insertIntoMappings(mappings, uriMap);
		}
		
		return mappings;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends URIMap> T getUriMap(String codingSchemeId, String localId, Class<T> uriMap) {	
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return (T) this.getSqlMapClientTemplate().queryForList(	
				GET_URIMAP_BY_LOCALNAME_AND_TYPE_SQL, 
				new PrefixedParameterTriple(prefix, codingSchemeId, localId, this.classToStringMappingClassifier.classify(uriMap)));
	}
	
	@SuppressWarnings("unchecked")
	public <T extends URIMap> boolean validateSupportedAttribute(String codingSchemeId, String localId, Class<T> uriMap) {	
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		String classifiedName = this.classToStringMappingClassifier.classify(uriMap);
		int count = (Integer) this.getSqlMapClientTemplate().queryForObject(	
				GET_URIMAP_COUNT_BY_LOCALNAME_AND_TYPE_SQL, 
				new PrefixedParameterTriple(prefix, codingSchemeId, localId, classifiedName));
		
		return count > 0;
	}
	
	protected InsertURIMapBean buildInsertURIMapBean(String prefix, String uriMapId, String codingSchemeId, String supportedAttributeTag, URIMap uriMap){
		InsertURIMapBean bean = new InsertURIMapBean();
		bean.setPrefix(prefix);
		bean.setSupportedAttributeTag(supportedAttributeTag);
		bean.setCodingSchemeId(codingSchemeId);
		bean.setUriMap(uriMap);
		bean.setId(uriMapId);
		
		return bean;
	}
	
	protected InsertCodingSchemeMultiAttribBean buildInsertSourceBean(String prefix, String sourceId, String codingSchemeId, Source source){
		InsertCodingSchemeMultiAttribBean bean = new InsertCodingSchemeMultiAttribBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeId(codingSchemeId);
		bean.setId(sourceId);
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_SOURCE);
		bean.setAttributeValue(source.getContent());
		bean.setSubRef(source.getSubRef());
		bean.setRole(source.getRole());
		
		return bean;
	}
	
	protected InsertCodingSchemeMultiAttribBean buildInsertLocalNameBean(String prefix, String sourceId, String codingSchemeId, String localName){
		InsertCodingSchemeMultiAttribBean bean = new InsertCodingSchemeMultiAttribBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeId(codingSchemeId);
		bean.setId(sourceId);
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_LOCALNAME);
		bean.setAttributeValue(localName);

		return bean;
	}
	
	protected InsertOrUpdateCodingSchemeBean buildInsertCodingSchemeBean(String prefix, String codingSchemeId, String entryStateId, CodingScheme codingScheme){
		InsertOrUpdateCodingSchemeBean bean = new InsertOrUpdateCodingSchemeBean();
		bean.setPrefix(prefix);
		bean.setCodingScheme(codingScheme);
		bean.setId(codingSchemeId);
		bean.setEntryStateId(entryStateId);
		
		return bean;
	}
	
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, this.supportedDatebaseVersion);
	}

	public void setVersionsDao(VersionsDao versionsDao) {
		this.versionsDao = versionsDao;
	}

	public VersionsDao getVersionsDao() {
		return versionsDao;
	}

	public void setEntityDao(EntityDao entityDao) {
		this.entityDao = entityDao;
	}

	public EntityDao getEntityDao() {
		return entityDao;
	}
}

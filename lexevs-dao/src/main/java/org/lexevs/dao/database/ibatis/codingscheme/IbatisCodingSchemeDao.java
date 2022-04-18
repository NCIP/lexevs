
package org.lexevs.dao.database.ibatis.codingscheme;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedHierarchy;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.URIMap;
import org.LexGrid.relations.Relations;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import static org.mybatis.dynamic.sql.SqlBuilder.*;
import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.access.systemRelease.SystemReleaseDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.constants.classifier.mapping.ClassToStringMappingClassifier;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.codingscheme.parameter.InsertOrUpdateCodingSchemeBean;
import org.lexevs.dao.database.ibatis.codingscheme.parameter.InsertOrUpdateCodingSchemeMultiAttribBean;
import org.lexevs.dao.database.ibatis.codingscheme.parameter.InsertOrUpdateURIMapBean;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
//import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * The Class IbatisCodingSchemeDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Cacheable(cacheName = "IbatisCodingSchemeDaoCache")
public class IbatisCodingSchemeDao extends AbstractIbatisDao implements CodingSchemeDao {
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");

	/** The SUPPORTE d_ attri b_ gette r_ prefix. */
	private static String SUPPORTED_ATTRIB_GETTER_PREFIX = "_supported";
	
	/** The CODIN g_ schem e_ namespace. */
	public static String CODING_SCHEME_NAMESPACE = "CodingScheme.";
	
	/** The REMOV e_ codin g_ schem e_ b y_ i d_ sql. */
	private static String REMOVE_CODING_SCHEME_BY_UID_SQL = CODING_SCHEME_NAMESPACE + "deleteCodingSchemeByUId";
	
	/** The INSER t_ codin g_ schem e_ sql. */
	private static String INSERT_CODING_SCHEME_SQL = CODING_SCHEME_NAMESPACE + "insertCodingScheme";
	
	/** The GE t_ codin g_ schem e_ b y_ i d_ sql. */
	private static String GET_CODING_SCHEME_BY_ID_SQL = CODING_SCHEME_NAMESPACE + "getCodingSchemeByUId";
	
	/** The GE t_ codin g_ schem e_ summar y_ b y_ ur i_ an d_ versio n_ sql. */
	private static String GET_CODING_SCHEME_SUMMARY_BY_URI_AND_VERSION_SQL = CODING_SCHEME_NAMESPACE + "getCodingSchemeSummaryByUriAndVersion";
	
	/** The GE t_ codin g_ schem e_ i d_ b y_ nam e_ an d_ versio n_ sql. */
	private static String GET_CODING_SCHEME_ID_BY_NAME_AND_VERSION_SQL = CODING_SCHEME_NAMESPACE + "getCodingSchemeIdByNameAndVersion";
	
	/** The GE t_ codin g_ schem e_ i d_ b y_ ur i_ an d_ versio n_ sql. */
	private static String GET_CODING_SCHEME_ID_BY_URI_AND_VERSION_SQL = CODING_SCHEME_NAMESPACE + "getCodingSchemeIdByUriAndVersion";
	
	/** The UPDAT e_ codin g_ schem e_ b y_ i d_ sql. */
	private static String UPDATE_CODING_SCHEME_BY_ID_SQL = CODING_SCHEME_NAMESPACE + "updateCodingSchemeByUId";
	
	/** update codingScheme versionableAttrib. */
	private static String UPDATE_CODING_SCHEME_VER_ATTRIB_BY_ID_SQL = CODING_SCHEME_NAMESPACE + "updateCodingSchemeVerAttribByUId";
	
	/** The INSER t_ codin g_ schem e_ multiattri b_ sql. */
	private static String INSERT_CODING_SCHEME_MULTIATTRIB_SQL = CODING_SCHEME_NAMESPACE + "insertCodingSchemeMultiAttrib";
	
	/** The INSER t_ urima p_ sql. */
	private static String INSERT_URIMAP_SQL = CODING_SCHEME_NAMESPACE + "insertURIMap";
	
	/** The GE t_ distinc t_ propert y_ name s_ o f_ c s_ sql. */
	private static String GET_DISTINCT_PROPERTY_NAMES_OF_CS_SQL = CODING_SCHEME_NAMESPACE + "getDistinctPropertyNames";
	
	private static String GET_DISTINCT_PROPERTY_NAME_AND_TYPE_SQL = CODING_SCHEME_NAMESPACE + "getDistinctPropertyNamdAndType";
	
	/** The GE t_ distinc t_ entit y_ type s_ o f_ c s_ sql. */
	private static String GET_DISTINCT_ENTITY_TYPES_OF_CS_SQL = CODING_SCHEME_NAMESPACE + "getDistinctEntityTypes";
	
	/** The GE t_ distinc t_ propert y_ qualifie r_ name s_ o f_ c s_ sql. */
	private static String GET_DISTINCT_PROPERTY_QUALIFIER_NAMES_OF_CS_SQL = CODING_SCHEME_NAMESPACE + "getDistinctPropertyQualifierNames";
	
	/** The GE t_ distinc t_ propert y_ qualifie r_ type s_ o f_ c s_ sql. */
	private static String GET_DISTINCT_PROPERTY_QUALIFIER_TYPES_OF_CS_SQL = CODING_SCHEME_NAMESPACE + "getDistinctPropertyQualifierTypes";
	
	/** The GE t_ distinc t_ format s_ o f_ c s_ sql. */
	private static String GET_DISTINCT_FORMATS_OF_CS_SQL = CODING_SCHEME_NAMESPACE + "getDistinctFormats";
	
	/** The GE t_ distinc t_ namespace s_ o f_ c s_ sql. */
	private static String GET_DISTINCT_NAMESPACES_OF_CS_SQL = CODING_SCHEME_NAMESPACE + "getDistinctNamespaces";
	
	/** The GE t_ distinc t_ language s_ o f_ c s_ sql. */
	private static String GET_DISTINCT_LANGUAGES_OF_CS_SQL = CODING_SCHEME_NAMESPACE + "getDistinctLanguages";
	
	/** The GE t_ urimap s_ sql. */
	private static String GET_URIMAPS_SQL = CODING_SCHEME_NAMESPACE + "getURIMaps";
	
	/** The GE t_ urima p_ b y_ localnam e_ an d_ typ e_ sql. */
	private static String GET_URIMAP_BY_LOCALNAME_AND_TYPE_SQL = CODING_SCHEME_NAMESPACE + "getURIMapByLocalNameAndType";
	
	/** The GE t_ urima p_ coun t_ b y_ localnam e_ an d_ typ e_ sql. */
	private static String GET_URIMAP_COUNT_BY_LOCALNAME_AND_TYPE_SQL = CODING_SCHEME_NAMESPACE + "getURIMapCountByLocalNameAndType";
	
	private static String GET_PROPERTY_URIMAP_FOR_PROPERTYTYPE_SQL = CODING_SCHEME_NAMESPACE + "getPropertyURIMapByPropertyType";
	
	private static String GET_CODING_SCHEME_BY_ID_AND_REVISION_GUID_SQL = CODING_SCHEME_NAMESPACE + "getCodingSchemeByIdAndRevisionId";
	
	private static String UPDATE_URIMAP_BY_LOCALID_SQL = CODING_SCHEME_NAMESPACE + "updateUriMapByLocalId";
	
	private static String UPDATE_CODINGSCHEME_SOURCE_BY_CONTENT_SQL = CODING_SCHEME_NAMESPACE + "updateCodingSchemeSourceByValue";
	
	private static String DELETE_CODINGSCHEME_MULTIATTRIBUTE_BY_CODINGSCHEME_ID_SQL = CODING_SCHEME_NAMESPACE + "deleteCodingSchemeMultiAttributeByCodingSchemeId";
	
	private static String DELETE_MAPPINGS_BY_CODINGSCHEME_ID_SQL = CODING_SCHEME_NAMESPACE + "deleteMappingsByCodingSchemeId";
	
	private static String GET_CODINGSCHEME_METADATA_BY_UID = CODING_SCHEME_NAMESPACE + "getCodingSchemeMetaDataByUId";
	
	/** */
	private static String GET_ENTRYSTATE_UID_BY_CODINGSCHEME_UID_SQL = CODING_SCHEME_NAMESPACE + "getEntryStateUIdByCodingSchemeUId";
	
	private static String UPDATE_CODING_SCHEME_ENTRYSTATE_UID = CODING_SCHEME_NAMESPACE + "updateCodingSchemeEntryStateUId";

	private static String GET_CODING_SCHEME_LATEST_REVISION_ID_BY_UID = CODING_SCHEME_NAMESPACE + "getCodingSchemeLatestRevisionIdByUId";
	
	private static String GET_CODING_SCHEME_REVISION_ID_WHEN_NEW_BY_UID = CODING_SCHEME_NAMESPACE + "getCodingSchemeRevisionIdWhenNewByUId";
	
	private static String DELETE_ALL_CODINGSCHEME_SOURCE_BY_CSID_SQL = CODING_SCHEME_NAMESPACE + "deleteAllCodingSchemeSourceByCodingSchemeUId";
	
	private static String DELETE_ALL_CODINGSCHEME_LOCALNAMES_BY_CSID_SQL = CODING_SCHEME_NAMESPACE + "deleteAllCodingSchemeLocalNamesByCodingSchemeUId";
	
	private static String GET_ALL_CODING_SCHEME_REVISIONS_SQL = CODING_SCHEME_NAMESPACE + "getAllCodingSchemeRevisionsByUId";
	
	private static String UPDATE_CS_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL = CODING_SCHEME_NAMESPACE + "updateCSMultiAttribEntryStateUId";
	
	/** The class to string mapping classifier. */
	private ClassToStringMappingClassifier classToStringMappingClassifier = new ClassToStringMappingClassifier();
	
	SqlSessionFactory sqlSessionFactory;
	
	/** The versions dao. */
	private VersionsDao versionsDao;
	
	/** The systemRelease dao*/
	private SystemReleaseDao systemReleaseDao = null;
	
	/** The entity dao. */
	private EntityDao entityDao;
	
	private AssociationDao associationDao;
	
	private PropertyDao propertyDao = null;
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeById(java.lang.String)
	 */
	@CacheMethod
	public CodingScheme getCodingSchemeByUId(String codingSchemeUId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		CodingScheme scheme = 
			(CodingScheme) this.getSqlSessionTemplate().selectOne(
				GET_CODING_SCHEME_BY_ID_SQL, new PrefixedParameter(prefix, codingSchemeUId));

		scheme.setMappings(
				this.getMappings(codingSchemeUId));
		
		for(String relationsUid : associationDao.getRelationsUIdsForCodingSchemeUId(codingSchemeUId)){
			scheme.addRelations(associationDao.getRelationsByUId(codingSchemeUId, relationsUid, true));
		}
		
		scheme.setProperties(new Properties());
		
		List<Property> properties = this.propertyDao.
			getAllPropertiesOfParent(codingSchemeUId, codingSchemeUId, PropertyType.CODINGSCHEME);
		
		scheme.getProperties().setProperty(properties);

		return scheme;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeByUriAndVersion(java.lang.String, java.lang.String)
	 */
	@CacheMethod
	public CodingScheme getCodingSchemeByUriAndVersion(String codingSchemeUri,
			String version) {
		String codingSchemeUId = this.getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		return this.getCodingSchemeByUId(codingSchemeUId);
	}


	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeByNameAndVersion(java.lang.String, java.lang.String)
	 */
	@CacheMethod
	public CodingScheme getCodingSchemeByNameAndVersion(String codingSchemeName, String representsVersion){
		String codingSchemeUId = this.getCodingSchemeUIdByNameAndVersion(codingSchemeName, representsVersion);
		return this.getCodingSchemeByUId(codingSchemeUId);
	}

	/**
	 * Delete source.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param version the version
	 * @param source the source
	 */
	@ClearCache
	public void deleteCodingSchemeSources(String codingSchemeId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		this.getSqlSessionTemplate().delete(
				DELETE_CODINGSCHEME_MULTIATTRIBUTE_BY_CODINGSCHEME_ID_SQL, 
				new PrefixedParameterTuple(prefix, codingSchemeId, SQLTableConstants.TBLCOLVAL_SOURCE));
		
	}
	
	@ClearCache
	public void deleteCodingSchemeLocalNames(String codingSchemeId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		this.getSqlSessionTemplate().delete(
				DELETE_CODINGSCHEME_MULTIATTRIBUTE_BY_CODINGSCHEME_ID_SQL, 
				new PrefixedParameterTuple(prefix, codingSchemeId, SQLTableConstants.TBLCOLVAL_LOCALNAME));
	}
	
	@ClearCache
	public void deleteCodingSchemeMappings(String codingSchemeId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		this.getSqlSessionTemplate().delete(
				DELETE_MAPPINGS_BY_CODINGSCHEME_ID_SQL, 
				new PrefixedParameter(prefix, codingSchemeId));
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeByRevision(java.lang.String, java.lang.String, java.lang.String)
	 */
	@CacheMethod
	public CodingScheme getHistoryCodingSchemeByRevision(String codingSchemeUId, String revisionId) {
		String prefix = this.getPrefixResolver().resolvePrefixForHistoryCodingScheme(codingSchemeUId);
		String actualTableSetPrefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		PrefixedParameterTuple param = new PrefixedParameterTuple(prefix, codingSchemeUId, revisionId);
		param.setActualTableSetPrefix(actualTableSetPrefix);
		
		return (CodingScheme)
			this.getSqlSessionTemplate().selectOne(GET_CODING_SCHEME_BY_ID_AND_REVISION_GUID_SQL, 
					param);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeSummaryByUriAndVersion(java.lang.String, java.lang.String)
	 */
	@CacheMethod
	public CodingSchemeSummary getCodingSchemeSummaryByUriAndVersion(
			String codingSchemeUri, String version) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUri, version);
		return (CodingSchemeSummary)
			this.getSqlSessionTemplate().selectOne(GET_CODING_SCHEME_SUMMARY_BY_URI_AND_VERSION_SQL, 
				new PrefixedParameterTuple(prefix, codingSchemeUri, version));
	}


	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertHistoryCodingScheme(java.lang.String, org.LexGrid.codingSchemes.CodingScheme)
	 */
/*	@Override
	public void insertHistoryCodingScheme(
			String codingSchemeUId, String releaseUId, String entryStateUId,
			CodingScheme codingScheme) {
		String prefix = this.getPrefixResolver().resolveHistoryPrefix();

		this.doInsertCodingScheme(
				codingSchemeUId, 
				prefix, 
				codingScheme, releaseUId, entryStateUId,
				true);
	}*/

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#deleteCodingSchemeById(java.lang.String)
	 */
	@ClearCache
	public void deleteCodingSchemeByUId(String codingSchemeUId) {
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		this.getSqlSessionTemplate().
			delete(REMOVE_CODING_SCHEME_BY_UID_SQL, new PrefixedParameter(prefix, codingSchemeUId));	
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertCodingScheme(org.LexGrid.codingSchemes.CodingScheme)
	 */
	public String insertCodingScheme(
			CodingScheme codingScheme, String releaseUId,
			boolean cascade) {
		String codingSchemeUId = this.createUniqueId();
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingScheme.getCodingSchemeURI(),
				codingScheme.getRepresentsVersion());
		String entryStateUId = this.createUniqueId();
		
		return this.doInsertCodingScheme(
				codingSchemeUId, 
				prefix, 
				codingScheme, releaseUId, entryStateUId,
				cascade);
	}
	
	/**
	 * Do insert coding scheme.
	 * 
	 * @param codingSchemeUId the coding scheme id
	 * @param prefix the prefix
	 * @param codingScheme the coding scheme
	 * 
	 * @return the string
	 */
	protected String doInsertCodingScheme(
			String codingSchemeUId, 
			String prefix, 
			CodingScheme codingScheme, 
			String releaseUId, 
			String entryStateUId,
			boolean cascade) {

		this.getSqlSessionTemplate().insert(INSERT_CODING_SCHEME_SQL, 
				this.buildInsertCodingSchemeBean(
						prefix,
						codingSchemeUId, releaseUId, entryStateUId, codingScheme));	

		versionsDao.insertEntryState(
				codingSchemeUId,
				entryStateUId, 
				codingSchemeUId,
				EntryStateType.CODINGSCHEME,
				null, 
				codingScheme.getEntryState());
		
		for(Source source : codingScheme.getSource()){
			String sourceUId = this.createUniqueId();
			this.doInsertCodingSchemeSource(prefix, codingSchemeUId, sourceUId, entryStateUId, source);
		}
		
		for(String localName : codingScheme.getLocalName()){
			String localNameUId = this.createUniqueId();
			this.doInsertCodingSchemeLocalName(prefix, codingSchemeUId, localNameUId, entryStateUId, localName);
		}
		
		this.insertMappings(codingSchemeUId, codingScheme.getMappings());
		
		if(cascade) {
			if(codingScheme.getEntities() != null) {
				List<Entity> entities = new ArrayList<Entity>();
				entities.addAll(Arrays.asList(codingScheme.getEntities().getEntity()));
				entities.addAll(Arrays.asList(codingScheme.getEntities().getAssociationEntity()));
				
				this.entityDao.insertBatchEntities(
						codingSchemeUId, 
						entities, 
						cascade);
			}
			
			for(Relations relations : codingScheme.getRelations()) {
				this.associationDao.
					insertRelations(
							codingSchemeUId, 
							relations,
							cascade);
			}
			
			if (codingScheme.getProperties() != null) {
				for (Property property : codingScheme.getProperties()
						.getProperty()) {

					this.propertyDao.insertProperty(codingSchemeUId,
							codingSchemeUId, PropertyType.CODINGSCHEME,
							property);
				}
			}
		}
		return codingSchemeUId;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#updateCodingScheme(java.lang.String, org.LexGrid.codingSchemes.CodingScheme)
	 */
	@ClearCache
	public String updateCodingScheme(String codingSchemeUId,
			CodingScheme codingScheme) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				codingSchemeUId);

		String entryStateUId = this.createUniqueId();

		InsertOrUpdateCodingSchemeBean bean = new InsertOrUpdateCodingSchemeBean();
		bean.setPrefix(prefix);
		bean.setCodingScheme(codingScheme);
		bean.setUId(codingSchemeUId);
		bean.setEntryStateUId(entryStateUId);

		this.getSqlSessionTemplate().update(UPDATE_CODING_SCHEME_BY_ID_SQL,
				bean);

		Source[] sourceList = codingScheme.getSource();

		if (sourceList.length != 0) {
			
			this.getSqlSessionTemplate().delete(
					DELETE_ALL_CODINGSCHEME_SOURCE_BY_CSID_SQL,
					new PrefixedParameter(prefix, codingSchemeUId));

			for (int i = 0; i < sourceList.length; i++) {

				InsertOrUpdateCodingSchemeMultiAttribBean multiAttribBean = new InsertOrUpdateCodingSchemeMultiAttribBean();

				multiAttribBean.setPrefix(prefix);
				multiAttribBean.setUId(this.createUniqueId());
				multiAttribBean.setCodingSchemeUId(codingSchemeUId);
				multiAttribBean.setAttributeType(SQLTableConstants.TBLCOLVAL_SOURCE);
				multiAttribBean.setAttributeValue(sourceList[i].getContent());
				multiAttribBean.setRole(sourceList[i].getRole());
				multiAttribBean.setSubRef(sourceList[i].getSubRef());
				multiAttribBean.setEntryStateUId(entryStateUId);

				this.getSqlSessionTemplate().insert(
						INSERT_CODING_SCHEME_MULTIATTRIB_SQL, multiAttribBean);
			}
		} else {
			
			this.getSqlSessionTemplate().update(
					UPDATE_CS_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
					new PrefixedParameterTriple(prefix, codingSchemeUId,
							SQLTableConstants.TBLCOLVAL_SOURCE,
							entryStateUId));
		}
		
		String[] localNameList = codingScheme.getLocalName();

		if (localNameList.length != 0) {

			this.getSqlSessionTemplate().delete(
					DELETE_ALL_CODINGSCHEME_LOCALNAMES_BY_CSID_SQL,
					new PrefixedParameter(prefix, codingSchemeUId));

			for (int i = 0; i < localNameList.length; i++) {
				InsertOrUpdateCodingSchemeMultiAttribBean multiAttribBean = new InsertOrUpdateCodingSchemeMultiAttribBean();

				multiAttribBean.setPrefix(prefix);
				multiAttribBean.setUId(this.createUniqueId());
				multiAttribBean.setCodingSchemeUId(codingSchemeUId);
				multiAttribBean.setAttributeType(SQLTableConstants.TBLCOLVAL_LOCALNAME);
				multiAttribBean.setAttributeValue(localNameList[i]);
				multiAttribBean.setEntryStateUId(entryStateUId);

				this.getSqlSessionTemplate().insert(
						INSERT_CODING_SCHEME_MULTIATTRIB_SQL, multiAttribBean);
			}
		} else {
			
			this.getSqlSessionTemplate().update(
					UPDATE_CS_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
					new PrefixedParameterTriple(prefix, codingSchemeUId,
							SQLTableConstants.TBLCOLVAL_LOCALNAME,
							entryStateUId));
		}
		
		if(codingScheme.getMappings() != null) {
			List<URIMap> maps = DaoUtility.getAllURIMappings(codingScheme.getMappings());
			for(URIMap uriMap : maps) {
				this.insertOrUpdateURIMap(codingSchemeUId, uriMap);
			}
		}

		return entryStateUId;
	}

	@Override
	@ClearCache
	public String updateCodingSchemeVersionableAttrib(String codingSchemeUId, CodingScheme codingScheme) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		String entryStateUId = this.createUniqueId();
		
		InsertOrUpdateCodingSchemeBean bean = new InsertOrUpdateCodingSchemeBean();
		bean.setPrefix(prefix);
		bean.setCodingScheme(codingScheme);
		bean.setUId(codingSchemeUId);
		bean.setEntryStateUId(entryStateUId);
		
		this.getSqlSessionTemplate().update(UPDATE_CODING_SCHEME_VER_ATTRIB_BY_ID_SQL, bean);
		
		this.getSqlSessionTemplate().update(
				UPDATE_CS_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
				new PrefixedParameterTriple(prefix, codingSchemeUId,
						SQLTableConstants.TBLCOLVAL_SOURCE,
						entryStateUId));
		
		this.getSqlSessionTemplate().update(
				UPDATE_CS_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
				new PrefixedParameterTriple(prefix, codingSchemeUId,
						SQLTableConstants.TBLCOLVAL_LOCALNAME,
						entryStateUId));
		
		return entryStateUId;
	}
	
	/**
	 * Update coding scheme.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param version the version
	 * @param codingScheme the coding scheme
	 */
	public void updateCodingScheme(String codingSchemeName, String version, CodingScheme codingScheme) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeIdByNameAndVersion(java.lang.String, java.lang.String)
	 */
	@CacheMethod
	public String getCodingSchemeUIdByNameAndVersion(String codingSchemeName, String version) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeName, version);
		return (String) this.getSqlSessionTemplate().selectOne(GET_CODING_SCHEME_ID_BY_NAME_AND_VERSION_SQL,
				new PrefixedParameterTuple(prefix, codingSchemeName, version));
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeIdByUriAndVersion(java.lang.String, java.lang.String)
	 */
	@CacheMethod
	public String getCodingSchemeUIdByUriAndVersion(String codingSchemeUri, String version) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUri, version);
		return (String) this.getSqlSessionTemplate().selectOne(GET_CODING_SCHEME_ID_BY_URI_AND_VERSION_SQL,
				new PrefixedParameterTuple(prefix, codingSchemeUri, version));
	}
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getEntryStateId(java.lang.String, java.lang.String)
	 */
	@CacheMethod
	public String getEntryStateUId(String codingSchemeUId) {
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
				
		return (String) this.getSqlSessionTemplate().selectOne(GET_ENTRYSTATE_UID_BY_CODINGSCHEME_UID_SQL,
				new PrefixedParameter(prefix, codingSchemeUId));
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertCodingSchemeSource(java.lang.String, org.LexGrid.commonTypes.Source)
	 */
	@ClearCache
	public void insertCodingSchemeSource(String codingSchemeId, Source source) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		String sourceId = this.createUniqueId();
		this.doInsertCodingSchemeSource(prefix, codingSchemeId, sourceId, null, source);
	}
	
	@ClearCache
	public void insertOrUpdateCodingSchemeSource(String codingSchemeId, Source source) {
		Assert.notNull(source, "Source is null for coding scheme source");
		Assert.hasText(source.getContent(), "Source context is empty for while updating coding scheme source");
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);

		int count = this.getSqlSessionTemplate().update(
				UPDATE_CODINGSCHEME_SOURCE_BY_CONTENT_SQL, 
				this.buildInsertOrUpdateSourceBean(prefix, null, codingSchemeId, null, source));
		
		if(count == 0) {
			this.insertCodingSchemeSource(codingSchemeId, source);
		}
	}
	
	protected void doInsertCodingSchemeSource(String prefix, String codingSchemeId, String sourceId, String entryStateId, Source source) {
		this.getSqlSessionTemplate().insert(INSERT_CODING_SCHEME_MULTIATTRIB_SQL,
				this.buildInsertOrUpdateSourceBean(prefix, sourceId, codingSchemeId, entryStateId, source));
	}
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertCodingSchemeLocalName(java.lang.String, java.lang.String)
	 */
	@ClearCache
	public void insertCodingSchemeLocalName(String codingSchemeId,
			String localName) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		String localNameId = this.createUniqueId();
		this.doInsertCodingSchemeLocalName(prefix, codingSchemeId, localNameId, null, localName);	
	}
	
	protected void doInsertCodingSchemeLocalName(String prefix, String codingSchemeId, String localNameId, String entryStateId, String localName) {
		this.getSqlSessionTemplate().insert(INSERT_CODING_SCHEME_MULTIATTRIB_SQL,
				this.buildInsertLocalNameBean(prefix, localNameId, codingSchemeId, entryStateId, localName));
	}
	

	@Override
	@ClearCache
	public void insertOrUpdateURIMap(String codingSchemeId, URIMap uriMap) {
		int rows = this.getSqlSessionTemplate().update(
				UPDATE_URIMAP_BY_LOCALID_SQL, 
				this.buildInsertOrUpdateURIMapBean(
						this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId), 
						null, 
						codingSchemeId, 
						classToStringMappingClassifier.classify(uriMap.getClass()),
						uriMap));
		
		if(rows == 0) {
			this.insertURIMap(codingSchemeId, uriMap);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertURIMap(java.lang.String, org.LexGrid.naming.URIMap)
	 */
	@ClearCache
	public void insertURIMap(String codingSchemeId, URIMap uriMap) {
		String uriMapId = this.createUniqueId();
		this.getSqlSessionTemplate().insert(
				INSERT_URIMAP_SQL, buildInsertOrUpdateURIMapBean(
									this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId),
									uriMapId, 
									codingSchemeId,
									classToStringMappingClassifier.classify(uriMap.getClass()),
									uriMap));
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertURIMap(java.lang.String, java.util.List)
	 */
	@ClearCache
	public void insertURIMap(final String codingSchemeId,
			final List<URIMap> supportedProperties) {


				for(URIMap uriMap : supportedProperties){
					String uriMapId = createUniqueId();
					
					this.getSqlSessionBatchTemplate().insert(INSERT_URIMAP_SQL, 
							buildInsertOrUpdateURIMapBean(
									getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId),
									uriMapId, 
									codingSchemeId,
									classToStringMappingClassifier.classify(uriMap.getClass()),
									uriMap));

			}		
		
		// see: https://developpaper.com/using-mybatis-to-realize-batch-insertion-in-spring/
//		
//		try(SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
//	        SimpleTableMapper mapper = session.getMapper(URIMap.class);
/////	        List<SimpleTableRecord> records = getRecordsToInsert(); // not shown
//
//	        BatchInsert<URIMap> batchInsert = insert(supportedProperties)
//	                .into(simpleTable)
//	                .map(id).toProperty("id")
//	                .map(firstName).toProperty("firstName")
//	                .map(lastName).toProperty("lastName")
//	                .map(birthDate).toProperty("birthDate")
//	                .map(employed).toProperty("employed")
//	                .map(occupation).toProperty("occupation")
//	                .build()
//	                .render(RenderingStrategies.MYBATIS3);
//
//	        batchInsert.insertStatements().forEach(mapper::insert);
//
//	        session.commit();
	    }
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertMappings(java.lang.String, org.LexGrid.naming.Mappings)
	 */
	@SuppressWarnings("unchecked")
	@ClearCache
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
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getDistinctFormatsOfCodingScheme(java.lang.String)
	 */
	@Override
	public List<String> getDistinctFormatsOfCodingScheme(String codingSchemeId) {
		return doDistinctQuery(
				GET_DISTINCT_FORMATS_OF_CS_SQL, codingSchemeId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getDistinctLanguagesOfCodingScheme(java.lang.String)
	 */
	@Override
	public List<String> getDistinctLanguagesOfCodingScheme(String codingSchemeId) {
		return doDistinctQuery(
				GET_DISTINCT_LANGUAGES_OF_CS_SQL, codingSchemeId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getDistinctNamespacesOfCodingScheme(java.lang.String)
	 */
	@Override
	public List<String> getDistinctNamespacesOfCodingScheme(
			String codingSchemeId) {
		return doDistinctQuery(
				GET_DISTINCT_NAMESPACES_OF_CS_SQL, codingSchemeId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getDistinctPropertyNamesOfCodingScheme(java.lang.String)
	 */
	public List<String> getDistinctPropertyNamesOfCodingScheme(
			String codingSchemeId) {
		return doDistinctQuery(
				GET_DISTINCT_PROPERTY_NAMES_OF_CS_SQL, codingSchemeId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getDistinctPropertyNameAndType(java.lang.String)
	 */
	@Override
	public List<NameAndValue> getDistinctPropertyNameAndType(
			String codingSchemeId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return this.getSqlSessionTemplate().selectList(
				GET_DISTINCT_PROPERTY_NAME_AND_TYPE_SQL, 
				new PrefixedParameter(prefix, codingSchemeId));
	}
	
	/**
	 * Do distinct query.
	 * 
	 * @param queryName the query name
	 * @param codingSchemeId the coding scheme id
	 * 
	 * @return the list< string>
	 */
	protected List<String> doDistinctQuery(String queryName, String codingSchemeId){
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return this.getSqlSessionTemplate().selectList(
				queryName, 
				new PrefixedParameter(prefix, codingSchemeId));
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getDistinctPropertyQualifierNamesOfCodingScheme(java.lang.String)
	 */
	@Override
	public List<String> getDistinctPropertyQualifierNamesOfCodingScheme(
			String codingSchemeId) {
		return doDistinctQuery(
				GET_DISTINCT_PROPERTY_QUALIFIER_NAMES_OF_CS_SQL, codingSchemeId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getDistinctPropertyQualifierTypesOfCodingScheme(java.lang.String)
	 */
	@Override
	public List<String> getDistinctPropertyQualifierTypesOfCodingScheme(
			String codingSchemeId) {
		return doDistinctQuery(
				GET_DISTINCT_PROPERTY_QUALIFIER_TYPES_OF_CS_SQL, codingSchemeId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getDistinctEntityTypesOfCodingScheme(java.lang.String)
	 */
	@Override
	public List<String> getDistinctEntityTypesOfCodingScheme(
			String codingSchemeId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return this.getSqlSessionTemplate().selectList(
				GET_DISTINCT_ENTITY_TYPES_OF_CS_SQL, 
				new PrefixedParameter(prefix, codingSchemeId));
	}

	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getMappings(java.lang.String)
	 */
	@Override
	public Mappings getMappings(String codingSchemeId) {
		Mappings mappings = new Mappings();
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		List<URIMap> uriMaps = this.getSqlSessionTemplate().selectList(	
				GET_URIMAPS_SQL, 
				new PrefixedParameter(prefix, codingSchemeId));
		
		for(URIMap uriMap : uriMaps) {
			DaoUtility.insertIntoMappings(mappings, uriMap);
		}
		
		return mappings;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getUriMap(java.lang.String, java.lang.String, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T extends URIMap> T getUriMap(String codingSchemeId, String localId, Class<T> uriMap) {	
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return (T) this.getSqlSessionTemplate().selectOne(	
				GET_URIMAP_BY_LOCALNAME_AND_TYPE_SQL, 
				new PrefixedParameterTriple(prefix, codingSchemeId, localId, this.classToStringMappingClassifier.classify(uriMap)));
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#validateSupportedAttribute(java.lang.String, java.lang.String, java.lang.Class)
	 */
	@CacheMethod
	public <T extends URIMap> boolean validateSupportedAttribute(String codingSchemeId, String localId, Class<T> uriMap) {	
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		String classifiedName = this.classToStringMappingClassifier.classify(uriMap);
		int count = (Integer) this.getSqlSessionTemplate().selectOne(	
				GET_URIMAP_COUNT_BY_LOCALNAME_AND_TYPE_SQL, 
				new PrefixedParameterTriple(prefix, codingSchemeId, localId, classifiedName));
		
		return count > 0;
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
	protected InsertOrUpdateURIMapBean buildInsertOrUpdateURIMapBean(String prefix, String uriMapId, String codingSchemeId, String supportedAttributeTag, URIMap uriMap){
		InsertOrUpdateURIMapBean bean = new InsertOrUpdateURIMapBean();
		bean.setPrefix(prefix);
		bean.setSupportedAttributeTag(supportedAttributeTag);
		bean.setCodingSchemeUId(codingSchemeId);
		bean.setUriMap(uriMap);
		bean.setUId(uriMapId);
		
		if (uriMap instanceof SupportedHierarchy){
			String[] assocNames = ((SupportedHierarchy)uriMap).getAssociationNames();
			bean.setAssociationNames(StringUtils.arrayToCommaDelimitedString(assocNames));
		}
		
		return bean;
	}
	
	/**
	 * Builds the insert source bean.
	 * 
	 * @param prefix the prefix
	 * @param sourceId the source id
	 * @param codingSchemeId the coding scheme id
	 * @param source the source
	 * 
	 * @return the insert coding scheme multi attrib bean
	 */
	protected InsertOrUpdateCodingSchemeMultiAttribBean buildInsertOrUpdateSourceBean(String prefix, String sourceId, String codingSchemeId, String entryStateId, Source source){
		InsertOrUpdateCodingSchemeMultiAttribBean bean = new InsertOrUpdateCodingSchemeMultiAttribBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeUId(codingSchemeId);
		bean.setUId(sourceId);
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_SOURCE);
		bean.setAttributeValue(source.getContent());
		bean.setSubRef(source.getSubRef());
		bean.setRole(source.getRole());
		bean.setEntryStateUId(entryStateId);
		
		return bean;
	}
	
	/**
	 * Builds the insert local name bean.
	 * 
	 * @param prefix the prefix
	 * @param sourceId the source id
	 * @param codingSchemeId the coding scheme id
	 * @param localName the local name
	 * 
	 * @return the insert coding scheme multi attrib bean
	 */
	protected InsertOrUpdateCodingSchemeMultiAttribBean buildInsertLocalNameBean(String prefix, String localNameId, String codingSchemeId, String entryStateId, String localName){
		InsertOrUpdateCodingSchemeMultiAttribBean bean = new InsertOrUpdateCodingSchemeMultiAttribBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeUId(codingSchemeId);
		bean.setUId(localNameId);
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_LOCALNAME);
		bean.setAttributeValue(localName);
		bean.setEntryStateUId(entryStateId);

		return bean;
	}
	
	/**
	 * Builds the insert coding scheme bean.
	 * 
	 * @param prefix the prefix
	 * @param codingSchemeUId the coding scheme id
	 * @param entryStateUId the entry state id
	 * @param codingScheme the coding scheme
	 * 
	 * @return the insert or update coding scheme bean
	 */
	protected InsertOrUpdateCodingSchemeBean buildInsertCodingSchemeBean(String prefix, String codingSchemeUId, String releaseUId, String entryStateUId, CodingScheme codingScheme){
		InsertOrUpdateCodingSchemeBean bean = new InsertOrUpdateCodingSchemeBean();
		bean.setPrefix(prefix);
		bean.setCodingScheme(codingScheme);
		bean.setReleaseUId(releaseUId);
		bean.setUId(codingSchemeUId);
		bean.setEntryStateUId(entryStateUId);
		
		return bean;
	}
	
	@Override
	public void insertCodingSchemeDependentChanges(String codingSchemeId,
			CodingScheme codingScheme) {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.AbstractBaseDao#doGetSupportedLgSchemaVersions()
	 */
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, this.supportedDatebaseVersion);
	}

	/**
	 * Sets the versions dao.
	 * 
	 * @param versionsDao the new versions dao
	 */
	public void setVersionsDao(VersionsDao versionsDao) {
		this.versionsDao = versionsDao;
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
	 * Sets the entity dao.
	 * 
	 * @param entityDao the new entity dao
	 */
	public void setEntityDao(EntityDao entityDao) {
		this.entityDao = entityDao;
	}

	/**
	 * Gets the entity dao.
	 * 
	 * @return the entity dao
	 */
	public EntityDao getEntityDao() {
		return entityDao;
	}

	public AssociationDao getAssociationDao() {
		return associationDao;
	}

	public void setAssociationDao(AssociationDao associationDao) {
		this.associationDao = associationDao;
	}

	public SystemReleaseDao getSystemReleaseDao() {
		return systemReleaseDao;
	}

	public void setSystemReleaseDao(SystemReleaseDao systemReleaseDao) {
		this.systemReleaseDao = systemReleaseDao;
	}

	@Override
	public String insertHistoryCodingScheme(String codingSchemeUId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		InsertOrUpdateCodingSchemeBean codingSchemeData = (InsertOrUpdateCodingSchemeBean) this
				.getSqlSessionTemplate().selectOne(GET_CODINGSCHEME_METADATA_BY_UID,
						new PrefixedParameter(prefix, codingSchemeUId));

		String historyPrefix = this.getPrefixResolver().resolvePrefixForHistoryCodingScheme(codingSchemeUId);
		
		codingSchemeData.setPrefix(historyPrefix);
		
		this.getSqlSessionTemplate().insert(
				INSERT_CODING_SCHEME_SQL, codingSchemeData);
		
		if( codingSchemeData.getCsMultiAttribList() != null ) {
			for (int i = 0; i < codingSchemeData.getCsMultiAttribList().size(); i++) {
				
				codingSchemeData.getCsMultiAttribList().get(i).setPrefix(historyPrefix);
				
				this.getSqlSessionTemplate().insert(
						INSERT_CODING_SCHEME_MULTIATTRIB_SQL,
						codingSchemeData.getCsMultiAttribList().get(i));
			}
		}
		
		if (!entryStateExists(prefix, codingSchemeData.getEntryStateUId())) {

			EntryState entryState = new EntryState();

			entryState.setChangeType(ChangeType.NEW);
			entryState.setRelativeOrder(0L);

			versionsDao.insertEntryState(
					codingSchemeUId,
					codingSchemeData.getEntryStateUId(),
					codingSchemeData.getUId(),
					EntryStateType.CODINGSCHEME, 
					null,
					entryState);
		}
		
		return codingSchemeData.getEntryStateUId();
	}


	@Override
	public List<SupportedProperty> getPropertyUriMapForPropertyType(
			String codingSchemeId, PropertyTypes propertyType) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return (List<SupportedProperty>) this.getSqlSessionTemplate().<SupportedProperty>selectList(	
				GET_PROPERTY_URIMAP_FOR_PROPERTYTYPE_SQL, 
				new PrefixedParameterTriple(prefix, codingSchemeId, SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTY, propertyType.name()));
	}

	@Override
	public void updateEntryStateUId(String codingSchemeUId, String entryStateUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		this.getSqlSessionTemplate().update(
				UPDATE_CODING_SCHEME_ENTRYSTATE_UID, 
				new PrefixedParameterTuple(prefix, codingSchemeUId, entryStateUId));
		
		this.getSqlSessionTemplate().update(
				UPDATE_CS_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
				new PrefixedParameterTriple(prefix, codingSchemeUId,
						SQLTableConstants.TBLCOLVAL_SOURCE,
						entryStateUId));
		
		this.getSqlSessionTemplate().update(
				UPDATE_CS_MULTI_ATTRIB_ENTRYSTATE_UID_BY_ID_AND_TYPE_SQL,
				new PrefixedParameterTriple(prefix, codingSchemeUId,
						SQLTableConstants.TBLCOLVAL_LOCALNAME,
						entryStateUId));
	}

	@Override
	public String getLatestRevision(String codingSchemeUId) {
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		return (String) this.getSqlSessionTemplate().selectOne(
				GET_CODING_SCHEME_LATEST_REVISION_ID_BY_UID, 
				new PrefixedParameter(prefix, codingSchemeUId));
	}
	
	@Override
	public String getRevisionWhenNew(String codingSchemeUId) {
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUId);
		
		return (String) this.getSqlSessionTemplate().selectOne(
				GET_CODING_SCHEME_REVISION_ID_WHEN_NEW_BY_UID, 
				new PrefixedParameter(prefix, codingSchemeUId));
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

	@Override
	public List<String> getAllCodingSchemeRevisions(String csUId) {

		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(
				csUId);

		List<String> revisionList = this.getSqlSessionTemplate()
				.selectList(GET_ALL_CODING_SCHEME_REVISIONS_SQL,
						new PrefixedParameter(prefix, csUId));

		return revisionList;
	}

	
}
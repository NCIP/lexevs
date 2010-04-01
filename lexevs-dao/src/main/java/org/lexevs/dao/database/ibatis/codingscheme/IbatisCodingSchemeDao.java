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
package org.lexevs.dao.database.ibatis.codingscheme;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.URIMap;
import org.LexGrid.relations.Relations;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.constants.classifier.mapping.ClassToStringMappingClassifier;
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

/**
 * The Class IbatisCodingSchemeDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Cacheable(cacheName = "IbatisCodingSchemeDao")
public class IbatisCodingSchemeDao extends AbstractIbatisDao implements CodingSchemeDao {
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");

	/** The SUPPORTE d_ attri b_ gette r_ prefix. */
	private static String SUPPORTED_ATTRIB_GETTER_PREFIX = "_supported";
	
	/** The CODIN g_ schem e_ namespace. */
	public static String CODING_SCHEME_NAMESPACE = "CodingScheme.";
	
	/** The REMOV e_ codin g_ schem e_ b y_ i d_ sql. */
	private static String REMOVE_CODING_SCHEME_BY_ID_SQL = CODING_SCHEME_NAMESPACE + "deleteCodingSchemeById";
	
	/** The INSER t_ codin g_ schem e_ sql. */
	private static String INSERT_CODING_SCHEME_SQL = CODING_SCHEME_NAMESPACE + "insertCodingScheme";
	
	/** The GE t_ codin g_ schem e_ b y_ i d_ sql. */
	private static String GET_CODING_SCHEME_BY_ID_SQL = CODING_SCHEME_NAMESPACE + "getCodingSchemeById";
	
	/** The GE t_ codin g_ schem e_ summar y_ b y_ ur i_ an d_ versio n_ sql. */
	private static String GET_CODING_SCHEME_SUMMARY_BY_URI_AND_VERSION_SQL = CODING_SCHEME_NAMESPACE + "getCodingSchemeSummaryByUriAndVersion";
	
	/** The GE t_ codin g_ schem e_ i d_ b y_ nam e_ an d_ versio n_ sql. */
	private static String GET_CODING_SCHEME_ID_BY_NAME_AND_VERSION_SQL = CODING_SCHEME_NAMESPACE + "getCodingSchemeIdByNameAndVersion";
	
	/** The GE t_ codin g_ schem e_ i d_ b y_ ur i_ an d_ versio n_ sql. */
	private static String GET_CODING_SCHEME_ID_BY_URI_AND_VERSION_SQL = CODING_SCHEME_NAMESPACE + "getCodingSchemeIdByUriAndVersion";
	
	/** The GE t_ codin g_ schem e_ sourc e_ lis t_ sql. */
	private static String GET_CODING_SCHEME_SOURCE_LIST_SQL = CODING_SCHEME_NAMESPACE + "getSourceListByCodingSchemeId";
	
	/** The GE t_ codin g_ schem e_ localnam e_ lis t_ sql. */
	private static String GET_CODING_SCHEME_LOCALNAME_LIST_SQL = CODING_SCHEME_NAMESPACE + "getLocalNameListByCodingSchemeId";
	
	/** The UPDAT e_ codin g_ schem e_ b y_ i d_ sql. */
	private static String UPDATE_CODING_SCHEME_BY_ID_SQL = CODING_SCHEME_NAMESPACE + "updateCodingSchemeById";
	
	/** The INSER t_ codin g_ schem e_ multiattri b_ sql. */
	private static String INSERT_CODING_SCHEME_MULTIATTRIB_SQL = CODING_SCHEME_NAMESPACE + "insertCodingSchemeMultiAttrib";
	
	/** The INSER t_ urima p_ sql. */
	private static String INSERT_URIMAP_SQL = CODING_SCHEME_NAMESPACE + "insertURIMap";
	
	/** The GE t_ distinc t_ propert y_ name s_ o f_ c s_ sql. */
	private static String GET_DISTINCT_PROPERTY_NAMES_OF_CS_SQL = CODING_SCHEME_NAMESPACE + "getDistinctPropertyNames";
	
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
	
	private static String GET_CODING_SCHEME_BY_ID_AND_REVISION_GUID_SQL = CODING_SCHEME_NAMESPACE + "getCodingSchemeByIdAndRevisionId";
	
	/** The class to string mapping classifier. */
	private ClassToStringMappingClassifier classToStringMappingClassifier = new ClassToStringMappingClassifier();
	
	/** The versions dao. */
	private VersionsDao versionsDao;
	
	/** The entity dao. */
	private EntityDao entityDao;
	
	private AssociationDao associationDao;

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeById(java.lang.String)
	 */
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
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeByUriAndVersion(java.lang.String, java.lang.String)
	 */
	public CodingScheme getCodingSchemeByUriAndVersion(String codingSchemeUri,
			String version) {
		String codingSchemeId = this.getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
		return this.getCodingSchemeById(codingSchemeId);
	}


	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeByNameAndVersion(java.lang.String, java.lang.String)
	 */
	public CodingScheme getCodingSchemeByNameAndVersion(String codingSchemeName, String representsVersion){
		String codingSchemeId = this.getCodingSchemeIdByNameAndVersion(codingSchemeName, representsVersion);
		return this.getCodingSchemeById(codingSchemeId);
	}

	/**
	 * Delete local name.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param version the version
	 * @param localName the local name
	 */
	public void deleteLocalName(String codingSchemeName, String version,
			String localName) {
		throw new UnsupportedOperationException();	
	}

	/**
	 * Delete source.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param version the version
	 * @param source the source
	 */
	public void deleteSource(String codingSchemeName, String version,
			Source source) {
		throw new UnsupportedOperationException();	
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeByRevision(java.lang.String, java.lang.String, java.lang.String)
	 */
	public CodingScheme getHistoryCodingSchemeByRevision(String codingSchemeId, String revisionId) {
		String prefix = this.getPrefixResolver().resolveHistoryPrefix();
		return (CodingScheme)
			this.getSqlMapClientTemplate().queryForObject(GET_CODING_SCHEME_BY_ID_AND_REVISION_GUID_SQL, 
				new PrefixedParameterTuple(prefix, codingSchemeId, revisionId));
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeSummaryByUriAndVersion(java.lang.String, java.lang.String)
	 */
	public CodingSchemeSummary getCodingSchemeSummaryByUriAndVersion(
			String codingSchemeUri, String version) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUri, version);
		return (CodingSchemeSummary)
			this.getSqlMapClientTemplate().queryForObject(GET_CODING_SCHEME_SUMMARY_BY_URI_AND_VERSION_SQL, 
				new PrefixedParameterTuple(prefix, codingSchemeUri, version));
	}


	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertHistoryCodingScheme(java.lang.String, org.LexGrid.codingSchemes.CodingScheme)
	 */
	public void insertHistoryCodingScheme(
			String codingSchemeId, 
			CodingScheme codingScheme) {
		String prefix = this.getPrefixResolver().resolveHistoryPrefix();

		this.doInsertCodingScheme(
				codingSchemeId, 
				prefix, 
				codingScheme,
				true);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#deleteCodingSchemeById(java.lang.String)
	 */
	@ClearCache
	public void deleteCodingSchemeById(String codingSchemeId) {
		String prefix = 
			this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		this.getSqlMapClientTemplate().
			delete(REMOVE_CODING_SCHEME_BY_ID_SQL, new PrefixedParameter(prefix, codingSchemeId));	
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertCodingScheme(org.LexGrid.codingSchemes.CodingScheme)
	 */
	public String insertCodingScheme(
			CodingScheme codingScheme,
			boolean cascade) {
		String codingSchemeId = this.createUniqueId();
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return this.doInsertCodingScheme(
				codingSchemeId, 
				prefix, 
				codingScheme,
				cascade);
	}
	
	/**
	 * Do insert coding scheme.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param prefix the prefix
	 * @param codingScheme the coding scheme
	 * 
	 * @return the string
	 */
	protected String doInsertCodingScheme(
			String codingSchemeId, 
			String prefix, 
			CodingScheme codingScheme,
			boolean cascade) {
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
				entryStateId, codingSchemeId, "CodingScheme", previousRevisionId, codingScheme.getEntryState());
		
		for(Source source : codingScheme.getSource()){
			String sourceId = this.createUniqueId();
			this.doInsertCodingSchemeSource(prefix, codingSchemeId, sourceId, entryStateId, source);
		}
		
		for(String localName : codingScheme.getLocalName()){
			String localNameId = this.createUniqueId();
			this.doInsertCodingSchemeLocalName(prefix, codingSchemeId, localNameId, entryStateId, localName);
		}
		
		this.insertMappings(codingSchemeId, codingScheme.getMappings());
		
		if(cascade) {
			if(codingScheme.getEntities() != null) {
				this.entityDao.insertBatchEntities(
						codingSchemeId, 
						Arrays.asList(codingScheme.getEntities().getEntity()), 
						cascade);
			}
			
			for(Relations relations : codingScheme.getRelations()) {
				this.associationDao.
					insertRelations(
							codingSchemeId, 
							relations,
							cascade);
			}
		}
		return codingSchemeId;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#updateCodingScheme(java.lang.String, org.LexGrid.codingSchemes.CodingScheme)
	 */
	public void updateCodingScheme(String codingSchemeId, CodingScheme codingScheme) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		InsertOrUpdateCodingSchemeBean bean = new InsertOrUpdateCodingSchemeBean();
		bean.setPrefix(prefix);
		bean.setCodingScheme(codingScheme);
		bean.setId(codingSchemeId);
		
		this.getSqlMapClientTemplate().update(UPDATE_CODING_SCHEME_BY_ID_SQL, bean);
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
	public String getCodingSchemeIdByNameAndVersion(String codingSchemeName, String version) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeName, version);
		return (String) this.getSqlMapClientTemplate().queryForObject(GET_CODING_SCHEME_ID_BY_NAME_AND_VERSION_SQL,
				new PrefixedParameterTuple(prefix, codingSchemeName, version));
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeIdByUriAndVersion(java.lang.String, java.lang.String)
	 */
	@CacheMethod
	public String getCodingSchemeIdByUriAndVersion(String codingSchemeUri, String version) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUri, version);
		return (String) this.getSqlMapClientTemplate().queryForObject(GET_CODING_SCHEME_ID_BY_URI_AND_VERSION_SQL,
				new PrefixedParameterTuple(prefix, codingSchemeUri, version));
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getEntryStateId(java.lang.String, java.lang.String)
	 */
	public String getEntryStateId(String codingSchemeName, String version) {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertCodingSchemeSource(java.lang.String, org.LexGrid.commonTypes.Source)
	 */
	public void insertCodingSchemeSource(String codingSchemeId, Source source) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		String sourceId = this.createUniqueId();
		this.doInsertCodingSchemeSource(prefix, codingSchemeId, sourceId, null, source);
		
	}
	
	protected void doInsertCodingSchemeSource(String prefix, String codingSchemeId, String sourceId, String entryStateId, Source source) {
		this.getSqlMapClientTemplate().insert(INSERT_CODING_SCHEME_MULTIATTRIB_SQL,
				this.buildInsertSourceBean(prefix, sourceId, codingSchemeId, entryStateId, source));
	}
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertCodingSchemeLocalName(java.lang.String, java.lang.String)
	 */
	public void insertCodingSchemeLocalName(String codingSchemeId,
			String localName) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		String localNameId = this.createUniqueId();
		this.doInsertCodingSchemeLocalName(prefix, codingSchemeId, localNameId, null, localName);	
	}
	
	protected void doInsertCodingSchemeLocalName(String prefix, String codingSchemeId, String localNameId, String entryStateId, String localName) {
		this.getSqlMapClientTemplate().insert(INSERT_CODING_SCHEME_MULTIATTRIB_SQL,
				this.buildInsertLocalNameBean(prefix, localNameId, codingSchemeId, entryStateId, localName));
	}
	
	/**
	 * Insert uri map.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param codingSchemeVersion the coding scheme version
	 * @param uriMap the uri map
	 */
	public void insertURIMap(String codingSchemeName,
			String codingSchemeVersion, URIMap uriMap) {
		String codingSchemeId = this.getCodingSchemeIdByNameAndVersion(codingSchemeName, codingSchemeVersion);
		this.insertURIMap(codingSchemeId, uriMap);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertURIMap(java.lang.String, org.LexGrid.naming.URIMap)
	 */
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
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertURIMap(java.lang.String, java.util.List)
	 */
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
	
	/**
	 * Insert mappings.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param codingSchemeVersion the coding scheme version
	 * @param mappings the mappings
	 */
	public void insertMappings(String codingSchemeName,
			String codingSchemeVersion, Mappings mappings) {
		String codingSchemeId = this.getCodingSchemeIdByNameAndVersion(codingSchemeName, codingSchemeVersion);
		this.insertMappings(codingSchemeId, mappings);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertMappings(java.lang.String, org.LexGrid.naming.Mappings)
	 */
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
	
	/**
	 * Do distinct query.
	 * 
	 * @param queryName the query name
	 * @param codingSchemeId the coding scheme id
	 * 
	 * @return the list< string>
	 */
	@SuppressWarnings("unchecked")
	protected List<String> doDistinctQuery(String queryName, String codingSchemeId){
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return this.getSqlMapClientTemplate().queryForList(
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
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getDistinctEntityTypesOfCodingScheme(
			String codingSchemeId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return this.getSqlMapClientTemplate().queryForList(
				GET_DISTINCT_ENTITY_TYPES_OF_CS_SQL, 
				new PrefixedParameter(prefix, codingSchemeId));
	}

	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getMappings(java.lang.String)
	 */
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
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getUriMap(java.lang.String, java.lang.String, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T extends URIMap> T getUriMap(String codingSchemeId, String localId, Class<T> uriMap) {	
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		return (T) this.getSqlMapClientTemplate().queryForList(	
				GET_URIMAP_BY_LOCALNAME_AND_TYPE_SQL, 
				new PrefixedParameterTriple(prefix, codingSchemeId, localId, this.classToStringMappingClassifier.classify(uriMap)));
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#validateSupportedAttribute(java.lang.String, java.lang.String, java.lang.Class)
	 */
	public <T extends URIMap> boolean validateSupportedAttribute(String codingSchemeId, String localId, Class<T> uriMap) {	
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		String classifiedName = this.classToStringMappingClassifier.classify(uriMap);
		int count = (Integer) this.getSqlMapClientTemplate().queryForObject(	
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
	protected InsertURIMapBean buildInsertURIMapBean(String prefix, String uriMapId, String codingSchemeId, String supportedAttributeTag, URIMap uriMap){
		InsertURIMapBean bean = new InsertURIMapBean();
		bean.setPrefix(prefix);
		bean.setSupportedAttributeTag(supportedAttributeTag);
		bean.setCodingSchemeId(codingSchemeId);
		bean.setUriMap(uriMap);
		bean.setId(uriMapId);
		
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
	protected InsertCodingSchemeMultiAttribBean buildInsertSourceBean(String prefix, String sourceId, String codingSchemeId, String entryStateId, Source source){
		InsertCodingSchemeMultiAttribBean bean = new InsertCodingSchemeMultiAttribBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeId(codingSchemeId);
		bean.setId(sourceId);
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_SOURCE);
		bean.setAttributeValue(source.getContent());
		bean.setSubRef(source.getSubRef());
		bean.setRole(source.getRole());
		bean.setEntryStateId(entryStateId);
		
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
	protected InsertCodingSchemeMultiAttribBean buildInsertLocalNameBean(String prefix, String localNameId, String codingSchemeId, String entryStateId, String localName){
		InsertCodingSchemeMultiAttribBean bean = new InsertCodingSchemeMultiAttribBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeId(codingSchemeId);
		bean.setId(localNameId);
		bean.setAttributeType(SQLTableConstants.TBLCOLVAL_LOCALNAME);
		bean.setAttributeValue(localName);
		bean.setEntryStateId(entryStateId);

		return bean;
	}
	
	/**
	 * Builds the insert coding scheme bean.
	 * 
	 * @param prefix the prefix
	 * @param codingSchemeId the coding scheme id
	 * @param entryStateId the entry state id
	 * @param codingScheme the coding scheme
	 * 
	 * @return the insert or update coding scheme bean
	 */
	protected InsertOrUpdateCodingSchemeBean buildInsertCodingSchemeBean(String prefix, String codingSchemeId, String entryStateId, CodingScheme codingScheme){
		InsertOrUpdateCodingSchemeBean bean = new InsertOrUpdateCodingSchemeBean();
		bean.setPrefix(prefix);
		bean.setCodingScheme(codingScheme);
		bean.setId(codingSchemeId);
		bean.setEntryStateId(entryStateId);
		
		return bean;
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
}

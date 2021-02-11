
package org.lexevs.dao.database.sqlimplementedmethods.codingscheme;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedContext;
import org.LexGrid.naming.SupportedLanguage;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.SupportedPropertyQualifier;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.naming.URIMap;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.connection.SQLInterface;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.sqlimplementedmethods.AbstraceSqlImplementedMethodsDao;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.exceptions.MissingResourceException;
import org.springframework.util.StringUtils;

/**
 * The Class SQLInterfaceCodingSchemeDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SQLInterfaceCodingSchemeDao extends AbstraceSqlImplementedMethodsDao implements CodingSchemeDao {
	
	/** The supported datebase versions. */
	private LexGridSchemaVersion supportedDatebaseVersionNMinus2 = LexGridSchemaVersion.parseStringToVersion("1.7");
	private LexGridSchemaVersion supportedDatebaseVersionNMinus1 = LexGridSchemaVersion.parseStringToVersion("1.8");
	
	/**
	 * Delete coding scheme.
	 * 
	 * @param codingScheme the coding scheme
	 */
	public void deleteCodingScheme(CodingScheme codingScheme) {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeById(java.lang.String)
	 */
	public CodingScheme getCodingSchemeByUId(String codingSchemeUId) {
		AbsoluteCodingSchemeVersionReference ref = resolveCodingSchemeKey(codingSchemeUId);
		
		return this.getCodingSchemeByUriAndVersion(
				ref.getCodingSchemeURN(), 
				ref.getCodingSchemeVersion());
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeByNameAndVersion(java.lang.String, java.lang.String)
	 */
	public CodingScheme getCodingSchemeByNameAndVersion(
			String codingSchemeName, String version) {
		try {
			return this.getSqlImplementedMethodsDao().buildCodingScheme(codingSchemeName, version);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeByRevision(java.lang.String, java.lang.String, java.lang.String)
	 */
	public CodingScheme getCodingSchemeByRevision(String codingSchemeName,
			String version, String revisionId) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeByUriAndVersion(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	public CodingScheme getCodingSchemeByUriAndVersion(String codingSchemeUri,
			String version) {
		try {
			String internalCodingSchemeName = this.getResourceManager().
				getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeUri, version);
			return this.getSqlImplementedMethodsDao().buildCodingScheme(internalCodingSchemeName, version);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeIdByNameAndVersion(java.lang.String, java.lang.String)
	 */
	public String getCodingSchemeUIdByNameAndVersion(String codingSchemeName,
			String version) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeIdByUriAndVersion(java.lang.String, java.lang.String)
	 */
	public String getCodingSchemeUIdByUriAndVersion(String codingSchemeUri,
			String version) {
		return resolveCodingSchemeKey(codingSchemeUri, version);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getCodingSchemeSummaryByUriAndVersion(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	public CodingSchemeSummary getCodingSchemeSummaryByUriAndVersion(
			String codingSchemeUri, String version) {
		String codingSchemeInternalName;
		try {
			codingSchemeInternalName = this.getResourceManager().
				getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeUri, version);
		} catch (LBParameterException e2) {
			throw new RuntimeException(e2);
		}
		
		SQLInterface si;
		try {
			si = this.getResourceManager().getSQLInterface(codingSchemeInternalName, version);
		} catch (MissingResourceException e1) {
			throw new RuntimeException(e1);
		}
		PreparedStatement getCodingSchemes = null;

		try {
			getCodingSchemes = si.checkOutPreparedStatement("Select * from "
					+ si.getTableName(SQLTableConstants.CODING_SCHEME));

			ResultSet results = getCodingSchemes.executeQuery();

			if(!results.next()){
				throw new RuntimeException("No CodingScheme Found.");
			}
			
			CodingSchemeRendering csr = new CodingSchemeRendering();

			csr.setCodingSchemeSummary(new CodingSchemeSummary());
			csr.getCodingSchemeSummary().setCodingSchemeDescription(new EntityDescription());
			csr.getCodingSchemeSummary().getCodingSchemeDescription().setContent(
					results.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION));
			csr.getCodingSchemeSummary().setCodingSchemeURI(
					(si.supports2009Model() ? results
							.getString(SQLTableConstants.TBLCOL_CODINGSCHEMEURI) : results
							.getString(SQLTableConstants.TBLCOL_REGISTEREDNAME)));
			csr.getCodingSchemeSummary().setFormalName(
					results.getString(SQLTableConstants.TBLCOL_FORMALNAME));
			csr.getCodingSchemeSummary().setLocalName(
					results.getString(SQLTableConstants.TBLCOL_CODINGSCHEMENAME));
			csr.getCodingSchemeSummary().setRepresentsVersion(
					results.getString(SQLTableConstants.TBLCOL_REPRESENTSVERSION));

			return csr.getCodingSchemeSummary();
		} catch (Exception e){
			throw new RuntimeException(e);
		} finally {
			si.checkInPreparedStatement(getCodingSchemes);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getEntryStateId(java.lang.String, java.lang.String)
	 */
	public String getEntryStateId(String codingSchemeName, String version) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertCodingScheme(org.LexGrid.codingSchemes.CodingScheme)
	 */
	@Override
	public String insertCodingScheme(CodingScheme cs, String releaseUId, boolean cascade) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertCodingSchemeLocalName(java.lang.String, java.lang.String)
	 */
	public void insertCodingSchemeLocalName(String codingSchemeId,
			String localName) {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertCodingSchemeSource(java.lang.String, org.LexGrid.commonTypes.Source)
	 */
	public void insertCodingSchemeSource(String codingSchemeId, Source source) {
		throw new UnsupportedOperationException();
		
	}

	/**
	 * Insert history coding scheme.
	 * 
	 * @param codingScheme the coding scheme
	 * 
	 * @return the string
	 */
	public String insertHistoryCodingScheme(CodingScheme codingScheme) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertMappings(java.lang.String, org.LexGrid.naming.Mappings)
	 */
	public void insertMappings(String codingSchemeId, Mappings mappings) {
		throw new UnsupportedOperationException();
		
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
		throw new UnsupportedOperationException();
		
	}

	/**
	 * Insert uri map.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param codingSchemeVersion the coding scheme version
	 * @param supportedProperty the supported property
	 */
	public void insertURIMap(String codingSchemeName,
			String codingSchemeVersion, URIMap supportedProperty) {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertURIMap(java.lang.String, java.util.List)
	 */
	public void insertURIMap(String codingSchemeId,
			List<URIMap> supportedProperty) {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertURIMap(java.lang.String, org.LexGrid.naming.URIMap)
	 */
	public void insertURIMap(String codingSchemeId, URIMap supportedProperty) {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#updateCodingScheme(java.lang.String, org.LexGrid.codingSchemes.CodingScheme)
	 */
	public String updateCodingScheme(String codingSchemeId,
			CodingScheme codingScheme) {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.AbstractBaseDao#doGetSupportedLgSchemaVersions()
	 */
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		List<LexGridSchemaVersion> supportedDBVersions = new ArrayList<LexGridSchemaVersion>();
		supportedDBVersions.add(supportedDatebaseVersionNMinus2);
		supportedDBVersions.add(supportedDatebaseVersionNMinus1);
		return supportedDBVersions;
//		return DaoUtility.createNonTypedList(supportedDatebaseVersion);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#deleteCodingSchemeById(java.lang.String)
	 */
	public void deleteCodingSchemeByUId(String codingSchemeId) {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao#executeInTransaction(org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao.IndividualDaoCallback)
	 */
	public <T> T executeInTransaction(IndividualDaoCallback<T> callback) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Resolve coding scheme key.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @return the string
	 */
	public static String resolveCodingSchemeKey(String uri, String version) {
		return uri + KEY_SEPERATOR + version;
	}
	
	/**
	 * Resolve coding scheme key.
	 * 
	 * @param key the key
	 * 
	 * @return the absolute coding scheme version reference
	 */
	public static AbsoluteCodingSchemeVersionReference resolveCodingSchemeKey(String key) {
		String[] keys = StringUtils.split(key, KEY_SEPERATOR);
		return DaoUtility.createAbsoluteCodingSchemeVersionReference(keys[0], keys[1]);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getDistinctEntityTypesOfCodingScheme(java.lang.String)
	 */
	@Override
	public List<String> getDistinctEntityTypesOfCodingScheme(
			String codingSchemeId) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getDistinctFormatsOfCodingScheme(java.lang.String)
	 */
	@Override
	public List<String> getDistinctFormatsOfCodingScheme(String codingSchemeId) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getDistinctLanguagesOfCodingScheme(java.lang.String)
	 */
	@Override
	public List<String> getDistinctLanguagesOfCodingScheme(String codingSchemeId) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getDistinctNamespacesOfCodingScheme(java.lang.String)
	 */
	@Override
	public List<String> getDistinctNamespacesOfCodingScheme(
			String codingSchemeId) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getDistinctPropertyNamesOfCodingScheme(java.lang.String)
	 */
	@Override
	public List<String> getDistinctPropertyNamesOfCodingScheme(
			String codingSchemeId) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getDistinctPropertyQualifierNamesOfCodingScheme(java.lang.String)
	 */
	@Override
	public List<String> getDistinctPropertyQualifierNamesOfCodingScheme(
			String codingSchemeId) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getDistinctPropertyQualifierTypesOfCodingScheme(java.lang.String)
	 */
	@Override
	public List<String> getDistinctPropertyQualifierTypesOfCodingScheme(
			String codingSchemeId) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getMappings(java.lang.String)
	 */
	@Override
	public Mappings getMappings(String codingSchemeId) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#getUriMap(java.lang.String, java.lang.String, java.lang.Class)
	 */
	@Override
	public <T extends URIMap> T getUriMap(String codingSchemeId,
			String localId, Class<T> uriMap) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#validateSupportedAttribute(java.lang.String, java.lang.String, java.lang.Class)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public <T extends URIMap> boolean validateSupportedAttribute(
			String codingSchemeId, String localId, Class<T> uriMap) {
		AbsoluteCodingSchemeVersionReference reference = 
			SQLInterfaceCodingSchemeDao.resolveCodingSchemeKey(codingSchemeId);

		try {
			String internalCodingSchemeName = this.getResourceManager().
			getInternalCodingSchemeNameForUserCodingSchemeName(
					reference.getCodingSchemeURN(), 
					reference.getCodingSchemeVersion());

			if(uriMap.equals(SupportedSource.class)) {
				return this.getSqlImplementedMethodsDao().validateSource(
						internalCodingSchemeName, 
						reference.getCodingSchemeVersion(), 
						localId);
			} else if(uriMap.equals(SupportedLanguage.class)) {
				return this.getSqlImplementedMethodsDao().validateLanguage(
						internalCodingSchemeName, 
						reference.getCodingSchemeVersion(), 
						localId);
			} else if(uriMap.equals(SupportedProperty.class)) {
				return this.getSqlImplementedMethodsDao().validateProperty(
						internalCodingSchemeName, 
						reference.getCodingSchemeVersion(), 
						localId);
			} else if(uriMap.equals(SupportedContext.class)) {
				return this.getSqlImplementedMethodsDao().validateContext(
						internalCodingSchemeName, 
						reference.getCodingSchemeVersion(), 
						localId);
			} else if(uriMap.equals(SupportedPropertyQualifier.class)) {
				return this.getSqlImplementedMethodsDao().validatePropertyQualifier(
						internalCodingSchemeName, 
						reference.getCodingSchemeVersion(), 
						localId);
			} else {
				throw new RuntimeException(uriMap + " cannot be mapped to a URIMap.");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codingscheme.CodingSchemeDao#insertHistoryCodingScheme(java.lang.String, org.LexGrid.codingSchemes.CodingScheme)
	 */
/*	@Override
	public void insertHistoryCodingScheme(String codingSchemeUId, String releaseUId, String entryStateUId,
			CodingScheme codingScheme) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}*/

	@Override
	public CodingScheme getHistoryCodingSchemeByRevision(String codingSchemeId,
			String revisionId) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}
	@Override
	public void insertCodingSchemeDependentChanges(String codingSchemeId,
			CodingScheme codingScheme) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void insertOrUpdateCodingSchemeSource(String codingSchemeId, Source source) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public void insertOrUpdateURIMap(String codingSchemeId,
			URIMap supportedProperty) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteCodingSchemeLocalNames(String codingSchemeId) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteCodingSchemeMappings(String codingSchemeId) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteCodingSchemeSources(String codingSchemeId) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public String insertHistoryCodingScheme(String codingSchemeUId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String updateCodingSchemeVersionableAttrib(String codingSchemeId,
			CodingScheme codingScheme) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<SupportedProperty> getPropertyUriMapForPropertyType(
			String codingSchemeId, PropertyTypes propertyType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getEntryStateUId(String codingSchemeUId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateEntryStateUId(String codingSchemeUId, String entryStateUId) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public String getLatestRevision(String codingSchemeUId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getAllCodingSchemeRevisions(String csUId) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public List<NameAndValue> getDistinctPropertyNameAndType(
			String codingSchemeId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRevisionWhenNew(String codingSchemeUId) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}
}
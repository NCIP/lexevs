package org.lexevs.dao.database.sqlimplementedmethods.codingscheme;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.URIMap;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.connection.SQLInterface;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.sqlimplementedmethods.AbstraceSqlImplementedMethodsDao;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.exceptions.MissingResourceException;
import org.springframework.util.StringUtils;

public class SQLInterfaceCodingSchemeDao extends AbstraceSqlImplementedMethodsDao implements CodingSchemeDao {
	
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("1.8");
	
	public void deleteCodingScheme(CodingScheme codingScheme) {
		throw new UnsupportedOperationException();
		
	}

	public CodingScheme getCodingSchemeById(String codingSchemeId) {
		throw new UnsupportedOperationException();
	}

	public CodingScheme getCodingSchemeByNameAndVersion(
			String codingSchemeName, String version) {
		throw new UnsupportedOperationException();
	}

	public CodingScheme getCodingSchemeByRevision(String codingSchemeName,
			String version, String revisionId) {
		throw new UnsupportedOperationException();
	}

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

	public String getCodingSchemeIdByNameAndVersion(String codingSchemeName,
			String version) {
		throw new UnsupportedOperationException();
	}

	public String getCodingSchemeIdByUriAndVersion(String codingSchemeUri,
			String version) {
		return resolveCodingSchemeKey(codingSchemeUri, version);
	}

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

	public String getEntryStateId(String codingSchemeName, String version) {
		throw new UnsupportedOperationException();
	}

	public String insertCodingScheme(CodingScheme cs) {
		throw new UnsupportedOperationException();
	}

	public void insertCodingSchemeLocalName(String codingSchemeId,
			String localName) {
		throw new UnsupportedOperationException();
		
	}

	public void insertCodingSchemeSource(String codingSchemeId, Source source) {
		throw new UnsupportedOperationException();
		
	}

	public String insertHistoryCodingScheme(CodingScheme codingScheme) {
		throw new UnsupportedOperationException();
	}

	public void insertMappings(String codingSchemeId, Mappings mappings) {
		throw new UnsupportedOperationException();
		
	}

	public void insertMappings(String codingSchemeName,
			String codingSchemeVersion, Mappings mappings) {
		throw new UnsupportedOperationException();
		
	}

	public void insertURIMap(String codingSchemeName,
			String codingSchemeVersion, URIMap supportedProperty) {
		throw new UnsupportedOperationException();
		
	}

	public void insertURIMap(String codingSchemeId,
			List<URIMap> supportedProperty) {
		throw new UnsupportedOperationException();
		
	}

	public void insertURIMap(String codingSchemeId, URIMap supportedProperty) {
		throw new UnsupportedOperationException();
		
	}

	public void updateCodingScheme(String codingSchemeId,
			CodingScheme codingScheme) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
	}

	public void deleteCodingSchemeById(String codingSchemeId) {
		throw new UnsupportedOperationException();
		
	}

	public <T> T executeInTransaction(IndividualDaoCallback<T> callback) {
		throw new UnsupportedOperationException();
	}
	
	public static String resolveCodingSchemeKey(String uri, String version) {
		return uri + KEY_SEPERATOR + version;
	}
	
	public static AbsoluteCodingSchemeVersionReference resolveCodingSchemeKey(String key) {
		String[] keys = StringUtils.split(key, KEY_SEPERATOR);
		return DaoUtility.createAbsoluteCodingSchemeVersionReference(keys[0], keys[1]);
	}

	@Override
	public List<String> getDistinctEntityTypesOfCodingScheme(
			String codingSchemeId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getDistinctFormatsOfCodingScheme(String codingSchemeId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getDistinctLanguagesOfCodingScheme(String codingSchemeId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getDistinctNamespacesOfCodingScheme(
			String codingSchemeId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getDistinctPropertyNamesOfCodingScheme(
			String codingSchemeId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getDistinctPropertyQualifierNamesOfCodingScheme(
			String codingSchemeId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getDistinctPropertyQualifierTypesOfCodingScheme(
			String codingSchemeId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Mappings getMappings(String codingSchemeId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends URIMap> T getUriMap(String codingSchemeId,
			String localId, Class<T> uriMap) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends URIMap> boolean validateSupportedAttribute(
			String codingSchemeId, String localId, Class<T> uriMap) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public void insertHistoryCodingScheme(String codingSchemeId,
			CodingScheme codingScheme) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}
}

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
		// TODO Auto-generated method stub
		
	}

	public CodingScheme getCodingSchemeById(String codingSchemeId) {
		// TODO Auto-generated method stub
		return null;
	}

	public CodingScheme getCodingSchemeByNameAndVersion(
			String codingSchemeName, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	public CodingScheme getCodingSchemeByRevision(String codingSchemeName,
			String version, String revisionId) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	public String getCodingSchemeIdByUriAndVersion(String codingSchemeUri,
			String version) {
		return this.resolveCodingSchemeKey(codingSchemeUri, version);
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
		// TODO Auto-generated method stub
		return null;
	}

	public String insertCodingScheme(CodingScheme cs) {
		// TODO Auto-generated method stub
		return null;
	}

	public void insertCodingSchemeLocalName(String codingSchemeId,
			String localName) {
		// TODO Auto-generated method stub
		
	}

	public void insertCodingSchemeSource(String codingSchemeId, Source source) {
		// TODO Auto-generated method stub
		
	}

	public String insertHistoryCodingScheme(CodingScheme codingScheme) {
		// TODO Auto-generated method stub
		return null;
	}

	public void insertMappings(String codingSchemeId, Mappings mappings) {
		// TODO Auto-generated method stub
		
	}

	public void insertMappings(String codingSchemeName,
			String codingSchemeVersion, Mappings mappings) {
		// TODO Auto-generated method stub
		
	}

	public void insertURIMap(String codingSchemeName,
			String codingSchemeVersion, URIMap supportedProperty) {
		// TODO Auto-generated method stub
		
	}

	public void insertURIMap(String codingSchemeId,
			List<URIMap> supportedProperty) {
		// TODO Auto-generated method stub
		
	}

	public void insertURIMap(String codingSchemeId, URIMap supportedProperty) {
		// TODO Auto-generated method stub
		
	}

	public void updateCodingScheme(String codingSchemeId,
			CodingScheme codingScheme) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
	}

	public void deleteCodingSchemeById(String codingSchemeId) {
		// TODO Auto-generated method stub
		
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
}

package org.lexevs.dao.database.access.codingscheme;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.URIMap;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

public interface CodingSchemeDao extends LexGridSchemaVersionAwareDao {
	
	public CodingScheme getCodingSchemeById(String codingSchemeId);

	public String insertCodingScheme(CodingScheme cs);
	
	public String insertHistoryCodingScheme(CodingScheme codingScheme);
	
	public CodingScheme getCodingSchemeByNameAndVersion(String codingSchemeName, String version);
	
	public CodingScheme getCodingSchemeByUriAndVersion(String codingSchemeUri, String version);
	
	public CodingSchemeSummary getCodingSchemeSummaryByUriAndVersion(String codingSchemeUri, String version);
	
	public CodingScheme getCodingSchemeByRevision(String codingSchemeName, String version, String revisionId);
	
	public void updateCodingScheme(String codingSchemeId, CodingScheme codingScheme);
	
	public String getCodingSchemeIdByNameAndVersion(String codingSchemeName, String version);
	
	public String getCodingSchemeIdByUriAndVersion(String codingSchemeUri, String version);
	
	public String getEntryStateId(String codingSchemeName, String version);
	
	public void deleteCodingSchemeById(String codingSchemeId);
	
	public void insertCodingSchemeSource(String codingSchemeId, Source source);
	
	public <T extends URIMap> T getUriMap(String codingSchemeId, String localId, Class<T> uriMap);
	
	public <T extends URIMap> boolean validateSupportedAttribute(String codingSchemeId, String localId, Class<T> uriMap);
	
	public void insertCodingSchemeLocalName(String codingSchemeId, String localName);
	
	public void insertMappings(String codingSchemeId, Mappings mappings);
	
	public Mappings getMappings(String codingSchemeId);
	
	public void insertURIMap(String codingSchemeId, List<URIMap> supportedProperty);

	public void insertURIMap(String codingSchemeId, URIMap supportedProperty);
	
	public List<String> getDistinctPropertyNamesOfCodingScheme(
			String codingSchemeId);
	
	public List<String> getDistinctFormatsOfCodingScheme(
			String codingSchemeId);
	
	public List<String> getDistinctPropertyQualifierNamesOfCodingScheme(
			String codingSchemeId);
	
	public List<String> getDistinctPropertyQualifierTypesOfCodingScheme(
			String codingSchemeId);

	public List<String> getDistinctNamespacesOfCodingScheme(String codingSchemeId);
	
	public List<String> getDistinctEntityTypesOfCodingScheme(String codingSchemeId);
	
	public List<String> getDistinctLanguagesOfCodingScheme(String codingSchemeId);
}

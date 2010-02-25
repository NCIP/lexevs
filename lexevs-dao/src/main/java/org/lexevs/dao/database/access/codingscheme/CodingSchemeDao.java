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
	
	public void updateCodingScheme(String codingSchemeName, String version, CodingScheme codingScheme);
	
	public String getCodingSchemeIdByNameAndVersion(String codingSchemeName, String version);
	
	public String getCodingSchemeIdByUriAndVersion(String codingSchemeUri, String version);
	
	public String getEntryStateId(String codingSchemeName, String version);
	
	public void deleteCodingScheme(CodingScheme codingScheme);
	
	public void insertCodingSchemeSource(String codingSchemeId, Source source);
	
	public void insertCodingSchemeLocalName(String codingSchemeId, String localName);
	
	public void insertMappings(String codingSchemeId, Mappings mappings);
	
	public void insertMappings(String codingSchemeName, String codingSchemeVersion, Mappings mappings);
	
	public void insertURIMap(String codingSchemeName, String codingSchemeVersion, URIMap supportedProperty);
	
	public void insertURIMap(String codingSchemeId, List<URIMap> supportedProperty);

	public void insertURIMap(String codingSchemeId, URIMap supportedProperty);

}

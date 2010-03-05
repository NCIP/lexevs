package org.lexevs.dao.database.service.codingscheme;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.URIMap;
import org.LexGrid.versions.EntryState;
import org.lexevs.dao.database.service.DatabaseService;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

public interface CodingSchemeService extends DatabaseService {

	public CodingScheme getCodingSchemeByUriAndVersion(
			String codingSchemeUri, String codingSchemeVersion);
	
	public CodingSchemeSummary getCodingSchemeSummaryByUriAndVersion(
			String codingSchemeUri, String codingSchemeVersion);
	
	public void destroyCodingScheme(
			String codingSchemeUri, String codingSchemeVersion);
	
	public void insertCodingScheme(
			CodingScheme scheme) throws CodingSchemeAlreadyLoadedException;
	
	public void updateCodingScheme(
			String codingSchemeUri, 
			String codingSchemeVersion,
			CodingScheme codingScheme);
	
	public void insertURIMap(
			String codingSchemeUri, 
			String codingSchemeVersion,
			URIMap uriMap);
	
	
	public void updateCodingSchemeEntryState( 
			CodingScheme codingScheme,
			EntryState entryState);
	
	public <T extends URIMap> boolean
		 validatedSupportedAttribute(String codingSchemeUri, String codingSchemeVersion, String localId, Class<T> attributeClass);

}

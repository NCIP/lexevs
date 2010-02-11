package org.lexevs.dao.database.service.codingscheme;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.URIMap;
import org.LexGrid.versions.EntryState;
import org.lexevs.dao.database.service.DatabaseService;

public interface CodingSchemeService extends DatabaseService {

	public void insertCodingScheme(
			CodingScheme scheme);
	
	public void updateCodingScheme(
			String codingSchemeName, 
			String codingSchemeVersion,
			CodingScheme codingScheme);
	
	public void insertURIMap(
			String codingSchemeName, 
			String codingSchemeVersion,
			URIMap uriMap);
	
	public void insertURIMap(
			String codingSchemeId, 
			URIMap uriMap);
	
	public void updateCodingSchemeEntryState( 
			CodingScheme codingScheme,
			EntryState entryState);

	public CodingScheme getCodingSchemeById(String codingSchemeId);
	
	public String getCodingSchemeId(String codingSchemeUri, String codingSchemeVersion);
}

package org.lexevs.dao.database.service.codingscheme;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.URIMap;
import org.LexGrid.versions.EntryState;

public interface CodingSchemeService {

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
}

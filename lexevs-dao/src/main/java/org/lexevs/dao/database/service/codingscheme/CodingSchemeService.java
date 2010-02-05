package org.lexevs.dao.database.service.codingscheme;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.LexGrid.versions.EntryState;

public interface CodingSchemeService {

	public void insertCodingScheme(
			CodingScheme scheme);
	
	public void updateCodingScheme(
			String codingSchemeName, 
			String codingSchemeVersion,
			CodingScheme codingScheme);
	
	public void updateCodingSchemeEntryState( 
			CodingScheme codingScheme,
			EntryState entryState);

}

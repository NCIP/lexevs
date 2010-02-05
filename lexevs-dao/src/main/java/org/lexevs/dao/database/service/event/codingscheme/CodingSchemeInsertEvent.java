package org.lexevs.dao.database.service.event.codingscheme;

import org.LexGrid.codingSchemes.CodingScheme;

public class CodingSchemeInsertEvent {

	private CodingScheme codingScheme;

	
	public CodingSchemeInsertEvent(
			CodingScheme codingScheme) {
		this.codingScheme = codingScheme;
	}

	public CodingScheme getCodingScheme() {
		return codingScheme;
	}


	public void setCodingScheme(CodingScheme codingScheme) {
		this.codingScheme = codingScheme;
	}
}

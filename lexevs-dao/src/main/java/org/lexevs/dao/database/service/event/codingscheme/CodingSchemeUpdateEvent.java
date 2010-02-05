package org.lexevs.dao.database.service.event.codingscheme;

import org.LexGrid.codingSchemes.CodingScheme;

public class CodingSchemeUpdateEvent {

	private String revisionId;
	
	private String entryStateId;
	
	private CodingScheme originalCodingScheme;
	
	private CodingScheme updatedCodingScheme;
	
	public CodingSchemeUpdateEvent(String revisionId, String entryStateId,
			CodingScheme originalCodingScheme, CodingScheme updatedCodingScheme) {
		this.revisionId = revisionId;
		this.entryStateId = entryStateId;
		this.originalCodingScheme = originalCodingScheme;
		this.updatedCodingScheme = updatedCodingScheme;
	}

	public String getRevisionId() {
		return revisionId;
	}

	public void setRevisionId(String revisionId) {
		this.revisionId = revisionId;
	}

	public CodingScheme getOriginalCodingScheme() {
		return originalCodingScheme;
	}

	public void setOriginalCodingScheme(CodingScheme originalCodingScheme) {
		this.originalCodingScheme = originalCodingScheme;
	}

	public CodingScheme getUpdatedCodingScheme() {
		return updatedCodingScheme;
	}

	public void setUpdatedCodingScheme(CodingScheme updatedCodingScheme) {
		this.updatedCodingScheme = updatedCodingScheme;
	}

	public void setEntryStateId(String entryStateId) {
		this.entryStateId = entryStateId;
	}

	public String getEntryStateId() {
		return entryStateId;
	}
}

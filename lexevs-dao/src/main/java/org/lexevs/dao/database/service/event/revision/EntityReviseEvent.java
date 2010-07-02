package org.lexevs.dao.database.service.event.revision;

import org.LexGrid.concepts.Entity;

public class EntityReviseEvent extends ReviseEvent<Entity>{
	
	private String codingSchemeUri;
	private String codingSchemeVersion;
	
	public String getCodingSchemeUri() {
		return codingSchemeUri;
	}
	public void setCodingSchemeUri(String codingSchemeUri) {
		this.codingSchemeUri = codingSchemeUri;
	}
	public String getCodingSchemeVersion() {
		return codingSchemeVersion;
	}
	public void setCodingSchemeVersion(String codingSchemeVersion) {
		this.codingSchemeVersion = codingSchemeVersion;
	}
}

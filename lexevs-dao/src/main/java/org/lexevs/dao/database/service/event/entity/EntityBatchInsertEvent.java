package org.lexevs.dao.database.service.event.entity;

import java.util.List;

import org.LexGrid.concepts.Entity;

public class EntityBatchInsertEvent {
	private String codingSchemeUri;
	private String version;
	private List<? extends Entity> entities;

	public EntityBatchInsertEvent(String codingSchemeUri, String version,
			List<? extends Entity> entities) {
		super();
		this.codingSchemeUri = codingSchemeUri;
		this.version = version;
		this.entities = entities;
	}
	
	public String getCodingSchemeUri() {
		return codingSchemeUri;
	}
	public void setCodingSchemeUri(String codingSchemeUri) {
		this.codingSchemeUri = codingSchemeUri;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public List<? extends Entity> getEntities() {
		return entities;
	}
	public void setEntities(List<? extends Entity> entities) {
		this.entities = entities;
	}
}

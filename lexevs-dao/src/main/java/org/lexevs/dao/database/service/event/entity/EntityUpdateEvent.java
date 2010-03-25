package org.lexevs.dao.database.service.event.entity;

import org.LexGrid.concepts.Entity;

public class EntityUpdateEvent {

	private String codingSchemeUri;
	private String codingSchemeVersion;
	private Entity originalEntity;
	private Entity entityUpdates;
	
	public EntityUpdateEvent() {
		super();
	}

	public EntityUpdateEvent(String codingSchemeUri,
			String codingSchemeVersion, Entity originalEntity,
			Entity entityUpdates) {
		super();
		this.codingSchemeUri = codingSchemeUri;
		this.codingSchemeVersion = codingSchemeVersion;
		this.originalEntity = originalEntity;
		this.entityUpdates = entityUpdates;
	}

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

	public Entity getOriginalEntity() {
		return originalEntity;
	}

	public void setOriginalEntity(Entity originalEntity) {
		this.originalEntity = originalEntity;
	}

	public Entity getEntityUpdates() {
		return entityUpdates;
	}

	public void setEntityUpdates(Entity entityUpdates) {
		this.entityUpdates = entityUpdates;
	}

}

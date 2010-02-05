package org.lexevs.dao.database.service.event;

import org.LexGrid.concepts.Entity;

public class EntityUpdateEvent {

	private String revisionId;
	
	private String entryStateId;
	
	private Entity originalEntity;
	
	private Entity updatedEntity;
	
	public EntityUpdateEvent(String revisionId, String entryStateId,
			Entity originalEntity, Entity updatedEntity) {
		this.revisionId = revisionId;
		this.entryStateId = entryStateId;
		this.originalEntity = originalEntity;
		this.updatedEntity = updatedEntity;
	}

	public String getRevisionId() {
		return revisionId;
	}

	public void setRevisionId(String revisionId) {
		this.revisionId = revisionId;
	}

	public String getEntryStateId() {
		return entryStateId;
	}

	public void setEntryStateId(String entryStateId) {
		this.entryStateId = entryStateId;
	}

	public Entity getOriginalEntity() {
		return originalEntity;
	}

	public void setOriginalEntity(Entity originalEntity) {
		this.originalEntity = originalEntity;
	}

	public Entity getUpdatedEntity() {
		return updatedEntity;
	}

	public void setUpdatedEntity(Entity updatedEntity) {
		this.updatedEntity = updatedEntity;
	}
}


package org.lexevs.dao.database.service.event.entity;

import org.LexGrid.concepts.Entity;

/**
 * The Class EntityUpdateEvent.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityUpdateEvent {

	/** The coding scheme uri. */
	private String codingSchemeUri;
	
	/** The coding scheme version. */
	private String codingSchemeVersion;
	
	/** The original entity. */
	private Entity originalEntity;
	
	/** The entity updates. */
	private Entity entityUpdates;
	
	/**
	 * Instantiates a new entity update event.
	 */
	public EntityUpdateEvent() {
		super();
	}

	/**
	 * Instantiates a new entity update event.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param originalEntity the original entity
	 * @param entityUpdates the entity updates
	 */
	public EntityUpdateEvent(String codingSchemeUri,
			String codingSchemeVersion, Entity originalEntity,
			Entity entityUpdates) {
		super();
		this.codingSchemeUri = codingSchemeUri;
		this.codingSchemeVersion = codingSchemeVersion;
		this.originalEntity = originalEntity;
		this.entityUpdates = entityUpdates;
	}

	/**
	 * Gets the coding scheme uri.
	 * 
	 * @return the coding scheme uri
	 */
	public String getCodingSchemeUri() {
		return codingSchemeUri;
	}

	/**
	 * Sets the coding scheme uri.
	 * 
	 * @param codingSchemeUri the new coding scheme uri
	 */
	public void setCodingSchemeUri(String codingSchemeUri) {
		this.codingSchemeUri = codingSchemeUri;
	}

	/**
	 * Gets the coding scheme version.
	 * 
	 * @return the coding scheme version
	 */
	public String getCodingSchemeVersion() {
		return codingSchemeVersion;
	}

	/**
	 * Sets the coding scheme version.
	 * 
	 * @param codingSchemeVersion the new coding scheme version
	 */
	public void setCodingSchemeVersion(String codingSchemeVersion) {
		this.codingSchemeVersion = codingSchemeVersion;
	}

	/**
	 * Gets the original entity.
	 * 
	 * @return the original entity
	 */
	public Entity getOriginalEntity() {
		return originalEntity;
	}

	/**
	 * Sets the original entity.
	 * 
	 * @param originalEntity the new original entity
	 */
	public void setOriginalEntity(Entity originalEntity) {
		this.originalEntity = originalEntity;
	}

	/**
	 * Gets the entity updates.
	 * 
	 * @return the entity updates
	 */
	public Entity getEntityUpdates() {
		return entityUpdates;
	}

	/**
	 * Sets the entity updates.
	 * 
	 * @param entityUpdates the new entity updates
	 */
	public void setEntityUpdates(Entity entityUpdates) {
		this.entityUpdates = entityUpdates;
	}

}
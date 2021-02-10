
package org.lexevs.dao.database.service.event.entity;

import org.LexGrid.concepts.Entity;

/**
 * The Class EntityInsertOrRemoveEvent.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityInsertOrRemoveEvent {
	
	/** The coding scheme uri. */
	private String codingSchemeUri;
	
	/** The version. */
	private String version;
	
	/** The entity. */
	private Entity entity;
	
	/**
	 * Instantiates a new entity insert or remove event.
	 */
	public EntityInsertOrRemoveEvent() {
		super();
	}
	
	/**
	 * Instantiates a new entity insert or remove event.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param entity the entity
	 */
	public EntityInsertOrRemoveEvent(String codingSchemeUri, String version, Entity entity) {
		this.codingSchemeUri = codingSchemeUri;
		this.version = version;
		this.entity = entity;
	}

	/**
	 * Gets the coding scheme uri.
	 * 
	 * @return the coding scheme uri
	 */
	public String getCodingSchemeUri() {
		return this.codingSchemeUri;
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
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return this.version;
	}
	
	/**
	 * Sets the version.
	 * 
	 * @param version the new version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Gets the entity.
	 * 
	 * @return the entity
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * Sets the entity.
	 * 
	 * @param entity the new entity
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
}
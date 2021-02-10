
package org.lexevs.dao.database.service.event.property;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;

/**
 * The Class PropertyUpdateEvent.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PropertyUpdateEvent {

	/** The coding scheme uri. */
	private String codingSchemeUri;
	
	/** The coding scheme version. */
	private String codingSchemeVersion;
	
	/** The entity. */
	private Entity entity;
	
	/** The property updates. */
	private Property propertyUpdates;
	
	/**
	 * Instantiates a new property update event.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param entity the entity
	 * @param propertyUpdates the property updates
	 */
	public PropertyUpdateEvent(String codingSchemeUri,
			String codingSchemeVersion, Entity entity,
			Property propertyUpdates) {
		super();
		this.codingSchemeUri = codingSchemeUri;
		this.codingSchemeVersion = codingSchemeVersion;
		this.entity = entity;
		this.propertyUpdates = propertyUpdates;
	}

	/**
	 * Instantiates a new property update event.
	 */
	public PropertyUpdateEvent() {
		super();
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

	/**
	 * Gets the property updates.
	 * 
	 * @return the property updates
	 */
	public Property getPropertyUpdates() {
		return propertyUpdates;
	}

	/**
	 * Sets the property updates.
	 * 
	 * @param propertyUpdates the new property updates
	 */
	public void setPropertyUpdates(Property propertyUpdates) {
		this.propertyUpdates = propertyUpdates;
	}
}
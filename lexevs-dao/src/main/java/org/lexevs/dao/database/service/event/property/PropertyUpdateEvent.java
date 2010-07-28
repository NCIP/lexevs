package org.lexevs.dao.database.service.event.property;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;

public class PropertyUpdateEvent {

	private String codingSchemeUri;
	private String codingSchemeVersion;
	private Entity entity;
	private Property propertyUpdates;
	
	public PropertyUpdateEvent(String codingSchemeUri,
			String codingSchemeVersion, Entity entity,
			Property propertyUpdates) {
		super();
		this.codingSchemeUri = codingSchemeUri;
		this.codingSchemeVersion = codingSchemeVersion;
		this.entity = entity;
		this.propertyUpdates = propertyUpdates;
	}

	public PropertyUpdateEvent() {
		super();
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

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Property getPropertyUpdates() {
		return propertyUpdates;
	}

	public void setPropertyUpdates(Property propertyUpdates) {
		this.propertyUpdates = propertyUpdates;
	}
}

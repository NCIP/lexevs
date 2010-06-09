package org.lexevs.dao.database.service.event.property;

import org.LexGrid.commonTypes.Property;

public class PropertyUpdateEvent {

	private String codingSchemeUri;
	private String codingSchemeVersion;
	private String entityCode;
	private String entityCodeNamespace;
	private Property propertyUpdates;
	
	public PropertyUpdateEvent(String codingSchemeUri,
			String codingSchemeVersion, String entityCode,
			String entityCodeNamespace,
			Property propertyUpdates) {
		super();
		this.codingSchemeUri = codingSchemeUri;
		this.codingSchemeVersion = codingSchemeVersion;
		this.entityCode = entityCode;
		this.entityCodeNamespace = entityCodeNamespace;
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

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public String getEntityCodeNamespace() {
		return entityCodeNamespace;
	}

	public void setEntityCodeNamespace(String entityCodeNamespace) {
		this.entityCodeNamespace = entityCodeNamespace;
	}

	public Property getPropertyUpdates() {
		return propertyUpdates;
	}

	public void setPropertyUpdates(Property propertyUpdates) {
		this.propertyUpdates = propertyUpdates;
	}
}

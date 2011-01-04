/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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
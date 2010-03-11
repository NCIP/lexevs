/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.database.service.event;

import org.LexGrid.concepts.Entity;

/**
 * The Class EntityUpdateEvent.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityUpdateEvent {

	/** The revision id. */
	private String revisionId;
	
	/** The entry state id. */
	private String entryStateId;
	
	/** The original entity. */
	private Entity originalEntity;
	
	/** The updated entity. */
	private Entity updatedEntity;
	
	/**
	 * Instantiates a new entity update event.
	 * 
	 * @param revisionId the revision id
	 * @param entryStateId the entry state id
	 * @param originalEntity the original entity
	 * @param updatedEntity the updated entity
	 */
	public EntityUpdateEvent(String revisionId, String entryStateId,
			Entity originalEntity, Entity updatedEntity) {
		this.revisionId = revisionId;
		this.entryStateId = entryStateId;
		this.originalEntity = originalEntity;
		this.updatedEntity = updatedEntity;
	}

	/**
	 * Gets the revision id.
	 * 
	 * @return the revision id
	 */
	public String getRevisionId() {
		return revisionId;
	}

	/**
	 * Sets the revision id.
	 * 
	 * @param revisionId the new revision id
	 */
	public void setRevisionId(String revisionId) {
		this.revisionId = revisionId;
	}

	/**
	 * Gets the entry state id.
	 * 
	 * @return the entry state id
	 */
	public String getEntryStateId() {
		return entryStateId;
	}

	/**
	 * Sets the entry state id.
	 * 
	 * @param entryStateId the new entry state id
	 */
	public void setEntryStateId(String entryStateId) {
		this.entryStateId = entryStateId;
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
	 * Gets the updated entity.
	 * 
	 * @return the updated entity
	 */
	public Entity getUpdatedEntity() {
		return updatedEntity;
	}

	/**
	 * Sets the updated entity.
	 * 
	 * @param updatedEntity the new updated entity
	 */
	public void setUpdatedEntity(Entity updatedEntity) {
		this.updatedEntity = updatedEntity;
	}
}

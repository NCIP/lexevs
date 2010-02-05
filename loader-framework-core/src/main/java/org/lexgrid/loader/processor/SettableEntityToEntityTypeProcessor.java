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
package org.lexgrid.loader.processor;

import org.LexGrid.persistence.model.Entity;
import org.LexGrid.persistence.model.EntityType;
import org.LexGrid.persistence.model.EntityTypeId;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class SettableEntityToEntityTypeProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SettableEntityToEntityTypeProcessor implements ItemProcessor<Entity, EntityType>{

	/** The entity type. */
	private String entityType = "concept";
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public EntityType process(Entity entity) throws Exception {
		EntityType type = new EntityType();
		EntityTypeId typeId = new EntityTypeId();
		
		typeId.setCodingSchemeName(entity.getId().getCodingSchemeName());
		typeId.setEntityCodeNamespace(entity.getId().getEntityCodeNamespace());
		typeId.setEntityCode(entity.getId().getEntityCode());	
		typeId.setEntityType(entityType);
		type.setId(typeId);
		
		return type;
	}

	/**
	 * Gets the entity type.
	 * 
	 * @return the entity type
	 */
	public String getEntityType() {
		return entityType;
	}

	/**
	 * Sets the entity type.
	 * 
	 * @param entityType the new entity type
	 */
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
}

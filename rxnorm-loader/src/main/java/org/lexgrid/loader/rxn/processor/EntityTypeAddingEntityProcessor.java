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
package org.lexgrid.loader.rxn.processor;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.concepts.Entity;
import org.LexGrid.commonTypes.types.EntityTypes;
import org.lexgrid.loader.rrf.model.Mrconso;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class EntityTypeAddingEntityProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityTypeAddingEntityProcessor implements ItemProcessor<List<Mrconso>, List<Object>> {

	/** The entity list processor. */
	private ItemProcessor<List<Mrconso>, Entity> entityListProcessor;
	
	/** The entity type processor. */
	private ItemProcessor<Entity, EntityTypes> entityTypeProcessor;

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public List<Object> process(List<Mrconso> itemList) throws Exception {
		List<Object> returnList = new ArrayList<Object>();
		Entity entity = entityListProcessor.process(itemList);
		EntityTypes entityType = entityTypeProcessor.process(entity);
		
		returnList.add(entity);
		returnList.add(entityType);
		return returnList;
	}

	/**
	 * Gets the entity list processor.
	 * 
	 * @return the entity list processor
	 */
	public ItemProcessor<List<Mrconso>, Entity> getEntityListProcessor() {
		return entityListProcessor;
	}

	/**
	 * Sets the entity list processor.
	 * 
	 * @param entityListProcessor the entity list processor
	 */
	public void setEntityListProcessor(
			ItemProcessor<List<Mrconso>, Entity> entityListProcessor) {
		this.entityListProcessor = entityListProcessor;
	}

	/**
	 * Gets the entity type processor.
	 * 
	 * @return the entity type processor
	 */
	public ItemProcessor<Entity, EntityTypes> getEntityTypeProcessor() {
		return entityTypeProcessor;
	}

	/**
	 * Sets the entity type processor.
	 * 
	 * @param entityTypeProcessor the entity type processor
	 */
	public void setEntityTypeProcessor(
			ItemProcessor<Entity, EntityTypes> entityTypeProcessor) {
		this.entityTypeProcessor = entityTypeProcessor;
	}
}

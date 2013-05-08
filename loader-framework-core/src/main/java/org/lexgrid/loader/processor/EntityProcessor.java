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
package org.lexgrid.loader.processor;

import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.concepts.Entity;
import org.lexgrid.loader.processor.support.EntityResolver;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class EntityProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityProcessor<I> extends CodingSchemeIdAwareProcessor implements ItemProcessor<I,CodingSchemeIdHolder<Entity>>{

	/** The entity resolver. */
	private EntityResolver<I> entityResolver;

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public CodingSchemeIdHolder<Entity> process(I item) throws Exception {
		CodingSchemeIdHolder<Entity> holder = new CodingSchemeIdHolder<Entity>();
		Entity entity = new Entity();
		
		entity.setEntityCodeNamespace(entityResolver.getEntityCodeNamespace(item));
		entity.setEntityCode(entityResolver.getEntityCode(item));		
		entity.setEntityDescription(Constructors.createEntityDescription(entityResolver.getEntityDescription(item)));
		entity.setIsActive(entityResolver.getIsActive(item));
		entity.setIsAnonymous(entityResolver.getIsAnonymous(item));
		entity.setIsDefined(entityResolver.getIsDefined(item));
		entity.setEntityType(entityResolver.getEntityTypes(item));
		
		holder.setItem(entity);
		holder.setCodingSchemeIdSetter(this.getCodingSchemeIdSetter());
		
		return holder;
	}

	/**
	 * Gets the entity resolver.
	 * 
	 * @return the entity resolver
	 */
	public EntityResolver<I> getEntityResolver() {
		return entityResolver;
	}

	/**
	 * Sets the entity resolver.
	 * 
	 * @param entityResolver the new entity resolver
	 */
	public void setEntityResolver(EntityResolver<I> entityResolver) {
		this.entityResolver = entityResolver;
	}


}
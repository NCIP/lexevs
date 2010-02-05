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
package org.lexgrid.loader.processor.decorator;

import java.util.List;

import org.LexGrid.persistence.model.EntityProperty;
import org.lexgrid.loader.data.property.ListIdSetter;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class EntityPropertyListIdAddingDecorator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityPropertyListIdAddingDecorator<I> implements ItemProcessor<List<I>, List<EntityProperty>> {

	
	/** The list id setter. */
	private ListIdSetter<EntityProperty> listIdSetter;
	
	/** The decorated processor. */
	private ItemProcessor<List<I>, List<EntityProperty>> decoratedProcessor;
	
	/**
	 * Instantiates a new entity property list id adding decorator.
	 * 
	 * @param decoratedProcessor the decorated processor
	 */
	public EntityPropertyListIdAddingDecorator(ItemProcessor<List<I>, List<EntityProperty>> decoratedProcessor){
		this.decoratedProcessor = decoratedProcessor;
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public List<EntityProperty> process(List<I> items)
			throws Exception {
		List<EntityProperty> processedItems = decoratedProcessor.process(items);
		listIdSetter.addIds(processedItems);
		return processedItems;
	}

	/**
	 * Gets the list id setter.
	 * 
	 * @return the list id setter
	 */
	public ListIdSetter<EntityProperty> getListIdSetter() {
		return listIdSetter;
	}

	/**
	 * Sets the list id setter.
	 * 
	 * @param listIdSetter the new list id setter
	 */
	public void setListIdSetter(ListIdSetter<EntityProperty> listIdSetter) {
		this.listIdSetter = listIdSetter;
	}	
	
	
}

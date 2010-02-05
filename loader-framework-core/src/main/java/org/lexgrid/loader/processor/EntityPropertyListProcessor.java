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

import java.util.List;

import org.LexGrid.persistence.model.EntityProperty;
import org.lexgrid.loader.data.property.ListIdSetter;
import org.lexgrid.loader.data.property.PreferredSetter;

/**
 * The Class EntityPropertyListProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityPropertyListProcessor<I> extends SortingListProcessor <I,EntityProperty>{

	/** The list id setter. */
	private ListIdSetter<EntityProperty> listIdSetter;
	
	/** The preferred setter. */
	private PreferredSetter<EntityProperty> preferredSetter;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.SortingListProcessor#afterProcessing(java.util.List)
	 */
	@Override
	protected List<EntityProperty> afterProcessing(List<EntityProperty> items) {
		listIdSetter.addIds(items);
		preferredSetter.setPreferred(items);
		return items;
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

	/**
	 * Gets the preferred setter.
	 * 
	 * @return the preferred setter
	 */
	public PreferredSetter<EntityProperty> getPreferredSetter() {
		return preferredSetter;
	}

	/**
	 * Sets the preferred setter.
	 * 
	 * @param preferredSetter the new preferred setter
	 */
	public void setPreferredSetter(PreferredSetter<EntityProperty> preferredSetter) {
		this.preferredSetter = preferredSetter;
	}
}

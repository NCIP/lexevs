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

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.lexgrid.loader.data.property.ListIdSetter;
import org.lexgrid.loader.data.property.PreferredSetter;
import org.lexgrid.loader.wrappers.ParentIdHolder;

/**
 * The Class EntityPropertyListProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityPropertyListProcessor<I> extends SortingListProcessor <I,ParentIdHolder<Property>>{

	/** The list id setter. */
	private ListIdSetter<ParentIdHolder<Property>> listIdSetter;
	
	/** The preferred setter. */
	private PreferredSetter<ParentIdHolder<Property>> preferredSetter;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.SortingListProcessor#afterProcessing(java.util.List)
	 */
	@Override
	protected List<ParentIdHolder<Property>> afterProcessing(List<ParentIdHolder<Property>> items, List<I> originalItems) {
		listIdSetter.addIds(items);
		preferredSetter.setPreferred(items);
		return items;
	}

	public ListIdSetter<ParentIdHolder<Property>> getListIdSetter() {
		return listIdSetter;
	}

	public void setListIdSetter(ListIdSetter<ParentIdHolder<Property>> listIdSetter) {
		this.listIdSetter = listIdSetter;
	}

	public PreferredSetter<ParentIdHolder<Property>> getPreferredSetter() {
		return preferredSetter;
	}

	public void setPreferredSetter(
			PreferredSetter<ParentIdHolder<Property>> preferredSetter) {
		this.preferredSetter = preferredSetter;
	}
}
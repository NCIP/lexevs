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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The Class SortingListProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SortingListProcessor<I,O> extends AbstractParameterPassingListProcessor<I,O> {

	/** The property comparator. */
	private Comparator<I> propertyComparator;

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.AbstractListProcessor#beforeProcessing(java.util.List)
	 */
	@Override
	protected List<I> beforeProcessing(List<I> items) {
		Collections.sort(items, propertyComparator);
		return items;
	}
	
	@Override
	protected List<O> afterProcessing(List<O> processedItems,
			List<I> originalItems) {
		return processedItems;
	}	

	/**
	 * Gets the property comparator.
	 * 
	 * @return the property comparator
	 */
	public Comparator<I> getPropertyComparator() {
		return propertyComparator;
	}

	/**
	 * Sets the property comparator.
	 * 
	 * @param propertyComparator the new property comparator
	 */
	public void setPropertyComparator(Comparator<I> propertyComparator) {
		this.propertyComparator = propertyComparator;
	}
}
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

import org.lexgrid.loader.processor.support.ListFilter;

/**
 * The Class PreFilteringListProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PreFilteringListProcessor<I,O> extends AbstractListProcessor<I,O> {

	private ListFilter<I> listFilter;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.AbstractListProcessor#beforeProcessing(java.util.List)
	 */
	@Override
	protected List<I> beforeProcessing(List<I> items) {	
		return listFilter.filter(items);
	}

	@Override
	protected List<O> afterProcessing(List<O> items) {
		return items;
	}

	public ListFilter<I> getListFilter() {
		return listFilter;
	}

	public void setListFilter(ListFilter<I> listFilter) {
		this.listFilter = listFilter;
	}	
}

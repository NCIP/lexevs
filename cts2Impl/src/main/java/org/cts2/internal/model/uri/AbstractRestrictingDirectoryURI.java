/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.internal.model.uri;

import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts2.core.Filter;
import org.cts2.core.FilterComponent;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.uri.DirectoryURI;
import org.cts2.uri.operation.Restrictable;

/**
 * The Class AbstractRestrictingDirectoryURI.
 *
 * @param <T> the
 * @param <U> the
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractRestrictingDirectoryURI
	<T,U extends DirectoryURI & Restrictable<U>> extends AbstractLexEvsDirectoryURI<T> implements Restrictable<U>{

	/** FILTER_ORDER_COMPARATOR. */
	//Order taken out of the spec for now... verify this.
	//private static Comparator<FilterComponent> FILTER_ORDER_COMPARATOR = new FilterOrderComparator();
	
	/**
	 * Instantiates a new abstract restricting directory uri.
	 *
	 * @param lexBIGService the lex big service
	 * @param beanMapper the bean mapper
	 */
	protected AbstractRestrictingDirectoryURI(
			LexBIGService lexBIGService,
			BeanMapper beanMapper) {
		super(lexBIGService, beanMapper);
	}

	/* (non-Javadoc)
	 * @see org.cts2.uri.operation.Restrictable#restrict(org.cts2.core.Filter)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public U restrict(Filter filter) {
		FilterComponent[] filterComponents = filter.getComponent();
		
		//See FILTER_ORDER_COMPARATOR comment above
		//Arrays.sort(filterComponents, FILTER_ORDER_COMPARATOR);
		
		for(FilterComponent filterComponent : filterComponents){
			this.applyFilterComponent(this.getLexEvsBackingObject(), filterComponent);
		}
		
		return (U) this;
	}
	
	/**
	 * Apply filter component.
	 *
	 * @param lexEvsBackingObject the lex evs backing object
	 * @param filterComponent the filter component
	 */
	protected abstract void applyFilterComponent(T lexEvsBackingObject, FilterComponent filterComponent);
	
	/**
	 * The Class FilterOrderComparator.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	/* Taken out of spec -- verify this.
	private static class FilterOrderComparator implements Comparator<FilterComponent>{

		@Override
		public int compare(FilterComponent o1, FilterComponent o2) {
			return (int) (o1.getComponentOrder() - o2.getComponentOrder());
		}
	}
	*/
}

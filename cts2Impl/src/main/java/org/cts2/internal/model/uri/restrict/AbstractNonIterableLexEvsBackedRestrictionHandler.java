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
package org.cts2.internal.model.uri.restrict;

import java.util.List;

import org.cts2.core.Filter;
import org.cts2.core.FilterComponent;


/**
 * The Class AbstractIterableLexEvsBackedRestrictionHandler.
 *
 * @param <T> the
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractNonIterableLexEvsBackedRestrictionHandler<T> extends AbstractRestrictionHandler implements NonIterableBasedResolvingRestrictionHandler<T> {

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.NonIterableBasedResolvingRestrictionHandler#restrict(java.lang.Object, org.cts2.core.Filter)
	 */
	@Override
	public T restrict(T originalState, Filter filter) {
		
		List<FilterComponent> filterComponents = this.sortFilterComponents(filter);
		
		for (FilterComponent filterComponent : filterComponents) {
			switch (filterComponent.getFilterOperator()){
				case UNION : {
					this.doUnion(originalState, filterComponent);
					break;
				}
				case INTERSECT: {
					this.doIntersect(originalState, filterComponent);
					break;
				}
				case SUBTRACT: {
					this.doSubtract(originalState, filterComponent);
					break;
				}
			}
		}	
		
		return originalState;
	}
	
	/**
	 * Do union.
	 *
	 * @param originalState the original state
	 * @param filterComponent the filter component
	 */
	protected abstract void doUnion(T originalState, FilterComponent filterComponent);
	
	/**
	 * Do intersect.
	 *
	 * @param originalState the original state
	 * @param filterComponent the filter component
	 */
	protected abstract void doIntersect(T originalState, FilterComponent filterComponent);
	
	/**
	 * Do subtract.
	 *
	 * @param originalState the original state
	 * @param filterComponent the filter component
	 */
	protected abstract void doSubtract(T originalState, FilterComponent filterComponent);
}

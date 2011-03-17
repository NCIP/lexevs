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

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.cts2.core.Filter;
import org.cts2.core.FilterComponent;

/**
 * The Class AbstractRestrictionHandler.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractRestrictionHandler implements RestrictionHandler {

	/** The FILTE r_ orde r_ comparator. */
	private Comparator<FilterComponent> FILTER_ORDER_COMPARATOR = new FilterOrderComparator();
	
	/**
	 * The Class FilterOrderComparator.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private static class FilterOrderComparator implements Comparator<FilterComponent>{

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(FilterComponent o1, FilterComponent o2) {
			return (int) (o1.getComponentOrder() - o2.getComponentOrder());
		}
	}
	
	/**
	 * Sort filter components.
	 *
	 * @param filter the filter
	 * @return the list
	 */
	protected List<FilterComponent> sortFilterComponents(Filter filter){
		List<FilterComponent> filterComponents = Arrays.asList(filter.getComponent());

		Collections.sort(filterComponents, FILTER_ORDER_COMPARATOR);

		return filterComponents;
	}
}

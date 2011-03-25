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
package org.cts2.uri.restriction;

import java.util.HashSet;
import java.util.Set;

import org.cts2.core.Filter;
import org.cts2.uri.DirectoryURI;

/**
 * The Class RestrictionState.
 *
 * @param <T> the
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RestrictionState<T extends DirectoryURI> {

	/** The set composite. */
	private SetComposite<T> setComposite;

	/** The filters. */
	private Set<Filter> filters = new HashSet<Filter>();

	/**
	 * Gets the sets the composite.
	 *
	 * @return the sets the composite
	 */
	public SetComposite<T> getSetComposite() {
		return setComposite;
	}

	/**
	 * Sets the sets the composite.
	 *
	 * @param setComposite the new sets the composite
	 */
	public void setSetComposite(SetComposite<T> setComposite) {
		this.setComposite = setComposite;
	}

	/**
	 * Gets the filters.
	 *
	 * @return the filters
	 */
	public Set<Filter> getFilters() {
		return filters;
	}

	/**
	 * Sets the filters.
	 *
	 * @param filters the new filters
	 */
	public void setFilters(Set<Filter> filters) {
		this.filters = filters;
	}
}
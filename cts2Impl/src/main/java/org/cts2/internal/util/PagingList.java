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
package org.cts2.internal.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An Iterator backed List designed to lazily populate a List by resolving the Iterator
 * on demand. Useful for Iterator->List conversions where a full traverse of the Iterator
 * is expensive.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PagingList<T> extends AbstractList<T> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5575701992345467751L;

	/** The iterator. */
	private Iterator<T> iterator;
	
	/** The size. */
	private int size;
	
	/** The cache. */
	private List<T> cache = new ArrayList<T>();
	
	/**
	 * Instantiates a new paging list.
	 *
	 * @param iterator the iterator
	 * @param size the size
	 */
	public PagingList(Iterator<T> iterator, int size){
		this.iterator = iterator;
		this.size = size;
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public T get(int index) {
		return doGet(index);
	}

	/**
	 * Do get.
	 *
	 * @param index the index
	 * @return the t
	 */
	protected T doGet(int index){
		while(this.cache.size() < (index + 1) 
				&& this.iterator.hasNext()){
			this.cache.add(this.iterator.next());
		}

		return this.cache.get(index);
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return this.size;
	}
}

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
package org.cts2.internal.model.directory.iterator;

import java.util.Iterator;

import org.cts2.internal.mapper.BeanMapper;

/**
 * The Class DirectoryEntryIterator.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DirectoryEntryIterator<I,O> implements Iterator<O>{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2616653453721025571L;

	/** The bean mapper. */
	private BeanMapper beanMapper;
	
	/** The iterator. */
	private Iterator<I> iterator;
	
	/** The output class. */
	private Class<O> outputClass;
	
	/**
	 * Instantiates a new directory entry iterator.
	 *
	 * @param iterator the iterator
	 * @param outputClass the output class
	 * @param beanMapper the bean mapper
	 */
	public DirectoryEntryIterator(Iterator<I> iterator, Class<O> outputClass, BeanMapper beanMapper) {
		super();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public O next() {
		return this.beanMapper.map(this.iterator.next(), this.outputClass);
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}

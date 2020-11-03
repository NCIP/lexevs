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
package org.lexgrid.loader.reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lexgrid.loader.reader.support.SkipPolicy;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * The Class SkippingItemReader.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SkippingItemReader<I> implements ItemReader<I>{
	
	/** The Constant log. */
	private static final Log log = LogFactory.getLog(SkippingItemReader.class);

	/** The delegate. */
	private ItemReader<I> delegate;
	
	/** The skip policy. */
	private SkipPolicy<I> skipPolicy;

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	public I read() throws Exception, UnexpectedInputException,
			ParseException {
		
		for(I readItem = delegate.read(); readItem != null; readItem = delegate.read()){
			if(!skipPolicy.toSkip(readItem)){
				return readItem;
			}
		}
		
		//when exhausted, return null;
		return null;
	}

	/**
	 * Gets the delegate.
	 * 
	 * @return the delegate
	 */
	public ItemReader<I> getDelegate() {
		return delegate;
	}

	/**
	 * Sets the delegate.
	 * 
	 * @param delegate the new delegate
	 */
	public void setDelegate(ItemReader<I> delegate) {
		this.delegate = delegate;
	}

	/**
	 * Gets the skip policy.
	 * 
	 * @return the skip policy
	 */
	public SkipPolicy<I> getSkipPolicy() {
		return skipPolicy;
	}

	/**
	 * Sets the skip policy.
	 * 
	 * @param skipPolicy the new skip policy
	 */
	public void setSkipPolicy(SkipPolicy<I> skipPolicy) {
		this.skipPolicy = skipPolicy;
	}	
}
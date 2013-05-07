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
package org.lexgrid.loader.writer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

/**
 * The Class ListWriter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ListWriter<T> implements ItemWriter<List<T>> {
	
	/** The delegate. */
	private ItemWriter<T> delegate;
	
	/** The max buffer size. */
	private int maxBufferSize = 200;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	public void write(List<? extends List<T>> list) throws Exception {
		List<T> buffer = new ArrayList<T>();
		for(List<T> item : list){
			if(item != null){
				buffer.addAll(item);
			}
			if(buffer.size() >= maxBufferSize){
				delegate.write(buffer);
				buffer.clear();
			}
		}
		
		delegate.write(buffer);
	}

	/**
	 * Gets the delegate.
	 * 
	 * @return the delegate
	 */
	public ItemWriter<T> getDelegate() {
		return delegate;
	}

	/**
	 * Sets the delegate.
	 * 
	 * @param delegate the new delegate
	 */
	public void setDelegate(ItemWriter<T> delegate) {
		this.delegate = delegate;
	}

	/**
	 * Gets the max buffer size.
	 * 
	 * @return the max buffer size
	 */
	public int getMaxBufferSize() {
		return maxBufferSize;
	}

	/**
	 * Sets the max buffer size.
	 * 
	 * @param maxBufferSize the new max buffer size
	 */
	public void setMaxBufferSize(int maxBufferSize) {
		this.maxBufferSize = maxBufferSize;
	}	
}
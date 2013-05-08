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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lexgrid.loader.reader.support.GroupDiscriminator;
import org.springframework.batch.item.ItemReader;

/**
 * The Class BufferedGroupItemReader.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class BufferedGroupItemReader<I> implements ItemReader<List<I>>  {
	
	/** The delegate. */
	private ItemReader<I> delegate;
	
	/** The group discriminator. */
	private GroupDiscriminator<I> groupDiscriminator;
	
	/** The Constant log. */
	private static final Log log = LogFactory.getLog(BufferedGroupItemReader.class);
	
	/** The last item. */
	protected I lastItem;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	public List<I> read() throws Exception {
		List<I> buffer = new ArrayList<I>();
		if(lastItem != null){
			buffer.add(lastItem);
		}
		
		while (process(delegate.read(), buffer)) {
			continue;
		}

		if (!buffer.isEmpty()) {	
			return buffer;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Process.
	 * 
	 * @param value the value
	 * @param buffer the buffer
	 * 
	 * @return true, if successful
	 */
	protected boolean process(I value, List<I> buffer) {
		// finish processing if we hit the end of file
		if (value == null) {
			log.debug("Exhausted ItemReader");
			lastItem = null;
			return false;
		}
		
		if(lastItem == null){
			lastItem = value;
			buffer.add(value);
			return true;
		}
		
		if(groupDiscriminator.getDiscriminatingValue(value).equals(groupDiscriminator.getDiscriminatingValue(lastItem))){
			buffer.add(value);
			return true;
		} else {
			lastItem = value;
			return false;
		}
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
	 * Gets the group discriminator.
	 * 
	 * @return the group discriminator
	 */
	public GroupDiscriminator<I> getGroupDiscriminator() {
		return groupDiscriminator;
	}

	/**
	 * Sets the group discriminator.
	 * 
	 * @param groupDiscriminator the new group discriminator
	 */
	public void setGroupDiscriminator(GroupDiscriminator<I> groupDiscriminator) {
		this.groupDiscriminator = groupDiscriminator;
	}
}
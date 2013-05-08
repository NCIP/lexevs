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
package org.lexgrid.loader.writer.decorator;

import java.util.List;

import org.lexgrid.loader.processor.support.Truncator;
import org.springframework.batch.item.ItemWriter;

/**
 * The Class FieldTruncatingWriterDecorator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class FieldTruncatingWriterDecorator<O> implements ItemWriter<O>{

	/** The decorated. */
	private ItemWriter<O> decorated;
	
	/** The truncator. */
	private Truncator truncator;
	
	/**
	 * Instantiates a new field truncating writer decorator.
	 * 
	 * @param decorated the decorated
	 */
	public FieldTruncatingWriterDecorator(
			ItemWriter<O> decorated){
		this.decorated = decorated;
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	public void write(List<? extends O> list) throws Exception {
		for(O item : list){
			if(item instanceof List){
				for(Object listItem : (List)item){
					truncator.truncate(listItem);
				}
			} else {
				truncator.truncate(item);
			}
		}
		decorated.write(list);
	}

	/**
	 * Gets the truncator.
	 * 
	 * @return the truncator
	 */
	public Truncator getTruncator() {
		return truncator;
	}

	/**
	 * Sets the truncator.
	 * 
	 * @param truncator the new truncator
	 */
	public void setTruncator(Truncator truncator) {
		this.truncator = truncator;
	}
}
/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.lexgrid.loader.processor.decorator;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.Assert;

/**
 * The Class ListToSingleItemProcessorDecorator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ListToSingleItemProcessorDecorator<I,O> implements ItemProcessor<List<I>,O>{

	/** The decorated item processor. */
	private ItemProcessor<I,O> decoratedItemProcessor;
	
	/**
	 * Instantiates a new list to single item processor decorator.
	 * 
	 * @param decoratedItemProcessor the decorated item processor
	 */
	public ListToSingleItemProcessorDecorator(ItemProcessor<I,O> decoratedItemProcessor){
		this.decoratedItemProcessor = decoratedItemProcessor;		
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public O process(List<I> items) throws Exception {
		Assert.notEmpty(items);
		return decoratedItemProcessor.process(items.get(0));
		
	}

}

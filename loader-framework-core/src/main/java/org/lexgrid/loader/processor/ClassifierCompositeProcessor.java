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
package org.lexgrid.loader.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;

/**
 * The Class ClassifierCompositeProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ClassifierCompositeProcessor<I> implements ItemProcessor<I, List<?>>{

	/** The processor list. */
	private List<ItemProcessor<I,?>> processorList;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public List<?> process(I item) throws Exception {
		List returnList = new ArrayList();
		
		for(ItemProcessor processor : processorList){
			Object processedResult = processor.process(item);
			returnList.add(processedResult);			
		}	
		return returnList;
	}

	/**
	 * Gets the processor list.
	 * 
	 * @return the processor list
	 */
	public List<ItemProcessor<I, ?>> getProcessorList() {
		return processorList;
	}

	/**
	 * Sets the processor list.
	 * 
	 * @param processorList the processor list
	 */
	public void setProcessorList(List<ItemProcessor<I, ?>> processorList) {
		this.processorList = processorList;
	}

}
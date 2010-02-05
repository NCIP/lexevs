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
package org.lexgrid.loader.processor;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class HighestRankingListProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class HighestRankingListProcessor<I,O> implements ItemProcessor<List<I>,O> {
	
	/** The Constant log. */
	private static final Log log = LogFactory.getLog(HighestRankingListProcessor.class);
	
	/** The sorting list processor. */
	private SortingListProcessor<I,O> sortingListProcessor;

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public O process(List<I> items) throws Exception {
		List<O> sortedList = sortingListProcessor.process(items);
		if(sortedList.size() < 1){
			log.info("List Processor recieved an empty List, returning null");
			return null;
		} else {
			return sortedList.get(0);
		}		
	}

	/**
	 * Gets the sorting list processor.
	 * 
	 * @return the sorting list processor
	 */
	public SortingListProcessor<I, O> getSortingListProcessor() {
		return sortingListProcessor;
	}

	/**
	 * Sets the sorting list processor.
	 * 
	 * @param sortingListProcessor the sorting list processor
	 */
	public void setSortingListProcessor(
			SortingListProcessor<I, O> sortingListProcessor) {
		this.sortingListProcessor = sortingListProcessor;
	}	
}

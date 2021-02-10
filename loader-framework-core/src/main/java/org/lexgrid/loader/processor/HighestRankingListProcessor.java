
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
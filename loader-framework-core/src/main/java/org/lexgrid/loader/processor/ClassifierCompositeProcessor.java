
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

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
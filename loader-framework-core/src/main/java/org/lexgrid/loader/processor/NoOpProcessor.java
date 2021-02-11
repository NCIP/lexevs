
package org.lexgrid.loader.processor;

import org.springframework.batch.item.ItemProcessor;

/**
 * NoOp List Processor. Simply forwards the input unaltered. Useful if you want to use Decorators
 * but don't want to do any Processing
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NoOpProcessor<T> implements ItemProcessor<T,T>{
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public T process(T item) throws Exception {
		return item;
	}
}
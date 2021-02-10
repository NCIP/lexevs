
package org.lexgrid.loader.processor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;

/**
 * The Class BasicListProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class BasicListProcessor<I,O> implements ItemProcessor<List<I>,List<O>>{

	/** The delegate. */
	private ItemProcessor<I,O>  delegate;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public List<O> process(List<I> items) throws Exception {
		List<O> buffer = new ArrayList<O>();
		for(I item : items){
			buffer.add(delegate.process(item));
		}
		return buffer;
	}
	
	/**
	 * Gets the delegate.
	 * 
	 * @return the delegate
	 */
	public ItemProcessor<I, O> getDelegate() {
		return delegate;
	}

	/**
	 * Sets the delegate.
	 * 
	 * @param delegate the delegate
	 */
	public void setDelegate(ItemProcessor<I, O> delegate) {
		this.delegate = delegate;
	}
}
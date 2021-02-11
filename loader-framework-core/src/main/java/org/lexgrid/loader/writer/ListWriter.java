
package org.lexgrid.loader.writer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

/**
 * The Class ListWriter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ListWriter<T> implements ItemWriter<List<T>> {
	
	/** The delegate. */
	private ItemWriter<T> delegate;
	
	/** The max buffer size. */
	private int maxBufferSize = 200;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	public void write(List<? extends List<T>> list) throws Exception {
		List<T> buffer = new ArrayList<T>();
		for(List<T> item : list){
			if(item != null){
				buffer.addAll(item);
			}
			if(buffer.size() >= maxBufferSize){
				delegate.write(buffer);
				buffer.clear();
			}
		}
		
		delegate.write(buffer);
	}

	/**
	 * Gets the delegate.
	 * 
	 * @return the delegate
	 */
	public ItemWriter<T> getDelegate() {
		return delegate;
	}

	/**
	 * Sets the delegate.
	 * 
	 * @param delegate the new delegate
	 */
	public void setDelegate(ItemWriter<T> delegate) {
		this.delegate = delegate;
	}

	/**
	 * Gets the max buffer size.
	 * 
	 * @return the max buffer size
	 */
	public int getMaxBufferSize() {
		return maxBufferSize;
	}

	/**
	 * Sets the max buffer size.
	 * 
	 * @param maxBufferSize the new max buffer size
	 */
	public void setMaxBufferSize(int maxBufferSize) {
		this.maxBufferSize = maxBufferSize;
	}	
}
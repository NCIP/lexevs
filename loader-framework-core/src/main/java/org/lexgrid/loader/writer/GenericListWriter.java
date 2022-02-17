
package org.lexgrid.loader.writer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;

/**
 * The Class GenericListWriter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class GenericListWriter implements ItemWriter<List<? extends Object>> {
	
	/** The Constant log. */
	private static final Log log = LogFactory.getLog(GenericListWriter.class);
	
	/** The delegate. */
	private ItemWriter<Object> delegate;
	
	/** The max buffer size. */
	private int maxBufferSize = 200;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	public void write(List<? extends List<? extends Object>> list) throws Exception {
		List buffer = new ArrayList();
		for(List<? extends Object> item : list){
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
	public ItemWriter<Object> getDelegate() {
		return delegate;
	}

	/**
	 * Sets the delegate.
	 * 
	 * @param delegate the new delegate
	 */
	public void setDelegate(ItemWriter<Object> delegate) {
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
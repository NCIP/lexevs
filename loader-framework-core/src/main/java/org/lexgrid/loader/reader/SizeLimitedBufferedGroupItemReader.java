
package org.lexgrid.loader.reader;

import java.util.List;

/**
 * The Class SizeLimitedBufferedGroupItemReader.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SizeLimitedBufferedGroupItemReader<I> extends BufferedGroupItemReader<I>  {
	
	/** The max buffer size. */
	private int maxBufferSize = -1;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.BufferedGroupItemReader#process(java.lang.Object, java.util.List)
	 */
	protected boolean process(I value, List<I> buffer) {
		if(maxBufferSize > 0 && buffer.size() >= maxBufferSize){
			lastItem = value;
			return false;
		} else {
			return super.process(value, buffer);
		}
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
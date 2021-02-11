
package org.lexgrid.loader.reader;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lexgrid.loader.reader.support.GroupDiscriminator;
import org.springframework.batch.item.ItemReader;

/**
 * The Class BufferedGroupItemReader.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class BufferedGroupItemReader<I> implements ItemReader<List<I>>  {
	
	/** The delegate. */
	private ItemReader<I> delegate;
	
	/** The group discriminator. */
	private GroupDiscriminator<I> groupDiscriminator;
	
	/** The Constant log. */
	private static final Log log = LogFactory.getLog(BufferedGroupItemReader.class);
	
	/** The last item. */
	protected I lastItem;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	public List<I> read() throws Exception {
		List<I> buffer = new ArrayList<I>();
		if(lastItem != null){
			buffer.add(lastItem);
		}
		
		while (process(delegate.read(), buffer)) {
			continue;
		}

		if (!buffer.isEmpty()) {	
			return buffer;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Process.
	 * 
	 * @param value the value
	 * @param buffer the buffer
	 * 
	 * @return true, if successful
	 */
	protected boolean process(I value, List<I> buffer) {
		// finish processing if we hit the end of file
		if (value == null) {
			log.debug("Exhausted ItemReader");
			lastItem = null;
			return false;
		}
		
		if(lastItem == null){
			lastItem = value;
			buffer.add(value);
			return true;
		}
		
		if(groupDiscriminator.getDiscriminatingValue(value).equals(groupDiscriminator.getDiscriminatingValue(lastItem))){
			buffer.add(value);
			return true;
		} else {
			lastItem = value;
			return false;
		}
	}

	/**
	 * Gets the delegate.
	 * 
	 * @return the delegate
	 */
	public ItemReader<I> getDelegate() {
		return delegate;
	}

	/**
	 * Sets the delegate.
	 * 
	 * @param delegate the new delegate
	 */
	public void setDelegate(ItemReader<I> delegate) {
		this.delegate = delegate;
	}

	/**
	 * Gets the group discriminator.
	 * 
	 * @return the group discriminator
	 */
	public GroupDiscriminator<I> getGroupDiscriminator() {
		return groupDiscriminator;
	}

	/**
	 * Sets the group discriminator.
	 * 
	 * @param groupDiscriminator the new group discriminator
	 */
	public void setGroupDiscriminator(GroupDiscriminator<I> groupDiscriminator) {
		this.groupDiscriminator = groupDiscriminator;
	}
}
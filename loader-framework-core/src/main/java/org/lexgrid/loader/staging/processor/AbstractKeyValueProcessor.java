
package org.lexgrid.loader.staging.processor;

import org.lexgrid.loader.staging.support.KeyValuePair;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class AbstractKeyValueProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractKeyValueProcessor<I> implements ItemProcessor<I, KeyValuePair>{

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public KeyValuePair process(I item) throws Exception {
		KeyValuePair keyValuePair = new KeyValuePair();
		keyValuePair.setKey(getKey(item));
		keyValuePair.setValue(getValue(item));
		return keyValuePair;
	}
	
	/**
	 * Gets the key.
	 * 
	 * @param item the item
	 * 
	 * @return the key
	 */
	public abstract String getKey(I item);
	
	/**
	 * Gets the value.
	 * 
	 * @param item the item
	 * 
	 * @return the value
	 */
	public abstract String getValue(I item);
}
/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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
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
package org.lexgrid.loader.reader.support;

import java.util.List;

/**
 * The Class CompositeReaderChunk.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CompositeReaderChunk<I1,I2> {

	/** The item1 list. */
	private List<I1> item1List;
	
	/** The item2 list. */
	private List<I2> item2List;
	
	/**
	 * Gets the item1 list.
	 * 
	 * @return the item1 list
	 */
	public List<I1> getItem1List() {
		return item1List;
	}
	
	/**
	 * Sets the item1 list.
	 * 
	 * @param item1List the new item1 list
	 */
	public void setItem1List(List<I1> item1List) {
		this.item1List = item1List;
	}
	
	/**
	 * Gets the item2 list.
	 * 
	 * @return the item2 list
	 */
	public List<I2> getItem2List() {
		return item2List;
	}
	
	/**
	 * Sets the item2 list.
	 * 
	 * @param item2List the new item2 list
	 */
	public void setItem2List(List<I2> item2List) {
		this.item2List = item2List;
	}	
}
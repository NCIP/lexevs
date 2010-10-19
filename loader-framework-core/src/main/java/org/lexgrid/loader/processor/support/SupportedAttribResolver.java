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
package org.lexgrid.loader.processor.support;

/**
 * The Interface SupportedAttribResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface SupportedAttribResolver<T> {
	
	/**
	 * Gets the supported attribute tag.
	 * 
	 * @return the supported attribute tag
	 */
	public String getSupportedAttributeTag();
	
	/**
	 * Gets the id.
	 * 
	 * @param item the item
	 * 
	 * @return the id
	 */
	public String getId(T item);
	
	/**
	 * Gets the uri.
	 * 
	 * @param item the item
	 * 
	 * @return the uri
	 */
	public String getUri(T item);
	
	/**
	 * Gets the id val.
	 * 
	 * @param item the item
	 * 
	 * @return the id val
	 */
	public String getIdVal(T item);
	
	/**
	 * Gets the val1.
	 * 
	 * @param item the item
	 * 
	 * @return the val1
	 */
	public String getVal1(T item);
	
	/**
	 * Gets the val2.
	 * 
	 * @param item the item
	 * 
	 * @return the val2
	 */
	public String getVal2(T item);
}
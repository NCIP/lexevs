/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
 * The Interface PropertyLinkResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface PropertyLinkResolver<T> {

	/**
	 * Gets the entity code.
	 * 
	 * @param item the item
	 * 
	 * @return the entity code
	 */
	public String getEntityCode(T item);
	
	/**
	 * Gets the link.
	 * 
	 * @param item the item
	 * 
	 * @return the link
	 */
	public String getLink(T item);
	
	/**
	 * Gets the source id.
	 * 
	 * @param item the item
	 * 
	 * @return the source id
	 */
	public String getSourceId(T item);
	
	/**
	 * Gets the target id.
	 * 
	 * @param item the item
	 * 
	 * @return the target id
	 */
	public String getTargetId(T item);
}

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
 * The Interface PropertyResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface PropertyResolver<T> {

	/**
	 * Gets the entity code.
	 * 
	 * @param item the item
	 * 
	 * @return the entity code
	 */
	public String getEntityCode(T item);
	
	public String getEntityCodeNamespace(T item);
	/**
	 * Gets the id.
	 * 
	 * @param item the item
	 * 
	 * @return the id
	 */
	public String getId(T item);
	
	/**
	 * Gets the property name.
	 * 
	 * @param item the item
	 * 
	 * @return the property name
	 */
	public String getPropertyName(T item);
	
	/**
	 * Gets the property type.
	 * 
	 * @param item the item
	 * 
	 * @return the property type
	 */
	public String getPropertyType(T item);
	
	/**
	 * Gets the degree of fidelity.
	 * 
	 * @param item the item
	 * 
	 * @return the degree of fidelity
	 */
	public String getDegreeOfFidelity(T item);
	
	/**
	 * Gets the format.
	 * 
	 * @param item the item
	 * 
	 * @return the format
	 */
	public String getFormat(T item);
	
	/**
	 * Gets the checks if is active.
	 * 
	 * @param item the item
	 * 
	 * @return the checks if is active
	 */
	public boolean getIsActive(T item);
	
	/**
	 * Gets the language.
	 * 
	 * @param item the item
	 * 
	 * @return the language
	 */
	public String getLanguage(T item);
	
	/**
	 * Gets the match if no context.
	 * 
	 * @param item the item
	 * 
	 * @return the match if no context
	 */
	public boolean getMatchIfNoContext(T item);
	
	/**
	 * Gets the property value.
	 * 
	 * @param item the item
	 * 
	 * @return the property value
	 */
	public String getPropertyValue(T item);
	
	/**
	 * Gets the representational form.
	 * 
	 * @param item the item
	 * 
	 * @return the representational form
	 */
	public String getRepresentationalForm(T item);
}
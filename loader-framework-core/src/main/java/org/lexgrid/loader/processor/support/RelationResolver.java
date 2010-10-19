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
 * The Interface RelationResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface RelationResolver<T> {
	
	/**
	 * Gets the relation.
	 * 
	 * @param item the item
	 * 
	 * @return the relation
	 */
	public String getRelation(T item);
	
	/**
	 * Gets the relation namespace.
	 * 
	 * @param item the item
	 * 
	 * @return the relation namespace
	 */
	public String getRelationNamespace(T item);
	
	/**
	 * Gets the container name.
	 * 
	 * @return the container name
	 */
	public String getContainerName();
	
	/**
	 * Gets the source.
	 * 
	 * @param item the item
	 * 
	 * @return the source
	 */
	public String getSource(T item);
	
	/**
	 * Gets the source namespace.
	 * 
	 * @param item the item
	 * 
	 * @return the source namespace
	 */
	public String getSourceNamespace(T item);
	
	/**
	 * Gets the target.
	 * 
	 * @param item the item
	 * 
	 * @return the target
	 */
	public String getTarget(T item);
	
	/**
	 * Gets the target namespace.
	 * 
	 * @param item the item
	 * 
	 * @return the target namespace
	 */
	public String getTargetNamespace(T item);
	
	/**
	 * Gets the source scheme.
	 * 
	 * @param item the item
	 * 
	 * @return the source scheme
	 */
	public String getSourceScheme(T item);
}
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
 * The Interface MultipleRelationsRelationResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface MultipleRelationsRelationResolver<T> extends RelationResolver<T> {
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getRelation(java.lang.Object)
	 */
	public String getRelation(T item);
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getRelationNamespace(java.lang.Object)
	 */
	public String getRelationNamespace(T item);
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getContainerName()
	 */
	public String getContainerName();
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getSource(java.lang.Object)
	 */
	public String getSource(T item);
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getSourceNamespace(java.lang.Object)
	 */
	public String getSourceNamespace(T item);
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getTarget(java.lang.Object)
	 */
	public String getTarget(T item);
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getTargetNamespace(java.lang.Object)
	 */
	public String getTargetNamespace(T item);
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getSourceScheme(java.lang.Object)
	 */
	public String getSourceScheme(T item);
}

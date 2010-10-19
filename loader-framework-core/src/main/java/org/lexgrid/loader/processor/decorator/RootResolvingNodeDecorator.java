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
package org.lexgrid.loader.processor.decorator;

import java.util.List;

import org.lexgrid.loader.processor.support.RootNodeResolver;
import org.springframework.batch.item.ItemProcessor;

public interface RootResolvingNodeDecorator<I,O> extends ItemProcessor<I,List<O>>{

/**
	 * Gets the root node resolver.
	 * 
	 * @return the root node resolver
	 */
public RootNodeResolver<O> getRootNodeResolver();

	/**
	 * Sets the root node resolver.
	 * 
	 * @param rootNodeResolver the new root node resolver
	 */
	public void setRootNodeResolver(RootNodeResolver<O> rootNodeResolver);

	/**
	 * Checks if is replace relation.
	 * 
	 * @return true, if is replace relation
	 */
	public boolean isReplaceRelation();

	/**
	 * Sets the replace relation.
	 * 
	 * @param replaceRelation the new replace relation
	 */
	public void setReplaceRelation(boolean replaceRelation);
}
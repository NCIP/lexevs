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

import org.LexGrid.commonTypes.Property;
import org.apache.commons.collections.CollectionUtils;
import org.lexgrid.loader.data.DataUtils;
import org.lexgrid.loader.processor.EntityPropertyProcessor;
import org.lexgrid.loader.processor.support.OptionalPropertyQualifierResolver;
import org.lexgrid.loader.processor.support.SourceResolver;
import org.lexgrid.loader.wrappers.ParentIdHolder;
import org.springframework.batch.item.ItemProcessor;

public class EntityPropertyQualifierAndSourceAddingDecorator<I> implements ItemProcessor<I, ParentIdHolder<Property>> {

	private EntityPropertyProcessor<I> delegate;
	
	private List<OptionalPropertyQualifierResolver<I>> qualifierResolvers;
	
	private List<SourceResolver<I>> sourceResolvers;
	
	public EntityPropertyQualifierAndSourceAddingDecorator(EntityPropertyProcessor<I> delegate){
		this.delegate = delegate;
	}

	@Override
	public ParentIdHolder<Property> process(I item) throws Exception {
		ParentIdHolder<Property> prop = delegate.process(item);
		
		if(CollectionUtils.isNotEmpty(qualifierResolvers)) {
			
			for(OptionalPropertyQualifierResolver<I> qualifierResolver : this.qualifierResolvers) {
				if(qualifierResolver.toProcess(item)) {
					prop.getItem().addPropertyQualifier(
							DataUtils.createPropertyQualifier(qualifierResolver, item));
				}
			}
		}
		
		if(CollectionUtils.isNotEmpty(sourceResolvers)) {
			
			for(SourceResolver<I> sourceResolver : sourceResolvers) {
				prop.getItem().addSource(
						DataUtils.createSource(sourceResolver, item));
			}
		}
		
		return prop;
	}

	public List<SourceResolver<I>> getSourceResolvers() {
		return sourceResolvers;
	}

	public void setSourceResolvers(List<SourceResolver<I>> sourceResolvers) {
		this.sourceResolvers = sourceResolvers;
	}

	public List<OptionalPropertyQualifierResolver<I>> getQualifierResolvers() {
		return qualifierResolvers;
	}

	public void setQualifierResolvers(
			List<OptionalPropertyQualifierResolver<I>> qualifierResolvers) {
		this.qualifierResolvers = qualifierResolvers;
	}
}
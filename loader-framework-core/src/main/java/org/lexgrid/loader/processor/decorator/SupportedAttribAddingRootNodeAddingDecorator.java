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
package org.lexgrid.loader.processor.decorator;

import java.util.Arrays;
import java.util.List;

import org.LexGrid.persistence.model.EntityAssnsToEntity;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.processor.support.RootNodeResolver;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class EntityAssnToEntityRootNodeAddingDecorator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SupportedAttribAddingRootNodeAddingDecorator<I> implements RootResolvingNodeDecorator<I,EntityAssnsToEntity>, ItemProcessor<I, List<EntityAssnsToEntity>>{

	/** The supported attribute template. */
	private SupportedAttributeTemplate supportedAttributeTemplate;
	
	private RootResolvingNodeDecorator<I, EntityAssnsToEntity> delegate;
	
	public SupportedAttribAddingRootNodeAddingDecorator(RootResolvingNodeDecorator<I, EntityAssnsToEntity> delegate){
		this.delegate = delegate;
	}

	public List<EntityAssnsToEntity> process(I item) throws Exception {
		List<EntityAssnsToEntity> processedItems = delegate.process(item);
		for(EntityAssnsToEntity assoc : processedItems){
			String rootCode = getRootCode(assoc);
			if(rootCode != null){
				register(assoc, rootCode);
			}
		}
		return processedItems;
	}

	private void register(EntityAssnsToEntity item, String rootCode)
			throws Exception {
		supportedAttributeTemplate.addSupportedHierarchy(item.getCodingSchemeName(),
				"is_a", null, Arrays.asList(item.getEntityCode()), true, rootCode);
	}
	
	/**
	 * Gets the root code, null if none
	 * 
	 * @param rootNode the root node
	 * 
	 * @return the root code
	 */
	private String getRootCode(EntityAssnsToEntity rootNode){
		String source = rootNode.getSourceEntityCode();
		String target = rootNode.getTargetEntityCode();
		
		if(source.equals("@") || target.equals("@")){
			return "@";
		} else if(source.equals("@@") || target.equals("@@")){
			return "@@";
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.SupportedAttributeSupport#getSupportedAttributeTemplate()
	 */
	public SupportedAttributeTemplate getSupportedAttributeTemplate() {
		return supportedAttributeTemplate;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.SupportedAttributeSupport#setSupportedAttributeTemplate(org.lexgrid.loader.dao.template.SupportedAttributeTemplate)
	 */
	public void setSupportedAttributeTemplate(
			SupportedAttributeTemplate supportedAttributeTemplate) {
		this.supportedAttributeTemplate = supportedAttributeTemplate;
	}

	public RootNodeResolver<EntityAssnsToEntity> getRootNodeResolver() {
		return delegate.getRootNodeResolver();
	}

	public boolean isReplaceRelation() {
		return delegate.isReplaceRelation();
	}

	public void setReplaceRelation(boolean replaceRelation) {
		delegate.setReplaceRelation(replaceRelation);
	}

	public void setRootNodeResolver(
			RootNodeResolver<EntityAssnsToEntity> rootNodeResolver) {
		delegate.setRootNodeResolver(rootNodeResolver);
	}
}

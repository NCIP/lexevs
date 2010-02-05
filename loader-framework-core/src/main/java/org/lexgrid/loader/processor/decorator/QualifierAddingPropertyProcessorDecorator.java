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

import java.util.List;

import org.LexGrid.persistence.dao.LexEvsDao;
import org.LexGrid.persistence.model.EntityProperty;
import org.LexGrid.persistence.model.EntityPropertyMultiAttrib;
import org.LexGrid.persistence.model.EntityPropertyMultiAttribId;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.processor.support.MinimalMultiAttribResolver;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class QualifierAddingPropertyProcessorDecorator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class QualifierAddingPropertyProcessorDecorator<I> implements ItemProcessor<I,EntityProperty>{

	/** The decorated item processor. */
	private ItemProcessor<I,EntityProperty> decoratedItemProcessor;
	
	private List<MinimalMultiAttribResolver<I>> decoratorMultiAttribResolver;
	
	/** The supported attribute template. */
	private SupportedAttributeTemplate supportedAttributeTemplate;
	
	private LexEvsDao lexEvsDao;
	
	/**
	 * Instantiates a new qualifier adding property processor decorator.
	 * 
	 * @param itemProcessor the item processor
	 */
	public QualifierAddingPropertyProcessorDecorator(ItemProcessor<I,EntityProperty> itemProcessor){
		this.decoratedItemProcessor = itemProcessor;
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public EntityProperty process(I item) throws Exception {	
		EntityProperty prop = decoratedItemProcessor.process(item);
		
		if(decoratorMultiAttribResolver != null){
			for(MinimalMultiAttribResolver<I> resolver : decoratorMultiAttribResolver){
				EntityPropertyMultiAttrib qual = buildEntityPropertyMultiAttrib(item, prop, resolver);
				lexEvsDao.insert(qual);
			}
		}
		return prop;
	}
	
	protected EntityPropertyMultiAttrib buildEntityPropertyMultiAttrib(I item, EntityProperty property, 
			MinimalMultiAttribResolver<I> resolver) {
		EntityPropertyMultiAttrib qual = new EntityPropertyMultiAttrib();
		EntityPropertyMultiAttribId qualId = new EntityPropertyMultiAttribId();
		qual.setId(qualId);
		
		qual.setVal2(resolver.getVal2(item));
		
		qual.getId().setAttributeValue(resolver.getAttributeValue(item));
		qual.getId().setCodingSchemeName(
				property.getId().getCodingSchemeName());
		qual.getId().setEntityCode(
				property.getId().getEntityCode());
		qual.getId().setEntityCodeNamespace(
				property.getId().getEntityCodeNamespace());
		qual.getId().setPropertyId(
				property.getId().getPropertyId());
		qual.getId().setTypeName(
				resolver.getTypeName());
		qual.getId().setVal1(
				resolver.getVal1(item));
		
		return qual;
	}
	
	protected void registerEntityPropertyMultiAttrib(EntityPropertyMultiAttrib qualifier) {
		supportedAttributeTemplate.addSupportedPropertyQualifier(
				qualifier.getId().getCodingSchemeName(), 
				qualifier.getId().getTypeName(),
				null,
				qualifier.getId().getTypeName());	
	}

	public LexEvsDao getLexEvsDao() {
		return lexEvsDao;
	}

	public void setLexEvsDao(LexEvsDao lexEvsDao) {
		this.lexEvsDao = lexEvsDao;
	}

	public List<MinimalMultiAttribResolver<I>> getDecoratorMultiAttribResolver() {
		return decoratorMultiAttribResolver;
	}

	public void setDecoratorMultiAttribResolver(
			List<MinimalMultiAttribResolver<I>> decoratorMultiAttribResolver) {
		this.decoratorMultiAttribResolver = decoratorMultiAttribResolver;
	}

	public SupportedAttributeTemplate getSupportedAttributeTemplate() {
		return supportedAttributeTemplate;
	}

	public void setSupportedAttributeTemplate(
			SupportedAttributeTemplate supportedAttributeTemplate) {
		this.supportedAttributeTemplate = supportedAttributeTemplate;
	}

	public ItemProcessor<I, EntityProperty> getDecoratedItemProcessor() {
		return decoratedItemProcessor;
	}	
}

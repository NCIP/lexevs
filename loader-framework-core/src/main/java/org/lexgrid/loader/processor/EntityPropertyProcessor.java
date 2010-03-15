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
package org.lexgrid.loader.processor;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.database.key.EntityKeyResolver;
import org.lexgrid.loader.processor.support.PropertyResolver;
import org.lexgrid.loader.wrappers.ParentIdHolder;

/**
 * The Class EntityPropertyProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityPropertyProcessor<I> extends AbstractSupportedAttributeRegisteringProcessor<I, ParentIdHolder<Property>>{
	
	/** The property resolver. */
	private PropertyResolver<I> propertyResolver;
	
	private EntityKeyResolver entityKeyResolver;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public ParentIdHolder<Property> doProcess(I item) throws Exception {
		
		Property prop = new Property();
		
		String parentKey = entityKeyResolver.
			resolveKey(super.getCodingSchemeIdSetter().getCodingSchemeName(), 
								propertyResolver.getEntityCode(item), 
								this.getPropertyResolver().getEntityCodeNamespace(item));	
		
		prop.setPropertyId(propertyResolver.getId(item));
	
		Text text = DaoUtility.createText(
				propertyResolver.getPropertyValue(item), 
				propertyResolver.getFormat(item));
		
		prop.setValue(text);
		prop.setIsActive(propertyResolver.getIsActive(item));
		prop.setLanguage(propertyResolver.getLanguage(item));
		prop.setPropertyName(propertyResolver.getPropertyName(item));
		prop.setPropertyType(propertyResolver.getPropertyType(item));
	
		return new ParentIdHolder<Property>(
				this.getCodingSchemeIdSetter(),
				parentKey, prop);
	}
	
	protected void registerSupportedAttributes(SupportedAttributeTemplate supportedAttributeTemplate, ParentIdHolder<Property> item){
		supportedAttributeTemplate.addSupportedProperty(
				super.getCodingSchemeIdSetter().getCodingSchemeUri(), 
				super.getCodingSchemeIdSetter().getCodingSchemeVersion(),
				item.getItem().getPropertyName(), null, null);
		
		String language = item.getItem().getLanguage();
		if(language != null){
			supportedAttributeTemplate.addSupportedLanguage(
					super.getCodingSchemeIdSetter().getCodingSchemeUri(), 
					super.getCodingSchemeIdSetter().getCodingSchemeVersion(),
					language, null, null);
		}
	}

	/**
	 * Gets the property resolver.
	 * 
	 * @return the property resolver
	 */
	public PropertyResolver<I> getPropertyResolver() {
		return propertyResolver;
	}

	/**
	 * Sets the property resolver.
	 * 
	 * @param propertyResolver the new property resolver
	 */
	public void setPropertyResolver(PropertyResolver<I> propertyResolver) {
		this.propertyResolver = propertyResolver;
	}

	public EntityKeyResolver getEntityKeyResolver() {
		return entityKeyResolver;
	}

	public void setEntityKeyResolver(EntityKeyResolver entityKeyResolver) {
		this.entityKeyResolver = entityKeyResolver;
	}
}

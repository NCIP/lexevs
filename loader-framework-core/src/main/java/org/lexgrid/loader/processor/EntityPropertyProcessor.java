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

import org.LexGrid.persistence.model.EntityProperty;
import org.LexGrid.persistence.model.EntityPropertyId;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.processor.support.PropertyResolver;

/**
 * The Class EntityPropertyProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityPropertyProcessor<I> extends AbstractSupportedAttributeRegisteringProcessor<I, EntityProperty>{
	
	/** The property resolver. */
	private PropertyResolver<I> propertyResolver;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public EntityProperty doProcess(I item) throws Exception {
		EntityPropertyId propId = new EntityPropertyId();
		EntityProperty prop = new EntityProperty();
		
		propId.setCodingSchemeName(getCodingSchemeNameSetter().getCodingSchemeName());
		propId.setEntityCodeNamespace(getCodingSchemeNameSetter().getCodingSchemeName());
		propId.setEntityCode(propertyResolver.getEntityCode(item));	
		propId.setPropertyId(propertyResolver.getId(item));
		prop.setId(propId);
		
		prop.setDegreeOfFidelity(propertyResolver.getDegreeOfFidelity(item));
		prop.setFormat(propertyResolver.getFormat(item));
		prop.setIsActive(propertyResolver.getIsActive(item));
		prop.setLanguage(propertyResolver.getLanguage(item));
		prop.setMatchIfNoContext(propertyResolver.getMatchIfNoContext(item));
		prop.setPropertyName(propertyResolver.getPropertyName(item));
		prop.setPropertyType(propertyResolver.getPropertyType(item));
		prop.setPropertyValue(propertyResolver.getPropertyValue(item));
		prop.setRepresentationalForm(propertyResolver.getRepresentationalForm(item));
		
		//Set isPreferred to FALSE by default -- it is subClass Processor's
		//responsibility to set this as appropriate.
		prop.setIsPreferred(false);
			
		return prop;
	}
	
	protected void registerSupportedAttributes(SupportedAttributeTemplate supportedAttributeTemplate, EntityProperty item){
		supportedAttributeTemplate.addSupportedProperty(getCodingSchemeNameSetter().getCodingSchemeName(), item.getPropertyName(), null, null);
		
		String repForm = item.getRepresentationalForm();

		if(repForm != null){
			supportedAttributeTemplate.addSupportedRepresentationalForm(getCodingSchemeNameSetter().getCodingSchemeName(), repForm, null, null);
		}

		String language = item.getLanguage();
		if(language != null){
			supportedAttributeTemplate.addSupportedLanguage(getCodingSchemeNameSetter().getCodingSchemeName(), language, null, null);
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


}

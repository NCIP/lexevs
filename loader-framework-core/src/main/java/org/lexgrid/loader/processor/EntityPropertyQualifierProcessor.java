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

import org.LexGrid.commonTypes.PropertyQualifier;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.database.key.EntityPropertyKeyResolver;
import org.lexgrid.loader.processor.support.PropertyQualifierResolver;
import org.lexgrid.loader.processor.support.PropertyResolver;
import org.lexgrid.loader.wrappers.ParentIdHolder;

/**
 * The Class EntityPropertyQualifierProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityPropertyQualifierProcessor<I> extends AbstractSupportedAttributeRegisteringProcessor<I, ParentIdHolder<PropertyQualifier>>{

	private PropertyQualifierResolver<I> propertyQualifierResolver;
	
	private EntityPropertyKeyResolver entityPropertyKeyResolver;
	private PropertyResolver<I> propertyResolver;
	
	@Override
	public ParentIdHolder<PropertyQualifier> doProcess(I item) throws Exception {
		PropertyQualifier qual = new PropertyQualifier();
		qual.setPropertyQualifierName(propertyQualifierResolver.getPropertyQualifierName(item));
		qual.setPropertyQualifierType(propertyQualifierResolver.getPropertyQualifierType(item));
		qual.setValue(propertyQualifierResolver.getPropertyQualifierValue(item));
		
		String parentId = 
			entityPropertyKeyResolver.
				resolveKey(this.getCodingSchemeIdSetter().getCodingSchemeName(), 
						propertyResolver.getEntityCode(item), 
						propertyResolver.getEntityCodeNamespace(item), 
						propertyResolver.getId(item));
		
		return new ParentIdHolder<PropertyQualifier>(
				this.getCodingSchemeIdSetter(),
				parentId, qual);
	}

	@Override
	protected void registerSupportedAttributes(SupportedAttributeTemplate s,
			ParentIdHolder<PropertyQualifier> item) {
		// TODO Auto-generated method stub
		
	}



}

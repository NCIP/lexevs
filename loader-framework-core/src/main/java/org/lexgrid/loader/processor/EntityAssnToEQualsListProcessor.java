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

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.persistence.model.EntityAssnsToEquals;
import org.LexGrid.persistence.model.EntityAssnsToEqualsId;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.data.association.MultiAttribKeyResolver;
import org.lexgrid.loader.processor.support.OptionalQualifierResolver;
import org.lexgrid.loader.processor.support.QualifierResolver;

/**
 * The Class EntityAssnToEQualsListProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnToEQualsListProcessor<I> extends AbstractSupportedAttributeRegisteringProcessor<I, List<EntityAssnsToEquals>> {

	/** The key resolver. */
	private MultiAttribKeyResolver<I> keyResolver;
	
	/** The qualifier resolver. */
	private List<OptionalQualifierResolver<I>> optionalQualifierResolver;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public List<EntityAssnsToEquals> doProcess(I item) throws Exception {
		List<EntityAssnsToEquals> returnlist = 
			new ArrayList<EntityAssnsToEquals>();

		for(OptionalQualifierResolver<I> qualifierResolver : optionalQualifierResolver){
			if(qualifierResolver.toProcess(item)){
				returnlist.add(buildEntityAssnsToEquals(item, 
						qualifierResolver));
			}
		}
		return returnlist;	
	}

	@Override
	protected void registerSupportedAttributes(SupportedAttributeTemplate supportedAttributeTemplate,
			List<EntityAssnsToEquals> items) {
		for(EntityAssnsToEquals qual : items){
			supportedAttributeTemplate.addSupportedAssociationQualifier(
					super.getCodingSchemeNameSetter().getCodingSchemeName(), 
					qual.getId().getQualifierName(), 
					null, 
					qual.getId().getQualifierName());
		}		
	}

	protected EntityAssnsToEquals buildEntityAssnsToEquals(I item, QualifierResolver<I> qualifierResolver){
		EntityAssnsToEquals qual = new EntityAssnsToEquals();
		EntityAssnsToEqualsId qualId = new EntityAssnsToEqualsId();

		qualId.setCodingSchemeName(getCodingSchemeNameSetter().getCodingSchemeName());
		qualId.setQualifierName(qualifierResolver.getQualifierName());
		qualId.setQualifierValue(qualifierResolver.getQualifierValue(item));
		qualId.setMultiAttributesKey(keyResolver.resolveMultiAttributesKey(item));

		qual.setId(qualId);
		
		return qual;
	}

	/**
	 * Gets the key resolver.
	 * 
	 * @return the key resolver
	 */
	public MultiAttribKeyResolver<I> getKeyResolver() {
		return keyResolver;
	}

	/**
	 * Sets the key resolver.
	 * 
	 * @param keyResolver the new key resolver
	 */
	public void setKeyResolver(MultiAttribKeyResolver<I> keyResolver) {
		this.keyResolver = keyResolver;
	}

	public List<OptionalQualifierResolver<I>> getOptionalQualifierResolver() {
		return optionalQualifierResolver;
	}

	public void setOptionalQualifierResolver(
			List<OptionalQualifierResolver<I>> optionalQualifierResolver) {
		this.optionalQualifierResolver = optionalQualifierResolver;
	}
}

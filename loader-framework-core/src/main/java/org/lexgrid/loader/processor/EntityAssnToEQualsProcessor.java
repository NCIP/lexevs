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

import java.util.List;

import org.LexGrid.persistence.model.EntityAssnsToEquals;
import org.LexGrid.persistence.model.EntityAssnsToEqualsId;
import org.apache.commons.lang.StringUtils;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.data.association.MultiAttribKeyResolver;
import org.springframework.batch.item.ItemProcessor;
import org.lexgrid.loader.data.codingScheme.CodingSchemeNameSetter;
import org.lexgrid.loader.processor.support.QualifierResolver;

/**
 * The Class EntityAssnToEQualsProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnToEQualsProcessor<I> extends AbstractSupportedAttributeRegisteringProcessor<I, EntityAssnsToEquals> {

	/** The key resolver. */
	private MultiAttribKeyResolver<I> keyResolver;
	
	/** The qualifier resolver. */
	private QualifierResolver<I> qualifierResolver;
	
	private boolean skipNullValueQualifiers = true;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public EntityAssnsToEquals doProcess(I item) throws Exception {
		String qualifierValue = qualifierResolver.getQualifierValue(item);
		if(StringUtils.isEmpty(qualifierValue) && skipNullValueQualifiers) {
			return null;
		}
		EntityAssnsToEquals qual = new EntityAssnsToEquals();
		EntityAssnsToEqualsId qualId = new EntityAssnsToEqualsId();
		
		qualId.setCodingSchemeName(getCodingSchemeNameSetter().getCodingSchemeName());
		qualId.setQualifierName(qualifierResolver.getQualifierName());
		qualId.setQualifierValue(qualifierValue);
		qualId.setMultiAttributesKey(keyResolver.resolveMultiAttributesKey(item));
		
		qual.setId(qualId);
		return qual;	
	}
	
	@Override
	protected void registerSupportedAttributes(SupportedAttributeTemplate supportedAttributeTemplate,
			EntityAssnsToEquals item) {
			supportedAttributeTemplate.addSupportedAssociationQualifier(
					super.getCodingSchemeNameSetter().getCodingSchemeName(), 
					item.getId().getQualifierName(), 
					null, 
					item.getId().getQualifierName());	
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

	/**
	 * Gets the qualifier resolver.
	 * 
	 * @return the qualifier resolver
	 */
	public QualifierResolver<I> getQualifierResolver() {
		return qualifierResolver;
	}

	/**
	 * Sets the qualifier resolver.
	 * 
	 * @param qualifierResolver the new qualifier resolver
	 */
	public void setQualifierResolver(QualifierResolver<I> qualifierResolver) {
		this.qualifierResolver = qualifierResolver;
	}

	public boolean isSkipNullValueQualifiers() {
		return skipNullValueQualifiers;
	}

	public void setSkipNullValueQualifiers(boolean skipNullValueQualifiers) {
		this.skipNullValueQualifiers = skipNullValueQualifiers;
	}	
}

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

import org.LexGrid.relations.AssociationQualification;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.data.DataUtils;
import org.lexgrid.loader.data.association.AssociationInstanceIdResolver;
import org.lexgrid.loader.database.key.AssociationInstanceKeyResolver;
import org.lexgrid.loader.processor.support.OptionalQualifierResolver;
import org.lexgrid.loader.processor.support.QualifierResolver;
import org.lexgrid.loader.wrappers.ParentIdHolder;

/**
 * The Class EntityAssnToEQualsProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnToEQualsProcessor<I> extends AbstractSupportedAttributeRegisteringProcessor<I, ParentIdHolder<AssociationQualification>> {

	/** The key resolver. */
	private AssociationInstanceIdResolver<I> associationInstanceIdResolver;
	
	private AssociationInstanceKeyResolver associationInstanceKeyResolver;
	
	/** The qualifier resolver. */
	private OptionalQualifierResolver<I> qualifierResolver;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public ParentIdHolder<AssociationQualification> doProcess(I item) throws Exception {
		if(!qualifierResolver.toProcess(item)) {
			return null;
		}
		
		AssociationQualification qual = DataUtils.createAssociationQualifier(qualifierResolver, item);

		String associationInstanceId = associationInstanceIdResolver.resolveAssociationInstanceId(item);
		return new ParentIdHolder<AssociationQualification>(
				this.getCodingSchemeIdSetter(),
				associationInstanceKeyResolver.
					resolveKey(this.getCodingSchemeIdSetter().getCodingSchemeName(), associationInstanceId), 
					qual);
	}

	@Override
	protected void registerSupportedAttributes(SupportedAttributeTemplate s,
			ParentIdHolder<AssociationQualification> item) {
		this.getSupportedAttributeTemplate().addSupportedAssociationQualifier(
				super.getCodingSchemeIdSetter().getCodingSchemeUri(), 
				super.getCodingSchemeIdSetter().getCodingSchemeVersion(),
				item.getItem().getAssociationQualifier(),
				null, 
				null);	
		
	}
	
	public AssociationInstanceIdResolver<I> getAssociationInstanceIdResolver() {
		return associationInstanceIdResolver;
	}

	public void setAssociationInstanceIdResolver(
			AssociationInstanceIdResolver<I> associationInstanceIdResolver) {
		this.associationInstanceIdResolver = associationInstanceIdResolver;
	}

	public AssociationInstanceKeyResolver getAssociationInstanceKeyResolver() {
		return associationInstanceKeyResolver;
	}

	public void setAssociationInstanceKeyResolver(
			AssociationInstanceKeyResolver associationInstanceKeyResolver) {
		this.associationInstanceKeyResolver = associationInstanceKeyResolver;
	}

	/**
	 * Gets the qualifier resolver.
	 * 
	 * @return the qualifier resolver
	 */
	public OptionalQualifierResolver<I> getQualifierResolver() {
		return qualifierResolver;
	}

	/**
	 * Sets the qualifier resolver.
	 * 
	 * @param qualifierResolver the new qualifier resolver
	 */
	public void setQualifierResolver(OptionalQualifierResolver<I> qualifierResolver) {
		this.qualifierResolver = qualifierResolver;
	}
}

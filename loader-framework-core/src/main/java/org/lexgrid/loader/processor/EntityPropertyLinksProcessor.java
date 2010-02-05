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

import org.LexGrid.persistence.model.EntityPropertyLinks;
import org.LexGrid.persistence.model.EntityPropertyLinksId;
import org.lexgrid.loader.data.codingScheme.CodingSchemeNameSetter;
import org.lexgrid.loader.processor.support.PropertyLinkResolver;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class EntityPropertyLinksProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityPropertyLinksProcessor<I> extends CodingSchemeNameAwareProcessor implements ItemProcessor<I, EntityPropertyLinks> {

	/** The property link resolver. */
	private PropertyLinkResolver<I> propertyLinkResolver;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public EntityPropertyLinks process(I item) throws Exception {
		EntityPropertyLinks propertyLink = new EntityPropertyLinks();
		EntityPropertyLinksId propertyLinkId = new EntityPropertyLinksId();
		propertyLink.setId(propertyLinkId);
		propertyLink.getId().setCodingSchemeName(getCodingSchemeNameSetter().getCodingSchemeName());
		propertyLink.getId().setEntityCodeNamespace(getCodingSchemeNameSetter().getCodingSchemeName());
		propertyLink.getId().setEntityCode(propertyLinkResolver.getEntityCode(item));
		propertyLink.getId().setLink(propertyLinkResolver.getLink(item));
		propertyLink.getId().setSourcePropertyId(propertyLinkResolver.getSourceId(item));
		propertyLink.getId().setTargetPropertyId(propertyLinkResolver.getTargetId(item));
		
		return propertyLink;
	}

	/**
	 * Gets the property link resolver.
	 * 
	 * @return the property link resolver
	 */
	public PropertyLinkResolver<I> getPropertyLinkResolver() {
		return propertyLinkResolver;
	}

	/**
	 * Sets the property link resolver.
	 * 
	 * @param propertyLinkResolver the new property link resolver
	 */
	public void setPropertyLinkResolver(PropertyLinkResolver<I> propertyLinkResolver) {
		this.propertyLinkResolver = propertyLinkResolver;
	}
}

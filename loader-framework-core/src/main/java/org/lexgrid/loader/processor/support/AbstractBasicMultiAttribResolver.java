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
package org.lexgrid.loader.processor.support;

import org.lexgrid.loader.data.property.IndividualIdSetter;

/**
 * The Class AbstractBasicMultiAttribResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractBasicMultiAttribResolver<I> extends AbstractNullValueSkippingOptionalMultiAttribResolver<I> {
	
	/** The entity code resolver. */
	private EntityCodeResolver<I> entityCodeResolver;
	
	/** The individual id setter. */
	private IndividualIdSetter<I> individualIdSetter;

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.MultiAttribResolver#getEntityCode(java.lang.Object)
	 */
	public String getEntityCode(I item) {
		return entityCodeResolver.getEntityCode(item);
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.MultiAttribResolver#getId(java.lang.Object)
	 */
	public String getId(I item) {
		return individualIdSetter.addId(item);
	}
	
	/**
	 * Gets the entity code resolver.
	 * 
	 * @return the entity code resolver
	 */
	public EntityCodeResolver<I> getEntityCodeResolver() {
		return entityCodeResolver;
	}
	
	/**
	 * Sets the entity code resolver.
	 * 
	 * @param entityCodeResolver the new entity code resolver
	 */
	public void setEntityCodeResolver(EntityCodeResolver<I> entityCodeResolver) {
		this.entityCodeResolver = entityCodeResolver;
	}
	
	/**
	 * Gets the individual id setter.
	 * 
	 * @return the individual id setter
	 */
	public IndividualIdSetter<I> getIndividualIdSetter() {
		return individualIdSetter;
	}
	
	/**
	 * Sets the individual id setter.
	 * 
	 * @param individualIdSetter the new individual id setter
	 */
	public void setIndividualIdSetter(IndividualIdSetter<I> individualIdSetter) {
		this.individualIdSetter = individualIdSetter;
	}	
}

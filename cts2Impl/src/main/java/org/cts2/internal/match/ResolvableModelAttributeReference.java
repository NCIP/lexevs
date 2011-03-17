/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.internal.match;

import org.cts2.core.ModelAttributeReference;

/**
 * The Class ResolvableModelAttributeReference.
 *
 * @param <T> the
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ResolvableModelAttributeReference<T> extends ModelAttributeReference {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5500382462242484409L;
	
	/** The attribute resolver. */
	private AttributeResolver<T> attributeResolver;
	
	/**
	 * Instantiates a new resolvable model attribute reference.
	 *
	 * @param attributeResolver the attribute resolver
	 */
	public ResolvableModelAttributeReference(AttributeResolver<T> attributeResolver){
		this.attributeResolver = attributeResolver;
	}
	
	/**
	 * Gets the model attribute value.
	 *
	 * @param modelObject the model object
	 * @return the model attribute value
	 */
	public String getModelAttributeValue(T modelObject){
		return this.attributeResolver.resolveAttribute(modelObject);
	}

}

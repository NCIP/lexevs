/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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

import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class AbstractSupportedAttributeRegisteringProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractSupportedAttributeRegisteringProcessor<I,O> extends CodingSchemeIdAwareProcessor implements ItemProcessor<I,O>{

	/** The supported attribute template. */
	private SupportedAttributeTemplate supportedAttributeTemplate;
	
	/** The register supported attributes. */
	private boolean registerSupportedAttributes = true;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public final O process(I item) throws Exception {
		O output = this.doProcess(item);

		if(registerSupportedAttributes && output != null) {
			this.registerSupportedAttributes(this.getSupportedAttributeTemplate(),
					output);
		}
		return output;
	}
	
	/**
	 * Do process.
	 * 
	 * @param item the item
	 * 
	 * @return the o
	 * 
	 * @throws Exception the exception
	 */
	public abstract O doProcess(I item) throws Exception;
		
	/**
	 * Register supported attributes.
	 * 
	 * @param s the s
	 * @param item the item
	 */
	protected abstract void registerSupportedAttributes(SupportedAttributeTemplate s, O item);
	
	/**
	 * Gets the supported attribute template.
	 * 
	 * @return the supported attribute template
	 */
	public SupportedAttributeTemplate getSupportedAttributeTemplate() {
		return supportedAttributeTemplate;
	}

	/**
	 * Sets the supported attribute template.
	 * 
	 * @param supportedAttributeTemplate the new supported attribute template
	 */
	public void setSupportedAttributeTemplate(
			SupportedAttributeTemplate supportedAttributeTemplate) {
		this.supportedAttributeTemplate = supportedAttributeTemplate;
	}

	/**
	 * Checks if is register supported attributes.
	 * 
	 * @return true, if is register supported attributes
	 */
	public boolean isRegisterSupportedAttributes() {
		return registerSupportedAttributes;
	}

	/**
	 * Sets the register supported attributes.
	 * 
	 * @param registerSupportedAttributes the new register supported attributes
	 */
	public void setRegisterSupportedAttributes(boolean registerSupportedAttributes) {
		this.registerSupportedAttributes = registerSupportedAttributes;
	}
}
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
package org.lexgrid.loader.meta.processor;

import java.util.Map;

import org.LexGrid.persistence.model.EntityPropertyMultiAttrib;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.processor.EntityPropertyQualifierProcessor;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class MetaSupportedSourceProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaSupportedSourceProcessor extends EntityPropertyQualifierProcessor<Mrconso> {

	/** The iso map. */
	private Map<String,String> isoMap;

	@Override
	protected void registerSupportedAttributes(
			SupportedAttributeTemplate template,
			EntityPropertyMultiAttrib source) {
		template.addSupportedSource(
				this.getCodingSchemeIdSetter().getCodingSchemeName(), 
				source.getId().getAttributeValue(), 
				isoMap.get(source.getId().getAttributeValue()), 
				null, 
				null);
	}
	
	/**
	 * Gets the iso map.
	 * 
	 * @return the iso map
	 */
	public Map<String, String> getIsoMap() {
		return isoMap;
	}

	/**
	 * Sets the iso map.
	 * 
	 * @param isoMap the iso map
	 */
	public void setIsoMap(Map<String, String> isoMap) {
		this.isoMap = isoMap;
	}
}

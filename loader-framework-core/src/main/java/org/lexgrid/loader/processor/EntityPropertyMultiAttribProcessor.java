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

import org.LexGrid.persistence.model.EntityPropertyMultiAttrib;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.processor.support.MultiAttribResolver;

/**
 * The Class EntityPropertyMultiAttribProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityPropertyMultiAttribProcessor<I> extends AbstractEntityPropertyMultiAttribProcessor<I, EntityPropertyMultiAttrib>{

	/** The multi attrib resolver. */
	private MultiAttribResolver<I> multiAttribResolver;

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@Override
	public EntityPropertyMultiAttrib doProcess(I item) throws Exception {
		return buildPropertyQualifier(multiAttribResolver, item);
	}
	
	@Override
	protected void registerSupportedAttributes(SupportedAttributeTemplate template,
			EntityPropertyMultiAttrib qualifier) {
		super.registerEntityPropertyMultiAttrib(template, qualifier);
	}

	public MultiAttribResolver<I> getMultiAttribResolver() {
		return multiAttribResolver;
	}

	public void setMultiAttribResolver(MultiAttribResolver<I> multiAttribResolver) {
		this.multiAttribResolver = multiAttribResolver;
	}
}

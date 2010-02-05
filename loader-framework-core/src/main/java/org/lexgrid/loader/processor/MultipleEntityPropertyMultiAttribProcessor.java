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

import org.LexGrid.persistence.model.EntityPropertyMultiAttrib;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.processor.support.OptionalMultiAttribResolver;

/**
 * The Class MultipleEntityPropertyMultiAttribProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MultipleEntityPropertyMultiAttribProcessor<I> extends AbstractEntityPropertyMultiAttribProcessor<I, List<EntityPropertyMultiAttrib>>{

	/** The multi attrib resolver. */
	private List<OptionalMultiAttribResolver<I>> optionalMultiAttribResolverList;

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@Override
	public List<EntityPropertyMultiAttrib> doProcess(I item) throws Exception {
		List<EntityPropertyMultiAttrib> returnList = new ArrayList<EntityPropertyMultiAttrib>();
		for(OptionalMultiAttribResolver<I> resolver : optionalMultiAttribResolverList) {
			EntityPropertyMultiAttrib multiAttrib = 
				buildPropertyQualifier(resolver, item);

			if(resolver.toProcess(item)){
				returnList.add(multiAttrib);
			}
		}	
		return returnList;
	}

	@Override
	protected void registerSupportedAttributes(SupportedAttributeTemplate template,
			List<EntityPropertyMultiAttrib> qualifiers) {
		for(EntityPropertyMultiAttrib qual : qualifiers) {
			super.registerEntityPropertyMultiAttrib(template, qual);
		}
	}

	public List<OptionalMultiAttribResolver<I>> getOptionalMultiAttribResolverList() {
		return optionalMultiAttribResolverList;
	}

	public void setOptionalMultiAttribResolverList(
			List<OptionalMultiAttribResolver<I>> optionalMultiAttribResolverList) {
		this.optionalMultiAttribResolverList = optionalMultiAttribResolverList;
	}
}

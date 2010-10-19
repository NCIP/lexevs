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
package org.lexgrid.loader.umls.processor;

import org.LexGrid.commonTypes.Property;
import org.lexgrid.loader.processor.EntityPropertyProcessor;
import org.lexgrid.loader.rrf.model.Mrsty;
import org.lexgrid.loader.wrappers.ParentIdHolder;

/**
 * The Class MrstyPropertyProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrstyPropertyProcessor extends EntityPropertyProcessor<Mrsty>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.EntityPropertyProcessor#process(java.lang.Object)
	 */
	/**
	 * Mrsty Properties are always set to 'isPreferred' = false;
	 * This processor assumes that all skippable entries in Mrsty have been skipped.
	 * 
	 * @param item the item
	 * 
	 * @return the entity property
	 * 
	 * @throws Exception the exception
	 */
	@Override
	public ParentIdHolder<Property> doProcess(Mrsty item) throws Exception {
		ParentIdHolder<Property> prop = super.doProcess(item);

		return prop;
	}
}
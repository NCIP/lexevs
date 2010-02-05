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
package org.lexgrid.loader.meta.processor.support;

import org.lexgrid.loader.processor.support.AbstractBasicMultiAttribResolver;

import org.lexgrid.loader.rrf.model.Mrsat;

/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 */
public class MetaMrsatMetauiMultiAttribResolver extends
		AbstractBasicMultiAttribResolver<Mrsat> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.MultiAttribResolver#getAttributeValue(java.lang.Object)
	 */
	public String getAttributeValue(Mrsat item) {
		// TODO Auto-generated method stub
		return "METAUI";
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.MultiAttribResolver#getTypeName()
	 */
	public String getTypeName() {
		// TODO Auto-generated method stub
		return "qualifier";
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.MultiAttribResolver#getVal1(java.lang.Object)
	 */
	public String getVal1(Mrsat item) {
		// TODO Auto-generated method stub
		return item.getMetaui();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.MultiAttribResolver#getVal2(java.lang.Object)
	 */
	public String getVal2(Mrsat item) {
		// TODO Auto-generated method stub
		return null;
	}

}

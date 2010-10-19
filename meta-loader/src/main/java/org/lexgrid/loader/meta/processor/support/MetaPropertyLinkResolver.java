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
package org.lexgrid.loader.meta.processor.support;

import org.lexgrid.loader.processor.support.PropertyLinkResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants.RrfRelationType;
import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class MetaPropertyLinkResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaPropertyLinkResolver implements PropertyLinkResolver<Mrrel>{

	/** The Rrfrelation type. */
	private RrfRelationType RrfrelationType;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyLinkResolver#getEntityCode(java.lang.Object)
	 */
	public String getEntityCode(Mrrel item) {
		return item.getCui1();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyLinkResolver#getLink(java.lang.Object)
	 */
	public String getLink(Mrrel item) {
		if(RrfrelationType == RrfRelationType.REL){
			return item.getRel();
		} else if (RrfrelationType == RrfRelationType.RELA){
			return item.getRela();
		} else {
			throw new RuntimeException("RRF Relation Type must be set.");
		}
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyLinkResolver#getSourceId(java.lang.Object)
	 */
	public String getSourceId(Mrrel item) {
		return item.getAui1();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyLinkResolver#getTargetId(java.lang.Object)
	 */
	public String getTargetId(Mrrel item) {
		return item.getAui2();
	}
	
	/**
	 * Gets the rrfrelation type.
	 * 
	 * @return the rrfrelation type
	 */
	public RrfRelationType getRrfrelationType() {
		return RrfrelationType;
	}

	/**
	 * Sets the rrfrelation type.
	 * 
	 * @param rrfrelationType the new rrfrelation type
	 */
	public void setRrfrelationType(RrfRelationType rrfrelationType) {
		RrfrelationType = rrfrelationType;
	}
}
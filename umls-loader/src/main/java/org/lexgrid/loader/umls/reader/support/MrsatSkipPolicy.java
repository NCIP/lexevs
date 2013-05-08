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
package org.lexgrid.loader.umls.reader.support;

import org.apache.commons.lang.StringUtils;
import org.lexgrid.loader.rrf.data.property.MrsatUtility;
import org.lexgrid.loader.rrf.model.Mrsat;

/**
 * The Class MrsatSkipPolicy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrsatSkipPolicy extends AbstractSabSkippingPolicy<Mrsat> {

	/** The mrsat utility. */
	private MrsatUtility mrsatUtility;

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.umls.reader.support.AbstractSabSkippingPolicy#toSkip(java.lang.Object)
	 */
	public boolean toSkip(Mrsat item) {
		if(StringUtils.isBlank(item.getCode())) {
			return true;
		}
		
		if(super.toSkip(item)){
			return true;
		}
		return mrsatUtility.toSkip(item);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.umls.reader.support.AbstractSabSkippingPolicy#getSab(java.lang.Object)
	 */
	@Override
	public String getSab(Mrsat item) {
		return item.getSab();
	}

	/**
	 * Gets the mrsat utility.
	 * 
	 * @return the mrsat utility
	 */
	public MrsatUtility getMrsatUtility() {
		return mrsatUtility;
	}

	/**
	 * Sets the mrsat utility.
	 * 
	 * @param mrsatUtility the new mrsat utility
	 */
	public void setMrsatUtility(MrsatUtility mrsatUtility) {
		this.mrsatUtility = mrsatUtility;
	}

}
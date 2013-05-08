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
package org.lexgrid.loader.rxn.processor.support;

import org.lexgrid.loader.processor.support.EntityCodeResolver;
import org.lexgrid.loader.rrf.data.entity.DefaultMrconsoNoCodeHandler;
import org.lexgrid.loader.rrf.data.entity.NoCodeHandler;
import org.lexgrid.loader.rrf.model.Mrconso;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

/**
 * The Class RxnMrconsoEntityCodeResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RxnMrconsoEntityCodeResolver implements EntityCodeResolver<Mrconso>{

	/** The no code handler. */
	private NoCodeHandler<Mrconso> noCodeHandler = new DefaultMrconsoNoCodeHandler();
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityCodeResolver#getEntityCode(java.lang.Object)
	 */
	public String getEntityCode(Mrconso item){
		if(item.getCode().equals(RrfLoaderConstants.NO_CODE)){
			return noCodeHandler.handleNoCode(item);
		} else {
			return item.getCode();
		}
	}

	/**
	 * Gets the no code handler.
	 * 
	 * @return the no code handler
	 */
	public NoCodeHandler<Mrconso> getNoCodeHandler() {
		return noCodeHandler;
	}

	/**
	 * Sets the no code handler.
	 * 
	 * @param noCodeHandler the new no code handler
	 */
	public void setNoCodeHandler(NoCodeHandler<Mrconso> noCodeHandler) {
		this.noCodeHandler = noCodeHandler;
	}
}

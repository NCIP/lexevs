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
package org.lexgrid.loader.processor.support.validator;

import java.util.List;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

/**
 * The Class ListValidatorAdapter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ListValidatorAdapter<I> implements Validator<List<I>> {

	/** The delegate. */
	private Validator<I> delegate;
	
	/**
	 * Instantiates a new list validator adapter.
	 * 
	 * @param delegate the delegate
	 */
	public ListValidatorAdapter(Validator<I> delegate){
		this.delegate = delegate;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.validator.Validator#validate(java.lang.Object)
	 */
	public void validate(List<I> items) throws ValidationException {
		for(I item : items){
			delegate.validate(item);
		}
	}
}
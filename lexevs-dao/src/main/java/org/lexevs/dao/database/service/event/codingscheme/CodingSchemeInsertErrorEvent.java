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
package org.lexevs.dao.database.service.event.codingscheme;

import org.LexGrid.codingSchemes.CodingScheme;

/**
 * The Class CodingSchemeInsertErrorEvent.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodingSchemeInsertErrorEvent<T extends Exception> {

	/** The original coding scheme. */
	private CodingScheme codingScheme;
	
	/** The exception. */
	private T exception;

	/**
	 * Instantiates a new coding scheme insert error event.
	 * 
	 * @param codingScheme the coding scheme
	 * @param exception the exception
	 */
	public CodingSchemeInsertErrorEvent(CodingScheme codingScheme,
			T exception) {
		super();
		this.codingScheme = codingScheme;
		this.exception = exception;
	}

	/**
	 * Gets the coding scheme.
	 * 
	 * @return the coding scheme
	 */
	public CodingScheme getCodingScheme() {
		return codingScheme;
	}

	/**
	 * Sets the coding scheme.
	 * 
	 * @param codingScheme the new coding scheme
	 */
	public void setCodingScheme(CodingScheme codingScheme) {
		this.codingScheme = codingScheme;
	}

	/**
	 * Gets the exception.
	 * 
	 * @return the exception
	 */
	public T getException() {
		return exception;
	}

	/**
	 * Sets the exception.
	 * 
	 * @param exception the new exception
	 */
	public void setException(T exception) {
		this.exception = exception;
	}
}
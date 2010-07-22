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
package org.lexevs.cts2.exception;

import java.util.Date;
import java.util.UUID;

import org.LexGrid.LexBIG.Exceptions.LBException;

/**
 * The Class AbstractCTS2CheckedException.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractCTS2CheckedException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8963284974393488275L;
	
	/** The exception identifier. */
	private String exceptionIdentifier;
	
	/** The exception date. */
	private Date exceptionDate;
	
	/**
	 * Instantiates a new abstract ct s2 checked exception.
	 * 
	 * @param message the message
	 */
	protected AbstractCTS2CheckedException(String message) {
		super(message);
		this.exceptionIdentifier = UUID.randomUUID().toString();
		this.exceptionDate = new Date();
	}
	
	protected AbstractCTS2CheckedException(String message, LBException exception) {
		super(message + "\n\n" +
				"  -- LexEVS Exception: " + exception.getMessage());
		this.exceptionIdentifier = UUID.randomUUID().toString();
		this.exceptionDate = new Date();
	}

	/**
	 * Gets the exception identifier.
	 * 
	 * @return the exception identifier
	 */
	public String getExceptionIdentifier() {
		return exceptionIdentifier;
	}

	/**
	 * Sets the exception identifier.
	 * 
	 * @param exceptionIdentifier the new exception identifier
	 */
	public void setExceptionIdentifier(String exceptionIdentifier) {
		this.exceptionIdentifier = exceptionIdentifier;
	}

	/**
	 * Gets the exception date.
	 * 
	 * @return the exception date
	 */
	public Date getExceptionDate() {
		return exceptionDate;
	}

	/**
	 * Sets the exception date.
	 * 
	 * @param exceptionDate the new exception date
	 */
	public void setExceptionDate(Date exceptionDate) {
		this.exceptionDate = exceptionDate;
	}
}

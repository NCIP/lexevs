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
package org.lexevs.dao.database.service.error;

import java.util.Date;
import java.util.UUID;

/**
 * The Class DefaultDatabaseError.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultDatabaseError implements DatabaseError {

	/** The error object. */
	private Object errorObject;
	
	/** The error exception. */
	private Exception errorException;
	
	/** The unique error id. */
	private String uniqueErrorId = UUID.randomUUID().toString();
	
	/** The error time. */
	private Date errorTime = new Date();
	
	private String errorCode;
    

	/**
	 * Instantiates a new default database error.
	 * 
	 * @param errorObject the error object
	 * @param errorException the error exception
	 */
	public DefaultDatabaseError(String errorCode, Object errorObject, Exception errorException) {
		super();
		this.errorObject = errorObject;
		this.errorException = errorException;
		this.errorCode = errorCode;
	}
	
	/**
	 * Gets the unique error id.
	 * 
	 * @return the unique error id
	 */
	public String getUniqueErrorId() {
		return uniqueErrorId;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.error.DatabaseError#getErrorObject()
	 */
	public Object getErrorObject() {
		return errorObject;
	}
	
	/**
	 * Sets the error object.
	 * 
	 * @param errorObject the new error object
	 */
	public void setErrorObject(Object errorObject) {
		this.errorObject = errorObject;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.error.DatabaseError#getErrorException()
	 */
	public Exception getErrorException() {
		return errorException;
	}
	
	/**
	 * Sets the error exception.
	 * 
	 * @param errorException the new error exception
	 */
	public void setErrorException(Exception errorException) {
		this.errorException = errorException;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.error.DatabaseError#getErrorTime()
	 */
	@Override
	public Date getErrorTime() {
		return errorTime;
	}

	public String getErrorCode() {
		return errorCode;
	}
}

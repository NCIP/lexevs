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

/**
 * The Interface DatabaseError.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface DatabaseError {

	public static String UNKNOWN_ERROR_CODE = "UNKNOWN_ERROR";

	/**
	 * Gets the error time.
	 * 
	 * @return the error time
	 */
	public Date getErrorTime();
	
    /**
     * Gets the error object.
     * 
     * @return the error object
     */
    public Object getErrorObject();
    
    /**
     * Gets the error exception.
     * 
     * @return the error exception
     */
    public Exception getErrorException();
    
    public String getUniqueErrorId();
    
    /**
     * Gets the error code.
     * 
     * @return the error code
     */
    public String getErrorCode();
}

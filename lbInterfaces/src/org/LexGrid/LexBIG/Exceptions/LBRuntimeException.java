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
package org.LexGrid.LexBIG.Exceptions;

/**
 * Superclass for unchecked exceptions issued by the LexBIG runtime.
 * <p>
 * Note: Runtime exceptions are used to indicate a programming problem
 * detected by the runtime system or inappropriate use of the API.
 * Intended to be used sparingly in cases where the cost of requesting
 * a check on the exception exceeds the benefit of catching
 * or declaring it.
 * 
 * @version 1.0
 * @created 27-Jan-2006 9:19:38 PM
 */
public class LBRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 4347639326985296334L;

	public LBRuntimeException(String message){
		super(message);
	}

	public LBRuntimeException(String message, Throwable cause){
		super(message, cause);
	}

}
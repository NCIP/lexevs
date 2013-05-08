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
package org.LexGrid.LexBIG.Exceptions;



public class LBRevisionException extends LBException {

/**
	 * 
	 */
private static final long serialVersionUID = 6335488236350630144L;

	public LBRevisionException(String message, Throwable cause) {
		super(message, cause);
	}

	public LBRevisionException(String message) {
		super(message);
	}
	
	public LBRevisionException(Throwable cause) {
		super(cause.getMessage(), cause);
	}

}
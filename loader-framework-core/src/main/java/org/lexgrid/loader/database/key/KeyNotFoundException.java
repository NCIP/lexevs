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
package org.lexgrid.loader.database.key;

/**
 * The Class KeyNotFoundException.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class KeyNotFoundException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8751575597071563864L;
	
	/**
	 * Instantiates a new key not found exception.
	 * 
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 */
	public KeyNotFoundException(String entityCode, String entityCodeNamespace){
		super("Could not map Entity Code: " + entityCode + " - Namespace: " + entityCodeNamespace + " to an EntityUid.");
	}
}
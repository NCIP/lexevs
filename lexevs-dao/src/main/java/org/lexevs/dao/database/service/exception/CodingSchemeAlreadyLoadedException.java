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
package org.lexevs.dao.database.service.exception;

import org.LexGrid.LexBIG.Exceptions.LBException;

/**
 * The Class CodingSchemeAlreadyLoadedException.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodingSchemeAlreadyLoadedException extends LBException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1857840295359784201L;

	/**
	 * Instantiates a new coding scheme already loaded exception.
	 * 
	 * @param uri the uri
	 * @param version the version
	 */
	public CodingSchemeAlreadyLoadedException(String uri, String version) {
		super("Coding Scheme URI: " + uri + " Version: " + version + " is already loaded in the system.");
	}
}
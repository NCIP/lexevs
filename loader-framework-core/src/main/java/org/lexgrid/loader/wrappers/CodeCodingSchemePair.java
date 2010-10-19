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
package org.lexgrid.loader.wrappers;

import java.io.Serializable;

/**
 * The Class CodeCodingSchemePair.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodeCodingSchemePair implements Serializable {
	
	/** The code. */
	private String code;
	
	/** The coding scheme. */
	private String codingScheme;
	
	public CodeCodingSchemePair(){}
	
	public CodeCodingSchemePair(String code, String codingScheme){
		this.code = code;
		this.codingScheme = codingScheme;
	}
	
	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Sets the code.
	 * 
	 * @param code the new code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * Gets the coding scheme.
	 * 
	 * @return the coding scheme
	 */
	public String getCodingScheme() {
		return codingScheme;
	}
	
	/**
	 * Sets the coding scheme.
	 * 
	 * @param codingScheme the new coding scheme
	 */
	public void setCodingScheme(String codingScheme) {
		this.codingScheme = codingScheme;
	}	
}
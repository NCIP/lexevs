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
package org.lexevs.dao.database.ibatis.parameter;

/**
 * The Class PrefixedParameterTriple.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PrefixedParameterTriple extends PrefixedParameterTuple{
	
	/** The param3. */
	private String param3;
	
	/**
	 * Instantiates a new prefixed parameter triple.
	 */
	public PrefixedParameterTriple() {
		super();
	}
	
	/**
	 * Instantiates a new prefixed parameter triple.
	 * 
	 * @param prefix the prefix
	 * @param param1 the param1
	 * @param param2 the param2
	 * @param param3 the param3
	 */
	public PrefixedParameterTriple(String prefix, String param1, String param2, String param3) {
		super(prefix, param1, param2);
		this.param3 = param3;
	}
	
	/**
	 * Gets the param3.
	 * 
	 * @return the param3
	 */
	public String getParam3() {
		return param3;
	}
	
	/**
	 * Sets the param3.
	 * 
	 * @param param3 the new param3
	 */
	public void setParam3(String param3) {
		this.param3 = param3;
	}
}
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
 * The Class PrefixedParameterQuad.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public class PrefixedParameterQuad extends PrefixedParameterTriple{
	
	/** The param3. */
	private String param4;
	
	/**
	 * Instantiates a new prefixed parameter quad.
	 */
	public PrefixedParameterQuad() {
		super();
	}
	
	/**
	 * Instantiates a new prefixed parameter quad.
	 * 
	 * @param prefix the prefix
	 * @param param1 the param1
	 * @param param2 the param2
	 * @param param3 the param3
	 * @param param4 the param4
	 */
	public PrefixedParameterQuad(String prefix, String param1, String param2, String param3, String param4) {
		super(prefix, param1, param2, param3);
		this.param4 = param4;
	}
	
	/**
	 * Gets the param4.
	 * 
	 * @return the param4
	 */
	public String getParam4() {
		return param4;
	}
	
	/**
	 * Sets the param4.
	 * 
	 * @param param3 the new param4
	 */
	public void setParam4(String param4) {
		this.param4 = param4;
	}
}
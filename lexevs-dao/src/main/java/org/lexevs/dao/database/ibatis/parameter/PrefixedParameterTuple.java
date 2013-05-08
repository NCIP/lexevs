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
 * The Class PrefixedParameterTuple.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PrefixedParameterTuple extends PrefixedParameter{
	
	/** The param2. */
	private String param2;
	
	/**
	 * Instantiates a new prefixed parameter tuple.
	 */
	public PrefixedParameterTuple(){
		super();
	}
	
	/**
	 * Instantiates a new prefixed parameter tuple.
	 * 
	 * @param prefix the prefix
	 * @param param1 the param1
	 * @param param2 the param2
	 */
	public PrefixedParameterTuple(String prefix, String param1, String param2) {
		super(prefix, param1);
		this.param2 = param2;
	}
	
	/**
	 * Gets the param2.
	 * 
	 * @return the param2
	 */
	public String getParam2() {
		return param2;
	}
	
	/**
	 * Sets the param2.
	 * 
	 * @param param2 the new param2
	 */
	public void setParam2(String param2) {
		this.param2 = param2;
	}

}
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
package org.lexevs.dao.database.ibatis.parameter;

/**
 * The Class PrefixedParameter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PrefixedParameter extends PrefixedTableParameterBean{

	/** The param1. */
	private String param1;
	
	/**
	 * Instantiates a new prefixed parameter.
	 */
	public PrefixedParameter(){
		super();
	}
	
	/**
	 * Instantiates a new prefixed parameter.
	 * 
	 * @param prefix the prefix
	 * @param param1 the param1
	 */
	public PrefixedParameter(String prefix, String param1) {
		super(prefix);
		this.param1 = param1;
	}
	
	/**
	 * Gets the param1.
	 * 
	 * @return the param1
	 */
	public String getParam1() {
		return param1;
	}
	
	/**
	 * Sets the param1.
	 * 
	 * @param param1 the new param1
	 */
	public void setParam1(String param1) {
		this.param1 = param1;
	}
}

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

import java.util.HashMap;

import org.springframework.util.Assert;

public class SequentialMappedParameterBean extends HashMap<String,Object>{

	private static String PARAMETER_PREFIX = "param";
	private static String PREFIX_PARAMETER = "prefix";
	private String ACTUAL_PREFIX_PARAMETER = "actualTableSetPrefix";

	private static final long serialVersionUID = 4510691698169582467L;

	public SequentialMappedParameterBean(Object... parameters) {
		Assert.notNull(parameters);

		int currentIndex = 1;
		
		for(Object param : parameters) {
			this.put(PARAMETER_PREFIX + Integer.toString(currentIndex++), param);
		}
	}

	public void setPrefix(String prefix) {
		this.put(PREFIX_PARAMETER, prefix);
	}
	
	public void setActualTableSetPrefix(String prefix) {
		this.put(ACTUAL_PREFIX_PARAMETER, prefix);
	}
}
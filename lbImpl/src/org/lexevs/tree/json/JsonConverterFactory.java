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
package org.lexevs.tree.json;

import org.lexevs.tree.service.ApplicationContextFactory;


/**
 * A factory for creating JsonConverter objects.
 */
@Deprecated
public class JsonConverterFactory {
	
	/**
	 * Gets the json converter.
	 * 
	 * @return the json converter
	 */
	public static JsonConverter getJsonConverter(){
		return (JsonConverter)ApplicationContextFactory.getInstance().getApplicationContext().getBean("childPagingJsonConverter");
	}
}
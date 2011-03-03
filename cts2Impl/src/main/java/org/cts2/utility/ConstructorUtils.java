/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.utility;

import org.cts2.service.core.NameOrURI;

/**
 * The Class ConstructorUtils.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ConstructorUtils {

	/**
	 * Uri to name or uri.
	 *
	 * @param uri the uri
	 * @return the name or uri
	 */
	public static NameOrURI uriToNameOrURI(String uri){
		NameOrURI nameOrUri = new NameOrURI();
		nameOrUri.setUri(uri);
		
		return nameOrUri;
	}
	
	/**
	 * Name to name or uri.
	 *
	 * @param name the name
	 * @return the name or uri
	 */
	public static NameOrURI nameToNameOrURI(String name){
		NameOrURI nameOrUri = new NameOrURI();
		nameOrUri.setName(name);
		
		return nameOrUri;
	}
}

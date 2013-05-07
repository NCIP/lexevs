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
package org.lexevs.dao.database.prefix;

/**
 * The Interface PrefixResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface PrefixResolver {

	public static String historyPrefix = "h_";
	/**
	 * Resolve default prefix.
	 * 
	 * @return the string
	 */
	public String resolveDefaultPrefix();

	/**
	 * Resolve prefix for coding scheme.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @return the string
	 */
	public String resolvePrefixForCodingScheme(String codingSchemeUri, String version);
	
	/**
	 * Resolve prefix for coding scheme.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * 
	 * @return the string
	 */
	public String resolvePrefixForCodingScheme(String codingSchemeId);
	
	/**
	 * Resolve prefix for history coding scheme.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * 
	 * @return the string
	 */
	public String resolvePrefixForHistoryCodingScheme(String codingSchemeId);
	
	/**
	 * Gets the next coding scheme prefix.
	 * 
	 * @return the next coding scheme prefix
	 */
	public String getNextCodingSchemePrefix();

	/**
	 * Resolve history prefix.
	 * 
	 * @return the string
	 */
	public String resolveHistoryPrefix();
}
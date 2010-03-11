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
package org.lexevs.dao.test;

import org.lexevs.dao.database.prefix.PrefixResolver;

/**
 * The Class StaticPrefixResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class StaticPrefixResolver implements PrefixResolver {

	/** The prefix. */
	private String prefix = "";
	
	/** The history prefix. */
	private String historyPrefix = "_h";
	
	/**
	 * Instantiates a new static prefix resolver.
	 */
	public StaticPrefixResolver(){
		super();
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.prefix.PrefixResolver#resolveHistoryPrefix()
	 */
	@Override
	public String resolveHistoryPrefix() {
		return prefix + historyPrefix;
	}
	
	/**
	 * Instantiates a new static prefix resolver.
	 * 
	 * @param prefix the prefix
	 */
	public StaticPrefixResolver(String prefix){
		this.prefix = prefix;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.prefix.PrefixResolver#resolveDefaultPrefix()
	 */
	public String resolveDefaultPrefix() {
		return prefix;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.prefix.PrefixResolver#resolvePrefixForCodingScheme(java.lang.String, java.lang.String)
	 */
	public String resolvePrefixForCodingScheme(String codingSchemeName,
			String version) {
		return prefix;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.prefix.PrefixResolver#resolvePrefixForCodingScheme(java.lang.String)
	 */
	public String resolvePrefixForCodingScheme(String codingSchemeId) {
		return prefix;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.prefix.PrefixResolver#getNextCodingSchemePrefix()
	 */
	public String getNextCodingSchemePrefix() {
		return prefix;
	}

	/**
	 * Gets the prefix.
	 * 
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Sets the prefix.
	 * 
	 * @param prefix the new prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Sets the history prefix.
	 * 
	 * @param historyPrefix the new history prefix
	 */
	public void setHistoryPrefix(String historyPrefix) {
		this.historyPrefix = historyPrefix;
	}

	/**
	 * Gets the history prefix.
	 * 
	 * @return the history prefix
	 */
	public String getHistoryPrefix() {
		return historyPrefix;
	}

	
}

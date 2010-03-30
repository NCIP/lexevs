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
package org.lexevs.dao.database.prefix;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.constants.SystemVariables;

/**
 * The Class DefaultPrefixResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultPrefixResolver implements PrefixResolver {

	/** The system variables. */
	private SystemVariables systemVariables;
	
	/** The registry. */
	private Registry registry;
	
	/** The history prefix. */
	private String historyPrefix = "h_";
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.prefix.PrefixResolver#resolveDefaultPrefix()
	 */
	public String resolveDefaultPrefix() {
		return systemVariables.getAutoLoadDBPrefix();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.prefix.PrefixResolver#resolveHistoryPrefix()
	 */
	public String resolveHistoryPrefix() {
		return this.resolveDefaultPrefix() + historyPrefix;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.prefix.PrefixResolver#resolvePrefixForCodingScheme(java.lang.String, java.lang.String)
	 */
	public String resolvePrefixForCodingScheme(String codingSchemeUri,
			String version) {
		try {
			//TODO: Enable this for multiple tables
			/*
			RegistryEntry entry = registry.getCodingSchemeEntry(
					DaoUtility.createAbsoluteCodingSchemeVersionReference(codingSchemeUri, version));
			
			String entryPrefix = entry.getPrefix();
			
			return resolveDefaultPrefix() + entryPrefix;
			*/
					
			return this.resolveDefaultPrefix();
		} catch (Exception e) {
			throw new RuntimeException("CodingScheme Uri: " + codingSchemeUri + " Version: " + version + " not found.");
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.prefix.PrefixResolver#resolvePrefixForCodingScheme(java.lang.String)
	 */
	public String resolvePrefixForCodingScheme(String codingSchemeId) {
		return resolveDefaultPrefix();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.prefix.PrefixResolver#getNextCodingSchemePrefix()
	 */
	public String getNextCodingSchemePrefix() {
		try {
			return registry.getNextDBIdentifier();
		} catch (LBInvocationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the registry.
	 * 
	 * @return the registry
	 */
	public Registry getRegistry() {
		return registry;
	}

	/**
	 * Sets the registry.
	 * 
	 * @param registry the new registry
	 */
	public void setRegistry(Registry registry) {
		this.registry = registry;
	}
	

	/**
	 * Gets the system variables.
	 * 
	 * @return the system variables
	 */
	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	/**
	 * Sets the system variables.
	 * 
	 * @param systemVariables the new system variables
	 */
	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
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

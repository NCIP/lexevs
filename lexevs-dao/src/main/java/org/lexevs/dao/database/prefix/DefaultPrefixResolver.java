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
import org.apache.commons.lang.StringUtils;
import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.system.constants.SystemVariables;

/**
 * The Class DefaultPrefixResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Cacheable(cacheName="ResourceCache")
public class DefaultPrefixResolver implements PrefixResolver {

	/** The system variables. */
	private SystemVariables systemVariables;
	
	/** The registry. */
	private Registry registry;
	
	/** The history prefix. */
	private String historyPrefix = "h_";
	
	private static String BLANK_PREFIX = "";
	
	private DatabaseServiceManager databaseServiceManager;
	
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
	@CacheMethod
	public String resolvePrefixForCodingScheme(String codingSchemeUri,
			String version) {
		try {

			RegistryEntry entry = registry.getCodingSchemeEntry(
					DaoUtility.createAbsoluteCodingSchemeVersionReference(codingSchemeUri, version));
			
			String entryPrefix = entry.getPrefix();
			
			entryPrefix = allowForNullPrefixValue(entryPrefix);
			
			return this.resolveDefaultPrefix() + entryPrefix;

		} catch (LBParameterException e) {
			throw new RuntimeException("CodingScheme Uri: " + codingSchemeUri + " Version: " + version + " not found.");
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.prefix.PrefixResolver#resolvePrefixForCodingScheme(java.lang.String)
	 */
	@CacheMethod
	public String resolvePrefixForCodingScheme(final String codingSchemeId) {
		String prefix =
			databaseServiceManager.getDaoCallbackService().executeInDaoLayer(new DaoCallback<String>() {

				@Override
				public String execute(DaoManager daoManager) {
					for(RegistryEntry entry : registry.getAllRegistryEntriesOfType(ResourceType.CODING_SCHEME)){
						String foundCodingSchemeId = daoManager.getCurrentCodingSchemeDao().
							getCodingSchemeUIdByUriAndVersion(entry.getResourceUri(), entry.getResourceVersion());
						
						if(foundCodingSchemeId.equals(codingSchemeId)) {
							return entry.getPrefix();
						}
					}
					throw new RuntimeException("CodingScheme ID: " + codingSchemeId + " not found.");
				}

			});
		
		prefix = allowForNullPrefixValue(prefix);

		return this.resolveDefaultPrefix() + prefix;
	}

	private String allowForNullPrefixValue(String prefix) {
		if(StringUtils.isBlank(prefix)) {
			prefix = BLANK_PREFIX;
		}
		return prefix;
	}

	@CacheMethod
	public String resolvePrefixForHistoryCodingScheme(final String codingSchemeId) {

		String prefix = this.resolvePrefixForCodingScheme(codingSchemeId);
		
		prefix += historyPrefix;
		
		return prefix ;
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

	public void setDatabaseServiceManager(DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}

	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}
}

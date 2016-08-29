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
package org.lexevs.registry.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.cache.annotation.ParameterKey;
import org.lexevs.registry.model.RegistryEntry;

/**
 * The Class DelegatingDatabaseToXmlRegistry.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Cacheable(cacheName="DelegatingDatabaseToXmlRegistryCache")
public class DelegatingDatabaseToXmlRegistry implements Registry {
	
	/** The database registry. */
	private Registry databaseRegistry;
	
	/** The xml registry. */
	private Registry xmlRegistry;

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#addNewItem(org.lexevs.registry.model.RegistryEntry)
	 */         
	@ClearCache(clearCaches = {"DatabaseRegistryCache","DelegatingSystemResourceServiceCache"})
	public void addNewItem(RegistryEntry entry) throws Exception {
	    databaseRegistry.addNewItem(entry);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getAllRegistryEntries()
	 */
	@CacheMethod
	public List<RegistryEntry> getAllRegistryEntries() {
		List<RegistryEntry> allEntries = new ArrayList<RegistryEntry>();
		allEntries.addAll(this.databaseRegistry.getAllRegistryEntries());
		allEntries.addAll(this.xmlRegistry.getAllRegistryEntries());
		
		return allEntries;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getAllRegistryEntriesOfType(org.lexevs.registry.service.Registry.ResourceType)
	 */
	@CacheMethod
	public List<RegistryEntry> getAllRegistryEntriesOfType(ResourceType type) {
		List<RegistryEntry> allEntries = new ArrayList<RegistryEntry>();
		allEntries.addAll(this.databaseRegistry.getAllRegistryEntriesOfType(type));
		allEntries.addAll(this.xmlRegistry.getAllRegistryEntriesOfType(type));
		
		return allEntries;
	}
	
	@CacheMethod
	public List<RegistryEntry> getAllRegistryEntriesOfTypeAndURI(ResourceType type, String uri) {
		List<RegistryEntry> allEntries = new ArrayList<RegistryEntry>();
		allEntries.addAll(this.databaseRegistry.getAllRegistryEntriesOfTypeAndURI(type, uri));
		allEntries.addAll(this.xmlRegistry.getAllRegistryEntriesOfTypeAndURI(type, uri));
		
		return allEntries;
	}
	
	@CacheMethod
	public List<RegistryEntry> getAllRegistryEntriesOfTypeURIAndVersion(ResourceType type, String uri, String version) {
		List<RegistryEntry> allEntries = new ArrayList<RegistryEntry>();
		allEntries.addAll(this.databaseRegistry.getAllRegistryEntriesOfTypeURIAndVersion(type, uri, version));
		allEntries.addAll(this.xmlRegistry.getAllRegistryEntriesOfTypeURIAndVersion(type, uri, version));
		
		return allEntries;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getEntriesForUri(java.lang.String)
	 */
	@CacheMethod
	public List<RegistryEntry> getEntriesForUri(String uri)
			throws LBParameterException {
		List<RegistryEntry> allEntries = new ArrayList<RegistryEntry>();
		
		allEntries.addAll(this.databaseRegistry.getEntriesForUri(uri));
		allEntries.addAll(this.xmlRegistry.getEntriesForUri(uri));
		
		return allEntries;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getLastUpdateTime()
	 */
	public Date getLastUpdateTime() {
		Date date1 = databaseRegistry.getLastUpdateTime();
		Date date2 = xmlRegistry.getLastUpdateTime();
		
		if(date1.after(date2)) {
			return date1;
		} else {
			return date2;
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getNextDBIdentifier()
	 */
	public String getNextDBIdentifier() throws LBInvocationException {
		return databaseRegistry.getNextDBIdentifier();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getNextHistoryIdentifier()
	 */
	public String getNextHistoryIdentifier() throws LBInvocationException {
		return databaseRegistry.getNextHistoryIdentifier();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#containsCodingSchemeEntry(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	@CacheMethod
	public boolean containsCodingSchemeEntry(
			@ParameterKey(field = { "_codingSchemeURN" , "_codingSchemeVersion"}) 
			AbsoluteCodingSchemeVersionReference codingScheme) {
		return databaseRegistry.containsCodingSchemeEntry(codingScheme)
			||
			xmlRegistry.containsCodingSchemeEntry(codingScheme);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#containsNonCodingSchemeEntry(java.lang.String)
	 */
	@CacheMethod
	public boolean containsNonCodingSchemeEntry(String uri) {
		return databaseRegistry.containsNonCodingSchemeEntry(uri) ||
			xmlRegistry.containsNonCodingSchemeEntry(uri);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getCodingSchemeEntry(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	@CacheMethod
	public RegistryEntry getCodingSchemeEntry(
			@ParameterKey(field = { "_codingSchemeURN", "_codingSchemeVersion" })
			AbsoluteCodingSchemeVersionReference codingScheme)
			throws LBParameterException {
		if(this.databaseRegistry.containsCodingSchemeEntry(codingScheme)){
			return databaseRegistry.getCodingSchemeEntry(codingScheme);
		} else if(this.xmlRegistry.containsCodingSchemeEntry(codingScheme)){
			return xmlRegistry.getCodingSchemeEntry(codingScheme);
		}
		
		else throw new RuntimeException("No CodingScheme Entry for URI: " 
				+ codingScheme.getCodingSchemeURN() + ", Version: " 
				+ codingScheme.getCodingSchemeVersion());
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getNonCodingSchemeEntry(java.lang.String)
	 */
	@CacheMethod
	public RegistryEntry getNonCodingSchemeEntry(String uri)
			throws LBParameterException {
		if(this.databaseRegistry.containsNonCodingSchemeEntry(uri)) {
			return databaseRegistry.getNonCodingSchemeEntry(uri);
		} else {
			return this.xmlRegistry.getNonCodingSchemeEntry(uri);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#removeEntry(org.lexevs.registry.model.RegistryEntry)
	 */
	@ClearCache(clearCaches = {"DatabaseRegistryCache","DelegatingSystemResourceServiceCache"})
	public void removeEntry(RegistryEntry entry) throws LBParameterException {
		databaseRegistry.removeEntry(entry);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#updateEntry(org.lexevs.registry.model.RegistryEntry)
	 */
	@ClearCache(clearCaches = {"DatabaseRegistryCache","DelegatingSystemResourceServiceCache"})
	public void updateEntry(RegistryEntry entry) throws LBParameterException {
		if(entry.getResourceType().equals(ResourceType.CODING_SCHEME)) {
			
			AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
			ref.setCodingSchemeURN(entry.getResourceUri());
			ref.setCodingSchemeVersion(entry.getResourceVersion());
			
			if(this.databaseRegistry.containsCodingSchemeEntry(ref)){
				 databaseRegistry.updateEntry(entry);
			} else {
				 xmlRegistry.updateEntry(entry);
			}	
		} else {
			
			if(this.databaseRegistry.containsNonCodingSchemeEntry(entry.getResourceUri())){
				 databaseRegistry.updateEntry(entry);
			} else {
				 xmlRegistry.updateEntry(entry);
			}	
		}
	}
	
	/**
	 * Update coding scheme entry tag.
	 * 
	 * @param codingScheme the coding scheme
	 * @param newTag the new tag
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	@ClearCache(clearCaches = {"DatabaseRegistryCache","DelegatingSystemResourceServiceCache"})
	public void updateCodingSchemeEntryTag(
			AbsoluteCodingSchemeVersionReference codingScheme, String newTag)
			throws LBParameterException {
		if(this.databaseRegistry.containsCodingSchemeEntry(codingScheme)){
			RegistryEntry entry = databaseRegistry.getCodingSchemeEntry(codingScheme);
			entry.setTag(newTag);
			databaseRegistry.updateEntry(entry);
		} else {
			RegistryEntry entry = xmlRegistry.getCodingSchemeEntry(codingScheme);
			entry.setTag(newTag);
			xmlRegistry.updateEntry(entry);
		}	
	}
	
	/**
	 * Gets the database registry.
	 * 
	 * @return the database registry
	 */
	public Registry getDatabaseRegistry() {
		return databaseRegistry;
	}

	/**
	 * Sets the database registry.
	 * 
	 * @param databaseRegistry the new database registry
	 */
	public void setDatabaseRegistry(Registry databaseRegistry) {
		this.databaseRegistry = databaseRegistry;
	}

	/**
	 * Gets the xml registry.
	 * 
	 * @return the xml registry
	 */
	public Registry getXmlRegistry() {
		return xmlRegistry;
	}

	/**
	 * Sets the xml registry.
	 * 
	 * @param xmlRegistry the new xml registry
	 */
	public void setXmlRegistry(Registry xmlRegistry) {
		this.xmlRegistry = xmlRegistry;
	}
}
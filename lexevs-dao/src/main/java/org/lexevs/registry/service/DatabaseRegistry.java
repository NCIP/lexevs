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
package org.lexevs.registry.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.dao.database.access.registry.RegistryDao;
import org.lexevs.dao.database.prefix.NextDatabasePrefixGenerator;
import org.lexevs.registry.event.RegistryEventSupport;
import org.lexevs.registry.model.RegistryEntry;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class DatabaseRegistry.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DatabaseRegistry extends RegistryEventSupport implements Registry {
	
	/** The registry dao. */
	private RegistryDao registryDao;
	
	/** The next database prefix generator. */
	private NextDatabasePrefixGenerator nextDatabasePrefixGenerator;

	/**
	 * Activate.
	 * 
	 * @param codingScheme the coding scheme
	 * 
	 * @throws LBInvocationException the LB invocation exception
	 * @throws LBParameterException the LB parameter exception
	 */
	@Transactional
	public void activate(AbsoluteCodingSchemeVersionReference codingScheme)
			throws LBInvocationException, LBParameterException {
		RegistryEntry entry = registryDao.getRegistryEntryForUriAndVersion(
				codingScheme.getCodingSchemeURN(), 
				codingScheme.getCodingSchemeVersion());
		
		entry.setStatus(CodingSchemeVersionStatus.ACTIVE.toString());
		
		registryDao.updateRegistryEntry(entry);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#addNewItem(org.lexevs.registry.model.RegistryEntry)
	 */
	@Transactional
	public void addNewItem(RegistryEntry entry)
			throws Exception {
		
		registryDao.insertRegistryEntry(entry);	
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getAllRegistryEntries()
	 */
	@Transactional
	public List<RegistryEntry> getAllRegistryEntries() {
		return registryDao.getAllRegistryEntries();
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getAllRegistryEntriesOfType(org.lexevs.registry.service.Registry.ResourceType)
	 */
	@Transactional
	public List<RegistryEntry> getAllRegistryEntriesOfType(ResourceType type) {
		List<RegistryEntry> returnList = new ArrayList<RegistryEntry>();
		
		returnList.addAll(registryDao.getAllRegistryEntriesOfType(type));
		
		return returnList;
	}
	
	@Transactional
	public List<RegistryEntry> getAllRegistryEntriesOfTypeAndURI(ResourceType type, String uri) {
		List<RegistryEntry> returnList = new ArrayList<RegistryEntry>();
		
		returnList.addAll(registryDao.getAllRegistryEntriesOfTypeAndURI(type, uri));
		
		return returnList;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getEntriesForUri(java.lang.String)
	 */
	@Transactional
	public List<RegistryEntry> getEntriesForUri(String uri)
			throws LBParameterException {
		return 
				registryDao.getRegistryEntriesForUri(uri);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getLastUpdateTime()
	 */
	@Transactional
	public Date getLastUpdateTime() {
		return this.registryDao.getLastUpdateTime();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getNextDBIdentifier()
	 */
	@Transactional
	public String getNextDBIdentifier() throws LBInvocationException {
		String currentDbIdentifier = registryDao.getLastUsedDbIdentifier();
		String nextDbIdentifier = nextDatabasePrefixGenerator.generateNextDatabasePrefix(currentDbIdentifier);
		registryDao.updateLastUsedDbIdentifier(nextDbIdentifier);
		
		return nextDbIdentifier;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getNextHistoryIdentifier()
	 */
	public String getNextHistoryIdentifier() throws LBInvocationException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#containsCodingSchemeEntry(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	@Transactional
	public boolean containsCodingSchemeEntry(
			AbsoluteCodingSchemeVersionReference codingScheme) {
		try {
			RegistryEntry entry = registryDao.getRegistryEntryForUriAndVersion(
					codingScheme.getCodingSchemeURN(), codingScheme.getCodingSchemeVersion());
		} catch (LBParameterException e) {
			return false;
		}
			
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#updateEntry(org.lexevs.registry.model.RegistryEntry)
	 */
	@Transactional
	public void updateEntry(
			RegistryEntry entry){
		this.registryDao.updateRegistryEntry(entry);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getCodingSchemeEntry(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	@Transactional
	public RegistryEntry getCodingSchemeEntry(
			AbsoluteCodingSchemeVersionReference codingScheme)
			throws LBParameterException {
		return registryDao.getRegistryEntryForUriAndVersion(codingScheme.getCodingSchemeURN(), codingScheme.getCodingSchemeVersion());
	}


	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getNonCodingSchemeEntry(java.lang.String)
	 */
	public RegistryEntry getNonCodingSchemeEntry(String uri)
			throws LBParameterException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#removeEntry(org.lexevs.registry.model.RegistryEntry)
	 */
	@Transactional
	public void removeEntry(RegistryEntry entry) throws LBParameterException {
		registryDao.deleteRegistryEntry(entry);	
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#containsNonCodingSchemeEntry(java.lang.String)
	 */
	public boolean containsNonCodingSchemeEntry(String uri) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Sets the registry dao.
	 * 
	 * @param registryDao the new registry dao
	 */
	public void setRegistryDao(RegistryDao registryDao) {
		this.registryDao = registryDao;
	}

	/**
	 * Gets the registry dao.
	 * 
	 * @return the registry dao
	 */
	public RegistryDao getRegistryDao() {
		return registryDao;
	}

	/**
	 * Gets the next database prefix generator.
	 * 
	 * @return the next database prefix generator
	 */
	public NextDatabasePrefixGenerator getNextDatabasePrefixGenerator() {
		return nextDatabasePrefixGenerator;
	}

	/**
	 * Sets the next database prefix generator.
	 * 
	 * @param nextDatabasePrefixGenerator the new next database prefix generator
	 */
	public void setNextDatabasePrefixGenerator(
			NextDatabasePrefixGenerator nextDatabasePrefixGenerator) {
		this.nextDatabasePrefixGenerator = nextDatabasePrefixGenerator;
	}	
}

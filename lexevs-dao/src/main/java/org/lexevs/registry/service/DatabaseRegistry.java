
package org.lexevs.registry.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.apache.commons.collections.CollectionUtils;
import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.cache.annotation.ParameterKey;
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
@Cacheable(cacheName="DatabaseRegistryCache")
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
	@ClearCache(clearCaches = {"DelegatingDatabaseToXmlRegistryCache","DelegatingSystemResourceServiceCache"})
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
	@ClearCache(clearCaches = {"DelegatingDatabaseToXmlRegistryCache","DelegatingSystemResourceServiceCache"})
	public void addNewItem(RegistryEntry entry)
			throws Exception {
		
		registryDao.insertRegistryEntry(entry);	
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getAllRegistryEntries()
	 */
	@Transactional
	@CacheMethod
	public List<RegistryEntry> getAllRegistryEntries() {
		return registryDao.getAllRegistryEntries();
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getAllRegistryEntriesOfType(org.lexevs.registry.service.Registry.ResourceType)
	 */
	@Transactional
	@CacheMethod
	public List<RegistryEntry> getAllRegistryEntriesOfType(ResourceType type) {
		List<RegistryEntry> returnList = new ArrayList<RegistryEntry>();
		
		returnList.addAll(registryDao.getAllRegistryEntriesOfType(type));
		
		return returnList;
	}
	
	@Transactional
	@CacheMethod
	public List<RegistryEntry> getAllRegistryEntriesOfTypeAndURI(ResourceType type, String uri) {
		List<RegistryEntry> returnList = new ArrayList<RegistryEntry>();
		
		returnList.addAll(registryDao.getAllRegistryEntriesOfTypeAndURI(type, uri));
		
		return returnList;
	}
	
	@Transactional
	@CacheMethod
	public List<RegistryEntry> getAllRegistryEntriesOfTypeURIAndVersion(ResourceType type, String uri, String version) {
		List<RegistryEntry> returnList = new ArrayList<RegistryEntry>();
		
		returnList.addAll(registryDao.getAllRegistryEntriesOfTypeURIAndVersion(type, uri, version));
		
		return returnList;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getEntriesForUri(java.lang.String)
	 */
	@Transactional
	@CacheMethod
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
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#containsCodingSchemeEntry(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	@Transactional
	@CacheMethod
	public boolean containsCodingSchemeEntry(
			@ParameterKey(field = { "_codingSchemeURN" , "_codingSchemeVersion"}) 
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
	@ClearCache(clearCaches = {"DelegatingDatabaseToXmlRegistryCache","DelegatingSystemResourceServiceCache"})
	public void updateEntry(
			RegistryEntry entry){
		this.registryDao.updateRegistryEntry(entry);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getCodingSchemeEntry(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	@Transactional
	@CacheMethod
	public RegistryEntry getCodingSchemeEntry(
			@ParameterKey(field = { "_codingSchemeURN" , "_codingSchemeVersion"}) 
			AbsoluteCodingSchemeVersionReference codingScheme)
			throws LBParameterException {
		return registryDao.getRegistryEntryForUriAndVersion(codingScheme.getCodingSchemeURN(), codingScheme.getCodingSchemeVersion());
	}


	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getNonCodingSchemeEntry(java.lang.String)
	 */
	@CacheMethod
	public RegistryEntry getNonCodingSchemeEntry(String uri)
			throws LBParameterException {
		List<RegistryEntry> entries = registryDao.getAllRegistryEntriesOfUriAndTypes(uri, getNonCodingSchemeResourceTypes());
		
		if(CollectionUtils.isNotEmpty(entries)) {
			if(entries.size() > 1) {
				throw new LBParameterException("More than one Non-CodingScheme Resource is registered in the system " +
						"with a URI of: " + uri);
			}
			return entries.get(0);
		}
		
		throw new LBParameterException("No Non-CodingScheme Resource is registered in the system " +
				"with a URI of: " + uri);	
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#removeEntry(org.lexevs.registry.model.RegistryEntry)
	 */
	@Transactional
	@ClearCache(clearCaches = {"DelegatingDatabaseToXmlRegistryCache","DelegatingSystemResourceServiceCache"})
	public void removeEntry(RegistryEntry entry) throws LBParameterException {
		registryDao.deleteRegistryEntry(entry);	
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#containsNonCodingSchemeEntry(java.lang.String)
	 */
	@Transactional
	public boolean containsNonCodingSchemeEntry(String uri) {
		List<RegistryEntry> entries =
				registryDao.getAllRegistryEntriesOfUriAndTypes(uri, getNonCodingSchemeResourceTypes());
				
		return CollectionUtils.isNotEmpty(entries);
	}
	
	protected ResourceType[] getNonCodingSchemeResourceTypes() {
		return new ResourceType[] {
				ResourceType.NCI_HISTORY,
				ResourceType.PICKLIST_DEFINITION,
				ResourceType.VALUESET_DEFINITION};
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
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

public class DatabaseRegistry extends RegistryEventSupport implements Registry {
	
	private RegistryDao registryDao;
	
	private NextDatabasePrefixGenerator nextDatabasePrefixGenerator;

	@Transactional
	public void activate(AbsoluteCodingSchemeVersionReference codingScheme)
			throws LBInvocationException, LBParameterException {
		RegistryEntry entry = registryDao.getRegistryEntryForUriAndVersion(
				codingScheme.getCodingSchemeURN(), 
				codingScheme.getCodingSchemeVersion());
		
		entry.setStatus(CodingSchemeVersionStatus.ACTIVE.toString());
		
		registryDao.updateRegistryEntry(entry);
	}

	@Transactional
	public void addNewItem(RegistryEntry entry)
			throws Exception {
		
		registryDao.insertRegistryEntry(entry);
		
	}

	@Transactional
	public List<RegistryEntry> getAllRegistryEntries() {
		return registryDao.getAllRegistryEntries();
	}
	
	@Transactional
	public List<RegistryEntry> getAllRegistryEntriesOfType(ResourceType type) {
		List<RegistryEntry> returnList = new ArrayList<RegistryEntry>();
		
		returnList.addAll(registryDao.getAllRegistryEntriesOfType(type));
		
		return returnList;
	}

	@Transactional
	public List<RegistryEntry> getEntriesForUri(String uri)
			throws LBParameterException {
		return 
				registryDao.getRegistryEntriesForUri(uri);
	}

	@Transactional
	public Date getLastUpdateTime() {
		return this.registryDao.getLastUpdateTime();
	}

	@Transactional
	public String getNextDBIdentifier() throws LBInvocationException {
		String currentDbIdentifier = registryDao.getLastUsedDbIdentifier();
		String nextDbIdentifier = nextDatabasePrefixGenerator.generateNextDatabasePrefix(currentDbIdentifier);
		registryDao.updateLastUsedDbIdentifier(nextDbIdentifier);
		
		return nextDbIdentifier;
	}

	public String getNextHistoryIdentifier() throws LBInvocationException {
		// TODO Auto-generated method stub
		return null;
	}

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
	
	@Transactional
	public void updateEntry(
			RegistryEntry entry){
		this.registryDao.updateRegistryEntry(entry);
	}

	public RegistryEntry getCodingSchemeEntry(
			AbsoluteCodingSchemeVersionReference codingScheme)
			throws LBParameterException {
		return registryDao.getRegistryEntryForUriAndVersion(codingScheme.getCodingSchemeURN(), codingScheme.getCodingSchemeVersion());
	}


	public RegistryEntry getNonCodingSchemeEntry(String uri)
			throws LBParameterException {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeEntry(RegistryEntry entry) throws LBParameterException {
		registryDao.deleteRegistryEntry(entry);	
	}
	
	public boolean containsNonCodingSchemeEntry(String uri) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setRegistryDao(RegistryDao registryDao) {
		this.registryDao = registryDao;
	}

	public RegistryDao getRegistryDao() {
		return registryDao;
	}

	public NextDatabasePrefixGenerator getNextDatabasePrefixGenerator() {
		return nextDatabasePrefixGenerator;
	}

	public void setNextDatabasePrefixGenerator(
			NextDatabasePrefixGenerator nextDatabasePrefixGenerator) {
		this.nextDatabasePrefixGenerator = nextDatabasePrefixGenerator;
	}	
}

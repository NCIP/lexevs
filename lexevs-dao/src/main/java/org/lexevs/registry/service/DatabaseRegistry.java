package org.lexevs.registry.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.dao.database.access.registry.RegistryDao;
import org.lexevs.dao.database.connection.SQLConnectionInfo;
import org.lexevs.dao.database.prefix.NextDatabasePrefixGenerator;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.exceptions.InternalException;
import org.lexevs.registry.model.RegistryEntry;
import org.springframework.transaction.annotation.Transactional;

public class DatabaseRegistry implements Registry {
	
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

	public void deactivate(RegistryEntry entry) throws LBInvocationException,
			LBParameterException {
		// TODO Auto-generated method stub
		
	}
	
	public List<RegistryEntry> getAllRegistryEntries() {
		return registryDao.getAllRegistryEntries();
	}
	
	public List<RegistryEntry> getAllRegistryEntriesOfType(ResourceType type) {
		return registryDao.getAllRegistryEntriesOfType(type);
	}


	@Transactional
	public Date getDeactivateDate(String codingSchemeURN, String version) {
		return registryDao.getRegistryEntryForUriAndVersion(codingSchemeURN, version).getDeactivationDate();
	}

	@Transactional
	public List<RegistryEntry> getEntriesForUri(String uri)
			throws LBParameterException {
		return 
				registryDao.getRegistryEntriesForUri(uri);
	}

	@Transactional
	public RegistryEntry getEntry(AbsoluteCodingSchemeVersionReference ref)
			throws LBParameterException {
		RegistryEntry entry = this.registryDao.
			getRegistryEntryForUriAndVersion(ref.getCodingSchemeURN(), 
					ref.getCodingSchemeVersion());
		
		return entry;
	}

	public List<RegistryEntry> getHistoryEntries() {
		return this.registryDao.getAllRegistryEntriesOfType(ResourceType.NCI_HISTORY);
	}

	@Transactional
	public Date getLastUpdateDate(String codingSchemeURN, String version) {
		return registryDao.getRegistryEntryForUriAndVersion(codingSchemeURN, version).getLastUpdateDate();
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

	public File getRegistryFile() {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLConnectionInfo getSQLConnectionInfoForCodeSystem(
			AbsoluteCodingSchemeVersionReference codingSchemeVersion) {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLConnectionInfo[] getSQLConnectionInfoForHistory(String urn) {
		// TODO Auto-generated method stub
		return null;
	}

	public CodingSchemeVersionStatus getStatus(String codingSchemeURN,
			String version) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTag(String codingSchemeURN, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getVersionForTag(String urn, String tag) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isActive(String codingSchemeURN, String version) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeCodingScheme(AbsoluteCodingSchemeVersionReference codingSchemeVersion){
		// TODO Auto-generated method stub
		
	}

	public void setDeactivateDate(AbsoluteCodingSchemeVersionReference acsvr,
			Date date) throws LBParameterException, LBInvocationException {
		// TODO Auto-generated method stub
		
	}

	@Transactional
	public void setStatusPending(RegistryEntry entry) throws LBInvocationException,
			LBParameterException {
		//this.registryDao.
		
	}

	public void updateTag(AbsoluteCodingSchemeVersionReference codingScheme,
			String newTag) throws LBInvocationException, LBParameterException {
		// TODO Auto-generated method stub
		
	}

	public void updateURNVersion(
			AbsoluteCodingSchemeVersionReference oldURNVerison,
			AbsoluteCodingSchemeVersionReference newURNVerison)
			throws LBInvocationException, LBParameterException {
		// TODO Auto-generated method stub
		
	}

	public void updateVersion(
			AbsoluteCodingSchemeVersionReference codingScheme, String newVersion)
			throws LBInvocationException, LBParameterException {
		// TODO Auto-generated method stub
		
	}
	
	@Transactional
	public LexGridSchemaVersion getSupportedLexGridSchemaVersion(
			AbsoluteCodingSchemeVersionReference ref)
			throws LBInvocationException {
		return LexGridSchemaVersion.parseStringToVersion(
				this.getRegistryDao().
				getRegistryEntryForUriAndVersion(ref.getCodingSchemeURN(), ref.getCodingSchemeVersion()).getDbSchemaVersion());
	}


	public void removeRegistryEntry(String uri) {
		// TODO Auto-generated method stub
		
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

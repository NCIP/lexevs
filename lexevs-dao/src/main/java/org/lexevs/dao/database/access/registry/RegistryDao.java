package org.lexevs.dao.database.access.registry;

import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;

public interface RegistryDao {
	
	public List<RegistryEntry> getAllRegistryEntriesOfType(ResourceType type);
	
	public List<RegistryEntry> getAllRegistryEntries();

	public RegistryEntry getRegistryEntryForUriAndVersion(String uri, String version) throws LBParameterException;
	
	public List<RegistryEntry> getRegistryEntriesForUri(String uri);
	
	public void updateLastUpdateTime(Date lastUpdateTime);
	
	public String getLastUsedDbIdentifier();
	
	public void initRegistryMetadata();
	
	public void updateLastUsedDbIdentifier(String databaseIdentifier);
	
	public String getLastUsedHistoryIdentifier();
	
	public Date getLastUpdateTime();
	
	public void updateRegistryEntry(RegistryEntry entry);
	
	public void insertRegistryEntry(RegistryEntry entry);
	
	public void removeRegistryEntry(String uri, String version);
	
	public void updateTag(String uri, String version, String newTag) throws LBParameterException;
}

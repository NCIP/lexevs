package org.lexevs.dao.database.access.registry;

import java.util.Date;

import org.lexevs.registry.model.RegistryEntry;

public interface RegistryDao {

	public RegistryEntry getCodingSchemeEntryForUriAndVersion(String uri, String version);
	
	public void updateLastUpdateTime(Date lastUpdateTime);
	
	public String getLastUsedDbIdentifier();
	
	public String getLastUsedHistoryIdentifier();
	
	public Date getLastUpdateTime();
	
	public void updateRegistryEntry(RegistryEntry entry);
	
	public void insertCodingSchemeEntry(RegistryEntry entry);
	
	public void removeCodingSchemeEntry(String uri, String version);
	
	public void updateTag(String uri, String version, String newTag);
}

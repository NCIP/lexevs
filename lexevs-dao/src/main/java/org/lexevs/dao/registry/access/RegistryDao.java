package org.lexevs.dao.registry.access;

import java.util.Calendar;
import java.util.Date;

import org.lexevs.dao.registry.model.CodingSchemeEntry;

public interface RegistryDao {

	public CodingSchemeEntry getCodingSchemeEntryForUriAndVersion(String uri, String version);
	
	public void updateLastUpdateTime(Date lastUpdateTime);
	
	public String getLastUsedDbIdentifier();
	
	public String getLastUsedHistoryIdentifier();
	
	public Date getLastUpdateTime();
	
	public void insertCodingSchemeEntry(CodingSchemeEntry entry);
	
	public void removeCodingSchemeEntry(String uri, String version);
	
	public void updateTag(String uri, String version, String newTag);
}

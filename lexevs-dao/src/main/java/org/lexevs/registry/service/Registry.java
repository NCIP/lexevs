package org.lexevs.registry.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.registry.model.RegistryEntry;

public interface Registry {
	
	public enum ResourceType {CODING_SCHEME, VALUE_DOMAIN, PICKLIST, NCI_HISTORY}
	public enum KnownTags {PRODUCTION};

	public List<RegistryEntry> getEntriesForUri(String uri)
			throws LBParameterException;

	public RegistryEntry getCodingSchemeEntry(AbsoluteCodingSchemeVersionReference codingScheme)
			throws LBParameterException;
	
	public RegistryEntry getNonCodingSchemeEntry(String uri)
		throws LBParameterException;
	
	public boolean containsCodingSchemeEntry(AbsoluteCodingSchemeVersionReference codingScheme);
	
	public boolean containsNonCodingSchemeEntry(String uri);

	public void addNewItem(RegistryEntry entry)
			throws Exception;

	public List<RegistryEntry> getAllRegistryEntries();
	
	public List<RegistryEntry> getAllRegistryEntriesOfType(ResourceType type);

	public Date getLastUpdateTime();

	public void removeEntry(RegistryEntry entry) throws LBParameterException;
	
	public void updateEntry(RegistryEntry entry) throws LBParameterException;
	
	public String getNextDBIdentifier() throws LBInvocationException;

	public String getNextHistoryIdentifier() throws LBInvocationException;

}

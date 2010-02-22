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
	
	public File getRegistryFile();

	public CodingSchemeVersionStatus getStatus(String codingSchemeURN,
			String version);

	public boolean isActive(String codingSchemeURN, String version);

	public String getTag(String codingSchemeURN, String version);

	public Date getDeactivateDate(String codingSchemeURN, String version) throws LBParameterException;

	public void setDeactivateDate(AbsoluteCodingSchemeVersionReference acsvr,
			Date date) throws LBParameterException, LBInvocationException;

	public Date getLastUpdateDate(String codingSchemeURN, String version) throws LBParameterException;

	public void deactivate(RegistryEntry entry) throws LBInvocationException,
			LBParameterException;

	public void setStatusPending(RegistryEntry entry) throws LBInvocationException,
			LBParameterException;

	public void activate(AbsoluteCodingSchemeVersionReference codingScheme)
			throws LBInvocationException, LBParameterException;

	public void updateTag(AbsoluteCodingSchemeVersionReference codingScheme,
			String newTag) throws LBInvocationException, LBParameterException;

	public void updateVersion(
			AbsoluteCodingSchemeVersionReference codingScheme, String newVersion)
			throws LBInvocationException, LBParameterException;

	public List<RegistryEntry> getEntriesForUri(String uri)
			throws LBParameterException;

	public RegistryEntry getEntry(AbsoluteCodingSchemeVersionReference codingScheme)
			throws LBParameterException;

	public void addNewItem(RegistryEntry entry)
			throws Exception;

	public String getVersionForTag(String urn, String tag);

	public List<RegistryEntry> getAllRegistryEntries();
	
	public List<RegistryEntry> getAllRegistryEntriesOfType(ResourceType type);

	public Date getLastUpdateTime();

	public void removeCodingScheme(AbsoluteCodingSchemeVersionReference codingSchemeVersion);
	
	public void removeRegistryEntry(String uri);

	public String getNextDBIdentifier() throws LBInvocationException;

	public String getNextHistoryIdentifier() throws LBInvocationException;
	
	public LexGridSchemaVersion getSupportedLexGridSchemaVersion(AbsoluteCodingSchemeVersionReference ref) throws LBInvocationException;

	public void updateURNVersion(
			AbsoluteCodingSchemeVersionReference oldURNVerison,
			AbsoluteCodingSchemeVersionReference newURNVerison)
			throws LBInvocationException, LBParameterException;
	
/*
	@Deprecated
    public class DBEntry {
    	public String urn;
        public String version;
        public String status;
        public String dbURL;
        public String prefix = "";
        public String tag;
        public String dbName;
        public long lastUpdateDate;
        public long deactiveDate;
    }
    
	@Deprecated
    public class HistoryEntry {
        public String urn;
        public String dbURL;
        public String prefix = "";
        public String dbName;
        public long lastUpdateDate;
    }
    */
}

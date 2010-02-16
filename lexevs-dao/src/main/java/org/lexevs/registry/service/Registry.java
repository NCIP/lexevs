package org.lexevs.registry.service;

import java.io.File;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.dao.database.connection.SQLConnectionInfo;
import org.lexevs.exceptions.InternalException;

public interface Registry {
	
	public enum ResourceType {CODING_SCHEME, VALUE_DOMAIN, PICKLIST, NCI_HISTORY}
	
	public File getRegistryFile();

	public CodingSchemeVersionStatus getStatus(String codingSchemeURN,
			String version);

	public boolean isActive(String codingSchemeURN, String version);

	public SQLConnectionInfo getSQLConnectionInfoForCodeSystem(
			AbsoluteCodingSchemeVersionReference codingSchemeVersion);

	public SQLConnectionInfo[] getSQLConnectionInfoForHistory(String urn);

	public String getTag(String codingSchemeURN, String version);

	public Date getDeactivateDate(String codingSchemeURN, String version);

	public void setDeactivateDate(AbsoluteCodingSchemeVersionReference acsvr,
			Date date) throws LBParameterException, LBInvocationException;

	public Date getLastUpdateDate(String codingSchemeURN, String version);

	public void deactivate(DBEntry entry) throws LBInvocationException,
			LBParameterException;

	public void setStatusPending(DBEntry entry) throws LBInvocationException,
			LBParameterException;

	public void activate(AbsoluteCodingSchemeVersionReference codingScheme)
			throws LBInvocationException, LBParameterException;

	public void updateTag(AbsoluteCodingSchemeVersionReference codingScheme,
			String newTag) throws LBInvocationException, LBParameterException;

	public void updateVersion(
			AbsoluteCodingSchemeVersionReference codingScheme, String newVersion)
			throws LBInvocationException, LBParameterException;

	public DBEntry getEntry(String codingSchemeURN, String version)
			throws LBParameterException;

	public DBEntry getEntry(AbsoluteCodingSchemeVersionReference codingScheme)
			throws LBParameterException;

	public void addNewItem(String urn, String version, String status,
			String dbURL, String tag, String dbName, String tablePrefix)
			throws Exception;


	public HistoryEntry addNewHistory(String urn, String dbURL, String dbName,
			String tablePrefix) throws Exception;

	public String getVersionForTag(String urn, String tag);

	public DBEntry[] getDBEntries();

	public HistoryEntry[] getHistoryEntries();

	public Date getLastUpdateTime();

	public void remove(AbsoluteCodingSchemeVersionReference codingSchemeVersion)
			throws InternalException, LBInvocationException,
			LBParameterException;

	public HistoryEntry getHistoryEntry(String urn) throws LBParameterException;

	public void removeHistoryEntry(String urn) throws InternalException,
			LBInvocationException, LBParameterException;

	public String getNextDBIdentifier() throws LBInvocationException;

	public String getNextHistoryIdentifier() throws LBInvocationException;

	public void updateURNVersion(
			AbsoluteCodingSchemeVersionReference oldURNVerison,
			AbsoluteCodingSchemeVersionReference newURNVerison)
			throws LBInvocationException, LBParameterException;
	

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
    
    public class HistoryEntry {
        public String urn;
        public String dbURL;
        public String prefix = "";
        public String dbName;
        public long lastUpdateDate;
    }
}

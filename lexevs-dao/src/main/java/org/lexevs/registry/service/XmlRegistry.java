
package org.lexevs.registry.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.sql.DataSource;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.lexevs.dao.database.connection.SQLConnectionInfo;
import org.lexevs.exceptions.InternalException;
import org.lexevs.exceptions.UnexpectedInternalError;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.registry.WriteLockManager;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.system.ResourceManager;
import org.lexevs.system.constants.SystemVariables;

/**
 * This class stores and provides access to information about loaded and tagged
 * terminologies.
 * 
 * Locking guidelines - any method that makes changes that are supposed to be
 * written out to the xml file must first - get a lock on the main lock file.
 * check for changes. read in the latest changes, if there are any. Then, make
 * the changes, write out the xml file, and finally, release the lock on the
 * lock file.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class XmlRegistry implements Registry {
    
    /** The last update time_. */
    private long lastUpdateTime_;
    // last used db or table identifiers. If we are in multiDB mode, this
    // identifier will
    // be a number that starts at 0 and counts up.
    // If we are in single db mode, this will be a character number combination
    // that increments.
    /** The last used db identifier_. */
    private String lastUsedDBIdentifier_;
    
    /** The last used history identifier_. */
    private String lastUsedHistoryIdentifier_;
    
    /** The entries_. */
    private ArrayList<DBEntry> entries_;
    
    /** The history entries_. */
    private ArrayList<HistoryEntry> historyEntries_;

    /** The urn version to entry map_. */
    private Hashtable<String, DBEntry> urnVersionToEntryMap_;
    
    /** The urn tag to version map_. */
    private Hashtable<String, String> urnTagToVersionMap_;
    
    /** The data source. */
    private DataSource dataSource;

    /** The file_. */
    private File file_;

    /**
     * Gets the logger.
     * 
     * @return the logger
     */
    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    /**
     * The Class DBEntry.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    public static class DBEntry {
    	
	    /** The urn. */
	    public String urn;
    	
	    /** The version. */
	    public String version;
    	
	    /** The status. */
	    public String status;
    	
	    /** The db url. */
	    public String dbURL;
        
        /** The prefix. */
        public String prefix = "";
        
        /** The tag. */
        public String tag;
        
        /** The db name. */
        public String dbName;
        
        /** The last update date. */
        public long lastUpdateDate;
        
        /** The deactive date. */
        public long deactiveDate;

    }

    /**
     * The Class HistoryEntry.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    public static class HistoryEntry {
    	
	    /** The urn. */
	    public String urn;
    	
	    /** The db url. */
	    public String dbURL;
    	
	    /** The prefix. */
	    public String prefix = "";
    	
	    /** The db name. */
	    public String dbName;
    	
	    /** The last update date. */
	    public long lastUpdateDate;

        /**
         * Gets the db name.
         * 
         * @return the db name
         */
        public String getdbName() {
            return dbName;
        }

        /**
         * Gets the prefix.
         * 
         * @return the prefix
         */
        public String getPrefix() {
            return prefix;
        }
    }

    /**
     * Gets the registry file.
     * 
     * @return the registry file
     */
    protected File getRegistryFile() {
        return file_;
    }

    /**
     * Gets the status.
     * 
     * @param codingSchemeURN the coding scheme urn
     * @param version the version
     * 
     * @return the status
     */
    public CodingSchemeVersionStatus getStatus(String codingSchemeURN, String version) {
        // If I don't have an entry in the registry, assume it is a -preloaded-
        // one, and just return active
        DBEntry entry = null;
        try {
            entry = getEntry(codingSchemeURN, version);
        } catch (LBParameterException e) {
            // impossible
        }
        if (entry == null) {
            return null;
        } else if (entry.status.equals(CodingSchemeVersionStatus.ACTIVE.toString())) {
            return CodingSchemeVersionStatus.ACTIVE;
        } else if (entry.status.equals(CodingSchemeVersionStatus.INACTIVE.toString())) {
            return CodingSchemeVersionStatus.INACTIVE;
        } else if (entry.status.equals(CodingSchemeVersionStatus.PENDING.toString())) {
            return CodingSchemeVersionStatus.PENDING;
        } else {
            return null;
        }
    }

    /**
     * Checks if is active.
     * 
     * @param codingSchemeURN the coding scheme urn
     * @param version the version
     * 
     * @return true, if is active
     */
    public boolean isActive(String codingSchemeURN, String version) {
        // this returns null if the coding scheme isn't registered.
        CodingSchemeVersionStatus temp = getStatus(codingSchemeURN, version);

        // return true for unregistered coding schemes.
        return (temp == null || temp == CodingSchemeVersionStatus.ACTIVE ? true : false);
    }

    /**
     * Gets the sQL connection info for code system.
     * 
     * @param codingSchemeVersion the coding scheme version
     * 
     * @return the sQL connection info for code system
     */
    public SQLConnectionInfo getSQLConnectionInfoForCodeSystem(AbsoluteCodingSchemeVersionReference codingSchemeVersion) {
        SystemVariables sv = ResourceManager.instance().getSystemVariables();

        if (codingSchemeVersion != null && codingSchemeVersion.getCodingSchemeURN() != null
                && codingSchemeVersion.getCodingSchemeVersion() != null
                && codingSchemeVersion.getCodingSchemeURN().length() > 0
                && codingSchemeVersion.getCodingSchemeVersion().length() > 0) {
            DBEntry dbEntry = urnVersionToEntryMap_.get(codingSchemeVersion.getCodingSchemeURN()
                    + ResourceManager.codingSchemeVersionSeparator_ + codingSchemeVersion.getCodingSchemeVersion());

            SQLConnectionInfo temp = new SQLConnectionInfo();
            temp.dbName = dbEntry.dbName;
            temp.driver = sv.getAutoLoadDBDriver();
            temp.password = sv.getAutoLoadDBPassword();
            temp.server = dbEntry.dbURL;
            temp.prefix = dbEntry.prefix;
            temp.username = sv.getAutoLoadDBUsername();
            temp.urn = dbEntry.urn;
            temp.version = dbEntry.version;
            return temp;
        } else {
            return null;
        }
    }

    /**
     * Gets the sQL connection info for history.
     * 
     * @param urn the urn
     * 
     * @return the sQL connection info for history
     */
    public SQLConnectionInfo[] getSQLConnectionInfoForHistory(String urn) {
        SQLConnectionInfo[] result = null;
        SystemVariables sv = ResourceManager.instance().getSystemVariables();

        if (urn != null && urn.length() > 0) {
            // return specified item
            for (int i = 0; i < historyEntries_.size(); i++) {
                if (historyEntries_.get(i).urn.equals(urn)) {
                    HistoryEntry hEntry = historyEntries_.get(i);
                    SQLConnectionInfo temp = new SQLConnectionInfo();
                    temp.dbName = hEntry.dbName;
                    temp.driver = sv.getAutoLoadDBDriver();
                    temp.password = sv.getAutoLoadDBPassword();
                    temp.server = hEntry.dbURL;
                    temp.prefix = hEntry.prefix;
                    temp.username = sv.getAutoLoadDBUsername();
                    temp.urn = hEntry.urn;
                    result = new SQLConnectionInfo[1];
                    result[0] = temp;
                    break;
                }
            }
        } else {
            result = new SQLConnectionInfo[historyEntries_.size()];
            for (int i = 0; i < historyEntries_.size(); i++) {
                HistoryEntry hEntry = historyEntries_.get(i);
                SQLConnectionInfo temp = new SQLConnectionInfo();
                temp.dbName = hEntry.dbName;
                temp.driver = sv.getAutoLoadDBDriver();
                temp.password = sv.getAutoLoadDBPassword();
                temp.server = hEntry.dbURL;
                temp.username = sv.getAutoLoadDBUsername();
                temp.urn = hEntry.urn;
                result[i] = temp;
            }
        }
        return result;
    }

    /**
     * Gets the tag.
     * 
     * @param codingSchemeURN the coding scheme urn
     * @param version the version
     * 
     * @return the tag
     */
    public String getTag(String codingSchemeURN, String version) {
        // If I don't have an entry in the registry, assume it is a -preloaded-
        // one, and just return null.
        DBEntry entry = null;
        try {
            entry = getEntry(codingSchemeURN, version);
        } catch (LBParameterException e) {
            // impossible
        }
        if (entry == null) {
            return null;
        } else {
            return entry.tag;
        }
    }

    /**
     * Gets the deactivate date.
     * 
     * @param codingSchemeURN the coding scheme urn
     * @param version the version
     * 
     * @return the deactivate date
     */
    public Date getDeactivateDate(String codingSchemeURN, String version) {
        // If I don't have an entry in the registry, assume it is a -preloaded-
        // one, and just return null.
        DBEntry entry = null;
        try {
            entry = getEntry(codingSchemeURN, version);
        } catch (LBParameterException e) {
            // impossible
        }
        if (entry == null) {
            return null;
        } else {
            if (entry.deactiveDate == 0) {
                return null;
            } else {
                return new Date(entry.deactiveDate);
            }
        }
    }

    /**
     * Sets the deactivate date.
     * 
     * @param acsvr the acsvr
     * @param date the date
     * 
     * @throws LBParameterException the LB parameter exception
     * @throws LBInvocationException the LB invocation exception
     */
    public synchronized void setDeactivateDate(AbsoluteCodingSchemeVersionReference acsvr, Date date)
            throws LBParameterException, LBInvocationException {
        WriteLockManager.instance().lockLockFile();
        WriteLockManager.instance().checkForRegistryUpdates();
        try {
            // go through the instance method to get the new Registry object (if
            // it was replaced by check for
            // updates)
            ResourceManager.instance().getRegistry().setDeactivateDateInternal(acsvr, date);
        } finally {
            WriteLockManager.instance().releaseLockFile();
        }
    }

    /**
     * Sets the deactivate date internal.
     * 
     * @param acsvr the acsvr
     * @param date the date
     * 
     * @throws LBParameterException the LB parameter exception
     * @throws LBInvocationException the LB invocation exception
     */
    public void setDeactivateDateInternal(AbsoluteCodingSchemeVersionReference acsvr, Date date)
            throws LBParameterException, LBInvocationException {
        try {
            DBEntry entry = null;
            entry = getDBCodingSchemeEntry(acsvr);

            WriteLockManager.instance().acquireLock(acsvr.getCodingSchemeURN(), acsvr.getCodingSchemeVersion());

            entry.deactiveDate = date.getTime();
            entry.lastUpdateDate = System.currentTimeMillis();
            writeFile2();
        } finally {
            WriteLockManager.instance().releaseLock(acsvr.getCodingSchemeURN(), acsvr.getCodingSchemeVersion());
        }
    }

    /**
     * Gets the last update date.
     * 
     * @param codingSchemeURN the coding scheme urn
     * @param version the version
     * 
     * @return the last update date
     */
    public Date getLastUpdateDate(String codingSchemeURN, String version) {
        // If I don't have an entry in the registry, assume it is a -preloaded-
        // one, and just return true.
        DBEntry entry = null;
        try {
            entry = getEntry(codingSchemeURN, version);
        } catch (LBParameterException e) {
            // impossible
        }
        if (entry == null) {
            return null;
        } else {
            if (entry.lastUpdateDate == 0) {
                return null;
            } else {
                return new Date(entry.lastUpdateDate);
            }
        }
    }

    /**
     * Deactivate.
     * 
     * @param entry the entry
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public synchronized void deactivate(DBEntry entry) throws LBInvocationException, LBParameterException {
        WriteLockManager.instance().lockLockFile();
        WriteLockManager.instance().checkForRegistryUpdates();
        try {
            // go through the instance method to get the new Registry object (if
            // it was replaced by check for
            // updates)
            ResourceManager.instance().getRegistry().deactivateInternal(entry);
        } finally {
            WriteLockManager.instance().releaseLockFile();
        }
    }

    /**
     * Deactivate internal.
     * 
     * @param entry the entry
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    private void deactivateInternal(DBEntry entry) throws LBInvocationException, LBParameterException {
        try {
            WriteLockManager.instance().acquireLock(entry.urn, entry.version);
            entry.status = CodingSchemeVersionStatus.INACTIVE.toString();
            entry.deactiveDate = System.currentTimeMillis();
            entry.lastUpdateDate = entry.deactiveDate;
            writeFile2();
        } finally {
            WriteLockManager.instance().releaseLock(entry.urn, entry.version);
        }
    }

    /**
     * Sets the status pending.
     * 
     * @param entry the new status pending
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public synchronized void setStatusPending(DBEntry entry) throws LBInvocationException, LBParameterException {
        WriteLockManager.instance().lockLockFile();
        WriteLockManager.instance().checkForRegistryUpdates();
        try {
            // go through the instance method to get the new Registry object (if
            // it was replaced by check for
            // updates)
            ResourceManager.instance().getRegistry().setStatusPendingInternal(entry);
        } finally {
            WriteLockManager.instance().releaseLockFile();
        }
    }

    /**
     * Sets the status pending internal.
     * 
     * @param entry the new status pending internal
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    private void setStatusPendingInternal(DBEntry entry) throws LBInvocationException, LBParameterException {
        try {
            WriteLockManager.instance().acquireLock(entry.urn, entry.version);
            entry.status = CodingSchemeVersionStatus.PENDING.toString();
            entry.deactiveDate = System.currentTimeMillis();
            entry.lastUpdateDate = entry.deactiveDate;
            writeFile2();
        } finally {
            WriteLockManager.instance().releaseLock(entry.urn, entry.version);
        }
    }

    /**
     * Activate a code system.
     * 
     * @param codingScheme the coding scheme
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public synchronized void activate(AbsoluteCodingSchemeVersionReference codingScheme) throws LBInvocationException,
            LBParameterException {
        WriteLockManager.instance().lockLockFile();
        WriteLockManager.instance().checkForRegistryUpdates();
        try {
            // go through the instance method to get the new Registry object (if
            // it was replaced by check for
            // updates)
            ResourceManager.instance().getRegistry().activateInternal(codingScheme);
        } finally {
            WriteLockManager.instance().releaseLockFile();
        }
    }

    /**
     * Activate internal.
     * 
     * @param codingScheme the coding scheme
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    private void activateInternal(AbsoluteCodingSchemeVersionReference codingScheme) throws LBInvocationException,
            LBParameterException {
        DBEntry entry = getDBCodingSchemeEntry(codingScheme);
        if (entry == null) {
            throw new LBParameterException("The specified coding scheme is not registered");
        }
        // not enforcing this anymore.
        // if
        // (!codingScheme.getCodingSchemeURN().toLowerCase().startsWith("urn:"))
        // {
        // throw new LBParameterException("The registered name "
        // + codingScheme.getCodingSchemeURN()
        // + " is invalid. It should have a 'urn:' prefix."
        // +
        // " This coding scheme will not be activated until the invalid registered name is corrected.");
        // }

        try {
            WriteLockManager.instance().acquireLock(entry.urn, entry.version);

            long time = System.currentTimeMillis();
            // if it is scheduled to be deactivated in the future, leave that
            // time in.
            if (entry.deactiveDate != 0 && entry.deactiveDate <= time) {
                entry.deactiveDate = 0;
            }
            entry.status = CodingSchemeVersionStatus.ACTIVE.toString();
            entry.lastUpdateDate = time;
            writeFile2();
        } finally {
            WriteLockManager.instance().releaseLock(entry.urn, entry.version);
        }
    }

    /**
     * Update tag.
     * 
     * @param codingScheme the coding scheme
     * @param newTag the new tag
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public synchronized void updateTag(AbsoluteCodingSchemeVersionReference codingScheme, String newTag)
            throws LBInvocationException, LBParameterException {
        WriteLockManager.instance().lockLockFile();
        WriteLockManager.instance().checkForRegistryUpdates();
        try {
            // go through the instance method to get the new Registry object (if
            // it was replaced by check for
            // updates)
            ResourceManager.instance().getRegistry().updateTagInternal(codingScheme, newTag);
        } finally {
            WriteLockManager.instance().releaseLockFile();
        }
    }

    /**
     * Update tag internal.
     * 
     * @param codingScheme the coding scheme
     * @param newTag the new tag
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    private void updateTagInternal(AbsoluteCodingSchemeVersionReference codingScheme, String newTag)
            throws LBInvocationException, LBParameterException {
        DBEntry entry = getDBCodingSchemeEntry(codingScheme);
        // if another version is assigned this tag, need to clear it.
        String version = getVersionForTag(codingScheme.getCodingSchemeURN(), newTag);

        if (version != null) {
            AbsoluteCodingSchemeVersionReference temp = new AbsoluteCodingSchemeVersionReference();
            temp.setCodingSchemeURN(codingScheme.getCodingSchemeURN());
            temp.setCodingSchemeVersion(version);
            clearTag(temp, newTag);
        }

        try {
            WriteLockManager.instance().acquireLock(entry.urn, entry.version);

            String oldKey = codingScheme.getCodingSchemeURN() + ResourceManager.codingSchemeVersionSeparator_
                    + entry.tag;
            urnTagToVersionMap_.remove(oldKey);

            entry.tag = newTag;
            entry.lastUpdateDate = System.currentTimeMillis();
            if (newTag != null && newTag.length() > 0) {
                urnTagToVersionMap_
                        .put(
                                codingScheme.getCodingSchemeURN() + ResourceManager.codingSchemeVersionSeparator_
                                        + newTag, codingScheme.getCodingSchemeVersion());
            }
            writeFile2();
        } finally {
            WriteLockManager.instance().releaseLock(entry.urn, entry.version);
        }
    }

    /**
     * Clear tag.
     * 
     * @param codingScheme the coding scheme
     * @param tag the tag
     * 
     * @throws LBParameterException the LB parameter exception
     * @throws LBInvocationException the LB invocation exception
     */
    private void clearTag(AbsoluteCodingSchemeVersionReference codingScheme, String tag) throws LBParameterException,
            LBInvocationException {
        try {
            WriteLockManager.instance().acquireLock(codingScheme.getCodingSchemeURN(),
                    codingScheme.getCodingSchemeVersion());
            urnTagToVersionMap_.remove(codingScheme.getCodingSchemeURN()
                    + ResourceManager.codingSchemeVersionSeparator_ + tag);
            getDBCodingSchemeEntry(codingScheme).tag = null;
            writeFile2();
        } finally {
            WriteLockManager.instance().releaseLock(codingScheme.getCodingSchemeURN(),
                    codingScheme.getCodingSchemeVersion());
        }
    }

    /**
     * Update version.
     * 
     * @param codingScheme the coding scheme
     * @param newVersion the new version
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public synchronized void updateVersion(AbsoluteCodingSchemeVersionReference codingScheme, String newVersion)
            throws LBInvocationException, LBParameterException {
        WriteLockManager.instance().lockLockFile();
        WriteLockManager.instance().checkForRegistryUpdates();
        try {
            // go through the instance method to get the new Registry object (if
            // it was replaced by check for
            // updates)
            ResourceManager.instance().getRegistry().updateVersionInternal(codingScheme, newVersion);
        } finally {
            WriteLockManager.instance().releaseLockFile();
        }
    }

    /**
     * Update version internal.
     * 
     * @param codingScheme the coding scheme
     * @param newVersion the new version
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    private void updateVersionInternal(AbsoluteCodingSchemeVersionReference codingScheme, String newVersion)
            throws LBInvocationException, LBParameterException {
        DBEntry entry = getDBCodingSchemeEntry(codingScheme);
        String urn = entry.urn;
        try {
            WriteLockManager.instance().acquireLock(entry.urn, entry.version);

            String oldKey = codingScheme.getCodingSchemeURN() + ResourceManager.codingSchemeVersionSeparator_
                    + entry.tag;
            urnTagToVersionMap_.remove(oldKey);

            entry.lastUpdateDate = System.currentTimeMillis();
            if (newVersion != null && newVersion.length() > 0) {
                urnTagToVersionMap_.put(codingScheme.getCodingSchemeURN()
                        + ResourceManager.codingSchemeVersionSeparator_ + entry.tag, newVersion);
            }
            // writeFile2();

            DBEntry dbe = entry;
            dbe.version = newVersion;
            dbe.lastUpdateDate = System.currentTimeMillis();

            // WriteLockManager.instance().acquireLock(entry.urn,
            // entry.version);
            urnVersionToEntryMap_.remove(entry.urn + ResourceManager.codingSchemeVersionSeparator_ + entry.version);
            entries_.remove(entry);

            entries_.add(dbe);

            urnVersionToEntryMap_.put(dbe.urn + ResourceManager.codingSchemeVersionSeparator_ + dbe.version, dbe);
            if (dbe.tag != null && dbe.tag.length() > 0) {
                urnTagToVersionMap_.put(dbe.urn + ResourceManager.codingSchemeVersionSeparator_ + dbe.tag, dbe.version);
            }
            writeFile2();
        } finally {
            WriteLockManager.instance().releaseLock(urn, newVersion);
        }
    }

    /**
     * Gets the entry.
     * 
     * @param codingSchemeURN the coding scheme urn
     * @param version the version
     * 
     * @return the entry
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public DBEntry getEntry(String codingSchemeURN, String version) throws LBParameterException {
        if (codingSchemeURN == null || version == null || codingSchemeURN.length() == 0 || version.length() == 0) {
            throw new LBParameterException("The URN and the version must be populated.");
        }
        return urnVersionToEntryMap_.get(codingSchemeURN + ResourceManager.codingSchemeVersionSeparator_ + version);
    }

    /**
     * Gets the dB coding scheme entry.
     * 
     * @param codingScheme the coding scheme
     * 
     * @return the dB coding scheme entry
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public DBEntry getDBCodingSchemeEntry(AbsoluteCodingSchemeVersionReference codingScheme) throws LBParameterException {
        if (codingScheme == null) {
            throw new LBParameterException(
                    "The URN and the version must be populated in the AbsoluteCodingSchemeReference");
        }
        return getEntry(codingScheme.getCodingSchemeURN(), codingScheme.getCodingSchemeVersion());
    }

    /**
     * Adds the new item.
     * 
     * @param urn the urn
     * @param version the version
     * @param status the status
     * @param dbURL the db url
     * @param tag the tag
     * @param dbName the db name
     * @param tablePrefix the table prefix
     * 
     * @throws Exception the exception
     */
    public synchronized void addNewItem(String urn, String version, String status, String dbURL, String tag,
            String dbName, String tablePrefix) throws Exception {
        WriteLockManager.instance().lockLockFile();
        WriteLockManager.instance().checkForRegistryUpdates();
        try {
            // go through the instance method to get the new Registry object (if
            // it was replaced by check for
            // updates)
            ResourceManager.instance().getRegistry().addNewItemInternal(urn, version, status, dbURL, tag, dbName,
                    tablePrefix);
        } finally {
            WriteLockManager.instance().releaseLockFile();
        }
    }

    /**
     * Adds the new item internal.
     * 
     * @param urn the urn
     * @param version the version
     * @param status the status
     * @param dbURL the db url
     * @param tag the tag
     * @param dbName the db name
     * @param tablePrefix the table prefix
     * 
     * @throws Exception the exception
     */
    private void addNewItemInternal(String urn, String version, String status, String dbURL, String tag, String dbName,
            String tablePrefix) throws Exception {
        if (urn == null || version == null || status == null || dbURL == null || urn.length() == 0
                || version.length() == 0 || status.length() == 0 || dbURL.length() == 0) {
            throw new LBParameterException("Required parameter missing");
        }

        DBEntry dbe = new DBEntry();
        dbe.urn = urn;
        dbe.version = version;
        dbe.status = status;
        dbe.dbURL = dbURL;
        dbe.tag = tag;
        dbe.dbName = dbName;
        dbe.prefix = tablePrefix;
        dbe.lastUpdateDate = System.currentTimeMillis();

        try

        {
            WriteLockManager.instance().acquireLock(dbe.urn, dbe.version);

            entries_.add(dbe);

            urnVersionToEntryMap_.put(dbe.urn + ResourceManager.codingSchemeVersionSeparator_ + dbe.version, dbe);
            if (dbe.tag != null && dbe.tag.length() > 0) {
                urnTagToVersionMap_.put(dbe.urn + ResourceManager.codingSchemeVersionSeparator_ + dbe.tag, dbe.version);
            }

            writeFile2();
        } finally {
            WriteLockManager.instance().releaseLock(dbe.urn, dbe.version);
        }
    }

    /**
     * Add a new history item to the registry. Returns the current existing
     * information (if any exists)
     * 
     * @param urn the urn
     * @param dbURL the db url
     * @param dbName the db name
     * @param tablePrefix the table prefix
     * 
     * @return the history entry
     * 
     * @throws Exception the exception
     */
    public synchronized HistoryEntry addNewHistory(String urn, String dbURL, String dbName, String tablePrefix)
            throws Exception {
        WriteLockManager.instance().lockLockFile();
        WriteLockManager.instance().checkForRegistryUpdates();
        try {
            // go through the instance method to get the new Registry object (if
            // it was replaced by check for
            // updates)
            return ResourceManager.instance().getRegistry().addNewHistoryInternal(urn, dbURL, dbName, tablePrefix);
        } finally {
            WriteLockManager.instance().releaseLockFile();
        }
    }

    /**
     * Add a new history item to the registry. Returns the current existing
     * information (if any exists)
     * 
     * @param urn the urn
     * @param dbURL the db url
     * @param dbName the db name
     * @param tablePrefix the table prefix
     * 
     * @return the history entry
     * 
     * @throws Exception the exception
     */
    private HistoryEntry addNewHistoryInternal(String urn, String dbURL, String dbName, String tablePrefix)
            throws Exception {
        // no further locking should be required for history entries.
        if (urn == null || dbURL == null || urn.length() == 0 || dbURL.length() == 0) {
            throw new LBParameterException("Required parameter missing");
        }

        HistoryEntry he = new HistoryEntry();
        he.urn = urn;
        he.dbURL = dbURL;
        he.dbName = dbName;
        he.lastUpdateDate = System.currentTimeMillis();
        he.prefix = tablePrefix;

        HistoryEntry old = null;
        for (int i = 0; i < historyEntries_.size(); i++) {
            if (historyEntries_.get(i).urn.equals(urn)) {
                // new registry entry will replace this current entry. Only one
                // history db
                // is allowed per urn.
                old = historyEntries_.get(i);
                historyEntries_.remove(i);
                break;
            }
        }

        historyEntries_.add(he);
        writeFile2();
        return old;
    }

    /**
     * Gets the version for tag.
     * 
     * @param urn the urn
     * @param tag the tag
     * 
     * @return the version for tag
     */
    public String getVersionForTag(String urn, String tag) {
        return urnTagToVersionMap_.get(urn + ResourceManager.codingSchemeVersionSeparator_ + tag);
    }
    
    /**
     * Instantiates a new xml registry.
     * 
     * @param systemVariables the system variables
     * 
     * @throws Exception the exception
     */
    public XmlRegistry(SystemVariables systemVariables) throws Exception {
    	this(systemVariables.getAutoLoadRegistryPath());
    }

    /**
     * Instantiates a new xml registry.
     * 
     * @param pathToRegistryFile the path to registry file
     * 
     * @throws Exception the exception
     */
    public XmlRegistry(String pathToRegistryFile) throws Exception {

    	try {
    		file_ = new File(pathToRegistryFile);
    		entries_ = new ArrayList<DBEntry>();
    		historyEntries_ = new ArrayList<HistoryEntry>();
    		urnVersionToEntryMap_ = new Hashtable<String, DBEntry>();
    		urnTagToVersionMap_ = new Hashtable<String, String>();

    		if (file_.exists()) {
    			WriteLockManager.instance(file_).lockLockFile();
    			readFile();
    			WriteLockManager.instance().releaseLockFile();
    		} else {

    			//Don't create a new XML Registry file...
    			//If there isn't one already, we don't need it.
    			getLogger().debug(
    			"There is no XML Registry file -- this instance of LexEVS supports ONLY a Database Registry.");
    		}
    	}

    	catch (IOException e) {
    		throw new Exception("Could not create a file to store the registration information.", e);
    	}
    }

    /**
     * Read file.
     * 
     * @throws Exception the exception
     */
    @SuppressWarnings("unchecked")
    private synchronized void readFile() throws Exception {
        // read in the contents of the xml file, populating the local variables.
        WriteLockManager.instance().lockLockFile();
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(file_);
            Element root = document.getRootElement();
            Element vars = root.getChild("variables");
            lastUpdateTime_ = Long.parseLong(vars.getChild("lastUpdateTime").getAttributeValue("value"));
            lastUsedDBIdentifier_ = vars.getChild("lastUsedDBIdentifer").getAttributeValue("value");

            if (vars.getChild("lastUsedHistoryIdentifer") == null) {
                lastUsedHistoryIdentifier_ = "a0";
            } else {
                lastUsedHistoryIdentifier_ = vars.getChild("lastUsedHistoryIdentifer").getAttributeValue("value");
            }

            Element codingSchemes = root.getChild("codingSchemes");
            List<Element> list = codingSchemes.getChildren("codingScheme");
            for (int i = 0; i < list.size(); i++) {
                Element cur = (Element) list.get(i);
                DBEntry dbe = new DBEntry();
                dbe.dbURL = cur.getAttributeValue("dbURL");
                dbe.prefix = cur.getAttributeValue("prefix");
                dbe.dbName = cur.getAttributeValue("dbName");
                dbe.deactiveDate = Long.parseLong(cur.getAttributeValue("deactivateDate"));
                dbe.lastUpdateDate = Long.parseLong(cur.getAttributeValue("lastUpdateDate"));
                dbe.status = cur.getAttributeValue("status");
                dbe.tag = cur.getAttributeValue("tag");
                dbe.urn = cur.getAttributeValue("urn");
                dbe.version = cur.getAttributeValue("version");

                entries_.add(dbe);
                urnVersionToEntryMap_.put(dbe.urn + ResourceManager.codingSchemeVersionSeparator_ + dbe.version, dbe);
                if (dbe.tag != null && dbe.tag.length() > 0) {
                    urnTagToVersionMap_.put(dbe.urn + ResourceManager.codingSchemeVersionSeparator_ + dbe.tag,
                            dbe.version);
                }

            }

            Element histories = root.getChild("histories");
            if (histories != null) {
                list = histories.getChildren("history");
                for (int i = 0; i < list.size(); i++) {
                    Element cur = (Element) list.get(i);
                    HistoryEntry he = new HistoryEntry();
                    he.dbURL = cur.getAttributeValue("dbURL");
                    he.prefix = cur.getAttributeValue("prefix");
                    he.dbName = cur.getAttributeValue("dbName");
                    he.lastUpdateDate = Long.parseLong(cur.getAttributeValue("lastUpdateDate"));
                    he.urn = cur.getAttributeValue("urn");

                    historyEntries_.add(he);
                }
            }
        } catch (NumberFormatException e) {
            throw new Exception(
                    "The existing LexBIG registry file contains an invalid value for a field the requires a number - lastUpdateTime, nextFreeDBIdentiefier, deactivateDate, lastUpdateDate");
        } catch (JDOMException e) {
            throw new Exception("The existing LexBIG registry file is invalid", e);
        } catch (IOException e) {
            throw new Exception("Could not access the specified registry file.");
        } finally {
            WriteLockManager.instance().releaseLockFile();
        }

    }

    /*
     * This method wraps the other file method, and converts the exception to a
     * type we want at runtime. The main write() method throws Exception,
     * because we can't use it yet the first time that is called
     * (SystemResourceService initialization issues)
     */

    /**
     * Write file2.
     * 
     * @throws LBInvocationException the LB invocation exception
     */
    private void writeFile2() throws LBInvocationException {
        try {
            writeFile();
        } catch (Exception e) {
            String id = getLogger().error("Problem writing the registry file", e);
            throw new LBInvocationException("There was an unexpected error", id);
        }
    }

    /**
     * Write file.
     * 
     * @throws Exception the exception
     */
    private synchronized void writeFile() throws Exception {
        // writeFile is only called after changed are made - so update the
        // timestamp.
        lastUpdateTime_ = System.currentTimeMillis();
        // Take the local variables, and write them out to the file.
        WriteLockManager.instance().lockLockFile();
        try {
            Document document = new Document(new Element("LexBIG_Registry"));
            Element root = document.getRootElement();
            Element newElement = new Element("variables");

            root.addContent(newElement);

            Element parentElement = newElement;

            newElement = new Element("lastUpdateTime");
            newElement.setAttribute("value", lastUpdateTime_ + "");

            parentElement.addContent(newElement);

            newElement = new Element("lastUsedDBIdentifer");
            newElement.setAttribute("value", lastUsedDBIdentifier_ + "");
            parentElement.addContent(newElement);

            newElement = new Element("lastUsedHistoryIdentifer");
            newElement.setAttribute("value", lastUsedHistoryIdentifier_ + "");

            parentElement.addContent(newElement);

            newElement = new Element("codingSchemes");
            root.addContent(newElement);

            parentElement = newElement;

            for (int i = 0; i < entries_.size(); i++) {
                newElement = new Element("codingScheme");
                DBEntry curEntry = entries_.get(i);
                newElement.setAttribute("urn", curEntry.urn);
                newElement.setAttribute("dbURL", curEntry.dbURL);
                newElement.setAttribute("dbName", curEntry.dbName);
                newElement.setAttribute("prefix", curEntry.prefix == null ? "" : curEntry.prefix);
                newElement.setAttribute("status", curEntry.status);
                newElement.setAttribute("tag", (curEntry.tag == null ? "" : curEntry.tag));
                newElement.setAttribute("version", curEntry.version);
                newElement.setAttribute("deactivateDate", curEntry.deactiveDate + "");
                newElement.setAttribute("lastUpdateDate", curEntry.lastUpdateDate + "");
                parentElement.addContent(newElement);
            }

            newElement = new Element("histories");
            root.addContent(newElement);

            parentElement = newElement;

            for (int i = 0; i < historyEntries_.size(); i++) {
                newElement = new Element("history");
                HistoryEntry curEntry = historyEntries_.get(i);
                newElement.setAttribute("urn", curEntry.urn);
                newElement.setAttribute("dbURL", curEntry.dbURL);
                newElement.setAttribute("prefix", curEntry.prefix == null ? "" : curEntry.prefix);
                newElement.setAttribute("dbName", curEntry.dbName);
                newElement.setAttribute("lastUpdateDate", curEntry.lastUpdateDate + "");
                parentElement.addContent(newElement);
            }

            XMLOutputter xmlFormatter = new XMLOutputter(Format.getPrettyFormat());

            FileWriter writer = new FileWriter(file_);

            writer.write(xmlFormatter.outputString(document));

            writer.close();

            WriteLockManager.instance().registryWasRevised();
        } catch (IOException e) {
            throw new Exception("There was a problem writing out the registry information", e);
        } finally {
            WriteLockManager.instance().releaseLockFile();
        }
    }

    /**
     * Gets the db entries.
     * 
     * @return the entries
     */
    public synchronized DBEntry[] getDBEntries() {
        return this.entries_.toArray(new DBEntry[entries_.size()]);
    }

    /**
     * Gets the history entries.
     * 
     * @return the entries
     */
    public synchronized HistoryEntry[] getHistoryEntries() {
        return this.historyEntries_.toArray(new HistoryEntry[historyEntries_.size()]);
    }

    /**
     * Gets the last update time.
     * 
     * @return the lastUpdateTime
     */
    public Date getLastUpdateTime() {
        return new Date(this.lastUpdateTime_);
    }

    /**
     * Removes the.
     * 
     * @param codingSchemeVersion the coding scheme version
     * 
     * @throws InternalException the internal exception
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public synchronized void remove(AbsoluteCodingSchemeVersionReference codingSchemeVersion)
            throws InternalException, LBInvocationException, LBParameterException {
        WriteLockManager.instance().lockLockFile();
        WriteLockManager.instance().checkForRegistryUpdates();
        try {
            // go through the instance method to get the new Registry object (if
            // it was replaced by check for
            // updates)
            ResourceManager.instance().getRegistry().removeInternal(codingSchemeVersion);
        } finally {
            WriteLockManager.instance().releaseLockFile();
        }
    }

    /**
     * Removes the internal.
     * 
     * @param codingSchemeVersion the coding scheme version
     * 
     * @throws InternalException the internal exception
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    private void removeInternal(AbsoluteCodingSchemeVersionReference codingSchemeVersion) throws InternalException,
            LBInvocationException, LBParameterException {
        try {
            DBEntry entry = getDBCodingSchemeEntry(codingSchemeVersion);

            WriteLockManager.instance().acquireLock(codingSchemeVersion.getCodingSchemeURN(),
                    codingSchemeVersion.getCodingSchemeVersion());

            // clear the hash maps
            urnVersionToEntryMap_.remove(urnVersionToEntryMap_.get(entry.urn
                    + ResourceManager.codingSchemeVersionSeparator_ + entry.version));
            urnTagToVersionMap_.remove(entry.urn + ResourceManager.codingSchemeVersionSeparator_ + entry.tag);

            // clear the array

            entries_.remove(entry);
            writeFile();
        } catch (Exception e) {
            throw new InternalException("There was a problem removing the entry from the registry", e);
        } finally {
            WriteLockManager.instance().releaseLock(codingSchemeVersion.getCodingSchemeURN(),
                    codingSchemeVersion.getCodingSchemeVersion());
            ;
        }
    }

    /**
     * Gets the history entry.
     * 
     * @param urn the urn
     * 
     * @return the history entry
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public HistoryEntry getHistoryEntry(String urn) throws LBParameterException {
        for (int i = 0; i < historyEntries_.size(); i++) {
            if (historyEntries_.get(i).urn.equals(urn)) {
                return historyEntries_.get(i);
            }
        }
        throw new LBParameterException("Unknown History URN");
    }

    /**
     * Removes the history entry.
     * 
     * @param urn the urn
     * 
     * @throws InternalException the internal exception
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public synchronized void removeHistoryEntry(String urn) throws InternalException, LBInvocationException,
            LBParameterException {
        WriteLockManager.instance().lockLockFile();
        WriteLockManager.instance().checkForRegistryUpdates();
        try {
            // go through the instance method to get the new Registry object (if
            // it was replaced by check for
            // updates)
            ResourceManager.instance().getRegistry().removeHistoryInternal(urn);
        } finally {
            WriteLockManager.instance().releaseLockFile();
        }
    }

    /**
     * Removes the history internal.
     * 
     * @param urn the urn
     * 
     * @throws InternalException the internal exception
     * @throws LBParameterException the LB parameter exception
     */
    private void removeHistoryInternal(String urn) throws InternalException, LBParameterException {
        try {
            boolean removed = false;
            for (int i = 0; i < historyEntries_.size(); i++) {
                if (historyEntries_.get(i).urn.equals(urn)) {
                    historyEntries_.remove(i);
                    removed = true;
                    break;
                }
            }
            writeFile();
            if (!removed) {
                throw new LBParameterException("Could not find that history item to remove", urn);
            }
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalException("There was a problem removing the entry from the registry", e);
        }
    }

    /**
     * Gets the next db identifier.
     * 
     * @return the lastUsedDBIdentifier
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws UnexpectedInternalError      */
    public synchronized String getNextDBIdentifier() throws LBInvocationException {
        WriteLockManager.instance().lockLockFile();
        WriteLockManager.instance().checkForRegistryUpdates();
        try {
            // go through the instance method to get the new Registry object (if
            // it was replaced by check for
            // updates)
            return ResourceManager.instance().getRegistry().getNextFreeDBIdentifierInternal();
        } finally {
            WriteLockManager.instance().releaseLockFile();
        }
    }

    /**
     * Gets the next free db identifier internal.
     * 
     * @return the next free db identifier internal
     * 
     * @throws LBInvocationException the LB invocation exception
     */
    private String getNextFreeDBIdentifierInternal() throws LBInvocationException {
        try {
            boolean singleDBMode = ResourceManager.instance().getSystemVariables().getAutoLoadSingleDBMode();
            if(!singleDBMode && !ResourceManager.instance().getSystemVariables().getOverrideSingleDbMode()){   
                String errorMessage = "Loading in Multidatabase Mode is no longer supported. Please change your " +
                    "lbconfig.props file to SINGLE_DB_MODE=true.";
                String id = getLogger().error(errorMessage);
                throw new LBInvocationException(errorMessage, id);
            }
            this.lastUsedDBIdentifier_ = DBUtility.computeNextIdentifier(this.lastUsedDBIdentifier_);
            writeFile2();
            return this.lastUsedDBIdentifier_;
        } catch (LBInvocationException e) {
            throw e;
        } catch (Exception e) {
            String id = getLogger().error("Problem getting next free db identifier", e);
            throw new LBInvocationException("There was an unexpected error incrementing the database prefix.", id);
        }
    }

    /**
     * Gets the next history identifier.
     * 
     * @return the lastUsedDBIdentifier
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws UnexpectedInternalError      */
    public synchronized String getNextHistoryIdentifier() throws LBInvocationException {
        WriteLockManager.instance().lockLockFile();
        WriteLockManager.instance().checkForRegistryUpdates();
        try {
            // go through the instance method to get the new Registry object (if
            // it was replaced by check for
            // updates)
            return ResourceManager.instance().getRegistry().getNextFreeHistoryIdentifierInternal();
        } finally {
            WriteLockManager.instance().releaseLockFile();
        }
    }

    /**
     * Gets the next free history identifier internal.
     * 
     * @return the next free history identifier internal
     * 
     * @throws LBInvocationException the LB invocation exception
     */
    private String getNextFreeHistoryIdentifierInternal() throws LBInvocationException {
        try {
            boolean singleDBMode = ResourceManager.instance().getSystemVariables().getAutoLoadSingleDBMode();

            if(!singleDBMode && !ResourceManager.instance().getSystemVariables().getOverrideSingleDbMode()){   
                String errorMessage = "Loading in Multidatabase Mode is no longer supported. Please change your " +
                "lbconfig.props file to SINGLE_DB_MODE=true.";
                String id = getLogger().error(errorMessage);
                throw new LBInvocationException(errorMessage, id);
            }

            this.lastUsedHistoryIdentifier_ = DBUtility.computeNextIdentifier(this.lastUsedDBIdentifier_);
            writeFile2();
            return this.lastUsedHistoryIdentifier_;
        } catch (LBInvocationException e) {
            throw e;
        } catch (Exception e) {
            String id = getLogger().error("Problem getting next free db identifier", e);
            throw new LBInvocationException("There was an unexpected error incrementing the database prefix.", id);
        }
    }

    /**
     * Activate a code system.
     * 
     * @param oldURNVerison the old urn verison
     * @param newURNVerison the new urn verison
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public synchronized void updateURNVersion(AbsoluteCodingSchemeVersionReference oldURNVerison,
            AbsoluteCodingSchemeVersionReference newURNVerison) throws LBInvocationException, LBParameterException {

        WriteLockManager.instance().lockLockFile();
        WriteLockManager.instance().checkForRegistryUpdates();
        try {

            ResourceManager.instance().getRegistry().updateURNVersionInternal(oldURNVerison, newURNVerison);

        } finally {
            WriteLockManager.instance().checkForRegistryUpdates();
            WriteLockManager.instance().releaseLockFile();
        }
    }

    /**
     * Update urn version internal.
     * 
     * @param oldURNVerison the old urn verison
     * @param newURNVerison the new urn verison
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    private void updateURNVersionInternal(AbsoluteCodingSchemeVersionReference oldURNVerison,
            AbsoluteCodingSchemeVersionReference newURNVerison) throws LBInvocationException, LBParameterException {

        DBEntry entry = getDBCodingSchemeEntry(oldURNVerison);

        if (entry == null) {
            throw new LBParameterException("The specified coding scheme is not registered");
        }

        try {
            WriteLockManager.instance().acquireLock(entry.urn, entry.version);
            entry.urn = newURNVerison.getCodingSchemeURN();
            entry.version = newURNVerison.getCodingSchemeVersion();
            entry.lastUpdateDate = System.currentTimeMillis();
            writeFile2();
        } finally {
            WriteLockManager.instance().releaseLock(oldURNVerison.getCodingSchemeURN(),
                    oldURNVerison.getCodingSchemeVersion());
        }
    }

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#addNewItem(org.lexevs.registry.model.RegistryEntry)
	 */
	public void addNewItem(RegistryEntry entry) throws Exception {
		throw new UnsupportedOperationException("No adding to XML Registry is allowed.");
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getAllRegistryEntries()
	 */
	public List<RegistryEntry> getAllRegistryEntries() {
		List<RegistryEntry> returnList = new ArrayList<RegistryEntry>();

		for(DBEntry entry : this.getDBEntries()){
			returnList.add(RegistryEntry.toRegistryEntry(entry));
		}

		for(HistoryEntry entry : this.getHistoryEntries()){
			returnList.add(RegistryEntry.toRegistryEntry(entry));
		}

		return returnList;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getAllRegistryEntriesOfType(org.lexevs.registry.service.Registry.ResourceType)
	 */
	public List<RegistryEntry> getAllRegistryEntriesOfType(ResourceType type) {
		List<RegistryEntry> returnList = new ArrayList<RegistryEntry>();
		
		if(type.equals(ResourceType.CODING_SCHEME)){
			for(DBEntry entry : this.getDBEntries()){
				returnList.add(RegistryEntry.toRegistryEntry(entry));
			}
		} else if(type.equals(ResourceType.NCI_HISTORY)){
			for(HistoryEntry entry : this.getHistoryEntries()){
				returnList.add(RegistryEntry.toRegistryEntry(entry));
			}
		}
		
		return returnList;
	}

	public List<RegistryEntry> getAllRegistryEntriesOfTypeAndURI(ResourceType type, String uri) {
		List<RegistryEntry> returnList = new ArrayList<RegistryEntry>();

		if(type.equals(ResourceType.CODING_SCHEME)){
			for(DBEntry entry : this.getDBEntries()){
				if(entry.urn.equals(uri)) {
					returnList.add(RegistryEntry.toRegistryEntry(entry));
				}
			}
		} else if(type.equals(ResourceType.NCI_HISTORY)){

			for(HistoryEntry entry : this.getHistoryEntries()){
				if(entry.urn.equals(uri)) {
					returnList.add(RegistryEntry.toRegistryEntry(entry));
				}
			}
		}
		return returnList;
	}
	
	public List<RegistryEntry> getAllRegistryEntriesOfTypeURIAndVersion(ResourceType type, String uri, String version) {
		List<RegistryEntry> returnList = new ArrayList<RegistryEntry>();

		if(type.equals(ResourceType.CODING_SCHEME)){
			for(DBEntry entry : this.getDBEntries()){
				if(entry.urn.equals(uri) && entry.version.equals(version)) {
					returnList.add(RegistryEntry.toRegistryEntry(entry));
				}
			}
		} else if(type.equals(ResourceType.NCI_HISTORY)){

			for(HistoryEntry entry : this.getHistoryEntries()){
				if(entry.urn.equals(uri)) {
					returnList.add(RegistryEntry.toRegistryEntry(entry));
				}
			}
		}
		return returnList;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getEntriesForUri(java.lang.String)
	 */
	public List<RegistryEntry> getEntriesForUri(String uri)
			throws LBParameterException {
		List<RegistryEntry> returnList = new ArrayList<RegistryEntry>();
		
		for(DBEntry entry : this.getDBEntries()){
			if(entry.urn.equals(uri)) {
				returnList.add(RegistryEntry.toRegistryEntry(entry));
			}
		}

		for(HistoryEntry entry : this.getHistoryEntries()){
			if(entry.urn.equals(uri)) {
				returnList.add(RegistryEntry.toRegistryEntry(entry));
			}
		}
		return returnList;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getNonCodingSchemeEntry(java.lang.String)
	 */
	public RegistryEntry getNonCodingSchemeEntry(
			String uri)
			throws LBParameterException {
		RegistryEntry entry =  RegistryEntry.toRegistryEntry(this.getHistoryEntry(uri));
		
		try {
			entry.setDbSchemaVersion(this.getSupportedLexGridSchemaVersionForHistory(uri));
		} catch (LBInvocationException e) {
			throw new RuntimeException(e);
		}
		return entry;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#containsCodingSchemeEntry(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	public boolean containsCodingSchemeEntry(
			AbsoluteCodingSchemeVersionReference codingScheme) {
		try {
			DBEntry entry = getDBCodingSchemeEntry(codingScheme);
			
			return (entry != null);
		} catch (LBParameterException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#getCodingSchemeEntry(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	public RegistryEntry getCodingSchemeEntry(
			AbsoluteCodingSchemeVersionReference codingScheme)
			throws LBParameterException {
		RegistryEntry entry =  RegistryEntry.toRegistryEntry(getDBCodingSchemeEntry(codingScheme));
		try {
			entry.setDbSchemaVersion(this.getSupportedLexGridSchemaVersionForCodingScheme(codingScheme));
		} catch (LBInvocationException e) {
			throw new RuntimeException(e);
		}
		return entry;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#removeEntry(org.lexevs.registry.model.RegistryEntry)
	 */
	public void removeEntry(RegistryEntry entry) throws LBParameterException {
		throw new UnsupportedOperationException("No editing to XML Registry is allowed.");
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#updateEntry(org.lexevs.registry.model.RegistryEntry)
	 */
	public void updateEntry(RegistryEntry entry) {
		if(entry.getResourceType().equals(ResourceType.CODING_SCHEME)) {
			AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
			ref.setCodingSchemeURN(entry.getResourceUri());
			ref.setCodingSchemeVersion(entry.getResourceVersion());
			
			try {
				RegistryEntry foundEntry = this.getCodingSchemeEntry(ref);
				if(! entry.getTag().equals(foundEntry.getTag())){
					this.updateTag(ref, entry.getTag());
				} else if (! entry.getStatus().equals(foundEntry.getStatus())){
					if(entry.getStatus().equals(CodingSchemeVersionStatus.ACTIVE.toString())) {
						this.activate(ref);
					} else {
						this.deactivate(this.getEntry(ref.getCodingSchemeURN(), ref.getCodingSchemeVersion()));
					}
				} else {
					throw new RuntimeException("Update cannot be performed.");
				}
			} catch (LBException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.registry.service.Registry#containsNonCodingSchemeEntry(java.lang.String)
	 */
	public boolean containsNonCodingSchemeEntry(String uri) {
		for (HistoryEntry entry : this.historyEntries_) {
            if (entry.urn.equals(uri)) {
                return true;
            }
        }
		return false;
	}
	
	/**
	 * Gets the supported lex grid schema version for coding scheme.
	 * 
	 * @param ref the ref
	 * 
	 * @return the supported lex grid schema version for coding scheme
	 * 
	 * @throws LBInvocationException the LB invocation exception
	 */
	protected String getSupportedLexGridSchemaVersionForCodingScheme(
			AbsoluteCodingSchemeVersionReference ref)
			throws LBInvocationException {
		try {
			DBEntry entry = this.getDBCodingSchemeEntry(ref);
			SQLTableUtilities utils = new SQLTableUtilities(dataSource, entry.prefix);
			return utils.getExistingTableVersion();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gets the supported lex grid schema version for history.
	 * 
	 * @param uri the uri
	 * 
	 * @return the supported lex grid schema version for history
	 * 
	 * @throws LBInvocationException the LB invocation exception
	 */
	protected String getSupportedLexGridSchemaVersionForHistory(
			String uri)
			throws LBInvocationException {
		try {
			HistoryEntry entry = this.getHistoryEntry(uri);
			SQLTableUtilities utils = new SQLTableUtilities(dataSource, entry.prefix);
			return utils.getExistingTableVersion();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gets the data source.
	 * 
	 * @return the data source
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * Sets the data source.
	 * 
	 * @param dataSource the new data source
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Update coding scheme entry tag.
	 * 
	 * @param codingScheme the coding scheme
	 * @param newTag the new tag
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void updateCodingSchemeEntryTag(
			AbsoluteCodingSchemeVersionReference codingScheme, String newTag)
	throws LBParameterException {

		try {
			this.updateTag(codingScheme, newTag);
		} catch (LBInvocationException e) {
			throw new RuntimeException(e);
		}
	}
}
/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.lexevs.dao.database.connection.SQLConnectionInfo;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.exceptions.InternalException;
import org.lexevs.exceptions.UnexpectedInternalError;
import org.lexevs.logging.LgLoggerIF;
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
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class XmlRegistry implements Registry {
    private long lastUpdateTime_;
    // last used db or table identifiers. If we are in multiDB mode, this
    // identifier will
    // be a number that starts at 0 and counts up.
    // If we are in single db mode, this will be a character number combination
    // that increments.
    private String lastUsedDBIdentifier_;
    private String lastUsedHistoryIdentifier_;
    private ArrayList<DBEntry> entries_;
    private ArrayList<HistoryEntry> historyEntries_;

    private Hashtable<String, DBEntry> urnVersionToEntryMap_;
    private Hashtable<String, String> urnTagToVersionMap_;
    
    private DataSource dataSource;

    private File file_;

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    public static class DBEntry {
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

    public static class HistoryEntry {
    	public String urn;
    	public String dbURL;
    	public String prefix = "";
    	public String dbName;
    	public long lastUpdateDate;

        public String getdbName() {
            return dbName;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    protected File getRegistryFile() {
        return file_;
    }

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

    public boolean isActive(String codingSchemeURN, String version) {
        // this returns null if the coding scheme isn't registered.
        CodingSchemeVersionStatus temp = getStatus(codingSchemeURN, version);

        // return true for unregistered coding schemes.
        return (temp == null || temp == CodingSchemeVersionStatus.ACTIVE ? true : false);
    }

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
     * @param codingScheme
     * @throws LBInvocationException
     * @throws LBParameterException
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

    public DBEntry getEntry(String codingSchemeURN, String version) throws LBParameterException {
        if (codingSchemeURN == null || version == null || codingSchemeURN.length() == 0 || version.length() == 0) {
            throw new LBParameterException("The URN and the version must be populated.");
        }
        return urnVersionToEntryMap_.get(codingSchemeURN + ResourceManager.codingSchemeVersionSeparator_ + version);
    }

    public DBEntry getDBCodingSchemeEntry(AbsoluteCodingSchemeVersionReference codingScheme) throws LBParameterException {
        if (codingScheme == null) {
            throw new LBParameterException(
                    "The URN and the version must be populated in the AbsoluteCodingSchemeReference");
        }
        return getEntry(codingScheme.getCodingSchemeURN(), codingScheme.getCodingSchemeVersion());
    }

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

    public String getVersionForTag(String urn, String tag) {
        return urnTagToVersionMap_.get(urn + ResourceManager.codingSchemeVersionSeparator_ + tag);
    }
    
    public XmlRegistry(SystemVariables systemVariables) throws Exception {
    	this(systemVariables.getAutoLoadRegistryPath());
    }

    public XmlRegistry(String pathToRegistryFile) throws Exception {

        try {
            file_ = new File(pathToRegistryFile);
            entries_ = new ArrayList<DBEntry>();
            historyEntries_ = new ArrayList<HistoryEntry>();
            urnVersionToEntryMap_ = new Hashtable<String, DBEntry>();
            urnTagToVersionMap_ = new Hashtable<String, String>();

            WriteLockManager.instance(file_).lockLockFile();
            try {
                if (file_.exists()) {

                    readFile();
                } else {
                    getLogger().warn(
                            "The registry file '" + file_.getAbsolutePath() + "' does not exist.  Creating a new file");

                    file_.createNewFile();

                    // set variable to defaults
                    lastUpdateTime_ = System.currentTimeMillis();
                    lastUsedDBIdentifier_ = "a0";
                    lastUsedHistoryIdentifier_ = "a0";
                    writeFile();
                }
            } finally {
                WriteLockManager.instance().releaseLockFile();
            }
        }

        catch (IOException e) {
            throw new Exception("Could not create a file to store the registration information.", e);
        }
    }

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

    private void writeFile2() throws LBInvocationException {
        try {
            writeFile();
        } catch (Exception e) {
            String id = getLogger().error("Problem writing the registry file", e);
            throw new LBInvocationException("There was an unexpected error", id);
        }
    }

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
     * @return the entries
     */
    public synchronized DBEntry[] getDBEntries() {
        return this.entries_.toArray(new DBEntry[entries_.size()]);
    }

    /**
     * @return the entries
     */
    public synchronized HistoryEntry[] getHistoryEntries() {
        return this.historyEntries_.toArray(new HistoryEntry[historyEntries_.size()]);
    }

    /**
     * @return the lastUpdateTime
     */
    public Date getLastUpdateTime() {
        return new Date(this.lastUpdateTime_);
    }

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

    public HistoryEntry getHistoryEntry(String urn) throws LBParameterException {
        for (int i = 0; i < historyEntries_.size(); i++) {
            if (historyEntries_.get(i).urn.equals(urn)) {
                return historyEntries_.get(i);
            }
        }
        throw new LBParameterException("Unknown History URN");
    }

    /**
     * @param urn
     * @throws InternalException
     * @throws LBInvocationException
     * @throws LBParameterException
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
     * @return the lastUsedDBIdentifier
     * @throws LBInvocationException
     * @throws UnexpectedInternalError
     */
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
     * @return the lastUsedDBIdentifier
     * @throws LBInvocationException
     * @throws UnexpectedInternalError
     */
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
     * @param codingScheme
     * @throws LBInvocationException
     * @throws LBParameterException
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

	public void addNewItem(RegistryEntry entry) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public List<RegistryEntry> getAllRegistryEntries() {
		// TODO Auto-generated method stub
		return null;
	}

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

	public List<RegistryEntry> getEntriesForUri(String uri)
			throws LBParameterException {
		// TODO Auto-generated method stub
		return null;
	}

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

	public boolean containsCodingSchemeEntry(
			AbsoluteCodingSchemeVersionReference codingScheme) {
		try {
			return (getDBCodingSchemeEntry(codingScheme) != null);
		} catch (LBParameterException e) {
			throw new RuntimeException(e);
		}
	}

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

	public void removeEntry(RegistryEntry entry) throws LBParameterException {
		// TODO Auto-generated method stub
		
	}

	public void updateEntry(RegistryEntry entry) throws LBInvocationException,
			LBParameterException {
		// TODO Auto-generated method stub
		
	}

	public boolean containsNonCodingSchemeEntry(String uri) {
		for (HistoryEntry entry : this.historyEntries_) {
            if (entry.urn.equals(uri)) {
                return true;
            }
        }
		return false;
	}
	
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
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
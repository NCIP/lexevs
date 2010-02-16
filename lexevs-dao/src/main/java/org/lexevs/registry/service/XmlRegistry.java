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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.lexevs.logging.LgLoggerIF;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.registry.WriteLockManager;
import org.lexevs.registry.service.Registry.DBEntry;
import org.lexevs.registry.service.Registry.HistoryEntry;
import org.lexevs.system.ResourceManager;

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
public class XmlRegistry {
	
	public enum KnownTags {PRODUCTION};
	
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

    private File file_;

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    /* (non-Javadoc)
	 * @see org.lexevs.registry.service.r#getRegistryFile()
	 */
    public File getRegistryFile() {
        return file_;
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
            setLastUpdateTime(Long.parseLong(vars.getChild("lastUpdateTime").getAttributeValue("value")));
            setLastUsedDBIdentifier(vars.getChild("lastUsedDBIdentifer").getAttributeValue("value"));

            if (vars.getChild("lastUsedHistoryIdentifer") == null) {
                setLastUsedHistoryIdentifier("a0");
            } else {
                setLastUsedHistoryIdentifier(vars.getChild("lastUsedHistoryIdentifer").getAttributeValue("value"));
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
    
    /* (non-Javadoc)
	 * @see org.lexevs.registry.service.r#getDBEntries()
	 */
    public DBEntry[] getDBEntries() {
        return this.entries_.toArray(new DBEntry[entries_.size()]);
    }
    
    public HistoryEntry[] getHistoryEntries() {
    	return this.historyEntries_.toArray(new HistoryEntry[historyEntries_.size()]);
    }

	public String getLastUsedDBIdentifier() {
		return lastUsedDBIdentifier_;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime_;
	}

	public String getLastUsedHistoryIdentifier() {
		return lastUsedHistoryIdentifier_;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		lastUpdateTime_ = lastUpdateTime;
	}

	public void setLastUsedDBIdentifier(String lastUsedDBIdentifier) {
		lastUsedDBIdentifier_ = lastUsedDBIdentifier;
	}

	public void setLastUsedHistoryIdentifier(String lastUsedHistoryIdentifier) {
		lastUsedHistoryIdentifier_ = lastUsedHistoryIdentifier;
	}

}
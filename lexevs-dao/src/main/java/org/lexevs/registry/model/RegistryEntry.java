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
package org.lexevs.registry.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.lexevs.dao.database.constants.DatabaseConstants;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.registry.service.XmlRegistry.DBEntry;
import org.lexevs.registry.service.XmlRegistry.HistoryEntry;

/**
 * The Class RegistryEntry.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Entity
@Table(name=DatabaseConstants.PREFIX_PLACEHOLDER + "registry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RegistryEntry {
	
	/** The id. */
	@Id
	@GeneratedValue(generator="system-uuid")
 	@GenericGenerator(name="system-uuid", strategy = "uuid")
 	@Column(name="registryGuid")
	public String id;
	
	/** The activation date. */
	private Timestamp activationDate;
	
	/** The base revision. */
	private String baseRevision;
	
	/** The db name. */
	private String dbName;
	
	/** The db schema description. */
	private String dbSchemaDescription;
	
	/** The db schema version. */
	private String dbSchemaVersion;
	
	/** The db uri. */
	private String dbUri;
	
	/** The deactivation date. */
	private Timestamp deactivationDate;
	
	/** The fixed at revision. */
	private String fixedAtRevision;
	
	/** The is locked. */
	private boolean isLocked;
	
	/** The last update date. */
	private Timestamp lastUpdateDate;
	
	/** The prefix. */
	private String prefix;
	
	/** The prefix. */
	private String stagingPrefix;
	
	/** The resource type. */
	@Enumerated(EnumType.STRING)
	private ResourceType resourceType;
	
	/** The resource version. */
	private String resourceVersion;
	
	/** The resource uri. */
	private String resourceUri;

	/** The status. */
	private String status;
	
	/** The tag. */
	private String tag;

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the tag.
	 * 
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Sets the tag.
	 * 
	 * @param tag the new tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the activation date.
	 * 
	 * @return the activation date
	 */
	public Timestamp getActivationDate() {
		return activationDate;
	}

	/**
	 * Sets the activation date.
	 * 
	 * @param activationDate the new activation date
	 */
	public void setActivationDate(Timestamp activationDate) {
		this.activationDate = activationDate;
	}

	/**
	 * Gets the db name.
	 * 
	 * @return the db name
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * Sets the db name.
	 * 
	 * @param dbName the new db name
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * Gets the db schema description.
	 * 
	 * @return the db schema description
	 */
	public String getDbSchemaDescription() {
		return dbSchemaDescription;
	}

	/**
	 * Sets the db schema description.
	 * 
	 * @param dbSchemaDescription the new db schema description
	 */
	public void setDbSchemaDescription(String dbSchemaDescription) {
		this.dbSchemaDescription = dbSchemaDescription;
	}

	/**
	 * Gets the db uri.
	 * 
	 * @return the db uri
	 */
	public String getDbUri() {
		return dbUri;
	}

	/**
	 * Sets the db uri.
	 * 
	 * @param dbUri the new db uri
	 */
	public void setDbUri(String dbUri) {
		this.dbUri = dbUri;
	}

	/**
	 * Gets the deactivation date.
	 * 
	 * @return the deactivation date
	 */
	public Timestamp getDeactivationDate() {
		return deactivationDate;
	}

	/**
	 * Sets the deactivation date.
	 * 
	 * @param deactivationDate the new deactivation date
	 */
	public void setDeactivationDate(Timestamp deactivationDate) {
		this.deactivationDate = deactivationDate;
	}

	/**
	 * Gets the fixed at revision.
	 * 
	 * @return the fixed at revision
	 */
	public String getFixedAtRevision() {
		return fixedAtRevision;
	}

	/**
	 * Sets the fixed at revision.
	 * 
	 * @param fixedAtRevision the new fixed at revision
	 */
	public void setFixedAtRevision(String fixedAtRevision) {
		this.fixedAtRevision = fixedAtRevision;
	}

	/**
	 * Checks if is locked.
	 * 
	 * @return true, if is locked
	 */
	public boolean isLocked() {
		return isLocked;
	}

	/**
	 * Sets the locked.
	 * 
	 * @param isLocked the new locked
	 */
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	/**
	 * Gets the last update date.
	 * 
	 * @return the last update date
	 */
	public Timestamp getLastUpdateDate() {
		return lastUpdateDate;
	}

	/**
	 * Sets the last update date.
	 * 
	 * @param lastUpdateDate the new last update date
	 */
	public void setLastUpdateDate(Timestamp lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	/**
	 * Gets the prefix.
	 * 
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Sets the prefix.
	 * 
	 * @param prefix the new prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Gets the resource type.
	 * 
	 * @return the resource type
	 */
	public ResourceType getResourceType() {
		return resourceType;
	}

	/**
	 * Sets the resource type.
	 * 
	 * @param resourceType the new resource type
	 */
	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 * Gets the resource uri.
	 * 
	 * @return the resource uri
	 */
	public String getResourceUri() {
		return resourceUri;
	}

	/**
	 * Sets the resource uri.
	 * 
	 * @param resourceUri the new resource uri
	 */
	public void setResourceUri(String resourceUri) {
		this.resourceUri = resourceUri;
	}

	/**
	 * Sets the db schema version.
	 * 
	 * @param dbSchemaVersion the new db schema version
	 */
	public void setDbSchemaVersion(String dbSchemaVersion) {
		this.dbSchemaVersion = dbSchemaVersion;
	}

	/**
	 * Gets the db schema version.
	 * 
	 * @return the db schema version
	 */
	public String getDbSchemaVersion() {
		return dbSchemaVersion;
	}

	/**
	 * Sets the resource version.
	 * 
	 * @param resourceVersion the new resource version
	 */
	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}

	/**
	 * Gets the resource version.
	 * 
	 * @return the resource version
	 */
	public String getResourceVersion() {
		return resourceVersion;
	}

	/**
	 * Sets the base revision.
	 * 
	 * @param baseRevision the new base revision
	 */
	public void setBaseRevision(String baseRevision) {
		this.baseRevision = baseRevision;
	}

	/**
	 * Gets the base revision.
	 * 
	 * @return the base revision
	 */
	public String getBaseRevision() {
		return baseRevision;
	}
	
	/**
	 * To db entry.
	 * 
	 * @param registryEntry the registry entry
	 * 
	 * @return the dB entry
	 */
	public static DBEntry toDbEntry(RegistryEntry registryEntry){
		DBEntry dbEntry = new DBEntry();
		dbEntry.dbName = registryEntry.getDbName();
		dbEntry.dbURL = registryEntry.getDbUri();
		
		if(registryEntry.getDeactivationDate() != null){
			dbEntry.deactiveDate = registryEntry.getDeactivationDate().getTime();
		}
		
		if(registryEntry.getLastUpdateDate() != null){
			dbEntry.lastUpdateDate = registryEntry.getLastUpdateDate().getTime();
		}
		
		dbEntry.prefix = registryEntry.getPrefix();
		dbEntry.tag = registryEntry.getTag();
		dbEntry.urn = registryEntry.getResourceUri();
		dbEntry.version = registryEntry.getResourceVersion();
		dbEntry.status = registryEntry.getStatus();
		
		return dbEntry;
	}
	
	/**
	 * To registry entry.
	 * 
	 * @param dbEntry the db entry
	 * 
	 * @return the registry entry
	 */
	public static RegistryEntry toRegistryEntry(DBEntry dbEntry){
		RegistryEntry entry = new RegistryEntry();
		entry.setDbName(dbEntry.dbName);
		entry.setDbUri(dbEntry.dbURL);
		entry.setDeactivationDate(new Timestamp(dbEntry.deactiveDate));
		entry.setLastUpdateDate(new Timestamp(dbEntry.lastUpdateDate));
		entry.setPrefix(dbEntry.prefix);
		entry.setStatus(dbEntry.status);
		entry.setTag(dbEntry.tag);
		entry.setResourceUri(dbEntry.urn);
		entry.setResourceVersion(dbEntry.version);
		
		return entry;
	}
	
	/**
	 * To registry entry.
	 * 
	 * @param historyEntry the history entry
	 * 
	 * @return the registry entry
	 */
	public static RegistryEntry toRegistryEntry(HistoryEntry historyEntry){
		RegistryEntry entry = new RegistryEntry();
		entry.setDbName(historyEntry.dbName);
		entry.setDbUri(historyEntry.dbURL);
		entry.setLastUpdateDate(new Timestamp(historyEntry.lastUpdateDate));
		entry.setPrefix(historyEntry.prefix);
		entry.setResourceUri(historyEntry.urn);
		
		return entry;
	}

	public void setStagingPrefix(String stagingPrefix) {
		this.stagingPrefix = stagingPrefix;
	}

	public String getStagingPrefix() {
		return stagingPrefix;
	}
}

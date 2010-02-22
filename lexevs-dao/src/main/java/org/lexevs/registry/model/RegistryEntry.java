package org.lexevs.registry.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.lexevs.dao.database.constants.DatabaseConstants;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.registry.service.XmlRegistry.DBEntry;
import org.lexevs.registry.service.XmlRegistry.HistoryEntry;

@Entity
@Table(name=DatabaseConstants.PREFIX_PLACEHOLDER + "registry")
public class RegistryEntry {
	
	@Id
	@GeneratedValue(generator="system-uuid")
 	@GenericGenerator(name="system-uuid", strategy = "uuid")
 	@Column(name="registryGuid")
	public String id;
	
	private Timestamp activationDate;
	
	private String baseRevision;
	
	private String dbName;
	
	private String dbSchemaDescription;
	
	private String dbSchemaVersion;
	
	private String dbUri;
	
	private Timestamp deactivationDate;
	
	private String fixedAtRevision;
	
	private boolean isLocked;
	
	private Timestamp lastUpdateDate;
	
	private String prefix;
	
	@Enumerated(EnumType.STRING)
	private ResourceType resourceType;
	
	private String resourceVersion;
	
	private String resourceUri;

	private String status;
	
	private String tag;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Timestamp getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Timestamp activationDate) {
		this.activationDate = activationDate;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbSchemaDescription() {
		return dbSchemaDescription;
	}

	public void setDbSchemaDescription(String dbSchemaDescription) {
		this.dbSchemaDescription = dbSchemaDescription;
	}

	public String getDbUri() {
		return dbUri;
	}

	public void setDbUri(String dbUri) {
		this.dbUri = dbUri;
	}

	public Timestamp getDeactivationDate() {
		return deactivationDate;
	}

	public void setDeactivationDate(Timestamp deactivationDate) {
		this.deactivationDate = deactivationDate;
	}

	public String getFixedAtRevision() {
		return fixedAtRevision;
	}

	public void setFixedAtRevision(String fixedAtRevision) {
		this.fixedAtRevision = fixedAtRevision;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public Timestamp getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Timestamp lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceUri() {
		return resourceUri;
	}

	public void setResourceUri(String resourceUri) {
		this.resourceUri = resourceUri;
	}

	public void setDbSchemaVersion(String dbSchemaVersion) {
		this.dbSchemaVersion = dbSchemaVersion;
	}

	public String getDbSchemaVersion() {
		return dbSchemaVersion;
	}

	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}

	public String getResourceVersion() {
		return resourceVersion;
	}

	public void setBaseRevision(String baseRevision) {
		this.baseRevision = baseRevision;
	}

	public String getBaseRevision() {
		return baseRevision;
	}
	
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
	
	public static RegistryEntry toRegistryEntry(HistoryEntry historyEntry){
		RegistryEntry entry = new RegistryEntry();
		entry.setDbName(historyEntry.dbName);
		entry.setDbUri(historyEntry.dbURL);
		entry.setLastUpdateDate(new Timestamp(historyEntry.lastUpdateDate));
		entry.setPrefix(historyEntry.prefix);
		entry.setResourceUri(historyEntry.urn);
		
		return entry;
	}
}

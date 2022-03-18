
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
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.type.NumericBooleanType;
import org.lexevs.dao.database.constants.DatabaseConstants;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.registry.service.XmlRegistry.DBEntry;
import org.lexevs.registry.service.XmlRegistry.HistoryEntry;

/**
 * The Class RegistryEntry.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@TypeDefs (
		{
			@TypeDef(
					name="numericBoolean", typeClass=NumericBooleanType.class
			)
		}
) 

@Entity
@Table(name=DatabaseConstants.PREFIX_PLACEHOLDER + "registry")
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
	@Type(type = "numericBoolean")
	private Boolean isLocked;
	
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
	
	private String supplementsUri;
	
	private String supplementsVersion;

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

	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
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

	public String getSupplementsUri() {
		return supplementsUri;
	}

	public void setSupplementsUri(String supplementsUri) {
		this.supplementsUri = supplementsUri;
	}

	public String getSupplementsVersion() {
		return supplementsVersion;
	}

	public void setSupplementsVersion(String supplementsVersion) {
		this.supplementsVersion = supplementsVersion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activationDate == null) ? 0 : activationDate.hashCode());
		result = prime * result
				+ ((baseRevision == null) ? 0 : baseRevision.hashCode());
		result = prime * result + ((dbName == null) ? 0 : dbName.hashCode());
		result = prime
				* result
				+ ((dbSchemaDescription == null) ? 0 : dbSchemaDescription
						.hashCode());
		result = prime * result
				+ ((dbSchemaVersion == null) ? 0 : dbSchemaVersion.hashCode());
		result = prime * result + ((dbUri == null) ? 0 : dbUri.hashCode());
		result = prime
				* result
				+ ((deactivationDate == null) ? 0 : deactivationDate.hashCode());
		result = prime * result
				+ ((fixedAtRevision == null) ? 0 : fixedAtRevision.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((isLocked == null) ? 0 : isLocked.hashCode());
		result = prime * result
				+ ((lastUpdateDate == null) ? 0 : lastUpdateDate.hashCode());
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
		result = prime * result
				+ ((resourceType == null) ? 0 : resourceType.hashCode());
		result = prime * result
				+ ((resourceUri == null) ? 0 : resourceUri.hashCode());
		result = prime * result
				+ ((resourceVersion == null) ? 0 : resourceVersion.hashCode());
		result = prime * result
				+ ((stagingPrefix == null) ? 0 : stagingPrefix.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((supplementsUri == null) ? 0 : supplementsUri.hashCode());
		result = prime
				* result
				+ ((supplementsVersion == null) ? 0 : supplementsVersion
						.hashCode());
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegistryEntry other = (RegistryEntry) obj;
		if (activationDate == null) {
			if (other.activationDate != null)
				return false;
		} else if (!activationDate.equals(other.activationDate))
			return false;
		if (baseRevision == null) {
			if (other.baseRevision != null)
				return false;
		} else if (!baseRevision.equals(other.baseRevision))
			return false;
		if (dbName == null) {
			if (other.dbName != null)
				return false;
		} else if (!dbName.equals(other.dbName))
			return false;
		if (dbSchemaDescription == null) {
			if (other.dbSchemaDescription != null)
				return false;
		} else if (!dbSchemaDescription.equals(other.dbSchemaDescription))
			return false;
		if (dbSchemaVersion == null) {
			if (other.dbSchemaVersion != null)
				return false;
		} else if (!dbSchemaVersion.equals(other.dbSchemaVersion))
			return false;
		if (dbUri == null) {
			if (other.dbUri != null)
				return false;
		} else if (!dbUri.equals(other.dbUri))
			return false;
		if (deactivationDate == null) {
			if (other.deactivationDate != null)
				return false;
		} else if (!deactivationDate.equals(other.deactivationDate))
			return false;
		if (fixedAtRevision == null) {
			if (other.fixedAtRevision != null)
				return false;
		} else if (!fixedAtRevision.equals(other.fixedAtRevision))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isLocked == null) {
			if (other.isLocked != null)
				return false;
		} else if (!isLocked.equals(other.isLocked))
			return false;
		if (lastUpdateDate == null) {
			if (other.lastUpdateDate != null)
				return false;
		} else if (!lastUpdateDate.equals(other.lastUpdateDate))
			return false;
		if (prefix == null) {
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
			return false;
		if (resourceType == null) {
			if (other.resourceType != null)
				return false;
		} else if (!resourceType.equals(other.resourceType))
			return false;
		if (resourceUri == null) {
			if (other.resourceUri != null)
				return false;
		} else if (!resourceUri.equals(other.resourceUri))
			return false;
		if (resourceVersion == null) {
			if (other.resourceVersion != null)
				return false;
		} else if (!resourceVersion.equals(other.resourceVersion))
			return false;
		if (stagingPrefix == null) {
			if (other.stagingPrefix != null)
				return false;
		} else if (!stagingPrefix.equals(other.stagingPrefix))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (supplementsUri == null) {
			if (other.supplementsUri != null)
				return false;
		} else if (!supplementsUri.equals(other.supplementsUri))
			return false;
		if (supplementsVersion == null) {
			if (other.supplementsVersion != null)
				return false;
		} else if (!supplementsVersion.equals(other.supplementsVersion))
			return false;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		return true;
	}
}
package org.lexevs.dao.registry.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractRegistryEntry {
	
	@Column(name="uri")
	private String uri;
	
	@Column(name="version")
	private String version;
	
	@Column(name="prefix")
	private String prefix;
	
	@Column(name="lastUpdateDate")
	private Timestamp lastUpdateDate;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Timestamp getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Timestamp lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
}

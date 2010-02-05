package org.lexevs.dao.registry.model;

import java.util.UUID;

import javax.persistence.Table;

import org.hibernate.annotations.Entity;

@Entity
@Table(name="@PREFIX@historyentry")
public class HistoryEntry {

	public UUID id;
	
	private String urn;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUrn() {
		return urn;
	}

	public void setUrn(String urn) {
		this.urn = urn;
	}
	
	
}

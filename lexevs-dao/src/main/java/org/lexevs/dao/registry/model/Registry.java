package org.lexevs.dao.registry.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name="registry")
public class Registry implements Serializable {

	private static final long serialVersionUID = 2383440967007176901L;

	@Id
	private int id = 0;
	
	private Calendar lastUpdateTime;
	
	@OneToMany
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
	public Set<CodingSchemeEntry> codingSchemeEntry;

	public Set<CodingSchemeEntry> getCodingSchemeEntry() {
		return codingSchemeEntry;
	}

	public void setCodingSchemeEntry(Set<CodingSchemeEntry> codingSchemeEntry) {
		this.codingSchemeEntry = codingSchemeEntry;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setLastUpdateTime(Calendar lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Calendar getLastUpdateTime() {
		return lastUpdateTime;
	}
}

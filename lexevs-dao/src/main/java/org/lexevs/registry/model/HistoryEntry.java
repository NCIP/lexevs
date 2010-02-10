package org.lexevs.registry.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.lexevs.dao.database.constants.DatabaseConstants;


@Entity
@Table(name=DatabaseConstants.PREFIX_PLACEHOLDER + "historyentry")
public class HistoryEntry extends AbstractRegistryEntry {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}

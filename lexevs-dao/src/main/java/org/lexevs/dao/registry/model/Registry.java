package org.lexevs.dao.registry.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.lexevs.dao.database.constants.DatabaseConstants;

@Entity
@Table(name=DatabaseConstants.PREFIX_PLACEHOLDER + "registry")
public class Registry implements Serializable {

	private static final long serialVersionUID = 2383440967007176901L;

	@Id
	@Column(name="id")
	private int id = 0;
	
	@Column(name="lastUpdateTime")
	private Timestamp lastUpdateTime;
	
	@Column(name="lastUsedDbIdentifer")
	private String lastUsedDbIdentifer;
	
	@Column(name="lastUsedHistoryIdentifer")
	private String lastUsedHistoryIdentifer;
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}


	public String getLastUsedDbIdentifer() {
		return lastUsedDbIdentifer;
	}

	public void setLastUsedDbIdentifer(String lastUsedDbIdentifer) {
		this.lastUsedDbIdentifer = lastUsedDbIdentifer;
	}

	public String getLastUsedHistoryIdentifer() {
		return lastUsedHistoryIdentifer;
	}

	public void setLastUsedHistoryIdentifer(String lastUsedHistoryIdentifer) {
		this.lastUsedHistoryIdentifer = lastUsedHistoryIdentifer;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}
	
}

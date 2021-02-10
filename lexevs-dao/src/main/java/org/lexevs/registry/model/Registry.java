
package org.lexevs.registry.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.lexevs.dao.database.constants.DatabaseConstants;

/**
 * The Class Registry.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Entity
@Table(name=DatabaseConstants.PREFIX_PLACEHOLDER + "registryMetaData")
public class Registry implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2383440967007176901L;

	/** The id. */
	@Id
	private int id = 0;
	
	/** The last update time. */
	@Column(name="lastUpdateTime")
	private Timestamp lastUpdateTime;
	
	/** The last used db identifer. */
	@Column(name="lastUsedDbIdentifer")
	private String lastUsedDbIdentifer;
	
	/** The last used history identifer. */
	@Column(name="lastUsedHistoryIdentifer")
	private String lastUsedHistoryIdentifer;
	
	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}


	/**
	 * Gets the last used db identifer.
	 * 
	 * @return the last used db identifer
	 */
	public String getLastUsedDbIdentifer() {
		return lastUsedDbIdentifer;
	}

	/**
	 * Sets the last used db identifer.
	 * 
	 * @param lastUsedDbIdentifer the new last used db identifer
	 */
	public void setLastUsedDbIdentifer(String lastUsedDbIdentifer) {
		this.lastUsedDbIdentifer = lastUsedDbIdentifer;
	}

	/**
	 * Gets the last used history identifer.
	 * 
	 * @return the last used history identifer
	 */
	public String getLastUsedHistoryIdentifer() {
		return lastUsedHistoryIdentifer;
	}

	/**
	 * Sets the last used history identifer.
	 * 
	 * @param lastUsedHistoryIdentifer the new last used history identifer
	 */
	public void setLastUsedHistoryIdentifer(String lastUsedHistoryIdentifer) {
		this.lastUsedHistoryIdentifer = lastUsedHistoryIdentifer;
	}

	/**
	 * Sets the last update time.
	 * 
	 * @param lastUpdateTime the new last update time
	 */
	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * Gets the last update time.
	 * 
	 * @return the last update time
	 */
	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result
				+ ((lastUpdateTime == null) ? 0 : lastUpdateTime.hashCode());
		result = prime
				* result
				+ ((lastUsedDbIdentifer == null) ? 0 : lastUsedDbIdentifer
						.hashCode());
		result = prime
				* result
				+ ((lastUsedHistoryIdentifer == null) ? 0
						: lastUsedHistoryIdentifer.hashCode());
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
		Registry other = (Registry) obj;
		if (id != other.id)
			return false;
		if (lastUpdateTime == null) {
			if (other.lastUpdateTime != null)
				return false;
		} else if (!lastUpdateTime.equals(other.lastUpdateTime))
			return false;
		if (lastUsedDbIdentifer == null) {
			if (other.lastUsedDbIdentifer != null)
				return false;
		} else if (!lastUsedDbIdentifer.equals(other.lastUsedDbIdentifer))
			return false;
		if (lastUsedHistoryIdentifer == null) {
			if (other.lastUsedHistoryIdentifer != null)
				return false;
		} else if (!lastUsedHistoryIdentifer
				.equals(other.lastUsedHistoryIdentifer))
			return false;
		return true;
	}
}
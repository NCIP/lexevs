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

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.lexevs.dao.database.constants.DatabaseConstants;

/**
 * The Class Registry.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Entity
@Table(name=DatabaseConstants.PREFIX_PLACEHOLDER + "registryMetaData")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
	
}

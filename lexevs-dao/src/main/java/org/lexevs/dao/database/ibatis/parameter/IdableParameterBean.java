/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.database.ibatis.parameter;

/**
 * The Class IdableParameterBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IdableParameterBean extends PrefixedTableParameterBean {

	/** The id. */
	private String uid;
	
	/** The entry state id. */
	private String entryStateUId;
	
	/**
	 * Instantiates a new idable parameter bean.
	 */
	public IdableParameterBean() {
		super();
	}
	
	/**
	 * Instantiates a new idable parameter bean.
	 * 
	 * @param prefix the prefix
	 * @param uid the id
	 * @param entryStateUId the entry state id
	 */
	public IdableParameterBean(String prefix, String uid, String entryStateUId) {
		super(prefix);
		this.uid = uid;
		this.entryStateUId = entryStateUId;
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getUId() {
		return uid;
	}
	
	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	public void setUId(String id) {
		this.uid = id;
	}
	
	/**
	 * Gets the entry state id.
	 * 
	 * @return the entry state id
	 */
	public String getEntryStateUId() {
		return entryStateUId;
	}
	
	/**
	 * Sets the entry state id.
	 * 
	 * @param entryStateUId the new entry state id
	 */
	public void setEntryStateUId(String entryStateUId) {
		this.entryStateUId = entryStateUId;
	}
}
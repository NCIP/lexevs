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
package org.lexevs.dao.database.ibatis.parameter;

/**
 * The Class IdableParameterBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IdableParameterBean extends PrefixedTableParameterBean {

	/** The id. */
	private String id;
	
	/** The entry state id. */
	private String entryStateId;
	
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
	 * @param id the id
	 * @param entryStateId the entry state id
	 */
	public IdableParameterBean(String prefix, String id, String entryStateId) {
		super(prefix);
		this.id = id;
		this.entryStateId = entryStateId;
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
	 * Gets the entry state id.
	 * 
	 * @return the entry state id
	 */
	public String getEntryStateId() {
		return entryStateId;
	}
	
	/**
	 * Sets the entry state id.
	 * 
	 * @param entryStateId the new entry state id
	 */
	public void setEntryStateId(String entryStateId) {
		this.entryStateId = entryStateId;
	}
}

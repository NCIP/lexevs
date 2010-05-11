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
package org.lexevs.dao.database.ibatis.association.parameter;

import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertRelationsBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertOrUpdateRelationsBean extends IdableParameterBean {
	
	/** The relations. */
	private Relations relations;
	
	/** The coding scheme id. */
	private String codingSchemeUId;

	/**
	 * Sets the relations.
	 * 
	 * @param relations the new relations
	 */
	public void setRelations(Relations relations) {
		this.relations = relations;
	}

	/**
	 * Gets the relations.
	 * 
	 * @return the relations
	 */
	public Relations getRelations() {
		return relations;
	}

	/**
	 * Sets the coding scheme id.
	 * 
	 * @param codingSchemeUId the new coding scheme id
	 */
	public void setCodingSchemeUId(String codingSchemeUId) {
		this.codingSchemeUId = codingSchemeUId;
	}

	/**
	 * Gets the coding scheme id.
	 * 
	 * @return the coding scheme id
	 */
	public String getCodingSchemeUId() {
		return codingSchemeUId;
	}
}

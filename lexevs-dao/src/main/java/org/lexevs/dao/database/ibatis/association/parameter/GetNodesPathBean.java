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
package org.lexevs.dao.database.ibatis.association.parameter;

import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class GetNodesPathBean extends IdableParameterBean{

/** The association predicate id. */
private String associationPredicateUId;

	/** The source entity code. */
	private String sourceEntityCode;
	
	/** The source entity code namespace. */
	private String sourceEntityCodeNamespace;
	
	/** The target entity code. */
	private String targetEntityCode;
	
	/** The target entity code namespace. */
	private String targetEntityCodeNamespace;

	/**
	 * Instantiates a new insert transitive closure bean.
	 */
	public GetNodesPathBean() {
		super();
	}

	/**
	 * Gets the association predicate id.
	 * 
	 * @return the association predicate id
	 */
	public String getAssociationPredicateUId() {
		return associationPredicateUId;
	}

	/**
	 * Sets the association predicate id.
	 * 
	 * @param associationPredicateUId the new association predicate id
	 */
	public void setAssociationPredicateUId(String associationPredicateUId) {
		this.associationPredicateUId = associationPredicateUId;
	}

	/**
	 * Gets the source entity code.
	 * 
	 * @return the source entity code
	 */
	public String getSourceEntityCode() {
		return sourceEntityCode;
	}

	/**
	 * Sets the source entity code.
	 * 
	 * @param sourceEntityCode the new source entity code
	 */
	public void setSourceEntityCode(String sourceEntityCode) {
		this.sourceEntityCode = sourceEntityCode;
	}

	/**
	 * Gets the source entity code namespace.
	 * 
	 * @return the source entity code namespace
	 */
	public String getSourceEntityCodeNamespace() {
		return sourceEntityCodeNamespace;
	}

	/**
	 * Sets the source entity code namespace.
	 * 
	 * @param sourceEntityCodeNamespace the new source entity code namespace
	 */
	public void setSourceEntityCodeNamespace(String sourceEntityCodeNamespace) {
		this.sourceEntityCodeNamespace = sourceEntityCodeNamespace;
	}

	/**
	 * Gets the target entity code.
	 * 
	 * @return the target entity code
	 */
	public String getTargetEntityCode() {
		return targetEntityCode;
	}

	/**
	 * Sets the target entity code.
	 * 
	 * @param targetEntityCode the new target entity code
	 */
	public void setTargetEntityCode(String targetEntityCode) {
		this.targetEntityCode = targetEntityCode;
	}

	/**
	 * Gets the target entity code namespace.
	 * 
	 * @return the target entity code namespace
	 */
	public String getTargetEntityCodeNamespace() {
		return targetEntityCodeNamespace;
	}

	/**
	 * Sets the target entity code namespace.
	 * 
	 * @param targetEntityCodeNamespace the new target entity code namespace
	 */
	public void setTargetEntityCodeNamespace(String targetEntityCodeNamespace) {
		this.targetEntityCodeNamespace = targetEntityCodeNamespace;
	}
}
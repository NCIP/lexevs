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
package org.lexevs.dao.database.access.association.model;

public class Triple {

	private String sourceEntityCode;
	private String sourceEntityNamespace;
	private String targetEntityCode;
	private String targetEntityNamespace;
	private String associationPredicateId;
	
	public String getSourceEntityCode() {
		return sourceEntityCode;
	}
	public void setSourceEntityCode(String sourceEntityCode) {
		this.sourceEntityCode = sourceEntityCode;
	}
	public String getSourceEntityNamespace() {
		return sourceEntityNamespace;
	}
	public void setSourceEntityNamespace(String sourceEntityNamespace) {
		this.sourceEntityNamespace = sourceEntityNamespace;
	}
	public String getTargetEntityCode() {
		return targetEntityCode;
	}
	public void setTargetEntityCode(String targetEntityCode) {
		this.targetEntityCode = targetEntityCode;
	}
	public String getTargetEntityNamespace() {
		return targetEntityNamespace;
	}
	public void setTargetEntityNamespace(String targetEntityNamespace) {
		this.targetEntityNamespace = targetEntityNamespace;
	}
	public String getAssociationPredicateId() {
		return associationPredicateId;
	}
	public void setAssociationPredicateId(String associationPredicateId) {
		this.associationPredicateId = associationPredicateId;
	}
}
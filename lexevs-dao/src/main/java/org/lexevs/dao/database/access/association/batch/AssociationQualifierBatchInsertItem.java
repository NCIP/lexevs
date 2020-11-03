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
package org.lexevs.dao.database.access.association.batch;

import org.LexGrid.relations.AssociationQualification;

/**
 * The Class AssociationqualifierBatchInsertItem.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AssociationQualifierBatchInsertItem {

	/** The parent id. */
	private String parentId;
	
	/** The association qualifier. */
	private AssociationQualification associationQualifier;
	
	/**
	 * Instantiates a new association qualifier batch insert item.
	 */
	public AssociationQualifierBatchInsertItem(){
		super();
	}
	
	/**
	 * Instantiates a new association qualifier batch insert item.
	 * 
	 * @param parentId the parent id
	 * @param associationQualifier the association qualifier
	 */
	public AssociationQualifierBatchInsertItem(String parentId, AssociationQualification associationQualifier) {
		super();
		this.parentId = parentId;
		this.associationQualifier = associationQualifier;
	}

	/**
	 * Gets the parent id.
	 * 
	 * @return the parent id
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * Sets the parent id.
	 * 
	 * @param parentId the new parent id
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * Gets the association qualifier.
	 * 
	 * @return the association qualifier
	 */
	public AssociationQualification getAssociationQualifier() {
		return associationQualifier;
	}

	/**
	 * Sets the association qualifier.
	 * 
	 * @param associationQualifier the new association qualifier
	 */
	public void setAssociationQualifier(AssociationQualification associationQualifier) {
		this.associationQualifier = associationQualifier;
	}

}
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
package org.lexevs.dao.database.access.association.batch;

import org.LexGrid.relations.AssociationSource;

/**
 * The Class AssociationSourceBatchInsertItem.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AssociationSourceBatchInsertItem {

	/** The parent id. */
	private String parentId;
	
	/** The association source. */
	private AssociationSource associationSource;
	
	/**
	 * Instantiates a new association source batch insert item.
	 */
	public AssociationSourceBatchInsertItem(){
		super();
	}
	
	/**
	 * Instantiates a new association source batch insert item.
	 * 
	 * @param parentId the parent id
	 * @param associationSource the association source
	 */
	public AssociationSourceBatchInsertItem(String parentId, AssociationSource associationSource) {
		super();
		this.parentId = parentId;
		this.associationSource = associationSource;
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
	 * Gets the association source.
	 * 
	 * @return the association source
	 */
	public AssociationSource getAssociationSource() {
		return associationSource;
	}

	/**
	 * Sets the association source.
	 * 
	 * @param associationSource the new association source
	 */
	public void setAssociationSource(AssociationSource associationSource) {
		this.associationSource = associationSource;
	}

}

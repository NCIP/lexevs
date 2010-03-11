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

import org.LexGrid.relations.AssociationPredicate;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertAssociationPredicateBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertAssociationPredicateBean extends IdableParameterBean {
	
	/** The association predicate. */
	private AssociationPredicate associationPredicate;
	
	/** The relation id. */
	private String relationId;
	
	/**
	 * Gets the association predicate.
	 * 
	 * @return the association predicate
	 */
	public AssociationPredicate getAssociationPredicate() {
		return associationPredicate;
	}
	
	/**
	 * Sets the association predicate.
	 * 
	 * @param associationPredicate the new association predicate
	 */
	public void setAssociationPredicate(AssociationPredicate associationPredicate) {
		this.associationPredicate = associationPredicate;
	}
	
	/**
	 * Gets the relation id.
	 * 
	 * @return the relation id
	 */
	public String getRelationId() {
		return relationId;
	}
	
	/**
	 * Sets the relation id.
	 * 
	 * @param relationId the new relation id
	 */
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
}

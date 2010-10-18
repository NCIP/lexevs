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

import java.util.List;

import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationSource;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertOrUpdateAssociationDataBean extends IdableParameterBean{

/** The association predicate id. */
private String associationPredicateUId;
	
	/** The association source. */
	private AssociationSource associationSource;
	
	/** The association data. */
	private AssociationData associationData;
	
	/** The association qualifications and usage contexts.*/
	private List<InsertAssociationQualificationOrUsageContextBean> assnQualsAndUsageContextList = null;
	
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
	
	/**
	 * Gets the association data.
	 * 
	 * @return the association data
	 */
	public AssociationData getAssociationData() {
		return associationData;
	}
	
	/**
	 * Sets the association data.
	 * 
	 * @param associationData the new association data
	 */
	public void setAssociationData(AssociationData associationData) {
		this.associationData = associationData;
	}

	/**
	 * @return the assnQualsAndUsageContext
	 */
	public List<InsertAssociationQualificationOrUsageContextBean> getAssnQualsAndUsageContext() {
		return assnQualsAndUsageContextList;
	}

	/**
	 * @param assnQualsAndUsageContext the assnQualsAndUsageContext to set
	 */
	public void setAssnQualsAndUsageContext(
			List<InsertAssociationQualificationOrUsageContextBean> assnQualsAndUsageContext) {
		this.assnQualsAndUsageContextList = assnQualsAndUsageContext;
	}
}
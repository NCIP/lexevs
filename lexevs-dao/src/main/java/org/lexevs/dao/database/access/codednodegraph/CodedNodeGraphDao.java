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
package org.lexevs.dao.database.access.codednodegraph;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

/**
 * The Interface CodedNodeGraphDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CodedNodeGraphDao extends LexGridSchemaVersionAwareDao {
	
	public enum TripleNode {SUBJECT, OBJECT}
	
	public List<String> getTripleUidsContainingSubject(
			String codingSchemeUid,
			String associationPredicateUid,
			String subjectEntityCode,
			String subjectEntityCodeNamespace, 
			int start, 
			int pageSize);
	
	public int getTripleUidsContainingSubjectCount(
			String codingSchemeUid,
			String associationPredicateUid,
			String subjectEntityCode,
			String subjectEntityCodeNamespace);
	
	public List<String> getTripleUidsContainingObject(
			String codingSchemeUid,
			String associationPredicateUid,
			String objectEntityCode,
			String objectEntityCodeNamespace, 
			int start, 
			int pageSize);
	
	public int getTripleUidsContainingObjectCount(
			String codingSchemeUid,
			String associationPredicateUid,
			String objectEntityCode,
			String objectEntityCodeNamespace);
	
	public AssociatedConcept getAssociatedConceptFromUid(
			String codingSchemeUid,
			String tripleUid,
			TripleNode node);
}

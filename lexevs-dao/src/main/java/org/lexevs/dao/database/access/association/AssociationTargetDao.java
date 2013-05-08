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
package org.lexevs.dao.database.access.association;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.inserter.Inserter;

public interface AssociationTargetDao extends LexGridSchemaVersionAwareDao {

	public String insertAssociationTarget(String codingSchemeUId, String associationPredicateUId, AssociationSource source, AssociationTarget target);

	public String insertAssociationTarget(
			String codingSchemeUId, 
			String associationPredicateUId, 
			String sourceEntityCode, 
			String sourceEntityCodeNamespace,
			AssociationTarget target);

	public String updateAssociationTarget(String codingSchemeUId, String associationTargetUId, AssociationTarget target);

	public String updateVersionableChanges(String codingSchemeUId, String associationTargetUId, AssociationTarget target);

	public String getAssociationTargetUId(String codingSchemeUId, String associationInstanceId);
	
	public AssociationSource getTripleByUid(String codingSchemeUId, String tripleUid);

	public String insertHistoryAssociationTarget(String codingSchemeUId,
			String assnEntityTripleUId, Boolean assnQualExists, Boolean contextExists);

	public void deleteAssnTargetByUId(String codingSchemeUId,
			String associationTargetUId);

	public String getLatestRevision(String csUId, String targetUId);

	public void deleteAssociationMultiAttribsByAssociationTargetUId(
			String codingSchemeUId, String associationTargetUId);

	public String insertAssociationTarget(String codingSchemeUId,
			String associationPredicateUId, AssociationSource source,
			AssociationTarget target, Inserter inserter);

	public boolean entryStateExists(String codingSchemeUId, String entryStateUId);
	
	public String getEntryStateUId(String codingSchemeUId, String associationTargetUid);
	
	public AssociationSource getHistoryTripleByRevision(
			String codingSchemeUId, 
			String tripleUid, 
			String revisionId);
}
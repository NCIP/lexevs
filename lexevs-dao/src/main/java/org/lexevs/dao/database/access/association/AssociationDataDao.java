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

import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationSource;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.inserter.Inserter;

public interface AssociationDataDao extends LexGridSchemaVersionAwareDao {

	public String insertAssociationData(String codingSchemeUId,
			String associationPredicateUId, AssociationSource source,
			AssociationData data);

	public String getAssociationDataUId(String codingSchemeUId,
			String associationInstanceId);

	public String insertHistoryAssociationData(String codingSchemeUId,
			String associationDataUId, Boolean assnQualExist,
			Boolean contextExist);

	public String updateAssociationData(String codingSchemeUId,
			String associationDataUId,
			AssociationData data);

	public void deleteAssociationData(String codingSchemeUId,
			String associationDataUId);

	public String updateVersionableChanges(String codingSchemeUId,
			String associationDataUId,
			AssociationData data);

	public void deleteAllAssocQualsByAssocDataUId(String codingSchemeUId,
			String associationDataUId);

	public String getLatestRevision(String csUId, String assocDataUId);
	
	public boolean entryStateExists(String codingSchemeUId, String entryStateUId);

	public String insertAssociationData(String codingSchemeUId,
			String associationPredicateUId, AssociationSource source,
			AssociationData data, Inserter inserter);

	public AssociationData getAssociationDataByUid(
			String codingSchemeUId,
			String associationDataUid);

	public String getEntryStateUId(String codingSchemeUId, String associationDataUid);

	public AssociationData getHistoryAssociationDataByRevision(
			String codingSchemeUId, 
			String associationDataUid, 
			String revisionId);

	public void insertAssociationData(
			String codingSchemeUId,
			String associationPredicateUId, 
			String sourceEntityCode,
			String sourceEntityCodeNamespace, 
			AssociationData data);

	public AssociationSource getTripleByUid(String codingSchemeUId, String tripleUid);

}
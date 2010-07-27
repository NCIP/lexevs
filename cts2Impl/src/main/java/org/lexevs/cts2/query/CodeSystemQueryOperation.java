/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and Research
 * (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the triple-shield Mayo
 * logo are trademarks and service marks of MFMER.
 * 
 * Except as contained in the copyright notice above, or as used to identify
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.lexevs.cts2.query;

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.relations.AssociationEntity;

public interface CodeSystemQueryOperation {

	/**
	 * List the coding systems that are available in cts 2 system
	 * 
	 * @param queryByExample
	 *            . Query by example.
	 * @return a list of codingSystems
	 * @throws LBInvocationException
	 */
	public CodingSchemeRenderingList listCodeSystems(
			CodingSchemeSummary queryByExample);

	/**
	 * Return a detailed codingScheme according to provided codingName and
	 * versionOrTag
	 * 
	 * @param codingSchemeName
	 * @param versionOrTag
	 * @return codingSystem
	 * @throws LBInvocationException
	 * @throws LBParameterException
	 */
	public CodingScheme getCodeSystemDetails(String codingSchemeName,
			CodingSchemeVersionOrTag versionOrTag);

	/**
	 * List the concepts in a specific codingSystem
	 * 
	 * @param codingSchemeName
	 * @param versionOrTag
	 * @param entityTypes (Optional)
	 * @param sortOptionList (Optional) 
	 * @return Iterator of codingSystemEntities
	 * @throws LBException
	 */
	public ResolvedConceptReferencesIterator listCodeSystemConcepts(
			String codingSchemeName, CodingSchemeVersionOrTag versionOrTag,
			LocalNameList entityTypes, SortOptionList sortOptionList);

	/**
	 * Return an instance of Entity according the requests
	 * 
	 * @param codingSchemeName
	 * @param versionOrTag
	 * @param code
	 * @param namespace
	 * @return coding system entity
	 * @throws LBException
	 */
	public Entity getConceptDetails(String codingSchemeName,
			CodingSchemeVersionOrTag versionOrTag, String code, String namespace);

	/**
	 * List all the associationTypes in a specific codingSystem
	 * 
	 * @param codingSchemeName
	 * @param versionOrTag
	 * @return a list of association types
	 */
	public List<SupportedAssociation> listAssociationTypes(
			String codingSchemeName, CodingSchemeVersionOrTag versionOrTag);

	/**
	 * Return detailed information for a certain associationType
	 * 
	 * @param codingSchemeName
	 * @param versionOrTag
	 * @param associationName
	 * @return association type detail
	 * @throws LBException
	 */
	public AssociationEntity getAssociationTypeDetails(String codingSchemeName,
			CodingSchemeVersionOrTag versionOrTag, String associationName);

}

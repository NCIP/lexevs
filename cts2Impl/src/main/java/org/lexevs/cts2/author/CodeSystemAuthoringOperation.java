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
package org.lexevs.cts2.author;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.cts2.exception.author.InvalidCodeSystemSupplementException;

public interface CodeSystemAuthoringOperation {
	/**
	 * 
	 * @param csURIAndVersion
	 * @param codeSystemProperties
	 * @return
	 * @throws LBException
	 */
	public CodingScheme createCodeSystem(RevisionInfo revision, String codingSchemeName, String codingSchemeURI, String formalName,
            String defaultLanguage, long approxNumConcepts, String representsVersion, List<String> localNameList,
            List<Source> sourceList, Text copyright, Mappings mappings, Properties properties, Entities entities,
            List<Relations>  relationsList) throws LBException;
	
	public boolean removeCodeSystem(RevisionInfo revision, String codingSchemeURI, String representsVersion) throws LBException;

	public CodingScheme updateCodeSystem(RevisionInfo revision, String codingSchemeName, String codingSchemeURI, String formalName,
	        String defaultLanguage, long approxNumConcepts, String representsVersion, List<String> localNameList,
	        List<Source> sourceList, Text copyright, Mappings mappings, Properties properties, Entities entities,
	        List<Relations>  relationsList) throws LBException;
	
	public int commitCodeSystem(CodingScheme codeSystem, RevisionInfo revision, ChangeType changeType) throws LBException;
	
	public Revision createCodeSystemChangeSet(String agent, String changeInstruction);
	
	public int commitChangeSet(Revision changeSet);
	
	public void updateCodeSystemVersion(String codingSchemeUri, String codeSystemVersion);
	public void updateCodeSystemVersionStatus(String codingSchemeUri, String codeSystemVersion);
	public void createCodeSystemSuppliment(AbsoluteCodingSchemeVersionReference parent, AbsoluteCodingSchemeVersionReference supplement) throws InvalidCodeSystemSupplementException;
	public void updateCodeSystemSuppliment() throws InvalidCodeSystemSupplementException;
	
	
	public void createConcept(
			String codingSchemeUri, 
			String codeSystemVersion, 
			String conceptCode, 
			String namespace, 
			RevisionInfo revision) throws LBException;
	
	public void updateConcept(
			String codingSchemeUri, 
			String codeSystemVersion, 
			Entity entity,
			RevisionInfo revision) throws LBException;
	
	public void updateConceptStatus();
	public void createAssociationType();
	public void updateAssociationType();


}

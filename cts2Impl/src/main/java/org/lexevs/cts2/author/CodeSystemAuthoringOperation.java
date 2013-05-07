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
package org.lexevs.cts2.author;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.versions.Revision;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.cts2.exception.author.InvalidCodeSystemSupplementException;

/**
 * The Interface CodeSystemAuthoringOperation.
 */
public interface CodeSystemAuthoringOperation {

/**
 * Creates the code system.
 * 
 * @param revision the revision
 * @param codingSchemeName the coding scheme name
 * @param codingSchemeURI the coding scheme uri
 * @param formalName the formal name
 * @param defaultLanguage the default language
 * @param approxNumConcepts the approx num concepts
 * @param representsVersion the represents version
 * @param localNameList the local name list
 * @param sourceList the source list
 * @param copyright the copyright
 * @param mappings the mappings
 * 
 * @return the coding scheme
 * 
 * @throws LBException the LB exception
 */
public CodingScheme createCodeSystem(RevisionInfo revision, String codingSchemeName, String codingSchemeURI, String formalName,
            String defaultLanguage, long approxNumConcepts, String representsVersion, List<String> localNameList,
            List<Source> sourceList, Text copyright, Mappings mappings) throws LBException;
	
	/**
	 * Removes the code system.
	 * 
	 * @param revision the revision
	 * @param codingSchemeURI the coding scheme uri
	 * @param representsVersion the represents version
	 * 
	 * @return true, if successful
	 * 
	 * @throws LBException the LB exception
	 */
	public boolean removeCodeSystem(RevisionInfo revision, String codingSchemeURI, String representsVersion) throws LBException;

	/**
	 * Update code system.
	 * 
	 * @param revision the revision
	 * @param codingSchemeName the coding scheme name
	 * @param codingSchemeURI the coding scheme uri
	 * @param formalName the formal name
	 * @param defaultLanguage the default language
	 * @param approxNumConcepts the approx num concepts
	 * @param representsVersion the represents version
	 * @param localNameList the local name list
	 * @param sourceList the source list
	 * @param copyright the copyright
	 * @param mappings the mappings
	 * 
	 * @return the coding scheme
	 * 
	 * @throws LBException the LB exception
	 */
	public CodingScheme updateCodeSystem(RevisionInfo revision, String codingSchemeName, String codingSchemeURI, String formalName,
	        String defaultLanguage, long approxNumConcepts, String representsVersion, List<String> localNameList,
	        List<Source> sourceList, Text copyright, Mappings mappings) throws LBException;
	
	/**
	 * Adds the code system properties.
	 * 
	 * @param revision the revision
	 * @param codingSchemeName the coding scheme name
	 * @param codingSchemeURI the coding scheme uri
	 * @param representsVersion the represents version
	 * @param properties the properties
	 * 
	 * @return the coding scheme
	 * 
	 * @throws LBException the LB exception
	 */
	public CodingScheme addCodeSystemProperties(RevisionInfo revision, String codingSchemeName, String codingSchemeURI, String representsVersion,
	        Properties properties) throws LBException;
	
	/**
	 * Update code system properties.
	 * 
	 * @param revision the revision
	 * @param codingSchemeName the coding scheme name
	 * @param codingSchemeURI the coding scheme uri
	 * @param representsVersion the represents version
	 * @param properties the properties
	 * 
	 * @return the coding scheme
	 * 
	 * @throws LBException the LB exception
	 */
	public CodingScheme updateCodeSystemProperties(RevisionInfo revision, String codingSchemeName, String codingSchemeURI, String representsVersion,
	        Properties properties) throws LBException;
	
	/**
	 * Removes the code system property.
	 * 
	 * @param revision the revision
	 * @param codingSchemeURI the coding scheme uri
	 * @param representsVersion the represents version
	 * @param propertyId the property id
	 * 
	 * @return the coding scheme
	 * 
	 * @throws LBException the LB exception
	 */
	public CodingScheme removeCodeSystemProperty(RevisionInfo revision, String codingSchemeURI, String representsVersion,
           String propertyId) throws LBException;
	
	/**
	 * Creates the code system change set.
	 * 
	 * @param agent the agent
	 * @param changeInstruction the change instruction
	 * 
	 * @return the revision
	 */
	public Revision createCodeSystemChangeSet(String agent, String changeInstruction);
	
	/**
	 * Commit change set.
	 * 
	 * @param changeSet the change set
	 * 
	 * @return the int
	 */
	public int commitChangeSet(Revision changeSet);
	
	/**
	 * Update code system version status.
	 * 
	 * @param codingSchemeURI the coding scheme uri
	 * @param codeSystemVersion the code system version
	 * @param status the status
	 * @param isActive the is active
	 * @param revision the revision
	 * 
	 * @throws LBException the LB exception
	 */
	public void updateCodeSystemVersionStatus(String codingSchemeURI, String codeSystemVersion, String status, Boolean isActive,
			RevisionInfo revision) throws LBException; 
	
	/**
	 * Creates the code system suppliment.
	 * 
	 * @param parent the parent
	 * @param supplement the supplement
	 * 
	 * @throws InvalidCodeSystemSupplementException the invalid code system supplement exception
	 */
	public void createCodeSystemSuppliment(
			AbsoluteCodingSchemeVersionReference parent, 
			AbsoluteCodingSchemeVersionReference supplement) throws InvalidCodeSystemSupplementException;

	/**
	 * Creates the concept.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codeSystemVersion the code system version
	 * @param conceptCode the concept code
	 * @param namespace the namespace
	 * @param revision the revision
	 * 
	 * @throws LBException the LB exception
	 */
	public void createConcept(
			String codingSchemeUri, 
			String codeSystemVersion, 
			String conceptCode, 
			String namespace, 
			RevisionInfo revision) throws LBException;
	
	/**
	 * Update concept.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codeSystemVersion the code system version
	 * @param entity the entity
	 * @param revision the revision
	 * 
	 * @throws LBException the LB exception
	 */
	public void updateConcept(
			String codingSchemeUri, 
			String codeSystemVersion, 
			Entity entity,
			RevisionInfo revision) throws LBException;
	
	/**
	 * Delete concept.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codeSystemVersion the code system version
	 * @param conceptCode the concept code
	 * @param namespace the namespace
	 * @param revision the revision
	 * 
	 * @throws LBException the LB exception
	 */
	public void deleteConcept(
			String codingSchemeUri, 
			String codeSystemVersion, 
			String conceptCode, 
			String namespace, 
			RevisionInfo revision) throws LBException;
	
	/**
	 * Adds the new concept property.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codeSystemVersion the code system version
	 * @param conceptCode the concept code
	 * @param namespace the namespace
	 * @param property the property
	 * @param revision the revision
	 * 
	 * @throws LBException the LB exception
	 */
	public void addNewConceptProperty(
			String codingSchemeUri, 
			String codeSystemVersion, 
			String conceptCode, 
			String namespace, 
			Property property,
			RevisionInfo revision) throws LBException;
	
	/**
	 * Update concept property.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codeSystemVersion the code system version
	 * @param conceptCode the concept code
	 * @param namespace the namespace
	 * @param property the property
	 * @param revision the revision
	 * 
	 * @throws LBException the LB exception
	 */
	public  void updateConceptProperty(
			String codingSchemeUri, 
			String codeSystemVersion, 
			String conceptCode, 
			String namespace, 
			Property property,
			RevisionInfo revision) throws LBException;
	
	/**
	 * Delete concept property.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codeSystemVersion the code system version
	 * @param conceptCode the concept code
	 * @param namespace the namespace
	 * @param property the property
	 * @param revision the revision
	 * 
	 * @throws LBException the LB exception
	 */
	public void deleteConceptProperty(
			String codingSchemeUri, 
			String codeSystemVersion, 
			String conceptCode, 
			String namespace, 
			Property property,
			RevisionInfo revision) throws LBException;
	
	/**
	 * Update concept status.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codeSystemVersion the code system version
	 * @param conceptCode the concept code
	 * @param namespace the namespace
	 * @param status the status
	 * @param isActive the is active
	 * @param revisionInfo the revision info
	 * 
	 * @throws LBException the LB exception
	 */
	public void updateConceptStatus(
			String codingSchemeUri, 
			String codeSystemVersion, 
			String conceptCode, 
			String namespace,
			String status,
			Boolean isActive,
			RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * Creates the association type.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codeSystemVersion the code system version
	 * @param relationsContainerName the relations container name
	 * @param associationName the association name
	 * @param forwardName the forward name
	 * @param reverseName the reverse name
	 * @param isNavigable the is navigable
	 * @param isTransitive the is transitive
	 * @param revision the revision
	 * 
	 * @throws LBException the LB exception
	 */
	public void createAssociationType(
			String codingSchemeUri, 
			String codeSystemVersion,
			String relationsContainerName,
			String associationName,
			String forwardName,
			String reverseName,
			Boolean isNavigable,
			Boolean isTransitive,
			RevisionInfo revision) throws LBException;
	
	/**
	 * Update association type.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codeSystemVersion the code system version
	 * @param associationEntity the association entity
	 * @param revision the revision
	 * 
	 * @throws LBException the LB exception
	 */
	public void updateAssociationType(
			String codingSchemeUri, 
			String codeSystemVersion, 
			AssociationEntity associationEntity,
			RevisionInfo revision) throws LBException;
}
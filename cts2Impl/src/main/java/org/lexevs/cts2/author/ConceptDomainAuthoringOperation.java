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

import java.net.URI;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.naming.Mappings;
import org.lexevs.cts2.core.update.RevisionInfo;
/**
 * LexEVS CTS 2 Concept Domain Authoring Operations.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface ConceptDomainAuthoringOperation {
	
	/**
	 * Create a code system to hold concept domain entities.
	 * 
	 * @param revision - revision information
	 * @param codeSystemName - concept domain code system name
	 * @param codeSystemURI - concept domain code system URI
	 * @param formalName - concept domain code system formal name
	 * @param defaultLanguage - default language
	 * @param representsVersion - concept domain code system version
	 * @param localNameList - (Optional) list of alternate names used for this code system
	 * @param sourceList - source list
	 * @param copyright - copy right information
	 * @param mappings - list of attributes supported by this code system
	 * @return concept domain code system
	 * @throws LBException
	 */
	public CodingScheme createConceptDomainCodeSystem(RevisionInfo revision, String codeSystemName, String codeSystemURI, String formalName,
            String defaultLanguage, long approxNumConcepts, String representsVersion, List<String> localNameList,
            List<Source> sourceList, Text copyright, Mappings mappings) throws LBException;
	
	/**
	 * Create new concept domain.
	 * 
	 * @param conceptDomainId - concept domain id
	 * @param conceptDomainName - concept domain name
	 * @param namespace - concept domain name space (If not provided, formal name of the code system will be used)
	 * @param revisionInfo - revision information
	 * @param description - description of concept domain
	 * @param status - status of concept domain
	 * @param isActive - is concept domain active
	 * @param properties - concept domain properties
	 * @param codeSystemNameOrURI - concept domain code system name or uri
	 * @param codeSystemVersion - concept domain code system version
	 * @return concept domain id if created successfully
	 * @throws LBException
	 */
	public String createConceptDomain(
			String conceptDomainId,
			String conceptDomainName, 
			String namespace,
			RevisionInfo revisionInfo, 
			String description, 
			String status,
			boolean isActive,
			Properties properties, 
			String codeSystemNameOrURI,
			String codeSystemVersion) throws LBException;
	
	/**
	 * Update concept domain status.
	 * 
	 * @param conceptDomainId - concept domain id
	 * @param namespace - concept domain name space (If not provided, formal name of the code system will be used)
	 * @param newStatus - new status for concept domain
	 * @param codeSystemNameOrURI - concept domain code system name or uri
	 * @param codeSystemVersion - concept domain code system version
	 * @param revisionInfo - concept domain code system version
	 * @return true; if update was success
	 * @throws LBException
	 */
	public boolean updateConceptDomainStatus(String conceptDomainId, String namespace, String newStatus, String codeSystemNameOrURI, 
			String codeSystemVersion, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * Activate concept domain.
	 * 
	 * @param conceptDomainId - concept domain id
	 * @param namespace - concept domain name space (If not provided, formal name of the code system will be used)
	 * @param codeSystemNameOrURI - concept domain code system name or uri
	 * @param codeSystemVersion - concept domain code system version
	 * @param revisionInfo - revision information
	 * @return  true; if activation was success
	 * @throws LBException
	 */
	public boolean activateConceptDomain(String conceptDomainId, String namespace, String codeSystemNameOrURI, 
			String codeSystemVersion, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * DeActivate concept domain.
	 * 
	 * @param conceptDomainId - concept domain id
	 * @param namespace - concept domain name space (If not provided, formal name of the code system will be used)
	 * @param codeSystemNameOrURI - concept domain code system name or uri
	 * @param codeSystemVersion - concept domain code system version
	 * @param revisionInfo - revision information
	 * @return  true; if deactivation was success
	 * @throws LBException
	 */
	public boolean deactivateConceptDomain(String conceptDomainId, String namespace, String codeSystemNameOrURI,
			String codeSystemVersion, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * Update concept domain versionable attributes like effective date, expiration date, owner, status etc.
	 * 
	 * @param conceptDomainId - concept domain id
	 * @param namespace - concept domain name space (If not provided, formal name of the code system will be used)
	 * @param changedVersionable - versionable (like:owner, effectiveDate, expirationDate, status etc) changes
	 * @param codeSystemNameOrURI - concept domain code system name or uri
	 * @param codeSystemVersion - concept domain code system version
	 * @param revisionInfo - revision information
	 * @return  true; if update was success
	 * @throws LBException
	 */
	public boolean updateConceptDomainVersionable(String conceptDomainId, String namespace,
			Versionable changedVersionable, String codeSystemNameOrURI,
			String codeSystemVersion, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * Add new property for a concept domain.
	 * 
	 * @param conceptDomainId - concept domain id
	 * @param namespace - concept domain name space (If not provided, formal name of the code system will be used)
	 * @param newProperty - new concept domain property
	 * @param codeSystemNameOrURI - concept domain code system name or uri
	 * @param codeSystemVersion - concept domain code system version
	 * @param revisionInfo - revision information
	 * @return  true; if addition of new property was success
	 * @throws LBException
	 */
	public boolean addConceptDomainProperty(String conceptDomainId, String namespace, Property newProperty, String codeSystemNameOrURI,
			String codeSystemVersion, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * Update existing property of a concept domain.
	 *  
	 * @param conceptDomainId - concept domain id
	 * @param namespace - concept domain name space (If not provided, formal name of the code system will be used)
	 * @param changedProperty - modified concept domain property
	 * @param codeSystemNameOrURI - concept domain code system name or uri
	 * @param codeSystemVersion - concept domain code system version
	 * @param revisionInfo - revision information
	 * @return  true; if update was success
	 * @throws LBException
	 */
	public boolean updateConceptDomainProperty(String conceptDomainId, String namespace, Property changedProperty, String codeSystemNameOrURI,
			String codeSystemVersion, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * Remove existing property of a concept domain.
	 * 
	 * @param conceptDomainId - concept domain id
	 * @param namespace - concept domain name space (If not provided, formal name of the code system will be used)
	 * @param property - property to be removed
	 * @param codeSystemNameOrURI - concept domain code system name or uri
	 * @param codeSystemVersion - concept domain code system version
	 * @param revisionInfo - revision information
	 * @return  true; if remove was success
	 * @throws LBException
	 */
	public boolean removeConceptDomainProperty(String conceptDomainId, String namespace, Property property, String codeSystemNameOrURI,
			String codeSystemVersion, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * Add concept domain to value set binding.
	 * 
	 * @param conceptDomainId - concept domain id
	 * @param namespace - concept domain name space (If not provided, formal name of the code system will be used)
	 * @param codeSystemNameOrURI - concept domain code system name or uri
	 * @param codeSystemVersion - concept domain code system version
	 * @param valueSetURIS
	 * @param revisionInfo - revision information
	 * @return  true; if binding update was success
	 * @throws LBException
	 */
	public boolean addConceptDomainToValueSetBinding(String conceptDomainId, String namespace, String codeSystemNameOrURI,
			String codeSystemVersion, List<URI> valueSetURIS, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * Remove concept domain to value set binding.
	 * 
	 * @param conceptDomainId - concept domain id
	 * @param namespace - concept domain name space (If not provided, formal name of the code system will be used)
	 * @param codeSystemNameOrURI - concept domain code system name or uri
	 * @param codeSystemVersion - concept domain code system version
	 * @param valueSetURIS
	 * @param revisionInfo - revision information
	 * @return  true; if binding update was success
	 * @throws LBException
	 */
	public boolean removeConceptDomainToValueSetBinding(String conceptDomainId, String namespace, String codeSystemNameOrURI,
			String codeSystemVersion, List<URI> valueSetURIS, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * Remove concept domain.
	 * 
	 * @param conceptDomainId - concept domain id
	 * @param namespace - concept domain name space (If not provided, formal name of the code system will be used)
	 * @param @param codeSystemNameOrURI - concept domain code system name or uri
	 * @param codeSystemVersion - concept domain code system version
	 * @param revisionInfo - revision information
	 * @return  true; if remove was success
	 * @throws LBException
	 */
	public boolean removeConceptDomain(String conceptDomainId, String namespace, String codeSystemNameOrURI,
			String codeSystemVersion, RevisionInfo revisionInfo) throws LBException;
}

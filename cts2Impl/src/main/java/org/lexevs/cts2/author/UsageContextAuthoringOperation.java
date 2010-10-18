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
 * LexEVS CTS 2 Usage Context Authoring Operations.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface UsageContextAuthoringOperation {
	
	/**
	 * Create a code system to hold usage context entities.
	 * 
	 * @param revision - revision information
	 * @param codeSystemName - usage context code system name
	 * @param codeSystemURI - usage context code system URI
	 * @param formalName - usage context code system formal name
	 * @param defaultLanguage - default language
	 * @param representsVersion - usage context code system version
	 * @param localNameList - (Optional) list of alternate names used for this code system
	 * @param sourceList - source list
	 * @param copyright - copy right information
	 * @param mappings - list of attributes supported by this code system
	 * @return usage context code system
	 * @throws LBException
	 */
	public CodingScheme createUsageContextCodeSystem(RevisionInfo revision, String codeSystemName, String codeSystemURI, String formalName,
            String defaultLanguage, long approxNumConcepts, String representsVersion, List<String> localNameList,
            List<Source> sourceList, Text copyright, Mappings mappings) throws LBException;
	
	/**
	 * Create new usage context.
	 * 
	 * @param usageContextId - usage context id
	 * @param usageContextName - usage context name
	 * @param namespace - usage context name space (If not provided, formal name of the code system will be used)
	 * @param revisionInfo - revision information
	 * @param description - description of usage context
	 * @param status - status of usage context
	 * @param isActive - is usage context active
	 * @param properties - usage context properties
	 * @param codeSystemNameOrURI - usage context code system name or uri
	 * @param codeSystemVersion - usage context code system version
	 * @return usage context id if created successfully
	 * @throws LBException
	 */
	public String createUsageContext(
			String usageContextId,
			String usageContextName, 
			String namespace,
			RevisionInfo revisionInfo, 
			String description, 
			String status,
			boolean isActive,
			Properties properties, 
			String codeSystemNameOrURI,
			String codeSystemVersion) throws LBException;
	
	/**
	 * Update usage context status.
	 * 
	 * @param usageContextId - usage context id
	 * @param namespace - usage context name space
	 * @param newStatus - new status for concept domain
	 * @param codeSystemNameOrURI - usage context code system name or URI
	 * @param codeSystemVersion - usage context code system version
	 * @param revisionInfo - revision information
	 * @return true, if update was success
	 * @throws LBException
	 */
	public boolean updateUsageContextStatus(String usageContextId, String namespace, String newStatus, 
			String codeSystemNameOrURI, String codeSystemVersion, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * Activate usage context.
	 * 
	 * @param usageContextId - usage context id
	 * @param namespace - usage context name space
	 * @param codeSystemNameOrURI - usage context code system name or URI
	 * @param codeSystemVersion - usage context code system version
	 * @param revisionInfo - revision information
	 * @return true, if update was success
	 * @throws LBException
	 */
	public boolean activateUsageContext(String usageContextId, String namespace, String codeSystemNameOrURI, 
			String codeSystemVersion, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * DeActivate usage context.
	 * 
	 * @param usageContextId - usage context id
	 * @param namespace - usage context name space
	 * @param codeSystemNameOrURI - usage context code system name or URI
	 * @param codeSystemVersion - usage context code system version
	 * @param revisionInfo - revision information
	 * @return true, if update was success
	 * @throws LBException
	 */
	public boolean deactivateUsageContext(String usageContextId, String namespace, String codeSystemNameOrURI,
			String codeSystemVersion, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * Update usage context versionable attributes like effective date, expiration date, owner, status etc.
	 * 
	 * @param usageContextId - usage context id
	 * @param namespace - usage context name space
	 * @param changedVersionable - modified usage context versionable attributes
	 * @param codeSystemNameOrURI - usage context code system name or URI
	 * @param codeSystemVersion - usage context code system version
	 * @param revisionInfo - revision information
	 * @return true, if update was success
	 * @throws LBException
	 */
	public boolean updateUsageContextVersionable(String usageContextId, String namespace, Versionable changedVersionable, 
			String codeSystemNameOrURI, String codeSystemVersion, RevisionInfo revision) throws LBException;
	
	/**
	 * Add new property for a usage context.
	 * 
	 * @param usageContextId - usage context id
	 * @param namespace - usage context name space
	 * @param newProperty - new usage context property
	 * @param codeSystemNameOrURI - usage context code system name or URI
	 * @param codeSystemVersion - usage context code system version
	 * @param revisionInfo - revision information
	 * @return true, if update was success
	 * @throws LBException
	 */
	public boolean addUsageContextProperty(String usageContextId, String namespace, Property newProperty, 
			String codeSystemNameOrURI, String codeSystemVersion, RevisionInfo revision) throws LBException;
	
	/**
	 * Update existing property of a usage context.
	 *  
	 * @param usageContextId - usage context id
	 * @param namespace - usage context name space
	 * @param changedProperty - modified usage context property
	 * @param codeSystemNameOrURI - usage context code system name or URI
	 * @param codeSystemVersion - usage context code system version
	 * @param revisionInfo - revision information
	 * @return true, if update was success
	 * @throws LBException
	 */
	public boolean updateUsageContextProperty(String usageContextId, String namespace, Property changedProperty, 
			String codeSystemNameOrURI, String codeSystemVersion, RevisionInfo revision) throws LBException;
	
	/**
	 * Remove existing property of a usage context.
	 * 
	 * @param usageContextId - usage context id
	 * @param namespace - usage context name space
	 * @param property - usage context property to be removed
	 * @param codeSystemNameOrURI - usage context code system name or URI
	 * @param codeSystemVersion - usage context code system version
	 * @param revisionInfo - revision information
	 * @return true, if update was success
	 * @throws LBException
	 */
	public boolean removeUsageContextProperty(String usageContextId, String namespace, Property property, 
			String codeSystemNameOrURI, String codeSystemVersion, RevisionInfo revision) throws LBException;
	
	/**
	 * Remove existing usage context.
	 * 
	 * @param usageContextId - usage context id
	 * @param namespace - usage context name space
	 * @param codeSystemNameOrURI - usage context code system name or URI
	 * @param codeSystemVersion - usage context code system version
	 * @param revisionInfo - revision information
	 * @return true, if update was success
	 * @throws LBException
	 */
	public boolean removeUsageContext(String usageContextId, String namespace, 
			String codeSystemNameOrURI, String codeSystemVersion, RevisionInfo revision) throws LBException;
}
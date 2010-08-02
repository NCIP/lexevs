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

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Versionable;
import org.lexevs.cts2.core.update.RevisionInfo;
/**
 * LexEVS CTS 2 Concept Domain Authoring Operations.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface ConceptDomainAuthoringOperation {
	
	/**
	 * Create new concept domain.
	 * 
	 * @param conceptDomainId
	 * @param conceptDomainName
	 * @param revisionInfo
	 * @param description
	 * @param status
	 * @param isActive
	 * @param properties
	 * @param versionOrTag
	 * @return
	 * @throws LBException
	 */
	public String createConceptDomain(
			String conceptDomainId,
			String conceptDomainName, 
			RevisionInfo revisionInfo, 
			String description, 
			String status,
			boolean isActive,
			Properties properties, 
			CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Update concept domain statsus.
	 * 
	 * @param conceptDomainId
	 * @param newStatus
	 * @param versionOrTag
	 * @param revisionInfo
	 * @return
	 * @throws LBException
	 */
	public boolean updateConceptDomainStatus(String conceptDomainId,
			String newStatus, CodingSchemeVersionOrTag versionOrTag, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * Activate concept domain.
	 * 
	 * @param conceptDomainId
	 * @param versionOrTag
	 * @param revisionInfo
	 * @return
	 * @throws LBException
	 */
	public boolean activateConceptDomain(String conceptDomainId,
			CodingSchemeVersionOrTag versionOrTag, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * DeActivate concept domain.
	 * 
	 * @param conceptDomainId
	 * @param versionOrTag
	 * @param revisionInfo
	 * @return
	 * @throws LBException
	 */
	public boolean deactivateConceptDomain(String conceptDomainId,
			CodingSchemeVersionOrTag versionOrTag, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * Update concept domain versionable attributes like effective date, expiration date, owner, status etc.
	 * 
	 * @param conceptDomainId
	 * @param changedVersionable
	 * @param versionOrTag
	 * @param revision
	 * @return
	 * @throws LBException
	 */
	public boolean updateConceptDomainVersionable(String conceptDomainId,
			Versionable changedVersionable, CodingSchemeVersionOrTag versionOrTag, RevisionInfo revision) throws LBException;
	
	/**
	 * Add new property for a concept domain.
	 * 
	 * @param conceptDomainId
	 * @param newProperty
	 * @param versionOrTag
	 * @param revision
	 * @return
	 * @throws LBException
	 */
	public boolean addConceptDomainProperty(String conceptDomainId, Property newProperty, CodingSchemeVersionOrTag versionOrTag, 
			RevisionInfo revision) throws LBException;
	
	/**
	 * Update existing property of a concept domain.
	 *  
	 * @param conceptDomainId
	 * @param changedProperty
	 * @param versionOrTag
	 * @param revision
	 * @return
	 * @throws LBException
	 */
	public boolean updateConceptDomainProperty(String conceptDomainId, Property changedProperty, CodingSchemeVersionOrTag versionOrTag, 
			RevisionInfo revision) throws LBException;
	
	/**
	 * Remove existing property of a concept domain.
	 * 
	 * @param conceptDomainId
	 * @param propertyId
	 * @param versionOrTag
	 * @param revision
	 * @return
	 * @throws LBException
	 */
	public boolean removeConceptDomainProperty(String conceptDomainId, String propertyId, CodingSchemeVersionOrTag versionOrTag, 
			RevisionInfo revision) throws LBException;
	
	/**
	 * Add concept domain to value set binding.
	 * 
	 * @param conceptDomainId
	 * @param valueSetURIS
	 * @param revisionInfo
	 * @return
	 * @throws LBException
	 */
	public boolean addConceptDomainToValueSetBinding(String conceptDomainId, List<URI> valueSetURIS, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * Remove concept domain to value set binding.
	 * 
	 * @param conceptDomainId
	 * @param valueSetURIS
	 * @param revisionInfo
	 * @return
	 * @throws LBException
	 */
	public boolean removeConceptDomainToValueSetBinding(String conceptDomainId, List<URI> valueSetURIS, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * Remove concept domain.
	 * 
	 * @param conceptDomainId
	 * @param versionOrTag
	 * @param revision
	 * @return
	 * @throws LBException
	 */
	public boolean removeConceptDomain(String conceptDomainId, CodingSchemeVersionOrTag versionOrTag, 
			RevisionInfo revision) throws LBException;
}

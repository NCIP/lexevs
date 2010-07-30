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

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Versionable;
import org.lexevs.cts2.core.update.RevisionInfo;

/**
 * LexEVS CTS 2 Usage Context Authoring Operations.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface UsageContextAuthoringOperation {
	/**
	 * Create new usage context.
	 * 
	 * @param usageContextId
	 * @param usageContextName
	 * @param revisionInfo
	 * @param description
	 * @param status
	 * @param isActive
	 * @param properties
	 * @param versionOrTag
	 * @return
	 * @throws LBException
	 */
	public String createUsageContext(
			String usageContextId,
			String usageContextName, 
			RevisionInfo revisionInfo, 
			String description, 
			String status,
			boolean isActive,
			Properties properties, 
			CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Update usage context statsus.
	 * 
	 * @param usageContextId
	 * @param newStatus
	 * @param versionOrTag
	 * @param revisionInfo
	 * @return
	 * @throws LBException
	 */
	public boolean updateUsageContextStatus(String usageContextId,
			String newStatus, CodingSchemeVersionOrTag versionOrTag, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * Activate usage context.
	 * 
	 * @param usageContextId
	 * @param versionOrTag
	 * @param revisionInfo
	 * @return
	 * @throws LBException
	 */
	public boolean activateUsageContext(String usageContextId,
			CodingSchemeVersionOrTag versionOrTag, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * DeActivate usage context.
	 * 
	 * @param usageContextId
	 * @param versionOrTag
	 * @param revisionInfo
	 * @return
	 * @throws LBException
	 */
	public boolean deactivateUsageContext(String usageContextId,
			CodingSchemeVersionOrTag versionOrTag, RevisionInfo revisionInfo) throws LBException;
	
	/**
	 * Update usage context versionable attributes like effective date, expiration date, owner, status etc.
	 * 
	 * @param usageContextId
	 * @param changedVersionable
	 * @param versionOrTag
	 * @param revision
	 * @return
	 * @throws LBException
	 */
	public boolean updateUsageContextVersionable(String usageContextId,
			Versionable changedVersionable, CodingSchemeVersionOrTag versionOrTag, RevisionInfo revision) throws LBException;
	
	/**
	 * Add new property for a usage context.
	 * 
	 * @param usageContextId
	 * @param newProperty
	 * @param versionOrTag
	 * @param revision
	 * @return
	 * @throws LBException
	 */
	public boolean addUsageContextProperty(String usageContextId, Property newProperty, CodingSchemeVersionOrTag versionOrTag, 
			RevisionInfo revision) throws LBException;
	
	/**
	 * Update existing property of a usage context.
	 *  
	 * @param usageContextId
	 * @param changedProperty
	 * @param versionOrTag
	 * @param revision
	 * @return
	 * @throws LBException
	 */
	public boolean updateUsageContextProperty(String usageContextId, Property changedProperty, CodingSchemeVersionOrTag versionOrTag, 
			RevisionInfo revision) throws LBException;
	
	/**
	 * Remove existing property of a usage context.
	 * 
	 * @param usageContextId
	 * @param propertyId
	 * @param versionOrTag
	 * @param revision
	 * @return
	 * @throws LBException
	 */
	public boolean removeUsageContextProperty(String usageContextId, String propertyId, CodingSchemeVersionOrTag versionOrTag, 
			RevisionInfo revision) throws LBException;	
}

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
package org.lexevs.dao.database.service.version;

import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;

/**
 * The Interface VersionService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface AuthoringService {

	/**
	 * Load system release.
	 * 
	 * @param systemRelease the system release
	 * @throws LBRevisionException 
	 */
	public void loadSystemRelease(SystemRelease systemRelease, Boolean indexNewCodingScheme) throws LBRevisionException;
	
	/**
	 * Revise.
	 * 
	 * @param revision the revision
	 * @param systemReleaseURI
	 * @throws LBRevisionException 
	 */
	public void loadRevision(Revision revision, String systemReleaseURI, Boolean indexNewCodingScheme) throws LBRevisionException;
	
	public void loadRevision(Versionable versionable, String releaseURI, Boolean indexNewCodingScheme) throws LBRevisionException;
	
	/**
	 * insert system release entry.
	 * 
	 * @param systemRelease
	 */
	public String insertSystemReleaseMetadata(SystemRelease systemRelease);
	
	/**
	 * get system release entry for a given uri.
	 * @param systemReleaseUri
	 * @return
	 */
	public SystemRelease getSystemReleaseMetadataByUri(String systemReleaseUri);
	
	/**
	 * get system release entry for a given unique id.
	 * @param systemReleaseId
	 * @return
	 */
	public SystemRelease getSystemReleaseMetadataById(String systemReleaseId);
}


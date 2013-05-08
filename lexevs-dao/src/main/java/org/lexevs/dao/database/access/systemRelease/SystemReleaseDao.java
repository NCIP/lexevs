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
package org.lexevs.dao.database.access.systemRelease;

import java.util.List;

import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

/**
 * The Interface SystemReleaseDao.
 * 
 * @author <a href="mailto:rao.ramachandra@mayo.edu">Ramachandra J S Rao</a>
 */
public interface SystemReleaseDao extends LexGridSchemaVersionAwareDao {

	/**
	 * insert system release entry.
	 * 
	 * @param systemRelease
	 */
	public String insertSystemReleaseEntry(SystemRelease systemRelease);
	
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
	
	/**
	 * get all system release entries.
	 * @return
	 */
	public List<SystemRelease> getAllSystemRelease();

	/**
	 * get system release id by uri.
	 * @param systemReleaseUri
	 * @return
	 */
	public String getSystemReleaseUIdByUri(String systemReleaseUri);
}
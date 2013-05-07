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
package org.lexevs.dao.database.access.revision;

import java.sql.Timestamp;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.versions.Revision;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

/**
 * The Interface RevisionDao.
 * 
 * @author <a href="mailto:rao.ramachandra@mayo.edu">Ramachandra J S Rao</a>
 */
public interface RevisionDao extends LexGridSchemaVersionAwareDao {

	/**
	 * insert system release entry.
	 * 
	 * @param revision
	 * @param systemReleaseGuid
	 * @throws LBRevisionException 
	 */
	public String insertRevisionEntry(Revision revision, String systemReleaseGuid) throws LBRevisionException;

	/**
	 * get revision entry for a given uri.
	 * 
	 * @param revisionUri
	 * @return
	 */
	public Revision getRevisionByUri(String revisionUri);

	/**
	 * get revision entry for a given guid.
	 * 
	 * @param revisionGuid
	 * @return
	 */
	public Revision getRevisionByGuid(String revisionGuid);

	/**
	 * get all system release entries.
	 * 
	 * @return
	 */
	public List<Revision> getAllRevisions();

	/**
	 * get revision guid by uri.
	 * 
	 * @param revisionUri
	 * @return
	 */
	public String getRevisionUIdById(String revisionId);

	public String getNewRevisionId();
	
	public String getRevisionIdForDate(Timestamp dateTime);

	/**
	 * remove revision record from the revision table if not used by any entry.
	 * 
	 * @param revisionId 
	 * @return true; if successful
	 * @throws LBException
	 */
	public boolean removeRevisionById(String revisionId) throws LBException;
}
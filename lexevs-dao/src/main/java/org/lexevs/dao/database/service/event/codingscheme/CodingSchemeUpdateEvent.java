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
package org.lexevs.dao.database.service.event.codingscheme;

import org.LexGrid.codingSchemes.CodingScheme;

/**
 * The Class CodingSchemeUpdateEvent.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodingSchemeUpdateEvent {

	/** The revision id. */
	private String revisionId;
	
	/** The entry state id. */
	private String entryStateId;
	
	/** The original coding scheme. */
	private CodingScheme originalCodingScheme;
	
	/** The updated coding scheme. */
	private CodingScheme updatedCodingScheme;
	
	/**
	 * Instantiates a new coding scheme update event.
	 * 
	 * @param revisionId the revision id
	 * @param entryStateId the entry state id
	 * @param originalCodingScheme the original coding scheme
	 * @param updatedCodingScheme the updated coding scheme
	 */
	public CodingSchemeUpdateEvent(String revisionId, String entryStateId,
			CodingScheme originalCodingScheme, CodingScheme updatedCodingScheme) {
		this.revisionId = revisionId;
		this.entryStateId = entryStateId;
		this.originalCodingScheme = originalCodingScheme;
		this.updatedCodingScheme = updatedCodingScheme;
	}

	/**
	 * Gets the revision id.
	 * 
	 * @return the revision id
	 */
	public String getRevisionId() {
		return revisionId;
	}

	/**
	 * Sets the revision id.
	 * 
	 * @param revisionId the new revision id
	 */
	public void setRevisionId(String revisionId) {
		this.revisionId = revisionId;
	}

	/**
	 * Gets the original coding scheme.
	 * 
	 * @return the original coding scheme
	 */
	public CodingScheme getOriginalCodingScheme() {
		return originalCodingScheme;
	}

	/**
	 * Sets the original coding scheme.
	 * 
	 * @param originalCodingScheme the new original coding scheme
	 */
	public void setOriginalCodingScheme(CodingScheme originalCodingScheme) {
		this.originalCodingScheme = originalCodingScheme;
	}

	/**
	 * Gets the updated coding scheme.
	 * 
	 * @return the updated coding scheme
	 */
	public CodingScheme getUpdatedCodingScheme() {
		return updatedCodingScheme;
	}

	/**
	 * Sets the updated coding scheme.
	 * 
	 * @param updatedCodingScheme the new updated coding scheme
	 */
	public void setUpdatedCodingScheme(CodingScheme updatedCodingScheme) {
		this.updatedCodingScheme = updatedCodingScheme;
	}

	/**
	 * Sets the entry state id.
	 * 
	 * @param entryStateId the new entry state id
	 */
	public void setEntryStateId(String entryStateId) {
		this.entryStateId = entryStateId;
	}

	/**
	 * Gets the entry state id.
	 * 
	 * @return the entry state id
	 */
	public String getEntryStateId() {
		return entryStateId;
	}
}

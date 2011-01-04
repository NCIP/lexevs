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
package org.lexevs.dao.database.service.event.association;

import java.util.List;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;

/**
 * The Class AssociationBatchInsertEvent.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AssociationBatchInsertEvent {

	/** The coding scheme uri. */
	private String codingSchemeUri;
	
	/** The version. */
	private String version;
	
	/** The relation. */
	private Relations relation;
	
	/** The source. */
	private AssociationSource source;
	
	/** The sources. */
	private List<? extends AssociationSource> sources;

	/**
	 * Instantiates a new association batch insert event.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relations the relations
	 * @param sources the sources
	 */
	public AssociationBatchInsertEvent(String codingSchemeUri, String version,
			Relations relations, List<? extends AssociationSource> sources) {
		super();
		this.codingSchemeUri = codingSchemeUri;
		this.version = version;
		this.relation = relations;
		this.sources = sources;
	}

	/**
	 * Gets the coding scheme uri.
	 * 
	 * @return the coding scheme uri
	 */
	public String getCodingSchemeUri() {
		return codingSchemeUri;
	}

	/**
	 * Sets the coding scheme uri.
	 * 
	 * @param codingSchemeUri the new coding scheme uri
	 */
	public void setCodingSchemeUri(String codingSchemeUri) {
		this.codingSchemeUri = codingSchemeUri;
	}

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 * 
	 * @param version the new version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Gets the relation.
	 * 
	 * @return the relation
	 */
	public Relations getRelation() {
		return relation;
	}

	/**
	 * Sets the relation.
	 * 
	 * @param relation the new relation
	 */
	public void setRelation(Relations relation) {
		this.relation = relation;
	}

	/**
	 * Gets the sources.
	 * 
	 * @return the sources
	 */
	public List<? extends AssociationSource> getSources() {
		return sources;
	}

	/**
	 * Sets the sources.
	 * 
	 * @param sources the new sources
	 */
	public void setSources(List<? extends AssociationSource> sources) {
		this.sources = sources;
	}

	/**
	 * Gets the source.
	 * 
	 * @return the source
	 */
	public AssociationSource getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 * 
	 * @param source the new source
	 */
	public void setSource(AssociationSource source) {
		this.source = source;
	}
}
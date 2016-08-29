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
package org.lexevs.dao.index.access.entity;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.access.LexEvsIndexFormatVersionAwareDao;

import java.util.List;
import java.util.Set;

/**
 * The Interface EntityDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface EntityDao extends LexEvsIndexFormatVersionAwareDao {
	
	public String getIndexName(String codingSchemeUri, String version);

	public void deleteDocuments(String codingSchemeUri, String version, Term term);
	
	public void deleteDocuments(String codingSchemeUri, String version, Query query);

	public void addDocuments(String codingSchemeUri, String version, List<Document> documents, Analyzer analyzer);
	
	public Document getDocumentById(String codingSchemeUri, String version, int id);
	
	public Document getDocumentById(String codingSchemeUri, String version,
			int id, Set<String> field);
	
	public List<ScoreDoc> query(String codingSchemeUri, String version, Query query);

}
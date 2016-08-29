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
package org.lexevs.dao.index.access.search;

import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.access.LexEvsIndexFormatVersionAwareDao;

/**
 * The Interface SearchDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface SearchDao extends LexEvsIndexFormatVersionAwareDao {
		
	
	public String getIndexName(String codingSchemeUri, String version);

	public void deleteDocuments(String codingSchemeUri, String version, Query query);

	public void addDocuments(String codingSchemeUri, String version, List<Document> documents, Analyzer analyzer);
	
	public List<ScoreDoc> query(Query query);
	
	public Filter getCodingSchemeFilter(String uri, String version);

	public Document getById(int id);
	
	public Document getById(Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude, int id);

	public List<ScoreDoc> query(Query query,
			Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude);
}
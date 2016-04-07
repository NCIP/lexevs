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
package org.lexevs.dao.index.service.search;

import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.concepts.Entity;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

/**
 * The Interface EntityIndexService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface SearchIndexService {

	public void updateIndexForEntity(String codingSchemeUri, String codingSchemeVersion, Entity entity);
	
	public void addEntityToIndex(String codingSchemeUri, String codingSchemeVersion, Entity entity);

//	public void optimize();

	public void deleteEntityFromIndex(
			String codingSchemeUri,
			String codingSchemeVersion, 
			Entity entity);

	public void dropIndex(AbsoluteCodingSchemeVersionReference reference);
	
	public boolean doesIndexExist(AbsoluteCodingSchemeVersionReference reference);
	
	public List<ScoreDoc> query(
		Set<AbsoluteCodingSchemeVersionReference> codeSystemToInclude, 
		Query query);
	
	public Document getById(int id);
	
	public Analyzer getAnalyzer();

	public void createIndex(AbsoluteCodingSchemeVersionReference ref);

	public Document getById(Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude,
			int doc);

}
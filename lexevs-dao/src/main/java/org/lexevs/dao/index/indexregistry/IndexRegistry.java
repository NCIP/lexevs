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
package org.lexevs.dao.index.indexregistry;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.apache.lucene.search.Filter;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;

import java.util.List;
import java.util.Map;

public interface IndexRegistry {

	public void registerCodingSchemeIndex(String codingSchemeUri, String version, String indexName);
	
	public void unRegisterCodingSchemeIndex(String codingSchemeUri, String version);

	public LuceneIndexTemplate getLuceneIndexTemplate(
			String codingSchemeUri, String version);
	
	public LuceneIndexTemplate getCommonLuceneIndexTemplate();
	
	public LuceneIndexTemplate getCommonLuceneIndexTemplate(List<AbsoluteCodingSchemeVersionReference> codingSchemes);
	
	public LuceneIndexTemplate getSearchLuceneIndexTemplate();
	
	public void destroyIndex(String indexName);
	
	public Map<String, Filter> getCodingSchemeFilterMap();

	public Map<String, Filter> getBoundaryDocFilterMap();
}
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
package org.lexevs.dao.index.lucene.v2009.entity;

import java.util.List;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.access.entity.EntityDao;
import org.lexevs.dao.index.indexregistry.IndexRegistry;
import org.lexevs.dao.index.lucene.v2010.entity.LuceneEntityDao;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

/**
 * The Class LuceneEntity2009Dao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LuceneEntity2009Dao extends LuceneEntityDao implements EntityDao {
	
	private IndexRegistry indexRegistry;
	
	/** The supported index version2009. */
	public static LexEvsIndexFormatVersion supportedIndexVersion2009 = LexEvsIndexFormatVersion.parseStringToVersion("2009");

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.lucene.v2010.entity.LuceneEntityDao#getMatchAllDocsQuery(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	@Override
	public Query getMatchAllDocsQuery(
			String uri, String version) {
		return new MatchAllDocsQuery();
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.lucene.v2010.entity.LuceneEntityDao#doGetSupportedLexEvsIndexFormatVersions()
	 */
	@Override
	public List<LexEvsIndexFormatVersion> doGetSupportedLexEvsIndexFormatVersions() {
		return DaoUtility.createList(LexEvsIndexFormatVersion.class, supportedIndexVersion2009);
	}
	
	@Override
	protected Filter getCodingSchemeFilterForCodingScheme(
			String codingSchemeUri, String codingSchemeVersion) {
		return new QueryWrapperFilter(new MatchAllDocsQuery());
	}

	@Override
	protected LuceneIndexTemplate getLuceneIndexTemplate(
			String codingSchemeUri, String version) {
		return indexRegistry.getLuceneIndexTemplate(codingSchemeUri, version);
	}

	public void setIndexRegistry(IndexRegistry indexRegistry) {
		this.indexRegistry = indexRegistry;
	}

	public IndexRegistry getIndexRegistry() {
		return indexRegistry;
	}
}

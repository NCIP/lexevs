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
package org.lexevs.dao.index.lucene;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.apache.commons.collections.CollectionUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.TermsFilter;
import org.compass.core.lucene.support.ChainedFilter;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.access.AbstractBaseIndexDao;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.index.indexregistry.IndexRegistry;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;

public abstract class AbstractBaseLuceneIndexTemplateDao extends AbstractBaseIndexDao {
	
	private IndexRegistry indexRegistry;
	
	protected Filter getBoundaryDocFilterForCodingScheme(List<AbsoluteCodingSchemeVersionReference> codingSchemes) {
		if(CollectionUtils.isEmpty(codingSchemes)) {
			return null;
		}
		
		String key = getFilterMapKey(codingSchemes);
		if(!indexRegistry.getBoundaryDocFilterMap().containsKey(key)) {
			Filter[] filters = new Filter[codingSchemes.size()];
			for(int i=0;i<codingSchemes.size();i++) {
				AbsoluteCodingSchemeVersionReference ref = codingSchemes.get(i);
				
				filters[i] = this.getBoundaryDocFilterForCodingScheme(
						ref.getCodingSchemeURN(), 
						ref.getCodingSchemeVersion());
			}
			
			Filter chainedFilter = new ChainedFilter(filters, ChainedFilter.OR);
			indexRegistry.getBoundaryDocFilterMap().put(key, new CachingWrapperFilter(chainedFilter));
		}
		return indexRegistry.getBoundaryDocFilterMap().get(key);
	}
	
	protected Filter getBoundaryDocFilterForCodingScheme(String codingSchemeUri, String codingSchemeVersion) {
		String key = getFilterMapKey(codingSchemeUri, codingSchemeUri);
		if(!indexRegistry.getBoundaryDocFilterMap().containsKey(key)) {
			Filter filter = createBoundaryDocFilter();
			indexRegistry.getBoundaryDocFilterMap().put(key, new CachingWrapperFilter(filter));
		}
		return new ChainedFilter(
				new Filter[] {indexRegistry.getBoundaryDocFilterMap().get(key), 
				this.getCodingSchemeFilterForCodingScheme(codingSchemeUri, codingSchemeVersion)}, 
				ChainedFilter.AND);
	}
	
	protected Filter createBoundaryDocFilter() {
		TermsFilter filter = new TermsFilter();
		filter.addTerm(new Term("codeBoundry", "T"));
		
		return filter;
	}
	
	protected Filter getCodingSchemeFilterForCodingScheme(List<AbsoluteCodingSchemeVersionReference> codingSchemes) {
		String key = getFilterMapKey(codingSchemes);
		if(!indexRegistry.getCodingSchemeFilterMap().containsKey(key)) {
			Filter[] filters = new Filter[codingSchemes.size()];
			for(int i=0;i<codingSchemes.size();i++) {
				AbsoluteCodingSchemeVersionReference ref = codingSchemes.get(i);
				
				filters[i] = this.getCodingSchemeFilterForCodingScheme(
						ref.getCodingSchemeURN(), 
						ref.getCodingSchemeVersion());
			}
			
			Filter chainedFilter = new ChainedFilter(filters, ChainedFilter.OR);
			indexRegistry.getCodingSchemeFilterMap().put(key, new CachingWrapperFilter(chainedFilter));
		}
		return indexRegistry.getCodingSchemeFilterMap().get(key);
	}
	
	protected Filter getCodingSchemeFilterForCodingScheme(String codingSchemeUri, String codingSchemeVersion) {
		String key = getFilterMapKey(codingSchemeUri, codingSchemeVersion);
		if(!this.indexRegistry.getCodingSchemeFilterMap().containsKey(key)) {
			Term term = new Term(
					LuceneLoaderCode.CODING_SCHEME_URI_VERSION_KEY_FIELD,
					LuceneLoaderCode.createCodingSchemeUriVersionKey(
							codingSchemeUri, codingSchemeVersion));
			TermsFilter filter = new TermsFilter();
			filter.addTerm(term);
			indexRegistry.getCodingSchemeFilterMap().put(key, new CachingWrapperFilter(filter));
		}
		return indexRegistry.getCodingSchemeFilterMap().get(key);
	}
	
	private String getFilterMapKey(List<AbsoluteCodingSchemeVersionReference> refs) {
		return DaoUtility.createKey(refs);
	}
	
	private String getFilterMapKey(String codingSchemeUri, String codingSchemeVersion) {
		return DaoUtility.createKey(codingSchemeUri, codingSchemeVersion);
	}

	protected abstract LuceneIndexTemplate getLuceneIndexTemplate(String codingSchemeUri, String version);

	public void setIndexRegistry(IndexRegistry indexRegistry) {
		this.indexRegistry = indexRegistry;
	}

	public IndexRegistry getIndexRegistry() {
		return indexRegistry;
	}
}
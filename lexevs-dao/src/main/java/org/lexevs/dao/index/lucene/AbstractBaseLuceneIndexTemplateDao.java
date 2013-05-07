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
import org.springframework.util.Assert;

public abstract class AbstractBaseLuceneIndexTemplateDao extends AbstractFilteringLuceneIndexTemplateDao {

	protected Filter getBoundaryDocFilterForCodingScheme(List<AbsoluteCodingSchemeVersionReference> codingSchemes) {
		if(CollectionUtils.isEmpty(codingSchemes)) {
			return null;
		}
		
		String key = getFilterMapKey(codingSchemes);
		if(!this.getIndexRegistry().getBoundaryDocFilterMap().containsKey(key)) {
			Filter[] filters = new Filter[codingSchemes.size()];
			for(int i=0;i<codingSchemes.size();i++) {
				AbsoluteCodingSchemeVersionReference ref = codingSchemes.get(i);
				
				filters[i] = this.getBoundaryDocFilterForCodingScheme(
						ref.getCodingSchemeURN(), 
						ref.getCodingSchemeVersion());
			}
			
			Filter chainedFilter = new CachingChainedFilter(filters, ChainedFilter.OR);
			this.getIndexRegistry().getBoundaryDocFilterMap().put(key, chainedFilter);
		}
		return this.getIndexRegistry().getBoundaryDocFilterMap().get(key);
	}
	
	protected Filter getBoundaryDocFilterForCodingScheme(String codingSchemeUri, String codingSchemeVersion) {
		
		String key = getFilterMapKey(codingSchemeUri, codingSchemeUri);
		if(!this.getIndexRegistry().getBoundaryDocFilterMap().containsKey(key)) {
			Filter filter1 = createBoundaryDocFilter();
			
			this.getIndexRegistry().getBoundaryDocFilterMap().put(key, new CachingWrapperFilter(filter1));
		}
		
		Filter returnFilter = this.getIndexRegistry().getBoundaryDocFilterMap().get(key);
		
		Assert.notNull(returnFilter);

		return returnFilter;
	}
	
	protected Filter createBoundaryDocFilter() {
		TermsFilter filter = new TermsFilter();
		filter.addTerm(new Term("codeBoundry", "T"));
		
		return filter;
	}
	
}
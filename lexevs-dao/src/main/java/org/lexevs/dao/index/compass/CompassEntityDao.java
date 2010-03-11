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
package org.lexevs.dao.index.compass;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.DuplicateFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.compass.core.CompassCallback;
import org.compass.core.CompassException;
import org.compass.core.CompassHit;
import org.compass.core.CompassQuery;
import org.compass.core.CompassSession;
import org.compass.core.lucene.util.LuceneHelper;
import org.compass.core.support.search.CompassSearchCommand;
import org.compass.core.support.search.CompassSearchHelper;
import org.compass.core.support.search.CompassSearchResults;
import org.compass.spring.CompassDaoSupport;
import org.lexevs.dao.index.access.entity.EntityDao;
import org.lexevs.dao.index.model.compass.v20.IndexedProperty;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

/**
 * The Class CompassEntityDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CompassEntityDao extends CompassDaoSupport implements EntityDao {

	/** The compass search helper. */
	private CompassSearchHelper compassSearchHelper;
	
	/**
	 * Gets the resource by id.
	 * 
	 * @param id the id
	 * 
	 * @return the resource by id
	 */
	public IndexedProperty getResourceById(String id) {
		
		
		return ((IndexedProperty)

			this.getCompassTemplate().get(
					IndexedProperty.ALIAS, id));
	}
	
	/**
	 * Insert resource.
	 * 
	 * @param resources the resources
	 */
	public void insertResource(List<IndexedProperty> resources) {
		for(IndexedProperty prop : resources){
			insertResource(prop);
		}
	}

	/**
	 * Insert resource.
	 * 
	 * @param resource the resource
	 */
	public void insertResource(IndexedProperty resource) {
		this.getCompassTemplate().save(resource);
	}
	
	/**
	 * Delete resource.
	 * 
	 * @param resource the resource
	 */
	public void deleteResource(IndexedProperty resource) {
		this.getCompassTemplate().delete(resource);
	}

	/**
	 * Query.
	 * 
	 * @param combinedQueries the combined queries
	 * @param individualQueries the individual queries
	 * 
	 * @return the list< indexed property>
	 */
	public List<IndexedProperty> query(List<CompassQuery> combinedQueries,
			List<CompassQuery> individualQueries) {
		// TODO Auto-generated method stub
		return null;
	}
//0.5639428
	/**
 * Query.
 * 
 * @param query the query
 * 
 * @return the list< indexed property>
 */
public List<IndexedProperty> query(final CompassQuery query) {
		CompassSearchResults results = this.getCompassTemplate().execute(new CompassCallback<CompassSearchResults>(){

			public CompassSearchResults doInCompass(CompassSession session)
					throws CompassException {
				Filter filter = new CachingWrapperFilter(new DuplicateFilter("entityCode", DuplicateFilter.KM_USE_FIRST_OCCURRENCE, DuplicateFilter.PM_FAST_INVALIDATION));
				
				query.setFilter(LuceneHelper.createCompassQueryFilter(session, filter));
				
				CompassSearchCommand searchCommand = new CompassSearchCommand(query.attach(session));
			
				return compassSearchHelper.search(searchCommand);
			}		
		});
		return unwrapCompassHits(results.getHits());
		
	}

	/**
	 * Query.
	 * 
	 * @param query the query
	 * @param page the page
	 * 
	 * @return the list< indexed property>
	 */
	public List<IndexedProperty> query(CompassQuery query, int page) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Query.
	 * 
	 * @param combinedQueries the combined queries
	 * @param individualQueries the individual queries
	 * 
	 * @return the list< indexed property>
	 */
	public List<IndexedProperty> query(CompassQuery combinedQueries,
			List<CompassQuery> individualQueries) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * Query.
	 * 
	 * @param query the query
	 * 
	 * @return the list< indexed property>
	 */
	public List<IndexedProperty> query(String query) {
		return this.unwrapCompassHits(
				this.getCompassTemplate().findWithDetach(query).getHits());
	}

	/**
	 * Query.
	 * 
	 * @param query the query
	 * @param page the page
	 * 
	 * @return the list< indexed property>
	 */
	public List<IndexedProperty> query(String query, int page) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Delete resource by id.
	 * 
	 * @param id the id
	 */
	public void deleteResourceById(String id) {
		//this.getCompassTemplate().
		
	}
	
	/**
	 * Unwrap compass hits.
	 * 
	 * @param hits the hits
	 * 
	 * @return the list< indexed property>
	 */
	protected List<IndexedProperty> unwrapCompassHits(CompassHit[] hits){
		List<IndexedProperty> returnList = new ArrayList<IndexedProperty>();
		for(CompassHit hit : hits){
			System.out.println(hit);
			IndexedProperty prop = (IndexedProperty)hit.getData();
			prop.setScore(hit.getScore());
			returnList.add(prop);
		}
		return returnList;
	}

	/**
	 * Gets the compass search helper.
	 * 
	 * @return the compass search helper
	 */
	public CompassSearchHelper getCompassSearchHelper() {
		return compassSearchHelper;
	}

	/**
	 * Sets the compass search helper.
	 * 
	 * @param compassSearchHelper the new compass search helper
	 */
	public void setCompassSearchHelper(CompassSearchHelper compassSearchHelper) {
		this.compassSearchHelper = compassSearchHelper;
	}

	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.access.LexEvsIndexFormatVersionAwareDao#supportsLexEvsIndexFormatVersion(org.lexevs.dao.index.version.LexEvsIndexFormatVersion)
	 */
	public boolean supportsLexEvsIndexFormatVersion(
			LexEvsIndexFormatVersion version) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.access.entity.EntityDao#query(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, java.util.List, java.util.List)
	 */
	public List<ScoreDoc> query(AbsoluteCodingSchemeVersionReference reference,
			List<? extends Query> combinedQueries, List<? extends Query> individualQueries) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.access.entity.EntityDao#getDocumentById(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, int)
	 */
	public Document getDocumentById(
			AbsoluteCodingSchemeVersionReference reference, int documentId) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.access.entity.EntityDao#deleteDocumentsOfCodingScheme(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	public void deleteDocumentsOfCodingScheme(
			AbsoluteCodingSchemeVersionReference reference) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.access.entity.EntityDao#getMatchAllDocsQuery(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	@Override
	public Query getMatchAllDocsQuery(
			AbsoluteCodingSchemeVersionReference reference) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}
}

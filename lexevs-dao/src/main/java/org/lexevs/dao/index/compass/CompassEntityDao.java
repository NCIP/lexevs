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

public class CompassEntityDao extends CompassDaoSupport implements EntityDao {

	private CompassSearchHelper compassSearchHelper;
	
	public IndexedProperty getResourceById(String id) {
		
		
		return ((IndexedProperty)

			this.getCompassTemplate().get(
					IndexedProperty.ALIAS, id));
	}
	
	public void insertResource(List<IndexedProperty> resources) {
		for(IndexedProperty prop : resources){
			insertResource(prop);
		}
	}

	public void insertResource(IndexedProperty resource) {
		this.getCompassTemplate().save(resource);
	}
	
	public void deleteResource(IndexedProperty resource) {
		this.getCompassTemplate().delete(resource);
	}

	public List<IndexedProperty> query(List<CompassQuery> combinedQueries,
			List<CompassQuery> individualQueries) {
		// TODO Auto-generated method stub
		return null;
	}
//0.5639428
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

	public List<IndexedProperty> query(CompassQuery query, int page) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<IndexedProperty> query(CompassQuery combinedQueries,
			List<CompassQuery> individualQueries) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<IndexedProperty> query(String query) {
		return this.unwrapCompassHits(
				this.getCompassTemplate().findWithDetach(query).getHits());
	}

	public List<IndexedProperty> query(String query, int page) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void deleteResourceById(String id) {
		//this.getCompassTemplate().
		
	}
	
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

	public CompassSearchHelper getCompassSearchHelper() {
		return compassSearchHelper;
	}

	public void setCompassSearchHelper(CompassSearchHelper compassSearchHelper) {
		this.compassSearchHelper = compassSearchHelper;
	}

	

	public boolean supportsLexEvsIndexFormatVersion(
			LexEvsIndexFormatVersion version) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	public List<ScoreDoc> query(AbsoluteCodingSchemeVersionReference reference,
			List<? extends Query> combinedQueries, List<? extends Query> individualQueries) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	public Document getDocumentById(
			AbsoluteCodingSchemeVersionReference reference, int documentId) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	public void deleteDocumentsOfCodingScheme(
			AbsoluteCodingSchemeVersionReference reference) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}
}

package org.lexevs.dao.index.service.search;

import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.indexer.IndexCreator;
import org.lexevs.dao.index.indexer.IndexCreator.IndexOption;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;

public class SourceAssertedValueSetSearchIndexService implements SearchIndexService {

	private IndexCreator indexCreator;
	private ConcurrentMetaData concurrentMetaData;

	@Override
	public void updateIndexForEntity(String codingSchemeUri, String codingSchemeVersion, Entity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addEntityToIndex(String codingSchemeUri, String codingSchemeVersion, Entity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteEntityFromIndex(String codingSchemeUri, String codingSchemeVersion, Entity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropIndex(AbsoluteCodingSchemeVersionReference reference) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean doesIndexExist(AbsoluteCodingSchemeVersionReference reference) {
//		String key = this.getCodingSchemeKey(reference);
		try {
			return StringUtils.isNotBlank(concurrentMetaData.getIndexMetaDataValue("AssertedValueSetIndex"));
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ScoreDoc> query(Set<AbsoluteCodingSchemeVersionReference> codeSystemToInclude, Query query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Analyzer getAnalyzer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createIndex(AbsoluteCodingSchemeVersionReference ref) {
		indexCreator.index(ref, IndexOption.SEARCH);

	}

	@Override
	public Document getById(Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude, int doc) {
		// TODO Auto-generated method stub
		return null;
	}

	public IndexCreator getIndexCreator() {
		return indexCreator;
	}

	public void setIndexCreator(IndexCreator indexCreator) {
		this.indexCreator = indexCreator;
	}

	public ConcurrentMetaData getConcurrentMetaData() {
		return concurrentMetaData;
	}

	public void setConcurrentMetaData(ConcurrentMetaData concurrentMetaData) {
		this.concurrentMetaData = concurrentMetaData;
	}

}

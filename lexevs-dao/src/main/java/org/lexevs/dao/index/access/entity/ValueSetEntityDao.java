package org.lexevs.dao.index.access.entity;

import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.lucene.AbstractBaseLuceneIndexTemplateDao;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

public class ValueSetEntityDao extends AbstractBaseLuceneIndexTemplateDao implements EntityDao {

	/** The supported index version2010. */
	public static LexEvsIndexFormatVersion supportedIndexVersion2010 = LexEvsIndexFormatVersion.parseStringToVersion("2010");
	private LuceneIndexTemplate luceneIndexTemplate;
	

	@Override
	public String getIndexName(String codingSchemeUri, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteDocuments(String codingSchemeUri, String version, Term term) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteDocuments(String codingSchemeUri, String version, Query query) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addDocuments(String codingSchemeUri, String version, List<Document> documents, Analyzer analyzer) {
		getLuceneIndexTemplate().addDocuments(documents, analyzer);
	}

	@Override
	public Document getDocumentById(String codingSchemeUri, String version, int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document getDocumentById(String codingSchemeUri, String version, int id, Set<String> field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ScoreDoc> query(String codingSchemeUri, String version, Query query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LexEvsIndexFormatVersion> doGetSupportedLexEvsIndexFormatVersions() {
		return DaoUtility.createList(
				LexEvsIndexFormatVersion.class, 
				supportedIndexVersion2010);
	}

		public LuceneIndexTemplate getLuceneIndexTemplate() {
			return luceneIndexTemplate;
		}

		public void setLuceneIndexTemplate(LuceneIndexTemplate luceneIndexTemplate) {
			this.luceneIndexTemplate = luceneIndexTemplate;
		}

}

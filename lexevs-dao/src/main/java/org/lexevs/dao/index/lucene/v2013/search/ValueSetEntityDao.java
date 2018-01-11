package org.lexevs.dao.index.lucene.v2013.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.LeafCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Scorer;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.access.search.SearchDao;
import org.lexevs.dao.index.lucene.AbstractBaseLuceneIndexTemplateDao;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;
import org.lexevs.logging.LoggerFactory;

public class ValueSetEntityDao extends AbstractBaseLuceneIndexTemplateDao implements SearchDao {

	/** The supported index version2013. */
	public static LexEvsIndexFormatVersion supportedIndexVersion2013 = LexEvsIndexFormatVersion
			.parseStringToVersion("2013");

	private static LgLoggerIF logger = LoggerFactory.getLogger();
	private LuceneIndexTemplate luceneIndexTemplate;

	@Override
	public String getIndexName(String codingSchemeUri, String version) {
		return luceneIndexTemplate.getIndexName();
	}

	@Override
	public void deleteDocuments(String codingSchemeUri, String version, Query query) {
		luceneIndexTemplate.removeDocuments(query);
	}

	@Override
	public void addDocuments(String codingSchemeUri, String version, List<Document> documents, Analyzer analyzer) {
		getLuceneIndexTemplate().addDocuments(documents, analyzer);
	}

	@Override
	public List<LexEvsIndexFormatVersion> doGetSupportedLexEvsIndexFormatVersions() {
		return DaoUtility.createList(LexEvsIndexFormatVersion.class, supportedIndexVersion2013);
	}

	public LuceneIndexTemplate getLuceneIndexTemplate() {
		return luceneIndexTemplate;
	}

	public void setLuceneIndexTemplate(LuceneIndexTemplate luceneIndexTemplate) {
		this.luceneIndexTemplate = luceneIndexTemplate;
	}

	@Override
	public List<ScoreDoc> query(Query query) {
		try {
			LuceneIndexTemplate template = this.getLuceneIndexTemplate();

			int maxDoc = template.getMaxDoc();

			if (maxDoc == 0) {
				String errorMsg = "Index does not exist: " + template.getIndexName();
				logger.error(errorMsg);
				throw new RuntimeException(errorMsg);
			}

			final List<ScoreDoc> scoreDocs = new ArrayList<ScoreDoc>();

			template.search(query, null, new Collector() {

				@Override
				public LeafCollector getLeafCollector(final LeafReaderContext context) throws IOException {
					return new LeafCollector() {

						private Scorer scorer;

						@Override
						public void collect(int docId) throws IOException {
							scoreDocs.add(new ScoreDoc(context.docBase + docId, this.scorer.score()));
						}

						@Override
						public void setScorer(Scorer scorer) throws IOException {
							this.scorer = scorer;
						}

					};
				}

				@Override
				public boolean needsScores() {
					return true;
				}

			});

			Collections.sort(scoreDocs, new Comparator<ScoreDoc>() {

				@Override
				public int compare(ScoreDoc o1, ScoreDoc o2) {
					if (o1.score > o2.score)
						return -1;
					if (o1.score < o2.score)
						return 1;
					return 0;
				}

			});
			return scoreDocs;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Filter getCodingSchemeFilter(String uri, String version) {
		System.out.println("Filtering by code system does not apply to source asserted value sets");
		return null;
	}

	@Override
	public Document getById(int id) {
		return this.getLuceneIndexTemplate().getDocumentById(id);
	}

	@Override
	public Document getById(Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude, int id) {
		throw new UnsupportedOperationException(
				"No CodingScheme References can be used to get lucene documents for source asserted value sets");
	}

	@Override
	public List<ScoreDoc> query(Query query, Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude) {
		throw new UnsupportedOperationException(
				"No CodingScheme References can be used to get document ids for source asserted value sets");
	}

}

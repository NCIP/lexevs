package org.lexevs.dao.index.lucene.v2013.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.join.QueryBitSetProducer;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.core.annotation.Order;

public class ValueSetDaoTest {
	ValueSetEntityDao vsdao;

	@Before
	public void setUp() throws Exception {
		vsdao = LexEvsServiceLocator.getInstance().getIndexDaoManager()
				.getValueSetEntityDao("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
	}

	@Test
	@Order(0)
	public void test() {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(new TermQuery(new Term("code", "C37927")), Occur.MUST);
		builder.add(new TermQuery(new Term("isParentDoc", "true")), Occur.MUST_NOT);
		Query query = builder.build();
		QueryBitSetProducer parentFilter;
		try {
			parentFilter = new QueryBitSetProducer(
					new QueryParser("isParentDoc", new StandardAnalyzer(new CharArraySet(0, true))).parse("true"));
		} catch (ParseException e) {
			throw new RuntimeException("Query Parser Failed against parent query: ", e);
		}
		ToParentBlockJoinQuery blockJoinQuery = new ToParentBlockJoinQuery(query, parentFilter, ScoreMode.Total);
		List<ScoreDoc> docs = vsdao.query(blockJoinQuery);
		assertNotNull(docs);
		ScoreDoc doc = docs.get(0);
		assertNotNull(docs);
		Document document = vsdao.getById(doc.doc);
		assertNotNull(document);
		assertTrue(document.getFields().stream().anyMatch(x -> x.name().equals("entityCode")));
		assertTrue(document.getFields().stream().filter(x -> x.name().equals("entityCode"))
				.anyMatch(y -> y.stringValue().equals("C37927")));
	}

	@Test
	@Order(1)
	public void testGetIndexName() {
		String name = vsdao.getIndexName(null, null);
		assertNotNull(name);
		assertEquals(name, "AssertedValueSetIndex");
	}

	@Test
	@Order(2)
	public void testDeleteEntityFromIndex() {
		Term term = new Term(LuceneLoaderCode.CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD,
				LuceneLoaderCode.createCodingSchemeUriVersionCodeNamespaceKey(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5", "C37927", "owl2lexevs"));
		vsdao.deleteDocuments("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5", new TermQuery(term));

		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(new TermQuery(new Term("code", "C37927")), Occur.MUST);
		builder.add(new TermQuery(new Term("isParentDoc", "true")), Occur.MUST_NOT);
		Query query = builder.build();
		QueryBitSetProducer parentFilter;
		try {
			parentFilter = new QueryBitSetProducer(
					new QueryParser("isParentDoc", new StandardAnalyzer(new CharArraySet(0, true))).parse("true"));
		} catch (ParseException e) {
			throw new RuntimeException("Query Parser Failed against parent query: ", e);
		}
		ToParentBlockJoinQuery blockJoinQuery = new ToParentBlockJoinQuery(query, parentFilter, ScoreMode.Total);
		List<ScoreDoc> docs = vsdao.query(blockJoinQuery);
		assertTrue(docs.size() == 0);
	}
}

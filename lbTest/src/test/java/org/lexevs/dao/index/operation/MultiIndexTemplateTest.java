package org.lexevs.dao.index.operation;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.join.QueryBitSetProducer;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.index.lucenesupport.DefaultLuceneDirectoryCreator;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.lexevs.dao.index.lucenesupport.MultiBaseLuceneIndexTemplate;
import org.lexevs.locator.LexEvsServiceLocator;

public class MultiIndexTemplateTest extends LexBIGServiceTestCase {
	
	private final String testID = "MultiIndexTest";
	private MultiBaseLuceneIndexTemplate multiTemplate;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}


	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void testSingleSchemeQuery() {
		//Verifies simple search against a single code system
		List<NamedDirectory> namedDirectories = new ArrayList<NamedDirectory>();		
		NamedDirectory directory = new DefaultLuceneDirectoryCreator().getDirectory("Automobiles-1_0", 
				new File(LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables().getAutoLoadIndexLocation()));
		namedDirectories.add(directory);
		multiTemplate = new MultiBaseLuceneIndexTemplate(namedDirectories );
		assertTrue(multiTemplate.getMaxDoc() == 63);
		TopScoreDocCollector collector = TopScoreDocCollector.create(multiTemplate.getMaxDoc());
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		TermQuery termQuery = new TermQuery(new Term("code","A0001"));
		builder.add(termQuery, Occur.MUST);
		multiTemplate.search(builder.build() , null, collector);
		assertTrue(collector.getTotalHits() > 0);
		ScoreDoc[] docs = collector.topDocs().scoreDocs;
		Document doc = multiTemplate.getDocumentById(docs[0].doc);
		assertTrue(doc.get("code").equals("A0001"));
	}
	
	public void testSingleSchemeQuerySpecialParenthesesQuery() {
		//Verifies simple search against a single code system
		List<NamedDirectory> namedDirectories = new ArrayList<NamedDirectory>();		
		NamedDirectory directory = new DefaultLuceneDirectoryCreator().getDirectory("Automobiles-1_0", 
				new File(LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables().getAutoLoadIndexLocation()));
		namedDirectories.add(directory);
		multiTemplate = new MultiBaseLuceneIndexTemplate(namedDirectories );
		assertTrue(multiTemplate.getMaxDoc() == 63);
		TopScoreDocCollector collector = TopScoreDocCollector.create(multiTemplate.getMaxDoc());
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		TermQuery termQuery = new TermQuery(new Term("code","C0011(5564)"));
		builder.add(termQuery, Occur.MUST);
		multiTemplate.search(builder.build() , null, collector);
		assertTrue(collector.getTotalHits() > 0);
		ScoreDoc[] docs = collector.topDocs().scoreDocs;
		Document doc = multiTemplate.getDocumentById(docs[0].doc);
		assertTrue(doc.get("code").equals("C0011(5564)"));
	}
	
	@Test
	public void testTwoSchemeQuery() {
		//Verifies simple search against two code systems
		List<NamedDirectory> namedDirectories = new ArrayList<NamedDirectory>();		
		NamedDirectory directory = new DefaultLuceneDirectoryCreator().getDirectory("Automobiles-1_0", 
				new File(LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables().getAutoLoadIndexLocation()));	
		NamedDirectory directory2 = new DefaultLuceneDirectoryCreator().getDirectory("GermanMadeParts-2_0", 
				new File(LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables().getAutoLoadIndexLocation()));
		namedDirectories.add(directory);
		namedDirectories.add(directory2);
		multiTemplate = new MultiBaseLuceneIndexTemplate(namedDirectories );
		assertTrue(multiTemplate.getMaxDoc() == 77);
		TopScoreDocCollector collector = TopScoreDocCollector.create(multiTemplate.getMaxDoc());
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		TermQuery termQuery = new TermQuery(new Term("code","A0001"));
		builder.add(termQuery, Occur.MUST);
		multiTemplate.search(builder.build() , null, collector);
		assertTrue(collector.getTotalHits() > 0);
		ScoreDoc[] docs = collector.topDocs().scoreDocs;
		Document doc = multiTemplate.getDocumentById(docs[0].doc);
		assertTrue(doc.get("code").equals("A0001"));
	}
	
	@Test
	public void testTwoSchemeQueryBlockJoin() throws ParseException {
		//Verifies to parent block join search against two code systems
		List<NamedDirectory> namedDirectories = new ArrayList<NamedDirectory>();		
		NamedDirectory directory = new DefaultLuceneDirectoryCreator().getDirectory("Automobiles-1_0", 
				new File(LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables().getAutoLoadIndexLocation()));	
		NamedDirectory directory2 = new DefaultLuceneDirectoryCreator().getDirectory("GermanMadeParts-2_0", 
				new File(LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables().getAutoLoadIndexLocation()));
		namedDirectories.add(directory);
		namedDirectories.add(directory2);
		multiTemplate = new MultiBaseLuceneIndexTemplate(namedDirectories );
		assertTrue(multiTemplate.getMaxDoc() == 77);
		TopScoreDocCollector collector = TopScoreDocCollector.create(multiTemplate.getMaxDoc());
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		QueryBitSetProducer parentFilter = 
				new QueryBitSetProducer(new QueryParser("isParentDoc", 
						new StandardAnalyzer(new CharArraySet( 0, true))).parse("true"));
		TermQuery termQuery = new TermQuery(new Term("code","A0001"));
        ToParentBlockJoinQuery termJoinQuery = new ToParentBlockJoinQuery(
                termQuery,
                parentFilter,
                ScoreMode.Total);

		builder.add(termJoinQuery, Occur.MUST);
		multiTemplate.search(builder.build() , null, collector);
		assertTrue(collector.getTotalHits() > 0);
		ScoreDoc[] docs = collector.topDocs().scoreDocs;
		Document doc = multiTemplate.getDocumentById(docs[0].doc);
		assertTrue(doc.get("entityCode").equals("A0001"));
	}

	@Test
	public void testTwoSchemeQueryPlusAnonBlockJoin() throws ParseException{
		//code:C0011\(5564\)
		//Verifies to parent block join search against two code systems
		List<NamedDirectory> namedDirectories = new ArrayList<NamedDirectory>();		
		NamedDirectory directory = new DefaultLuceneDirectoryCreator().getDirectory("Automobiles-1_0", 
				new File(LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables().getAutoLoadIndexLocation()));	
		NamedDirectory directory2 = new DefaultLuceneDirectoryCreator().getDirectory("GermanMadeParts-2_0", 
				new File(LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables().getAutoLoadIndexLocation()));
		namedDirectories.add(directory);
		namedDirectories.add(directory2);
		multiTemplate = new MultiBaseLuceneIndexTemplate(namedDirectories );
		assertTrue(multiTemplate.getMaxDoc() == 77);
		TopScoreDocCollector collector = TopScoreDocCollector.create(multiTemplate.getMaxDoc());
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		QueryBitSetProducer parentFilter = 
				new QueryBitSetProducer(new QueryParser("isParentDoc", 
						new StandardAnalyzer(new CharArraySet( 0, true))).parse("true"));
		TermQuery termQuery = new TermQuery(new Term("code","A0001"));
		TermQuery anonTermNo = new TermQuery(new Term("isAnonymous", "T"));
		builder.add(termQuery, Occur.MUST);
		builder.add(anonTermNo, Occur.MUST_NOT);
        ToParentBlockJoinQuery termJoinQuery = new ToParentBlockJoinQuery(
                builder.build(),
                parentFilter,
                ScoreMode.Total);

		builder.add(termJoinQuery, Occur.MUST);
		multiTemplate.search(termJoinQuery , null, collector);
		assertTrue(collector.getTotalHits() > 0);
		ScoreDoc[] docs = collector.topDocs().scoreDocs;
		Document doc = multiTemplate.getDocumentById(docs[0].doc);
		assertTrue(doc.get("entityCode").equals("A0001"));
	}
	
	@Override
	protected String getTestID() {
		return testID;
	}

}

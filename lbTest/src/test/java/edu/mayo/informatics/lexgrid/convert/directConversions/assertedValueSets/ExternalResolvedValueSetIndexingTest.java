package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension.MatchAlgorithm;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search.SourceAssertedValueSetSearchExtensionImpl;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.concepts.Entity;
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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.dao.index.service.search.SourceAssertedValueSetSearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.resolvedvalueset.impl.ExternalResolvedValueSetIndexService;

public class ExternalResolvedValueSetIndexingTest {
	static ExternalResolvedValueSetIndexService service;
	static SourceAssertedValueSetSearchIndexService vsSvc;
	private static LexBIGServiceImpl lbsvc;
	private static SourceAssertedValueSetSearchExtensionImpl assertedVSsvc;
	
	@BeforeClass
	public static void setUp() throws Exception {
		vsSvc = LexEvsServiceLocator.getInstance().getIndexServiceManager().getAssertedValueSetIndexService();
		service = new ExternalResolvedValueSetIndexService();
		service.indexExternalResolvedValueSetsToAssertedValueSetIndex();
		lbsvc = LexBIGServiceImpl.defaultInstance();
		assertedVSsvc = (SourceAssertedValueSetSearchExtensionImpl) lbsvc
				.getGenericExtension("AssertedValueSetSearchExtension");
	}

	@Test
	public void testGetExternalResolvedValueSets() {
		List<AbsoluteCodingSchemeVersionReference> refs = 
				service.getExternalResolvedValueSetCodingSchemes();
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertEquals(refs.size(), 3);
		assertTrue(refs.stream().anyMatch(ref -> 
		ref.getCodingSchemeURN().equals("SRITEST:AUTO:AllDomesticButGM")));
		assertTrue(refs.stream().anyMatch(ref -> 
		ref.getCodingSchemeURN().equals("SRITEST:AUTO:AllDomesticButGMWithlt250charName")));
	}
	
	@Test
	public void testGetEntitiesForResolvedVSReferences() throws URISyntaxException {
	List<Entity> entities = service.getEntitiesForExternalResolvedValueSet("SRITEST:AUTO:AllDomesticButGM", "12.03test");
	assertNotNull(entities);
	assertTrue(entities.size() > 0);
	assertEquals(entities.size(), 6);
	assertTrue(entities.stream().anyMatch(x -> x.getEntityCode().equals("005")));
	}
	
	@Test
	public void testSearchExternalRVSets() throws ParseException {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(new TermQuery(new Term("isParentDoc", "true")), Occur.MUST_NOT);
		builder.add(new TermQuery(new Term("code", "005")), Occur.MUST);
		Query query = builder.build();
		QueryBitSetProducer parentFilter;
		parentFilter = new QueryBitSetProducer(
					new QueryParser("isParentDoc", new StandardAnalyzer(new CharArraySet(0, true))).parse("true"));
		ToParentBlockJoinQuery blockJoinQuery = new ToParentBlockJoinQuery(query, parentFilter, ScoreMode.Total);

		List<ScoreDoc> docs = vsSvc.query(null, blockJoinQuery);
		assertNotNull(docs);
		assertTrue(docs.size() > 0);
		ScoreDoc sd = docs.get(0);
		Document doc = vsSvc.getById(sd.doc);
		assertNotNull(doc);
		assertTrue(doc.getFields().stream().anyMatch(x -> x.name().equals("entityCode")));
		assertTrue(doc.getFields().stream().filter(x -> x.name().equals("entityCode"))
				.anyMatch(y -> y.stringValue().equals("005")));
	}
	
	@Test
	public void testExternalCodeExact() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("005", null, null, MatchAlgorithm.CODE_EXACT,
				false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "Domestic Auto Makers");
	}
	
	@Test
	public void testPresentationExact() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("Domestic Auto Makers", null, null,
				MatchAlgorithm.PRESENTATION_EXACT, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "Domestic Auto Makers");
	}
	
	@Test
	public void testPresentationContains() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("Auto", null, null,
				MatchAlgorithm.PRESENTATION_CONTAINS, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "Domestic Auto Makers");
	}
	
	@Test
	public void testPropertyExact() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("American Car Companies", null, null,
				MatchAlgorithm.PROPERTY_EXACT, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "Domestic Auto Makers");
	}
	
	@Test
	public void testPropertyContains() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("Car", null, null,
				MatchAlgorithm.PRESENTATION_CONTAINS, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "Domestic Auto Makers");
	}
	
	@Test
	public void testLuceneQuery() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("F150", null, null,
				MatchAlgorithm.LUCENE, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "Ford F150");
	}
	
	
	@Test
	public void testLuceneQueryManyResults() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("FORD", null, null,
				MatchAlgorithm.LUCENE, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReferenceList refs = itr.next(-1);
		Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(ref -> ref.getEntityDescription().getContent().equals("Ford Motor Company"));
		Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(ref -> ref.getEntityDescription().getContent().equals("Ford F150"));
	}
	
	@AfterClass
	public static void dropIndex() {
		vsSvc.dropIndex(Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5"));
	}
	

}

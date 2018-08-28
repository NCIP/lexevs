package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension.MatchAlgorithm;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.apache.lucene.analysis.Analyzer;
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
import org.lexgrid.valuesets.sourceasserted.SourceAssertedValueSetService;
import org.lexgrid.valuesets.sourceasserted.impl.SourceAssertedValueSetServiceImpl;
import org.springframework.core.annotation.Order;

public class SourceAssertedValueSetSearchIndexServiceTest {
	static SourceAssertedValueSetSearchIndexService service;
	static SourceAssertedValueSetService svc;
	String codingSchemeName = "phonyPhoneNumbers";
	String codingSchemeURI = AssertedValueSetServices.BASE + codingSchemeName;

	@BeforeClass
	public static void setUp() throws Exception {
		service = LexEvsServiceLocator.getInstance().getIndexServiceManager().getAssertedValueSetIndexService();
		AssertedValueSetParameters params = new AssertedValueSetParameters.Builder("0.1.5.1").
				assertedDefaultHierarchyVSRelation("Concept_In_Subset").
				codingSchemeName("owl2lexevs").
				codingSchemeURI("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl").
				rootConcept("C54453")
				.build();
		svc = SourceAssertedValueSetServiceImpl.getDefaultValueSetServiceForVersion(params);
	}


	@Test
	@Order(1)
	public void updateIndexForEntityTest() {
		Entity entity = new Entity();
		entity.setEntityCode("BR549");
		entity.setEntityCodeNamespace("ontology");
		List<String> typeList = new ArrayList<String>();
		typeList.add("concept");
		entity.setEntityType(typeList);
		entity.setEntityDescription(Constructors.createEntityDescription("telephone number"));
		entity.setIsActive(true);
		entity.setIsAnonymous(false);

		Properties props = new Properties();
		Property prop1 = new Property();
		prop1.setPropertyName("textualPresentation");
		prop1.setPropertyType("presentation");
		prop1.setValue(Constructors.createText("I'm a presentation property"));
		props.addProperty(prop1);
		Property prop2 = new Property();
		prop2.setPropertyName("definition");
		prop2.setPropertyType(PropertyType.DEFINITION.name());
		prop2.setValue(Constructors.createText("I'm a definition property"));
		props.addProperty(prop2);
		Property prop3 = new Property();
		prop3.setPropertyName("comment");
		prop3.setPropertyType(PropertyType.COMMENT.name());
		prop3.setValue(Constructors.createText("I'm a comment property"));
		props.addProperty(prop3);
		Property prop4 = new Property();
		prop4.setPropertyName("instruction");
		prop4.setPropertyType(PropertyType.GENERIC.name());
		prop4.setValue(Constructors.createText("I'm an instruction property"));
		props.addProperty(prop4);
		Property prop5 = new Property();
		prop5.setPropertyName("generic");
		prop5.setPropertyType(PropertyType.GENERIC.name());
		prop5.setValue(Constructors.createText("I'm a definition property"));
		props.addProperty(prop5);

		Presentation pres = new Presentation();
		pres.setIsPreferred(true);
		pres.setPropertyName("presentation");
		pres.setPropertyType(PropertyType.PRESENTATION.name());
		pres.setValue(Constructors.createText("I'm a presentation property"));

		Presentation[] vPresentationList = { pres };
		entity.setPresentation(vPresentationList);
		entity.setProperty(props.getProperty());

		Source source1 = new Source();
		source1.setRole("PrimarySource");
		source1.setContent("NCI");
		Source source2 = new Source();
		source2.setRole("SecondarySource");
		source2.setContent("CDISC");

		PropertyQualifier qual1 = new PropertyQualifier();
		qual1.setPropertyQualifierName("modification");
		qual1.setValue(Constructors.createText("mod1"));
		PropertyQualifier qual2 = new PropertyQualifier();
		qual2.setPropertyQualifierName("qualification");
		qual2.setValue(Constructors.createText("qual1"));

		Source[] sources = { source1, source2 };
		PropertyQualifier[] quals = { qual1, qual2 };
		pres.setSource(sources);
		pres.setPropertyQualifier(quals);

		service.updateIndexForEntity(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5.1", codingSchemeURI, codingSchemeName, entity);

		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(new TermQuery(new Term("code", "BR549")), Occur.MUST);
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

		List<ScoreDoc> docs = service.query(null, blockJoinQuery);
		assertNotNull(docs);
		assertTrue(docs.size() > 0);
	}

	@Test
	@Order(2)
	public void addEntityToIndexTest() {
		Entity entity = new Entity();
		entity.setEntityCode("3675309");
		entity.setEntityCodeNamespace("terminology");
		List<String> typeList = new ArrayList<String>();
		typeList.add("concept");
		entity.setEntityType(typeList);
		entity.setEntityDescription(Constructors.createEntityDescription("friends number"));
		entity.setIsActive(true);
		entity.setIsAnonymous(false);

		Properties props = new Properties();
		Property prop1 = new Property();
		prop1.setPropertyName("textualPresentation");
		prop1.setPropertyType("presentation");
		prop1.setValue(Constructors.createText("I'm a pop song"));
		props.addProperty(prop1);
		Property prop2 = new Property();
		prop2.setPropertyName("definition");
		prop2.setPropertyType(PropertyType.DEFINITION.name());
		prop2.setValue(Constructors.createText("this is a test"));
		props.addProperty(prop2);
		Property prop3 = new Property();
		prop3.setPropertyName("comment");
		prop3.setPropertyType(PropertyType.COMMENT.name());
		prop3.setValue(Constructors.createText("say something nice"));
		props.addProperty(prop3);
		Property prop4 = new Property();
		prop4.setPropertyName("instruction");
		prop4.setPropertyType(PropertyType.GENERIC.name());
		prop4.setValue(Constructors.createText("tell what to do"));
		props.addProperty(prop4);
		Property prop5 = new Property();
		prop5.setPropertyName("generic");
		prop5.setPropertyType(PropertyType.GENERIC.name());
		prop5.setValue(Constructors.createText("generic"));
		props.addProperty(prop5);

		Presentation pres = new Presentation();
		pres.setIsPreferred(true);
		pres.setPropertyName("presentation");
		pres.setPropertyType(PropertyType.PRESENTATION.name());
		pres.setValue(Constructors.createText("Truly a presentation"));

		Presentation[] vPresentationList = { pres };
		entity.setPresentation(vPresentationList);
		entity.setProperty(props.getProperty());

		Source source1 = new Source();
		source1.setRole("PrimarySource");
		source1.setContent("NCI");
		Source source2 = new Source();
		source2.setRole("SecondarySource");
		source2.setContent("CDISC");

		PropertyQualifier qual1 = new PropertyQualifier();
		qual1.setPropertyQualifierName("modification");
		qual1.setValue(Constructors.createText("mod1"));
		PropertyQualifier qual2 = new PropertyQualifier();
		qual2.setPropertyQualifierName("qualification");
		qual2.setValue(Constructors.createText("qual1"));

		Source[] sources = { source1, source2 };
		PropertyQualifier[] quals = { qual1, qual2 };
		pres.setSource(sources);
		pres.setPropertyQualifier(quals);

		service.addEntityToIndex(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5.1", 
				codingSchemeURI, codingSchemeName, entity);

		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(new TermQuery(new Term("code", "3675309")), Occur.MUST);
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

		List<ScoreDoc> docs = service.query(null, blockJoinQuery);
		assertNotNull(docs);
		assertTrue(docs.size() > 0);
		ScoreDoc sd = docs.get(0);
		Document doc = service.getById(sd.doc);
		assertNotNull(doc);
		assertTrue(doc.getFields().stream().anyMatch(x -> x.name().equals("entityCode")));
		assertTrue(doc.getFields().stream().filter(x -> x.name().equals("entityCode"))
				.anyMatch(y -> y.stringValue().equals("3675309")));
	}

	@Test
	@Order(3)
	public void deleteEntityFromIndexTest() {
		Entity entity = new Entity();
		entity.setEntityCode("BR549");
		entity.setEntityCodeNamespace("ontology");
		service.deleteEntityFromIndex(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5.1", 
				codingSchemeURI, codingSchemeName, entity);

		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(new TermQuery(new Term("code", "BR549")), Occur.MUST);
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

		List<ScoreDoc> docs = service.query(null, blockJoinQuery);
		assertNotNull(docs);
		assertTrue(docs.size() == 0);
		

		BooleanQuery.Builder remainsbuilder = new BooleanQuery.Builder();
		remainsbuilder.add(new TermQuery(new Term("code", "3675309")), Occur.MUST);
		remainsbuilder.add(new TermQuery(new Term("isParentDoc", "true")), Occur.MUST_NOT);
		Query remainsquery = remainsbuilder.build();
		QueryBitSetProducer remainsparentFilter;
		try {
			remainsparentFilter = new QueryBitSetProducer(
					new QueryParser("isParentDoc", new StandardAnalyzer(new CharArraySet(0, true))).parse("true"));
		} catch (ParseException e) {
			throw new RuntimeException("Query Parser Failed against parent query: ", e);
		}
		ToParentBlockJoinQuery remainsblockJoinQuery = new ToParentBlockJoinQuery(remainsquery, remainsparentFilter, ScoreMode.Total);
		List<ScoreDoc> remainsdocs = service.query(null, remainsblockJoinQuery);
		assertNotNull(remainsdocs);
		assertTrue(remainsdocs.size() > 0);
	}
	
	@Test
	@Order(4)
	public void queryPropertyTest() throws ParseException {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(new TermQuery(new Term("isParentDoc", "true")), Occur.MUST_NOT);
		builder.add(new TermQuery(new Term("code", "C99998")), Occur.MUST);
		builder.add(new TermQuery(new Term("propertyName", "Contributing_Source")), Occur.MUST);
		QueryParser propValueParser = new QueryParser("propertyValue", service.getAnalyzer());
		builder.add(propValueParser.createBooleanQuery("propertyValue", "FDA"), Occur.MUST);
		Query query = builder.build();
		QueryBitSetProducer parentFilter;
		parentFilter = new QueryBitSetProducer(
					new QueryParser("isParentDoc", new StandardAnalyzer(new CharArraySet(0, true))).parse("true"));
		ToParentBlockJoinQuery blockJoinQuery = new ToParentBlockJoinQuery(query, parentFilter, ScoreMode.Total);

		List<ScoreDoc> docs = service.query(null, blockJoinQuery);
		assertNotNull(docs);
		assertTrue(docs.size() > 0);
		ScoreDoc sd = docs.get(0);
		Document doc = service.getById(sd.doc);
		assertNotNull(doc);
		assertTrue(doc.getFields().stream().anyMatch(x -> x.name().equals("entityCode")));
		assertTrue(doc.getFields().stream().filter(x -> x.name().equals("entityCode"))
				.anyMatch(y -> y.stringValue().equals("C99998")));
		assertTrue(doc.getFields().stream().anyMatch(x -> x.name().equals("codingSchemeUri")));
		assertTrue(doc.getFields().stream().filter(x -> x.name().equals("codingSchemeUri"))
				.anyMatch(y -> y.stringValue().equals("http://evs.nci.nih.gov/valueset/FDA/C48323")));
	}
	
	@Test
	@Order(5)
	public void queryPropertyFromTwoValueSetsTest() throws ParseException {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(new TermQuery(new Term("isParentDoc", "true")), Occur.MUST_NOT);
		builder.add(new TermQuery(new Term("code", "C99996")), Occur.MUST);
		builder.add(new TermQuery(new Term("propertyName", "Contributing_Source")), Occur.MUST);
		QueryParser propValueParser = new QueryParser("propertyValue", service.getAnalyzer());
		builder.add(propValueParser.createBooleanQuery("propertyValue", "FDA"), Occur.MUST);
		Query query = builder.build();
		QueryBitSetProducer parentFilter;
		parentFilter = new QueryBitSetProducer(
					new QueryParser("isParentDoc", new StandardAnalyzer(new CharArraySet(0, true))).parse("true"));
		ToParentBlockJoinQuery blockJoinQuery = new ToParentBlockJoinQuery(query, parentFilter, ScoreMode.Total);

		List<ScoreDoc> docs = service.query(null, blockJoinQuery);
		assertNotNull(docs);
		assertTrue(docs.size() > 1);
		List<Document> documents = docs.stream().map(scoredoc ->service.getById(scoredoc.doc)).collect(Collectors.toList());
		assertTrue(documents.size() > 0);
		assertTrue(documents.stream().anyMatch(
				doc -> doc.getFields().stream().anyMatch(
						x -> x.name().equals("entityCode") && x.stringValue().equals("C99996"))));
		assertTrue(documents.stream().anyMatch(
				doc2 -> doc2.getFields().stream().anyMatch(
						x -> x.name().equals("codingSchemeUri") && 
						x.stringValue().equals("http://evs.nci.nih.gov/valueset/FDA/C48325"))));
		assertTrue(documents.stream().anyMatch(
				doc2 -> doc2.getFields().stream().anyMatch(
						x -> x.name().equals("codingSchemeUri") &&
						x.stringValue().equals("http://evs.nci.nih.gov/valueset/FDA/C111112"))));

	}
	
	@Test
	@Order(6)
	public void queryOnValueSetMembershipAndText() throws ParseException {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(new TermQuery(new Term("isParentDoc", "true")), Occur.MUST_NOT);
		builder.add(new TermQuery(new Term("propertyValue", "BlindingWhite")), Occur.MUST_NOT);
		builder.add(new TermQuery(new Term("codingSchemeUri", "http://evs.nci.nih.gov/valueset/FDA/C48325")), Occur.SHOULD);
		builder.add(new TermQuery(new Term("codingSchemeUri", "http://evs.nci.nih.gov/valueset/FDA/C111112")), Occur.SHOULD);
		Query query = builder.build();
		QueryBitSetProducer parentFilter;
		parentFilter = new QueryBitSetProducer(
					new QueryParser("isParentDoc", new StandardAnalyzer(new CharArraySet(0, true))).parse("true"));
		ToParentBlockJoinQuery blockJoinQuery = new ToParentBlockJoinQuery(query, parentFilter, ScoreMode.Total);

		List<ScoreDoc> docs = service.query(null, blockJoinQuery);
		assertNotNull(docs);
		assertTrue(docs.size() > 1);
		List<Document> documents = docs.stream().map(scoredoc ->service.getById(scoredoc.doc)).collect(Collectors.toList());
		assertTrue(documents.size() > 0);
		assertTrue(documents.stream().anyMatch(
				doc -> doc.getFields().stream().anyMatch(
						x -> x.name().equals("entityCode") && x.stringValue().equals("C99996"))));
		assertTrue(documents.stream().anyMatch(
				doc2 -> doc2.getFields().stream().anyMatch(
						x -> x.name().equals("codingSchemeUri") && 
						x.stringValue().equals("http://evs.nci.nih.gov/valueset/FDA/C48325"))));
		assertTrue(documents.stream().anyMatch(
				doc2 -> doc2.getFields().stream().anyMatch(
						x -> x.name().equals("codingSchemeUri") &&
						x.stringValue().equals("http://evs.nci.nih.gov/valueset/FDA/C111112"))));

	}
	
	@Test
	@Order(7)
	public void queryOnValueSetMembershipAndTextForOneValueSet() throws ParseException {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(new TermQuery(new Term("isParentDoc", "true")), Occur.MUST_NOT);
		builder.add(new TermQuery(new Term("propertyValue", "BlindingWhite")), Occur.MUST_NOT);
		builder.add(new TermQuery(new Term("codingSchemeUri", "http://evs.nci.nih.gov/valueset/FDA/C48325")), Occur.SHOULD);;
		Query query = builder.build();
		QueryBitSetProducer parentFilter;
		parentFilter = new QueryBitSetProducer(
					new QueryParser("isParentDoc", new StandardAnalyzer(new CharArraySet(0, true))).parse("true"));
		ToParentBlockJoinQuery blockJoinQuery = new ToParentBlockJoinQuery(query, parentFilter, ScoreMode.Total);

		List<ScoreDoc> docs = service.query(null, blockJoinQuery);
		assertNotNull(docs);
		assertTrue(docs.size() > 1);
		List<Document> documents = docs.stream().map(scoredoc ->service.getById(scoredoc.doc)).collect(Collectors.toList());
		assertTrue(documents.size() > 0);
		assertTrue(documents.stream().anyMatch(
				doc -> doc.getFields().stream().anyMatch(
						x -> x.name().equals("entityCode") && x.stringValue().equals("C99996"))));
		assertTrue(documents.stream().anyMatch(
				doc2 -> doc2.getFields().stream().anyMatch(
						x -> x.name().equals("codingSchemeUri") && 
						x.stringValue().equals("http://evs.nci.nih.gov/valueset/FDA/C48325"))));
		assertFalse(documents.stream().anyMatch(
				doc2 -> doc2.getFields().stream().anyMatch(
						x -> x.name().equals("codingSchemeUri") &&
						x.stringValue().equals("http://evs.nci.nih.gov/valueset/FDA/C111112"))));

	}
	
	@Test
	@Order(8)
	public void queryPublishPropertyTest() throws ParseException {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(new TermQuery(new Term("isParentDoc", "true")), Occur.MUST_NOT);
		builder.add(new TermQuery(new Term("code", "C99999")), Occur.MUST);
		builder.add(new TermQuery(new Term("propertyName", "Publish_Value_Set")), Occur.MUST);
		QueryParser propValueParser = new QueryParser("propertyValue", service.getAnalyzer());
		builder.add(propValueParser.createBooleanQuery("propertyValue", "Yes"), Occur.MUST);
		Query query = builder.build();
		QueryBitSetProducer parentFilter;
		parentFilter = new QueryBitSetProducer(
					new QueryParser("isParentDoc", new StandardAnalyzer(new CharArraySet(0, true))).parse("true"));
		ToParentBlockJoinQuery blockJoinQuery = new ToParentBlockJoinQuery(query, parentFilter, ScoreMode.Total);

		List<ScoreDoc> docs = service.query(null, blockJoinQuery);
		assertNotNull(docs);
		assertTrue(docs.size() > 0);
		ScoreDoc sd = docs.get(0);
		Document doc = service.getById(sd.doc);
		assertNotNull(doc);
		assertTrue(doc.getFields().stream().anyMatch(x -> x.name().equals("entityCode")));
		assertTrue(doc.getFields().stream().filter(x -> x.name().equals("entityCode"))
				.anyMatch(y -> y.stringValue().equals("C99999")));
	}
	
	@Test
	@Order(9)
	public void queryPreferredPropertyTest() throws ParseException {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(new TermQuery(new Term("isParentDoc", "true")), Occur.MUST_NOT);
		builder.add(new TermQuery(new Term("code", "C99999")), Occur.MUST);
		builder.add(new TermQuery(new Term("isPreferred", "T")), Occur.MUST);
		Query query = builder.build();
		QueryBitSetProducer parentFilter;
		parentFilter = new QueryBitSetProducer(
					new QueryParser("isParentDoc", new StandardAnalyzer(new CharArraySet(0, true))).parse("true"));
		ToParentBlockJoinQuery blockJoinQuery = new ToParentBlockJoinQuery(query, parentFilter, ScoreMode.Total);

		List<ScoreDoc> docs = service.query(null, blockJoinQuery);
		assertNotNull(docs);
		assertTrue(docs.size() > 0);
		ScoreDoc sd = docs.get(0);
		Document doc = service.getById(sd.doc);
		assertNotNull(doc);
		assertTrue(doc.getFields().stream().anyMatch(x -> x.name().equals("entityCode")));
		assertTrue(doc.getFields().stream().filter(x -> x.name().equals("entityCode"))
				.anyMatch(y -> y.stringValue().equals("C99999")));
	}
	
//	@Test 
//	public void toChildBlockJoinQuery() throws ParseException {
//		BooleanQuery.Builder builder = new BooleanQuery.Builder();
//		builder.add(new TermQuery(new Term("isParentDoc", "true")), Occur.MUST);
//		builder.add(new TermQuery(new Term("codingSchemeUri", "http://evs.nci.nih.gov/valueset/FDA/C99997")), Occur.MUST);
//		Query query = builder.build();
//
//		List<ScoreDoc> docs = service.query(null, query);
//		assertNotNull(docs);
//		assertTrue(docs.size() > 0);
//		for(ScoreDoc sd: docs) {
//		Document doc = service.getById(sd.doc);
//		assertNotNull(doc);
//	}
//	}
	
	@Test
	@Order(10)
	public void testListAllSourceAssertedValueSets() throws LBException {
		List<CodingScheme> schemes = svc.listAllSourceAssertedValueSets();
		long count = schemes.stream().count();
		assertTrue(count > 0L);
		assertEquals(count, 7L);
		assertTrue(schemes.stream().filter(x -> x.getCodingSchemeName().equals("Black")).findAny().isPresent());
	}
	
	//TODO:  Needs addtl non asserted value sets loaded
	@Test
	@Order(8)
	public void testListAllValueSets() throws LBException {
		List<CodingScheme> schemes = svc.getMinimalSourceAssertedValueSetSchemes();
		long count = schemes.stream().count();
		assertTrue(count > 0L);
		assertEquals(count, 7L);
		assertTrue(schemes.stream().filter(x -> x.getCodingSchemeName().equals("Black")).findAny().isPresent());
	}

	@Test
	@Order(11)
	public void testgetSourceAssertedValueSetsForConceptReference() throws LBException {
		ConceptReference reference = Constructors.createConceptReference("C48323", "owl2lexevs");
		List<CodingScheme> schemes = svc.getSourceAssertedValueSetsForConceptReference(reference );
		long count = schemes.stream().count();
		assertTrue(count > 0L);
 		assertTrue(schemes.stream().filter(x -> x.getCodingSchemeName().equals("Structured Product Labeling Color Terminology")).findAny().isPresent());
 		assertTrue(schemes.stream().filter(x -> x.getCodingSchemeName().equals("CDISC SDTM Ophthalmic Exam Test Code Terminology")).findAny().isPresent());
	}
	
	
	@Test
	@Order(12)
	public void testSchemeData() throws LBException, URISyntaxException {
		CodingScheme scheme = svc.getSourceAssertedValueSetForValueSetURI(new URI(AssertedValueSetServices.BASE + "C54453"));
		assertNotNull(scheme);
		assertNotNull(scheme.getCodingSchemeName());
		assertEquals("Structured Product Labeling Color Terminology",scheme.getCodingSchemeName());
		assertEquals(AssertedValueSetServices.BASE + "FDA/" + "C54453", scheme.getCodingSchemeURI());
		assertTrue(scheme.getIsActive());
		assertEquals("C48323", scheme.getEntities().getEntityAsReference().stream().filter(x -> x.getEntityDescription().
				getContent().equals("Black")).findAny().get().getEntityCode());
		
	}
	
	
	
	@Test
	@Order(13)
	public void testGetSourceAssertedValueSetTopNodesForRootCode() {
		List<String> roots = svc.getSourceAssertedValueSetTopNodesForRootCode("C54453");
		assertNotNull(roots);
		assertTrue(roots.size() > 0);
		assertTrue(roots.stream().filter(x -> x.equals("C99999")).findAny().isPresent());
		assertTrue(roots.stream().filter(x -> x.equals("C117743")).findAny().isPresent());
		assertTrue(roots.stream().filter(x -> x.equals("C54453")).findAny().isPresent());
		assertTrue(roots.stream().filter(x -> x.equals("C111112")).findAny().isPresent());
		assertTrue(roots.stream().filter(x -> x.equals("C48323")).findAny().isPresent());
		assertTrue(roots.stream().filter(x -> x.equals("C48325")).findAny().isPresent());
	}
	
	@Test
	@Order(14)
	public void testGetSourceAssertedValueSetEntitiesForURI() {
		ResolvedConceptReferenceList list = svc.getSourceAssertedValueSetEntitiesForURI(AssertedValueSetServices.BASE + "C99999");
		List<ResolvedConceptReference> refs = Arrays.asList(list.getResolvedConceptReference());
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertTrue(refs.stream().filter(x -> x.getCode().equals("C99989")).findAny().isPresent());
		assertTrue(refs.stream().filter(x -> x.getCode().equals("C99988")).findAny().isPresent());
	}
	
	@Test
	@Order(15)
	public void testGetSourceAssertedValueSetforEntityCode() throws LBException {
		List<CodingScheme> schemes = svc.getSourceAssertedValueSetforTopNodeEntityCode("C48323");
		assertNotNull(schemes);
		assertTrue(schemes.size() > 0);
		assertEquals("Black", schemes.get(0).getCodingSchemeName());
		assertTrue(schemes.get(0).getEntities().getEntityCount() == 2);
		assertTrue(schemes.get(0).getEntities().getEntityAsReference().stream().anyMatch(x -> x.getEntityCode().equals("C99999")));	
	}
	
	@Test
	@Order(16)
	public void testGetSourceAssertedValueSetIteratorForURI() throws LBResourceUnavailableException {
		ResolvedConceptReferencesIterator itr = svc.getSourceAssertedValueSetIteratorForURI(AssertedValueSetServices.BASE + "FDA/" + "C54453");
		assertTrue(itr.hasNext());
		assertTrue(itr.numberRemaining() > 0);
		assertEquals(itr.numberRemaining(), 5);
	}
	
	@Test
	@Order(17)
	public void testGetListOfCodingSchemeVersionsUsedInResolution() throws LBException {
		List<CodingScheme> schemes = svc.getSourceAssertedValueSetforTopNodeEntityCode("C48323");
		CodingScheme scheme = schemes.get(0);
		AbsoluteCodingSchemeVersionReferenceList list = svc.getListOfCodingSchemeVersionsUsedInResolution(scheme);
		assertTrue(list.getAbsoluteCodingSchemeVersionReferenceCount() == 1);
		assertTrue(list.getAbsoluteCodingSchemeVersionReference(0).getCodingSchemeURN().equals("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl"));
		assertTrue(list.getAbsoluteCodingSchemeVersionReference(0).getCodingSchemeVersion().equals("0.1.5.1"));
	}
	
	@Test
	@Order(18)
	public void testGetSourceAssertedValueSetforValueSetMemberEntityCode() throws LBException {
		List<CodingScheme> schemes = svc.getSourceAssertedValueSetforValueSetMemberEntityCode("C99988");
		assertNotNull(schemes);
		assertTrue(schemes.size() > 0);
		assertTrue(schemes.stream().filter(x -> x.getCodingSchemeName().equals("Blacker")).findAny().isPresent());
		
		schemes = svc.getSourceAssertedValueSetforValueSetMemberEntityCode("C48323");
		long count = schemes.stream().count();
		assertTrue(count > 0L);
 		assertTrue(schemes.stream().filter(x -> x.getCodingSchemeName().equals("Structured Product Labeling Color Terminology")).findAny().isPresent());
 		assertTrue(schemes.stream().filter(x -> x.getCodingSchemeName().equals("CDISC SDTM Ophthalmic Exam Test Code Terminology")).findAny().isPresent());
	}
	
	@Test
	@Order(19)
	public void testGetSourceAssertedValueSetsforTextSearch() throws LBException {
		List<AbsoluteCodingSchemeVersionReference> acsvr = svc.getSourceAssertedValueSetsforTextSearch("Black", MatchAlgorithm.LUCENE);
		assertTrue(acsvr.stream().filter(x -> x.getCodingSchemeURN().equals(AssertedValueSetServices.BASE + "C54453")).findAny().isPresent());
		assertTrue(acsvr.stream().filter(x -> x.getCodingSchemeVersion().equals("0.1.5.1")).findAny().isPresent());
		assertTrue(acsvr.stream().filter(x -> x.getCodingSchemeURN().equals(AssertedValueSetServices.BASE + "C117743")).findAny().isPresent());
	}
	
	@Test
	@Order(20)
	public void testGetAllSourceAssertedValueSetEntities() {
		@SuppressWarnings("unchecked")
		List<Entity> entities = (List<Entity>) svc.getAllSourceAssertedValueSetEntities();
		assertNotNull(entities);
		assertTrue(entities.size() > 0);
		assertEquals(entities.size(), 13);
	}
	
	@Test
	@Order(21)
	public void testGetValueSetCodeForUri() {
		String code = ((SourceAssertedValueSetServiceImpl) svc).
		getEntityCodeFromValueSetDefinition(AssertedValueSetServices.BASE + "FDA/" + "C54453");
		assertNotNull(code);
		assertEquals(code, "C54453");
	}
	
	@Test
	@Order(22)
	public void getAnalyzerTest() {
		Analyzer an = service.getAnalyzer();
		assertNotNull(an);
	}
	
	@Test
	@Order(23)
	public void DeleteFromOneVSButNotTheOther() {
		Entity entity = new Entity();
	    entity.setEntityCode("C99997");
	    entity.setEntityCodeNamespace("owl2lexevs");
		service.deleteEntityFromIndex("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", 
				"0.1.5.1","http://evs.nci.nih.gov/valueset/FDA/C111112", "Whiter Shade of Grey", entity);
		
		//Entity still exists in another coding scheme
		BooleanQuery.Builder remainsbuilder = new BooleanQuery.Builder();
		remainsbuilder.add(new TermQuery(new Term("code", "C99997")), Occur.MUST);
		remainsbuilder.add(new TermQuery(new Term("isParentDoc", "true")), Occur.MUST_NOT);
		Query remainsquery = remainsbuilder.build();
		QueryBitSetProducer remainsparentFilter;
		try {
			remainsparentFilter = new QueryBitSetProducer(
					new QueryParser("isParentDoc", new StandardAnalyzer(new CharArraySet(0, true))).parse("true"));
		} catch (ParseException e) {
			throw new RuntimeException("Query Parser Failed against parent query: ", e);
		}
		ToParentBlockJoinQuery remainsblockJoinQuery = new ToParentBlockJoinQuery(remainsquery, remainsparentFilter, ScoreMode.Total);
		List<ScoreDoc> remainsdocs = service.query(null, remainsblockJoinQuery);
		assertNotNull(remainsdocs);
		assertTrue(remainsdocs.size() > 0);
		Document doc = service.getById(remainsdocs.get(0).doc);
		assertTrue(doc.getFields().stream().filter(
				x -> x.name().equals("codingSchemeUri")).anyMatch(
				y -> y.stringValue().equals("http://evs.nci.nih.gov/valueset/FDA/C48325")));
		
		//All values of Coding Scheme in delete are not wiped from the index
		BooleanQuery.Builder remainsBuilder2 = new BooleanQuery.Builder();
		remainsBuilder2.add(new TermQuery(new Term("codingSchemeUri", "http://evs.nci.nih.gov/valueset/FDA/C111112")), Occur.MUST);
		remainsBuilder2.add(new TermQuery(new Term("isParentDoc", "true")), Occur.MUST_NOT);
		Query remainsQuery2 = remainsBuilder2.build();
		QueryBitSetProducer remainsparentFilter2;
		try {
			remainsparentFilter2 = new QueryBitSetProducer(
					new QueryParser("isParentDoc", new StandardAnalyzer(new CharArraySet(0, true))).parse("true"));
		} catch (ParseException e) {
			throw new RuntimeException("Query Parser Failed against parent query: ", e);
		}
		ToParentBlockJoinQuery remainsblockJoinQuery2 = new ToParentBlockJoinQuery(remainsQuery2, remainsparentFilter2, ScoreMode.Total);
		List<ScoreDoc> remainsdocs2 = service.query(null, remainsblockJoinQuery2);
		assertNotNull(remainsdocs2);
		assertTrue(remainsdocs2.size() > 0);
		Document doc2 = service.getById(remainsdocs2.get(0).doc);
		assertTrue(doc2.getFields().stream().filter(
				x -> x.name().equals("codingSchemeUri")).anyMatch(
				y -> y.stringValue().equals("http://evs.nci.nih.gov/valueset/FDA/C111112")));
		
	}

}

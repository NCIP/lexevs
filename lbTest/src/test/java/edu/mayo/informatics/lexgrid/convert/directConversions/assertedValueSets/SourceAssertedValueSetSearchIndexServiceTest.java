package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Impl.loaders.AssertedValueSetIndexLoaderImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
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
import org.lexevs.dao.index.indexer.AssertedValueSetEntityIndexer;
import org.lexevs.dao.index.service.search.SourceAssertedValueSetSearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;

public class SourceAssertedValueSetSearchIndexServiceTest {
	static SourceAssertedValueSetSearchIndexService service;

	@BeforeClass
	public static void setUp() throws Exception {
		service = LexEvsServiceLocator.getInstance().getIndexServiceManager().getAssertedValueSetIndexService();
		service.createIndex(Constructors.createAbsoluteCodingSchemeVersionReference(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5"));
	}


	@Test
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
				"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5", entity);

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
				"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5", entity);

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
	}

	@Test
	public void deleteEntityFromIndexTest() {
		Entity entity = new Entity();
		entity.setEntityCode("BR549");
		entity.setEntityCodeNamespace("ontology");
		service.deleteEntityFromIndex(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5", entity);

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
	}

	@Test
	public void queryUpdateTest() throws ParseException {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(new TermQuery(new Term("code", "3675309")), Occur.MUST);
		builder.add(new TermQuery(new Term("isParentDoc", "true")), Occur.MUST_NOT);
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
				.anyMatch(y -> y.stringValue().equals("3675309")));
	}
	
	@Test
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
	}
	
	@Test
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

	@Test
	public void getAnalyzerTest() {
		Analyzer an = service.getAnalyzer();
		assertNotNull(an);
	}

    @AfterClass
	public static void dropIndexTest() {
		service.dropIndex(Constructors.createAbsoluteCodingSchemeVersionReference(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5"));
		boolean doesExist = service.doesIndexExist(Constructors.
				createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5"));
		assertFalse(doesExist);
	}

}

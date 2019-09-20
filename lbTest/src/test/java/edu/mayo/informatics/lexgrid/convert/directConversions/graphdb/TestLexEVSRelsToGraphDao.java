package edu.mayo.informatics.lexgrid.convert.directConversions.graphdb;

import static org.junit.Assert.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.graph.LexEVSRelsToGraphDao;
import org.lexevs.dao.database.service.graphdb.GraphingDataBaseService;
import org.lexevs.dao.database.service.graphdb.GraphingDataBaseServiceImpl;
import org.lexevs.dao.database.utility.GraphingDatabaseUtil;
import org.lexevs.locator.LexEvsServiceLocator;

import com.arangodb.ArangoDatabase;

public class TestLexEVSRelsToGraphDao {

	static LexEVSRelsToGraphDao graphRels;

	@BeforeClass
	public static void setUp() throws Exception {
		graphRels = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getGraphingDatabaseService()
				.getRels2graph();
		LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getGraphingDatabaseService()
				.loadGraphsForTerminologyURIAndVersion("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
	}

	@Test
	public void testGetSupportedAssociationNamesForScheme() {
		List<String> rels = graphRels
				.getSupportedAssociationNamesForScheme("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		rels.stream().forEach(x -> System.out.println(x));
		assertEquals(rels.size(), 65);
	}

	@Test
	public void testGetEdgesForAssociationName() {
		List<Triple> triples = graphRels.getValidTriplesForAssociationNames("subClassOf",
				"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		assertTrue(triples != null);
		assertTrue(triples.size() > 0);
		assertEquals(triples.size(), 55);
		// Have anonymous nodes been removed?
		triples.stream().forEach(
				x -> assertTrue(!x.getSourceEntityCode().contains("@") && !x.getTargetEntityCode().contains("@")));
	}
	
	@Test
	public void testEdgesPersisted(){
		List<String> rels = graphRels
				.getSupportedAssociationNamesForScheme("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		List <Triple> triples = rels.stream().map(associationName -> graphRels.getValidTriplesForAssociationNames(associationName,
				"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5")).flatMap(List::stream).collect(Collectors.toList());
		assertNotNull(triples);
		assertTrue(triples.size() > 0);
		assertEquals(triples.size(), 165);
	}
	
	@Test
	public void testGetDataBaseConnectionForURIandVersion(){
	ArangoDatabase db = graphRels.
			getDataBaseConnectionForScheme(
					"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
	assertNotNull(db);
	assertTrue(db.getAccessibleDatabases().stream().anyMatch(z -> z.equals("owl2lexevs")));
	db.getCollections().stream().map(x -> x.getName()).forEach(System.out::println);
	assertTrue(db.getCollections().stream().anyMatch(x -> x.getName().equals("E_subClassOf")));
	}
	
	
	@Test
	public void testNormaliseGraphNames(){
		String notNormalName = " _this isn't normal ";
		String result = GraphingDatabaseUtil.normalizeGraphandGraphDatabaseName(notNormalName);
		assertEquals( "this_isn't_normal", result);
	}
	
	@Test
	public void testGetVertexCollectionName(){
		String notNormalName = "ThisIsAnVertexCollectionThatIsLongerThan_Any_VertexName_shouldbe_but_some_descriptions-JustKEepGoing";
		String result = graphRels.getVertexCollectionName(notNormalName);
		assertEquals( "V_ThisIsAnVertexCollectionThatIsLongerThan_Any_VertexName_should", result);
	}
	
	@Test
	public void testGetEdgeCollectionName(){
		String notNormalName = "ThisIsAnEdgeThatIsLongerThan_Any_EdgeName_shouldbe_but_some_descriptions_of_edges-JustKEepGoing";
		String result = graphRels.getAssociationEdgeNameForRow(notNormalName);
		assertEquals( "E_ThisIsAnEdgeThatIsLongerThan_Any_EdgeName_shouldbe_but_some_de", result);
	}
	
	@Test
	public void testGetProductionVersionForURI(){
		String version = ((GraphingDataBaseServiceImpl)LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getGraphingDatabaseService()).getVersionForProductionTaggedTerminology("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl");
		assertNotNull(version);
		assertEquals("0.1.5", version);
	}
	
	@AfterClass
	public static void after(){
		graphRels.getGraphSourceMgr().getDataSource("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl").dropGraphsAndDatabaseForDataSource();
	}
	
}

package edu.mayo.informatics.lexgrid.convert.directConversions.graphdb;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.graph.LexEVSRelsToGraphDao;
import org.lexevs.locator.LexEvsServiceLocator;

public class TestLexEVSRelsToGraph {
	LexEVSRelsToGraphDao graphRels;

	@Before
	public void setUp() throws Exception {
		graphRels = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getGraphingDatabaseService()
				.getRels2graph();
	}

	@Test
	public void testGetSupportedAssociationNamesForScheme() {
		List<String> rels = graphRels
				.getSupportedAssociationNamesForScheme("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "18.05b");
		rels.stream().forEach(x -> System.out.println(x));
		assertEquals(rels.size(), 128);
	}

	@Test
	public void testGetEdgesForAssociationName() {
		List<Triple> triples = graphRels.getValidTriplesForAssociationNames("subClassOf",
				"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "18.05b");
		assertTrue(triples != null);
		assertTrue(triples.size() > 0);
		// Have anonymous nodes been removed?
		triples.stream().forEach(
				x -> assertTrue(!x.getSourceEntityCode().contains("@") && !x.getTargetEntityCode().contains("@")));
	}
	
	@Test
	public void testNormaliseGraphNames(){
		String notNormalName = " _this isn't normal ";
		String result = graphRels.normalizeGraphName(notNormalName);
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
	
}

package edu.mayo.informatics.lexgrid.convert.directConversions.graphdb;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.graph.LexEVSRelsToGraphDao;
import org.lexevs.locator.LexEvsServiceLocator;

public class TestLexEVSRelsToGraphDao {

	LexEVSRelsToGraphDao graphRels;

	@Before
	public void setUp() throws Exception {
		graphRels = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getGraphingDatabaseService()
				.getRels2graph();
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
}

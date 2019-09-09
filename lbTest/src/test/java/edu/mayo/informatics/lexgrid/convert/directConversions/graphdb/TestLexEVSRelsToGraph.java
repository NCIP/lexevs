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
		graphRels = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getGraphingDatabaseService().getRels2graph();
	}
	@Test
	public void testGetSupportedAssociationNamesForScheme() {
		List<String> rels = graphRels.
				getSupportedAssociationNamesForScheme("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#","18.05b");
		rels.stream().forEach(x-> System.out.println(x));
		assertEquals(rels.size(), 128);
	}
	
	@Test
	public void testGetRootsForAssociationName(){
		List<Triple> triples = graphRels.getValidTriplesForAssociationNames(
				"subClassOf",
				"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#",
				"18.05b");
		assertTrue(triples != null);
		assertTrue(triples.size() > 0);
		triples.stream().forEach(x -> assertTrue(!x.getSourceEntityCode().contains("@") &&  ! x.getTargetEntityCode().contains("@")));
	}
}

package edu.mayo.informatics.lexgrid.convert.directConversions.graphdb;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.access.association.model.Triple;

public class TestLexEVSRelsToGraph {
	LexEVSRelsToGraph graphRels;
	@Before
	public void setUp() throws Exception {
		graphRels = 
				new LexEVSRelsToGraph(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#","18.05b");
	}

	@Test
	public void testGetSupportedAssociationNamesForScheme() {
		List<String> rels = graphRels.getSupportedAssociationNamesForScheme();
		rels.stream().forEach(x-> System.out.println(x));
		assertEquals(rels.size(), 128);
	}
	
	@Test
	public void testGetRootsForAssociationName(){
		List<Triple> triples = graphRels.getValidTriplesForAssociationNames("subClassOf");
		assertTrue(triples != null);
		assertTrue(triples.size() > 0);
		triples.stream().forEach(x -> assertTrue(!x.getSourceEntityCode().contains("@") &&  ! x.getTargetEntityCode().contains("@")));
	}
}

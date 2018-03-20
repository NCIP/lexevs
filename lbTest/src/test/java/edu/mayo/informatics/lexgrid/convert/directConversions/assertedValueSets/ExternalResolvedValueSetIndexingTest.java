package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import static org.junit.Assert.*;

import java.net.URISyntaxException;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.resolvedvalueset.impl.ExternalResolvedValueSetIndexService;

public class ExternalResolvedValueSetIndexingTest {
	ExternalResolvedValueSetIndexService service;
	@Before
	public void setUp() throws Exception {
		service = new ExternalResolvedValueSetIndexService();
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
	public void testIndexExternalEntitiesForExternalRVSets() {
		service.indexExternalResolvedValueSetsToAssertedValueSetIndex();
	}

}

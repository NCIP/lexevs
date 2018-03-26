package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension.MatchAlgorithm;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.junit.Before;
import org.junit.Test;

public class SourceAssertedValueSetSearchExtensionTest {
	LexBIGService lbsvc;
	SourceAssertedValueSetSearchExtensionImpl assertedVSsvc;

	@Before
	public void setUp() throws Exception {
		lbsvc = LexBIGServiceImpl.defaultInstance();
		assertedVSsvc = (SourceAssertedValueSetSearchExtensionImpl) lbsvc
				.getGenericExtension("AssertedValueSetSearchExtension");
	}

	@Test
	public void testCodeExact() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("C48323", null, null, MatchAlgorithm.CODE_EXACT,
				false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "Black");
	}

	@Test
	public void testPresentationExact() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("Black", null, null,
				MatchAlgorithm.PRESENTATION_EXACT, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "Black");
	}

	@Test
	public void testPresentationContains() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("Blinding", null, null,
				MatchAlgorithm.PRESENTATION_CONTAINS, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "BlindingWhite");
	}

	@Test
	public void testPropertyExact() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("BlindingWhite", null, null,
				MatchAlgorithm.PROPERTY_EXACT, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "BlindingWhite");
	}

	@Test
	public void testPropertyContains() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("Blinding", null, null,
				MatchAlgorithm.PRESENTATION_CONTAINS, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "BlindingWhite");
	}

	@Test
	public void testLuceneQuery() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("BlindingWhite", null, null,
				MatchAlgorithm.LUCENE, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "BlindingWhite");
	}
	
	@Test 
	public void testCodeExactRedux() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("C37927", null, null, MatchAlgorithm.CODE_EXACT,
				false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "Color");
	}
	
	@Test
	public void testPresentationExactRedux() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("Color", null, null,
				MatchAlgorithm.PRESENTATION_EXACT, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "Color");
	}
	
	@Test
	public void testPresentationExactLC() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("color", null, null,
				MatchAlgorithm.PRESENTATION_EXACT, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "Color");
	}
	
	@Test
	public void testPropertyExactRedux() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("the appearance of objects (or " + 
						"light sources) described in terms " + 
						"of a person's perception of " + 
						"their hue and lightness (or " + 
						"brightness) and saturation. (nci)", null, null,
				MatchAlgorithm.PROPERTY_EXACT, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "Color");
	}
	
	@Test
	public void testPropertyContainsBoundary1() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("The appearance of", null, null,
				MatchAlgorithm.PROPERTY_CONTAINS, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "Color");
	}
	
	@Test
	public void testPropertyContainsBoundary2() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("(or light sources)", null, null,
				MatchAlgorithm.PROPERTY_CONTAINS, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "Color");
	}
	
	@Test
	public void testPropertyContainsBoundary3() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("saturation. (nci)", null, null,
				MatchAlgorithm.PROPERTY_CONTAINS, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "Color");
	}
	
	
}

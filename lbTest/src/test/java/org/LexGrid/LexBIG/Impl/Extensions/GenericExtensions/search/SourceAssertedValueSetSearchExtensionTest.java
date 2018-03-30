package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
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
				search("C99998", null, null, MatchAlgorithm.CODE_EXACT,
				false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "UberBlack");
	}
	
	@Test
	public void testPresentationExactRedux() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("UberBlack", null, null,
				MatchAlgorithm.PRESENTATION_EXACT, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "UberBlack");
	}
	
	@Test
	public void testPresentationExactLC() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("uberblack", null, null,
				MatchAlgorithm.PRESENTATION_EXACT, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "UberBlack");
	}
	
	@Test
	public void testPropertyExactRedux() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("Uber dark color;", null, null,
				MatchAlgorithm.PROPERTY_EXACT, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "UberBlack");
	}
	
	@Test
	public void testPropertyContainsBoundary1() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("Uber dark", null, null,
				MatchAlgorithm.PROPERTY_CONTAINS, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "UberBlack");
	}
	
	@Test
	public void testPropertyContainsBoundary2() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("dark color", null, null,
				MatchAlgorithm.PROPERTY_CONTAINS, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "UberBlack");
	}
	
	@Test
	public void testPropertyContainsBoundary3() throws LBException {
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("dark", null, null,
				MatchAlgorithm.PROPERTY_CONTAINS, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "UberBlack");
	}
	
	@Test
	public void testRestrictToAssertedSchemeCodeExactMatch() throws LBException {
		Set<CodingSchemeReference> refs = new HashSet<CodingSchemeReference>();
		CodingSchemeReference r = new CodingSchemeReference();
		r.setCodingScheme("http://evs.nci.nih.gov/valueset/FDA/C54453");
		refs.add(r);
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("C111112", refs, null,
				MatchAlgorithm.CODE_EXACT, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertEquals(ref.getEntityDescription().getContent(), "Whiter Shade of Grey");
		assertNotNull(ref);	
		ref = itr.next();
		assertNull(ref);	
	}
	
	@Test
	public void testRestrictToAssertedSchemesCodeExactMatchAppearsInTwoSchemes() throws LBException {
		Set<CodingSchemeReference> refs = new HashSet<CodingSchemeReference>();
		CodingSchemeReference r = new CodingSchemeReference();
		r.setCodingScheme("http://evs.nci.nih.gov/valueset/FDA/C111112");
		CodingSchemeReference s = new CodingSchemeReference();
		s.setCodingScheme("http://evs.nci.nih.gov/valueset/FDA/C48325");

		refs.add(r);
		refs.add(s);
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("C99996", refs, null,
				MatchAlgorithm.CODE_EXACT, false, false);
		assertTrue(itr.hasNext());
		while(itr.hasNext()) {
		ResolvedConceptReference ref = itr.next();
		assertTrue(ref.getEntityDescription().getContent().equals("BlindingWhite") || 
				ref.getEntityDescription().getContent().equals("Whiter Shade of Grey"));
		}	
	}
	
	@Test
	public void testRestrictToAssertedSchemesCodeExactMatchAppearsInOneOfTwoSchemes() throws LBException {
		Set<CodingSchemeReference> refs = new HashSet<CodingSchemeReference>();
		CodingSchemeReference r = new CodingSchemeReference();
		r.setCodingScheme("http://evs.nci.nih.gov/valueset/FDA/C111112");
		CodingSchemeReference s = new CodingSchemeReference();
		s.setCodingScheme("http://evs.nci.nih.gov/valueset/FDA/C54453");

		refs.add(r);
		refs.add(s);
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("C99996", refs, null,
				MatchAlgorithm.CODE_EXACT, false, false);
		assertTrue(itr.hasNext());

		ResolvedConceptReference ref = itr.next();
		assertTrue(ref.getEntityDescription().getContent().equals("BlindingWhite"));
		assertFalse(ref.getEntityDescription().getContent().equals("Whiter Shade of Grey"));	
		assertNull(itr.next());
	}
	
	@Test
	public void testPresentationExactRestrictToOneScheme() throws LBException {
		Set<CodingSchemeReference> refs = new HashSet<CodingSchemeReference>();
		CodingSchemeReference r = new CodingSchemeReference();
		r.setCodingScheme("http://evs.nci.nih.gov/valueset/FDA/C54453");
		refs.add(r);
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("Whiter Shade of Grey", refs, null,
				MatchAlgorithm.PRESENTATION_EXACT, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertEquals(ref.getEntityDescription().getContent(), "Whiter Shade of Grey");
		assertNotNull(ref);	
		ref = itr.next();
		assertNull(ref);	
	}
	
	@Test
	public void testPresentationExactRestrictToTwoSchemes() throws LBException {
		Set<CodingSchemeReference> refs = new HashSet<CodingSchemeReference>();
		CodingSchemeReference r = new CodingSchemeReference();
		r.setCodingScheme("http://evs.nci.nih.gov/valueset/FDA/C111112");
		CodingSchemeReference s = new CodingSchemeReference();
		s.setCodingScheme("http://evs.nci.nih.gov/valueset/FDA/C48325");

		refs.add(r);
		refs.add(s);
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("BlindingWhite", refs, null,
				MatchAlgorithm.PRESENTATION_EXACT, false, false);
		assertTrue(itr.hasNext());
		while(itr.hasNext()) {
		ResolvedConceptReference ref = itr.next();
		assertTrue(ref.getEntityDescription().getContent().equals("BlindingWhite"));
		}	
	}
	
	@Test
	public void testPresentationContainsRestrictToOneScheme() throws LBException {
		Set<CodingSchemeReference> refs = new HashSet<CodingSchemeReference>();
		CodingSchemeReference r = new CodingSchemeReference();
		r.setCodingScheme("http://evs.nci.nih.gov/valueset/FDA/C54453");
		refs.add(r);
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("Shade", refs, null,
				MatchAlgorithm.PRESENTATION_CONTAINS, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertEquals(ref.getEntityDescription().getContent(), "Whiter Shade of Grey");
		assertNotNull(ref);	
		ref = itr.next();
		assertNull(ref);	
	}
	
	@Test
	public void testPresentationContainsRestrictToTwoSchemes() throws LBException {
		Set<CodingSchemeReference> refs = new HashSet<CodingSchemeReference>();
		CodingSchemeReference r = new CodingSchemeReference();
		r.setCodingScheme("http://evs.nci.nih.gov/valueset/FDA/C111112");
		CodingSchemeReference s = new CodingSchemeReference();
		s.setCodingScheme("http://evs.nci.nih.gov/valueset/FDA/C48325");

		refs.add(r);
		refs.add(s);
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("Blinding", refs, null,
				MatchAlgorithm.PRESENTATION_CONTAINS, false, false);
		assertTrue(itr.hasNext());
		while(itr.hasNext()) {
		ResolvedConceptReference ref = itr.next();
		assertTrue(ref.getEntityDescription().getContent().equals("BlindingWhite"));
		}	
	}
	
	@Test
	public void testPropertyExactRestrictToOneScheme() throws LBException {
		Set<CodingSchemeReference> refs = new HashSet<CodingSchemeReference>();
		CodingSchemeReference r = new CodingSchemeReference();
		r.setCodingScheme("http://evs.nci.nih.gov/valueset/FDA/C111112");
		refs.add(r);
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("blindingly white color", refs, null,
				MatchAlgorithm.PROPERTY_EXACT, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertEquals(ref.getEntityDescription().getContent(), "BlindingWhite");
		assertNotNull(ref);	
		ref = itr.next();
		assertNull(ref);	
	}
	
	@Test
	public void testPropertyExactRestrictToTwoSchemes() throws LBException {
		Set<CodingSchemeReference> refs = new HashSet<CodingSchemeReference>();
		CodingSchemeReference r = new CodingSchemeReference();
		r.setCodingScheme("http://evs.nci.nih.gov/valueset/FDA/C111112");
		CodingSchemeReference s = new CodingSchemeReference();
		s.setCodingScheme("http://evs.nci.nih.gov/valueset/FDA/C48325");

		refs.add(r);
		refs.add(s);
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("blindingly white color", refs, null,
				MatchAlgorithm.PROPERTY_EXACT, false, false);
		assertTrue(itr.hasNext());
		while(itr.hasNext()) {
		ResolvedConceptReference ref = itr.next();
		assertTrue(ref.getEntityDescription().getContent().equals("BlindingWhite"));
		}	
	}
	
	@Test
	public void testRestrictToAssertedSchemeAndPropertyContainsEmpty() throws LBException {
		Set<CodingSchemeReference> refs = new HashSet<CodingSchemeReference>();
		CodingSchemeReference r = new CodingSchemeReference();
		r.setCodingScheme("http://evs.nci.nih.gov/valueset/FDA/C111112");
		refs.add(r);
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("dark", refs, null,
				MatchAlgorithm.PROPERTY_CONTAINS, false, false);
		assertFalse(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNull(ref);
	}
	
	@Test
	public void testRestrictToAssertedSchemePopulated() throws LBException {
		Set<CodingSchemeReference> refs = new HashSet<CodingSchemeReference>();
		CodingSchemeReference r = new CodingSchemeReference();
		r.setCodingScheme("http://evs.nci.nih.gov/valueset/FDA/C111112");
		refs.add(r);
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("White", refs, null,
				MatchAlgorithm.PROPERTY_CONTAINS, false, false);
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		assertEquals(ref.getEntityDescription().getContent(), "White");
		assertEquals(ref.getCodingSchemeURI(), "http://evs.nci.nih.gov/valueset/FDA/C111112");
		while(itr.hasNext()) {
			ResolvedConceptReference ref2 = itr.next();
			assertEquals(ref2.getCodingSchemeURI(), "http://evs.nci.nih.gov/valueset/FDA/C111112");
		}
	}
	
	@Test
	public void testRestrictToAssertedSchemesPopulated() throws LBException {
		Set<CodingSchemeReference> refs = new HashSet<CodingSchemeReference>();
		CodingSchemeReference r = new CodingSchemeReference();
		r.setCodingScheme("http://evs.nci.nih.gov/valueset/FDA/C54453");
		CodingSchemeReference s = new CodingSchemeReference();
		s.setCodingScheme("http://evs.nci.nih.gov/valueset/FDA/C48323");

		refs.add(r);
		refs.add(s);
		
		ResolvedConceptReferencesIterator itr = assertedVSsvc.
				search("Black", refs, null,
				MatchAlgorithm.PROPERTY_CONTAINS, false, false);
		assertTrue(itr.hasNext());
		while(itr.hasNext()) {
			ResolvedConceptReference ref2 = itr.next();
			assertTrue(ref2.getCodingSchemeURI().equals( "http://evs.nci.nih.gov/valueset/FDA/C54453") || 
					ref2.getCodingSchemeURI().equals( "http://evs.nci.nih.gov/valueset/FDA/C48323")) ;
			assertTrue(ref2.getEntityDescription().getContent().equals("UberBlack") || 
					ref2.getEntityDescription().getContent().equals("Black")  || 
					ref2.getEntityDescription().getContent().equals("White") || 
					ref2.getEntityDescription().getContent().equals("Whiter Shade of Grey") ||
					ref2.getEntityDescription().getContent().equals("Blacker"));
		}
	}
	
}

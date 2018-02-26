package org.LexGrid.valuesets.sourceasserted.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension.MatchAlgorithm;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.lexevs.dao.index.service.search.SourceAssertedValueSetSearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.valuesets.sourceasserted.SourceAssertedValueSetService;
import org.lexgrid.valuesets.sourceasserted.impl.SourceAssertedValueSetServiceImpl;

import junit.framework.TestCase;

@Category(IncludeForDistributedTests.class)
public class SourceAssertedValueSetTest{
	static SourceAssertedValueSetService svc;
	static SourceAssertedValueSetSearchIndexService service;

	@BeforeClass
	public static void createIndex() throws Exception {
		service = LexEvsServiceLocator.getInstance().getIndexServiceManager().getAssertedValueSetIndexService();
		service.createIndex(Constructors.createAbsoluteCodingSchemeVersionReference(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5"));
		
		AssertedValueSetParameters params = new AssertedValueSetParameters.Builder("0.1.5").
				assertedDefaultHierarchyVSRelation("Concept_In_Subset").
				codingSchemeName("owl2lexevs").
				codingSchemeURI("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl")
				.build();
		svc = SourceAssertedValueSetServiceImpl.getDefaultValueSetServiceForVersion(params);
	}
	
	
	@Test
	public void testListAllSourceAssertedValueSets() throws LBException {
		List<CodingScheme> schemes = svc.listAllSourceAssertedValueSets();
		long count = schemes.stream().count();
		assertTrue(count > 0L);
		assertEquals(count, 8L);
		assertTrue(schemes.stream().filter(x -> x.getCodingSchemeName().equals("Black")).findAny().isPresent());
	}
	
	//TODO:  Needs addtl non asserted value sets loaded
	@Test
	public void testListAllValueSets() throws LBException {
		List<CodingScheme> schemes = svc.getMinimalSourceAssertedValueSetSchemes();
		long count = schemes.stream().count();
		assertTrue(count > 0L);
		assertEquals(count, 15L);
		assertTrue(schemes.stream().filter(x -> x.getCodingSchemeName().equals("Black")).findAny().isPresent());
	}

	@Test
	public void testgetSourceAssertedValueSetsForConceptReference() throws LBException {
		ConceptReference reference = Constructors.createConceptReference("C48323", "owl2lexevs");
		List<CodingScheme> schemes = svc.getSourceAssertedValueSetsForConceptReference(reference );
		long count = schemes.stream().count();
		assertTrue(count > 0L);
 		assertTrue(schemes.stream().filter(x -> x.getCodingSchemeName().equals("Structured Product Labeling Color Terminology")).findAny().isPresent());
 		assertTrue(schemes.stream().filter(x -> x.getCodingSchemeName().equals("CDISC SDTM Ophthalmic Exam Test Code Terminology")).findAny().isPresent());
	}
	
	
	@Test
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
	public void testGetSourceAssertedValueSetTopNodesForRootCode() {
		List<String> roots = svc.getSourceAssertedValueSetTopNodesForRootCode("C54453");
		assertNotNull(roots);
		assertTrue(roots.size() > 0);
		assertTrue(roots.stream().filter(x -> x.equals("C99999")).findAny().isPresent());
		assertTrue(roots.stream().filter(x -> x.equals("C99998")).findAny().isPresent());
		assertTrue(roots.stream().filter(x -> x.equals("C99997")).findAny().isPresent());
		assertTrue(roots.stream().filter(x -> x.equals("C99996")).findAny().isPresent());
		assertTrue(roots.stream().filter(x -> x.equals("C99989")).findAny().isPresent());
		assertTrue(roots.stream().filter(x -> x.equals("C99988")).findAny().isPresent());
		assertTrue(roots.stream().filter(x -> x.equals("C48323")).findAny().isPresent());
		assertTrue(roots.stream().filter(x -> x.equals("C48325")).findAny().isPresent());
		assertFalse(roots.stream().filter(x -> x.equals("C37927")).findAny().isPresent());
	}
	
	@Test
	public void testGetSourceAssertedValueSetEntitiesForURI() {
		ResolvedConceptReferenceList list = svc.getSourceAssertedValueSetEntitiesForURI(AssertedValueSetServices.BASE + "C99999");
		List<ResolvedConceptReference> refs = Arrays.asList(list.getResolvedConceptReference());
		assertNotNull(refs);
		assertTrue(refs.size() > 0);
		assertTrue(refs.stream().filter(x -> x.getCode().equals("C99989")).findAny().isPresent());
		assertTrue(refs.stream().filter(x -> x.getCode().equals("C99988")).findAny().isPresent());
	}
	
	@Test
	public void testGetSourceAssertedValueSetforEntityCode() throws LBException {
		List<CodingScheme> schemes = svc.getSourceAssertedValueSetforTopNodeEntityCode("C48323");
		assertNotNull(schemes);
		assertTrue(schemes.size() > 0);
		assertEquals("Black", schemes.get(0).getCodingSchemeName());
		assertTrue(schemes.get(0).getEntities().getEntityCount() == 2);
		assertTrue(schemes.get(0).getEntities().getEntityAsReference().stream().anyMatch(x -> x.getEntityCode().equals("C99999")));	
	}
	
	@Test
	public void testGetSourceAssertedValueSetIteratorForURI() throws LBResourceUnavailableException {
		ResolvedConceptReferencesIterator itr = svc.getSourceAssertedValueSetIteratorForURI(AssertedValueSetServices.BASE + "FDA/" + "C54453");
		assertTrue(itr.hasNext());
		assertTrue(itr.numberRemaining() > 0);
		assertEquals(itr.numberRemaining(), 2);
	}
	
	@Test
	public void testGetListOfCodingSchemeVersionsUsedInResolution() throws LBException {
		List<CodingScheme> schemes = svc.getSourceAssertedValueSetforTopNodeEntityCode("C48323");
		CodingScheme scheme = schemes.get(0);
		AbsoluteCodingSchemeVersionReferenceList list = svc.getListOfCodingSchemeVersionsUsedInResolution(scheme);
		assertTrue(list.getAbsoluteCodingSchemeVersionReferenceCount() == 1);
		assertTrue(list.getAbsoluteCodingSchemeVersionReference(0).getCodingSchemeURN().equals("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl"));
		assertTrue(list.getAbsoluteCodingSchemeVersionReference(0).getCodingSchemeVersion().equals("0.1.5"));
	}
	
	@Test
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
	public void testGetSourceAssertedValueSetsforTextSearch() throws LBException {
		List<AbsoluteCodingSchemeVersionReference> acsvr = svc.getSourceAssertedValueSetsforTextSearch("Black", MatchAlgorithm.LUCENE);
		assertTrue(acsvr.stream().filter(x -> x.getCodingSchemeURN().equals(AssertedValueSetServices.BASE + "C54453")).findAny().isPresent());
		assertTrue(acsvr.stream().filter(x -> x.getCodingSchemeVersion().equals("0.1.5")).findAny().isPresent());
		assertTrue(acsvr.stream().filter(x -> x.getCodingSchemeURN().equals(AssertedValueSetServices.BASE + "C117743")).findAny().isPresent());
	}
	
	@Test
	public void testGetAllSourceAssertedValueSetEntities() {
		@SuppressWarnings("unchecked")
		List<Entity> entities = (List<Entity>) svc.getAllSourceAssertedValueSetEntities();
		assertNotNull(entities);
		assertTrue(entities.size() > 0);
		assertEquals(entities.size(), 6);
	}
	
	@Test
	public void testGetValueSetCodeForUri() {
		String code = ((SourceAssertedValueSetServiceImpl) svc).
		getEntityCodeFromValueSetDefinition(AssertedValueSetServices.BASE + "FDA/" + "C54453");
		assertNotNull(code);
		assertEquals(code, "C54453");
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

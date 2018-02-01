package org.LexGrid.valuesets.sourceasserted.impl;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.valuesets.sourceasserted.SourceAssertedValueSetService;
import org.lexgrid.valuesets.sourceasserted.impl.SourceAssertedValueSetServiceImpl;

import junit.framework.TestCase;

public class SourceAssertedValueSetTest extends TestCase {
	SourceAssertedValueSetService svc;
	
	@Before
	public void setUp() {
		
		AssertedValueSetParameters params = new AssertedValueSetParameters.Builder("0.1.5").
				assertedDefaultHierarchyVSRelation("Concept_In_Subset").
				codingSchemeName("owl2lexevs").
				codingSchemeURI("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl")
				.build();
		svc = SourceAssertedValueSetServiceImpl.getDefaultValueSetServiceForVersion(params);
		
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
	public void testGetSourceAssertedValueSetforEntityCode() throws LBException {
		List<CodingScheme> schemes = svc.getSourceAssertedValueSetforEntityCode("C48323");
		assertNotNull(schemes);
		assertTrue(schemes.size() > 0);
		assertEquals("Black", schemes.get(0).getCodingSchemeName());
		assertTrue(schemes.get(0).getEntities().getEntityCount() == 2);
		assertTrue(schemes.get(0).getEntities().getEntityAsReference().stream().anyMatch(x -> x.getEntityCode().equals("C99999")));	
	}
	
	@Test
	public void testGetListOfCodingSchemeVersionsUsedInResolution() throws LBException {
		List<CodingScheme> schemes = svc.getSourceAssertedValueSetforEntityCode("C48323");
		CodingScheme scheme = schemes.get(0);
		AbsoluteCodingSchemeVersionReferenceList list = svc.getListOfCodingSchemeVersionsUsedInResolution(scheme);
		assertTrue(list.getAbsoluteCodingSchemeVersionReferenceCount() == 1);
		assertTrue(list.getAbsoluteCodingSchemeVersionReference(0).getCodingSchemeURN().equals("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl"));
		assertTrue(list.getAbsoluteCodingSchemeVersionReference(0).getCodingSchemeVersion().equals("0.1.5"));
	}
	
	@Test
	public void testGetValueSetCodeForUri() {
		String code = ((SourceAssertedValueSetServiceImpl) svc).
		getEntityCodeFromValueSetDefinition(AssertedValueSetServices.BASE + "FDA/" + "C54453");
		assertNotNull(code);
		assertEquals(code, "C54453");
	}

}

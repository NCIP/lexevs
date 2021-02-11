
package org.LexGrid.valueset.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.RemoveFromDistributedTests;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.lexevs.dao.index.service.search.SourceAssertedValueSetSearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;

/**
 * JUnit for Resolved Value Set Service.
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * @author <A HREF="mailto:bauer.scott@mayo.edu">Scott Bauer</A>
 */
public class LexEVSResolvedValueSetInvalidParamsTest {
	static AssertedValueSetParameters params;
	static LexEVSResolvedValueSetService service;
	static private LexBIGService lbs;
	static SourceAssertedValueSetSearchIndexService vsSvc;

	@BeforeClass
	public static void setUp() {
		lbs = getLexBIGService();
		
		params = new AssertedValueSetParameters.Builder().
		codingSchemeTag("WRONG_TAG").
		assertedDefaultHierarchyVSRelation("Concept_In_Subset").
		codingSchemeName("owl2lexevs").
		codingSchemeURI("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl").
		rootConcept("C54453")
		.build();
		
		vsSvc = LexEvsServiceLocator.getInstance().getIndexServiceManager().getAssertedValueSetIndexService();
		vsSvc.createIndex(Constructors.createAbsoluteCodingSchemeVersionReference(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5"));
		
		service = new LexEVSResolvedValueSetServiceImpl(params);
	}

	@Test
	public void testListAllResolvedValueSetsWithWrongTag() throws Exception {
		// Test with bad coding scheme tag.  
		// This should not return any asserted value sets.
		List<CodingScheme> list = service.listAllResolvedValueSets();
		System.out.println("Resoloved VS List size: " + list.size());
		assertTrue(list.size() > 0);
		assertEquals(list.size(), 3);
		assertEquals(list.stream().
		filter(scheme -> scheme.getProperties().getPropertyAsReference().
			stream().filter(
			prop -> prop.getPropertyName().
			equals(LexEVSValueSetDefinitionServices.RESOLVED_AGAINST_CODING_SCHEME_VERSION)).
				findAny().isPresent()).count(), 3);
		
		//Source asserted value set - Should be none
		assertTrue(!list.stream().filter(scheme -> scheme.getCodingSchemeName().equals("Black")).findAny().isPresent());
		
		//Resolved value set coding scheme
		assertTrue(list.stream().filter(scheme -> scheme.getCodingSchemeName().equals("All Domestic Autos But GM")).findAny().isPresent());
		
		ResolvedConceptReferenceList refList = service.getValueSetEntitiesForURI(list.get(0).getCodingSchemeURI());

		@SuppressWarnings("unchecked")
		Iterator<ResolvedConceptReference> refs = (Iterator<ResolvedConceptReference>) refList.iterateResolvedConceptReference();
		while (refs.hasNext()) {

			ResolvedConceptReference ref = refs.next();
			System.out.println("Namespace: " + ref.getEntity().getEntityCodeNamespace());
			System.out.println("Code: " + ref.getCode());
			System.out.println("Description: " + ref.getEntityDescription().getContent());
		}
	}
			
	public static LexBIGService getLexBIGService(){
		if(lbs == null){
			lbs = LexBIGServiceImpl.defaultInstance();
		}
		return lbs;
	}
	
	public void setLexBIGService(LexBIGService lbsvc){
		lbs = lbsvc;
	}

	/**
	 * @return the service
	 */
	public LexEVSResolvedValueSetService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(LexEVSResolvedValueSetService service) {
		this.service = service;
	}
	
	@AfterClass
	 @Category(RemoveFromDistributedTests.class)
	public static void tearDown() {
		vsSvc.dropIndex(Constructors.createAbsoluteCodingSchemeVersionReference(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5"));
	}

}
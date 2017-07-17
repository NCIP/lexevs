package org.lexevs.dao.index.operation;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.NoSuchElementException;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.loaders.OWL2LoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.SourceAssertedValueSetBatchLoader;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.LexBIG.admin.Util;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.admin.RemoveAllValueSetDefinitions;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

public class SourceAssertedVSLoadTest {
	
	@Before
	public void setUp() throws LBException, InterruptedException, NoSuchElementException{

			LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

			OWL2LoaderImpl loader = (OWL2LoaderImpl) lbsm.getLoader("OWL2Loader");
	        
			loader.setLoaderPreferences(new File("resources/testData/owl2/OWLPrefsLoadAnonAsAssocPF2SetTopNodes.XML").toURI());
	        loader.load(new File("resources/testData/owl2/owl2-special-cases-Defined-Annotated.owl").toURI(),null,  1, false, true);
	        
	        while (loader.getStatus().getEndTime() == null) {
	            Thread.sleep(1000);
	        }
	        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
	        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

	        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
	        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
	        
	        SourceAssertedValueSetBatchLoader vsdbatchLoader = 
	        		new SourceAssertedValueSetBatchLoader("owl2lexevs", 
	        				"0.1.5", "Concept_In_Subset", true, "http://evs.nci.nih.gov/valueset/", "NCI", "Semantic_Type");
	        vsdbatchLoader.run("Contributing_Source");
	  
	}

	@Test
	public void testResolveValueSetDefinitionFromOWL2() throws LBException, URISyntaxException {
		
		ValueSetDefinition def = LexEVSValueSetDefinitionServicesImpl.defaultInstance().
				getValueSetDefinition(new URI("http://evs.nci.nih.gov/valueset/FDA/C54453"), null);
		
		assertNotNull(def);
		assertEquals(def.getConceptDomain(), "Intellectual Product");
		assertEquals(def.getDefaultCodingScheme(), "owl2lexevs");
		assertTrue(def.getDefinitionEntry(0).getIsActive().booleanValue());
		assertEquals(def.getDefinitionEntry(0).getOperator().compareTo(DefinitionOperator.OR), 0);
		assertEquals(def.getOwner(), "NCI");
		long l = def.getDefinitionEntry(0).getRuleOrder();
		assertEquals(l, 0L);
		assertEquals(def.getDefinitionEntry(0).getStatus(), "1");
		assertEquals(def.getValueSetDefinitionURI(), "http://evs.nci.nih.gov/valueset/FDA/C54453");
		assertEquals(def.getValueSetDefinitionName(), "Structured Product Labeling Color Terminology");
		assertEquals(def.getDefinitionEntry(0).getEntityReference().getEntityCode(), "C54453");
		assertEquals(def.getDefinitionEntry(0).getEntityReference().getEntityCodeNamespace(), "owl2lexevs");
		assertTrue(def.getDefinitionEntry(0).getEntityReference().getLeafOnly());
		assertTrue(def.getDefinitionEntry(0).getEntityReference().getTargetToSource());
		assertTrue(def.getDefinitionEntry(0).getEntityReference().isTransitiveClosure());
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);
		lbsm.deactivateCodingSchemeVersion(Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5"), null);
		lbsm.removeCodingSchemeVersion(Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5"));
		final LexEVSValueSetDefinitionServices vss = new LexEVSValueSetDefinitionServicesImpl();
		List<String> uris = vss.listValueSetDefinitionURIs();
		for (String urn : uris) {
			try {
				vss.removeValueSetDefinition(URI.create(urn));
			} catch (LBException e) {
				e.printStackTrace();
			}
			Util.displayMessage("ValueSetDefinition removed: " + urn);
		}
	}

}

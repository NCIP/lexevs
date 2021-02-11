
package org.lexevs.dao.index.operation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.NoSuchElementException;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.loaders.OWL2LoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.LexBIG.admin.Util;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;
import org.lexgrid.valuesets.sourceasserted.SourceAssertedValueSetService;
import org.lexgrid.valuesets.sourceasserted.impl.SourceAssertedValueSetServiceImpl;

import edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets.EntityToVSDTransformer;

public class SourceAssertedVSLoadTest {
	
	public static final String CODING_SCHEME_URI = "http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl";
	public static final String CODING_SCHEME = "owl2lexevs";
	public static final String VERSION = "0.1.5";
	public static final String ASSOCIATION_NAME = "Concept_In_Subset";
	private static  EntityToVSDTransformer transformer;
	static SourceAssertedValueSetService svc;

	@BeforeClass
	public static void setUp() throws LBException, InterruptedException, NoSuchElementException{

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

			transformer = new EntityToVSDTransformer(null, null, null, null, null, ASSOCIATION_NAME, null);
			
			AssertedValueSetParameters params = new AssertedValueSetParameters.Builder("0.1.5").
					assertedDefaultHierarchyVSRelation("Concept_In_Subset").
					codingSchemeName("owl2lexevs").
					codingSchemeURI("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl")
					.build();
			svc = SourceAssertedValueSetServiceImpl.getDefaultValueSetServiceForVersion(params);
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
	
	@Test
	public void testTransformerIntegration() throws LBParameterException {
		String version = transformer.getProductionVersionForCodingSchemeURI(CODING_SCHEME_URI);
		assertNotNull(version);
	}
	
	@Test
	public void testSchemeData() throws LBException, URISyntaxException {
		CodingScheme scheme = svc.getSourceAssertedValueSetForValueSetURI(new URI(AssertedValueSetServices.BASE + "C54453"));
		assertEquals(scheme, null);
		
		scheme = svc.getSourceAssertedValueSetForValueSetURI(new URI(AssertedValueSetServices.BASE + "FDA/" + "C54453"));
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
		assertTrue(roots.stream().anyMatch(x -> x.equals("C99999")));
		assertTrue(roots.stream().anyMatch(x -> x.equals("C54453")));
		assertTrue(roots.stream().anyMatch(x -> x.equals("C48323")));
		assertTrue(roots.stream().anyMatch(x -> x.equals("C48325")));
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
	public void testGetListOfCodingSchemeVersionsUsedInResolution() throws LBException {
		List<CodingScheme> schemes = svc.getSourceAssertedValueSetforTopNodeEntityCode("C48323");
		CodingScheme scheme = schemes.get(0);
		AbsoluteCodingSchemeVersionReferenceList list = svc.getListOfCodingSchemeVersionsUsedInResolution(scheme);
		assertTrue(list.getAbsoluteCodingSchemeVersionReferenceCount() == 1);
		assertTrue(list.getAbsoluteCodingSchemeVersionReference(0).getCodingSchemeURN().equals("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl"));
		assertTrue(list.getAbsoluteCodingSchemeVersionReference(0).getCodingSchemeVersion().equals("0.1.5"));
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
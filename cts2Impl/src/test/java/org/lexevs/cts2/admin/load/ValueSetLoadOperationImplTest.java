/**
 * 
 */
package org.lexevs.cts2.admin.load;

import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.valueSets.CodingSchemeReference;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexgrid.valuesets.LexEVSPickListDefinitionServices;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSPickListDefinitionServicesImpl;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * @author m004181
 *
 */
public class ValueSetLoadOperationImplTest {
	private LexEVSValueSetDefinitionServices vds_;
	private LexEVSPickListDefinitionServices pls_;
	
	/**
	 * Test method for {@link org.lexevs.cts2.admin.load.ValueSetLoadOperationImpl#load(java.net.URI, java.net.URI, java.lang.String, java.lang.Boolean)}.
	 * @throws LBException 
	 */
	@Test
	public void testLoadURIURIStringBoolean() throws LBException {
		LexEvsCTS2 cts2 = LexEvsCTS2Impl.defaultInstance();
		ValueSetLoadOperation vsLoadOp = cts2.getAdminOperation().getValueSetLoadOperation();
		
		URNVersionPair[] urns = vsLoadOp.load(new File(
						"src/test/resources/testData/valueSets/vdTestData.xml").toURI(), 
						null, "LexGrid_Loader", true);
		
		assertTrue("Number of VSD loaded : " + urns.length, urns.length == 18);	

		urns = vsLoadOp.load(new File(
						"src/test/resources/testData/valueSets/VSDOnlyTest.xml").toURI(), 
						null, "LexGrid_Loader", true);

		assertTrue("Number of VSD loaded : " + urns.length, urns.length == 1);
		
		for (URNVersionPair urn : urns) {
			assertTrue(urn.getUrn().equals("SRITEST:AUTO:PropertyRefTest1-VSDONLY"));
			assertTrue(urn.getVersion().equals("R001"));
		}

		urns = vsLoadOp.load(new File(
				"src/test/resources/testData/valueSets/pickListTestData.xml").toURI(), 
				null, "LexGrid_Loader", true);
		
		assertTrue("Number of PLD loaded : " + urns.length, urns.length == 10);
		
		urns = vsLoadOp.load(new File(
				"src/test/resources/testData/valueSets/pickListOnlyTest.xml").toURI(), 
				null, "LexGrid_Loader", true);
		
		assertTrue("Number of PLD loaded : " + urns.length, urns.length == 1);
		
		for (URNVersionPair urn : urns) {
			assertTrue(urn.getUrn().equals("SRITEST:AUTO:DomesticAutoMakers-PickListOnly"));
			assertTrue(urn.getVersion() == null);
		}
	}

	/**
	 * Test method for {@link org.lexevs.cts2.admin.load.ValueSetLoadOperationImpl#load(org.LexGrid.valueSets.ValueSetDefinition, java.net.URI, java.lang.Boolean)}.
	 * @throws LBException 
	 */
	@Test
	public void testLoadValueSetDefinitionURIBoolean() throws LBException {
		ValueSetDefinition vsd = new ValueSetDefinition();
		vsd.setValueSetDefinitionURI("SRITEST:JUNIT:TEST:VSD1");
		vsd.setValueSetDefinitionName("JUnit test vsd 1");
		vsd.setConceptDomain("cd");
		vsd.addRepresentsRealmOrContext("context");
		vsd.setDefaultCodingScheme("Automobiles");
		vsd.setIsActive(false);
		vsd.setOwner("cts2");
		List<Source> srcList = new ArrayList<Source>();
		Source src = new Source();
		src.setContent("lexevs");
		srcList.add(src);
		src = new Source();
		src.setContent("cts2");
		srcList.add(src);
		vsd.setSource(srcList);
		
		DefinitionEntry de = new DefinitionEntry();
		de.setRuleOrder(1L);
		de.setOperator(DefinitionOperator.OR);
		CodingSchemeReference csr = new CodingSchemeReference();
		csr.setCodingScheme("Automobiles");
		de.setCodingSchemeReference(csr);
		vsd.addDefinitionEntry(de);
		
		LexEvsCTS2 cts2 = LexEvsCTS2Impl.defaultInstance();
		ValueSetLoadOperation vsLoadOp = cts2.getAdminOperation().getValueSetLoadOperation();
		String vsdURI = vsLoadOp.load(vsd, null, true);
		
		assertTrue(vsdURI.equals("SRITEST:JUNIT:TEST:VSD1"));
	}
	
	@Test
	public void testRemoveAllTestValueDomains() throws LBException, URISyntaxException {
		List<String> uris = getValueSetDefinitionService().listValueSetDefinitions(null);
		assertTrue(uris.size() > 0);
		
		for (String uri : uris)
		{
			if (uri.startsWith("SRITEST:"))
				getValueSetDefinitionService().removeValueSetDefinition(new URI(uri));
		}
		
		// check if we missed any test valueDomains
		uris = getValueSetDefinitionService().listValueSetDefinitions(null);
		
		for (String uri : uris)
		{
			if (uri.toString().startsWith("SRITEST:"))
				assertTrue("Not all test value domains were deleted.",false);
		}
	}
	
	@Test
	public void testRemoveAllTestPickLists() throws LBException {
		List<String> pickListIds = getPickListService().listPickListIds();
		for (String pickListId : pickListIds)
		{
			if (pickListId.startsWith("SRITEST"))
				getPickListService().removePickList(pickListId);
		}
		
		// check if we missed any test pick lists
		pickListIds = getPickListService().listPickListIds();
		for (String pickListId : pickListIds)
		{
			if (pickListId.startsWith("SRITEST"))
				assertTrue("Not all test pick lists were deleted.", false);
		}
		
		pickListIds.clear();
	}
	
	private LexEVSValueSetDefinitionServices getValueSetDefinitionService(){
		if (vds_ == null) {
			vds_ = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
		}
		return vds_;
	}
	
	private LexEVSPickListDefinitionServices getPickListService(){
		if (pls_ == null) {
			pls_ = LexEVSPickListDefinitionServicesImpl.defaultInstance();
		}
		return pls_;
	}
}

/**
 * 
 */
package org.lexevs.cts2.admin.load;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.valueSets.CodingSchemeReference;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListEntryExclusion;
import org.LexGrid.valueSets.PickListEntryNode;
import org.LexGrid.valueSets.PickListEntryNodeChoice;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2;
import org.lexevs.cts2.LexEvsCTS2Impl;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * @author m004181
 *
 */
public class ValueSetLoadOperationImplTest {

	/**
	 * Test method for {@link org.lexevs.cts2.admin.load.ValueSetLoadOperationImpl#load(java.net.URI, java.net.URI, java.lang.String, java.lang.Boolean)}.
	 * @throws LBException 
	 */
	@Test
	public void testLoadURIURIStringBoolean() throws LBException {
		LexEvsCTS2 cts2 = LexEvsCTS2Impl.defaultInstance();
		ValueSetLoadOperation vsLoadOp = cts2.getAdminOperation().getValueSetLoadOperation();
		URNVersionPair[] urns = vsLoadOp.load(new File(
				"../lbTest/resources/testData/valueDomain/vdTestData.xml").toURI(), null, "LexGrid_Loader", true);
		for(URNVersionPair urn : urns)
		{
			System.out.println("vsd loaded : " + urn);
		}
		
		urns = vsLoadOp.load(new File(
				"../lbTest/resources/testData/valueDomain/pickListTestData.xml").toURI(), null, "LexGrid_Loader", true);
		for(URNVersionPair urn : urns)
		{
			System.out.println("vsd loaded : " + urn);
		}
	}

	/**
	 * Test method for {@link org.lexevs.cts2.admin.load.ValueSetLoadOperationImpl#load(org.LexGrid.valueSets.ValueSetDefinition, java.net.URI, java.lang.Boolean)}.
	 * @throws LBException 
	 */
	@Test
	public void testLoadValueSetDefinitionURIBoolean() throws LBException {
		ValueSetDefinition vsd = new ValueSetDefinition();
		vsd.setValueSetDefinitionURI("JUNIT:TEST:VSD1");
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
		System.out.println("vsdURI : " + vsdURI);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.admin.load.ValueSetLoadOperationImpl#load(org.LexGrid.valueSets.PickListDefinition, java.net.URI, java.lang.Boolean)}.
	 * @throws LBException 
	 */
	@Test
	public void testLoadPickListDefinitionURIBoolean() throws LBException {
		PickListDefinition pld = new PickListDefinition();
		pld.setPickListId("JUNIT PickListID 1");
		pld.setCompleteSet(true);
		pld.setRepresentsValueSetDefinition("JUNIT:TEST:VSD1");
		
		PickListEntryExclusion plExclusion = new PickListEntryExclusion();
		plExclusion.setEntityCode("005");
		plExclusion.setEntityCodeNamespace("Automobiles");
		
		PickListEntryNode entryNode = new PickListEntryNode();
		entryNode.setPickListEntryId("Junit PLENTRYNODE 1");
		PickListEntryNodeChoice choice = new PickListEntryNodeChoice();
		choice.setExclusionEntry(plExclusion);
		entryNode.setPickListEntryNodeChoice(choice);
		
		pld.addPickListEntryNode(entryNode);
		
		LexEvsCTS2 cts2 = LexEvsCTS2Impl.defaultInstance();
		ValueSetLoadOperation vsLoadOp = cts2.getAdminOperation().getValueSetLoadOperation();
		String plId = vsLoadOp.load(pld, null, true);
		System.out.println("plid loaded : " + plId);
		
	}

}

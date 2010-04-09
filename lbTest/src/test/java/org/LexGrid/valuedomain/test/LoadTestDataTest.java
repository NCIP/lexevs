/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.valuedomain.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.OBO_Loader;
import org.LexGrid.LexBIG.Impl.loaders.LexGridLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListEntry;
import org.LexGrid.valueSets.PickListEntryExclusion;
import org.LexGrid.valueSets.PickListEntryNode;
import org.LexGrid.valueSets.PickListEntryNodeChoice;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.junit.Test;
import org.lexgrid.valuesets.LexEVSPickListDefinitionServices;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSPickListDefinitionServicesImpl;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

/**
 * This set of tests loads the necessary data for the value set and pick list definition test.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LoadTestDataTest extends TestCase {
	private LexEVSValueSetDefinitionServices vds_;
	private LexEVSPickListDefinitionServices pls_;
	
	public LoadTestDataTest(String serverName) {
		super(serverName);
	}

//	public void testLoadAutombilesV1() throws LBParameterException,
//			LBInvocationException, InterruptedException, LBException {
//	    loadXML("resources/testData/valueDomain/Automobiles.xml", "devel");
//	}
//	
//	public void testLoadAutombilesV2() throws LBParameterException,
//            LBInvocationException, InterruptedException, LBException {
//	     loadXML("resources/testData/valueDomain/AutomobilesV2.xml", LBConstants.KnownTags.PRODUCTION.toString());
//	}
//
//	public void testLoadGermanMadeParts() throws LBParameterException,
//            LBInvocationException, InterruptedException, LBException {
//        loadXML("resources/testData/valueDomain/German Made Parts.xml", LBConstants.KnownTags.PRODUCTION.toString());
//    }
//	
//	private void loadXML(String fileName, String tag)throws LBException, InterruptedException {
//        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
//        .getServiceManager(null);
//
//        LexGridLoaderImpl loader = (LexGridLoaderImpl) lbsm
//                .getLoader("LexGridLoader");
//        
//        // load non-async - this should block
//        loader.load(new File(fileName).toURI(), true, false);
//        
//        while (loader.getStatus().getEndTime() == null) {
//            Thread.sleep(1000);
//        }
//        assertTrue(loader.getStatus().getEndTime() != null);
//        assertTrue(loader.getStatus().getState().getType() == ProcessState.COMPLETED_TYPE);
//        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
//        
//        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
//        
//        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], tag);
//	}
//
//	/**
//	 * @throws InterruptedException
//	 * @throws LBException
//	 */
//	public void testLoadObo() throws InterruptedException, LBException {
//		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
//				.getServiceManager(null);
//
//		OBO_Loader loader = (OBO_Loader) lbsm.getLoader("OBOLoader");
//
//		loader.load(new File(
//				"resources/testData/valueDomain/fungal_anatomy.obo").toURI(),
//				null, true, true);
//
//		while (loader.getStatus().getEndTime() == null) {
//			Thread.sleep(1000);
//		}
//		assertTrue(loader.getStatus().getState().getType() == ProcessState.COMPLETED_TYPE);
//		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
//
//		lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
//
//		lbsm.setVersionTag(loader.getCodingSchemeReferences()[0],
//				LBConstants.KnownTags.PRODUCTION.toString());
//	}
	
//	@Test
//	public void testLoadPickList() throws LBException {
//		PickListDefinition plDef = new PickListDefinition();
//		plDef.setPickListId("TESTPL1");
//		plDef.setRepresentsValueSetDefinition("TESTVSD");
//		plDef.setDefaultEntityCodeNamespace("DefaultECNS");
//		plDef.setDefaultLanguage("defaultLanguage");
//		plDef.setDefaultSortOrder("defaultSortOrder");
//		plDef.setCompleteSet(true);
//		
//		PickListEntryNode plEntryNode = new PickListEntryNode();
//		plEntryNode.setPickListEntryId("plEntryNode1");
//		PickListEntry plEntry = new PickListEntry();
//		plEntry.setEntityCode("entityCode");
//		plEntry.setPickText("pickText");
//		
//		PickListEntryNodeChoice plChoice = new PickListEntryNodeChoice();
//		plChoice.setInclusionEntry(plEntry);
//		plEntryNode.setPickListEntryNodeChoice(plChoice);
//		
//		plDef.addPickListEntryNode(plEntryNode);
//		
//		getPickListService().loadPickList(plDef, null, null);
//		
//		plDef = new PickListDefinition();
//		plDef.setPickListId("TESTPL2");
//		plDef.setRepresentsValueSetDefinition("TESTVSD 2");
//		plDef.setDefaultEntityCodeNamespace("DefaultECNS 2");
//		plDef.setDefaultLanguage("defaultLanguage 2");
//		plDef.setDefaultSortOrder("defaultSortOrder 2");
//		plDef.setCompleteSet(true);
//		
//		plEntryNode = new PickListEntryNode();
//		plEntryNode.setPickListEntryId("plEntryNode1");
//		plEntry = new PickListEntry();
//		plEntry.setEntityCode("entityCode 2");
//		plEntry.setPickText("pickText 2");
//		
//		plChoice = new PickListEntryNodeChoice();
//		plChoice.setInclusionEntry(plEntry);
//		plEntryNode.setPickListEntryNodeChoice(plChoice);
//		
//		plDef.addPickListEntryNode(plEntryNode);
//		
//		plEntryNode = new PickListEntryNode();
//		plEntryNode.setPickListEntryId("plEntryNode2");
//		PickListEntryExclusion plExclusion = new PickListEntryExclusion();
//		plExclusion.setEntityCode("excludeEntityCoded");
//		plExclusion.setEntityCodeNamespace("excludeentityCodeNamespace");
//		
//		plChoice = new PickListEntryNodeChoice();
//		plChoice.setExclusionEntry(plExclusion);
//		
//		plEntryNode.setPickListEntryNodeChoice(plChoice);
//		
//		plDef.addPickListEntryNode(plEntryNode);
//		
//		
//		getPickListService().loadPickList(plDef, null, null);
////		getPickListService().loadPickList("resources/testData/valueDomain/pickListTestData.xml", true);
//	}
	
//	@Test
//	public void testRemovePickListDefinitionByPickListId() throws LBException {
////		getPickListService().removePickList("TESTPL1");
////		getPickListService().removePickList("TESTPL2");
//	}
	
	@Test
	public void testLoadValueDomain() throws Exception {
		ValueSetDefinition vsd = new ValueSetDefinition();
		vsd.setValueSetDefinitionURI("TEST:VSD:URI");
		vsd.setValueSetDefinitionName("TESTVSDNAME");
		vsd.setConceptDomain("conceptDomain");
		vsd.setDefaultCodingScheme("defaultCodingScheme");
		vsd.setIsActive(true);
		vsd.setOwner("owner");
		
		EntityReference er = new EntityReference();
		er.setEntityCode("entityCode");
		er.setEntityCodeNamespace("entityCodeNamespace");
		er.setLeafOnly(false);
		er.setReferenceAssociation("referenceAssociation");
		er.setTargetToSource(true);
		er.setTransitiveClosure(true);
		
		DefinitionEntry de = new DefinitionEntry();
		de.setEntityReference(er);
		de.setRuleOrder(Long.valueOf("1"));
		de.setOperator(DefinitionOperator.OR);
		
		vsd.addDefinitionEntry(de);
		
		getValueDomainService().loadValueSetDefinition(vsd, "systemReleaseURI", null);
		
//		getValueDomainService().loadValueDomain("resources/testData/valueDomain/vdTestData.xml", true);
	}

	private LexEVSValueSetDefinitionServices getValueDomainService(){
		if (vds_ == null) {
			vds_ = new LexEVSValueSetDefinitionServicesImpl();
		}
		return vds_;
	}
	
	private LexEVSPickListDefinitionServices getPickListService(){
		if (pls_ == null) {
			pls_ = new LexEVSPickListDefinitionServicesImpl();
		}
		return pls_;
	}
}
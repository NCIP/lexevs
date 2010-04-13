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
import java.net.URI;
import java.net.URISyntaxException;
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
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListEntry;
import org.LexGrid.valueSets.PickListEntryExclusion;
import org.LexGrid.valueSets.PickListEntryNode;
import org.LexGrid.valueSets.PickListEntryNodeChoice;
import org.LexGrid.valueSets.PropertyMatchValue;
import org.LexGrid.valueSets.PropertyReference;
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
//		Properties properties = new Properties();
//		Property property = new Property();
//		property.setPropertyId("TESTPL1 - PROPERTYID");
//		property.setPropertyName("TESTPL1 - PROPERTYNAME");
//		property.setOwner("TESTPL1 - owner");
//		Text text = new Text();
//		text.setContent("TESTPL1 - PROPERTYVALUE");
//		text.setDataType("TESTPL1 - DATATYPE");
//		property.setValue(text);
//		property.setIsActive(true);
//		property.setStatus("TESTPL1 - status");
//		
//		PropertyQualifier propertyQualifier = new PropertyQualifier();
//		propertyQualifier.setPropertyQualifierName("TESTPL1 - propertyQualifierName");
//		propertyQualifier.setPropertyQualifierType("TESTPL1 - propertyQualifierType");
//		text = new Text();
//		text.setContent("TESTPL1 - PROPQUAL VALUE");
//		text.setDataType("TSTPL1 - PROPQUAL datatype");
//		propertyQualifier.setValue(text);
//		property.addPropertyQualifier(propertyQualifier);
//		
//		List<Source> sources = new ArrayList<Source>();
//		Source source = new Source();
//		source.setContent("TESTPL1 - Property source");
//		source.setRole("TESTPL1 - PROPERTY ROLE");
//		source.setSubRef("TESTPL1 - PROPERTY subrEF");
//		sources.add(source);
//		property.setSource(sources);
//		
//		properties.addProperty(property);
//		
//		plDef.setProperties(properties);
//		
//		sources = new ArrayList<Source>();
//		source = new Source();
//		source.setContent("TESTPL1 - source");
//		source.setRole("TESTPL1 - ROLE");
//		source.setSubRef("TESTPL1 - subrEF");
//		sources.add(source);
//		plDef.setSource(sources);
//		
//		List<String> pickContext = new ArrayList<String>();
//		pickContext.add("plContext 1");
//		pickContext.add("plContext 2");
//		pickContext.add("plContext 3");
//		plDef.setDefaultPickContext(pickContext);
//		
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
//		properties = new Properties();
//		property = new Property();
//		property.setPropertyId("plEntryNode1 - PROPERTYID");
//		property.setPropertyName("plEntryNode1 - PROPERTYNAME");
//		property.setOwner("plEntryNode1 - owner");
//		text = new Text();
//		text.setContent("plEntryNode1 - PROPERTYVALUE");
//		text.setDataType("plEntryNode1 - DATATYPE");
//		property.setValue(text);
//		property.setIsActive(true);
//		property.setStatus("plEntryNode1 - status");
//		properties.addProperty(property);
//		plEntryNode.setProperties(properties);
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
//		getPickListService().removePickList("TESTPL1");
//		getPickListService().removePickList("TESTPL2");
//	}
//	
	@Test
	public void testRemoveValueSetDefinitionByURI() throws LBException {
		try {
			getValueDomainService().removeValueSetDefinition(new URI("TEST:VSD:URI"));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	@Test
//	public void testLoadValueDomain() throws Exception {
//		ValueSetDefinition vsd = new ValueSetDefinition();
//		vsd.setValueSetDefinitionURI("TEST:VSD:URI");
//		vsd.setValueSetDefinitionName("TESTVSDNAME");
//		vsd.setConceptDomain("conceptDomain");
//		vsd.setDefaultCodingScheme("defaultCodingScheme");
//		vsd.setIsActive(true);
//		vsd.setOwner("owner");
//		
//		List<Source> srcList = new ArrayList<Source>();
//		Source src = new Source();
//		src.setContent("TESTVSD1 - content 1");
//		src.setRole("testvsd1 - role 1");
//		src.setSubRef("testvsd1 - subRef 1");
//		srcList.add(src);
//		
//		src = new Source();
//		src.setContent("TESTVSD1 - content 2");
//		src.setRole("testvsd1 - role 2");
//		src.setSubRef("testvsd1 - subRef 2");
//		srcList.add(src);
//		
//		vsd.setSource(srcList);
//		
//		List<String> context = new ArrayList<String>();
//		context.add("TESTVSD1 - Context 1");
//		context.add("TESTVSD1 - Context 2");
//		context.add("TESTVSD1 - Context 3");
//		vsd.setRepresentsRealmOrContext(context);		
//		
//		Property prop = new Property();
//		prop.setPropertyId("vsd1propertyId");
//		prop.setPropertyName("propertyName");
//		prop.setIsActive(true);
//		prop.setLanguage("language");
//		prop.setOwner("owner");
//		prop.setStatus("status");
//		Text text = new Text();
//		text.setContent("propValue");
//		text.setDataType("dataType");
//		prop.setValue(text);
//		
//		Properties props = new Properties();
//		
//		props.addProperty(prop);
//		vsd.setProperties(props);
//		
//		EntityReference er = new EntityReference();
//		er.setEntityCode("entityCode");
//		er.setEntityCodeNamespace("entityCodeNamespace");
//		er.setLeafOnly(false);
//		er.setReferenceAssociation("referenceAssociation");
//		er.setTargetToSource(true);
//		er.setTransitiveClosure(true);
//		
//		DefinitionEntry de = new DefinitionEntry();
//		de.setEntityReference(er);
//		de.setRuleOrder(Long.valueOf("1"));
//		de.setOperator(DefinitionOperator.OR);
//		
//		vsd.addDefinitionEntry(de);
//		
//		PropertyReference pr = new PropertyReference();
//		PropertyMatchValue pmv = new PropertyMatchValue();
//		pr.setCodingScheme("codingScheme");
//		pr.setPropertyName("propertyName");
//		pmv.setContent("propertyMatchvalueString");
//		pmv.setDataType("PMVdataType");
//		pmv.setMatchAlgorithm("matchAlgorithm used");
//		pr.setPropertyMatchValue(pmv);
//		
//		de = new DefinitionEntry();
//		de.setRuleOrder(Long.valueOf("2"));
//		de.setOperator(DefinitionOperator.OR);
//		de.setPropertyReference(pr);
//		
//		vsd.addDefinitionEntry(de);
//		
//		
//		Mappings mappings = new Mappings();
//		SupportedAssociation sa = new SupportedAssociation();
//		sa.setUri("sauri");
//		sa.setLocalId("sa id");
//		sa.setContent("sa content");
//		mappings.addSupportedAssociation(sa);
//		
//		SupportedCodingScheme scs = new SupportedCodingScheme();
//		scs.setContent("scs content");
//		scs.setIsImported(false);
//		scs.setLocalId("scs localId");
//		scs.setUri("scs uri");
//		mappings.addSupportedCodingScheme(scs);
//		
//		SupportedNamespace sn = new SupportedNamespace();
//		sn.setContent("sn content");
//		sn.setEquivalentCodingScheme("sn equivalentCodingScheme");
//		sn.setLocalId("sn localId");
//		sn.setUri("sn uri");
//		mappings.addSupportedNamespace(sn);
//		
//		vsd.setMappings(mappings);
//		getValueDomainService().loadValueSetDefinition(vsd, "systemReleaseURI", null);
//		
////		getValueDomainService().loadValueDomain("resources/testData/valueDomain/vdTestData.xml", true);
//	}

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
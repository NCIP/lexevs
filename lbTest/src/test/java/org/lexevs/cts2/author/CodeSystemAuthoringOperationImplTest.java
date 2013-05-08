/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.lexevs.cts2.author;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedLanguage;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.admin.load.CodeSystemLoadOperation;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.cts2.test.Cts2BaseTest;
import org.lexevs.cts2.test.Cts2TestConstants;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;

public class CodeSystemAuthoringOperationImplTest extends Cts2BaseTest {

private volatile List<String> revIds_ = new ArrayList<String>();
	
	private void loadAutomobiles() throws Exception {
		CodeSystemLoadOperation csLoadOp = LexEvsCTS2Impl.defaultInstance().getAdminOperation().getCodeSystemLoadOperation();
		
		try {
			csLoadOp.load(new File("resources/testData/cts2/Cts2Automobiles.xml").toURI(), null, null, "LexGrid_Loader", true, true, true, "DEV", true);
		} catch (LBException e) {
			e.printStackTrace();
		}
	}
	
	private void removeAutomobiles() throws Exception {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		AbsoluteCodingSchemeVersionReference ref = 
			Constructors.createAbsoluteCodingSchemeVersionReference(Cts2TestConstants.CTS2_AUTOMOBILES_URI, Cts2TestConstants.CTS2_AUTOMOBILES_VERSION);
		
		lbs.getServiceManager(null).deactivateCodingSchemeVersion(ref, null);
		
		lbs.getServiceManager(null).removeCodingSchemeVersion(ref);
		
		AuthoringService authServ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
		
		for(String id : this.revIds_) {
			authServ.removeRevisionRecordbyId(id);
		}
	}
	
	@Test
	public void testCreateCodeSystem()   throws LBException, URISyntaxException{
		this.createCodeSystem();
		this.removeCodeSystem();
	}
	

	private void createCodeSystem()   throws LBException, URISyntaxException{
		
		String randomID = getRevId();
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId(randomID);
		
		String codingSchemeURI = Cts2TestConstants.CTS2_CREATE_URI;
		String representsVersion = Cts2TestConstants.CTS2_CREATE_VERSION;
		
		String codingSchemeName = "New Coding Scheme";
	    String formalName = "CTS 2 API Created Code System";
	    String defaultLanguage = "";
	    Long approxNumConcepts = new Long(1);
	    List<String> localNameList = Arrays.asList(); 
	    
	    Source source = new Source();
	    source.setContent("source");
	    List<Source> sourceList = Arrays.asList(source);
	    
	    Text copyright = new Text();
	    Mappings mappings = new Mappings();
	    
	    
	    CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
		
		CodingScheme codeScheme = codeSystemAuthOp.createCodeSystem(revInfo, codingSchemeName, codingSchemeURI, formalName, defaultLanguage, approxNumConcepts, representsVersion, localNameList, sourceList, copyright, mappings);
		
		assertEquals(Cts2TestConstants.CTS2_CREATE_URI,codeScheme.getCodingSchemeURI());
	}

	@Test
		public void testUpdateCodeSystem()   throws LBException, URISyntaxException{
			this.createCodeSystem();
			
			String randomID = getRevId();
		
			RevisionInfo revInfo = new RevisionInfo();
			revInfo.setChangeAgent("changeAgent");
			revInfo.setChangeInstruction("changeInstruction");
			revInfo.setDescription("new description");
			revInfo.setEditOrder(1L);
			revInfo.setRevisionDate(new Date());
			revInfo.setRevisionId(randomID);				        
	        
	        String codingSchemeURI = Cts2TestConstants.CTS2_CREATE_URI;
			String representsVersion = Cts2TestConstants.CTS2_CREATE_VERSION;
	        
			String codingSchemeName = "New Coding Scheme - Updated";
	        String formalName = "CTS 2 API Created Code System - Updated";
	        String defaultLanguage = "eng";
	        Long approxNumConcepts = 0L;
	        
	        List<String> localNameList = Arrays.asList("local name"); 
	        
	    	Source source = new Source();
	    	source.setContent("source");
	    	List<Source> sourceList = Arrays.asList(source);
	        
	        Text copyright = new Text();
	        copyright.setContent("CTS 2 API");
	        
	        Mappings mappings = new Mappings();
	        SupportedLanguage csSupportedLanguage = new SupportedLanguage();
	        csSupportedLanguage.setContent("eng");
	        csSupportedLanguage.setLocalId("langLocalId");
	        mappings.addSupportedLanguage(csSupportedLanguage);	        	        
	        
	        CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
	               
	        CodingScheme codeScheme = codeSystemAuthOp.updateCodeSystem(revInfo, 
	        															codingSchemeName, 
	        															codingSchemeURI, 
	        															formalName, 
	        															defaultLanguage, 
	        															approxNumConcepts, 
	        															representsVersion, 
	        															localNameList, 
	        															sourceList, 
	        															copyright, 
	        															mappings 
	        															);
	        
	
	        assertEquals("New Coding Scheme - Updated", codeScheme.getCodingSchemeName());
	        assertEquals("CTS 2 API Created Code System - Updated", codeScheme.getFormalName());
	        assertEquals("eng", codeScheme.getDefaultLanguage());
	        assertEquals("CTS 2 API", codeScheme.getCopyright().getContent());
	        assertEquals(1, codeScheme.getMappings().getSupportedLanguageCount());
	        assertEquals(1, codeScheme.getLocalNameCount());
	        assertEquals(1, codeScheme.getSourceCount());
	        
	        this.removeCodeSystem();
		}

	@Test
		public void testUpdateCodeSystemMappings()   throws LBException, URISyntaxException{
		this.createCodeSystem();
		
		
			String randomID = getRevId();
			
			RevisionInfo revInfo = new RevisionInfo();
			revInfo.setChangeAgent("changeAgent");
			revInfo.setChangeInstruction("changeInstruction - update mappings");
			revInfo.setDescription("new description");
			revInfo.setEditOrder(1L);
			revInfo.setRevisionDate(new Date());
			revInfo.setRevisionId(randomID);
			

	        Mappings mappings = new Mappings();
	        SupportedCodingScheme scScheme = new SupportedCodingScheme();
	        scScheme.setLocalId("localId");
	        scScheme.setContent("supported coding Scheme");
	        mappings.addSupportedCodingScheme(scScheme);
	        	        
	        String codingSchemeURI = Cts2TestConstants.CTS2_CREATE_URI;
			String representsVersion = Cts2TestConstants.CTS2_CREATE_VERSION;
	        
			String codingSchemeName = null;
	        String formalName = null;
	        String defaultLanguage = null;
	        Long approxNumConcepts = 0L;
	        List<String> localNameList = null; 
	        List<Source> sourceList = null;
	        Text copyright = null;	        
	        
	        CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
	               
	        CodingScheme codeScheme = codeSystemAuthOp.updateCodeSystem(revInfo, 
	        															codingSchemeName, 
	        															codingSchemeURI, 
	        															formalName, 
	        															defaultLanguage, 
	        															approxNumConcepts, 
	        															representsVersion, 
	        															localNameList, 
	        															sourceList, 
	        															copyright, 
	        															mappings 
	        															);
	        
			
			assertEquals(1, codeScheme.getMappings().getSupportedCodingSchemeCount());			
	
			this.removeCodeSystem();
	}

	@Test
	public void testAddCodeSystemProperty()   throws LBException, URISyntaxException{
		this.createCodeSystem();
		this.addCodeSystemProperty();
		this.removeCodeSystem();
	}
	

	private void addCodeSystemProperty()   throws LBException, URISyntaxException{
		
		String randomID = getRevId();
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("new description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId(randomID);
	    
	    String codingSchemeURI = Cts2TestConstants.CTS2_CREATE_URI;
		String representsVersion = Cts2TestConstants.CTS2_CREATE_VERSION;
	    
		String codingSchemeName = null;
	
	    
	    Properties properties = new Properties();
	    
	    Presentation prop = new Presentation();
		prop.setPropertyId("propertyId1");
		prop.setPropertyName("propertyName");
		prop.setIsActive(false);
		prop.setLanguage("english");
		prop.setOwner("owner");
		prop.setPropertyType(PropertyTypes.PROPERTY.name());
		Text text = new Text();
		text.setContent("content");
		text.setDataType("Text datatype");
		prop.setValue(text);
		prop.setDegreeOfFidelity("degreeOfFidelity");
		prop.setMatchIfNoContext(true);
		prop.setRepresentationalForm("representationalForm");
		
		properties.addProperty(prop);
		
		Presentation prop2 = new Presentation();
		prop2.setPropertyId("propertyId2");
		prop2.setPropertyName("propertyName-2");
		prop2.setIsActive(false);
		prop2.setLanguage("english-2");
		prop2.setOwner("owner-2");
		prop2.setPropertyType(PropertyTypes.PROPERTY.name());
		Text text2 = new Text();
		text2.setContent("content-2");
		text2.setDataType("Text datatype-2");
		prop2.setValue(text2);
		prop2.setDegreeOfFidelity("degreeOfFidelity-2");
		prop2.setMatchIfNoContext(true);
		prop2.setRepresentationalForm("representationalForm-2");
		
		properties.addProperty(prop2);
		
	    CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
	           
	    CodingScheme codeScheme = codeSystemAuthOp.addCodeSystemProperties (revInfo, 
	    															codingSchemeName, 
	    															codingSchemeURI,  
	    															representsVersion, 
	    															properties);
	    
		
		assertEquals(2, codeScheme.getProperties().getPropertyCount());
	}
	
	//@Test
	public void testUpdateCodeSystemProperty()   throws LBException, URISyntaxException{
		this.createCodeSystem();
		
		String randomID = getRevId();
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("new description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId(randomID);
	    
	    
	    String codingSchemeURI = Cts2TestConstants.CTS2_CREATE_URI;
		String representsVersion = Cts2TestConstants.CTS2_CREATE_VERSION;
	    
		String codingSchemeName = null;
	
	    
	    Properties properties = new Properties();
	    
	    Presentation prop = new Presentation();
	
		prop.setPropertyId("propertyId1");
		prop.setPropertyName("propertyName");
		
		prop.setIsActive(false);
		prop.setLanguage("english - update");
		prop.setOwner("owner- update");
		prop.setPropertyType(PropertyTypes.PROPERTY.name());
		Text text = new Text();
		text.setContent("content - update");
		text.setDataType("Text datatype");
		prop.setValue(text);
		prop.setDegreeOfFidelity("degreeOfFidelity - update");
		prop.setMatchIfNoContext(true);
		prop.setRepresentationalForm("representationalForm - update");
		
		properties.addProperty(prop);
		
	    
	    CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
	           
	    CodingScheme codeScheme = codeSystemAuthOp.updateCodeSystemProperties (revInfo, 
	    															codingSchemeName, 
	    															codingSchemeURI,  
	    															representsVersion, 
	    															properties);
	   
				
		// Check updated values for property
		Property csCurrentProperty = null;
				
		for (Property csProp : codeScheme.getProperties().getPropertyAsReference())
		{
			if (csProp.getPropertyId().equalsIgnoreCase("propertyId1"))
				csCurrentProperty = csProp;
		}
		
		assertNotNull("Property Not found.", csCurrentProperty);
		
		assertEquals("propertyId1", csCurrentProperty.getPropertyId());
		assertEquals("propertyName", csCurrentProperty.getPropertyName());
		assertEquals("english - update", csCurrentProperty.getLanguage());		
	
		this.removeCodeSystem();
	}

	@Test
	public void testRemoveCodeSystemProperty()   throws LBException, URISyntaxException{
		this.createCodeSystem();
		this.addCodeSystemProperty();
		
		
		String randomID = getRevId();
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("new description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId(randomID);
        
        
        String codingSchemeURI = Cts2TestConstants.CTS2_CREATE_URI;
		String representsVersion = Cts2TestConstants.CTS2_CREATE_VERSION;
        
        String propertyId = "propertyId2";
        
        
        CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
               
        CodingScheme codeScheme = codeSystemAuthOp.removeCodeSystemProperty (revInfo, 
        															codingSchemeURI,  
        															representsVersion, 
        															propertyId);
        
		

        
		Property currentProperty = null;
		
		for (Property csProp : codeScheme.getProperties().getPropertyAsReference())
		{
			if (csProp.getPropertyId().equalsIgnoreCase("propertyId2"))
				currentProperty = csProp;
		}
		
		assertNull(currentProperty);
		
		this.removeCodeSystem();
	}

	@Test
	public void testUpdateCodeSystemVersionStatus()   throws LBException, URISyntaxException{
		this.createCodeSystem();
		
		String randomID = getRevId();
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("Description - Update status.");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId(randomID);
		
        
        String codingSchemeURI = Cts2TestConstants.CTS2_CREATE_URI;
		String representsVersion = Cts2TestConstants.CTS2_CREATE_VERSION;
        
        String status = "test status";
        boolean isActive = true;
     
        
        
        CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
               
        try {
        	codeSystemAuthOp.updateCodeSystemVersionStatus(codingSchemeURI, representsVersion, status, isActive, revInfo);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 


        CodingScheme updatedCodingScheme = 
        	LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodingSchemeService().getCompleteCodingScheme(codingSchemeURI, representsVersion);
        		
		
        assertTrue(updatedCodingScheme.getIsActive());
        
        this.removeCodeSystem();
        
	}

	@Test
	public void testRemoveCodeSystem()   throws LBException, URISyntaxException{
		this.createCodeSystem();
		this.removeCodeSystem();
	}
	
	private void removeCodeSystem()   throws LBException, URISyntaxException{
		
		String randomID = getRevId();
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId(randomID);
		
	    
	    String codingSchemeURI = Cts2TestConstants.CTS2_CREATE_URI;
		String representsVersion = Cts2TestConstants.CTS2_CREATE_VERSION;
		
		Boolean removeStatus = false;
	    
	    CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
	
	    try {
			removeStatus = codeSystemAuthOp.removeCodeSystem(revInfo, codingSchemeURI, representsVersion);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
		assertTrue(removeStatus);
		
		AuthoringService authServ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
		
		for(String id : this.revIds_) {
			authServ.removeRevisionRecordbyId(id);
		}
	}

	@Test
	public void testUpdateConcept() throws Exception {
		this.loadAutomobiles();
		
		CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
        RevisionInfo info = new RevisionInfo();
        info.setRevisionId(getRevId());
        
		Entity entityToUpdate = new Entity();
		entityToUpdate.setEntityCode("005");
		entityToUpdate.setEntityCodeNamespace(Cts2TestConstants.CTS2_AUTOMOBILES_NAME);
		entityToUpdate.setEntityDescription(Constructors.createEntityDescription("Modified ED"));
		
		codeSystemAuthOp.updateConcept(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI, 
				Cts2TestConstants.CTS2_AUTOMOBILES_VERSION, 
				entityToUpdate, 
				info);
		
		CodedNodeSet cns = super.getLexBIGService().getNodeSet(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI,
				null, 
				null);
		
		ResolvedConceptReferenceList refList = cns.restrictToCodes(Constructors.createConceptReferenceList("005")).resolveToList(null, null, null, -1);
	
		assertEquals(1,refList.getResolvedConceptReferenceCount());
		
		ResolvedConceptReference ref = refList.getResolvedConceptReference(0);
		
		assertEquals("Modified ED",ref.getEntityDescription().getContent());
		
		this.removeAutomobiles();
	}
	
	@Test
	public void testUpdateProperty() throws Exception {
		this.loadAutomobiles();
		
		CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
        RevisionInfo info = new RevisionInfo();
        info.setRevisionId(getRevId());
        
		Property propertyToUpdate = new Property();
		propertyToUpdate.setPropertyName("textualPresentation");
		propertyToUpdate.setPropertyId("p1");
		propertyToUpdate.setValue(Constructors.createText("Modded text"));
		
		codeSystemAuthOp.updateConceptProperty(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI, 
				Cts2TestConstants.CTS2_AUTOMOBILES_VERSION, 
				"005",
				Cts2TestConstants.CTS2_AUTOMOBILES_NAME,
				propertyToUpdate, 
				info);
		
		CodedNodeSet cns = super.getLexBIGService().getNodeSet(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI,
				null, 
				null);
		
		ResolvedConceptReferenceList refList = cns.restrictToCodes(Constructors.createConceptReferenceList("005")).resolveToList(null, null, null, -1);
	
		assertEquals(1,refList.getResolvedConceptReferenceCount());
		
		ResolvedConceptReference ref = refList.getResolvedConceptReference(0);
		
		Property foundProp = getPropertyWithId(ref.getEntity().getAllProperties(), "p1");
		
		assertEquals("Modded text",foundProp.getValue().getContent());
		
		this.removeAutomobiles();
	}
	
	@Test
	public void testRemoveProperty() throws Exception {
		this.loadAutomobiles();
		
		CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
        RevisionInfo info = new RevisionInfo();
        info.setRevisionId(getRevId());
        
		Property propertyToUpdate = new Property();
		propertyToUpdate.setPropertyName("textualPresentation");
		propertyToUpdate.setPropertyId("p1");
		
		codeSystemAuthOp.deleteConceptProperty(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI, 
				Cts2TestConstants.CTS2_AUTOMOBILES_VERSION, 
				"005",
				Cts2TestConstants.CTS2_AUTOMOBILES_NAME,
				propertyToUpdate, 
				info);
		
		CodedNodeSet cns = super.getLexBIGService().getNodeSet(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI,
				null, 
				null);
		
		ResolvedConceptReferenceList refList = cns.restrictToCodes(Constructors.createConceptReferenceList("005")).resolveToList(null, null, null, -1);
	
		assertEquals(1,refList.getResolvedConceptReferenceCount());
		
		Property foundProp = null;
		
		try {
			foundProp = getPropertyWithId(refList
					.getResolvedConceptReference(0).getEntity()
					.getAllProperties(), "p1");
		} catch (RuntimeException e) {
			// TODO: handle exception
		}
		assertNull(foundProp);
		
		this.removeAutomobiles();
	}
	
	@Test
	public void testUpdateConceptStatus() throws Exception {
		this.loadAutomobiles();
		
		CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
        RevisionInfo info = new RevisionInfo();
        info.setRevisionId(getRevId());
        
		Entity entityToUpdate = new Entity();
		entityToUpdate.setEntityCode("005");
		entityToUpdate.setEntityCodeNamespace(Cts2TestConstants.CTS2_AUTOMOBILES_NAME);
		
		codeSystemAuthOp.updateConceptStatus(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI, 
				Cts2TestConstants.CTS2_AUTOMOBILES_VERSION, 
				"005",
				Cts2TestConstants.CTS2_AUTOMOBILES_NAME,
				"Modified",
				false,
				info);
		
		CodedNodeSet cns = super.getLexBIGService().getNodeSet(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI,
				null, 
				null);
		
		ResolvedConceptReferenceList refList = cns.restrictToCodes(Constructors.createConceptReferenceList("005")).resolveToList(null, null, null, -1);
	
		assertEquals(1,refList.getResolvedConceptReferenceCount());
		
		ResolvedConceptReference ref = refList.getResolvedConceptReference(0);
		
		assertEquals(false,ref.getEntity().isIsActive());
		assertEquals("Modified",ref.getEntity().getStatus());
		
		this.removeAutomobiles();
	}
	
	@Test
	public void testRemoveConcept() throws Exception {
		this.loadAutomobiles();
		
		CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
        RevisionInfo info = new RevisionInfo();
        info.setRevisionId(getRevId());
        
		Entity entityToUpdate = new Entity();
		entityToUpdate.setEntityCode("005");
		entityToUpdate.setEntityCodeNamespace(Cts2TestConstants.CTS2_AUTOMOBILES_NAME);
		
		codeSystemAuthOp.deleteConcept(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI, 
				Cts2TestConstants.CTS2_AUTOMOBILES_VERSION, 
				"005",
				Cts2TestConstants.CTS2_AUTOMOBILES_NAME,
				info);
		
		CodedNodeSet cns = super.getLexBIGService().getNodeSet(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI,
				null, 
				null);
		
		ResolvedConceptReferenceList refList = cns.restrictToCodes(Constructors.createConceptReferenceList("005")).resolveToList(null, null, null, -1);
	
		// since code '005' is used in associations, it won't be deleted.
		assertEquals(1, refList.getResolvedConceptReferenceCount());
		

		info = new RevisionInfo();
        info.setRevisionId(getRevId());
        
		entityToUpdate = new Entity();
		entityToUpdate.setEntityCode("Anonymous-mobile");
		entityToUpdate.setEntityCodeNamespace(Cts2TestConstants.CTS2_AUTOMOBILES_NAME);
		
		codeSystemAuthOp.deleteConcept(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI, 
				Cts2TestConstants.CTS2_AUTOMOBILES_VERSION, 
				"Anonymous-mobile",
				Cts2TestConstants.CTS2_AUTOMOBILES_NAME,
				info);
		
		cns = super.getLexBIGService().getNodeSet(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI,
				null, 
				null);
		
		refList = cns.restrictToCodes(Constructors.createConceptReferenceList("Anonymous-mobile")).resolveToList(null, null, null, -1);
	
		// since code 'Anonymous-mobile' is not used anywhere else in the code system, it should be deleted.
		assertEquals(0, refList.getResolvedConceptReferenceCount());
		
		this.removeAutomobiles();
	}

	private String getRevId(){
		String revId = UUID.randomUUID().toString();
		revIds_.add(revId);
		
		return revId;
	}
	
	private static Property getPropertyWithId(Property[] properties, String id) {
		for(Property prop : properties){
			if(prop.getPropertyId().equals(id)){
				return prop;
			}
		}
		
		throw new RuntimeException("Property Not Found.");
	}
}
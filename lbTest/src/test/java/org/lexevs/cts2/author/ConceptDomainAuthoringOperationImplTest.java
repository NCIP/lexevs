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

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.admin.load.CodeSystemLoadOperation;
import org.lexevs.cts2.admin.load.ValueSetLoadOperation;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.cts2.query.ConceptDomainQueryOperation;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.conceptdomain.util.ConceptDomainConstants;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

/**
 * @author m004181
 *
 */
public class ConceptDomainAuthoringOperationImplTest {

	private static ConceptDomainAuthoringOperation CD_AUTH_OP;
	private static ConceptDomainQueryOperation CD_QUERY_OP;
	
	private static List<String> revIds_ = new ArrayList<String>();
	private static LexEVSValueSetDefinitionServices vds_ = null;
	
	@BeforeClass
	public static void testSetUp() throws Exception {
		CD_AUTH_OP = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getConceptDomainAuthoringOperation();
		CD_QUERY_OP = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getConceptDomainQueryOperation();
		vds_ = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
		
		ValueSetLoadOperation vsLoadOp = LexEvsCTS2Impl.defaultInstance().getAdminOperation().getValueSetLoadOperation();
		try {
			vsLoadOp.load(new File("resources/testData/cts2/valueSets/VSDOnlyTest.xml").toURI(), null, "LexGrid_Loader", true);
		} catch (LBException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testCreateConceptDomainCodeSystem() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		Mappings maps = new Mappings();
		SupportedCodingScheme scs = new SupportedCodingScheme();
		scs.setContent(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME);
		scs.setUri(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI);
		scs.setLocalId(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME);
		scs.setIsImported(false);
		
		maps.addSupportedCodingScheme(scs);
		
		CD_AUTH_OP.createConceptDomainCodeSystem(rev, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				"en", 0, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, null, null, null, maps);
		
		CodeSystemLoadOperation csLoadOp = LexEvsCTS2Impl.defaultInstance().getAdminOperation().getCodeSystemLoadOperation();
		csLoadOp.activateCodeSystem(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
	}
	
	@Test
	public void testCreateConceptDomainFromFile() throws LBException, IOException {
		RevisionInfo rev = new RevisionInfo();
		
		File file = new File("resources/testData/cts2/ConceptDomainTestData.txt");
		 
		BufferedReader bufRdr  = new BufferedReader(new FileReader(file));
		String line = null;
		int row = 0;
		int col = 0;
		 
		List<ConceptDomainData> cdDatas = new ArrayList<ConceptDomainData>();
		ConceptDomainData cdData = null;
		//read each line of text file
		
		while((line = bufRdr.readLine()) != null && row <= 10)
		{
			if (line.startsWith("#")) // ignore the comment record
				continue;
			
			StringTokenizer st = new StringTokenizer(line,"|");
			cdData = new ConceptDomainData();
			col = 1;
			while (st.hasMoreTokens())
			{
				//get next token and store it in the array
				switch(col)
				{
					case 1 : cdData.id = st.nextToken();break;
					case 2 : cdData.name = st.nextToken();break;
					case 3 : cdData.description = st.nextToken();break;
				}
				col++;
			}
			cdDatas.add(cdData);
			row++;
		}
		 
		//close the file
		bufRdr.close();
		for (ConceptDomainData cd : cdDatas)
		{
			rev.setRevisionId(getRevId());
			
			CD_AUTH_OP.createConceptDomain(cd.id, cd.name, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, rev, cd.description, "testing", 
					false, null, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		}
		
		cdDatas = null;
	}
	
	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#createConceptDomain(java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo, java.lang.String, java.lang.String, boolean, org.LexGrid.commonTypes.Properties, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 * @throws LBException 
	 * @throws IOException 
	 */
	@Test
	public void testCreateConceptDomain() throws LBException, IOException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		Properties props = new Properties();
		Property prop = new Property();
		prop.setPropertyId("cd1propId1");
		Text value = new Text();
		value.setContent("cd1content");
		prop.setValue(value);
		prop.setPropertyName("cd1propertyName");
		props.addProperty(prop);
		
		CD_AUTH_OP.createConceptDomain("cdunitest1", "cd unit test 1", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, rev, "cd unit test 1", "testing", 
				false, props, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		
		props = new Properties();
		prop = new Property();
		prop.setPropertyId("cd2propId1");
		value = new Text();
		value.setContent("cd2content");
		prop.setValue(value);
		prop.setPropertyName("cd2propertyName");
		prop.setIsActive(true);
		prop.setOwner("owner test");
		props.addProperty(prop);
		
		
		rev.setRevisionId(getRevId());
		
		CD_AUTH_OP.createConceptDomain("cdunitest2", "cd unit test 2", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, rev, "cd unit test 2", "testing", 
				false, props, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		
		Entity cd = CD_QUERY_OP.getConceptDomainEntity("cdunitest2", "cd unit test 2", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(cd.getEntityCode().equals("cdunitest2"));
	}
	
	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#updateConceptDomainStatus(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void testUpdateConceptDomainStatus() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		
		String revId = getRevId();
		rev.setRevisionId(revId);
		
		CD_AUTH_OP.updateConceptDomainStatus("cdunitest1", null, "New Status " + revId, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
		

		Entity cd = CD_QUERY_OP.getConceptDomainEntity("cdunitest1", "cd unit test 2", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(cd.getEntityCode().equals("cdunitest1"));
		assertTrue(cd.getStatus().equals("New Status " + revId));
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#activateConceptDomain(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void testActivateConceptDomain() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		CD_AUTH_OP.activateConceptDomain("cdunitest1", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
		
		Entity cd = CD_QUERY_OP.getConceptDomainEntity("cdunitest1", "cd unit test 2", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(cd.getEntityCode().equals("cdunitest1"));
		assertTrue(cd.getIsActive());		
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#deactivateConceptDomain(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void testDeactivateConceptDomain() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		assertTrue(CD_AUTH_OP.deactivateConceptDomain("cdunitest1", null, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev));
		
		Entity cd = CD_QUERY_OP.getConceptDomainEntity("cdunitest1", "cd unit test 2", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(cd.getEntityCode().equals("cdunitest1"));
		assertTrue(!cd.getIsActive());		
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#updateConceptDomainVersionable(java.lang.String, org.LexGrid.commonTypes.Versionable, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void testUpdateConceptDomainVersionable() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		String revId = getRevId();
		rev.setRevisionId(revId);
		
		Versionable changedVersionable = new Versionable();
		changedVersionable.setEffectiveDate(new Date());
		changedVersionable.setIsActive(true);
		changedVersionable.setOwner("new Owner - " + revId);
		changedVersionable.setStatus("new status - " + revId);
		
		assertTrue(CD_AUTH_OP.updateConceptDomainVersionable("cdunitest1", null, changedVersionable, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev));
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#addConceptDomainProperty(java.lang.String, org.LexGrid.commonTypes.Property, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void testAddConceptDomainProperty() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		Property prop = new Property();
		prop.setPropertyId("cd2propId2");
		Text value = new Text();
		value.setContent("cd2content2");
		prop.setValue(value);
		prop.setPropertyName("cd2propertyName2");
		
		assertTrue(CD_AUTH_OP.addConceptDomainProperty("cdunitest2", null, prop, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev));
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#updateConceptDomainProperty(java.lang.String, org.LexGrid.commonTypes.Property, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void testUpdateConceptDomainProperty() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		String revId = getRevId();
		rev.setRevisionId(revId);
		
		Property prop = new Property();
		prop.setPropertyId("cd2propId2");
		Text value = new Text();
		value.setContent("cd2content2 updated - cdR212");
		prop.setValue(value);
		prop.setPropertyName("cd2propertyName2");
		prop.setLanguage("en updated");
		prop.setIsActive(true);
		prop.setOwner("owner updated");
		
		assertTrue(CD_AUTH_OP.updateConceptDomainProperty("cdunitest2", null, prop, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev));
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#addConceptDomainToValueSetBinding(java.lang.String, java.util.List, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 * @throws URISyntaxException 
	 */
	@Test
	public void testAddConceptDomainToValueSetBinding() throws LBException, URISyntaxException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		assertTrue(CD_AUTH_OP.addConceptDomainToValueSetBinding("cdunitest1", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI, 
				ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, 
				new URI("SRITEST:AUTO:PropertyRefTest1-VSDONLY"), rev));

	}
	
	/**
	 * Test method for {@link org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainCodingScheme(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 * @throws LBException 
	 */
	@Test
	public void testGetConceptDomainCodingScheme() throws LBException {
		CodingScheme cs = CD_QUERY_OP.getConceptDomainCodingScheme(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(cs.getCodingSchemeName().equals(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME));
		assertTrue(cs.getCodingSchemeURI().equals(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI));
		assertTrue(cs.getFormalName().equals(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME));
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainEntitisWithName(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption, java.lang.String, java.lang.String)}.
	 * @throws LBException 
	 */
	@Test
	public void testGetConceptDomainEntitisWithName() throws LBException {
		List<Entity> entityList = CD_QUERY_OP.getConceptDomainEntitisWithName("code", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, null, null, "subString", null);
		
		assertTrue(entityList.size() == 4);
		for (Entity entity : entityList)
		{
			assertTrue(entity.getEntityCode().equals("AcknowledgementDetailCode")
					|| entity.getEntityCode().equals("AcknowledgementDetailNotSupportedCode")
					|| entity.getEntityCode().equals("AcknowledgementDetailSyntaxErrorCode")
					|| entity.getEntityCode().equals("ActAdjudicationInformationCode"));
			assertTrue(entity.getEntityCodeNamespace().equals(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME));
		}
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainEntity(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 * @throws LBException 
	 */
	@Test
	public void testGetConceptDomainEntity() throws LBException {
		Entity entity = CD_QUERY_OP.getConceptDomainEntity("ActAdjudicationInformationCode", null, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(entity.getEntityCode().equalsIgnoreCase("ActAdjudicationInformationCode"));
		assertTrue(entity.getEntityCodeNamespace().equals(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME));
	}
	
	/**
	 * Test method for {@link org.lexevs.cts2.query.ConceptDomainQueryOperation#listAllConceptDomainEntities(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 * @throws LBException 
	 */
	@Test
	public void testListAllConceptDomainEntities() throws LBException {
		List<Entity> entities = CD_QUERY_OP.listAllConceptDomainEntities(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(entities.size() >= 10);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ConceptDomainQueryOperation#listAllConceptDomainIds(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 * @throws LBException 
	 */
	@Test
	public void testListAllConceptDomainIds() throws LBException {
		
		List<String> cdIds = CD_QUERY_OP.listAllConceptDomainIds(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(cdIds.size() >= 10);
	}
	
	@Test
	public void testGetValueSetBinding()
	{
		List<String> urisStr = vds_.getValueSetDefinitionURIsWithConceptDomain("cdunitest1", null);
		
		assertTrue(urisStr.size() == 1);
		
		for (String uri : urisStr)
		{
			assertTrue(uri.equals("SRITEST:AUTO:PropertyRefTest1-VSDONLY"));
		}		
	}

	public static void removeConceptDomainToValueSetBinding() throws LBException, URISyntaxException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		assertTrue(CD_AUTH_OP.removeConceptDomainToValueSetBinding("cdunitest1", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI, 
				ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, 
				new URI("SRITEST:AUTO:PropertyRefTest1-VSDONLY"), rev));
	}
	
	public static void getValueSetBinding2(){		
		List<String> urisStr = vds_.getValueSetDefinitionURIsWithConceptDomain("cdunitest1", null);
		
		assertTrue(urisStr.size() == 0);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#removeConceptDomainProperty(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void testRemoveConceptDomainProperty() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		Property prop = new Property();
		prop.setPropertyId("cd2propId2");
		prop.setPropertyName("cd2propertyName2");
		
		CD_AUTH_OP.removeConceptDomainProperty("cdunitest2", null, prop, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
	}
	

	public static void removeConceptDomain() throws LBException{
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		CD_AUTH_OP.removeConceptDomain("AcknowledgementCondition", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME,
				ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
	}
	
	@AfterClass
	public static void removeConceptDomainCodeSystem() throws Exception{
		removeConceptDomainToValueSetBinding();
		removeConceptDomain();
		getValueSetBinding2();
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId(getRevId());
		
	    
	    String codingSchemeURI = ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI;
		String representsVersion = ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION;
		
		Boolean removeStatus = false;
	    
	    CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
	
	    try {
			removeStatus = codeSystemAuthOp.removeCodeSystem(revInfo, codingSchemeURI, representsVersion);
		} catch (Exception e) {
			e.printStackTrace();
			
			//if above fails -- at least try this so it won't effect anything downstream.
			LexEvsServiceLocator.getInstance().getSystemResourceService().removeCodingSchemeResourceFromSystem(codingSchemeURI, representsVersion);
		} 
	
		
		assertTrue(removeStatus);
		
		tearDown();
	}
	
	class ConceptDomainData{
		String id;
		String name;
		String description;
	}
	
	private static String getRevId(){
		String revId = UUID.randomUUID().toString();
		revIds_.add(revId);
		
		return revId;
	}

	public static void tearDown() throws Exception {
		CD_AUTH_OP = null;
		CD_QUERY_OP = null;
		
		List<String> uris = vds_.listValueSetDefinitions(null);
		assertTrue(uris.size() > 0);
		
		for (String uri : uris)
		{
			if (uri.startsWith("SRITEST:"))
				vds_.removeValueSetDefinition(new URI(uri));
		}
		
		// check if we missed any test valueDomains
		uris = vds_.listValueSetDefinitions(null);
		
		for (String uri : uris)
		{
			if (uri.toString().startsWith("SRITEST:"))
				assertTrue("Not all test value domains were deleted.",false);
		}
		
		AuthoringService authServ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
		for (String revId : revIds_)
		{
			assertTrue(authServ.removeRevisionRecordbyId(revId));
		}
		
		vds_ = null;
	}
}
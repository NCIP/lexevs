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
package org.lexevs.cts2.query;

import static junit.framework.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.admin.load.CodeSystemLoadOperation;
import org.lexevs.cts2.author.CodeSystemAuthoringOperation;
import org.lexevs.cts2.author.ConceptDomainAuthoringOperation;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.conceptdomain.util.ConceptDomainConstants;

/**
 * @author m004181
 *
 */
public class ConceptDomainQueryOperationTest {
	private static ConceptDomainQueryOperation CD_QUERY_OP;
	private static ConceptDomainAuthoringOperation CD_AUTH_OP;
	private static List<String> revIds_ = new ArrayList<String>();
	
	@BeforeClass
	public static void runBeforeClass(){
		CD_QUERY_OP = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getConceptDomainQueryOperation();
		CD_AUTH_OP = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getConceptDomainAuthoringOperation();
	}
	
	@AfterClass
	public static void runAfterClass(){
		CD_QUERY_OP = null;
		CD_AUTH_OP = null;
	}
	
	@Test
	public void createConceptDomainCodeSystem() throws LBException {
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
	public void createConceptDomainFromFile() throws LBException, IOException {
		RevisionInfo rev = new RevisionInfo();
		
		File file = new File("src/test/resources/testData/ConceptDomainTestData.txt");
		 
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
	public void createConceptDomain() throws LBException, IOException {
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
	 * Test method for {@link org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainCodingScheme(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 * @throws LBException 
	 */
	@Test
	public void getConceptDomainCodingScheme() throws LBException {
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
	public void getConceptDomainEntitisWithName() throws LBException {
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
	public void getConceptDomainEntity() throws LBException {
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
	public void removeConceptDomainCodeSystem() throws LBException{
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
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
		
		assertTrue(removeStatus);
	}
	
	@Test
	public void removeRevisionRecordById() throws LBException {
		AuthoringService authServ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
		for (String revId : revIds_)
		{
			assertTrue(authServ.removeRevisionRecordbyId(revId));
		}
	}
	
	private String getRevId(){
		String revId = UUID.randomUUID().toString();
		revIds_.add(revId);
		
		return revId;
	}
	
	class ConceptDomainData{
		String id;
		String name;
		String description;
	}
	
}
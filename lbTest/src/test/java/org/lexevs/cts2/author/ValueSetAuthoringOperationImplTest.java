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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.apache.commons.lang.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.cts2.query.ValueSetQueryOperation;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * @author m004181
 *
 */
public class ValueSetAuthoringOperationImplTest {

	private static ValueSetAuthoringOperation VS_AUTH_OP = null;
	private static ValueSetQueryOperation VS_QUERY_OP = null;
	
	@BeforeClass
	public static void runBeforeClass(){
		VS_AUTH_OP = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getValueSetAuthoringOperation();
		VS_QUERY_OP = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getValueSetQueryOperation();
	}
	
	@AfterClass
	public static void runAfterClass(){
		VS_AUTH_OP = null;
		VS_QUERY_OP = null;
	}
	
	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#createValueSet(java.net.URI, java.lang.String, java.lang.String, java.lang.String, java.util.List, java.util.List, org.LexGrid.commonTypes.Properties, org.LexGrid.valueSets.DefinitionEntry, org.LexGrid.commonTypes.Versionable, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 * @throws URISyntaxException 
	 * @throws LBException 
	 */
	@Test
	public void createValueSetUsingURI() throws LBException, URISyntaxException {
		Properties props = new Properties();
		Property prop = new Property();
		prop.setPropertyId("p1");
		prop.setIsActive(true);
		prop.setLanguage("en");
		prop.setOwner("owner");
		prop.setPropertyName("propertyName");
		prop.setPropertyType(PropertyTypes.PROPERTY.name());
		Text text = new Text();
		text.setContent("prop value");
		prop.setValue(text);
		
		props.addProperty(prop);
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId("vsdR100");
		
		URI vsdURI = VS_AUTH_OP.createValueSet(new URI("VSD:AUTHORING:JUNIT:TEST1"), 
				"Authoring create vsd junit test1",
				"Automobiles", "Autos", null, null, 
				props, null, null, revInfo);
		
		
		assertTrue("VSD URI loaded : " + vsdURI.toString(), vsdURI.toString().equals("VSD:AUTHORING:JUNIT:TEST1"));
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#createValueSet(org.LexGrid.valueSets.ValueSetDefinition, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 * @throws LBException 
	 */
	@Test
	public void createValueSetUsingValueSetDefinition() throws LBException {
		ValueSetDefinition vsd = new ValueSetDefinition();
		vsd.setValueSetDefinitionURI("VSD:AUTHORING:JUNIT:TEST2");
		vsd.setValueSetDefinitionName("Authoring create vsd junit test2");
		vsd.setConceptDomain("Autos CD");
		vsd.setDefaultCodingScheme("Automobiles");
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setRevisionId("vsdR200");
		revInfo.setChangeAgent("testChangeAgent");
		revInfo.setChangeInstruction("testChangeInstruction");
		revInfo.setEditOrder(0L);
		revInfo.setDescription("testDescription");
		revInfo.setRevisionDate(new Date());
		
		URI vsdURI = VS_AUTH_OP.createValueSet(vsd, revInfo);
		
		assertTrue("VSD URI loaded : " + vsdURI.toString(), vsdURI.toString().equals("VSD:AUTHORING:JUNIT:TEST2"));
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#addDefinitionEntry(java.net.URI, org.LexGrid.valueSets.DefinitionEntry, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 * @throws URISyntaxException 
	 * @throws LBException 
	 */
	@Test
	public void addDefinitionEntry1() throws LBException, URISyntaxException {
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setRevisionId("vsdR210");
		revInfo.setChangeAgent("testChangeAgent");
		revInfo.setChangeInstruction("Adding new definition entry");
		revInfo.setEditOrder(0L);
		revInfo.setDescription("adding new def entry to VSD:AUTHORING:JUNIT:TEST2");
		revInfo.setRevisionDate(new Date());
		
		DefinitionEntry newDefEntry = new DefinitionEntry();
		newDefEntry.setOperator(DefinitionOperator.OR);
		newDefEntry.setRuleOrder(0L);
		
		EntityReference er = new EntityReference();
		er.setEntityCode("GM");
		er.setEntityCodeNamespace("Automobiles");
		er.setLeafOnly(false);
		er.setReferenceAssociation("hasSubtype");
		er.setTargetToSource(false);
		er.setTransitiveClosure(true);
		
		newDefEntry.setEntityReference(er);
		
		assertTrue(VS_AUTH_OP.addDefinitionEntry(new URI("VSD:AUTHORING:JUNIT:TEST2"), newDefEntry, revInfo));
	}
	
	/**
	 * Test to check definitionEntry added in previously test is now accessible 
	 * @throws LBException
	 */
	@Test
	public void getVSDDetails1() throws LBException{
		ValueSetDefinition vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST2", null);
		
		assertTrue(vsd.getDefinitionEntryCount() == 1);
		
		EntityReference er = vsd.getDefinitionEntry(0).getEntityReference();
		
		assertTrue(er.getEntityCode().equals("GM"));
		assertTrue(er.getEntityCodeNamespace().equals("Automobiles"));
		assertFalse(er.getLeafOnly());
		assertTrue(er.getReferenceAssociation().equals("hasSubtype"));
		assertFalse(er.getTargetToSource());
		assertTrue(er.getTransitiveClosure());
		
		vsd = null;
	}
	
	/**
	 * Add 2nd definition entry to VSD - VSD:AUTHORING:JUNIT:TEST2
	 * @throws LBException
	 * @throws URISyntaxException
	 */
	@Test
	public void addDefinitionEntry2() throws LBException, URISyntaxException {
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setRevisionId("vsdR220");
		revInfo.setChangeAgent("testChangeAgent");
		revInfo.setChangeInstruction("Adding new definition entry");
		revInfo.setEditOrder(0L);
		revInfo.setDescription("adding new def entry");
		revInfo.setRevisionDate(new Date());
		
		DefinitionEntry newDefEntry = new DefinitionEntry();
		newDefEntry.setOperator(DefinitionOperator.OR);
		newDefEntry.setRuleOrder(1L);
		
		EntityReference er = new EntityReference();
		er.setEntityCode("Ford");
		er.setEntityCodeNamespace("Automobiles");
		er.setLeafOnly(false);
		er.setReferenceAssociation(null);
		er.setTargetToSource(null);
		er.setTransitiveClosure(null);
		
		newDefEntry.setEntityReference(er);
		
		assertTrue(VS_AUTH_OP.addDefinitionEntry(new URI("VSD:AUTHORING:JUNIT:TEST2"), newDefEntry, revInfo));
	}
	
	/**
	 * Test to check definitionEntry added in previously two tests is now accessible 
	 * @throws LBException
	 */
	@Test
	public void getVSDDetails2() throws LBException{
		ValueSetDefinition vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST2", null);
		
		assertTrue(vsd.getDefinitionEntryCount() == 2);
		
		EntityReference er = vsd.getDefinitionEntry(0).getEntityReference();
		
		assertTrue(er.getEntityCode().equals("GM"));
		assertTrue(er.getEntityCodeNamespace().equals("Automobiles"));
		assertFalse(er.getLeafOnly());
		assertTrue(er.getReferenceAssociation().equals("hasSubtype"));
		assertFalse(er.getTargetToSource());
		assertTrue(er.getTransitiveClosure());
		
		er = vsd.getDefinitionEntry(1).getEntityReference();
		
		assertTrue(er.getEntityCode().equals("Ford"));
		assertTrue(er.getEntityCodeNamespace().equals("Automobiles"));
		assertFalse(er.getLeafOnly());
		assertTrue(StringUtils.isEmpty(er.getReferenceAssociation()));
		assertTrue(null == er.getTargetToSource());
		assertTrue(null == er.getTransitiveClosure());
		
		vsd = null;
		
		/**
		 * When revision id is provided for first definitionEntry, only one should be returned
		 */
		vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST2", "vsdR210");
		
		assertTrue(vsd.getDefinitionEntryCount() == 1);
		
		er = vsd.getDefinitionEntry(0).getEntityReference();
		
		assertTrue(er.getEntityCode().equals("GM"));
		assertTrue(er.getEntityCodeNamespace().equals("Automobiles"));
		assertFalse(er.getLeafOnly());
		assertTrue(er.getReferenceAssociation().equals("hasSubtype"));
		assertFalse(er.getTargetToSource());
		assertTrue(er.getTransitiveClosure());
		
		vsd = null;
		
		/**
		 * When revision id is provided for second definitionEntry, both should be returned
		 */
		vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST2", "vsdR220");
		
		assertTrue(vsd.getDefinitionEntryCount() == 2);
		
		er = getEntityReference(vsd, "GM");
		
		assertEquals("GM",er.getEntityCode());
		assertTrue(er.getEntityCodeNamespace().equals("Automobiles"));
		assertFalse(er.getLeafOnly());
		assertTrue(er.getReferenceAssociation().equals("hasSubtype"));
		assertFalse(er.getTargetToSource());
		assertTrue(er.getTransitiveClosure());
		
		er = getEntityReference(vsd, "Ford");
		
		assertTrue(er.getEntityCode().equals("Ford"));
		assertTrue(er.getEntityCodeNamespace().equals("Automobiles"));
		assertFalse(er.getLeafOnly());
		assertTrue(StringUtils.isEmpty(er.getReferenceAssociation()));
		assertTrue(null == er.getTargetToSource());
		assertTrue(null == er.getTransitiveClosure());
		
		vsd = null;
	}
	
	private EntityReference getEntityReference(ValueSetDefinition def, String code) {
		for(DefinitionEntry definition : def.getDefinitionEntry()) {
			if(definition.getEntityReference().getEntityCode().equals(code)) {
				return definition.getEntityReference();
			}
		}
		fail();
		return null;
	}
	
	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#addValueSetProperty(java.net.URI, org.LexGrid.commonTypes.Property, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 * @throws URISyntaxException 
	 * @throws LBException 
	 */
	@Test
	public void addValueSetProperty1() throws LBException, URISyntaxException {
		Property prop = new Property();
		prop.setPropertyId("propertyId1");
		prop.setPropertyName("propertyName 1");
		prop.setIsActive(true);
		prop.setLanguage("en");
		prop.setOwner("owner");
		prop.setPropertyType(PropertyTypes.PROPERTY.name());
		Text text = new Text();
		text.setContent("original content 1");
		prop.setValue(text);
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId("vsdR101");
		
		assertTrue(VS_AUTH_OP.addValueSetProperty(new URI("VSD:AUTHORING:JUNIT:TEST1"), 
					prop, revInfo));
	}
	
	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#addValueSetProperty(java.net.URI, org.LexGrid.commonTypes.Property, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 * @throws URISyntaxException 
	 * @throws LBException 
	 */
	@Test
	public void addValueSetProperty2() throws LBException, URISyntaxException {
		Property prop = new Property();
		prop.setPropertyId("propertyId2");
		prop.setPropertyName("propertyName 2");
		prop.setIsActive(true);
		prop.setLanguage("en");
		prop.setOwner("owner");
		prop.setPropertyType(PropertyTypes.PROPERTY.name());
		Text text = new Text();
		text.setContent("original content 2");
		prop.setValue(text);
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId("vsdR102");
		
		assertTrue(VS_AUTH_OP.addValueSetProperty(new URI("VSD:AUTHORING:JUNIT:TEST1"), 
					prop, revInfo));
	}
	
	/**
	 * Test to check property added originally and in previously test is now accessible 
	 * @throws LBException
	 */
	@Test
	public void getVSDProperties1() throws LBException{
		ValueSetDefinition vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST1", null);
		
		assertTrue(vsd.getProperties().getPropertyCount() == 3);
		
		for (Property prop : vsd.getProperties().getPropertyAsReference())
		{
			
			assertTrue(prop.getPropertyId().equals("propertyId1") ||
					prop.getPropertyId().equals("propertyId2") ||
					prop.getPropertyId().equals("p1"));
			
			if (prop.getPropertyId().equals("propertyId1"))
			{
				assertTrue(prop.getPropertyName().equals("propertyName 1"));
				assertTrue(prop.getValue().getContent().equals("original content 1"));
				assertTrue(prop.getIsActive());
				assertTrue(prop.getLanguage().equals("en"));
				assertTrue(prop.getOwner().equals("owner"));
			}
			else if (prop.getPropertyId().equals("propertyId2"))
			{			
				assertTrue(prop.getPropertyId().equals("propertyId2"));
				assertTrue(prop.getPropertyName().equals("propertyName 2"));
				assertTrue(prop.getIsActive());
				assertTrue(prop.getLanguage().equals("en"));
				assertTrue(prop.getOwner().equals("owner"));
				assertTrue(prop.getValue().getContent().equals("original content 2"));
			}
			else
			{
				assertTrue(prop.getPropertyId().equals("p1"));
				assertTrue(prop.getIsActive());
				assertTrue(prop.getLanguage().equals("en"));
				assertTrue(prop.getOwner().equals("owner"));
				assertTrue(prop.getPropertyName().equals("propertyName"));
				assertTrue(prop.getValue().getContent().equals("prop value"));
			}
		}
		
		/**
		 * revision 'vsdR101' had only two (p1 and propertyId1) property for vsd - VSD:AUTHORING:JUNIT:TEST1 
		 */
		
		vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST1", "vsdR101");
		
		assertTrue(vsd.getProperties().getPropertyCount() == 2);
		
		for (Property prop : vsd.getProperties().getPropertyAsReference())
		{
			
			assertTrue(prop.getPropertyId().equals("propertyId1") ||
					prop.getPropertyId().equals("p1"));
		}
		
		/**
		 * revision 'vsdR102' had all three property for vsd - VSD:AUTHORING:JUNIT:TEST1 
		 */
		
		vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST1", "vsdR102");
		
		assertTrue(vsd.getProperties().getPropertyCount() == 3);
		
		for (Property prop : vsd.getProperties().getPropertyAsReference())
		{
			
			assertTrue(prop.getPropertyId().equals("propertyId1") ||
					prop.getPropertyId().equals("propertyId2") ||
					prop.getPropertyId().equals("p1"));
		}
		
		vsd = null;
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#updateDefinitionEntry(java.net.URI, org.LexGrid.valueSets.DefinitionEntry, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 * @throws URISyntaxException 
	 * @throws LBException 
	 */
	@Test
	public void updateDefinitionEntry() throws LBException, URISyntaxException {
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setRevisionId("vsdR230");
		revInfo.setChangeAgent("testUpdateChangeAgent");
		revInfo.setChangeInstruction("testUpdateChangeInstruction");
		revInfo.setEditOrder(0L);
		revInfo.setDescription("testUpdateDescription");
		revInfo.setRevisionDate(new Date());
		
		DefinitionEntry changedDefinitionEntry = new DefinitionEntry();
		changedDefinitionEntry.setRuleOrder(0L);
		changedDefinitionEntry.setOperator(DefinitionOperator.OR);
		EntityReference er = new EntityReference();
		er.setEntityCode("GM");
		er.setEntityCodeNamespace("Automobiles");
		// change leafOnly to true and association type to 'CHD'
		er.setLeafOnly(true);
		er.setReferenceAssociation("CHD");
		
		changedDefinitionEntry.setEntityReference(er);
		
		assertTrue(VS_AUTH_OP.updateDefinitionEntry(new URI("VSD:AUTHORING:JUNIT:TEST2"), changedDefinitionEntry, revInfo));
	}
	
	/**
	 * Test to check definitionEntry updated in previously test is actually updated
	 * @throws LBException
	 */
	@Test
	public void getVSDDetails3() throws LBException{
		ValueSetDefinition vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST2", null);
		
		for (DefinitionEntry de : vsd.getDefinitionEntryAsReference())
		{
			EntityReference er = de.getEntityReference();
			
			if (er.getEntityCode().equals("GM"))
			{
				assertTrue(er.getEntityCode().equals("GM"));
				assertTrue(er.getEntityCodeNamespace().equals("Automobiles"));
				assertTrue(er.getLeafOnly()); // changed to true
				assertTrue(er.getReferenceAssociation().equals("CHD")); // changed from 'hasSubtype'
				assertFalse(er.getTargetToSource());
				assertTrue(er.getTransitiveClosure());
			}
		}
		
		/**
		 * If we look back at the revision 'vsdR220' we should get back previous values
		 */
		vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST2", "vsdR220");
		
		for (DefinitionEntry de : vsd.getDefinitionEntryAsReference())
		{
			EntityReference er = de.getEntityReference();
			
			if (er.getEntityCode().equals("GM"))
			{
				assertTrue(er.getEntityCode().equals("GM"));
				assertTrue(er.getEntityCodeNamespace().equals("Automobiles"));
				assertFalse(er.getLeafOnly()); // changed to true
				assertTrue(er.getReferenceAssociation().equals("hasSubtype")); // changed to 'CHD' later revision
				assertFalse(er.getTargetToSource());
				assertTrue(er.getTransitiveClosure());
			}
		}
		
		vsd = null;
	}
	

	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#updateValueSetProperty(java.net.URI, org.LexGrid.commonTypes.Property, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 * @throws URISyntaxException 
	 * @throws LBException 
	 */
	@Test
	public void updateValueSetProperty() throws LBException, URISyntaxException {
		Presentation prop = new Presentation();
		prop.setPropertyId("p1");
		prop.setPropertyName("propertyName");
		// change owner from 'owner' to 'owner updated vsd103'
		prop.setOwner("owner updated vsd103");
		// change property value from 'prop value' to 'prop value updated vsdR103'
		Text text = new Text();
		text.setContent("prop value updated vsdR103");
		// set dataType		
		text.setDataType("Text datatype");
		prop.setValue(text);
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId("vsdR103");
		
		assertTrue(VS_AUTH_OP.updateValueSetProperty(new URI("VSD:AUTHORING:JUNIT:TEST1"), prop, revInfo));
	}
	
	/**
	 * Test to check property updates in previously test is now accessible 
	 * @throws LBException
	 */
	@Test
	public void getVSDProperties2() throws LBException{
		ValueSetDefinition vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST1", null);
		
		assertTrue(vsd.getProperties().getPropertyCount() == 3);
		
		for (Property prop : vsd.getProperties().getPropertyAsReference())
		{
			
			assertTrue(prop.getPropertyId().equals("propertyId1") ||
					prop.getPropertyId().equals("propertyId2") ||
					prop.getPropertyId().equals("p1"));
			
			if (prop.getPropertyId().equals("propertyId1"))
			{
				assertTrue(prop.getPropertyName().equals("propertyName 1"));
				assertTrue(prop.getValue().getContent().equals("original content 1"));
				assertTrue(prop.getIsActive());
				assertTrue(prop.getLanguage().equals("en"));
				assertTrue(prop.getOwner().equals("owner"));
			}
			else if (prop.getPropertyId().equals("propertyId2"))
			{			
				assertTrue(prop.getPropertyId().equals("propertyId2"));
				assertTrue(prop.getPropertyName().equals("propertyName 2"));
				assertTrue(prop.getIsActive());
				assertTrue(prop.getLanguage().equals("en"));
				assertTrue(prop.getOwner().equals("owner"));
				assertTrue(prop.getValue().getContent().equals("original content 2"));
			}
			else
			{
				assertTrue(prop.getPropertyId().equals("p1"));
				assertTrue(prop.getIsActive());
				assertTrue(prop.getLanguage().equals("en"));
				assertTrue(prop.getOwner().equals("owner updated vsd103")); // previously it was 'owner'
				assertTrue(prop.getPropertyName().equals("propertyName"));
				assertTrue(prop.getValue().getContent().equals("prop value updated vsdR103")); // previously it was 'prop value'
				assertTrue(prop.getValue().getDataType().equals("Text datatype")); // added in previous test case
			}
		}
		
		/**
		 * revision 'vsdR101' should return original p1 property 
		 */
		
		vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST1", "vsdR101");
		
		assertTrue(vsd.getProperties().getPropertyCount() == 2);
		
		for (Property prop : vsd.getProperties().getPropertyAsReference())
		{
			
			assertTrue(prop.getPropertyId().equals("propertyId1") ||
					prop.getPropertyId().equals("p1"));
			if (prop.getPropertyId().equals("p1"))
			{
				assertTrue(prop.getPropertyId().equals("p1"));
				assertTrue(prop.getIsActive());
				assertTrue(prop.getLanguage().equals("en"));
				assertTrue(prop.getOwner().equals("owner"));
				assertTrue(prop.getPropertyName().equals("propertyName"));
				assertTrue(prop.getValue().getContent().equals("prop value"));
			}
		}
		
		/**
		 * revision 'vsdR102' should also return original p1 property 
		 */
		
		vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST1", "vsdR102");
		
		assertTrue(vsd.getProperties().getPropertyCount() == 3);
		
		for (Property prop : vsd.getProperties().getPropertyAsReference())
		{
			
			assertTrue(prop.getPropertyId().equals("propertyId1") ||
					prop.getPropertyId().equals("propertyId2") ||
					prop.getPropertyId().equals("p1"));
			
			if (prop.getPropertyId().equals("p1"))
			{
				assertTrue(prop.getPropertyId().equals("p1"));
				assertTrue(prop.getIsActive());
				assertTrue(prop.getLanguage().equals("en"));
				assertTrue(prop.getOwner().equals("owner"));
				assertTrue(prop.getPropertyName().equals("propertyName"));
				assertTrue(prop.getValue().getContent().equals("prop value"));
			}
		}
		
		vsd = null;
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#updateValueSetMetaData(java.net.URI, java.lang.String, java.lang.String, java.lang.String, java.util.List, java.util.List, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws URISyntaxException 
	 * @throws LBException 
	 */
	@Test
	public void updateValueSetMetaData() throws LBException, URISyntaxException {
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setRevisionId("vsdR250");
		revInfo.setChangeAgent("testUpdateChangeAgent");
		revInfo.setChangeInstruction("testUpdateChangeInstruction");
		revInfo.setEditOrder(0L);
		revInfo.setDescription("testUpdateDescription");
		revInfo.setRevisionDate(new Date());
		
		List<Source> srcList = new ArrayList<Source>();
		Source src = new Source();
		src.setContent("LexEVS as src");
		srcList.add(src);
		src = new Source();
		src.setContent("Mayo as src");
		srcList.add(src);
		
		List<String> contextList = new ArrayList<String>();
		contextList.add("Autos as context");
		contextList.add("lexevs as context");
		
		// add two new source, 2 new context, update name and concept domain
		assertTrue(VS_AUTH_OP.updateValueSetMetaData(new URI("VSD:AUTHORING:JUNIT:TEST2"), 
				"VSD after metatdate update vsdR250",
				null, "Autos CD update vsdR250", srcList, contextList, 
				revInfo));
	}

	/**
	 * Test to check meta data updated in previously two tests is now accessible 
	 * @throws LBException
	 */
	@Test
	public void getVSDDetails4() throws LBException{
		ValueSetDefinition vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST2", null);
		
		assertTrue(vsd.getValueSetDefinitionName().equals("VSD after metatdate update vsdR250")); //updated value
		assertTrue(vsd.getConceptDomain().equals("Autos CD update vsdR250")); // updated value
		assertTrue(vsd.getDefaultCodingScheme().equals("Automobiles")); // no change here
		
		assertTrue(vsd.getSourceCount() == 2);
		for (Source src : vsd.getSourceAsReference())
		{
			assertTrue(src.getContent().equals("LexEVS as src") ||
						src.getContent().equals("Mayo as src"));
		}
		
		assertTrue(vsd.getRepresentsRealmOrContextCount() == 2);
		for (String ctx : vsd.getRepresentsRealmOrContextAsReference())
		{
			assertTrue(ctx.equals("Autos as context") ||
					ctx.equals("lexevs as context"));
		}
		
		/**
		 * If we look back at the revision 'vsdR220' we should get back original vsd meta data values
		 */
		vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST2", "vsdR220");
		
		assertTrue(vsd.getValueSetDefinitionName().equals("Authoring create vsd junit test2")); //original value
		assertTrue(vsd.getConceptDomain().equals("Autos CD")); // original value
		assertTrue(vsd.getDefaultCodingScheme().equals("Automobiles")); // no change here
		
		assertTrue(vsd.getSourceCount() == 0); // no source originally
		assertTrue(vsd.getRepresentsRealmOrContextCount() == 0); // no context originally
		
		vsd = null;
	}
	
	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#updateValueSetStatus(java.net.URI, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.	 
	 * @throws URISyntaxException 
	 * @throws LBException 
	 */
	@Test
	public void updateValueSetStatus() throws LBException, URISyntaxException {
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setRevisionId("vsdR260");
		revInfo.setChangeAgent("testUpdateStatusChangeAgent");
		revInfo.setChangeInstruction("testUpdateStautsChangeInstruction");
		revInfo.setEditOrder(0L);
		revInfo.setDescription("testUpdateStatusDescription");
		revInfo.setRevisionDate(new Date());
		
		assertTrue(VS_AUTH_OP.updateValueSetStatus(new URI("VSD:AUTHORING:JUNIT:TEST2"), "status at vsdR260", revInfo));
	}

	/**
	 * Test to check status updated in previously two tests is now accessible 
	 * @throws LBException
	 */
	@Test
	public void getVSDDetails5() throws LBException{
		ValueSetDefinition vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST2", null);
		
		assertTrue(vsd.getStatus().equals("status at vsdR260")); //updated value

		/**
		 * If we look back at the revision 'vsdR220' we should get back original vsd status values
		 */
		vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST2", "vsdR220");
		
		assertTrue(vsd.getStatus() == null); //original value
		
		vsd = null;
	}
	
	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#updateValueSetVersionable(java.net.URI, org.LexGrid.commonTypes.Versionable, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 * @throws URISyntaxException 
	 * @throws LBException 
	 */
	@Test
	public void updateValueSetVersionable() throws LBException, URISyntaxException {
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setRevisionId("vsdR270");
		revInfo.setChangeAgent("testUpdateStatusChangeAgent");
		revInfo.setChangeInstruction("testUpdateStautsChangeInstruction");
		revInfo.setEditOrder(0L);
		revInfo.setDescription("testUpdateStatusDescription");
		revInfo.setRevisionDate(new Date());
		
		Versionable ver = new Versionable();
		// set to active
		ver.setIsActive(true);
		// change the owner
		ver.setOwner("new owner at vsdR270");
		
		assertTrue(VS_AUTH_OP.updateValueSetVersionable(new URI("VSD:AUTHORING:JUNIT:TEST2"), ver, revInfo));
	}
	
	/**
	 * Test to check definitionEntry updated in previously two tests is now accessible 
	 * @throws LBException
	 */
	@Test
	public void getVSDDetails6() throws LBException{
		ValueSetDefinition vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST2", null);
		
		assertTrue(vsd.getIsActive()); //updated value
		assertTrue(vsd.getOwner().equals("new owner at vsdR270"));

		/**
		 * If we look back at the revision 'vsdR220' we should get back original vsd status values
		 */
		vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST2", "vsdR260");
		
		assertTrue(vsd.getIsActive() == null); //original value
		assertTrue(vsd.getOwner() == null); // original value
		
		vsd = null;
	}
	
	@Test
	public void removeDefinitionEntry() throws LBException, URISyntaxException{
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setRevisionId("vsdR280");
		revInfo.setChangeAgent("testDeleteDEChangeAgent");
		revInfo.setChangeInstruction("testDeleteDEChangeInstruction");
		revInfo.setEditOrder(0L);
		revInfo.setDescription("testDeleteDEDescription");
		revInfo.setRevisionDate(new Date());
		
		// removing definitionEntry with entityCode 'Ford'
		assertTrue(VS_AUTH_OP.removeDefinitionEntry(new URI("VSD:AUTHORING:JUNIT:TEST2"), 1L, revInfo));
	}
	
	/**
	 * Test to check definitionEntry removal in previously test
	 * @throws LBException
	 */
	@Test
	public void getVSDDetails7() throws LBException{
		ValueSetDefinition vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST2", null);
		
		assertTrue(vsd.getDefinitionEntryCount() == 1); // entityReference with code 'Ford' is removed
		for (DefinitionEntry de : vsd.getDefinitionEntryAsReference())
		{
			EntityReference er = de.getEntityReference();
			
			assertTrue(er.getEntityCode().equals("GM"));
			assertTrue(er.getEntityCodeNamespace().equals("Automobiles"));
			assertTrue(er.getLeafOnly()); // changed to true
			assertTrue(er.getReferenceAssociation().equals("CHD")); // changed from 'hasSubtype'
			assertFalse(er.getTargetToSource());
			assertTrue(er.getTransitiveClosure());
		}
		
		/**
		 * If we look back at the revision 'vsdR230' we should NOT get back removed definitionEntry as 'REMOVAL' is PERMANENT
		 */
		vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST2", "vsdR220");
		
		assertTrue(vsd.getDefinitionEntryCount() == 1); // both entityReference 'Ford' and 'GM' were present
		for (DefinitionEntry de : vsd.getDefinitionEntryAsReference())
		{
			EntityReference er = de.getEntityReference();
			
			assertTrue(er.getEntityCode().equals("GM"));
		}
		vsd = null;
	}
	
	
	@Test
	public void removeValueSetProperty() throws LBException, URISyntaxException {
		
		// before removing a property, there are 3 properties for vsd - VSD:AUTHOR:JUNIT:TEST1
		ValueSetDefinition vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST1", null);
		
		assertTrue(vsd.getProperties().getPropertyCount() == 3);
		for (Property prop : vsd.getProperties().getPropertyAsReference())
		{
			assertTrue(prop.getPropertyId().equals("propertyId1") ||
				prop.getPropertyId().equals("propertyId2") ||
				prop.getPropertyId().equals("p1"));
		}
		
		
		// removing property 'propertyId1'
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId("vsdR150");
		
		ValueSetAuthoringOperation valueSetAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getValueSetAuthoringOperation();
		
		assertTrue(valueSetAuthOp.removeValueSetProperty(new URI("VSD:AUTHORING:JUNIT:TEST1"), "propertyId1", revInfo));
		
		// after deletion, now there should be only 2 properties
		vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST1", null);
		
		assertTrue(vsd.getProperties().getPropertyCount() == 2);
		for (Property prop : vsd.getProperties().getPropertyAsReference())
		{
			assertTrue(prop.getPropertyId().equals("propertyId2") ||
				prop.getPropertyId().equals("p1"));
		}
		
		vsd = null;
	}

	@Test
	public void removeValueSet1() throws LBException, URISyntaxException{
		// before removing a vsd, it does exist
		ValueSetDefinition vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST1", null);
		assertTrue(vsd != null);
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setRevisionId("vsdR160");
		revInfo.setChangeAgent("testDeleteVSDChangeAgent");
		revInfo.setChangeInstruction("testDeleteVSDChangeInstruction");
		revInfo.setEditOrder(0L);
		revInfo.setDescription("testDeleteVSDDescription");
		revInfo.setRevisionDate(new Date());
		
		assertTrue(VS_AUTH_OP.removeValueSet(new URI("VSD:AUTHORING:JUNIT:TEST1"), revInfo));
		
		// after deletion, vsd object should be null
		vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST1", null);
		assertTrue(vsd == null);
		
		vsd = null;
	}
	
	@Test
	public void removeValueSet2() throws LBException, URISyntaxException{
		// before removing a vsd, it does exist
		ValueSetDefinition vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST2", null);
		assertTrue(vsd != null);
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setRevisionId("vsdR290");
		revInfo.setChangeAgent("testDeleteVSDChangeAgent");
		revInfo.setChangeInstruction("testDeleteVSDChangeInstruction");
		revInfo.setEditOrder(0L);
		revInfo.setDescription("testDeleteVSDDescription");
		revInfo.setRevisionDate(new Date());
		
		assertTrue(VS_AUTH_OP.removeValueSet(new URI("VSD:AUTHORING:JUNIT:TEST2"), revInfo));
		
		// after deletion, vsd object should be null
		vsd = VS_QUERY_OP.getValueSetDetails("VSD:AUTHORING:JUNIT:TEST2", null);
		assertTrue(vsd == null);
		
		vsd = null;
	}
	
	@Test
	public void removeRevisionRecordById() throws LBException {
		AuthoringService authServ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
		assertTrue(authServ.removeRevisionRecordbyId("vsdR100"));
		assertTrue(authServ.removeRevisionRecordbyId("vsdR200"));
		assertTrue(authServ.removeRevisionRecordbyId("vsdR210"));
		assertTrue(authServ.removeRevisionRecordbyId("vsdR220"));
		assertTrue(authServ.removeRevisionRecordbyId("vsdR101"));
		assertTrue(authServ.removeRevisionRecordbyId("vsdR102"));
		assertTrue(authServ.removeRevisionRecordbyId("vsdR230"));
		assertTrue(authServ.removeRevisionRecordbyId("vsdR103"));
		assertTrue(authServ.removeRevisionRecordbyId("vsdR250"));
		assertTrue(authServ.removeRevisionRecordbyId("vsdR260"));
		assertTrue(authServ.removeRevisionRecordbyId("vsdR270"));
		assertTrue(authServ.removeRevisionRecordbyId("vsdR280"));
		assertTrue(authServ.removeRevisionRecordbyId("vsdR150"));
		assertTrue(authServ.removeRevisionRecordbyId("vsdR160"));
		assertTrue(authServ.removeRevisionRecordbyId("vsdR290"));

	}

}
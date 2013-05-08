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
package org.LexGrid.LexBIG.load.xml;

import java.io.File;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.Impl.loaders.MessageDirector;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;

import edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon.LexGridElementProcessor;
import edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon.LexGridXMLProcessor;

public class TestLexGridXMLProcessorForLoad extends TestCase{

	LexGridXMLProcessor processor;
	CachingMessageDirectorImpl messages;
	LexGridElementProcessor elementProcessor;
	public TestLexGridXMLProcessorForLoad(String serverName){
		super(serverName);
	}
	
	public void setUp(){
		processor = new LexGridXMLProcessor();
		elementProcessor = new LexGridElementProcessor();
		LoadStatus ls = new LoadStatus();
		 messages = new CachingMessageDirectorImpl( new MessageDirector("Test XML",ls));
	}
	
	public void testEntryPointDetermination(){
		assertTrue(processor.getEntryPointType(new File("resources/testData/Automobiles.xml").toURI(), messages) == 1);
		assertTrue(processor.getEntryPointType(new File("resources/testData/RevisionOfAutomobiles.xml").toURI(), messages) == 2);
		assertTrue(processor.getEntryPointType(new File("resources/testData/valueDomain/pickListTestData.xml").toURI(), messages) == 3);
		assertTrue(processor.getEntryPointType(new File("resources/testData/valueDomain/VSDOnlyTest.xml").toURI(), messages) == 4);
		assertTrue(processor.getEntryPointType(new File("resources/testData/valueDomain/pickListOnlyTest.xml").toURI(), messages) == 5);
	}
	
	public void testCodingSchemeInLgXml(){
		assertTrue(processor.isCodingSchemePresent(new File("resources/testData/Automobiles.xml").toURI(), messages));
		assertTrue(processor.isCodingSchemePresent(new File("resources/testData/SystemReleaseCodingSchemeTestData.xml").toURI(), messages));
		assertTrue(processor.isCodingSchemePresent(new File("resources/testData/RevisionOfAutomobiles.xml").toURI(), messages));
		assertFalse(processor.isCodingSchemePresent(new File("resources/testData/valueDomain/pickListTestData.xml").toURI(), messages));
		assertFalse(processor.isCodingSchemePresent(new File("resources/testData/valueDomain/VSDOnlyTest.xml").toURI(), messages));
	}
	
	public void testRevisionPresent(){
		assertTrue(processor.getLastRevisionElement(new File("resources/testData/valueDomain/RevisionOfPickList.xml").toURI(), messages)==2);
		assertTrue(processor.getLastRevisionElement(new File("resources/testData/valueDomain/RevisionOfValueSetR001.xml").toURI(), messages)==2);
		assertTrue(processor.getLastRevisionElement(new File("resources/testData/RevisionOfAutomobiles.xml").toURI(), messages)==2);
	}
	
	public void testRelationPropertiesPresence(){
		assertFalse(processor.setRelationsPropertiesFlag(new File("resources/testData/Automobiles.xml").toURI(), messages));
		assertTrue(processor.setRelationsPropertiesFlag(new File("resources/testData/csRevision/RevisionTP.4.xml").toURI(), messages));
	}
}
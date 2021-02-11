
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
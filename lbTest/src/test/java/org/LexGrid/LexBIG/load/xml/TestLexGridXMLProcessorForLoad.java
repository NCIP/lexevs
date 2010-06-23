package org.LexGrid.LexBIG.load.xml;

import java.lang.reflect.Method;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Impl.loaders.MessageDirector;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;

import edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon.LexGridElementProcessor;
import edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon.LexGridXMLProcessor;
import junit.framework.TestCase;

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
		assertTrue(processor.getEntryPointType("resources/testData/Automobiles.xml", messages) == 1);
		assertTrue(processor.getEntryPointType("resources/testData/RevisionOfAutomobiles.xml", messages) == 2);
		assertTrue(processor.getEntryPointType("resources/testData/valueDomain/pickListTestData.xml", messages) == 3);
		assertTrue(processor.getEntryPointType("resources/testData/valueDomain/VSDOnlyTest.xml", messages) == 4);
		assertTrue(processor.getEntryPointType("resources/testData/valueDomain/pickListOnlyTest.xml", messages) == 5);
	}
	
	public void testCodingSchemeInLgXml(){
		assertTrue(processor.isCodingSchemePresent("resources/testData/Automobiles.xml", messages));
		assertTrue(processor.isCodingSchemePresent("resources/testData/SystemReleaseCodingSchemeTestData.xml", messages));
		assertTrue(processor.isCodingSchemePresent("resources/testData/RevisionOfAutomobiles.xml", messages));
		assertFalse(processor.isCodingSchemePresent("resources/testData/valueDomain/pickListTestData.xml", messages));
		assertFalse(processor.isCodingSchemePresent("resources/testData/valueDomain/VSDOnlyTest.xml", messages));
	}
	
	public void testRevisionPresent(){
		assertTrue(processor.getLastRevisionElement("resources/testData/valueDomain/RevisionOfPickList.xml", messages)==2);
		assertTrue(processor.getLastRevisionElement("resources/testData/valueDomain/RevisionOfValueSetR001.xml", messages)==2);
		assertTrue(processor.getLastRevisionElement("resources/testData/RevisionOfAutomobiles.xml", messages)==2);
	}
	
	public void testRelationPropertiesPresence(){
		assertFalse(processor.setRelationsPropertiesFlag("resources/testData/Automobiles.xml", messages));
		assertTrue(processor.setRelationsPropertiesFlag("resources/testData/csRevision/RevisionTP.4.xml", messages));
	}
}

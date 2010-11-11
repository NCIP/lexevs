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
package org.LexGrid.LexBIG.Impl.load.umls;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.junit.After;
import org.junit.Test;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.loader.logging.StatusTrackingLogger;
import org.lexgrid.loader.rrf.factory.IsoMapFactory;

/**
 * The Class DataLoadTestBase.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@SuppressWarnings("unchecked")
public class IsoMapTest extends TestCase {

	@After
	public void tearDown() {
		//can't delete the file without a gc... strange.
		System.gc();
		if(!this.getFile().delete()) {
			this.getFile().deleteOnExit();
		}
	}
	
	@Test
	public void testGetIsoMap() throws Exception {
		IsoMapFactory factory = new IsoMapFactory();
		factory.setLogger(new TestStatusTrackingLogger());
		factory.setLexEvsServiceLocator(LexEvsServiceLocator.getInstance());
		
		Map<String,String> isoMap = (Map<String, String>) factory.getObject();
		
		assertTrue(isoMap.size() > 0);
	}
	
	@Test
	public void testGetIsoMapWithAddition() throws Exception {
		IsoMapFactory factory = new IsoMapFactory();
		factory.setLogger(new TestStatusTrackingLogger());
		factory.setLexEvsServiceLocator(LexEvsServiceLocator.getInstance());

		File newFile = this.getFile();
		newFile.createNewFile();
		
		this.writeToFile(newFile, "someNewSab=newOid");
		
		Map<String,String> isoMap = (Map<String, String>) factory.getObject();
		
		String newOid = isoMap.get("someNewSab");
		
		assertEquals("newOid", newOid);
	}
	
	@Test
	public void testGetIsoMapWithOverride() throws Exception {
		//overriding AOD=urn:oid:2.16.840.1.113883.6.112
		
		IsoMapFactory factory = new IsoMapFactory();
		factory.setLogger(new TestStatusTrackingLogger());
		factory.setLexEvsServiceLocator(LexEvsServiceLocator.getInstance());
		
		Map<String,String> isoMap = (Map<String, String>) factory.getObject();
		assertEquals("urn:oid:2.16.840.1.113883.6.112", isoMap.get("AOD"));
		
		factory = new IsoMapFactory();
		factory.setLogger(new TestStatusTrackingLogger());
		factory.setLexEvsServiceLocator(LexEvsServiceLocator.getInstance());
		
		File newFile = this.getFile();
		newFile.createNewFile();
		
		this.writeToFile(newFile, "AOD=newOid");
		
		isoMap = (Map<String, String>) factory.getObject();
		assertEquals("newOid", isoMap.get("AOD"));
	}
	
	private class TestStatusTrackingLogger implements StatusTrackingLogger {

		@Override
		public LoadStatus getProcessStatus() {
			return null;
		}

		@Override
		public void busy() {
			//
		}

		@Override
		public String debug(String message) {
			return "";
		}

		@Override
		public String error(String message) {
			return "";
		}

		@Override
		public String error(String message, Throwable sourceException) {
			return "";
		}

		@Override
		public String fatal(String message) {
			return "";
		}

		@Override
		public String fatal(String message, Throwable sourceException) {
			return "";
		}

		@Override
		public void fatalAndThrowException(String message) throws Exception {
			//
		}

		@Override
		public void fatalAndThrowException(String message,
				Throwable sourceException) throws Exception {
			//
		}

		@Override
		public String info(String message) {
			return "";
		}

		@Override
		public String warn(String message) {
			return "";
		}

		@Override
		public String warn(String message, Throwable sourceException) {
			return "";
		}
		
	};
	
	private void writeToFile(File file, String text) throws Exception {
		FileWriter writer = new FileWriter(file);
		writer.write(text);
		writer.flush();
		writer.close();
	}
	
	private File getFile() {
		File file = 
			new File(LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables().getConfigFileLocation() 
					+ File.separator + ".." + File.separator + IsoMapFactory.ISO_MAP_FILE_NAME);

		return file;
	}
}
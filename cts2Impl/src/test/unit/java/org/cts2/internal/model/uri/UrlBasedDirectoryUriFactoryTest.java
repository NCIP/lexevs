/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.internal.model.uri;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;

import org.cts2.codesystem.CodeSystemDirectory;
import org.cts2.uri.CodeSystemDirectoryURI;
import org.cts2.uri.CodeSystemVersionDirectoryURI;
import org.cts2.uri.UrlBasedDirectoryUriFactory;
import org.exolab.castor.xml.Marshaller;
import org.junit.Test;

public class UrlBasedDirectoryUriFactoryTest {

	@Test
	public void testCreateDirectoryUriUrlProxy(){
		CodeSystemVersionDirectoryURI uri = 
			UrlBasedDirectoryUriFactory.createUrlBasedDirectoryUri("http://test.org", CodeSystemVersionDirectoryURI.class);
		
		assertNotNull(uri);
	}
	
	@Test
	public void testMarshall() throws Exception{
		
		CodeSystemDirectory dir = new CodeSystemDirectory();
		
		CodeSystemDirectoryURI uri = UrlBasedDirectoryUriFactory.createUrlBasedDirectoryUri("http://test.org", CodeSystemDirectoryURI.class);
		
		dir.setNext(uri);
		
		StringWriter sw = new StringWriter();
		Marshaller.marshal(dir, sw);
		
		assertTrue(sw.toString().contains("next=\"http://test.org\""));
	}
}

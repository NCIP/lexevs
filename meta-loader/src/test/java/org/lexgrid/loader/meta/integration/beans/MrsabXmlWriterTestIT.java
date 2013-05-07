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
package org.lexgrid.loader.meta.integration.beans;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.model.Mrsab;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * The Class MrsabXmlWriterTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrsabXmlWriterTestIT extends BeanTestBase {	
	
	/** The mrsab xml writer. */
	@Autowired
	@Qualifier("mrsabXmlWriter")
	private StaxEventItemWriter<Mrsab> mrsabXmlWriter;
	
	/** The mrsab. */
	private Mrsab mrsab;
	
	/**
	 * Buildmrsab.
	 */
	@Before
	public void buildmrsab(){
		mrsab = new Mrsab();
		mrsab.setAtnl("atnl");
		mrsab.setCenc("cenc");
		mrsab.setCfr("cfr");
	}
	
	/**
	 * Test write xml.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testWriteXml() throws Exception {
		List<Mrsab> mrsabList = new ArrayList<Mrsab>();
		mrsabList.add(mrsab);
	
		File xmlFile = File.createTempFile("metaxml", ".xml");
		Resource resource = new FileSystemResource(xmlFile);
		mrsabXmlWriter.setResource(resource);
		
		mrsabXmlWriter.afterPropertiesSet();
		mrsabXmlWriter.open(new ExecutionContext());
		mrsabXmlWriter.write(mrsabList);
		
		assertTrue(resource.getFile().exists());
	}
}
/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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

import static org.junit.Assert.assertTrue;

import org.LexGrid.persistence.model.EntityAssnsToEntity;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.data.codingScheme.CodingSchemeNameSetter;
import org.lexgrid.loader.rrf.model.Mrrel;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * The Class EntityAssnsToEntityTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnsToEntityTestIT extends BeanTestBase {	
	
	/** The meta entity assoc to entity processor. */
	@Autowired
	@Qualifier("metaEntityAssocToEntityProcessor")
	private ItemProcessor<Mrrel,EntityAssnsToEntity> metaEntityAssocToEntityProcessor;
	
	/** The meta coding scheme name setter. */
	@Autowired
	@Qualifier("metaCodingSchemeNameSetter")
	private CodingSchemeNameSetter metaCodingSchemeNameSetter;
	
	/** The mrrel. */
	private Mrrel mrrel;
	
	/**
	 * Builds the mrrel.
	 */
	@Before
	public void buildMrrel(){
		mrrel = new Mrrel();
		mrrel.setAui1("a1");
		mrrel.setAui2("a2");
		mrrel.setCui1("c1");
		mrrel.setCui2("c2");
		mrrel.setDir("dir");
		mrrel.setRel("rel");
		mrrel.setRela("rela");
		mrrel.setRg("rg");
		mrrel.setRui("rui");
		mrrel.setSab("sab");
		mrrel.setSl("sl");
		mrrel.setSrui("srui");
		mrrel.setStype1("stype1");
		mrrel.setStype2("stype2");
		mrrel.setSuppress("suppress");
	}
	
	/**
	 * Test source code.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceCode() throws Exception{
		
		EntityAssnsToEntity assoc = metaEntityAssocToEntityProcessor.process(mrrel);
		String sourceCode = assoc.getSourceEntityCode();
		assertTrue(sourceCode, sourceCode.equals("c1"));
	}
	
	/**
	 * Test source code namespace.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceCodeNamespace() throws Exception{
		
		EntityAssnsToEntity assoc = metaEntityAssocToEntityProcessor.process(mrrel);
		String namespace = assoc.getSourceEntityCodeNamespace();
		assertTrue(namespace, namespace.equals(metaCodingSchemeNameSetter.getCodingSchemeName()));
	}
}

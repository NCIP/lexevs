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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.LexGrid.persistence.model.EntityAssnsToEquals;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.meta.constants.MetaLoaderConstants;
import org.lexgrid.loader.processor.EntityAssnToEQualsListProcessor;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrrel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * The Class EntityAssnsToEntityQualsTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnsToEntityQualsTestIT extends BeanTestBase {	
	
	/** The entity assn e quals processor. */
	@Autowired
	@Qualifier("entityAssnEQualsProcessor")
	private EntityAssnToEQualsListProcessor<Mrrel> entityAssnEQualsProcessor;
	
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
		
		entityAssnEQualsProcessor.setRegisterSupportedAttributes(false);
	}
	
	/**
	 * Test qual count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testQualCount() throws Exception{
		List<EntityAssnsToEquals> qualList = 
			entityAssnEQualsProcessor.process(mrrel);
	
		assertTrue(qualList.size() > 1);
	}
	
	/**
	 * Test source aui qual.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceAuiQual() throws Exception{
		List<EntityAssnsToEquals> qualList = 
			entityAssnEQualsProcessor.process(mrrel);
	
		assertTrue(this.qualiferListContainsQualifier(qualList, 
				MetaLoaderConstants.SOURCE_AUI_QUALIFIER, 
				"a1"));
	}
	
	/**
	 * Test target aui qual.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testTargetAuiQual() throws Exception{
		List<EntityAssnsToEquals> qualList = 
			entityAssnEQualsProcessor.process(mrrel);
	
		assertTrue(this.qualiferListContainsQualifier(qualList, 
				MetaLoaderConstants.TARGET_AUI_QUALIFIER, 
				"a2"));
	}
	
	/**
	 * Test source qual.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSourceQual() throws Exception{
		List<EntityAssnsToEquals> qualList = 
			entityAssnEQualsProcessor.process(mrrel);
	
		assertTrue(this.qualiferListContainsQualifier(qualList, 
				MetaLoaderConstants.SOURCE_QUALIFIER, 
				"sab"));
	}
	
	/**
	 * Test rela qual.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testRelaQual() throws Exception{
		List<EntityAssnsToEquals> qualList = 
			entityAssnEQualsProcessor.process(mrrel);
	
		assertTrue(this.qualiferListContainsQualifier(qualList, 
				RrfLoaderConstants.RELA_QUALIFIER, 
				"rela"));
	}
	
	/**
	 * Test null rela qual.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testNullRelaQual() throws Exception{
		mrrel.setRela(null);
		
		List<EntityAssnsToEquals> qualList = 
			entityAssnEQualsProcessor.process(mrrel);
	
		assertFalse(this.qualiferListContainsQualifier(qualList, 
				RrfLoaderConstants.RELA_QUALIFIER, 
				"rela"));
	}
	
	/**
	 * Test empty rela qual.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testEmptyRelaQual() throws Exception{
		mrrel.setRela("");
		
		List<EntityAssnsToEquals> qualList = 
			entityAssnEQualsProcessor.process(mrrel);
	
		assertFalse(this.qualiferListContainsQualifier(qualList, 
				RrfLoaderConstants.RELA_QUALIFIER, 
				"rela"));
	}
	
	/**
	 * Test blank rela qual.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testBlankRelaQual() throws Exception{
		mrrel.setRela("  ");
		
		List<EntityAssnsToEquals> qualList = 
			entityAssnEQualsProcessor.process(mrrel);
	
		assertFalse(this.qualiferListContainsQualifier(qualList, 
				RrfLoaderConstants.RELA_QUALIFIER, 
				"rela"));
	}
	
	/**
	 * Test self referencing qual.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSelfReferencingQual() throws Exception{
		mrrel.setCui1("cui");
		mrrel.setCui2("cui");
		
		List<EntityAssnsToEquals> qualList = 
			entityAssnEQualsProcessor.process(mrrel);
	
		assertTrue(this.qualiferListContainsQualifier(qualList, 
				MetaLoaderConstants.SELF_REFERENCING_QUALIFIER,
				MetaLoaderConstants.SELF_REFERENCING_QUALIFIER_TRUE_VALUE));
	}
	
	/**
	 * Test no self referencing qual.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testNoSelfReferencingQual() throws Exception{
		mrrel.setCui1("cui1");
		mrrel.setCui2("cui2");
		
		List<EntityAssnsToEquals> qualList = 
			entityAssnEQualsProcessor.process(mrrel);
	
		assertFalse(this.qualiferListContainsQualifier(qualList, 
				MetaLoaderConstants.SELF_REFERENCING_QUALIFIER,
				MetaLoaderConstants.SELF_REFERENCING_QUALIFIER_TRUE_VALUE));
	}
	
	/**
	 * Qualifer list contains qualifier.
	 * 
	 * @param qualList the qual list
	 * @param qualifierName the qualifier name
	 * @param qualifierValue the qualifier value
	 * 
	 * @return true, if successful
	 */
	protected boolean qualiferListContainsQualifier(List<EntityAssnsToEquals> qualList, String qualifierName, String qualifierValue){
		for(EntityAssnsToEquals qual : qualList){
			if(qual.getId().getQualifierName().equals(qualifierName) &&
			qual.getId().getQualifierValue().equals(qualifierValue)){
				return true;
			}
		}
		return false;
	}
}

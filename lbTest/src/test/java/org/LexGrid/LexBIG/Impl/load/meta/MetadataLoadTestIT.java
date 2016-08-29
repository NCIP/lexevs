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
package org.LexGrid.LexBIG.Impl.load.meta;

import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class MetadataLoadTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetadataLoadTestIT extends DataLoadTestBase {

	/** The lbsm. */
	private LexBIGServiceMetadata lbsm;
	
	/**
	 * Sets the up metadata service.
	 * 
	 * @throws LBException the LB exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		lbsm = lbs.getServiceMetadata();
	}
	
	/**
	 * Test metadata load count.
	 * 
	 * @throws LBException the LB exception
	 */
	@Test
	public void testMetadataLoadCount() throws LBException{
		//TODO needs a replacement for ChainedFilter in AbstractLazyCodeHolderFactory;
		assertTrue(lbsm.listCodingSchemes().getAbsoluteCodingSchemeVersionReferenceCount() > 0);
	}
	
	/**
	 * Test check metadata entries for meta.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testCheckMetadataEntriesForMeta() throws Exception {
		AbsoluteCodingSchemeVersionReference ref = 
			Constructors.createAbsoluteCodingSchemeVersionReference(
					LexBIGServiceTestCase.META_URN, LexBIGServiceTestCase.SAMPLE_META_VERSION);
		lbsm.restrictToCodingScheme(ref);
		MetadataPropertyList mdpl = lbsm.resolve();
		assertTrue(mdpl.getMetadataPropertyCount() > 0);
	}
	
	/**
	 * Test metadata properties.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testMetadataProperties() throws Exception {
		AbsoluteCodingSchemeVersionReference ref = 
			Constructors.createAbsoluteCodingSchemeVersionReference(
					LexBIGServiceTestCase.META_URN, LexBIGServiceTestCase.SAMPLE_META_VERSION);
		lbsm.restrictToCodingScheme(ref);
		lbsm.restrictToProperties(new String[]{"rsab"});
		
		MetadataPropertyList mdpl = lbsm.resolve();
		assertTrue(mdpl.getMetadataPropertyCount() > 0);
	}
	
	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(MetadataLoadTestIT.class);  
	}  
}
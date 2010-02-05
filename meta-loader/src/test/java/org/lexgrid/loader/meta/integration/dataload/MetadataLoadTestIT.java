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
package org.lexgrid.loader.meta.integration.dataload;

import static org.junit.Assert.assertTrue;

import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;
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
	public void setUpMetadataService() throws LBException{
		lbsm = lbs.getServiceMetadata();
	}
	
	/**
	 * Test metadata load count.
	 * 
	 * @throws LBException the LB exception
	 */
	@Test
	public void testMetadataLoadCount() throws LBException{
		assertTrue(lbsm.listCodingSchemes().getAbsoluteCodingSchemeVersionReferenceCount() == 1);
	}
	
	/**
	 * Test check metadata entries for meta.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testCheckMetadataEntriesForMeta() throws Exception {
		AbsoluteCodingSchemeVersionReference ref = 
			lbsm.listCodingSchemes().getAbsoluteCodingSchemeVersionReference(0);
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
			lbsm.listCodingSchemes().getAbsoluteCodingSchemeVersionReference(0);
		lbsm.restrictToCodingScheme(ref);
		lbsm.restrictToProperties(new String[]{"rsab"});
		
		MetadataPropertyList mdpl = lbsm.resolve();
		assertTrue(mdpl.getMetadataPropertyCount() > 0);
	}
}

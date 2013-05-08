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
package org.LexGrid.LexBIG.Impl.load.owl;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

/**
 * The Class DataLoadTestBase.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DataLoadTestBase extends TestCase{
	
	/** The lbs. */
	protected LexBIGService lbs;
	protected CodedNodeSet cns;
	protected CodedNodeGraph cng;
	
	/**
	 * Sets the up lbs.
	 * @throws LBException 
	 */
	@Before
	public void setUp() throws Exception{
		lbs = ServiceHolder.instance().getLexBIGService();
		cns = lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.NPO_SCHEME_URI, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.NPO_SCHEME_VERSION));
		cng = lbs.getNodeGraph(LexBIGServiceTestCase.NPO_SCHEME_URI, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.NPO_SCHEME_VERSION), null);
	}
	
	public CodedNodeSet getCodedNodeSet() throws Exception {
		setUp();
		return lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.NPO_SCHEME_URI, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.NPO_SCHEME_VERSION));
	}
	
	@Test
	public void testSetUp() throws Exception {
		Assert.noNullElements(new Object[] {lbs,cns,cng});
	}
}
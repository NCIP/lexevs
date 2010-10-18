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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

/**
 * The Class PrensentationPropertyDataTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class TuiPropertyDataTestIT extends DataLoadTestBase {
	
	/** The test entity. */
	private Entity testEntity;

	/**
	 * Builds the test entity.
	 * 
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		cns.restrictToCodes(Constructors.createConceptReferenceList("ACRMG"));
		ResolvedConceptReference rcr1 = cns.resolveToList(null, null, null, -1).getResolvedConceptReference()[0];
	
		testEntity = rcr1.getEntity();
	}
	
	@Test
	public void testPropertyNotNull() throws Exception {	
		assertNotNull(testEntity.getProperty());
	}
	
	
	@Test
	public void testTuiPopertyCount() throws Exception {	
		List<Property> tuiProps = DataTestUtils.getPropertiesFromEntity(testEntity, RrfLoaderConstants.TUI_PROPERTY);
		assertTrue(tuiProps.size() == 1);
	}
	
	@Test
	public void testTuiPopertyName() throws Exception {	
		List<Property> tuiProps = DataTestUtils.getPropertiesFromEntity(testEntity, RrfLoaderConstants.TUI_PROPERTY);
		assertTrue(tuiProps.get(0).getPropertyName().equals(RrfLoaderConstants.TUI_PROPERTY));
	}
	
	@Test
	public void testTuiPopertyValue() throws Exception {	
		List<Property> tuiProps = DataTestUtils.getPropertiesFromEntity(testEntity, RrfLoaderConstants.TUI_PROPERTY);
		assertTrue(tuiProps.get(0).getValue().getContent().equals("T047"));
	}
	
	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(TuiPropertyDataTestIT.class);  
	}  
}
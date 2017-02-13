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

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.concepts.Entity;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class GenericPropertySourceQualifierTestIT
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class GenericPropertyQueryBySourceOnlyTest extends DataLoadTestBase {
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		LocalNameList sourceList = Constructors.createLocalNameList("MSH");
		cns.restrictToProperties(null, new PropertyType[]{PropertyType.PRESENTATION}, sourceList, null, null);

	}
	
	@Test
	public void testQueryBySourceAndQual() throws Exception {
		ResolvedConceptReference[] rcr1 = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
		
	
		assertTrue(rcr1.length > 0);

	}
}
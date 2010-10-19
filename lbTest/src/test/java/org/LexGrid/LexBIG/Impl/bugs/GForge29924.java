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
package org.LexGrid.LexBIG.Impl.bugs;


import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedAssociation;

public class GForge29924 extends LexBIGServiceTestCase {
    final static String testID = "GForge29924";
    
    @Override
    protected String getTestID() {
        return testID;
    }

/**
     * 
     * 
     * GForge #29924
     * https://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=29924&group_id=491&atid=1850
     * 
     * @throws Throwable
     */
public void testSupportedAssociationLocalid() throws Throwable {
    	LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
    	CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(CAMERA_SCHEME_MANIFEST_VERSION);
    	CodingScheme cs = lbs.resolveCodingScheme(CAMERA_SCHEME_MANIFEST_URN, csvt);
    	for (SupportedAssociation sa :cs.getMappings().getSupportedAssociation()) {
    		assertEquals(sa.getLocalId().contains(" "), false);
    	}
    }
}
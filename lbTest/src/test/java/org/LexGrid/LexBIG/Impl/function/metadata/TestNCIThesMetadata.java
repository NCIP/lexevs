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
package org.LexGrid.LexBIG.Impl.function.metadata;

import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.MetadataProperty;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;

/**
 * Tests Metadata search for NCI Thes.
 * 
 * @author Kevin Peterson
 * 
 */
public class TestNCIThesMetadata extends LexBIGServiceTestCase {

    final static String testID = "T1_FNC_70";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_50() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();

        LexBIGServiceMetadata smd = lbsi.getServiceMetadata();

        AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
        acsvr.setCodingSchemeURN(META_URN);
        acsvr.setCodingSchemeVersion(SAMPLE_META_VERSION);
        smd.restrictToCodingScheme(acsvr);

        MetadataPropertyList mdpl = smd.resolve();
        Iterator<? extends MetadataProperty> metaItr = mdpl.iterateMetadataProperty();
        assertTrue("Did not find the Metadata for the Coding Scheme", metaItr.hasNext());

        // Look through the Metadata and make sure its returning the coding
        // schemes...
        // Here we check a few.
        boolean foundLoinc = false;
        boolean foundMedra = false;
        while (metaItr.hasNext()) {
            MetadataProperty property = metaItr.next();
           
            if (property.getName().equals("rsab") && property.getValue().equals("LNC")) {
            	foundLoinc = true;
            }
            if (property.getName().equals("rsab") && property.getValue().equals("MDR")) {
                foundMedra = true;
            }
           
        }
        assertTrue("Didn't find the Metadata that was expected", foundLoinc);
        assertTrue("Didn't find the Metadata that was expected", foundMedra);
    }
}
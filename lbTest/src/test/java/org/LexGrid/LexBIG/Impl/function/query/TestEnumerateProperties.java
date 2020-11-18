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
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_29	TestEnumerateProperties

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;

public class TestEnumerateProperties extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_29";

    @Override
    protected String getTestID() {
        return testID;
    }

    /**
     * Enumerate properties for the code.
     * 
     * @param code
     * @param ServiceHolder
     *            .instance().getLexBIGService()
     * @throws LBException
     */
    protected void enumerateProps(String code, String[] properties) throws LBException {
        // Perform the query ...
        ConceptReferenceList crefs = ConvenienceMethods.createConceptReferenceList(new String[] { code }, THES_SCHEME);

        ResolvedConceptReferenceList matches = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(
                THES_SCHEME, null).restrictToCodes(crefs).resolveToList(null, null, null, 1);

        // Analyze the result ...
        assertTrue(matches.getResolvedConceptReferenceCount() > 0);
        ResolvedConceptReference ref = (ResolvedConceptReference) matches.enumerateResolvedConceptReference()
                .nextElement();
        Entity node = ref.getEntity();

        // Print properties (definitions, comments, presentations
        // are not printed but would be similarly handled)
        Property[] props = node.getProperty();
        assertTrue(props.length > 0);
        assertTrue(contains(props, properties));

    }

    public boolean contains(Property[] props, String[] property) {
        if (props.length != property.length) {
            return false;
        }
        for (int i = 0; i < property.length; i++) {
            boolean found = false;
            for (int j = 0; j < props.length; j++) {
                if (props[j].getPropertyName().equals(property[i])) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }

        return true;
    }

    public void testT1_FNC_29a() throws LBException {
        enumerateProps("External_Lip", new String[] { "label", "Semantic_Type", "code" });
    }

    public void testT1_FNC_29b() throws LBException {
        enumerateProps("Bone_of_the_Extremity", new String[] { "label", "Semantic_Type", "code" });
    }

}
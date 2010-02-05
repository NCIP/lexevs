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
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_07	TestDAGWalking

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

public class TestDAGWalking extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_07";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_07() throws LBException {
        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(THES_SCHEME, null, "roles");
        cng.restrictToAssociations(Constructors.createNameAndValueList(new String[] { "subClassOf",
                "Anatomic_Structure_is_Physical_Part_of" }), null);

        ResolvedConceptReference[] rcr = cng.resolveAsList(Constructors.createConceptReference("Bone", THES_SCHEME),
                true, true, 1, 1, null, null, null, 0).getResolvedConceptReference();

        // check for some target (up) codes that I know should be there.
        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getConceptCode().equals("Bone"));

        Association[] a = rcr[0].getTargetOf().getAssociation();

       
        assertTrue(contains(a, "subClassOf", "Vertebra"));
        assertTrue(contains(a, "subClassOf", "Bone_of_the_Extremity"));

        // check for some source (down) codes that I know should be there
        a = rcr[0].getSourceOf().getAssociation();

        assertTrue(contains(a, "Anatomic_Structure_is_Physical_Part_of", "Skeletal_System"));;
        assertTrue(contains(a, "subClassOf", "Musculoskeletal_System_Part"));

        // go down one more level from one of the codes.
        ConvenienceMethods cm = new ConvenienceMethods(ServiceHolder.instance().getLexBIGService());
        a = new Association[] { cm.getAssociationForwardOneLevel("Vertebra", "roles", "subClassOf", THES_SCHEME, null, false, null) };

        contains(a, "subClassOf", "Thoracic_Vertebra");

        // go up one more level from one of the codes.
        a = new Association[] { cm.getAssociationReverseOneLevel("Musculoskeletal_System_Part", "roles", "subClassOf", THES_SCHEME, null, false, null) };

        contains(a, "subClassOf", "Body_Part");

    }

    private boolean contains(Association[] a, String association, String conceptCode) {
        boolean found = false;
        for (int i = 0; i < a.length; i++) {
            if (a[i].getAssociationName().equals(association)
                    && contains(a[i].getAssociatedConcepts().getAssociatedConcept(), conceptCode)) {
                found = true;
                break;
            }
        }

        return found;
    }

    private boolean contains(AssociatedConcept[] ac, String conceptCode) {
        boolean found = false;
        for (int i = 0; i < ac.length; i++) {
            if (ac[i].getConceptCode().equals(conceptCode)) {
                found = true;
                break;
            }

        }

        return found;
    }

}
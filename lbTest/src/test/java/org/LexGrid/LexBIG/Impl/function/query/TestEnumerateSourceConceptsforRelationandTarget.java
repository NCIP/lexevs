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

// LexBIG Test ID: T1_FNC_06	TestEnumerateSourceConceptsforRelationandTarget

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

public class TestEnumerateSourceConceptsforRelationandTarget extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_06";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_06() throws LBException {

        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(THES_SCHEME, null, null);

        cng.restrictToAssociations(Constructors.createNameAndValueList("Anatomic_Structure_Has_Location"), null);

        ConvenienceMethods cm = new ConvenienceMethods(ServiceHolder.instance().getLexBIGService());

        cng = cng.restrictToTargetCodes(cm.createCodedNodeSet(new String[] { "Membranous_Labyrinth" }, THES_SCHEME, null));

        ResolvedConceptReference[] rcr = cng.resolveAsList(Constructors.createConceptReference("Membranous_Labyrinth", THES_SCHEME),
                false, true, 1, 1, null, null, null, 0).getResolvedConceptReference();

        AssociatedConcept[] ac = rcr[0].getTargetOf().getAssociation()[0].getAssociatedConcepts()
                .getAssociatedConcept();

        assertEquals(1,ac.length);

    }

}
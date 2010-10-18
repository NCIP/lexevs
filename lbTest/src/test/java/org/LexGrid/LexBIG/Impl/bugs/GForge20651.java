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

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Entity;

/**
 * This class should be used as a place to write JUnit tests which show a bug,
 * and pass when the bug is fixed.
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 */
public class GForge20651 extends LexBIGServiceTestCase {
    final static String testID = "GForge20651";

    private CodedNodeSet cns = null;
    private CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
    
    @Override
    protected String getTestID() {
        return testID;
    }

    /*
     * LexBIG Bug #20651 -
     * https://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=20651&group_id=491&atid=1850
     */
    public void setUp(){
        try {
            LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();      
            csvt.setVersion(LexBIGServiceTestCase.AMINOACID_VERSION);   
            cns = lbsi.getCodingSchemeConcepts(LexBIGServiceTestCase.AMINOACID_SCHEME, csvt);
        } catch (Exception e) {
          fail(e.getMessage());
        }
    }
 
    public void testConceptHasCommentProperty() throws Exception {
        ConceptReferenceList codeList = Constructors.createConceptReferenceList("Size");
        cns.restrictToCodes(codeList);
        ResolvedConceptReferenceList rcrl= cns.resolveToList(null, null, null, -1);
        if (rcrl.getResolvedConceptReferenceCount() > 0) {
            ResolvedConceptReference rcr[]= rcrl.getResolvedConceptReference();
            Entity concept= rcr[0].getReferencedEntry();
            Comment comments[] = concept.getComment();
            assertNotNull(comments);
            assertTrue(comments.length > 0);
            assertTrue(comments[0].getValue().getContent().equalsIgnoreCase("The different size options"));
            
        } else {
            fail("Could not find concept");
        }
        
    }
   
 
}
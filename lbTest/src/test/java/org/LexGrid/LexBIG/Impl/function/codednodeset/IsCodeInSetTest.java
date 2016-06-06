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
package org.LexGrid.LexBIG.Impl.function.codednodeset;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;

/**
 * The Class IsCodeInSetTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RemoteApiSafeTest
public class IsCodeInSetTest extends BaseCodedNodeSetTest {
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.codednodeset.BaseCodedNodeSetTest#getTestID()
     */
    @Override
    protected String getTestID() {
        return "IsCodeInSetTest Union Test";
    }
    
   /**
    * Test is code in set.
    * 
    * @throws LBInvocationException the LB invocation exception
    * @throws LBParameterException the LB parameter exception
    */
   public void testIsCodeInSet() throws LBInvocationException, LBParameterException{
       assertTrue(
               cns.isCodeInSet(
                       Constructors.createConceptReference("A0001", LexBIGServiceTestCase.AUTO_SCHEME )));
   }
   
   /**
    * Test is code not in set.
    * 
    * @throws LBInvocationException the LB invocation exception
    * @throws LBParameterException the LB parameter exception
    */
   public void testIsCodeNotInSet() throws LBInvocationException, LBParameterException{
       assertFalse(
               cns.isCodeInSet(
                       Constructors.createConceptReference("BOGUS_CODE", LexBIGServiceTestCase.AUTO_SCHEME )));
   }
   
   /**
    * Test is code in set with restriction.
    * 
    * @throws LBInvocationException the LB invocation exception
    * @throws LBParameterException the LB parameter exception
    */
   public void testIsCodeInSetWithRestriction() throws LBInvocationException, LBParameterException{
       cns = cns.restrictToCodes(Constructors.createConceptReferenceList("A0001"));
       assertTrue(
               cns.isCodeInSet(
                       Constructors.createConceptReference("A0001", LexBIGServiceTestCase.AUTO_SCHEME )));
   }
   
   /**
    * Test is code not in set with restriction.
    * 
    * @throws LBInvocationException the LB invocation exception
    * @throws LBParameterException the LB parameter exception
    */
   public void testIsCodeNotInSetWithRestriction() throws LBInvocationException, LBParameterException{
       cns = cns.restrictToCodes(Constructors.createConceptReferenceList("T0001"));
       assertFalse(
               cns.isCodeInSet(
                       Constructors.createConceptReference("A0001", LexBIGServiceTestCase.AUTO_SCHEME )));
   }
}
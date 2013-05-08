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
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

import junit.framework.TestCase;


public class TestRemoveMrMap2Loads extends TestCase {
	public void testRemoveMrMapOneMapping() throws LBException, LBInvocationException{
        LexBIGServiceManager lbsm = getLexBIGServiceManager();;

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:CL413321.MDR.CST", "200909");
        AbsoluteCodingSchemeVersionReference b = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:CL413320.MDR.ICD9CM", "200909");
        
        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.deactivateCodingSchemeVersion(b, null);
        lbsm.removeCodingSchemeVersion(a);
        lbsm.removeCodingSchemeVersion(b);
	}
	
    private LexBIGServiceManager getLexBIGServiceManager() throws LBParameterException, LBInvocationException{
    	return LexBIGServiceImpl.defaultInstance().getServiceManager(null);
    }
}
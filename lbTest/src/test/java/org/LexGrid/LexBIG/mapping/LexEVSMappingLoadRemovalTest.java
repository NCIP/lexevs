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
package org.LexGrid.LexBIG.mapping;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.LexEVSAuthoringServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;

import junit.framework.TestCase;

public class LexEVSMappingLoadRemovalTest extends TestCase {

	   LexBIGService lbs;
		LexBIGServiceManager lbsm;
		CodingSchemeVersionOrTag csvt;
	   public void setUp(){

;
		   lbs = LexBIGServiceImpl.defaultInstance();
		   csvt = new CodingSchemeVersionOrTag();
			 csvt.setVersion("1.0");

		   try {
			lbsm = lbs.getServiceManager(null);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
	
	public void testMappingRemoval()throws LBException {
	
				AbsoluteCodingSchemeVersionReference scheme = new AbsoluteCodingSchemeVersionReference();
				scheme.setCodingSchemeURN("urn:oid:11.11.0.2");
				scheme.setCodingSchemeVersion("2.0");
				lbsm.deactivateCodingSchemeVersion(scheme, null);
				lbsm.removeCodingSchemeVersion(scheme);
	
		   }
}
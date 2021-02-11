
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

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
	public void testRemoveMrMap2Mapping() throws LBException, LBInvocationException{
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
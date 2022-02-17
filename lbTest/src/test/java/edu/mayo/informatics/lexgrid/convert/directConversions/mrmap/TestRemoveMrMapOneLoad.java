
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

import junit.framework.TestCase;

public class TestRemoveMrMapOneLoad extends TestCase {

	public void testRemoveMrMapOneMapping() throws LBException, LBInvocationException{
        LexBIGServiceManager lbsm = getLexBIGServiceManager();;

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:CL413321.MDR.CST", "200909");

        lbsm.deactivateCodingSchemeVersion(a, null);

        lbsm.removeCodingSchemeVersion(a);
	}
	
    private LexBIGServiceManager getLexBIGServiceManager() throws LBParameterException, LBInvocationException{
    	return LexBIGServiceImpl.defaultInstance().getServiceManager(null);
    }
}
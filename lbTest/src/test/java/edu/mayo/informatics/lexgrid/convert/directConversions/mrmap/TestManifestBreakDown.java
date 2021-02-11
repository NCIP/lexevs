
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

import junit.framework.TestCase;

public class TestManifestBreakDown extends TestCase {

    public void testBreakDown() throws LBException{
        LexBIGServiceManager lbsm = getLexBIGServiceManager();;

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "http://MDR.test.shell", "1.1");

        lbsm.deactivateCodingSchemeVersion(a, null);

        lbsm.removeCodingSchemeVersion(a);
        AbsoluteCodingSchemeVersionReference b = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "http://CST.test.shell", "1.1");

        lbsm.deactivateCodingSchemeVersion(b, null);

        lbsm.removeCodingSchemeVersion(b);
    }
    
  private LexBIGServiceManager getLexBIGServiceManager() throws LBException{
	return LexBIGServiceImpl.defaultInstance().getServiceManager(null);
}
}
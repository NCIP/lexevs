
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;
import java.io.File;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexOnt.CodingSchemeManifest;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;
import junit.framework.TestCase;


public class TestManifestSetup extends TestCase {
	LexBIGService svc;

	public void testSetUP() throws LBException, InterruptedException, LgConvertException{
		svc = LexBIGServiceImpl.defaultInstance();
		LexBIGServiceManager lbsm = null;
		LexGridMultiLoaderImpl loader = null;

			lbsm = getLexBIGServiceManager();
			loader = (LexGridMultiLoaderImpl) lbsm.getLoader("LexGrid_Loader");
			loader.load(new File(
					"resources/testData/mrmap_mapping/CST_shell.xml")
					.toURI(), true, true);
			while (loader.getStatus().getEndTime() == null) {

				Thread.sleep(500);
			}

			loader = (LexGridMultiLoaderImpl) lbsm.getLoader("LexGrid_Loader");
			loader.load(new File(
					"resources/testData/mrmap_mapping/MedDRA_test.xml")
					.toURI(), true, true);
			while (loader.getStatus().getEndTime() == null) {

				Thread.sleep(500);
			}


        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "http://MDR.test.shell", "1.1");

        AbsoluteCodingSchemeVersionReference b = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "http://CST.test.shell", "1.1");
		lbsm.activateCodingSchemeVersion(a);
		lbsm.activateCodingSchemeVersion(b);
		lbsm.setVersionTag(a, "PRODUCTION");
		lbsm.setVersionTag(b, "PRODUCTION");

		  URNVersionPair pair = new URNVersionPair(
	                "urn:oid:CL413321.MDR.CST", "200909");
		ManifestUtil manifestService = new ManifestUtil();
		CodingSchemeManifest manifest = manifestService.getManifest(new File("resources/testData/mrmap_mapping/MRMAP_manifest.xml").toURI());
		manifestService.applyManifest(manifest, pair);
	}

	
	
    private LexBIGServiceManager getLexBIGServiceManager() throws LBException{
    	return svc.getServiceManager(null);
    }
}
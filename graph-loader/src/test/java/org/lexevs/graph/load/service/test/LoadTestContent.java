package org.lexevs.graph.load.service.test;

import java.io.File;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.OWL_Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.LexOnt.CsmfCodingSchemeURI;

public class LoadTestContent {
	
	public  void run() throws LBException, InterruptedException {

    LexBIGServiceManager lbsm = getLexBIGServiceManager();

    OWL_Loader loader = (OWL_Loader) lbsm.getLoader("OWLLoader");
    
    loader.load(new File("src/test/resources/testData/sample.owl").toURI(), null, 1, false, true);

    while (loader.getStatus().getEndTime() == null) {
        Thread.sleep(500);
    }

    lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

    lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());

	}
	
    private LexBIGServiceManager getLexBIGServiceManager() throws LBException {
		LexBIGService svc = LexBIGServiceImpl.defaultInstance();
    	return svc.getServiceManager(null);
    }
    
    public void cleanup() throws LBException{
    	
    	LexBIGServiceManager lbsm = getLexBIGServiceManager();
    	AbsoluteCodingSchemeVersionReference codingSchemeVersion = new AbsoluteCodingSchemeVersionReference();
		codingSchemeVersion.setCodingSchemeURN(GLTestConstants.THES_SCHEME_URI);
		codingSchemeVersion.setCodingSchemeVersion(GLTestConstants.THES_SCHEME_VERSION);
		lbsm.deactivateCodingSchemeVersion(codingSchemeVersion, null);
		lbsm.removeCodingSchemeVersion(codingSchemeVersion );
    	
    }
    
	public static void main(String[] args) {
		try {
 			new LoadTestContent().run();
//			new LoadTestContent().cleanup();
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

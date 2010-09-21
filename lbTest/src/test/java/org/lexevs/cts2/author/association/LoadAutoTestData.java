package org.lexevs.cts2.author.association;

import java.io.File;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants;

import junit.framework.TestCase;

public class LoadAutoTestData extends TestCase {
	public void testLoadData() throws LBException, InterruptedException {
    LexBIGServiceManager lbsm = getLexBIGServiceManager();

	LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
	.getLoader("LexGrid_Loader");

	loader
	.load(new File(
			"resources/testData/cts2/Cts2Automobiles.xml")
			.toURI(), true, true);
    while (loader.getStatus().getEndTime() == null) {
        Thread.sleep(500);
    }

	LexGridMultiLoaderImpl loader1 = (LexGridMultiLoaderImpl) lbsm
	.getLoader("LexGrid_Loader");
	loader1
	.load(new File(
			"resources/testData/cts2/German_Made_Parts.xml")
			.toURI(), true, true);
    while (loader1.getStatus().getEndTime() == null) {
        Thread.sleep(500);
    }
    lbsm.activateCodingSchemeVersion(Constructors.createAbsoluteCodingSchemeVersionReference(
    		AuthoringConstants.TARGET_URN, AuthoringConstants.TARGET_VERSION));
    lbsm.activateCodingSchemeVersion(Constructors.createAbsoluteCodingSchemeVersionReference(
    		AuthoringConstants.SOURCE_URN, AuthoringConstants.SOURCE_VERSION));
    lbsm.setVersionTag(Constructors.createAbsoluteCodingSchemeVersionReference(
    		AuthoringConstants.TARGET_URN, AuthoringConstants.TARGET_VERSION), LBConstants.KnownTags.PRODUCTION.toString());
    lbsm.setVersionTag(Constructors.createAbsoluteCodingSchemeVersionReference(
    		AuthoringConstants.SOURCE_URN, AuthoringConstants.SOURCE_VERSION),LBConstants.KnownTags.PRODUCTION.toString());
}
    private LexBIGServiceManager getLexBIGServiceManager() throws LBParameterException, LBInvocationException{
    	return LexBIGServiceImpl.defaultInstance().getServiceManager(null);
    }

}

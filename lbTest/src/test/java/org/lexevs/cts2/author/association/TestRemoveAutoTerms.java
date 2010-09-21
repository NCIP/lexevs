package org.lexevs.cts2.author.association;

import java.net.URISyntaxException;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;

public class TestRemoveAutoTerms extends TestCase {
	public static void testRemove() throws LBException, URISyntaxException{
		
		LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
		AbsoluteCodingSchemeVersionReference ref = 
			Constructors.createAbsoluteCodingSchemeVersionReference(
					AuthoringConstants.SOURCE_URN, AuthoringConstants.SOURCE_VERSION);
		
		lbs.getServiceManager(null).deactivateCodingSchemeVersion(ref, null);
		
		lbs.getServiceManager(null).removeCodingSchemeVersion(ref);
		
		ref = 
			Constructors.createAbsoluteCodingSchemeVersionReference(
					AuthoringConstants.TARGET_URN, AuthoringConstants.TARGET_VERSION);
		
		lbs.getServiceManager(null).deactivateCodingSchemeVersion(ref, null);
		
		lbs.getServiceManager(null).removeCodingSchemeVersion(ref);
		
//		AuthoringService authServ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
//		assertTrue(authServ.removeRevisionRecordbyId("NEW_MAPPING"));
//		assertTrue(authServ.removeRevisionRecordbyId("TestNewForExistingCTSMapping"));
//		assertTrue(authServ.removeRevisionRecordbyId("TestModifyForUpdatePredicate"));
//		assertTrue(authServ.removeRevisionRecordbyId("TestNewForExistingScheme"));
		
	}

}

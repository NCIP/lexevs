package org.lexevs.cts2.author.association;

import java.net.URISyntaxException;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.author.CodeSystemAuthoringOperation;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;

import junit.framework.TestCase;

public class TestRemoveCreatedMapping extends TestCase {
	
	public void testRemoveCodeSystem()   throws LBException, URISyntaxException{	
		
		//String randomID = getRevId();
		

		LexBIGService lbs = LexBIGServiceImpl.defaultInstance();

		AbsoluteCodingSchemeVersionReference ref =
			Constructors.createAbsoluteCodingSchemeVersionReference(
					AuthoringConstants.MAPPING_URN, AuthoringConstants.MAPPING_VERSION);
		
		lbs.getServiceManager(null).deactivateCodingSchemeVersion(ref, null);
		
		lbs.getServiceManager(null).removeCodingSchemeVersion(ref);

		
	}	

}

package org.lexevs.cts2.author.association;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;

import junit.framework.TestCase;

public class TestRemoveRevisions extends TestCase {
public void testRemoveRevisionsCleanUP() throws LBException{
	AuthoringService authServ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
	assertTrue(authServ.removeRevisionRecordbyId("NEW_MAPPING"));
	assertTrue(authServ.removeRevisionRecordbyId("TestNewForExistingCTSMapping"));
	assertTrue(authServ.removeRevisionRecordbyId("TestModifyForUpdatePredicate"));
	assertTrue(authServ.removeRevisionRecordbyId("TestNewForExistingScheme"));
}
}

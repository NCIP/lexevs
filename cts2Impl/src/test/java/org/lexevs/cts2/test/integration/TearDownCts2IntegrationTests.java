package org.lexevs.cts2.test.integration;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Test;
import org.lexevs.cts2.test.Cts2BaseTest;
import org.lexevs.cts2.test.Cts2TestConstants;

public class TearDownCts2IntegrationTests extends Cts2BaseTest {

	@Test
	public void tearDown() throws LBException {
		AbsoluteCodingSchemeVersionReference ref = 
			Constructors.createAbsoluteCodingSchemeVersionReference(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI,
				Cts2TestConstants.CTS2_AUTOMOBILES_VERSION);
		super.getLexBIGService().
			getServiceManager(null).
				deactivateCodingSchemeVersion(
						ref, null);
		
		super.getLexBIGService().
			getServiceManager(null).removeCodingSchemeVersion(ref);
	}
}

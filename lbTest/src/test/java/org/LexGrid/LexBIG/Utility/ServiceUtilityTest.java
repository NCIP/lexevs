package org.LexGrid.LexBIG.Utility;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.junit.Test;

public class ServiceUtilityTest extends LexBIGServiceTestCase {

	//TODO Write tests for all other methods in this class
	String testId = "ServiceUtilityTest";
	
	@Test
	public void testGetUriForName() throws LBParameterException {
		
		String uri = ServiceUtility.getUriForCodingSchemeName(AUTO_SCHEME);
		assertEquals(uri, AUTO_URN);
	}

	@Override
	protected String getTestID() {

		return testId;
	}

}

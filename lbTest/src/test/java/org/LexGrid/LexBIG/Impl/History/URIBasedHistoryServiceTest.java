
package org.LexGrid.LexBIG.Impl.History;

import static org.junit.Assert.*;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.versions.SystemRelease;
import org.junit.Before;
import org.junit.Test;

public class URIBasedHistoryServiceTest {
	UriBasedHistoryServiceImpl service;
	@Before
	public void setUp() throws Exception {
		service = new UriBasedHistoryServiceImpl("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl");
	}

	//Basically testing to make sure the load works for this URL.  Loader is modified for testing purposes.
	@Test
	public void test() throws LBInvocationException {
		SystemRelease sysr = service.getLatestBaseline();
		assertNotNull(sysr);
	}

}
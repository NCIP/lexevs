package org.lexevs.graph.load.service.test;

import static org.junit.Assert.*;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CleanupContentTestITTest {

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void cleanUpTestIT() {
		LoadTestContent load = new LoadTestContent();
		try {
			load.cleanup();
		} catch (LBException e) {
			fail("clean up failed");
			e.printStackTrace();
		}
	}

}

package org.lexevs.graph.load.service.test;

import static org.junit.Assert.*;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.junit.Test;

public class LoadTestContentTestIT {


	@Test
	public void loadTestContentTest() {
		LoadTestContent testload = new LoadTestContent();
		try {
			testload.run();
		} catch (LBException e) {
			fail("LBException");
			e.printStackTrace();
		} catch (InterruptedException e) {
			fail("Interupt exception");
			e.printStackTrace();
		}
		
	}


}

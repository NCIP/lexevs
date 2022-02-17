
package util.integration;

import org.junit.Test;

import test.rrf.BaseTestRrf;

public class CleanIntegration {
	
	@Test
	public void clean() throws Exception {	
		BaseTestRrf.cleanUpAfter();
	}
}
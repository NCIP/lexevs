
package util.integration;

import org.junit.Test;

import test.rrf.BaseTestRrf;

public class MetaCleanIntegration {
	
	@Test
	public void clean() throws Exception {	
		BaseTestRrf.cleanUpAfter();
	}
}
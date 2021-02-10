
package org.lexgrid.loader.logging;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.lexgrid.loader.test.LoaderFrameworkCoreTestBase;
import org.springframework.beans.factory.annotation.Autowired;

public class LgMessageDirectorIFFactoryTest extends LoaderFrameworkCoreTestBase 
{
	@Autowired
	private StatusTrackingLogger logger;
	
	@Test
	public void testLogger()
	{
		assertNotNull(logger);
	}
}
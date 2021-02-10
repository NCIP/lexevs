
package org.lexgrid.loader.listener;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.lexgrid.loader.staging.StagingManager;
import org.lexgrid.loader.test.LoaderFrameworkCoreTestBase;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;

public class SetupListenerTest extends LoaderFrameworkCoreTestBase
{
	@Autowired
	private SetupListener setupListener;
	
	@Test
	public void testSetupListener()
	{
		assertNotNull(setupListener);
	}
	
	@Test
	public void testBeforeJob()
	{
		JobExecution je = EasyMock.createMock(JobExecution.class);
		EasyMock.expect(je.getId()).andReturn(new Long(1234)).anyTimes();
		EasyMock.expect(je.getStartTime()).andReturn(new Date()).anyTimes();
		EasyMock.replay(je);
		
		setupListener.beforeJob(je);
	}
	
	@Test
	public void testAfterJob()
	{
		JobExecution je = EasyMock.createMock(JobExecution.class);
		EasyMock.expect(je.getExitStatus()).andReturn(null).anyTimes();
		EasyMock.replay(je);
		
		setupListener.afterJob(je);
	}
}
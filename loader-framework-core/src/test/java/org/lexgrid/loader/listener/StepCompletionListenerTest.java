
package org.lexgrid.loader.listener;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import junit.framework.Assert;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.lexgrid.loader.logging.StatusTrackingLogger;
import org.lexgrid.loader.test.LoaderFrameworkCoreTestBase;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;

public class StepCompletionListenerTest extends LoaderFrameworkCoreTestBase
{
	@Autowired
	private StepCompletionListener stepComplListener;
	
	@Test
	public void testStepListener()
	{
		assertNotNull(stepComplListener);
	}
	
	
	@Test
	public void testBeforeStep()
	{
		StepExecution se = 
			EasyMock.createMock(StepExecution.class);
		
		EasyMock.expect(se.getStepName()).andReturn("Test Step").anyTimes();
		EasyMock.replay(se);
		
		StatusTrackingLogger originalLogger = this.stepComplListener.getLogger();
	
		stepComplListener.setLogger(new StatusTrackingLogger(){

			public LoadStatus getProcessStatus() {
				// TODO Auto-generated method stub
				return null;
			}

			public void busy() {
				// TODO Auto-generated method stub
				
			}

			public String debug(String message) {
				// TODO Auto-generated method stub
				return null;
			}

			public String error(String message) {
				// TODO Auto-generated method stub
				return null;
			}

			public String error(String message, Throwable sourceException) {
				// TODO Auto-generated method stub
				return null;
			}

			public String fatal(String message) {
				// TODO Auto-generated method stub
				return null;
			}

			public String fatal(String message, Throwable sourceException) {
				// TODO Auto-generated method stub
				return null;
			}

			public void fatalAndThrowException(String message) throws Exception {
				// TODO Auto-generated method stub
				
			}

			public void fatalAndThrowException(String message,
					Throwable sourceException) throws Exception {
				// TODO Auto-generated method stub
				
			}

			public String info(String message) {
				
				Assert.assertEquals("Step: Test Step starting.", message);
				return message;
			}

			public String warn(String message) {
				// TODO Auto-generated method stub
				return null;
			}

			public String warn(String message, Throwable sourceException) {
				// TODO Auto-generated method stub
				return null;
			}
			
		});
		
		stepComplListener.beforeStep(se);
		
		this.stepComplListener.setLogger(originalLogger);
	}
	
	@Test
	public void testAfterStep()
	{
		StepExecution se = 
			EasyMock.createMock(StepExecution.class);
		
		EasyMock.expect(se.getStepName()).andReturn("Test Step").anyTimes();
		EasyMock.expect(se.getSummary()).andReturn("Test Summary").anyTimes();
		EasyMock.expect(se.getFailureExceptions()).andReturn(new ArrayList<Throwable>()).anyTimes();
		EasyMock.expect(se.getExitStatus()).andReturn(null).anyTimes();
		EasyMock.replay(se);
	
		stepComplListener.afterStep(se);
	}
}
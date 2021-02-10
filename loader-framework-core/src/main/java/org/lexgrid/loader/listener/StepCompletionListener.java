
package org.lexgrid.loader.listener;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.lexgrid.loader.logging.LoggingBean;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * The listener interface for receiving stepCompletion events.
 * The class that is interested in processing a stepCompletion
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addStepCompletionListener<code> method. When
 * the stepCompletion event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see StepCompletionEvent
 */
public class StepCompletionListener extends LoggingBean implements StepExecutionListener {

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.StepExecutionListener#afterStep(org.springframework.batch.core.StepExecution)
	 */
	public ExitStatus afterStep(StepExecution step) {
		getLogger().info("Step: " + step.getStepName() + " completed.");
		getLogger().info("Summary for Step: " +step.getStepName() + "\n" + step.getSummary().replace(",", "\n - Step Property: "));
		
		List<Throwable> exceptionList = step.getFailureExceptions();
		if(exceptionList.size() > 0){
			getLogger().warn("Exceptions for Step: " +step.getStepName() + " " + step.getSummary());
			for(Throwable ex : exceptionList){
				getLogger().warn("- Exception: " +  ex + "\n" + getStackTrace(ex));
			}
		}

		return step.getExitStatus();
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.StepExecutionListener#beforeStep(org.springframework.batch.core.StepExecution)
	 */
	public void beforeStep(StepExecution step) {
		getLogger().info("Step: " + step.getStepName() + " starting.");
	}
	
	/**
	 * Gets the stack trace.
	 * 
	 * @param t the t
	 * 
	 * @return the stack trace
	 */
	private static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		t.printStackTrace(pw);
		pw.flush();
		sw.flush();
		return sw.toString();
	}
}
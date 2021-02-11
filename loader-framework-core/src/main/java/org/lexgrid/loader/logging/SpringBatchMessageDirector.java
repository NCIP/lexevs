
package org.lexgrid.loader.logging;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.Impl.loaders.MessageDirector;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;

public class SpringBatchMessageDirector extends CachingMessageDirectorImpl implements StatusTrackingLogger {

	private LoadStatus loadStatus;
	
	public SpringBatchMessageDirector(String programName, LoadStatus status) {
		super(new MessageDirector(programName, status));
		loadStatus = status;
	}

	public LoadStatus getProcessStatus() {
		return loadStatus;
	}
}
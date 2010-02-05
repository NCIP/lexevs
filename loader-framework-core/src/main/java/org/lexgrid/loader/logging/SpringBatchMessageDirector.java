package org.lexgrid.loader.logging;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.Impl.loaders.MessageDirector;

public class SpringBatchMessageDirector extends MessageDirector implements StatusTrackingLogger {

	private LoadStatus loadStatus;
	
	public SpringBatchMessageDirector(String programName, LoadStatus status) {
		super(programName, status);
		loadStatus = status;
	}

	public LoadStatus getProcessStatus() {
		return loadStatus;
	}
}

package org.lexgrid.loader.logging;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.messaging.LgMessageDirectorIF;

public interface StatusTrackingLogger extends LgMessageDirectorIF{

	public LoadStatus getProcessStatus();
}

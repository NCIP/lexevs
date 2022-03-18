
package org.lexgrid.loader.logging;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;

public interface StatusTrackingLogger extends LgMessageDirectorIF{

	public LoadStatus getProcessStatus();
}
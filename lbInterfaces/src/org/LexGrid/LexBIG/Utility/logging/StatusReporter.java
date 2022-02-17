
package org.LexGrid.LexBIG.Utility.logging;

import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.types.LogLevel;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ProcessStatus;

public interface StatusReporter {

	public LogEntry[] getLog(LogLevel level);
	
	public ProcessStatus getStatus();
}
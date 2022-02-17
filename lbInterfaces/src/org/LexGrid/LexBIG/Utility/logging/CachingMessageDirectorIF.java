
package org.LexGrid.LexBIG.Utility.logging;

import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.types.LogLevel;

/**
 * This interface extends the LgMessageDirectorIF to add caching of messages.
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 */
public interface CachingMessageDirectorIF extends LgMessageDirectorIF {
	
	/**
	 * Gets the LogEntries given a specified LogLevel.
	 * 
	 * @param level the level
	 * 
	 * @return the log
	 */
	public LogEntry[] getLog(LogLevel level) ;

	/**
	 * Clear log.
	 */
	public void clearLog();
}

package org.LexGrid.LexBIG.Impl.loaders;

import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.types.LogLevel;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ProcessStatus;
import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.LexGrid.LexBIG.Utility.logging.StatusReporter;

public class StatusReportingCallback implements StatusReporter {
    
    private CachingMessageDirectorIF messageDirectorCallback;
    private ProcessStatus statusCallback;

    public StatusReportingCallback(CachingMessageDirectorIF messageDirectorCallback, ProcessStatus statusCallback) {
        this.messageDirectorCallback = messageDirectorCallback;
        this.statusCallback = statusCallback;
    }
    
    public LogEntry[] getLog(LogLevel level) {
        return messageDirectorCallback.getLog(level);
    }

    public ProcessStatus getStatus() {
       return statusCallback;
    }
}
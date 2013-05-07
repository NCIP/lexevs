/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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
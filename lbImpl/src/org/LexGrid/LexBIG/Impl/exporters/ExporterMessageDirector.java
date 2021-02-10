
package org.LexGrid.LexBIG.Impl.exporters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.types.LogLevel;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ProcessStatus;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.lexevs.logging.LoggerFactory;

/**
 * A message director to redirect messages as necessary for LexBig.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class ExporterMessageDirector implements LgMessageDirectorIF {
    protected List<LogEntry> messages_ = Collections.synchronizedList(new ArrayList<LogEntry>());
    protected int count_ = 1;
    protected String programName_;
    protected ProcessStatus status_;

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    public ExporterMessageDirector(String programName, ProcessStatus status) {
        programName_ = programName;
        status_ = status;
    }

    public void busy() {
        // do nothing method
    }

    public void clearMessages() {
        messages_.clear();
    }

    public LogEntry[] getLogEntries(LogLevel level) {
        ArrayList<LogEntry> result = new ArrayList<LogEntry>();
        for (int i = 0; i < messages_.size(); i++) {
            if (level == null || messages_.get(i).getEntryLevel().equals(level)) {
                result.add(messages_.get(i));
            }
        }
        return result.toArray(new LogEntry[result.size()]);
    }

    public String error(String message) {
        return error(message, null);
    }

    public String error(String message, Throwable sourceException) {
        LogEntry le = new LogEntry();
        le.setAssociatedURL(null);
        le.setEntryLevel(LogLevel.ERROR);
        le.setEntryTime(new Date(System.currentTimeMillis()));
        le.setMessage(message);
        le.setMessageNumber(count_++);
        le.setProcessUID(Thread.currentThread().getName());
        le.setProgramName(programName_);
        messages_.add(le);
        status_.setMessage(message);
        status_.setErrorsLogged(new Boolean(true));
        getLogger().exportLogError(message);
        return le.getMessageNumber() + "";
    }

    public String fatal(String message) {
        return fatal(message, null);
    }

    public String fatal(String message, Throwable sourceException) {
        LogEntry le = new LogEntry();
        le.setAssociatedURL(null);
        le.setEntryLevel(LogLevel.FATAL);
        le.setEntryTime(new Date(System.currentTimeMillis()));
        le.setMessage(message + " Exception - " + sourceException);
        le.setMessageNumber(count_++);
        le.setProcessUID(Thread.currentThread().getName());
        le.setProgramName(programName_);
        messages_.add(le);
        status_.setMessage(message);
        status_.setErrorsLogged(new Boolean(true));
        getLogger().exportLogError(message, sourceException);
        return le.getMessageNumber() + "";
    }

    public void fatalAndThrowException(String message) throws Exception {
        fatalAndThrowException(message, null);
    }

    public void fatalAndThrowException(String message, Throwable sourceException) throws Exception {
        fatal(message, sourceException);
        throw new Exception(message, sourceException);
    }

    public String info(String message) {
        LogEntry le = new LogEntry();
        le.setAssociatedURL(null);
        le.setEntryLevel(LogLevel.INFO);
        le.setEntryTime(new Date(System.currentTimeMillis()));
        le.setMessage(message);
        le.setMessageNumber(count_++);
        le.setProcessUID(Thread.currentThread().getName());
        le.setProgramName(programName_);
        messages_.add(le);
        status_.setMessage(message);
        getLogger().exportLogDebug(message);
        return le.getMessageNumber() + "";
    }

    public String warn(String message) {
        return warn(message, null);
    }

    public String warn(String message, Throwable sourceException) {
        LogEntry le = new LogEntry();
        le.setAssociatedURL(null);
        le.setEntryLevel(LogLevel.WARN);
        le.setEntryTime(new Date(System.currentTimeMillis()));
        le.setMessage(message + " Exception - " + sourceException);
        le.setMessageNumber(count_++);
        le.setProcessUID(Thread.currentThread().getName());
        le.setProgramName(programName_);
        messages_.add(le);
        status_.setMessage(message);
        status_.setWarningsLogged(new Boolean(true));
        getLogger().exportLogWarn(message, sourceException);
        return le.getMessageNumber() + "";
    }

    public String debug(String message) {
        LogEntry le = new LogEntry();
        le.setAssociatedURL(null);
        le.setEntryLevel(LogLevel.DEBUG);
        le.setEntryTime(new Date(System.currentTimeMillis()));
        le.setMessage(message);
        le.setMessageNumber(count_++);
        le.setProcessUID(Thread.currentThread().getName());
        le.setProgramName(programName_);
        messages_.add(le);
        status_.setMessage(message);
        getLogger().exportLogDebug(message);
        return le.getMessageNumber() + "";
    }
}
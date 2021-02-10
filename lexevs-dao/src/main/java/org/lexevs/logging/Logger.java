
package org.lexevs.logging;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.constants.SystemVariables;

/**
 * All Log message from LexBIG methods should go through this class. This wraps
 * log4j, and assigns unique identifiers for the log messages.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:rokickik@mail.nih.gov">Konrad Rokicki</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class Logger implements LgLoggerIF, Serializable {

	private static final long serialVersionUID = -4792431175428737078L;

	/** The gui log_. */
    private org.apache.log4j.Logger fatal_, error_, warn_, info_, debug_, loadLog_, exportLog_, apiLog_, sqlLog_, guiLog_, vsGUILog_;

    /** The log message id_. */
    private int logMessageId_ = 1;
    
    /** The message queue_. */
    private ArrayList<MessageHolder> messageQueue_;
    
    /** The is api logging enabled. */
    private boolean isAPILoggingEnabled = false;
    
    /** The is debug enabled. */
    private boolean isDebugEnabled = false;

    /**
     * Instantiates a new logger.
     */
    public Logger() {
        Appender simpleAppender = new ConsoleAppender(new PatternLayout("%c %p - %d - %m%n"));
        // only warnings and worse to the screen

        fatal_ = org.apache.log4j.Logger.getLogger("LB_FATAL_LOGGER");
        fatal_.setAdditivity(false);
        fatal_.addAppender(simpleAppender);

        error_ = org.apache.log4j.Logger.getLogger("LB_ERROR_LOGGER");
        error_.setAdditivity(false);
        error_.addAppender(simpleAppender);

        warn_ = org.apache.log4j.Logger.getLogger("LB_WARN_LOGGER");
        warn_.setAdditivity(false);
        warn_.addAppender(simpleAppender);

        info_ = org.apache.log4j.Logger.getLogger("LB_INFO_LOGGER");
        info_.setAdditivity(false);
        info_.setLevel(Level.INFO);

        debug_ = LogManager.getLogger("LB_DEBUG_LOGGER");
        debug_.setAdditivity(false);
        debug_.setLevel(Level.DEBUG);

        loadLog_ = LogManager.getLogger("LB_LOAD_LOGGER");
        loadLog_.setAdditivity(false);
        loadLog_.setLevel(Level.DEBUG);
        
        exportLog_ = LogManager.getLogger("LB_EXPORT_LOGGER");
        exportLog_.setAdditivity(false);
        exportLog_.setLevel(Level.DEBUG);

        apiLog_ = LogManager.getLogger("LB_API_LOGGER");
        apiLog_.setAdditivity(false);
        apiLog_.setLevel(Level.DEBUG);
        
        sqlLog_= LogManager.getLogger("org.LexGrid.util.sql.sqlReconnect.WrappedPreparedStatement");
        sqlLog_.setAdditivity(false);
        sqlLog_.setLevel(Level.ERROR);
        sqlLog_.addAppender(simpleAppender);
        

        guiLog_ = LogManager.getLogger("LB_GUI_LOGGER");
        guiLog_.setAdditivity(false);
        guiLog_.addAppender(simpleAppender);
        
        vsGUILog_ = LogManager.getLogger("LB_VSGUI_LOGGER");
        vsGUILog_.setAdditivity(false);
        vsGUILog_.addAppender(simpleAppender);

        // The root logger by default has a console appender - only let it write
        // warnings or worse.
        // this catches all of the loggers in the software that I am including.
        LogManager.getRootLogger().setLevel(Level.WARN);

        // I need to queue messages before the logger is completely configured.
        messageQueue_ = new ArrayList<MessageHolder>();

        this.debug("LexBIG Logger configured");
    }

    /**
     * Finish log config.
     * 
     * @param vars the vars
     */
    public void finishLogConfig(SystemVariables vars) {
        // These operations cannot be done until the system variables are read,
        // but the logger
        // cant be used until the system variables are read... chicken and egg
        // problem.
        try {
            File parent = new File(vars.getLogLocation());

            if (!parent.exists()) {
                parent.mkdir();
            }
            if (!parent.isDirectory()) {
                error("Problem trying to create the log file location '" + parent.getAbsolutePath() + "'");
            } else {
                File fullLog = new File(vars.getLogLocation(), "LexBIG_full_log.txt");
                File loadLog = new File(vars.getLogLocation(), "LexBIG_load_log.txt");
                File exportLog = new File(vars.getLogLocation(), "LexBIG_export_log.txt");

                Appender fileAppender;
                Appender loadFileAppender, exportFileAppender;
                Appender consoleAppender = new ConsoleAppender(new PatternLayout("%c %p - %d - %m%n"));

                String logChange = vars.getLogChange();
                if (logChange.equals("daily") || logChange.equals("weekly") || logChange.equals("monthly")) {
                    long now = System.currentTimeMillis();
                    // erase logs after is in days - convert to ms
                    long eraseLogsOlderThan = now
                            - ((long) vars.getEraseLogsAfter() * (long) 24 * (long) 60 * (long) 60 * (long) 1000);

                    File[] oldLogFiles = parent.listFiles();
                    for (int i = 0; i < oldLogFiles.length; i++) {
                        if (!oldLogFiles[i].isDirectory() && oldLogFiles[i].getName().startsWith("LexBIG_")
                                && oldLogFiles[i].lastModified() < eraseLogsOlderThan) {
                            oldLogFiles[i].delete();
                        }
                    }

                    String formatString = "";
                    if (logChange.equals("daily")) {
                        formatString = "'.'yyyy-MM-dd";
                    } else if (logChange.equals("weekly")) {
                        formatString = "'.'yyyy-ww";
                    } else {
                        formatString = "'.'yyyy-MM";
                    }
                    fileAppender = new DailyRollingFileAppender(new PatternLayout("%c %p - %d - %m%n"), fullLog
                            .getAbsolutePath(), formatString);
                    ((DailyRollingFileAppender) fileAppender).setAppend(true);
                    ((DailyRollingFileAppender) fileAppender).activateOptions();

                    loadFileAppender = new DailyRollingFileAppender(new PatternLayout("%c %p - %d - %m%n"), loadLog
                            .getAbsolutePath(), formatString);
                    ((DailyRollingFileAppender) loadFileAppender).setAppend(true);
                    ((DailyRollingFileAppender) loadFileAppender).activateOptions();
                    
                    exportFileAppender = new DailyRollingFileAppender(new PatternLayout("%c %p - %d - %m%n"), exportLog
                            .getAbsolutePath(), formatString);
                    ((DailyRollingFileAppender) exportFileAppender).setAppend(true);
                    ((DailyRollingFileAppender) exportFileAppender).activateOptions();

                } else {
                    fileAppender = new RollingFileAppender(new PatternLayout("%c %p - %d - %m%n"), fullLog
                            .getAbsolutePath(), true);
                    ((RollingFileAppender) fileAppender).setMaxBackupIndex(Integer.parseInt(logChange));
                    ((RollingFileAppender) fileAppender).setMaxFileSize(vars.getEraseLogsAfter() + "MB");

                    loadFileAppender = new RollingFileAppender(new PatternLayout("%c %p - %d - %m%n"), loadLog
                            .getAbsolutePath(), true);
                    ((RollingFileAppender) loadFileAppender).setMaxBackupIndex(Integer.parseInt(logChange));
                    ((RollingFileAppender) loadFileAppender).setMaxFileSize(vars.getEraseLogsAfter() + "MB");
                    
                    exportFileAppender = new RollingFileAppender(new PatternLayout("%c %p - %d - %m%n"), exportLog
                            .getAbsolutePath(), true);
                    ((RollingFileAppender) exportFileAppender).setMaxBackupIndex(Integer.parseInt(logChange));
                    ((RollingFileAppender) exportFileAppender).setMaxFileSize(vars.getEraseLogsAfter() + "MB");
                }

                // log everything to the file.
                fatal_.addAppender(fileAppender);
                error_.addAppender(fileAppender);
                warn_.addAppender(fileAppender);
                info_.addAppender(fileAppender);
                debug_.addAppender(fileAppender);
                sqlLog_.addAppender(fileAppender);
                apiLog_.addAppender(fileAppender);

                loadLog_.addAppender(loadFileAppender);
                exportLog_.addAppender(exportFileAppender);

                // add the root logger to the console appender.
                LogManager.getRootLogger().addAppender(consoleAppender);

                // configure the e-mail appender (if they want one)
                if (vars.emailErrors()) {
                    SimpleEmailAppender emailAppender = new SimpleEmailAppender(new EmailTrigger());
                    emailAppender.setBufferSize(1);
                    emailAppender.setLayout(new PatternLayout("%c %p - %d - %m%n"));
                    emailAppender.setSMTPHost(vars.getSMTPServer());
                    emailAppender.setSubject("[LexBIG Error]");
                    try {
                        InetAddress addr = InetAddress.getLocalHost();
                        emailAppender.setFrom("LexBIG@" + addr.getHostName());
                    } catch (RuntimeException e) {
                        emailAppender.setFrom("LexBIG@unknown");
                    }
                    emailAppender.setTo(vars.getEmailTo());

                    emailAppender.activateOptions();

                    debug_.addAppender(emailAppender);
                    debug_
                            .debug("[This is not an error] This is a test message to ensure that LexBIG is properly configured to send e-mail.  If this message does not arrive as an e-mail, then it is not configured properly.");
                    debug_.removeAppender(emailAppender);
                    emailAppender.setThreshold(Level.WARN);

                    fatal_.addAppender(emailAppender);
                    error_.addAppender(emailAppender);
                    warn_.addAppender(emailAppender);
                }
            }
        } catch (IOException e) {
            error("Problem creating file appender", e);
        }

        Level level = Level.WARN;
        if (SystemVariables.isDebugEnabled()) {
            level = Level.DEBUG;
        }

        if (vars.isSQLLoggingEnabled()) {
            sqlLog_.setLevel(Level.DEBUG);
        }
        // enable SQL statement debugging
        org.apache.log4j.Logger sqlStatements = LogManager
                .getLogger("managedobj.service.jdbc.sqlReconnect.WrappedPreparedStatement");
        sqlStatements.setLevel(level);

        // I don't think that we need to debug the wrapped connections anymore -
        // preparedStatement debugging
        // should give us everything we need.
        // org.apache.log4j.Logger sqlStatements2 = LogManager
        // .getLogger("managedobj.service.jdbc.sqlReconnect.WrappedConnection");
        // sqlStatements2.setLevel(level);

        if(messageQueue_ == null){
        	return;
        }
        for (int i = 0; i < messageQueue_.size(); i++) {
            MessageHolder temp = messageQueue_.get(i);
            if (temp.type.equals("debug")) {
                if (temp.cause == null) {
                    debug_.debug(temp.message);
                } else {
                    debug_.debug(temp.message, temp.cause);
                }
            } else if (temp.type.equals("info")) {
                if (temp.cause == null) {
                    info_.info(temp.message);
                } else {
                    info_.info(temp.message, temp.cause);
                }
            } else if (temp.type.equals("error")) {
                if (temp.cause == null) {
                    error_.error(temp.message);
                } else {
                    error_.error(temp.message, temp.cause);
                }
            } else if (temp.type.equals("fatal")) {
                if (temp.cause == null) {
                    fatal_.fatal(temp.message);
                } else {
                    fatal_.fatal(temp.message, temp.cause);
                }
            } else if (temp.type.equals("warn")) {
                if (temp.cause == null) {
                    warn_.warn(temp.message);
                } else {
                    warn_.warn(temp.message, temp.cause);
                }
            }
        }

        messageQueue_ = null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#debug(java.lang.String)
     */
    public String debug(String debugMessage) {
        if (SystemVariables.isDebugEnabled()) {
            String idString = getIDString();
            if (messageQueue_ == null) {
                debug_.debug(idString + debugMessage);
            } else {
                messageQueue_.add(new MessageHolder(idString + debugMessage, "debug", null));
            }
            return idString;
        }
        return null;
    }

    /**
     * Debug.
     * 
     * @param debugMessage the debug message
     * @param cause the cause
     * 
     * @return the string
     */
    public String debug(String debugMessage, Throwable cause) {
        if (SystemVariables.isDebugEnabled()) {
            String idString = getIDString();
            if (messageQueue_ == null) {
                debug_.debug(idString + debugMessage, cause);
            } else {
                messageQueue_.add(new MessageHolder(idString + debugMessage, "debug", cause));
            }
            return idString;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#info(java.lang.String)
     */
    public String info(String infoMessage) {
        String idString = getIDString();
        if (messageQueue_ == null) {
            info_.info(idString + infoMessage);
        } else {
            messageQueue_.add(new MessageHolder(idString + infoMessage, "info", null));
        }
        return idString;
    }

    /**
     * Info.
     * 
     * @param infoMessage the info message
     * @param cause the cause
     * 
     * @return the string
     */
    public String info(String infoMessage, Throwable cause) {
        String idString = getIDString();
        info_.info(idString + infoMessage, cause);
        return idString;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#warn(java.lang.String)
     */
    public String warn(String warning) {
        String idString = getIDString();
        if (messageQueue_ == null) {
            warn_.warn(idString + warning);
        } else {
            messageQueue_.add(new MessageHolder(idString + warning, "warn", null));
        }
        return idString;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#warn(java.lang.String, java.lang.Throwable)
     */
    public String warn(String warning, Throwable cause) {
        String idString = getIDString();
        if (messageQueue_ == null) {
            warn_.warn(idString + warning, cause);
        } else {
            messageQueue_.add(new MessageHolder(idString + warning, "warn", cause));
        }
        return idString;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#error(java.lang.String)
     */
    public String error(String errorMessage) {
        String idString = getIDString();
        if (errorMessage != null && errorMessage.indexOf("<JUNIT_IGNORE>") != -1) {
            // don't write this to the logger if it was an error that is
            // expected to be caused by JUnit.
            // some of the JUnit tests cause exceptions that I don't care to log
            // - they are the expected
            // behavior. These logs get rather noisy, so I'm filting those out.
            return idString;
        }
        if (messageQueue_ == null) {
            error_.error(idString + errorMessage);
        } else {
            messageQueue_.add(new MessageHolder(idString + errorMessage, "error", null));
        }
        return idString;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#error(java.lang.String, java.lang.Throwable)
     */
    public String error(String errorMessage, Throwable cause) {
        String idString = getIDString();
        if ((errorMessage != null && errorMessage.indexOf("<JUNIT_IGNORE>") != -1)
                || (cause != null && cause.getMessage() != null && cause.getMessage().indexOf("<JUNIT_IGNORE>") != -1)) {
            // don't write this to the logger if it was an error that is
            // expected to be caused by JUnit.
            // some of the JUnit tests cause exceptions that I don't care to log
            // - they are the expected
            // behavior. These logs get rather noisy, so I'm filting those out.
            return idString;
        }
        if (messageQueue_ == null) {
            error_.error(idString + errorMessage, cause);
        } else {
            messageQueue_.add(new MessageHolder(idString + errorMessage, "error", cause));
        }
        return idString;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#fatal(java.lang.String)
     */
    public String fatal(String errorMessage) {
        String idString = getIDString();
        if (messageQueue_ == null) {
            fatal_.fatal(idString + errorMessage);
        } else {
            messageQueue_.add(new MessageHolder(idString + errorMessage, "fatal", null));
        }
        return idString;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#fatal(java.lang.String, java.lang.Throwable)
     */
    public String fatal(String errorMessage, Throwable cause) {
        String idString = getIDString();
        if (messageQueue_ == null) {
            fatal_.fatal(idString + errorMessage, cause);
        } else {
            messageQueue_.add(new MessageHolder(idString + errorMessage, "fatal", cause));
        }
        return idString;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#loadLogDebug(java.lang.String)
     */
    public void loadLogDebug(String message) {
        loadLog_.debug(message);
    }
    
    public void exportLogDebug(String message) {
        exportLog_.debug(message);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#logMethod()
     */
    public void logMethod() {
        logMethod(null);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#logMethod(java.lang.Object[])
     */
    public void logMethod(Object[] params) {

        try {
            if (LexEvsServiceLocator.getInstance().getResourceManager().getSystemVariables().isAPILoggingEnabled()
                    || LexEvsServiceLocator.getInstance().getResourceManager().getSystemVariables().isDebugEnabled()) {
                StringBuffer temp = new StringBuffer();
                if (params != null && params.length > 0) {
                    for (int i = 0; i < params.length; i++) {
                        temp.append("Param " + (i + 1) + ": ");
                       // temp.append(ObjectToString.toString(params[i]) + ", ");
                    }
                }
                StackTraceElement ste = new Exception().getStackTrace()[1];
                if (ste.getMethodName().equals("<init>")) {
                    apiLog_.info(Thread.currentThread().getId() + " - " + ste.getClassName() + " constructor " + temp);
                } else {
                    apiLog_.info(Thread.currentThread().getId() + " - " + ste.getClassName() + "."
                            + ste.getMethodName() + " " + temp);
                }

            }
        } catch (RuntimeException e) {
            error("There was a problem logging a method call", e);
        }
   
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#exportLogError(java.lang.String, java.lang.Throwable)
     */
    public void exportLogError(String message, Throwable e) {
        exportLog_.error(message, e);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#exportLogError(java.lang.String)
     */
    public void exportLogError(String message) {
        exportLog_.error(message);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#exportLogWarn(java.lang.String, java.lang.Throwable)
     */
    public void exportLogWarn(String message, Throwable e) {
        exportLog_.warn(message, e);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#loadLogError(java.lang.String, java.lang.Throwable)
     */
    public void loadLogError(String message, Throwable e) {
        loadLog_.error(message, e);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#loadLogError(java.lang.String)
     */
    public void loadLogError(String message) {
        loadLog_.error(message);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#loadLogWarn(java.lang.String, java.lang.Throwable)
     */
    public void loadLogWarn(String message, Throwable e) {
        loadLog_.warn(message, e);
    }

    /**
     * Gets the iD string.
     * 
     * @return the iD string
     */
    private String getIDString() {
        return "[LOG_ID " + logMessageId_++ + "] ";
    }

    /**
     * Sys err log queue.
     */
    public void sysErrLogQueue() {
        System.err.println("Dumping queued log messages to System.Error and System.Out");
        System.err.println("Dumping queued log messages to System.Error and System.Out");
        if (messageQueue_ != null) {
            for (int i = 0; i < messageQueue_.size(); i++) {
                MessageHolder m = messageQueue_.get(i);
                System.err.println(m.type + ": " + m.message + ": " + (m.cause == null ? "" : m.cause));
                System.out.println(m.type + ": " + m.message + ": " + (m.cause == null ? "" : m.cause));
            }
        }
    }

    /**
     * The Class MessageHolder.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    private class MessageHolder implements Serializable {
     
		private static final long serialVersionUID = 3293868011576100366L;

		/**
         * Instantiates a new message holder.
         * 
         * @param message the message
         * @param type the type
         * @param cause the cause
         */
        public MessageHolder(String message, String type, Throwable cause) {
            this.message = message;
            this.type = type;
            this.cause = cause;
        }

        /** The message. */
        String message;
        
        /** The type. */
        String type;
        
        /** The cause. */
        Throwable cause;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#busy()
     */
    public void busy() {
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#fatalAndThrowException(java.lang.String)
     */
    public void fatalAndThrowException(String message) throws Exception {
        fatal(message);
        throw new Exception(message);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#fatalAndThrowException(java.lang.String, java.lang.Throwable)
     */
    public void fatalAndThrowException(String message, Throwable sourceException) throws Exception {
        fatal(message, sourceException);
        throw new Exception(sourceException);
    }

	/**
	 * Checks if is aPI logging enabled.
	 * 
	 * @return true, if is aPI logging enabled
	 */
	public boolean isAPILoggingEnabled() {
		return isAPILoggingEnabled;
	}

	/**
	 * Sets the aPI logging enabled.
	 * 
	 * @param isAPILoggingEnabled the new aPI logging enabled
	 */
	public void setAPILoggingEnabled(boolean isAPILoggingEnabled) {
		this.isAPILoggingEnabled = isAPILoggingEnabled;
	}

	/**
	 * Checks if is debug enabled.
	 * 
	 * @return true, if is debug enabled
	 */
	public boolean isDebugEnabled() {
		return isDebugEnabled;
	}

	/**
	 * Sets the debug enabled.
	 * 
	 * @param isDebugEnabled the new debug enabled
	 */
	public void setDebugEnabled(boolean isDebugEnabled) {
		this.isDebugEnabled = isDebugEnabled;
	}
}
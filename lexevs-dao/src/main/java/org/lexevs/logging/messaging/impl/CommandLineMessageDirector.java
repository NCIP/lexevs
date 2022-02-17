
package org.lexevs.logging.messaging.impl;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An implementation of the LgMessageDirectorIF that routes the messages to the
 * command line. Also prints messages to a log4j logger (if one is provided)
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class CommandLineMessageDirector implements LgMessageDirectorIF {
    
    /** The log. */
    private static Logger log = null;
    
    /** The print debug. */
    private boolean printDebug = false;

    /**
     * Construct a command line message director that will also write to a log4j
     * logger of the given name.
     * 
     * @param logName the log name
     * @param showDebug should debug messages be printed to the screen.
     */
    public CommandLineMessageDirector(String logName, boolean showDebug) {
        if (logName != null && logName.length() > 0) {
            log = LogManager.getLogger(logName);
        } else {
            log = null;
        }
        printDebug = showDebug;
    }

    /**
     * Construct a command line message director that will also write to a log4j
     * logger of the given name.
     * 
     * @param logName the log name
     */
    public CommandLineMessageDirector(String logName) {
        if (logName != null && logName.length() > 0) {
            log = LogManager.getLogger(logName);
        } else {
            log = null;
        }
        printDebug = false;
    }

    /**
     * Instantiates a new command line message director.
     */
    public CommandLineMessageDirector() {
        log = null;
        printDebug = false;
    }

    /**
     * Output to indicate system is busy.
     */
    public void busy() {
        System.out.print(".");
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#error(java.lang.String)
     */
    public String error(String message) {
        if (log != null) {
            log.error(message);
        }
        System.err.println();
        System.err.print(message);
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#error(java.lang.String, java.lang.Throwable)
     */
    public String error(String message, Throwable sourceException) {
        if (log != null) {
            log.error(message, sourceException);
        }
        System.err.println();
        System.err.print(message + sourceException);
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#fatal(java.lang.String)
     */
    public String fatal(String message) {
        if (log != null) {
            log.fatal(message);
        }
        System.err.println();
        System.err.print(message);
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#fatal(java.lang.String, java.lang.Throwable)
     */
    public String fatal(String message, Throwable sourceException) {
        if (log != null) {
            log.fatal(message, sourceException);
        }
        System.err.println();
        System.err.print(message + sourceException);
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#fatalAndThrowException(java.lang.String)
     */
    public void fatalAndThrowException(String message) throws Exception {
        fatalAndThrowException(message, null);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#fatalAndThrowException(java.lang.String, java.lang.Throwable)
     */
    public void fatalAndThrowException(String message, Throwable sourceException) throws Exception {
        if (log != null) {
            log.fatal(message, sourceException);
        }
        System.err.println();
        System.err.print(message + sourceException);
        throw new Exception(message, sourceException);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#info(java.lang.String)
     */
    public String info(String message) {
        if (log != null) {
            log.info(message);
        }
        System.out.println();
        System.out.println(message);
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#warn(java.lang.String)
     */
    public String warn(String message) {
        if (log != null) {
            log.warn(message);
        }
        System.out.println();
        System.out.println(message);
        return null;

    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#warn(java.lang.String, java.lang.Throwable)
     */
    public String warn(String message, Throwable sourceException) {
        if (log != null) {
            log.warn(message, sourceException);
        }
        System.out.println();
        System.out.println(message + sourceException);
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#debug(java.lang.String)
     */
    public String debug(String message) {
        if (log != null) {
            log.debug(message);
        }
        if (printDebug) {
            System.out.println();
            System.out.println(message);
        }
        return null;
    }
}
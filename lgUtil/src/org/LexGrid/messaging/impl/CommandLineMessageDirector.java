/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.LexGrid.messaging.impl;

import org.LexGrid.messaging.LgMessageDirectorIF;
import org.apache.log4j.Logger;

/**
 * An implementation of the LgMessageDirectorIF that routes the messages to the
 * command line. Also prints messages to a log4j logger (if one is provided)
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class CommandLineMessageDirector implements LgMessageDirectorIF {
    private static Logger log = null;
    private boolean printDebug = false;

    /**
     * Construct a command line message director that will also write to a log4j
     * logger of the given name.
     * 
     * @param logName
     * @param showDebug
     *            should debug messages be printed to the screen.
     */
    public CommandLineMessageDirector(String logName, boolean showDebug) {
        if (logName != null && logName.length() > 0) {
            log = Logger.getLogger(logName);
        } else {
            log = null;
        }
        printDebug = showDebug;
    }

    /**
     * Construct a command line message director that will also write to a log4j
     * logger of the given name.
     * 
     * @param logName
     */
    public CommandLineMessageDirector(String logName) {
        if (logName != null && logName.length() > 0) {
            log = Logger.getLogger(logName);
        } else {
            log = null;
        }
        printDebug = false;
    }

    public CommandLineMessageDirector() {
        log = null;
        printDebug = false;
    }

    /**
     * Output to indicate system is busy
     */
    public void busy() {
        System.out.print(".");
    }

    public String error(String message) {
        if (log != null) {
            log.error(message);
        }
        System.err.println();
        System.err.print(message);
        return null;
    }

    public String error(String message, Throwable sourceException) {
        if (log != null) {
            log.error(message, sourceException);
        }
        System.err.println();
        System.err.print(message + sourceException);
        return null;
    }

    public String fatal(String message) {
        if (log != null) {
            log.fatal(message);
        }
        System.err.println();
        System.err.print(message);
        return null;
    }

    public String fatal(String message, Throwable sourceException) {
        if (log != null) {
            log.fatal(message, sourceException);
        }
        System.err.println();
        System.err.print(message + sourceException);
        return null;
    }

    public void fatalAndThrowException(String message) throws Exception {
        fatalAndThrowException(message, null);
    }

    public void fatalAndThrowException(String message, Throwable sourceException) throws Exception {
        if (log != null) {
            log.fatal(message, sourceException);
        }
        System.err.println();
        System.err.print(message + sourceException);
        throw new Exception(message, sourceException);
    }

    public String info(String message) {
        if (log != null) {
            log.info(message);
        }
        System.out.println();
        System.out.println(message);
        return null;
    }

    public String warn(String message) {
        if (log != null) {
            log.warn(message);
        }
        System.out.println();
        System.out.println(message);
        return null;

    }

    public String warn(String message, Throwable sourceException) {
        if (log != null) {
            log.warn(message, sourceException);
        }
        System.out.println();
        System.out.println(message + sourceException);
        return null;
    }

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
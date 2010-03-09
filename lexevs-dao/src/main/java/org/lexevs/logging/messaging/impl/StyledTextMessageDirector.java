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
package org.lexevs.logging.messaging.impl;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Shell;

/**
 * A class to help with the routing of messages back to the users.
 * 
 * Writes messages to a Log4J logger and to a Styled Text widget.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class StyledTextMessageDirector implements LgMessageDirectorIF {
    private StyledText textArea_;
    private Shell shell_;
    private static Logger log = Logger.getLogger("convert.MessageUtility");

    /**
     * 
     * @param commandLine
     *            true if is from a command line
     * @param textArea
     *            where the message is displayed
     * @param loggerName
     *            name of the logger used
     */
    public StyledTextMessageDirector(StyledText textArea, String loggerName, Shell shell) {
        this(textArea, shell);
        log = Logger.getLogger(loggerName);
    }

    /**
     * 
     * @param commandLine
     *            true if from command line
     * @param textArea
     *            where the text is diplayed
     */
    public StyledTextMessageDirector(StyledText textArea, Shell shell) {
        textArea_ = textArea;
        shell_ = shell;
    }

    /**
     * Displays message either on the command line or TextArea
     * 
     * @param message
     *            message to be displayed
     */
    private void showMessage(final String message, final boolean errorMessage) {
        if (textArea_ != null) {
            shell_.getDisplay().syncExec(new Runnable() {
                public void run() {
                    int pos = textArea_.getText().length();
                    if (pos > 0) {
                        String prev = textArea_.getText().substring(pos - 1);
                        if (prev.equals("-") || prev.equals("/") || prev.equals("\\") || prev.equals("|")) {
                            textArea_.replaceTextRange(pos - 1, 1, "");
                            textArea_.append(message);
                        } else {
                            textArea_.append("\n" + message);
                        }
                    } else {
                        textArea_.append(message);
                    }
                    textArea_.setSelection(textArea_.getText().length());
                    if (errorMessage) {
                        StyleRange range = new StyleRange();
                        range.length = message.length();
                        range.start = textArea_.getText().length() - message.length();
                        range.foreground = textArea_.getDisplay().getSystemColor(SWT.COLOR_RED);
                        textArea_.setStyleRange(range);
                    }
                }
            });
        }
    }

    /**
     * Output to indicate system is busy
     */
    public void busy() {
        if (textArea_ != null) {
            shell_.getDisplay().syncExec(new Runnable() {
                public void run() {
                    int pos = textArea_.getText().length();
                    String prev = textArea_.getText().substring(pos - 1);
                    String next = "-";
                    if (prev.equals("/")) {
                        next = "-";
                    } else if (prev.equals("-")) {
                        next = "\\";
                    } else if (prev.equals("\\")) {
                        next = "|";
                    } else if (prev.equals("|")) {
                        next = "/";
                    } else {
                        textArea_.append("\n ");
                        pos = textArea_.getText().length();
                        textArea_.setSelection(pos);
                    }

                    textArea_.replaceTextRange(pos - 1, 1, next);
                }
            });
        }
    }

    public String error(String message) {
        return error(message, null);
    }

    public String error(String message, Throwable sourceException) {
        if (sourceException == null) {
            log.error(message);
        } else {
            log.error(message, sourceException);
        }
        showMessage(message, true);
        return null;
    }

    public String fatal(String message) {
        return fatal(message, null);
    }

    public String fatal(String message, Throwable sourceException) {
        if (sourceException == null) {
            log.fatal(message);
        } else {
            log.fatal(message, sourceException);
        }
        showMessage(message, true);
        return null;
    }

    public void fatalAndThrowException(String message) throws Exception {
        fatalAndThrowException(message, null);
    }

    public void fatalAndThrowException(String message, Throwable sourceException) throws Exception {
        if (sourceException == null) {
            log.fatal(message);
        } else {
            log.fatal(message, sourceException);
        }
        showMessage(message, true);
        throw new Exception(message, sourceException);
    }

    public String info(String message) {
        log.info(message);
        showMessage(message, false);
        return null;
    }

    public String warn(String message) {
        return warn(message, null);
    }

    public String warn(String message, Throwable sourceException) {
        if (sourceException == null) {
            log.warn(message);
        } else {
            log.warn(message, sourceException);
        }
        showMessage(message, true);
        return null;
    }

    public String debug(String message) {
        log.debug(message);
        return null;
    }
}
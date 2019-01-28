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
package org.LexGrid.LexBIG.admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Date;
import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExportStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ProcessStatus;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Export.Exporter;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.LexBIG.Utility.logging.StatusReporter;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.lexevs.system.ResourceManager;

/**
 * Common utility functions required by admin tasks.
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 */
public class Util {
    static final private String _lineReturn = System.getProperty("line.separator");
    static final private LgLoggerIF _logger = ResourceManager.instance().getLogger();
    static final private PrintWriter _printWriter = new PrintWriter(System.out);

    /**
     * Outputs messages to the error log and administration console, with
     * additional tagging to assist servicability.
     * 
     * @param message
     *            The message to display.
     * @param cause
     *            Error associated with the message.
     */
    public static void displayAndLogError(String message, Throwable cause) {
        displayAndLogMessage(message);
        _logger.error(message, cause);
        _logger.error(ExceptionUtils.getFullStackTrace(cause));
    }

    /**
     * Outputs messages to the error log and administration console, with
     * additional tagging to assist servicability.
     * 
     * @param cause
     *            Error associated with the message.
     */
    public static void displayAndLogError(Throwable cause) {
        displayAndLogError(cause.getMessage(), cause);
    }
    
    /**
     * Outputs messages to the information log and administration console, with
     * additional tagging to assist service-ability.
     * 
     * @param message
     *            Non-exception or generated message. Information
     *            useful to the user
     */
    public static void displayAndLogMessage(String message) {
        displayTaggedMessage(message);
        _logger.info(message);
    }

    /**
     * Outputs a standard message to administration console indicating supported
     * command line options.
     * 
     * @param syntax
     *            Named syntax.
     * @param options
     *            Provided options.
     * @param example
     *            Example usage, if applicable.
     * @param parseErr
     *            Error that occurred parsing the command line, if applicable.
     */
    public static void displayCommandOptions(String syntax, Options options, String example, Throwable parseErr) {
        displayMessage("");
        if (parseErr != null) {
            displayMessage("Unable to parse command options>> " + parseErr.getMessage());
            displayMessage("");
        }
        try {
            new HelpFormatter().printHelp(_printWriter, 80, syntax, "", options, 0, 0, "", true);
        } finally {
            _printWriter.flush();
        }
        if (example != null) {
            displayMessage("");
            displayMessage("Example: " + example);
        }
    }
    
    /**
     * Displays any available status messages, polling periodically and
     * returning when the export operation is complete.
     * 
     * @param loader
     */
    public static void displayStatus(StatusReporter reporter) {
        ProcessStatus status = null;
        String msg = "";
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            status = reporter.getStatus();
            String s = status.getMessage();
            if (s != null && !s.equals(msg)) {
                Util.displayAndLogMessage(s);
                msg = s;
            }
        } while (status.getEndTime() == null);
    }

    /**
     * Displays any available status messages, polling periodically and
     * returning when the export operation is complete.
     * 
     * @param loader
     */
    public static void displayExporterStatus(Exporter exporter) {
        ExportStatus status = null;
        String msg = "";
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            status = exporter.getStatus();
            String s = status.getMessage();
            if (s != null && !s.equals(msg)) {
                Util.displayAndLogMessage(s);
                msg = s;
            }
        } while (status.getEndTime() == null);
    }

    /**
     * Displays any available status messages, polling periodically and
     * returning when the load operation is complete.
     * 
     * @param loader
     */
    public static void displayLoaderStatus(Loader loader) {
        LoadStatus status = null;
        Integer cnum = null, rnum = null;
        String msg = "";
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            status = loader.getStatus();
            Integer num = status.getNumConceptsLoaded();
            if (num != cnum) {
                Util.displayAndLogMessage("# concepts processed: " + num);
                cnum = num;
            }
            num = status.getNumRelationsLoaded();
            if (num != rnum) {
                Util.displayAndLogMessage("# relations processed: " + num);
                rnum = num;
            }
            String s = status.getMessage();
            if (s != null && !s.equals(msg)) {
                Util.displayAndLogMessage(s);
                msg = s;
            }
        } while (status.getEndTime() == null);
    }

    /**
     * Displays a message to the administration console.
     * 
     * @param message
     *            The message to display.
     */
    public static void displayMessage(String message) {
        try {
            _printWriter.println(message);
        } finally {
            _printWriter.flush();
        }
    }

    /**
     * Displays a message to the administration console, with additional tagging
     * to assist servicability.
     * 
     * @param message
     *            The message to display.
     */
    public static void displayTaggedMessage(String message) {
        displayTaggedMessage(message, null, null);
    }

    /**
     * Displays a message to the administration console, with additional tagging
     * to assist servicability.
     * 
     * @param message
     *            The message to display.
     * @param cause
     *            Optional error associated with the message.
     * @param logID
     *            Optional identifier as registered in the LexBIG logs.
     */
    public static void displayTaggedMessage(String message, Throwable cause, String logID) {
        StringBuffer sb = new StringBuffer("[LB] ").append("[" + new Date(System.currentTimeMillis()) + "] ").append(
                message);
        if (cause != null) {
            String causeMsg = cause.getMessage();
            if (causeMsg != null && !causeMsg.equals(message)) {
                sb.append(_lineReturn).append("\t*** Cause: ").append(causeMsg);
            }
        }
        if (logID != null) {
            sb.append(_lineReturn).append("\t*** Refer to message with ID = ").append(logID)
                    .append(" in the log file.");
        }
        displayMessage(sb.toString());
    }

    /**
     * Read and return a single character based on user input to the
     * Administration console.
     * 
     * @return char
     */
    public static char getConsoleCharacter() throws IOException {
        char ch = (char) System.in.read();
        while (System.in.available() > 0)
            System.in.read();
        return ch;
    }

    /**
     * Returns common text to append to displayed help for commands that allow
     * the user to prompt for coding scheme information instead of providing urn
     * and version information as parameters.
     * 
     * @return String
     */
    public static String getPromptForSchemeHelp() {
        return "\n" + "\nNote: If the URN and version values are unspecified, a list of"
                + "\navailable coding schemes will be presented for user selection.";
    }

    /**
     * Returns rendering detail associated with the following coding scheme
     * summary; null if not available.
     * 
     * @param css
     */
    public static CodingSchemeRendering getRenderingDetail(CodingSchemeSummary css) {
        CodingSchemeRendering rendering = null;
        if (css != null) {
            String urn = css.getCodingSchemeURI();
            String ver = css.getRepresentsVersion();
            if (urn != null && ver != null)
                try {
                    urn = urn.trim();
                    ver = ver.trim();
                    LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
                    Enumeration<? extends CodingSchemeRendering> schemes = lbs.getSupportedCodingSchemes()
                            .enumerateCodingSchemeRendering();
                    while (schemes.hasMoreElements() && rendering == null) {
                        CodingSchemeRendering csrtemp = schemes.nextElement();
                        CodingSchemeSummary csstemp = csrtemp.getCodingSchemeSummary();
                        if (urn.equalsIgnoreCase(csstemp.getCodingSchemeURI())
                                && ver.equalsIgnoreCase(csstemp.getRepresentsVersion())) {
                            rendering = csrtemp;
                        }
                    }
                } catch (LBInvocationException e) {
                    displayAndLogError(e);
                }
        }
        return rendering;
    }

    /**
     * Returns common text to append to displayed help for commands that accept
     * URI parameters (e.g. how to deal with imbedded spaces).
     * 
     * @return String
     */
    public static String getURIHelp() {
        String osName = System.getProperty("os.name");
        return osName.equalsIgnoreCase("Linux") ? ("\n"
                + "\nSpaces are allowed in URI values.  However, spaces in Linux"
                + "\n(even when surrounded by quotes or escaped) are interpreted"
                + "\nas argument separators by the Java runtime environment."
                + "\nTo avoid this situation, one or more of the following"
                + "\nspecial values can be substituted for each space in the" + "\nprovided URI:"
                + "\n\t[[:space:]] (matches whitespace)" + "\n\t? (single-character wildcard)"
                + "\n\t* (multi-character wildcard)" + "\nFor example, \"file name\" can be represented as:"
                + "\n\t\"file[[:space:]]name\"" + "\n\t\"file?name\"" + "\n\t\"file*name\"") : "";
    }

    /**
     * Display a list of available code systems and
     * 
     * @return The coding scheme summary for the selected code system; null if
     *         no valid selection was made.
     */
    public static CodingSchemeSummary promptForCodeSystem() {
        return new CodingSchemeSelectionMenu().displayAndGetSelection();
    }

    /**
     * Returns a file URI corresponding to the given string.
     * 
     * @param s
     * @return java.net.URI
     * @throws org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException
     */
    public static URI string2FileURI(String s) throws LBResourceUnavailableException {
        String trimmed = s.trim();
        try {
            // Resolve to file, treating the string as either a
            // standard file path or URI.
            File f;
            if (!(f = new File(trimmed)).exists()) {
                f = new File(new URI(trimmed.replace(" ", "%20")));
                if (!f.exists())
                    throw new FileNotFoundException();
            }

            // Accomodate embedded spaces ...
            return new URI(f.toURI().toString().replace(" ", "%20"));
        } catch (Exception e) {
            displayAndLogMessage(e.getMessage());
            throw new LBResourceUnavailableException("UNABLE TO RESOLVE RESOURCE: " + trimmed);
        }
    }
}
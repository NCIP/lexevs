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
package edu.mayo.informatics.lexgrid.convert.formats.inputFormats;

import java.net.URI;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;

import edu.mayo.informatics.lexgrid.convert.directConversions.UMLSHistoryFileToSQL;
import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.exceptions.UnexpectedError;
import edu.mayo.informatics.lexgrid.convert.formats.InputFormatInterface;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.baseFormats.URIBase;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;

/**
 * Details for reading a LexGrid Delimited Text File.
 * 
 * @author <A HREF="mailto:Rao.Ramachandra@mayo.edu">Rao</A>
 */
public class UMLSHistoryFile extends URIBase implements InputFormatInterface {
    public static String description = "UMLS History File";
    private boolean stopOnErrors_ = false;
    private LgMessageDirectorIF message_ = null;

    public UMLSHistoryFile(URI sourcePath, boolean stopOnErrors, LgMessageDirectorIF md) {
        fileLocation = sourcePath;
        this.stopOnErrors_ = stopOnErrors;
        this.message_ = md;
    }

    public UMLSHistoryFile() {

    }

    public String testConnection() throws ConnectionFailure {

        try {
            message_.info("Validating Input File(s)...");
            UMLSHistoryFileToSQL.validateFile(fileLocation, null, false);
            message_.info("Validation Success.");
        } catch (Exception e) {
            throw new ConnectionFailure("Validation error: Problem in reading the file: " + e.getMessage());
        }
        return "";
    }

    public String getDescription() {
        return description;
    }

    public String[] getSupportedOutputFormats() {
        return new String[] { LexGridSQLOut.description };
    }

    public String getConnectionSummary() {
        return getConnectionSummary(description);
    }

    public Option[] getOptions() {
        return new Option[] { new Option(Option.DELIMITER, "|"), new Option(Option.FAIL_ON_ERROR, new Boolean(true)) };
    }

    public String[] getAvailableTerminologies() throws ConnectionFailure, UnexpectedError {
        return null;
    }
}
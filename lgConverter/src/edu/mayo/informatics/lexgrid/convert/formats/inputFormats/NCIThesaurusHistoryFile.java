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
package edu.mayo.informatics.lexgrid.convert.formats.inputFormats;

import java.net.URI;

import edu.mayo.informatics.lexgrid.convert.directConversions.NCIThesaurusHistoryFileToSQL;
import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.InputFormatInterface;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.baseFormats.URIBase;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;

/**
 * Details for reading a LexGrid Delimited Text File.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 1052 $ checked in on $Date: 2006-01-30
 *          05:42:24 -0600 (Mon, 30 Jan 2006) $
 */
public class NCIThesaurusHistoryFile extends URIBase implements InputFormatInterface {
    public static String description = "NCI Thesaurus History File";
    private URI historyVersionFileLocation_;

    public NCIThesaurusHistoryFile(URI fileLocation, URI historyVersionFileLocation) {
        this.fileLocation = fileLocation;
        this.historyVersionFileLocation_ = historyVersionFileLocation;
    }

    public NCIThesaurusHistoryFile() {

    }

    public String testConnection() throws ConnectionFailure {
        super.testConnection();
        try {
            NCIThesaurusHistoryFileToSQL.validateFile(this.fileLocation, this.historyVersionFileLocation_, null, false);
        } catch (Exception e) {
            throw new ConnectionFailure("There was a problem reading the file", e);
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

    public String[] getAvailableTerminologies() {
        return null;
    }

    /**
     * @return the historyVersionFileLocation
     */
    public URI getHistoryVersionFileLocation() {
        return this.historyVersionFileLocation_;
    }

    /**
     * @param historyVersionFileLocation
     *            the historyVersionFileLocation to set
     */
    public void setHistoryVersionFileLocation(URI historyVersionFileLocation) {
        this.historyVersionFileLocation_ = historyVersionFileLocation;
    }
}
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

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URI;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.exceptions.UnexpectedError;
import edu.mayo.informatics.lexgrid.convert.formats.InputFormatInterface;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.baseFormats.URIBase;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridLDAPOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridXMLOut;

/**
 * Format interface for converting RRF files to SQL.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class SemNetFiles extends URIBase implements InputFormatInterface {

    public static final String description = "Semantic Net Files";

    public SemNetFiles() {

    }

    public SemNetFiles(URI location) {
        this.fileLocation = location;
    }

    public String[] getSupportedOutputFormats() {
        return new String[] { LexGridSQLOut.description, LexGridLDAPOut.description, LexGridXMLOut.description };
    }

    public String getDescription() {
        return description;
    }

    public String testConnection() throws ConnectionFailure {
        if (fileLocation == null) {
            throw new ConnectionFailure("The file location is required");
        }

        try {
            URI temp = fileLocation.resolve("SRDEF");

            if (temp == null) {
                throw new ConnectionFailure("Did not find the expected SRDEF file in the location provided.");
            }

            if (temp.getScheme().equals("file")) {
                new FileReader(new File(temp)).close();
            } else {
                new InputStreamReader(temp.toURL().openConnection().getInputStream()).close();
            }
        }

        catch (Exception e) {
            throw new ConnectionFailure("The required file 'SRDEF' cannot be read");
        }
        return "";
    }

    public Option[] getOptions() {
        return new Option[] {};
    }

    public String getConnectionSummary() {
        return getConnectionSummary(description);
    }

    public String[] getAvailableTerminologies() throws ConnectionFailure, UnexpectedError {
        return null;
    }
}
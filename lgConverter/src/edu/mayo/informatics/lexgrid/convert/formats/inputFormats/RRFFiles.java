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

import org.LexGrid.messaging.impl.NullMessageDirector;

import edu.mayo.informatics.lexgrid.convert.directConversions.UmlsCommon.LoadRRFToDB;
import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.exceptions.UnexpectedError;
import edu.mayo.informatics.lexgrid.convert.formats.InputFormatInterface;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.baseFormats.URIBase;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.SQLOut;

/**
 * Format interface for converting RRF files to SQL.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RRFFiles extends URIBase implements InputFormatInterface {

    public static final String description = "RRF Text Files";

    public RRFFiles() {

    }

    public RRFFiles(URI location) {
        this.fileLocation = location;
    }

    public String[] getSupportedOutputFormats() {
        return new String[] { SQLOut.description };
    }

    public String getDescription() {
        return description;
    }

    public String testConnection() throws ConnectionFailure {
        if (fileLocation == null) {
            throw new ConnectionFailure("The file location is required");
        }

        try {
            URI temp = fileLocation.resolve("MRDOC.RRF");

            if (temp == null) {
                throw new ConnectionFailure("Did not find the expected RRF file in the location provided.");
            }

            if (temp.getScheme().equals("file")) {
                new FileReader(new File(temp)).close();
            } else {
                new InputStreamReader(temp.toURL().openConnection().getInputStream()).close();
            }
        }

        catch (Exception e) {
            throw new ConnectionFailure("The required file 'MRDOC.RRF' cannot be read");
        }

        try {
            // the true says only check files used by lexgrid.
            // not exactly correct, but the best I can do (no access to options
            // here)
            LoadRRFToDB.validateRRF(fileLocation, true, new NullMessageDirector());
        } catch (Exception e) {
            throw new ConnectionFailure("There is a problem with this location", e);
        }

        return "";
    }

    public Option[] getOptions() {
        return new Option[] { new Option(Option.SKIP_NON_LEXGRID_FILES, new Boolean(true)) };
    }

    public String getConnectionSummary() {
        return getConnectionSummary(description);
    }

    public String[] getAvailableTerminologies() throws ConnectionFailure, UnexpectedError {
        return null;
    }
}
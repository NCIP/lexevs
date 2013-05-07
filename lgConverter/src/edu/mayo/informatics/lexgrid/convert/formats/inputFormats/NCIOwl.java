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

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.InputFormatInterface;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.baseFormats.FileBase;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridLDAPOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridXMLOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.OBOOut;

/**
 * Details for reading from NCI Owl files.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 8668 $ checked in on $Date: 2008-06-09
 *          19:35:33 +0000 (Mon, 09 Jun 2008) $
 */
public class NCIOwl extends FileBase implements InputFormatInterface {
    public final static String description = "NCI Owl File";

    protected URI manifestLocation = null;

    public NCIOwl(String fileLocation, URI manifestLocation) {
        this.fileLocation = fileLocation;
        this.manifestLocation = manifestLocation;
    }

    public NCIOwl() {

    }

    public String getDescription() {
        return description;
    }

    public String[] getSupportedOutputFormats() {
        return new String[] { LexGridSQLOut.description, LexGridLDAPOut.description, LexGridXMLOut.description,
                OBOOut.description };
    }

    public String getConnectionSummary() {
        return getConnectionSummary(description);
    }

    public Option[] getOptions() {
        return new Option[] {};
    }

    public String[] getAvailableTerminologies() {
        return null;
    }

    public URI getManifestLocation() {
        return manifestLocation;
    }

    public void setManifestLocation(URI manifestLocation) {
        this.manifestLocation = manifestLocation;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeedu.mayo.informatics.lexgrid.convert.formats.baseFormats.FileBase#
     * testConnection()
     */
    public String testConnection() throws ConnectionFailure {
        String superTest = super.testConnection();
        return superTest + "";
    }
}
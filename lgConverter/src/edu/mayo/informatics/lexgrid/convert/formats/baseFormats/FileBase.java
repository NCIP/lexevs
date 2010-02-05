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
package edu.mayo.informatics.lexgrid.convert.formats.baseFormats;

import java.io.File;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;

/**
 * Common bits for formats that read or write from a file.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 8558 $ checked in on $Date: 2008-06-04
 *          16:05:01 +0000 (Wed, 04 Jun 2008) $
 */
public class FileBase extends CommonBase {
    protected String fileLocation = "";

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getConnectionSummary(String description) {
        return description + " will be loaded from '" + fileLocation + "'";
    }

    public String testConnection() throws ConnectionFailure {
        if (fileLocation == null || fileLocation.length() == 0) {
            throw new ConnectionFailure("The file location is required");
        }
        File file = new File(fileLocation);
        if (file.exists()) {
            return super.testConnection() + "";
        } else {
            throw new ConnectionFailure("The file '" + fileLocation + "' does not exist");
        }
    }

    public String getFileLocation() {
        return fileLocation;
    }
}
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
package edu.mayo.informatics.lexgrid.convert.formats.outputFormats;

import java.io.File;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.OutputFormatInterface;

/**
 * Details for writing an OBO file.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 1052 $ checked in on $Date: 2006-01-30
 *          11:42:24 +0000 (Mon, 30 Jan 2006) $
 */
public class OBOOut implements OutputFormatInterface {
    public static final String description = "OBO File";
    protected String folderLocation_;

    public OBOOut(String folderLocation) {
        this.folderLocation_ = folderLocation;
    }

    public OBOOut() {

    }

    public String getConnectionSummary() {
        StringBuffer temp = new StringBuffer(description + "\n");
        temp.append("A file (with the name matching the terminology) will be written to the folder " + folderLocation_);
        return temp.toString();
    }

    public String getDescription() {
        return description;
    }

    public Option[] getOptions() {
        return new Option[] { new Option(Option.OVERWRITE, new Boolean(true)),
                new Option(Option.FAIL_ON_ERROR, new Boolean(true)) };
    }

    public String getFolderLocation() {
        return folderLocation_;
    }

    public String testConnection() throws ConnectionFailure {
        File file = new File(folderLocation_);
        if (file.exists() && file.isDirectory()) {
            return "";
        } else {
            throw new ConnectionFailure("The directory '" + folderLocation_ + "' does not exist");
        }
    }

}
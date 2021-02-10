
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

package edu.mayo.informatics.lexgrid.convert.formats.baseFormats;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URI;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;

/**
 * Common bits for formats that read or write from a file.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 1052 $ checked in on $Date: 2006-01-30
 *          05:42:24 -0600 (Mon, 30 Jan 2006) $
 */
public class URIBase extends CommonBase {
    protected URI fileLocation;

    public void setFileLocation(URI fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getConnectionSummary(String description) {
        return description + " will be loaded from '" + fileLocation + "'";
    }

    public String testConnection() throws ConnectionFailure {
        if (fileLocation == null) {
            throw new ConnectionFailure("The file location is required");
        }

        try {
            if (fileLocation.getScheme().equals("file")) {
                new FileReader(new File(fileLocation));
            } else {
                new InputStreamReader(fileLocation.toURL().openConnection().getInputStream());
            }
        }

        catch (Exception e) {
            throw new ConnectionFailure("The file '" + fileLocation + "' cannot be read");
        }

        return super.testConnection() + "";
    }

    public URI getFileLocation() {
        return fileLocation;
    }

}
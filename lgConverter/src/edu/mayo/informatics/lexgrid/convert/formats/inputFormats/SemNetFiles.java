
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
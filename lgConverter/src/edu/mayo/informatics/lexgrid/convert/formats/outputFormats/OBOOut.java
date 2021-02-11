
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
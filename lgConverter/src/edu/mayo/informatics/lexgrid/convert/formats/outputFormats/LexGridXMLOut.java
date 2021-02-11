
package edu.mayo.informatics.lexgrid.convert.formats.outputFormats;

import java.io.File;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.OutputFormatInterface;

/**
 * Details for writing an XML file.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class LexGridXMLOut implements OutputFormatInterface {
    public static final String description = "LexGrid XML File";
    protected String folderLocation_;

    public LexGridXMLOut(String folderLocation) {
        this.folderLocation_ = folderLocation;
    }

    public LexGridXMLOut() {

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
        return new Option[] { new Option(Option.OVERWRITE, Boolean.TRUE),
                new Option(Option.FAIL_ON_ERROR, Boolean.FALSE) };
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
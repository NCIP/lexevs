
package edu.mayo.informatics.lexgrid.convert.formats.inputFormats;

import java.net.URI;

import edu.mayo.informatics.lexgrid.convert.formats.InputFormatInterface;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.baseFormats.URIBase;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridLDAPOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridXMLOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.OBOOut;

/**
 * Details for reading OBO files.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class OBO extends URIBase implements InputFormatInterface {
    public static final String description = "OBO File";

    public OBO(URI fileLocation) {
        this.fileLocation = fileLocation;
    }

    public OBO() {

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
}
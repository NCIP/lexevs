
package edu.mayo.informatics.lexgrid.convert.formats.inputFormats;

import org.LexGrid.LexOnt.CodingSchemeManifest;

import edu.mayo.informatics.lexgrid.convert.formats.InputFormatInterface;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.baseFormats.FileBase;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridLDAPOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridXMLOut;

/**
 * Details for reading a LexGrid XML file.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 8819 $ checked in on $Date: 2008-06-13
 *          16:15:16 +0000 (Fri, 13 Jun 2008) $
 */
public class LexGridXML extends FileBase implements InputFormatInterface {
    public static final String description = "LexGrid XML File";

    public LexGridXML(String fileLocation, CodingSchemeManifest codingSchemeManifest) {
        this.fileLocation = fileLocation;
        setCodingSchemeManifest(codingSchemeManifest);
    }

    public LexGridXML() {

    }

    public String getDescription() {
        return description;
    }

    public String[] getSupportedOutputFormats() {
        return new String[] { LexGridSQLOut.description, LexGridLDAPOut.description, LexGridXMLOut.description };
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
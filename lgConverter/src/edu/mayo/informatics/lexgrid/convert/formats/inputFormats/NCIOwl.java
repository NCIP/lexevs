
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
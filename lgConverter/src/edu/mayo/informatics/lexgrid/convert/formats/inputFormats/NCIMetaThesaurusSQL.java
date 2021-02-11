
package edu.mayo.informatics.lexgrid.convert.formats.inputFormats;

import edu.mayo.informatics.lexgrid.convert.formats.InputFormatInterface;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.baseFormats.SQLBase;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;

/**
 * Details for reading from NCIMetaThesaurus format.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class NCIMetaThesaurusSQL extends SQLBase implements InputFormatInterface {
    public static final String description = "NCI MetaThesaurus SQL Database";

    public NCIMetaThesaurusSQL(String username, String password, String server, String driver) {
        this.username = username;
        this.password = password;
        this.server = server;
        this.driver = driver;
    }

    public NCIMetaThesaurusSQL() {

    }

    public String getConnectionSummary() {
        return getConnectionSummary(description);
    }

    public String getDescription() {
        return description;
    }

    public Option[] getOptions() {
        return new Option[] {};
    }

    public String[] getSupportedOutputFormats() {
        return new String[] { LexGridSQLOut.description };
    }

    public String[] getAvailableTerminologies() {
        return null;
    }
}
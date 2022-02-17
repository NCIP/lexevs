
package edu.mayo.informatics.lexgrid.convert.formats.outputFormats;

import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.OutputFormatInterface;
import edu.mayo.informatics.lexgrid.convert.formats.baseFormats.SQLBase;

/**
 * Details for writing to SQL.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 1052 $ checked in on $Date: 2006-01-30
 *          05:42:24 -0600 (Mon, 30 Jan 2006) $
 */
public class SQLOut extends SQLBase implements OutputFormatInterface {
    public static final String description = "SQL Database";

    public SQLOut(String username, String password, String server, String driver) {
        this.username = username;
        this.password = password;
        this.server = server;
        this.driver = driver;
    }

    public SQLOut() {

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
}
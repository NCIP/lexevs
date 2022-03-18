
package edu.mayo.informatics.lexgrid.convert.formats.outputFormats;

import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.OutputFormatInterface;
import edu.mayo.informatics.lexgrid.convert.formats.baseFormats.SQLBase;

/**
 * Details for writing to SQL.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class LexGridSQLOut extends SQLBase implements OutputFormatInterface {
    public static final String description = "LexGrid SQL Database";

    public LexGridSQLOut(String username, String password, String server, String driver, String tablePrefix) {
        this.username = username;
        this.password = password;
        this.server = server;
        this.driver = driver;
        this.tablePrefix = tablePrefix;
    }

    public LexGridSQLOut() {

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
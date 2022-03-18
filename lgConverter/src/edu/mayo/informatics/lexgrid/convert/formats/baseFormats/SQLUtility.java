
package edu.mayo.informatics.lexgrid.convert.formats.baseFormats;

import java.sql.Connection;

import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;

/**
 * Utility stuff for formats that read or write to SQL.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 8481 $ checked in on $Date: 2008-05-30
 *          19:31:55 +0000 (Fri, 30 May 2008) $
 */
public class SQLUtility {

    public static String testSQLConnection(String username, String password, String server, String driver,
            String tablePrefix) throws ConnectionFailure

    {
        try {
            String warning = "";
            Connection c = DBUtility.connectToDatabase(server, driver, username, password);

            SQLTableUtilities stu = new SQLTableUtilities(c, tablePrefix);
            String version = stu.getExistingTableVersion();
            if (version == null) {
                // no tables present - they will be created - so no problem.
            }// current version and version 1.2 are supported
            else if (!version.equals(SQLTableUtilities.versionString) && !version.equals("1.5")) {
                warning = "The existing LexGrid tables are an older (or newer) version than what this tool expects - you may get unexpected errors."
                        + System.getProperty("line.separator")
                        + "I was expecting "
                        + SQLTableUtilities.versionString
                        + " but found " + version;
            }

            c.close();
            return warning;
        } catch (Exception e) {
            throw new ConnectionFailure(e.toString());
        }
    }
}
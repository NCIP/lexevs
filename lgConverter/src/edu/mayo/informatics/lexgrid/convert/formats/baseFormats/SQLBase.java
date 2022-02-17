
package edu.mayo.informatics.lexgrid.convert.formats.baseFormats;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;

/**
 * Common bits for formats that read or write from sql.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 8558 $ checked in on $Date: 2008-06-04
 *          16:05:01 +0000 (Wed, 04 Jun 2008) $
 */
public class SQLBase extends CommonBase {

    protected String username;
    protected String password;
    protected String server;
    protected String driver;
    protected String tablePrefix;

    protected String getConnectionSummary(String description) {
        StringBuffer temp = new StringBuffer();
        temp.append(description + "\n");
        temp.append("Server: " + server + "\n");
        temp.append("Driver: " + driver + "\n");
        temp.append("Username: " + username + "\n");
        temp.append("Table Prefix: " + tablePrefix + "\n");
        return temp.toString();
    }

    public String testConnection() throws ConnectionFailure {
        if (server == null || server.length() == 0) {
            throw new ConnectionFailure("The connection string is required");
        }
        if (driver == null || driver.length() == 0) {
            throw new ConnectionFailure("The driver name is required");
        }
        return SQLUtility.testSQLConnection(username, password, server, driver, tablePrefix);

    }

    public String getDriver() {
        return this.driver;
    }

    public String getPassword() {
        return this.password;
    }

    public String getServer() {
        return this.server;
    }

    public String getUsername() {
        return this.username;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

}
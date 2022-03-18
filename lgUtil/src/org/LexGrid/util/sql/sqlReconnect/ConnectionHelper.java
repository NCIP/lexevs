
package org.LexGrid.util.sql.sqlReconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * This class is just a hack so that I can get the driver manager to connect to
 * a sql database with a driver that was loaded using a different class loader.
 * 
 * This class needs to be loaded by the same class loader that registered the
 * sql driver(s).
 * 
 * @author armbrust
 */
public class ConnectionHelper implements ConnectionHelperIF {
    public Connection getConnection(String url, Properties properties) throws SQLException {
        return DriverManager.getConnection(url, properties);
    }

}
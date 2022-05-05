/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.platform.cloudscape.CloudscapePlatform;
import org.apache.ddlutils.platform.derby.DerbyBuilder;
import org.apache.ddlutils.platform.derby.DerbyModelReader;

public class DerbyPlatform
extends CloudscapePlatform {
    public static final String DATABASENAME = "Derby";
    public static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    public static final String JDBC_DRIVER_EMBEDDED = "org.apache.derby.jdbc.EmbeddedDriver";
    public static final String JDBC_SUBPROTOCOL = "derby";

    public DerbyPlatform() {
        this.getPlatformInfo().addNativeTypeMapping(8, "DOUBLE");
        this.getPlatformInfo().addNativeTypeMapping(6, "DOUBLE", 8);
        this.setSqlBuilder(new DerbyBuilder(this));
        this.setModelReader(new DerbyModelReader(this));
    }


    public String getName() {
        return DATABASENAME;
    }


    public void createDatabase(String jdbcDriverClassName, String connectionUrl, String username, String password, Map<String,String> parameters) throws DatabaseOperationException, UnsupportedOperationException {
        block14: {
            if (JDBC_DRIVER.equals(jdbcDriverClassName) || JDBC_DRIVER_EMBEDDED.equals(jdbcDriverClassName)) {
                StringBuffer creationUrl = new StringBuffer();
                Connection connection = null;
                creationUrl.append(connectionUrl);
                creationUrl.append(";create=true");
                if (parameters != null && !parameters.isEmpty()) {
                    for (Map.Entry entry : parameters.entrySet()) {
                        if ("create".equalsIgnoreCase(entry.getKey().toString())) continue;
                        creationUrl.append(";");
                        creationUrl.append(entry.getKey().toString());
                        creationUrl.append("=");
                        if (entry.getValue() == null) continue;
                        creationUrl.append(entry.getValue().toString());
                    }
                }
                if (this.getLog().isDebugEnabled()) {
                    this.getLog().debug((Object)("About to create database using this URL: " + creationUrl.toString()));
                }
                try {
                    try {
                        Class.forName(jdbcDriverClassName);
                        connection = DriverManager.getConnection(creationUrl.toString(), username, password);
                        this.logWarnings(connection);
                        break block14;
                    }
                    catch (Exception ex) {
                        throw new DatabaseOperationException("Error while trying to create a database", ex);
                    }
                }
                finally {
                    if (connection != null) {
                        try {
                            connection.close();
                        }
                        catch (SQLException sQLException) {}
                    }
                }
            }
            throw new UnsupportedOperationException("Unable to create a Derby database via the driver " + jdbcDriverClassName);
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.mckoi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.platform.PlatformImplBase;
import org.apache.ddlutils.platform.mckoi.MckoiBuilder;
import org.apache.ddlutils.platform.mckoi.MckoiModelReader;

public class MckoiPlatform
extends PlatformImplBase {
    public static final String DATABASENAME = "McKoi";
    public static final String JDBC_DRIVER = "com.mckoi.JDBCDriver";
    public static final String JDBC_SUBPROTOCOL = "mckoi";

    public MckoiPlatform() {
        PlatformInfo info = this.getPlatformInfo();
        info.setIndicesSupported(false);
        info.setIndicesEmbedded(true);
        info.setDefaultValueUsedForIdentitySpec(true);
        info.setAutoCommitModeForLastIdentityValueReading(false);
        info.addNativeTypeMapping(2003, "BLOB", 2004);
        info.addNativeTypeMapping(2001, "BLOB", 2004);
        info.addNativeTypeMapping(6, "DOUBLE", 8);
        info.addNativeTypeMapping(0, "BLOB", 2004);
        info.addNativeTypeMapping(1111, "BLOB", 2004);
        info.addNativeTypeMapping(2006, "BLOB", 2004);
        info.addNativeTypeMapping(2002, "BLOB", 2004);
        info.addNativeTypeMapping("BIT", "BOOLEAN", "BOOLEAN");
        info.addNativeTypeMapping("DATALINK", "BLOB", "BLOB");
        info.setDefaultSize(1, 1024);
        info.setDefaultSize(12, 1024);
        info.setDefaultSize(-2, 1024);
        info.setDefaultSize(-3, 1024);
        this.setSqlBuilder(new MckoiBuilder(this));
        this.setModelReader(new MckoiModelReader(this));
    }


    public String getName() {
        return DATABASENAME;
    }

    @Override
    public void createDatabase(String jdbcDriverClassName, String connectionUrl, String username, String password, Map parameters) throws DatabaseOperationException, UnsupportedOperationException {
        block14: {
            if (JDBC_DRIVER.equals(jdbcDriverClassName)) {
                StringBuffer creationUrl = new StringBuffer();
                Connection connection = null;
                creationUrl.append(connectionUrl);
                creationUrl.append("?create=true");
                if (parameters != null && !parameters.isEmpty()) {
                    for (Object entryObject: parameters.entrySet()) {
                        if(entryObject instanceof Map.Entry){
                            Map.Entry entry = (Map.Entry)entryObject;
                        if ("create".equalsIgnoreCase(entry.getKey().toString())) continue;
                        creationUrl.append("&");
                        creationUrl.append(entry.getKey().toString());
                        creationUrl.append("=");
                        if (entry.getValue() == null) continue;
                        creationUrl.append(entry.getValue().toString());
                    }
                }}
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


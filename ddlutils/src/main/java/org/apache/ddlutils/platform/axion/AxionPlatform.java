/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.axion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.platform.PlatformImplBase;
import org.apache.ddlutils.platform.axion.AxionBuilder;
import org.apache.ddlutils.platform.axion.AxionModelReader;

public class AxionPlatform
extends PlatformImplBase {
    public static final String DATABASENAME = "Axion";
    public static final String JDBC_DRIVER = "org.axiondb.jdbc.AxionDriver";
    public static final String JDBC_SUBPROTOCOL = "axiondb";

    public AxionPlatform() {
        PlatformInfo info = this.getPlatformInfo();
        info.setDelimitedIdentifiersSupported(false);
        info.setSqlCommentsSupported(false);
        info.setLastIdentityValueReadable(false);
        info.addNativeTypeMapping(2003, "BLOB", 2004);
        info.addNativeTypeMapping(-7, "BOOLEAN");
        info.addNativeTypeMapping(2001, "VARBINARY", -3);
        info.addNativeTypeMapping(0, "VARBINARY", -3);
        info.addNativeTypeMapping(1111, "BLOB", 2004);
        info.addNativeTypeMapping(7, "REAL", 6);
        info.addNativeTypeMapping(2006, "VARBINARY", -3);
        info.addNativeTypeMapping(2002, "VARBINARY", -3);
        info.addNativeTypeMapping(-6, "SMALLINT", 5);
        info.addNativeTypeMapping("DATALINK", "VARBINARY", "VARBINARY");
        this.setSqlBuilder(new AxionBuilder(this));
        this.setModelReader(new AxionModelReader(this));
    }


    public String getName() {
        return DATABASENAME;
    }


    public void createDatabase(String jdbcDriverClassName, String connectionUrl, String username, String password, Map parameters) throws DatabaseOperationException, UnsupportedOperationException {
        block11: {
            if (JDBC_DRIVER.equals(jdbcDriverClassName)) {
                Connection connection = null;
                try {
                    try {
                        Class.forName(jdbcDriverClassName);
                        connection = DriverManager.getConnection(connectionUrl, username, password);
                        this.logWarnings(connection);
                        break block11;
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
            throw new UnsupportedOperationException("Unable to create a Axion database via the driver " + jdbcDriverClassName);
        }
    }

    @Override
    protected Object extractColumnValue(ResultSet resultSet, String columnName, int columnIdx, int jdbcType) throws SQLException {
        boolean useIdx = columnName == null;
        Object value = null;
        switch (jdbcType) {
            case -5: {
                String strValue = useIdx ? resultSet.getString(columnIdx) : resultSet.getString(columnName);
                value = resultSet.wasNull() ? null : new Long(strValue);
                break;
            }
            default: {
                value = super.extractColumnValue(resultSet, columnName, columnIdx, jdbcType);
            }
        }
        return value;
    }
}


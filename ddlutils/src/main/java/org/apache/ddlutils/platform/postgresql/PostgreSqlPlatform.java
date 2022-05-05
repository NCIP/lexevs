/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.DynaBean
 */
package org.apache.ddlutils.platform.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import org.apache.commons.beanutils.DynaBean;
import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.dynabean.SqlDynaProperty;
import org.apache.ddlutils.platform.PlatformImplBase;
import org.apache.ddlutils.platform.postgresql.PostgreSqlBuilder;
import org.apache.ddlutils.platform.postgresql.PostgreSqlModelReader;

public class PostgreSqlPlatform
extends PlatformImplBase {
    public static final String DATABASENAME = "PostgreSql";
    public static final String JDBC_DRIVER = "org.postgresql.Driver";
    public static final String JDBC_SUBPROTOCOL = "postgresql";

    public PostgreSqlPlatform() {
        PlatformInfo info = this.getPlatformInfo();
        info.setMaxIdentifierLength(31);
        info.addNativeTypeMapping(2003, "BYTEA", -4);
        info.addNativeTypeMapping(-2, "BYTEA", -4);
        info.addNativeTypeMapping(-7, "BOOLEAN");
        info.addNativeTypeMapping(2004, "BYTEA", -4);
        info.addNativeTypeMapping(2005, "TEXT", -1);
        info.addNativeTypeMapping(3, "NUMERIC", 2);
        info.addNativeTypeMapping(2001, "BYTEA", -4);
        info.addNativeTypeMapping(8, "DOUBLE PRECISION");
        info.addNativeTypeMapping(6, "DOUBLE PRECISION", 8);
        info.addNativeTypeMapping(2000, "BYTEA", -4);
        info.addNativeTypeMapping(-4, "BYTEA");
        info.addNativeTypeMapping(-1, "TEXT", -1);
        info.addNativeTypeMapping(0, "BYTEA", -4);
        info.addNativeTypeMapping(1111, "BYTEA", -4);
        info.addNativeTypeMapping(2006, "BYTEA", -4);
        info.addNativeTypeMapping(2002, "BYTEA", -4);
        info.addNativeTypeMapping(-6, "SMALLINT", 5);
        info.addNativeTypeMapping(-3, "BYTEA", -4);
        info.addNativeTypeMapping("BOOLEAN", "BOOLEAN", "BIT");
        info.addNativeTypeMapping("DATALINK", "BYTEA", "LONGVARBINARY");
        info.setDefaultSize(1, 254);
        info.setDefaultSize(12, 254);
        info.setHasSize(-2, false);
        info.setHasSize(-3, false);
        this.setSqlBuilder(new PostgreSqlBuilder(this));
        this.setModelReader(new PostgreSqlModelReader(this));
    }


    public String getName() {
        return DATABASENAME;
    }

    private void createOrDropDatabase(String jdbcDriverClassName, String connectionUrl, String username, String password, Map parameters, boolean createDb) throws DatabaseOperationException, UnsupportedOperationException {
        block20: {
            if (JDBC_DRIVER.equals(jdbcDriverClassName)) {
                int slashPos = connectionUrl.lastIndexOf(47);
                if (slashPos < 0) {
                    throw new DatabaseOperationException("Cannot parse the given connection url " + connectionUrl);
                }
                int paramPos = connectionUrl.lastIndexOf(63);
                String baseDb = String.valueOf(connectionUrl.substring(0, slashPos + 1)) + "template1";
                String dbName = paramPos > slashPos ? connectionUrl.substring(slashPos + 1, paramPos) : connectionUrl.substring(slashPos + 1);
                Connection connection = null;
                Statement stmt = null;
                StringBuffer sql = new StringBuffer();
                sql.append(createDb ? "CREATE" : "DROP");
                sql.append(" DATABASE ");
                sql.append(dbName);
                if (parameters != null && !parameters.isEmpty()) {

                    for (Object entryObject: parameters.entrySet()) {
                        if(entryObject instanceof Map.Entry){
                            Map.Entry entry = (Map.Entry)entryObject;
                        sql.append(" ");
                        sql.append(entry.getKey().toString());
                        if (entry.getValue() == null) continue;
                        sql.append(" ");
                        sql.append(entry.getValue().toString());
                    }}
                }
                if (this.getLog().isDebugEnabled()) {
                    this.getLog().debug((Object)("About to create database via " + baseDb + " using this SQL: " + sql.toString()));
                }
                try {
                    try {
                        Class.forName(jdbcDriverClassName);
                        connection = DriverManager.getConnection(baseDb, username, password);
                        stmt = connection.createStatement();
                        stmt.execute(sql.toString());
                        this.logWarnings(connection);
                        break block20;
                    }
                    catch (Exception ex) {
                        throw new DatabaseOperationException("Error while trying to " + (createDb ? "create" : "drop") + " a database: " + ex.getLocalizedMessage(), ex);
                    }
                }
                finally {
                    if (stmt != null) {
                        try {
                            stmt.close();
                        }
                        catch (SQLException sQLException) {}
                    }
                    if (connection != null) {
                        try {
                            connection.close();
                        }
                        catch (SQLException sQLException) {}
                    }
                }
            }
            throw new UnsupportedOperationException("Unable to " + (createDb ? "create" : "drop") + " a PostgreSQL database via the driver " + jdbcDriverClassName);
        }
    }

    @Override
    public void createDatabase(String jdbcDriverClassName, String connectionUrl, String username, String password, Map parameters) throws DatabaseOperationException, UnsupportedOperationException {
        this.createOrDropDatabase(jdbcDriverClassName, connectionUrl, username, password, parameters, true);
    }

    @Override
    public void dropDatabase(String jdbcDriverClassName, String connectionUrl, String username, String password) throws DatabaseOperationException, UnsupportedOperationException {
        this.createOrDropDatabase(jdbcDriverClassName, connectionUrl, username, password, null, false);
    }

    @Override
    protected void setObject(PreparedStatement statement, int sqlIndex, DynaBean dynaBean, SqlDynaProperty property) throws SQLException {
        int typeCode = property.getColumn().getTypeCode();
        Object value = dynaBean.get(property.getName());
        if (value == null) {
            switch (typeCode) {
                case -4: 
                case -3: 
                case -2: 
                case 2004: {
                    statement.setBytes(sqlIndex, null);
                    break;
                }
                default: {
                    statement.setNull(sqlIndex, typeCode);
                    break;
                }
            }
        } else {
            super.setObject(statement, sqlIndex, dynaBean, property);
        }
    }
}


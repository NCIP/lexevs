/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.mssql;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.PlatformImplBase;
import org.apache.ddlutils.platform.mssql.MSSqlBuilder;
import org.apache.ddlutils.platform.mssql.MSSqlModelReader;

public class MSSqlPlatform
extends PlatformImplBase {
    public static final String DATABASENAME = "MsSql";
    public static final String JDBC_DRIVER = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
    public static final String JDBC_DRIVER_NEW = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String JDBC_SUBPROTOCOL = "microsoft:sqlserver";
    public static final String JDBC_SUBPROTOCOL_NEW = "sqlserver";
    public static final String JDBC_SUBPROTOCOL_INTERNAL = "sqljdbc";

    public MSSqlPlatform() {
        PlatformInfo info = this.getPlatformInfo();
        info.setMaxIdentifierLength(128);
        info.addNativeTypeMapping(2003, "IMAGE", -4);
        info.addNativeTypeMapping(-5, "DECIMAL(19,0)");
        info.addNativeTypeMapping(2004, "IMAGE", -4);
        info.addNativeTypeMapping(2005, "TEXT", -1);
        info.addNativeTypeMapping(91, "DATETIME", 93);
        info.addNativeTypeMapping(2001, "IMAGE", -4);
        info.addNativeTypeMapping(8, "FLOAT", 6);
        info.addNativeTypeMapping(4, "INT");
        info.addNativeTypeMapping(2000, "IMAGE", -4);
        info.addNativeTypeMapping(-4, "IMAGE");
        info.addNativeTypeMapping(-1, "TEXT");
        info.addNativeTypeMapping(0, "IMAGE", -4);
        info.addNativeTypeMapping(1111, "IMAGE", -4);
        info.addNativeTypeMapping(2006, "IMAGE", -4);
        info.addNativeTypeMapping(2002, "IMAGE", -4);
        info.addNativeTypeMapping(92, "DATETIME", 93);
        info.addNativeTypeMapping(93, "DATETIME");
        info.addNativeTypeMapping(-6, "SMALLINT", 5);
        info.addNativeTypeMapping("BOOLEAN", "BIT", "BIT");
        info.addNativeTypeMapping("DATALINK", "IMAGE", "LONGVARBINARY");
        info.setDefaultSize(1, 254);
        info.setDefaultSize(12, 254);
        info.setDefaultSize(-2, 254);
        info.setDefaultSize(-3, 254);
        this.setSqlBuilder(new MSSqlBuilder(this));
        this.setModelReader(new MSSqlModelReader(this));
    }


    public String getName() {
        return DATABASENAME;
    }

    private boolean useIdentityOverrideFor(Table table) {
        return this.isIdentityOverrideOn() && this.getPlatformInfo().isIdentityOverrideAllowed() && table.getAutoIncrementColumns().length > 0;
    }

    @Override
    protected void beforeInsert(Connection connection, Table table) throws SQLException {
        if (this.useIdentityOverrideFor(table)) {
            MSSqlBuilder builder = (MSSqlBuilder)this.getSqlBuilder();
            connection.createStatement().execute(builder.getEnableIdentityOverrideSql(table));
        }
    }

    @Override
    protected void afterInsert(Connection connection, Table table) throws SQLException {
        if (this.useIdentityOverrideFor(table)) {
            MSSqlBuilder builder = (MSSqlBuilder)this.getSqlBuilder();
            connection.createStatement().execute(builder.getDisableIdentityOverrideSql(table));
        }
    }

    @Override
    protected void beforeUpdate(Connection connection, Table table) throws SQLException {
        this.beforeInsert(connection, table);
    }

    @Override
    protected void afterUpdate(Connection connection, Table table) throws SQLException {
        this.afterInsert(connection, table);
    }
}


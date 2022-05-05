/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.hsqldb;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.platform.PlatformImplBase;
import org.apache.ddlutils.platform.hsqldb.HsqlDbBuilder;
import org.apache.ddlutils.platform.hsqldb.HsqlDbModelReader;

public class HsqlDbPlatform
extends PlatformImplBase {
    public static final String DATABASENAME = "HsqlDb";
    public static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";
    public static final String JDBC_SUBPROTOCOL = "hsqldb";

    public HsqlDbPlatform() {
        PlatformInfo info = this.getPlatformInfo();
        info.setNonPKIdentityColumnsSupported(false);
        info.setIdentityOverrideAllowed(false);
        info.setSystemForeignKeyIndicesAlwaysNonUnique(true);
        info.addNativeTypeMapping(2003, "LONGVARBINARY", -4);
        info.addNativeTypeMapping(2004, "LONGVARBINARY", -4);
        info.addNativeTypeMapping(2005, "LONGVARCHAR", -1);
        info.addNativeTypeMapping(2001, "LONGVARBINARY", -4);
        info.addNativeTypeMapping(6, "DOUBLE", 8);
        info.addNativeTypeMapping(2000, "OBJECT");
        info.addNativeTypeMapping(0, "LONGVARBINARY", -4);
        info.addNativeTypeMapping(2006, "LONGVARBINARY", -4);
        info.addNativeTypeMapping(2002, "LONGVARBINARY", -4);
        info.addNativeTypeMapping(-6, "SMALLINT", 5);
        info.addNativeTypeMapping("BIT", "BOOLEAN", "BOOLEAN");
        info.addNativeTypeMapping("DATALINK", "LONGVARBINARY", "LONGVARBINARY");
        info.setDefaultSize(1, Integer.MAX_VALUE);
        info.setDefaultSize(12, Integer.MAX_VALUE);
        info.setDefaultSize(-2, Integer.MAX_VALUE);
        info.setDefaultSize(-3, Integer.MAX_VALUE);
        this.setSqlBuilder(new HsqlDbBuilder(this));
        this.setModelReader(new HsqlDbModelReader(this));
    }


    public String getName() {
        return DATABASENAME;
    }

    @Override
    public void shutdownDatabase(Connection connection) {
        Statement stmt = null;
        try {
            try {
                stmt = connection.createStatement();
                stmt.executeUpdate("SHUTDOWN");
            }
            catch (SQLException ex) {
                throw new DdlUtilsException(ex);
            }
        }
        finally {
            this.closeStatement(stmt);
        }
    }
}


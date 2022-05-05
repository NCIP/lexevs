/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.db2;

import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.platform.PlatformImplBase;
import org.apache.ddlutils.platform.db2.Db2Builder;
import org.apache.ddlutils.platform.db2.Db2ModelReader;

public class Db2Platform
extends PlatformImplBase {
    public static final String DATABASENAME = "DB2";
    public static final String JDBC_DRIVER = "com.ibm.db2.jcc.DB2Driver";
    public static final String JDBC_DRIVER_OLD1 = "COM.ibm.db2.jdbc.app.DB2Driver";
    public static final String JDBC_DRIVER_OLD2 = "COM.ibm.db2os390.sqlj.jdbc.DB2SQLJDriver";
    public static final String JDBC_DRIVER_JTOPEN = "com.ibm.as400.access.AS400JDBCDriver";
    public static final String JDBC_SUBPROTOCOL = "db2";
    public static final String JDBC_SUBPROTOCOL_OS390_1 = "db2os390";
    public static final String JDBC_SUBPROTOCOL_OS390_2 = "db2os390sqlj";
    public static final String JDBC_SUBPROTOCOL_JTOPEN = "as400";

    public Db2Platform() {
        PlatformInfo info = this.getPlatformInfo();
        info.setMaxTableNameLength(-1);
        info.setMaxConstraintNameLength(18);
        info.setMaxForeignKeyNameLength(18);
        info.addNativeTypeMapping(2003, "BLOB", 2004);
        info.addNativeTypeMapping(-2, "CHAR {0} FOR BIT DATA");
        info.addNativeTypeMapping(-7, "SMALLINT", 5);
        info.addNativeTypeMapping(6, "DOUBLE", 8);
        info.addNativeTypeMapping(2000, "BLOB", 2004);
        info.addNativeTypeMapping(-4, "LONG VARCHAR FOR BIT DATA");
        info.addNativeTypeMapping(-1, "LONG VARCHAR");
        info.addNativeTypeMapping(0, "LONG VARCHAR FOR BIT DATA", -4);
        info.addNativeTypeMapping(2, "DECIMAL", 3);
        info.addNativeTypeMapping(1111, "BLOB", 2004);
        info.addNativeTypeMapping(2002, "BLOB", 2004);
        info.addNativeTypeMapping(-6, "SMALLINT", 5);
        info.addNativeTypeMapping(-3, "VARCHAR {0} FOR BIT DATA");
        info.addNativeTypeMapping("BOOLEAN", "SMALLINT", "SMALLINT");
        info.setDefaultSize(1, 254);
        info.setDefaultSize(12, 254);
        info.setDefaultSize(-2, 254);
        info.setDefaultSize(-3, 254);
        this.setSqlBuilder(new Db2Builder(this));
        this.setModelReader(new Db2ModelReader(this));
    }


    public String getName() {
        return DATABASENAME;
    }
}


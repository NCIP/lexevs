/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.sapdb;

import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.platform.PlatformImplBase;
import org.apache.ddlutils.platform.sapdb.SapDbBuilder;
import org.apache.ddlutils.platform.sapdb.SapDbModelReader;

public class SapDbPlatform
extends PlatformImplBase {
    public static final String DATABASENAME = "SapDB";
    public static final String JDBC_DRIVER = "com.sap.dbtech.jdbc.DriverSapDB";
    public static final String JDBC_SUBPROTOCOL = "sapdb";

    public SapDbPlatform() {
        PlatformInfo info = this.getPlatformInfo();
        info.setMaxIdentifierLength(32);
        info.setCommentPrefix("/*");
        info.setCommentSuffix("*/");
        info.addNativeTypeMapping(2003, "LONG BYTE", -4);
        info.addNativeTypeMapping(-5, "FIXED(38,0)");
        info.addNativeTypeMapping(-2, "CHAR{0} BYTE");
        info.addNativeTypeMapping(-7, "BOOLEAN");
        info.addNativeTypeMapping(2004, "LONG BYTE", -4);
        info.addNativeTypeMapping(2005, "LONG", -1);
        info.addNativeTypeMapping(3, "FIXED");
        info.addNativeTypeMapping(2001, "LONG BYTE", -4);
        info.addNativeTypeMapping(8, "FLOAT(38)", 6);
        info.addNativeTypeMapping(6, "FLOAT(38)");
        info.addNativeTypeMapping(2000, "LONG BYTE", -4);
        info.addNativeTypeMapping(-4, "LONG BYTE");
        info.addNativeTypeMapping(-1, "LONG");
        info.addNativeTypeMapping(0, "LONG BYTE", -4);
        info.addNativeTypeMapping(2, "FIXED", 3);
        info.addNativeTypeMapping(1111, "LONG BYTE", -4);
        info.addNativeTypeMapping(7, "FLOAT(16)", 6);
        info.addNativeTypeMapping(2006, "LONG BYTE", -4);
        info.addNativeTypeMapping(2002, "LONG BYTE", -4);
        info.addNativeTypeMapping(-6, "SMALLINT", 5);
        info.addNativeTypeMapping(-3, "VARCHAR{0} BYTE");
        info.addNativeTypeMapping("BOOLEAN", "BOOLEAN", "BIT");
        info.addNativeTypeMapping("DATALINK", "LONG BYTE", "LONGVARBINARY");
        info.setDefaultSize(1, 254);
        info.setDefaultSize(12, 254);
        info.setDefaultSize(-2, 254);
        info.setDefaultSize(-3, 254);
        this.setSqlBuilder(new SapDbBuilder(this));
        this.setModelReader(new SapDbModelReader(this));
    }


    public String getName() {
        return DATABASENAME;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.firebird;

import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.platform.PlatformImplBase;
import org.apache.ddlutils.platform.firebird.FirebirdBuilder;
import org.apache.ddlutils.platform.firebird.FirebirdModelReader;

public class FirebirdPlatform
extends PlatformImplBase {
    public static final String DATABASENAME = "Firebird";
    public static final String JDBC_DRIVER = "org.firebirdsql.jdbc.FBDriver";
    public static final String JDBC_SUBPROTOCOL = "firebirdsql";

    public FirebirdPlatform() {
        PlatformInfo info = this.getPlatformInfo();
        info.setMaxIdentifierLength(31);
        info.setSystemForeignKeyIndicesAlwaysNonUnique(true);
        info.setCommentPrefix("/*");
        info.setCommentSuffix("*/");
        info.addNativeTypeMapping(2003, "BLOB", -4);
        info.addNativeTypeMapping(-2, "BLOB", -4);
        info.addNativeTypeMapping(-7, "SMALLINT", 5);
        info.addNativeTypeMapping(2005, "BLOB SUB_TYPE TEXT", -1);
        info.addNativeTypeMapping(2001, "BLOB", -4);
        info.addNativeTypeMapping(2004, "BLOB", -4);
        info.addNativeTypeMapping(8, "DOUBLE PRECISION");
        info.addNativeTypeMapping(6, "DOUBLE PRECISION", 8);
        info.addNativeTypeMapping(2000, "BLOB", -4);
        info.addNativeTypeMapping(-4, "BLOB", -4);
        info.addNativeTypeMapping(-1, "BLOB SUB_TYPE TEXT");
        info.addNativeTypeMapping(0, "BLOB", -4);
        info.addNativeTypeMapping(1111, "BLOB", -4);
        info.addNativeTypeMapping(7, "FLOAT");
        info.addNativeTypeMapping(2006, "BLOB", -4);
        info.addNativeTypeMapping(2002, "BLOB", -4);
        info.addNativeTypeMapping(-6, "SMALLINT", 5);
        info.addNativeTypeMapping(-3, "BLOB", -4);
        info.addNativeTypeMapping("BOOLEAN", "SMALLINT", "SMALLINT");
        info.addNativeTypeMapping("DATALINK", "BLOB", "LONGVARBINARY");
        info.setDefaultSize(12, 254);
        info.setDefaultSize(1, 254);
        this.setSqlBuilder(new FirebirdBuilder(this));
        this.setModelReader(new FirebirdModelReader(this));
    }

    @Override
    public String getName() {
        return DATABASENAME;
    }
}


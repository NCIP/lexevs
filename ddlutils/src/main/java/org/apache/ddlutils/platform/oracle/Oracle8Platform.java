/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.oracle;

import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.platform.PlatformImplBase;
import org.apache.ddlutils.platform.oracle.Oracle8Builder;
import org.apache.ddlutils.platform.oracle.Oracle8ModelReader;

public class Oracle8Platform
extends PlatformImplBase {
    public static final String DATABASENAME = "Oracle";
    public static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    public static final String JDBC_DRIVER_OLD = "oracle.jdbc.dnlddriver.OracleDriver";
    public static final String JDBC_SUBPROTOCOL_THIN = "oracle:thin";
    public static final String JDBC_SUBPROTOCOL_OCI8 = "oracle:oci8";
    public static final String JDBC_SUBPROTOCOL_THIN_OLD = "oracle:dnldthin";

    public Oracle8Platform() {
        PlatformInfo info = this.getPlatformInfo();
        info.setMaxIdentifierLength(-1);
        info.setIdentityStatusReadingSupported(false);
        info.addNativeTypeMapping(2003, "BLOB", 2004);
        info.addNativeTypeMapping(-5, "NUMBER(38)");
        info.addNativeTypeMapping(-2, "RAW", -3);
        info.addNativeTypeMapping(-7, "NUMBER(1)");
        info.addNativeTypeMapping(91, "DATE", 93);
        info.addNativeTypeMapping(3, "NUMBER");
        info.addNativeTypeMapping(2001, "BLOB", 2004);
        info.addNativeTypeMapping(8, "DOUBLE PRECISION");
        info.addNativeTypeMapping(6, "FLOAT", 8);
        info.addNativeTypeMapping(2000, "BLOB", 2004);
        info.addNativeTypeMapping(-4, "BLOB", 2004);
        info.addNativeTypeMapping(-1, "CLOB", 2005);
        info.addNativeTypeMapping(0, "BLOB", 2004);
        info.addNativeTypeMapping(2, "NUMBER", 3);
        info.addNativeTypeMapping(1111, "BLOB", 2004);
        info.addNativeTypeMapping(2006, "BLOB", 2004);
        info.addNativeTypeMapping(5, "NUMBER(5)");
        info.addNativeTypeMapping(2002, "BLOB", 2004);
        info.addNativeTypeMapping(92, "DATE", 93);
        info.addNativeTypeMapping(93, "DATE");
        info.addNativeTypeMapping(-6, "NUMBER(3)");
        info.addNativeTypeMapping(-3, "RAW");
        info.addNativeTypeMapping(12, "VARCHAR2");
        info.addNativeTypeMapping("BOOLEAN", "NUMBER(1)", "BIT");
        info.addNativeTypeMapping("DATALINK", "BLOB", "BLOB");
        info.setDefaultSize(1, 254);
        info.setDefaultSize(12, 254);
        info.setDefaultSize(-2, 254);
        info.setDefaultSize(-3, 254);
        this.setSqlBuilder(new Oracle8Builder(this));
        this.setModelReader(new Oracle8ModelReader(this));
    }


    public String getName() {
        return DATABASENAME;
    }
}


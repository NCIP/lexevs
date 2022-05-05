/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.mysql;

import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.platform.PlatformImplBase;
import org.apache.ddlutils.platform.mysql.MySqlBuilder;
import org.apache.ddlutils.platform.mysql.MySqlModelReader;

public class MySqlPlatform
extends PlatformImplBase {
    public static final String DATABASENAME = "MySQL";
    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String JDBC_DRIVER_OLD = "org.gjt.mm.mysql.Driver";
    public static final String JDBC_SUBPROTOCOL = "mysql";

    public MySqlPlatform() {
        PlatformInfo info = this.getPlatformInfo();
        info.setMaxIdentifierLength(64);
        info.setNullAsDefaultValueRequired(true);
        info.setDefaultValuesForLongTypesSupported(false);
        info.setNonPKIdentityColumnsSupported(false);
        info.setSyntheticDefaultValueForRequiredReturned(true);
        info.setCommentPrefix("#");
        info.setDelimiterToken("`");
        info.addNativeTypeMapping(2003, "LONGBLOB", -4);
        info.addNativeTypeMapping(-7, "TINYINT(1)");
        info.addNativeTypeMapping(2004, "LONGBLOB", -4);
        info.addNativeTypeMapping(2005, "LONGTEXT", -1);
        info.addNativeTypeMapping(2001, "LONGBLOB", -4);
        info.addNativeTypeMapping(6, "DOUBLE", 8);
        info.addNativeTypeMapping(2000, "LONGBLOB", -4);
        info.addNativeTypeMapping(-4, "MEDIUMBLOB");
        info.addNativeTypeMapping(-1, "MEDIUMTEXT");
        info.addNativeTypeMapping(0, "MEDIUMBLOB", -4);
        info.addNativeTypeMapping(2, "DECIMAL", 3);
        info.addNativeTypeMapping(1111, "LONGBLOB", -4);
        info.addNativeTypeMapping(7, "FLOAT");
        info.addNativeTypeMapping(2006, "MEDIUMBLOB", -4);
        info.addNativeTypeMapping(2002, "LONGBLOB", -4);
        info.addNativeTypeMapping(93, "DATETIME");
        info.addNativeTypeMapping(-6, "SMALLINT", 5);
        info.addNativeTypeMapping("BOOLEAN", "TINYINT(1)", "BIT");
        info.addNativeTypeMapping("DATALINK", "MEDIUMBLOB", "LONGVARBINARY");
        info.setDefaultSize(1, 254);
        info.setDefaultSize(12, 254);
        info.setDefaultSize(-2, 254);
        info.setDefaultSize(-3, 254);
        this.setSqlBuilder(new MySqlBuilder(this));
        this.setModelReader(new MySqlModelReader(this));
    }


    public String getName() {
        return DATABASENAME;
    }
}


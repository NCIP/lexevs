/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.cloudscape;

import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.platform.PlatformImplBase;
import org.apache.ddlutils.platform.cloudscape.CloudscapeBuilder;

public class CloudscapePlatform
extends PlatformImplBase {
    public static final String DATABASENAME = "Cloudscape";
    public static final String JDBC_SUBPROTOCOL_1 = "db2j:net";
    public static final String JDBC_SUBPROTOCOL_2 = "cloudscape:net";

    public CloudscapePlatform() {
        PlatformInfo info = this.getPlatformInfo();
        info.setMaxIdentifierLength(128);
        info.setSystemForeignKeyIndicesAlwaysNonUnique(true);
        info.addNativeTypeMapping(2003, "BLOB", 2004);
        info.addNativeTypeMapping(-2, "CHAR {0} FOR BIT DATA");
        info.addNativeTypeMapping(-7, "SMALLINT", 5);
        info.addNativeTypeMapping(2001, "BLOB", 2004);
        info.addNativeTypeMapping(8, "DOUBLE PRECISION");
        info.addNativeTypeMapping(6, "DOUBLE PRECISION", 8);
        info.addNativeTypeMapping(2000, "BLOB", 2004);
        info.addNativeTypeMapping(-4, "LONG VARCHAR FOR BIT DATA");
        info.addNativeTypeMapping(-1, "LONG VARCHAR");
        info.addNativeTypeMapping(0, "LONG VARCHAR FOR BIT DATA", -4);
        info.addNativeTypeMapping(1111, "BLOB", 2004);
        info.addNativeTypeMapping(2006, "LONG VARCHAR FOR BIT DATA", -4);
        info.addNativeTypeMapping(2002, "BLOB", 2004);
        info.addNativeTypeMapping(-6, "SMALLINT", 5);
        info.addNativeTypeMapping(-3, "VARCHAR {0} FOR BIT DATA");
        info.addNativeTypeMapping("BOOLEAN", "SMALLINT", "SMALLINT");
        info.addNativeTypeMapping("DATALINK", "LONG VARCHAR FOR BIT DATA", "LONGVARBINARY");
        info.setDefaultSize(-2, 254);
        info.setDefaultSize(1, 254);
        info.setDefaultSize(-3, 254);
        info.setDefaultSize(12, 254);
        this.setSqlBuilder(new CloudscapeBuilder(this));
    }


    public String getName() {
        return DATABASENAME;
    }
}


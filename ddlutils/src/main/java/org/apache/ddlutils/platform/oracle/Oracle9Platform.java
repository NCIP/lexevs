/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.oracle;

import org.apache.ddlutils.platform.oracle.Oracle8Platform;

public class Oracle9Platform
extends Oracle8Platform {
    public static final String DATABASENAME = "Oracle9";

    public Oracle9Platform() {
        this.getPlatformInfo().addNativeTypeMapping(93, "TIMESTAMP");
    }

    @Override
    public String getName() {
        return DATABASENAME;
    }
}


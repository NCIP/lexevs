/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.oracle;

import org.apache.ddlutils.platform.oracle.Oracle10Builder;
import org.apache.ddlutils.platform.oracle.Oracle10ModelReader;
import org.apache.ddlutils.platform.oracle.Oracle9Platform;

public class Oracle10Platform
extends Oracle9Platform {
    public static final String DATABASENAME = "Oracle10";

    public Oracle10Platform() {
        this.setSqlBuilder(new Oracle10Builder(this));
        this.setModelReader(new Oracle10ModelReader(this));
    }

    @Override
    public String getName() {
        return DATABASENAME;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.maxdb;

import org.apache.ddlutils.platform.maxdb.MaxDbBuilder;
import org.apache.ddlutils.platform.maxdb.MaxDbModelReader;
import org.apache.ddlutils.platform.sapdb.SapDbPlatform;

public class MaxDbPlatform
extends SapDbPlatform {
    public static final String DATABASENAME = "MaxDB";

    public MaxDbPlatform() {
        this.setSqlBuilder(new MaxDbBuilder(this));
        this.setModelReader(new MaxDbModelReader(this));
    }

    @Override
    public String getName() {
        return DATABASENAME;
    }
}


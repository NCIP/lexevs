/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.db2;

import org.apache.ddlutils.platform.db2.Db2Platform;
import org.apache.ddlutils.platform.db2.Db2v8Builder;

public class Db2v8Platform
extends Db2Platform {
    public static final String DATABASENAME = "DB2v8";

    public Db2v8Platform() {
        this.getPlatformInfo().setMaxIdentifierLength(128);
        this.getPlatformInfo().setMaxColumnNameLength(30);
        this.getPlatformInfo().setMaxConstraintNameLength(18);
        this.getPlatformInfo().setMaxForeignKeyNameLength(18);
        this.setSqlBuilder(new Db2v8Builder(this));
    }
}


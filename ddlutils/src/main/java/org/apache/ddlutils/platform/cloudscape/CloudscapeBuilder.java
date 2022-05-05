/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.cloudscape;

import java.io.IOException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.SqlBuilder;

public class CloudscapeBuilder
extends SqlBuilder {
    public CloudscapeBuilder(Platform platform) {
        super(platform);
        this.addEscapedCharSequence("'", "''");
    }

    @Override
    protected void writeColumnAutoIncrementStmt(Table table, Column column) throws IOException {
        this.print("GENERATED ALWAYS AS IDENTITY");
    }

    @Override
    public String getSelectLastIdentityValues(Table table) {
        return "VALUES IDENTITY_VAL_LOCAL()";
    }
}


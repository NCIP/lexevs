/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.oracle;

import java.io.IOException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.oracle.Oracle8Builder;

public class Oracle10Builder
extends Oracle8Builder {
    public Oracle10Builder(Platform platform) {
        super(platform);
    }

    @Override
    public void dropTable(Table table) throws IOException {
        Column[] columns = table.getAutoIncrementColumns();
        int idx = 0;
        while (idx < columns.length) {
            this.dropAutoIncrementTrigger(table, columns[idx]);
            this.dropAutoIncrementSequence(table, columns[idx]);
            ++idx;
        }
        this.print("DROP TABLE ");
        this.printIdentifier(this.getTableName(table));
        this.print(" CASCADE CONSTRAINTS PURGE");
        this.printEndOfStatement();
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.alteration;

import org.apache.ddlutils.alteration.TableChangeImplBase;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

public class RemoveTableChange
extends TableChangeImplBase {
    public RemoveTableChange(Table table) {
        super(table);
    }

    @Override
    public void apply(Database database, boolean caseSensitive) {
        Table table = database.findTable(this.getChangedTable().getName(), caseSensitive);
        database.removeTable(table);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.alteration;

import org.apache.ddlutils.alteration.TableChangeImplBase;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;

public class RemoveIndexChange
extends TableChangeImplBase {
    private Index _index;

    public RemoveIndexChange(Table table, Index index) {
        super(table);
        this._index = index;
    }

    public Index getIndex() {
        return this._index;
    }

    @Override
    public void apply(Database database, boolean caseSensitive) {
        Table table = database.findTable(this.getChangedTable().getName(), caseSensitive);
        Index index = table.findIndex(this._index.getName(), caseSensitive);
        table.removeIndex(index);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.alteration;

import org.apache.ddlutils.alteration.TableChange;
import org.apache.ddlutils.model.Table;

public abstract class TableChangeImplBase
implements TableChange {
    private Table _table;

    public TableChangeImplBase(Table table) {
        this._table = table;
    }

    @Override
    public Table getChangedTable() {
        return this._table;
    }
}


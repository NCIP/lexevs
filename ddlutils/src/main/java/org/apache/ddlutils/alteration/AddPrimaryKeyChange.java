/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.alteration;

import org.apache.ddlutils.alteration.TableChangeImplBase;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

public class AddPrimaryKeyChange
extends TableChangeImplBase {
    private Column[] _primaryKeyColumns;

    public AddPrimaryKeyChange(Table table, Column[] primaryKeyColumns) {
        super(table);
        this._primaryKeyColumns = primaryKeyColumns;
    }

    public Column[] getPrimaryKeyColumns() {
        return this._primaryKeyColumns;
    }

    @Override
    public void apply(Database database, boolean caseSensitive) {
        Table table = database.findTable(this.getChangedTable().getName(), caseSensitive);
        int idx = 0;
        while (idx < this._primaryKeyColumns.length) {
            Column column = table.findColumn(this._primaryKeyColumns[idx].getName(), caseSensitive);
            column.setPrimaryKey(true);
            ++idx;
        }
    }
}


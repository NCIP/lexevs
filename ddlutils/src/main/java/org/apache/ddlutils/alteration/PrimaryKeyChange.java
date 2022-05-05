/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.alteration;

import org.apache.ddlutils.alteration.TableChangeImplBase;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

public class PrimaryKeyChange
extends TableChangeImplBase {
    private Column[] _oldPrimaryKeyColumns;
    private Column[] _newPrimaryKeyColumns;

    public PrimaryKeyChange(Table table, Column[] oldPrimaryKeyColumns, Column[] newPrimaryKeyColumns) {
        super(table);
        this._oldPrimaryKeyColumns = oldPrimaryKeyColumns;
        this._newPrimaryKeyColumns = newPrimaryKeyColumns;
    }

    public Column[] getOldPrimaryKeyColumns() {
        return this._oldPrimaryKeyColumns;
    }

    public Column[] getNewPrimaryKeyColumns() {
        return this._newPrimaryKeyColumns;
    }

    @Override
    public void apply(Database database, boolean caseSensitive) {
        Column column;
        Table table = database.findTable(this.getChangedTable().getName(), caseSensitive);
        int idx = 0;
        while (idx < this._oldPrimaryKeyColumns.length) {
            column = table.findColumn(this._oldPrimaryKeyColumns[idx].getName(), caseSensitive);
            column.setPrimaryKey(false);
            ++idx;
        }
        idx = 0;
        while (idx < this._newPrimaryKeyColumns.length) {
            column = table.findColumn(this._newPrimaryKeyColumns[idx].getName(), caseSensitive);
            column.setPrimaryKey(true);
            ++idx;
        }
    }
}


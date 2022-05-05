/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.alteration;

import org.apache.ddlutils.alteration.ColumnChange;
import org.apache.ddlutils.alteration.TableChangeImplBase;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

public class ColumnDefaultValueChange
extends TableChangeImplBase
implements ColumnChange {
    private Column _column;
    private String _newDefaultValue;

    public ColumnDefaultValueChange(Table table, Column column, String newDefaultValue) {
        super(table);
        this._column = column;
        this._newDefaultValue = newDefaultValue;
    }

    @Override
    public Column getChangedColumn() {
        return this._column;
    }

    public String getNewDefaultValue() {
        return this._newDefaultValue;
    }

    @Override
    public void apply(Database database, boolean caseSensitive) {
        Table table = database.findTable(this.getChangedTable().getName(), caseSensitive);
        Column column = table.findColumn(this._column.getName(), caseSensitive);
        column.setDefaultValue(this._newDefaultValue);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.alteration;

import org.apache.ddlutils.alteration.ColumnChange;
import org.apache.ddlutils.alteration.TableChangeImplBase;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

public class ColumnDataTypeChange
extends TableChangeImplBase
implements ColumnChange {
    private Column _column;
    private int _newTypeCode;

    public ColumnDataTypeChange(Table table, Column column, int newTypeCode) {
        super(table);
        this._column = column;
        this._newTypeCode = newTypeCode;
    }

    @Override
    public Column getChangedColumn() {
        return this._column;
    }

    public int getNewTypeCode() {
        return this._newTypeCode;
    }

    @Override
    public void apply(Database database, boolean caseSensitive) {
        Table table = database.findTable(this.getChangedTable().getName(), caseSensitive);
        Column column = table.findColumn(this._column.getName(), caseSensitive);
        column.setTypeCode(this._newTypeCode);
    }
}


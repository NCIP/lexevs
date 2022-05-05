/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.alteration;

import org.apache.ddlutils.alteration.ColumnChange;
import org.apache.ddlutils.alteration.TableChangeImplBase;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

public class ColumnSizeChange
extends TableChangeImplBase
implements ColumnChange {
    private Column _column;
    private int _newSize;
    private int _newScale;

    public ColumnSizeChange(Table table, Column column, int newSize, int newScale) {
        super(table);
        this._column = column;
        this._newSize = newSize;
        this._newScale = newScale;
    }

    @Override
    public Column getChangedColumn() {
        return this._column;
    }

    public int getNewSize() {
        return this._newSize;
    }

    public int getNewScale() {
        return this._newScale;
    }

    @Override
    public void apply(Database database, boolean caseSensitive) {
        Table table = database.findTable(this.getChangedTable().getName(), caseSensitive);
        Column column = table.findColumn(this._column.getName(), caseSensitive);
        column.setSizeAndScale(this._newSize, this._newScale);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.alteration;

import org.apache.ddlutils.alteration.ColumnChange;
import org.apache.ddlutils.alteration.TableChangeImplBase;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

public class ColumnRequiredChange
extends TableChangeImplBase
implements ColumnChange {
    private Column _column;

    public ColumnRequiredChange(Table table, Column column) {
        super(table);
        this._column = column;
    }

    @Override
    public Column getChangedColumn() {
        return this._column;
    }

    @Override
    public void apply(Database database, boolean caseSensitive) {
        Table table = database.findTable(this.getChangedTable().getName(), caseSensitive);
        Column column = table.findColumn(this._column.getName(), caseSensitive);
        column.setRequired(!this._column.isRequired());
    }
}


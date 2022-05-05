/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.alteration;

import java.util.ArrayList;
import java.util.Map;
import org.apache.ddlutils.alteration.TableChangeImplBase;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

public class ColumnOrderChange
extends TableChangeImplBase {
    private Map _newPositions;

    public ColumnOrderChange(Table table, Map newPositions) {
        super(table);
        this._newPositions = newPositions;
    }

    public int getNewPosition(Column sourceColumn) {
        Integer newPos = (Integer)this._newPositions.get(sourceColumn);
        return newPos == null ? -1 : newPos;
    }

    @Override
    public void apply(Database database, boolean caseSensitive) {
        Table table = database.findTable(this.getChangedTable().getName(), caseSensitive);
        ArrayList<Column> newColumns = new ArrayList<Column>(table.getColumnCount());
        int idx = 0;
        while (idx < table.getColumnCount()) {
            Column column = table.getColumn(idx);
            int newPos = this.getNewPosition(column);
            newColumns.set(newPos < 0 ? idx : newPos, column);
            ++idx;
        }
        idx = 0;
        while (idx < table.getColumnCount()) {
            table.removeColumn(idx);
            ++idx;
        }
        table.addColumns(newColumns);
    }
}


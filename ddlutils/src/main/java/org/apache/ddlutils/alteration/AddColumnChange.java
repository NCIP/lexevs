/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.alteration;

import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.alteration.TableChangeImplBase;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

public class AddColumnChange
extends TableChangeImplBase {
    private Column _newColumn;
    private Column _previousColumn;
    private Column _nextColumn;
    private boolean _atEnd;

    public AddColumnChange(Table table, Column newColumn, Column previousColumn, Column nextColumn) {
        super(table);
        this._newColumn = newColumn;
        this._previousColumn = previousColumn;
        this._nextColumn = nextColumn;
    }

    public Column getNewColumn() {
        return this._newColumn;
    }

    public Column getPreviousColumn() {
        return this._previousColumn;
    }

    public Column getNextColumn() {
        return this._nextColumn;
    }

    public boolean isAtEnd() {
        return this._atEnd;
    }

    public void setAtEnd(boolean atEnd) {
        this._atEnd = atEnd;
    }

    @Override
    public void apply(Database database, boolean caseSensitive) {
        Column newColumn = null;
        try {
            newColumn = (Column)this._newColumn.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new DdlUtilsException(ex);
        }
        Table table = database.findTable(this.getChangedTable().getName(), caseSensitive);
        if (this._previousColumn != null && this._nextColumn != null) {
            int idx = table.getColumnIndex(this._previousColumn) + 1;
            table.addColumn(idx, newColumn);
        } else {
            table.addColumn(newColumn);
        }
    }
}


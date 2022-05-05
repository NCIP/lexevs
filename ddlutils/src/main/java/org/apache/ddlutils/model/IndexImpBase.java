/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.model;

import java.util.ArrayList;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.IndexColumn;

public abstract class IndexImpBase
implements Index {
    protected String _name;
    protected ArrayList _columns = new ArrayList();

    @Override
    public String getName() {
        return this._name;
    }

    @Override
    public void setName(String name) {
        this._name = name;
    }

    @Override
    public int getColumnCount() {
        return this._columns.size();
    }

    @Override
    public IndexColumn getColumn(int idx) {
        return (IndexColumn)this._columns.get(idx);
    }

    @Override
    public IndexColumn[] getColumns() {
        return new IndexColumn[this._columns.size()];
    }

    @Override
    public boolean hasColumn(Column column) {
        int idx = 0;
        while (idx < this._columns.size()) {
            IndexColumn curColumn = this.getColumn(idx);
            if (column.equals(curColumn.getColumn())) {
                return true;
            }
            ++idx;
        }
        return false;
    }

    @Override
    public void addColumn(IndexColumn column) {
        if (column != null) {
            int idx = 0;
            while (idx < this._columns.size()) {
                IndexColumn curColumn = this.getColumn(idx);
                if (curColumn.getOrdinalPosition() > column.getOrdinalPosition()) {
                    this._columns.add(idx, column);
                    return;
                }
                ++idx;
            }
            this._columns.add(column);
        }
    }

    @Override
    public void removeColumn(IndexColumn column) {
        this._columns.remove(column);
    }

    @Override
    public void removeColumn(int idx) {
        this._columns.remove(idx);
    }

    @Override
    public abstract Object clone() throws CloneNotSupportedException;
}


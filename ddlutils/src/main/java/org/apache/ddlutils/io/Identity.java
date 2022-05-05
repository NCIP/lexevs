/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.io;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.ddlutils.model.Table;

public class Identity {
    private Table _table;
    private String _fkName;
    private HashMap<String,Object> _columnValues = new HashMap<String,Object>();

    public Identity(Table table) {
        this._table = table;
    }

    public Identity(Table table, String fkName) {
        this._table = table;
        this._fkName = fkName;
    }

    public Table getTable() {
        return this._table;
    }

    public String getForeignKeyName() {
        return this._fkName;
    }

    public void setColumnValue(String name, Object value) {
        this._columnValues.put(name, value);
    }

    public Object getColumnValue(String name) {
        return this._columnValues.get(name);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Identity)) {
            return false;
        }
        Identity otherIdentity = (Identity)obj;
        if (!this._table.equals(otherIdentity._table)) {
            return false;
        }
        if (this._columnValues.keySet().size() != otherIdentity._columnValues.keySet().size()) {
            return false;
        }
        for (Map.Entry entry : this._columnValues.entrySet()) {
            Object otherValue = otherIdentity._columnValues.get(entry.getKey());
            if (!(entry.getValue() == null ? otherValue != null : !entry.getValue().equals(otherValue))) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this._table.getName());
        buffer.append(":");
        Iterator<Map.Entry<String,Object>> it = this._columnValues.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = it.next();
            buffer.append(entry.getKey());
            buffer.append("=");
            buffer.append(entry.getValue());
            if (!it.hasNext()) continue;
            buffer.append(";");
        }
        return buffer.toString();
    }
}


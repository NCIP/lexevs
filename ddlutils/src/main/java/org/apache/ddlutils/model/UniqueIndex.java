/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.builder.EqualsBuilder
 */
package org.apache.ddlutils.model;

import java.util.ArrayList;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.IndexImpBase;

public class UniqueIndex
extends IndexImpBase {
    private static final long serialVersionUID = -4097003126550294993L;

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        UniqueIndex result = new UniqueIndex();
        result._name = this._name;
        result._columns = (ArrayList)this._columns.clone();
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof UniqueIndex) {
            UniqueIndex other = (UniqueIndex)obj;
            return new EqualsBuilder().append((Object)this._name, (Object)other._name).append((Object)this._columns, (Object)other._columns).isEquals();
        }
        return false;
    }

    @Override
    public boolean equalsIgnoreCase(Index other) {
        if (other instanceof UniqueIndex) {
            boolean checkName;
            UniqueIndex otherIndex = (UniqueIndex)other;
            boolean bl = checkName = this._name != null && this._name.length() > 0 && otherIndex._name != null && otherIndex._name.length() > 0;
            if ((!checkName || this._name.equalsIgnoreCase(otherIndex._name)) && this.getColumnCount() == otherIndex.getColumnCount()) {
                int idx = 0;
                while (idx < this.getColumnCount()) {
                    if (!this.getColumn(idx).equalsIgnoreCase(otherIndex.getColumn(idx))) {
                        return false;
                    }
                    ++idx;
                }
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return this._columns.hashCode();
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("Unique index [name=");
        result.append(this.getName());
        result.append("; ");
        result.append(this.getColumnCount());
        result.append(" columns]");
        return result.toString();
    }

    @Override
    public String toVerboseString() {
        StringBuffer result = new StringBuffer();
        result.append("Unique index [");
        result.append(this.getName());
        result.append("] columns:");
        int idx = 0;
        while (idx < this.getColumnCount()) {
            result.append(" ");
            result.append(this.getColumn(idx).toString());
            ++idx;
        }
        return result.toString();
    }
}


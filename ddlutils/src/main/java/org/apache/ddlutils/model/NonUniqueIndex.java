/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.builder.EqualsBuilder
 *  org.apache.commons.lang.builder.HashCodeBuilder
 */
package org.apache.ddlutils.model;

import java.util.ArrayList;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.IndexImpBase;

public class NonUniqueIndex
extends IndexImpBase {
    private static final long serialVersionUID = -3591499395114850301L;

    @Override
    public boolean isUnique() {
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        NonUniqueIndex result = new NonUniqueIndex();
        result._name = this._name;
        result._columns = (ArrayList)this._columns.clone();
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof NonUniqueIndex) {
            NonUniqueIndex other = (NonUniqueIndex)obj;
            return new EqualsBuilder().append((Object)this._name, (Object)other._name).append((Object)this._columns, (Object)other._columns).isEquals();
        }
        return false;
    }

    @Override
    public boolean equalsIgnoreCase(Index other) {
        if (other instanceof NonUniqueIndex) {
            boolean checkName;
            NonUniqueIndex otherIndex = (NonUniqueIndex)other;
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
        return new HashCodeBuilder(17, 37).append((Object)this._name).append((Object)this._columns).toHashCode();
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("Index [name=");
        result.append(this.getName());
        result.append("; ");
        result.append(this.getColumnCount());
        result.append(" columns]");
        return result.toString();
    }

    @Override
    public String toVerboseString() {
        StringBuffer result = new StringBuffer();
        result.append("Index [");
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


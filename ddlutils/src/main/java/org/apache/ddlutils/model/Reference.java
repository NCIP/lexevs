/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.builder.EqualsBuilder
 *  org.apache.commons.lang.builder.HashCodeBuilder
 */
package org.apache.ddlutils.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.ddlutils.model.Column;

public class Reference
implements Cloneable,
Serializable {
    private static final long serialVersionUID = 6062467640266171664L;
    private int _sequenceValue;
    private Column _localColumn;
    private Column _foreignColumn;
    private String _localColumnName;
    private String _foreignColumnName;

    public Reference() {
    }

    public Reference(Column localColumn, Column foreignColumn) {
        this.setLocalColumn(localColumn);
        this.setForeignColumn(foreignColumn);
    }

    public int getSequenceValue() {
        return this._sequenceValue;
    }

    public void setSequenceValue(int sequenceValue) {
        this._sequenceValue = sequenceValue;
    }

    public Column getLocalColumn() {
        return this._localColumn;
    }

    public void setLocalColumn(Column localColumn) {
        this._localColumn = localColumn;
        this._localColumnName = localColumn == null ? null : localColumn.getName();
    }

    public Column getForeignColumn() {
        return this._foreignColumn;
    }

    public void setForeignColumn(Column foreignColumn) {
        this._foreignColumn = foreignColumn;
        this._foreignColumnName = foreignColumn == null ? null : foreignColumn.getName();
    }

    public String getLocalColumnName() {
        return this._localColumnName;
    }

    public void setLocalColumnName(String localColumnName) {
        if (this._localColumn != null && !this._localColumn.getName().equals(localColumnName)) {
            this._localColumn = null;
        }
        this._localColumnName = localColumnName;
    }

    public String getForeignColumnName() {
        return this._foreignColumnName;
    }

    public void setForeignColumnName(String foreignColumnName) {
        if (this._foreignColumn != null && !this._foreignColumn.getName().equals(foreignColumnName)) {
            this._foreignColumn = null;
        }
        this._foreignColumnName = foreignColumnName;
    }

    public Object clone() throws CloneNotSupportedException {
        Reference result = (Reference)super.clone();
        result._localColumnName = this._localColumnName;
        result._foreignColumnName = this._foreignColumnName;
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Reference) {
            Reference other = (Reference)obj;
            return new EqualsBuilder().append((Object)this._localColumnName, (Object)other._localColumnName).append((Object)this._foreignColumnName, (Object)other._foreignColumnName).isEquals();
        }
        return false;
    }

    public boolean equalsIgnoreCase(Reference otherRef) {
        return otherRef != null && this._localColumnName.equalsIgnoreCase(otherRef._localColumnName) && this._foreignColumnName.equalsIgnoreCase(otherRef._foreignColumnName);
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).append((Object)this._localColumnName).append((Object)this._foreignColumnName).toHashCode();
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(this.getLocalColumnName());
        result.append(" -> ");
        result.append(this.getForeignColumnName());
        return result.toString();
    }
}


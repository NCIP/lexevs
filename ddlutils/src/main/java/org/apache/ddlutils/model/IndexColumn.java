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

public class IndexColumn
implements Cloneable,
Serializable {
    private static final long serialVersionUID = -5009366896427504739L;
    private int _ordinalPosition;
    private Column _column;
    protected String _name;
    protected String _size;

    public IndexColumn() {
    }

    public IndexColumn(Column column) {
        this._column = column;
        this._name = column.getName();
    }

    public IndexColumn(String columnName) {
        this._name = columnName;
    }

    public int getOrdinalPosition() {
        return this._ordinalPosition;
    }

    public void setOrdinalPosition(int position) {
        this._ordinalPosition = position;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public Column getColumn() {
        return this._column;
    }

    public void setColumn(Column column) {
        this._column = column;
        this._name = column == null ? null : column.getName();
    }

    public String getSize() {
        return this._size;
    }

    public void setSize(String size) {
        this._size = size;
    }

    public Object clone() throws CloneNotSupportedException {
        IndexColumn result = (IndexColumn)super.clone();
        result._name = this._name;
        result._size = this._size;
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IndexColumn) {
            IndexColumn other = (IndexColumn)obj;
            return new EqualsBuilder().append((Object)this._name, (Object)other._name).append((Object)this._size, (Object)other._size).isEquals();
        }
        return false;
    }

    public boolean equalsIgnoreCase(IndexColumn other) {
        return new EqualsBuilder().append((Object)this._name.toUpperCase(), (Object)other._name.toUpperCase()).append((Object)this._size, (Object)other._size).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).append((Object)this._name).append((Object)this._size).toHashCode();
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("Index column [name=");
        result.append(this.getName());
        result.append("; size=");
        result.append(this.getSize());
        result.append("]");
        return result.toString();
    }
}


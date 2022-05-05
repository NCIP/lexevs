/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.model;

import java.io.Serializable;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.IndexColumn;

public interface Index
extends Cloneable,
Serializable {
    public boolean isUnique();

    public String getName();

    public void setName(String var1);

    public int getColumnCount();

    public IndexColumn getColumn(int var1);

    public IndexColumn[] getColumns();

    public boolean hasColumn(Column var1);

    public void addColumn(IndexColumn var1);

    public void removeColumn(IndexColumn var1);

    public void removeColumn(int var1);

    public Object clone() throws CloneNotSupportedException;

    public boolean equalsIgnoreCase(Index var1);

    public String toVerboseString();
}


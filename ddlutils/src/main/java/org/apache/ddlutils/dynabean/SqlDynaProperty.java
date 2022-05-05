/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.DynaProperty
 */
package org.apache.ddlutils.dynabean;

import org.apache.commons.beanutils.DynaProperty;
import org.apache.ddlutils.model.Column;

public class SqlDynaProperty
extends DynaProperty {
    private static final long serialVersionUID = -4491018827449106588L;
    private Column _column;

    public SqlDynaProperty(Column column) {
        super(column.getName());
        this._column = column;
    }

    public SqlDynaProperty(Column column, Class type) {
        super(column.getName(), type);
        this._column = column;
    }

    public Column getColumn() {
        return this._column;
    }

    public boolean isPrimaryKey() {
        return this.getColumn().isPrimaryKey();
    }
}


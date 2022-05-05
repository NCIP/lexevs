/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.alteration;

import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.alteration.ModelChange;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

public class AddTableChange
implements ModelChange {
    private Table _newTable;

    public AddTableChange(Table newTable) {
        this._newTable = newTable;
    }

    public Table getNewTable() {
        return this._newTable;
    }

    @Override
    public void apply(Database database, boolean caseSensitive) {
        try {
            database.addTable((Table)this._newTable.clone());
        }
        catch (CloneNotSupportedException ex) {
            throw new DdlUtilsException(ex);
        }
    }
}


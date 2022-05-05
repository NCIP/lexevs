/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.alteration;

import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.alteration.TableChangeImplBase;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;

public class AddIndexChange
extends TableChangeImplBase {
    private Index _newIndex;

    public AddIndexChange(Table table, Index newIndex) {
        super(table);
        this._newIndex = newIndex;
    }

    public Index getNewIndex() {
        return this._newIndex;
    }

    @Override
    public void apply(Database database, boolean caseSensitive) {
        Index newIndex = null;
        try {
            newIndex = (Index)this._newIndex.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new DdlUtilsException(ex);
        }
        database.findTable(this.getChangedTable().getName(), caseSensitive).addIndex(newIndex);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.alteration;

import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.alteration.TableChangeImplBase;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Table;

public class AddForeignKeyChange
extends TableChangeImplBase {
    private ForeignKey _newForeignKey;

    public AddForeignKeyChange(Table table, ForeignKey newForeignKey) {
        super(table);
        this._newForeignKey = newForeignKey;
    }

    public ForeignKey getNewForeignKey() {
        return this._newForeignKey;
    }

    @Override
    public void apply(Database database, boolean caseSensitive) {
        ForeignKey newFK = null;
        try {
            newFK = (ForeignKey)this._newForeignKey.clone();
            newFK.setForeignTable(database.findTable(this._newForeignKey.getForeignTableName(), caseSensitive));
        }
        catch (CloneNotSupportedException ex) {
            throw new DdlUtilsException(ex);
        }
        database.findTable(this.getChangedTable().getName()).addForeignKey(newFK);
    }
}


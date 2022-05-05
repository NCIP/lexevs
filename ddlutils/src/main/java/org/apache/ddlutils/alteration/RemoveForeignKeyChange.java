/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.alteration;

import org.apache.ddlutils.alteration.TableChangeImplBase;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Table;

public class RemoveForeignKeyChange
extends TableChangeImplBase {
    private ForeignKey _foreignKey;

    public RemoveForeignKeyChange(Table table, ForeignKey foreignKey) {
        super(table);
        this._foreignKey = foreignKey;
    }

    public ForeignKey getForeignKey() {
        return this._foreignKey;
    }

    @Override
    public void apply(Database database, boolean caseSensitive) {
        Table table = database.findTable(this.getChangedTable().getName(), caseSensitive);
        table.removeForeignKey(this._foreignKey);
    }
}


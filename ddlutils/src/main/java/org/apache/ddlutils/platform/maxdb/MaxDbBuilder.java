/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.maxdb;

import java.io.IOException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.sapdb.SapDbBuilder;

public class MaxDbBuilder
extends SapDbBuilder {
    public MaxDbBuilder(Platform platform) {
        super(platform);
    }

    @Override
    protected void writeExternalPrimaryKeysCreateStmt(Table table, Column[] primaryKeyColumns) throws IOException {
        if (primaryKeyColumns.length > 0 && this.shouldGeneratePrimaryKeys(primaryKeyColumns)) {
            this.print("ALTER TABLE ");
            this.printlnIdentifier(this.getTableName(table));
            this.printIndent();
            this.print("ADD CONSTRAINT ");
            this.printIdentifier(this.getConstraintName(null, table, "PK", null));
            this.print(" ");
            this.writePrimaryKeyStmt(table, primaryKeyColumns);
            this.printEndOfStatement();
        }
    }

    @Override
    protected void writeExternalForeignKeyCreateStmt(Database database, Table table, ForeignKey key) throws IOException {
        if (key.getForeignTableName() == null) {
            this._log.warn((Object)("Foreign key table is null for key " + key));
        } else {
            this.writeTableAlterStmt(table);
            this.print("ADD CONSTRAINT ");
            this.printIdentifier(this.getForeignKeyName(table, key));
            this.print(" FOREIGN KEY (");
            this.writeLocalReferences(key);
            this.print(") REFERENCES ");
            this.printIdentifier(this.getTableName(database.findTable(key.getForeignTableName())));
            this.print(" (");
            this.writeForeignReferences(key);
            this.print(")");
            this.printEndOfStatement();
        }
    }

    @Override
    protected void writeExternalForeignKeyDropStmt(Table table, ForeignKey foreignKey) throws IOException {
        this.writeTableAlterStmt(table);
        this.print("DROP CONSTRAINT ");
        this.printIdentifier(this.getForeignKeyName(table, foreignKey));
        this.printEndOfStatement();
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.sapdb;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.alteration.AddColumnChange;
import org.apache.ddlutils.alteration.AddPrimaryKeyChange;
import org.apache.ddlutils.alteration.ColumnDefaultValueChange;
import org.apache.ddlutils.alteration.ColumnRequiredChange;
import org.apache.ddlutils.alteration.PrimaryKeyChange;
import org.apache.ddlutils.alteration.RemoveColumnChange;
import org.apache.ddlutils.alteration.RemovePrimaryKeyChange;
import org.apache.ddlutils.alteration.TableChange;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.SqlBuilder;

public class SapDbBuilder
extends SqlBuilder {
    public SapDbBuilder(Platform platform) {
        super(platform);
        this.addEscapedCharSequence("'", "''");
    }

    @Override
    public void dropTable(Table table) throws IOException {
        this.print("DROP TABLE ");
        this.printIdentifier(this.getTableName(table));
        this.print(" CASCADE");
        this.printEndOfStatement();
    }

    @Override
    protected void writeColumnAutoIncrementStmt(Table table, Column column) throws IOException {
        this.print("DEFAULT SERIAL(1)");
    }

    @Override
    protected void writeExternalPrimaryKeysCreateStmt(Table table, Column[] primaryKeyColumns) throws IOException {
        if (primaryKeyColumns.length > 0 && this.shouldGeneratePrimaryKeys(primaryKeyColumns)) {
            this.print("ALTER TABLE ");
            this.printlnIdentifier(this.getTableName(table));
            this.printIndent();
            this.print("ADD ");
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
            this.print(" ADD FOREIGN KEY ");
            this.printIdentifier(this.getForeignKeyName(table, key));
            this.print(" (");
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
        this.print("DROP FOREIGN KEY ");
        this.printIdentifier(this.getForeignKeyName(table, foreignKey));
        this.printEndOfStatement();
    }

    @Override
    public String getSelectLastIdentityValues(Table table) {
        StringBuffer result = new StringBuffer();
        result.append("SELECT ");
        result.append(this.getDelimitedIdentifier(this.getTableName(table)));
        result.append(".CURRVAL FROM DUAL");
        return result.toString();
    }

    @Override
    protected void processTableStructureChanges(Database currentModel, Database desiredModel, Table sourceTable, Table targetTable, Map parameters, List changes) throws IOException {
        PrimaryKeyChange pkChange;

        for (Object change2 : changes) {
            AddColumnChange addColumnChange;
            if (!(change2 instanceof AddColumnChange) || (addColumnChange = (AddColumnChange)change2).isAtEnd()) continue;
            return;
        }
        TableChange change2;
        Iterator changeIt = changes.iterator();
        while (changeIt.hasNext()) {
            change2 = (TableChange)changeIt.next();
            if (change2 instanceof RemovePrimaryKeyChange) {
                this.processChange(currentModel, desiredModel, (RemovePrimaryKeyChange)change2);
                changeIt.remove();
                continue;
            }
            if (!(change2 instanceof PrimaryKeyChange)) continue;
            pkChange = (PrimaryKeyChange)change2;
            RemovePrimaryKeyChange removePkChange = new RemovePrimaryKeyChange(pkChange.getChangedTable(), pkChange.getOldPrimaryKeyColumns());
            this.processChange(currentModel, desiredModel, removePkChange);
        }
        changeIt = changes.iterator();
        while (changeIt.hasNext()) {
            change2 = (TableChange)changeIt.next();
            if (change2 instanceof AddColumnChange) {
                this.processChange(currentModel, desiredModel, (AddColumnChange)change2);
                changeIt.remove();
                continue;
            }
            if (change2 instanceof ColumnDefaultValueChange) {
                this.processChange(currentModel, desiredModel, (ColumnDefaultValueChange)change2);
                changeIt.remove();
                continue;
            }
            if (change2 instanceof ColumnRequiredChange) {
                this.processChange(currentModel, desiredModel, (ColumnRequiredChange)change2);
                changeIt.remove();
                continue;
            }
            if (!(change2 instanceof RemoveColumnChange)) continue;
            this.processChange(currentModel, desiredModel, (RemoveColumnChange)change2);
            changeIt.remove();
        }
        changeIt = changes.iterator();
        while (changeIt.hasNext()) {
            change2 = (TableChange)changeIt.next();
            if (change2 instanceof AddPrimaryKeyChange) {
                this.processChange(currentModel, desiredModel, (AddPrimaryKeyChange)change2);
                changeIt.remove();
                continue;
            }
            if (!(change2 instanceof PrimaryKeyChange)) continue;
            pkChange = (PrimaryKeyChange)change2;
            AddPrimaryKeyChange addPkChange = new AddPrimaryKeyChange(pkChange.getChangedTable(), pkChange.getNewPrimaryKeyColumns());
            this.processChange(currentModel, desiredModel, addPkChange);
            changeIt.remove();
        }
    }

    protected void processChange(Database currentModel, Database desiredModel, AddColumnChange change) throws IOException {
        this.print("ALTER TABLE ");
        this.printlnIdentifier(this.getTableName(change.getChangedTable()));
        this.printIndent();
        this.print("ADD ");
        this.writeColumn(change.getChangedTable(), change.getNewColumn());
        this.printEndOfStatement();
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processChange(Database currentModel, Database desiredModel, RemoveColumnChange change) throws IOException {
        this.print("ALTER TABLE ");
        this.printlnIdentifier(this.getTableName(change.getChangedTable()));
        this.printIndent();
        this.print("DROP ");
        this.printIdentifier(this.getColumnName(change.getColumn()));
        this.print(" RELEASE SPACE");
        this.printEndOfStatement();
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processChange(Database currentModel, Database desiredModel, RemovePrimaryKeyChange change) throws IOException {
        this.print("ALTER TABLE ");
        this.printlnIdentifier(this.getTableName(change.getChangedTable()));
        this.printIndent();
        this.print("DROP PRIMARY KEY");
        this.printEndOfStatement();
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processChange(Database currentModel, Database desiredModel, ColumnRequiredChange change) throws IOException {
        this.print("ALTER TABLE ");
        this.printlnIdentifier(this.getTableName(change.getChangedTable()));
        this.printIndent();
        this.print("COLUMN ");
        this.printIdentifier(this.getColumnName(change.getChangedColumn()));
        if (change.getChangedColumn().isRequired()) {
            this.print(" DEFAULT NULL");
        } else {
            this.print(" NOT NULL");
        }
        this.printEndOfStatement();
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processChange(Database currentModel, Database desiredModel, ColumnDefaultValueChange change) throws IOException {
        boolean hasDefault;
        this.print("ALTER TABLE ");
        this.printlnIdentifier(this.getTableName(change.getChangedTable()));
        this.printIndent();
        this.print("COLUMN ");
        this.printIdentifier(this.getColumnName(change.getChangedColumn()));
        Table curTable = currentModel.findTable(change.getChangedTable().getName(), this.getPlatform().isDelimitedIdentifierModeOn());
        Column curColumn = curTable.findColumn(change.getChangedColumn().getName(), this.getPlatform().isDelimitedIdentifierModeOn());
        boolean bl = hasDefault = curColumn.getParsedDefaultValue() != null;
        if (this.isValidDefaultValue(change.getNewDefaultValue(), curColumn.getTypeCode())) {
            if (hasDefault) {
                this.print(" ALTER DEFAULT ");
            } else {
                this.print(" ADD DEFAULT ");
            }
            this.printDefaultValue(change.getNewDefaultValue(), curColumn.getTypeCode());
        } else if (hasDefault) {
            this.print(" DROP DEFAULT");
        }
        this.printEndOfStatement();
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }
}


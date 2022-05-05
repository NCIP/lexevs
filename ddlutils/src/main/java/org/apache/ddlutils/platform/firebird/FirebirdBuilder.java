/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.firebird;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.alteration.AddColumnChange;
import org.apache.ddlutils.alteration.AddPrimaryKeyChange;
import org.apache.ddlutils.alteration.RemoveColumnChange;
import org.apache.ddlutils.alteration.TableChange;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.util.Jdbc3Utils;

public class FirebirdBuilder
extends SqlBuilder {
    public FirebirdBuilder(Platform platform) {
        super(platform);
        this.addEscapedCharSequence("'", "''");
    }

    @Override
    public void createTable(Database database, Table table, Map parameters) throws IOException {
        super.createTable(database, table, parameters);
        Column[] columns = table.getAutoIncrementColumns();
        int idx = 0;
        while (idx < columns.length) {
            this.writeAutoIncrementCreateStmts(database, table, columns[idx]);
            ++idx;
        }
    }

    @Override
    public void dropTable(Table table) throws IOException {
        Column[] columns = table.getAutoIncrementColumns();
        int idx = 0;
        while (idx < columns.length) {
            this.writeAutoIncrementDropStmts(table, columns[idx]);
            ++idx;
        }
        super.dropTable(table);
    }

    private void writeAutoIncrementCreateStmts(Database database, Table table, Column column) throws IOException {
        this.print("CREATE GENERATOR ");
        this.printIdentifier(this.getGeneratorName(table, column));
        this.printEndOfStatement();
        this.print("CREATE TRIGGER ");
        this.printIdentifier(this.getConstraintName("trg", table, column.getName(), null));
        this.print(" FOR ");
        this.printlnIdentifier(this.getTableName(table));
        this.println("ACTIVE BEFORE INSERT POSITION 0 AS");
        this.print("BEGIN IF (NEW.");
        this.printIdentifier(this.getColumnName(column));
        this.print(" IS NULL) THEN NEW.");
        this.printIdentifier(this.getColumnName(column));
        this.print(" = GEN_ID(");
        this.printIdentifier(this.getGeneratorName(table, column));
        this.print(", 1); END");
        this.printEndOfStatement();
    }

    private void writeAutoIncrementDropStmts(Table table, Column column) throws IOException {
        this.print("DROP TRIGGER ");
        this.printIdentifier(this.getConstraintName("trg", table, column.getName(), null));
        this.printEndOfStatement();
        this.print("DROP GENERATOR ");
        this.printIdentifier(this.getGeneratorName(table, column));
        this.printEndOfStatement();
    }

    protected String getGeneratorName(Table table, Column column) {
        return this.getConstraintName("gen", table, column.getName(), null);
    }

    @Override
    protected void writeColumnAutoIncrementStmt(Table table, Column column) throws IOException {
    }

    @Override
    public String getSelectLastIdentityValues(Table table) {
        Column[] columns = table.getAutoIncrementColumns();
        if (columns.length == 0) {
            return null;
        }
        StringBuffer result = new StringBuffer();
        result.append("SELECT ");
        int idx = 0;
        while (idx < columns.length) {
            result.append("GEN_ID(");
            result.append(this.getDelimitedIdentifier(this.getGeneratorName(table, columns[idx])));
            result.append(", 0)");
            ++idx;
        }
        result.append(" FROM RDB$DATABASE");
        return result.toString();
    }

    @Override
    protected String getNativeDefaultValue(Column column) {
        if (column.getTypeCode() == -7 || Jdbc3Utils.supportsJava14JdbcTypes() && column.getTypeCode() == Jdbc3Utils.determineBooleanTypeCode()) {
            return this.getDefaultValueHelper().convert(column.getDefaultValue(), column.getTypeCode(), 5).toString();
        }
        return super.getNativeDefaultValue(column);
    }

    @Override
    public void createExternalForeignKeys(Database database) throws IOException {
        int idx = 0;
        while (idx < database.getTableCount()) {
            this.createExternalForeignKeys(database, database.getTable(idx));
            ++idx;
        }
    }

    @Override
    public void writeExternalIndexDropStmt(Table table, Index index) throws IOException {
        this.print("DROP INDEX ");
        this.printIdentifier(this.getIndexName(index));
        this.printEndOfStatement();
    }

    @Override
    protected void processTableStructureChanges(Database currentModel, Database desiredModel, Table sourceTable, Table targetTable, Map parameters, List changes) throws IOException {
        TableChange change;
        boolean pkColumnAdded = false;
        Iterator changeIt = changes.iterator();
        while (changeIt.hasNext()) {
            RemoveColumnChange removeColumnChange;
            change = (TableChange)changeIt.next();
            if (change instanceof AddColumnChange) {
                AddColumnChange addColumnChange = (AddColumnChange)change;
                if (addColumnChange.getNewColumn().isPrimaryKey()) {
                    pkColumnAdded = true;
                    continue;
                }
                this.processChange(currentModel, desiredModel, addColumnChange);
                changeIt.remove();
                continue;
            }
            if (!(change instanceof RemoveColumnChange) || (removeColumnChange = (RemoveColumnChange)change).getColumn().isPrimaryKey()) continue;
            this.processChange(currentModel, desiredModel, removeColumnChange);
            changeIt.remove();
        }
        changeIt = changes.iterator();
        while (changeIt.hasNext()) {
            change = (TableChange)changeIt.next();
            if (!(change instanceof AddPrimaryKeyChange) || pkColumnAdded) continue;
            this.processChange(currentModel, desiredModel, (AddPrimaryKeyChange)change);
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
        Table curTable = currentModel.findTable(change.getChangedTable().getName(), this.getPlatform().isDelimitedIdentifierModeOn());
        if (!change.isAtEnd()) {
            Column prevColumn = change.getPreviousColumn();
            if (prevColumn != null) {
                prevColumn = curTable.findColumn(prevColumn.getName(), this.getPlatform().isDelimitedIdentifierModeOn());
            }
            this.print("ALTER TABLE ");
            this.printlnIdentifier(this.getTableName(change.getChangedTable()));
            this.printIndent();
            this.print("ALTER ");
            this.printIdentifier(this.getColumnName(change.getNewColumn()));
            this.print(" POSITION ");
            this.print(prevColumn == null ? "1" : String.valueOf(curTable.getColumnIndex(prevColumn) + 2));
            this.printEndOfStatement();
        }
        if (change.getNewColumn().isAutoIncrement()) {
            this.writeAutoIncrementCreateStmts(currentModel, curTable, change.getNewColumn());
        }
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processChange(Database currentModel, Database desiredModel, RemoveColumnChange change) throws IOException {
        if (change.getColumn().isAutoIncrement()) {
            this.writeAutoIncrementDropStmts(change.getChangedTable(), change.getColumn());
        }
        this.print("ALTER TABLE ");
        this.printlnIdentifier(this.getTableName(change.getChangedTable()));
        this.printIndent();
        this.print("DROP ");
        this.printIdentifier(this.getColumnName(change.getColumn()));
        this.printEndOfStatement();
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }
}


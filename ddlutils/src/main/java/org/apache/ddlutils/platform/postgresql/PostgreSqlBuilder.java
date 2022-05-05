/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.postgresql;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.alteration.AddColumnChange;
import org.apache.ddlutils.alteration.RemoveColumnChange;
import org.apache.ddlutils.alteration.TableChange;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.SqlBuilder;

public class PostgreSqlBuilder
extends SqlBuilder {
    public PostgreSqlBuilder(Platform platform) {
        super(platform);
        this.addEscapedCharSequence("\\", "\\\\");
        this.addEscapedCharSequence("'", "\\'");
        this.addEscapedCharSequence("\b", "\\b");
        this.addEscapedCharSequence("\f", "\\f");
        this.addEscapedCharSequence("\n", "\\n");
        this.addEscapedCharSequence("\r", "\\r");
        this.addEscapedCharSequence("\t", "\\t");
    }

    @Override
    public void dropTable(Table table) throws IOException {
        this.print("DROP TABLE ");
        this.printIdentifier(this.getTableName(table));
        this.print(" CASCADE");
        this.printEndOfStatement();
        Column[] columns = table.getAutoIncrementColumns();
        int idx = 0;
        while (idx < columns.length) {
            this.dropAutoIncrementSequence(table, columns[idx]);
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
    public void createTable(Database database, Table table, Map parameters) throws IOException {
        int idx = 0;
        while (idx < table.getColumnCount()) {
            Column column = table.getColumn(idx);
            if (column.isAutoIncrement()) {
                this.createAutoIncrementSequence(table, column);
            }
            ++idx;
        }
        super.createTable(database, table, parameters);
    }

    private void createAutoIncrementSequence(Table table, Column column) throws IOException {
        this.print("CREATE SEQUENCE ");
        this.printIdentifier(this.getConstraintName(null, table, column.getName(), "seq"));
        this.printEndOfStatement();
    }

    private void dropAutoIncrementSequence(Table table, Column column) throws IOException {
        this.print("DROP SEQUENCE ");
        this.printIdentifier(this.getConstraintName(null, table, column.getName(), "seq"));
        this.printEndOfStatement();
    }

    @Override
    protected void writeColumnAutoIncrementStmt(Table table, Column column) throws IOException {
        this.print("UNIQUE DEFAULT nextval('");
        this.print(this.getConstraintName(null, table, column.getName(), "seq"));
        this.print("')");
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
            if (idx > 0) {
                result.append(", ");
            }
            result.append("currval('");
            result.append(this.getConstraintName(null, table, columns[idx].getName(), "seq"));
            result.append("') AS ");
            result.append(this.getDelimitedIdentifier(columns[idx].getName()));
            ++idx;
        }
        return result.toString();
    }

    @Override
    protected void processTableStructureChanges(Database currentModel, Database desiredModel, Table sourceTable, Table targetTable, Map parameters, List changes) throws IOException {
        Iterator changeIt = changes.iterator();
        while (changeIt.hasNext()) {
            TableChange change = (TableChange)changeIt.next();
            if (change instanceof AddColumnChange) {
                AddColumnChange addColumnChange = (AddColumnChange)change;
                if (addColumnChange.getNewColumn().isRequired() || addColumnChange.getNewColumn().getDefaultValue() != null || addColumnChange.getNextColumn() != null) continue;
                this.processChange(currentModel, desiredModel, addColumnChange);
                changeIt.remove();
                continue;
            }
            if (!(change instanceof RemoveColumnChange)) continue;
            this.processChange(currentModel, desiredModel, (RemoveColumnChange)change);
            changeIt.remove();
        }
        super.processTableStructureChanges(currentModel, desiredModel, sourceTable, targetTable, parameters, changes);
    }

    protected void processChange(Database currentModel, Database desiredModel, AddColumnChange change) throws IOException {
        this.print("ALTER TABLE ");
        this.printlnIdentifier(this.getTableName(change.getChangedTable()));
        this.printIndent();
        this.print("ADD COLUMN ");
        this.writeColumn(change.getChangedTable(), change.getNewColumn());
        this.printEndOfStatement();
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processChange(Database currentModel, Database desiredModel, RemoveColumnChange change) throws IOException {
        this.print("ALTER TABLE ");
        this.printlnIdentifier(this.getTableName(change.getChangedTable()));
        this.printIndent();
        this.print("DROP COLUMN ");
        this.printIdentifier(this.getColumnName(change.getColumn()));
        this.printEndOfStatement();
        if (change.getColumn().isAutoIncrement()) {
            this.dropAutoIncrementSequence(change.getChangedTable(), change.getColumn());
        }
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }
}


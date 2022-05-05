/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.collections.set.ListOrderedSet
 *  org.apache.commons.lang.StringUtils
 */
package org.apache.ddlutils.platform.mysql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.commons.lang.StringUtils;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.alteration.AddColumnChange;
import org.apache.ddlutils.alteration.AddPrimaryKeyChange;
import org.apache.ddlutils.alteration.ColumnChange;
import org.apache.ddlutils.alteration.PrimaryKeyChange;
import org.apache.ddlutils.alteration.RemoveColumnChange;
import org.apache.ddlutils.alteration.RemovePrimaryKeyChange;
import org.apache.ddlutils.alteration.TableChange;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.IndexColumn;
import org.apache.ddlutils.model.ModelException;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.SqlBuilder;
//import org.checkerframework.common.aliasing.qual.MaybeAliased;

public class MySqlBuilder
extends SqlBuilder {
    public MySqlBuilder(Platform platform) {
        super(platform);
        this.addEscapedCharSequence("\\", "\\\\");
        this.addEscapedCharSequence("\u0000", "\\0");
        this.addEscapedCharSequence("'", "\\'");
        this.addEscapedCharSequence("\"", "\\\"");
        this.addEscapedCharSequence("\b", "\\b");
        this.addEscapedCharSequence("\n", "\\n");
        this.addEscapedCharSequence("\r", "\\r");
        this.addEscapedCharSequence("\t", "\\t");
        this.addEscapedCharSequence("\u001a", "\\Z");
        this.addEscapedCharSequence("%", "\\%");
        this.addEscapedCharSequence("_", "\\_");
    }

    @Override
    public void dropTable(Table table) throws IOException {
        this.print("DROP TABLE IF EXISTS ");
        this.printIdentifier(this.getTableName(table));
        this.printEndOfStatement();
    }

    @Override
    protected void writeColumnAutoIncrementStmt(Table table, Column column) throws IOException {
        this.print("AUTO_INCREMENT");
    }

    @Override
    protected boolean shouldGeneratePrimaryKeys(Column[] primaryKeyColumns) {
        return true;
    }

    @Override
    public String getSelectLastIdentityValues(Table table) {
        String autoIncrementKeyName = "";
        if (table.getAutoIncrementColumns().length > 0) {
            autoIncrementKeyName = table.getAutoIncrementColumns()[0].getName();
        }
        return "SELECT LAST_INSERT_ID() " + autoIncrementKeyName;
    }

    @Override
    protected void writeTableCreationStmtEnding(Table table, Map parameters) throws IOException {
        if (parameters != null) {
            this.print(" ");
            Iterator it = parameters.entrySet().iterator();
            while (it.hasNext()) {
                Object entryObject = it.next();
                if(entryObject instanceof Map.Entry){
                    Map.Entry entry = (Map.Entry)entryObject;
                this.print(entry.getKey().toString());
                if (entry.getValue() != null) {
                    this.print("=");
                    this.print(entry.getValue().toString());
                }
                if (!it.hasNext()) continue;
                this.print(" ");
            }}
        }
        super.writeTableCreationStmtEnding(table, parameters);
    }

    @Override
    protected void writeExternalForeignKeyDropStmt(Table table, ForeignKey foreignKey) throws IOException {
        if (foreignKey.isAutoIndexPresent()) {
            this.writeTableAlterStmt(table);
            this.print("DROP INDEX ");
            this.printIdentifier(this.getForeignKeyName(table, foreignKey));
            this.printEndOfStatement();
        }
    }

    @Override
    protected void processTableStructureChanges(Database currentModel, Database desiredModel, Table sourceTable, Table targetTable, Map parameters, List changes) throws IOException {
        ArrayList<AddColumnChange> addColumnChanges = new ArrayList<AddColumnChange>();
        Iterator changeIt = changes.iterator();
        while (changeIt.hasNext()) {
            TableChange change = (TableChange)changeIt.next();
            if (!(change instanceof AddColumnChange)) continue;
            addColumnChanges.add((AddColumnChange)change);
            changeIt.remove();
        }
        changeIt = addColumnChanges.iterator();
        while (changeIt.hasNext()) {
            AddColumnChange addColumnChange = (AddColumnChange)changeIt.next();
            this.processChange(currentModel, desiredModel, addColumnChange);
            changeIt.remove();
        }
        ListOrderedSet changedColumns = new ListOrderedSet();
        Iterator changeIt2 = changes.iterator();
        while (changeIt2.hasNext()) {
            TableChange change = (TableChange)changeIt2.next();
            if (change instanceof RemoveColumnChange) {
                this.processChange(currentModel, desiredModel, (RemoveColumnChange)change);
                changeIt2.remove();
                continue;
            }
            if (change instanceof AddPrimaryKeyChange) {
                this.processChange(currentModel, desiredModel, (AddPrimaryKeyChange)change);
                changeIt2.remove();
                continue;
            }
            if (change instanceof PrimaryKeyChange) {
                this.processChange(currentModel, desiredModel, (PrimaryKeyChange)change);
                changeIt2.remove();
                continue;
            }
            if (change instanceof RemovePrimaryKeyChange) {
                this.processChange(currentModel, desiredModel, (RemovePrimaryKeyChange)change);
                changeIt2.remove();
                continue;
            }
            if (!(change instanceof ColumnChange)) continue;
            changedColumns.add((Object)((ColumnChange)change).getChangedColumn());
            changeIt2.remove();
        }
        for (Object sourceColumnObject : changedColumns) {
            if(sourceColumnObject instanceof Column){
                Column sourceColumn = (Column)sourceColumnObject;
            Column targetColumn = targetTable.findColumn(sourceColumn.getName(), this.getPlatform().isDelimitedIdentifierModeOn());
            this.processColumnChange(sourceTable, targetTable, sourceColumn, targetColumn);
        }}
    }

    protected void processChange(Database currentModel, Database desiredModel, AddColumnChange change) throws IOException {
        this.print("ALTER TABLE ");
        this.printlnIdentifier(this.getTableName(change.getChangedTable()));
        this.printIndent();
        this.print("ADD COLUMN ");
        this.writeColumn(change.getChangedTable(), change.getNewColumn());
        if (change.getPreviousColumn() != null) {
            this.print(" AFTER ");
            this.printIdentifier(this.getColumnName(change.getPreviousColumn()));
        } else {
            this.print(" FIRST");
        }
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

    protected void processChange(Database currentModel, Database desiredModel, PrimaryKeyChange change) throws IOException {
        this.print("ALTER TABLE ");
        this.printlnIdentifier(this.getTableName(change.getChangedTable()));
        this.printIndent();
        this.print("DROP PRIMARY KEY");
        this.printEndOfStatement();
        this.writeExternalPrimaryKeysCreateStmt(change.getChangedTable(), change.getNewPrimaryKeyColumns());
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processColumnChange(Table sourceTable, Table targetTable, Column sourceColumn, Column targetColumn) throws IOException {
        this.print("ALTER TABLE ");
        this.printlnIdentifier(this.getTableName(sourceTable));
        this.printIndent();
        this.print("MODIFY COLUMN ");
        this.writeColumn(targetTable, targetColumn);
        this.printEndOfStatement();
    }

    @Override
    protected void writeExternalIndexCreateStmt(Table table, Index index) throws IOException {
        if (this.getPlatformInfo().isIndicesSupported()) {
            if (index.getName() == null) {
                this._log.warn((Object)("Cannot write unnamed index " + index));
            } else {
                this.print("CREATE");
                if (index.isUnique()) {
                    this.print(" UNIQUE");
                }
                this.print(" INDEX ");
                this.printIdentifier(this.getIndexName(index));
                this.print(" ON ");
                this.printIdentifier(this.getTableName(table));
                this.print(" (");
                int idx = 0;
                while (idx < index.getColumnCount()) {
                    IndexColumn idxColumn = index.getColumn(idx);
                    Column col = table.findColumn(idxColumn.getName());
                    if (col == null) {
                        throw new ModelException("Invalid column '" + idxColumn.getName() + "' on index " + index.getName() + " for table " + table.getName());
                    }
                    if (idx > 0) {
                        this.print(", ");
                    }
                    this.printIdentifier(this.getColumnName(col));
                    String indexSize = idxColumn.getSize();
                    if (StringUtils.isNotBlank((String)indexSize)) {
                        this.print("(" + indexSize + ")");
                    }
                    ++idx;
                }
                this.print(")");
                this.printEndOfStatement();
            }
        }
    }

    @Override
    protected void writeEmbeddedIndexCreateStmt(Table table, Index index) throws IOException {
        if (index.getName() != null && index.getName().length() > 0) {
            this.print(" CONSTRAINT ");
            this.printIdentifier(this.getIndexName(index));
        }
        if (index.isUnique()) {
            this.print(" UNIQUE");
        } else {
            this.print(" INDEX ");
        }
        this.print(" (");
        int idx = 0;
        while (idx < index.getColumnCount()) {
            IndexColumn idxColumn = index.getColumn(idx);
            Column col = table.findColumn(idxColumn.getName());
            if (col == null) {
                throw new ModelException("Invalid column '" + idxColumn.getName() + "' on index " + index.getName() + " for table " + table.getName());
            }
            if (idx > 0) {
                this.print(", ");
            }
            this.printIdentifier(this.getColumnName(col));
            String indexSize = idxColumn.getSize();
            if (StringUtils.isNotBlank((String)indexSize)) {
                this.print("(" + indexSize + ")");
            }
            ++idx;
        }
        this.print(")");
    }
}


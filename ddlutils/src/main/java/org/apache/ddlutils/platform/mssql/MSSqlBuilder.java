/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.mssql;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.alteration.AddColumnChange;
import org.apache.ddlutils.alteration.AddForeignKeyChange;
import org.apache.ddlutils.alteration.AddIndexChange;
import org.apache.ddlutils.alteration.AddPrimaryKeyChange;
import org.apache.ddlutils.alteration.ColumnAutoIncrementChange;
import org.apache.ddlutils.alteration.ColumnChange;
import org.apache.ddlutils.alteration.ColumnDataTypeChange;
import org.apache.ddlutils.alteration.ColumnSizeChange;
import org.apache.ddlutils.alteration.PrimaryKeyChange;
import org.apache.ddlutils.alteration.RemoveColumnChange;
import org.apache.ddlutils.alteration.RemoveForeignKeyChange;
import org.apache.ddlutils.alteration.RemoveIndexChange;
import org.apache.ddlutils.alteration.RemovePrimaryKeyChange;
import org.apache.ddlutils.alteration.TableChange;
import org.apache.ddlutils.alteration.TableChangeImplBase;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.CreationParameters;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.util.Jdbc3Utils;

public class MSSqlBuilder
extends SqlBuilder {
    private DateFormat _genericDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat _genericTimeFormat = new SimpleDateFormat("HH:mm:ss");

    public MSSqlBuilder(Platform platform) {
        super(platform);
        this.addEscapedCharSequence("'", "''");
    }

    @Override
    public void createTable(Database database, Table table, Map parameters) throws IOException {
        this.writeQuotationOnStatement();
        super.createTable(database, table, parameters);
    }

    @Override
    public void dropTable(Table table) throws IOException {
        String tableName = this.getTableName(table);
        String tableNameVar = "tn" + this.createUniqueIdentifier();
        String constraintNameVar = "cn" + this.createUniqueIdentifier();
        this.writeQuotationOnStatement();
        this.print("IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = ");
        this.printAlwaysSingleQuotedIdentifier(tableName);
        this.println(")");
        this.println("BEGIN");
        this.println("  DECLARE @" + tableNameVar + " nvarchar(256), @" + constraintNameVar + " nvarchar(256)");
        this.println("  DECLARE refcursor CURSOR FOR");
        this.println("  SELECT object_name(objs.parent_obj) tablename, objs.name constraintname");
        this.println("    FROM sysobjects objs JOIN sysconstraints cons ON objs.id = cons.constid");
        this.print("    WHERE objs.xtype != 'PK' AND object_name(objs.parent_obj) = ");
        this.printAlwaysSingleQuotedIdentifier(tableName);
        this.println("  OPEN refcursor");
        this.println("  FETCH NEXT FROM refcursor INTO @" + tableNameVar + ", @" + constraintNameVar);
        this.println("  WHILE @@FETCH_STATUS = 0");
        this.println("    BEGIN");
        this.println("      EXEC ('ALTER TABLE '+@" + tableNameVar + "+' DROP CONSTRAINT '+@" + constraintNameVar + ")");
        this.println("      FETCH NEXT FROM refcursor INTO @" + tableNameVar + ", @" + constraintNameVar);
        this.println("    END");
        this.println("  CLOSE refcursor");
        this.println("  DEALLOCATE refcursor");
        this.print("  DROP TABLE ");
        this.printlnIdentifier(tableName);
        this.print("END");
        this.printEndOfStatement();
    }

    @Override
    public void dropExternalForeignKeys(Table table) throws IOException {
        this.writeQuotationOnStatement();
        super.dropExternalForeignKeys(table);
    }

    @Override
    protected DateFormat getValueDateFormat() {
        return this._genericDateFormat;
    }

    @Override
    protected DateFormat getValueTimeFormat() {
        return this._genericTimeFormat;
    }

    @Override
    protected String getValueAsString(Column column, Object value) {
        if (value == null) {
            return "NULL";
        }
        StringBuffer result = new StringBuffer();
        switch (column.getTypeCode()) {
            case 2: 
            case 3: 
            case 6: 
            case 7: 
            case 8: {
                if (!(value instanceof String) && this.getValueNumberFormat() != null) {
                    result.append(this.getValueNumberFormat().format(value));
                    break;
                }
                result.append(value.toString());
                break;
            }
            case 91: {
                result.append("CAST(");
                result.append(this.getPlatformInfo().getValueQuoteToken());
                result.append(value instanceof String ? (String)value : this.getValueDateFormat().format(value));
                result.append(this.getPlatformInfo().getValueQuoteToken());
                result.append(" AS datetime)");
                break;
            }
            case 92: {
                result.append("CAST(");
                result.append(this.getPlatformInfo().getValueQuoteToken());
                result.append(value instanceof String ? (String)value : this.getValueTimeFormat().format(value));
                result.append(this.getPlatformInfo().getValueQuoteToken());
                result.append(" AS datetime)");
                break;
            }
            case 93: {
                result.append("CAST(");
                result.append(this.getPlatformInfo().getValueQuoteToken());
                result.append(value.toString());
                result.append(this.getPlatformInfo().getValueQuoteToken());
                result.append(" AS datetime)");
            }
        }
        return super.getValueAsString(column, value);
    }

    @Override
    protected String getNativeDefaultValue(Column column) {
        if (column.getTypeCode() == -7 || Jdbc3Utils.supportsJava14JdbcTypes() && column.getTypeCode() == Jdbc3Utils.determineBooleanTypeCode()) {
            return this.getDefaultValueHelper().convert(column.getDefaultValue(), column.getTypeCode(), 5).toString();
        }
        return super.getNativeDefaultValue(column);
    }

    @Override
    protected void writeColumnAutoIncrementStmt(Table table, Column column) throws IOException {
        this.print("IDENTITY (1,1) ");
    }

    @Override
    public void writeExternalIndexDropStmt(Table table, Index index) throws IOException {
        this.print("DROP INDEX ");
        this.printIdentifier(this.getTableName(table));
        this.print(".");
        this.printIdentifier(this.getIndexName(index));
        this.printEndOfStatement();
    }

    @Override
    protected void writeExternalForeignKeyDropStmt(Table table, ForeignKey foreignKey) throws IOException {
        String constraintName = this.getForeignKeyName(table, foreignKey);
        this.print("IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'F' AND name = ");
        this.printAlwaysSingleQuotedIdentifier(constraintName);
        this.println(")");
        this.printIndent();
        this.print("ALTER TABLE ");
        this.printIdentifier(this.getTableName(table));
        this.print(" DROP CONSTRAINT ");
        this.printIdentifier(constraintName);
        this.printEndOfStatement();
    }

    private String getQuotationOnStatement() {
        if (this.getPlatform().isDelimitedIdentifierModeOn()) {
            return "SET quoted_identifier on" + this.getPlatformInfo().getSqlCommandDelimiter() + "\n";
        }
        return "";
    }

    private void writeQuotationOnStatement() throws IOException {
        this.print(this.getQuotationOnStatement());
    }

    @Override
    public String getSelectLastIdentityValues(Table table) {
        return "SELECT @@IDENTITY";
    }

    protected String getEnableIdentityOverrideSql(Table table) {
        StringBuffer result = new StringBuffer();
        result.append(this.getQuotationOnStatement());
        result.append("SET IDENTITY_INSERT ");
        result.append(this.getDelimitedIdentifier(this.getTableName(table)));
        result.append(" ON");
        result.append(this.getPlatformInfo().getSqlCommandDelimiter());
        return result.toString();
    }

    protected String getDisableIdentityOverrideSql(Table table) {
        StringBuffer result = new StringBuffer();
        result.append(this.getQuotationOnStatement());
        result.append("SET IDENTITY_INSERT ");
        result.append(this.getDelimitedIdentifier(this.getTableName(table)));
        result.append(" OFF");
        result.append(this.getPlatformInfo().getSqlCommandDelimiter());
        return result.toString();
    }

    @Override
    public String getDeleteSql(Table table, Map<String,Object> pkValues, boolean genPlaceholders) {
        return String.valueOf(this.getQuotationOnStatement()) + super.getDeleteSql(table, pkValues, genPlaceholders);
    }

    @Override
    public String getInsertSql(Table table, Map columnValues, boolean genPlaceholders) {
        return String.valueOf(this.getQuotationOnStatement()) + super.getInsertSql(table, columnValues, genPlaceholders);
    }

    @Override
    public String getUpdateSql(Table table, Map columnValues, boolean genPlaceholders) {
        return String.valueOf(this.getQuotationOnStatement()) + super.getUpdateSql(table, columnValues, genPlaceholders);
    }

    private void printAlwaysSingleQuotedIdentifier(String identifier) throws IOException {
        this.print("'");
        this.print(identifier);
        this.print("'");
    }

    @Override
    protected void writeCopyDataStatement(Table sourceTable, Table targetTable) throws IOException {
        boolean hasIdentityColumns;
        boolean bl = hasIdentityColumns = targetTable.getAutoIncrementColumns().length > 0;
        if (hasIdentityColumns) {
            this.print("SET IDENTITY_INSERT ");
            this.printIdentifier(this.getTableName(targetTable));
            this.print(" ON");
            this.printEndOfStatement();
        }
        super.writeCopyDataStatement(sourceTable, targetTable);
        if (hasIdentityColumns) {
            this.print("SET IDENTITY_INSERT ");
            this.printIdentifier(this.getTableName(targetTable));
            this.print(" OFF");
            this.printEndOfStatement();
        }
    }

    @Override
    protected void processChanges(Database currentModel, Database desiredModel, List changes, CreationParameters params) throws IOException {
        if (!changes.isEmpty()) {
            this.writeQuotationOnStatement();
        }
        HashSet<Index> removedIndexes = new HashSet<Index>();
        HashSet<ForeignKey> removedForeignKeys = new HashSet<ForeignKey>();
        HashSet<Table> removedPKs = new HashSet<Table>();
        for (Object change : changes) {
            if (change instanceof RemoveIndexChange) {
                removedIndexes.add(((RemoveIndexChange)change).getIndex());
                continue;
            }
            if (change instanceof RemoveForeignKeyChange) {
                removedForeignKeys.add(((RemoveForeignKeyChange)change).getForeignKey());
                continue;
            }
            if (!(change instanceof RemovePrimaryKeyChange)) continue;
            removedPKs.add(((RemovePrimaryKeyChange)change).getChangedTable());
        }
        ArrayList<TableChangeImplBase> additionalChanges = new ArrayList<TableChangeImplBase>();
        for (Object change : changes) {
            if (!(change instanceof ColumnDataTypeChange) && !(change instanceof ColumnSizeChange)) continue;
            Column column = ((ColumnChange)change).getChangedColumn();
            Table table = ((ColumnChange)change).getChangedTable();
            if (column.isPrimaryKey() && !removedPKs.contains(table)) {
                Column[] pk = table.getPrimaryKeyColumns();
                additionalChanges.add(new RemovePrimaryKeyChange(table, pk));
                additionalChanges.add(new AddPrimaryKeyChange(table, pk));
                removedPKs.add(table);
            }
            int idx = 0;
            while (idx < table.getIndexCount()) {
                Index index = table.getIndex(idx);
                if (index.hasColumn(column) && !removedIndexes.contains(index)) {
                    additionalChanges.add(new RemoveIndexChange(table, index));
                    additionalChanges.add(new AddIndexChange(table, index));
                    removedIndexes.add(index);
                }
                ++idx;
            }
            int tableIdx = 0;
            while (tableIdx < currentModel.getTableCount()) {
                Table curTable = currentModel.getTable(tableIdx);
                int fkIdx = 0;
                while (fkIdx < curTable.getForeignKeyCount()) {
                    ForeignKey curFk = curTable.getForeignKey(fkIdx);
                    if ((curFk.hasLocalColumn(column) || curFk.hasForeignColumn(column)) && !removedForeignKeys.contains(curFk)) {
                        additionalChanges.add(new RemoveForeignKeyChange(curTable, curFk));
                        additionalChanges.add(new AddForeignKeyChange(curTable, curFk));
                        removedForeignKeys.add(curFk);
                    }
                    ++fkIdx;
                }
                ++tableIdx;
            }
        }
        changes.addAll(additionalChanges);
        super.processChanges(currentModel, desiredModel, changes, params);
    }

    @Override
    protected void processTableStructureChanges(Database currentModel, Database desiredModel, Table sourceTable, Table targetTable, Map parameters, List changes) throws IOException {
        TableChange change;
        Iterator changeIt = changes.iterator();
        while (changeIt.hasNext()) {
            TableChange change2 = (TableChange)changeIt.next();
            if (change2 instanceof RemovePrimaryKeyChange) {
                this.processChange(currentModel, desiredModel, (RemovePrimaryKeyChange)change2);
                changeIt.remove();
                continue;
            }
            if (!(change2 instanceof PrimaryKeyChange)) continue;
            PrimaryKeyChange pkChange = (PrimaryKeyChange)change2;
            RemovePrimaryKeyChange removePrimaryKeyChange = new RemovePrimaryKeyChange(pkChange.getChangedTable(), pkChange.getOldPrimaryKeyColumns());
            this.processChange(currentModel, desiredModel, removePrimaryKeyChange);
        }
        ArrayList<TableChange> columnChanges = new ArrayList<TableChange>();
        Iterator changeIt2 = changes.iterator();
        while (changeIt2.hasNext()) {
            change = (TableChange)changeIt2.next();
            if (change instanceof AddColumnChange) {
                AddColumnChange addColumnChange = (AddColumnChange)change;
                if (!addColumnChange.isAtEnd()) continue;
                this.processChange(currentModel, desiredModel, addColumnChange);
                changeIt2.remove();
                continue;
            }
            if (change instanceof RemoveColumnChange) {
                this.processChange(currentModel, desiredModel, (RemoveColumnChange)change);
                changeIt2.remove();
                continue;
            }
            if (change instanceof ColumnAutoIncrementChange) {
                columnChanges = null;
                continue;
            }
            if (!(change instanceof ColumnChange) || columnChanges == null) continue;
            columnChanges.add(change);
        }
        if (columnChanges != null) {
            HashSet<Column> processedColumns = new HashSet<Column>();
            for (TableChange tableChange : columnChanges) {
                ColumnChange columnChange = (ColumnChange) tableChange;
                if(columnChange instanceof ColumnChange){
                Column sourceColumn = columnChange.getChangedColumn();
                Column targetColumn = targetTable.findColumn(sourceColumn.getName(), this.getPlatform().isDelimitedIdentifierModeOn());
                if (!processedColumns.contains(targetColumn)) {
                    this.processColumnChange(sourceTable, targetTable, sourceColumn, targetColumn, columnChange instanceof ColumnDataTypeChange || columnChange instanceof ColumnSizeChange);
                    processedColumns.add(targetColumn);
                }
                changes.remove(columnChange);
                columnChange.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
            }}
        }
        Iterator changeIt3 = changes.iterator();
        while (changeIt3.hasNext()) {
            change = (TableChange)changeIt3.next();
            if (change instanceof AddPrimaryKeyChange) {
                this.processChange(currentModel, desiredModel, (AddPrimaryKeyChange)change);
                changeIt3.remove();
                continue;
            }
            if (!(change instanceof PrimaryKeyChange)) continue;
            PrimaryKeyChange primaryKeyChange = (PrimaryKeyChange)change;
            AddPrimaryKeyChange addPkChange = new AddPrimaryKeyChange(primaryKeyChange.getChangedTable(), primaryKeyChange.getNewPrimaryKeyColumns());
            this.processChange(currentModel, desiredModel, addPkChange);
            changeIt3.remove();
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
        this.print("DROP COLUMN ");
        this.printIdentifier(this.getColumnName(change.getColumn()));
        this.printEndOfStatement();
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processChange(Database currentModel, Database desiredModel, RemovePrimaryKeyChange change) throws IOException {
        String tableName = this.getTableName(change.getChangedTable());
        String tableNameVar = "tn" + this.createUniqueIdentifier();
        String constraintNameVar = "cn" + this.createUniqueIdentifier();
        this.println("BEGIN");
        this.println("  DECLARE @" + tableNameVar + " nvarchar(256), @" + constraintNameVar + " nvarchar(256)");
        this.println("  DECLARE refcursor CURSOR FOR");
        this.println("  SELECT object_name(objs.parent_obj) tablename, objs.name constraintname");
        this.println("    FROM sysobjects objs JOIN sysconstraints cons ON objs.id = cons.constid");
        this.print("    WHERE objs.xtype = 'PK' AND object_name(objs.parent_obj) = ");
        this.printAlwaysSingleQuotedIdentifier(tableName);
        this.println("  OPEN refcursor");
        this.println("  FETCH NEXT FROM refcursor INTO @" + tableNameVar + ", @" + constraintNameVar);
        this.println("  WHILE @@FETCH_STATUS = 0");
        this.println("    BEGIN");
        this.println("      EXEC ('ALTER TABLE '+@" + tableNameVar + "+' DROP CONSTRAINT '+@" + constraintNameVar + ")");
        this.println("      FETCH NEXT FROM refcursor INTO @" + tableNameVar + ", @" + constraintNameVar);
        this.println("    END");
        this.println("  CLOSE refcursor");
        this.println("  DEALLOCATE refcursor");
        this.print("END");
        this.printEndOfStatement();
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processColumnChange(Table sourceTable, Table targetTable, Column sourceColumn, Column targetColumn, boolean typeChange) throws IOException {
        boolean hasDefault = sourceColumn.getParsedDefaultValue() != null;
        boolean shallHaveDefault = targetColumn.getParsedDefaultValue() != null;
        String newDefault = targetColumn.getDefaultValue();
        if (newDefault != null) {
            targetColumn.setDefaultValue(null);
        }
        if (hasDefault) {
            String tableName = this.getTableName(sourceTable);
            String columnName = this.getColumnName(sourceColumn);
            String tableNameVar = "tn" + this.createUniqueIdentifier();
            String constraintNameVar = "cn" + this.createUniqueIdentifier();
            this.println("BEGIN");
            this.println("  DECLARE @" + tableNameVar + " nvarchar(256), @" + constraintNameVar + " nvarchar(256)");
            this.println("  DECLARE refcursor CURSOR FOR");
            this.println("  SELECT object_name(objs.parent_obj) tablename, objs.name constraintname");
            this.println("    FROM sysobjects objs JOIN sysconstraints cons ON objs.id = cons.constid");
            this.println("    WHERE objs.xtype = 'D' AND");
            this.print("          cons.colid = (SELECT colid FROM syscolumns WHERE id = object_id(");
            this.printAlwaysSingleQuotedIdentifier(tableName);
            this.print(") AND name = ");
            this.printAlwaysSingleQuotedIdentifier(columnName);
            this.println(") AND");
            this.print("          object_name(objs.parent_obj) = ");
            this.printAlwaysSingleQuotedIdentifier(tableName);
            this.println("  OPEN refcursor");
            this.println("  FETCH NEXT FROM refcursor INTO @" + tableNameVar + ", @" + constraintNameVar);
            this.println("  WHILE @@FETCH_STATUS = 0");
            this.println("    BEGIN");
            this.println("      EXEC ('ALTER TABLE '+@" + tableNameVar + "+' DROP CONSTRAINT '+@" + constraintNameVar + ")");
            this.println("      FETCH NEXT FROM refcursor INTO @" + tableNameVar + ", @" + constraintNameVar);
            this.println("    END");
            this.println("  CLOSE refcursor");
            this.println("  DEALLOCATE refcursor");
            this.print("END");
            this.printEndOfStatement();
        }
        this.print("ALTER TABLE ");
        this.printlnIdentifier(this.getTableName(sourceTable));
        this.printIndent();
        this.print("ALTER COLUMN ");
        this.writeColumn(sourceTable, targetColumn);
        this.printEndOfStatement();
        if (shallHaveDefault) {
            targetColumn.setDefaultValue(newDefault);
            this.print("ALTER TABLE ");
            this.printlnIdentifier(this.getTableName(sourceTable));
            this.printIndent();
            this.print("ADD CONSTRAINT ");
            this.printIdentifier(this.getConstraintName("DF", sourceTable, sourceColumn.getName(), null));
            this.writeColumnDefaultValueStmt(sourceTable, targetColumn);
            this.print(" FOR ");
            this.printIdentifier(this.getColumnName(sourceColumn));
            this.printEndOfStatement();
        }
    }
}


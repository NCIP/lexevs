/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.sybase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.alteration.AddColumnChange;
import org.apache.ddlutils.alteration.AddPrimaryKeyChange;
import org.apache.ddlutils.alteration.ColumnAutoIncrementChange;
import org.apache.ddlutils.alteration.ColumnChange;
import org.apache.ddlutils.alteration.ColumnDefaultValueChange;
import org.apache.ddlutils.alteration.PrimaryKeyChange;
import org.apache.ddlutils.alteration.RemoveColumnChange;
import org.apache.ddlutils.alteration.RemovePrimaryKeyChange;
import org.apache.ddlutils.alteration.TableChange;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.CreationParameters;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.util.Jdbc3Utils;

public class SybaseBuilder
extends SqlBuilder {
    public SybaseBuilder(Platform platform) {
        super(platform);
        this.addEscapedCharSequence("'", "''");
    }

    @Override
    public void createTable(Database database, Table table, Map parameters) throws IOException {
        this.writeQuotationOnStatement();
        super.createTable(database, table, parameters);
    }

    @Override
    protected void writeTableCreationStmtEnding(Table table, Map parameters) throws IOException {
        if (parameters != null) {
            String lockValue = (String)parameters.get("lock");
            String atValue = (String)parameters.get("at");
            String externalTableAtValue = (String)parameters.get("external table at");
            String onValue = (String)parameters.get("on");
            if (lockValue != null) {
                this.print(" lock ");
                this.print(lockValue);
            }
            boolean writtenWithParameters = false;
            for (Object entryObject : parameters.entrySet()) {
                if(entryObject instanceof Map.Entry){
                    Map.Entry entry = (Map.Entry)entryObject;
                String name = entry.getKey().toString();
                if ("lock".equals(name) || "at".equals(name) || "external table at".equals(name) || "on".equals(name)) continue;
                if (!writtenWithParameters) {
                    this.print(" with ");
                    writtenWithParameters = true;
                } else {
                    this.print(", ");
                }
                this.print(name);
                if (entry.getValue() == null) continue;
                this.print("=");
                this.print(entry.getValue().toString());
            }}
            if (onValue != null) {
                this.print(" on ");
                this.print(onValue);
            }
            if (externalTableAtValue != null) {
                this.print(" external table at \"");
                this.print(externalTableAtValue);
                this.print("\"");
            } else if (atValue != null) {
                this.print(" at \"");
                this.print(atValue);
                this.print("\"");
            }
        }
        super.writeTableCreationStmtEnding(table, parameters);
    }

    @Override
    protected void writeColumn(Table table, Column column) throws IOException {
        this.printIdentifier(this.getColumnName(column));
        this.print(" ");
        this.print(this.getSqlType(column));
        this.writeColumnDefaultValueStmt(table, column);
        if (column.isAutoIncrement()) {
            this.print(" ");
            this.writeColumnAutoIncrementStmt(table, column);
        } else {
            this.print(" ");
            if (column.isRequired()) {
                this.writeColumnNotNullableStmt();
            } else {
                this.writeColumnNullableStmt();
            }
        }
    }

    @Override
    protected String getNativeDefaultValue(Column column) {
        if (column.getTypeCode() == -7 || Jdbc3Utils.supportsJava14JdbcTypes() && column.getTypeCode() == Jdbc3Utils.determineBooleanTypeCode()) {
            return this.getDefaultValueHelper().convert(column.getDefaultValue(), column.getTypeCode(), 5).toString();
        }
        return super.getNativeDefaultValue(column);
    }

    @Override
    public void dropTable(Table table) throws IOException {
        this.writeQuotationOnStatement();
        this.print("IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = ");
        this.printAlwaysSingleQuotedIdentifier(this.getTableName(table));
        this.println(")");
        this.println("BEGIN");
        this.printIndent();
        this.print("DROP TABLE ");
        this.printlnIdentifier(this.getTableName(table));
        this.print("END");
        this.printEndOfStatement();
    }

    @Override
    protected void writeExternalForeignKeyDropStmt(Table table, ForeignKey foreignKey) throws IOException {
        String constraintName = this.getForeignKeyName(table, foreignKey);
        this.print("IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'RI' AND name = ");
        this.printAlwaysSingleQuotedIdentifier(constraintName);
        this.println(")");
        this.printIndent();
        this.print("ALTER TABLE ");
        this.printIdentifier(this.getTableName(table));
        this.print(" DROP CONSTRAINT ");
        this.printIdentifier(constraintName);
        this.printEndOfStatement();
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
    public void dropExternalForeignKeys(Table table) throws IOException {
        this.writeQuotationOnStatement();
        super.dropExternalForeignKeys(table);
    }

    @Override
    public String getSelectLastIdentityValues(Table table) {
        return "SELECT @@IDENTITY";
    }

    protected String getEnableIdentityOverrideSql(Table table) {
        StringBuffer result = new StringBuffer();
        result.append("SET IDENTITY_INSERT ");
        result.append(this.getDelimitedIdentifier(this.getTableName(table)));
        result.append(" ON");
        return result.toString();
    }

    protected String getDisableIdentityOverrideSql(Table table) {
        StringBuffer result = new StringBuffer();
        result.append("SET IDENTITY_INSERT ");
        result.append(this.getDelimitedIdentifier(this.getTableName(table)));
        result.append(" OFF");
        return result.toString();
    }

    protected String getQuotationOnStatement() {
        if (this.getPlatform().isDelimitedIdentifierModeOn()) {
            return "SET quoted_identifier on";
        }
        return "";
    }

    private void writeQuotationOnStatement() throws IOException {
        this.print(this.getQuotationOnStatement());
        this.printEndOfStatement();
    }

    private void printAlwaysSingleQuotedIdentifier(String identifier) throws IOException {
        this.print("'");
        this.print(identifier);
        this.print("'");
    }

    @Override
    protected void writeCopyDataStatement(Table sourceTable, Table targetTable) throws IOException {
        boolean hasIdentity;
        boolean bl = hasIdentity = targetTable.getAutoIncrementColumns().length > 0;
        if (hasIdentity) {
            this.print("SET IDENTITY_INSERT ");
            this.printIdentifier(this.getTableName(targetTable));
            this.print(" ON");
            this.printEndOfStatement();
        }
        super.writeCopyDataStatement(sourceTable, targetTable);
        if (hasIdentity) {
            this.print("SET IDENTITY_INSERT ");
            this.printIdentifier(this.getTableName(targetTable));
            this.print(" OFF");
            this.printEndOfStatement();
        }
    }

    @Override
    protected void writeCastExpression(Column sourceColumn, Column targetColumn) throws IOException {
        String targetNativeType;
        String sourceNativeType = this.getBareNativeType(sourceColumn);
        if (sourceNativeType.equals(targetNativeType = this.getBareNativeType(targetColumn))) {
            this.printIdentifier(this.getColumnName(sourceColumn));
        } else {
            this.print("CONVERT(");
            this.print(this.getNativeType(targetColumn));
            this.print(",");
            this.printIdentifier(this.getColumnName(sourceColumn));
            this.print(")");
        }
    }

    @Override
    protected void processChanges(Database currentModel, Database desiredModel, List changes, CreationParameters params) throws IOException {
        if (!changes.isEmpty()) {
            this.writeQuotationOnStatement();
        }
        super.processChanges(currentModel, desiredModel, changes, params);
    }

    @Override
    protected void processTableStructureChanges(Database currentModel, Database desiredModel, Table sourceTable, Table targetTable, Map parameters, List changes) throws IOException {
        ArrayList changesPerColumn;
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
            RemovePrimaryKeyChange removePkChange = new RemovePrimaryKeyChange(pkChange.getChangedTable(), pkChange.getOldPrimaryKeyColumns());
            this.processChange(currentModel, desiredModel, removePkChange);
        }
        HashMap<Column, ArrayList<TableChange>> columnChanges = new HashMap<Column, ArrayList<TableChange>>();
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
            ColumnChange columnChange = (ColumnChange)change;
            changesPerColumn = (ArrayList)columnChanges.get(columnChange.getChangedColumn());
            if (changesPerColumn == null) {
                changesPerColumn = new ArrayList();
                columnChanges.put(columnChange.getChangedColumn(), changesPerColumn);
            }
            changesPerColumn.add(change);
        }
        if (columnChanges != null) {
            for (Map.Entry entry : columnChanges.entrySet()) {
                Column sourceColumn = (Column)entry.getKey();
                changesPerColumn = (ArrayList)entry.getValue();
                if (changesPerColumn.size() == 1 && changesPerColumn.get(0) instanceof ColumnDefaultValueChange) {
                    this.processChange(currentModel, desiredModel, (ColumnDefaultValueChange)changesPerColumn.get(0));
                } else {
                    Column targetColumn = targetTable.findColumn(sourceColumn.getName(), this.getPlatform().isDelimitedIdentifierModeOn());
                    this.processColumnChange(sourceTable, targetTable, sourceColumn, targetColumn);
                }
                Iterator changeIt3 = changesPerColumn.iterator();
                while (changeIt3.hasNext()) {
                    ((ColumnChange)changeIt3.next()).apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
                }
            }
        }
        changeIt2 = changes.iterator();
        while (changeIt2.hasNext()) {
            change = (TableChange)changeIt2.next();
            if (change instanceof AddPrimaryKeyChange) {
                this.processChange(currentModel, desiredModel, (AddPrimaryKeyChange)change);
                changeIt2.remove();
                continue;
            }
            if (!(change instanceof PrimaryKeyChange)) continue;
            PrimaryKeyChange pkChange = (PrimaryKeyChange)change;
            AddPrimaryKeyChange addPkChange = new AddPrimaryKeyChange(pkChange.getChangedTable(), pkChange.getNewPrimaryKeyColumns());
            this.processChange(currentModel, desiredModel, addPkChange);
            changeIt2.remove();
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
        this.printEndOfStatement();
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processChange(Database currentModel, Database desiredModel, RemovePrimaryKeyChange change) throws IOException {
        String tableName = this.getTableName(change.getChangedTable());
        String tableNameVar = "tn" + this.createUniqueIdentifier();
        String constraintNameVar = "cn" + this.createUniqueIdentifier();
        this.println("BEGIN");
        this.println("  DECLARE @" + tableNameVar + " nvarchar(60), @" + constraintNameVar + " nvarchar(60)");
        this.println("  WHILE EXISTS(SELECT sysindexes.name");
        this.println("                 FROM sysindexes, sysobjects");
        this.print("                 WHERE sysobjects.name = ");
        this.printAlwaysSingleQuotedIdentifier(tableName);
        this.println(" AND sysobjects.id = sysindexes.id AND (sysindexes.status & 2048) > 0)");
        this.println("  BEGIN");
        this.println("    SELECT @" + tableNameVar + " = sysobjects.name, @" + constraintNameVar + " = sysindexes.name");
        this.println("      FROM sysindexes, sysobjects");
        this.print("      WHERE sysobjects.name = ");
        this.printAlwaysSingleQuotedIdentifier(tableName);
        this.print(" AND sysobjects.id = sysindexes.id AND (sysindexes.status & 2048) > 0");
        this.println("    EXEC ('ALTER TABLE '+@" + tableNameVar + "+' DROP CONSTRAINT '+@" + constraintNameVar + ")");
        this.println("  END");
        this.print("END");
        this.printEndOfStatement();
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processChange(Database currentModel, Database desiredModel, ColumnDefaultValueChange change) throws IOException {
        this.print("ALTER TABLE ");
        this.printlnIdentifier(this.getTableName(change.getChangedTable()));
        this.printIndent();
        this.print("REPLACE ");
        this.printIdentifier(this.getColumnName(change.getChangedColumn()));
        Table curTable = currentModel.findTable(change.getChangedTable().getName(), this.getPlatform().isDelimitedIdentifierModeOn());
        Column curColumn = curTable.findColumn(change.getChangedColumn().getName(), this.getPlatform().isDelimitedIdentifierModeOn());
        this.print(" DEFAULT ");
        if (this.isValidDefaultValue(change.getNewDefaultValue(), curColumn.getTypeCode())) {
            this.printDefaultValue(change.getNewDefaultValue(), curColumn.getTypeCode());
        } else {
            this.print("NULL");
        }
        this.printEndOfStatement();
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processColumnChange(Table sourceTable, Table targetTable, Column sourceColumn, Column targetColumn) throws IOException {
        boolean defaultChanges;
        Object oldParsedDefault = sourceColumn.getParsedDefaultValue();
        Object newParsedDefault = targetColumn.getParsedDefaultValue();
        String newDefault = targetColumn.getDefaultValue();
        boolean bl = defaultChanges = oldParsedDefault == null && newParsedDefault != null || oldParsedDefault != null && !oldParsedDefault.equals(newParsedDefault);
        if (newDefault != null) {
            targetColumn.setDefaultValue(null);
        }
        if (defaultChanges) {
            this.print("ALTER TABLE ");
            this.printlnIdentifier(this.getTableName(sourceTable));
            this.printIndent();
            this.print("REPLACE ");
            this.printIdentifier(this.getColumnName(sourceColumn));
            this.print(" DEFAULT NULL");
            this.printEndOfStatement();
        }
        this.print("ALTER TABLE ");
        this.printlnIdentifier(this.getTableName(sourceTable));
        this.printIndent();
        this.print("MODIFY ");
        this.writeColumn(sourceTable, targetColumn);
        this.printEndOfStatement();
        if (defaultChanges) {
            this.print("ALTER TABLE ");
            this.printlnIdentifier(this.getTableName(sourceTable));
            this.printIndent();
            this.print("REPLACE ");
            this.printIdentifier(this.getColumnName(sourceColumn));
            if (newDefault != null) {
                this.writeColumnDefaultValueStmt(sourceTable, targetColumn);
            } else {
                this.print(" DEFAULT NULL");
            }
            this.printEndOfStatement();
        }
    }
}


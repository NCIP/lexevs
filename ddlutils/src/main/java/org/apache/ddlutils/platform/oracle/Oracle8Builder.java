/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.oro.text.regex.MalformedPatternException
 *  org.apache.oro.text.regex.Pattern
 *  org.apache.oro.text.regex.Perl5Compiler
 *  org.apache.oro.text.regex.Perl5Matcher
 */
package org.apache.ddlutils.platform.oracle;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.alteration.AddColumnChange;
import org.apache.ddlutils.alteration.AddPrimaryKeyChange;
import org.apache.ddlutils.alteration.PrimaryKeyChange;
import org.apache.ddlutils.alteration.RemoveColumnChange;
import org.apache.ddlutils.alteration.RemovePrimaryKeyChange;
import org.apache.ddlutils.alteration.TableChange;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.util.Jdbc3Utils;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

public class Oracle8Builder
extends SqlBuilder {
    private Pattern _isoDatePattern;
    private Pattern _isoTimePattern;
    private Pattern _isoTimestampPattern;

    public Oracle8Builder(Platform platform) {
        super(platform);
        this.addEscapedCharSequence("'", "''");
        Perl5Compiler compiler = new Perl5Compiler();
        try {
            this._isoDatePattern = compiler.compile("\\d{4}\\-\\d{2}\\-\\d{2}");
            this._isoTimePattern = compiler.compile("\\d{2}:\\d{2}:\\d{2}");
            this._isoTimestampPattern = compiler.compile("\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}:\\d{2}:\\d{2}[\\.\\d{1,8}]?");
        }
        catch (MalformedPatternException ex) {
            throw new DdlUtilsException(ex);
        }
    }

    @Override
    public void createTable(Database database, Table table, Map parameters) throws IOException {
        Column[] columns = table.getAutoIncrementColumns();
        int idx = 0;
        while (idx < columns.length) {
            this.createAutoIncrementSequence(table, columns[idx]);
            ++idx;
        }
        super.createTable(database, table, parameters);
        idx = 0;
        while (idx < columns.length) {
            this.createAutoIncrementTrigger(table, columns[idx]);
            ++idx;
        }
    }

    @Override
    public void dropTable(Table table) throws IOException {
        Column[] columns = table.getAutoIncrementColumns();
        int idx = 0;
        while (idx < columns.length) {
            this.dropAutoIncrementTrigger(table, columns[idx]);
            this.dropAutoIncrementSequence(table, columns[idx]);
            ++idx;
        }
        this.print("DROP TABLE ");
        this.printIdentifier(this.getTableName(table));
        this.print(" CASCADE CONSTRAINTS");
        this.printEndOfStatement();
    }

    protected void createAutoIncrementSequence(Table table, Column column) throws IOException {
        this.print("CREATE SEQUENCE ");
        this.printIdentifier(this.getConstraintName("seq", table, column.getName(), null));
        this.printEndOfStatement();
    }

    protected void createAutoIncrementTrigger(Table table, Column column) throws IOException {
        String columnName = this.getColumnName(column);
        String triggerName = this.getConstraintName("trg", table, column.getName(), null);
        if (this.getPlatform().isScriptModeOn()) {
            this.print("CREATE OR REPLACE TRIGGER ");
            this.printlnIdentifier(triggerName);
            this.print("BEFORE INSERT ON ");
            this.printlnIdentifier(this.getTableName(table));
            this.print("FOR EACH ROW WHEN (new.");
            this.printIdentifier(columnName);
            this.println(" IS NULL)");
            this.println("BEGIN");
            this.print("  SELECT ");
            this.printIdentifier(this.getConstraintName("seq", table, column.getName(), null));
            this.print(".nextval INTO :new.");
            this.printIdentifier(columnName);
            this.print(" FROM dual");
            this.println(this.getPlatformInfo().getSqlCommandDelimiter());
            this.print("END");
            this.println(this.getPlatformInfo().getSqlCommandDelimiter());
            this.println("/");
            this.println();
        } else {
            this.print("CREATE OR REPLACE TRIGGER ");
            this.printIdentifier(triggerName);
            this.print(" BEFORE INSERT ON ");
            this.printIdentifier(this.getTableName(table));
            this.print(" FOR EACH ROW WHEN (new.");
            this.printIdentifier(columnName);
            this.println(" IS NULL)");
            this.print("BEGIN SELECT ");
            this.printIdentifier(this.getConstraintName("seq", table, column.getName(), null));
            this.print(".nextval INTO :new.");
            this.printIdentifier(columnName);
            this.print(" FROM dual");
            this.print(this.getPlatformInfo().getSqlCommandDelimiter());
            this.print(" END");
            this.print(this.getPlatformInfo().getSqlCommandDelimiter());
            this.printEndOfStatement();
        }
    }

    protected void dropAutoIncrementSequence(Table table, Column column) throws IOException {
        this.print("DROP SEQUENCE ");
        this.printIdentifier(this.getConstraintName("seq", table, column.getName(), null));
        this.printEndOfStatement();
    }

    protected void dropAutoIncrementTrigger(Table table, Column column) throws IOException {
        this.print("DROP TRIGGER ");
        this.printIdentifier(this.getConstraintName("trg", table, column.getName(), null));
        this.printEndOfStatement();
    }

    @Override
    protected void createTemporaryTable(Database database, Table table, Map parameters) throws IOException {
        this.createTable(database, table, parameters);
    }

    @Override
    protected void dropTemporaryTable(Database database, Table table) throws IOException {
        this.dropTable(table);
    }

    @Override
    public void dropExternalForeignKeys(Table table) throws IOException {
    }

    @Override
    public void writeExternalIndexDropStmt(Table table, Index index) throws IOException {
        this.print("DROP INDEX ");
        this.printIdentifier(this.getIndexName(index));
        this.printEndOfStatement();
    }

    @Override
    protected void printDefaultValue(Object defaultValue, int typeCode) throws IOException {
        if (defaultValue != null) {
            boolean shouldUseQuotes;
            String defaultValueStr = defaultValue.toString();
            boolean bl = shouldUseQuotes = !TypeMap.isNumericType(typeCode) && !defaultValueStr.startsWith("TO_DATE(");
            if (shouldUseQuotes) {
                this.print(this.getPlatformInfo().getValueQuoteToken());
                this.print(this.escapeStringValue(defaultValueStr));
                this.print(this.getPlatformInfo().getValueQuoteToken());
            } else {
                this.print(defaultValueStr);
            }
        }
    }

    @Override
    protected String getNativeDefaultValue(Column column) {
        if (column.getTypeCode() == -7 || Jdbc3Utils.supportsJava14JdbcTypes() && column.getTypeCode() == Jdbc3Utils.determineBooleanTypeCode()) {
            return this.getDefaultValueHelper().convert(column.getDefaultValue(), column.getTypeCode(), 5).toString();
        }
        if (column.getTypeCode() == 91) {
            if (new Perl5Matcher().matches(column.getDefaultValue(), this._isoDatePattern)) {
                return "TO_DATE('" + column.getDefaultValue() + "', 'YYYY-MM-DD')";
            }
        } else if (column.getTypeCode() == 92) {
            if (new Perl5Matcher().matches(column.getDefaultValue(), this._isoTimePattern)) {
                return "TO_DATE('" + column.getDefaultValue() + "', 'HH24:MI:SS')";
            }
        } else if (column.getTypeCode() == 93 && new Perl5Matcher().matches(column.getDefaultValue(), this._isoTimestampPattern)) {
            return "TO_DATE('" + column.getDefaultValue() + "', 'YYYY-MM-DD HH24:MI:SS')";
        }
        return super.getNativeDefaultValue(column);
    }

    @Override
    protected void writeColumnAutoIncrementStmt(Table table, Column column) throws IOException {
    }

    @Override
    public String getSelectLastIdentityValues(Table table) {
        Column[] columns = table.getAutoIncrementColumns();
        if (columns.length > 0) {
            StringBuffer result = new StringBuffer();
            result.append("SELECT ");
            int idx = 0;
            while (idx < columns.length) {
                if (idx > 0) {
                    result.append(",");
                }
                result.append(this.getDelimitedIdentifier(this.getConstraintName("seq", table, columns[idx].getName(), null)));
                result.append(".currval");
                ++idx;
            }
            result.append(" FROM dual");
            return result.toString();
        }
        return null;
    }

    @Override
    protected void processTableStructureChanges(Database currentModel, Database desiredModel, Table sourceTable, Table targetTable, Map parameters, List changes) throws IOException {
        PrimaryKeyChange pkChange;

        for (Object change2 : changes) {
            AddColumnChange addColumnChange;
            if (!(change2 instanceof AddColumnChange) || (addColumnChange = (AddColumnChange)change2).isAtEnd() && (!addColumnChange.getNewColumn().isRequired() || addColumnChange.getNewColumn().getDefaultValue() != null)) continue;
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
        if (change.getNewColumn().isAutoIncrement()) {
            this.createAutoIncrementSequence(change.getChangedTable(), change.getNewColumn());
            this.createAutoIncrementTrigger(change.getChangedTable(), change.getNewColumn());
        }
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processChange(Database currentModel, Database desiredModel, RemoveColumnChange change) throws IOException {
        if (change.getColumn().isAutoIncrement()) {
            this.dropAutoIncrementTrigger(change.getChangedTable(), change.getColumn());
            this.dropAutoIncrementSequence(change.getChangedTable(), change.getColumn());
        }
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
}


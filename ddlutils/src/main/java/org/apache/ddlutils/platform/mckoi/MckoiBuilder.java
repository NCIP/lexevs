/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.mckoi;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.alteration.AddColumnChange;
import org.apache.ddlutils.alteration.ColumnAutoIncrementChange;
import org.apache.ddlutils.alteration.RemoveColumnChange;
import org.apache.ddlutils.alteration.TableChange;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.SqlBuilder;

public class MckoiBuilder
extends SqlBuilder {
    public MckoiBuilder(Platform platform) {
        super(platform);
        this.addEscapedCharSequence("\\", "\\\\");
        this.addEscapedCharSequence("'", "\\'");
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
    }

    @Override
    public void dropTable(Table table) throws IOException {
        this.print("DROP TABLE IF EXISTS ");
        this.printIdentifier(this.getTableName(table));
        this.printEndOfStatement();
        Column[] columns = table.getAutoIncrementColumns();
        int idx = 0;
        while (idx < columns.length) {
            this.dropAutoIncrementSequence(table, columns[idx]);
            ++idx;
        }
    }

    protected void createAutoIncrementSequence(Table table, Column column) throws IOException {
        this.print("CREATE SEQUENCE ");
        this.printIdentifier(this.getConstraintName("seq", table, column.getName(), null));
        this.printEndOfStatement();
    }

    protected void dropAutoIncrementSequence(Table table, Column column) throws IOException {
        this.print("DROP SEQUENCE ");
        this.printIdentifier(this.getConstraintName("seq", table, column.getName(), null));
        this.printEndOfStatement();
    }

    @Override
    protected void writeColumnDefaultValue(Table table, Column column) throws IOException {
        if (column.isAutoIncrement()) {
            this.print("NEXTVAL('");
            this.print(this.getConstraintName("seq", table, column.getName(), null));
            this.print("')");
        } else {
            super.writeColumnDefaultValue(table, column);
        }
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
                result.append("CURRVAL('");
                result.append(this.getConstraintName("seq", table, columns[idx].getName(), null));
                result.append("')");
                ++idx;
            }
            return result.toString();
        }
        return null;
    }

    @Override
    protected void processTableStructureChanges(Database currentModel, Database desiredModel, Table sourceTable, Table targetTable, Map parameters, List changes) throws IOException {
        ColumnAutoIncrementChange autoIncrChange;
        Column column;
        for (Object change : changes) {
            AddColumnChange addColumnChange;
            if (change instanceof ColumnAutoIncrementChange) {
                column = ((ColumnAutoIncrementChange)change).getColumn();
                if (column.isAutoIncrement()) continue;
                autoIncrChange = (ColumnAutoIncrementChange)change;
                this.createAutoIncrementSequence(autoIncrChange.getChangedTable(), autoIncrChange.getColumn());
                continue;
            }
            if (!(change instanceof AddColumnChange) || !(addColumnChange = (AddColumnChange)change).getNewColumn().isAutoIncrement()) continue;
            this.createAutoIncrementSequence(addColumnChange.getChangedTable(), addColumnChange.getNewColumn());
        }
        this.print("ALTER ");
        super.createTable(desiredModel, targetTable, parameters);
        for (Object change : changes) {
            RemoveColumnChange removeColumnChange;
            if (change instanceof ColumnAutoIncrementChange) {
                column = ((ColumnAutoIncrementChange)change).getColumn();
                if (!column.isAutoIncrement()) continue;
                autoIncrChange = (ColumnAutoIncrementChange)change;
                this.dropAutoIncrementSequence(autoIncrChange.getChangedTable(), autoIncrChange.getColumn());
                continue;
            }
            if (!(change instanceof RemoveColumnChange) || !(removeColumnChange = (RemoveColumnChange)change).getColumn().isAutoIncrement()) continue;
            this.dropAutoIncrementSequence(removeColumnChange.getChangedTable(), removeColumnChange.getColumn());
        }
        changes.clear();
    }
}


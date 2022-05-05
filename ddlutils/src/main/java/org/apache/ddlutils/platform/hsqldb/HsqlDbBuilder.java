/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.hsqldb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.alteration.AddColumnChange;
import org.apache.ddlutils.alteration.RemoveColumnChange;
import org.apache.ddlutils.alteration.TableChange;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.SqlBuilder;

public class HsqlDbBuilder
extends SqlBuilder {
    public HsqlDbBuilder(Platform platform) {
        super(platform);
        this.addEscapedCharSequence("'", "''");
    }

    @Override
    public void dropTable(Table table) throws IOException {
        this.print("DROP TABLE ");
        this.printIdentifier(this.getTableName(table));
        this.print(" IF EXISTS");
        this.printEndOfStatement();
    }

    @Override
    public String getSelectLastIdentityValues(Table table) {
        return "CALL IDENTITY()";
    }

    @Override
    protected void processTableStructureChanges(Database currentModel, Database desiredModel, Table sourceTable, Table targetTable, Map parameters, List changes) throws IOException {
        TableChange change;
        for (Object change2 : changes) {
            if (!(change2 instanceof RemoveColumnChange) || !((RemoveColumnChange)change2).getColumn().isPrimaryKey()) continue;
            return;
        }
        ArrayList<TableChange> addColumnChanges = new ArrayList<TableChange>();
        Iterator changeIt = changes.iterator();
        while (changeIt.hasNext()) {
            change = (TableChange)changeIt.next();
            if (!(change instanceof AddColumnChange)) continue;
            addColumnChanges.add(change);
            changeIt.remove();
        }
        changeIt = addColumnChanges.listIterator(addColumnChanges.size());
        while (((ListIterator<?>) changeIt).hasPrevious()) {
            AddColumnChange addColumnChange = (AddColumnChange) ((ListIterator<?>) changeIt).previous();
            this.processChange(currentModel, desiredModel, addColumnChange);
            changeIt.remove();
        }
        changeIt = changes.iterator();
        while (changeIt.hasNext()) {
            change = (TableChange)changeIt.next();
            if (!(change instanceof RemoveColumnChange)) continue;
            RemoveColumnChange removeColumnChange = (RemoveColumnChange)change;
            this.processChange(currentModel, desiredModel, removeColumnChange);
            changeIt.remove();
        }
    }

    protected void processChange(Database currentModel, Database desiredModel, AddColumnChange change) throws IOException {
        this.print("ALTER TABLE ");
        this.printlnIdentifier(this.getTableName(change.getChangedTable()));
        this.printIndent();
        this.print("ADD COLUMN ");
        this.writeColumn(change.getChangedTable(), change.getNewColumn());
        if (change.getNextColumn() != null) {
            this.print(" BEFORE ");
            this.printIdentifier(this.getColumnName(change.getNextColumn()));
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
}


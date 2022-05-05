/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.task;

import java.util.ArrayList;
import java.util.StringTokenizer;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.task.Parameter;

public class TableSpecificParameter
extends Parameter {
    private ArrayList<String> _tables = new ArrayList<String>();

    public void setTables(String tableList) {
        StringTokenizer tokenizer = new StringTokenizer(tableList, ",");
        while (tokenizer.hasMoreTokens()) {
            String tableName = tokenizer.nextToken().trim();
            this._tables.add(tableName);
        }
    }

    public void setTable(String tableName) {
        this._tables.add(tableName);
    }

    public boolean isForTable(Table table, boolean caseSensitive) {
        if (this._tables.isEmpty()) {
            return true;
        }
        for (String tableName : this._tables) {
            if ((!caseSensitive || !tableName.equals(table.getName())) && (caseSensitive || !tableName.equalsIgnoreCase(table.getName()))) continue;
            return true;
        }
        return false;
    }
}


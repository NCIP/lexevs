/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.io;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

public class DataDtdWriter {
    public void writeDtd(Database model, Writer output) throws IOException {
        PrintWriter writer = new PrintWriter(output);
        writer.println("<!-- DTD for XML data files for database " + model.getName() + " -->\n");
        writer.println("<!ELEMENT data (");
        int idx = 0;
        while (idx < model.getTableCount()) {
            Table table = model.getTable(idx);
            writer.print("    " + table.getName());
            if (idx < model.getTableCount() - 1) {
                writer.println(" |");
            } else {
                writer.println();
            }
            ++idx;
        }
        writer.println(")*>");
        idx = 0;
        while (idx < model.getTableCount()) {
            this.writeTableElement(model.getTable(idx), writer);
            ++idx;
        }
    }

    private void writeTableElement(Table table, PrintWriter writer) throws IOException {
        writer.println("\n<!ELEMENT " + table.getName() + " EMPTY>");
        writer.println("<!ATTLIST " + table.getName());
        int idx = 0;
        while (idx < table.getColumnCount()) {
            this.writeColumnAttributeEntry(table.getColumn(idx), writer);
            ++idx;
        }
        writer.println(">");
    }

    private void writeColumnAttributeEntry(Column column, PrintWriter writer) throws IOException {
        writer.print("    <!--");
        if (column.isPrimaryKey()) {
            writer.print(" primary key,");
        }
        if (column.isAutoIncrement()) {
            writer.print(" auto increment,");
        }
        writer.print(" JDBC type: " + column.getType());
        if (column.getSize() != null && column.getSize().length() > 0) {
            writer.print("(" + column.getSize() + ")");
        }
        writer.println(" -->");
        writer.print("    " + column.getName() + " CDATA ");
        if (column.getDefaultValue() != null && column.getDefaultValue().length() > 0) {
            writer.println("\"" + column.getDefaultValue() + "\"");
        } else {
            writer.println(column.isRequired() ? "#REQUIRED" : "#IMPLIED");
        }
    }
}


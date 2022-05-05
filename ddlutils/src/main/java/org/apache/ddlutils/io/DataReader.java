/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.digester.Digester
 *  org.apache.commons.digester.Rules
 */
package org.apache.ddlutils.io;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rules;
import org.apache.ddlutils.io.ConverterConfiguration;
import org.apache.ddlutils.io.DataSink;
import org.apache.ddlutils.io.DigesterRules;
import org.apache.ddlutils.io.DynaSqlCreateRule;
import org.apache.ddlutils.io.SetColumnPropertyFromSubElementRule;
import org.apache.ddlutils.io.SetColumnPropertyRule;
import org.apache.ddlutils.io.converters.SqlTypeConverter;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

public class DataReader
extends Digester {
    private Database _model;
    private DataSink _sink;
    private boolean _needsConfiguration = true;
    private ConverterConfiguration _converterConf = new ConverterConfiguration();
    private boolean _caseSensitive = false;

    public ConverterConfiguration getConverterConfiguration() {
        return this._converterConf;
    }

    public Database getModel() {
        return this._model;
    }

    public void setModel(Database model) {
        this._model = model;
        this._needsConfiguration = true;
    }

    public DataSink getSink() {
        return this._sink;
    }

    public void setSink(DataSink sink) {
        this._sink = sink;
        this._needsConfiguration = true;
    }

    public boolean isCaseSensitive() {
        return this._caseSensitive;
    }

    public void setCaseSensitive(boolean beCaseSensitive) {
        this._caseSensitive = beCaseSensitive;
    }

    protected void configure() {
        if (this._needsConfiguration) {
            if (this._model == null) {
                throw new NullPointerException("No database model specified");
            }
            if (this._sink == null) {
                throw new NullPointerException("No data sink model specified");
            }
            DigesterRules rules = new DigesterRules();
            rules.setCaseSensitive(this.isCaseSensitive());
            this.setRules((Rules)rules);
            int tableIdx = 0;
            while (tableIdx < this._model.getTableCount()) {
                Table table = this._model.getTable(tableIdx);
                String path = "data/" + table.getName();
                this.addRule(path, new DynaSqlCreateRule(this._model, table, this._sink));
                int columnIdx = 0;
                while (columnIdx < table.getColumnCount()) {
                    Column column = table.getColumn(columnIdx);
                    SqlTypeConverter converter = this._converterConf.getRegisteredConverter(table, column);
                    this.addRule(path, new SetColumnPropertyRule(column, converter, this.isCaseSensitive()));
                    this.addRule(String.valueOf(path) + "/" + column.getName(), new SetColumnPropertyFromSubElementRule(column, converter));
                    ++columnIdx;
                }
                ++tableIdx;
            }
            this._needsConfiguration = false;
        }
        super.configure();
    }
}


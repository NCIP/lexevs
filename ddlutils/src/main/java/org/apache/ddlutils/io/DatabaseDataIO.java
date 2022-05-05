/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.collections.map.ListOrderedMap
 */
package org.apache.ddlutils.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.io.ConverterConfiguration;
import org.apache.ddlutils.io.DataConverterRegistration;
import org.apache.ddlutils.io.DataReader;
import org.apache.ddlutils.io.DataToDatabaseSink;
import org.apache.ddlutils.io.DataWriter;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

public class DatabaseDataIO {
    private ArrayList<DataConverterRegistration> _converters = new ArrayList<DataConverterRegistration>();
    private boolean _failOnError = true;
    private boolean _ensureFKOrder = true;
    private boolean _useBatchMode;
    private Integer _batchSize;
    private boolean _determineSchema;
    private String _schemaPattern;

    public void registerConverter(DataConverterRegistration converterRegistration) {
        this._converters.add(converterRegistration);
    }

    public boolean isFailOnError() {
        return this._failOnError;
    }

    public void setFailOnError(boolean failOnError) {
        this._failOnError = failOnError;
    }

    public boolean getUseBatchMode() {
        return this._useBatchMode;
    }

    public void setUseBatchMode(boolean useBatchMode) {
        this._useBatchMode = useBatchMode;
    }

    public Integer getBatchSize() {
        return this._batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this._batchSize = batchSize;
    }

    public boolean isEnsureFKOrder() {
        return this._ensureFKOrder;
    }

    public void setEnsureFKOrder(boolean ensureFKOrder) {
        this._ensureFKOrder = ensureFKOrder;
    }

    public void setDetermineSchema(boolean determineSchema) {
        this._determineSchema = determineSchema;
    }

    public void setSchemaPattern(String schemaPattern) {
        this._schemaPattern = schemaPattern;
    }

    private void registerConverters(ConverterConfiguration converterConf) throws DdlUtilsException {
        for (DataConverterRegistration registrationInfo : this._converters) {
            if (registrationInfo.getTypeCode() != Integer.MIN_VALUE) {
                converterConf.registerConverter(registrationInfo.getTypeCode(), registrationInfo.getConverter());
                continue;
            }
            if (registrationInfo.getTable() == null || registrationInfo.getColumn() == null) {
                throw new DdlUtilsException("Please specify either the jdbc type or a table/column pair for which the converter shall be defined");
            }
            converterConf.registerConverter(registrationInfo.getTable(), registrationInfo.getColumn(), registrationInfo.getConverter());
        }
    }

    public DataWriter getConfiguredDataWriter(String path, String xmlEncoding) throws DdlUtilsException {
        try {
            DataWriter writer = new DataWriter(new FileOutputStream(path), xmlEncoding);
            this.registerConverters(writer.getConverterConfiguration());
            return writer;
        }
        catch (IOException ex) {
            throw new DdlUtilsException(ex);
        }
    }

    public DataWriter getConfiguredDataWriter(OutputStream output, String xmlEncoding) throws DdlUtilsException {
        DataWriter writer = new DataWriter(output, xmlEncoding);
        this.registerConverters(writer.getConverterConfiguration());
        return writer;
    }

    public DataWriter getConfiguredDataWriter(Writer output, String xmlEncoding) throws DdlUtilsException {
        DataWriter writer = new DataWriter(output, xmlEncoding);
        this.registerConverters(writer.getConverterConfiguration());
        return writer;
    }

    public void writeDataToXML(Platform platform, String path, String xmlEncoding) throws Throwable {
        this.writeDataToXML(platform, this.getConfiguredDataWriter(path, xmlEncoding));
    }

    public void writeDataToXML(Platform platform, Database model, String path, String xmlEncoding) throws Throwable {
        this.writeDataToXML(platform, model, this.getConfiguredDataWriter(path, xmlEncoding));
    }

    public void writeDataToXML(Platform platform, OutputStream output, String xmlEncoding) throws Throwable {
        this.writeDataToXML(platform, this.getConfiguredDataWriter(output, xmlEncoding));
    }

    public void writeDataToXML(Platform platform, Database model, OutputStream output, String xmlEncoding) throws Throwable {
        this.writeDataToXML(platform, model, this.getConfiguredDataWriter(output, xmlEncoding));
    }

    public void writeDataToXML(Platform platform, Writer output, String xmlEncoding) throws Throwable {
        this.writeDataToXML(platform, this.getConfiguredDataWriter(output, xmlEncoding));
    }

    public void writeDataToXML(Platform platform, Database model, Writer output, String xmlEncoding) throws Throwable {
        this.writeDataToXML(platform, model, this.getConfiguredDataWriter(output, xmlEncoding));
    }

    public void writeDataToXML(Platform platform, DataWriter writer) throws Throwable {
        this.writeDataToXML(platform, platform.readModelFromDatabase("unnamed"), writer);
    }

    public void writeDataToXML(Platform platform, Database model, DataWriter writer) throws Throwable {
        this.registerConverters(writer.getConverterConfiguration());
        List tables = this.sortTables(model.getTables());
        writer.writeDocumentStart();
        Iterator it = tables.iterator();
        while (it.hasNext()) {
            this.writeDataForTableToXML(platform, model, (Table)it.next(), writer);
        }
        writer.writeDocumentEnd();
    }

    private List sortTables(Table[] tables) {
        Iterator it;
        ArrayList<Table> result = new ArrayList<Table>();
        HashSet<Table> processed = new HashSet<Table>();
        ListOrderedMap pending = new ListOrderedMap();
        int idx = 0;
        while (idx < tables.length) {
            Table table = tables[idx];
            if (table.getForeignKeyCount() == 0) {
                result.add(table);
                processed.add(table);
            } else {
                HashSet<Table> waitedFor = new HashSet<Table>();
                int fkIdx = 0;
                while (fkIdx < table.getForeignKeyCount()) {
                    Table waitedForTable = table.getForeignKey(fkIdx).getForeignTable();
                    if (!table.equals(waitedForTable)) {
                        waitedFor.add(waitedForTable);
                    }
                    ++fkIdx;
                }
                pending.put((Object)table, waitedFor);
            }
            ++idx;
        }
        HashSet<Table> newProcessed = new HashSet<Table>();
        while (!processed.isEmpty() && !pending.isEmpty()) {
            newProcessed.clear();
            it = pending.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry)it.next();
                Table table = (Table)entry.getKey();
                HashSet waitedFor = (HashSet)entry.getValue();
                waitedFor.removeAll(processed);
                if (!waitedFor.isEmpty()) continue;
                it.remove();
                result.add(table);
                newProcessed.add(table);
            }
            processed.clear();
            HashSet<Table> tmp = processed;
            processed = newProcessed;
            newProcessed = tmp;
        }
        it = pending.keySet().iterator();
        while (it.hasNext()) {
            result.add((Table)it.next());
        }
        return result;
    }

    private void writeDataForTableToXML(Platform platform, Database model, Table table, DataWriter writer) throws Throwable {
        String schema;
        StringBuffer query;
        Table[] tables;
        block22: {
            tables = new Table[]{table};
            query = new StringBuffer();
            query.append("SELECT ");
            Connection connection = null;
            schema = null;
            if (this._determineSchema) {
                try {
                    try {
                        connection = platform.borrowConnection();
                        schema = platform.getModelReader().determineSchemaOf(connection, this._schemaPattern, tables[0]);
                    }
                    catch (SQLException sQLException) {
                        if (connection != null) {
                            try {
                                connection.close();
                            }
                            catch (SQLException sQLException2) {}
                        }
                        break block22;
                    }
                }
                catch (Throwable throwable) {
                    if (connection != null) {
                        try {
                            connection.close();
                        }
                        catch (SQLException sQLException) {
                            // empty catch block
                        }
                    }
                    throw throwable;
                }
                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException sQLException) {
                        // empty catch block
                    }
                }
            }
        }
        Column[] columns = tables[0].getColumns();
        int columnIdx = 0;
        while (columnIdx < columns.length) {
            if (columnIdx > 0) {
                query.append(",");
            }
            if (platform.isDelimitedIdentifierModeOn()) {
                query.append(platform.getPlatformInfo().getDelimiterToken());
            }
            query.append(columns[columnIdx].getName());
            if (platform.isDelimitedIdentifierModeOn()) {
                query.append(platform.getPlatformInfo().getDelimiterToken());
            }
            ++columnIdx;
        }
        query.append(" FROM ");
        if (platform.isDelimitedIdentifierModeOn()) {
            query.append(platform.getPlatformInfo().getDelimiterToken());
        }
        if (schema != null) {
            query.append(schema);
            query.append(".");
        }
        query.append(tables[0].getName());
        if (platform.isDelimitedIdentifierModeOn()) {
            query.append(platform.getPlatformInfo().getDelimiterToken());
        }
        writer.write(platform.query(model, query.toString(), tables));
    }

    public DataReader getConfiguredDataReader(Platform platform, Database model) throws DdlUtilsException {
        DataToDatabaseSink sink = new DataToDatabaseSink(platform, model);
        DataReader reader = new DataReader();
        sink.setHaltOnErrors(this._failOnError);
        sink.setEnsureForeignKeyOrder(this._ensureFKOrder);
        sink.setUseBatchMode(this._useBatchMode);
        if (this._batchSize != null) {
            sink.setBatchSize(this._batchSize);
        }
        reader.setModel(model);
        reader.setSink(sink);
        this.registerConverters(reader.getConverterConfiguration());
        return reader;
    }

    public void writeDataToDatabase(Platform platform, String[] files) throws DdlUtilsException {
        this.writeDataToDatabase(platform, platform.readModelFromDatabase("unnamed"), files);
    }

    public void writeDataToDatabase(Platform platform, InputStream[] inputs) throws DdlUtilsException {
        this.writeDataToDatabase(platform, platform.readModelFromDatabase("unnamed"), inputs);
    }

    public void writeDataToDatabase(Platform platform, Reader[] inputs) throws DdlUtilsException {
        this.writeDataToDatabase(platform, platform.readModelFromDatabase("unnamed"), inputs);
    }

    public void writeDataToDatabase(Platform platform, Database model, String[] files) throws DdlUtilsException {
        DataReader dataReader = this.getConfiguredDataReader(platform, model);
        dataReader.getSink().start();
        int idx = 0;
        while (files != null && idx < files.length) {
            this.writeDataToDatabase(dataReader, files[idx]);
            ++idx;
        }
        dataReader.getSink().end();
    }

    public void writeDataToDatabase(Platform platform, Database model, InputStream[] inputs) throws DdlUtilsException {
        DataReader dataReader = this.getConfiguredDataReader(platform, model);
        dataReader.getSink().start();
        int idx = 0;
        while (inputs != null && idx < inputs.length) {
            this.writeDataToDatabase(dataReader, inputs[idx]);
            ++idx;
        }
        dataReader.getSink().end();
    }

    public void writeDataToDatabase(Platform platform, Database model, Reader[] inputs) throws DdlUtilsException {
        DataReader dataReader = this.getConfiguredDataReader(platform, model);
        dataReader.getSink().start();
        int idx = 0;
        while (inputs != null && idx < inputs.length) {
            this.writeDataToDatabase(dataReader, inputs[idx]);
            ++idx;
        }
        dataReader.getSink().end();
    }

    public void writeDataToDatabase(DataReader dataReader, String[] files) throws DdlUtilsException {
        int idx = 0;
        while (files != null && idx < files.length) {
            this.writeDataToDatabase(dataReader, files[idx]);
            ++idx;
        }
    }

    public void writeDataToDatabase(DataReader dataReader, InputStream[] inputs) throws DdlUtilsException {
        int idx = 0;
        while (inputs != null && idx < inputs.length) {
            this.writeDataToDatabase(dataReader, inputs[idx]);
            ++idx;
        }
    }

    public void writeDataToDatabase(DataReader dataReader, Reader[] inputs) throws DdlUtilsException {
        int idx = 0;
        while (inputs != null && idx < inputs.length) {
            this.writeDataToDatabase(dataReader, inputs[idx]);
            ++idx;
        }
    }

    public void writeDataToDatabase(DataReader dataReader, String path) throws DdlUtilsException {
        try {
            dataReader.parse(path);
        }
        catch (Exception ex) {
            throw new DdlUtilsException(ex);
        }
    }

    public void writeDataToDatabase(DataReader dataReader, InputStream input) throws DdlUtilsException {
        try {
            dataReader.parse(input);
        }
        catch (Exception ex) {
            throw new DdlUtilsException(ex);
        }
    }

    public void writeDataToDatabase(DataReader dataReader, Reader input) throws DdlUtilsException {
        try {
            dataReader.parse(input);
        }
        catch (Exception ex) {
            throw new DdlUtilsException(ex);
        }
    }
}


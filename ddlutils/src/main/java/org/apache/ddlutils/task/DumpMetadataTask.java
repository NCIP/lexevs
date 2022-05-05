/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.collections.set.ListOrderedSet
 *  org.apache.commons.dbcp.BasicDataSource
 *  org.apache.tools.ant.BuildException
 *  org.apache.tools.ant.Task
 *  org.dom4j.Document
 *  org.dom4j.DocumentFactory
 *  org.dom4j.Element
 *  org.dom4j.io.OutputFormat
 *  org.dom4j.io.XMLWriter
 */
package org.apache.ddlutils.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class DumpMetadataTask
extends Task {
    private static final String[] IGNORED_PROPERTY_METHODS = new String[]{"getConnection", "getCatalogs", "getSchemas"};
    private BasicDataSource _dataSource;
    private File _outputFile = null;
    private String _outputEncoding = "UTF-8";
    private String _catalogPattern = "%";
    private String _schemaPattern = "%";
    private String _tablePattern = "%";
    private String _procedurePattern = "%";
    private String _columnPattern = "%";
    private String[] _tableTypes = null;
    private boolean _dumpTables = true;
    private boolean _dumpProcedures = true;

    public void addConfiguredDatabase(BasicDataSource dataSource) {
        this._dataSource = dataSource;
    }

    public void setOutputFile(File outputFile) {
        this._outputFile = outputFile;
    }

    public void setOutputEncoding(String encoding) {
        this._outputEncoding = encoding;
    }

    public void setCatalogPattern(String catalogPattern) {
        this._catalogPattern = catalogPattern == null || catalogPattern.length() == 0 ? null : catalogPattern;
    }

    public void setSchemaPattern(String schemaPattern) {
        this._schemaPattern = schemaPattern == null || schemaPattern.length() == 0 ? null : schemaPattern;
    }

    public void setTablePattern(String tablePattern) {
        this._tablePattern = tablePattern == null || tablePattern.length() == 0 ? null : tablePattern;
    }

    public void setProcedurePattern(String procedurePattern) {
        this._procedurePattern = procedurePattern == null || procedurePattern.length() == 0 ? null : procedurePattern;
    }

    public void setColumnPattern(String columnPattern) {
        this._columnPattern = columnPattern == null || columnPattern.length() == 0 ? null : columnPattern;
    }

    public void setTableTypes(String tableTypes) {
        ArrayList<String> types = new ArrayList<String>();
        if (tableTypes != null) {
            StringTokenizer tokenizer = new StringTokenizer(tableTypes, ",");
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken().trim();
                if (token.length() <= 0) continue;
                types.add(token);
            }
        }
        this._tableTypes = types.toArray(new String[types.size()]);
    }

    public void setDumpProcedures(boolean readProcedures) {
        this._dumpProcedures = readProcedures;
    }

    public void setDumpTables(boolean readTables) {
        this._dumpTables = readTables;
    }

    public void execute() throws BuildException {
        if (this._dataSource == null) {
            this.log("No data source specified, so there is nothing to do.", 2);
            return;
        }
        Connection connection = null;
        try {
            try {
                Document document = DocumentFactory.getInstance().createDocument();
                Element root = document.addElement("metadata");
                root.addAttribute("driverClassName", this._dataSource.getDriverClassName());
                connection = this._dataSource.getConnection();
                this.dumpMetaData(root, connection.getMetaData());
                OutputFormat outputFormat = OutputFormat.createPrettyPrint();
                XMLWriter xmlWriter = null;
                outputFormat.setEncoding(this._outputEncoding);
                xmlWriter = this._outputFile == null ? new XMLWriter((OutputStream)System.out, outputFormat) : new XMLWriter((OutputStream)new FileOutputStream(this._outputFile), outputFormat);
                xmlWriter.write(document);
                xmlWriter.close();
            }
            catch (Exception ex) {
                throw new BuildException((Throwable)ex);
            }
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException sQLException) {}
            }
        }
    }

    private void dumpMetaData(Element element, DatabaseMetaData metaData) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, SQLException {
        Method[] methods = metaData.getClass().getMethods();
        HashSet<String> filtered = new HashSet<String>(Arrays.asList(IGNORED_PROPERTY_METHODS));
        int idx = 0;
        while (idx < methods.length) {
            if (methods[idx].getParameterTypes().length == 0 && methods[idx].getReturnType() != null && Object.class != methods[idx].getDeclaringClass() && !filtered.contains(methods[idx].getName())) {
                this.dumpProperty(element, metaData, methods[idx]);
            }
            ++idx;
        }
        this.dumpCatalogsAndSchemas(element, metaData);
        if (this._dumpTables) {
            this.dumpTables(element, metaData);
        }
        if (this._dumpProcedures) {
            this.dumpProcedures(element, metaData);
        }
    }

    private void dumpProperty(Element parent, Object obj, Method propGetter) {
        try {
            this.addProperty(parent, this.getPropertyName(propGetter.getName()), propGetter.invoke(obj, null));
        }
        catch (Throwable ex) {
            this.log("Could not dump property " + propGetter.getName() + " because of " + ex.getMessage(), 1);
        }
    }

    private void addProperty(Element element, String name, Object value) {
        if (value != null) {
            if (value.getClass().isArray()) {
                this.addArrayProperty(element, name, (Object[])value);
            } else if (value.getClass().isPrimitive() || value instanceof String) {
                element.addAttribute(name, value.toString());
            } else if (value instanceof ResultSet) {
                this.addResultSetProperty(element, name, (ResultSet)value);
            }
        }
    }

    private void addArrayProperty(Element element, String name, Object[] values) {
        String propName = name;
        if (propName.endsWith("s")) {
            propName = propName.substring(0, propName.length() - 1);
        }
        Element arrayElem = element.addElement(String.valueOf(propName) + "s");
        int idx = 0;
        while (idx < values.length) {
            this.addProperty(arrayElem, "value", values[idx]);
            ++idx;
        }
    }

    private void addResultSetProperty(Element element, String name, ResultSet result) {
        try {
            String propName = name;
            if (propName.endsWith("s")) {
                propName = propName.substring(0, propName.length() - 1);
            }
            Element resultSetElem = element.addElement(String.valueOf(propName) + "s");
            ResultSetMetaData metaData = result.getMetaData();
            while (result.next()) {
                Element curRow = resultSetElem.addElement(propName);
                int idx = 1;
                while (idx <= metaData.getColumnCount()) {
                    Object value = result.getObject(idx);
                    this.addProperty(curRow, metaData.getColumnLabel(idx), value);
                    ++idx;
                }
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private String getPropertyName(String methodName) {
        if (methodName.startsWith("get")) {
            if (Character.isLowerCase(methodName.charAt(4))) {
                return String.valueOf(Character.toLowerCase(methodName.charAt(3))) + methodName.substring(4);
            }
            return methodName.substring(3);
        }
        if (methodName.startsWith("is")) {
            if (Character.isLowerCase(methodName.charAt(3))) {
                return String.valueOf(Character.toLowerCase(methodName.charAt(2))) + methodName.substring(3);
            }
            return methodName.substring(2);
        }
        return methodName;
    }

    private void dumpCatalogsAndSchemas(Element parent, DatabaseMetaData metaData) throws SQLException {
        Element catalogsElem = parent.addElement("catalogs");
        ResultSet result = metaData.getCatalogs();
        try {
            while (result.next()) {
                String catalogName = this.getString(result, "TABLE_CAT");
                if (catalogName == null || catalogName.length() <= 0) continue;
                Element catalogElem = catalogsElem.addElement("catalog");
                catalogElem.addAttribute("name", catalogName);
            }
        }
        finally {
            if (result != null) {
                result.close();
            }
        }
        Element schemasElem = parent.addElement("schemas");
        result = metaData.getSchemas();
        try {
            while (result.next()) {
                String schemaName = this.getString(result, "TABLE_SCHEM");
                if (schemaName == null || schemaName.length() <= 0) continue;
                Element schemaElem = schemasElem.addElement("schema");
                schemaElem.addAttribute("name", schemaName);
            }
        }
        finally {
            if (result != null) {
                result.close();
            }
        }
    }

    private void dumpTables(Element parent, DatabaseMetaData metaData) throws SQLException {
        String[] tableTypes = this._tableTypes;
        ResultSet result = null;
        if (tableTypes == null || tableTypes.length == 0) {
            ArrayList<String> tableTypeList = new ArrayList<String>();
            result = metaData.getTableTypes();
            try {
                while (result.next()) {
                    tableTypeList.add(this.getString(result, "TABLE_TYPE"));
                }
            }
            finally {
                if (result != null) {
                    result.close();
                }
            }
            tableTypes = tableTypeList.toArray(new String[tableTypeList.size()]);
        }
        try {
            result = metaData.getTables(this._catalogPattern, this._schemaPattern, this._tablePattern, tableTypes);
        }
        catch (SQLException ex) {
            this.log("Could not determine the tables: " + ex.getMessage(), 0);
            return;
        }
        Element tablesElem = parent.addElement("tables");
        Set columns = this.getColumnsInResultSet(result);
        try {
            while (result.next()) {
                String tableName = this.getString(result, "TABLE_NAME");
                if (tableName == null || tableName.length() == 0) continue;
                Element tableElem = tablesElem.addElement("table");
                String catalog = this.getString(result, "TABLE_CAT");
                String schema = this.getString(result, "TABLE_SCHEM");
                this.log("Reading table " + (schema != null && schema.length() > 0 ? String.valueOf(schema) + "." : "") + tableName, 2);
                tableElem.addAttribute("name", tableName);
                if (catalog != null) {
                    tableElem.addAttribute("catalog", catalog);
                }
                if (schema != null) {
                    tableElem.addAttribute("schema", schema);
                }
                this.addStringAttribute(result, columns, "TABLE_TYPE", tableElem, "type");
                this.addStringAttribute(result, columns, "REMARKS", tableElem, "remarks");
                this.addStringAttribute(result, columns, "TYPE_NAME", tableElem, "typeName");
                this.addStringAttribute(result, columns, "TYPE_CAT", tableElem, "typeCatalog");
                this.addStringAttribute(result, columns, "TYPE_SCHEM", tableElem, "typeSchema");
                this.addStringAttribute(result, columns, "SELF_REFERENCING_COL_NAME", tableElem, "identifierColumn");
                this.addStringAttribute(result, columns, "REF_GENERATION", tableElem, "identifierGeneration");
                this.dumpColumns(tableElem, metaData, catalog, schema, tableName);
                this.dumpPKs(tableElem, metaData, catalog, schema, tableName);
                this.dumpVersionColumns(tableElem, metaData, catalog, schema, tableName);
                this.dumpFKs(tableElem, metaData, catalog, schema, tableName);
                this.dumpIndices(tableElem, metaData, catalog, schema, tableName);
            }
        }
        finally {
            if (result != null) {
                result.close();
            }
        }
    }

    private void dumpColumns(Element tableElem, DatabaseMetaData metaData, String catalogName, String schemaName, String tableName) throws SQLException {
        ResultSet result = null;
        try {
            result = metaData.getColumns(catalogName, schemaName, tableName, this._columnPattern);
        }
        catch (SQLException ex) {
            this.log("Could not determine the columns for table '" + tableName + "': " + ex.getMessage(), 0);
            return;
        }
        Set columns = this.getColumnsInResultSet(result);
        try {
            while (result.next()) {
                String columnName = this.getString(result, "COLUMN_NAME");
                if (columnName == null || columnName.length() == 0) continue;
                Element columnElem = tableElem.addElement("column");
                columnElem.addAttribute("name", columnName);
                this.addIntAttribute(result, columns, "DATA_TYPE", columnElem, "typeCode");
                this.addStringAttribute(result, columns, "TYPE_NAME", columnElem, "type");
                this.addIntAttribute(result, columns, "COLUMN_SIZE", columnElem, "size");
                this.addIntAttribute(result, columns, "DECIMAL_DIGITS", columnElem, "digits");
                this.addIntAttribute(result, columns, "NUM_PREC_RADIX", columnElem, "precision");
                if (columns.contains("NULLABLE")) {
                    switch (result.getInt("NULLABLE")) {
                        case 0: {
                            columnElem.addAttribute("nullable", "false");
                            break;
                        }
                        case 1: {
                            columnElem.addAttribute("nullable", "true");
                            break;
                        }
                        default: {
                            columnElem.addAttribute("nullable", "unknown");
                        }
                    }
                }
                this.addStringAttribute(result, columns, "REMARKS", columnElem, "remarks");
                this.addStringAttribute(result, columns, "COLUMN_DEF", columnElem, "defaultValue");
                this.addIntAttribute(result, columns, "CHAR_OCTET_LENGTH", columnElem, "maxByteLength");
                this.addIntAttribute(result, columns, "ORDINAL_POSITION", columnElem, "index");
                if (columns.contains("IS_NULLABLE")) {
                    String value = this.getString(result, "IS_NULLABLE");
                    if ("no".equalsIgnoreCase(value)) {
                        columnElem.addAttribute("isNullable", "false");
                    } else if ("yes".equalsIgnoreCase(value)) {
                        columnElem.addAttribute("isNullable", "true");
                    } else {
                        columnElem.addAttribute("isNullable", "unknown");
                    }
                }
                this.addStringAttribute(result, columns, "SCOPE_CATLOG", columnElem, "refCatalog");
                this.addStringAttribute(result, columns, "SCOPE_SCHEMA", columnElem, "refSchema");
                this.addStringAttribute(result, columns, "SCOPE_TABLE", columnElem, "refTable");
                this.addShortAttribute(result, columns, "SOURCE_DATA_TYPE", columnElem, "sourceTypeCode");
            }
        }
        finally {
            if (result != null) {
                result.close();
            }
        }
    }

    private void dumpPKs(Element tableElem, DatabaseMetaData metaData, String catalogName, String schemaName, String tableName) throws SQLException {
        ResultSet result = null;
        try {
            result = metaData.getPrimaryKeys(catalogName, schemaName, tableName);
        }
        catch (SQLException ex) {
            this.log("Could not determine the primary key columns for table '" + tableName + "': " + ex.getMessage(), 0);
            return;
        }
        Set columns = this.getColumnsInResultSet(result);
        try {
            while (result.next()) {
                String columnName = this.getString(result, "COLUMN_NAME");
                if (columnName == null || columnName.length() == 0) continue;
                Element pkElem = tableElem.addElement("primaryKey");
                pkElem.addAttribute("column", columnName);
                this.addStringAttribute(result, columns, "PK_NAME", pkElem, "name");
                this.addShortAttribute(result, columns, "KEY_SEQ", pkElem, "sequenceNumberInPK");
            }
        }
        finally {
            if (result != null) {
                result.close();
            }
        }
    }

    private void dumpVersionColumns(Element tableElem, DatabaseMetaData metaData, String catalogName, String schemaName, String tableName) throws SQLException {
        ResultSet result = null;
        try {
            result = metaData.getVersionColumns(catalogName, schemaName, tableName);
        }
        catch (SQLException ex) {
            this.log("Could not determine the versioned columns for table '" + tableName + "': " + ex.getMessage(), 0);
            return;
        }
        Set columns = this.getColumnsInResultSet(result);
        try {
            while (result.next()) {
                String columnName = this.getString(result, "COLUMN_NAME");
                if (columnName == null || columnName.length() == 0) continue;
                Element columnElem = tableElem.addElement("versionedColumn");
                columnElem.addAttribute("column", columnName);
                this.addIntAttribute(result, columns, "DATA_TYPE", columnElem, "typeCode");
                this.addStringAttribute(result, columns, "TYPE_NAME", columnElem, "type");
                this.addIntAttribute(result, columns, "BUFFER_LENGTH", columnElem, "size");
                this.addIntAttribute(result, columns, "COLUMN_SIZE", columnElem, "precision");
                this.addShortAttribute(result, columns, "DECIMAL_DIGITS", columnElem, "scale");
                if (!columns.contains("PSEUDO_COLUMN")) continue;
                switch (result.getShort("PSEUDO_COLUMN")) {
                    case 2: {
                        columnElem.addAttribute("columnType", "pseudo column");
                        break;
                    }
                    case 1: {
                        columnElem.addAttribute("columnType", "real column");
                        break;
                    }
                    default: {
                        columnElem.addAttribute("columnType", "unknown");
                    }
                }
            }
        }
        finally {
            if (result != null) {
                result.close();
            }
        }
    }

    private void dumpFKs(Element tableElem, DatabaseMetaData metaData, String catalogName, String schemaName, String tableName) throws SQLException {
        ResultSet result = null;
        try {
            result = metaData.getImportedKeys(catalogName, schemaName, tableName);
        }
        catch (SQLException ex) {
            this.log("Could not determine the foreign keys for table '" + tableName + "': " + ex.getMessage(), 0);
            return;
        }
        Set columns = this.getColumnsInResultSet(result);
        try {
            while (result.next()) {
                Element fkElem = tableElem.addElement("foreignKey");
                this.addStringAttribute(result, columns, "FK_NAME", fkElem, "name");
                this.addStringAttribute(result, columns, "PK_NAME", fkElem, "primaryKeyName");
                this.addStringAttribute(result, columns, "PKCOLUMN_NAME", fkElem, "column");
                this.addStringAttribute(result, columns, "FKTABLE_CAT", fkElem, "foreignCatalog");
                this.addStringAttribute(result, columns, "FKTABLE_SCHEM", fkElem, "foreignSchema");
                this.addStringAttribute(result, columns, "FKTABLE_NAME", fkElem, "foreignTable");
                this.addStringAttribute(result, columns, "FKCOLUMN_NAME", fkElem, "foreignColumn");
                this.addShortAttribute(result, columns, "KEY_SEQ", fkElem, "sequenceNumberInFK");
                if (columns.contains("UPDATE_RULE")) {
                    switch (result.getShort("UPDATE_RULE")) {
                        case 3: {
                            fkElem.addAttribute("updateRule", "no action");
                            break;
                        }
                        case 0: {
                            fkElem.addAttribute("updateRule", "cascade PK change");
                            break;
                        }
                        case 2: {
                            fkElem.addAttribute("updateRule", "set FK to NULL");
                            break;
                        }
                        case 4: {
                            fkElem.addAttribute("updateRule", "set FK to default");
                            break;
                        }
                        default: {
                            fkElem.addAttribute("updateRule", "unknown");
                        }
                    }
                }
                if (columns.contains("DELETE_RULE")) {
                    switch (result.getShort("DELETE_RULE")) {
                        case 1: 
                        case 3: {
                            fkElem.addAttribute("deleteRule", "no action");
                            break;
                        }
                        case 0: {
                            fkElem.addAttribute("deleteRule", "cascade PK change");
                            break;
                        }
                        case 2: {
                            fkElem.addAttribute("deleteRule", "set FK to NULL");
                            break;
                        }
                        case 4: {
                            fkElem.addAttribute("deleteRule", "set FK to default");
                            break;
                        }
                        default: {
                            fkElem.addAttribute("deleteRule", "unknown");
                        }
                    }
                }
                if (!columns.contains("DEFERRABILITY")) continue;
                switch (result.getShort("DEFERRABILITY")) {
                    case 5: {
                        fkElem.addAttribute("deferrability", "initially deferred");
                        break;
                    }
                    case 6: {
                        fkElem.addAttribute("deferrability", "immediately deferred");
                        break;
                    }
                    case 7: {
                        fkElem.addAttribute("deferrability", "not deferred");
                        break;
                    }
                    default: {
                        fkElem.addAttribute("deferrability", "unknown");
                    }
                }
            }
        }
        finally {
            if (result != null) {
                result.close();
            }
        }
    }

    private void dumpIndices(Element tableElem, DatabaseMetaData metaData, String catalogName, String schemaName, String tableName) throws SQLException {
        ResultSet result = null;
        try {
            result = metaData.getIndexInfo(catalogName, schemaName, tableName, false, false);
        }
        catch (SQLException ex) {
            this.log("Could not determine the indices for table '" + tableName + "': " + ex.getMessage(), 0);
            return;
        }
        Set columns = this.getColumnsInResultSet(result);
        try {
            while (result.next()) {
                Element indexElem = tableElem.addElement("index");
                this.addStringAttribute(result, columns, "INDEX_NAME", indexElem, "name");
                this.addBooleanAttribute(result, columns, "NON_UNIQUE", indexElem, "nonUnique");
                this.addStringAttribute(result, columns, "INDEX_QUALIFIER", indexElem, "indexCatalog");
                if (columns.contains("TYPE")) {
                    switch (result.getShort("TYPE")) {
                        case 0: {
                            indexElem.addAttribute("type", "table statistics");
                            break;
                        }
                        case 1: {
                            indexElem.addAttribute("type", "clustered");
                            break;
                        }
                        case 2: {
                            indexElem.addAttribute("type", "hashed");
                            break;
                        }
                        case 3: {
                            indexElem.addAttribute("type", "other");
                            break;
                        }
                        default: {
                            indexElem.addAttribute("type", "unknown");
                        }
                    }
                }
                this.addStringAttribute(result, columns, "COLUMN_NAME", indexElem, "column");
                this.addShortAttribute(result, columns, "ORDINAL_POSITION", indexElem, "sequenceNumberInIndex");
                if (columns.contains("ASC_OR_DESC")) {
                    String value = this.getString(result, "ASC_OR_DESC");
                    if ("A".equalsIgnoreCase(value)) {
                        indexElem.addAttribute("sortOrder", "ascending");
                    } else if ("D".equalsIgnoreCase(value)) {
                        indexElem.addAttribute("sortOrder", "descending");
                    } else {
                        indexElem.addAttribute("sortOrder", "unknown");
                    }
                }
                this.addIntAttribute(result, columns, "CARDINALITY", indexElem, "cardinality");
                this.addIntAttribute(result, columns, "PAGES", indexElem, "pages");
                this.addStringAttribute(result, columns, "FILTER_CONDITION", indexElem, "filter");
            }
        }
        finally {
            if (result != null) {
                result.close();
            }
        }
    }

    private void dumpProcedures(Element parent, DatabaseMetaData metaData) throws SQLException {
        ResultSet result = null;
        try {
            result = metaData.getProcedures(this._catalogPattern, this._schemaPattern, this._procedurePattern);
        }
        catch (SQLException ex) {
            this.log("Could not determine the procedures: " + ex.getMessage(), 0);
            return;
        }
        Element proceduresElem = parent.addElement("procedures");
        Set columns = this.getColumnsInResultSet(result);
        try {
            while (result.next()) {
                String procedureName = this.getString(result, "PROCEDURE_NAME");
                if (procedureName == null || procedureName.length() == 0) continue;
                Element procedureElem = proceduresElem.addElement("procedure");
                String catalog = this.getString(result, "PROCEDURE_CAT");
                String schema = this.getString(result, "PROCEDURE_SCHEM");
                this.log("Reading procedure " + (schema != null && schema.length() > 0 ? String.valueOf(schema) + "." : "") + procedureName, 2);
                procedureElem.addAttribute("name", procedureName);
                if (catalog != null) {
                    procedureElem.addAttribute("catalog", catalog);
                }
                if (schema != null) {
                    procedureElem.addAttribute("schema", schema);
                }
                this.addStringAttribute(result, columns, "REMARKS", procedureElem, "remarks");
                if (columns.contains("PROCEDURE_TYPE")) {
                    switch (result.getShort("PROCEDURE_TYPE")) {
                        case 2: {
                            procedureElem.addAttribute("type", "returns result");
                            break;
                        }
                        case 1: {
                            procedureElem.addAttribute("type", "doesn't return result");
                            break;
                        }
                        case 0: {
                            procedureElem.addAttribute("type", "may return result");
                            break;
                        }
                        default: {
                            procedureElem.addAttribute("type", "unknown");
                        }
                    }
                }
                this.dumpProcedure(procedureElem, metaData, "%", "%", procedureName);
            }
        }
        finally {
            if (result != null) {
                result.close();
            }
        }
    }

    private void dumpProcedure(Element procedureElem, DatabaseMetaData metaData, String catalogName, String schemaName, String procedureName) throws SQLException {
        ResultSet result = null;
        try {
            result = metaData.getProcedureColumns(catalogName, schemaName, procedureName, this._columnPattern);
        }
        catch (SQLException ex) {
            this.log("Could not determine the columns for procedure '" + procedureName + "': " + ex.getMessage(), 0);
            return;
        }
        Set columns = this.getColumnsInResultSet(result);
        try {
            while (result.next()) {
                String columnName = this.getString(result, "COLUMN_NAME");
                if (columnName == null || columnName.length() == 0) continue;
                Element columnElem = procedureElem.addElement("column");
                columnElem.addAttribute("name", columnName);
                if (columns.contains("COLUMN_TYPE")) {
                    switch (result.getShort("COLUMN_TYPE")) {
                        case 1: {
                            columnElem.addAttribute("type", "in parameter");
                            break;
                        }
                        case 2: {
                            columnElem.addAttribute("type", "in/out parameter");
                            break;
                        }
                        case 4: {
                            columnElem.addAttribute("type", "out parameter");
                            break;
                        }
                        case 5: {
                            columnElem.addAttribute("type", "return value");
                            break;
                        }
                        case 3: {
                            columnElem.addAttribute("type", "result column in ResultSet");
                            break;
                        }
                        default: {
                            columnElem.addAttribute("type", "unknown");
                        }
                    }
                }
                this.addIntAttribute(result, columns, "DATA_TYPE", columnElem, "typeCode");
                this.addStringAttribute(result, columns, "TYPE_NAME", columnElem, "type");
                this.addIntAttribute(result, columns, "LENGTH", columnElem, "length");
                this.addIntAttribute(result, columns, "PRECISION", columnElem, "precision");
                this.addShortAttribute(result, columns, "SCALE", columnElem, "short");
                this.addShortAttribute(result, columns, "RADIX", columnElem, "radix");
                if (columns.contains("NULLABLE")) {
                    switch (result.getInt("NULLABLE")) {
                        case 0: {
                            columnElem.addAttribute("nullable", "false");
                            break;
                        }
                        case 1: {
                            columnElem.addAttribute("nullable", "true");
                            break;
                        }
                        default: {
                            columnElem.addAttribute("nullable", "unknown");
                        }
                    }
                }
                this.addStringAttribute(result, columns, "REMARKS", columnElem, "remarks");
            }
        }
        finally {
            if (result != null) {
                result.close();
            }
        }
    }

    private String addStringAttribute(ResultSet result, Set columns, String columnName, Element element, String attrName) throws SQLException {
        String value = null;
        if (columns.contains(columnName)) {
            value = this.getString(result, columnName);
            element.addAttribute(attrName, value);
        }
        return value;
    }

    private String addIntAttribute(ResultSet result, Set columns, String columnName, Element element, String attrName) throws SQLException {
        String value = null;
        if (columns.contains(columnName)) {
            block5: {
                try {
                    value = String.valueOf(result.getInt(columnName));
                }
                catch (SQLException ex) {
                    value = result.getString(columnName);
                    if (value == null) break block5;
                    try {
                        Integer.parseInt(value);
                    }
                    catch (NumberFormatException parseEx) {
                        throw ex;
                    }
                }
            }
            element.addAttribute(attrName, value);
        }
        return value;
    }

    private String addShortAttribute(ResultSet result, Set columns, String columnName, Element element, String attrName) throws SQLException {
        String value = null;
        if (columns.contains(columnName)) {
            block5: {
                try {
                    value = String.valueOf(result.getShort(columnName));
                }
                catch (SQLException ex) {
                    value = result.getString(columnName);
                    if (value == null) break block5;
                    try {
                        Short.parseShort(value);
                    }
                    catch (NumberFormatException parseEx) {
                        throw ex;
                    }
                }
            }
            element.addAttribute(attrName, value);
        }
        return value;
    }

    private String addBooleanAttribute(ResultSet result, Set columns, String columnName, Element element, String attrName) throws SQLException {
        String value = null;
        if (columns.contains(columnName)) {
            value = String.valueOf(result.getBoolean(columnName));
            element.addAttribute(attrName, value);
        }
        return value;
    }

    private String getString(ResultSet result, String columnName) throws SQLException {
        return result.getString(columnName);
    }

    private Set getColumnsInResultSet(ResultSet resultSet) throws SQLException {
        ListOrderedSet result = new ListOrderedSet();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int idx = 1;
        while (idx <= metaData.getColumnCount()) {
            result.add((Object)metaData.getColumnName(idx).toUpperCase());
            ++idx;
        }
        return result;
    }
}


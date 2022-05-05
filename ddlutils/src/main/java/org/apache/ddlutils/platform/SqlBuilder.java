/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.collections.Closure
 *  org.apache.commons.collections.CollectionUtils
 *  org.apache.commons.collections.Predicate
 *  org.apache.commons.collections.map.ListOrderedMap
 *  org.apache.commons.lang.StringUtils
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 */
package org.apache.ddlutils.platform;

import java.io.IOException;
import java.io.Writer;
import java.rmi.server.UID;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.alteration.AddColumnChange;
import org.apache.ddlutils.alteration.AddForeignKeyChange;
import org.apache.ddlutils.alteration.AddIndexChange;
import org.apache.ddlutils.alteration.AddPrimaryKeyChange;
import org.apache.ddlutils.alteration.AddTableChange;
import org.apache.ddlutils.alteration.ColumnAutoIncrementChange;
import org.apache.ddlutils.alteration.ColumnDataTypeChange;
import org.apache.ddlutils.alteration.ColumnDefaultValueChange;
import org.apache.ddlutils.alteration.ColumnOrderChange;
import org.apache.ddlutils.alteration.ColumnRequiredChange;
import org.apache.ddlutils.alteration.ColumnSizeChange;
import org.apache.ddlutils.alteration.ModelChange;
import org.apache.ddlutils.alteration.ModelComparator;
import org.apache.ddlutils.alteration.PrimaryKeyChange;
import org.apache.ddlutils.alteration.RemoveColumnChange;
import org.apache.ddlutils.alteration.RemoveForeignKeyChange;
import org.apache.ddlutils.alteration.RemoveIndexChange;
import org.apache.ddlutils.alteration.RemovePrimaryKeyChange;
import org.apache.ddlutils.alteration.RemoveTableChange;
import org.apache.ddlutils.alteration.TableChange;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.IndexColumn;
import org.apache.ddlutils.model.ModelException;
import org.apache.ddlutils.model.Reference;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.platform.CreationParameters;
import org.apache.ddlutils.platform.DefaultValueHelper;
import org.apache.ddlutils.util.CallbackClosure;
import org.apache.ddlutils.util.MultiInstanceofPredicate;

public abstract class SqlBuilder {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    protected static final String SIZE_PLACEHOLDER = "{0}";
    protected final Log _log = LogFactory.getLog(SqlBuilder.class);
    private Platform _platform;
    private Writer _writer;
    private String _indent = "    ";
    private String _valueLocale;
    private DateFormat _valueDateFormat;
    private DateFormat _valueTimeFormat;
    private NumberFormat _valueNumberFormat;
    private DefaultValueHelper _defaultValueHelper = new DefaultValueHelper();
    private Map<String,String> _charSequencesToEscape = new ListOrderedMap();

    public SqlBuilder(Platform platform) {
        this._platform = platform;
    }

    public Platform getPlatform() {
        return this._platform;
    }

    public PlatformInfo getPlatformInfo() {
        return this._platform.getPlatformInfo();
    }

    public Writer getWriter() {
        return this._writer;
    }

    public void setWriter(Writer writer) {
        this._writer = writer;
    }

    public DefaultValueHelper getDefaultValueHelper() {
        return this._defaultValueHelper;
    }

    public String getIndent() {
        return this._indent;
    }

    public void setIndent(String indent) {
        this._indent = indent;
    }

    public String getValueLocale() {
        return this._valueLocale;
    }

    public void setValueLocale(String localeStr) {
        if (localeStr != null) {
            int sepPos = localeStr.indexOf(95);
            String language = null;
            String country = null;
            String variant = null;
            if (sepPos > 0) {
                language = localeStr.substring(0, sepPos);
                country = localeStr.substring(sepPos + 1);
                if ((sepPos = country.indexOf(95)) > 0) {
                    variant = country.substring(sepPos + 1);
                    country = country.substring(0, sepPos);
                }
            } else {
                language = localeStr;
            }
            if (language != null) {
                Locale locale = null;
                locale = variant != null ? new Locale(language, country, variant) : (country != null ? new Locale(language, country) : new Locale(language));
                this._valueLocale = localeStr;
                this.setValueDateFormat(DateFormat.getDateInstance(3, locale));
                this.setValueTimeFormat(DateFormat.getTimeInstance(3, locale));
                this.setValueNumberFormat(NumberFormat.getNumberInstance(locale));
                return;
            }
        }
        this._valueLocale = null;
        this.setValueDateFormat(null);
        this.setValueTimeFormat(null);
        this.setValueNumberFormat(null);
    }

    protected DateFormat getValueDateFormat() {
        return this._valueDateFormat;
    }

    protected void setValueDateFormat(DateFormat format) {
        this._valueDateFormat = format;
    }

    protected DateFormat getValueTimeFormat() {
        return this._valueTimeFormat;
    }

    protected void setValueTimeFormat(DateFormat format) {
        this._valueTimeFormat = format;
    }

    protected NumberFormat getValueNumberFormat() {
        return this._valueNumberFormat;
    }

    protected void setValueNumberFormat(NumberFormat format) {
        this._valueNumberFormat = format;
    }

    protected void addEscapedCharSequence(String charSequence, String escapedVersion) {
        this._charSequencesToEscape.put(charSequence, escapedVersion);
    }

    public int getMaxTableNameLength() {
        return this.getPlatformInfo().getMaxTableNameLength();
    }

    public int getMaxColumnNameLength() {
        return this.getPlatformInfo().getMaxColumnNameLength();
    }

    public int getMaxConstraintNameLength() {
        return this.getPlatformInfo().getMaxConstraintNameLength();
    }

    public int getMaxForeignKeyNameLength() {
        return this.getPlatformInfo().getMaxForeignKeyNameLength();
    }

    public void createTables(Database database) throws IOException {
        this.createTables(database, null, true);
    }

    public void createTables(Database database, boolean dropTables) throws IOException {
        this.createTables(database, null, dropTables);
    }

    public void createTables(Database database, CreationParameters params, boolean dropTables) throws IOException {
        if (dropTables) {
            this.dropTables(database);
        }
        int idx = 0;
        while (idx < database.getTableCount()) {
            Table table = database.getTable(idx);
            this.writeTableComment(table);
            this.createTable(database, table, params == null ? null : params.getParametersFor(table));
            ++idx;
        }
        this.createExternalForeignKeys(database);
    }

    public void alterDatabase(Database currentModel, Database desiredModel, CreationParameters params) throws IOException {
        ModelComparator comparator = new ModelComparator(this.getPlatformInfo(), this.getPlatform().isDelimitedIdentifierModeOn());
        List changes = comparator.compare(currentModel, desiredModel);
        this.processChanges(currentModel, desiredModel, changes, params);
    }

    protected void applyForSelectedChanges(Collection changes, Class[] changeTypes, final Closure closure) {
        final MultiInstanceofPredicate predicate = new MultiInstanceofPredicate(changeTypes);
        CollectionUtils.filter((Collection)changes, (Predicate)new Predicate(){

            public boolean evaluate(Object obj) {
                if (predicate.evaluate(obj)) {
                    closure.execute(obj);
                    return false;
                }
                return true;
            }
        });
    }

    protected void processChanges(Database currentModel, Database desiredModel, List changes, CreationParameters params) throws IOException {
        Class[] classArray = new Class[4];
        classArray[0] = Database.class;
        classArray[1] = Database.class;
        classArray[2] = CreationParameters.class;
        Object[] objectArray = new Object[4];
        objectArray[0] = currentModel;
        objectArray[1] = desiredModel;
        objectArray[2] = params;
        CallbackClosure callbackClosure = new CallbackClosure(this, "processChange", classArray, objectArray);
        this.applyForSelectedChanges(changes, new Class[]{RemoveForeignKeyChange.class, RemoveIndexChange.class}, callbackClosure);
        this.applyForSelectedChanges(changes, new Class[]{RemoveTableChange.class}, callbackClosure);
        MultiInstanceofPredicate predicate = new MultiInstanceofPredicate(new Class[]{RemovePrimaryKeyChange.class, AddPrimaryKeyChange.class, PrimaryKeyChange.class, RemoveColumnChange.class, AddColumnChange.class, ColumnOrderChange.class, ColumnAutoIncrementChange.class, ColumnDefaultValueChange.class, ColumnRequiredChange.class, ColumnDataTypeChange.class, ColumnSizeChange.class});
        this.processTableStructureChanges(currentModel, desiredModel, params, CollectionUtils.select((Collection)changes, (Predicate)predicate));
        this.applyForSelectedChanges(changes, new Class[]{AddTableChange.class}, callbackClosure);
        this.applyForSelectedChanges(changes, new Class[]{AddForeignKeyChange.class, AddIndexChange.class}, callbackClosure);
    }

    protected void processChange(Database currentModel, Database desiredModel, CreationParameters params, ModelChange change) throws IOException {
        this._log.warn((Object)("Change of type " + change.getClass() + " was not handled"));
    }

    protected void processChange(Database currentModel, Database desiredModel, CreationParameters params, RemoveForeignKeyChange change) throws IOException {
        this.writeExternalForeignKeyDropStmt(change.getChangedTable(), change.getForeignKey());
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processChange(Database currentModel, Database desiredModel, CreationParameters params, RemoveIndexChange change) throws IOException {
        this.writeExternalIndexDropStmt(change.getChangedTable(), change.getIndex());
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processChange(Database currentModel, Database desiredModel, CreationParameters params, RemoveTableChange change) throws IOException {
        this.dropTable(change.getChangedTable());
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processChange(Database currentModel, Database desiredModel, CreationParameters params, AddTableChange change) throws IOException {
        this.createTable(desiredModel, change.getNewTable(), params == null ? null : params.getParametersFor(change.getNewTable()));
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processChange(Database currentModel, Database desiredModel, CreationParameters params, AddForeignKeyChange change) throws IOException {
        this.writeExternalForeignKeyCreateStmt(desiredModel, change.getChangedTable(), change.getNewForeignKey());
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processChange(Database currentModel, Database desiredModel, CreationParameters params, AddIndexChange change) throws IOException {
        this.writeExternalIndexCreateStmt(change.getChangedTable(), change.getNewIndex());
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected void processTableStructureChanges(Database currentModel, Database desiredModel, CreationParameters params, Collection<TableChange> changes) throws IOException {
        Table targetTable;
        ListOrderedMap changesPerTable = new ListOrderedMap();
        ListOrderedMap unchangedFKs = new ListOrderedMap();
        boolean caseSensitive = this.getPlatform().isDelimitedIdentifierModeOn();
        for (TableChange change : changes) {
            ArrayList<TableChange> changesForTable;
            String name = change.getChangedTable().getName();
            if (!caseSensitive) {
                name = name.toUpperCase();
            }
            if ((changesForTable = (ArrayList<TableChange>)changesPerTable.get((Object)name)) == null) {
                changesForTable = new ArrayList<TableChange>();
                changesPerTable.put((Object)name, changesForTable);
                unchangedFKs.put(name, this.getUnchangedForeignKeys(currentModel, desiredModel, name));
            }
            changesForTable.add(change);
        }
        this.addRelevantFKsFromUnchangedTables(currentModel, desiredModel, changesPerTable.keySet(), (Map)unchangedFKs);
        for (Object entryObject : unchangedFKs.entrySet()) {
            if(entryObject instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry)entryObject;
            Table targetTable2 = desiredModel.findTable((String)entry.getKey(), caseSensitive);
            Iterator fkIt = ((List)entry.getValue()).iterator();
            while (fkIt.hasNext()) {
                this.writeExternalForeignKeyDropStmt(targetTable2, (ForeignKey)fkIt.next());
            }}
        }
        Database copyOfCurrentModel = null;
        try {
            copyOfCurrentModel = (Database)currentModel.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new DdlUtilsException(ex);
        }
        for (Object entryObject : changesPerTable.entrySet()) {
            if(entryObject instanceof Map.Entry){
                Map.Entry entry = (Map.Entry)entryObject;
            targetTable = desiredModel.findTable((String)entry.getKey(), caseSensitive);
            this.processTableStructureChanges(copyOfCurrentModel, desiredModel, (String)entry.getKey(), params == null ? null : params.getParametersFor(targetTable), (List)entry.getValue());
        }}
        for (Object entryObject : unchangedFKs.entrySet()) {
            if(entryObject instanceof Map.Entry){
                Map.Entry entry = (Map.Entry)entryObject;
            targetTable = desiredModel.findTable((String)entry.getKey(), caseSensitive);
            Iterator fkIt = ((List)entry.getValue()).iterator();
            while (fkIt.hasNext()) {
                this.writeExternalForeignKeyCreateStmt(desiredModel, targetTable, (ForeignKey)fkIt.next());
            }}
        }
    }

    private List getUnchangedForeignKeys(Database currentModel, Database desiredModel, String tableName) {
        ArrayList<ForeignKey> unchangedFKs = new ArrayList<ForeignKey>();
        boolean caseSensitive = this.getPlatform().isDelimitedIdentifierModeOn();
        Table sourceTable = currentModel.findTable(tableName, caseSensitive);
        Table targetTable = desiredModel.findTable(tableName, caseSensitive);
        int idx = 0;
        while (idx < targetTable.getForeignKeyCount()) {
            ForeignKey targetFK = targetTable.getForeignKey(idx);
            ForeignKey sourceFK = sourceTable.findForeignKey(targetFK, caseSensitive);
            if (sourceFK != null) {
                unchangedFKs.add(targetFK);
            }
            ++idx;
        }
        return unchangedFKs;
    }

    private void addRelevantFKsFromUnchangedTables(Database currentModel, Database desiredModel, Set namesOfKnownChangedTables, Map fksPerTable) {
        boolean caseSensitive = this.getPlatform().isDelimitedIdentifierModeOn();
        int tableIdx = 0;
        while (tableIdx < desiredModel.getTableCount()) {
            Table targetTable = desiredModel.getTable(tableIdx);
            String name = targetTable.getName();
            Table sourceTable = currentModel.findTable(name, caseSensitive);
            ArrayList<ForeignKey> relevantFks = null;
            if (!caseSensitive) {
                name = name.toUpperCase();
            }
            if (sourceTable != null && !namesOfKnownChangedTables.contains(name)) {
                int fkIdx = 0;
                while (fkIdx < targetTable.getForeignKeyCount()) {
                    ForeignKey targetFk = targetTable.getForeignKey(fkIdx);
                    ForeignKey sourceFk = sourceTable.findForeignKey(targetFk, caseSensitive);
                    String refName = targetFk.getForeignTableName();
                    if (!caseSensitive) {
                        refName = refName.toUpperCase();
                    }
                    if (sourceFk != null && namesOfKnownChangedTables.contains(refName)) {
                        if (relevantFks == null) {
                            relevantFks = new ArrayList<ForeignKey>();
                            fksPerTable.put(name, relevantFks);
                        }
                        relevantFks.add(targetFk);
                    }
                    ++fkIdx;
                }
            }
            ++tableIdx;
        }
    }

    protected void processTableStructureChanges(Database currentModel, Database desiredModel, String tableName, Map parameters, List changes) throws IOException {
        Table sourceTable = currentModel.findTable(tableName, this.getPlatform().isDelimitedIdentifierModeOn());
        Table targetTable = desiredModel.findTable(tableName, this.getPlatform().isDelimitedIdentifierModeOn());
        boolean requiresFullRebuild = false;
        Iterator changeIt = changes.iterator();
        while (!requiresFullRebuild && changeIt.hasNext()) {
            AddColumnChange addColumnChange;
            TableChange change = (TableChange)changeIt.next();
            if (!(change instanceof AddColumnChange) || !(addColumnChange = (AddColumnChange)change).getNewColumn().isRequired() || addColumnChange.getNewColumn().getDefaultValue() != null || addColumnChange.getNewColumn().isAutoIncrement()) continue;
            requiresFullRebuild = true;
        }
        if (!requiresFullRebuild) {
            this.processTableStructureChanges(currentModel, desiredModel, sourceTable, targetTable, parameters, changes);
        }
        if (!changes.isEmpty()) {
            boolean canMigrateData = true;
            Iterator it = changes.iterator();
            while (canMigrateData && it.hasNext()) {
                AddColumnChange addColumnChange;
                TableChange change = (TableChange)it.next();
                if (!(change instanceof AddColumnChange) || !(addColumnChange = (AddColumnChange)change).getNewColumn().isRequired() || addColumnChange.getNewColumn().isAutoIncrement() || addColumnChange.getNewColumn().getDefaultValue() != null) continue;
                this._log.warn((Object)("Data cannot be retained in table " + change.getChangedTable().getName() + " because of the addition of the required column " + addColumnChange.getNewColumn().getName()));
                canMigrateData = false;
            }
            Table realTargetTable = this.getRealTargetTableFor(desiredModel, sourceTable, targetTable);
            if (canMigrateData) {
                Table tempTable = this.getTemporaryTableFor(desiredModel, targetTable);
                this.createTemporaryTable(desiredModel, tempTable, parameters);
                this.writeCopyDataStatement(sourceTable, tempTable);
                this.dropTable(sourceTable);
                this.createTable(desiredModel, realTargetTable, parameters);
                this.writeCopyDataStatement(tempTable, targetTable);
                this.dropTemporaryTable(desiredModel, tempTable);
            } else {
                this.dropTable(sourceTable);
                this.createTable(desiredModel, realTargetTable, parameters);
            }
        }
    }

    protected void processTableStructureChanges(Database currentModel, Database desiredModel, Table sourceTable, Table targetTable, Map parameters, List changes) throws IOException {
        TableChange change;
        if (changes.size() == 1 && (change = (TableChange)changes.get(0)) instanceof AddPrimaryKeyChange) {
            this.processChange(currentModel, desiredModel, (AddPrimaryKeyChange)change);
            changes.clear();
        }
    }

    protected Table getTemporaryTableFor(Database targetModel, Table targetTable) throws IOException {
        Table table = new Table();
        table.setCatalog(targetTable.getCatalog());
        table.setSchema(targetTable.getSchema());
        table.setName(String.valueOf(targetTable.getName()) + "_");
        table.setType(targetTable.getType());
        int idx = 0;
        while (idx < targetTable.getColumnCount()) {
            try {
                table.addColumn((Column)targetTable.getColumn(idx).clone());
            }
            catch (CloneNotSupportedException ex) {
                throw new DdlUtilsException(ex);
            }
            ++idx;
        }
        return table;
    }

    protected void createTemporaryTable(Database database, Table table, Map parameters) throws IOException {
        this.createTable(database, table, parameters);
    }

    protected void dropTemporaryTable(Database database, Table table) throws IOException {
        this.dropTable(table);
    }

    protected Table getRealTargetTableFor(Database targetModel, Table sourceTable, Table targetTable) throws IOException {
        Table table = new Table();
        table.setCatalog(targetTable.getCatalog());
        table.setSchema(targetTable.getSchema());
        table.setName(targetTable.getName());
        table.setType(targetTable.getType());
        int idx = 0;
        while (idx < targetTable.getColumnCount()) {
            try {
                table.addColumn((Column)targetTable.getColumn(idx).clone());
            }
            catch (CloneNotSupportedException ex) {
                throw new DdlUtilsException(ex);
            }
            ++idx;
        }
        boolean caseSensitive = this.getPlatform().isDelimitedIdentifierModeOn();
        int idx2 = 0;
        while (idx2 < targetTable.getIndexCount()) {
            Index targetIndex = targetTable.getIndex(idx2);
            Index sourceIndex = sourceTable.findIndex(targetIndex.getName(), caseSensitive);
            if (sourceIndex != null && (caseSensitive && sourceIndex.equals(targetIndex) || !caseSensitive && sourceIndex.equalsIgnoreCase(targetIndex))) {
                table.addIndex(targetIndex);
            }
            ++idx2;
        }
        return table;
    }

    protected void writeCopyDataStatement(Table sourceTable, Table targetTable) throws IOException {
        ListOrderedMap columns = new ListOrderedMap();
        int idx = 0;
        while (idx < sourceTable.getColumnCount()) {
            Column sourceColumn = sourceTable.getColumn(idx);
            Column targetColumn = targetTable.findColumn(sourceColumn.getName(), this.getPlatform().isDelimitedIdentifierModeOn());
            if (targetColumn != null) {
                columns.put((Object)sourceColumn, (Object)targetColumn);
            }
            ++idx;
        }
        this.print("INSERT INTO ");
        this.printIdentifier(this.getTableName(targetTable));
        this.print(" (");
        Iterator columnIt = columns.keySet().iterator();
        while (columnIt.hasNext()) {
            this.printIdentifier(this.getColumnName((Column)columnIt.next()));
            if (!columnIt.hasNext()) continue;
            this.print(",");
        }
        this.print(") SELECT ");
        Iterator columnsIt = columns.entrySet().iterator();
        while (columnsIt.hasNext()) {
            Map.Entry entry = (Map.Entry)columnsIt.next();
            this.writeCastExpression((Column)entry.getKey(), (Column)entry.getValue());
            if (!columnsIt.hasNext()) continue;
            this.print(",");
        }
        this.print(" FROM ");
        this.printIdentifier(this.getTableName(sourceTable));
        this.printEndOfStatement();
    }

    protected void writeCastExpression(Column sourceColumn, Column targetColumn) throws IOException {
        this.printIdentifier(this.getColumnName(sourceColumn));
    }

    protected void processChange(Database currentModel, Database desiredModel, AddPrimaryKeyChange change) throws IOException {
        this.writeExternalPrimaryKeysCreateStmt(change.getChangedTable(), change.getPrimaryKeyColumns());
        change.apply(currentModel, this.getPlatform().isDelimitedIdentifierModeOn());
    }

    protected ForeignKey findCorrespondingForeignKey(Table table, ForeignKey fk) {
        boolean caseMatters = this.getPlatform().isDelimitedIdentifierModeOn();
        boolean checkFkName = fk.getName() != null && fk.getName().length() > 0;
        Reference[] refs = fk.getReferences();
        ArrayList curRefs = new ArrayList();
        int fkIdx = 0;
        while (fkIdx < table.getForeignKeyCount()) {
            boolean checkCurFkName;
            ForeignKey curFk = table.getForeignKey(fkIdx);
            boolean bl = checkCurFkName = checkFkName && curFk.getName() != null && curFk.getName().length() > 0;
            if ((!checkCurFkName || this.areEqual(fk.getName(), curFk.getName(), caseMatters)) && this.areEqual(fk.getForeignTableName(), curFk.getForeignTableName(), caseMatters)) {
                curRefs.clear();
                CollectionUtils.addAll(curRefs, (Object[])curFk.getReferences());
                if (curRefs.size() == refs.length) {
                    int refIdx = 0;
                    while (refIdx < refs.length) {
                        boolean found = false;
                        int curRefIdx = 0;
                        while (!found && curRefIdx < curRefs.size()) {
                            Reference curRef = (Reference)curRefs.get(curRefIdx);
                            if (caseMatters && refs[refIdx].equals(curRef) || !caseMatters && refs[refIdx].equalsIgnoreCase(curRef)) {
                                curRefs.remove(curRefIdx);
                                found = true;
                            }
                            ++curRefIdx;
                        }
                        ++refIdx;
                    }
                    if (curRefs.isEmpty()) {
                        return curFk;
                    }
                }
            }
            ++fkIdx;
        }
        return null;
    }

    protected boolean areEqual(String string1, String string2, boolean caseMatters) {
        return caseMatters && string1.equals(string2) || !caseMatters && string1.equalsIgnoreCase(string2);
    }

    public void createTable(Database database, Table table) throws IOException {
        this.createTable(database, table, null);
    }

    public void createTable(Database database, Table table, Map parameters) throws IOException {
        this.writeTableCreationStmt(database, table, parameters);
        this.writeTableCreationStmtEnding(table, parameters);
        if (!this.getPlatformInfo().isPrimaryKeyEmbedded()) {
            this.writeExternalPrimaryKeysCreateStmt(table, table.getPrimaryKeyColumns());
        }
        if (!this.getPlatformInfo().isIndicesEmbedded()) {
            this.writeExternalIndicesCreateStmt(table);
        }
    }

    public void createExternalForeignKeys(Database database) throws IOException {
        int idx = 0;
        while (idx < database.getTableCount()) {
            this.createExternalForeignKeys(database, database.getTable(idx));
            ++idx;
        }
    }

    public void createExternalForeignKeys(Database database, Table table) throws IOException {
        if (!this.getPlatformInfo().isForeignKeysEmbedded()) {
            int idx = 0;
            while (idx < table.getForeignKeyCount()) {
                this.writeExternalForeignKeyCreateStmt(database, table, table.getForeignKey(idx));
                ++idx;
            }
        }
    }

    public void dropTables(Database database) throws IOException {
        int idx = database.getTableCount() - 1;
        while (idx >= 0) {
            Table table = database.getTable(idx);
            if (table.getName() != null && table.getName().length() > 0) {
                this.dropExternalForeignKeys(table);
            }
            --idx;
        }
        for (Table table : SqlBuilder.sortTablesForDrop(database)) {
            if (table.getName() == null || table.getName().length() <= 0) continue;
            this.writeTableComment(table);
            this.dropTable(table);
        }
    }

    public static void main(String[] args) {
        Database db = new Database();
        Table table1 = new Table();
        table1.setName("1");
        Table table2 = new Table();
        table2.setName("2");
        Table table3 = new Table();
        table3.setName("3");
        ForeignKey fk1 = new ForeignKey();
        fk1.setForeignTable(table2);
        table1.addForeignKey(fk1);
        ForeignKey fk2 = new ForeignKey();
        fk2.setForeignTable(table3);
        table2.addForeignKey(fk2);
        db.addTables(Arrays.asList(table2, table1, table3));
        List<Table> tables = SqlBuilder.sortTablesForDrop(db);
        for (Table table : tables) {
            System.out.println(table.getName());
        }
    }

    private static List<Table> sortTablesForDrop(Database database) {
        LinkedList<Table> linkedList = new LinkedList<Table>();
        Table[] tableArray = database.getTables();
        int n = tableArray.length;
        int n2 = 0;
        while (n2 < n) {
            Table t = tableArray[n2];
            int pos = SqlBuilder.insertTable(t, linkedList);
            linkedList.add(pos, t);
            ++n2;
        }
        return linkedList;
    }

    private static int insertTable(Table table, List<Table> list) {
        int pos = list.size();
        ForeignKey[] foreignKeyArray = table.getForeignKeys();
        int n = foreignKeyArray.length;
        int n2 = 0;
        while (n2 < n) {
            ForeignKey fk = foreignKeyArray[n2];
            String fkTableName = fk.getForeignTableName();
            int i = 0;
            while (i < list.size()) {
                Table t = list.get(i);
                if (t.getName().equals(fkTableName) && i < pos) {
                    pos = i;
                }
                ++i;
            }
            ++n2;
        }
        if (pos != list.size()) {
            return pos;
        }
        int i = 0;
        while (i < list.size()) {
            Table t = list.get(i);
            ForeignKey[] foreignKeyArray2 = t.getForeignKeys();
            int n3 = foreignKeyArray2.length;
            int n4 = 0;
            while (n4 < n3) {
                ForeignKey fk = foreignKeyArray2[n4];
                if (fk.getForeignTableName().equals(table.getName())) {
                    return i + 1;
                }
                ++n4;
            }
            ++i;
        }
        return list.size();
    }

    public void dropTable(Database database, Table table) throws IOException {
        int idx = database.getTableCount() - 1;
        while (idx >= 0) {
            Table otherTable = database.getTable(idx);
            ForeignKey[] fks = otherTable.getForeignKeys();
            int fkIdx = 0;
            while (fks != null && fkIdx < fks.length) {
                if (fks[fkIdx].getForeignTable().equals(table)) {
                    this.writeExternalForeignKeyDropStmt(otherTable, fks[fkIdx]);
                }
                ++fkIdx;
            }
            --idx;
        }
        this.dropExternalForeignKeys(table);
        this.writeTableComment(table);
        this.dropTable(table);
    }

    public void dropTable(Table table) throws IOException {
        this.print("DROP TABLE ");
        this.printIdentifier(this.getTableName(table));
        this.printEndOfStatement();
    }

    public void dropExternalForeignKeys(Table table) throws IOException {
        if (!this.getPlatformInfo().isForeignKeysEmbedded()) {
            int idx = 0;
            while (idx < table.getForeignKeyCount()) {
                this.writeExternalForeignKeyDropStmt(table, table.getForeignKey(idx));
                ++idx;
            }
        }
    }

    public String getInsertSql(Table table, Map columnValues, boolean genPlaceholders) {
        Column column;
        StringBuffer buffer = new StringBuffer("INSERT INTO ");
        boolean addComma = false;
        buffer.append(this.getDelimitedIdentifier(this.getTableName(table)));
        buffer.append(" (");
        int idx = 0;
        while (idx < table.getColumnCount()) {
            column = table.getColumn(idx);
            if (columnValues.containsKey(column.getName())) {
                if (addComma) {
                    buffer.append(", ");
                }
                buffer.append(this.getDelimitedIdentifier(column.getName()));
                addComma = true;
            }
            ++idx;
        }
        buffer.append(") VALUES (");
        if (genPlaceholders) {
            addComma = false;
            idx = 0;
            while (idx < columnValues.size()) {
                if (addComma) {
                    buffer.append(", ");
                }
                buffer.append("?");
                addComma = true;
                ++idx;
            }
        } else {
            addComma = false;
            idx = 0;
            while (idx < table.getColumnCount()) {
                column = table.getColumn(idx);
                if (columnValues.containsKey(column.getName())) {
                    if (addComma) {
                        buffer.append(", ");
                    }
                    buffer.append(this.getValueAsString(column, columnValues.get(column.getName())));
                    addComma = true;
                }
                ++idx;
            }
        }
        buffer.append(")");
        return buffer.toString();
    }

    public String getUpdateSql(Table table, Map columnValues, boolean genPlaceholders) {
        Column column;
        StringBuffer buffer = new StringBuffer("UPDATE ");
        boolean addSep = false;
        buffer.append(this.getDelimitedIdentifier(this.getTableName(table)));
        buffer.append(" SET ");
        int idx = 0;
        while (idx < table.getColumnCount()) {
            column = table.getColumn(idx);
            if (!column.isPrimaryKey() && columnValues.containsKey(column.getName())) {
                if (addSep) {
                    buffer.append(", ");
                }
                buffer.append(this.getDelimitedIdentifier(column.getName()));
                buffer.append(" = ");
                if (genPlaceholders) {
                    buffer.append("?");
                } else {
                    buffer.append(this.getValueAsString(column, columnValues.get(column.getName())));
                }
                addSep = true;
            }
            ++idx;
        }
        buffer.append(" WHERE ");
        addSep = false;
        idx = 0;
        while (idx < table.getColumnCount()) {
            column = table.getColumn(idx);
            if (column.isPrimaryKey() && columnValues.containsKey(column.getName())) {
                if (addSep) {
                    buffer.append(" AND ");
                }
                buffer.append(this.getDelimitedIdentifier(column.getName()));
                buffer.append(" = ");
                if (genPlaceholders) {
                    buffer.append("?");
                } else {
                    buffer.append(this.getValueAsString(column, columnValues.get(column.getName())));
                }
                addSep = true;
            }
            ++idx;
        }
        return buffer.toString();
    }

    public String getDeleteSql(Table table, Map<String,Object> pkValues, boolean genPlaceholders) {
        StringBuffer buffer = new StringBuffer("DELETE FROM ");
        boolean addSep = false;
        buffer.append(this.getDelimitedIdentifier(this.getTableName(table)));
        if (pkValues != null && !pkValues.isEmpty()) {
            buffer.append(" WHERE ");
            for (Map.Entry entry : pkValues.entrySet()) {
                Column column = table.findColumn((String)entry.getKey());
                if (addSep) {
                    buffer.append(" AND ");
                }
                buffer.append(this.getDelimitedIdentifier(entry.getKey().toString()));
                buffer.append(" = ");
                if (genPlaceholders) {
                    buffer.append("?");
                } else {
                    buffer.append(column == null ? entry.getValue() : this.getValueAsString(column, entry.getValue()));
                }
                addSep = true;
            }
        }
        return buffer.toString();
    }

    protected String getValueAsString(Column column, Object value) {
        if (value == null) {
            return "NULL";
        }
        StringBuffer result = new StringBuffer();
        switch (column.getTypeCode()) {
            case 91: {
                result.append(this.getPlatformInfo().getValueQuoteToken());
                if (!(value instanceof String) && this.getValueDateFormat() != null) {
                    result.append(this.getValueDateFormat().format(value));
                } else {
                    result.append(value.toString());
                }
                result.append(this.getPlatformInfo().getValueQuoteToken());
                break;
            }
            case 92: {
                result.append(this.getPlatformInfo().getValueQuoteToken());
                if (!(value instanceof String) && this.getValueTimeFormat() != null) {
                    result.append(this.getValueTimeFormat().format(value));
                } else {
                    result.append(value.toString());
                }
                result.append(this.getPlatformInfo().getValueQuoteToken());
                break;
            }
            case 93: {
                result.append(this.getPlatformInfo().getValueQuoteToken());
                result.append(value.toString());
                result.append(this.getPlatformInfo().getValueQuoteToken());
                break;
            }
            case 2: 
            case 3: 
            case 6: 
            case 7: 
            case 8: {
                result.append(this.getPlatformInfo().getValueQuoteToken());
                if (!(value instanceof String) && this.getValueNumberFormat() != null) {
                    result.append(this.getValueNumberFormat().format(value));
                } else {
                    result.append(value.toString());
                }
                result.append(this.getPlatformInfo().getValueQuoteToken());
                break;
            }
            default: {
                result.append(this.getPlatformInfo().getValueQuoteToken());
                result.append(this.escapeStringValue(value.toString()));
                result.append(this.getPlatformInfo().getValueQuoteToken());
            }
        }
        return result.toString();
    }

    public String getSelectLastIdentityValues(Table table) {
        return null;
    }

    public String shortenName(String name, int desiredLength) {
        int originalLength = name.length();
        if (desiredLength <= 0 || originalLength <= desiredLength) {
            return name;
        }
        int delta = originalLength - desiredLength;
        int startCut = desiredLength / 2;
        StringBuffer result = new StringBuffer();
        result.append(name.substring(0, startCut));
        if (!(startCut != 0 && name.charAt(startCut - 1) == '_' || startCut + delta + 1 != originalLength && name.charAt(startCut + delta + 1) == '_')) {
            result.append("_");
        }
        result.append(name.substring(startCut + delta + 1, originalLength));
        return result.toString();
    }

    public String getTableName(Table table) {
        return this.shortenName(table.getName(), this.getMaxTableNameLength());
    }

    protected void writeTableComment(Table table) throws IOException {
        this.printComment("-----------------------------------------------------------------------");
        this.printComment(this.getTableName(table));
        this.printComment("-----------------------------------------------------------------------");
        this.println();
    }

    protected void writeTableAlterStmt(Table table) throws IOException {
        this.print("ALTER TABLE ");
        this.printlnIdentifier(this.getTableName(table));
        this.printIndent();
    }

    protected void writeTableCreationStmt(Database database, Table table, Map parameters) throws IOException {
        this.print("CREATE TABLE ");
        this.printlnIdentifier(this.getTableName(table));
        this.println("(");
        this.writeColumns(table);
        if (this.getPlatformInfo().isPrimaryKeyEmbedded()) {
            this.writeEmbeddedPrimaryKeysStmt(table);
        }
        if (this.getPlatformInfo().isForeignKeysEmbedded()) {
            this.writeEmbeddedForeignKeysStmt(database, table);
        }
        if (this.getPlatformInfo().isIndicesEmbedded()) {
            this.writeEmbeddedIndicesStmt(table);
        }
        this.println();
        this.print(")");
    }

    protected void writeTableCreationStmtEnding(Table table, Map parameters) throws IOException {
        this.printEndOfStatement();
    }

    protected void writeColumns(Table table) throws IOException {
        int idx = 0;
        while (idx < table.getColumnCount()) {
            this.printIndent();
            this.writeColumn(table, table.getColumn(idx));
            if (idx < table.getColumnCount() - 1) {
                this.println(",");
            }
            ++idx;
        }
    }

    protected String getColumnName(Column column) throws IOException {
        return this.shortenName(column.getName(), this.getMaxColumnNameLength());
    }

    protected void writeColumn(Table table, Column column) throws IOException {
        this.printIdentifier(this.getColumnName(column));
        this.print(" ");
        this.print(this.getSqlType(column));
        this.writeColumnDefaultValueStmt(table, column);
        if (column.isRequired()) {
            this.print(" ");
            this.writeColumnNotNullableStmt();
        } else if (this.getPlatformInfo().isNullAsDefaultValueRequired() && this.getPlatformInfo().hasNullDefault(column.getTypeCode())) {
            this.print(" ");
            this.writeColumnNullableStmt();
        }
        if (column.isAutoIncrement() && !this.getPlatformInfo().isDefaultValueUsedForIdentitySpec()) {
            if (!this.getPlatformInfo().isNonPKIdentityColumnsSupported() && !column.isPrimaryKey()) {
                throw new ModelException("Column " + column.getName() + " in table " + table.getName() + " is auto-incrementing but not a primary key column, which is not supported by the platform");
            }
            this.print(" ");
            this.writeColumnAutoIncrementStmt(table, column);
        }
    }

    protected String getSqlType(Column column) {
        String nativeType = this.getNativeType(column);
        int sizePos = nativeType.indexOf(SIZE_PLACEHOLDER);
        StringBuffer sqlType = new StringBuffer();
        sqlType.append(sizePos >= 0 ? nativeType.substring(0, sizePos) : nativeType);
        Object sizeSpec = column.getSize();
        if (sizeSpec == null) {
            sizeSpec = this.getPlatformInfo().getDefaultSize(column.getTypeCode());
        }
        if (sizeSpec != null) {
            if (this.getPlatformInfo().hasSize(column.getTypeCode())) {
                sqlType.append("(");
                sqlType.append(sizeSpec.toString());
                sqlType.append(")");
            } else if (this.getPlatformInfo().hasPrecisionAndScale(column.getTypeCode())) {
                sqlType.append("(");
                sqlType.append(column.getSizeAsInt());
                sqlType.append(",");
                sqlType.append(column.getScale());
                sqlType.append(")");
            }
        }
        sqlType.append(sizePos >= 0 ? nativeType.substring(sizePos + SIZE_PLACEHOLDER.length()) : "");
        return sqlType.toString();
    }

    protected String getNativeType(Column column) {
        String nativeType = this.getPlatformInfo().getNativeType(column.getTypeCode());
        return nativeType == null ? column.getType() : nativeType;
    }

    protected String getBareNativeType(Column column) {
        String nativeType = this.getNativeType(column);
        int sizePos = nativeType.indexOf(SIZE_PLACEHOLDER);
        return sizePos >= 0 ? nativeType.substring(0, sizePos) : nativeType;
    }

    protected String getNativeDefaultValue(Column column) {
        return column.getDefaultValue();
    }

    protected String escapeStringValue(String value) {
        String result = value;
        for (Map.Entry entry : this._charSequencesToEscape.entrySet()) {
            result = StringUtils.replace((String)result, (String)((String)entry.getKey()), (String)((String)entry.getValue()));
        }
        return result;
    }

    protected boolean isValidDefaultValue(String defaultSpec, int typeCode) {
        return defaultSpec != null && (defaultSpec.length() > 0 || !TypeMap.isNumericType(typeCode) && !TypeMap.isDateTimeType(typeCode));
    }

    protected void writeColumnDefaultValueStmt(Table table, Column column) throws IOException {
        Object parsedDefault = column.getParsedDefaultValue();
        if (parsedDefault != null) {
            if (!(this.getPlatformInfo().isDefaultValuesForLongTypesSupported() || column.getTypeCode() != -4 && column.getTypeCode() != -1)) {
                throw new ModelException("The platform does not support default values for LONGVARCHAR or LONGVARBINARY columns");
            }
            if (this.isValidDefaultValue(column.getDefaultValue(), column.getTypeCode())) {
                this.print(" DEFAULT ");
                this.writeColumnDefaultValue(table, column);
            }
        } else if (this.getPlatformInfo().isDefaultValueUsedForIdentitySpec() && column.isAutoIncrement()) {
            this.print(" DEFAULT ");
            this.writeColumnDefaultValue(table, column);
        }
    }

    protected void writeColumnDefaultValue(Table table, Column column) throws IOException {
        this.printDefaultValue(this.getNativeDefaultValue(column), column.getTypeCode());
    }

    protected void printDefaultValue(Object defaultValue, int typeCode) throws IOException {
        if (defaultValue != null) {
            boolean shouldUseQuotes;
            boolean bl = shouldUseQuotes = !TypeMap.isNumericType(typeCode);
            if (shouldUseQuotes) {
                this.print(this.getPlatformInfo().getValueQuoteToken());
                this.print(this.escapeStringValue(defaultValue.toString()));
                this.print(this.getPlatformInfo().getValueQuoteToken());
            } else {
                this.print(defaultValue.toString());
            }
        }
    }

    protected void writeColumnAutoIncrementStmt(Table table, Column column) throws IOException {
        this.print("IDENTITY");
    }

    protected void writeColumnNullableStmt() throws IOException {
        this.print("NULL");
    }

    protected void writeColumnNotNullableStmt() throws IOException {
        this.print("NOT NULL");
    }

    protected boolean columnsDiffer(Column currentColumn, Column desiredColumn) {
        boolean sizeMatters;
        String desiredDefault = desiredColumn.getDefaultValue();
        String currentDefault = currentColumn.getDefaultValue();
        boolean defaultsEqual = desiredDefault == null || desiredDefault.equals(currentDefault);
        boolean bl = sizeMatters = this.getPlatformInfo().hasSize(currentColumn.getTypeCode()) && desiredColumn.getSize() != null;
        return this.getPlatformInfo().getTargetJdbcType(desiredColumn.getTypeCode()) != currentColumn.getTypeCode() || desiredColumn.isRequired() != currentColumn.isRequired() || sizeMatters && !StringUtils.equals((String)desiredColumn.getSize(), (String)currentColumn.getSize()) || !defaultsEqual;
    }

    public String getForeignKeyName(Table table, ForeignKey fk) {
        boolean needsName;
        String fkName = fk.getName();
        boolean bl = needsName = fkName == null || fkName.length() == 0;
        if (needsName) {
            StringBuffer name = new StringBuffer();
            int idx = 0;
            while (idx < fk.getReferenceCount()) {
                name.append(fk.getReference(idx).getLocalColumnName());
                name.append("_");
                ++idx;
            }
            name.append(fk.getForeignTableName());
            fkName = this.getConstraintName(null, table, "FK", name.toString());
        }
        fkName = this.shortenName(fkName, this.getMaxForeignKeyNameLength());
        if (needsName) {
            this._log.warn((Object)("Encountered a foreign key in table " + table.getName() + " that has no name. " + "DdlUtils will use the auto-generated and shortened name " + fkName + " instead."));
        }
        return fkName;
    }

    public String getConstraintName(String prefix, Table table, String secondPart, String suffix) {
        StringBuffer result = new StringBuffer();
        if (prefix != null) {
            result.append(prefix);
            result.append("_");
        }
        result.append(table.getName());
        result.append("_");
        result.append(secondPart);
        if (suffix != null) {
            result.append("_");
            result.append(suffix);
        }
        return this.shortenName(result.toString(), this.getMaxConstraintNameLength());
    }

    protected void writeEmbeddedPrimaryKeysStmt(Table table) throws IOException {
        Column[] primaryKeyColumns = table.getPrimaryKeyColumns();
        if (primaryKeyColumns.length > 0 && this.shouldGeneratePrimaryKeys(primaryKeyColumns)) {
            this.printStartOfEmbeddedStatement();
            this.writePrimaryKeyStmt(table, primaryKeyColumns);
        }
    }

    protected void writeExternalPrimaryKeysCreateStmt(Table table, Column[] primaryKeyColumns) throws IOException {
        if (primaryKeyColumns.length > 0 && this.shouldGeneratePrimaryKeys(primaryKeyColumns)) {
            this.print("ALTER TABLE ");
            this.printlnIdentifier(this.getTableName(table));
            this.printIndent();
            this.print("ADD CONSTRAINT ");
            this.printIdentifier(this.getConstraintName(null, table, "PK", null));
            this.print(" ");
            this.writePrimaryKeyStmt(table, primaryKeyColumns);
            this.printEndOfStatement();
        }
    }

    protected boolean shouldGeneratePrimaryKeys(Column[] primaryKeyColumns) {
        return true;
    }

    protected void writePrimaryKeyStmt(Table table, Column[] primaryKeyColumns) throws IOException {
        this.print("PRIMARY KEY (");
        int idx = 0;
        while (idx < primaryKeyColumns.length) {
            this.printIdentifier(this.getColumnName(primaryKeyColumns[idx]));
            if (idx < primaryKeyColumns.length - 1) {
                this.print(", ");
            }
            ++idx;
        }
        this.print(")");
    }

    public String getIndexName(Index index) {
        return this.shortenName(index.getName(), this.getMaxConstraintNameLength());
    }

    protected void writeExternalIndicesCreateStmt(Table table) throws IOException {
        int idx = 0;
        while (idx < table.getIndexCount()) {
            Index index = table.getIndex(idx);
            if (!index.isUnique() && !this.getPlatformInfo().isIndicesSupported()) {
                throw new ModelException("Platform does not support non-unique indices");
            }
            this.writeExternalIndexCreateStmt(table, index);
            ++idx;
        }
    }

    protected void writeEmbeddedIndicesStmt(Table table) throws IOException {
        if (this.getPlatformInfo().isIndicesSupported()) {
            int idx = 0;
            while (idx < table.getIndexCount()) {
                this.printStartOfEmbeddedStatement();
                this.writeEmbeddedIndexCreateStmt(table, table.getIndex(idx));
                ++idx;
            }
        }
    }

    protected void writeExternalIndexCreateStmt(Table table, Index index) throws IOException {
        if (this.getPlatformInfo().isIndicesSupported()) {
            if (index.getColumnCount() == 1 && StringUtils.isNotBlank((String)index.getColumn(0).getSize())) {
                return;
            }
            if (index.getName() == null) {
                this._log.warn((Object)("Cannot write unnamed index " + index));
            } else {
                this.print("CREATE");
                if (index.isUnique()) {
                    this.print(" UNIQUE");
                }
                this.print(" INDEX ");
                this.printIdentifier(this.getIndexName(index));
                this.print(" ON ");
                this.printIdentifier(this.getTableName(table));
                this.print(" (");
                int idx = 0;
                while (idx < index.getColumnCount()) {
                    IndexColumn idxColumn = index.getColumn(idx);
                    Column col = table.findColumn(idxColumn.getName());
                    String indexSize = idxColumn.getSize();
                    if (!StringUtils.isNotBlank((String)indexSize)) {
                        if (col == null) {
                            throw new ModelException("Invalid column '" + idxColumn.getName() + "' on index " + index.getName() + " for table " + table.getName());
                        }
                        if (idx > 0) {
                            this.print(", ");
                        }
                        this.printIdentifier(this.getColumnName(col));
                    }
                    ++idx;
                }
                this.print(")");
                this.printEndOfStatement();
            }
        }
    }

    protected void writeEmbeddedIndexCreateStmt(Table table, Index index) throws IOException {
        if (index.getName() != null && index.getName().length() > 0) {
            this.print(" CONSTRAINT ");
            this.printIdentifier(this.getIndexName(index));
        }
        if (index.isUnique()) {
            this.print(" UNIQUE");
        } else {
            this.print(" INDEX ");
        }
        this.print(" (");
        int idx = 0;
        while (idx < index.getColumnCount()) {
            IndexColumn idxColumn = index.getColumn(idx);
            Column col = table.findColumn(idxColumn.getName());
            String indexSize = idxColumn.getSize();
            if (!StringUtils.isNotBlank((String)indexSize)) {
                if (col == null) {
                    throw new ModelException("Invalid column '" + idxColumn.getName() + "' on index " + index.getName() + " for table " + table.getName());
                }
                if (idx > 0) {
                    this.print(", ");
                }
                this.printIdentifier(this.getColumnName(col));
            }
            ++idx;
        }
        this.print(")");
    }

    public void writeExternalIndexDropStmt(Table table, Index index) throws IOException {
        if (this.getPlatformInfo().isAlterTableForDropUsed()) {
            this.writeTableAlterStmt(table);
        }
        this.print("DROP INDEX ");
        this.printIdentifier(this.getIndexName(index));
        if (!this.getPlatformInfo().isAlterTableForDropUsed()) {
            this.print(" ON ");
            this.printIdentifier(this.getTableName(table));
        }
        this.printEndOfStatement();
    }

    protected void writeEmbeddedForeignKeysStmt(Database database, Table table) throws IOException {
        int idx = 0;
        while (idx < table.getForeignKeyCount()) {
            ForeignKey key = table.getForeignKey(idx);
            if (key.getForeignTableName() == null) {
                this._log.warn((Object)("Foreign key table is null for key " + key));
            } else {
                this.printStartOfEmbeddedStatement();
                if (this.getPlatformInfo().isEmbeddedForeignKeysNamed()) {
                    this.print("CONSTRAINT ");
                    this.printIdentifier(this.getForeignKeyName(table, key));
                    this.print(" ");
                }
                this.print("FOREIGN KEY (");
                this.writeLocalReferences(key);
                this.print(") REFERENCES ");
                this.printIdentifier(this.getTableName(database.findTable(key.getForeignTableName())));
                this.print(" (");
                this.writeForeignReferences(key);
                this.print(")");
            }
            ++idx;
        }
    }

    protected void writeExternalForeignKeyCreateStmt(Database database, Table table, ForeignKey key) throws IOException {
        if (key.getForeignTableName() == null) {
            this._log.warn((Object)("Foreign key table is null for key " + key));
        } else {
            this.writeTableAlterStmt(table);
            this.print("ADD CONSTRAINT ");
            this.printIdentifier(this.getForeignKeyName(table, key));
            this.print(" FOREIGN KEY (");
            this.writeLocalReferences(key);
            this.print(") REFERENCES ");
            this.printIdentifier(this.getTableName(database.findTable(key.getForeignTableName())));
            this.print(" (");
            this.writeForeignReferences(key);
            this.print(")");
            String action = key.getOnDeleteAction();
            if (!action.equals("none")) {
                this.print(" ON DELETE " + action);
            }
            this.printEndOfStatement();
        }
    }

    protected void writeLocalReferences(ForeignKey key) throws IOException {
        int idx = 0;
        while (idx < key.getReferenceCount()) {
            if (idx > 0) {
                this.print(", ");
            }
            this.printIdentifier(key.getReference(idx).getLocalColumnName());
            ++idx;
        }
    }

    protected void writeForeignReferences(ForeignKey key) throws IOException {
        int idx = 0;
        while (idx < key.getReferenceCount()) {
            if (idx > 0) {
                this.print(", ");
            }
            this.printIdentifier(key.getReference(idx).getForeignColumnName());
            ++idx;
        }
    }

    protected void writeExternalForeignKeyDropStmt(Table table, ForeignKey foreignKey) throws IOException {
    }

    protected void printComment(String text) throws IOException {
        if (this.getPlatform().isSqlCommentsOn()) {
            this.print(this.getPlatformInfo().getCommentPrefix());
            this.print(" ");
            this.print(text);
            this.print(" ");
            this.print(this.getPlatformInfo().getCommentSuffix());
            this.println();
        }
    }

    protected void printStartOfEmbeddedStatement() throws IOException {
        this.println(",");
        this.printIndent();
    }

    protected void printEndOfStatement() throws IOException {
        this.println(this.getPlatformInfo().getSqlCommandDelimiter());
        this.println();
    }

    protected void println() throws IOException {
        this.print(LINE_SEPARATOR);
    }

    protected void print(String text) throws IOException {
        this._writer.write(text);
    }

    protected String getDelimitedIdentifier(String identifier) {
        if (this.getPlatform().isDelimitedIdentifierModeOn()) {
            return String.valueOf(this.getPlatformInfo().getDelimiterToken()) + identifier + this.getPlatformInfo().getDelimiterToken();
        }
        return identifier;
    }

    protected void printIdentifier(String identifier) throws IOException {
        this.print(this.getDelimitedIdentifier(identifier));
    }

    protected void printlnIdentifier(String identifier) throws IOException {
        this.println(this.getDelimitedIdentifier(identifier));
    }

    protected void println(String text) throws IOException {
        this.print(text);
        this.println();
    }

    protected void printIndent() throws IOException {
        this.print(this.getIndent());
    }

    protected String createUniqueIdentifier() {
        return new UID().toString().replace(':', '_').replace('-', '_');
    }
}


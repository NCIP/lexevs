/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.DynaBean
 *  org.apache.commons.lang.builder.EqualsBuilder
 *  org.apache.commons.lang.builder.HashCodeBuilder
 */
package org.apache.ddlutils.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.ddlutils.dynabean.DynaClassCache;
import org.apache.ddlutils.dynabean.SqlDynaClass;
import org.apache.ddlutils.dynabean.SqlDynaException;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.IndexColumn;
import org.apache.ddlutils.model.ModelException;
import org.apache.ddlutils.model.Reference;
import org.apache.ddlutils.model.Table;

public class Database
implements Serializable,
Cloneable {
    private static final long serialVersionUID = -3160443396757573868L;
    private String _name;
    private String _idMethod;
    private String _version;
    private ArrayList<Table> _tables = new ArrayList<Table>();
    private transient DynaClassCache _dynaClassCache = null;

    public void mergeWith(Database otherDb) throws ModelException {
        for (Table table : otherDb._tables) {
            if (this.findTable(table.getName()) != null) {
                throw new ModelException("Cannot merge the models because table " + table.getName() + " already defined in this model");
            }
            try {
                this.addTable((Table)table.clone());
            }
            catch (CloneNotSupportedException cloneNotSupportedException) {
                // empty catch block
            }
        }
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getVersion() {
        return this._version;
    }

    public void setVersion(String version) {
        this._version = version;
    }

    public String getIdMethod() {
        return this._idMethod;
    }

    public void setIdMethod(String idMethod) {
        this._idMethod = idMethod;
    }

    public int getTableCount() {
        return this._tables.size();
    }

    public Table[] getTables() {
        return this._tables.toArray(new Table[this._tables.size()]);
    }

    public Table getTable(int idx) {
        return (Table)this._tables.get(idx);
    }

    public void addTable(Table table) {
        if (table != null) {
            this._tables.add(table);
        }
    }

    public void addTable(int idx, Table table) {
        if (table != null) {
            this._tables.add(idx, table);
        }
    }

    public void addTables(Collection tables) {
        Iterator it = tables.iterator();
        while (it.hasNext()) {
            this.addTable((Table)it.next());
        }
    }

    public void removeTable(Table table) {
        if (table != null) {
            this._tables.remove(table);
        }
    }

    public void removeTable(int idx) {
        this._tables.remove(idx);
    }

    public void initialize() throws ModelException {
        HashSet<String> namesOfProcessedTables = new HashSet<String>();
        HashSet<String> namesOfProcessedColumns = new HashSet<String>();
        HashSet<String> namesOfProcessedFks = new HashSet<String>();
        HashSet<String> namesOfProcessedIndices = new HashSet<String>();
        int tableIdx = 0;
        if (this.getName() == null || this.getName().length() == 0) {
            throw new ModelException("The database model has no name");
        }
        for (Table curTable : this._tables) {
            if (curTable.getName() == null || curTable.getName().length() == 0) {
                throw new ModelException("The table nr. " + tableIdx + " has no name");
            }
            if (namesOfProcessedTables.contains(curTable.getName())) {
                throw new ModelException("There are multiple tables with the name " + curTable.getName());
            }
            namesOfProcessedTables.add(curTable.getName());
            namesOfProcessedColumns.clear();
            namesOfProcessedFks.clear();
            namesOfProcessedIndices.clear();
            int idx = 0;
            while (idx < curTable.getColumnCount()) {
                Column column = curTable.getColumn(idx);
                if (column.getName() == null || column.getName().length() == 0) {
                    throw new ModelException("The column nr. " + idx + " in table " + curTable.getName() + " has no name");
                }
                if (namesOfProcessedColumns.contains(column.getName())) {
                    throw new ModelException("There are multiple column with the name " + column.getName() + " in the table " + curTable.getName());
                }
                namesOfProcessedColumns.add(column.getName());
                if (column.getType() == null || column.getType().length() == 0) {
                    throw new ModelException("The column nr. " + idx + " in table " + curTable.getName() + " has no type");
                }
                if (column.getTypeCode() == 1111 && !"OTHER".equalsIgnoreCase(column.getType())) {
                    throw new ModelException("The column nr. " + idx + " in table " + curTable.getName() + " has an unknown type " + column.getType());
                }
                namesOfProcessedColumns.add(column.getName());
                ++idx;
            }
            idx = 0;
            while (idx < curTable.getForeignKeyCount()) {
                String fkDesc;
                ForeignKey fk = curTable.getForeignKey(idx);
                String fkName = fk.getName() == null ? "" : fk.getName();
                String string = fkDesc = fkName.length() == 0 ? "nr. " + idx : fkName;
                if (fkName.length() > 0) {
                    if (namesOfProcessedFks.contains(fkName)) {
                        throw new ModelException("There are multiple foreign keys in table " + curTable.getName() + " with the name " + fkName);
                    }
                    namesOfProcessedFks.add(fkName);
                }
                if (fk.getForeignTable() == null) {
                    Table targetTable = this.findTable(fk.getForeignTableName(), true);
                    if (targetTable == null) {
                        throw new ModelException("The foreignkey " + fkDesc + " in table " + curTable.getName() + " references the undefined table " + fk.getForeignTableName());
                    }
                    fk.setForeignTable(targetTable);
                }
                int refIdx = 0;
                while (refIdx < fk.getReferenceCount()) {
                    Reference ref = fk.getReference(refIdx);
                    if (ref.getLocalColumn() == null) {
                        Column localColumn = curTable.findColumn(ref.getLocalColumnName(), true);
                        if (localColumn == null) {
                            throw new ModelException("The foreignkey " + fkDesc + " in table " + curTable.getName() + " references the undefined local column " + ref.getLocalColumnName());
                        }
                        ref.setLocalColumn(localColumn);
                    }
                    if (ref.getForeignColumn() == null) {
                        Column foreignColumn = fk.getForeignTable().findColumn(ref.getForeignColumnName(), true);
                        if (foreignColumn == null) {
                            throw new ModelException("The foreignkey " + fkDesc + " in table " + curTable.getName() + " references the undefined local column " + ref.getForeignColumnName() + " in table " + fk.getForeignTable().getName());
                        }
                        ref.setForeignColumn(foreignColumn);
                    }
                    ++refIdx;
                }
                ++idx;
            }
            idx = 0;
            while (idx < curTable.getIndexCount()) {
                String indexDesc;
                Index index = curTable.getIndex(idx);
                String indexName = index.getName() == null ? "" : index.getName();
                String string = indexDesc = indexName.length() == 0 ? "nr. " + idx : indexName;
                if (indexName.length() > 0) {
                    if (namesOfProcessedIndices.contains(indexName)) {
                        throw new ModelException("There are multiple indices in table " + curTable.getName() + " with the name " + indexName);
                    }
                    namesOfProcessedIndices.add(indexName);
                }
                int indexColumnIdx = 0;
                while (indexColumnIdx < index.getColumnCount()) {
                    IndexColumn indexColumn = index.getColumn(indexColumnIdx);
                    Column column = curTable.findColumn(indexColumn.getName(), true);
                    if (column == null) {
                        throw new ModelException("The index " + indexDesc + " in table " + curTable.getName() + " references the undefined column " + indexColumn.getName());
                    }
                    indexColumn.setColumn(column);
                    ++indexColumnIdx;
                }
                ++idx;
            }
            ++tableIdx;
        }
    }

    public Table findTable(String name) {
        return this.findTable(name, false);
    }

    public Table findTable(String name, boolean caseSensitive) {
        for (Table table : this._tables) {
            if (!(caseSensitive ? table.getName().equals(name) : table.getName().equalsIgnoreCase(name))) continue;
            return table;
        }
        return null;
    }

    private DynaClassCache getDynaClassCache() {
        if (this._dynaClassCache == null) {
            this._dynaClassCache = new DynaClassCache();
        }
        return this._dynaClassCache;
    }

    public void resetDynaClassCache() {
        this._dynaClassCache = null;
    }

    public SqlDynaClass getDynaClassFor(String tableName) {
        Table table = this.findTable(tableName);
        return table != null ? this.getDynaClassCache().getDynaClass(table) : null;
    }

    public SqlDynaClass getDynaClassFor(DynaBean bean) {
        return this.getDynaClassCache().getDynaClass(bean);
    }

    public DynaBean createDynaBeanFor(Table table) throws SqlDynaException {
        return this.getDynaClassCache().createNewInstance(table);
    }

    public DynaBean createDynaBeanFor(String tableName, boolean caseSensitive) throws SqlDynaException {
        return this.getDynaClassCache().createNewInstance(this.findTable(tableName, caseSensitive));
    }

    public Object clone() throws CloneNotSupportedException {
        Database result = (Database)super.clone();
        result._name = this._name;
        result._idMethod = this._idMethod;
        result._version = this._version;
        result._tables = (ArrayList)this._tables.clone();
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Database) {
            Database other = (Database)obj;
            return new EqualsBuilder().append((Object)this._name, (Object)other._name).append((Object)this._tables, (Object)other._tables).isEquals();
        }
        return false;
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).append((Object)this._name).append((Object)this._tables).toHashCode();
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("Database [name=");
        result.append(this.getName());
        result.append("; ");
        result.append(this.getTableCount());
        result.append(" tables]");
        return result.toString();
    }

    public String toVerboseString() {
        StringBuffer result = new StringBuffer();
        result.append("Database [");
        result.append(this.getName());
        result.append("] tables:");
        int idx = 0;
        while (idx < this.getTableCount()) {
            result.append(" ");
            result.append(this.getTable(idx).toVerboseString());
            ++idx;
        }
        return result.toString();
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.collections.CollectionUtils
 *  org.apache.commons.collections.Predicate
 *  org.apache.commons.lang.builder.EqualsBuilder
 *  org.apache.commons.lang.builder.HashCodeBuilder
 */
package org.apache.ddlutils.model;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Index;

public class Table
implements Serializable,
Cloneable {
    private static final long serialVersionUID = -5541154961302342608L;
    private String _catalog = null;
    private String _schema = null;
    private String _name = null;
    private String _description = null;
    private String _type = null;
    private ArrayList<Column> _columns = new ArrayList<Column>();
    private ArrayList<ForeignKey> _foreignKeys = new ArrayList<ForeignKey>();
    private ArrayList<Index> _indices = new ArrayList<Index>();

    public String getCatalog() {
        return this._catalog;
    }

    public void setCatalog(String catalog) {
        this._catalog = catalog;
    }

    public String getSchema() {
        return this._schema;
    }

    public void setSchema(String schema) {
        this._schema = schema;
    }

    public String getType() {
        return this._type;
    }

    public void setType(String type) {
        this._type = type;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getDescription() {
        return this._description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public int getColumnCount() {
        return this._columns.size();
    }

    public Column getColumn(int idx) {
        return (Column)this._columns.get(idx);
    }

    public Column[] getColumns() {
        return new Column[this._columns.size()];
    }

    public void addColumn(Column column) {
        if (column != null) {
            this._columns.add(column);
        }
    }

    public void addColumn(int idx, Column column) {
        if (column != null) {
            this._columns.add(idx, column);
        }
    }

    public void addColumn(Column previousColumn, Column column) {
        if (column != null) {
            if (previousColumn == null) {
                this._columns.add(0, column);
            } else {
                this._columns.add(this._columns.indexOf(previousColumn), column);
            }
        }
    }

    public void addColumns(Collection columns) {
        Iterator it = columns.iterator();
        while (it.hasNext()) {
            this.addColumn((Column)it.next());
        }
    }

    public void removeColumn(Column column) {
        if (column != null) {
            this._columns.remove(column);
        }
    }

    public void removeColumn(int idx) {
        this._columns.remove(idx);
    }

    public int getForeignKeyCount() {
        return this._foreignKeys.size();
    }

    public ForeignKey getForeignKey(int idx) {
        return (ForeignKey)this._foreignKeys.get(idx);
    }

    public ForeignKey[] getForeignKeys() {
        return new ForeignKey[this._foreignKeys.size()];
    }

    public void addForeignKey(ForeignKey foreignKey) {
        if (foreignKey != null) {
            this._foreignKeys.add(foreignKey);
        }
    }

    public void addForeignKey(int idx, ForeignKey foreignKey) {
        if (foreignKey != null) {
            this._foreignKeys.add(idx, foreignKey);
        }
    }

    public void addForeignKeys(Collection foreignKeys) {
        Iterator it = foreignKeys.iterator();
        while (it.hasNext()) {
            this.addForeignKey((ForeignKey)it.next());
        }
    }

    public void removeForeignKey(ForeignKey foreignKey) {
        if (foreignKey != null) {
            this._foreignKeys.remove(foreignKey);
        }
    }

    public void removeForeignKey(int idx) {
        this._foreignKeys.remove(idx);
    }

    public int getIndexCount() {
        return this._indices.size();
    }

    public Index getIndex(int idx) {
        return (Index)this._indices.get(idx);
    }

    public void addIndex(Index index) {
        if (index != null) {
            this._indices.add(index);
        }
    }

    public void addIndex(int idx, Index index) {
        if (index != null) {
            this._indices.add(idx, index);
        }
    }

    public void addIndices(Collection indices) {
        Iterator it = indices.iterator();
        while (it.hasNext()) {
            this.addIndex((Index)it.next());
        }
    }

    public Index[] getIndices() {
        return new Index[this._indices.size()];
    }

    public Index[] getNonUniqueIndices() {
        Collection nonUniqueIndices = CollectionUtils.select((Collection)this._indices, (Predicate)new Predicate(){

            public boolean evaluate(Object input) {
                return !((Index)input).isUnique();
            }
        });
        return new Index[nonUniqueIndices.size()];
    }

    public Index[] getUniqueIndices() {
        Collection uniqueIndices = CollectionUtils.select((Collection)this._indices, (Predicate)new Predicate(){

            public boolean evaluate(Object input) {
                return ((Index)input).isUnique();
            }
        });
        return new Index[uniqueIndices.size()];
    }

    public void removeIndex(Index index) {
        if (index != null) {
            this._indices.remove(index);
        }
    }

    public void removeIndex(int idx) {
        this._indices.remove(idx);
    }

    public boolean hasPrimaryKey() {
        for (Column column : this._columns) {
            if (!column.isPrimaryKey()) continue;
            return true;
        }
        return false;
    }

    public Column findColumn(String name) {
        return this.findColumn(name, false);
    }

    public Column findColumn(String name, boolean caseSensitive) {
        for (Column column : this._columns) {
            if (!(caseSensitive ? column.getName().equals(name) : column.getName().equalsIgnoreCase(name))) continue;
            return column;
        }
        return null;
    }

    public int getColumnIndex(Column column) {
        int idx = 0;
        Iterator it = this._columns.iterator();
        while (it.hasNext()) {
            if (column == it.next()) {
                return idx;
            }
            ++idx;
        }
        return -1;
    }

    public Index findIndex(String name) {
        return this.findIndex(name, false);
    }

    public Index findIndex(String name, boolean caseSensitive) {
        int idx = 0;
        while (idx < this.getIndexCount()) {
            Index index = this.getIndex(idx);
            if (caseSensitive ? index.getName().equals(name) : index.getName().equalsIgnoreCase(name)) {
                return index;
            }
            ++idx;
        }
        return null;
    }

    public ForeignKey findForeignKey(ForeignKey key) {
        int idx = 0;
        while (idx < this.getForeignKeyCount()) {
            ForeignKey fk = this.getForeignKey(idx);
            if (fk.equals(key)) {
                return fk;
            }
            ++idx;
        }
        return null;
    }

    public ForeignKey findForeignKey(ForeignKey key, boolean caseSensitive) {
        int idx = 0;
        while (idx < this.getForeignKeyCount()) {
            ForeignKey fk = this.getForeignKey(idx);
            if (caseSensitive && fk.equals(key) || !caseSensitive && fk.equalsIgnoreCase(key)) {
                return fk;
            }
            ++idx;
        }
        return null;
    }

    public ForeignKey getSelfReferencingForeignKey() {
        int idx = 0;
        while (idx < this.getForeignKeyCount()) {
            ForeignKey fk = this.getForeignKey(idx);
            if (this.equals(fk.getForeignTable())) {
                return fk;
            }
            ++idx;
        }
        return null;
    }

    public Column[] getPrimaryKeyColumns() {
        Collection pkColumns = CollectionUtils.select((Collection)this._columns, (Predicate)new Predicate(){

            public boolean evaluate(Object input) {
                return ((Column)input).isPrimaryKey();
            }
        });
        return new Column[pkColumns.size()];
    }

    public Column[] getAutoIncrementColumns() {
        Collection autoIncrColumns = CollectionUtils.select((Collection)this._columns, (Predicate)new Predicate(){

            public boolean evaluate(Object input) {
                return ((Column)input).isAutoIncrement();
            }
        });
        return new Column[autoIncrColumns.size()];
    }

    public void sortForeignKeys(final boolean caseSensitive) {
        if (!this._foreignKeys.isEmpty()) {
            final Collator collator = Collator.getInstance();
            Collections.sort(this._foreignKeys, new Comparator(){

                public int compare(Object obj1, Object obj2) {
                    String fk1Name = ((ForeignKey)obj1).getName();
                    String fk2Name = ((ForeignKey)obj2).getName();
                    if (!caseSensitive) {
                        fk1Name = fk1Name != null ? fk1Name.toLowerCase() : null;
                        fk2Name = fk2Name != null ? fk2Name.toLowerCase() : null;
                    }
                    return collator.compare(fk1Name, fk2Name);
                }
            });
        }
    }

    public Object clone() throws CloneNotSupportedException {
        Table result = (Table)super.clone();
        result._catalog = this._catalog;
        result._schema = this._schema;
        result._name = this._name;
        result._type = this._type;
        result._columns = (ArrayList)this._columns.clone();
        result._foreignKeys = (ArrayList)this._foreignKeys.clone();
        result._indices = (ArrayList)this._indices.clone();
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Table) {
            Table other = (Table)obj;
            return new EqualsBuilder().append((Object)this._name, (Object)other._name).append((Object)this._columns, (Object)other._columns).append(new HashSet(this._foreignKeys), new HashSet(other._foreignKeys)).append(new HashSet(this._indices), new HashSet(other._indices)).isEquals();
        }
        return false;
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).append((Object)this._name).append((Object)this._columns).append(new HashSet(this._foreignKeys)).append(new HashSet(this._indices)).toHashCode();
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("Table [name=");
        result.append(this.getName());
        result.append("; ");
        result.append(this.getColumnCount());
        result.append(" columns]");
        return result.toString();
    }

    public String toVerboseString() {
        StringBuffer result = new StringBuffer();
        result.append("Table [name=");
        result.append(this.getName());
        result.append("; catalog=");
        result.append(this.getCatalog());
        result.append("; schema=");
        result.append(this.getCatalog());
        result.append("; type=");
        result.append(this.getType());
        result.append("] columns:");
        int idx = 0;
        while (idx < this.getColumnCount()) {
            result.append(" ");
            result.append(this.getColumn(idx).toVerboseString());
            ++idx;
        }
        result.append("; indices:");
        idx = 0;
        while (idx < this.getIndexCount()) {
            result.append(" ");
            result.append(this.getIndex(idx).toVerboseString());
            ++idx;
        }
        result.append("; foreign keys:");
        idx = 0;
        while (idx < this.getForeignKeyCount()) {
            result.append(" ");
            result.append(this.getForeignKey(idx).toVerboseString());
            ++idx;
        }
        return result.toString();
    }
}


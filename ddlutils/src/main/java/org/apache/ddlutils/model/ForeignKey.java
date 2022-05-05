/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.collections.set.ListOrderedSet
 *  org.apache.commons.lang.builder.EqualsBuilder
 *  org.apache.commons.lang.builder.HashCodeBuilder
 */
package org.apache.ddlutils.model;

import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Reference;
import org.apache.ddlutils.model.Table;

public class ForeignKey
implements Cloneable {
    private String _name;
    private Table _foreignTable;
    private String _foreignTableName;
    private ListOrderedSet _references = new ListOrderedSet();
    private boolean _autoIndexPresent;
    private String onDeleteAction = "CASCADE";

    public ForeignKey() {
        this(null);
    }

    public ForeignKey(String name) {
        this._name = name;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public Table getForeignTable() {
        return this._foreignTable;
    }

    public void setForeignTable(Table foreignTable) {
        this._foreignTable = foreignTable;
        this._foreignTableName = foreignTable == null ? null : foreignTable.getName();
    }

    public String getForeignTableName() {
        return this._foreignTableName;
    }

    public void setForeignTableName(String foreignTableName) {
        if (this._foreignTable != null && !this._foreignTable.getName().equals(foreignTableName)) {
            this._foreignTable = null;
        }
        this._foreignTableName = foreignTableName;
    }

    public int getReferenceCount() {
        return this._references.size();
    }

    public Reference getReference(int idx) {
        return (Reference)this._references.get(idx);
    }

    public Reference[] getReferences() {
        return (Reference[])this._references.toArray((Object[])new Reference[this._references.size()]);
    }

    public Reference getFirstReference() {
        return (Reference)(this._references.isEmpty() ? null : this._references.get(0));
    }

    public void addReference(Reference reference) {
        if (reference != null) {
            int idx = 0;
            while (idx < this._references.size()) {
                Reference curRef = this.getReference(idx);
                if (curRef.getSequenceValue() > reference.getSequenceValue()) {
                    this._references.add(idx, (Object)reference);
                    return;
                }
                ++idx;
            }
            this._references.add((Object)reference);
        }
    }

    public void removeReference(Reference reference) {
        if (reference != null) {
            this._references.remove((Object)reference);
        }
    }

    public void removeReference(int idx) {
        this._references.remove(idx);
    }

    public boolean hasLocalColumn(Column column) {
        int idx = 0;
        while (idx < this.getReferenceCount()) {
            if (column.equals(this.getReference(idx).getLocalColumn())) {
                return true;
            }
            ++idx;
        }
        return false;
    }

    public boolean hasForeignColumn(Column column) {
        int idx = 0;
        while (idx < this.getReferenceCount()) {
            if (column.equals(this.getReference(idx).getForeignColumn())) {
                return true;
            }
            ++idx;
        }
        return false;
    }

    public boolean isAutoIndexPresent() {
        return this._autoIndexPresent;
    }

    public void setAutoIndexPresent(boolean autoIndexPresent) {
        this._autoIndexPresent = autoIndexPresent;
    }

    public Object clone() throws CloneNotSupportedException {
        ForeignKey result = (ForeignKey)super.clone();
        result._name = this._name;
        result._foreignTableName = this._foreignTableName;
        result._references = new ListOrderedSet();
        result.onDeleteAction = this.onDeleteAction;
        Iterator it = this._references.iterator();
        while (it.hasNext()) {
            result._references.add(((Reference)it.next()).clone());
        }
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ForeignKey) {
            ForeignKey otherFk = (ForeignKey)obj;
            EqualsBuilder builder = new EqualsBuilder();
            if (this._name != null && this._name.length() > 0 && otherFk._name != null && otherFk._name.length() > 0) {
                builder.append((Object)this._name, (Object)otherFk._name);
            }
            return builder.append((Object)this._foreignTableName, (Object)otherFk._foreignTableName).append((Object)this._references, (Object)otherFk._references).isEquals();
        }
        return false;
    }

    public boolean equalsIgnoreCase(ForeignKey otherFk) {
        boolean checkName;
        boolean bl = checkName = this._name != null && this._name.length() > 0 && otherFk._name != null && otherFk._name.length() > 0;
        if ((!checkName || this._name.equalsIgnoreCase(otherFk._name)) && this._foreignTableName.equalsIgnoreCase(otherFk._foreignTableName)) {
            HashSet otherRefs = new HashSet();
            otherRefs.addAll(otherFk._references);
            for (Object curLocalRefObj : this._references) {
                if(curLocalRefObj instanceof Reference){
                    Reference curLocalRef = (Reference) curLocalRefObj;
                boolean found = false;
                Iterator otherIt = otherRefs.iterator();
                while (otherIt.hasNext()) {
                    Reference curOtherRef = (Reference)otherIt.next();
                    if (!curLocalRef.equalsIgnoreCase(curOtherRef)) continue;
                    otherIt.remove();
                    found = true;
                    break;
                }
                if (found) continue;
                return false;
            }}
            return otherRefs.isEmpty();
        }
        return false;
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).append((Object)this._name).append((Object)this._foreignTableName).append((Object)this._references).toHashCode();
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("Foreign key [");
        if (this.getName() != null && this.getName().length() > 0) {
            result.append("name=");
            result.append(this.getName());
            result.append("; ");
        }
        result.append("foreign table=");
        result.append(this.getForeignTableName());
        result.append("; ");
        result.append(this.getReferenceCount());
        result.append(" references]");
        return result.toString();
    }

    public String toVerboseString() {
        StringBuffer result = new StringBuffer();
        result.append("ForeignK ky [");
        if (this.getName() != null && this.getName().length() > 0) {
            result.append("name=");
            result.append(this.getName());
            result.append("; ");
        }
        result.append("foreign table=");
        result.append(this.getForeignTableName());
        result.append("] references:");
        int idx = 0;
        while (idx < this.getReferenceCount()) {
            result.append(" ");
            result.append(this.getReference(idx).toString());
            ++idx;
        }
        return result.toString();
    }

    public void setOnDeleteAction(String onDeleteAction) {
        this.onDeleteAction = onDeleteAction;
    }

    public String getOnDeleteAction() {
        return this.onDeleteAction;
    }
}


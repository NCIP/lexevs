/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.DynaBean
 *  org.apache.commons.collections.set.ListOrderedSet
 */
package org.apache.ddlutils.io;

import java.util.Iterator;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.ddlutils.io.Identity;

public class WaitingObject {
    private DynaBean _obj;
    private Identity _objIdentity;
    private ListOrderedSet _waitedForIdentites = new ListOrderedSet();

    public WaitingObject(DynaBean obj, Identity objIdentity) {
        this._obj = obj;
        this._objIdentity = objIdentity;
    }

    public DynaBean getObject() {
        return this._obj;
    }

    public void addPendingFK(Identity fkIdentity) {
        this._waitedForIdentites.add((Object)fkIdentity);
    }

    public Iterator getPendingFKs() {
        return this._waitedForIdentites.iterator();
    }

    public Identity removePendingFK(Identity fkIdentity) {
        Identity result = null;
        int idx = this._waitedForIdentites.indexOf((Object)fkIdentity);
        if (idx >= 0) {
            result = (Identity)this._waitedForIdentites.get(idx);
            this._waitedForIdentites.remove(idx);
        }
        return result;
    }

    public boolean hasPendingFKs() {
        return !this._waitedForIdentites.isEmpty();
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(this._objIdentity);
        result.append(" waiting for ");
        result.append(this._waitedForIdentites.toString());
        return result.toString();
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.collections.Predicate
 */
package org.apache.ddlutils.util;

import org.apache.commons.collections.Predicate;

public class MultiInstanceofPredicate
implements Predicate {
    private Class[] _typesToCheck;

    public MultiInstanceofPredicate(Class[] typesToCheck) {
        this._typesToCheck = typesToCheck;
    }

    public boolean evaluate(Object obj) {
        if (this._typesToCheck == null || this._typesToCheck.length == 0) {
            return true;
        }
        Class<?> typeOfObj = obj.getClass();
        int idx = 0;
        while (idx < this._typesToCheck.length) {
            if (this._typesToCheck[idx].isAssignableFrom(typeOfObj)) {
                return true;
            }
            ++idx;
        }
        return false;
    }
}


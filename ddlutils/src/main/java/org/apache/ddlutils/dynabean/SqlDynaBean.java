/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.BasicDynaBean
 *  org.apache.commons.beanutils.DynaClass
 *  org.apache.commons.beanutils.DynaProperty
 */
package org.apache.ddlutils.dynabean;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;

public class SqlDynaBean
extends BasicDynaBean {
    private static final long serialVersionUID = -6946514447446174227L;

    public SqlDynaBean(DynaClass dynaClass) {
        super(dynaClass);
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        DynaClass type = this.getDynaClass();
        DynaProperty[] props = type.getDynaProperties();
        result.append(type.getName());
        result.append(": ");
        int idx = 0;
        while (idx < props.length) {
            if (idx > 0) {
                result.append(", ");
            }
            result.append(props[idx].getName());
            result.append(" = ");
            result.append(this.get(props[idx].getName()));
            ++idx;
        }
        return result.toString();
    }
}


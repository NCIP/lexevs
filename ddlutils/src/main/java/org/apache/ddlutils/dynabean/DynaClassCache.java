/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.BeanUtils
 *  org.apache.commons.beanutils.DynaBean
 *  org.apache.commons.beanutils.DynaClass
 */
package org.apache.ddlutils.dynabean;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.ddlutils.dynabean.SqlDynaClass;
import org.apache.ddlutils.dynabean.SqlDynaException;
import org.apache.ddlutils.model.Table;

public class DynaClassCache {
    private Map _dynaClassCache = new HashMap();

    public DynaBean createNewInstance(Table table) throws SqlDynaException {
        try {
            return this.getDynaClass(table).newInstance();
        }
        catch (InstantiationException ex) {
            throw new SqlDynaException("Could not create a new dyna bean for table " + table.getName(), ex);
        }
        catch (IllegalAccessException ex) {
            throw new SqlDynaException("Could not create a new dyna bean for table " + table.getName(), ex);
        }
    }

    public DynaBean copy(Table table, Object source) throws SqlDynaException {
        DynaBean answer = this.createNewInstance(table);
        try {
            BeanUtils.copyProperties((Object)answer, (Object)source);
        }
        catch (InvocationTargetException ex) {
            throw new SqlDynaException("Could not populate the bean", ex);
        }
        catch (IllegalAccessException ex) {
            throw new SqlDynaException("Could not populate the bean", ex);
        }
        return answer;
    }

    public SqlDynaClass getDynaClass(Table table) {
        SqlDynaClass answer = (SqlDynaClass)((Object)this._dynaClassCache.get(table.getName()));
        if (answer == null) {
            answer = this.createDynaClass(table);
            this._dynaClassCache.put(table.getName(), answer);
        }
        return answer;
    }

    public SqlDynaClass getDynaClass(DynaBean dynaBean) throws SqlDynaException {
        DynaClass dynaClass = dynaBean.getDynaClass();
        if (dynaClass instanceof SqlDynaClass) {
            return (SqlDynaClass)dynaClass;
        }
        throw new SqlDynaException("The dyna bean is not an instance of a SqlDynaClass");
    }

    private SqlDynaClass createDynaClass(Table table) {
        return SqlDynaClass.newInstance(table);
    }
}


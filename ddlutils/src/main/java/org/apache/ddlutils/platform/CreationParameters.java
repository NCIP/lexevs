/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.collections.map.ListOrderedMap
 */
package org.apache.ddlutils.platform;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.ddlutils.model.Table;

public class CreationParameters {
    private Map _parametersPerTable = new HashMap();

    public Map getParametersFor(Table table) {
        ListOrderedMap result = new ListOrderedMap();
        Map globalParams = (Map)this._parametersPerTable.get(null);
        Map tableParams = (Map)this._parametersPerTable.get(table);
        if (globalParams != null) {
            result.putAll(globalParams);
        }
        if (tableParams != null) {
            result.putAll(tableParams);
        }
        return result;
    }

    public void addParameter(Table table, String paramName, String paramValue) {
        Map params = (Map)this._parametersPerTable.get(table);
        if (params == null) {
            params = new ListOrderedMap();
            this._parametersPerTable.put(table, params);
        }
        params.put(paramName, paramValue);
    }
}


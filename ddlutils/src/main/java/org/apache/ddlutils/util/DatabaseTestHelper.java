/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  junit.framework.Assert
 *  junit.framework.AssertionFailedError
 *  org.apache.commons.beanutils.DynaBean
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 */
package org.apache.ddlutils.util;

import java.util.Iterator;
import java.util.List;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

public class DatabaseTestHelper
extends Assert {
    private final Log _log = LogFactory.getLog(DatabaseTestHelper.class);

    public void assertHasSameData(Database model, Platform origDbPlatform, Platform testedDbPlatform) {
        this.assertHasSameData(null, model, origDbPlatform, testedDbPlatform);
    }

    public void assertHasSameData(String failureMsg, Database model, Platform origDbPlatform, Platform testedDbPlatform) {
        boolean hasError = false;
        int idx = 0;
        while (idx < model.getTableCount()) {
            Table table = model.getTable(idx);
            Column[] pkCols = table.getPrimaryKeyColumns();
            Iterator it = origDbPlatform.query(model, this.buildQueryString(origDbPlatform, table, null, null), new Table[]{table});
            while (it.hasNext()) {
                DynaBean obj = (DynaBean)it.next();
                List result = testedDbPlatform.fetch(model, this.buildQueryString(origDbPlatform, table, pkCols, obj), new Table[]{table});
                if (result.isEmpty()) {
                    if (this._log.isDebugEnabled()) {
                        hasError = true;
                        this._log.debug((Object)("Row " + obj.toString() + " is not present in second database"));
                        continue;
                    }
                    throw new AssertionFailedError(failureMsg);
                }
                if (result.size() > 1) {
                    if (this._log.isDebugEnabled()) {
                        hasError = true;
                        StringBuffer debugMsg = new StringBuffer();
                        debugMsg.append("Row ");
                        debugMsg.append(obj.toString());
                        debugMsg.append(" is present more than once in the second database:\n");
                        Iterator resultIt = result.iterator();
                        while (resultIt.hasNext()) {
                            debugMsg.append("  ");
                            debugMsg.append(resultIt.next().toString());
                        }
                        this._log.debug((Object)debugMsg.toString());
                        continue;
                    }
                    throw new AssertionFailedError(failureMsg);
                }
                DynaBean otherObj = (DynaBean)result.iterator().next();
                if (obj.equals(otherObj)) continue;
                if (this._log.isDebugEnabled()) {
                    hasError = true;
                    this._log.debug((Object)("Row " + obj.toString() + " is different in the second database: " + otherObj.toString()));
                    continue;
                }
                throw new AssertionFailedError(failureMsg);
            }
            ++idx;
        }
        if (hasError) {
            throw new AssertionFailedError(failureMsg);
        }
    }

    private String buildQueryString(Platform targetPlatform, Table table, Column[] whereCols, DynaBean whereValues) {
        StringBuffer result = new StringBuffer();
        result.append("SELECT * FROM ");
        if (targetPlatform.isDelimitedIdentifierModeOn()) {
            result.append(targetPlatform.getPlatformInfo().getDelimiterToken());
        }
        result.append(table.getName());
        if (targetPlatform.isDelimitedIdentifierModeOn()) {
            result.append(targetPlatform.getPlatformInfo().getDelimiterToken());
        }
        if (whereCols != null && whereCols.length > 0) {
            result.append(" WHERE ");
            int idx = 0;
            while (idx < whereCols.length) {
                Object value;
                Object object = value = whereValues == null ? null : whereValues.get(whereCols[idx].getName());
                if (idx > 0) {
                    result.append(" AND ");
                }
                if (targetPlatform.isDelimitedIdentifierModeOn()) {
                    result.append(targetPlatform.getPlatformInfo().getDelimiterToken());
                }
                result.append(whereCols[idx].getName());
                if (targetPlatform.isDelimitedIdentifierModeOn()) {
                    result.append(targetPlatform.getPlatformInfo().getDelimiterToken());
                }
                result.append(" = ");
                if (value == null) {
                    result.append("NULL");
                } else {
                    if (!whereCols[idx].isOfNumericType()) {
                        result.append(targetPlatform.getPlatformInfo().getValueQuoteToken());
                    }
                    result.append(value.toString());
                    if (!whereCols[idx].isOfNumericType()) {
                        result.append(targetPlatform.getPlatformInfo().getValueQuoteToken());
                    }
                }
                ++idx;
            }
        }
        return result.toString();
    }
}


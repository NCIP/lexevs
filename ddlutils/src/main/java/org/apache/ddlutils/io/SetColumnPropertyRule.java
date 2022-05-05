/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.PropertyUtils
 *  org.apache.commons.digester.Rule
 */
package org.apache.ddlutils.io;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.digester.Rule;
import org.apache.ddlutils.io.converters.SqlTypeConverter;
import org.apache.ddlutils.model.Column;
import org.xml.sax.Attributes;

public class SetColumnPropertyRule
extends Rule {
    private Column _column;
    private SqlTypeConverter _converter;
    private boolean _caseSensitive;

    public SetColumnPropertyRule(Column column, SqlTypeConverter converter, boolean beCaseSensitive) {
        this._column = column;
        this._converter = converter;
        this._caseSensitive = beCaseSensitive;
    }

    public void begin(Attributes attributes) throws Exception {
        Object bean = this.digester.peek();
        int idx = 0;
        while (idx < attributes.getLength()) {
            String attrName = attributes.getLocalName(idx);
            if ("".equals(attrName)) {
                attrName = attributes.getQName(idx);
            }
            if (this._caseSensitive && attrName.equals(this._column.getName()) || !this._caseSensitive && attrName.equalsIgnoreCase(this._column.getName())) {
                String propValue;
                String attrValue = attributes.getValue(idx);
                String string = propValue = this._converter != null ? (String) this._converter.convertFromString(attrValue, this._column.getTypeCode()) : attrValue;
                if (this.digester.getLogger().isDebugEnabled()) {
                    this.digester.getLogger().debug((Object)("[SetColumnPropertyRule]{" + this.digester.getMatch() + "} Setting property '" + this._column.getName() + "' to '" + propValue + "'"));
                }
                PropertyUtils.setProperty((Object)bean, (String)this._column.getName(), (Object)propValue);
            }
            ++idx;
        }
    }
}


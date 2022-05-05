/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.PropertyUtils
 *  org.apache.commons.codec.binary.Base64
 *  org.apache.commons.digester.Rule
 */
package org.apache.ddlutils.io;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.digester.Rule;
import org.apache.ddlutils.io.converters.SqlTypeConverter;
import org.apache.ddlutils.model.Column;
import org.xml.sax.Attributes;

public class SetColumnPropertyFromSubElementRule
extends Rule {
    private Column _column;
    private SqlTypeConverter _converter;
    private boolean _usesBase64 = false;

    public SetColumnPropertyFromSubElementRule(Column column, SqlTypeConverter converter) {
        this._column = column;
        this._converter = converter;
    }

    public void begin(Attributes attributes) throws Exception {
        int idx = 0;
        while (idx < attributes.getLength()) {
            String attrName = attributes.getLocalName(idx);
            if ("".equals(attrName)) {
                attrName = attributes.getQName(idx);
            }
            if ("base64".equals(attrName) && "true".equalsIgnoreCase(attributes.getValue(idx))) {
                this._usesBase64 = true;
                break;
            }
            ++idx;
        }
    }

    public void end() throws Exception {
        this._usesBase64 = false;
    }

    public void body(String text) throws Exception {
        String propValue;
        String attrValue = text.trim();
        if (this._usesBase64 && attrValue != null) {
            attrValue = new String(Base64.decodeBase64((byte[])attrValue.getBytes()));
        }
        String string = propValue = this._converter != null ? (String) this._converter.convertFromString(attrValue, this._column.getTypeCode()) : attrValue;
        if (this.digester.getLogger().isDebugEnabled()) {
            this.digester.getLogger().debug((Object)("[SetColumnPropertyFromSubElementRule]{" + this.digester.getMatch() + "} Setting property '" + this._column.getName() + "' to '" + propValue + "'"));
        }
        PropertyUtils.setProperty((Object)this.digester.peek(), (String)this._column.getName(), (Object)propValue);
    }
}


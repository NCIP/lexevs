/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.digester.Rule
 *  org.apache.commons.digester.RulesBase
 */
package org.apache.ddlutils.io;

import java.util.List;
import org.apache.commons.digester.Rule;
import org.apache.commons.digester.RulesBase;

public class DigesterRules
extends RulesBase {
    private boolean _caseSensitive = false;

    public boolean isCaseSensitive() {
        return this._caseSensitive;
    }

    public void setCaseSensitive(boolean beCaseSensitive) {
        this._caseSensitive = beCaseSensitive;
    }

    public void add(String pattern, Rule rule) {
        super.add(this._caseSensitive ? pattern : pattern.toLowerCase(), rule);
    }

    protected List lookup(String namespaceURI, String pattern) {
        return super.lookup(namespaceURI, this._caseSensitive ? pattern : pattern.toLowerCase());
    }
}


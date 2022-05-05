/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.DynaBean
 *  org.apache.commons.digester.Rule
 */
package org.apache.ddlutils.io;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.digester.Rule;
import org.apache.ddlutils.io.DataSink;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.xml.sax.Attributes;

public class DynaSqlCreateRule
extends Rule {
    private Database _model;
    private Table _table;
    private DataSink _receiver;

    public DynaSqlCreateRule(Database model, Table table, DataSink receiver) {
        this._model = model;
        this._table = table;
        this._receiver = receiver;
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        DynaBean instance = this._model.createDynaBeanFor(this._table);
        if (this.digester.getLogger().isDebugEnabled()) {
            this.digester.getLogger().debug((Object)("[DynaSqlCreateRule]{" + this.digester.getMatch() + "} New dyna bean '" + this._table.getName() + "' created"));
        }
        this.digester.push((Object)instance);
    }

    public void end(String namespace, String name) throws Exception {
        DynaBean top = (DynaBean)this.digester.pop();
        if (this.digester.getLogger().isDebugEnabled()) {
            this.digester.getLogger().debug((Object)("[DynaSqlCreateRule]{" + this.digester.getMatch() + "} Pop " + top.getDynaClass().getName()));
        }
        this._receiver.addBean(top);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.alteration;

import org.apache.ddlutils.alteration.ModelChange;
import org.apache.ddlutils.model.Table;

public interface TableChange
extends ModelChange {
    public Table getChangedTable();
}


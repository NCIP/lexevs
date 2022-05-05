/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.alteration;

import org.apache.ddlutils.alteration.TableChange;
import org.apache.ddlutils.model.Column;

public interface ColumnChange
extends TableChange {
    public Column getChangedColumn();
}


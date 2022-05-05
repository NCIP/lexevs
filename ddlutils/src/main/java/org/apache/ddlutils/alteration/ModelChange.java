/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.alteration;

import org.apache.ddlutils.model.Database;

public interface ModelChange {
    public void apply(Database var1, boolean var2);
}


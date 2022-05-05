/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.DynaBean
 */
package org.apache.ddlutils.io;

import org.apache.commons.beanutils.DynaBean;
import org.apache.ddlutils.io.DataSinkException;

public interface DataSink {
    public void start() throws DataSinkException;

    public void addBean(DynaBean var1) throws DataSinkException;

    public void end() throws DataSinkException;
}


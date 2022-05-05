/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.mysql;

import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.platform.mysql.MySql50ModelReader;
import org.apache.ddlutils.platform.mysql.MySqlPlatform;

public class MySql50Platform
extends MySqlPlatform {
    public static final String DATABASENAME = "MySQL5";

    public MySql50Platform() {
        PlatformInfo info = this.getPlatformInfo();
        info.setSyntheticDefaultValueForRequiredReturned(false);
        this.setModelReader(new MySql50ModelReader(this));
    }

    @Override
    public String getName() {
        return DATABASENAME;
    }
}


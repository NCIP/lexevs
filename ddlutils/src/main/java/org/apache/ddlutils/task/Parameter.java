/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.task;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.ddlutils.PlatformFactory;

public class Parameter {
    private String _name;
    private String _value;
    private Set<String> _platforms = new HashSet();

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getValue() {
        return this._value;
    }

    public void setValue(String value) {
        this._value = value;
    }

    public void setPlatforms(String platforms) {
        this._platforms.clear();
        if (platforms != null) {
            StringTokenizer tokenizer = new StringTokenizer(platforms, ",");
            while (tokenizer.hasMoreTokens()) {
                String platform = tokenizer.nextToken().trim();
                if (PlatformFactory.isPlatformSupported(platform)) {
                    this._platforms.add(platform.toLowerCase());
                    continue;
                }
                throw new IllegalArgumentException("Platform " + platform + " is not supported");
            }
        }
    }

    public boolean isForPlatform(String platformName) {
        return this._platforms.isEmpty() || this._platforms.contains(platformName.toLowerCase());
    }
}


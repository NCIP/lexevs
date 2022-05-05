/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Level
 *  org.apache.tools.ant.types.EnumeratedAttribute
 */
package org.apache.ddlutils.task;

import org.apache.logging.log4j.Level;
import org.apache.tools.ant.types.EnumeratedAttribute;

public class VerbosityLevel
extends EnumeratedAttribute {
    private static final String[] LEVELS = new String[]{Level.FATAL.toString().toUpperCase(), Level.ERROR.toString().toUpperCase(), Level.WARN.toString().toUpperCase(), Level.INFO.toString().toUpperCase(), Level.DEBUG.toString().toUpperCase(), Level.FATAL.toString().toLowerCase(), Level.ERROR.toString().toLowerCase(), Level.WARN.toString().toLowerCase(), Level.INFO.toString().toLowerCase(), Level.DEBUG.toString().toLowerCase()};

    public VerbosityLevel() {
    }

    public VerbosityLevel(String level) {
        this.setValue(level);
    }

    public String[] getValues() {
        return LEVELS;
    }

    public boolean isDebug() {
        return Level.DEBUG.toString().equalsIgnoreCase(this.getValue());
    }
}


/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.util.config.parameter;

/**
 * An application parameter of a boolean type.
 */
import java.io.Serializable;

public class BooleanParameter implements Serializable {
    private boolean value;// = null;
    private String usage;

    public BooleanParameter(boolean value) {
        this.value = value;
        usage = "";
    }

    public BooleanParameter(String value) {
        setValue(value);
        usage = "";
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    protected boolean setValue(String value) {

        if (value.equalsIgnoreCase("t") || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("y")
                || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("-1")
                || value.equalsIgnoreCase("+"))
            this.value = true;
        else if (value.equalsIgnoreCase("f") || value.equalsIgnoreCase("false") || value.equalsIgnoreCase("n")
                || value.equalsIgnoreCase("no") || value.equalsIgnoreCase("0") || value.equalsIgnoreCase("-"))
            this.value = false;
        else
            return false;
        return true;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
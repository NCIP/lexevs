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
package org.LexGrid.util;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * A couple of utility type methods.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
public class Utility {

    public static String stringArrayToString(String[] string) {
        if (string == null)
            return "Null";

        StringBuffer foo = new StringBuffer();
        for (int i = 0; i < string.length; i++) {
            foo.append(string[i]);
            if (i != string.length - 1)
                foo.append("|");
        }
        return foo.toString();
    }

    /**
     * This method is for printing out a hashtable (keys and values). It assumes
     * that the keys are strings.
     * 
     * @param hashTable
     *            - The hashtable to print
     * @param keysToObscure
     *            - If one of the keys is sensitive, like a password, you can
     *            have it not be printed.
     * @return - The String representing the hashtable.
     */
    public static String hashTableToString(Hashtable hashTable, String[] keysToObscure) {
        if (hashTable == null)
            return "Null";

        if (keysToObscure == null)
            keysToObscure = new String[] {};

        StringBuffer foo = new StringBuffer();
        Enumeration enumeration = hashTable.keys();
        boolean obscure = false;
        while (enumeration.hasMoreElements()) {
            Object current = enumeration.nextElement();
            for (int i = 0; i < keysToObscure.length; i++) {
                if (current.toString().equals(keysToObscure[i]))
                    obscure = true;
            }
            if (obscure) {
                foo.append(current.toString() + ":" + "**********" + System.getProperty("line.separator"));
                obscure = false; // set for next time
            } else
                foo.append(current.toString() + ":" + hashTable.get(current) + System.getProperty("line.separator"));
        }
        return foo.toString();
    }

    /**
     * This method is for printing out a hashtable (keys and values). It works
     * best if the keys and values are strings.
     * 
     * @param hashTable
     *            - The hashtable to print
     * @return - The String representing the hashtable.
     */
    public static String hashTableToString(Hashtable hashTable) {
        if (hashTable == null)
            return "Null";
        StringBuffer foo = new StringBuffer();
        Enumeration enumeration = hashTable.keys();
        while (enumeration.hasMoreElements()) {
            Object current = enumeration.nextElement();
            foo.append(current.toString() + ":" + hashTable.get(current) + System.getProperty("line.separator"));
        }
        return foo.toString();
    }

    /**
     * method removes the trailing spaces in a string and returns it. Returns
     * null if str is null.
     * 
     * @param str
     * @return trimmed string
     */
    public static String trim(String str) {
        if (str != null) {
            return str.trim();
        }
        return null;
    }
}
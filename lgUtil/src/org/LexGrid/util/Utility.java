
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
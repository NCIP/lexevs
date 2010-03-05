/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package edu.mayo.informatics.lexgrid.convert.emfConversions.obo1_2;

import java.util.Collection;
import java.util.Iterator;

/**
 * OBO To EMF Implementation.
 * 
 * @author <A HREF="mailto:dks02@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: 2637 $ checked in on $Date: 2006-06-27
 *          02:08:04 +0000 (Tue, 27 Jun 2006) $
 */
public class OBO2EMFUtils {
    /**
     * Compares a string to null, "null" or just with empty string
     * 
     * @param String
     *            -- input string
     * @return boolean -- true if comparison succeeds, otherwise false.
     */
    public static boolean isNull(String str) {
        return ((str == null) || ("".equals(str)) || ("null".equals(str)));
    }

    public static String getWithOBOURN(String item) {
        if (!isNull(item))
            return OBO2EMFConstants.OBO_URN + item;

        return item;
    }

    public static String toCamelCase(String str) {
        if (isNull(str))
            return str;

        String result = str;
        String conv = str.replaceAll("\t", " ");

        if (conv.indexOf(" ") != -1) {
            String[] words = conv.split(" ");

            if (words.length > 1) {
                result = words[0];

                for (int i = 1; i < words.length; i++) {
                    String current = words[i].trim();

                    if (!isNull(current))
                        result += current.substring(0, 1).toUpperCase() + current.substring(1);
                }
            }
        } else
            return str;

        return result;
    }

    public static String toNMTokenNotNeeded(String str) {
        if (isNull(str))
            return str;

        String result = str;

        String conv = str.trim().replaceAll("\\s+", "_");
        conv = conv.replace('/', '_');
        conv = conv.replaceAll("[^a-zA-Z0-9._-]", "");

        if (conv.length() > 1 && conv.substring(0, 1).matches("[0-9]"))
            conv = "_" + conv;

        result = conv;

        return removeInvalidXMLCharacters(result, "test");
    }

    public static String removeInvalidXMLCharacters(String str, String concept) {
        if (isNull(str))
            return str;

        String result = str;
        result = result.replaceAll("[\\x00-\\x08\\x0b-\\x1f\\x7f]", "");
        return result;
    }

    public static boolean containsIgnoreCase(Collection<String> c, String str) {

        Iterator<String> it = c.iterator();
        while (it.hasNext()) {
            String temp = it.next();
            if (temp.equalsIgnoreCase(str)) {
                return true;
            }

        }
        return false;

    }
}
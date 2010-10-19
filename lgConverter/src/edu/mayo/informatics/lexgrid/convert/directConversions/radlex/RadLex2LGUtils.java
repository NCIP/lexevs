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
package edu.mayo.informatics.lexgrid.convert.directConversions.radlex;

/*
 * @author <A HREF="mailto:dks02@mayo.edu">Deepak Sharma</A> 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * 
 * @version subversion $Revision: 2917 $ checked in on $Date: 2006-06-19
 *          15:52:21 +0000 (Mon, 19 Jun 2006) $
 */

public class RadLex2LGUtils {
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

    public static String getWithRadlexURN(String item) {
        if (!isNull(item))
            return RadLex2LGConstants.Radlex_URN + RadLex2LGConstants.URN_DELIM + toNMToken(item);

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

    public static String toNMToken(String str) {
        if (isNull(str))
            return str;

        String result = str;

        try {
            String conv = str.trim().replaceAll("\\s+", "_");
            conv = conv.replace('/', '_');
            conv = conv.replaceAll("[^a-zA-Z0-9._-]", "");

            if (conv.substring(0, 1).matches("[0-9]"))
                conv = "_" + conv;

            result = conv;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return removeInvalidXMLCharacters(result, "test");
    }

    public static String toSourceFormat(String str) {
        if (isNull(str))
            return str;

        String result = str;

        try {
            String conv = null;

            // what is it?
            if (!"Rosse MD".equals(str)) {
                conv = str;
                if (conv.indexOf(":") != -1)
                    conv = str.split(":")[0];
                if (conv.indexOf(".p") != -1)
                    conv = conv.split(".p")[0];
                if (conv.indexOf("p.") != -1)
                    conv = conv.split("p.")[0];
                if (conv.indexOf(",p.") != -1)
                    conv = conv.split(",p.")[0];
                if (conv.indexOf("(p.)") != -1)
                    conv = conv.split("(p.)")[0];
            }

            result = toNMToken(conv);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String removeInvalidXMLCharacters(String str, String concept) {
        if (isNull(str))
            return str;

        String result = str;

        try {

            result = result.replaceAll("[\\x00-\\x08\\x0b-\\x1f\\x7f]", "");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
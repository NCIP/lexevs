
package edu.mayo.informatics.lexgrid.convert.directConversions.obo1_2;

import java.util.Collection;
import java.util.Iterator;

/**
 * OBO To EMF Implementation.
 * 
 * @author <A HREF="mailto:dks02@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: 2637 $ checked in on $Date: 2006-06-27
 *          02:08:04 +0000 (Tue, 27 Jun 2006) $
 */
public class OBO2LGUtils {
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
            return OBO2LGConstants.OBO_URN + item;

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
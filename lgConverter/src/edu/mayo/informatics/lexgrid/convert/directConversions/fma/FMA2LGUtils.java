
package edu.mayo.informatics.lexgrid.convert.directConversions.fma;

/*
 * @author <A HREF="mailto:dks02@mayo.edu">Deepak Sharma</A> 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * 
 * @version subversion $Revision: 2917 $ checked in on $Date: 2006-06-19
 *          15:52:21 +0000 (Mon, 19 Jun 2006) $
 */

public class FMA2LGUtils {
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

    public static String getWithFMAURN(String item) {
        if (!isNull(item))
            return FMA2LGConstants.FMA_URN + FMA2LGConstants.URN_DELIM + toNMToken(item);

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
            /*
             * boolean found = false; char [] conv = str.toCharArray();
             * 
             * if ((conv != null)&&(conv.length > 0)) { for (int i=0; i <
             * conv.length; i++) { char c = conv[i]; int v =
             * Character.getNumericValue(c); if (v < 32) { if
             * (result.indexOf(conv[i]) != -1) { found = true;
             * System.out.println("***Trouble Character=" +
             * Character.getNumericValue(conv[i])); result.replace(conv[i], '
             * '); } } } }
             * 
             * if (found) { System.out.println("Found trouble character for
             * concept=" + concept + " Value=" + str); }
             */

            result = result.replaceAll("[\\x00-\\x08\\x0b-\\x1f\\x7f]", "");

            /*
             * if (!result.equals(str)) { System.out.println(">>>>>>String
             * changed for concept=" + concept); System.out.println("###OLD=" +
             * str); System.out.println("***NEW=" + result); }
             */
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
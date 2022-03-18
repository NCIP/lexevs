
package edu.mayo.informatics.resourcereader.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Convenient methods for String manupulation
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class StringUtils {
    /**
     * Compares a string to null, "null" or just with empty string
     * 
     * @param str
     *            -- input string
     * @return boolean -- true if comparison succeeds, otherwise false.
     */

    private static final String fWHITESPACE_AND_QUOTES = " \t\r\n\"";
    private static final String fQUOTES_ONLY = "\"";
    private static final String ESCAPED_COMMA = "@ESCAPEDCOMMA@";

    public static boolean isNull(String str) {
        return ((str == null) || ("".equals(str.trim())) || ("null".equalsIgnoreCase((str))));
    }

    public static String makeStringList(Collection c) {
        String str = "";
        for (Iterator e = c.iterator(); e.hasNext();) {
            str += c.toString() + "\n";
        }
        return str;

    }

    /**
     * 
     * @param sentence
     * @return A Vector of words in the string. Words in double quotes are
     *         treated as a single term. Example
     *         sentence="UK_SPELLING \"British spelling\" EXACT" returns
     *         UK_SPELLING, British spelling, EXACT
     */
    public static Vector<String> makeWordVectorOfSentence(String sentence) {
        // the parser flips between these two sets of delimiters
        Vector<String> result = new Vector<String>();
        boolean returnTokens = true;

        String currentDelims = fWHITESPACE_AND_QUOTES;
        if (isNull(sentence)) {
            return result;
        }
        StringTokenizer parser = new StringTokenizer(sentence, currentDelims, returnTokens);

        String token = null;
        while (parser.hasMoreTokens()) {
            token = parser.nextToken(currentDelims);
            if (!isDoubleQuote(token)) {
                addNonTrivialWordToResult(token, result, currentDelims);
            } else {
                currentDelims = flipDelimiters(currentDelims);
            }
        }
        return result;

    }

    private static void addNonTrivialWordToResult(String str, Vector<String> result, String currentDelims) {
        if (str != null && str.trim().length() != 0) {
            if (currentDelims.equalsIgnoreCase(fQUOTES_ONLY)) {
                result.add("\"" + str + "\"");
            } else {
                result.add(str);
            }
        }
    }

    private static boolean isDoubleQuote(String aToken) {
        return aToken.equals(fQUOTES_ONLY);
    }

    private static String flipDelimiters(String aCurrentDelims) {
        String result = null;
        if (aCurrentDelims.equals(fWHITESPACE_AND_QUOTES)) {
            result = fQUOTES_ONLY;
        } else {
            result = fWHITESPACE_AND_QUOTES;
        }
        return result;
    }

    public static String parseAsSimpleKeyValue(String source, String keyString) {
        if ((!StringUtils.isNull(source)) && (!StringUtils.isNull(keyString)) && (source.startsWith(keyString)))
            return removeComments(source.substring(keyString.length()).trim());

        return null;
    }

    public static String removeComments(String str) {
        if (str != null) {
            if (str.indexOf("!") != -1) {
                str = (str.split("!")[0]).trim();
            }
        }

        return str;
    }
    
    /**
     * This function replaces comma's that are in quoted sentences with a @escapedcomma@ tag.
     * For example, the string=abc "Run, Jump and play" gets transformed to
     * string=abc "Run@escapedcomma@ Jump and play"
     * @param str
     * @return
     */
    public static String escapeQuotedComma(String str) {

        String returnStr = "";
        int startPos = 0;
        
        while (startPos != -1) {
            int posFirstQuote = str.indexOf("\"", startPos);
            if (posFirstQuote != -1) {
                int secondQuote = str.indexOf("\"", posFirstQuote+1);
                if (secondQuote != -1) {
                    String quotedStr = str.substring(posFirstQuote, secondQuote+1);
                    String escapedQuotedStr = quotedStr.replaceAll(",", ESCAPED_COMMA);
                    returnStr += str.substring(startPos, posFirstQuote) + escapedQuotedStr;
                    startPos = secondQuote+1;
                } else {
                    returnStr += str.substring(startPos);
                    startPos = -1;
                }
            } else {
                returnStr += str.substring(startPos);
                startPos= -1;
            }
        }
        return returnStr;

    }

    
    public static String unEscapeQuotedComma(String str) {
        String unEscapedStr = str.replaceAll(ESCAPED_COMMA, ",");
        return unEscapedStr;

    }    
    /**
     * Split a string taking care not to split the escaped regex sequence For
     * example, if str= "ab \, cd, efg" and the regex="," then the function
     * should return ab \,cd and efg. We also need to ensure that we do not split
     * on commas that are within quotes. For example in
     * str="neutrophil, stem cell, think" , aa:bb, cc:dd "EFD aaa" 
     * we do not want to split "neutrophil, stem cell, think" apart.
     * 
     * @param str
     * @param regex
     * @return
     */
    public static String[] splitEscapedString(String str, String regex) {
        String[] list = new String[0];
        if (str != null) {
            String escaped_regex = "\\\\" + regex;
            str = str.replaceAll(escaped_regex, "@PRADIP@");
            str= escapeQuotedComma(str);
            
            list = str.split(regex);
            for (int i = 0; i < list.length; i++) {
                list[i] = list[i].replaceAll("@PRADIP@", escaped_regex);
                list[i]= unEscapeQuotedComma(list[i]);
            }

        }

        return list;
    }

    public static String removeOuterMostQuotes(String str) {
        if (str != null) {
            str= str.trim();
            if (str.indexOf("\"")== 0 && str.lastIndexOf("\"")== str.length() -1 && str.indexOf("\"") < str.lastIndexOf("\"") ) {
                str= str.substring(1, str.length() -1);
            }            
        }
        return str; 
    }
    
    private static void printForTest(String str) {
        String escapedstr, unescapedstr;        
        escapedstr= escapeQuotedComma(str);
        unescapedstr= unEscapeQuotedComma(escapedstr);
        System.out.println("Original ="+str);
        System.out.println("Escaped  ="+escapedstr);
        System.out.println("Unescaped="+unescapedstr);
        System.out.println("\n"); 
    }
    
    
    public static void main(String[] args) {
        String str;
        str= "synonym: \"neutrophil, stem cell, think\" , aa:bb, cc:dd \"EFD aaa\" ";
        printForTest(str);
        str= "[DOI:10.1007/BF02814484 \"Theissen G (2005) Birth, life and death of developmental control genes: New challenges for the homology concept. Theory in Biosciences 124:199-212\", DOI:10.1007/s00427-003-0301-4 \"Nielsen C and Martinez P (2003) Patterns of gene expression: homology or homocracy? Development Genes and Evolution 213: 149-154\", DOI:10.1186/1742-9994-2-15 \"Sanetra M et al. (2005) Conservation and co-option in developmental programmes: the importance of homology relationships. Frontiers in Zoology 2:15\"]";
        printForTest(str);
        str= "Birth,\"";
        printForTest(str);
        str= "\"\"";
        printForTest(str);
        str= "\"";
        printForTest(str);

    }
}
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
package org.lexevs.dao.indexer.api.generators;

import java.util.HashSet;
import java.util.Set;

/**
 * This class will generate a query for you to use in searching.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
public class QueryGenerator {

    public static String removeExtraWhiteSpaceCharacters(String searchString, Set whiteSpaceCharsFromAnalyzer) {
        StringBuffer temp = new StringBuffer(searchString);

        for (int i = 0; i < temp.length(); i++) {
            // if we have found a character that we are supposed to remove
            Character current = new Character(temp.charAt(i));

            if (whiteSpaceCharsFromAnalyzer.contains(current)) {
                int start = i;
                int end = i + 1;

                boolean remove = true;
                boolean isSpecial = false;
                // remove it, unless it is an unescaped QueryParser special
                // character.
                // characters that are special to the query parser must be
                // escaped before I will strip
                // them ahead of time. If they are not escaped, then the
                // QueryParser deals with them.

                if (isQueryParserSpecialString(current.toString())) {
                    remove = false;
                    isSpecial = true;
                }

                // There are a couple of 2 character special strings
                if (temp.length() > i + 1) {
                    String a = temp.substring(i, i + 2);
                    if (isQueryParserSpecialString(a)) {
                        remove = false;
                        isSpecial = true;
                        end = end + 1;
                    }
                }

                // see if it is escaped.
                if (isSpecial && i > 0) {
                    char prev = temp.charAt(i - 1);
                    if (prev == '\\') {
                        remove = true;
                        start = start - 1;
                    }

                }

                if (remove && start + 1 == end) {
                    temp.setCharAt(i, ' ');
                } else if (remove) {
                    temp.replace(start, end, " ");
                }

            }
        }
        return temp.toString();
    }

    private static Set queryParserSpecialStrings = null;

    private static boolean isQueryParserSpecialString(String str) {
        if (queryParserSpecialStrings == null) {
            // one time population
            queryParserSpecialStrings = new HashSet();
            queryParserSpecialStrings.add("+");
            queryParserSpecialStrings.add("-");
            queryParserSpecialStrings.add("&&");
            queryParserSpecialStrings.add("||");
            queryParserSpecialStrings.add("!");
            queryParserSpecialStrings.add("(");
            queryParserSpecialStrings.add(")");
            queryParserSpecialStrings.add("{");
            queryParserSpecialStrings.add("}");
            queryParserSpecialStrings.add("[");
            queryParserSpecialStrings.add("]");
            queryParserSpecialStrings.add("^");
            queryParserSpecialStrings.add("\"");
            queryParserSpecialStrings.add("~");
            queryParserSpecialStrings.add("*");
            queryParserSpecialStrings.add("?");
            queryParserSpecialStrings.add(":");
            queryParserSpecialStrings.add("\\");
            queryParserSpecialStrings.add("'");
        }
        return queryParserSpecialStrings.contains(str);
    }

}
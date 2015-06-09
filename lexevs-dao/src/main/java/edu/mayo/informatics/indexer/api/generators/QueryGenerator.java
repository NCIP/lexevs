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
package edu.mayo.informatics.indexer.api.generators;

import java.util.HashSet;
import java.util.Set;

//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.queryParser.ParseException;
//import org.apache.lucene.queryParser.QueryParser;
//import org.apache.lucene.queryParser.TokenMgrError;
//import org.apache.lucene.search.Query;
//
//import edu.mayo.informatics.indexer.lucene.Index;
//import edu.mayo.informatics.indexer.lucene.analyzers.EncoderAnalyzer;
//import edu.mayo.informatics.indexer.lucene.analyzers.FieldSkippingAnalyzer;
//import edu.mayo.informatics.indexer.lucene.analyzers.NormAnalyzer;
//import edu.mayo.informatics.indexer.lucene.analyzers.WhiteSpaceLowerCaseAnalyzer;

/**
 * This class will generate a query for you to use in searching.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
public class QueryGenerator {
//    private Analyzer analyzer_;
//    private QueryParser parser_;
//    private Set additionalWhiteSpaceChars_;
//
//    /**
//     * Constructs a default QueryGenerator, it will use a StandardAnalyzer.
//     */
//    public QueryGenerator() {
//        analyzer_ = new StandardAnalyzer();
//        additionalWhiteSpaceChars_ = null;
//        parser_ = new QueryParser(Index.UNIQUE_DOCUMENT_IDENTIFIER_FIELD, analyzer_);
//    }
//
//    /**
//     * Constructs a QueryGenerator using the given analyzer. If your Analyzer is
//     * setup to remove certain characters (like the WhiteSpaceLowerCaseAnalyzer
//     * does) or break on additional characters then it will "do the right thing"
//     * with the analyzer and the QueryParser for the remove and white space
//     * characters.
//     * 
//     * @param analyzer
//     *            The analyzer to use in construction of the index
//     */
//    public QueryGenerator(Analyzer analyzer) {
//        analyzer_ = analyzer;
//        additionalWhiteSpaceChars_ = null;
//        WhiteSpaceLowerCaseAnalyzer temp = null;
//
//        // The WhiteSpaceLowerCaseAnalyzer has an option to use custom
//        // characters as space
//        // characters, but the query parser doesn't support these. So, i have to
//        // manually make
//        // the same substitutions before passing things into the query parser.
//        // I'll construct a new analyzer so it doesn't duplicate work, and
//        // confuse the issue.
//        // I go through every one of my analyzers that uses the
//        // WhiteSpaceLowerCaseAnalyzer.
//        if (analyzer_ instanceof FieldSkippingAnalyzer) {
//            if (((FieldSkippingAnalyzer) analyzer_).getSuppliedAnalyzer() instanceof WhiteSpaceLowerCaseAnalyzer) {
//                // pull out the analyser.
//                temp = (WhiteSpaceLowerCaseAnalyzer) ((FieldSkippingAnalyzer) analyzer_).getSuppliedAnalyzer();
//                // blank the char removal table.
//                ((FieldSkippingAnalyzer) analyzer_).setSuppliedAnalyzer(new WhiteSpaceLowerCaseAnalyzer(temp
//                        .getCurrentStopWordTable(), temp.getCurrentCharRemovalTable(), null));
//            } else if (((FieldSkippingAnalyzer) analyzer_).getSuppliedAnalyzer() instanceof NormAnalyzer) {
//                // pull out the analyser.
//                temp = ((NormAnalyzer) ((FieldSkippingAnalyzer) analyzer_).getSuppliedAnalyzer())
//                        .getWhiteSpaceLowerCaseAnalyzer();
//                // blank the char removal table.
//                ((NormAnalyzer) ((FieldSkippingAnalyzer) analyzer_).getSuppliedAnalyzer())
//                        .setWhiteSpaceLowerCaseAnalyzer(new WhiteSpaceLowerCaseAnalyzer(temp.getCurrentStopWordTable(),
//                                temp.getCurrentCharRemovalTable(), null));
//            } else if (((FieldSkippingAnalyzer) analyzer_).getSuppliedAnalyzer() instanceof EncoderAnalyzer) {
//                // pull out the analyser.
//                temp = ((EncoderAnalyzer) ((FieldSkippingAnalyzer) analyzer_).getSuppliedAnalyzer())
//                        .getWhiteSpaceLowerCaseAnalyzer();
//                // blank the char removal table.
//                ((EncoderAnalyzer) ((FieldSkippingAnalyzer) analyzer_).getSuppliedAnalyzer())
//                        .setWhiteSpaceLowerCaseAnalyzer(new WhiteSpaceLowerCaseAnalyzer(temp.getCurrentStopWordTable(),
//                                temp.getCurrentCharRemovalTable(), null));
//            }
//        } else if (analyzer_ instanceof NormAnalyzer) {
//            // pull out the analyser.
//            temp = ((NormAnalyzer) analyzer_).getWhiteSpaceLowerCaseAnalyzer();
//            // blank the char removal table.
//            ((NormAnalyzer) analyzer_).setWhiteSpaceLowerCaseAnalyzer(new WhiteSpaceLowerCaseAnalyzer(temp
//                    .getCurrentStopWordTable(), temp.getCurrentCharRemovalTable(), null));
//        } else if (analyzer_ instanceof EncoderAnalyzer) {
//            // pull out the analyser.
//            temp = ((EncoderAnalyzer) analyzer_).getWhiteSpaceLowerCaseAnalyzer();
//            // blank the char removal table.
//            ((EncoderAnalyzer) analyzer_).setWhiteSpaceLowerCaseAnalyzer(new WhiteSpaceLowerCaseAnalyzer(temp
//                    .getCurrentStopWordTable(), temp.getCurrentCharRemovalTable(), null));
//        } else if (analyzer_ instanceof WhiteSpaceLowerCaseAnalyzer) {
//            // pull out the analyser.
//            temp = ((WhiteSpaceLowerCaseAnalyzer) analyzer_);
//            // blank the char removal table.
//            analyzer_ = new WhiteSpaceLowerCaseAnalyzer(temp.getCurrentStopWordTable(), temp
//                    .getCurrentCharRemovalTable(), null);
//        }
//
//        // if we found a whiteSpaceLowerCaseAnalyser, pull out its removal
//        // characters.
//        if (temp != null) {
//            additionalWhiteSpaceChars_ = temp.getCurrentWhiteSpaceEquivalentTable();
//        }
//
//        parser_ = new QueryParser(Index.UNIQUE_DOCUMENT_IDENTIFIER_FIELD, analyzer_);
//    }
//
//    /**
//     * @param searchString
//     *            The string to search for. See
//     *            http://jakarta.apache.org/lucene/docs/queryparsersyntax.html
//     *            for details on what can be parsed.
//     * @param fields
//     *            What fields of the index to search for this string.
//     * @return An indexer ready query.
//     * @throws ParseException
//     * @throws TokenMgrError
//     */
//    public Query createQuery(String searchString, String[] fields) throws ParseException, TokenMgrError {
//        if (additionalWhiteSpaceChars_ != null && additionalWhiteSpaceChars_.size() > 0) {
//            // change additional characters to spaces as necessary.
//            searchString = removeExtraWhiteSpaceCharacters(searchString, additionalWhiteSpaceChars_);
//        }
//
//        StringBuffer queryString = new StringBuffer();
//
//        for (int i = 0; i < fields.length; i++) {
//            queryString.append(fields[i] + ":(" + searchString + ") ");
//        }
//        return parser_.parse(queryString.toString());
//    }
//
//    /**
//     * Lucene's Query Parser has the following special characters: + - && || ! (
//     * ) { } [ ] ^ " ~ * ? : \
//     * 
//     * If you want to write a query that contains any of these characters, you
//     * must escape them (prefix with \)
//     * 
//     * My analyzers also give you the ability to remove characters. If you ask
//     * the analyzer to treat characters like ',' as white space, then you should
//     * use this method on the search string, to make sure that user queries that
//     * contain ',' also treat ',' as white space.
//     * 
//     * If you remove a character like '-' which is also special to the
//     * QueryParser, then you need to ensure that the user query has the escape
//     * character in front of the '-'. This method will then turn the escape
//     * character and the '-' into white space.
//     * 
//     * @param searchString
//     *            Search string to modify.
//     * @param whiteSpaceCharsFromAnalyzer
//     *            Set of Character objects to remove. Same as used in your
//     *            Analyzer.
//     */

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

//    /**
//     * Change the analyzer of an index. You MUST call this method if you
//     * constructed the index with an analyzer of your own. You always must use
//     * the same Analyzer as you constructed the index with.
//     * 
//     * @param analyzer
//     */
//    public void setAnalyzer(Analyzer analyzer) {
//        analyzer_ = analyzer;
//        parser_ = new QueryParser(Index.UNIQUE_DOCUMENT_IDENTIFIER_FIELD, analyzer_);
//    }

}
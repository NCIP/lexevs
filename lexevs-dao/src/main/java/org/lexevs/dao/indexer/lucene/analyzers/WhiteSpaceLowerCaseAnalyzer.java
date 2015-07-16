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
package org.lexevs.dao.indexer.lucene.analyzers;

import java.io.Reader;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.lexevs.dao.indexer.lucene.filters.CharRemovingFilter;
import org.lexevs.dao.indexer.lucene.tokenizers.CustomWhiteSpaceTokenizer;

/**
 * This analyzer uses the WhiteSpaceTokenizer, LowerCaseFilter, and StopFilter.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class WhiteSpaceLowerCaseAnalyzer extends Analyzer {
    /*
     * Stop table
     */
    private Set stopTable = null;

    /*
     * chars to remove
     */
    private Set charRemovalTable = null;

    /*
     * chars to treat as whitespace
     */

    private Set charWhiteSpaceTable = null;

    /**
     * Construct the WhiteSpaceLowerCase analyzer, using the stop words from the
     * Standard Analyzer. Uses default character removal rules/whitespace
     * 
     * @see #getDefaultCharRemovalSet()
     * @see #getDefaultWhiteSpaceSet()
     */
    public WhiteSpaceLowerCaseAnalyzer() {
        super();
        stopTable = StopFilter.makeStopSet((List<?>) StandardAnalyzer.STOP_WORDS_SET);
        charRemovalTable = CharRemovingFilter.makeCharRemovalSet(getDefaultCharRemovalSet());
        charWhiteSpaceTable = CustomWhiteSpaceTokenizer.makeCharWhiteSpaceSet(getDefaultWhiteSpaceSet());
    }

    /**
     * Default characters to remove from indexed content. , . / \ ` ' " + * = @
     * # $ % ^ & ? !
     * 
     * Note that this does not include the underscore - '_'
     */

    public static char[] getDefaultCharRemovalSet() {
        return new char[] { ',', '.', '/', '\\', '`', '\'', '"', '+', '*', '=', '@', '#', '$', '%', '^', '&', '?', '!' };
    }

    /**
     * Default characters to treat as whitespace (in addition to standard
     * whitespace characters). - : ; ( ) { } [ ] < > |
     * 
     * Note that this does not include the underscore - '_'
     */
    public static char[] getDefaultWhiteSpaceSet() {
        return new char[] { '-', ':', ';', '(', ')', '{', '}', '[', ']', '<', '>', '|' };
    }

    /**
     * Construct the WhiteSpaceLowerCase analyzer, using the provided stop
     * words.
     * 
     * @param stopWords
     *            - Stop words to use. Null or empty causes it to not use stop
     *            words.
     * @param charsToRemove
     *            - Characters to strip from input. null or empty causes it to
     *            not remove any characters. @see getDefaultCharRemovalSet for a
     *            recommended set of characters to to remove from input.
     * @param charsToTreatAsWhitespace
     *            - Characters to treat as whitespace (or split points in the
     *            tokenization) null or empty causes it to just split on
     *            whitespace.
     */
    public WhiteSpaceLowerCaseAnalyzer(String[] stopWords, char[] charsToRemove, char[] charsToTreatAsWhitespace) {
        super();
        if (stopWords == null || stopWords.length == 0) {
            stopTable = null;
        } else {
            stopTable = StopFilter.makeStopSet(stopWords);
        }

        if (charsToRemove == null || charsToRemove.length == 0) {
            charRemovalTable = null;
        } else {
            charRemovalTable = CharRemovingFilter.makeCharRemovalSet(charsToRemove);
        }

        if (charsToTreatAsWhitespace == null || charsToTreatAsWhitespace.length == 0) {
            charWhiteSpaceTable = null;
        } else {
            charWhiteSpaceTable = CustomWhiteSpaceTokenizer.makeCharWhiteSpaceSet(charsToTreatAsWhitespace);
        }
    }

    /**
     * Construct the WhiteSpaceLowerCase analyzer, using the provided stop
     * words.
     * 
     * @param stopWords
     *            - Stop words to use. Null or empty causes it to not use stop
     *            words.
     * @param charsToRemove
     *            - Characters to strip from input. null or empty causes it to
     *            not remove any characters. @see getDefaultCharRemovalSet for a
     *            recommended set of characters to to remove from input.
     * @param charsToTreatAsWhitespace
     *            - Characters to treat as whitespace (or split points in the
     *            tokenization) null or empty causes it to just split on
     *            whitespace.
     */
    public WhiteSpaceLowerCaseAnalyzer(Set stopWords, Set charsToRemove, Set charsToTreatAsWhitespace) {
        super();
        if (stopWords == null || stopWords.size() == 0) {
            stopTable = null;
        } else {
            stopTable = stopWords;
        }

        if (charsToRemove == null || charsToRemove.size() == 0) {
            charRemovalTable = null;
        } else {
            charRemovalTable = charsToRemove;
        }

        if (charsToTreatAsWhitespace == null || charsToTreatAsWhitespace.size() == 0) {
            charWhiteSpaceTable = null;
        } else {
            charWhiteSpaceTable = charsToTreatAsWhitespace;
        }
    }

    /*
     * Create a token stream for this analyzer.
     */
      // CANNOT OVERRIDE FINAL METHOD
//    public final TokenStream tokenStream(String fieldname, final Reader reader) {
//        TokenStream result;
//        if (charWhiteSpaceTable == null) {
//            result = new WhitespaceTokenizer(reader);
//        } else {
//            result = new CustomWhiteSpaceTokenizer(reader, charWhiteSpaceTable);
//        }
//        result = new LowerCaseFilter(result);
//        if (charRemovalTable != null) {
//            result = new CharRemovingFilter(result, charRemovalTable);
//        }
//        if (stopTable != null) {
//            result = new StopFilter(result, stopTable);
//        }
//        return result;
//    }

    public Set getCurrentCharRemovalTable() {
        return this.charRemovalTable;
    }

    public Set getCurrentWhiteSpaceEquivalentTable() {
        return this.charWhiteSpaceTable;
    }

    public Set getCurrentStopWordTable() {
        return this.stopTable;
    }

	@Override
	protected TokenStreamComponents createComponents(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
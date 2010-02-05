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
package edu.mayo.informatics.indexer.lucene.analyzers;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.tartarus.snowball.ext.EnglishStemmer;

import edu.mayo.informatics.indexer.lucene.filters.SnowballFilter;

/**
 * This is an analyzer that uses Snowball to stem each term before it is
 * inserted into the index.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class SnowballAnalyzer extends Analyzer {
    private boolean keepOrigional_ = false;
    private String snowballName_;

    private WhiteSpaceLowerCaseAnalyzer wslc;

    /**
     * Create a new SnowballAnalyzer. Uses all defaults in the @see
     * WhiteSpaceLowerCaseAnalyzer, and the default snowball name "English".
     */
    public SnowballAnalyzer() {
        super();
        keepOrigional_ = false;
        wslc = new WhiteSpaceLowerCaseAnalyzer();
        snowballName_ = "English";
    }

    /**
     * Create a Snowball analyzer.
     * 
     * @param keepOrigional
     * @param snowballName
     *            - Available stemmers are listed in
     *            {@link org.tartarus.snowball.ext} . The name of a stemmer is
     *            the part of the class name before "Stemmer", e.g., the stemmer
     *            in {@link EnglishStemmer} is named "English".
     * @param stopWords
     *            - Stop words to use - not used if null or empty.
     * @param charsToRemove
     *            - characters to remove from input (before norm) - not used if
     *            null or empty.
     * @param charsToTreatAsWhiteSpace
     *            - characters to treat as whitespace (split points) - defaults
     *            to typical whitespace if null or empty.
     */
    public SnowballAnalyzer(boolean keepOrigional, String snowballName, String[] stopWords, char[] charsToRemove,
            char[] charsToTreatAsWhiteSpace) {
        super();
        this.keepOrigional_ = keepOrigional;
        wslc = new WhiteSpaceLowerCaseAnalyzer(stopWords, charsToRemove, charsToTreatAsWhiteSpace);
        snowballName_ = snowballName;
    }

    /**
     * Create a Snowball analyzer.
     * 
     * @param keepOrigional
     * @param snowballName
     *            - Available stemmers are listed in
     *            {@link org.tartarus.snowball.ext} . The name of a stemmer is
     *            the part of the class name before "Stemmer", e.g., the stemmer
     *            in {@link EnglishStemmer} is named "English". Uses all
     *            defaults in the @see WhiteSpaceLowerCaseAnalyzer.
     */
    public SnowballAnalyzer(boolean keepOrigional, String snowballName) {
        super();
        this.keepOrigional_ = keepOrigional;
        wslc = new WhiteSpaceLowerCaseAnalyzer();
        snowballName_ = snowballName;
    }

    /*
     * Create a token stream for this analyzer.
     */
    public final TokenStream tokenStream(String fieldname, final Reader reader) {
        TokenStream result = wslc.tokenStream(fieldname, reader);
        return result = new SnowballFilter(snowballName_, result, keepOrigional_);
    }

    /**
     * This method should not be part of the public API - but design
     * requirements require it to be public. Do not use this method.
     */
    public WhiteSpaceLowerCaseAnalyzer getWhiteSpaceLowerCaseAnalyzer() {
        return this.wslc;
    }

    /**
     * This method should not be part of the public API - but design
     * requirements require it to be public. Do not use this method.
     */
    public void setWhiteSpaceLowerCaseAnalyzer(WhiteSpaceLowerCaseAnalyzer whiteSpaceLowerCaseAnalyzer) {
        this.wslc = whiteSpaceLowerCaseAnalyzer;
    }
}
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

import org.apache.commons.codec.Encoder;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.lexevs.dao.indexer.lucene.filters.EncoderFilter;

/**
 * This is an analyzer that generates codes for each token to index. Uses the
 * Apache commons coded package.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class EncoderAnalyzer extends Analyzer {
    private Encoder encoder_;

    private WhiteSpaceLowerCaseAnalyzer wslc;

    /**
     * Create a new PhonetixAnalyzer. Uses all defaults in the
     * 
     * @see WhiteSpaceLowerCaseAnalyzer and a DoubleMetaphone generator set to
     *      the default values.
     * 
     */
    public EncoderAnalyzer() {
        super();
        wslc = new WhiteSpaceLowerCaseAnalyzer();
        encoder_ = new DoubleMetaphone();
    }

    /**
     * Create a new EncoderAnalyzer - everything configured by the user.
     * 
     * @param encoder
     *            - The encoder to use. DoubleMetaphone, Metaphone, Soundex,
     *            etc.
     * @param stopWords
     *            - Stop words to use - not used if null or empty.
     * @param charsToRemove
     *            - characters to remove from input (before encoding) - not used
     *            if null or empty.
     * @param charsToTreatAsWhiteSpace
     *            - characters to treat as whitespace (split points) - defaults
     *            to typical whitespace if null or empty.
     */
    public EncoderAnalyzer(Encoder encoder, String[] stopWords, char[] charsToRemove, char[] charsToTreatAsWhiteSpace) {
        super();
        wslc = new WhiteSpaceLowerCaseAnalyzer(stopWords, charsToRemove, charsToTreatAsWhiteSpace);
        encoder_ = encoder;
    }

    /**
     * Create a new EncoderAnalyzer - uses a default configured DoubleMetaphone
     * encoder.
     * 
     * @param stopWords
     *            - Stop words to use - not used if null or empty.
     * @param charsToRemove
     *            - characters to remove from input (before encoding) - not used
     *            if null or empty.
     * @param charsToTreatAsWhiteSpace
     *            - characters to treat as whitespace (split points) - defaults
     *            to typical whitespace if null or empty.
     */
    public EncoderAnalyzer(String[] stopWords, char[] charsToRemove, char[] charsToTreatAsWhiteSpace) {
        super();
        wslc = new WhiteSpaceLowerCaseAnalyzer(stopWords, charsToRemove, charsToTreatAsWhiteSpace);
        encoder_ = new DoubleMetaphone();
    }

    /**
     * The lvg config file location is required. Uses all defaults in the
     * 
     * @see WhiteSpaceLowerCaseAnalyzer
     * @param encoder
     */
    public EncoderAnalyzer(Encoder encoder) {
        super();
        wslc = new WhiteSpaceLowerCaseAnalyzer();
        encoder_ = encoder;
    }

    /**
     * Create a token stream for this analyzer.
     */
    public final TokenStream tokenStream(String fieldname, final Reader reader) {
        TokenStream result = wslc.tokenStream(fieldname, reader);
        return result = new EncoderFilter(result, encoder_);
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
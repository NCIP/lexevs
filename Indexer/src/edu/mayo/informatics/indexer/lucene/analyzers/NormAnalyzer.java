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
package edu.mayo.informatics.indexer.lucene.analyzers;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import edu.mayo.informatics.indexer.lucene.filters.NormFilter;
import edu.mayo.informatics.indexer.utility.CachedNormApi;

/**
 * This is an analyzer that uses LVG to normalize each term before it is
 * inserted into the index.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class NormAnalyzer extends Analyzer {
    private CachedNormApi norm_;
    private boolean keepOrigional_ = false;

    private WhiteSpaceLowerCaseAnalyzer wslc;

    public static String LVG_CONFIG_FILE_ABSOLUTE = "\\\\mir04\\LargeData\\LargeInstallers\\lvg2005Portable\\lvg2005\\data\\config\\lvg.properties";
    public static int LVG_CACHE_SIZE = 10000;

    /**
     * Create a new NormAnalyzer. Uses all defaults in the @see
     * WhiteSpaceLowerCaseAnalyzer, and config file location from the
     * LVG_CONFIGJ_FILE_ABSOLUTE variable.
     * 
     */
    public NormAnalyzer() {
        super();
        keepOrigional_ = false;
        wslc = new WhiteSpaceLowerCaseAnalyzer();
        connectNorm();
    }

    /**
     * The lvg config file location is required.
     * 
     * @param lvgConfigFileLocation
     * @param keepOrigional
     * @param stopWords
     *            - Stop words to use - not used if null or empty.
     * @param charsToRemove
     *            - characters to remove from input (before norm) - not used if
     *            null or empty.
     * @param charsToTreatAsWhiteSpace
     *            - characters to treat as whitespace (split points) - defaults
     *            to typical whitespace if null or empty.
     */
    public NormAnalyzer(String lvgConfigFileLocation, boolean keepOrigional, String[] stopWords, char[] charsToRemove,
            char[] charsToTreatAsWhiteSpace) {
        super();
        this.keepOrigional_ = keepOrigional;
        NormAnalyzer.LVG_CONFIG_FILE_ABSOLUTE = lvgConfigFileLocation;
        wslc = new WhiteSpaceLowerCaseAnalyzer(stopWords, charsToRemove, charsToTreatAsWhiteSpace);

        connectNorm();
    }

    /**
     * Create a norm analyzer. Uses preset LVG_CONFIG_File value.
     * 
     * @param keepOrigional
     * @param stopWords
     *            - Stop words to use - not used if null or empty.
     * @param charsToRemove
     *            - characters to remove from input (before norm) - not used if
     *            null or empty.
     * @param charsToTreatAsWhiteSpace
     *            - characters to treat as whitespace (split points) - defaults
     *            to typical whitespace if null or empty.
     */
    public NormAnalyzer(boolean keepOrigional, String[] stopWords, char[] charsToRemove, char[] charsToTreatAsWhiteSpace) {
        super();
        this.keepOrigional_ = keepOrigional;
        wslc = new WhiteSpaceLowerCaseAnalyzer(stopWords, charsToRemove, charsToTreatAsWhiteSpace);
        connectNorm();
    }

    /**
     * The lvg config file location is required. Uses all defaults in the @see
     * WhiteSpaceLowerCaseAnalyzer.
     * 
     * @param lvgConfigFileLocation
     * @param keepOrigional
     */
    public NormAnalyzer(String lvgConfigFileLocation, boolean keepOrigional) {
        super();
        this.keepOrigional_ = keepOrigional;
        NormAnalyzer.LVG_CONFIG_FILE_ABSOLUTE = lvgConfigFileLocation;
        wslc = new WhiteSpaceLowerCaseAnalyzer();
        connectNorm();
    }

    public NormAnalyzer(boolean keepOrigional) {
        super();
        this.keepOrigional_ = keepOrigional;
        wslc = new WhiteSpaceLowerCaseAnalyzer();
        connectNorm();
    }

    private void connectNorm() {
        norm_ = new CachedNormApi(NormAnalyzer.LVG_CONFIG_FILE_ABSOLUTE);
    }

    /*
     * Create a token stream for this analyzer.
     */
    public final TokenStream tokenStream(String fieldname, final Reader reader) {
        TokenStream result = wslc.tokenStream(fieldname, reader);
        return result = new NormFilter(result, norm_, keepOrigional_);
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
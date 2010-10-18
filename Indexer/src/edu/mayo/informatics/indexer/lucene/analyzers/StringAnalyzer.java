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
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.TokenStream;

import edu.mayo.informatics.indexer.lucene.filters.StringFilter;

/**
 * This analyzer will only break things into tokens at the occurrence of a given
 * string. It doesn't do anything else ( no lowercasing, removing of characters,
 * etc) This is useful when you have a series of terms that are already
 * tokenized by an outside process - and you want to enter them into a field in
 * a lucene document as the same series of tokens.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
// TODO - this analyzer needs testing.
public class StringAnalyzer extends Analyzer {
    private String tokenString_;
    private KeywordAnalyzer baseAnalyzer_;

    public StringAnalyzer(String stringToTokenizeOn) {
        tokenString_ = stringToTokenizeOn;
        baseAnalyzer_ = new KeywordAnalyzer();
    }

    public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream filter = baseAnalyzer_.tokenStream(fieldName, reader);
        return new StringFilter(filter, tokenString_);
    }
}
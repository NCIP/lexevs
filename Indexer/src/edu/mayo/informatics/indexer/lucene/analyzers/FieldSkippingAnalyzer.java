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
import java.util.HashSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharTokenizer;
import org.apache.lucene.analysis.TokenStream;

/**
 * This is an analyzer that allows you to search on both tokenized and
 * non-tokenized Fields. Supply the list of fields that are untokenized, and it
 * won't analyze them.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class FieldSkippingAnalyzer extends Analyzer {
    private Analyzer suppliedAnalyzer_;
    private HashSet fieldsToSkip_;

    public FieldSkippingAnalyzer(String[] fieldsToSkip, Analyzer analyzer) {
        suppliedAnalyzer_ = analyzer;
        fieldsToSkip_ = new HashSet();
        for (int i = 0; i < fieldsToSkip.length; i++) {
            fieldsToSkip_.add(fieldsToSkip[i]);
        }
    }

    public TokenStream tokenStream(String field, final Reader reader) {
        // do not tokenize fields in the skip set.
        if (fieldsToSkip_.contains(field)) {
            return new CharTokenizer(reader) {
                protected boolean isTokenChar(char c) {
                    return true;
                }
            };
        } else {
            // use supplied analyzer
            return suppliedAnalyzer_.tokenStream(field, reader);
        }
    }

    public Analyzer getSuppliedAnalyzer() {
        return this.suppliedAnalyzer_;
    }

    public void setSuppliedAnalyzer(Analyzer suppliedAnalyzer) {
        this.suppliedAnalyzer_ = suppliedAnalyzer;
    }

}
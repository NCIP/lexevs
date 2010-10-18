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
package edu.mayo.informatics.indexer.lucene;

import java.io.IOException;
import java.io.Serializable;
import java.util.BitSet;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.search.Filter;

/**
 * This implements a filter for lucene that will filter on numeric fields.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class NumericFilter extends Filter implements Serializable {
    private static final long serialVersionUID = -2739309270891340247L;
    String field_;
    String start_ = "0";
    String end_ = "999999999999999999999999";

    private NumericFilter(String f) {
        field_ = f;
    }

    public NumericFilter(String field, String from, String to) {
        this.field_ = field;
        start_ = from;
        end_ = to;
    }

    // public NumericFilter(String field, int from, int to)
    // {
    // this.field = field;
    // start = from + "";
    // end = to + "";
    // }

    public static NumericFilter Before(String field, String date) {
        NumericFilter result = new NumericFilter(field);
        result.end_ = date;
        return result;
    }

    // public static NumericFilter Before(String field, int date)
    // {
    // NumericFilter result = new NumericFilter(field);
    // result.end = date + "";
    // return result;
    // }

    public static NumericFilter After(String field, String date) {
        NumericFilter result = new NumericFilter(field);
        result.start_ = date;
        return result;
    }

    // public static NumericFilter After(String field, int date)
    // {
    // NumericFilter result = new NumericFilter(field);
    // result.start = date + "";
    // return result;
    // }

    final public BitSet bits(IndexReader reader) throws IOException {
        BitSet bits = new BitSet(reader.maxDoc());

        TermEnum enumer = reader.terms(new Term(field_, start_));
        try {
            Term stop = new Term(field_, end_);
            while (enumer.term().compareTo(stop) <= 0) {
                TermDocs termDocs = reader.termDocs(enumer.term());
                try {
                    while (termDocs.next())
                        bits.set(termDocs.doc());
                } finally {
                    termDocs.close();
                }
                if (!enumer.next()) {
                    break;
                }
            }
        } finally {
            enumer.close();
        }
        return bits;
    }

    public final String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(field_);
        buffer.append(":");
        buffer.append(start_);
        buffer.append("-");
        buffer.append(end_);
        return buffer.toString();
    }
}
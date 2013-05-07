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
import java.util.BitSet;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Filter;

/**
 * Lucene can only accept one filter at a time. This class allows you to add
 * multipe filters to a lucene query.
 * 
 * This was origionally written by Kelvan Tan.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class ChainableFilter extends Filter {
    private static final long serialVersionUID = -1330685992539679125L;
    public static final int OR = 0;
    public static final int AND = 1;
    public static final int ANDNOT = 2;
    public static final int XOR = 3;

    private static final int DEFAULT = AND;

    /** The filter chain */
    private Filter[] chain_ = null;

    /**
     * Creates a new ChainableFilter.
     * 
     * @param chain
     *            The chain of filters.
     */
    public ChainableFilter(Filter[] chain) {
        this.chain_ = chain;
    }

    public BitSet bits(IndexReader reader) throws IOException {
        return bits(reader, DEFAULT);
    }

    public BitSet bits(IndexReader reader, int logic) throws IOException {
        BitSet result;
        int i = 0;

        if (logic == AND) {
            result = chain_[i].bits(reader);
            i = 1;
        } else {
            result = new BitSet(reader.maxDoc());
        }

        for (; i < chain_.length; i++) {
            doChain(result, reader, logic, chain_[i]);
        }
        return result;
    }

    // public BitSet bits(IndexReader reader, int[] logic) throws IOException
    // {
    // BitSet result = new BitSet(reader.maxDoc());
    // if (logic.length != chain.length)
    // throw new
    // IllegalArgumentException("Invalid number of elements in logic array");
    // for (int i = 0; i < chain.length; i++)
    // {
    // doChain(result, reader, logic[i], chain[i]);
    // }
    // return result;
    // }

    private void doChain(BitSet result, IndexReader reader, int logic, Filter filter) throws IOException {
        if (logic == OR)
            result.or(filter.bits(reader));
        else if (logic == AND)
            result.and(filter.bits(reader));
        else if (logic == ANDNOT)
            result.andNot(filter.bits(reader));
        else if (logic == XOR)
            result.xor(filter.bits(reader));
        else
            doChain(result, reader, DEFAULT, filter);
    }
}
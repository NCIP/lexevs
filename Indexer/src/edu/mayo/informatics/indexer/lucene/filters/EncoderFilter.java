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
package edu.mayo.informatics.indexer.lucene.filters;

/**
 * This filter is used by the EncoderAnalyzer to actually do the encoding of the terms using the 
 * apache commons coded package before they are inserted into the index.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
import java.io.IOException;

import org.apache.commons.codec.Encoder;
import org.apache.commons.codec.EncoderException;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

public final class EncoderFilter extends TokenFilter {
    private Encoder encoder_;

    public EncoderFilter(TokenStream in, Encoder encoder) {
        super(in);
        this.encoder_ = encoder;
    }

    /**
     * This implementation calls encode() on every term, and returns a new term
     * with the result.
     */
    public final Token next() throws IOException {
        Token t = input.next();

        if (t == null) {
            return null;
        }

        String result;
        try {
            result = encoder_.encode(t.termText()).toString();
            return new Token(result, t.startOffset(), t.endOffset());
        } catch (EncoderException e) {
            throw new IOException("There was a problem with the encoder - " + e);
        }
    }
}
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
 * This filter is used by the NormAnalyzer to actually do the normalizing 
 * of the terms using LVG before they are inserted into the index.  This 
 * particular filter keeps the origional word, plus all the norm variants.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

import edu.mayo.informatics.indexer.utility.CachedNormApi;

/**
 * Normalizes token text with lvg norm.
 * 
 * @version $Id: NormFilter.java,v 1.1 2005/08/22 21:39:04 armbrust Exp $
 */
public final class NormFilter extends TokenFilter {
    private CachedNormApi norm_;
    private ArrayList tokenBuffer_;
    private boolean keepOrigionalToken_;

    private final Logger logger = Logger.getLogger("Indexer.Index");

    public NormFilter(TokenStream in, CachedNormApi norm, boolean keepOrigionalToken) {
        super(in);
        this.norm_ = norm;
        tokenBuffer_ = new ArrayList();
        keepOrigionalToken_ = keepOrigionalToken;
    }

    /**
     * This implementation calls norm on every term, and then adds norms results
     * to the position of the term. If keepOrigionalToken is set in the
     * constructor, then it also keeps the non-normed token in the token stream.
     */
    public final Token next() throws IOException {
        // If the tokenBuffer is empty, populate it with the results of the norm
        // call for the next token.
        if (tokenBuffer_.size() == 0) {
            Token t = input.next();

            if (t == null) {
                return null;
            }

            try {
                if (keepOrigionalToken_) {
                    tokenBuffer_.add(t);
                }
                Vector result = norm_.Mutate(t.termText());

                for (int i = 0; i < result.size(); i++) {
                    // make sure we aren't adding the origional - sometimes norm
                    // returns the input text
                    if (!((String) result.get(i)).equals(t.termText())) {
                        Token temp = new Token((String) result.get(i), t.startOffset(), t.endOffset());
                        if (i > 0 || keepOrigionalToken_) {
                            // if we are adding more than one token from one
                            // token, then set the
                            // position increment to 0 on all of the subsequent
                            // tokens.
                            // position increment defaults to 1 - only change it
                            // on subsequent adds.
                            temp.setPositionIncrement(0);
                        }

                        tokenBuffer_.add(temp);
                    }
                }
                if (tokenBuffer_.size() == 0) {
                    // If we didn't add anything, then the token must already be
                    // normalized. Put it on the buffer.
                    // (should have at least one to return)
                    tokenBuffer_.add(t);
                }
            } catch (Exception e) {
                logger.error("Norm problem", e);
                throw new IOException();
            }
        }

        // pull one off the buffer and return it.
        return (Token) tokenBuffer_.remove(0);
    }
}
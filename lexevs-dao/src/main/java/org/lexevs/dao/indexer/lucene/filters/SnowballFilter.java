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
package org.lexevs.dao.indexer.lucene.filters;

/**
 * This filter is used by the NormAnalyzer to actually do the normalizing of the terms using Snowball before
 * they are inserted into the index. This particular filter can keep the origional word, plus all the stemmed
 * variant.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.tartarus.snowball.SnowballProgram;

/**
 * Normalizes token text with Snowball Stemmer - optionally keeping the
 * origional terms.
 * 
 * @version $Id: NormFilter.java,v 1.1 2005/08/22 21:39:04 armbrust Exp $
 */
public final class SnowballFilter extends TokenFilter {
    private static final Object[] EMPTY_ARGS = new Object[0];
    private ArrayList tokenBuffer_;
    private boolean keepOrigionalToken_;

    private SnowballProgram stemmer;
    private Method stemMethod;

    private final Logger logger = Logger.getLogger("Indexer.Index");

    public SnowballFilter(String snowballName, TokenStream in, boolean keepOrigionalToken) {
        super(in);
        tokenBuffer_ = new ArrayList();
        keepOrigionalToken_ = keepOrigionalToken;
        try {
            Class stemClass = Class.forName("org.tartarus.snowball.ext." + snowballName + "Stemmer");
            stemmer = (SnowballProgram) stemClass.newInstance();
            // why doesn't the SnowballProgram class have an (abstract?) stem
            // method?
            stemMethod = stemClass.getMethod("stem", new Class[0]);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    /**
     * This implementation calls stem on every term, and then adds stems's
     * results to the position of the term. If keepOrigionalToken is set in the
     * constructor, then it also keeps the non-normed token in the token stream.
     */
    public final Token next() throws IOException {
        // If the tokenBuffer is empty, populate it with the results of the norm
        // call for the next token.
//        if (tokenBuffer_.size() == 0) {
//            Token t = input.next();
//
//            if (t == null) {
//                return null;
//            }
//
//            try {
//                if (keepOrigionalToken_) {
//                    tokenBuffer_.add(t);
//                }
//
//                stemmer.setCurrent(t.termText());
//                stemMethod.invoke(stemmer, EMPTY_ARGS);
//                String result = stemmer.getCurrent();
//
//                // only add the stemmed word if it is different from the kept
//                // origional,
//                // or if the origional wasn't kept.
//                if (!keepOrigionalToken_ || !t.termText().equals(result)) {
//
//                    Token temp = new Token(result, t.startOffset(), t.endOffset());
//                    if (keepOrigionalToken_) {
//                        // if we are adding more than one token from one token,
//                        // then set the
//                        // position increment to 0 on all of the subsequent
//                        // tokens.
//                        // position increment defaults to 1 - only change it on
//                        // subsequent adds.
//                        temp.setPositionIncrement(0);
//                    }
//
//                    tokenBuffer_.add(temp);
//                }
//
//            } catch (Exception e) {
//                logger.error("Norm problem", e);
//                throw new IOException();
//            }
//        }

        // pull one off the buffer and return it.
        return (Token) tokenBuffer_.remove(0);
    }

	@Override
	public boolean incrementToken() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}
}
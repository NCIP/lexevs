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

import java.io.IOException;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

/**
 * TokenFilter which splits tokens on given strings. Useful if I always want to
 * split on something strange like "<:>".
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class StringFilter extends TokenFilter {
    String stringToTokenizeOn;
    String currentText;
    Token currentToken;
    int startAt;

    public StringFilter(TokenStream in, String stringToTokenizeOn) {
        super(in);
        this.stringToTokenizeOn = stringToTokenizeOn;
    }

    public Token next() throws IOException {
        while (currentText == null || currentText.length() == 0) {
            currentToken = input.next();
            if (currentToken == null) {
                // out of tokens.
                return null;
            }
            currentText = currentToken.termText();
            startAt = 0;
        }

        // Ok, now I have some text to work with.
        int index = currentText.indexOf(stringToTokenizeOn, startAt);
        Token returnValue;
        if (index > 0) {
            // we found a split point.
            returnValue = new Token(currentText.substring(startAt, index), currentToken.startOffset() + startAt,
                    currentToken.startOffset() + index);
            startAt = index + stringToTokenizeOn.length();
        } else {
            // didn't find a split point.
            returnValue = new Token(currentText.substring(startAt), currentToken.startOffset() + startAt, currentToken
                    .startOffset()
                    + currentText.length());
            currentText = null; // done with this text block.
        }

        return returnValue;
    }
}
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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

/**
 * Filter which removes specified characters from terms. Useful for removing
 * punctuation, etc.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class CharRemovingFilter extends TokenFilter {
    StringBuffer temp = new StringBuffer();
    Set charsToRemove;

    /**
     * Builds a Set from an array of chars to remove, appropriate for passing
     * into the CharRemovingFilter constructor.
     */
    public static final Set makeCharRemovalSet(char[] charsToRemove) {
        HashSet temp = new HashSet(charsToRemove.length);
        for (int i = 0; i < charsToRemove.length; i++) {
            temp.add(new Character(charsToRemove[i]));
        }
        return temp;
    }

    public CharRemovingFilter(TokenStream in, Set charsToRemove) {
        super(in);
        this.charsToRemove = charsToRemove;
    }

    public Token next() throws IOException {
        Token t;

        temp.setLength(0);

        while (true) {
            // scan through tokens until we find one with characters that we
            // don't want to throw away.
            t = input.next();

            if (t == null) {
                // end of the token stream.
                return null;
            }

            String current = t.termText();

            for (int i = 0; i < current.length(); i++) {
                if (!charsToRemove.contains(new Character(current.charAt(i)))) {
                    temp.append(current.charAt(i));
                }
            }

            if (temp.length() > 0) {
                // we found characters to keep in the token, break out of the
                // loop.
                break;
            }

        }

        Token returnValue = new Token(temp.toString(), t.startOffset(), t.endOffset());
        temp.setLength(0);

        return returnValue;
    }
}
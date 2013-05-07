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
package edu.mayo.informatics.indexer.lucene.tokenizers;

import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.CharTokenizer;

/**
 * A WhiteSpace Tokenizer that allows additional whitespace characters.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class CustomWhiteSpaceTokenizer extends CharTokenizer {
    private Set whiteSpaceChars_;

    /**
     * Builds a Set from an array of chars to treat as whitespace, appropriate
     * for passing into the CustomWhiteSpaceTokenizer constructor.
     */
    public static final Set makeCharWhiteSpaceSet(char[] charsToTreatAsWhiteSpace) {
        HashSet temp = new HashSet(charsToTreatAsWhiteSpace.length);
        for (int i = 0; i < charsToTreatAsWhiteSpace.length; i++) {
            temp.add(new Character(charsToTreatAsWhiteSpace[i]));
        }
        return temp;
    }

    /** Construct a new WhitespaceTokenizer. */
    public CustomWhiteSpaceTokenizer(Reader in, Set whiteSpaceChars) {
        super(in);
        whiteSpaceChars_ = whiteSpaceChars;
    }

    /**
     * Collects only characters which do not satisfy
     * {@link Character#isWhitespace(char)}, and are not in the
     * whiteSpaceCharsToRemove set.
     */
    protected boolean isTokenChar(char c) {
        if (Character.isWhitespace(c) || whiteSpaceChars_.contains(new Character(c))) {
            return false;
        } else {
            return true;
        }
    }
}
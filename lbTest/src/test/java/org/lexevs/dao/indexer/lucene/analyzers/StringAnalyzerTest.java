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
package org.lexevs.dao.indexer.lucene.analyzers;

import java.io.StringReader;

import junit.framework.TestCase;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.lexevs.dao.indexer.lucene.analyzers.StringAnalyzer;

/**
 * Test cases for the NormAnalyzer.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version 1.0 - cvs $Revision: 1.1 $ checked in on $Date: 2005/08/24 15:00:43
 *          $
 */
public class StringAnalyzerTest extends TestCase {
    public void testStringAnalyzer() throws Exception {
        StringAnalyzer temp = new StringAnalyzer("<:>");

        String input = new String("The<:>trees<:>have<:>Leaves!");

        StringReader reader = new StringReader(input);
        TokenStream result = temp.tokenStream("test", reader);

        Token token = result.next();
        assertTrue(token.termText().equals("The"));
        assertTrue(token.getPositionIncrement() == 1);
        assertTrue(token.startOffset() == 0);
        assertTrue(token.endOffset() == 3);

        token = result.next();
        assertTrue(token.termText().equals("trees"));
        assertTrue(token.getPositionIncrement() == 1);
        assertTrue(token.startOffset() == 6);
        assertTrue(token.endOffset() == 11);

        token = result.next();
        assertTrue(token.termText().equals("have"));
        assertTrue(token.getPositionIncrement() == 1);
        assertTrue(token.startOffset() == 14);
        assertTrue(token.endOffset() == 18);

        token = result.next();
        assertTrue(token.termText().equals("Leaves!"));
        assertTrue(token.getPositionIncrement() == 1);
        assertTrue(token.startOffset() == 21);
        assertTrue(token.endOffset() == 28);

        token = result.next();

        assertTrue(result.next() == null);
    }

    public static void main(String[] args) throws Exception {
        StringAnalyzerTest foo = new StringAnalyzerTest();
        foo.testStringAnalyzer();
    }

}
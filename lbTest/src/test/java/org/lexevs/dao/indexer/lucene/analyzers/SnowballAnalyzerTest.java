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

import junit.framework.TestCase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.AttributeFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test cases for the SnowballAnalyzer.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version 1.0 - cvs $Revision: 1.1 $ checked in on $Date: 2005/08/24 15:00:43
 *          $
 */

public class SnowballAnalyzerTest extends TestCase {
	
	@Test
    public void testDontKeepOrigional() throws Exception {
        Analyzer temp = new Analyzer() {
        	
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {
                final StandardTokenizer source = new StandardTokenizer(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY);
                source.setMaxTokenLength(StandardAnalyzer.DEFAULT_MAX_TOKEN_LENGTH);
                TokenStream filter = new StandardFilter(source);
                filter = new LowerCaseFilter( filter);
                filter = new StopFilter(filter, StandardAnalyzer.STOP_WORDS_SET);
                filter = new SnowballFilter(filter, "English");
                return new TokenStreamComponents(source, filter);
            }
        };
        
        String input = new String("The trees have Leaves!");
        String[] output = {"tree", "have", "leav"};
        BaseTokenStreamTestCase.assertAnalyzesTo(temp, input, output);
    }
	
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(SnowballAnalyzerTest.class);
    }
    
// This gives a sense of what we used to do.
//        SnowballAnalyzer temp = new SnowballAnalyzer(false, "English", new String[] { "foo", "bar" },
//                new char[] { '!' }, new char[] { '-' });

//        String input = new String("The trees have Leaves!");
//
//        StringReader reader = new StringReader(input);
//        TokenStream result = temp.tokenStream("test", reader);
//
//        Token token = result.next();
//        assertTrue(token.termText().equals("the"));
//        assertTrue(token.getPositionIncrement() == 1);
//        assertTrue(token.startOffset() == 0);
//        assertTrue(token.endOffset() == 3);
//
//        token = result.next();
//        assertTrue(token.termText().equals("tree"));
//        assertTrue(token.getPositionIncrement() == 1);
//        assertTrue(token.startOffset() == 4);
//        assertTrue(token.endOffset() == 9);
//
//        token = result.next();
//        assertTrue(token.termText().equals("have"));
//        assertTrue(token.getPositionIncrement() == 1);
//        assertTrue(token.startOffset() == 10);
//        assertTrue(token.endOffset() == 14);
//
//        token = result.next();
//        assertTrue(token.termText().equals("leav"));
//        assertTrue(token.getPositionIncrement() == 1);
//        assertTrue(token.startOffset() == 15);
//        assertTrue(token.endOffset() == 22);
//
//        assertTrue(result.next() == null);
//    }
//
//    public void testKeepOrigional() throws Exception {
//        SnowballAnalyzer temp = new SnowballAnalyzer(true, "English", new String[] { "foo", "bar" },
//                new char[] { '!' }, new char[] { '-' });
//
//        String input = new String("The trees have Leaves!");
//
//        StringReader reader = new StringReader(input);
//        TokenStream result = temp.tokenStream("test", reader);
//
//        Token token = result.next();
//        assertTrue(token.termText().equals("the"));
//        assertTrue(token.getPositionIncrement() == 1);
//        assertTrue(token.startOffset() == 0);
//        assertTrue(token.endOffset() == 3);
//
//        token = result.next();
//        assertTrue(token.termText().equals("trees"));
//        assertTrue(token.getPositionIncrement() == 1);
//        assertTrue(token.startOffset() == 4);
//        assertTrue(token.endOffset() == 9);
//
//        token = result.next();
//        assertTrue(token.termText().equals("tree"));
//        assertTrue(token.getPositionIncrement() == 0);
//        assertTrue(token.startOffset() == 4);
//        assertTrue(token.endOffset() == 9);
//
//        token = result.next();
//        assertTrue(token.termText().equals("have"));
//        assertTrue(token.getPositionIncrement() == 1);
//        assertTrue(token.startOffset() == 10);
//        assertTrue(token.endOffset() == 14);
//
//        token = result.next();
//        assertTrue(token.termText().equals("leaves"));
//        assertTrue(token.getPositionIncrement() == 1);
//        assertTrue(token.startOffset() == 15);
//        assertTrue(token.endOffset() == 22);
//
//        token = result.next();
//        assertTrue(token.termText().equals("leav"));
//        assertTrue(token.getPositionIncrement() == 0);
//        assertTrue(token.startOffset() == 15);
//        assertTrue(token.endOffset() == 22);
//
//        assertTrue(result.next() == null);
//    }
}
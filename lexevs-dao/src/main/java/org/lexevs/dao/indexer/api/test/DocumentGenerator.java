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
package org.lexevs.dao.indexer.api.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.lexevs.dao.indexer.lucene.Index;

/**
 * This class generates random documents for testing the indexer.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
public class DocumentGenerator {

    // avg. word length = 8, so 8 * 3 bytes => 24 bytes
    private static final short WORDS_PER_TITLE = 3;
    // avg. word length = 8, so 8 * 2000 bytes => 16000 bytes
    private static final short WORDS_PER_BODY = 1000;

    private static String _dict;
    // private static int _indexSize;
    private static String[] _words;
    private static Random _random;
    private int maxRandNumber;
    private int docCounter = 0;

    public DocumentGenerator(String dictLocation) throws IOException {
        _dict = dictLocation;
        System.out.println("Loading words from " + _dict);
        Set wordSet = loadWords();
        _words = (String[]) wordSet.toArray(new String[wordSet.size()]);
        _random = new Random();
        maxRandNumber = _words.length;
    }

    private Set loadWords() throws IOException {
        int wordLength = 0;
        String word;
        Set wordSet = new HashSet(99905);

        File file = new File(_dict);
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((word = br.readLine()) != null) {
            // System.out.println("WORD: " + word);
            wordSet.add(word);
            wordLength += word.length();
        }
        System.out.println("WORD COUNT:        " + wordSet.size());
        System.out.println("TOTAL WORD LENGTH: " + wordLength);
        System.out.println("AVG WORD LENGTH:   " + wordLength / wordSet.size());

        return wordSet;
    }

    public Document makeDocument() {
        Document doc = new Document();
        StringBuffer fieldValue = new StringBuffer(WORDS_PER_TITLE * 8);
        for (int i = 0; i < WORDS_PER_TITLE; i++) {
            int rand = _random.nextInt(maxRandNumber);
            fieldValue.append(" ").append(_words[rand]);
        }
        // System.out.println("Title: " + fieldValue);
        doc.add(new TextField("title", fieldValue.toString(), Store.YES));

        fieldValue.setLength(0);
        // fieldValue.setLength(WORDS_PER_BODY * 8);

        for (int i = 0; i < WORDS_PER_BODY; i++) {
            int rand = _random.nextInt(maxRandNumber);
            fieldValue.append(" ").append(_words[rand]);
        }
        // System.out.println("Body: " + fieldValue);
        doc.add(new TextField("body", fieldValue.toString(), Store.YES));

        doc.add(new TextField(Index.UNIQUE_DOCUMENT_IDENTIFIER_FIELD, docCounter++ + "",  Store.YES));

        return doc;
    }

}
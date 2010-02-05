/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package edu.mayo.informatics.indexer.api.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;

import edu.mayo.informatics.indexer.api.IndexerService;
import edu.mayo.informatics.indexer.api.SearchServiceInterface;
import edu.mayo.informatics.indexer.api.generators.FilterGenerator;
import edu.mayo.informatics.indexer.api.generators.QueryGenerator;

/**
 * Class used for testing the search features.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
public class TestSearch {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Must pass in 1 value... the path where to construct the indexer service");
        }

        IndexerService service = new IndexerService(args[0], true);

        // String[] indexes = service.listIndexes();

        SearchServiceInterface searcher = service.getIndexSearcher("ICD9v2");

        FilterGenerator filterGenerator = new FilterGenerator();

        // filterGenerator.addNumFilter("age", 3, 30, 3);

        // filterGenerator.addAllowNumAboveFilter("age", 2, 3);

        // filterGenerator.addAllowNumBelowFilter("age", 27, 3);

        // filterGenerator.addAllowNumAboveFilter("dob", "20020705", 1);

        QueryGenerator temp = new QueryGenerator(new StandardAnalyzer());

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        String[] fieldsToSearch;

        while (true) {
            System.out.println("Search fields (enter for all) - available fields:");

            String[] array = searcher.searchableFields();

            if (array == null) {
                System.out.println("null");
            } else {
                for (int i = 0; i < array.length; i++) {
                    System.out.print(array[i] + "|");
                }
                System.out.println("");
            }

            input = bufferedReader.readLine().trim();
            if (input.length() > 0) {
                System.out.println("Sorry, dan broke this");
                fieldsToSearch = null;
                // fieldsToSearch = WordUtility.getWords(input, "|");
            } else {
                fieldsToSearch = searcher.searchableFields();
            }

            System.out.println("Search text:");

            input = bufferedReader.readLine().trim();
            if (input.length() == 0) {
                break;
            }

            Query foo = temp.createQuery(input, fieldsToSearch);

            Document[] docs = searcher.search(foo, filterGenerator.returnFilter(), false, 20);
            float[] scores = searcher.getScores();

            System.out.println(searcher.getHitTotal() + " total hits");

            for (int j = 0; j < docs.length; j++) {
                System.out.println(scores[j] + " " + docs[j].toString());
            }

            // while (searcher.hasMoreHits())
            // {
            // docs = searcher.getNextSearchResults(25);
            // System.out.println("Getting the next batch... (got) " +
            // docs.length);
            // for (int j = 0; j < docs.length; j++)
            // {
            // System.out.println(docs[j].toString());
            // }
            // }

        }
        System.out.println("Goodbye");
    }
}
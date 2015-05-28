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
package edu.mayo.informatics.indexer.api.test;

import java.io.File;

import edu.mayo.informatics.indexer.api.IndexerService;
import edu.mayo.informatics.indexer.api.generators.DocumentFromXMLGenerator;
import edu.mayo.informatics.indexer.api.generators.DocumentFromXSLGenerator;

/**
 * Class for testing the IndexWriter.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
public class TestAddDocs {
    private static int numberOfDocs = 500;

    private static final String RANDOM_WORDFILE_LOCATION = "resources/words.txt";
    private static final String XML_FILE_LOCATION = "resources/xmlSamples";

    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            System.out.println("Must pass in 1 value... the path where to construct the indexer service");
        }

        long startTime = System.currentTimeMillis();
        long batchStartTime = System.currentTimeMillis();

        IndexerService service = new IndexerService(args[0], true);

        // This section just shows adding a large batch of randomly generated
        // docs to an index

        DocumentGenerator docGen = new DocumentGenerator(RANDOM_WORDFILE_LOCATION);

        String index1 = "random";
        service.createIndex(index1);
        service.openBatchWriter(index1, true, false);

        for (int i = 0; i < numberOfDocs; i++) {
            service.addDocument(index1, docGen.makeDocument());
            if (i % 100 == 0) {
                System.out.println(i + " " + String.valueOf(System.currentTimeMillis() - batchStartTime) + " ms");
                batchStartTime = System.currentTimeMillis();
            }

        }

        service.closeWriter(index1);

        long totalTime = System.currentTimeMillis() - startTime;
        long optimizeTime = 0;

        System.out.println("Batch Done");

        // performance stats on batch add of random docs..

        System.out.println("Total time: " + String.valueOf(totalTime) + " ms");
        System.out.println("Avg time per document (not counting optimizing) "
                + String.valueOf((totalTime - optimizeTime) / numberOfDocs) + " ms");
        try {
            System.out.println("Avg time per 1000 documents (not counting optimizing) "
                    + String.valueOf((totalTime - optimizeTime) / (numberOfDocs / 1000)) + " ms");
        } catch (RuntimeException e) {
            System.out.println("Too small of a sample to output avg time...");
        }

        // This section shows adding a folder full of properly formatted (xml
        // which matches
        // the provided schema)xml files to an index

        String index2 = "sample_xml";
        service.createIndex(index2);
        service.openWriter(index2, true);

        DocumentFromXMLGenerator creator = new DocumentFromXMLGenerator(true);

        File temp = new File(XML_FILE_LOCATION);
        File[] files = temp.listFiles();
        for (int i = 0; i < files.length; i++) {
            try {
                if (files[i].isFile())
                    service.addDocument(index2, creator.create(files[i]));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        service.closeWriter(index2);

        String index4 = "fromxsl";
        service.createIndex(index4);
        service.openWriter(index4, true);

        DocumentFromXSLGenerator xslGenerator = new DocumentFromXSLGenerator(new File(
                "resources/xml-xsd-Samples/sample.xsl"), true);

        service.addDocument(index4, xslGenerator.create("resources/xml-xsd-Samples/sample.xml"));

        service.closeWriter(index4);

        System.out.println("Finished!");
    }
}
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

import edu.mayo.informatics.indexer.api.IndexerService;
import edu.mayo.informatics.indexer.api.exceptions.InternalErrorException;
import edu.mayo.informatics.indexer.utility.MetaData;

/**
 * A class for testing the metadata storage.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
public class TestMetaData {
    public static void main(String[] args) throws InternalErrorException {

        if (args.length != 1) {
            System.out.println("Must pass in 1 value... the path where to construct the indexer service");
        }

        IndexerService service = new IndexerService(args[0], true);
        MetaData metadata = service.getMetaData();
        String[] temp = metadata.getIndexMetaDataKeys();
        for (int i = 0; i < temp.length; i++) {
            System.out.println(temp[i] + "value: " + metadata.getIndexMetaDataValue(temp[i]));
        }

        temp = metadata.getIndexMetaDataKeys("ICD9");
        for (int i = 0; i < temp.length; i++) {
            System.out.println(temp[i] + "value: " + metadata.getIndexMetaDataValue("ICD9", temp[i]));
        }

        metadata.setIndexMetaDataValue("testing4", "one thing");

        metadata.setIndexMetaDataValue("ICD9", "testing4", "anoiter");

        metadata.setIndexMetaDataValue("wer", "testing6", "sdfsdflkfr");

        System.out.println(metadata.getIndexMetaDataValue("testing4"));

        metadata.removeIndexMetaDataValue("wer", "testing6");
        metadata.removeIndexMetaDataValue("dfd");

    }

}
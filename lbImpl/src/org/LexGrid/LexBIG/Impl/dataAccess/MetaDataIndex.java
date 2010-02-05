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
package org.LexGrid.LexBIG.Impl.dataAccess;

import edu.mayo.informatics.indexer.api.IndexerService;
import edu.mayo.informatics.indexer.api.exceptions.IndexNotFoundException;
import edu.mayo.informatics.indexer.api.exceptions.InternalIndexerErrorException;
import edu.mayo.informatics.indexer.api.exceptions.OperatorErrorException;

/**
 * Base functions for the MetaData Index.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class MetaDataIndex {
    public static final String STRING_TOKEINZER_TOKEN = "<:>";
    public static final String CONCATINATED_VALUE_SPLIT_TOKEN = ":";

    public static void removeMeta(String codingSchemeRegisteredName, String codingSchemeVersion)
            throws IndexNotFoundException, InternalIndexerErrorException, OperatorErrorException {
        IndexerService indexerService = getIndexerService();
        String metaDataIndexName = getIndexName();

        try {
            indexerService.openBatchRemover(metaDataIndexName);
        } catch (IndexNotFoundException e) {
            // this is ok - not all deployments use this index.
            return;
        }

        indexerService.removeDocument(metaDataIndexName, "codingSchemeNameVersion", codingSchemeRegisteredName
                + CONCATINATED_VALUE_SPLIT_TOKEN + codingSchemeVersion);

        indexerService.closeBatchRemover(metaDataIndexName);

        // need to force a reopen on the index reader
        ResourceManager.instance().getMetaDataIndexInterface().reopenMetaDataIndexReader();
    }

    protected static IndexerService getIndexerService() {
        return ResourceManager.instance().getMetaDataIndexInterface().getBaseIndexerService();
    }

    protected static String getIndexName() {
        return SystemVariables.getMetaDataIndexName();
    }
}
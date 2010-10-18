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
package org.LexGrid.LexBIG.Impl.loaders;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ProcessStatus;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.index.indexer.IndexCreator.EntityIndexerProgressCallback;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * Loader for rebuilding lucene indexes.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class IndexLoaderImpl extends AbstractProcessRunner {

    @Override
    protected void doRunProcess(
            AbsoluteCodingSchemeVersionReference codingSchemeVersion, 
            OntologyFormat format,
            final LgMessageDirectorIF md,
            ProcessStatus status) {
  
        EntityIndexService entityIndexService = 
            LexEvsServiceLocator.getInstance().getIndexServiceManager().getEntityIndexService();
        
        if(entityIndexService.doesIndexExist(codingSchemeVersion)) {
            entityIndexService.dropIndex(codingSchemeVersion);
        }
        
        CountingEntityIndexerProgressCallback callback = new CountingEntityIndexerProgressCallback(md);
        entityIndexService.createIndex(codingSchemeVersion, callback);
        
        md.info("Indexed a total of: " + callback.getNumberIndexed() + " Entities.");
      
    }
    
    private static class CountingEntityIndexerProgressCallback implements EntityIndexerProgressCallback {

        private int numberIndexed = 0;
        private LgMessageDirectorIF md;
        
        private CountingEntityIndexerProgressCallback(LgMessageDirectorIF md){
            this.md = md;
        }
        
        public void onEntityIndex(Entity entity) {
            if(numberIndexed % 1000 == 0) {
                md.info("Indexed: " + numberIndexed + " Entities.");
            }
            numberIndexed++;
        }
        
        public int getNumberIndexed() {
            return this.numberIndexed;
        } 
    }
}
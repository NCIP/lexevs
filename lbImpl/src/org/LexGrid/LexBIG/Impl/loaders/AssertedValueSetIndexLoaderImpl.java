
package org.LexGrid.LexBIG.Impl.loaders;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ProcessStatus;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.lexevs.dao.index.service.search.SourceAssertedValueSetSearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;

public class AssertedValueSetIndexLoaderImpl extends AbstractProcessRunner {

    @Override
    protected void doRunProcess(AbsoluteCodingSchemeVersionReference codingSchemeVersion, OntologyFormat format,
            LgMessageDirectorIF md, ProcessStatus status) {
        if(codingSchemeVersion == null) { 
            md.info("Coding Scheme Reference cannot be null");
            throw new RuntimeException("Coding Scheme Reference cannot be null");}
        SourceAssertedValueSetSearchIndexService entityIndexService = 
                LexEvsServiceLocator.getInstance().getIndexServiceManager().getAssertedValueSetIndexService();
            
            if(entityIndexService.doesIndexExist(codingSchemeVersion)) {
                entityIndexService.dropIndex(codingSchemeVersion);
            }
            
            entityIndexService.createIndex(codingSchemeVersion);
            
            md.info("Indexed Asserted Value Set Entities.");

    }

}

package org.LexGrid.LexBIG.Impl.loaders;

import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ProcessStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
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
            
            try {
            entityIndexService.createIndex(codingSchemeVersion);
            }catch(OutOfMemoryError e) {
                String message = "Out of Memory for this monolithic Index. Increase Memory Size.";
                status.setMessage(message + " " + e.getMessage());
                status.setState(ProcessState.FAILED);
                status.setEndTime(new Date(System.currentTimeMillis()));
                status.setErrorsLogged(true);
                Thread.currentThread().interrupt();
                throw new RuntimeException(message, e);
            }
            
            md.info("Indexed Asserted Value Set Entities.");

    }

}
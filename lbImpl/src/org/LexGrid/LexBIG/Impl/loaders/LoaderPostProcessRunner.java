
package org.LexGrid.LexBIG.Impl.loaders;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ProcessStatus;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.postprocessor.LoaderPostProcessor;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;

/**
 * The Class LoaderPostProcessRunner.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LoaderPostProcessRunner extends AbstractProcessRunner {

    /** The post processor. */
    private LoaderPostProcessor postProcessor;
    
    /**
     * Instantiates a new loader post process runner.
     * 
     * @param postProcessor the post processor
     */
    public LoaderPostProcessRunner(LoaderPostProcessor postProcessor){
        this.postProcessor = postProcessor;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.loaders.AbstractProcessRunner#doRunProcess(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF, org.LexGrid.LexBIG.DataModel.InterfaceElements.ProcessStatus)
     */
    @Override
    protected void doRunProcess(
            AbsoluteCodingSchemeVersionReference codingSchemeVersion, 
            OntologyFormat ontFormat,
            LgMessageDirectorIF md,
            ProcessStatus status) {
        
        md.info("Running Post Processor: " + postProcessor.getName());
        
        postProcessor.runPostProcess(codingSchemeVersion, ontFormat);
        
        md.info("Finished Running Post Processor: " + postProcessor.getName());
    }
}
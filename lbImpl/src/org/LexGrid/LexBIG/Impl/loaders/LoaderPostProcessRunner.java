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
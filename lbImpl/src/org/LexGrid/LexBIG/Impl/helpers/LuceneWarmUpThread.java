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
package org.LexGrid.LexBIG.Impl.helpers;

import java.util.UUID;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.lexevs.logging.LoggerFactory;

/**
 * Lucene Index warm up thread -- on launch, this will run a sample query to
 * "warm up" various Lucene buffers and caches.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LuceneWarmUpThread extends Thread {

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    public void run() {
        LoggerFactory.getLogger().info("Starting Lucene Warm Up Thread...");
        
        LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
        
        CodingSchemeRenderingList csrl;
       
        try {
            csrl = lbs.getSupportedCodingSchemes();
        } catch (LBInvocationException e) {
           LoggerFactory.getLogger().warn("Error warming up Lucene Index.", e);
           return;
        }
        
        for(CodingSchemeRendering csr : csrl.getCodingSchemeRendering()) {
            CodingSchemeSummary css = csr.getCodingSchemeSummary();
            String uri = css.getCodingSchemeURI();
            String version = css.getRepresentsVersion();
            try {
                CodedNodeSet cns = lbs.getCodingSchemeConcepts(
                        uri, 
                        Constructors.createCodingSchemeVersionOrTagFromVersion(version));
      
                cns = cns.restrictToMatchingDesignations(
                        UUID.randomUUID().toString(), 
                        SearchDesignationOption.ALL, "LuceneQuery", null);
                
                cns.resolve(null, null, null);
                
                LoggerFactory.getLogger().info(
                        "Lucene warm up completed for URI: " + uri + " Version: " + version + ".");
                
            } catch (Exception e) {
                LoggerFactory.getLogger().warn(
                        "Error warming up Lucene Index for URI: " + uri + " Version: " + version + ".", e);
                continue;
            }
        }
        
        LoggerFactory.getLogger().info("Completed Lucene Warm Up.");
    }
}

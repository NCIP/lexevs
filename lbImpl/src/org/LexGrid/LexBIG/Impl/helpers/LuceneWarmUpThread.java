
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
           LoggerFactory.getLogger().warn("Error warming up Lucene Index.");
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
                        "Error warming up Lucene Index for URI: " + uri + " Version: " + version + ".");
                continue;
            }
        }
        
        LoggerFactory.getLogger().info("Completed Lucene Warm Up.");
    }
}
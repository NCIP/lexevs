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
package org.LexGrid.LexBIG.Impl.bugs;

import java.io.File;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

public class GForge22826 extends TestCase {

    
    public void testAssociationEntryState() throws LBException {
    	CodingSchemeVersionOrTag tagOrVersion = new CodingSchemeVersionOrTag();
    	tagOrVersion.setVersion("1.0");

    	ResolvedConceptReferencesIterator itr = 
    		ServiceHolder.instance().getLexBIGService().
    			getNodeSet("Automobiles", null, Constructors.createLocalNameList("association")).resolve(null, null, null);
    	
    	assert(itr.hasNext());
    }

/**
     * @param args
     */
public static void main(String[] args) {
        try {
            ServiceHolder.configureForSingleConfig();
            
            loadAutomobilesOntology();
            new GForge22826().testAssociationEntryState();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void loadAutomobilesOntology() throws Exception {
        LexBIGServiceManager lbsm = null;

        lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(
                null);

        LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
                .getLoader("LexGridLoader");

        loader.load(new File("resources/testData/Automobiles.xml").toURI(),
                true, false);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
        
    }

}
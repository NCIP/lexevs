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

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.Relations;

import junit.framework.TestCase;

public class GForge22296 extends TestCase 
{
    public void testConceptWithParanthesis() throws LBException 
    {
    	LexBIGService service = ServiceHolder.instance().getLexBIGService();

        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion("1.0");
        
        try 
        {
            CodedNodeSet nodeSet = service.getCodingSchemeConcepts("Automobiles", versionOrTag);
            
            nodeSet.restrictToCodes(Constructors.createConceptReferenceList("C0011(5564)"));
            
            ResolvedConceptReferenceList resolvedCodeList = nodeSet.resolveToList(null, null, null, 1);
            
            ResolvedConceptReference resolvedCode = resolvedCodeList.getResolvedConceptReference()[0];
            
            assertNotNull(resolvedCode);
        
        } catch (LBInvocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (LBParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
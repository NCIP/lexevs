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
package org.LexGrid.LexBIG.Impl.export.common.util;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;

public class CngFactory {
	
    public static CodedNodeGraph createCngExportAll(String urn, String ver) throws LBException {
    	LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
        CodingSchemeVersionOrTag csVerOrTag = new CodingSchemeVersionOrTag();
        csVerOrTag.setVersion(ver);
        org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph cng = lbs.getNodeGraph(urn, csVerOrTag ,null);
        return cng;
    }
    
    public static CodedNodeGraph createCngAssociationsOnly(String urn, String ver) throws LBException {
    	LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
        CodingSchemeVersionOrTag csVerOrTag = new CodingSchemeVersionOrTag();
        csVerOrTag.setVersion(ver);
        org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph cng = lbs.getNodeGraph(urn, csVerOrTag ,null);
        return cng;
    }
    
    public static CodedNodeGraph crateCngRestrictToAssociation(String urn, String ver, String assocName) {
    	LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
        CodingSchemeVersionOrTag csVerOrTag = new CodingSchemeVersionOrTag();
        csVerOrTag.setVersion(ver);
        
        org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph cng = null;
		try {
			cng = lbs.getNodeGraph(urn, csVerOrTag ,null);
	        NameAndValueList nameAndValueList = new NameAndValueList();
	        NameAndValue nameAndValue = new NameAndValue();
	        nameAndValue.setName(assocName);
	        nameAndValueList.addNameAndValue(nameAndValue);
	        cng.restrictToAssociations(nameAndValueList, null);
		} catch (LBException e) {
			e.printStackTrace();
		}
        return cng;        
    }
    
    public static CodedNodeGraph createCngEntitiesOnly(String urn, String ver) throws LBException {
        return null;
    }    
    
}

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

package org.LexGrid.LexBIG.Impl.export.common.util;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;

public class CnsFactory {
	
    public static CodedNodeSet createCnsExportAll(String urn, String ver) throws LBException {
    	LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
        CodingSchemeVersionOrTag csVerOrTag = new CodingSchemeVersionOrTag();
        csVerOrTag.setVersion(ver);
        org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns = lbs.getNodeSet(urn, csVerOrTag, null);
        return cns;
    }
    
    public static CodedNodeSet createCnsEntitiesOnly(String urn, String ver) throws LBException {
    	LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
        CodingSchemeVersionOrTag csVerOrTag = new CodingSchemeVersionOrTag();
        csVerOrTag.setVersion(ver);
        org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns = lbs.getNodeSet(urn, csVerOrTag, null);
        return cns;
    }    
    
    public static CodedNodeSet createCnsAssociationsOnly(String urn, String ver) throws LBException {
        return null;
    }

    public static CodedNodeSet crateCnsRestrictToAssociation(String urn, String ver) throws LBException {
        return null;
    }
    
}
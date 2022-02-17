
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.util;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.apache.commons.cli.CommandLine;

public class FilterParser {
    
    public static CnsCngPair parse( OptionHolder options ) {
        return null;
    }
    
    public static CnsCngPair parse( LexBIGService lbs, String uri, String version, CommandLine cl ) throws LBException {
        String assocName = cl.getOptionValue("an");
        boolean xall = cl.hasOption("xall");
        boolean xa = cl.hasOption("xa");
        boolean xc = cl.hasOption("xc");
         
        return FilterParser.createCnsCng(lbs, uri, version, assocName, xall, xa, xc);
    }
    
    private static CnsCngPair createCnsCng(LexBIGService lbs, String urn, String ver, String assocName, boolean xall, boolean xa, boolean xc) throws LBException {
        
        if(xall == true || (xall == false && xa == false && xc == false)) {
            return FilterParser.createCnsCngExportAll(lbs, urn, ver);
        }
        
        if(xc == true && xa == true) {  // may need some error handling here
            throw new RuntimeException("ERROR: xc is true and xa is true. If you want all content, specify xall");
        }
        
        if(xc == true) {
            return FilterParser.createCnsCngConceptsOnly(lbs, urn, ver);
        }
        
        if(xa == true) {
            if(assocName != null && assocName.length() > 0) {
                return FilterParser.crateCnsCngRestrictToAssociation(lbs, urn, ver, assocName);
            } else {
                return FilterParser.createCnsCngAssociationsOnly(lbs, urn, ver);
            }
        }
        System.out.print("how did I get here?");
        return null;
    }
    
    private static CnsCngPair crateCnsCngRestrictToAssociation(LexBIGService lbs, String urn, String ver, String assocName) throws LBException {
        CodingSchemeVersionOrTag csVerOrTag = new CodingSchemeVersionOrTag();
        csVerOrTag.setVersion(ver);
        
        org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph cng = lbs.getNodeGraph(urn, csVerOrTag ,null);
        
        NameAndValueList nameAndValueList = new NameAndValueList();
        NameAndValue nameAndValue = new NameAndValue();
        nameAndValue.setName(assocName);
        
        nameAndValueList.addNameAndValue(nameAndValue);
        
        cng.restrictToAssociations(nameAndValueList, null);
        org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns = null;
        
        CnsCngPair result = new CnsCngPair(cns, cng);
        return result;        
    }
    
    private static CnsCngPair createCnsCngAssociationsOnly(LexBIGService lbs, String urn, String ver) throws LBException {
        CodingSchemeVersionOrTag csVerOrTag = new CodingSchemeVersionOrTag();
        csVerOrTag.setVersion(ver);
        
        org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph cng = lbs.getNodeGraph(urn, csVerOrTag ,null);
        org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns = null;
        
        CnsCngPair result = new CnsCngPair(cns, cng);
        return result;
    }
    
    private static CnsCngPair createCnsCngConceptsOnly(LexBIGService lbs, String urn, String ver) throws LBException {
        CodingSchemeVersionOrTag csVerOrTag = new CodingSchemeVersionOrTag();
        csVerOrTag.setVersion(ver);
        
        org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph cng = null;
        org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns = lbs.getNodeSet(urn, csVerOrTag, null);
        
        CnsCngPair result = new CnsCngPair(cns, cng);
        return result;
        
    }
    
    private static CnsCngPair createCnsCngExportAll(LexBIGService lbs, String urn, String ver) throws LBException {
        
        CodingSchemeVersionOrTag csVerOrTag = new CodingSchemeVersionOrTag();
        csVerOrTag.setVersion(ver);
        
        org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph cng = lbs.getNodeGraph(urn, csVerOrTag ,null);
        org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns = lbs.getNodeSet(urn, csVerOrTag, null);
        
        CnsCngPair result = new CnsCngPair(cns, cng);
        return result;

    }
    
}
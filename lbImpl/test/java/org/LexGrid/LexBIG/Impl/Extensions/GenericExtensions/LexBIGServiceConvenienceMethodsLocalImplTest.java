
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions;

import junit.framework.Assert;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.relations.AssociationEntity;
import org.easymock.EasyMock;
import org.junit.Test;

public class LexBIGServiceConvenienceMethodsLocalImplTest {

    @Test
    public void getAssociationForwardNameLocalTest() throws LBException {
        LexBIGService lbs = 
            EasyMock.createMock(LexBIGService.class);
    
        CodedNodeSet cns = EasyMock.createNiceMock(CodedNodeSet.class);
        
        CodingScheme cs = new CodingScheme();
        cs.setCodingSchemeName("test");
        cs.setMappings(new Mappings());
        
        SupportedAssociation sa = new SupportedAssociation();
        sa.setLocalId("assocName");
        sa.setEntityCode("a code");
        sa.setEntityCodeNamespace("a ns");
        
        cs.getMappings().addSupportedAssociation(sa);
        
        EasyMock.expect(lbs.resolveCodingScheme("test", null)).andReturn(cs);
        
        EasyMock.expect(
                lbs.getNodeSet(
                        EasyMock.eq("test"), 
                        (CodingSchemeVersionOrTag)EasyMock.isNull(), 
                        (LocalNameList) EasyMock.anyObject())).
                            andReturn(cns);
        
        EasyMock.expect(cns.restrictToCodes(
                (ConceptReferenceList)EasyMock.anyObject())).andReturn(cns);
        
        ResolvedConceptReferenceList refList =
            new ResolvedConceptReferenceList();
        
        ResolvedConceptReference ref =
            new ResolvedConceptReference();
        
        AssociationEntity ae = new AssociationEntity();
        ae.setForwardName("some forward name");
        ref.setEntity(ae);
        
        refList.addResolvedConceptReference(ref);
        
        EasyMock.expect(cns.resolveToList(null, null, null, -1)).andReturn(refList);
        
        EasyMock.replay(lbs,cns);
        
        LexBIGServiceConvenienceMethodsImpl lbscm =
            new LexBIGServiceConvenienceMethodsImpl();
    
        lbscm.setLexBIGService(lbs);
        
        String forwardName =
            lbscm.getAssociationForwardName("assocName", "test", null);
        
        Assert.assertEquals("some forward name", forwardName);
    }
}
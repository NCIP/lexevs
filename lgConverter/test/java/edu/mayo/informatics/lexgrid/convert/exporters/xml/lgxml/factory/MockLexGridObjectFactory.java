package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.factory;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.easymock.EasyMock;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.Constants;

public class MockLexGridObjectFactory {
   
    /*
     * create mock CNS
     */
    public static CodedNodeSet createCns() throws LBException {
        

        CodedNodeSet cns = EasyMock.createNiceMock(CodedNodeSet.class);
        
        ResolvedConceptReferenceList refList1 = new ResolvedConceptReferenceList();
        ResolvedConceptReferenceList refList2 = new ResolvedConceptReferenceList();
        ResolvedConceptReferenceList refList3 = new ResolvedConceptReferenceList();
        
        ResolvedConceptReference rcr = null;
        // create automaker and add to refList list
        rcr = ResolvedConceptReferenceFactory.createRcrAutoMaker();
        refList1.addResolvedConceptReference(rcr);
        
        // create ford and add to refList list
        rcr = ResolvedConceptReferenceFactory.createRcrFord();
        refList1.addResolvedConceptReference(rcr);        

        // create root and add to refList list
        rcr = ResolvedConceptReferenceFactory.createRcrRoot();
        refList1.addResolvedConceptReference(rcr);
        
        // create automobile and add to refList list
        rcr = ResolvedConceptReferenceFactory.createRcrAutomobile();
        refList2.addResolvedConceptReference(rcr);
        
        // create car and add to refList list
        rcr = ResolvedConceptReferenceFactory.createRcrCar();
        refList2.addResolvedConceptReference(rcr);
        
        // create truck and add to refList list
        rcr = ResolvedConceptReferenceFactory.createRcrTruck();
        refList3.addResolvedConceptReference(rcr);
        
        ResolvedConceptReferencesIterator rcri = EasyMock.createNiceMock(ResolvedConceptReferencesIterator.class);
        EasyMock.expect(rcri.next(Constants.VALUE_PAGE_SIZE)).andReturn(refList1).times(1).andReturn(refList2).times(1).andReturn(refList3).times(1);
        EasyMock.expect(rcri.hasNext()).andReturn(true).times(2);

        EasyMock.expect(cns.resolve(null, null, null, null, true)).andReturn(rcri);
        EasyMock.replay(cns, rcri);
        
        return cns;
        
    }
    
    /*
     * create mock CNS for filtering test
     * cns.restrictToMatchingDesignations("T*", SearchDesignationOption.ALL, "LuceneQuery", null);
     */
    public static CodedNodeSet createCnsFiltered() throws LBException {
        

        CodedNodeSet cns = EasyMock.createNiceMock(CodedNodeSet.class);
        
        ResolvedConceptReferenceList refList1 = new ResolvedConceptReferenceList();
        
        ResolvedConceptReference rcr = null;
        
        // create truck and add to refList list
        rcr = ResolvedConceptReferenceFactory.createRcrTruck();
        refList1.addResolvedConceptReference(rcr);
        
        ResolvedConceptReferencesIterator rcri = EasyMock.createNiceMock(ResolvedConceptReferencesIterator.class);
        EasyMock.expect(rcri.next(Constants.VALUE_PAGE_SIZE)).andReturn(refList1).times(1);
        EasyMock.expect(rcri.hasNext()).andReturn(false).times(1);

        EasyMock.expect(cns.restrictToMatchingDesignations("T*", SearchDesignationOption.ALL, "LuceneQuery", null)).andReturn(cns);
        EasyMock.expect(cns.resolve(null, null, null, null, true)).andReturn(rcri);
        EasyMock.replay(cns, rcri);
        
        return cns;
        
    }
    
    
    /*
     * create mock CNG
     */
    public static CodedNodeGraph createMockCng() throws LBException {
        
        // ResolvedConceptReferenceList rcrl = cng.resolveAsList(null, true, false, -1, -1, null, null, null, null, -1);
        CodedNodeGraph cng = EasyMock.createNiceMock(CodedNodeGraph.class);
        ResolvedConceptReferenceList rcrlTopNodes = new ResolvedConceptReferenceList();
        
        
        EasyMock.expect(cng.resolveAsList(null, true, false, -1, -1, null, null, null, null, -1)).andReturn(rcrlTopNodes);
        EasyMock.replay(cng);
        return cng;
    }
    
    


}

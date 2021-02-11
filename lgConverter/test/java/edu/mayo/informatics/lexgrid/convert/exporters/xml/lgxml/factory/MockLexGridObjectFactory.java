
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.factory;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.commonTypes.types.EntityTypes;
import org.easymock.EasyMock;
import org.hibernate.type.EntityType;

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
    public static CodedNodeGraph createCng() throws LBException {
        
        // ResolvedConceptReferenceList rcrl = cng.resolveAsList(null, true, false, -1, -1, null, null, null, null, -1);
        CodedNodeGraph cng = EasyMock.createNiceMock(CodedNodeGraph.class);
        
        ResolvedConceptReferenceList rcrlTopNodes = new ResolvedConceptReferenceList();

        // Adding top nodes for case when null as focus node.
        ResolvedConceptReference rcr = ResolvedConceptReferenceFactory.createRcrAutoMaker();
        rcrlTopNodes.addResolvedConceptReference(rcr);
        
        ResolvedConceptReferenceList firstBranchNodes = new ResolvedConceptReferenceList();
        ResolvedConceptReference rcr2 = ResolvedConceptReferenceFactory.createRcrFord();
        firstBranchNodes.addResolvedConceptReference(rcr2);
        
        AssociatedConcept asc = new AssociatedConcept();
        asc.setCode(rcr2.getCode());
        asc.setCodingSchemeName(rcr2.getCodingSchemeName());
        asc.setCodeNamespace(rcr2.getCodeNamespace());
        
        AssociatedConceptList ascl = new AssociatedConceptList();
        ascl.addAssociatedConcept(asc);
        
        Association asn = new Association();
        asn.setAssociationName(Constants.VALUE_HAS_SUB_TYPE);
        asn.setAssociatedConcepts(ascl);

        AssociationList asl = new AssociationList();
        asl.addAssociation(asn);

        rcr.setSourceOf(asl);
        
        ConceptReference focus1 = new ConceptReference();
        focus1.setCode(rcr.getConceptCode());
        focus1.setCodingSchemeName(rcr.getCodingSchemeName());
        
        rcr = ResolvedConceptReferenceFactory.createRcrAutomobile();
        rcrlTopNodes.addResolvedConceptReference(rcr);
        
        EasyMock.expect(cng.resolveAsList(null, true, false, 0, -1, null, null, null, null, -1)).andReturn(rcrlTopNodes);
        EasyMock.expect(cng.resolveAsList(focus1, true, false, 0, -1, null, null, null, null, -1)).andReturn(firstBranchNodes);
        
        EasyMock.replay(cng);
        return cng;
    }
    
    /*
     * create mock CNG
     */
    public static CodedNodeGraph createCngWith2AssociationPredicates() throws LBException {
        
        // ResolvedConceptReferenceList rcrl = cng.resolveAsList(null, true, false, -1, -1, null, null, null, null, -1);
        CodedNodeGraph cng = EasyMock.createNiceMock(CodedNodeGraph.class);
        CodedNodeGraph cng2 = EasyMock.createNiceMock(CodedNodeGraph.class);
        
        ResolvedConceptReferenceList rcrlTopNodes = new ResolvedConceptReferenceList();
        ResolvedConceptReferenceList rcrlTopNodesFiltered = new ResolvedConceptReferenceList();

        // Adding top nodes for case when null as focus node.
        ResolvedConceptReference rcr = ResolvedConceptReferenceFactory.createRcrAutoMaker();
        rcrlTopNodes.addResolvedConceptReference(rcr);
        
        ResolvedConceptReference rcrDup = ResolvedConceptReferenceFactory.createRcrAutoMaker();
        rcrlTopNodesFiltered.addResolvedConceptReference(rcrDup);

        
        ResolvedConceptReferenceList firstBranchNodes = new ResolvedConceptReferenceList();
        ResolvedConceptReference rcr2 = ResolvedConceptReferenceFactory.createRcrFord();
        
        ResolvedConceptReference rcr3 = ResolvedConceptReferenceFactory.createRcrTires();
        
        firstBranchNodes.addResolvedConceptReference(rcr2);
        
        AssociatedConcept asc = new AssociatedConcept();
        asc.setCode(rcr2.getCode());
        asc.setCodingSchemeName(rcr2.getCodingSchemeName());
        asc.setCodeNamespace(rcr2.getCodeNamespace());
        
        AssociatedConcept asc2 = new AssociatedConcept();
        asc2.setCode(rcr3.getCode());
        asc2.setCodingSchemeName(rcr3.getCodingSchemeName());
        asc2.setCodeNamespace(rcr3.getCodeNamespace());
        
        AssociatedConceptList ascl = new AssociatedConceptList();
        ascl.addAssociatedConcept(asc);
        
        AssociatedConceptList ascl2 = new AssociatedConceptList();
        ascl2.addAssociatedConcept(asc2);
        
        Association asn = new Association();
        asn.setAssociationName(Constants.VALUE_HAS_SUB_TYPE);
        asn.setAssociatedConcepts(ascl);

        Association asn2 = new Association();
        asn2.setAssociationName(Constants.VALUE_USES);
        asn2.setAssociatedConcepts(ascl2);
        
        AssociationList asl = new AssociationList();
        asl.addAssociation(asn);
        asl.addAssociation(asn2);

        AssociationList asl2 = new AssociationList();
        asl2.addAssociation(asn2);
        
        rcr.setSourceOf(asl);
        rcrDup.setSourceOf(asl2);
        
        ConceptReference focus1 = new ConceptReference();
        focus1.setCode(rcr.getConceptCode());
        focus1.setCodingSchemeName(rcr.getCodingSchemeName());
        
        NameAndValue nv = new NameAndValue();
        nv.setName(Constants.VALUE_USES);
        NameAndValueList assocNames = new NameAndValueList();
        assocNames.addNameAndValue(nv);
        
        rcr = ResolvedConceptReferenceFactory.createRcrAutomobile();
        rcrlTopNodes.addResolvedConceptReference(rcr);
        
        EasyMock.expect(cng.resolveAsList(null, true, false, 0, -1, null, null, null, null, -1)).andReturn(rcrlTopNodes);
        EasyMock.expect(cng.resolveAsList(focus1, true, false, 0, -1, null, null, null, null, -1)).andReturn(firstBranchNodes);
        EasyMock.expect(cng2.resolveAsList(null, true, false, 0, -1, null, null, null, null, -1)).andReturn(rcrlTopNodesFiltered);
        EasyMock.replay(cng2);
        
        EasyMock.expect(cng.restrictToAssociations(assocNames, null)).andReturn(cng2);
        
        EasyMock.replay(cng);
        return cng;
    }
}
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;

import junit.framework.Assert;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.relations.AssociationEntity;
import org.easymock.EasyMock;
import org.junit.Test;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.LexGridConstants;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.formatters.XmlContentWriter;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;

/*
 * Create a basic version of automobiles and 
 * create mock object of CNS and CNG.
 * 
 * Pass CNS and CNG to LexGridExport function. 
 * 
 * Note:  \ or /  is hasSubType
 *        +       is uses
 *  
 *  
 *                           @
 *                         /   \
 *                        /     \ 
 *               Auto Maker     Automobile
 *                            +   +  \     \
 *                           +   +    \     \
 *                      Tires Brakes Truck  Car
 */                             

public class LexGridExportTest {
    
    private CodingScheme cs = null;
    private CodedNodeGraph cng = null;
    private CodedNodeSet cns = null;
    

    @Test
    public void lexGridExportTest() throws LBException {
            //
        this.setUp();
        String outFileName = "lexGridExportTest.xml";
        
        
        File outFile = new File(outFileName);
        
        
        Writer w = null;
        BufferedWriter out = null;
        LexBIGService lbsvc = null;
        try {
            w = new FileWriter(outFile, false);
            out = new BufferedWriter(w);
            
            
            
            
    
//            lbsvc = LexBIGServiceImpl.defaultInstance();
//            codingScheme = lbsvc.resolveCodingScheme(codingSchemeUri, 
//                                        Constructors.createCodingSchemeVersionOrTagFromVersion(codingSchemeVersion));
            
            // create coded node graph
//            cng = lbsvc.getNodeGraph(codingScheme.getCodingSchemeURI(), 
//                    Constructors.createCodingSchemeVersionOrTagFromVersion(codingScheme.getRepresentsVersion()),null);
            
            // create coded node set
//            cns = lbsvc.getCodingSchemeConcepts(codingScheme.getCodingSchemeURI(), 
//                    Constructors.createCodingSchemeVersionOrTagFromVersion(codingScheme.getRepresentsVersion()) );
            
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
        Entities entities = new Entities();
        Entity entity = new Entity();
        entity.setEntityCode(LexGridConstants.MR_FLAG);
        entities.addEntity(entity);
        cs.setEntities(entities);
        
        
        XmlContentWriter xmlContentWriter = new XmlContentWriter();
        xmlContentWriter.marshalToXml(cs, cng, cns, out);
    }
    
    
    /*
     * create mock CNG, CNS
     */
    private void setUp() throws LBException {
        

        cs = LexGridObjectFactory.createCodingScheme();
        cns = EasyMock.createNiceMock(CodedNodeSet.class);
        cng = null;

        SupportedAssociation saHasSubType = LexGridObjectFactory.createSupportedAssociationHasSubType();
        cs.getMappings().addSupportedAssociation(saHasSubType);
        SupportedAssociation saUses = LexGridObjectFactory.createSupportedAssociationUses();
        cs.getMappings().addSupportedAssociation(saUses);
        
        ResolvedConceptReferenceList refList = new ResolvedConceptReferenceList();
        
        // create automaker and add to refList list
        ResolvedConceptReference rcrAutoMaker = new ResolvedConceptReference();
        Entity eAutoMaker = LexGridObjectFactory.createEntityAutoMaker();
        rcrAutoMaker.setEntity(eAutoMaker);
        refList.addResolvedConceptReference(rcrAutoMaker);

        // create root and add to refList list
        ResolvedConceptReference rcrRoot = new ResolvedConceptReference();
        Entity eRoot = LexGridObjectFactory.createEntityRoot();
        rcrRoot.setEntity(eRoot);
        refList.addResolvedConceptReference(rcrRoot);
        
        // create automobile and add to refList list
        ResolvedConceptReference rcrAutomobile = new ResolvedConceptReference();
        Entity eAutomobile = LexGridObjectFactory.createEntityAutomobile();
        rcrAutomobile.setEntity(eAutomobile);
        refList.addResolvedConceptReference(rcrAutomobile);
        
        // create car and add to refList list
        ResolvedConceptReference rcrCar = new ResolvedConceptReference();
        Entity eCar = LexGridObjectFactory.createEntityCar();
        rcrCar.setEntity(eCar);
        refList.addResolvedConceptReference(rcrCar);
        
        // create truck and add to refList list
        ResolvedConceptReference rcrTruck = new ResolvedConceptReference();
        Entity eTruck = LexGridObjectFactory.createEntityTruck();
        rcrTruck.setEntity(eTruck);
        refList.addResolvedConceptReference(rcrTruck);
        
        
        
        
//        ResolvedConceptReferencesIterator rcri = new ResolvedConceptReferencesIteratorImpl();
        
        ResolvedConceptReferencesIterator rcri = EasyMock.createNiceMock(ResolvedConceptReferencesIterator.class);
        EasyMock.expect(rcri.next(10)).andReturn(refList);
        EasyMock.expect(rcri.numberRemaining()).andReturn(1);

        EasyMock.expect(cns.resolve(null, null, null, null, true)).andReturn(rcri);
        EasyMock.replay(cns, rcri);
        
//        ResolvedConceptReferencesIterator rcri = new ResolvedConceptReferencesIterator();
        
        
//        EasyMock.expect(lbs.resolveCodingScheme("test", null)).andReturn(cs);
        
//        EasyMock.expect(
//                lbs.getNodeSet(
//                        EasyMock.eq("test"), 
//                        (CodingSchemeVersionOrTag)EasyMock.isNull(), 
//                        (LocalNameList) EasyMock.anyObject())).
//                            andReturn(cns);
        
//        EasyMock.expect(cns.restrictToCodes(
//                (ConceptReferenceList)EasyMock.anyObject())).andReturn(cns);
        
//        ResolvedConceptReferenceList refList =
//            new ResolvedConceptReferenceList();
        
//        ResolvedConceptReference ref =
//            new ResolvedConceptReference();
        
//        AssociationEntity ae = new AssociationEntity();
//        ae.setForwardName("some forward name");
//        ref.setEntity(ae);
        
//        refList.addResolvedConceptReference(ref);
        
//        EasyMock.expect(cns.resolveToList(null, null, null, -1)).andReturn(refList);
        
//        EasyMock.replay(lbs,cns);
        
//        LexBIGServiceConvenienceMethodsImpl lbscm =
//            new LexBIGServiceConvenienceMethodsImpl();
    
//        lbscm.setLexBIGService(lbs);
        
//        String forwardName =
//            lbscm.getAssociationForwardName("assocName", "test", null);
        
//        Assert.assertEquals("some forward name", forwardName);
    }
    

}

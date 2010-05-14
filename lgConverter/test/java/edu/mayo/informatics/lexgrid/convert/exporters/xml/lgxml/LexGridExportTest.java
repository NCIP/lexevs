package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import junit.framework.Assert;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.junit.Test;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.Constants;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.LexGridConstants;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.factory.CodingSchemeFactory;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.factory.MockLexGridObjectFactory;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.formatters.XmlContentWriter;

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
    
    @Test
    public void lexGridExportTestCns() throws LBException {
        String outFileName = "lexGridExportTestCns.xml";
        CodingScheme cs = CodingSchemeFactory.createCodingScheme();
        CodedNodeSet cns = MockLexGridObjectFactory.createCns();
        CodedNodeGraph cng = null;
        LexGridExportTest.lexGridExportTestRunner(cs, cng, cns, outFileName);
        Assert.assertTrue(true);
    }
    
    @Test
    public void lexGridExportTestCng() throws LBException {
        Assert.assertTrue(false);
    }
    
    @Test
    public void lexGridExportTestAll() throws LBException {
        Assert.assertTrue(false);
    }
    
    
    private static void lexGridExportTestRunner(CodingScheme cs, CodedNodeGraph cng, CodedNodeSet cns, String outFileName) throws LBException {

        File outFile = new File(outFileName);
        
        Writer w = null;
        BufferedWriter out = null;
        try {
            w = new FileWriter(outFile, false);
            out = new BufferedWriter(w);
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
        Entities entities = new Entities();
        Entity entity = new Entity();
        entity.setEntityCode(LexGridConstants.MR_FLAG);
        entities.addEntity(entity);
        cs.setEntities(entities);
        
        XmlContentWriter xmlContentWriter = new XmlContentWriter();
        xmlContentWriter.marshalToXml(cs, cng, cns, out, Constants.VALUE_PAGE_SIZE);
    }
}

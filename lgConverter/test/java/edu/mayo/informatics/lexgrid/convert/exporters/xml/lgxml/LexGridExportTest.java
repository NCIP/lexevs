
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import junit.framework.Assert;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
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
 *    Domestic Auto Makers     Automobile
 *                  /         +   +  \     \
 *                 /         +   +    \     \
 *               Ford    Tires Brakes Truck  Car
 */          

public class LexGridExportTest {
    
    private static final String SEARCH_STRING_ENTITY_CODE_AUTOMOBILE = "entityCode=\"A0001\"";
    private static final String SEARCH_STRING_ENTITY_CODE_CAR = "entityCode=\"C0001\"";
    private static final String SEARCH_STRING_ENTITY_CODE_DOMESTIC_AUTO_MAKERS = "entityCode=\"005\"";
    private static final String SEARCH_STRING_ENTITY_CODE_FORD = "entityCode=\"Ford\"";
    private static final String SEARCH_STRING_ENTITY_CODE_TRUCK = "entityCode=\"T0001\"";
    
    private static final String SEARCH_STRING_SRC_ENTITY_CODE_DOMESTIC_AUTO_MAKERS = "sourceEntityCode=\"005\"";
    private static final String SEARCH_STRING_TRG_ENTITY_CODE_FORD = "targetEntityCode=\"Ford\"";
    private static final String SEARCH_STRING_TRG_ENTITY_CODE_TIRES = "targetEntityCode=\"Tires\"";
    
    @Test    
    public void lexGridExportTestCns() throws LBException {
        StringWriter out = new StringWriter();
        CodingScheme cs = CodingSchemeFactory.createCodingScheme();
        CodedNodeSet cns = MockLexGridObjectFactory.createCns();
        CodedNodeGraph cng = null;
        LexGridExportTest.lexGridExportTestRunner(cs, cng, cns, out);
        
        //-----------------------------------------------------
        // to verify, all entity codes should exist
        //-----------------------------------------------------
        String marshaledContent = out.toString();
        
        // check for Domestic Auto Makers: entityCode="005"
        boolean entityCodeExists = marshaledContent.contains(LexGridExportTest.SEARCH_STRING_ENTITY_CODE_DOMESTIC_AUTO_MAKERS);
        Assert.assertTrue(entityCodeExists);
        
        // check for Automobile: entityCode="A0001"
        entityCodeExists = marshaledContent.contains(LexGridExportTest.SEARCH_STRING_ENTITY_CODE_AUTOMOBILE);
        Assert.assertTrue(entityCodeExists);        
        
        // check for Ford: entityCode="Ford"
        entityCodeExists = marshaledContent.contains(LexGridExportTest.SEARCH_STRING_ENTITY_CODE_FORD);
        Assert.assertTrue(entityCodeExists);
        
        // check for Truck: entityCode="T0001"
        entityCodeExists = marshaledContent.contains(LexGridExportTest.SEARCH_STRING_ENTITY_CODE_TRUCK);  
        Assert.assertTrue(entityCodeExists);
        
        // check for Car: entityCode="C0001"
        entityCodeExists = marshaledContent.contains(LexGridExportTest.SEARCH_STRING_ENTITY_CODE_CAR);
        Assert.assertTrue(entityCodeExists);
        
    }
    
    /*
     * cns.restrictToMatchingDesignations("T*", SearchDesignationOption.ALL, "LuceneQuery", null);
     */
    @Test
    public void lexGridExportTestCnsFilter() throws LBException {
        StringWriter out = new StringWriter();
        CodingScheme cs = CodingSchemeFactory.createCodingScheme();
        CodedNodeSet cns = MockLexGridObjectFactory.createCnsFiltered();
        CodedNodeGraph cng = null;
        LexGridExportTest.lexGridExportTestRunner(cs, cng, cns, out);
        
        //-----------------------------------------------------
        // to verify, only the truck entity code should exist
        //-----------------------------------------------------
        String marshaledContent = out.toString();
        
        // check for Domestic Auto Makers: entityCode="005"
        boolean entityCodeExists = marshaledContent.contains(LexGridExportTest.SEARCH_STRING_ENTITY_CODE_DOMESTIC_AUTO_MAKERS);
        Assert.assertFalse(entityCodeExists);
        
        // check for Automobile: entityCode="A0001"
        entityCodeExists = marshaledContent.contains(LexGridExportTest.SEARCH_STRING_ENTITY_CODE_AUTOMOBILE);
        Assert.assertFalse(entityCodeExists);        
        
        // check for Ford: entityCode="Ford"
        entityCodeExists = marshaledContent.contains(LexGridExportTest.SEARCH_STRING_ENTITY_CODE_FORD);
        Assert.assertFalse(entityCodeExists);
        
        // check for Truck: entityCode="T0001"
        entityCodeExists = marshaledContent.contains(LexGridExportTest.SEARCH_STRING_ENTITY_CODE_TRUCK);  
        Assert.assertTrue(entityCodeExists);
        
        // check for Car: entityCode="C0001"
        entityCodeExists = marshaledContent.contains(LexGridExportTest.SEARCH_STRING_ENTITY_CODE_CAR);
        Assert.assertFalse(entityCodeExists);
    }
    
    
    
    @Test
    public void lexGridExportTestCng() throws LBException 
    {
        StringWriter out = new StringWriter();
        CodingScheme cs = CodingSchemeFactory.createCodingSchemeWithAssociationPredicate();
        CodedNodeGraph cng = MockLexGridObjectFactory.createCng();
        CodedNodeSet cns = null;

        LexGridExportTest.lexGridExportTestRunner(cs, cng, cns, out);
        
      //-----------------------------------------------------
        // to verify, all entity codes should exist
        //-----------------------------------------------------
        String marshaledContent = out.toString();
        
        // check for Domestic Auto Makers: entityCode="005"
        boolean srcEntityCodeExists = marshaledContent.contains(LexGridExportTest.SEARCH_STRING_SRC_ENTITY_CODE_DOMESTIC_AUTO_MAKERS);
        boolean trgEntityCodeExists = marshaledContent.contains(LexGridExportTest.SEARCH_STRING_TRG_ENTITY_CODE_FORD);
        Assert.assertTrue(srcEntityCodeExists && trgEntityCodeExists);
    }

    @Test
    public void lexGridExportTestCngFilter() throws LBException 
    {
        StringWriter out = new StringWriter();
        CodingScheme cs = CodingSchemeFactory.createCodingSchemeWithAssociationPredicate();
        CodedNodeGraph cng = MockLexGridObjectFactory.createCngWith2AssociationPredicates();
        
        NameAndValue nv = new NameAndValue();
        nv.setName(Constants.VALUE_USES);
        NameAndValueList assocNames = new NameAndValueList();
        assocNames.addNameAndValue(nv);
        CodedNodeGraph cng2 = cng.restrictToAssociations(assocNames, null);
        CodedNodeSet cns = null;

        LexGridExportTest.lexGridExportTestRunner(cs, cng2, cns, out);
        
      //-----------------------------------------------------
        // to verify, all entity codes should exist
        //-----------------------------------------------------
        String marshaledContent = out.toString();
        
     // check for "Domestic Auto Makers" --> "Ford"  (MUST NOT Exists)
        boolean srcEntityCodeExists = marshaledContent.contains(LexGridExportTest.SEARCH_STRING_SRC_ENTITY_CODE_DOMESTIC_AUTO_MAKERS);
        boolean trgEntityCodeExists = marshaledContent.contains(LexGridExportTest.SEARCH_STRING_TRG_ENTITY_CODE_FORD);
        Assert.assertFalse(srcEntityCodeExists && trgEntityCodeExists);
        
        // check for "Domestic Auto Makers" --> "Tires"  (MUST Exists)
        srcEntityCodeExists = marshaledContent.contains(LexGridExportTest.SEARCH_STRING_SRC_ENTITY_CODE_DOMESTIC_AUTO_MAKERS);
        trgEntityCodeExists = marshaledContent.contains(LexGridExportTest.SEARCH_STRING_TRG_ENTITY_CODE_TIRES);
        Assert.assertTrue(srcEntityCodeExists && trgEntityCodeExists);
    }

    @Test
    public void lexGridExportTestFileWriter() {
        
        String outFileName = "lexGridExportTestFileWriter.xml";        
        File outFile = new File(outFileName);
        Writer w = null;
        BufferedWriter out = null;
        try {
            w = new FileWriter(outFile, false);
            out = new BufferedWriter(w);
            CodingScheme cs = CodingSchemeFactory.createCodingScheme();
            CodedNodeSet cns = MockLexGridObjectFactory.createCns();
            CodedNodeGraph cng = null;
            LexGridExportTest.lexGridExportTestRunner(cs, cng, cns, out);
            
            boolean outFileExists = outFile.exists();
            Assert.assertTrue(outFileExists);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(out != null){
                try {
                    out.close();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(w != null) {
                try {
                    w.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        boolean outFileHasContent = LexGridExportTest.verifyOutFileHasContent(outFile);
        if(outFile != null && outFile.exists()) {
            boolean result = outFile.delete();
            //System.out.println("File delete result: " + result);
        }
        
        Assert.assertTrue(outFileHasContent);       
        

        
    }
    
    private static boolean verifyOutFileHasContent(File outFile) {
        boolean verifyTrue = false;
        final String searchTarget = LexGridExportTest.SEARCH_STRING_ENTITY_CODE_DOMESTIC_AUTO_MAKERS;
        //final String searchTarget = "blah";
        Reader r = null;
        BufferedReader in = null;
        try {
            r = new FileReader(outFile);
            in = new BufferedReader(r);
            if(in != null) {
                boolean done = false;
                String line = null;
                while(!done)
                {
                    line = in.readLine();
                    if(line == null) {
                        done = true;
                    } else {
                        if(line.contains(searchTarget) == true) {
                            verifyTrue = true;
                            done = true;
                        }                        
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(r != null) {
                try {
                    r.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return verifyTrue;
    }

    
    private static void lexGridExportTestRunner(CodingScheme cs, CodedNodeGraph cng, CodedNodeSet cns, Writer out) throws LBException {
        
        Entities entities = new Entities();
        Entity entity = new Entity();
        entity.setEntityCode(LexGridConstants.MR_FLAG);
        entities.addEntity(entity);
        cs.setEntities(entities);
        
        XmlContentWriter xmlContentWriter = new XmlContentWriter();
        xmlContentWriter.marshalToXml(cs, cng, cns, out, Constants.VALUE_PAGE_SIZE, false, false, null);  // TODO: need to pass in a LgMessageDirectorIF instead of null.
    }
}
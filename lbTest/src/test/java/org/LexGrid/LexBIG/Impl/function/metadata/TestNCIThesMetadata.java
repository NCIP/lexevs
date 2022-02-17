
package org.LexGrid.LexBIG.Impl.function.metadata;

import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.MetadataProperty;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;

/**
 * Tests Metadata search for NCI Thes.
 * 
 * @author Kevin Peterson
 * 
 */
public class TestNCIThesMetadata extends LexBIGServiceTestCase {

    final static String testID = "T1_FNC_70";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testT1_FNC_50() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();

        LexBIGServiceMetadata smd = lbsi.getServiceMetadata();

        AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
        acsvr.setCodingSchemeURN(META_URN);
        acsvr.setCodingSchemeVersion(SAMPLE_META_VERSION);
        smd.restrictToCodingScheme(acsvr);

        MetadataPropertyList mdpl = smd.resolve();
        Iterator<? extends MetadataProperty> metaItr = mdpl.iterateMetadataProperty();
        assertTrue("Did not find the Metadata for the Coding Scheme", metaItr.hasNext());

        // Look through the Metadata and make sure its returning the coding
        // schemes...
        // Here we check a few.
        boolean foundLoinc = false;
        boolean foundMedra = false;
        while (metaItr.hasNext()) {
            MetadataProperty property = metaItr.next();
           
            if (property.getName().equals("rsab") && property.getValue().equals("LNC")) {
            	foundLoinc = true;
            }
            if (property.getName().equals("rsab") && property.getValue().equals("MDR")) {
                foundMedra = true;
            }
           
        }
        assertTrue("Didn't find the Metadata that was expected", foundLoinc);
        assertTrue("Didn't find the Metadata that was expected", foundMedra);
    }
}
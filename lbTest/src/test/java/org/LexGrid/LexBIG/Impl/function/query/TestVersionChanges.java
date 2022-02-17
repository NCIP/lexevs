
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_03	TestVersionChanges

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.LexBIG.DataModel.NCIHistory.types.ChangeType;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;

public class TestVersionChanges extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_03";

    @Override
    protected String getTestID() {
        return testID;
    }

    @SuppressWarnings("deprecation")
	public void testT1_FNC_03() throws URISyntaxException, LBException {

        HistoryService hs = ServiceHolder.instance().getLexBIGService().getHistoryService(
                "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");

        NCIChangeEvent[] nce = hs.getEditActionList(null,
                new URI("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:05.11f")).getEntry();

        assertTrue(nce.length == 1);
        assertTrue(nce[0].getConceptcode().equals("C640"));
        assertTrue(nce[0].getConceptName().equals("Methaqualone"));
        
        Date date = nce[0].getEditDate();
        assertEquals(7, date.getDate());
        assertEquals(11, date.getMonth());
        assertEquals(105, date.getYear());
        
        assertTrue(nce[0].getReferencecode() == null || nce[0].getReferencecode().equals(""));
        assertTrue(nce[0].getReferencename().equals("Current Dental Terminology 2005"));
        assertTrue(nce[0].getEditaction().equals(ChangeType.MODIFY));

    }
}
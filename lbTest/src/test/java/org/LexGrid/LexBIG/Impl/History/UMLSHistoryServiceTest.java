
package org.LexGrid.LexBIG.Impl.History;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Collections.SystemReleaseList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SystemReleaseDetail;
import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.LexBIG.DataModel.NCIHistory.types.ChangeType;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.versions.CodingSchemeVersion;
import org.LexGrid.versions.EntityVersion;
import org.LexGrid.versions.SystemRelease;

public class UMLSHistoryServiceTest extends TestCase {

    private static final String sRelease200505_ = "200505";
    private static final String sRelease200603_ = "200603";
    private static final String sRelease200610_ = "200610";
    private static final String sRelease200702_ = "200702";

    private static Date dRelease200505_ = null;
    private static Date dRelease200603_ = null;
    private static Date dRelease200610_ = null;
    private static Date dRelease200702_ = null;

    private static final String sReleaseAgency_ = "http://nlm.gov";

    public UMLSHistoryServiceTest() {

        Calendar cal = Calendar.getInstance();

        cal.set(2005, Calendar.MAY, 01, 00, 00, 00);
        cal.set(Calendar.MILLISECOND, 00);
        dRelease200505_ = cal.getTime();

        cal.set(2006, Calendar.MARCH, 01, 00, 00, 00);
        cal.set(Calendar.MILLISECOND, 00);
        dRelease200603_ = cal.getTime();

        cal.set(2006, Calendar.OCTOBER, 01, 00, 00, 00);
        cal.set(Calendar.MILLISECOND, 00);
        dRelease200610_ = cal.getTime();

        cal.set(2007, Calendar.FEBRUARY, 01, 00, 00, 00);
        cal.set(Calendar.MILLISECOND, 00);
        dRelease200702_ = cal.getTime();

    }

    public void testGetBaselines() throws LBException {
        ServiceHolder.configureForSingleConfig();
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService(HistoryService.metaURN);

        SystemReleaseList srl = hs.getBaselines(null, null);
        assertTrue(srl.getSystemReleaseCount() == 12);
        assertTrue(containsSystemReleaseId(srl, sRelease200505_));
        assertTrue(containsSystemReleaseId(srl, sRelease200603_));
        assertTrue(containsSystemReleaseId(srl, sRelease200610_));
        assertTrue(containsSystemReleaseId(srl, sRelease200702_));

        srl = hs.getBaselines(dRelease200505_, dRelease200603_);
        assertTrue(srl.getSystemReleaseCount() == 5);

        srl = hs.getBaselines(dRelease200505_, null);
        assertTrue(srl.getSystemReleaseCount() == 12);

        srl = hs.getBaselines(null, dRelease200610_);
        assertTrue(srl.getSystemReleaseCount() == 9);
    }

    public void testGetEarliestBaseline() throws LBException {

        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService(HistoryService.metaURN);

        SystemRelease sr = hs.getEarliestBaseline();
        assertTrue(sr.getReleaseId().equals(sRelease200505_));
        assertTrue(sr.getReleaseAgency().equals(sReleaseAgency_));
        assertTrue(sr.getReleaseURI().equals(HistoryService.metaURN + ":" + sRelease200505_));
        assertTrue(sr.getReleaseDate().equals(dRelease200505_));
    }

    public void testGetLatestBaseline() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService(HistoryService.metaURN);

        SystemRelease sr = hs.getLatestBaseline();
        assertTrue(sr.getReleaseId().equals(sRelease200702_));
        assertTrue(sr.getReleaseAgency().equals(sReleaseAgency_));
        assertTrue(sr.getReleaseURI().equals(HistoryService.metaURN + ":" + sRelease200702_));
        assertTrue(sr.getReleaseDate().equals(dRelease200702_));
    }

    public void testGetSystemRelease() throws LBException, URISyntaxException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService(HistoryService.metaURN);

        SystemReleaseDetail srd = hs.getSystemRelease(new URI(HistoryService.metaURN + ":" + sRelease200610_));

        EntityVersion[] ev = srd.getEntityVersions();
        assertFalse(ev[1].getIsComplete().booleanValue());
        assertTrue(ev[1].getReleaseURN().equals(HistoryService.metaURN + ":" + sRelease200610_));

        assertTrue(ev[1].getVersionDate().equals(dRelease200610_));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");

        assertTrue(ev[1].getVersion().equalsIgnoreCase(dateFormat.format(dRelease200610_)));
    }

    public void testGetDescandents() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();

        HistoryService hs = lbsi.getHistoryService(HistoryService.metaURN);

        NCIChangeEvent[] nce = hs.getDescendants(ConvenienceMethods.createConceptReference("C0025154", null))
                .getEntry();

        assertTrue(nce.length == 1);

        assertTrue(nce[0].getReferencecode().equals("C0701380"));
    }

    public void testGetAncestors() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService(HistoryService.metaURN);

        NCIChangeEvent[] nce = hs.getAncestors(ConvenienceMethods.createConceptReference("CL357442", null)).getEntry();

        assertTrue(nce.length == 2);

        if (nce[0].getConceptcode().equals("C0434589")) {
            assertTrue(nce[1].getConceptcode().equals("C0149977"));
        } else {
            assertTrue(nce[0].getConceptcode().equals("C0149977"));
        }
    }

    public void testGetConceptCreationVersion() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService(HistoryService.metaURN);

        try {
            hs.getConceptCreationVersion(Constructors.createConceptReference("CL357442", null));
        } catch (Exception e) {
            return;
        }
        fail("Did not throw exception it should have.");
    }

    public void testGetConceptChangeVersions() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService(HistoryService.metaURN);

        CodingSchemeVersion[] csv = hs.getConceptChangeVersions(Constructors.createConceptReference("C0026055", null),
                null, null).getEntry();

        assertTrue(csv.length == 1);
        assertTrue(csv[0].getReleaseURN().equals(HistoryService.metaURN + ":" + sRelease200610_));
        assertTrue(csv[0].getVersion().equals("01-OCT-06"));
        assertFalse(csv[0].getIsComplete());
        assertTrue(csv[0].getVersionDate().getTime() == dRelease200610_.getTime());

    }
/*
    public void testGetEditActionList() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService(HistoryService.metaURN);

        CodingSchemeVersion csv = new CodingSchemeVersion();
        csv.setReleaseURN(HistoryService.metaURN + ":200610");

        NCIChangeEvent[] nce = hs.getEditActionList(null, csv).getEntry();

        assertEquals(54, nce.length);
        assertTrue(nce[6].getConceptcode().equals("C0027396"));
        assertTrue(nce[6].getConceptName().equals("Not Available."));
        assertTrue(nce[6].getEditDate().getTime() == Long.parseLong("1159678800000"));
        assertTrue(nce[6].getReferencecode().equals("C0700017"));
        assertTrue(nce[6].getReferencename().equals("Not Available."));
        assertTrue(nce[6].getEditaction().equals(ChangeType.MERGE));

        csv = new CodingSchemeVersion();
        csv.setReleaseURN(HistoryService.metaURN + ":200610");
        nce = hs.getEditActionList(Constructors.createConceptReference("C0004029", ""), csv).getEntry();

        assertTrue(nce.length == 1);

        assertTrue(nce[0].getConceptcode().equals("C0004029"));
        assertTrue(nce[0].getEditDate().getTime() == Long.parseLong("1159678800000"));
        assertTrue(nce[0].getReferencecode().equals("C0017641"));
        assertTrue(nce[0].getEditaction().equals(ChangeType.MERGE));
    }

    public void testGetEditActionList2() throws LBException {

        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService(HistoryService.metaURN);

        Calendar cal = Calendar.getInstance();
        cal.set(2006, Calendar.APRIL, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date begin = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(2006, Calendar.JUNE, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date end = cal.getTime();

        NCIChangeEvent[] nce = hs.getEditActionList(Constructors.createConceptReference("C1608803", ""), null, null)
                .getEntry();

        assertTrue(nce.length == 3);

        nce = hs.getEditActionList(Constructors.createConceptReference("C1608803", ""), null, end).getEntry();
        assertTrue(nce.length == 2);

        assertTrue(nce[1].getConceptcode().equals("C0390269"));
        assertTrue(nce[1].getEditDate().getTime() == Long.parseLong("1146459600000"));
        assertTrue(nce[1].getReferencecode().equals("C1608803"));
        assertTrue(nce[1].getEditaction().equals(ChangeType.MERGE));

        nce = hs.getEditActionList(Constructors.createConceptReference("C1608803", ""), begin, null).getEntry();
        assertTrue(nce.length == 3);

        assertTrue(nce[0].getConceptcode().equals("C0390267"));
        assertTrue(nce[0].getEditDate().getTime() == Long.parseLong("1146459600000"));
        assertTrue(nce[0].getReferencecode().equals("C1608803"));
        assertTrue(nce[0].getEditaction().equals(ChangeType.RETIRE));

        nce = hs.getEditActionList(Constructors.createConceptReference("C1608803", ""), begin, end).getEntry();
        assertTrue(nce.length == 2);

        assertTrue(nce[1].getConceptcode().equals("C0390269"));
        assertTrue(nce[1].getEditDate().getTime() == Long.parseLong("1146459600000"));
        assertTrue(nce[1].getReferencecode().equals("C1608803"));
        assertTrue(nce[1].getEditaction().equals(ChangeType.MERGE));
    }
*/
    @SuppressWarnings("deprecation")
	public void testGetEditActionList3() throws LBException, URISyntaxException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService(HistoryService.metaURN);

        NCIChangeEvent[] nce = hs.getEditActionList(Constructors.createConceptReference("C0359583", null),
                new URI(HistoryService.metaURN + ":" + sRelease200702_)).getEntry();

        assertTrue(nce.length == 1);
        assertTrue(nce[0].getConceptcode().equals("C0359583"));
        
        Date date = nce[0].getEditDate();
        assertEquals(1, date.getDate());
        assertEquals(1, date.getMonth());
        assertEquals(107, date.getYear());
        
        assertTrue(nce[0].getReferencecode().equals("C0242295"));
        assertTrue(nce[0].getEditaction().equals(ChangeType.RETIRE));
    }

    private static boolean containsSystemReleaseId(SystemReleaseList list, String id) {
    	for(SystemRelease sr : list.getSystemRelease()) {
    		if(sr.getReleaseId().endsWith(id)) {
    			return true;
    		}
    	}
    	return false;
    }
}

package org.LexGrid.LexBIG.Impl.History;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
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

/**
 * JUnit tests for history service.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
@SuppressWarnings("deprecation")
public class NCIThesaurusHistoryServiceTest extends TestCase {
	
    public void testGetBaselines() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");

        SystemReleaseList srl = hs.getBaselines(null, null);

        assertTrue(srl.getSystemReleaseCount() == 29);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2005);
        cal.set(Calendar.MONTH, 10);
        cal.set(Calendar.DAY_OF_MONTH, 25);

        Date after = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2005);
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, 25);

        Date before = cal.getTime();

        srl = hs.getBaselines(after, before);
        assertTrue(srl.getSystemReleaseCount() == 2);

        srl = hs.getBaselines(after, null);
        assertTrue(srl.getSystemReleaseCount() == 4);

        srl = hs.getBaselines(null, before);
        assertTrue(srl.getSystemReleaseCount() == 27);
    }

    public void testGetEarliestBaseline() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");

        SystemRelease sr = hs.getEarliestBaseline();
        assertTrue(sr.getReleaseId().equals("v1.0"));
        assertTrue(sr.getReleaseAgency().equals("http://nci.nih.gov/"));
        assertTrue(sr.getReleaseURI().equals("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:v1.0"));
        
        Date date = sr.getReleaseDate();
        assertEquals(15, date.getDate());
        assertEquals(7, date.getMonth());
        assertEquals(102, date.getYear());
       
        assertTrue(sr.getEntityDescription().getContent().equals(
                "NCI Thesaurus with editing completed through July 22, 2002"));

    }

    public void testGetLatestBaseline() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");

        SystemRelease sr = hs.getLatestBaseline();
        assertTrue(sr.getReleaseId().equals("06.01c"));
    }

    public void testGetSystemRelease() throws LBException, URISyntaxException, ParseException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");

        SystemReleaseDetail srd = hs.getSystemRelease(new URI(
                "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:05.12f"));

        EntityVersion[] ev = srd.getEntityVersions();
        assertFalse(ev[0].getIsComplete().booleanValue());
        assertTrue(ev[0].getReleaseURN().equals("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:05.12f"));
        assertTrue(ev[0]
                .getEntityDescription()
                .getContent()
                .equals(
                        "Editing of NCI Thesaurus 05.12f was completed on January 3, 2006.  Version 05.12f was December's sixth build in our development cycle."));
    }

    public void testGetConceptCreationVersion() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");

        CodingSchemeVersion csv = hs.getConceptCreationVersion(Constructors.createConceptReference("C49239", null));

        assertTrue(csv.getReleaseURN().equals("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:05.12f"));
        assertEquals("03-JAN-06", csv.getVersion());
        assertFalse(csv.getIsComplete());
        
        Date date = csv.getVersionDate();
        assertEquals(6, date.getDate());
        assertEquals(1, date.getMonth());
        assertEquals(106, date.getYear());
        
        assertTrue(csv
                .getEntityDescription()
                .getContent()
                .equals(
                        "Editing of NCI Thesaurus 05.12f was completed on January 3, 2006.  Version 05.12f was December's sixth build in our development cycle."));

        csv = hs.getConceptCreationVersion(Constructors.createConceptReference("C49239",
                "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#"));

        assertTrue(csv.getReleaseURN().equals("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:05.12f"));
        assertTrue(csv.getVersion().equals("03-JAN-06"));
        assertFalse(csv.getIsComplete());
        
        date = csv.getVersionDate();
        assertEquals(6, date.getDate());
        assertEquals(1, date.getMonth());
        assertEquals(106, date.getYear());
        
        assertTrue(csv
                .getEntityDescription()
                .getContent()
                .equals(
                        "Editing of NCI Thesaurus 05.12f was completed on January 3, 2006.  Version 05.12f was December's sixth build in our development cycle."));
    }

    public void testGetConceptChangeVersions() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");

        CodingSchemeVersion[] csv = hs.getConceptChangeVersions(Constructors.createConceptReference("C51826", null),
                null, null).getEntry();

        assertEquals(2,csv.length);
        assertTrue(csv[0].getReleaseURN().equals("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:05.12f"));
        assertTrue(csv[0].getVersion().equals("22-DEC-05") || csv[1].getVersion().equals("22-DEC-05"));
        assertFalse(csv[0].getIsComplete());
       
        Date date = csv[0].getVersionDate();
        assertEquals(6, date.getDate());
        assertEquals(1, date.getMonth());
        assertEquals(106, date.getYear());
        
        assertTrue(csv[0]
                .getEntityDescription()
                .getContent()
                .equals(
                        "Editing of NCI Thesaurus 05.12f was completed on January 3, 2006.  Version 05.12f was December's sixth build in our development cycle."));
    }

    public void testGetEditActionList() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");

        CodingSchemeVersion csv = new CodingSchemeVersion();
        csv.setVersion("22-DEC-05");

        NCIChangeEvent[] nce = hs.getEditActionList(null, csv).getEntry();

        assertEquals(1,nce.length);
        assertTrue(nce[0].getConceptcode().equals("C51826"));
        assertTrue(nce[0].getConceptName().equals("Grant_Principal_Investigator"));
        
        Date date = nce[0].getEditDate();
        assertEquals(22, date.getDate());
        assertEquals(11, date.getMonth());
        assertEquals(105, date.getYear());
        
        assertTrue(nce[0].getReferencecode() == null || nce[0].getReferencecode().equals(""));
        assertTrue(nce[0].getReferencename().equals("Grant_PI"));
        assertTrue(nce[0].getEditaction().equals(ChangeType.MODIFY));

        csv = new CodingSchemeVersion();
        csv.setVersion("03-JAN-06");
        nce = hs.getEditActionList(Constructors.createConceptReference("C16205", ""), csv).getEntry();

        assertEquals(2,nce.length);
        int i = 0;
        if (nce[0].getConceptcode().equals("C15363")) {
            i = 0;
        } else {
            i = 1;
        }
        assertTrue(nce[i].getConceptcode().equals("C15363"));
        assertTrue(nce[i].getConceptName().equals("Healthcare"));
        
        date = nce[i].getEditDate();
        assertEquals(3, date.getDate());
        assertEquals(0, date.getMonth());
        assertEquals(106, date.getYear());
        
        assertTrue(nce[i].getReferencecode().equals("C16205"));
        assertTrue(nce[i].getReferencename().equals("Healthcare_Activity"));
        assertTrue(nce[i].getEditaction().equals(ChangeType.MERGE));
    }

    public void testGetEditActionList2() throws LBException {
        // no, the data doesn't make any sense. They have modify dates that are
        // before the
        // being dates... not my fault.
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2006);
        cal.set(Calendar.MONTH, 10);
        cal.set(Calendar.DAY_OF_MONTH, 25);

        Date after = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2006);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        Date before = cal.getTime();

        NCIChangeEvent[] nce = hs.getEditActionList(Constructors.createConceptReference("C51826", ""), null, null)
                .getEntry();

        assertTrue(nce.length == 2);

        nce = hs.getEditActionList(Constructors.createConceptReference("C51826", ""), null, before).getEntry();
        assertTrue(nce.length == 1);

        assertTrue(nce[0].getConceptcode().equals("C51826"));
        assertTrue(nce[0].getConceptName().equals("Grant_Principal_Investigator"));
        
        Date date = nce[0].getEditDate();
        assertEquals(22, date.getDate());
        assertEquals(11, date.getMonth());
        assertEquals(105, date.getYear());
        
        assertTrue(nce[0].getReferencecode() == null || nce[0].getReferencecode().length() == 0);
        assertTrue(nce[0].getReferencename().equals("Grant_PI"));
        assertTrue(nce[0].getEditaction().equals(ChangeType.MODIFY));

        nce = hs.getEditActionList(Constructors.createConceptReference("C51826", ""), before, null).getEntry();
        assertTrue(nce.length == 1);

        assertTrue(nce[0].getConceptcode().equals("C51826"));
        assertTrue(nce[0].getConceptName().equals("Grant_Principal_Investigator"));
        
        date = nce[0].getEditDate();
        assertEquals(3, date.getDate());
        assertEquals(0, date.getMonth());
        assertEquals(106, date.getYear());
        
        assertTrue(nce[0].getReferencecode() == null || nce[0].getReferencecode().length() == 0);
        assertTrue(nce[0].getReferencename() == null || nce[0].getReferencename().length() == 0);
        assertTrue(nce[0].getEditaction().equals(ChangeType.CREATE));

        nce = hs.getEditActionList(Constructors.createConceptReference("C51826", ""), before, after).getEntry();
        assertTrue(nce.length == 1);
        assertTrue(nce[0].getConceptcode().equals("C51826"));
        assertTrue(nce[0].getConceptName().equals("Grant_Principal_Investigator"));
        
        date = nce[0].getEditDate();
        assertEquals(3, date.getDate());
        assertEquals(0, date.getMonth());
        assertEquals(106, date.getYear());
        
        assertTrue(nce[0].getReferencecode() == null || nce[0].getReferencecode().length() == 0);
        assertTrue(nce[0].getReferencename() == null || nce[0].getReferencename().length() == 0);
        assertTrue(nce[0].getEditaction().equals(ChangeType.CREATE));
    }

	public void testGetEditActionList3() throws LBException, URISyntaxException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");

        NCIChangeEvent[] nce = hs.getEditActionList(null,
                new URI("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:05.11f")).getEntry();

        assertEquals(1,nce.length);
        assertTrue(nce[0].getConceptcode().equals("C640"));
        assertTrue(nce[0].getConceptName().equals("Methaqualone"));
        
        Date date = nce[0].getEditDate();
        assertEquals(7, date.getDate());
        assertEquals(11, date.getMonth());
        assertEquals(105, date.getYear());
        
        assertTrue(nce[0].getReferencecode() == null || nce[0].getReferencecode().equals(""));
        assertTrue(nce[0].getReferencename().equals("Current Dental Terminology 2005"));
        assertTrue(nce[0].getEditaction().equals(ChangeType.MODIFY));

        nce = hs.getEditActionList(Constructors.createConceptReference("C640", null),
                new URI("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:05.11f")).getEntry();

        assertTrue(nce.length == 1);
        assertTrue(nce[0].getConceptcode().equals("C640"));
        assertTrue(nce[0].getConceptName().equals("Methaqualone"));
        
        date = nce[0].getEditDate();
        assertEquals(7, date.getDate());
        assertEquals(11, date.getMonth());
        assertEquals(105, date.getYear());
        
        assertTrue(nce[0].getReferencecode() == null || nce[0].getReferencecode().equals(""));
        assertTrue(nce[0].getReferencename().equals("Current Dental Terminology 2005"));
        assertTrue(nce[0].getEditaction().equals(ChangeType.MODIFY));

        nce = hs.getEditActionList(Constructors.createConceptReference("", null),
                new URI("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:05.12f")).getEntry();

        assertEquals(1392,nce.length);
    }

    public void testGetAncestors() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");

        NCIChangeEvent[] nce = hs.getAncestors(ConvenienceMethods.createConceptReference("C15219", null)).getEntry();

        assertTrue(nce.length == 2);

        if (nce[0].getConceptcode().equals("C15219")) {
            assertTrue(nce[1].getConceptcode().equals("C16000"));
        } else {
            assertTrue(nce[0].getConceptcode().equals("C16000"));
        }

    }

    public void testGetDescandents() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();

        HistoryService hs = lbsi.getHistoryService("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");

        NCIChangeEvent[] nce = hs.getDescendants(ConvenienceMethods.createConceptReference("C16000", null)).getEntry();

        assertTrue(nce.length == 1);

        assertTrue(nce[0].getReferencecode().equals("C15219"));
    }
}
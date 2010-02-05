/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.History;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Collections.SystemReleaseList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SystemReleaseDetail;
import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.LexBIG.DataModel.NCIHistory.types.ChangeType;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
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
public class NCIThesaurusHistoryServiceTest extends TestCase {
    public void testGetBaselines() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");

        SystemReleaseList srl = hs.getBaselines(null, null);
        assertTrue(srl.getSystemReleaseCount() == 29);
        assertTrue(srl.getSystemRelease(0).getReleaseId().equals("v1.0"));
        assertTrue(srl.getSystemRelease(28).getReleaseId().equals("06.01c"));

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
        assertTrue(sr.getReleaseDate().getTime() == Long.parseLong("1029387600000"));
        assertTrue(sr.getEntityDescription().getContent().equals(
                "NCI Thesaurus with editing completed through July 22, 2002"));

    }

    public void testGetLatestBaseline() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");

        SystemRelease sr = hs.getLatestBaseline();
        assertTrue(sr.getReleaseId().equals("06.01c"));
    }

    public void testGetSystemRelease() throws LBException, URISyntaxException {
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
        assertTrue(ev[0].getVersionDate().getTime() == Long.parseLong("1139205600000"));
        assertTrue(ev[0].getVersion().equals("16-DEC-05"));
    }

    public void testGetConceptCreationVersion() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");

        CodingSchemeVersion csv = hs.getConceptCreationVersion(Constructors.createConceptReference("C49239", null));

        assertTrue(csv.getReleaseURN().equals("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:05.12f"));
        assertTrue(csv.getVersion().equals("03-JAN-06"));
        assertFalse(csv.getIsComplete());
        assertTrue(csv.getVersionDate().getTime() == Long.parseLong("1139205600000"));
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
        assertTrue(csv.getVersionDate().getTime() == Long.parseLong("1139205600000"));
        assertTrue(csv
                .getEntityDescription()
                .getContent()
                .equals(
                        "Editing of NCI Thesaurus 05.12f was completed on January 3, 2006.  Version 05.12f was December's sixth build in our development cycle."));

        try {
            csv = hs.getConceptCreationVersion(Constructors.createConceptReference("C49239", "urn:oid:25.5.5."));
            fail("Did not throw exception it should have");
        } catch (LBParameterException e) {
            // expected path
        }

    }

    public void testGetConceptChangeVersions() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");

        CodingSchemeVersion[] csv = hs.getConceptChangeVersions(Constructors.createConceptReference("C51826", null),
                null, null).getEntry();

        assertTrue(csv.length == 2);
        assertTrue(csv[0].getReleaseURN().equals("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:05.12f"));
        assertTrue(csv[0].getVersion().equals("22-DEC-05") || csv[1].getVersion().equals("22-DEC-05"));
        assertFalse(csv[0].getIsComplete());
        assertTrue(csv[0].getVersionDate().getTime() == Long.parseLong("1139205600000"));
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

        assertTrue(nce.length == 1);
        assertTrue(nce[0].getConceptcode().equals("C51826"));
        assertTrue(nce[0].getConceptName().equals("Grant_Principal_Investigator"));
        assertTrue(nce[0].getEditDate().getTime() == Long.parseLong("1135231200000"));
        assertTrue(nce[0].getReferencecode() == null || nce[0].getReferencecode().equals(""));
        assertTrue(nce[0].getReferencename().equals("Grant_PI"));
        assertTrue(nce[0].getEditaction().getType() == ChangeType.MODIFY_TYPE);

        csv = new CodingSchemeVersion();
        csv.setVersion("03-JAN-06");
        nce = hs.getEditActionList(Constructors.createConceptReference("C16205", ""), csv).getEntry();

        assertTrue(nce.length == 2);
        int i = 0;
        if (nce[0].getConceptcode().equals("C15363")) {
            i = 0;
        } else {
            i = 1;
        }
        assertTrue(nce[i].getConceptcode().equals("C15363"));
        assertTrue(nce[i].getConceptName().equals("Healthcare"));
        assertTrue(nce[i].getEditDate().getTime() == Long.parseLong("1136268000000"));
        assertTrue(nce[i].getReferencecode().equals("C16205"));
        assertTrue(nce[i].getReferencename().equals("Healthcare_Activity"));
        assertTrue(nce[i].getEditaction().getType() == ChangeType.MERGE_TYPE);
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
        assertTrue(nce[0].getEditDate().getTime() == Long.parseLong("1135231200000"));
        assertTrue(nce[0].getReferencecode() == null || nce[0].getReferencecode().length() == 0);
        assertTrue(nce[0].getReferencename().equals("Grant_PI"));
        assertTrue(nce[0].getEditaction().getType() == ChangeType.MODIFY_TYPE);

        nce = hs.getEditActionList(Constructors.createConceptReference("C51826", ""), before, null).getEntry();
        assertTrue(nce.length == 1);

        assertTrue(nce[0].getConceptcode().equals("C51826"));
        assertTrue(nce[0].getConceptName().equals("Grant_Principal_Investigator"));
        assertTrue(nce[0].getEditDate().getTime() == Long.parseLong("1136268000000"));
        assertTrue(nce[0].getReferencecode() == null || nce[0].getReferencecode().length() == 0);
        assertTrue(nce[0].getReferencename() == null || nce[0].getReferencename().length() == 0);
        assertTrue(nce[0].getEditaction().getType() == ChangeType.CREATE_TYPE);

        nce = hs.getEditActionList(Constructors.createConceptReference("C51826", ""), before, after).getEntry();
        assertTrue(nce.length == 1);
        assertTrue(nce[0].getConceptcode().equals("C51826"));
        assertTrue(nce[0].getConceptName().equals("Grant_Principal_Investigator"));
        assertTrue(nce[0].getEditDate().getTime() == Long.parseLong("1136268000000"));
        assertTrue(nce[0].getReferencecode() == null || nce[0].getReferencecode().length() == 0);
        assertTrue(nce[0].getReferencename() == null || nce[0].getReferencename().length() == 0);
        assertTrue(nce[0].getEditaction().getType() == ChangeType.CREATE_TYPE);
    }

    public void testGetEditActionList3() throws LBException, URISyntaxException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        HistoryService hs = lbsi.getHistoryService("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");

        NCIChangeEvent[] nce = hs.getEditActionList(null,
                new URI("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:05.11f")).getEntry();

        assertTrue(nce.length == 1);
        assertTrue(nce[0].getConceptcode().equals("C640"));
        assertTrue(nce[0].getConceptName().equals("Methaqualone"));
        assertTrue(nce[0].getEditDate().getTime() == Long.parseLong("1133935200000"));
        assertTrue(nce[0].getReferencecode() == null || nce[0].getReferencecode().equals(""));
        assertTrue(nce[0].getReferencename().equals("Current Dental Terminology 2005"));
        assertTrue(nce[0].getEditaction().getType() == ChangeType.MODIFY_TYPE);

        nce = hs.getEditActionList(Constructors.createConceptReference("C640", null),
                new URI("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:05.11f")).getEntry();

        assertTrue(nce.length == 1);
        assertTrue(nce[0].getConceptcode().equals("C640"));
        assertTrue(nce[0].getConceptName().equals("Methaqualone"));
        assertTrue(nce[0].getEditDate().getTime() == Long.parseLong("1133935200000"));
        assertTrue(nce[0].getReferencecode() == null || nce[0].getReferencecode().equals(""));
        assertTrue(nce[0].getReferencename().equals("Current Dental Terminology 2005"));
        assertTrue(nce[0].getEditaction().getType() == ChangeType.MODIFY_TYPE);

        nce = hs.getEditActionList(Constructors.createConceptReference("", null),
                new URI("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:05.12f")).getEntry();

        assertTrue(nce.length == 1392);
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
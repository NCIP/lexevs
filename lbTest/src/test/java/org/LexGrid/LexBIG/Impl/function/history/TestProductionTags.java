
package org.LexGrid.LexBIG.Impl.function.history;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.function.TestUtil;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.LexBIG.Utility.LBConstants.KnownTags;
import org.LexGrid.LexBIG.Utility.OrderingTestRunner;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.core.annotation.Order;

import java.io.File;
import java.util.Arrays;

@RunWith(OrderingTestRunner.class)
public class TestProductionTags extends LexBIGServiceTestCase {
    final static String testID = "T1_HIS_01";

    @Override
    protected String getTestID() {
        return testID;
    }

    String updatedVersion = "1.1";

    @Test
    @Order(2)
    public void testProductionTags01() throws InterruptedException, LBException {
        // info("01 Load/Activate version 1.1 of Automobiles vocabulary (see
        // TestUtil.loadLgXML() as
        // reference)");

        // silence some extraneous warnings from a logger here:
        Logger temp = LogManager.getLogger("org.LexGrid.emf.base.xml.LgXMLHandlerImpl");
        temp.atLevel(Level.ERROR);

        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm.getLoader("LexGrid_Loader");

        loader.load(new File("resources/testData/Automobiles2.xml").toURI(), true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }

        if (TestUtil.verifyScheme(AUTO_SCHEME, AUTO_URN, updatedVersion, CodingSchemeVersionStatus.INACTIVE)) {
            assertTrue(TestUtil.activateScheme(AUTO_URN, updatedVersion));
        }

        assertTrue(TestUtil.verifyScheme(AUTO_SCHEME, AUTO_URN, updatedVersion, CodingSchemeVersionStatus.ACTIVE));
    }

    /**
     * 02 Assign 'PRODUCTION' tag to 1.0 version; verify tag assignment
     * 
     * @throws LBException
     * @throws LBInvocationException
     * @throws LBParameterException
     * 
     */
    @Test
    @Order(2)
    public void testProductionTags02() throws LBParameterException, LBInvocationException, LBException {
        // info("02 Assign 'PRODUCTION' tag to 1.0 version; verify tag assignment ");
        CodingSchemeVersionOrTag tag = new CodingSchemeVersionOrTag();
        tag.setTag(LBConstants.KnownTags.PRODUCTION.toString());
        assertTrue(tag.getTag().equals(LBConstants.KnownTags.PRODUCTION.toString()));

        AbsoluteCodingSchemeVersionReference ref;
        ref = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(AUTO_URN, AUTO_VERSION);
        assertNotNull(ref);
        ServiceHolder.instance().getLexBIGService().getServiceManager(null).setVersionTag(ref, tag.getTag().toString());

        // validate change
        // (this is using a non-public API call...)
        String foundVersion = LexEvsServiceLocator.getInstance().getRegistry().getCodingSchemeEntry(ref).getResourceVersion();
        assertTrue(foundVersion.equals("1.0"));

    }

    /**
     * 03 Perform concept lookup by coding scheme name/tag; verify that
     * 'Chrysler' is not included
     * 
     * @throws LBException
     * 
     */
    @Test
    @Order(3)
    public void testProductionTags03() throws LBException {
        // info("03 Perform concept lookup by coding scheme name/tag; verify
        // that 'Chrysler' is not included
        // ");
        CodedNodeSet cns;

        if (TestUtil.verifyScheme(AUTO_SCHEME, AUTO_URN, AUTO_VERSION, CodingSchemeVersionStatus.INACTIVE))
            assertTrue(TestUtil.activateScheme(AUTO_URN, AUTO_VERSION));
        CodingSchemeVersionOrTag production = new CodingSchemeVersionOrTag();
        production.setTag(LBConstants.KnownTags.PRODUCTION.toString());
        cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, production);
        cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "Chrysler" }, AUTO_SCHEME));
        assertTrue(cns.resolveToList(null, null, null, 0).getResolvedConceptReference().length == 0);

    }

    /**
     * 04 Perform concept lookup by coding scheme name only; verify that
     * 'Chrysler' is not included
     * 
     * @throws LBException
     * 
     */
    @Test
    @Order(4)
    public void testProductionTags04() throws LBException {
        // info("04 Perform concept lookup by coding scheme name only; verify
        // that 'Chrysler' is not included
        // ");
        // not providing a version number gets you the PRODUCTION (which can be
        // assumed to be the latest good version)

        CodedNodeSet cns;

        cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "Chrysler" }, AUTO_SCHEME));
        assertTrue(cns.resolveToList(null, null, null, 0).getResolvedConceptReference().length == 0);

    }

    /**
     * 05 Perform relation lookup by coding scheme name/tag; verify that
     * 'Domestic Auto Makers' has subtype 'Ford' but not 'Chrysler'
     * 
     * @throws LBException
     * 
     */
    @Test
    @Order(5)
    public void testProductionTags05() throws LBException {
        // info("05 Perform relation lookup by coding scheme name/tag; verify
        // that 'Domestic Auto Makers' has
        // subtype 'Ford' but not 'Chrysler'");

        CodingSchemeVersionOrTag production = new CodingSchemeVersionOrTag();
        production.setTag(LBConstants.KnownTags.PRODUCTION.toString());
        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(AUTO_SCHEME, production,
                "relations");

        // 005 is 'domestic auto makers'

        assertNotNull(cng);
        cng.restrictToAssociations(Constructors.createNameAndValueList(new String[] { "hasSubtype" }), null);

        ConceptReference cref = Constructors.createConceptReference("005", AUTO_SCHEME);
        /*
         * resolveAsList(ConceptReference graphFocus, boolean resolveForward,
         * boolean resolveBackward, int resolveCodedEntryDepth, int
         * resolveAssociationDepth, LocalNameList restrictToProperties, String
         * sortByProperty, int maxToReturn) throws LBInvocationException,
         * LBParameterExceptionnew
         */
        ResolvedConceptReference[] rcr = cng.resolveAsList(cref, true, false, 1, 1, new LocalNameList(), null, null, 0)
                .getResolvedConceptReference();

        assertNotNull(Arrays.asList(rcr));

        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getEntity().getEntityCode().equals("005"));
        AssociatedConcept[] ac = rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts()
                .getAssociatedConcept();
        assertTrue(ac.length == 3);
        assertTrue(contains(ac, "Ford"));
        assertTrue(doesNotContain(ac, "Chrysler"));

    }

    private boolean contains(AssociatedConcept[] ac, String code) {
        boolean result = false;
        for (int i = 0; i < ac.length; i++) {
            if (ac[i].getConceptCode().equals(code)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean doesNotContain(AssociatedConcept[] ac, String code) {
        boolean result = true;
        for (int i = 0; i < ac.length; i++) {
            if (ac[i].getConceptCode().equals(code)) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * 06 Perform relation lookup by coding scheme name only; verify that
     * 'Domestic Auto Makers' has subtype 'Ford' but not 'Chrysler'
     * 
     * @throws LBException
     * 
     */
    @Test
    @Order(6)
    public void testProductionTags06() throws LBException {
        // info("06 Perform relation lookup by coding scheme name only; verify
        // that 'Domestic Auto Makers' has
        // subtype 'Ford' but not 'Chrysler'");

        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(AUTO_SCHEME, null, "relations");
        // 005 is 'domestic auto makers'

        assertNotNull(cng);
        cng.restrictToAssociations(Constructors.createNameAndValueList(new String[] { "hasSubtype" }), null);

        ConceptReference cref = Constructors.createConceptReference("005", AUTO_SCHEME);
        /*
         * resolveAsList(ConceptReference graphFocus, boolean resolveForward,
         * boolean resolveBackward, int resolveCodedEntryDepth, int
         * resolveAssociationDepth, LocalNameList restrictToProperties, String
         * sortByProperty, int maxToReturn) throws LBInvocationException,
         * LBParameterExceptionnew
         */
        ResolvedConceptReference[] rcr = cng.resolveAsList(cref, true, false, 1, 1, new LocalNameList(), null, null, 0)
                .getResolvedConceptReference();

        assertNotNull(Arrays.asList(rcr));

        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getEntity().getEntityCode().equals("005"));
        AssociatedConcept[] ac = rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts()
                .getAssociatedConcept();
        assertTrue(ac.length == 3);
        assertTrue(contains(ac, "Ford"));
        assertTrue(doesNotContain(ac, "Chrysler"));

    }

    /**
     * 07 Assign 'PRODUCTION' tag to 1.1 version; verify tag assignment
     * 
     * @throws LBException
     * @throws LBInvocationException
     * @throws LBParameterException
     * 
     */
    @Test
    @Order(7)
    public void testProductionTags07() throws LBParameterException, LBInvocationException, LBException {
    	 AbsoluteCodingSchemeVersionReference ref1;
         ref1 = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(AUTO_URN, AUTO_VERSION);
     
        AbsoluteCodingSchemeVersionReference ref2;
        ref2 = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(AUTO_URN, updatedVersion);
        
        ServiceHolder.instance().getLexBIGService().getServiceManager(null).setVersionTag(ref1, "");
        ServiceHolder.instance().getLexBIGService().getServiceManager(null).setVersionTag(ref2, KnownTags.PRODUCTION.toString());
        
        // validate change - this is using a non api method
        assertTrue(StringUtils.isBlank(
        		LexEvsServiceLocator.getInstance().getRegistry().getCodingSchemeEntry(ref1).getTag()));
        assertEquals(KnownTags.PRODUCTION.toString(), LexEvsServiceLocator.getInstance().getRegistry().getCodingSchemeEntry(ref2).getTag());

    }

    /**
     * 08 Perform concept lookup by coding scheme name/tag; verify that
     * 'Chrysler' is included
     * 
     * @throws LBException
     * @throws LBInvocationException
     * 
     */
    @Test
    @Order(8)
    public void testProductionTags08() throws LBInvocationException, LBException {
        // info("08 Perform concept lookup by coding scheme name/tag; verify that 'Chrysler' is included ");
        CodedNodeSet cns;

        if (TestUtil.verifyScheme(AUTO_SCHEME, AUTO_URN, updatedVersion, CodingSchemeVersionStatus.INACTIVE))
            assertTrue(TestUtil.activateScheme(AUTO_URN, updatedVersion));
        CodingSchemeVersionOrTag production = new CodingSchemeVersionOrTag();
        production.setTag(LBConstants.KnownTags.PRODUCTION.toString());
        cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, production);
        cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "Chrysler" }, AUTO_SCHEME));
        assertEquals(1, cns.resolveToList(null, null, null, 0).getResolvedConceptReference().length);

    }

    /**
     * 09 Perform concept lookup by coding scheme name only; verify that
     * 'Chrysler' is included
     * 
     * @throws LBException
     * 
     */
    @Test
    @Order(9)
    public void testProductionTags09() throws LBException {
        // info("09 Perform concept lookup by coding scheme name only; verify that 'Chrysler' is included ");
        // not providing a version number gets you the PRODUCTION (which can be
        // assumed to be the latest good version)

        CodedNodeSet cns;

        cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
        cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "Chrysler" }, AUTO_SCHEME));
        assertTrue(cns.resolveToList(null, null, null, 0).getResolvedConceptReference().length == 1);

    }

    /**
     * 10 Perform relation lookup by coding scheme name/tag; verify that
     * 'Domestic Auto Makers' has subtype 'Ford' and 'Chrysler'
     * 
     * @throws LBException
     * 
     */
    @Test
    @Order(10)
    public void testProductionTags10() throws LBException {
        // info("10 Perform relation lookup by coding scheme name/tag; verify
        // that 'Domestic Auto Makers' has
        // subtype 'Ford' and 'Chrysler'");
        CodingSchemeVersionOrTag production = new CodingSchemeVersionOrTag();
        production.setTag(LBConstants.KnownTags.PRODUCTION.toString());
        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(AUTO_SCHEME, production,
               null);

        // 005 is 'domestic auto makers'

        assertNotNull(cng);
        cng.restrictToAssociations(Constructors.createNameAndValueList(new String[] { "hasSubtype" }), null);

        ConceptReference cref = Constructors.createConceptReference("005", AUTO_SCHEME);
        /*
         * resolveAsList(ConceptReference graphFocus, boolean resolveForward,
         * boolean resolveBackward, int resolveCodedEntryDepth, int
         * resolveAssociationDepth, LocalNameList restrictToProperties, String
         * sortByProperty, int maxToReturn) throws LBInvocationException,
         * LBParameterExceptionnew
         */
        ResolvedConceptReference[] rcr = cng.resolveAsList(cref, true, false, 1, 1, new LocalNameList(), null, null, 0)
                .getResolvedConceptReference();

        assertNotNull(Arrays.asList(rcr));

        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getEntity().getEntityCode().equals("005"));
        AssociatedConcept[] ac = rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts()
                .getAssociatedConcept();
        assertTrue(ac.length == 3);
        assertTrue(contains(ac, "Ford"));
        assertTrue(contains(ac, "Chrysler"));

    }

    /**
     * 11 Perform relation lookup by coding scheme name only; verify that
     * 'Domestic Auto Makers' has subtype 'Ford' and 'Chrysler'
     * 
     * @throws LBException
     * 
     */
    @Test
    @Order(11)
    public void testProductionTags11() throws LBException {
        // info("11 Perform relation lookup by coding scheme name only; verify
        // that 'Domestic Auto Makers' has
        // subtype 'Ford' and 'Chrysler'");

        CodedNodeGraph cng = ServiceHolder.instance().getLexBIGService().getNodeGraph(AUTO_SCHEME, null, null);

        // 005 is 'domestic auto makers'

        assertNotNull(cng);
        cng.restrictToAssociations(Constructors.createNameAndValueList(new String[] { "hasSubtype" }), null);

        ConceptReference cref = Constructors.createConceptReference("005", AUTO_SCHEME);
        /*
         * resolveAsList(ConceptReference graphFocus, boolean resolveForward,
         * boolean resolveBackward, int resolveCodedEntryDepth, int
         * resolveAssociationDepth, LocalNameList restrictToProperties, String
         * sortByProperty, int maxToReturn) throws LBInvocationException,
         * LBParameterExceptionnew
         */
        ResolvedConceptReference[] rcr = cng.resolveAsList(cref, true, false, 1, 1, new LocalNameList(), null, null, 0)
                .getResolvedConceptReference();

        assertNotNull(Arrays.asList(rcr));
        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getEntity().getEntityCode().equals("005"));
        AssociatedConcept[] ac = rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts()
                .getAssociatedConcept();
        assertTrue(ac.length == 3);
        assertTrue(contains(ac, "Ford"));
        assertTrue(contains(ac, "Chrysler"));

    }

    /**
     * 12 Clear 'PRODUCTION' tag on 1.1 version by setting to empty string;
     * verify tag is cleared
     * 
     * @throws LBException
     * @throws LBInvocationException
     * @throws LBParameterException
     * 
     */
    @Test
    @Order(12)
    public void testProductionTags12() throws LBParameterException, LBInvocationException, LBException {
        // info("12 Clear 'PRODUCTION' tag on 1.1 version by setting to empty
        // string; verify tag is cleared
        // ");
        CodingSchemeVersionOrTag tag = new CodingSchemeVersionOrTag();
        tag.setTag("");
        assertTrue(tag.getTag().equals(""));
        // write this to registry

        AbsoluteCodingSchemeVersionReference ref;
        ref = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(AUTO_URN, updatedVersion);
        assertNotNull(ref);
        ServiceHolder.instance().getLexBIGService().getServiceManager(null).setVersionTag(ref, tag.getTag().toString());
        // validate change
        assertTrue(
        		StringUtils.isBlank(
        				LexEvsServiceLocator.getInstance().getRegistry().getCodingSchemeEntry(ref).getTag()));

    }

    /**
     * 13 Attempt to perform concept lookup without specifying a version; verify
     * exception
     * 
     */
    @Test
    @Order(13)
    public void testProductionTags13() {
        // info("13 Attempt to perform concept lookup without specifying a version; verify exception ");
        CodedNodeSet cns;
        try {
            cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME, null);
            cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "Chrysler" }, AUTO_SCHEME));
            cns.resolveToList(null, null, null, 0);
            fail("Did not throw an exception when it should have");
        } catch (LBException e) {
            // correct path
        }
    }

    /**
     * 14 Assign 'TEST' tag to 1.0 version; verify tag assignment
     * 
     * @throws LBException
     * @throws LBInvocationException
     * @throws LBParameterException
     * 
     */
    @Test
    @Order(14)
    public void testProductionTags14() throws LBParameterException, LBInvocationException, LBException {
        // info("14 Assign 'TEST' tag to 1.0 version; verify tag assignment ");
        CodingSchemeVersionOrTag tag = new CodingSchemeVersionOrTag();
        tag.setTag("TEST");
        // write this to registry

        AbsoluteCodingSchemeVersionReference ref;
        ref = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(AUTO_URN, AUTO_VERSION);
        assertNotNull(ref);
        ServiceHolder.instance().getLexBIGService().getServiceManager(null).setVersionTag(ref, tag.getTag().toString());
        // validate change
        assertEquals(LexEvsServiceLocator.getInstance().getRegistry().getCodingSchemeEntry(ref).getTag(), tag.getTag());

    }

    @Test
    @Order(15)
    public void testProductionTags15() throws LBException {
        // lets do a search on the 'TEST' coding scheme, make sure it doesn't
        // have chrysler
        CodedNodeSet cns;

        cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(AUTO_SCHEME,
                Constructors.createCodingSchemeVersionOrTagFromTag("TEST"));
        cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "Chrysler" }, AUTO_SCHEME));
        assertTrue(cns.resolveToList(null, null, null, 0).getResolvedConceptReference().length == 0);
        
        //set 1.0 back to PRODUCTION for any following tests.
        ServiceHolder.instance().getLexBIGService().getServiceManager(null).setVersionTag(
        		ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(AUTO_URN, AUTO_VERSION), LBConstants.KnownTags.PRODUCTION.toString());

    }

    /**
     * 15 Deactivate/Remove version 1.1 (see deactivate/remove methods on
     * TestUtil as reference)
     * 
     * @throws LBException
     * @throws LBInvocationException
     * 
     */
    @Test
    @Order(16)
    public void testProductionTags16() throws LBInvocationException, LBException {
        // info("15 Deactivate/Remove version 1.1 (see deactivate/remove methods on TestUtil as reference)");
        if (TestUtil.verifyScheme(AUTO_SCHEME, AUTO_URN, updatedVersion, CodingSchemeVersionStatus.ACTIVE))
            assertTrue(TestUtil.deactivateScheme(AUTO_URN, updatedVersion));
        assertTrue(TestUtil.removeScheme(AUTO_URN, updatedVersion));

        // restore the logger level
        Logger temp = LogManager.getLogger("org.LexGrid.emf.base.xml.LgXMLHandlerImpl");
        temp.atLevel(Level.DEBUG);
    }

}
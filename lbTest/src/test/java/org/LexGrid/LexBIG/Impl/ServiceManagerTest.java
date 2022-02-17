
package org.LexGrid.LexBIG.Impl;

import java.io.File;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.LBConstants;

/**
 * JUnit Tests for the Service Manager API
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class ServiceManagerTest extends TestCase {

    public void testAddRemoveCycle() throws Exception {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();

        LexBIGServiceManager lbsm = lbsi.getServiceManager(null);

        // The start up process has already loaded German made parts and
        // automobiles.

        // Will drop one, and reload it.

        AbsoluteCodingSchemeVersionReference acsvr = Constructors.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:11.11.0.2", "2.0");
        try {
            lbsm.removeCodingSchemeVersion(acsvr);
            fail("Should not have let me remove an active coding scheme");
        } catch (LBException e) {
            // desired path...
        }

        // should work
        lbsi.getCodingSchemeConcepts("GermanMadeParts", null);

        lbsm.deactivateCodingSchemeVersion(acsvr, null);

        // should not be able to load the same scheme twice:
        LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm.getLoader("LexGrid_Loader");

        loader.load(new File("resources/testData/German_Made_Parts.xml").toURI(), true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }
        if (!loader.getStatus().getState().equals(ProcessState.FAILED)) {

            fail("Should not be able to load the same coding system twice");
        }

        try {
            lbsi.getCodingSchemeConcepts("GermanMadeParts", null);
            fail("Should not be able to use an inactive code system");
        } catch (LBResourceUnavailableException e) {
            // desired path
        }

        lbsm.removeCodingSchemeVersion(acsvr);

        // ok, delete worked... now load it back up.

        loader.load(new File("resources/testData/German_Made_Parts.xml").toURI(), true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }

        try {
            lbsi.getCodingSchemeConcepts("GermanMadeParts", null);
            fail("Shouldn't be able to use an inactive code system (loaded inactive by default)");
        } catch (LBResourceUnavailableException e) {
            // expected path
        }

        lbsm.activateCodingSchemeVersion(ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:11.11.0.2", "2.0"));

        lbsm.setVersionTag(ConvenienceMethods.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.2", "2.0"),
                LBConstants.KnownTags.PRODUCTION.toString());

        lbsi.getCodingSchemeConcepts("GermanMadeParts", null);
    }
}
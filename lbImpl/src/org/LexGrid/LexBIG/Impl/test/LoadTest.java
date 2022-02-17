
package org.LexGrid.LexBIG.Impl.test;

import java.io.File;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Extensions.Load.OBO_Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.ObjectToString;

/**
 * Class for testing the load framework.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LoadTest {

    public static void main(String[] args) throws Exception {
        LexBIGService lb = LexBIGServiceImpl.defaultInstance();
        LexBIGServiceManager lbsm = lb.getServiceManager(null);

        // LexGrid_Loader loader = (LexGrid_Loader)
        // lbsm.getLoader("LexGridLoader");
        // OWL_Loader loader = (OWL_Loader) lbsm.getLoader("OWLLoader");
        // loader.load(new
        // File("K:\\NCI Thesaurus\\Thesaurus_06.01c.owl").toURI(), null, true);

        // loader.load(new
        // File("C:\\Documents and Settings\\ARMBRUST\\Desktop\\Gemet.xml")
        // .toURI(), true);
        // loader.load(new
        // File("C:\\Eclipse Projects\\general-workspace\\lbImpl\\resources\\testData\\Automobiles.xml")
        // .toURI(), true);
        // loader.load( new File("O:\\NCI_Thesaurus.xml").toURI(), true);

        // test loading many obo files

        File temp = new File("C:\\temp\\obo files");
        File[] oboFiles = temp.listFiles();
        for (int i = 0; i < oboFiles.length; i++) {
            if (oboFiles[i].isDirectory()) {
                continue;
            }
            System.out.println("loading " + oboFiles[i].getName());
            OBO_Loader loader = (OBO_Loader) lbsm.getLoader("OBOLoader");

            loader.load(oboFiles[i].toURI(), null, false, true);

            while (loader.getStatus().getEndTime() == null) {
                Thread.sleep(5000);
            }

            System.out.println(ObjectToString.toString(loader.getLog(null)));
            System.out.println(loader.getStatus().getMessage());

        }

        // activate all of the code systems that are inactive.
        //        
        CodingSchemeRendering[] csr = lb.getSupportedCodingSchemes().getCodingSchemeRendering();
        for (int i = 0; i < csr.length; i++) {
            lbsm.activateCodingSchemeVersion(ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(csr[i]
                    .getCodingSchemeSummary().getCodingSchemeURI(), csr[i].getCodingSchemeSummary()
                    .getRepresentsVersion()));
        }

        //        
        // lbsm.activateCodingSchemeVersion(ConvenienceMethods
        // .createAbsoluteCodingSchemeVersionReference("urn:oid:2.16.840.1.113883.3.26.1.1",
        // "06.01c"));

        //
        // lbsm.activateCodingSchemeVersion(ConvenienceMethods
        // .createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.2",
        // "2.0"));
        //
        // lbsm.setVersionTag(ConvenienceMethods.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.2",
        // "2.0"),
        // LBConstants.KnownTags.PRODUCTION.toString());
        //
        // System.out.println(ObjectToString.toString(lb.getCodingSchemeConcepts("German Made Parts",
        // ConvenienceMethods.createProductionTag(), false).resolveToList(null,
        // null, 500)));

        // test reindex

        // lbsm
        // .rebuildIndexBase(ConvenienceMethods.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.2",
        // "2.0"));
        // lbsm.rebuildIndexBase(null);

        // String oldmsg = "";
        // String msg =
        // ((LexBIGServiceManagerImpl)lbsm).getIndexStatus().getMessage();
        // while (((LexBIGServiceManagerImpl)lbsm).getIndexStatus().getEndTime()
        // == null)
        // {
        // if (msg != null && !msg.equals(oldmsg))
        // {
        // System.out.println(msg);
        // oldmsg = msg;
        // }
        // Thread.sleep(5000);
        // msg = ((LexBIGServiceManagerImpl)lbsm).getIndexStatus().getMessage();
        // }
        // 
        // System.out.println(ObjectToString.toString(((LexBIGServiceManagerImpl)lbsm).getIndexLog()));
        // System.out.println(ObjectToString.toString(lb.getCodingSchemeConcepts("German Made Parts",
        // ConvenienceMethods.createProductionTag(), false).resolveToList(null,
        // null, 500)));

        // test add / remove

        // lbsm.deactivateCodingSchemeVersion(ConvenienceMethods.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.2",
        // "2.0"), null);
        // lbsm.removeCodingSchemeVersion(ConvenienceMethods.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.2",
        // "2.0"));
        //        
        // loader.load(new
        // File("resources\\testData\\German Made Parts.xml").toURI(), true);
        // String oldmsg = "";
        // String msg = loader.getStatus().getMessage();
        // while (loader.getStatus().getEndTime() == null)
        // {
        // if (msg != null && !msg.equals(oldmsg))
        // {
        // System.out.println(msg);
        // oldmsg = msg;
        // }
        // Thread.sleep(5000);
        // msg = loader.getStatus().getMessage();
        // }
        //
        // System.out.println(ObjectToString.toString(loader.getLog()));
        //
        // loader.load(new File(
        // "D:\\Eclipse
        // Projects\\general-workspace\\lbImpl\\resources\\testData\\German Made
        // Parts.xml").toURI(),
        // true);
        // while (loader.getStatus().getEndTime() == null)
        // {
        // Thread.sleep(500);
        // }
        //
        // lbsm.activateCodingSchemeVersion(ConvenienceMethods
        // .createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.2",
        // "2.0"));
        //
        // lbsm.setVersionTag(ConvenienceMethods.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.2",
        // "2.0"),
        // "PRODUCTION");
        //
        // System.out.println(ObjectToString.toString(lb.getCodingSchemeConcepts("German Made Parts",
        // ConvenienceMethods.createProductionTag(),
        // false).resolveToList(null, null, 500)));
        // lbsm.deactivateCodingSchemeVersion(ConvenienceMethods
        // .createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.2",
        // "2.0"), null);
        //
        // lbsm.removeCodingSchemeVersion(ConvenienceMethods
        // .createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.2",
        // "2.0"));
        //
        // System.out.println(ObjectToString.toString(lb.getCodingSchemeConcepts("German Made Parts",
        // ConvenienceMethods.createProductionTag(),
        // false).resolveToList(null, null, 500)));

        // Test text file loader

        // Text_Loader tLoader = (Text_Loader) lbsm.getLoader("TextLoader");
        // tLoader.load(new File(
        // "D:\\Eclipse
        // Projects\\general-workspace\\LexGridConverter\\commentedSamples\\textLoad_A.txt").toURI(),
        // null,
        // false, true);
        // while (tLoader.getStatus().getEndTime() == null)
        // {
        // Thread.sleep(500);
        // }

        // test the history loader
        // NCIHistoryLoader loader = (NCIHistoryLoader)
        // lbsm.getLoader("NCIThesaurusHistoryLoader");
        //
        // loader.load(new
        // File("C:\\Documents and Settings\\ARMBRUST\\Desktop\\history\\full_pipe_out_12f.txt").toURI(),
        // new
        // File("C:\\Eclipse Projects\\general-workspace\\lbImpl\\resources\\NCISystemReleaseHistory.txt").toURI(),
        // true, true);
        //
        // while (loader.getStatus().getEndTime() == null)
        // {
        // Thread.sleep(500);
        // }
        //
        // System.out.println(ObjectToString.toString(loader.getLog()));

        // Test the NCI Metathesaurus loader
        // NCI_MetaThesaurusLoader loader = (NCI_MetaThesaurusLoader)
        // lbsm.getLoader("NCIMetaThesaurusLoader");
        //
        // loader.load(new
        // File("K:\\NCIMetathesaurus\\Metathesaurus_200510E.RRF").toURI(),
        // true);
        //       
        // String oldmsg = "";
        // String msg = loader.getStatus().getMessage();
        // while (loader.getStatus().getEndTime() == null)
        // {
        // if (msg != null && !msg.equals(oldmsg))
        // {
        // System.out.println(msg);
        // oldmsg = msg;
        // }
        // Thread.sleep(20000);
        // msg = loader.getStatus().getMessage();
        // }
        //
        // System.out.println(ObjectToString.toString(loader.getLog()));

    }
}
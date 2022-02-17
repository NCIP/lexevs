
package org.LexGrid.LexBIG.Impl.test;

import java.net.URI;

import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ObjectToString;

/**
 * Random test code used during the developement process.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class HistoryTests {
    public static void main(String[] args) throws Exception {
        try {

            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();

            String codingScheme = "NCI_Thesaurus";

            HistoryService hs = lbs.getHistoryService(codingScheme);

            // Test getBaselines
            // System.out.println(ObjectToString.toString(hs.getBaselines(null,
            // null)));
            //
            // Calendar cal = Calendar.getInstance();
            // cal.set(cal.YEAR, 2005);
            // cal.set(cal.MONTH, 10);
            // cal.set(cal.DAY_OF_MONTH, 25);
            //
            // Date after = cal.getTime();
            //
            // cal = Calendar.getInstance();
            // cal.set(cal.YEAR, 2005);
            // cal.set(cal.MONTH, 11);
            // cal.set(cal.DAY_OF_MONTH, 25);
            //
            // Date before = cal.getTime();
            //
            // System.out.println(ObjectToString.toString(hs.getBaselines(after,
            // before)));
            //
            // System.out.println(ObjectToString.toString(hs.getBaselines(after,
            // null)));
            //
            // System.out.println(ObjectToString.toString(hs.getBaselines(null,
            // before)));

            // earliest baseline
            // System.out.println(ObjectToString.toString(hs.getEarliestBaseline()));

            // latest baseline
            // System.out.println(ObjectToString.toString(hs.getLatestBaseline()));

            // test get system release detail

            // System.out.println(ObjectToString.toString(hs.getSystemRelease(new
            // URI("urn:oid:2.16.840.1.113883.3.26.1.1:03.12a"))));

            // test getEditActionList
            // CodingSchemeVersion temp = new CodingSchemeVersion();
            // temp.setVersion("29-JAN-04");
            // System.out.println(ObjectToString.toString(hs.getEditActionList(temp)));

            // test getConceptCreationVersion
            // System.out.println(ObjectToString.toString(hs.getConceptCreationVersion(Constructors.createConceptReference("C7696",
            // null))));

            // test getConceptChangeVersions
            // System.out.println(ObjectToString.toString(hs.getConceptChangeVersions(Constructors
            // .createConceptReference("C7696", null), null, null)));
            //
            // Calendar cal = Calendar.getInstance();
            // cal.set(Calendar.YEAR, 2004);
            // cal.set(Calendar.MONTH, 3);
            // cal.set(Calendar.DAY_OF_MONTH, 25);
            //
            // Date after = cal.getTime();
            //
            // cal = Calendar.getInstance();
            // cal.set(Calendar.YEAR, 2004);
            // cal.set(Calendar.MONTH, 7);
            // cal.set(Calendar.DAY_OF_MONTH, 25);
            //            
            // Date before = cal.getTime();
            //
            // System.out.println(ObjectToString.toString(hs.getConceptChangeVersions(Constructors
            // .createConceptReference("C7696", null), after, before)));
            //            
            // System.out.println(ObjectToString.toString(hs.getConceptChangeVersions(Constructors
            // .createConceptReference("C7696", null), null, before)));
            //            
            // System.out.println(ObjectToString.toString(hs.getConceptChangeVersions(Constructors
            // .createConceptReference("C7696", null), after, null)));
            //            
            // System.out.println(ObjectToString.toString(hs.getEditActionList(Constructors
            // .createConceptReference("C12799", null), null, null)));

            // test another getEditActionList

            System.out.println(ObjectToString.toString(hs.getEditActionList(Constructors.createConceptReference("",
                    null), new URI("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:04.03n"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
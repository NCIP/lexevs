
package org.LexGrid.LexBIG.example;

import java.util.Enumeration;
import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.MetadataProperty;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;

/**
 * Example how to query stored metadata for a code system. For the example, use
 * the LoadSampleMetaDataData.bat to load the required code system and metadata.
 */
public class MetaDataSearch {

    public MetaDataSearch() {
        super();
    }

    /**
     * Entry point for processing.
     * 
     * @param args
     */
    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Example: MetaDataSearch \"test string\"");
            return;
        }
        ;

        try {
            String s = args[0];
            System.out.println("string passed: " + s);
            new MetaDataSearch().run(s);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public void run(String s) throws LBException {

        try {
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();

            LexBIGServiceMetadata smd = lbs.getServiceMetadata();

            AbsoluteCodingSchemeVersionReferenceList al = smd.listCodingSchemes();

            int i = 0;
            AbsoluteCodingSchemeVersionReference ref = null;

            for (Enumeration<? extends AbsoluteCodingSchemeVersionReference> items = al
                    .enumerateAbsoluteCodingSchemeVersionReference(); items.hasMoreElements();) {
                ref = items.nextElement();
                String csurn = ref.getCodingSchemeURN();
                String csvers = ref.getCodingSchemeVersion();

                // Search for localName in metadata for specified coding scheme
                smd = lbs.getServiceMetadata();
                smd.restrictToCodingScheme(ref);

                // Restrict to properties. In this example, 'localName'
                smd.restrictToProperties(new String[] { s });
                printCodingSchemes(csurn, csvers, smd.resolve(), s);

                i++;
            }

            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printCodingSchemes(String urn, String version, MetadataPropertyList mdpl, String searchString) {

        int i = 0;
        System.out.println("Results for metadata search in coding scheme:  " + urn + " (" + version + ")");
        System.out.println();
        System.out.println("Search String: " + searchString);
        System.out.println();

        for (Enumeration<? extends MetadataProperty> items = mdpl.enumerateMetadataProperty(); items.hasMoreElements();) {
            MetadataProperty mdp = items.nextElement();
            i++;

            System.out.println("[" + i + "] " + mdp.getValue());

        }
        System.out.println();
        System.out.println("Count: " + i);

        System.out.println();
        System.out.println();

    }

}
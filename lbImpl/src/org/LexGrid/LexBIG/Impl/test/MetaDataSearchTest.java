
package org.LexGrid.LexBIG.Impl.test;

import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.MetadataProperty;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;

/**
 * adhoc test class for the metadata searcher.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class MetaDataSearchTest {
    public static void main(String[] args) throws Exception {
        try {
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();

            LexBIGServiceMetadata smd = lbs.getServiceMetadata();

            System.out.println("Indexed metadata:");
            AbsoluteCodingSchemeVersionReferenceList al = smd.listCodingSchemes();
            int i = 0;
            for (Enumeration<? extends AbsoluteCodingSchemeVersionReference> items = al
                    .enumerateAbsoluteCodingSchemeVersionReference(); items.hasMoreElements();) {
                AbsoluteCodingSchemeVersionReference ref = items.nextElement();
                System.out.println("Coding Scheme URN - " + ref.getCodingSchemeURN());
                System.out.println("Coding Scheme version - " + ref.getCodingSchemeVersion());
                i++;
            }

            System.out.println();

            lbs.getServiceManager(null).removeCodingSchemeVersionMetaData(
                    Constructors.createAbsoluteCodingSchemeVersionReference("111", "version 1"));

            smd.restrictToValue("f*", MatchAlgorithms.LuceneQuery.toString());
            smd.restrictToProperties(new String[] { "email" });

            print(smd.resolve());

            smd = lbs.getServiceMetadata();

            smd.restrictToProperties(new String[] { "contact" });

            print(smd.resolve());

            smd = lbs.getServiceMetadata();

            smd.restrictToProperties(new String[] { "contact" });
            smd.restrictToPropertyParents(new String[] { "authority", "ontology_description" });
            smd.restrictToCodingScheme(Constructors.createAbsoluteCodingSchemeVersionReference(
                    "urn:lsid:bioontology.org:cell", null));

            print(smd.resolve());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void print(MetadataPropertyList mdpl) {

        int i = 0;
        for (Enumeration<? extends MetadataProperty> items = mdpl.enumerateMetadataProperty(); items.hasMoreElements();) {
            MetadataProperty mdp = items.nextElement();
            System.out.println("Search result " + i++);
            System.out.println("Code system name: " + mdp.getCodingSchemeURI());
            System.out.println("Code system version: " + mdp.getCodingSchemeVersion());
            System.out.println("Matching property: " + mdp.getName());
            System.out.println("Matczing property value: " + mdp.getValue());
            System.out.print("Property Parents: ");
            for (Enumeration<? extends Object> parents = mdp.enumerateContext(); parents.hasMoreElements();) {
                System.out.print(parents.nextElement().toString());
                if (parents.hasMoreElements()) {
                    System.out.print(", ");
                }
            }

            System.out.println();
            System.out.println();
        }
    }
}

package org.LexGrid.LexBIG.Impl.test;

import java.util.Enumeration;
import java.util.Hashtable;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.MetadataProperty;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;

/**
 * Example test class to demonstrate searching the HL7 Metadata .
 * 
 * @author <A HREF="mailto:stancl.craig@mayo.edu">Craig Stancl</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class HL7MetaDataSearchTest {
    public static void main(String[] args) throws Exception {
        try {
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();

            LexBIGServiceMetadata smd = lbs.getServiceMetadata();

            // Print the Coding Scheme and Version

            System.out.println("Indexed HL7 metadata:");
            AbsoluteCodingSchemeVersionReferenceList al = smd.listCodingSchemes();
            int i = 0;
            for (Enumeration<? extends AbsoluteCodingSchemeVersionReference> items = al
                    .enumerateAbsoluteCodingSchemeVersionReference(); items.hasMoreElements();) {
                AbsoluteCodingSchemeVersionReference ref = items.nextElement();
                System.out.println("LexBIG Coding Scheme URN - " + ref.getCodingSchemeURN());
                System.out.println("LexBIG Coding Scheme version - " + ref.getCodingSchemeVersion());
                i++;
            }

            System.out.println();

            Hashtable localNamesHash = new Hashtable();
            Hashtable registeredNamesHash = new Hashtable();

            // Search for localName metadata
            smd = lbs.getServiceMetadata();
            smd.restrictToProperties(new String[] { "localName" });
            printCodingSchemes(smd.resolve());
            printCodingSchemes(smd.resolve(), localNamesHash);

            // Search for registeredName metadata
            smd = lbs.getServiceMetadata();
            smd.restrictToProperties(new String[] { "registeredName" });
            printCodingSchemes(smd.resolve(), registeredNamesHash);

            // Print Local Name and Registered name Metadata
            System.out.println("HL7 Code System Local Name and Registered Name:");
            for (int c = 1; c < (localNamesHash.size() + 1); c++) {
                System.out.println("[" + c + "] " + localNamesHash.get(c) + " (" + registeredNamesHash.get(c) + ")");
            }
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
            System.out.println("Matching property value: " + mdp.getValue());
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

    private static void printCodingSchemes(MetadataPropertyList mdpl, Hashtable table) {

        int i = 0;

        for (Enumeration<? extends MetadataProperty> items = mdpl.enumerateMetadataProperty(); items.hasMoreElements();) {
            MetadataProperty mdp = items.nextElement();
            i++;
            table.put(i, new String(mdp.getValue()));

        }

    }

    private static void printCodingSchemes(MetadataPropertyList mdpl) {

        int i = 0;
        System.out.println("Available HL7 Coding Schemes");
        for (Enumeration<? extends MetadataProperty> items = mdpl.enumerateMetadataProperty(); items.hasMoreElements();) {
            MetadataProperty mdp = items.nextElement();
            i++;

            System.out.println("[" + i + "] " + mdp.getValue());
        }

        System.out.println("Coding Scheme Count: " + i);

        System.out.println();
        System.out.println();

    }

}
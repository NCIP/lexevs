/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
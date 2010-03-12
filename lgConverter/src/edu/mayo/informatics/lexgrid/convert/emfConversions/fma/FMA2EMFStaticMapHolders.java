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
package edu.mayo.informatics.lexgrid.convert.emfConversions.fma;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedDataType;
import org.LexGrid.naming.SupportedLanguage;

import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;

/*
 * @author <A HREF="mailto:dks02@mayo.edu">Deepak Sharma</A> 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * 
 * @version subversion $Revision: 2917 $ checked in on $Date: 2006-06-19
 *          15:52:21 +0000 (Mon, 19 Jun 2006) $
 */

public class FMA2EMFStaticMapHolders {
    private static Vector properties = new Vector();
    private static Vector association = new Vector();

    private LgMessageDirectorIF messages_;

    private void init(KnowledgeBase kb) {
    }

    private void prepareSupportedLanguages(List suppLang, KnowledgeBase kb) {
        try {
            // Read allowed values of languages from
            // Language slot's allowed values
            boolean languagesFound = false;

            try {
                Slot lSlot = kb.getSlot("Language");

                if (lSlot != null) {
                    Collection avals = kb.getAllowedValues(lSlot);

                    if ((avals != null) && (!avals.isEmpty())) {
                        Iterator itr = avals.iterator();

                        while (itr.hasNext()) {
                            String val = (String) itr.next();

                            if (!FMA2EMFUtils.isNull(val)) {
                                SupportedLanguage lang = new SupportedLanguage();
                                lang.setLocalId(val);
                                lang.setUri(getLanguageURN(val));
                                suppLang.add(lang);
                                languagesFound = true;
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }

            if (!languagesFound) {
                SupportedLanguage lang = new SupportedLanguage();
                lang.setLocalId(FMA2EMFConstants.LANG_ENGLISH);
                lang.setUri(FMA2EMFConstants.LANG_ENGLISH_URN);
                suppLang.add(lang);

                lang = new SupportedLanguage();
                lang.setLocalId(FMA2EMFConstants.LANG_LATIN);
                lang.setUri(FMA2EMFConstants.LANG_LATIN_URN);
                suppLang.add(lang);
            }
        } catch (Exception e) {
            System.out.println("Failed while setting supported languages...");
            e.printStackTrace();
        }
    }

    private String getLanguageURN(String lng) {
        if (FMA2EMFConstants.LANG_ENGLISH.equals(lng))
            return FMA2EMFConstants.LANG_ENGLISH_URN;

        if (FMA2EMFConstants.LANG_GERMAN.equals(lng))
            return FMA2EMFConstants.LANG_GERMAN_URN;

        if (FMA2EMFConstants.LANG_FRENCH.equals(lng))
            return FMA2EMFConstants.LANG_FRENCH_URN;

        if (FMA2EMFConstants.LANG_RUSSIAN.equals(lng))
            return FMA2EMFConstants.LANG_RUSSIAN_URN;

        if (FMA2EMFConstants.LANG_SPANISH.equals(lng))
            return FMA2EMFConstants.LANG_SPANISH_URN;

        if (FMA2EMFConstants.LANG_LATIN.equals(lng))
            return FMA2EMFConstants.LANG_LATIN_URN;

        return FMA2EMFConstants.LANG_URN + FMA2EMFConstants.URN_DELIM + lng;
    }

    /*
     * private void prepareSupportedDataTypes(List suppDTps) { try {
     * SupportedDataType dataT = nameFactory.createSupportedDataType();
     * dataT.setLocalName(FMA2EMFConstants.PROT_ANY);
     * dataT.setUri(FMA2EMFConstants.PROT_ANY_URN); suppDTps.add(dataT);
     * 
     * dataT = nameFactory.createSupportedDataType();
     * dataT.setLocalName(FMA2EMFConstants.PROT_BOOLEAN);
     * dataT.setUri(FMA2EMFConstants.PROT_BOOLEAN_URN); suppDTps.add(dataT);
     * 
     * dataT = nameFactory.createSupportedDataType();
     * dataT.setLocalName(FMA2EMFConstants.PROT_CLS);
     * dataT.setUri(FMA2EMFConstants.PROT_CLS_URN); suppDTps.add(dataT);
     * 
     * dataT = nameFactory.createSupportedDataType();
     * dataT.setLocalName(FMA2EMFConstants.PROT_FLOAT);
     * dataT.setUri(FMA2EMFConstants.PROT_FLOAT_URN); suppDTps.add(dataT);
     * 
     * dataT = nameFactory.createSupportedDataType();
     * dataT.setLocalName(FMA2EMFConstants.PROT_INSTANCE);
     * dataT.setUri(FMA2EMFConstants.PROT_INSTANCE_URN); suppDTps.add(dataT);
     * 
     * dataT = nameFactory.createSupportedDataType();
     * dataT.setLocalName(FMA2EMFConstants.PROT_INTEGER);
     * dataT.setUri(FMA2EMFConstants.PROT_INTEGER_URN); suppDTps.add(dataT);
     * 
     * dataT = nameFactory.createSupportedDataType();
     * dataT.setLocalName(FMA2EMFConstants.PROT_STRING);
     * dataT.setUri(FMA2EMFConstants.PROT_STRING_URN); suppDTps.add(dataT);
     * 
     * dataT = nameFactory.createSupportedDataType();
     * dataT.setLocalName(FMA2EMFConstants.PROT_SYMBOL);
     * dataT.setUri(FMA2EMFConstants.PROT_SYMBOL_URN); suppDTps.add(dataT); }
     * catch (Exception e) { System.out.println("Failed while setting supported
     * languages..."); e.printStackTrace(); } }
     */

    private void prepareSupportedCodingScheme(CodingScheme csclass) {
        try {
            List suppCodingScheme = Arrays.asList(csclass.getMappings().getSupportedCodingScheme());
            SupportedCodingScheme scs = new SupportedCodingScheme();
            scs.setLocalId(csclass.getCodingSchemeName());
            scs.setUri(csclass.getCodingSchemeURI());
            suppCodingScheme.add(scs);
        } catch (Exception e) {
            messages_.error("Failed while setting supported codingScheme...", e);
        }
    }

    private void prepareSupportedFormats(List suppFmt) {
        SupportedDataType fmt = new SupportedDataType();
        fmt.setLocalId(FMA2EMFConstants.PLAIN_FORMAT);
        fmt.setUri(FMA2EMFConstants.PLAIN_FORMAT_URN);
        suppFmt.add(fmt);
    }

    public CodingScheme getFMACodingScheme(KnowledgeBase kb) {
        CodingScheme csclass = null;
        try {
            init(kb);

            csclass = new CodingScheme();
            csclass.setMappings(new Mappings());
            csclass.setCodingSchemeName(FMA2EMFConstants.CODING_SCHEME_NAME);
            csclass.setFormalName(FMA2EMFConstants.FORMAL_NAME);
            csclass.setCodingSchemeURI(FMA2EMFConstants.REGISTERED_NAME);
            csclass.setDefaultLanguage(FMA2EMFConstants.LANG_ENGLISH);
            csclass.setRepresentsVersion(FMA2EMFConstants.VERSION);
            csclass.addLocalName(FMA2EMFConstants.LOCAL_NAME);

            List supportedLanguages = Arrays.asList(csclass.getMappings().getSupportedLanguage());
            prepareSupportedLanguages(supportedLanguages, kb);

            List supportedFormats = Arrays.asList(csclass.getMappings().getSupportedDataType());
            prepareSupportedFormats(supportedFormats);
            prepareSupportedCodingScheme(csclass);

            // EList supportedDataTypes = csclass.getSupportedDataType();
            // prepareSupportedDataTypes(supportedDataTypes);
        } catch (Exception e) {
            System.out.println("Failed while preparing for Coding Scheme Class");
            e.printStackTrace();
        }

        return csclass;
    }

    public static Vector getFixedProperties() {
        properties.clear();
        properties.add(FMA2EMFConstants.PROPERTY_COMMENT);
        properties.add(FMA2EMFConstants.PROPERTY_DEFINITION);
        properties.add(FMA2EMFConstants.PROPERTY_INSTRUCTION);
        properties.add(FMA2EMFConstants.PROPERTY_TEXTPRESENTATION);

        return properties;
    }

    public static Vector getFixedAssociations() {
        association.clear();
        association.add(FMA2EMFConstants.ASSOCIATION_HASSUBTYPE);

        return association;
    }
}
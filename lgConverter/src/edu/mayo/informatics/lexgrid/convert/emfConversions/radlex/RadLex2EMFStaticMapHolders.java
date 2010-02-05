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
 *      http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package edu.mayo.informatics.lexgrid.convert.emfConversions.radlex;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.LexGrid.emf.codingSchemes.CodingschemesFactory;
import org.LexGrid.emf.codingSchemes.impl.CodingschemesFactoryImpl;
import org.LexGrid.emf.naming.Mappings;
import org.LexGrid.emf.naming.NamingFactory;
import org.LexGrid.emf.naming.SupportedCodingScheme;
import org.LexGrid.emf.naming.SupportedDataType;
import org.LexGrid.emf.naming.SupportedLanguage;
import org.LexGrid.emf.naming.impl.NamingFactoryImpl;
import org.LexGrid.messaging.LgMessageDirectorIF;
import org.LexGrid.naming.SupportedHierarchy;

import edu.mayo.informatics.lexgrid.convert.emfConversions.protegeOwl.ProtegeOwl2EMFConstants;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;

/* @author <A HREF="mailto:dks02@mayo.edu">Deepak Sharma</A>
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * @version subversion $Revision: 2917 $ checked in on $Date: 2006-06-19
 *          15:52:21 +0000 (Mon, 19 Jun 2006) $
 */

public class RadLex2EMFStaticMapHolders {
    private static Vector properties = new Vector();
    private static Vector association = new Vector();

    private NamingFactory nameFactory = null;
    private CodingschemesFactory csFactory = new CodingschemesFactoryImpl();
    private LgMessageDirectorIF messages_;

    private void init(KnowledgeBase kb) {
        nameFactory = new NamingFactoryImpl();
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

                            if (!RadLex2EMFUtils.isNull(val)) {
                                SupportedLanguage lang = nameFactory.createSupportedLanguage();
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
                SupportedLanguage lang = nameFactory.createSupportedLanguage();
                lang.setLocalId(RadLex2EMFConstants.LANG_ENGLISH);
                lang.setUri(RadLex2EMFConstants.LANG_ENGLISH_URN);
                suppLang.add(lang);

            }
        } catch (Exception e) {
            System.out.println("Failed while setting supported languages...");
            e.printStackTrace();
        }
    }

    private String getLanguageURN(String lng) {
        if (RadLex2EMFConstants.LANG_ENGLISH.equals(lng))
            return RadLex2EMFConstants.LANG_ENGLISH_URN;

        return RadLex2EMFConstants.LANG_URN + RadLex2EMFConstants.URN_DELIM + lng;
    }

    private void prepareSupportedCodingScheme(CodingScheme csclass) {
        try {
            List suppCodingScheme = csclass.getMappings().getSupportedCodingScheme();
            SupportedCodingScheme scs = nameFactory.createSupportedCodingScheme();
            scs.setLocalId(csclass.getCodingSchemeName());
            scs.setUri(csclass.getCodingSchemeURI());
            suppCodingScheme.add(scs);
        } catch (Exception e) {
            messages_.error("Failed while setting supported codingScheme...", e);
        }
    }
    
    public void prepareSupportedHierarchy(CodingScheme csclass) {
        try {
            List suppHierarchy = csclass.getMappings().getSupportedHierarchy();
            org.LexGrid.emf.naming.SupportedHierarchy shr = nameFactory.createSupportedHierarchy();
            shr.setLocalId(RadLex2EMFConstants.SUPP_HIERARCHY_ISA);
            shr.setUri(RadLex2EMFConstants.SUPP_HIERARCHY_ISA_URI);
            shr.setValue(RadLex2EMFConstants.SUPP_HIERARCHY_ISA);
            shr.setIsForwardNavigable(true);
            shr.setRootCode(RadLex2EMFConstants.ROOT_CODE);
            suppHierarchy.add(shr);
            shr.setAssociationNames(
                    Arrays.asList(RadLex2EMFConstants.SUPP_HIERARCHY_ISA_ASSOCIATION_LIST));
        } catch (Exception e) {
            messages_.error("Failed while setting supported Hierarchy...", e);
        }
    }

    private void prepareSupportedFormats(List suppFmt) {
        SupportedDataType fmt = nameFactory.createSupportedDataType();
        fmt.setLocalId(RadLex2EMFConstants.PLAIN_FORMAT);
        fmt.setUri(RadLex2EMFConstants.PLAIN_FORMAT_URN);
        suppFmt.add(fmt);
    }

    public CodingScheme getRadlexCodingScheme(KnowledgeBase kb) {
        CodingScheme csclass = null;
        try {
            init(kb);

            csclass = csFactory.createCodingScheme();
            csclass.setMappings((Mappings) nameFactory.createMappings());
            csclass.setCodingSchemeName(RadLex2EMFConstants.CODING_SCHEME_NAME);
            csclass.setFormalName(RadLex2EMFConstants.FORMAL_NAME);
            csclass.setCodingSchemeURI(RadLex2EMFConstants.REGISTERED_NAME);
            csclass.setDefaultLanguage(RadLex2EMFConstants.LANG_ENGLISH);
            csclass.setRepresentsVersion(RadLex2EMFConstants.VERSION);
            csclass.getLocalName().add(RadLex2EMFConstants.LOCAL_NAME);

            List supportedLanguages = csclass.getMappings().getSupportedLanguage();
            prepareSupportedLanguages(supportedLanguages, kb);

            List supportedFormats = csclass.getMappings().getSupportedDataType();
            prepareSupportedFormats(supportedFormats);
            prepareSupportedCodingScheme(csclass);
            prepareSupportedHierarchy(csclass);
            
            // EList supportedDataTypes = csclass.getSupportedDataType();
            // prepareSupportedDataTypes(supportedDataTypes);
        } catch (Exception e) {
            System.out.println("Failed while preparing for Coding Scheme Class");
            e.printStackTrace();
        }

        return csclass;
    }

    public static Vector getFixedProperties() 
    {
        properties.clear();
        properties.add(RadLex2EMFConstants.PROPERTY_COMMENT);
        properties.add(RadLex2EMFConstants.PROPERTY_DEFINITION);
        properties.add(RadLex2EMFConstants.PROPERTY_INSTRUCTION);
        properties.add(RadLex2EMFConstants.PROPERTY_TEXTPRESENTATION);
        properties.add(RadLex2EMFConstants.PROPERTY_SYNONYM);

        return properties;
    }

    public static Vector getFixedAssociations() {
        association.clear();
        association.add(RadLex2EMFConstants.ASSOCIATION_HASSUBTYPE);

        return association;
    }
}
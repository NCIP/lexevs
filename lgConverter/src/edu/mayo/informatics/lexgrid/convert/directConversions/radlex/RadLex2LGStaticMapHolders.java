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
package edu.mayo.informatics.lexgrid.convert.directConversions.radlex;

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

/* @author <A HREF="mailto:dks02@mayo.edu">Deepak Sharma</A>
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * @version subversion $Revision: 2917 $ checked in on $Date: 2006-06-19
 *          15:52:21 +0000 (Mon, 19 Jun 2006) $
 */

public class RadLex2LGStaticMapHolders {
    private static Vector properties = new Vector();
    private static Vector association = new Vector();

    private LgMessageDirectorIF messages_;

    private void init(KnowledgeBase kb) {

    }

    private void prepareSupportedLanguages(CodingScheme cs, KnowledgeBase kb) {
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

                            if (!RadLex2LGUtils.isNull(val)) {
                                SupportedLanguage lang = new SupportedLanguage();
                                lang.setLocalId(val);
                                lang.setUri(getLanguageURN(val));

                                cs.getMappings().addSupportedLanguage(lang);
                                languagesFound = true;
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }

            if (!languagesFound) {
                SupportedLanguage lang = new SupportedLanguage();
                lang.setLocalId(RadLex2LGConstants.LANG_ENGLISH);
                lang.setUri(RadLex2LGConstants.LANG_ENGLISH_URN);
                cs.getMappings().addSupportedLanguage(lang);

            }
        } catch (Exception e) {
            System.out.println("Failed while setting supported languages...");
            e.printStackTrace();
        }
    }

    private String getLanguageURN(String lng) {
        if (RadLex2LGConstants.LANG_ENGLISH.equals(lng))
            return RadLex2LGConstants.LANG_ENGLISH_URN;

        return RadLex2LGConstants.LANG_URN + RadLex2LGConstants.URN_DELIM + lng;
    }

    private void prepareSupportedCodingScheme(CodingScheme csclass) {
        try {
            SupportedCodingScheme scs = new SupportedCodingScheme();
            scs.setLocalId(csclass.getCodingSchemeName());
            scs.setUri(csclass.getCodingSchemeURI());
            csclass.getMappings().addSupportedCodingScheme(scs);
        } catch (Exception e) {
            messages_.error("Failed while setting supported codingScheme...", e);
        }
    }

    public void prepareSupportedHierarchy(CodingScheme csclass) {
        try {
            org.LexGrid.naming.SupportedHierarchy shr = new org.LexGrid.naming.SupportedHierarchy();
            shr.setLocalId(RadLex2LGConstants.SUPP_HIERARCHY_ISA);
            shr.setUri(RadLex2LGConstants.SUPP_HIERARCHY_ISA_URI);
            shr.setContent(RadLex2LGConstants.SUPP_HIERARCHY_ISA);
            shr.setIsForwardNavigable(true);
            shr.setRootCode(RadLex2LGConstants.ROOT_CODE);
            shr.setAssociationNames(Arrays.asList(RadLex2LGConstants.SUPP_HIERARCHY_ISA_ASSOCIATION_LIST));
            csclass.getMappings().addSupportedHierarchy(shr);
        } catch (Exception e) {
            messages_.error("Failed while setting supported Hierarchy...", e);
        }
    }

    private void prepareSupportedFormats(CodingScheme cs) {
        SupportedDataType fmt = new SupportedDataType();
        fmt.setLocalId(RadLex2LGConstants.PLAIN_FORMAT);
        fmt.setUri(RadLex2LGConstants.PLAIN_FORMAT_URN);
        cs.getMappings().addSupportedDataType(fmt);
    }

    public CodingScheme getRadlexCodingScheme(KnowledgeBase kb) {
        CodingScheme csclass = null;
        try {
            init(kb);

            csclass = new CodingScheme();
            csclass.setMappings(new Mappings());
            csclass.setCodingSchemeName(RadLex2LGConstants.CODING_SCHEME_NAME);
            csclass.setFormalName(RadLex2LGConstants.FORMAL_NAME);
            csclass.setCodingSchemeURI(RadLex2LGConstants.REGISTERED_NAME);
            csclass.setDefaultLanguage(RadLex2LGConstants.LANG_ENGLISH);
            csclass.setRepresentsVersion(RadLex2LGConstants.VERSION);
            csclass.addLocalName(RadLex2LGConstants.LOCAL_NAME);

            prepareSupportedLanguages(csclass, kb);

            prepareSupportedFormats(csclass);
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

    public static Vector getFixedProperties() {
        properties.clear();
        properties.add(RadLex2LGConstants.PROPERTY_COMMENT);
        properties.add(RadLex2LGConstants.PROPERTY_DEFINITION);
        properties.add(RadLex2LGConstants.PROPERTY_INSTRUCTION);
        properties.add(RadLex2LGConstants.PROPERTY_TEXTPRESENTATION);
        properties.add(RadLex2LGConstants.PROPERTY_SYNONYM);

        return properties;
    }

    public static Vector getFixedAssociations() {
        association.clear();
        association.add(RadLex2LGConstants.ASSOCIATION_HASSUBTYPE);

        return association;
    }
}
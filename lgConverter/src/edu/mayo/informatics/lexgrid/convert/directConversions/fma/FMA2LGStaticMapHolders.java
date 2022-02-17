
package edu.mayo.informatics.lexgrid.convert.directConversions.fma;

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

public class FMA2LGStaticMapHolders {
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

                            if (!FMA2LGUtils.isNull(val)) {
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
                lang.setLocalId(FMA2LGConstants.LANG_ENGLISH);
                lang.setUri(FMA2LGConstants.LANG_ENGLISH_URN);
                cs.getMappings().addSupportedLanguage(lang);

                lang = new SupportedLanguage();
                lang.setLocalId(FMA2LGConstants.LANG_LATIN);
                lang.setUri(FMA2LGConstants.LANG_LATIN_URN);
                cs.getMappings().addSupportedLanguage(lang);
            }
        } catch (Exception e) {
            System.out.println("Failed while setting supported languages...");
            e.printStackTrace();
        }
    }

    private String getLanguageURN(String lng) {
        if (FMA2LGConstants.LANG_ENGLISH.equals(lng))
            return FMA2LGConstants.LANG_ENGLISH_URN;

        if (FMA2LGConstants.LANG_GERMAN.equals(lng))
            return FMA2LGConstants.LANG_GERMAN_URN;

        if (FMA2LGConstants.LANG_FRENCH.equals(lng))
            return FMA2LGConstants.LANG_FRENCH_URN;

        if (FMA2LGConstants.LANG_RUSSIAN.equals(lng))
            return FMA2LGConstants.LANG_RUSSIAN_URN;

        if (FMA2LGConstants.LANG_SPANISH.equals(lng))
            return FMA2LGConstants.LANG_SPANISH_URN;

        if (FMA2LGConstants.LANG_LATIN.equals(lng))
            return FMA2LGConstants.LANG_LATIN_URN;

        return FMA2LGConstants.LANG_URN + FMA2LGConstants.URN_DELIM + lng;
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
            SupportedCodingScheme scs = new SupportedCodingScheme();
            scs.setLocalId(csclass.getCodingSchemeName());
            scs.setUri(csclass.getCodingSchemeURI());
            csclass.getMappings().addSupportedCodingScheme(scs);
        } catch (Exception e) {
            messages_.error("Failed while setting supported codingScheme...", e);
        }
    }

    private void prepareSupportedFormats(CodingScheme cs) {
        SupportedDataType fmt = new SupportedDataType();
        fmt.setLocalId(FMA2LGConstants.PLAIN_FORMAT);
        fmt.setUri(FMA2LGConstants.PLAIN_FORMAT_URN);
        cs.getMappings().addSupportedDataType(fmt);
    }

    public CodingScheme getFMACodingScheme(KnowledgeBase kb) {
        CodingScheme csclass = null;
        try {
            init(kb);

            csclass = new CodingScheme();
            csclass.setMappings(new Mappings());
            csclass.setCodingSchemeName(FMA2LGConstants.CODING_SCHEME_NAME);
            csclass.setFormalName(FMA2LGConstants.FORMAL_NAME);
            csclass.setCodingSchemeURI(FMA2LGConstants.REGISTERED_NAME);
            csclass.setDefaultLanguage(FMA2LGConstants.LANG_ENGLISH);
            csclass.setRepresentsVersion(FMA2LGConstants.VERSION);
            csclass.addLocalName(FMA2LGConstants.LOCAL_NAME);

            prepareSupportedLanguages(csclass, kb);

            prepareSupportedFormats(csclass);
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
        properties.add(FMA2LGConstants.PROPERTY_COMMENT);
        properties.add(FMA2LGConstants.PROPERTY_DEFINITION);
        properties.add(FMA2LGConstants.PROPERTY_INSTRUCTION);
        properties.add(FMA2LGConstants.PROPERTY_TEXTPRESENTATION);

        return properties;
    }

    public static Vector getFixedAssociations() {
        association.clear();
        association.add(FMA2LGConstants.ASSOCIATION_HASSUBTYPE);

        return association;
    }
}
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

import edu.stanford.smi.protege.model.Model;

/* @author <A HREF="mailto:dks02@mayo.edu">Deepak Sharma</A>
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * @version subversion $Revision: 2917 $ checked in on $Date: 2006-06-19
 *          15:52:21 +0000 (Mon, 19 Jun 2006) $
 */

public class RadLex2LGConstants {
    public static String URN_DELIM = ":";
    public static String DELIM = "::";
    public static String OUTPUT_XML_FILE = "C:/Radlexmap.xml";
    public static String OUTPUT_DEFAULT_URI = "file:///C:/Radlexmap.xml";
    public static String SERVICE_NAME = "Radlex";

    public static String CODING_SCHEME_NAME = "Radlex";
    public static String LOCAL_NAME = "Radlex";
    public static String FORMAL_NAME = "Radiology Lexicon";
    public static String REGISTERED_NAME = "urn:iso:2.16.84.01.113883.6.150"; // need
                                                                              // to
                                                                              // be
                                                                              // confirmed
    public static String Radlex_URN = "urn:iso:2.16.84.01.113883.6.150"; // need
                                                                         // to
                                                                         // be
                                                                         // confirmed

    public static String COPYRIGHT = "Copyright text goes here";

    public static String SOURCE = "http://www.radlex.org";

    public static String VERSION = "2.1";

    public static final String CODING_SCHEME_DC = "codingSchemes";
    public static final String CONCEPT_DC = "concepts";
    // Langauges
    // need to be confirmed
    public static final String LANG_URN = "urn:iso:2.16.840.1.113883.6.99"; // need
                                                                            // to
                                                                            // be
                                                                            // changed?
    public static final String LANG_ENGLISH_URN = LANG_URN + URN_DELIM + "en";
    /*
     * public static final String LANG_GERMAN_URN = LANG_URN + URN_DELIM + "de";
     * public static final String LANG_FRENCH_URN = LANG_URN + URN_DELIM + "fr";
     * public static final String LANG_SPANISH_URN = LANG_URN + URN_DELIM +
     * "es"; public static final String LANG_RUSSIAN_URN = LANG_URN + URN_DELIM
     * + "ru";
     */
    public static final String LANG_LATIN_URN = LANG_URN + URN_DELIM + "la";

    public static final String LANG_ENGLISH = "English";
    /*
     * public static final String LANG_GERMAN = "German"; public static final
     * String LANG_FRENCH = "French"; public static final String LANG_SPANISH =
     * "Spanish"; public static final String LANG_RUSSIAN = "Russian";
     */
    public static final String LANG_LATIN = "Latin";

    // Formats
    // need to be confirmed
    public static final String PLAIN_FORMAT_URN = "urn:iso:2.16.840.1.113883.6.10" + URN_DELIM + "text_plain";
    public static final String PLAIN_FORMAT = "text_plain";

    public static final String PROPERTY_PREFIX = "Property_";
    public static final String DEFINITON_PREFIX = "Definition_";
    public static final String COMMENT_PREFIX = "Comment_";
    public static final String INSTRUCTION_PREFIX = "Instruction_";

    // Fixed DataTypes (from protege)
    public final static String PROT_ANY_URN = "urn:iso:http://protege.stanford.edu" + URN_DELIM + "Any";
    public final static String PROT_BOOLEAN_URN = "urn:iso:http://protege.stanford.edu" + URN_DELIM + "Boolean";
    public final static String PROT_CLS_URN = "urn:iso:http://protege.stanford.edu" + URN_DELIM + "Class";
    public final static String PROT_FLOAT_URN = "urn:iso:http://protege.stanford.edu" + URN_DELIM + "Float";
    public final static String PROT_INSTANCE_URN = "urn:iso:http://protege.stanford.edu" + URN_DELIM + "Instance";
    public final static String PROT_INTEGER_URN = "urn:iso:http://protege.stanford.edu" + URN_DELIM + "Integer";
    public final static String PROT_STRING_URN = "urn:iso:http://protege.stanford.edu" + URN_DELIM + "String";
    public final static String PROT_SYMBOL_URN = "urn:iso:http://protege.stanford.edu" + URN_DELIM + "Symbol";

    public final static String PROT_ANY = "Any";
    public final static String PROT_BOOLEAN = "Boolean";
    public final static String PROT_CLS = "Class";
    public final static String PROT_FLOAT = "Float";
    public final static String PROT_INSTANCE = "Instance";
    public final static String PROT_INTEGER = "Integer";
    public final static String PROT_STRING = "String";
    public final static String PROT_SYMBOL = "Symbol";

    // Fixed Properties
    public static final String PROPERTY_TEXTPRESENTATION = "textualPresentation";
    public static final String PROPERTY_DEFINITION = "definition";
    public static final String PROPERTY_COMMENT = "comment";
    public static final String PROPERTY_INSTRUCTION = "instruction";
    public static final String PROPERTY_SYNONYM = "synonym";

    public static final String PROPERTY_ID_PREFIX = "P";

    public static final String ASSOCIATION_HASSUBTYPE = "hasSubtype";
    public static final String ASSOCIATION_ISA = "Is_A";

    public static final String URI_MISSING = null;
    public static final String ROOT_CODE = "@";
    public static final String SUPP_HIERARCHY = "Hierarchy";
    public static final String SUPP_HIERARCHY_ISA = "is_a";
    public static final String SUPP_HIERARCHY_ISA_URI = URI_MISSING;
    public static final String[] SUPP_HIERARCHY_ISA_ASSOCIATION_LIST =
        new String[] { ASSOCIATION_HASSUBTYPE };
    
    // Radlex Constants
    // need to be changed
    public static final String[] topClasses = {"RID0"};

    // public static final String [] topClasses = {"Attribute entity"};

    public static final String SLOT_Radlex_CONCEPT_CODE = ":NAME";
    public static final String SLOT_Radlex_PREFERRED_NAME = "Preferred_Name";
    public static final String SLOT_Radlex_SYNONYM = "Synonym";
    public static final String SLOT_Radlex_SYNONYM_OF = "Synonym_Of";
    public static final String SLOT_Radlex_SYNONYMS_SLOT = "Synonym_Name";
    public static final String SLOT_Radlex_OTHER_ENG_EQUIV = "English equivalent";
    public static final String SLOT_Radlex_NON_ENG_EQUIV = "Non-English equivalents";

    public static final String SLOT_NAME = "Name";
    public static final String SLOT_HASSUBTYPE = "Has_Subtype";

    public static final String SLOT_DEFINITION = "Definition";
    public static final String SLOT_DOCUMENTATION = Model.Slot.DOCUMENTATION;
    public static final String SLOT_COMMENT = "Comment";

    public static final String SLOT_TYPE_PRESENTATION = "presentation";
    public static final String SLOT_TYPE_INSTRUCTION = "instruction";
    public static final String SLOT_TYPE_PROPERTY = "property";
    public static final String SLOT_TYPE_ASSOCIATION = "association";
    public static final String SLOT_TYPE_DEFINITION = "Definition";
    public static final String SLOT_TYPE_COMMENT = "Comment";

    public static final String UNKNOWN = "Unknown";
}
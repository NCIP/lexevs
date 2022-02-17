
package edu.mayo.informatics.lexgrid.convert.directConversions.fma;

import edu.stanford.smi.protege.model.Model;

/* @author <A HREF="mailto:dks02@mayo.edu">Deepak Sharma</A>
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * @version subversion $Revision: 2917 $ checked in on $Date: 2006-06-19
 *          15:52:21 +0000 (Mon, 19 Jun 2006) $
 */

public class FMA2LGConstants {
    public static String URN_DELIM = ":";
    public static String DELIM = "::";
    public static String OUTPUT_XML_FILE = "C:/fmamap.xml";
    public static String OUTPUT_DEFAULT_URI = "file:///C:/fmamap.xml";
    public static String SERVICE_NAME = "FMA";

    public static String CODING_SCHEME_NAME = "FMA";
    public static String LOCAL_NAME = "FMA";
    public static String FORMAL_NAME = "Foundational Model of Anatomy";
    public static String REGISTERED_NAME = "urn:iso:2.16.84.01.113883.6.119";
    public static String FMA_URN = "urn:iso:2.16.84.01.113883.6.119";

    public static String COPYRIGHT = "Copyright text goes here";

    public static String SOURCE = "http://sig.biostr.washington.edu/projects/fm/AboutFM.html";

    public static String VERSION = "1.1.0";

    public static final String CODING_SCHEME_DC = "codingSchemes";
    public static final String CONCEPT_DC = "concepts";
    // Langauges
    public static final String LANG_URN = "urn:iso:2.16.840.1.113883.6.99";
    public static final String LANG_ENGLISH_URN = LANG_URN + URN_DELIM + "en";
    public static final String LANG_GERMAN_URN = LANG_URN + URN_DELIM + "de";
    public static final String LANG_FRENCH_URN = LANG_URN + URN_DELIM + "fr";
    public static final String LANG_SPANISH_URN = LANG_URN + URN_DELIM + "es";
    public static final String LANG_RUSSIAN_URN = LANG_URN + URN_DELIM + "ru";
    public static final String LANG_LATIN_URN = LANG_URN + URN_DELIM + "la";

    public static final String LANG_ENGLISH = "English";
    public static final String LANG_GERMAN = "German";
    public static final String LANG_FRENCH = "French";
    public static final String LANG_SPANISH = "Spanish";
    public static final String LANG_RUSSIAN = "Russian";
    public static final String LANG_LATIN = "Latin";

    // Formats
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
    public static final String PROPERTY_TEXTPRESENTATION_ID = "P1";
    public static final String PROPERTY_DEFINITION = "definition";
    public static final String PROPERTY_DEFINITION_ID = "P2";
    public static final String PROPERTY_COMMENT = "comment";
    public static final String PROPERTY_COMMENT_ID = "P3";
    public static final String PROPERTY_INSTRUCTION = "instruction";
    public static final String PROPERTY_INSTRUCTION_ID = "P4";

    public static final String PROPERTY_ID_PREFIX = "P";

    public static final String ASSOCIATION_HASSUBTYPE = "hasSubtype";
    public static final String ASSOCIATION_ISA = "isA";

    // FMA Constants
    public static final String[] topClasses = {// Model.Cls.STANDARD_SLOT,
    "Dimensional entity", "Anatomical entity", "Attribute entity", "Anatomical transformation entity" };

    // public static final String [] topClasses = {"Attribute entity"};

    // public static final String SLOT_FMA_CONCEPT_CODE = "UWDAID";
    public static final String SLOT_FMA_CONCEPT_CODE = "FMAID";
    public static final String SLOT_FMA_PREFERRED_NAME = "Preferred name";
    public static final String SLOT_FMA_SYNONYM = "Synonym";
    public static final String SLOT_FMA_OTHER_ENG_EQUIV = "English equivalent";
    public static final String SLOT_FMA_NON_ENG_EQUIV = "Non-English equivalent";

    public static final String SLOT_NAME = "name";

    public static final String SLOT_DEFINITION = "definition";
    public static final String SLOT_COMMENT = Model.Slot.DOCUMENTATION;

    public static final String SLOT_TYPE_PRESENTATION = "presentation";
    public static final String SLOT_TYPE_INSTRUCTION = "instruction";
    public static final String SLOT_TYPE_PROPERTY = "property";
    public static final String SLOT_TYPE_ASSOCIATION = "association";
    public static final String SLOT_TYPE_DEFINITION = "definition";
    public static final String SLOT_TYPE_COMMENT = "comment";

    public static final String UNKNOWN = "Unknown";
}
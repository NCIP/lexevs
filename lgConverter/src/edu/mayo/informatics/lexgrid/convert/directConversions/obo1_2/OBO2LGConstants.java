
package edu.mayo.informatics.lexgrid.convert.directConversions.obo1_2;

/**
 * OBO To EMF Implementation.
 * 
 * @author <A HREF="mailto:dks02@mayo.edu">Deepak Sharma</A>
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 * @version subversion $Revision: 2954 $ checked in on $Date: 2006-08-10
 *          14:27:54 +0000 (Thu, 10 Aug 2006) $
 */
public class OBO2LGConstants {
    public static String URN_DELIM = ":";
    public static String DELIM = "::";
    public static String HL7OID_ID_PREFIX = "urn:oid:2.16.840.1.113883.6.";
    public static final String PROPERTY_ID_PREFIX = "P";
    public static final String HIERARCHY_DEFAULT_NAME = "is_a";
    public static final String HIERARCHY_DEFAULT_ROOT = "@@";
    public static final String ASSOCIATION_HASSUBTYPE_FORWARDNAME = "has_subclass";
    public static final String ASSOCIATION_ISA = "is_a";
    public static final String ASSOCIATION_ISA_ISFORWARDNAVIGABLE = "false";
    public static final String ASSOCIATION_HASSUBTYPE = "hasSubtype";
    public static final String ASSOCIATION_INTERSECTION_OF = "intersection_of";
    public static final String ASSOCIATION_DISJOINT_FROM = "disjoint_from";
    public static final String ASSOCIATION_INSTANCE_OF = "instance_of";
    public static final String ASSOCIATION_UNION_OF = "union_of";
    public static final String ASSOCIATION_INVERSE_OF = "inverse_of";
    public static final String ASSOCIATION_PART_OF = "part_of";
    public static final String ASSOCIATION_OBO_REL_PART_OF = "OBO_REL:part_of";
    public static final String ASSOCIATION_HAS_PART = "has_part";
    public static final String ASSOCIATION_PART_OF_ISFORWARDNAVIGABLE = "false";
    public static final String ASSOCIATION_DEVELOPS_FROM = "develops_from";
    public static final String ASSOCIATION_DEVELOPS_TO = "develops_to";
    public static final String ASSOCIATION_DEVELOPS_FROM_ISFORWARDNAVIGABLE = "false";
    
    public static final String ASSOCIATION_LOCATED_IN = "located_in";
    public static final String ASSOCIATION_DERIVES_FROM = "derives_from";
    public static final String ASSOCIATION_BROADER = "broader";

    public static final String LANG_URN = HL7OID_ID_PREFIX + "84";

    public static final String LANG_ENGLISH_URN = LANG_URN + URN_DELIM + "en";
    public static final String LANG_ENGLISH = "en";

    public static final String CODING_SCHEME_DC = "codingSchemes";
    public static final String CONCEPT_DC = "concepts";
    public final static String PROT_STRING_URN = "urn:oid:java.lang.String" + URN_DELIM + "String";
    public final static String PROT_STRING = "String";
    // Formats
    public static final String PLAIN_FORMAT_URN = HL7OID_ID_PREFIX + "10" + URN_DELIM + "text_plain";
    public static final String PLAIN_FORMAT = "text_plain";

    // Fixed Properties
    public static final String ANONYMOUS_TEXTPRESENTATION = "_Anon";
    public static final String PROPERTY_TEXTPRESENTATION = "textualPresentation";
    public static final String PROPERTY_TEXTPRESENTATION_ID = "P1";
    public static final String PROPERTY_DEFINITION = "definition";
    public static final String PROPERTY_DEFINITION_ID = "P2";
    public static final String PROPERTY_COMMENT = "comment";
    public static final String PROPERTY_COMMENT_ID = "P3";
    public static final String PROPERTY_INSTRUCTION = "instruction";
    public static final String PROPERTY_INSTRUCTION_ID = "P4";
    public static final String PROPERTY_SUBSET = "subset";
    public static final String PROPERTY_ALTID = "alt_id";
    public static final String PROPERTY_CREATED_BY = "created_by";
    public static final String PROPERTY_CREATION_DATE = "creation_date";
    public static final String PROPERTY_XREF = "xref";

    public static final String PROPERTY_SYNONYM = "synonym";
    public static final String BIOONTOLOGY_LSID_PREFIX = "urn:lsid:bioontology.org:";
    public static final String REGISTERED_PREFIX = HL7OID_ID_PREFIX + "200."; // Pradip
    public static final String LOCAL_FULL_PREFIX = "bioontology.org:"; // Pradip

    public static String OBO_URN = "";
    public static final String FORMAT_VERSION = "format-version:";
    public static final String TYPEREF = "typeref:";
    public static final String VERSION = "version:";
    public static final String DATE = "date:";
    public static final String SAVED_BY = "saved-by:";
    public static final String AUTO_GENERATED_BY = "auto-generated-by:";
    public static final String DEFAULT_NS = "default-namespace:";
    public static final String NAMESPACE = "namespace:";
    public static final String REMARK = "remark:";
    public static final String SUBSETDEF = "subsetdef:";
    public static final String OBO_REL_FILENAME = "relationship.obo";
    public static final String OBO_ABB_FILENAME = "GO_xrf_abbs.obo";

    public static final String BUILT_IN_ASSOCIATIONS[] = { ASSOCIATION_ISA, ASSOCIATION_INTERSECTION_OF,
            ASSOCIATION_DISJOINT_FROM, ASSOCIATION_INSTANCE_OF, ASSOCIATION_UNION_OF, ASSOCIATION_INVERSE_OF };

    public static final String BUILT_IN_SPECIAL_ASSOCIATIONS[] = { ASSOCIATION_INTERSECTION_OF,
             ASSOCIATION_UNION_OF };


 
}
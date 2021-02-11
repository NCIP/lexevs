
package edu.mayo.informatics.resourcereader.obo;

import edu.mayo.informatics.resourcereader.core.StringUtils;

/**
 * This file contains the OBO Constants. This file is used while parsing the obo
 * text file. It is also used by the programs that read the OBO content into the
 * EMF model.
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOConstants extends OBO {
    public static String DELIM = "::";

    public static final String OBO_CURRENT_FORMAT = "1.2";

    public static final String REL_ISA = "is_a";
    public static final String REL_INTERSECTIONOF = "intersection_of";
    public static final String REL_UNIONOF = "union_of";
    public static final String REL_DISJOINTFROM = "disjoint_from";
    public static final String REL_INVERSEOF = "inverse_of";
    public static final String REL_INSTANCEOF = "instance_of";

    public static final String REL_REPLACEDBY = "replaced_by";

    public static final String OBOTYPES_TERM = "obo:TERM";
    public static final String OBOTYPES_TYPE = "obo:TYPE";
    public static final String OBOTYPES_TERMORTYPE = "obo:TERM_OR_TYPE";
    public static final String OBOTYPES_INSTANCE = "obo:INSTANCE";
    public static final String OBODEF_TRLMODF = "obo:defs";

    // Header tags
    // Required tags
    public static final String TAG_FORMAT_VERSION = "format-version:";

    public static final String UNASSIGNED_LABEL = "UNASSIGNED";

    // Optional tags
    public static final String TAG_DATAVERSION = "data-version:";
    public static final String TAG_VERSION = "version:"; // deprecated
    public static final String TAG_DATE = "date:";
    public static final String TAG_SAVED_BY = "saved-by:";
    public static final String TAG_AUTO_GENERATED_BY = "auto-generated-by:";
    public static final String TAG_SUBSETDEF = "subsetdef:";
    public static final String TAG_IMPORT = "import:";
    public static final String TAG_TYPEREF = "typeref:"; // deprecated
    public static final String TAG_SYNONYMTYPEDEF = "synonymtypedef:";
    public static final String TAG_IDSPACE = "idspace:";
    public static final String TAG_DEFAULT_NS = "default-namespace:";
    public static final String TAG_DEFAULTRELATIONSHIPID = "default-relationship-id:";
    public static final String TAG_IDMAPPING = "id-mapping:";
    public static final String TAG_REMARK = "remark:";

    public static final String TAG_TERM = "[Term]";
    public static final String TAG_TYPEDEF = "[Typedef]";
    public static final String TAG_INSTANCE = "[Instance]";

    public static final String TAG_ABBREVIATION = "abbreviation:";
    public static final String TAG_GENERICURL = "generic_url:";
    public static final String TAG_SYNONYM = "synonym:";
    public static final String TAG_SYNONYM_EXACT = "exact_synonym:";
    public static final String TAG_SYNONYM_BROAD = "broad_synonym:";
    public static final String TAG_SYNONYM_NARROW = "narrow_synonym:";
    public static final String TAG_SYNONYM_RELATED = "related_synonym:";

    public static final String TAG_ID = "id:";
    public static final String TAG_ALTID = "alt_id:";
    public static final String TAG_CREATED_BY ="created_by:";
    public static final String TAG_CREATION_DATE ="creation_date:";
    public static final String TAG_NAME = "name:";
    public static final String TAG_DOMAIN = "domain:";
    public static final String TAG_RANGE = "range:";
    public static final String TAG_NAMESPACE = "namespace:";
    public static final String TAG_ISANONYMOUS = "is_anonymous:";
    public static final String TAG_ISTRANSITIVE = "is_transitive:";
    public static final String TAG_ISSYMMETRIC = "is_symmetric:";
    public static final String TAG_ISANTISYMMETRIC = "is_anti_symmetric:";
    public static final String TAG_ISREFLEXIVE = "is_reflexive:";
    public static final String TAG_ISCYCLIC = "is_cyclic:";
    public static final String TAG_DEF = "def:";
    public static final String TAG_COMMENT = "comment:";
    public static final String TAG_SUBSET = "subset:";
    public static final String TAG_ISA = REL_ISA + ":";
    public static final String TAG_INTERSECTIONOF = REL_INTERSECTIONOF + ":";
    public static final String TAG_UNIONOF = REL_UNIONOF + ":";
    public static final String TAG_DISJOINTFROM = REL_DISJOINTFROM + ":";
    public static final String TAG_ISOBSOLETE = "is_obsolete:";
    public static final String TAG_XREF = "xref:";
    public static final String TAG_XREFANALOG = "xref_analog:";
    public static final String TAG_XREFUNK = "xref_unk:";
    public static final String TAG_RELATIONSHIP = "relationship:";
    public static final String TAG_INVERSEOF = REL_INVERSEOF + ":";
    public static final String TAG_INSTANCEOF = REL_INSTANCEOF + ":";
    public static final String TAG_REPLACEDBY = REL_REPLACEDBY + ":";
    public static final String TAG_CONSIDER = "consider:";
    public static final String TAG_USETERM = "use_term:";
    public static final String TAG_PROPERTYVALUE = "property_value:";

    public static final String HTTP = "http";
    public static final String START_TM = "[";
    public static final String END_TM = "]";

    public static final int ABBREVIATION_CTX = 0;
    public static final int TERM_CTX = 1;
    public static final int RELATION_CTX = 2;
    public static final int INSTANCE_CTX = 3;

    // Built-in Relations
    public static final String BLTNREL_ISA_ID = REL_ISA;
    public static final String BLTNREL_ISA_NAME = REL_ISA;
    public static final String BLTNREL_ISA_DOMAIN = OBOTYPES_TERMORTYPE;
    public static final String BLTNREL_ISA_RANGE = OBOTYPES_TERMORTYPE;
    public static final String BLTNREL_ISA_DEF = "The basic subclassing relationship";
    public static final String BLTNREL_ISA_DEFSRC = OBODEF_TRLMODF;
    // public static final String BLTNREL_ISA_INVERSEOF = "hasSubtype";

    public static final String BLTNREL_DISJFROM_ID = REL_DISJOINTFROM;
    public static final String BLTNREL_DISJFROM_NAME = REL_DISJOINTFROM;
    public static final String BLTNREL_DISJFROM_DOMAIN = OBOTYPES_TERM;
    public static final String BLTNREL_DISJFROM_RANGE = OBOTYPES_TERM;
    public static final String BLTNREL_DISJFROM_DEF = "Indicates that two classes are disjoint";
    public static final String BLTNREL_DISJFROM_DEFSRC = OBODEF_TRLMODF;

    public static final String BLTNREL_UNIONOF_ID = REL_UNIONOF;
    public static final String BLTNREL_UNIONOF_NAME = REL_UNIONOF;
    public static final String BLTNREL_UNIONOF_DOMAIN = OBOTYPES_TERM;
    public static final String BLTNREL_UNIONOF_RANGE = OBOTYPES_TERM;
    public static final String BLTNREL_UNIONOF_DEF = "Indicates that a term is the union of several others";
    public static final String BLTNREL_UNIONOF_DEFSRC = OBODEF_TRLMODF;

    public static final String BLTNREL_INTRSECOF_ID = REL_INTERSECTIONOF;
    public static final String BLTNREL_INTRSECOF_NAME = REL_INTERSECTIONOF;
    public static final String BLTNREL_INTRSECOF_DOMAIN = OBOTYPES_TERM;
    public static final String BLTNREL_INTRSECOF_RANGE = OBOTYPES_TERM;
    public static final String BLTNREL_INTRSECOF_DEF = "Indicates that a term is the intersection of several others";
    public static final String BLTNREL_INTRSECOF_DEFSRC = OBODEF_TRLMODF;

    public static final String BLTNREL_INSTOF_ID = REL_INSTANCEOF;
    public static final String BLTNREL_INSTOF_NAME = REL_INSTANCEOF;
    public static final String BLTNREL_INSTOF_DOMAIN = OBOTYPES_INSTANCE;
    public static final String BLTNREL_INSTOF_RANGE = OBOTYPES_TERM;
    public static final String BLTNREL_INSTOF_DEF = "Indicates the type of an instance";
    public static final String BLTNREL_INSTOF_DEFSRC = OBODEF_TRLMODF;

    public static final String BLTNREL_INVOF_ID = REL_INVERSEOF;
    public static final String BLTNREL_INVOF_NAME = REL_INVERSEOF;
    public static final String BLTNREL_INVOF_DOMAIN = OBOTYPES_TYPE;
    public static final String BLTNREL_INVOF_RANGE = OBOTYPES_TYPE;
    public static final String BLTNREL_INVOF_DEF = "Indicates that one relationship type is the inverse of another";
    public static final String BLTNREL_INVOF_DEFSRC = OBODEF_TRLMODF;

    public static final String TAG_IGNORE_DATABASE = "database:";
    public static final String TAG_IGNORE_OBJECT = "object:";
    public static final String TAG_IGNORE_EXAMPLE = "example:";
    public static final String TAG_IGNORE_BUILTIN = "builtin:";
    public static final String TAG_IGNORE_URLSNTX = "url_syntax:";
    public static final String TAG_IGNORE_SHORTHNDNAME = "shorthand_name:";
    public static final String TAG_IGNORE_URLEXMPL = "url_example:";
    public static final String TAG_IGNORE_EXMPLID = "example_id:";
    public static final String TAG_IGNORE_NSIDRULE = "namespace-id-rule:";

    public static boolean isHeaderTag(String line) {
        if (line != null) {
            return (line.startsWith(TAG_FORMAT_VERSION) || line.startsWith(TAG_DATAVERSION)
                    || line.startsWith(TAG_VERSION) || line.startsWith(TAG_DATE) || line.startsWith(TAG_SAVED_BY)
                    || line.startsWith(TAG_AUTO_GENERATED_BY) || line.startsWith(TAG_SUBSETDEF)
                    || line.startsWith(TAG_IMPORT) || line.startsWith(TAG_SYNONYMTYPEDEF)
                    || line.startsWith(TAG_TYPEREF) || line.startsWith(TAG_IDSPACE) || line.startsWith(TAG_DEFAULT_NS)
                    || line.startsWith(TAG_DEFAULTRELATIONSHIPID) || line.startsWith(TAG_IDMAPPING) || line
                    .startsWith(TAG_REMARK));
        }
        return false;
    }

    public static boolean isIgnoredTag(String line) {
        if (line != null) {
            return (line.startsWith(TAG_IGNORE_DATABASE) || line.startsWith(TAG_IGNORE_OBJECT)
                    || line.startsWith(TAG_IGNORE_EXAMPLE) || line.startsWith(TAG_IGNORE_BUILTIN)
                    || line.startsWith(TAG_IGNORE_URLSNTX) || line.startsWith(TAG_IGNORE_SHORTHNDNAME)
                    || line.startsWith(TAG_IGNORE_URLEXMPL) || line.startsWith(TAG_IGNORE_EXMPLID)
                    || line.startsWith(TAG_IGNORE_NSIDRULE) || StringUtils.isNull(line));
        }
        return false;
    }
}
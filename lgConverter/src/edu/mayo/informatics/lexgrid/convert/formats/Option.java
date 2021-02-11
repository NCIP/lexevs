
package edu.mayo.informatics.lexgrid.convert.formats;

/**
 * Define all options that can be used by various conversions.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: 9250 $ checked in on $Date: 2008-07-23
 *          21:56:18 +0000 (Wed, 23 Jul 2008) $
 */
public class Option {
    public final static int DELIMITER = 1;
    public final static int FORCE_FORMAT_B = 2;
    public final static int ENFORCE_INTEGRITY = 3;
    public final static int FAIL_ON_ERROR = 4;
    public final static int DO_WITH_EMF = 5;
    public final static int LDAP_PAGE_SIZE = 6;
    public final static int SQL_FETCH_SIZE = 7;
    public final static int HOSTED_BY = 8;
    public final static int CONTACT_INFO = 9;
    public final static int PUBLIC_USERNAME = 10;
    public final static int PUBLIC_PASSWORD = 11;
    public final static int BUILD_DB_METAPHONE = 12;
    public final static int USE_COMPOUND_FMT = 13;
    public final static int SKIP_NON_LEXGRID_FILES = 14;
    public final static int OVERWRITE = 15;
    public final static int BUILD_STEM = 16;
    public final static int MRHIER_OPT = 17;
    public final static int ROOT_RECALC = 18;
    public final static int MEMORY_SAFE = 19;

    private String optionName, optionDescription;
    private Object optionValue;

    public Option(int type, Object optionValue) {
        this.optionName = getNameForType(type);
        this.optionDescription = getDescriptionForType(type);
        this.optionValue = optionValue;
    }

    public static String getNameForType(int type) {
        switch (type) {
        case DELIMITER:
            return "Delimiter";
        case FORCE_FORMAT_B:
            return "Force Format B";
        case ENFORCE_INTEGRITY:
            return "Enforce Integrity";
        case FAIL_ON_ERROR:
            return "Fail on all errors";
        case DO_WITH_EMF:
            return "Do conversion with EMF";
        case LDAP_PAGE_SIZE:
            return "LDAP Page Size";
        case SQL_FETCH_SIZE:
            return "SQL Fetch Size";
        case HOSTED_BY:
            return "Hosted By";
        case CONTACT_INFO:
            return "Contact Info";
        case PUBLIC_USERNAME:
            return "Public Username";
        case PUBLIC_PASSWORD:
            return "Public Password";
        case BUILD_DB_METAPHONE:
            return "Build Double Metaphone Index";
        case BUILD_STEM:
            return "Build Stemming Index";
        case USE_COMPOUND_FMT:
            return "Use Compound Index Format";
        case SKIP_NON_LEXGRID_FILES:
            return "Skip non LexGrid files";
        case OVERWRITE:
            return "Overwrite existing files";
        case MRHIER_OPT:
            return "MRHIER processing flag";
        case ROOT_RECALC:
            return "Recalculate root nodes";
        case MEMORY_SAFE:
            return "Memory safe";
        default:
            return "";
        }

    }

    public static String getDescriptionForType(int type) {
        switch (type) {
        case DELIMITER:
            return "The Delimiter that seperates the items in the file.  Defaults to tab.  Optional.";
        case FORCE_FORMAT_B:
            return "Forces it to read a format 'A' file as a format 'B' File.";
        case ENFORCE_INTEGRITY:
            return "Should the table constraints be enforced";
        case FAIL_ON_ERROR:
            return "Unchecking this option will allow you to continue over minor errors.";
        case DO_WITH_EMF:
            return "Checking this option will cause the conversion to be done with the EMF code instead of directly.";
        case LDAP_PAGE_SIZE:
            return "How many results to retrieve at once from the LDAP server";
        case SQL_FETCH_SIZE:
            return "How many results to retrieve at once from the SQL server (not all databases support this)";
        case HOSTED_BY:
            return "Who is hosting the content to be registered?";
        case CONTACT_INFO:
            return "How does someone contact you about the content?";
        case PUBLIC_USERNAME:
            return "The username to include in the LexGrid Service Index - viewable by everyone.";
        case PUBLIC_PASSWORD:
            return "The password to include in the LexGrid Service Index - viewable by everyone.";
        case BUILD_DB_METAPHONE:
            return "Should the index be able to answer 'Sounds-Like' queries?";
        case BUILD_STEM:
            return "Should the index have stemmed fields (trees -> tree)?";
        case USE_COMPOUND_FMT:
            return "Use the Lucene Compound index format (slower but uses less file handles)";
        case SKIP_NON_LEXGRID_FILES:
            return "Don't load RRF files that aren't needed by LexGrid";
        case OVERWRITE:
            return "Overwrite files that exist in the destination location";
        case MRHIER_OPT:
            return "Process hierarchical relationships stored by the "
                    + "MRHIER file. 0=none (default), 1=HCD-tagged items";
        case ROOT_RECALC:
            return "Recalculate links to system-designated top and end nodes";
        case MEMORY_SAFE:
            return "Tune processing for reduced memory footprint.\n" +
                   "Options are:\n" +
                   "1 (Recommended) = Faster/more memory (holds OWL in memory)\n" +
                   "2 = Slower/less memory (cache OWL to database)";
        default:
            return "";
        }

    }

    public String getOptionName() {
        return optionName;
    }

    public String getOptionDescription() {
        return optionDescription;
    }

    public Object getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(Object value) {
        optionValue = value;
    }

}
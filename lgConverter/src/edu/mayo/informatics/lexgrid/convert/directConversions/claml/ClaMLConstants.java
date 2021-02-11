
package edu.mayo.informatics.lexgrid.convert.directConversions.claml;

/**
 * Contains ClaML domain knowledge
 */
public class ClaMLConstants {
    // ClaML concepts
    public static final String EXCLUDES1 = "Excludes1";
    public static final String EXCLUDES2 = "Excludes2";
    public static final String INCLUDES = "Includes";
    public static final String SYNONYMS = "Inclusion/Synonym Terms"; 
    public final static String SUPER_NAME = "(CHAPTER";
    public final static String CHILD_REL = "CHD";
    // ClaML Header information
    public static final String XML_VERSION = "1.0";
    public static final String XML_ENCODING = "UTF-16";
    public static final String CLAML_VERSION = "2.0.0";
    public static final String ID_AUTHORITY = "WHO";
    public static final String TITLE_VERSION = "July 2007";   
    public static final String TITLE_NAME = "ICD-10-CM";
    public static final String ID_UID = "id-to-be-added-later";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String META_TOP_LEVEL_SORT = "TopLevelSort";
    // ClaML file information
    public static final String LANG = "en";
    public static final String DEFAULT = "default";
    public static final String IN_BRACKETS = "in brackets";
    public static final String SEP = "_";
    public static final String ROOT = "@";
    public static final String HIERARCHY_ID = "is_a";

    public enum ClassKinds { 
        CHAPTER("chapter"), 
        BLOCK("block"),
        CATEGORY("category");

        private final String description;  

        ClassKinds(String desc){  
            this.description = desc;  
        } 

        public String description(){  
            return this.description;  
        }   
    }; 
    
    public enum RubricKinds { 
        PREFERRED("preferred"), 
        INCLUSION("inclusion"),
        EXCLUSION1("exclusion1"),
        EXCLUSION2("exclusion2"),
        NOTE("note");
        
        private final String description;  
           
        RubricKinds(String desc){  
            this.description = desc;  
        } 
        
        public String description(){  
            return this.description;  
        }   
    }; 

}
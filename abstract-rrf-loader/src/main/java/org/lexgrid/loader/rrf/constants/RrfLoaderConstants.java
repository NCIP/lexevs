
package org.lexgrid.loader.rrf.constants;

import java.util.Arrays;
import java.util.List;

/**
 * The Class RrfLoaderConstants.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RrfLoaderConstants {

	/**
	 * The Enum RrfRelationType.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static enum RrfRelationType {/** The REL. */
REL, /** The RELA. */
 RELA};
	
	/** The RELATIO n_ expande d_ form. */
	public static String RELATION_EXPANDED_FORM = "expanded_form";
	
	/** The RELATIO n_ inverse. */
	public static String RELATION_INVERSE = "_inverse";
	
	/** The MET a_ asso c_ entit y_ desc. */
	public static String META_ASSOC_ENTITY_DESC = "MetaThesaurus Associations";
	
	/** The N o_ code. */
	public static String NO_CODE = "NOCODE";
	
	/** The RR f_ partitio n_ number. */
	public static String RRF_PARTITION_NUMBER = "rrfPartitionNumber";
	
	/** The SEMANTI c_ type s_ property. */
	public static String SEMANTIC_TYPES_PROPERTY = "Semantic_Type";
	
	public static String TUI_PROPERTY = "TUI";
	
	/** The UML s_ cu i_ property. */
	public static String UMLS_CUI_PROPERTY = "UMLS_CUI";
	
	public static String LUI_QUALIFIER = "LUI";
	
	public static String SUI_QUALIFIER = "SUI";
	
	public static String SAUI_QUALIFIER = "SAUI";
	
	public static String SATUI_QUALIFIER = "SATUI";
	
	public static String STYPE1_QUALIFIER = "STYPE1";
	
	public static String STYPE2_QUALIFIER = "STYPE2";
	
	public static String SRUI_QUALIFIER = "SRUI";
	
	public static String RG_QUALIFIER = "RG";
	
	public static String RUI_QUALIFIER = "RUI";
	
	public static String SCUI_QUALIFIER = "SCUI";
	
	public static String SDUI_QUALIFIER = "SDUI";
	
	public static String CVF_QUALIFIER = "CVF";
	
	public static String SUPPRESS_QUALIFIER = "SUPPRESS";
	
	/** The AU i_ qualifier. */
	public static String AUI_QUALIFIER = "AUI";
	
	public static String ATUI_QUALIFIER = "ATUI";
	
	public static String HCD_QUALIFIER = "HCD";
	
	public static String PTR_QUALIFIER = "PTR";
	
	public static String RELA_QUALIFIER = "rela";
	
	public static String ASSOC_DIR_ASSERTED_TRUE = "Y";
	
	public static String ASSOC_DIR_ASSERTED_FALSE = "N";
	
	public static String MRRANK_PROPERTY_QUALIFIER_NAME = "mrrank";
	
	public static String SIB = "SIB";
	
	/** The TRANSITIV e_ associations. */
	public static List<String> TRANSITIVE_ASSOCIATIONS = Arrays.asList(new String[]{"PAR", "CHD", "isa", "inverse_isa"});
	
	/** The RE l_ hie r_ relations. */
	public static List<String> REL_HIER_RELATIONS = Arrays.asList(new String[]{"CHD", "PAR"});
	
	/** The REL a_ hie r_ relations. */
	public static List<String> RELA_HIER_RELATIONS = Arrays.asList(new String[]{"inverse_isa", 
			"has_part", "has_branch", "contains", "has_segment", "continuous_with", "has_tributary",
			"isa", "part_of", "contained_in", "branch_of", "segment_of", "tributary_of, continuous_with"});


	/** The UML s_ relation s_ name. */
	public static String UMLS_RELATIONS_NAME = "UMLS_Relations";
	
	/** The UML s_ relation s_ entit y_ description. */
	public static String UMLS_RELATIONS_ENTITY_DESCRIPTION = "UMLS-defined relationships (e.g. RB, RN, CHD, PAR)";
}
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
package edu.mayo.informatics.lexgrid.convert.directConversions.owlapi;

import java.util.Arrays;
import java.util.List;

import org.semanticweb.owlapi.vocab.Namespaces;

/**
 * Constants used by the OwlApi loader Implementation.
 * 
 * Made some changes by deprecating some of the constants, as well as, adding
 * more "OWL specific" ones.
 * 
 * @author Pradip Kanjamala (kanjamala.pradip@mayo.edu)
 */
public class OwlApi2LGConstants {
    // Processing flags and options
    public static final boolean CREATE_CONCEPT_FOR_OBJECT_PROP = false;
    public static final String DATATYPE_PROP_SWITCH = "both";
    public static final boolean IS_DBXREF_SOURCE = false;
    public static final boolean IS_DBXREF_REPFORM = false;
    public static final int MEMOPT_ALL_IN_MEMORY = 0;
    public static final int MEMOPT_LEXGRID_DIRECT_DB = 1;
    public static final boolean PROCESS_COMPLEX_PROP = false;
    public static final boolean STRICT_OWL_IMPLEMENTATION = true;

    /* Namespaces */
    public static final String RDF_NAMESPACE = Namespaces.RDF.toString();
    public static final String OWL_NAMESPACE = Namespaces.OWL.toString();
    public static final String RDFS_NAMESPACE= Namespaces.RDFS.toString();

    /* Languages */
    public static final String LANG_URI = "urn:oid:2.16.840.1.113883.6.84";
    public static final String LANG_ENGLISH = "en";

    /* Property names */
    public static final String PROPNAME_ANNOTATIONPROPERTY = "isAnnotationProperty";
    public static final String PROPNAME_ENUMERATION = "isEnumeration";
    public static final String PROPNAME_INTERSECTION = "isIntersection";
    public static final String PROPNAME_PRIMITIVE = "primitive";
    public static final String PROPNAME_RDF_ID = "rdf:id";
    public static final String PROPNAME_RDFS_LABEL = "rdfs:label";
    public static final String PROPNAME_TYPE = "type";
    public static final String PROPNAME_UNION = "isUnion";
    
    /* Data types */
    public static final String DATATYPE_BOOLEAN = "boolean";
    public static final String DATATYPE_BOOLEAN_URI = "http://www.w3.org/2001/XMLSchema#boolean";
    public static final String DATATYPE_STRING = "string";
    public static final String DATATYPE_STRING_URI = "http://www.w3.org/2001/XMLSchema#string";

    /* Root node definition */
    public static final String ROOT_CODE = "@@";
    public static final String ROOT_NAME = "Bottom Thing";
    public static final String ROOT_DESCRIPTION = "Root node for subclass relations.";
    
    /* Special association containers */
    public static final String DC_ASSOCIATIONS = "associations";
    public static final String DC_ROLES = "roles";

    /* Special associations */
    public static final String ASSOC_ALLDIFFERENT = "AllDifferent";
    public static final String ASSOC_ALLDIFFERENT_URI = OWL_NAMESPACE + "AllDifferent";
    public static final String ASSOC_COMPLEMENTOF = "complementOf";
    public static final String ASSOC_COMPLEMENTOF_URI = OWL_NAMESPACE + "complementOf";
    public static final String ASSOC_DATATYPE = "datatype";
    public static final String ASSOC_DATATYPE_URI = RDFS_NAMESPACE + "Datatype";
    public static final String ASSOC_DATATYPEVALUE = "datatypeValue";
    public static final String ASSOC_DATATYPEVALUE_URI = RDFS_NAMESPACE + "DatatypeValue";
    public static final String ASSOC_DIFFERENTFROM = "differentFrom";
    public static final String ASSOC_DIFFERENTFROM_URI = OWL_NAMESPACE + "differentFrom";
    public static final String ASSOC_DISJOINTWITH = "disjointWith";
    public static final String ASSOC_DISJOINTWITH_URI = OWL_NAMESPACE + "disjointWith";
    public static final String ASSOC_DISJOINTUNION = "disjointUnion";
    public static final String ASSOC_DISJOINTUNION_URI = "disjointUnion";
    public static final String ASSOC_DOMAIN = "domain";
    public static final String ASSOC_DOMAIN_FWD = "domain";
    public static final String ASSOC_DOMAIN_URI = RDFS_NAMESPACE + "domain";
    public static final String ASSOC_EQUIVALENTCLASS = "equivalentClass";
    public static final String ASSOC_EQUIVALENTCLASS_URI = OWL_NAMESPACE + "equivalentClass";
    public static final String ASSOC_EQUIVALENTPROPERTY = "equivalentProperty";
    public static final String ASSOC_EQUIVALENTPROPERTY_URI = OWL_NAMESPACE + "equivalentProperty";
    public static final String ASSOC_TYPE = "type";
    public static final String ASSOC_TYPE_URI = RDF_NAMESPACE + "type";
   
    // This property is manufactured for LexGrid import.
    public static final String ASSOC_INVERSEOF = "inverseOf";
    public static final String ASSOC_INVERSEOF_URI = OWL_NAMESPACE + "inverseOf";
    public static final String ASSOC_RANGE = "range";
    public static final String ASSOC_RANGE_FWD = "range";
    public static final String ASSOC_RANGE_URI = RDFS_NAMESPACE + "range";
    public static final String ASSOC_SAMEAS = "sameAs";
    public static final String ASSOC_SAMEAS_URI = OWL_NAMESPACE + "sameAs";
    public static final String ASSOC_SUBCLASSOF = "subClassOf";
    public static final String ASSOC_SUBCLASSOF_URI = RDFS_NAMESPACE + "subClassOf";
    public static final String ASSOC_SUBPROPERTYOF = "subPropertyOf";
    public static final String ASSOC_SUBPROPERTYOF_URI = RDFS_NAMESPACE + "subPropertyOf";

    /* Hierarchies supported within the code system */
    public static final String URI_MISSING = null;
    public static final String SUPP_HIERARCHY = "Hierarchy";
    public static final String SUPP_HIERARCHY_ISA = "is_a";
    public static final String SUPP_HIERARCHY_ISA_URI = URI_MISSING;
    public static final String[] SUPP_HIERARCHY_ISA_ASSOCIATION_LIST =
        new String[] { ASSOC_SUBCLASSOF };
    
    /* Individual designation */
    public static final String INSTANCE_SUFFIX = "_OWL_IND";

    /*
     * Indicate property names to be treated as specific classes of
     * properties within the LexGrid model (can be altered via preferences).
     */
    public static final List<String> PRIORITIZED_COMMENT_NAMES = Arrays.asList(new String[] { "DesignNote", "Editor_Note",
            "Citation", "VA_Workflow_Comment", "rdfs:comment" });
    public static final List<String> PRIORITIZED_DEFINITION_NAMES = Arrays.asList(new String[] { "DEFINITION", "dDEFINITION",
            "LONG_DEFINITION", "ALT_DEFINITION", "ALT_LONG_DEFINITION", "MeSH_Definition","definition", "rdf:Description" });
    public static final List<String> PRIORITIZED_PRESENTATION_NAMES = Arrays.asList(new String[] { "NCI_Preferred_Term",
            "Preferred_Name", "Display_Name", "Search_Name", "FULL_SYN", "Synonym", "VA_Print_Name",
            "VA_National_Formulary_Name", "VA_Abbreviation", "VA_Dose_Form_Print_Name", "VA_Trade_Name", "MeSH_Name",
            "NDFRT_Name", "RxNorm_Name", "rdfs:label" });
    public static final String[] DATEFORMATS = {"MMMM dd, yyyy", "yyyy-dd-mm"}; 

    // Regular expressions to enable generic processing of the OWL source.
    // Defaults defined here, changeable via preferences.
    public static final String MATCH_CONCEPT_CODE = "";
    public static final String MATCH_CONCEPT_STATUS = "(Concept_Status)";
    public static final String MATCH_INACTIVE_STATUS = "(Retired_Concept)";
    public static final String MATCH_NOOP_NAMESPACES = "(:|@_:|protege:|xsp:).*";
    public static final String MATCH_ROOT_NAME = null;

    public static final String MATCH_XMLREPFORM_NAMES = "(term-group)";
    public static final String MATCH_XMLREPFORM_VALUES = "([A-Z0-9]{1,3})";
    public static final String MATCH_XMLSOURCE_NAMES = "(term-source|def-source|source)";
    public static final String MATCH_XMLSOURCE_VALUES = "([A-Z]*[^ISBN]:[a-z0-9_\\-\\.]*)";
    public static final String MATCH_XMLTEXT_NAMES = "(term-name|def-definition|go-term)";

    //Complex Properties Tags and upper level elements
    //Tags that may prompt further processing as qualifiers, source or definition.
    public static final String COMP_PROP_TAG_TERM_NAME = "term-name";
    public static final String COMP_PROP_TAG_TERM_GROUP = "term-group";
    public static final String COMP_PROP_TAG_TERM_SOURCE = "term-source";
    public static final String COMP_PROP_TAG_DEF_DEFINITION = "def-definition";
    public static final String COMP_PROP_TAG_DEF_SOURCE ="def-source";
    public static final String COMP_PROP_TAG_DEF_REV_NAME ="Definition_Reviewer_Name";
    public static final String COMP_PROP_TAG_DEF_REV_DATE = "Definition_Review_Date";
    public static final String COMP_PROP_TAG_GO_TERM = "go-term";
    public static final String COMP_PROP_TAG_GO_ID = "go-id";
    public static final String COMP_PROP_TAG_GO_SOURCE = "go-source";
    public static final String COMP_PROP_TAG_GO_EVI = "go-evi";
    public static final String COMP_PROP_TAG_GO_SOURCE_DATE = "source-date";
   // public static final String COMP_PROP_TAG_GO_SOURCE_CODE = "source-code";
    
    //Complex properties top level tags -- ignore
    public static final String COMP_PROP_TAG_COMPLEXDEFINITION = "ComplexDefinition";
    public static final String COMP_PROP_TAG_COMPLEXANNOTATION = "ComplexGOAnnotation";
    public static final String COMP_PROP_TAG_COMPLEXTERM = "ComplexTerm";
    
    //Complex properties namespace
    public static final String COMP_PROP_NAMESPACE = "ncicp";
    
    //Entity type for datatype implementation
    public static final String DATA_TYPE_ENTITY_TYPE = "datatype";
    public static final boolean MANAGE_TRANSITIVE_AND_INVERSE = false;
}
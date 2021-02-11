
package org.LexGrid.LexBIG.Extensions.Load;

public enum OntologyFormat {
    OBO,
    OWL,
    OWLRDF,
    HL7,
    UMLS,
    TEXT,
    LEXGRID_XML,
    LEXGRID_MAPPING,
    NICHISTORY,
    UMLSHISTORY,
    MRMAP,
    RESOLVEDVALUESET,
    SOURCEASSERTEDRESOLVEDVS,
    MIFVOCABULARY,
    MEDDRA;
    
    public static String getMetaName() {
    	return "ontologyFormat";
    }
}
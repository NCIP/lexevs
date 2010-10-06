package org.LexGrid.LexBIG.Extensions.Load;

public enum OntologyFormat {
    OBO,
    OWLRDF,
    HL7,
    UMLS,
    TEXT,
    RADLEX,
    NICHISTORY,
    UMLSHISTORY,
    MRMAP;
    
    public static String getMetaName() {
    	return "ontologyFormat";
    }
}

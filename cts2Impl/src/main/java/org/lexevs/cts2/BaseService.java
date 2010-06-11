package org.lexevs.cts2;


public abstract interface BaseService {

	public static enum LoadFormats {
		LexGridXML, OBO, OWL, HL7, CLAML, RadLexFrames, UMLS
	};
	
	public static enum ExportFormats {
		LexGridXML, OBO, OWL
	};
	
	public static enum SortableProperties {
        matchToQuery, code, codeSystem, entityDescription, conceptStatus, isActive
    };

    public static enum MatchAlgorithms {
        exactMatch, startsWith, LuceneQuery, NormalizedLuceneQuery, DoubleMetaphoneLuceneQuery, contains, RegExp
    };

    public static enum KnownTags {
        PRODUCTION
    };
    
    public ServiceInfo getServiceInfo();    
}


package org.LexGrid.LexBIG.Utility;

/**
 * Constants for use in the LexBIG API.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LBConstants {
    public static enum SortableProperties {
        matchToQuery, code, codeSystem, entityDescription, conceptStatus, isActive
    };

    public static enum MatchAlgorithms {
        exactMatch, startsWith, LuceneQuery, NormalizedLuceneQuery, DoubleMetaphoneLuceneQuery, contains, RegExp
    };

    public static enum KnownTags {
        PRODUCTION
    };

}
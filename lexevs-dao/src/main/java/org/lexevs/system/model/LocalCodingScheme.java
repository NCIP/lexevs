
package org.lexevs.system.model;

import org.lexevs.system.ResourceManager;

/**
 * Holder for coding scheme information that I need to store.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LocalCodingScheme {
    
    /** The coding scheme name. */
    public String codingSchemeName;
    
    /** The version. */
    public String version;

    /**
     * Gets the key.
     * 
     * @return the key
     */
    public String getKey() {
        if (codingSchemeName.indexOf(ResourceManager.codingSchemeVersionSeparator_) == -1) {
            // if it is just a codingSchemeName - no version information
            // embedded, return a
            // combination
            return codingSchemeName + ResourceManager.codingSchemeVersionSeparator_ + version;
        } else {
            // if it already has the version stuff, then it should be good as
            // is.
            return codingSchemeName;
        }
    }

    /**
     * Gets the coding scheme name without version.
     * 
     * @return the coding scheme name without version
     */
    public String getCodingSchemeNameWithoutVersion() {
        if (codingSchemeName.indexOf(ResourceManager.codingSchemeVersionSeparator_) == -1) {
            return codingSchemeName;
        } else {
            return codingSchemeName.substring(0, codingSchemeName
                    .indexOf(ResourceManager.codingSchemeVersionSeparator_));
        }
    }
    
    /**
     * Gets the local coding scheme.
     * 
     * @param codingSchemeName the coding scheme name
     * @param version the version
     * 
     * @return the local coding scheme
     */
    public static LocalCodingScheme getLocalCodingScheme(String codingSchemeName, String version) {
    	LocalCodingScheme lcs = new LocalCodingScheme();
    	lcs.codingSchemeName = codingSchemeName;
    	lcs.version = version;
    	return lcs;
    }
}
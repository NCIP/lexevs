
package edu.mayo.informatics.resourcereader.obo;

import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;

import edu.mayo.informatics.resourcereader.core.IF.ResourceEntity;

/**
 * This class stores OBO Abbreviation information
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOAbbreviation extends OBO implements ResourceEntity {
    public String abbreviation = null;
    public String genericURL = null;

    public OBOAbbreviation() {
    };

    public OBOAbbreviation(CachingMessageDirectorIF rLogger) {
        super(rLogger);
    }

    public String toString() {
        return "Abbreviation:[" + abbreviation + ", " + genericURL + "] \n";
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getGenericURL() {
        return genericURL;
    }

    public void setGenericURL(String genericURL) {
        this.genericURL = genericURL;
    }

}
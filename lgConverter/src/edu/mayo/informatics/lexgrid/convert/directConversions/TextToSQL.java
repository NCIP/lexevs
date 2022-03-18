
package edu.mayo.informatics.lexgrid.convert.directConversions;

import java.net.URI;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.CodingScheme;
import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.Concept;
import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.TextUtility;

/**
 * Converstion tool for loading a delimited text format into SQL.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @version subversion $Revision: 8756 $ checked in on $Date: 2007-08-30
 *          17:13:22 +0000 (Thu, 30 Aug 2007) $
 */
public class TextToSQL {
    private String token_ = "\t";
    private LgMessageDirectorIF messages_;

    private Concept specialConcept = new Concept("@", "Top Thing",
            "Points to all concepts that aren't children of any other concepts", -1);
    private String codingSchemeName_;
    private String representsVersion_;
    
    private CodingScheme codingScheme;

    /**
     * @return the codingSchemeName
     */
    public String getCodingSchemeName() {
        return this.codingSchemeName_;
    }

    /**
     * @return the representsVersion
     */
    public String getVersion() {
        return this.representsVersion_;
    }

    /**
     * Text to SQL Converter. Format of the text files is as follows: Format a:
     * (ignore the *'s) (this does not display properly in HTML - please see the
     * readme file)
     * 
     * <pre>
     * 
     *    &lt;codingSchemeName&gt;\t&lt;codingSchemeId&gt;\t&lt;defaultLanguage&gt;\t&lt;formalName&gt;[\t&lt;version&gt;][\t&lt;source&gt;][\t&lt;description&gt;][\t&lt;copyright&gt;]
     *    &lt;name1&gt;[\t &lt;description&gt;] 
     *    \t &lt;name2&gt;[\t &lt;description&gt;] 
     *    \t\t &lt;name3&gt;[\t &lt;description&gt;]
     *    \t\t &lt;name4&gt;[\t &lt;description&gt;]
     * </pre>
     * 
     * Where the leading tabs represent hierarchical hasSubtype relationship
     * nesting (name1 hasSubtype name2 and name2 hasSubtype name3,) The line
     * that starts with keyword Relation is used to setup the association
     * information.
     * 
     * Lines starting with "#" are view comments - they are completely ignored.
     * 
     * Rules - if <name>doesn't already exist in the database, assign it a
     * unique numeric concept code. Name becomes the entity description and
     * preferred presentation. If description is supplied, it becomes the
     * definition. - if <name>already exists in the database, use the
     * pre-assigned code. If <description>is supplied (a) if one doesn't exist,
     * already, use the supplied one (b) if one exists already and it doesn't
     * match - issue a warning.
     * 
     * Format b:
     * 
     * <pre>
     * &lt;code&gt;
     *   \t&lt;name&gt;[\t&lt;description&gt;]
     * </pre>
     * 
     * Same as (a) except that the concept codes are part of the input. If the
     * same code occurs twice, the names must match. Description rules same as
     * above
     * 
     * @param fileLocation
     *            location of the tab delimited file
     * @param token
     *            parsing token, if null default is "/t"
     * @param sqlLiteServer
     *            location of the SQLLite server
     * @param sqlLiteDriver
     *            driver class
     * @param sqlLiteUsername
     *            username for server authentification
     * @param sqlLitePassword
     *            password for server authenification
     * @param loaderPrefs
     *            Loader Preferences
     * @param messageDirector
     *            log message output
     * @param forceFormatB
     *            Force reading of a format A file as Format B
     * @throws Exception
     */
    public org.LexGrid.codingSchemes.CodingScheme load(URI fileLocation, String token, LoaderPreferences loaderPrefs,
            LgMessageDirectorIF messageDirector, boolean forceFormatB) throws CodingSchemeAlreadyLoadedException {
       
        messages_ = messageDirector;
        if (token != null && token.length() > 0) {
            token_ = token;
        }
        
        // this verifies all of the rules except the description rules - and
        // determines A or B.
        try {
            codingScheme = TextUtility.readAndVerifyConcepts(fileLocation, messages_, token_, forceFormatB);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
        
        org.LexGrid.codingSchemes.CodingScheme cs = CodingScheme.toCodingScheme(codingScheme);

        return cs;
    }

    // return the codingscheme
    public CodingScheme getCodingScheme(){
        return codingScheme;
    }

}

package edu.mayo.informatics.lexgrid.convert.formats.inputFormats;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.exceptions.UnexpectedError;
import edu.mayo.informatics.lexgrid.convert.formats.InputFormatInterface;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.baseFormats.URIBase;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * SQL default elements for MSACCESS
 * 
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 * 
 */
public class HL7SQL extends URIBase implements InputFormatInterface {

    public String filePath;
    private URNVersionPair currentCodingScheme;
    public static final String description = "HL7 SQL Database";
    public static final String MSACCESS_SERVER = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
    public static final String MSACCESS_DRIVER = "sun.jdbc.odbc.JdbcOdbcDriver";

    // private static Logger log = Logger.getLogger("convert.hl7sql");

    public HL7SQL(String fileLocation) // throws URISyntaxException

    {
        this.filePath = fileLocation;
        this.currentCodingScheme = null;
    }

    public HL7SQL() {
    }

    public String[] getAvailableTerminologies() throws ConnectionFailure, UnexpectedError {
        // TODO Auto-generated method stub
        return null;
    }

    public String getConnectionSummary() {
        return getConnectionSummary(description);
    }

    public String getDescription() {
        return description;
    }

    // I'm using this to pick up the one that is currently being loaded.
    public URNVersionPair getCurrentCodingScheme() {
        return currentCodingScheme;
    }

    public void setCurrentCodingScheme(URNVersionPair codingScheme) {
        currentCodingScheme = codingScheme;
    }

    public Option[] getOptions() {
        return new Option[] {};
    }

    public String[] getSupportedOutputFormats() {

        return new String[] { LexGridSQLOut.description };
    }

    public String testConnection() throws ConnectionFailure {
        // TODO Auto-generated method stub
        return null;
    }

    public String getPath() {
        return filePath;
    }

    public String getDriver() {
        // TODO Auto-generated method stub
        return MSACCESS_DRIVER;
    }

    public String getServer() {
        // TODO Auto-generated method stub
        return MSACCESS_SERVER;
    }
}